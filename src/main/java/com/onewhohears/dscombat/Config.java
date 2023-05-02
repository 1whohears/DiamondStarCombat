package com.onewhohears.dscombat;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
	
	public static class Client {
		
		public final ForgeConfigSpec.DoubleValue mouseModeMaxRadius;
		public final ForgeConfigSpec.DoubleValue mouseStickDeadzoneRadius;
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

	static final ForgeConfigSpec serverSpec;
	public static final Config.Server SERVER;
	
	static {
        final Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder()
        		.configure(Config.Client::new);
        clientSpec = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();

        final Pair<Server, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder()
        		.configure(Server::new);
        serverSpec = serverSpecPair.getRight();
        SERVER = serverSpecPair.getLeft();
	}
	
}
