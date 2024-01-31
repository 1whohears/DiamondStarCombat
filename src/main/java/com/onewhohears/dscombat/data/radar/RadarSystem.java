package com.onewhohears.dscombat.data.radar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientRWRWarning;
import com.onewhohears.dscombat.common.network.toclient.ToClientRadarPings;
import com.onewhohears.dscombat.common.network.toserver.ToServerPingSelect;
import com.onewhohears.dscombat.data.radar.RadarData.RadarPing;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.init.DataSerializers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

/**
 * manages the radar/targeting/rwr system for {@link EntityVehicle}.
 * individual radars are abstracted into {@link RadarData}.
 * individual radars update the radar system's link of {@link RadarPing} on the server side.
 * the updated link of pings are then sent to the client. 
 * the client then tells the server which ping is selected.
 * then the {@link com.onewhohears.dscombat.data.weapon.WeaponSystem} gets the target entity from here.
 * @author 1whohears
 */
public class RadarSystem {
	
	private final EntityVehicle parent;
	private boolean readData = false;
	
	private List<RadarData> radars = new ArrayList<RadarData>();
	private List<EntityMissile> rockets = new ArrayList<EntityMissile>();
	
	private List<RadarPing> targets = new ArrayList<RadarPing>();
	private int selectedIndex = -1;
	private List<RadarPing> clientTargets = new ArrayList<RadarPing>();
	private int clientSelectedIndex = -1, clientSelectedTime = -21;
	public int clientPingRefreshTime = 0;
	public int clientRwrRefreshTime = 0;
	
	private Map<Integer, RWRWarning> rwrWarnings = new HashMap<>();
	private boolean rwrMissile, rwrRadar;
	
	public boolean dataLink = false;
	
	public RadarSystem(EntityVehicle parent) {
		this.parent = parent;
	}
	
	public List<RadarData> getRadars() {
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
		return parent.isPlayerRiding() || (parent.level.getGameRules().getBoolean(DSCGameRules.MOBS_TICK_RADAR) && parent.isBotUsingRadar());
	}
	
	public void tickUpdateTargets() {
		RadarPing old = null; 
		if (selectedIndex != -1 && selectedIndex < targets.size()) old = targets.get(selectedIndex);
		selectedIndex = -1;
		// PLANE RADARS
		for (RadarData r : radars) r.tickUpdateTargets(parent, targets);
		// DATA LINK
		if (parent.tickCount % 20 == 0) {
			clearDataLink();
			if (hasDataLink()) {
				Entity controller = parent.getControllingPassenger();
				if (!(controller instanceof Player player)) return;
				List<? extends Player> players = parent.level.players();
				for (Player p : players) {
					if (player.equals(p)) continue;
					if (!player.isAlliedTo(p)) continue;
					if (!(p.getRootVehicle() instanceof EntityVehicle plane)) continue;
					if (!plane.radarSystem.hasDataLink()) continue;
					if (plane.equals(parent)) continue;
					for (RadarPing rp : plane.radarSystem.targets) {
						if (rp.id == parent.getId()) continue;
						if (rp.isShared()) continue;
						if (hasTarget(rp.id)) continue;
						targets.add(rp.getCopy(true));
					}
				} 
			}
		}
		// PICK PREVIOUS TARGET
		if (old != null) for (int i = 0; i < targets.size(); ++i) 
			if (targets.get(i).id == old.id) {
				selectedIndex = i;
				if (getSelectedTarget() instanceof EntityVehicle plane) {
					plane.lockedOnto(parent);
				}
				break;
			}
		// ROCKETS
		updateRockets();
		// PACKET
		if (parent.tickCount % 20 == 0) parent.toClientPassengers(
				new ToClientRadarPings(parent.getId(), targets));
	}
	
	public int getClientPingIndexByEntityId(int id) {
		for (int i = 0; i < clientTargets.size(); ++i) if (clientTargets.get(i).id == id) return i;
		return -1;
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
	
	private void clearDataLink() {
		for (int i = 0; i < targets.size(); ++i) 
			if (targets.get(i).isShared()) 
				targets.remove(i--);
	}
	
	private void updateRockets() {
		for (int i = 0; i < rockets.size(); ++i) {
			EntityMissile r = rockets.get(i);
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
	
	public void addRocket(EntityMissile r) {
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
	public LivingEntity getLivingTargetByWeapon(WeaponData wd) {
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
	public Player getPlayerTargetByWeapon(WeaponData wd) {
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
		int s = clientSelectedIndex + 1;
		if (s >= size) s = 0;
		clientSelectTarget(s);
	}
	
	public void clientSelectTarget(int pingIndex) {
		if (pingIndex < 0 || pingIndex >= getClientRadarPings().size()) return;
		if (parent.tickCount-clientSelectedTime < 2) return;
		clientSelectedIndex = pingIndex;
		parent.soundManager.playRadarLockSound();
		PacketHandler.INSTANCE.sendToServer(new ToServerPingSelect(
				parent.getId(), clientTargets.get(pingIndex)));
		clientSelectedTime = parent.tickCount;
	}
	
	public int getClientSelectedPingIndex() {
		return clientSelectedIndex;
	}
	
	@Nonnull
	public List<RadarPing> getClientRadarPings() {
		return clientTargets;
	}
	
	public boolean isClientLocking() {
		return getClientSelectedPingIndex() != -1;
	}
	
	public void readClientPingsFromServer(List<RadarPing> pings) {
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
	
	public boolean hasRadar() {
		return radars.size() > 0;
	}
	
	public boolean hasRadar(String id) {
		for (RadarData r : radars) if (r.getId().equals(id)) return true;
		return false;
	}
	
	@Nullable
	public RadarData get(String id, String slotId) {
		for (RadarData r : radars) if (r.idMatch(id, slotId)) return r;
		return null;
	}
	
	public boolean addRadar(RadarData r) {
		if (get(r.getId(), r.getSlotId()) != null) return false;
		radars.add(r);
		return true;
	}
	
	public boolean removeRadar(String id, String slotId) {
		RadarData radar = get(id, slotId);
		if (radar == null) return false;
		radar.resetPings(targets);
		return radars.remove(radar);
	}
	
	public double getMaxAirRange() {
		double max = 0;
		for (RadarData r : radars) 
			if (r.isScanAir() && r.getRange() > max) 
				max = r.getRange();
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
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> parent), 
				new ToClientRWRWarning(parent.getId(), warning));
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
	
	public void serverTick() {
		tickUpdateTargets();
	}
	
	public void clientTick() {
		ageRWR();
		updateClientPingPos();
	}
	
	private void ageRWR() {
		rwrRadar = false;
		rwrMissile = false;
		if (rwrWarnings.size() > 0) {
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
	
}
