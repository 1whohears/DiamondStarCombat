package com.onewhohears.dscombat.command;

import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.onewholibs.common.command.CustomGameRules;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public class DSCGameRules {
	
	public static GameRules.Key<GameRules.BooleanValue> CONSUME_FULE;
	public static GameRules.Key<GameRules.BooleanValue> CONSUME_AMMO;
	public static GameRules.Key<GameRules.BooleanValue> CONSUME_FLARES;
	public static GameRules.Key<GameRules.IntegerValue> FUEL_PER_OIL_BLOCK;
	public static GameRules.Key<GameRules.BooleanValue> DATA_LINK_ALWAYS_ON;
	public static GameRules.Key<GameRules.BooleanValue> DISABLE_ELYTRA_FLYING;
	public static GameRules.Key<GameRules.IntegerValue> ITEM_COOLDOWN_VEHICLE_FRESH;
	public static GameRules.Key<GameRules.IntegerValue> ITEM_COOLDOWN_VEHICLE_SHOOT;
	public static GameRules.Key<GameRules.BooleanValue> BROADCAST_MISSILE_HIT;
	public static GameRules.Key<GameRules.BooleanValue> BROADCAST_MISSILE_HIT_TEAM_ONLY;
	public static GameRules.Key<GameRules.BooleanValue> MOBS_USE_TURRETS;
	public static GameRules.Key<GameRules.IntegerValue> MOB_TURRET_VERTICAL_RANGE;
	public static GameRules.Key<GameRules.BooleanValue> MOBS_TICK_RADAR;
	public static GameRules.Key<GameRules.BooleanValue> MOBS_RIDE_VEHICLES;
	public static GameRules.Key<GameRules.IntegerValue> VEHICLE_ARMOR_PERCENT;
	public static GameRules.Key<GameRules.IntegerValue> BULLET_DAMAGE_VEHICLE_PER;
	public static GameRules.Key<GameRules.IntegerValue> EXPLO_DAMAGE_VEHICLE_PER;
	public static GameRules.Key<GameRules.IntegerValue> BULLET_DAMAGE_PLANE_PER;
	public static GameRules.Key<GameRules.IntegerValue> BULLET_DAMAGE_HELI_PER;
	public static GameRules.Key<GameRules.BooleanValue> DISABLE_3RD_PERSON_VEHICLE;
	public static GameRules.Key<GameRules.BooleanValue> PLANE_ARCADE_MODE;
	public static GameRules.Key<GameRules.IntegerValue> REMOVE_DEAD_VEHICLES_TIME;
	public static GameRules.Key<GameRules.BooleanValue> WEAPONS_BREAK_BLOCKS;
	public static GameRules.Key<GameRules.BooleanValue> VEHICLE_TRAMPLE;
	public static GameRules.Key<GameRules.BooleanValue> FORCE_PUBLIC_PERM;
	public static GameRules.Key<GameRules.BooleanValue> CAN_ITEM_WHILE_MOVING;
	
	public static void registerAll() {
		CONSUME_FULE = CustomGameRules.registerBoolean("dscombat:consumeFuel", true, GameRules.Category.PLAYER);
		CONSUME_AMMO = CustomGameRules.registerBoolean("dscombat:consumeAmmo", true, GameRules.Category.PLAYER);
		CONSUME_FLARES = CustomGameRules.registerBoolean("dscombat:consumeFlares", true, GameRules.Category.PLAYER);
		FUEL_PER_OIL_BLOCK = CustomGameRules.registerInteger("dscombat:fuelPerOilBlock", 50, GameRules.Category.PLAYER);
		DATA_LINK_ALWAYS_ON = CustomGameRules.registerBoolean("dscombat:dataLinkAlwaysOn", false, GameRules.Category.PLAYER);
		DISABLE_ELYTRA_FLYING = CustomGameRules.registerBoolean("dscombat:disableElytraFlying", false, GameRules.Category.PLAYER);
		ITEM_COOLDOWN_VEHICLE_FRESH = CustomGameRules.registerInteger("dscombat:itemCooldownVehicleFresh", 0, GameRules.Category.PLAYER);
		ITEM_COOLDOWN_VEHICLE_SHOOT = CustomGameRules.registerInteger("dscombat:itemCooldownVehicleShoot", 30, GameRules.Category.PLAYER);
		BROADCAST_MISSILE_HIT = CustomGameRules.registerBoolean("dscombat:broadcastMissileHit", true, GameRules.Category.CHAT);
		BROADCAST_MISSILE_HIT_TEAM_ONLY = CustomGameRules.registerBoolean("dscombat:broadcastMissileHitTeamOnly", false, GameRules.Category.CHAT);
		MOBS_USE_TURRETS = CustomGameRules.registerBoolean("dscombat:mobsUseTurrets", true, GameRules.Category.MOBS);
		MOB_TURRET_VERTICAL_RANGE = CustomGameRules.registerInteger("dscombat:mobTurretVerticalRange", 500, GameRules.Category.MOBS);
		MOBS_TICK_RADAR = CustomGameRules.registerBoolean("dscombat:mobsTickRadar", true, GameRules.Category.MOBS);
		MOBS_RIDE_VEHICLES = CustomGameRules.registerBoolean("dscombat:mobsRideVehicles", true, GameRules.Category.MOBS);
		VEHICLE_ARMOR_PERCENT = CustomGameRules.registerInteger("dscombat:vehicleArmorStrengthPercent", 100, GameRules.Category.PLAYER);
		BULLET_DAMAGE_VEHICLE_PER = CustomGameRules.registerInteger("dscombat:bulletDamageVehiclePercent", 50, GameRules.Category.PLAYER);
		EXPLO_DAMAGE_VEHICLE_PER = CustomGameRules.registerInteger("dscombat:explosionDamageVehiclePercent", 300, GameRules.Category.PLAYER);
		BULLET_DAMAGE_PLANE_PER = CustomGameRules.registerInteger("dscombat:bulletDamagePlanePercent", 100, GameRules.Category.PLAYER);
		BULLET_DAMAGE_HELI_PER = CustomGameRules.registerInteger("dscombat:bulletDamageHeliPercent", 100, GameRules.Category.PLAYER);
		DISABLE_3RD_PERSON_VEHICLE = CustomGameRules.registerSyncBoolean("dscombat:disable3rdPersonVehicle", false, GameRules.Category.PLAYER);
		PLANE_ARCADE_MODE = CustomGameRules.registerSyncBoolean("dscombat:planeArcadeMode", false, GameRules.Category.PLAYER);
		REMOVE_DEAD_VEHICLES_TIME = CustomGameRules.registerInteger("dscombat:removeDeadVehiclesTime", -1, GameRules.Category.PLAYER);
		WEAPONS_BREAK_BLOCKS = CustomGameRules.registerSyncBoolean("dscombat:weaponsBreakBlocks", true, GameRules.Category.PLAYER);
		VEHICLE_TRAMPLE = CustomGameRules.registerSyncBoolean("dscombat:vehicleTrample", true, GameRules.Category.PLAYER);
		FORCE_PUBLIC_PERM = CustomGameRules.registerSyncBoolean("dscombat:vehicleForcePublicPerm", false, GameRules.Category.PLAYER);
		CAN_ITEM_WHILE_MOVING = CustomGameRules.registerBoolean("dscombat:canVehicleItemWhileMoving", false, GameRules.Category.PLAYER);
	}

	public static boolean isPlaneArcadeMode(Level level) {
		if (level.isClientSide()) return DSCClientInputs.planeArcadePhysicsMode;
		return level.getGameRules().getBoolean(PLANE_ARCADE_MODE);
	}
	
	public static float getVehicleArmorStrengthFactor(Level level) {
		return level.getGameRules().getInt(VEHICLE_ARMOR_PERCENT) * 0.01f;
	}
	
	public static float getBulletDamageVehicleFactor(Level level) {
		return level.getGameRules().getInt(BULLET_DAMAGE_VEHICLE_PER) * 0.01f;
	}
	
	public static float getExplodeDamagerVehicleFactor(Level level) {
		return level.getGameRules().getInt(EXPLO_DAMAGE_VEHICLE_PER) * 0.01f;
	}
	
	public static float getBulletDamagePlaneFactor(Level level) {
		return level.getGameRules().getInt(BULLET_DAMAGE_PLANE_PER) * 0.01f;
	}
	
	public static float getBulletDamageHeliFactor(Level level) {
		return level.getGameRules().getInt(BULLET_DAMAGE_HELI_PER) * 0.01f;
	}
	
	public static int getFuelPerOilBlock(Level level) {
		return level.getGameRules().getInt(FUEL_PER_OIL_BLOCK);
	}

	public static boolean isForcePublicPerm(Level level) {
		return level.getGameRules().getBoolean(FORCE_PUBLIC_PERM);
	}
	
}
