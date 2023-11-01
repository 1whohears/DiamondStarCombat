package com.onewhohears.dscombat.command;

import net.minecraft.world.level.GameRules;

public class DSCGameRules {
	
	public static GameRules.Key<GameRules.BooleanValue> CONSUME_FULE;
	public static GameRules.Key<GameRules.BooleanValue> CONSUME_AMMO;
	public static GameRules.Key<GameRules.BooleanValue> CONSUME_FLARES;
	public static GameRules.Key<GameRules.BooleanValue> DATA_LINK_ALWAYS_ON;
	public static GameRules.Key<GameRules.BooleanValue> DISABLE_ELYTRA_FLYING;
	public static GameRules.Key<GameRules.IntegerValue> ITEM_COOLDOWN_VEHICLE_FRESH;
	public static GameRules.Key<GameRules.IntegerValue> ITEM_COOLDOWN_VEHICLE_SHOOT;
	public static GameRules.Key<GameRules.BooleanValue> WEAPON_HIT_FEEDBACK;
	public static GameRules.Key<GameRules.BooleanValue> BROADCAST_MISSILE_HIT;
	public static GameRules.Key<GameRules.BooleanValue> BROADCAST_MISSILE_HIT_TEAM_ONLY;
	public static GameRules.Key<GameRules.BooleanValue> MOBS_USE_TURRETS;
	public static GameRules.Key<GameRules.IntegerValue> MOB_TURRET_VERTICAL_RANGE;
	public static GameRules.Key<GameRules.BooleanValue> MOBS_TICK_RADAR;
	public static GameRules.Key<GameRules.BooleanValue> MOBS_RIDE_VEHICLES;
	public static GameRules.Key<GameRules.IntegerValue> PLANE_SPEED_PERCENT;
	// TODO 7.1 baby mode gamerule (arcade mode) for planes disabled by default
	
	public static void registerAll() {
		CONSUME_FULE = registerBoolean("consumeFuel", true, GameRules.Category.PLAYER);
		CONSUME_AMMO = registerBoolean("consumeAmmo", true, GameRules.Category.PLAYER);
		CONSUME_FLARES = registerBoolean("consumeFlares", true, GameRules.Category.PLAYER);
		DATA_LINK_ALWAYS_ON = registerBoolean("dataLinkAlwaysOn", false, GameRules.Category.PLAYER);
		DISABLE_ELYTRA_FLYING = registerBoolean("disableElytraFlying", false, GameRules.Category.PLAYER);
		ITEM_COOLDOWN_VEHICLE_FRESH = registerInteger("itemCooldownVehicleFresh", 0, GameRules.Category.PLAYER);
		ITEM_COOLDOWN_VEHICLE_SHOOT = registerInteger("itemCooldownVehicleShoot", 30, GameRules.Category.PLAYER);
		WEAPON_HIT_FEEDBACK = registerBoolean("weaponHitFeedback", true, GameRules.Category.PLAYER);
		BROADCAST_MISSILE_HIT = registerBoolean("broadcastMissileHit", true, GameRules.Category.CHAT);
		BROADCAST_MISSILE_HIT_TEAM_ONLY = registerBoolean("broadcastMissileHitTeamOnly", false, GameRules.Category.CHAT);
		MOBS_USE_TURRETS = registerBoolean("mobsUseTurrets", true, GameRules.Category.MOBS);
		MOB_TURRET_VERTICAL_RANGE = registerInteger("mobTurretVerticalRange", 300, GameRules.Category.MOBS);
		MOBS_TICK_RADAR = registerBoolean("mobsTickRadar", true, GameRules.Category.MOBS);
		MOBS_RIDE_VEHICLES = registerBoolean("mobsRideVehicles", true, GameRules.Category.MOBS);
		PLANE_SPEED_PERCENT = registerInteger("planeSpeedPercent", 100, GameRules.Category.PLAYER);
	}
	
	public static GameRules.Key<GameRules.BooleanValue> registerBoolean(String name, boolean defaultValue) {
		return registerBoolean(name, defaultValue, GameRules.Category.MISC);
	}
	
	public static GameRules.Key<GameRules.BooleanValue> registerBoolean(String name, boolean defaultValue, GameRules.Category category) {
		return GameRules.register(name, category, GameRules.BooleanValue.create(defaultValue));
	}
	
	public static GameRules.Key<GameRules.IntegerValue> registerInteger(String name, int defaultValue, GameRules.Category category) {
		return GameRules.register(name, category, GameRules.IntegerValue.create(defaultValue));
	}
	
}
