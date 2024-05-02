package com.onewhohears.dscombat.entity.damagesource;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.UtilMCText;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class AircraftDamageSource extends DamageSource {
	
	public final EntityVehicle aircraft;
	
	public AircraftDamageSource(String type, EntityVehicle aircraft) {
		super(type);
		this.aircraft = aircraft;
	}
	
	public static DamageSource roadKill(EntityVehicle aircraft) {
		return new AircraftDamageSource(getRoadKillDeath(), aircraft);
	}
	
	public static DamageSource fall(EntityVehicle aircraft) {
		return new AircraftDamageSource(getFallDeath(), aircraft).setExplosion();
	}
	
	public static DamageSource collide(EntityVehicle aircraft) {
		return new AircraftDamageSource(getCollideDeath(), aircraft).setExplosion();
	}
	
	public static final String[] roadKillDeaths = {"roadkill1","roadkill2"};
	public static final String[] crashDeaths = {"plane_crash1","plane_crash2"};
	public static final String[] fallCrashDeaths = {"plane_crash_fall1","plane_crash_fall2","plane_crash_fall3"};
	public static final String[] wallCrashDeaths = {"plane_crash_collide1","plane_crash_collide2","plane_crash_collide3"};
	
	public static String getRoadKillDeath() {
		return UtilParse.getRandomString(crashDeaths, roadKillDeaths, wallCrashDeaths);
	}
	
	public static String getFallDeath() {
		return UtilParse.getRandomString(crashDeaths, fallCrashDeaths);
	}
	
	public static String getCollideDeath() {
		return UtilParse.getRandomString(crashDeaths, wallCrashDeaths);
	}
	
	@Override
	public Component getLocalizedDeathMessage(LivingEntity killed) {
		Entity killer = aircraft.getControllingPassenger();
		String s = "death.attack."+DSCombatMod.MODID+"."+msgId;
		if (killer == null) {
			return UtilMCText.translatable(s, killed.getDisplayName());
		} else if (killed.equals(killer)) {
			return UtilMCText.translatable(s+".self", killed.getDisplayName());
		} else {
			return UtilMCText.translatable(s+".player", killed.getDisplayName(), killer.getDisplayName());
		}
	}

}
