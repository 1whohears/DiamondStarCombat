package com.onewhohears.dscombat.data.radar;

import com.onewhohears.dscombat.DependencySafety;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.VehicleSyncAction;
import com.onewhohears.dscombat.common.network.toclient.ToClientRWRWarning;
import com.onewhohears.dscombat.common.network.toclient.ToClientRadarPings;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.onewholibs.common.core.SimulatedEntityManager;
import com.onewhohears.onewholibs.entity.SimulatedEntity;
import com.onewhohears.onewholibs.util.UtilEntity;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * manages the radar/targeting/rwr system for {@link EntityVehicle}.
 * individual radars are abstracted into {@link RadarStats}.
 * individual radars update the radar system's link of {@link RadarTarget} on the server side.
 * the updated link of pings are then sent to the client. 
 * the client then tells the server which ping is selected.
 * then the {@link com.onewhohears.dscombat.data.weapon.WeaponSystem} gets the target entity from here.
 * @author 1whohears
 */
public class RadarSystem {
	
	private final EntityVehicle parent;
	private boolean readData = false;
	
	private final List<RadarInstance<?>> radars = new ArrayList<>();
	private final List<EntityMissile<?>> rockets = new ArrayList<>();
	
	private final IntObjectMap<RadarTarget> targets = new IntObjectHashMap<>();
    private final Set<Integer> forRemoval = new HashSet<>();
	private int selectedTargetId = -1;
	private final IntObjectMap<RadarTarget> clientTargets = new IntObjectHashMap<>();
	private int clientSelectedId = -1, clientSelectedTime = -21;
	public int clientPingRefreshTime = 0;
	public int clientRwrRefreshTime = 0;
	
	private final List<RadarTarget> dataLinkBuffer = new ArrayList<>();
	
	private final Map<Integer, RWRWarning> rwrWarnings = new HashMap<>();
	private boolean rwrMissile, rwrRadar;
	
	public boolean dataLink = false;
	
	public RadarSystem(EntityVehicle parent) {
		this.parent = parent;
	}
	
	public List<RadarInstance<?>> getRadars() {
		return radars;
	}
	
	public boolean hasDataLink() {
		return dataLink || parent.getWorld().getGameRules().getBoolean(DSCGameRules.DATA_LINK_ALWAYS_ON);
	}
	
	public void tick() {
		if (parent.isClientSide()) clientTick();
		else if (canServerTick()) serverTick();
	}
	
	public boolean canServerTick() {
		return parent.isOperational() && (parent.isStationaryRadar() || parent.isPlayerRiding()
				|| (parent.getWorld().getGameRules().getBoolean(DSCGameRules.MOBS_TICK_RADAR) && parent.isBotUsingRadar()));
	}
	
	public void tickUpdateTargets() {
		// PLANE RADARS
		for (RadarInstance<?> r : radars) r.tickUpdateTargets(parent, targets);
		// DATA LINK
		if (parent.tickCount % 20 == 0) updateDataLink();
        // REMOVE OLD
        removeOldTargets();
		// PICK PREVIOUS TARGET
        Entity target = getSelectedTargetEntity();
        if (target == null) {
            selectedTargetId = -1;
        } else if (target instanceof EntityVehicle plane) {
            plane.lockedOnto(parent);
        }
		// SEMI ACTIVE TRACK ROCKETS
		updateSemiActiveTrackMissiles();
		// PACKET
		if (parent.tickCount % 10 == 0) {
			if (parent.isStationaryRadar()) parent.toTrackers(new ToClientRadarPings(parent.getId(), targets));
			else parent.toClientPassengers(new ToClientRadarPings(parent.getId(), targets));
		}
	}

    protected void removeOldTargets() {
        long currentTime = parent.getWorld().getGameTime();
        targets.forEach((id, target) -> {
            long timeDiff = currentTime - target.getUpdateTime();
            if (timeDiff > target.getExpireTime()) {
                forRemoval.add(id);
            }
        });
        forRemoval.forEach(targets::remove);
        forRemoval.clear();
    }

    public void addUpdateTarget(RadarTarget newTarget) {
        forRemoval.remove(newTarget.entityId);
        RadarTarget old = targets.get(newTarget.entityId);
        if (old == null) {
            targets.put(newTarget.entityId, newTarget);
        } else {
            old.updateFromRadar(newTarget);
        }
    }

