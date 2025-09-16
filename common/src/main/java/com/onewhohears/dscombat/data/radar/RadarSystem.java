package com.onewhohears.dscombat.data.radar;

import java.util.*;

import javax.annotation.Nonnull;
import org.jetbrains.annotations.Nullable;

import com.onewhohears.dscombat.DependencySafety;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.VehicleSyncAction;
import com.onewhohears.dscombat.common.network.toclient.ToClientRWRWarning;
import com.onewhohears.dscombat.common.network.toclient.ToClientRadarPings;
import com.onewhohears.dscombat.data.radar.RadarStats.RadarMode;
import com.onewhohears.dscombat.data.radar.RadarStats.RadarPing;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.init.DataSerializers;

import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

/**
 * manages the radar/targeting/rwr system for {@link EntityVehicle}.
 * individual radars are abstracted into {@link RadarStats}.
 * individual radars update the radar system's link of {@link RadarPing} on the server side.
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
	
	private final List<RadarPing> targets = new ArrayList<>();
	private int selectedIndex = -1;
	private List<RadarPing> clientTargets = new ArrayList<>();
	private int clientSelectedIndex = -1, clientSelectedTime = -21;
	public int clientPingRefreshTime = 0;
	public int clientRwrRefreshTime = 0;
	
	private final List<RadarPing> dataLinkBuffer = new ArrayList<>();
	
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
		return dataLink || parent.level.getGameRules().getBoolean(DSCGameRules.DATA_LINK_ALWAYS_ON);
	}
	
	public void tick() {
		if (parent.level.isClientSide) clientTick();
		else if (canServerTick()) serverTick();
	}
	
	public boolean canServerTick() {
		return parent.isOperational() && (parent.isStationaryRadar() || parent.isPlayerRiding()
				|| (parent.level.getGameRules().getBoolean(DSCGameRules.MOBS_TICK_RADAR) && parent.isBotUsingRadar()));
	}
	
	public void tickUpdateTargets() {
		RadarPing old = null; 
		if (selectedIndex != -1 && selectedIndex < targets.size()) old = targets.get(selectedIndex);
		selectedIndex = -1;
		// PLANE RADARS
		for (RadarInstance<?> r : radars) r.tickUpdateTargets(parent, targets);
		// DATA LINK
		if (parent.tickCount % 20 == 0) updateDataLink();
		// PICK PREVIOUS TARGET
		if (old != null) for (int i = 0; i < targets.size(); ++i) 
			if (targets.get(i).id == old.id) {
				selectedIndex = i;
				if (getSelectedTarget() instanceof EntityVehicle plane) {
					plane.lockedOnto(parent);
				}
				break;
			}
		// SEMI ACTIVE TRACK ROCKETS
		updateSemiActiveTrackMissiles();
		// PACKET
		if (parent.tickCount % 20 == 0) {
			if (parent.isStationaryRadar()) parent.toTrackers(new ToClientRadarPings(parent.getId(), targets));
			else parent.toClientPassengers(new ToClientRadarPings(parent.getId(), targets));
		}
	}

	protected void updateVisibility() {
		if (parent.getLevel().isClientSide()) return;
		if (!parent.isPlayerRiding()) {
			TrackableEntitiesManager.addTrackableEntity(parent);
			DependencySafety.addExtraEntityToRDP(Objects.requireNonNull(parent.getServer()), parent);
		}
		else TrackableEntitiesManager.removeTrackableEntity(parent);
	}

	public void onParentRemove() {
		if (parent.getLevel().isClientSide()) return;
		TrackableEntitiesManager.removeTrackableEntity(parent);
	}
	
	protected void updateDataLink() {
		if (parent.getLevel().isClientSide()) return;
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
		List<? extends Player> players = parent.getLevel().players();
		for (Player p : players) {
			if (check_equals && controller.equals(p))
				continue;
			if (playerController != null) {
				if (!UtilEntity.arePlayersAllied(playerController, (ServerPlayer) p))
					continue;
			} else if (!controller.isAlliedTo(p))
				continue;
			if (!controller.getLevel().dimension().equals(p.getLevel().dimension()))
				continue;
			if (!(p.getRootVehicle() instanceof EntityVehicle plane))
				continue;
			if (!plane.radarSystem.hasDataLink())
				continue;
			if (plane.equals(parent))
				continue;
			for (RadarPing rp : targets) {
				if (rp.id == plane.getId()) continue;
				if (rp.isShared()) continue;
				if (plane.radarSystem.hasDataLinkBuffer(rp.id)) continue;
				plane.radarSystem.dataLinkBuffer.add(rp.getCopy(true));
			}
		} 
	}
	
	public int getClientPingIndexByEntityId(int id) {
		for (int i = 0; i < clientTargets.size(); ++i) if (clientTargets.get(i).id == id) return i;
		return -1;
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
	
	public boolean hasTarget(int id) {
		for (RadarPing rp : targets) if (rp.id == id) return true;
		return false;
	}
	
	private boolean hasDataLinkBuffer(int id) {
		for (RadarPing rp : dataLinkBuffer) if (rp.id == id) return true;
		return false;
	}
	
	private void refreshDataLink() {
		for (int i = 0; i < targets.size(); ++i) 
			if (targets.get(i).isShared()) 
				targets.remove(i--);
		for (int i = 0; i < dataLinkBuffer.size(); ++i) 
			if (!hasTarget(dataLinkBuffer.get(i).id)) 
				targets.add(dataLinkBuffer.get(i));
		dataLinkBuffer.clear();
	}
	
	private void updateSemiActiveTrackMissiles() {
		for (int i = 0; i < rockets.size(); ++i) {
			EntityMissile<?> r = rockets.get(i);
			if (r.isRemoved()) {
				rockets.remove(i--);
				continue;
			}
			boolean b = false;
			for (int j = 0; j < targets.size(); ++j) if (targets.get(j).id == r.target.getId()) {
				r.targetPos = targets.get(j).pos;
				b = true;
				break;
			}
			if (b) continue;
			rockets.remove(i--);
			r.kill();
		}
	}
	
	public void addRocket(EntityMissile<?> r) {
		if (!rockets.contains(r)) rockets.add(r);
	}
	
	public void selectTarget(RadarPing ping) {
		selectTarget(ping.id);
	}
	
	public void selectTarget(int id) {
		selectedIndex = -1;
		for (int i = 0; i < targets.size(); ++i) if (targets.get(i).id == id) {
			selectedIndex = i;
			break;
		}
	}
	
	public void selectTarget(Entity entity) {
		selectedIndex = -1;
		if (hasTarget(entity.getId())) selectTarget(entity.getId());
		else if (entity.isPassenger()) {
			Entity v = entity.getRootVehicle();
			if (hasTarget(v.getId())) selectTarget(v.getId());
		}
	}
	
	@Nullable
	public Entity getSelectedTarget() {
		if (selectedIndex == -1) return null;
		int id = targets.get(selectedIndex).id;
		return parent.level.getEntity(id);
	}

	@Nullable
	public RadarPing getServerSelectedPing() {
		if (selectedIndex == -1) return null;
		return targets.get(selectedIndex);
	}

	@Nullable
	public LivingEntity getLivingTargetByWeapon(WeaponInstance<?> wd) {
		for (RadarPing ping : targets) {
			if (ping.isFriendly) continue;
			Entity entity = parent.level.getEntity(ping.id);
			if (entity instanceof LivingEntity target 
					&& wd.couldRadarWeaponTargetEntity(entity, parent)) 
				return target;
		}
		return null;
	}
	
	@Nullable
	public Player getPlayerTargetByWeapon(WeaponInstance<?> wd) {
		for (RadarPing ping : targets) {
			if (ping.isFriendly) continue;
			Entity entity = parent.level.getEntity(ping.id);
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
	
	public void clientSelectTarget(RadarPing ping) {
		clientSelectedIndex = -1;
		for (int i = 0; i < clientTargets.size(); ++i) 
			if (clientTargets.get(i).id == ping.id) 
				clientSelectTarget(i);
	}
	
	public void clientSelectNextTarget() {
		int size = getClientRadarPings().size();
		if (size == 0) return;
		int k = 0, s = clientSelectedIndex;
		while (k++ < size) {
			s++;
			if (s >= size) s = 0;
			if (!getClientRadarPings().get(s).entityType.isMissile()) break;
		}
		clientSelectTarget(s);
	}
	
	public void clientSelectTarget(int pingIndex) {
		if (pingIndex < 0 || pingIndex >= getClientRadarPings().size()) return;
		if (parent.tickCount-clientSelectedTime < 2) return;
		clientSelectedIndex = pingIndex;
		parent.soundManager.playPassengerRadarLockSound();
		VehicleSyncAction.sendSyncAction(new VehicleSyncAction.PingSelectAction(clientTargets.get(pingIndex)));
		clientSelectedTime = parent.tickCount;
	}
	
	public int getClientSelectedPingIndex() {
		return clientSelectedIndex;
	}
	
	@Nonnull
	public List<RadarPing> getClientRadarPings() {
		return clientTargets;
	}
	
	@Nullable
	public RadarPing getClientSelectedPing() {
		if (clientSelectedIndex < 0 || clientSelectedIndex >= getClientRadarPings().size()) return null;
		return getClientRadarPings().get(clientSelectedIndex);
	}
	
	public boolean isClientLocking() {
		return getClientSelectedPingIndex() != -1;
	}
	
	public void readClientPingsFromServer(List<RadarPing> pings) {
		removeUnwantedPings(pings);
		RadarPing oldSelect = null; 
		if (clientSelectedIndex != -1) oldSelect = clientTargets.get(clientSelectedIndex);
		clientTargets = pings;
		clientSelectedIndex = -1;
		if (oldSelect != null) {
			int id = oldSelect.id;
			for (int i = 0; i < clientTargets.size(); ++i) 
				if (clientTargets.get(i).id == id) {
					clientSelectedIndex = i;
					break;
				}
		}
		updateClientPingPos();
		clientPingRefreshTime = parent.tickCount;
	}
	
	private void removeUnwantedPings(List<RadarPing> pings) {
		RadarMode mode = DSCClientInputs.getPreferredRadarMode();
		for (int i = 0; i < pings.size(); ++i) 
			if (pings.get(i).dontDisplayByMode(mode)) 
				pings.remove(i--);
	}
	
	public boolean hasRadar() {
		return radars.size() > 0;
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
		radar.resetPings(targets);
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
		if (parent == null || parent.level.isClientSide || !hasRadar()) return;
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
		for (RadarPing ping : clientTargets) ping.setClientPos(parent.level);
	}
	
	public boolean clientHasRWRWarnings() {
		return !rwrWarnings.isEmpty();
	}
	
	public Collection<RWRWarning> getClientRWRWarnings() {
		return rwrWarnings.values();
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
	
}
