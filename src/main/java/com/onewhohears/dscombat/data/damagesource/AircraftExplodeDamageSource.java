package com.onewhohears.dscombat.data.damagesource;

import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class AircraftExplodeDamageSource extends DamageSource {
	
	public final EntityAircraft aircraft;
	
	public AircraftExplodeDamageSource(String type, EntityAircraft aircraft) {
		super(type);
		setExplosion();
		this.aircraft = aircraft;
	}
	
	public static AircraftExplodeDamageSource fall(EntityAircraft aircraft) {
		return new AircraftExplodeDamageSource("dscombat.plane_crash_fall", aircraft);
	}
	
	public static AircraftExplodeDamageSource collide(EntityAircraft aircraft) {
		return new AircraftExplodeDamageSource("dscombat.plane_crash_collide", aircraft);
	}
	
	public Component getLocalizedDeathMessage(LivingEntity killed) {
		Entity killer = aircraft.getControllingPassenger();
		String s = "death.attack."+msgId;
		if (killer == null) {
			return Component.translatable(s, killed.getDisplayName());
		} else if (killed.equals(killer)) {
			return Component.translatable(s+".self", killed.getDisplayName());
		} else {
			return Component.translatable(s+".player", killed.getDisplayName(), killer.getDisplayName());
		}
	}

}