    public void removeTarget(int entityId) {
        RadarTarget target = targets.get(entityId);
        if (target == null) return;
        long currentTime = parent.getWorld().getGameTime();
        long timeDiff = currentTime - target.getUpdateTime();
        if (timeDiff > target.getExpireTime()) {
            forRemoval.add(entityId);
        }
    }

	protected void updateVisibility() {
		if (parent.getWorld().isClientSide()) return;
		if (!parent.isPlayerRiding()) {
			TrackableEntitiesManager.addTrackableEntity(parent);
			DependencySafety.addExtraEntityToRDP(Objects.requireNonNull(parent.getServer()), parent);
		}
		else TrackableEntitiesManager.removeTrackableEntity(parent);
	}

	public void onParentRemove() {
		if (parent.getWorld().isClientSide()) return;
		TrackableEntitiesManager.removeTrackableEntity(parent);
	}
	
	protected void updateDataLink() {
		if (parent.getWorld().isClientSide()) return;
		refreshDataLink();
		if (!hasDataLink()) return;
		Entity controller = parent.getControllingPlayerOrBot();
		boolean check_equals = true;
		if (parent.isStationaryRadar() && controller == null) {
			controller = parent.getOwner();
			check_equals = false;
		}
		if (controller == null) return;
		ServerPlayer playerController = null;
		if (controller instanceof ServerPlayer sp) playerController = sp;
		List<? extends Player> players = parent.getWorld().players();
		for (Player p : players) {
			if (check_equals && controller.equals(p))
				continue;
			if (playerController != null) {
				if (!UtilEntity.arePlayersAllied(playerController, (ServerPlayer) p))
					continue;
			} else if (!controller.isAlliedTo(p))
				continue;
			if (!UtilEntity.getLevel(controller).dimension().equals(UtilEntity.getLevel(p).dimension()))
				continue;
			if (!(p.getRootVehicle() instanceof EntityVehicle plane))
				continue;
			if (!plane.radarSystem.hasDataLink())
				continue;
			if (plane.equals(parent))
				continue;
			for (RadarTarget rp : targets.values()) {
				if (rp.entityId == plane.getId()) continue;
				if (rp.isShared()) continue;
				if (plane.radarSystem.hasDataLinkBuffer(rp.entityId)) continue;
				plane.radarSystem.dataLinkBuffer.add(rp.getCopy(true));
			}
		} 
	}

	public boolean hasTargets() {
		return !targets.isEmpty();
	}

	public boolean hasTarget(Entity entity) {
		if (hasTarget(entity.getId())) return true;
		if (entity.isPassenger()) {
			Entity v = entity.getRootVehicle();
			if (hasTarget(v.getId())) return true;
		}
		return false;
	}
	
	public boolean hasTarget(int entityId) {
        return targets.containsKey(entityId);
	}
	
	private boolean hasDataLinkBuffer(int id) {
		for (RadarTarget rp : dataLinkBuffer) if (rp.entityId == id) return true;
		return false;
	}
	
	private void refreshDataLink() {
        for (RadarTarget dataLinkTarget : dataLinkBuffer) {
            RadarTarget old = targets.get(dataLinkTarget.entityId);
            if (old != null) old.updateFromDataLink(dataLinkTarget);
            else targets.put(dataLinkTarget.entityId, dataLinkTarget);
            forRemoval.remove(dataLinkTarget.entityId);
        }
		dataLinkBuffer.clear();
	}
	
	private void updateSemiActiveTrackMissiles() {
		for (int i = 0; i < rockets.size(); ++i) {
			EntityMissile<?> r = rockets.get(i);
			if (!r.isSimulateEnabled()) {
				rockets.remove(i--);
				continue;
			}
			boolean b = false;
            for (RadarTarget target : targets.values()) {
                if (target.entityId == r.target.getId()) {
                    r.targetPos = target.pos;
                    b = true;
                    break;
                }
            }
			if (b) continue;
			rockets.remove(i--);
			r.kill();
		}
	}
	
	public void addRocket(EntityMissile<?> r) {
		if (!rockets.contains(r)) rockets.add(r);
	}
	
	public void selectTarget(RadarTarget ping) {
		selectTarget(ping.entityId);
	}
	
	public void selectTarget(int entityId) {
		selectedTargetId = -1;
        if (targets.containsKey(entityId)) selectedTargetId = entityId;
	}
	
	public void selectTarget(Entity entity) {
		selectedTargetId = -1;
		if (hasTarget(entity.getId())) selectTarget(entity.getId());
		else if (entity.isPassenger()) {
			Entity v = entity.getRootVehicle();
			if (hasTarget(v.getId())) selectTarget(v.getId());
		}
	}
	
