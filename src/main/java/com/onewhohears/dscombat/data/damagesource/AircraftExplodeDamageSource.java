package com.onewhohears.dscombat.data.damagesource;

import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.world.damagesource.EntityDamageSource;

public class AircraftExplodeDamageSource extends EntityDamageSource {

	public AircraftExplodeDamageSource(String type, EntityAircraft aircraft) {
		super(type, aircraft);
		setExplosion();
	}
	
	public static AircraftExplodeDamageSource fall(EntityAircraft aircraft) {
		return new AircraftExplodeDamageSource("dscombat.plane_crash_fall", aircraft);
	}
	
	public static AircraftExplodeDamageSource collide(EntityAircraft aircraft) {
		return new AircraftExplodeDamageSource("dscombat.plane_crash_collide", aircraft);
	}

}
