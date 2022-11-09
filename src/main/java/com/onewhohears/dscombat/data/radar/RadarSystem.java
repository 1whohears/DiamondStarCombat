package com.onewhohears.dscombat.data.radar;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundAddRadarPacket;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundPingsPacket;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundRemoveRadarPacket;
import com.onewhohears.dscombat.common.network.toserver.ServerBoundPingSelectPacket;
import com.onewhohears.dscombat.data.radar.RadarData.RadarPing;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

public class RadarSystem {
	
	private List<RadarData> radars = new ArrayList<RadarData>();
	private EntityAbstractAircraft parent;
	
	private List<RadarPing> targets = new ArrayList<RadarPing>();
	private int selectedIndex = -1;
	private List<RadarPing> clientTargets = new ArrayList<RadarPing>();
	private int clientSelectedIndex = -1;
	private List<EntityMissile> rockets = new ArrayList<EntityMissile>();
	private boolean readData = true;
	
	public RadarSystem() {
		readData = false;
	}
	
	public RadarSystem(CompoundTag compound) {
		radars.clear();
		ListTag list = compound.getList("radars", 10);
		for (int i = 0; i < list.size(); ++i) radars.add(new RadarData(list.getCompound(i)));
	}
	
	public void write(CompoundTag compound) {
		ListTag list = new ListTag();
		for (int i = 0; i < radars.size(); ++i) list.add(radars.get(i).write());
		compound.put("radars", list);
	}
	
	public RadarSystem(FriendlyByteBuf buffer) {
		radars.clear();
		int num = buffer.readInt();
		for (int i = 0; i < num; ++i) radars.add(new RadarData(buffer));
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(radars.size());
		for (int i = 0; i < radars.size(); ++i) radars.get(i).write(buffer);
	}
	
	public void tickUpdateTargets() {
		RadarPing old = null; 
		if (selectedIndex != -1 && selectedIndex < targets.size()) old = targets.get(selectedIndex);
		selectedIndex = -1;
		for (RadarData r : radars) r.tickUpdateTargets(parent, targets);
		if (old != null) for (int i = 0; i < targets.size(); ++i) 
			if (targets.get(i).id == old.id) {
				selectedIndex = i;
				if (getSelectedTarget(parent.level) instanceof EntityAbstractAircraft plane) {
					plane.lockedOnto();
				}
				break;
			}
		//System.out.println("chunk source "+radar.getCommandSenderWorld().getChunkSource().getClass().getName());
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> parent), 
				new ClientBoundPingsPacket(parent.getId(), targets));
		updateRockets();
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
		int id = ping.id;
		selectedIndex = -1;
		for (int i = 0; i < targets.size(); ++i) if (targets.get(i).id == id) {
			selectedIndex = i;
			break;
		}
	}
	
	@Nullable
	public Entity getSelectedTarget(Level level) {
		if (selectedIndex == -1) return null;
		int id = targets.get(selectedIndex).id;
		return level.getEntity(id);
	}
	
	public void clientSelectTarget(RadarPing ping) {
		int id = ping.id;
		clientSelectedIndex = -1;
		for (int i = 0; i < clientTargets.size(); ++i) 
			if (clientTargets.get(i).id == id) {
				clientSelectedIndex = i;
				PacketHandler.INSTANCE.sendToServer(new ServerBoundPingSelectPacket(parent.getId(), ping));
				break;
			}
		//System.out.println("new selected index "+clientSelectedIndex);
	}
	
	public int getClientSelectedPingIndex() {
		return clientSelectedIndex;
	}
	
	public List<RadarPing> getClientRadarPings() {
		return clientTargets;
	}
	
	public void readClientPingsFromServer(List<RadarPing> pings) {
		//System.out.println("pre selected index "+clientSelectedIndex);
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
		//System.out.println("old select "+oldSelect);
		//System.out.println("refreshed selected index "+clientSelectedIndex);
	}
	
	public boolean hasRadar() {
		return radars.size() > 0;
	}
	
	@Nullable
	public RadarData get(String id, String slotId) {
		for (RadarData r : radars) if (r.idMatch(id, slotId)) return r;
		return null;
	}
	
	public boolean addRadar(RadarData r, boolean updateClient) {
		if (get(r.getId(), r.getSlotId()) != null) return false;
		radars.add(r);
		if (updateClient) {
			PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> parent), 
					new ClientBoundAddRadarPacket(parent.getId(), r));
		}
		return true;
	}
	
	public void removeRadar(String id, String slotId, boolean updateClient) {
		boolean ok = radars.remove(get(id, slotId));
		if (ok && updateClient) {
			PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> parent), 
					new ClientBoundRemoveRadarPacket(parent.getId(), id, slotId));
		}
		return;
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
	
	public void setup(EntityAbstractAircraft e) {
		parent = e;
	}
	
	public void clientSetup(EntityAbstractAircraft e) {
		parent = e;
	}
	
}
