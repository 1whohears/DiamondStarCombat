package com.onewhohears.dscombat.util;

import java.util.Random;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModParticles;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class UtilParticles {
	
	public static Random random = new Random();
	
	public static void vehicleCrashExplosion(Level level, Vec3 pos, double expRadius) {
		expRadius *= 1.8;
		// dark large smoke spread across the ground
		for (double d = 1; d <= expRadius; d += 1) {
			for (int i = 0; i < 360; i += 15) {
				level.addAlwaysVisibleParticle(ModParticles.LARGE_SMOKE_CLOUD.get(), 
					true, pos.x, pos.y+0.2, pos.z, 
					Math.cos(i)*d, 0.5, Math.sin(i)*d);
			}
		}
		// dark large smoke shoot up in a pillar
		for (double d = 1; d <= expRadius; d += 1) {
			for (int i = 0; i < 360; i += 45) {
				level.addAlwaysVisibleParticle(ModParticles.LARGE_SMOKE_CLOUD.get(), 
					true, pos.x, pos.y+0.2, pos.z, 
					Math.cos(i), d, Math.sin(i));
			}
		}
		// black dirt stuff shooting everywhere biased up
		for (double d = 1; d <= expRadius; d += 1) {
			double h = (expRadius-d+1)*0.075;
			for (int i = 0; i < 360; i += 10) {
				level.addAlwaysVisibleParticle(ModParticles.SHRAPNEL.get(), 
					true, pos.x, pos.y+0.2, pos.z, 
					Math.cos(i)*h, d*0.15, Math.sin(i)*h);
			}
		}
		// fire shooting up and everywhere
		for (int i = 0; i < 32; ++i) {
			level.addAlwaysVisibleParticle(ModParticles.BIG_FLAME.get(), 
				true, pos.x, pos.y+0.2, pos.z, 
				randomSpeed(0.3, 0.2), 
				randomSpeedUp(0.6, 0.3), 
				randomSpeed(0.3, 0.2));
		}
	}
	
	public static double randomSpeedUp(double speed, double range) {
		return speed + range * random.nextGaussian();
	}
	
	public static double randomSpeed(double speed, double range) {
		return speed * randPosNeg() + range * random.nextGaussian();
	}
	
	public static int randPosNeg() {
		if (random.nextBoolean()) return 1;
		else return -1;
	}
	
	public static void vehicleParticles(EntityVehicle vehicle) {
		vehicleEngineSmoke(vehicle);
		// TODO 6.8 fuel leak smoke
		// TODO 6.9 engine fire particles
		vehicleDamageSmoke(vehicle);
	}
	
	public static void vehicleEngineSmoke(EntityVehicle vehicle) {
		Quaternion q = vehicle.getClientQ();
		Vec3 dir = vehicle.getLookAngle().scale(-vehicle.getCurrentThrottle()*0.4);
		for (Vec3 relPos : vehicle.getAfterBurnerSmokePos()) {
			Vec3 pos = UtilAngles.rotateVector(relPos, q).add(vehicle.position());
			engineSmoke(vehicle, pos, dir);
		}
	}
	
	public static void engineSmoke(EntityVehicle vehicle, Vec3 pos, Vec3 dir) {
		if (vehicle.showContrailParticles()) {
			vehicle.level.addParticle(ModParticles.CONTRAIL.get(), 
				pos.x, pos.y, pos.z, 
				dir.x, dir.y, dir.z);
		}
		if (vehicle.showAfterBurnerParticles()) {
			vehicle.level.addParticle(ModParticles.AFTER_BURNER.get(), 
				pos.x, pos.y, pos.z, 
				dir.x, dir.y, dir.z);
			dir = dir.scale(0.5);
			if (vehicle.showMoreAfterBurnerParticles()) {
				vehicle.level.addParticle(ModParticles.AFTER_BURNER.get(), 
					pos.x, pos.y, pos.z, 
					dir.x, dir.y, dir.z);
			}
		}
	}
	
	public static void vehicleDamageSmoke(EntityVehicle vehicle) {
		float r = vehicle.getHealth() / vehicle.getMaxHealth();
		if (r < 0.5f) smoke(vehicle.level, vehicle.position());
		if (r < 0.3f) for (int i = 0; i < 2; ++i) smoke(vehicle.level, vehicle.position());
		if (r < 0.1f) for (int i = 0; i < 4; ++i) smoke(vehicle.level, vehicle.position());
	}
	
	public static void smoke(Level level, Vec3 pos) {
		if (Math.random() > 0.4d) return;
		level.addParticle(ParticleTypes.SMOKE, 
				pos.x, pos.y, pos.z, 
				random.nextGaussian() * 0.08D, 
				0.1D, 
				random.nextGaussian() * 0.08D);
	}
	
	public static void missileTrail(Level level, Vec3 pos, Vec3 move, double size, boolean inWater) {
		level.addParticle(ModParticles.CONTRAIL.get(), 
				pos.x, pos.y, pos.z, 
				-move.x*0.5, -move.y*0.5, -move.z*0.5);
		level.addParticle(ModParticles.CONTRAIL.get(), 
				pos.x, pos.y, pos.z, 
				-move.x*0.25, -move.y*0.25, -move.z*0.25);
	}
	
	public static void bulletImpact(Level level, Vec3 pos, double damage) {
		// TODO 6.3 bullet impact particles
		for (int i = 0; i < 360; i += 30) for (int j = 0; j <= 90; j += 30) {
			level.addAlwaysVisibleParticle(ParticleTypes.LARGE_SMOKE, 
				true, pos.x, pos.y+0.2, pos.z, 
				Math.cos(i)*0.05, Math.sin(j)*0.05, Math.sin(i)*0.05);
		}
	}
	
	public static void bulletExplode(Level level, Vec3 pos, double radius, boolean fire) {
		// TODO 6.5 bullet explode particles
		radius *= 1.3;
		for (double d = 1; d <= radius; d += 1) {
			for (int i = 0; i < 360; i += 20) for (int j = -90; j <= 90; j += 30) {
				level.addAlwaysVisibleParticle(ModParticles.LARGE_SMOKE_CLOUD.get(), 
					true, pos.x, pos.y+0.2, pos.z, 
					Math.cos(i)*d, Math.sin(j)*d, Math.sin(i)*d);
			}
		}
		if (fire) for (int i = 0; i < 24; ++i) {
			level.addAlwaysVisibleParticle(ModParticles.BIG_FLAME.get(), 
				true, pos.x, pos.y+0.2, pos.z, 
				randomSpeed(0.3, 0.2), 
				randomSpeedUp(0.45, 0.3), 
				randomSpeed(0.3, 0.2));
		}
	}
	
	public static void bombExplode(Level level, Vec3 pos, double radius, boolean fire) {
		// TODO 6.6 bomb explode particles
		radius *= 1.3;
		for (double d = 1; d <= radius; d += 1) {
			for (int i = 0; i < 360; i += 20) for (int j = -90; j <= 90; j += 30) {
				level.addAlwaysVisibleParticle(ModParticles.LARGE_SMOKE_CLOUD.get(), 
					true, pos.x, pos.y+0.2, pos.z, 
					Math.cos(i)*d, Math.sin(j)*d, Math.sin(i)*d);
			}
		}
		if (fire) for (int i = 0; i < 24; ++i) {
			level.addAlwaysVisibleParticle(ModParticles.BIG_FLAME.get(), 
				true, pos.x, pos.y+0.2, pos.z, 
				randomSpeed(0.3, 0.2), 
				randomSpeedUp(0.45, 0.3), 
				randomSpeed(0.3, 0.2));
		}
	}
	
	public static void missileExplode(Level level, Vec3 pos, double radius, boolean fire) {
		// TODO 6.7 missile explode particles
		radius *= 1.3;
		for (double d = 1; d <= radius; d += 1) {
			for (int i = 0; i < 360; i += 20) for (int j = -90; j <= 90; j += 30) {
				level.addAlwaysVisibleParticle(ModParticles.LARGE_SMOKE_CLOUD.get(), 
					true, pos.x, pos.y+0.2, pos.z, 
					Math.cos(i)*d, Math.sin(j)*d, Math.sin(i)*d);
			}
		}
		if (fire) for (int i = 0; i < 24; ++i) {
			level.addAlwaysVisibleParticle(ModParticles.BIG_FLAME.get(), 
				true, pos.x, pos.y+0.2, pos.z, 
				randomSpeed(0.3, 0.2), 
				randomSpeedUp(0.45, 0.3), 
				randomSpeed(0.3, 0.2));
		}
	}
	
}