	@Nullable
	public Entity getSelectedTargetEntity() {
		if (selectedTargetId == -1 || !targets.containsKey(selectedTargetId)) return null;
		int id = targets.get(selectedTargetId).entityId;
		Entity entity = parent.getWorld().getEntity(id);
		if (entity != null) return entity;
		SimulatedEntity sim = SimulatedEntityManager.get().getById(id);
		if (sim != null) return (Entity) sim;
		return null;
	}

	@Nullable
	public RadarTarget getServerSelectedTarget() {
		if (selectedTargetId == -1) return null;
		return targets.get(selectedTargetId);
	}

	@Nullable
	public LivingEntity getLivingTargetByWeapon(WeaponInstance<?> wd) {
		for (RadarTarget ping : targets.values()) {
			if (ping.isFriendly) continue;
			Entity entity = parent.getWorld().getEntity(ping.entityId);
			if (entity instanceof LivingEntity target 
					&& wd.couldRadarWeaponTargetEntity(entity, parent)) 
				return target;
		}
		return null;
	}
	
	@Nullable
	public Player getPlayerTargetByWeapon(WeaponInstance<?> wd) {
		for (RadarTarget ping : targets.values()) {
			if (ping.isFriendly) continue;
			Entity entity = parent.getWorld().getEntity(ping.entityId);
			if (entity == null) continue;
			if (entity instanceof Player target 
					&& !target.isCreative()
					&& wd.couldRadarWeaponTargetEntity(entity, parent)) 
				return target;
			else if (entity.getControllingPassenger() instanceof Player target 
					&& !target.isCreative()
					&& wd.couldRadarWeaponTargetEntity(entity, parent)) 
				return target;
		}
		return null;
	}
	
	public void clientSelectTarget(RadarTarget target) {
		clientSelectTarget(target.entityId);
	}
	
	public void clientSelectNextTarget() {
		int size = getClientRadarPings().size();
		if (size == 0) return;
		int k = 0, s = clientSelectedId, firstId = clientSelectedId;
        for (RadarTarget target : getClientRadarPings()) {
            if (k == 0) firstId = target.entityId;
            if (target.entityId > clientSelectedId) {
                s = target.entityId;
                break;
            }
            ++k;
        }
        if (k == size) s = firstId;
		clientSelectTarget(s);
	}
	
	public void clientSelectTarget(int entityId) {
        clientSelectedId = -1;
		if (!clientTargets.containsKey(entityId)) return;
		if (parent.tickCount-clientSelectedTime < 2) return;
		clientSelectedId = entityId;
		parent.soundManager.playPassengerRadarLockSound();
		VehicleSyncAction.sendSyncAction(new VehicleSyncAction.PingSelectAction(clientTargets.get(entityId)));
		clientSelectedTime = parent.tickCount;
	}
	
	public int getClientSelectedTargetId() {
		return clientSelectedId;
	}
	
	@NotNull
	public Collection<RadarTarget> getClientRadarPings() {
		return clientTargets.values();
	}
	
	@Nullable
	public RadarTarget getClientSelectedPing() {
		return clientTargets.get(clientSelectedId);
	}
	
	public boolean isClientLocking() {
		return getClientSelectedTargetId() != -1;
	}
	
	public void readClientPingsFromServer(IntObjectMap<RadarTarget> pings) {
        clientTargets.clear();
        clientTargets.putAll(pings);
        finishReadClientPings();
	}

    public void readClientPingsFromServer(List<RadarTarget> pings) {
        clientTargets.clear();
        pings.forEach(ping -> clientTargets.put(ping.entityId, ping));
        finishReadClientPings();
    }

    private void finishReadClientPings() {
        removeUnwantedPings();
        updateClientPingPos();
        clientPingRefreshTime = parent.tickCount;
    }
	
	private void removeUnwantedPings() {
		RadarFilterMode mode = DSCClientInputs.getRadarFilterMode();
		clientTargets.entrySet().removeIf(entry -> entry.getValue().dontDisplayByMode(mode));
	}
	
	public boolean hasRadar() {
		return !radars.isEmpty();
	}
	
	public boolean hasRadar(String id) {
		for (RadarInstance<?> r : radars) if (r.getStatsId().equals(id)) return true;
		return false;
	}
	
	@Nullable
	public RadarInstance<?> get(String id, String slotId) {
		for (RadarInstance<?> r : radars) if (r.idMatch(id, slotId)) return r;
		return null;
	}
	
