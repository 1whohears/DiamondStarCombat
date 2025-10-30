package com.onewhohears.dscombat.entity.damagesource;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModDamageTypes;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleDamageSource extends DamageSource {
	
	public final EntityVehicle aircraft;
	
	public VehicleDamageSource(String type, EntityVehicle aircraft, ResourceKey<DamageType> damageType) {
		super(ModDamageTypes.getDamageTypeHolder(aircraft.level(), damageType));
		this.aircraft = aircraft;
	}
	
	public static DamageSource roadKill(EntityVehicle aircraft) {
		return new VehicleDamageSource(getRoadKillDeath(), aircraft, ModDamageTypes.VEHICLE_ROAD_KILL);
	}
	
	public static DamageSource fall(EntityVehicle aircraft) {
		return new VehicleDamageSource(getFallDeath(), aircraft, ModDamageTypes.VEHICLE_FALL);
	}
	
	public static DamageSource collide(EntityVehicle aircraft) {
		return new VehicleDamageSource(getCollideDeath(), aircraft, ModDamageTypes.VEHICLE_COLLIDE);
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
	public @NotNull Component getLocalizedDeathMessage(LivingEntity killed) {
		Entity killer = aircraft.getControllingPassenger();
		String s = "death.attack."+DSCombatMod.MODID+"."+getMsgId();
		if (killer == null) {
			return UtilMCText.translatable(s, killed.getDisplayName());
		} else if (killed.equals(killer)) {
			return UtilMCText.translatable(s+".self", killed.getDisplayName());
		} else {
			return UtilMCText.translatable(s+".player", killed.getDisplayName(), killer.getDisplayName());
		}
	}

	@NotNull
	@Override
	public Entity getDirectEntity() {
		return aircraft;
	}

	@Nullable
	@Override
	public Entity getEntity() {
		return aircraft.getControllingPassenger();
	}

}
