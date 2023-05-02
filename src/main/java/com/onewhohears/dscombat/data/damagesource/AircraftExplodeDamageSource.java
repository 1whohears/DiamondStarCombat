package com.onewhohears.dscombat.data.damagesource;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.UtilParse;

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
		return new AircraftExplodeDamageSource(getFallDeath(), aircraft);
	}
	
	public static AircraftExplodeDamageSource collide(EntityAircraft aircraft) {
		return new AircraftExplodeDamageSource(getCollideDeath(), aircraft);
	}
	
	public static final String[] crashDeaths = {"plane_crash1","plane_crash2"};
	public static final String[] fallDeaths = {"plane_crash_fall1","plane_crash_fall2","plane_crash_fall3"};
	public static final String[] collideDeaths = {"plane_crash_collide1","plane_crash_collide2","plane_crash_collide3"};
	
	public static String getFallDeath() {
		return UtilParse.getRandomString(crashDeaths, fallDeaths);
	}
	
	public static String getCollideDeath() {
		return UtilParse.getRandomString(crashDeaths, collideDeaths);
	}
	
	public Component getLocalizedDeathMessage(LivingEntity killed) {
		Entity killer = aircraft.getControllingPassenger();
		String s = "death.attack."+DSCombatMod.MODID+"."+msgId;
		if (killer == null) {
			return Component.translatable(s, killed.getDisplayName());
		} else if (killed.equals(killer)) {
			return Component.translatable(s+".self", killed.getDisplayName());
		} else {
			return Component.translatable(s+".player", killed.getDisplayName(), killer.getDisplayName());
		}
	}

}
