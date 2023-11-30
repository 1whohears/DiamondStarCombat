package com.onewhohears.dscombat.util;

import java.util.Random;

import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.init.ModParticles;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class UtilParticles {
	
	private static Random random = new Random();
	
	// TODO 6.1 improve particle system
	
	public static void vehicleCrashExplosion(Level level, Vec3 pos, double expRadius) {
		System.out.println("vehicleCrashExplosion "+pos);
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
		for (int i = 0; i < 25; ++i) {
			level.addAlwaysVisibleParticle(ModParticles.BIG_FLAME.get(), 
				true, pos.x, pos.y+0.2, pos.z, 
				randomSpeed(0.3, 0.2), 
				randomSpeed(0.6, 0.3), 
				randomSpeed(0.3, 0.2));
		}
	}
	
	public static double randomSpeed(double speed, double range) {
		return speed * randPosNeg() + range * random.nextGaussian();
	}
	
	public static int randPosNeg() {
		if (random.nextBoolean()) return 1;
		else return -1;
	}
	
	public static void vehicleParticles(EntityVehicle vehicle) {
		// TODO 6.2 engine smoke
		vehicleDamageSmoke(vehicle);
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
	
	public static void weaponExplode(Level level, Vec3 pos, double radius, boolean fire) {
		
	}
	
	public static void missileTrail(Level level, Vec3 pos, Vec3 move, double size, boolean inWater) {
		level.addParticle(ModParticles.CONTRAIL.get(), 
				pos.x, pos.y, pos.z, 
				-move.x*0.5, -move.y*0.5, -move.z*0.5);
		level.addParticle(ModParticles.CONTRAIL.get(), 
				pos.x, pos.y, pos.z, 
				-move.x*0.25, -move.y*0.25, -move.z*0.25);
	}
	
	public static void onWeaponHitBlock(EntityWeapon weapon, BlockHitResult hit) {
		
	}
	
	public static void onWeaponHitEntity(EntityWeapon weapon, EntityHitResult hit) {
		
	}
	
}
