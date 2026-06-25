package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.radar.TrackableEntitiesManager;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.stats.AntiRadarMissileStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AntiRadarMissile<T extends AntiRadarMissileStats> extends EntityMissile<T> {
	
	public AntiRadarMissile(EntityType<? extends AntiRadarMissile<?>> type, Level level, String defaultWeaponId) {
		super(type, level, defaultWeaponId);
	}
	
	@Override
	public WeaponType getWeaponType() {
		return WeaponType.ANTI_RADAR_MISSILE;
	}
	
	@Override
	public void tickGuide() {
		if (tickCount % 10 == 0) findARTarget();
		if (target != null) guideToTarget();
	}
	
	@Override
	public boolean dieIfNoTargetOutsideTickRange() {
		return false;
	}
	
	protected List<ARTarget> targets = new ArrayList<ARTarget>();
	
	protected void findARTarget() {
		targets.clear();
		MinecraftServer server = getWorld().getServer();
		if (server == null) return;
		double rangeSqr = getWeaponStats().getScanRange() * getWeaponStats().getScanRange();
		List<ServerPlayer> players = server.getPlayerList().getPlayers();
		for (ServerPlayer player : players) checkEntity(player, rangeSqr);
		Collection<Entity> entities = TrackableEntitiesManager.getTrackableEntities();
		for (Entity entity : entities) checkEntity(entity, rangeSqr);
		if (targets.isEmpty()) {
			this.target = null;
			this.targetPos = null;
			return;
		}
		ARTarget max = targets.get(0);
		for (int i = 1; i < targets.size(); ++i) 
			if (targets.get(i).radiation > max.radiation) 
				max = targets.get(i);
		this.target = max.entity;
		this.targetPos = max.entity.position();
	}

	protected void checkEntity(Entity entity, double rangeSqr) {
		if (entity.isSpectator()) return;
		if (distanceToSqr(entity) > rangeSqr) return;
		if (!UtilEntity.getLevel(entity).dimension().equals(getWorld().dimension())) return;
		EntityVehicle vehicle;
		if (entity instanceof EntityVehicle ev) vehicle = ev;
		else if (entity.getRootVehicle() instanceof EntityVehicle ev) vehicle = ev;
		else return;
		if (isAlliedTo(vehicle)) return;
		if (!basicCheck(vehicle)) return;
		float distSqr = (float)distanceToSqr(vehicle);
		// Radar source
		boolean hasActiveRadar = vehicle.radarSystem.hasRadar()
				&& !vehicle.getRadarMode().isOff()
				&& vehicle.radarSystem.canServerTick();
		// ECM jammer source
		boolean hasActiveJammer = vehicle.partsManager.getActiveJammerStrength() > 0f;
		if (!hasActiveRadar && !hasActiveJammer) return;
		float radiation = hasActiveRadar
				? (float) vehicle.radarSystem.getMaxAirRange() / distSqr
				: vehicle.partsManager.getActiveJammerStrength() * 10000f / distSqr;
		targets.add(new ARTarget(vehicle, radiation));
	}
	
	protected boolean basicCheck(Entity ping) {
		if (isAlliedTo(ping)) return false;
		if (!checkTargetRange(ping, getWeaponStats().getScanRange())) return false;
		if (!checkCanSee(ping)) return false;
		return true;
	}
	
	public static class ARTarget {
		
		public final Entity entity;
		public final float radiation;
		
		public ARTarget(Entity entity, float radiation) {
			this.entity = entity;
			this.radiation = radiation;
		}
		
	}

}
