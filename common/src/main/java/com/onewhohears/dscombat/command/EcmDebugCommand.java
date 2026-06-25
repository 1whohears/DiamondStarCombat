package com.onewhohears.dscombat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Debug command that toggles a persistent ECM jammer radius visualization.
 * /ecm_debug — toggle on/off while sitting in a vehicle with a jammer.
 * The sphere stays at the position where it was activated until toggled off.
 */
public class EcmDebugCommand {

    private static final int RINGS = 16;
    private static final int POINTS_PER_RING = 48;
    private static final int REDRAW_INTERVAL = 10;

    private static final DustParticleOptions JAMMER_DUST =
            new DustParticleOptions(new Vector3f(0.0f, 1.0f, 0.8f), 3.0f);

    /** UUID -> frozen sphere data (position + radius) */
    private static final Map<UUID, SphereData> activeSpheres = new HashMap<>();

    public EcmDebugCommand(CommandDispatcher<CommandSourceStack> d) {
        d.register(Commands.literal("ecm_debug")
            .requires(stack -> stack.hasPermission(2))
            .executes(context -> {
                CommandSourceStack source = context.getSource();
                ServerPlayer player = source.getPlayer();
                if (player == null) {
                    source.sendFailure(Component.literal("Must be used by a player!"));
                    return 0;
                }
                UUID uuid = player.getUUID();
                // toggle off
                if (activeSpheres.containsKey(uuid)) {
                    activeSpheres.remove(uuid);
                    source.sendSuccess(() -> Component.literal("ECM debug OFF"), false);
                    return 1;
                }
                // toggle on — must be in a vehicle with jammer
                Entity root = player.getRootVehicle();
                if (!(root instanceof EntityVehicle vehicle)) {
                    source.sendFailure(Component.literal("You must be sitting in a DSC vehicle!"));
                    return 0;
                }
                float jamStrength = vehicle.partsManager.getActiveJammerStrength();
                if (jamStrength <= 0f) {
                    source.sendFailure(Component.literal("No active ECM jammer on this vehicle!"));
                    return 0;
                }
                float jamRadius = vehicle.partsManager.getActiveJammerRadius();
                // freeze position at activation moment
                activeSpheres.put(uuid, new SphereData(vehicle.position(), jamRadius));
                source.sendSuccess(() -> Component.literal(
                    "ECM debug ON — radius=" + jamRadius + " blocks, strength=" + (int)(jamStrength * 100) + "%"
                ), false);
                return 1;
            })
        );
    }

    public static void serverTick(MinecraftServer server, int tickCount) {
        if (tickCount % REDRAW_INTERVAL != 0) return;
        if (activeSpheres.isEmpty()) return;
        activeSpheres.forEach((uuid, sphere) -> {
            ServerPlayer player = server.getPlayerList().getPlayer(uuid);
            if (player == null) return;
            ServerLevel level = player.serverLevel();
            spawnJammerSphere(level, sphere.center, sphere.radius);
        });
    }

    private static void spawnJammerSphere(ServerLevel level, Vec3 center, float radius) {
        for (int ring = 0; ring < RINGS; ring++) {
            double lat = Math.PI * (-0.5 + (double) ring / (RINGS - 1));
            double ringRadius = Math.cos(lat) * radius;
            double y = center.y + Math.sin(lat) * radius;
            for (int p = 0; p < POINTS_PER_RING; p++) {
                double lon = 2 * Math.PI * p / POINTS_PER_RING;
                double x = center.x + Math.cos(lon) * ringRadius;
                double z = center.z + Math.sin(lon) * ringRadius;
                for (ServerPlayer player : level.players()) {
                    level.sendParticles(player, JAMMER_DUST, true, x, y, z, 1, 0, 0, 0, 0);
                }
            }
        }
    }

    public static void onServerStop() {
        activeSpheres.clear();
    }

    private record SphereData(Vec3 center, float radius) {}
}
