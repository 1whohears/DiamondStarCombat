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
	
	public static void vehicleCrashExplosion(EntityVehicle vehicle) {
		System.out.println("vehicleCrashExplosion");
		double speed = 0.5;
		for (int j = 0; j < 90; j += 5) {
			double sinj = Math.sin(j);
			for (int i = 0; i < 360; i += 5) {
				vehicle.level.addParticle(ModParticles.GIANT_EXPLOSION.get(), 
					vehicle.getX(), vehicle.getY(), vehicle.getZ(), 
					randomSpeed((Math.cos(i)-sinj)*speed, 0.02), 
					randomSpeed(sinj*0.1, 0.02), 
					randomSpeed((Math.sin(i)-sinj)*speed, 0.02));
			}
		}
	}
	
	public static double randomSpeed(double speed, double range) {
		return speed + random.nextGaussian() * range;
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
		level.addParticle(ParticleTypes.SMOKE, 
				pos.x, pos.y, pos.z, 
				-move.x * 0.5D + random.nextGaussian() * 0.05D, 
				-move.y * 0.5D + random.nextGaussian() * 0.05D, 
				-move.z * 0.5D + random.nextGaussian() * 0.05D);
	}
	
	public static void onWeaponHitBlock(EntityWeapon weapon, BlockHitResult hit) {
		
	}
	
	public static void onWeaponHitEntity(EntityWeapon weapon, EntityHitResult hit) {
		
	}
	
}
