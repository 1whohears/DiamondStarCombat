package com.onewhohears.dscombat;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.onewhohears.dscombat.data.aircraft.VehicleSoundManager.PassengerSoundPack;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
	
	public static class Client {
		
		public final ForgeConfigSpec.DoubleValue mouseModeMaxRadius;
		public final ForgeConfigSpec.DoubleValue mouseYReturnRate;
		public final ForgeConfigSpec.DoubleValue mouseXReturnRate;
		public final ForgeConfigSpec.IntValue mouseYSteps;
		public final ForgeConfigSpec.IntValue mouseXSteps;
		public final ForgeConfigSpec.BooleanValue invertY;
		public final ForgeConfigSpec.BooleanValue cameraTurnRelativeToVehicle;
		public final ForgeConfigSpec.BooleanValue customDismount;
		public final ForgeConfigSpec.BooleanValue debugMode;
		public final ForgeConfigSpec.DoubleValue rwrWarningVol, missileWarningVol, irTargetToneVol;
		public final ForgeConfigSpec.DoubleValue cockpitVoiceLineVol;
		public final ForgeConfigSpec.EnumValue<PassengerSoundPack> passengerSoundPack;
		public final ForgeConfigSpec.IntValue maxRenderRackMissileNum;
		
		public Client(ForgeConfigSpec.Builder builder) {
			builder.push("mouse-joystick-settings");
			mouseModeMaxRadius = builder
					.comment("Only for vehicles in Mouse Mode. How far your mouse must move from rest to get a maximum angle.")
					.defineInRange("mouseModeMaxRadius", 400d, 0, 10000d);
			mouseYReturnRate = builder
					.comment("Speed the control stick vertically snaps back to rest when mouse isn't moving.")
					.defineInRange("stickPitchReturnRate", 0, 0, 100d);
			mouseXReturnRate = builder
					.comment("Speed the control stick horizontally snaps back to rest when mouse isn't moving.")
					.defineInRange("stickRollReturnRate", 40d, 0, 100d);
			mouseYSteps = builder
					.defineInRange("stickPitchSteps", 5, 1, 100);
			mouseXSteps = builder
					.defineInRange("stickRollSteps", 5, 1, 100);
			builder.pop();
			builder.push("control");
			invertY = builder
					.comment("Invert vertical inputs.")
					.define("invertY", false);
			cameraTurnRelativeToVehicle = builder
					.comment("If enabled, turning your player head may feel more natural.")
					.define("cameraTurnRelativeToVehicle", true);
			customDismount = builder
					.comment("If enabled, your sneak key binding becomes Special2, and Special2 binding becomes dismount.")
					.define("customDismount", true);
			builder.pop();
			builder.push("sounds");
			rwrWarningVol = builder
					.comment("RWR Warning Sound Volume")
					.defineInRange("rwrWarningVol", 1, 0, 1d);
			missileWarningVol = builder
					.comment("Missile Warning Sound Volume")
					.defineInRange("missileWarningVol", 1, 0, 1d);
			irTargetToneVol = builder
					.comment("IR Target Found Sound Volume")
					.defineInRange("irTargetToneVol", 0.5d, 0, 1d);
			cockpitVoiceLineVol = builder
					.comment("Cockpit Voicelines Volume")
					.defineInRange("cockpitVoiceLineVol", 1d, 0, 1d);
			passengerSoundPack = builder
					.defineEnum("passengerSoundPackOverride", PassengerSoundPack.SAME_AS_VEHICLE);
			builder.pop();
			builder.push("performance");
			maxRenderRackMissileNum = builder
					.defineInRange("maxRenderRackMissileNum", 30, 0, 300);
			debugMode = builder
					.comment("Stats for nerds.")
					.define("debugMode", false);
			builder.pop();
		}
		
	}
	
	public static class Common {
		
		public final ForgeConfigSpec.IntValue maxBlockCheckDepth;
		public final ForgeConfigSpec.DoubleValue gasCanXpRepairRate;
		/**
		 * classname
		 */
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> radarMobs;
		
		public Common(ForgeConfigSpec.Builder builder) {
			maxBlockCheckDepth = builder
					.comment("The number of blocks between 2 entities to check if they can see eachother.")
					.defineInRange("maxBlockCheckDepth", 250, 10, 400);
			gasCanXpRepairRate = builder
					.comment("The average durability repaired by mending per xp point.")
					.defineInRange("gasCanXpRepairRate", 2.0, 2.0, 1000.0);
			radarMobs = builder.defineList("radarMobs", 
					Arrays.asList(
							"net.minecraft.world.entity.Mob"), 
					entry -> true);
		}
		
	}
	
	static final ForgeConfigSpec clientSpec;
	public static final Config.Client CLIENT;
	
	static final ForgeConfigSpec commonSpec;
	public static final Config.Common COMMON;
	
	static {
        final Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder()
        		.configure(Config.Client::new);
        clientSpec = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
        
        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder()
        		.configure(Common::new);
        commonSpec = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
	}
	
}
