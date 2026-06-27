package com.onewhohears.dscombat.util;

import com.onewhohears.dscombat.data.weapon.MuzzleSmokeData;
import com.onewhohears.dscombat.entity.weapon.EntityMuzzleFlash;
import com.onewhohears.dscombat.init.ModEntities;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Утилита для создания 3D вспышек дульного огня
 */
public class UtilMuzzleFlash {
	
	/**
	 * Создает entity вспышки дульного огня в мире
	 * 
	 * @param level мир
	 * @param position позиция вспышки
	 * @param direction направление вспышки (нормализованный вектор)
	 * @param data данные о вспышке из конфигурации оружия
	 */
	public static void spawnMuzzleFlash(Level level, Vec3 position, Vec3 direction, MuzzleSmokeData data) {
		if (!level.isClientSide) {
			System.out.println("ERROR: spawnMuzzleFlash called on server side!");
			return;
		}
		
		System.out.println("SPAWNING MUZZLE FLASH: pos=" + position + ", dir=" + direction + ", scale=" + data.flashScale + ", lifetime=" + data.flashLifetime);
		
		EntityMuzzleFlash flash = new EntityMuzzleFlash(ModEntities.MUZZLE_FLASH.get(), level);
		flash.setPos(position);
		flash.setShootDirection(direction);
		flash.setScale(data.flashScale);
		flash.setMaxLifetime(data.flashLifetime);
		level.addFreshEntity(flash);
		
		System.out.println("MUZZLE FLASH ENTITY CREATED: " + flash + ", id=" + flash.getId());
	}
	
	/**
	 * Создает вспышку с параметрами по умолчанию
	 */
	public static void spawnMuzzleFlash(Level level, Vec3 position, Vec3 direction) {
		spawnMuzzleFlash(level, position, direction, 
			new MuzzleSmokeData(Vec3.ZERO, 1.0f, 1.0f, 1.0f, 5));
	}
}
