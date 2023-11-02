package com.onewhohears.dscombat.data.weapon;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class WeaponShootParameters {
	
	public final Level level;
	public final Entity owner;
	public final Vec3 pos, direction;
	public final EntityVehicle vehicle;
	public final boolean ignoreRecoil;
	public final boolean isTurret;
	
	public WeaponShootParameters(Level level, Entity owner, Vec3 pos, Vec3 direction, 
			@Nullable EntityVehicle vehicle, boolean ignoreRecoil, boolean isTurret) {
		this.level = level;
		this.owner = owner;
		this.pos = pos;
		this.direction = direction;
		this.vehicle = vehicle;
		this.ignoreRecoil = ignoreRecoil;
		this.isTurret = isTurret;
	}
	
}
