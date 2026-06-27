package com.onewhohears.dscombat.client.renderer;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

/**
 * Рендерит эффект теплового искажения воздуха от форсажа
 */
public class HeatHazeRenderer {
	
	/**
	 * Создает частицы теплового искажения вокруг пламени
	 */
	public static void renderHeatHaze(EntityVehicle entity, Vec3 flamePosition, float intensity) {
		if (entity.level().isClientSide()) {
			// Используем random из level вместо entity
			RandomSource random = entity.level().random;
			
			// Количество частиц зависит от интенсивности
			int particleCount = (int)(intensity * 3);
			
			for (int i = 0; i < particleCount; i++) {
				// Случайное смещение вокруг пламени
				double offsetX = (random.nextDouble() - 0.5) * 1.5;
				double offsetY = (random.nextDouble() - 0.5) * 1.5;
				double offsetZ = (random.nextDouble() - 0.5) * 0.5;
				
				double x = flamePosition.x + offsetX;
				double y = flamePosition.y + offsetY;
				double z = flamePosition.z + offsetZ;
				
				// Медленное движение вверх и в стороны
				double velX = (random.nextDouble() - 0.5) * 0.02;
				double velY = random.nextDouble() * 0.05;
				double velZ = (random.nextDouble() - 0.5) * 0.02;
				
				// Используем частицы дыма или портала для эффекта искажения
				entity.level().addParticle(
					ParticleTypes.CAMPFIRE_COSY_SMOKE, // Или PORTAL для более заметного эффекта
					x, y, z,
					velX, velY, velZ
				);
			}
		}
	}
	
	/**
	 * Вычисляет интенсивность теплового искажения на основе скорости
	 */
	public static float calculateIntensity(EntityVehicle entity) {
		float speed = (float) entity.getDeltaMovement().length();
		// Интенсивность от 0 до 1
		return Math.min(speed * 0.3f, 1.0f);
	}
}
