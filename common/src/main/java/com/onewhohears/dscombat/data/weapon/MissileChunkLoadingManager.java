package com.onewhohears.dscombat.data.weapon;

import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.DependencySafety;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.LinkedHashSet;

/**
 * Manages missiles flying through unloaded chunks.
 *
 * Key insight: a missile is "ticking" only when inEntityTickingRange() is true.
 * world.hasChunk() is NOT sufficient — chunks can be loaded for structure generation
 * but not for entity ticking.
 *
 * Fix: when a missile is UNLOADED we force-load a small radius of chunks around it
 * every tick so the missile always has terrain to fly through. Tickets are released
 * when the missile dies or re-enters the player's tick range.
 *
 * State machine:
 *   ACTIVE   – inEntityTickingRange() == true, Minecraft ticks the missile normally
 *   UNLOADED – inEntityTickingRange() == false, we tick it manually via tickOutRange()
 *              and keep chunks loaded with a FORCED ticket
 *   DEAD     – missile exploded or timed out, remove from manager
 */
public class MissileChunkLoadingManager {

    private static final Logger LOGGER = LogUtils.getLogger();

    /** Ticket type used to keep missile chunks loaded. */
    private static final TicketType<ChunkPos> MISSILE_TICKET =
            TicketType.create("dscombat_missile", Comparator.comparingLong(ChunkPos::toLong), 40);

    /**
     * Chunk radius to keep loaded around the missile.
     * 0 = only the missile's own chunk — minimal footprint.
     */
    private static final int CHUNK_LOAD_RADIUS = 0;

    /**
     * Ticket level 33 = border (loaded but not ticking entities or blocks).
     * Just enough for block collision raycasts without forcing terrain generation.
     * We use level 32 (block ticking) so hasChunk() returns true for collision checks.
     */
    private static final int TICKET_LEVEL = 32;

    private static final Map<UUID, MissileState> missileStates = new ConcurrentHashMap<>();
    // Use LinkedHashSet for O(1) add/remove while preserving iteration order
    private static final Set<EntityMissile<?>> activeMissiles = new LinkedHashSet<>();

    private enum State { ACTIVE, UNLOADED, DEAD }

    private static class MissileState {
        final EntityMissile<?> missile;
        State state;
        ChunkPos lastTicketPos = null;
        int stateChangeTick = 0;
        /** Last observed tickCount, to detect when Minecraft stops ticking the entity. */
        int lastObservedTickCount = 0;
        int frozenTicks = 0;

        MissileState(EntityMissile<?> missile, State state) {
            this.missile = missile;
            this.state = state;
        }
    }

    /** How often (in server ticks) to log flight state for each missile. */
    private static final int LOG_INTERVAL_TICKS = 40;
    /** How many ticks in UNLOADED state before logging a warning about potential stuck missile. */
    private static final int STUCK_WARN_TICKS = 200;
    /** Maximum number of missiles allowed to exist simultaneously. */
    private static final int MAX_MISSILES = 100;

    private static int globalTick = 0;

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /** Called from EntityMissile constructor (server side only). */
    public static void registerMissile(EntityMissile<?> missile) {
        if (missile.level().isClientSide()) return;
        UUID uuid = missile.getUUID();
        if (missileStates.containsKey(uuid)) return;
        
        // Enforce missile limit
        if (missileStates.size() >= MAX_MISSILES) {
            LOGGER.warn("[DSCombat] Missile limit reached ({}/{}), killing oldest missile", missileStates.size(), MAX_MISSILES);
            // Find and kill the oldest missile
            MissileState oldest = null;
            int oldestTick = Integer.MAX_VALUE;
            for (MissileState ms : missileStates.values()) {
                if (ms.missile.tickCount < oldestTick) {
                    oldestTick = ms.missile.tickCount;
                    oldest = ms;
                }
            }
            if (oldest != null) {
                oldest.missile.kill();
                oldest.state = State.DEAD;
            }
        }
        
        missileStates.put(uuid, new MissileState(missile, State.ACTIVE));
        activeMissiles.add(missile);
        LOGGER.debug("[DSCombat] Registered missile {} type={}", uuid, missile.getType().getDescriptionId());
    }

    /** Called from EntityMissile.kill(). */
    public static void markMissileDead(EntityMissile<?> missile) {
        MissileState ms = missileStates.get(missile.getUUID());
        if (ms != null) {
            // Chunk tickets disabled — no need to release
            ms.state = State.DEAD;
        }
    }

