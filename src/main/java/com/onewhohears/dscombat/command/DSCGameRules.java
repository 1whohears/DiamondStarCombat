package com.onewhohears.dscombat.command;

import java.util.function.BiConsumer;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientSynchGameRules;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

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
	public static GameRules.Key<GameRules.IntegerValue> PLANE_SPEED_PERCENT;
	public static GameRules.Key<GameRules.IntegerValue> VEHICLE_ARMOR_PERCENT;
	public static GameRules.Key<GameRules.IntegerValue> BULLET_DAMAGE_VEHICLE_PER;
	public static GameRules.Key<GameRules.IntegerValue> EXPLO_DAMAGE_VEHICLE_PER;
	public static GameRules.Key<GameRules.IntegerValue> BULLET_DAMAGE_PLANE_PER;
	public static GameRules.Key<GameRules.IntegerValue> BULLET_DAMAGE_HELI_PER;
	public static GameRules.Key<GameRules.BooleanValue> DISABLE_3RD_PERSON_VEHICLE;
	// TODO 7.2 baby mode gamerule (arcade mode) for planes disabled by default
	
	public static void registerAll() {
		CONSUME_FULE = registerBoolean("consumeFuel", true, GameRules.Category.PLAYER);
		CONSUME_AMMO = registerBoolean("consumeAmmo", true, GameRules.Category.PLAYER);
		CONSUME_FLARES = registerBoolean("consumeFlares", true, GameRules.Category.PLAYER);
		FUEL_PER_OIL_BLOCK = registerInteger("fuelPerOilBlock", 50, GameRules.Category.PLAYER);
		DATA_LINK_ALWAYS_ON = registerBoolean("dataLinkAlwaysOn", false, GameRules.Category.PLAYER);
		DISABLE_ELYTRA_FLYING = registerBoolean("disableElytraFlying", false, GameRules.Category.PLAYER);
		ITEM_COOLDOWN_VEHICLE_FRESH = registerInteger("itemCooldownVehicleFresh", 0, GameRules.Category.PLAYER);
		ITEM_COOLDOWN_VEHICLE_SHOOT = registerInteger("itemCooldownVehicleShoot", 30, GameRules.Category.PLAYER);
		BROADCAST_MISSILE_HIT = registerBoolean("broadcastMissileHit", true, GameRules.Category.CHAT);
		BROADCAST_MISSILE_HIT_TEAM_ONLY = registerBoolean("broadcastMissileHitTeamOnly", false, GameRules.Category.CHAT);
		MOBS_USE_TURRETS = registerBoolean("mobsUseTurrets", true, GameRules.Category.MOBS);
		MOB_TURRET_VERTICAL_RANGE = registerInteger("mobTurretVerticalRange", 500, GameRules.Category.MOBS);
		MOBS_TICK_RADAR = registerBoolean("mobsTickRadar", true, GameRules.Category.MOBS);
		MOBS_RIDE_VEHICLES = registerBoolean("mobsRideVehicles", true, GameRules.Category.MOBS);
		PLANE_SPEED_PERCENT = registerInteger("planeSpeedPercent", 100, GameRules.Category.PLAYER);
		VEHICLE_ARMOR_PERCENT = registerInteger("vehicleArmorStrengthPercent", 100, GameRules.Category.PLAYER);
		BULLET_DAMAGE_VEHICLE_PER = registerInteger("bulletDamageVehiclePercent", 40, GameRules.Category.PLAYER);
		EXPLO_DAMAGE_VEHICLE_PER = registerInteger("explosionDamageVehiclePercent", 1000, GameRules.Category.PLAYER);
		BULLET_DAMAGE_PLANE_PER = registerInteger("bulletDamagePlanePercent", 20, GameRules.Category.PLAYER);
		BULLET_DAMAGE_HELI_PER = registerInteger("bulletDamageHeliPercent", 20, GameRules.Category.PLAYER);
		DISABLE_3RD_PERSON_VEHICLE = registerBoolean("disable3rdPersonVehicle", false, GameRules.Category.PLAYER, clientSyncListener());
	}
	
	private static BiConsumer<MinecraftServer, GameRules.BooleanValue> clientSyncListener() {
		return (server, value) -> PacketHandler.INSTANCE.send(
				PacketDistributor.ALL.noArg(), 
				new ToClientSynchGameRules(server));
	}
	
	public static GameRules.Key<GameRules.BooleanValue> registerBoolean(String name, boolean defaultValue) {
		return registerBoolean(name, defaultValue, GameRules.Category.MISC);
	}
	
	public static GameRules.Key<GameRules.BooleanValue> registerBoolean(String name, boolean defaultValue, GameRules.Category category) {
		return GameRules.register(name, category, GameRules.BooleanValue.create(defaultValue));
	}
	
	public static GameRules.Key<GameRules.BooleanValue> registerBoolean(String name, boolean defaultValue, GameRules.Category category, 
			BiConsumer<MinecraftServer, GameRules.BooleanValue> listener) {
		return GameRules.register(name, category, 
				GameRules.BooleanValue.create(defaultValue, listener));
	}
	
	public static GameRules.Key<GameRules.IntegerValue> registerInteger(String name, int defaultValue, GameRules.Category category) {
		return GameRules.register(name, category, GameRules.IntegerValue.create(defaultValue));
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
	
}
