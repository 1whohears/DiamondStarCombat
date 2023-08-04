package com.onewhohears.dscombat;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
	
	public static class Client {
		
		public final ForgeConfigSpec.DoubleValue mouseModeMaxRadius;
		public final ForgeConfigSpec.DoubleValue mouseStickDeadzoneRadius;
		public final ForgeConfigSpec.BooleanValue invertY;
		public final ForgeConfigSpec.BooleanValue cameraTurnRelativeToVehicle;
		public final ForgeConfigSpec.BooleanValue customDismount;
		public final ForgeConfigSpec.BooleanValue debugMode;
		
		public Client(ForgeConfigSpec.Builder builder) {
			mouseModeMaxRadius = builder
					.comment("Only for vehicles in Mouse Mode. How far your mouse must move from rest to get a maximum angle.")
					.defineInRange("mouseModeMaxRadius", 1000d, 0, 10000d);
			mouseStickDeadzoneRadius = builder
					.comment("Only for vehicles in Mouse Mode. How far your mouse must move from rest to register an input.")
					.defineInRange("mouseStickDeadzoneRadius", 100d, 0, 1000d);
			invertY = builder
					.define("invertY", false);
			cameraTurnRelativeToVehicle = builder
					.comment("If enabled, turning your player head may feel more natural.")
					.worldRestart()
					.define("cameraTurnRelativeToVehicle", true);
			customDismount = builder
					.comment("If enabled, your sneak key binding becomes Special2, and Special2 binding becomes dismount.")
					.define("customDismount", true);
			debugMode = builder
					.comment("Stats for nerds.")
					.define("debugMode", false);
		}
		
	}
	
	public static class Common {
		
		public final ForgeConfigSpec.IntValue maxBlockCheckDepth;
		public final ForgeConfigSpec.DoubleValue toItemCooldown;
		// TODO 7.5 to item cool down after weapon is fired
		public final ForgeConfigSpec.BooleanValue autoDataLink;
		// TODO 7.2 baby mode for planes disabled by default
		// TODO 7.3 configurable entities that the radar considers vehicles/mobs
		/**
		 * classname
		 */
		//public final ForgeConfigSpec.ConfigValue<List<? extends String>> radarVehicles;
		/**
		 * classname
		 */
		//public final ForgeConfigSpec.ConfigValue<List<? extends String>> radarMobs;
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
			toItemCooldown = builder
					.comment("Seconds before a vehicle can become an item.")
					.defineInRange("toItemCooldown", 30.0, 0, 600.0);
			/*radarVehicles = builder
					.defineList("radarVehicles", 
					Arrays.asList(), 
					entry -> true);
			radarMobs = builder.defineList("radarMobs", 
					Arrays.asList(""), 
					entry -> true);*/
			irEntities = builder
				.comment("entities that ir missiles will target (path to entity class)/(default heat value)")
				.defineList("irEntities", 
					Arrays.asList(
							"net.minecraft.world.entity.projectile.FireworkRocketEntity/2.5f", 
							"net.minecraft.world.entity.Mob/0.4f",
							"xyz.przemyk.simpleplanes.entities.PlaneEntity/6.0f"), 
					entry -> true);
			specificEntityHeat = builder
				.comment("entities with a specific heat value (entity_id)/(heat value override)")
				.defineList("specificEntityHeat", 
					Arrays.asList(
							"minecraft:blaze/10f",
							"minecraft:magma_cube/8f",
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
