package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public class EntityCar extends EntityAircraft {

	public EntityCar(EntityType<? extends EntityAircraft> entity, Level level, ResourceLocation texture,
			RegistryObject<SoundEvent> engineSound, RegistryObject<Item> item) {
		super(entity, level, texture, engineSound, item);
		// TODO make a functional car/tank/air defense
	}

	@Override
	public Vec3 getThrustForce(Quaternion q) {
		return Vec3.ZERO;
	}

}