    /** Called every server tick from CommonEventHandlers. */
    public static void serverTick(MinecraftServer server) {
        long startTime = System.nanoTime();
        ++globalTick;
        boolean doLog = (globalTick % LOG_INTERVAL_TICKS == 0);

        if (doLog && !missileStates.isEmpty()) {
            LOGGER.warn("[DSCombat] MissileChunkLoadingManager tick={} tracking {} missiles", globalTick, missileStates.size());
        }

        Iterator<Map.Entry<UUID, MissileState>> it = missileStates.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, MissileState> entry = it.next();
            MissileState ms = entry.getValue();

            if (ms.state == State.DEAD) {
                it.remove();
                activeMissiles.remove(ms.missile);
                continue;
            }

            try {
                tickMissile(ms, server);
                if (doLog) logFlightState(ms);
            } catch (Exception e) {
                LOGGER.error("Error ticking missile {}", entry.getKey(), e);
                ms.state = State.DEAD;
            }
        }
        
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        if (durationMs > 10 && !missileStates.isEmpty()) {
            LOGGER.warn("[DSCombat] MissileChunkLoadingManager.serverTick took {}ms for {} missiles ({}ms per missile)",
                    durationMs, missileStates.size(), durationMs / missileStates.size());
        }
    }

    /**
     * Logs current flight state of a missile.
     */
    private static void logFlightState(MissileState ms) {
        EntityMissile<?> missile = ms.missile;
        net.minecraft.world.phys.Vec3 pos = missile.position();
        net.minecraft.world.phys.Vec3 vel = missile.getDeltaMovement();
        double speed = vel.length();
        String targetInfo = missile.targetPos != null
                ? String.format("target=(%.1f,%.1f,%.1f)", missile.targetPos.x, missile.targetPos.y, missile.targetPos.z)
                : (missile.target != null ? "target=entity:" + missile.target.getId() : "target=none");
        String type = missile.getType().getDescriptionId();
        int ticksInState = missile.tickCount - ms.stateChangeTick;
        boolean inTickRange = missile.inEntityTickingRange();

        String msg = String.format(
                "[DSCombat][Missile %s] type=%s state=%s tick=%d ticksInState=%d inTickRange=%b removed=%b discardedButTicking=%b pos=(%.1f,%.1f,%.1f) chunk=(%d,%d) speed=%.2f %s",
                missile.getUUID().toString().substring(0, 8),
                type, ms.state, missile.tickCount, ticksInState, inTickRange,
                missile.isRemoved(), missile.isDiscardedButTicking(),
                pos.x, pos.y, pos.z,
                missile.chunkPosition().x, missile.chunkPosition().z,
                speed, targetInfo);

        // Always use logger at debug level
        LOGGER.debug(msg);

        if (ms.state == State.UNLOADED && ticksInState > STUCK_WARN_TICKS) {
            LOGGER.warn("[DSCombat][STUCK WARNING] Missile {} has been UNLOADED for {} ticks without resolving!",
                    missile.getUUID(), ticksInState);
        }
    }

    /** All missiles tracked by this manager (for radar). Returns an unmodifiable view — do not cache. */
    public static Collection<EntityMissile<?>> getActiveMissiles() {
        return Collections.unmodifiableCollection(activeMissiles);
    }

    /** Called on server stop. */
    public static void clearAll() {
        missileStates.clear();
        activeMissiles.clear();
    }

    // -------------------------------------------------------------------------
    // Chunk ticket helpers
    // -------------------------------------------------------------------------

    /**
     * Issues FORCED tickets for a CHUNK_LOAD_RADIUS square around the missile's
     * current chunk. Only re-issues if the missile moved to a different chunk.
     */
    private static void updateChunkTickets(MissileState ms) {
        EntityMissile<?> missile = ms.missile;
        if (!(missile.getWorld() instanceof ServerLevel sl)) return;

        ChunkPos cur = missile.chunkPosition();
        if (cur.equals(ms.lastTicketPos)) return; // same chunk, nothing to do

        // Release old tickets first
        releaseChunkTickets(ms);

        // Issue new tickets at TICKET_LEVEL for full entity ticking
        for (int dx = -CHUNK_LOAD_RADIUS; dx <= CHUNK_LOAD_RADIUS; dx++) {
            for (int dz = -CHUNK_LOAD_RADIUS; dz <= CHUNK_LOAD_RADIUS; dz++) {
                ChunkPos cp = new ChunkPos(cur.x + dx, cur.z + dz);
                sl.getChunkSource().addRegionTicket(MISSILE_TICKET, cp, TICKET_LEVEL, cp);
            }
        }
        ms.lastTicketPos = cur;
    }

    /** Releases all FORCED tickets held for this missile. */
    private static void releaseChunkTickets(MissileState ms) {
        if (ms.lastTicketPos == null) return;
        EntityMissile<?> missile = ms.missile;
        if (!(missile.getWorld() instanceof ServerLevel sl)) return;

        ChunkPos old = ms.lastTicketPos;
        for (int dx = -CHUNK_LOAD_RADIUS; dx <= CHUNK_LOAD_RADIUS; dx++) {
            for (int dz = -CHUNK_LOAD_RADIUS; dz <= CHUNK_LOAD_RADIUS; dz++) {
                ChunkPos cp = new ChunkPos(old.x + dx, old.z + dz);
                sl.getChunkSource().removeRegionTicket(MISSILE_TICKET, cp, TICKET_LEVEL, cp);
            }
        }
        ms.lastTicketPos = null;
    }

    public static String getDebugInfo() {
        int active = 0, unloaded = 0, dead = 0;
        for (MissileState ms : missileStates.values()) {
            switch (ms.state) {
                case ACTIVE   -> active++;
                case UNLOADED -> unloaded++;
                case DEAD     -> dead++;
            }
        }
        return String.format("Missiles: %d (Active: %d, Unloaded: %d, Dead: %d)",
                missileStates.size(), active, unloaded, dead);
    }

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    private static void tickMissile(MissileState ms, MinecraftServer server) {
        EntityMissile<?> missile = ms.missile;

        // If truly dead (killed, not just discarded-but-ticking) → mark DEAD
        if (missile.isRemoved() && !missile.isDiscardedButTicking()) {
            ms.state = State.DEAD;
            return;
        }

        // Validate world
        if (missile.getWorld() == null || missile.getWorld().isClientSide()) {
            ms.state = State.DEAD;
            return;
        }

        boolean inTickRange = missile.inEntityTickingRange();

        switch (ms.state) {
            case ACTIVE -> {
                // Detect if Minecraft stopped ticking this entity despite inTickRange=true
                if (missile.tickCount == ms.lastObservedTickCount) {
                    ms.frozenTicks++;
                    // Kill frozen missiles faster (5 ticks = 0.25 seconds instead of 20 = 1 second)
                    if (ms.frozenTicks >= 5) {
                        LOGGER.warn("[DSCombat] Missile {} FROZEN in ACTIVE for {} server ticks at pos={} chunk={} removed={} -> KILLING",
                                missile.getUUID(), ms.frozenTicks, missile.position(), missile.chunkPosition(), missile.isRemoved());
                        // Don't transition to UNLOADED — just kill it
                        missile.kill();
                        ms.state = State.DEAD;
                        ms.frozenTicks = 0;
                    }
                } else {
                    ms.frozenTicks = 0;
                    ms.lastObservedTickCount = missile.tickCount;
                }
                if (!inTickRange && ms.state == State.ACTIVE) {
                    LOGGER.debug("[DSCombat] Missile {} leaving tick range -> UNLOADED pos={}", missile.getUUID(), missile.position());
                    if (!missile.isRemoved()) missile.discardButTick();
                    ms.state = State.UNLOADED;
                    ms.stateChangeTick = missile.tickCount;
                    ms.frozenTicks = 0;
                    // Chunk tickets disabled
                }
            }

            case UNLOADED -> {
                // Chunk tickets disabled — missiles fly through unloaded chunks without forcing generation
                if (inTickRange) {
                    LOGGER.debug("[DSCombat] Missile {} entering tick range -> ACTIVE (reviving)", missile.getUUID());
                    reviveMissile(ms, server);
                    ms.stateChangeTick = missile.tickCount;
                } else {
                    manualTick(missile, server);
                }
            }
        }
    }

    private static void reviveMissile(MissileState ms, MinecraftServer server) {
        EntityMissile<?> missile = ms.missile;
        try {
            // Check if already in world (e.g. revive called twice)
            Entity existing = missile.getWorld().getEntity(missile.getId());
            if (existing != null && existing.getUUID().equals(missile.getUUID())) {
                // Already there — just mark active
                missile.invokeRevive();
                ms.state = State.ACTIVE;
                return;
            }

            missile.invokeRevive();
            boolean added = missile.getWorld().addFreshEntity(missile);
            if (added) {
                ms.state = State.ACTIVE;
                LOGGER.debug("Missile {} revived successfully", missile.getUUID());
            } else {
                // addFreshEntity failed (UUID conflict etc.) — keep ticking manually
                LOGGER.warn("Missile {} addFreshEntity returned false, continuing manual tick", missile.getUUID());
            }
        } catch (Exception e) {
            LOGGER.error("Failed to revive missile {}", missile.getUUID(), e);
            ms.state = State.DEAD;
        }
    }

    private static void manualTick(EntityMissile<?> missile, MinecraftServer server) {
        missile.tickOutRange();
        // tickOutRange already syncs position every 5 ticks — no need to do it again here
    }
}
