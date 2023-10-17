package com.onewhohears.dscombat.command;

import java.lang.reflect.Method;

import net.minecraft.world.level.GameRules;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class DSCGameRules {
	
	public static GameRules.Key<GameRules.BooleanValue> CONSUME_FULE;
	public static GameRules.Key<GameRules.BooleanValue> CONSUME_AMMO;
	public static GameRules.Key<GameRules.BooleanValue> CONSUME_FLARES;
	public static GameRules.Key<GameRules.BooleanValue> DATA_LINK_ALWAYS_ON;
	public static GameRules.Key<GameRules.BooleanValue> DISABLE_ELYTRA_FLYING;
	public static GameRules.Key<GameRules.IntegerValue> ITEM_COOLDOWN_VEHICLE_FRESH;
	public static GameRules.Key<GameRules.IntegerValue> ITEM_COOLDOWN_VEHICLE_SHOOT;
	// TODO 7.2 baby mode for planes disabled by default
	
	public static void registerAll() {
		CONSUME_FULE = registerBoolean("consumeFuel", true, GameRules.Category.PLAYER);
		CONSUME_AMMO = registerBoolean("consumeAmmo", true, GameRules.Category.PLAYER);
		CONSUME_FLARES = registerBoolean("consumeFlares", true, GameRules.Category.PLAYER);
		DATA_LINK_ALWAYS_ON = registerBoolean("dataLinkAlwaysOn", false, GameRules.Category.PLAYER);
		DISABLE_ELYTRA_FLYING = registerBoolean("disableElytraFlying", false, GameRules.Category.PLAYER);
		ITEM_COOLDOWN_VEHICLE_FRESH = registerInteger("itemCooldownVehicleFresh", 0, GameRules.Category.PLAYER);
		ITEM_COOLDOWN_VEHICLE_SHOOT = registerInteger("itemCooldownVehicleShoot", 30, GameRules.Category.PLAYER);
	}
	
	public static GameRules.Key<GameRules.BooleanValue> registerBoolean(String name, boolean defaultValue) {
		return registerBoolean(name, defaultValue, GameRules.Category.MISC);
	}
	
	public static GameRules.Key<GameRules.BooleanValue> registerBoolean(String name, boolean defaultValue, GameRules.Category category) {
		return GameRules.register(name, category, createBooleanType(defaultValue));
	}
	
	public static GameRules.Key<GameRules.IntegerValue> registerInteger(String name, int defaultValue, GameRules.Category category) {
		return GameRules.register(name, category, createIntType(defaultValue));
	}
	
	@SuppressWarnings("unchecked")
	public static GameRules.Type<GameRules.BooleanValue> createBooleanType(boolean defaultValue) {
		try {
			Method m = ObfuscationReflectionHelper.findMethod(GameRules.BooleanValue.class, "m_46250_", boolean.class);
			m.setAccessible(true);
			return (GameRules.Type<GameRules.BooleanValue>) m.invoke(null, defaultValue);
		} catch (Exception e) {
			System.out.println("FAILED TO CREATE GAMERULE");
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static GameRules.Type<GameRules.IntegerValue> createIntType(int defaultValue) {
		try {
			Method m = ObfuscationReflectionHelper.findMethod(GameRules.IntegerValue.class, "m_46252_", int.class);
			m.setAccessible(true);
			return (GameRules.Type<GameRules.IntegerValue>) m.invoke(null, defaultValue);
		} catch (Exception e) {
			System.out.println("FAILED TO CREATE GAMERULE");
			e.printStackTrace();
		}
		return null;
	}
	
}