	public boolean addRadar(RadarInstance<?> r) {
		if (get(r.getStatsId(), r.getSlotId()) != null) return false;
		radars.add(r);
		return true;
	}
	
	public boolean removeRadar(String id, String slotId) {
		RadarInstance<?> radar = get(id, slotId);
		if (radar == null) return false;
		radar.resetPings(parent);
		return radars.remove(radar);
	}
	
	public double getMaxAirRange() {
		double max = 0;
		for (RadarInstance<?> r : radars) 
			if (r.getStats().isScanAir() && r.getStats().getRange() > max) 
				max = r.getStats().getRange();
		return max;
	}
	
	@Override
	public String toString() {
		String s = "Radars:";
		for (int i = 0; i < radars.size(); ++i) s += radars.get(i).toString();
		return s;
	}
	
	public boolean isReadData() {
		return readData;
	}
	
	public void addRWRWarning(int fromId, Vec3 pos, boolean isMissile, boolean fromGround) {
		if (parent == null || parent.isClientSide() || !hasRadar()) return;
		RWRWarning warning = new RWRWarning(fromId, pos, fromGround, isMissile);
        PacketHandler.sendToTrackers(new ToClientRWRWarning(parent.getId(), warning), parent);
	}
	
	public void readRWRWarningFromServer(RWRWarning warning) {
		if (rwrWarnings.containsKey(warning.fromId)) {
			RWRWarning w = rwrWarnings.get(warning.fromId);
			w.age = 0;
			w.pos = warning.pos;
		} else rwrWarnings.put(warning.fromId, warning);
		if (warning.isMissile) rwrMissile = true;
		rwrRadar = true;
		clientRwrRefreshTime = parent.tickCount;
	}
	
	public boolean isTrackedByMissile() {
		return rwrMissile;
	}
	
	public boolean isTrackedByRadar() {
		return rwrRadar;
	}

	public boolean clientConsumePingWarningSound() {
		return false;
	}
	
	public void serverTick() {
		tickUpdateTargets();
		updateVisibility();
	}
	
	public void clientTick() {
		ageRWR();
		updateClientPingPos();
        if (!clientTargets.containsKey(clientSelectedId)) clientSelectedId = -1;
	}
	
	private void ageRWR() {
		rwrRadar = false;
		rwrMissile = false;
		if (rwrWarnings.isEmpty()) return;
		Iterator<RWRWarning> it = rwrWarnings.values().iterator();
		while (it.hasNext()) {
			RWRWarning n = it.next();
			++n.age;
			if (n.age <= 10) {
				if (n.isMissile) rwrMissile = true;
				rwrRadar = true;
			}
			if (n.isMissile && n.age > 20) it.remove();
			else if (n.age > 60) it.remove();
		}
	}
	
	private void updateClientPingPos() {
		clientTargets.forEach((id, target) -> target.setClientPos(parent.getWorld()));
	}
	
	public boolean clientHasRWRWarnings() {
		return !rwrWarnings.isEmpty();
	}
	
	public Collection<RWRWarning> getClientRWRWarnings() {
		return rwrWarnings.values();
	}

    public boolean hasClientTarget(int entityId) {
        return clientTargets.containsKey(entityId);
    }

    @Nullable
    public RadarTarget getClientTarget(int entityId) {
        return clientTargets.get(entityId);
    }

    public static class RWRWarning {
		public final int fromId;
		public final boolean fromGround;
		public final boolean isMissile;
		public Vec3 pos;
		public int age = 0;
		public RWRWarning(int fromId, Vec3 pos, boolean fromGround, boolean isMissile) {
			this.fromId = fromId;
			this.pos = pos;
			this.fromGround = fromGround;
			this.isMissile = isMissile;
		}
		public RWRWarning(FriendlyByteBuf buffer) {
			fromId = buffer.readInt();
			pos = DataSerializers.VEC3.read(buffer);
			fromGround = buffer.readBoolean();
			isMissile = buffer.readBoolean();
		}
		public void write(FriendlyByteBuf buffer) {
			buffer.writeInt(fromId);
			DataSerializers.VEC3.write(buffer, pos);
			buffer.writeBoolean(fromGround);
			buffer.writeBoolean(isMissile);
		}
		@Override
		public String toString() {
			return "RWR["+(int)pos.x+","+(int)pos.y+","+(int)pos.z+"]";
		}
	}

	public int getJammedTicks() {
		if (!hasRadar()) return 0;
		return 0;
	}

	public Collection<RadarTarget> getServerPings() {
		return targets.values();
	}
	
}
