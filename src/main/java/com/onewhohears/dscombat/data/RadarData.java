package com.onewhohears.dscombat.data;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.common.PacketHandler;
import com.onewhohears.dscombat.common.network.ClientBoundPingsPacket;
import com.onewhohears.dscombat.common.network.ServerBoundPingSelectPacket;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class RadarData extends PartData {
	
	private double range;
	private double fov;
	private int scanRate;
	private boolean scanAircraft;
	private boolean scanPlayers;
	private boolean scanMobs;
	
	private List<RadarPing> targets = new ArrayList<RadarPing>();
	private int selectedIndex = -1;
	private boolean freshTargets;
	private int scanTicks;
	private List<EntityMissile> rockets = new ArrayList<EntityMissile>();
	
	private List<RadarPing> clientTargets = new ArrayList<RadarPing>();
	private int clientSelectedIndex = -1;
	
	public RadarData(String id, double range, double fov, int scanRate) {
		super(id, Vec3.ZERO);
		this.range = range;
		this.fov = fov;
		this.scanRate = scanRate;
	}
	
	public RadarData(CompoundTag tag) {
		super(tag);
		range = tag.getDouble("range");
		fov = tag.getDouble("fov");
		scanRate = tag.getInt("scanRate");
		scanAircraft = tag.getBoolean("scanAircraft");
		scanPlayers = tag.getBoolean("scanPlayers");
		scanMobs = tag.getBoolean("scanMobs");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putDouble("range", range);
		tag.putDouble("fov", fov);
		tag.putInt("scanRate", scanRate);
		tag.putBoolean("scanAircraft", scanAircraft);
		tag.putBoolean("scanPlayers", scanPlayers);
		tag.putBoolean("scanMobs", scanMobs);
		return tag;
	}
	
	public RadarData(FriendlyByteBuf buffer) {
		super(buffer);
		range = buffer.readDouble();
		fov = buffer.readDouble();
		scanRate = buffer.readInt();
		scanAircraft = buffer.readBoolean();
		scanPlayers = buffer.readBoolean();
		scanMobs = buffer.readBoolean();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeDouble(range);
		buffer.writeDouble(fov);
		buffer.writeInt(scanRate);
		buffer.writeBoolean(scanAircraft);
		buffer.writeBoolean(scanPlayers);
		buffer.writeBoolean(scanMobs);
	}
	
	public void tickUpdateTargets(Entity radar) {
		if (scanTicks > scanRate) scanTicks = 0;
		else {
			++scanTicks;
			freshTargets = false;
			return;
		}
		freshTargets = true;
		RadarPing old = null; 
		if (selectedIndex != -1) old = targets.get(selectedIndex);
		targets.clear();
		selectedIndex = -1;
		Level level = radar.level;
		List<Entity> list = level.getEntities(radar, getRadarBoundingBox(radar));
		for (int i = 0; i < list.size(); ++i) {
			Entity target = list.get(i);
			if (target.isInvisible()) continue;
			if (this.scanAircraft && target instanceof EntityAbstractAircraft plane) {
				if (!checkTargetRange(radar, target, plane.getStealth())) continue;
				if (!checkCanSee(radar, target)) continue;
				targets.add(new RadarPing(target));
			} else if (this.scanPlayers && target instanceof Player player) {
				if (!checkTargetRange(radar, target, 1)) continue;
				if (!checkCanSee(radar, target)) continue;
				targets.add(new RadarPing(target));
			} else if (this.scanMobs && target instanceof Mob mob) {
				if (!checkTargetRange(radar, target, 1)) continue;
				if (!checkCanSee(radar, target)) continue;
				targets.add(new RadarPing(target));
			}
		}
		if (old != null) for (int i = 0; i < targets.size(); ++i) 
			if (targets.get(i).id == old.id) {
				selectedIndex = i;
				break;
			}
		updateRockets();
		//System.out.println("chunk source "+radar.getCommandSenderWorld().getChunkSource().getClass().getName());
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> radar), 
				new ClientBoundPingsPacket(radar.getId(), targets));
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
	
	public Entity getSelectedTarget(Level level) {
		if (selectedIndex == -1) return null;
		int id = targets.get(selectedIndex).id;
		return level.getEntity(id);
	}
	
	public void clientSelectTarget(Entity radar, RadarPing ping) {
		int id = ping.id;
		clientSelectedIndex = -1;
		for (int i = 0; i < clientTargets.size(); ++i) 
			if (clientTargets.get(i).id == id) {
				clientSelectedIndex = i;
				PacketHandler.INSTANCE.sendToServer(new ServerBoundPingSelectPacket(
						radar.getId(), ping));
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
	
	private boolean checkTargetRange(Entity radar, Entity target, double rangeMod) {
		if (fov == -1) return true;
		return UtilGeometry.isPointInsideCone(
				target.position(), 
				radar.position().add(getRelativePos()), 
				radar.getLookAngle(), 
				fov, range*rangeMod);
	}
	
	private boolean checkCanSee(Entity radar, Entity target) {
		return UtilGeometry.canEntitySeeEntity(radar, target);
	}
	
	private AABB getRadarBoundingBox(Entity radar) {
		double x = radar.getX();
		double y = radar.getY();
		double z = radar.getZ();
		double w = range/2;
		return new AABB(x+w, y+w, z+w, x-w, y-w, z-w);
	}
	
	@Override
	public PartType getType() {
		return PartType.RADAR;
	}

	@Override
	public void setup(EntityAbstractAircraft craft) {
		super.setup(craft);
	}
	
	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public double getFov() {
		return fov;
	}

	public void setFov(double fov) {
		this.fov = fov;
	}

	public int getScanRate() {
		return scanRate;
	}

	public void setScanRate(int scanRate) {
		this.scanRate = scanRate;
	}
	
	public boolean isFreshTargets() {
		return freshTargets;
	}

	public boolean isScanAircraft() {
		return scanAircraft;
	}

	public boolean isScanPlayers() {
		return scanPlayers;
	}

	public boolean isScanMobs() {
		return scanMobs;
	}

	public void setScanAircraft(boolean scanAircraft) {
		this.scanAircraft = scanAircraft;
	}

	public void setScanPlayers(boolean scanPlayers) {
		this.scanPlayers = scanPlayers;
	}

	public void setScanMobs(boolean scanMobs) {
		this.scanMobs = scanMobs;
	}
	
	public static class RadarPing {
		
		public final int id;
		public final Vec3 pos;
		
		public RadarPing(Entity ping) {
			id = ping.getId();
			pos = ping.position();
		}
		
		public RadarPing(FriendlyByteBuf buffer) {
			id = buffer.readInt();
			double x, y, z;
			x = buffer.readDouble();
			y = buffer.readDouble();
			z = buffer.readDouble();
			pos = new Vec3(x, y, z);
		}
		
		public void write(FriendlyByteBuf buffer) {
			buffer.writeInt(id);
			buffer.writeDouble(pos.x);
			buffer.writeDouble(pos.y);
			buffer.writeDouble(pos.z);
		}
		
	}
	
}
