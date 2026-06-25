package com.onewhohears.dscombat.client.util;

import java.util.Random;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModParticles;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class UtilParticles {
	
	public static Random random = new Random();

	// throttle предыдущего тика для каждой машины — для расчёта всплеска дыма
	private static final java.util.Map<Integer, Float> prevThrottleMap = new java.util.HashMap<>();
	// накопленный всплеск дыма (затухает со временем)
	private static final java.util.Map<Integer, Float> throttleSurgeMap = new java.util.HashMap<>();
	
	public static void vehicleCrashExplosion(Level level, Vec3 pos, double expRadius, int vehicleId) {
		expRadius *= 1.5;
		
		// Взрывная волна (только кастомные частицы)
		explosionShockwave(level, pos, expRadius);
		
		// Ядро взрыва - яркое, в центре
		int coreCount = (int)(expRadius * 8) + 5;
		for (int i = 0; i < coreCount; ++i) {
			double angle = random.nextDouble() * Math.PI * 2;
			double pitch = (random.nextDouble() - 0.5) * Math.PI * 0.6;
			double dist = random.nextDouble() * 0.5;
			
			// БЫСТРОЕ движение
			double speedX = Math.cos(angle) * Math.cos(pitch) * 0.06; // Быстрее (было 0.02)
			double speedY = Math.abs(Math.sin(pitch) * 0.08) + 0.03; // Быстрее вверх (было 0.03 + 0.01)
			double speedZ = Math.sin(angle) * Math.cos(pitch) * 0.06; // Быстрее (было 0.02)
			
			level.addAlwaysVisibleParticle(ModParticles.EXPLOSION_CORE.get(), 
				true, 
				pos.x + Math.cos(angle) * Math.cos(pitch) * dist, 
				pos.y - 0.3 + Math.sin(pitch) * dist,
				pos.z + Math.sin(angle) * Math.cos(pitch) * dist, 
				speedX, speedY, speedZ);
		}
		
		// Дым взрыва - БЫСТРО расползается в стороны, МЕДЛЕННО вверх
		int smokeCount = (int)(expRadius * 20) + 15;
		for (int i = 0; i < smokeCount; ++i) {
			double angle = random.nextDouble() * Math.PI * 2;
			double pitch = random.nextDouble() * Math.PI * 0.4 - Math.PI * 0.1;
			
			// БЫСТРАЯ скорость в стороны, МЕДЛЕННАЯ вверх
			double baseSpeed = 0.06 + random.nextDouble() * 0.08;
			double speedX = Math.cos(angle) * Math.cos(pitch) * baseSpeed;
			double speedY = Math.abs(Math.sin(pitch) * baseSpeed) + 0.02; // МЕДЛЕННЕЕ вверх (было 0.04)
			double speedZ = Math.sin(angle) * Math.cos(pitch) * baseSpeed;
			
			level.addAlwaysVisibleParticle(ModParticles.EXPLOSION_SMOKE.get(), 
				true, 
				pos.x + (random.nextDouble() - 0.5) * 0.6, 
				pos.y - 0.3 + (random.nextDouble() - 0.5) * 0.6,
				pos.z + (random.nextDouble() - 0.5) * 0.6, 
				speedX, speedY, speedZ);
		}
		
		// ОСКОЛКИ - МНОГО, быстрые, разлетаются во все стороны (обломки техники)
		int shrapnelCount = (int)(expRadius * 25) + 20;
		for (int i = 0; i < shrapnelCount; ++i) {
			double angle = random.nextDouble() * Math.PI * 2;
			double pitch = (random.nextDouble() - 0.5) * Math.PI;
			double speed = 0.6 + random.nextDouble() * 1.0; // Очень быстрые (было 0.4-1.1)
			
			level.addAlwaysVisibleParticle(ModParticles.SHRAPNEL.get(), 
				true, 
				pos.x, pos.y - 0.3, pos.z, 
				Math.cos(angle) * Math.cos(pitch) * speed,
				Math.sin(pitch) * speed,
				Math.sin(angle) * Math.cos(pitch) * speed);
		}
		
		// Огонь - используем BIG_FLAME (кастомная частица)
		for (int i = 0; i < 30; ++i) {
			level.addAlwaysVisibleParticle(ModParticles.BIG_FLAME.get(), 
				true, pos.x, pos.y - 0.3, pos.z,
				randomSpeed(0.3, 0.2), 
				randomSpeedUp(0.5, 0.3), 
				randomSpeed(0.3, 0.2));
		}
	}
	/**
	 * Creates a shockwave effect expanding from the explosion center
	 * @param level The world level
	 * @param pos Explosion position
	 * @param expRadius Explosion radius
	 */
	public static void explosionShockwave(Level level, Vec3 pos, double expRadius) {
		// Используем только кастомные частицы дыма для волны
		double shockwaveRadius = Math.min(expRadius * 0.8, 5.0);

		// Кольцо дыма на разной высоте
		for (double height = 0; height <= 1.0; height += 0.5) {
			for (int angle = 0; angle < 360; angle += 25) {
				double radians = Math.toRadians(angle);
				double x = Math.cos(radians) * shockwaveRadius;
				double z = Math.sin(radians) * shockwaveRadius;

				// Используем кастомный дым вместо CLOUD
				level.addAlwaysVisibleParticle(ModParticles.EXPLOSION_SMOKE.get(),
					true,
					pos.x + x * 0.4, pos.y - 0.3 + height, pos.z + z * 0.4, // Опущено ниже
					x * 0.15, height * 0.03 + 0.02, z * 0.15);
			}
		}

		// Дополнительный дым по кругу
		for (int angle = 0; angle < 360; angle += 30) {
			double radians = Math.toRadians(angle);
			double x = Math.cos(radians) * shockwaveRadius * 0.5;
			double z = Math.sin(radians) * shockwaveRadius * 0.5;

			level.addAlwaysVisibleParticle(ModParticles.EXPLOSION_SMOKE.get(),
				true,
				pos.x + x, pos.y - 0.3, pos.z + z, // Опущено ниже (было +0.2)
				x * 0.15, 0.06, z * 0.15);
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
		vehicleAfterBurner(vehicle);
		vehicleExhaustSmoke(vehicle);
		fuelLeakSmoke(vehicle);
		engineFireSmoke(vehicle);
		vehicleDamageSmoke(vehicle);
		tankTrackParticles(vehicle);
	}
	
	public static void vehicleAfterBurner(EntityVehicle vehicle) {
		QuaternionF q = vehicle.getClientQ();
		Vec3 dir = vehicle.getLookAngle().scale(-vehicle.getCurrentThrottle()*0.4);
		for (Vec3 relPos : vehicle.getAfterBurnerSmokePos()) {
			Vec3 pos = UtilAngles.rotateVector(relPos, q).add(vehicle.position());
			afterBurner(vehicle, pos, dir);
		}
	}
	
	public static void vehicleExhaustSmoke(EntityVehicle vehicle) {
		// Only show exhaust smoke for ground vehicles when engine is running
		if (vehicle.getVehicleType() != com.onewhohears.dscombat.data.vehicle.VehicleType.CAR) return;
		if (!vehicle.isOperational()) return;
		if (!vehicle.hasControllingPassenger()) return;

		float throttle = Math.abs(vehicle.getCurrentThrottle());
		float yawRate = Math.abs(vehicle.getYawRate());
		int id = vehicle.getId();

		// --- Всплеск дыма при резком нажатии газа ---
		float prev = prevThrottleMap.getOrDefault(id, throttle);
		float delta = throttle - prev; // положительная = газ нажали резче
		prevThrottleMap.put(id, throttle);

		float surge = throttleSurgeMap.getOrDefault(id, 0f);
		if (delta > 0.05f) {
			// резкое нажатие — добавляем всплеск пропорционально скорости изменения
			surge = Math.min(surge + delta * 3.5f, 2.0f);
		}
		// всплеск затухает каждый тик
		surge *= 0.75f;
		throttleSurgeMap.put(id, surge);

		// базовая активность + всплеск
		float activity = Math.max(throttle, yawRate * 10f);
		activity = Math.min(activity, 1.0f);
		if (activity < 0.1f) activity = 0.1f;
		activity = Math.min(activity + surge, 3.0f); // surge может утроить дым

		QuaternionF q = vehicle.getClientQ();
		for (com.onewhohears.dscombat.data.vehicle.stats.VehicleStats.ExhaustSmokeData data : vehicle.getExhaustSmokePos()) {
			Vec3 pos = UtilAngles.rotateVector(data.pos, q).add(vehicle.position());
			exhaustSmoke(vehicle, pos, activity * data.particleCount);
		}
	}
	
	public static void exhaustSmoke(EntityVehicle vehicle, Vec3 pos, float activity) {
		if (random.nextFloat() < activity * 0.6f) {
			vehicle.getWorld().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
				pos.x + random.nextGaussian() * 0.08,
				pos.y + random.nextGaussian() * 0.08,
				pos.z + random.nextGaussian() * 0.08,
				random.nextGaussian() * 0.02,
				0.06 + random.nextDouble() * 0.08,
				random.nextGaussian() * 0.02);
		}
	}
	
	public static void afterBurner(EntityVehicle vehicle, Vec3 pos, Vec3 dir) {
		if (vehicle.showContrailParticles()) {
			vehicle.getWorld().addParticle(ModParticles.CONTRAIL.get(),
				pos.x, pos.y, pos.z, 
				dir.x, dir.y, dir.z);
		}
		// Огонь убран - теперь только 3D модель в рендерере
		// if (vehicle.showAfterBurnerParticles()) { ... }
	}
	
	public static void fuelLeakSmoke(EntityVehicle vehicle) {
		if (!vehicle.isFuelLeak() || vehicle.getCurrentFuel() <= 0) return;
		Vec3 pos = vehicle.position();
		for (int i = 0; i < 4; ++i) vehicle.getWorld().addParticle(
			ParticleTypes.FALLING_NECTAR, 
			pos.x, pos.y, pos.z, 
			random.nextGaussian()*0.02, 
			-0.1, 
			random.nextGaussian()*0.02);
	}
	
	public static void engineFireSmoke(EntityVehicle vehicle) {
		if (!vehicle.isEngineFire()) return;
		for (Vec3 pos : vehicle.getEngineFirePos()) {
			for (int i = 0; i < 2; ++i) flame(vehicle.getWorld(), pos);
			for (int i = 0; i < 10; ++i) smoke(vehicle.getWorld(), pos);
			for (int i = 0; i < 2; ++i) bigSmoke(vehicle.getWorld(), pos);
		}
	}
	
	public static void vehicleDamageSmoke(EntityVehicle vehicle) {
		float r = vehicle.getHealth() / vehicle.getMaxHealth();
		if (r < 0.5f) smoke(vehicle.getWorld(), vehicle.position());
		if (r < 0.3f) {
			for (int i = 0; i < 2; ++i) smoke(vehicle.getWorld(), vehicle.position());
			bigSmoke(vehicle.getWorld(), vehicle.position());
		}
		if (r < 0.1f) {
			for (int i = 0; i < 4; ++i) smoke(vehicle.getWorld(), vehicle.position());
			for (int i = 0; i < 3; ++i) bigSmoke(vehicle.getWorld(), vehicle.position());
		}
	}
	
	public static void smoke(Level level, Vec3 pos) {
		if (Math.random() > 0.65d) return;
		level.addParticle(ParticleTypes.LARGE_SMOKE, 
				pos.x, pos.y, pos.z, 
				random.nextGaussian() * 0.01, 
				0.1D, 
				random.nextGaussian() * 0.01);
	}
	
	public static void bigSmoke(Level level, Vec3 pos) {
		if (Math.random() > 0.4d) return;
		level.addParticle(ModParticles.LARGE_SMOKE_CLOUD.get(), 
				pos.x, pos.y, pos.z, 
				random.nextGaussian() * 0.5, 
				random.nextGaussian() * 0.5, 
				random.nextGaussian() * 0.5);
	}
	
	public static void flame(Level level, Vec3 pos) {
		if (Math.random() > 0.8d) return;
		level.addParticle(ParticleTypes.FLAME, 
				pos.x, pos.y, pos.z, 
				random.nextGaussian()*0.005, 
				0.1, 
				random.nextGaussian()*0.005);
	}
	
	public static void missileTrail(Level level, Vec3 pos, Vec3 move, double size, boolean inWater) {
		level.addParticle(ModParticles.CONTRAIL.get(), 
				pos.x, pos.y, pos.z, 
				-move.x*0.5, -move.y*0.5, -move.z*0.5);
		level.addParticle(ModParticles.CONTRAIL.get(), 
				pos.x, pos.y, pos.z, 
				-move.x*0.25, -move.y*0.25, -move.z*0.25);
	}
	
	public static void missileAfterBurner(Level level, Vec3 pos, Vec3 dir) {
		level.addParticle(ModParticles.AFTER_BURNER.get(), 
			pos.x, pos.y, pos.z, 
			dir.x, dir.y, dir.z);
	}

	public static void muzzleFlash(Level level, Vec3 muzzlePos, Vec3 dir) {
		muzzleFlash(level, muzzlePos, dir, 25, 0.05f);
	}
	
	public static void muzzleFlash(Level level, Vec3 muzzlePos, Vec3 dir, float particleCount, float spreadDistance) {
		int count = Math.max(1, (int)(particleCount * 5));
		for (int i = 0; i < count; ++i) {
			level.addParticle(ParticleTypes.CLOUD,
				muzzlePos.x, muzzlePos.y, muzzlePos.z,
				dir.x * 0.15 + random.nextGaussian() * spreadDistance,
				dir.y * 0.15 + random.nextGaussian() * spreadDistance,
				dir.z * 0.15 + random.nextGaussian() * spreadDistance);
		}
	}

	public static void bulletImpact(Level level, Vec3 pos, double damage) {
		for (int i = 0; i < 360; i += 30) for (int j = 0; j <= 90; j += 30) {
			level.addAlwaysVisibleParticle(ParticleTypes.LARGE_SMOKE, 
				true, pos.x, pos.y+0.2, pos.z, 
				Math.cos(i)*0.05, Math.sin(j)*0.05, Math.sin(i)*0.05);
		}
	}
	
	public static void bulletExplode(Level level, Vec3 pos, double radius, boolean fire) {
		radius *= 0.5;
		
		// Больше частиц дыма для более плавного эффекта
		int smokeCount = (int)(radius * 20) + 8;
		for (int i = 0; i < smokeCount; ++i) {
			// Случайное направление в сфере
			double angle = random.nextDouble() * Math.PI * 2;
			double pitch = (random.nextDouble() - 0.5) * Math.PI;
			double speed = (0.5 + random.nextDouble() * 0.5) * radius;
			
			level.addAlwaysVisibleParticle(ModParticles.LARGE_SMOKE_CLOUD.get(), 
				true, 
				pos.x + (random.nextDouble() - 0.5) * 0.3, 
				pos.y - 0.3 + (random.nextDouble() - 0.5) * 0.3,
				pos.z + (random.nextDouble() - 0.5) * 0.3, 
				Math.cos(angle) * Math.cos(pitch) * speed,
				Math.sin(pitch) * speed,
				Math.sin(angle) * Math.cos(pitch) * speed);
		}
		
		// ОСКОЛКИ - мелкие, быстрые
		int shrapnelCount = (int)(radius * 10) + 5;
		for (int i = 0; i < shrapnelCount; ++i) {
			double angle = random.nextDouble() * Math.PI * 2;
			double pitch = (random.nextDouble() - 0.5) * Math.PI;
			double speed = 0.2 + random.nextDouble() * 0.4;
			
			level.addAlwaysVisibleParticle(ModParticles.SHRAPNEL.get(), 
				true, 
				pos.x, pos.y - 0.3, pos.z, 
				Math.cos(angle) * Math.cos(pitch) * speed,
				Math.sin(pitch) * speed,
				Math.sin(angle) * Math.cos(pitch) * speed);
		}
		
		if (fire) for (int i = 0; i < 24; ++i) {
			level.addAlwaysVisibleParticle(ModParticles.BIG_FLAME.get(), 
				true, pos.x, pos.y - 0.3, pos.z,
				randomSpeed(0.3, 0.2), 
				randomSpeedUp(0.45, 0.3), 
				randomSpeed(0.3, 0.2));
		}
	}
	
	public static void bombExplode(Level level, Vec3 pos, double radius, boolean fire) {
		radius *= 0.9;
		
		// Ядро взрыва - яркое, в центре, меньше частиц
		int coreCount = (int)(radius * 4) + 3;
		for (int i = 0; i < coreCount; ++i) {
			// Случайное распределение в сфере для более плавного эффекта
			double angle = random.nextDouble() * Math.PI * 2;
			double pitch = (random.nextDouble() - 0.5) * Math.PI * 0.6;
			double dist = random.nextDouble() * 0.3; // Небольшой разброс
			
			// БЫСТРОЕ движение вверх и в стороны
			double speedX = Math.cos(angle) * Math.cos(pitch) * 0.06; // Быстрее (было 0.02)
			double speedY = Math.abs(Math.sin(pitch) * 0.08) + 0.03; // Быстрее вверх (было 0.03 + 0.01)
			double speedZ = Math.sin(angle) * Math.cos(pitch) * 0.06; // Быстрее (было 0.02)
			
			level.addAlwaysVisibleParticle(ModParticles.EXPLOSION_CORE.get(), 
				true, 
				pos.x + Math.cos(angle) * Math.cos(pitch) * dist, 
				pos.y - 0.3 + Math.sin(pitch) * dist,
				pos.z + Math.sin(angle) * Math.cos(pitch) * dist, 
				speedX, speedY, speedZ);
		}
		
		// Дым взрыва - БЫСТРО расползается в стороны, МЕДЛЕННО вверх
		int smokeCount = (int)(radius * 12) + 8;
		for (int i = 0; i < smokeCount; ++i) {
			// Случайное направление, больше вверх
			double angle = random.nextDouble() * Math.PI * 2;
			double pitch = random.nextDouble() * Math.PI * 0.4 - Math.PI * 0.1; // Больше вверх
			
			// БЫСТРАЯ скорость в стороны, МЕДЛЕННАЯ вверх
			double baseSpeed = 0.06 + random.nextDouble() * 0.08;
			double speedX = Math.cos(angle) * Math.cos(pitch) * baseSpeed;
			double speedY = Math.abs(Math.sin(pitch) * baseSpeed) + 0.02; // МЕДЛЕННЕЕ вверх (было 0.04)
			double speedZ = Math.sin(angle) * Math.cos(pitch) * baseSpeed;
			
			level.addAlwaysVisibleParticle(ModParticles.EXPLOSION_SMOKE.get(), 
				true, 
				pos.x + (random.nextDouble() - 0.5) * 0.4, 
				pos.y - 0.3 + (random.nextDouble() - 0.5) * 0.4,
				pos.z + (random.nextDouble() - 0.5) * 0.4, 
				speedX, speedY, speedZ);
		}
		
		// ОСКОЛКИ - быстрые, разлетаются во все стороны
		int shrapnelCount = (int)(radius * 15) + 10;
		for (int i = 0; i < shrapnelCount; ++i) {
			double angle = random.nextDouble() * Math.PI * 2;
			double pitch = (random.nextDouble() - 0.5) * Math.PI;
			double speed = 0.5 + random.nextDouble() * 0.8; // Быстрее (было 0.3-0.8)
			
			level.addAlwaysVisibleParticle(ModParticles.SHRAPNEL.get(), 
				true, 
				pos.x, pos.y - 0.3, pos.z, 
				Math.cos(angle) * Math.cos(pitch) * speed,
				Math.sin(pitch) * speed,
				Math.sin(angle) * Math.cos(pitch) * speed);
		}
		
		// Огонь (если включен)
		if (fire) for (int i = 0; i < 24; ++i) {
			level.addAlwaysVisibleParticle(ModParticles.BIG_FLAME.get(), 
				true, pos.x, pos.y - 0.3, pos.z,
				randomSpeed(0.3, 0.2), 
				randomSpeedUp(0.45, 0.3), 
				randomSpeed(0.3, 0.2));
		}
	}
	
	public static void missileExplode(Level level, Vec3 pos, double radius, boolean fire) {
		radius *= 0.8;
		
		// Больше частиц дыма для более плавного эффекта
		int smokeCount = (int)(radius * 25) + 12;
		for (int i = 0; i < smokeCount; ++i) {
			// Случайное направление в сфере
			double angle = random.nextDouble() * Math.PI * 2;
			double pitch = (random.nextDouble() - 0.5) * Math.PI;
			double speed = (0.4 + random.nextDouble() * 0.6) * radius;
			
			level.addAlwaysVisibleParticle(ModParticles.LARGE_SMOKE_CLOUD.get(), 
				true, 
				pos.x + (random.nextDouble() - 0.5) * 0.4, 
				pos.y - 0.3 + (random.nextDouble() - 0.5) * 0.4,
				pos.z + (random.nextDouble() - 0.5) * 0.4, 
				Math.cos(angle) * Math.cos(pitch) * speed,
				Math.sin(pitch) * speed,
				Math.sin(angle) * Math.cos(pitch) * speed);
		}
		
		// ОСКОЛКИ - средние, быстрые (обломки ракеты)
		int shrapnelCount = (int)(radius * 18) + 12;
		for (int i = 0; i < shrapnelCount; ++i) {
			double angle = random.nextDouble() * Math.PI * 2;
			double pitch = (random.nextDouble() - 0.5) * Math.PI;
			double speed = 0.35 + random.nextDouble() * 0.6;
			
			level.addAlwaysVisibleParticle(ModParticles.SHRAPNEL.get(), 
				true, 
				pos.x, pos.y - 0.3, pos.z, 
				Math.cos(angle) * Math.cos(pitch) * speed,
				Math.sin(pitch) * speed,
				Math.sin(angle) * Math.cos(pitch) * speed);
		}
		
		if (fire) for (int i = 0; i < 24; ++i) {
			level.addAlwaysVisibleParticle(ModParticles.BIG_FLAME.get(), 
				true, pos.x, pos.y - 0.3, pos.z,
				randomSpeed(0.3, 0.2), 
				randomSpeedUp(0.45, 0.3), 
				randomSpeed(0.3, 0.2));
		}
	}
	
	public static void ballisticMissileExplosion(Level level, Vec3 pos, double radius, boolean fire) {
		// 1. MASSIVE EXPLOSION CORE - huge fireball
		for (int i = 0; i < 40; ++i) {
			double angle = level.random.nextDouble() * Math.PI * 2;
			double pitch = (level.random.nextDouble() - 0.5) * Math.PI;
			double speed = 0.8 + level.random.nextDouble() * 1.5;
			
			level.addAlwaysVisibleParticle(net.minecraft.core.particles.ParticleTypes.EXPLOSION_EMITTER,
				true, pos.x, pos.y, pos.z,
				Math.cos(angle) * Math.cos(pitch) * speed,
				Math.sin(pitch) * speed,
				Math.sin(angle) * Math.cos(pitch) * speed);
		}
		
		// 2. LARGE EXPLOSION PARTICLES - expanding fireball
		for (int i = 0; i < 80; ++i) {
			double angle = level.random.nextDouble() * Math.PI * 2;
			double pitch = (level.random.nextDouble() - 0.5) * Math.PI;
			double speed = 0.5 + level.random.nextDouble() * 1.0;
			
			level.addAlwaysVisibleParticle(net.minecraft.core.particles.ParticleTypes.EXPLOSION,
				true, pos.x, pos.y, pos.z,
				Math.cos(angle) * Math.cos(pitch) * speed,
				Math.sin(pitch) * speed,
				Math.sin(angle) * Math.cos(pitch) * speed);
		}
		
		// 3. SHOCKWAVE RINGS - multiple expanding rings
		for (int ring = 0; ring < 3; ring++) {
			double ringRadius = radius * (0.4 + ring * 0.5);
			for (int i = 0; i < 50; i++) {
				double angle = (i / 50.0) * Math.PI * 2;
				double x = pos.x + Math.cos(angle) * ringRadius;
				double z = pos.z + Math.sin(angle) * ringRadius;
				
				level.addAlwaysVisibleParticle(net.minecraft.core.particles.ParticleTypes.EXPLOSION,
					true, x, pos.y + ring * 0.3, z,
					Math.cos(angle) * 0.4,
					0.15 + ring * 0.05,
					Math.sin(angle) * 0.4);
			}
		}
		
		// 4. FLYING SPARKS AND DEBRIS - hot fragments
		for (int i = 0; i < 60; ++i) {
			double angle = level.random.nextDouble() * Math.PI * 2;
			double pitch = (level.random.nextDouble() - 0.3) * Math.PI * 0.8;
			double speed = 0.6 + level.random.nextDouble() * 2.0;
			
			// Lava particles
			level.addAlwaysVisibleParticle(net.minecraft.core.particles.ParticleTypes.LAVA,
				true, pos.x, pos.y, pos.z,
				Math.cos(angle) * Math.cos(pitch) * speed,
				Math.sin(pitch) * speed * 1.5,
				Math.sin(angle) * Math.cos(pitch) * speed);
			
			// Flame particles
			if (level.random.nextFloat() < 0.7f) {
				level.addAlwaysVisibleParticle(net.minecraft.core.particles.ParticleTypes.FLAME,
					true, pos.x, pos.y, pos.z,
					Math.cos(angle) * Math.cos(pitch) * speed * 0.9,
					Math.sin(pitch) * speed * 1.3,
					Math.sin(angle) * Math.cos(pitch) * speed * 0.9);
			}
		}
		
		// 5. MASSIVE SMOKE CLOUDS - thick black smoke
		double smokeRadius = radius * 1.5;
		for (double d = 0.5; d <= smokeRadius; d += 1.2) {
			for (int i = 0; i < 360; i += 30) {
				for (int j = -30; j <= 90; j += 35) {
					double angleRad = Math.toRadians(i);
					double pitchRad = Math.toRadians(j);
					
					level.addAlwaysVisibleParticle(ModParticles.LARGE_SMOKE_CLOUD.get(),
						true, pos.x, pos.y + 0.5, pos.z,
						Math.cos(angleRad) * Math.cos(pitchRad) * d * 0.2,
						Math.sin(pitchRad) * d * 0.2,
						Math.sin(angleRad) * Math.cos(pitchRad) * d * 0.2);
				}
			}
		}
		
		// 6. ADDITIONAL LARGE SMOKE - rising smoke column
		for (int i = 0; i < 50; ++i) {
			double offsetX = (level.random.nextDouble() - 0.5) * radius * 1.5;
			double offsetZ = (level.random.nextDouble() - 0.5) * radius * 1.5;
			double offsetY = level.random.nextDouble() * radius;
			
			level.addAlwaysVisibleParticle(net.minecraft.core.particles.ParticleTypes.LARGE_SMOKE,
				true, pos.x + offsetX, pos.y + offsetY, pos.z + offsetZ,
				randomSpeed(0.3, 0.2),
				randomSpeedUp(0.5, 0.3),
				randomSpeed(0.3, 0.2));
		}
		
		// 7. FIRE COLUMN - massive flames
		if (fire) {
			for (int i = 0; i < 40; ++i) {
				double offsetX = (level.random.nextDouble() - 0.5) * radius * 1.2;
				double offsetZ = (level.random.nextDouble() - 0.5) * radius * 1.2;
				double height = level.random.nextDouble() * radius * 0.8;
				
				level.addAlwaysVisibleParticle(ModParticles.BIG_FLAME.get(),
					true, pos.x + offsetX, pos.y + height, pos.z + offsetZ,
					randomSpeed(0.25, 0.2),
					randomSpeedUp(0.7, 0.5),
					randomSpeed(0.25, 0.2));
			}
			
			// Additional fire particles
			for (int i = 0; i < 30; ++i) {
				level.addAlwaysVisibleParticle(net.minecraft.core.particles.ParticleTypes.FLAME,
					true, pos.x, pos.y, pos.z,
					randomSpeed(0.5, 0.4),
					randomSpeedUp(1.0, 0.6),
					randomSpeed(0.5, 0.4));
			}
		}
		
		// 8. GROUND DUST RING - expanding dust wave
		for (int i = 0; i < 40; i++) {
			double angle = (i / 40.0) * Math.PI * 2;
			double dist = radius * 1.5;
			double x = pos.x + Math.cos(angle) * dist;
			double z = pos.z + Math.sin(angle) * dist;
			
			level.addAlwaysVisibleParticle(net.minecraft.core.particles.ParticleTypes.POOF,
				true, x, pos.y - 0.5, z,
				Math.cos(angle) * 0.5,
				0.3,
				Math.sin(angle) * 0.5);
		}
		
		// 9. CAMPFIRE SMOKE - additional thick smoke
		for (int i = 0; i < 30; ++i) {
			double offsetX = (level.random.nextDouble() - 0.5) * radius;
			double offsetZ = (level.random.nextDouble() - 0.5) * radius;
			
			level.addAlwaysVisibleParticle(net.minecraft.core.particles.ParticleTypes.CAMPFIRE_COSY_SMOKE,
				true, pos.x + offsetX, pos.y, pos.z + offsetZ,
				randomSpeed(0.2, 0.15),
				randomSpeedUp(0.4, 0.3),
				randomSpeed(0.2, 0.15));
		}
	}
	
	/**
	 * Spawns block particles under tank tracks when moving
	 */
	public static void tankTrackParticles(EntityVehicle vehicle) {
		if (!vehicle.isTank()) return;

		Vec3 motion = vehicle.getDeltaMovement();
		double speed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);

		// Инерция: плавно нарастаем/убываем сглаженную скорость
		double smoothSpeed = smoothSpeedMap.getOrDefault(vehicle.getId(), 0.0);
		smoothSpeed += (speed - smoothSpeed) * 0.12;
		smoothSpeedMap.put(vehicle.getId(), smoothSpeed);

		if (smoothSpeed < 0.003) {
			com.onewhohears.dscombat.client.renderer.TrackMarkManager.sealStrip(vehicle.getId(), true);
			com.onewhohears.dscombat.client.renderer.TrackMarkManager.sealStrip(vehicle.getId(), false);
			return;
		}

		com.onewhohears.dscombat.data.vehicle.TrackTextureData trackData = vehicle.getStats().getTrackTextureData();

		com.onewhohears.onewholibs.util.math.QuaternionF q = vehicle.getClientQ();
		Vec3 leftWorldPos  = com.onewhohears.onewholibs.util.math.UtilAngles.rotateVector(trackData.getLeftTrackPos(),  q).add(vehicle.position());
		Vec3 rightWorldPos = com.onewhohears.onewholibs.util.math.UtilAngles.rotateVector(trackData.getRightTrackPos(), q).add(vehicle.position());

		net.minecraft.world.level.block.state.BlockState leftBlock  = vehicle.level().getBlockState(net.minecraft.core.BlockPos.containing(leftWorldPos));
		net.minecraft.world.level.block.state.BlockState rightBlock = vehicle.level().getBlockState(net.minecraft.core.BlockPos.containing(rightWorldPos));

		// Следы гусениц — всегда, независимо от track_textures
		if (!leftBlock.isAir()) {
			com.onewhohears.dscombat.client.renderer.TrackMarkManager.addTrackPoint(
				vehicle.getId(), leftWorldPos, vehicle.getYRot(), true);
		} else {
			com.onewhohears.dscombat.client.renderer.TrackMarkManager.sealStrip(vehicle.getId(), true);
		}
		if (!rightBlock.isAir()) {
			com.onewhohears.dscombat.client.renderer.TrackMarkManager.addTrackPoint(
				vehicle.getId(), rightWorldPos, vehicle.getYRot(), false);
		} else {
			com.onewhohears.dscombat.client.renderer.TrackMarkManager.sealStrip(vehicle.getId(), false);
		}

		// Пыль — только если track_textures включены
		// ОТКЛЮЧЕНО: дым из-под гусениц танка
		/*
		if (!trackData.isEnabled()) return;

		// 1-2 частицы за тик, зависит от сглаженной скорости
		int count = smoothSpeed > 0.05 ? 2 : 1;

		Vec3 vehicleDir = vehicle.getLookAngle();
		Vec3 rightDir = new Vec3(-vehicleDir.z, 0, vehicleDir.x).normalize();

		if (!leftBlock.isAir()) {
			float[] col = dustColorForBlock(leftBlock);
			for (int i = 0; i < count; i++)
				spawnTrackParticle(vehicle.level(), leftWorldPos, motion, rightDir.scale(-1), smoothSpeed, col);
		}
		if (!rightBlock.isAir()) {
			float[] col = dustColorForBlock(rightBlock);
			for (int i = 0; i < count; i++)
				spawnTrackParticle(vehicle.level(), rightWorldPos, motion, rightDir, smoothSpeed, col);
		}
		*/
	}

	// Клиентское хранилище сглаженной скорости (entityId -> smoothSpeed)
	private static final java.util.Map<Integer, Double> smoothSpeedMap = new java.util.HashMap<>();

	/** Возвращает RGB пыли в зависимости от блока под гусеницей */
	private static float[] dustColorForBlock(net.minecraft.world.level.block.state.BlockState state) {
		net.minecraft.world.level.block.Block block = state.getBlock();
		if (block == net.minecraft.world.level.block.Blocks.SAND)
			return new float[]{0.93f, 0.84f, 0.62f};
		if (block == net.minecraft.world.level.block.Blocks.RED_SAND)
			return new float[]{0.85f, 0.52f, 0.28f};
		if (block == net.minecraft.world.level.block.Blocks.GRAVEL)
			return new float[]{0.60f, 0.58f, 0.56f};
		if (block == net.minecraft.world.level.block.Blocks.STONE
		 || block == net.minecraft.world.level.block.Blocks.COBBLESTONE
		 || block == net.minecraft.world.level.block.Blocks.STONE_BRICKS)
			return new float[]{0.55f, 0.55f, 0.55f};
		if (block == net.minecraft.world.level.block.Blocks.SNOW
		 || block == net.minecraft.world.level.block.Blocks.SNOW_BLOCK)
			return new float[]{0.92f, 0.95f, 0.98f};
		if (block == net.minecraft.world.level.block.Blocks.GRASS_BLOCK
		 || block == net.minecraft.world.level.block.Blocks.DIRT_PATH)
			return new float[]{0.62f, 0.52f, 0.35f};
		// дефолт — земля
		return new float[]{0.72f, 0.58f, 0.40f};
	}
	
	/**
	 * Spawns a single block particle at track position
	 * @param sideDir direction to spray particles (left or right from vehicle)
	 * @param speed vehicle speed for scaling particle velocity
	 */
	private static void spawnTrackParticle(Level level, Vec3 pos, Vec3 motion, Vec3 sideDir, double speed, float[] rgb) {
		try {
			double offsetX = (random.nextDouble() - 0.5) * 1.2;
			double offsetZ = (random.nextDouble() - 0.5) * 1.2;

			double speedScale = Math.min(speed * speed * 10.0, 1.0);

			double sidewaysFactor = (0.25 + random.nextDouble() * 0.35) * speedScale;
			double backwardFactor = (0.05 + random.nextDouble() * 0.08) * speedScale;
			double upwardVel      = (0.08 + random.nextDouble() * 0.12) * speedScale;

			Vec3 backward = motion.scale(-backwardFactor);
			Vec3 sideways = sideDir.scale(sidewaysFactor);

			double velX = backward.x + sideways.x + (random.nextDouble() - 0.5) * 0.04;
			double velY = upwardVel;
			double velZ = backward.z + sideways.z + (random.nextDouble() - 0.5) * 0.04;

			// Передаём velocity через статическое поле (читается в Provider до создания объекта)
			com.onewhohears.dscombat.client.particle.TankDustParticle.pendingVel = new double[]{velX, velY, velZ};

			// dx/dy/dz используются как RGB цвет
			level.addAlwaysVisibleParticle(
				com.onewhohears.dscombat.init.ModParticles.TANK_DUST.get(),
				true,
				pos.x + offsetX, pos.y + 0.2, pos.z + offsetZ,
				rgb[0], rgb[1], rgb[2]
			);
		} catch (Exception e) {
			// ignore
		}
	}
	
	/**
	 * Spawns track mark particles that stay on ground for 20 seconds
	 */
	private static void spawnTrackMark(Level level, Vec3 pos, net.minecraft.world.level.block.state.BlockState blockState) {
		// Use TrackMarkManager instead of particles
		// This will be called from tankTrackParticles
	}
	
}

