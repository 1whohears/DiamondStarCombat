package com.onewhohears.dscombat;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
	
	public static class Client {
		
		public final ForgeConfigSpec.DoubleValue mouseModeMaxRadius;
		public final ForgeConfigSpec.DoubleValue mouseStickDeadzoneRadius;
		public final ForgeConfigSpec.DoubleValue mouseYReturnRate;
		public final ForgeConfigSpec.DoubleValue mouseXReturnRate;
		public final ForgeConfigSpec.BooleanValue invertY;
		public final ForgeConfigSpec.BooleanValue cameraTurnRelativeToVehicle;
		public final ForgeConfigSpec.BooleanValue customDismount;
		public final ForgeConfigSpec.BooleanValue debugMode;
		public final ForgeConfigSpec.DoubleValue rwrWarningVol;
		public final ForgeConfigSpec.DoubleValue missileWarningVol;
		public final ForgeConfigSpec.DoubleValue irTargetToneVol;
		
		public Client(ForgeConfigSpec.Builder builder) {
			mouseModeMaxRadius = builder
					.comment("Only for vehicles in Mouse Mode. How far your mouse must move from rest to get a maximum angle.")
					.defineInRange("mouseModeMaxRadius", 400d, 0, 10000d);
			mouseStickDeadzoneRadius = builder
					.comment("Only for vehicles in Mouse Mode. How far your mouse must move from rest to register an input.")
					.defineInRange("mouseStickDeadzoneRadius", 0d, 0, 1000d);
			mouseYReturnRate = builder
					.comment("Speed the control stick vertically snaps back to rest when mouse isn't moving.")
					.defineInRange("mouseYReturnRate", 20d, 0, 100d);
			mouseXReturnRate = builder
					.comment("Speed the control stick horizontally snaps back to rest when mouse isn't moving.")
					.defineInRange("mouseXReturnRate", 40d, 0, 100d);
			invertY = builder
					.comment("Invert vertical inputs.")
					.define("invertY", false);
			cameraTurnRelativeToVehicle = builder
					.comment("If enabled, turning your player head may feel more natural.")
					.define("cameraTurnRelativeToVehicle", true);
			customDismount = builder
					.comment("If enabled, your sneak key binding becomes Special2, and Special2 binding becomes dismount.")
					.define("customDismount", true);
			debugMode = builder
					.comment("Stats for nerds.")
					.define("debugMode", false);
			rwrWarningVol = builder
					.comment("RWR Warning Sound Volume")
					.defineInRange("rwrWarningVol", 1, 0, 10d);
			missileWarningVol = builder
					.comment("Missile Warning Sound Volume")
					.defineInRange("missileWarningVol", 1, 0, 10d);
			irTargetToneVol = builder
					.comment("IR Target Found Sound Volume")
					.defineInRange("irTargetToneVol", 0.5d, 0, 10d);
		}
		
	}
	
	public static class Common {
		
		public final ForgeConfigSpec.IntValue maxBlockCheckDepth;
		public final ForgeConfigSpec.DoubleValue freshEntityToItemCooldown;
		public final ForgeConfigSpec.DoubleValue usedWeaponToItemCooldown;
		public final ForgeConfigSpec.BooleanValue autoDataLink;
		// TODO 7.2 baby mode for planes disabled by default
		/**
		 * classname
		 */
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> radarVehicles;
		/**
		 * classname
		 */
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> radarMobs;
		/**
		 * entity_id/area
		 */
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> radarCrossSecAreas;
		/**
		 * classname/heat
		 */
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> irEntities;
		/**
		 * entity_id/heat
		 */
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> specificEntityHeat;
		
		public Common(ForgeConfigSpec.Builder builder) {
			maxBlockCheckDepth = builder
					.comment("The number of blocks between 2 entities to check if they can see eachother.")
					.defineInRange("maxBlockCheckDepth", 250, 10, 400);
			autoDataLink = builder
					.comment("All vehicles will behave as if they have datalink even if they don't have the module.")
					.define("autoDataLink", false);
			freshEntityToItemCooldown = builder
					.comment("Seconds before a fresh vehicle entity can become an item.")
					.defineInRange("freshEntityToItemCooldown", 30.0, 0, 600.0);
			usedWeaponToItemCooldown = builder
					.comment("Seconds before a vehicle entity that used a weapon can become an item.")
					.defineInRange("usedWeaponToItemCooldown", 30.0, 0, 600.0);
			radarVehicles = builder
					.defineList("radarVehicles", 
					Arrays.asList(
							"net.minecraft.world.entity.vehicle.Boat",
							"net.minecraft.world.entity.vehicle.AbstractMinecart",
							"com.onewhohears.dscombat.entity.EntityParachute",
							"xyz.przemyk.simpleplanes.entities.PlaneEntity"), 
					entry -> true);
			radarMobs = builder.defineList("radarMobs", 
					Arrays.asList(
							"net.minecraft.world.entity.Mob"), 
					entry -> true);
			radarCrossSecAreas = builder
					.comment("smaller values means its harder for a radar to see."
							+ " most radars have a sensitivity of at least 0.5m^2."
							+ " [entity_id]/[cross sectional area override (float)]")
					.defineList("specificEntityHeat", 
						Arrays.asList(
								"minecraft:boat/1.0f",
								"minecraft:minecart/1.0f",
								"dscombat:parachute/1.0f"), 
						entry -> true);
			irEntities = builder
				.comment("entities that ir missiles will target [path to entity class]/[default heat value (float)]")
				.defineList("irEntities", 
					Arrays.asList(
							"net.minecraft.world.entity.player.Player/0.5f",
							"net.minecraft.world.entity.Mob/0.4f",
							"net.minecraft.world.entity.projectile.FireworkRocketEntity/5f",
							"com.onewhohears.dscombat.entity.weapon.EntityMissile/7.0f",
							"com.mrcrayfish.guns.entity.MissileEntity/7.0f",
							"xyz.przemyk.simpleplanes.entities.PlaneEntity/8.0f"),
					entry -> true);
			specificEntityHeat = builder
				.comment("entities with a specific heat value [entity_id]/[heat value override (float)]")
				.defineList("specificEntityHeat", 
					Arrays.asList(
							"minecraft:blaze/20f",
							"minecraft:magma_cube/12f",
							"minecraft:wither/200f",
							"minecraft:ender_dragon/100f"), 
					entry -> true);
		}
		
	}
	
	public static class Server {
		
		public final ForgeConfigSpec.DoubleValue accGravity;
		public final ForgeConfigSpec.DoubleValue coDrag;
		public final ForgeConfigSpec.DoubleValue coStaticFriction;
		public final ForgeConfigSpec.DoubleValue coKineticFriction;
		public final ForgeConfigSpec.DoubleValue coLift;
		public final ForgeConfigSpec.DoubleValue coFloat;
		public final ForgeConfigSpec.DoubleValue collideSpeedThreshHold;
		public final ForgeConfigSpec.DoubleValue collideSpeedWithGearThreshHold;
		public final ForgeConfigSpec.DoubleValue collideDamageRate;
		public final ForgeConfigSpec.DoubleValue maxFallSpeed;
		
		public Server(ForgeConfigSpec.Builder builder) {
			accGravity = builder
					.defineInRange("accGravity", 0.025, 0, 1);
			coDrag = builder
					.defineInRange("coDrag", 0.015, 0, 100);
			coStaticFriction = builder
					.defineInRange("coStaticFriction", 4.10, 0, 100);
			coKineticFriction = builder
					.defineInRange("coKineticFriction", 1.50, 0, 100);
			coLift = builder
					.defineInRange("coLift", 0.110, 0, 100);
			coFloat = builder
					.defineInRange("coFloat", 0.10, 0, 100);
			collideSpeedThreshHold = builder
					.defineInRange("collideSpeedThreshHold", 0.5, 0, 10);
			collideSpeedWithGearThreshHold = builder
					.defineInRange("collideSpeedWithGearThreshHold", 1.5, 0, 10);
			collideDamageRate = builder
					.defineInRange("collideDamageRate", 300.0, 0, 1000);
			maxFallSpeed = builder
					.defineInRange("maxFallSpeed", 2.5, 0, 10);
		}
		
	}
	
	static final ForgeConfigSpec clientSpec;
	public static final Config.Client CLIENT;
	
	static final ForgeConfigSpec commonSpec;
	public static final Config.Common COMMON;

	static final ForgeConfigSpec serverSpec;
	public static final Config.Server SERVER;
	
	static {
        final Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder()
        		.configure(Config.Client::new);
        clientSpec = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
        
        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder()
        		.configure(Common::new);
        commonSpec = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();

        final Pair<Server, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder()
        		.configure(Server::new);
        serverSpec = serverSpecPair.getRight();
        SERVER = serverSpecPair.getLeft();
	}
	
}
