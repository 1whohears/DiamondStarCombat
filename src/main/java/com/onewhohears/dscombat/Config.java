package com.onewhohears.dscombat;

import java.util.List;

import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

import com.onewhohears.dscombat.data.radar.RadarStats.RadarMode;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
	
	public static class Client {
		// MOUSE SETTINGS
		public final ForgeConfigSpec.DoubleValue mouseModeMaxRadius;
		public final ForgeConfigSpec.DoubleValue mouseYReturnRate;
		public final ForgeConfigSpec.DoubleValue mouseXReturnRate;
		public final ForgeConfigSpec.IntValue mouseYSteps;
		public final ForgeConfigSpec.IntValue mouseXSteps;
		// CONTROLS
		public final ForgeConfigSpec.BooleanValue invertY;
		public final ForgeConfigSpec.BooleanValue cameraTurnRelativeToVehicle;
		public final ForgeConfigSpec.BooleanValue customDismount;
		// TARGET POS
		public final ForgeConfigSpec.DoubleValue targetPosX, targetPosY, targetPosZ;
		// VOLUME/SOUND
		public final ForgeConfigSpec.DoubleValue rwrWarningVol, missileWarningVol, irTargetToneVol;
		public final ForgeConfigSpec.DoubleValue cockpitVoiceLineVol;
		public final ForgeConfigSpec.ConfigValue<String> passengerSoundPack;
		// DISPLAY
		public final ForgeConfigSpec.IntValue radarPingOverlaySize;
		public final ForgeConfigSpec.EnumValue<RadarMode> defaultRadarMode;
		// RENDER DISTANCES
		public final ForgeConfigSpec.IntValue maxRenderRackMissileNum;
		public final ForgeConfigSpec.DoubleValue renderWeaponRackDistance;
		public final ForgeConfigSpec.DoubleValue renderTurretDistance;
		public final ForgeConfigSpec.DoubleValue renderEngineDistance;
		public final ForgeConfigSpec.DoubleValue renderRadarDistance;
		public final ForgeConfigSpec.DoubleValue renderOtherExternalPartDistance;
		// OTHER
		public final ForgeConfigSpec.BooleanValue debugMode;
		public final ForgeConfigSpec.IntValue syncSeatPosRate;
		
		public Client(ForgeConfigSpec.Builder builder) {
			builder.push("display");
			radarPingOverlaySize = builder
					.defineInRange("radarPingOverlaySize", 100, 10, 1000);
			defaultRadarMode = builder
					.defineEnum("defaultRadarMode", RadarMode.ALL);
			builder.pop();
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
					.comment("If enabled, your sneak key binding doesn't dismount you from DSC vehicles. " +
							"You will have to you the diamond star combat dismount keybinding instead (H by default.)")
					.define("customDismount", true);
			builder.push("targetPos");
			targetPosX = builder.defineInRange("targetPosX", 0, Double.MIN_VALUE, Double.MAX_VALUE);
			targetPosY = builder.defineInRange("targetPosY", 0, Double.MIN_VALUE, Double.MAX_VALUE);
			targetPosZ = builder.defineInRange("targetPosZ", 0, Double.MIN_VALUE, Double.MAX_VALUE);
			builder.pop();
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
					.comment("The voice line pack your fighter jets use. You can use resource packs to add custom " +
							"sound packs. The packs that come built into the mod are 'eng_non_binary_goober', " +
							"'eng_generic_male'.")
					.define("passengerSoundPack", "eng_non_binary_goober");
			builder.pop();
			builder.push("performance");
			maxRenderRackMissileNum = builder
					.defineInRange("maxRenderRackMissileNum", 30, 0, 300);
			debugMode = builder
					.comment("Stats for nerds.")
					.define("debugMode", false);
			syncSeatPosRate = builder
					.comment("Sometimes when the server lags, the server side position of the player's seat " +
							"entity doesn't get updated. Eventually the server thinks the seat is outside " +
							"of the player's render distance and sends a discard packet. This causes the player " +
							"to randomly fall out of their plane. This is solved by having the client tell the " +
							"server explicitly where the seat actually is on the client side. This config controls " +
							"how often (in ticks) this sync packet is sent from the client to the server. This used " +
							"to be set to 40 for everyone, but some have exceptionally poor connections and may " +
							"need this value to be lowered.")
					.defineInRange("syncSeatPosRate", 10, 0, 200);
			builder.push("entity-render-distance");
			renderWeaponRackDistance = builder
					.defineInRange("renderWeaponRackDistance", 256.0, 0, 1000);
			renderTurretDistance = builder
					.defineInRange("renderTurretDistance", 256.0, 0, 1000);
			renderEngineDistance = builder
					.defineInRange("renderEngineDistance", 256.0, 0, 1000);
			renderRadarDistance = builder
					.defineInRange("renderRadarDistance", 256.0, 0, 1000);
			renderOtherExternalPartDistance = builder
					.defineInRange("renderOtherExternalPartDistance", 192.0, 0, 1000);
			builder.pop();
			builder.pop();
		}

		public Vec3 getTargetPos() {
			return new Vec3(targetPosX.get(), targetPosY.get(), targetPosZ.get());
		}
	}
	
	public static class Common {
		
		public final ForgeConfigSpec.IntValue maxBlockCheckDepth;
		public final ForgeConfigSpec.BooleanValue logTurretAIDebug;
		public final ForgeConfigSpec.DoubleValue gasCanXpRepairRate;
		public final ForgeConfigSpec.DoubleValue recoverPartWeight;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> radarMobs;
		
		public Common(ForgeConfigSpec.Builder builder) {
			builder.push("performance");
			maxBlockCheckDepth = builder
					.comment("The number of blocks between 2 entities to check if they can see eachother.")
					.defineInRange("maxBlockCheckDepth", 256, 10, 400);
			builder.pop();
			builder.push("debug");
			logTurretAIDebug = builder.define("logTurretAIDebug", false);
			builder.pop();
			builder.push("gameplay");
			gasCanXpRepairRate = builder
					.comment("The average durability repaired by mending per xp point.")
					.defineInRange("gasCanXpRepairRate", 2.0, 2.0, 1000.0);
			recoverPartWeight = builder
					.comment("Roughly the randomly weighted percentage of vehicle recipe items recovered from crash site.")
					.defineInRange("recoverPartWeight", 0.7, 0.0, 1.0);
			radarMobs = builder.defineList("radarMobs",
                    List.of("net.minecraft.world.entity.Mob"),
					entry -> UtilEntity.getEntityClass((String)entry) != null);
			builder.pop();
		}
		
	}

	public static class Server {
		public final ForgeConfigSpec.DoubleValue vehicleSpeedFactor;
		public final ForgeConfigSpec.DoubleValue planeSpeedFactor;
		public final ForgeConfigSpec.DoubleValue heliSpeedFactor;
		public final ForgeConfigSpec.DoubleValue carSpeedFactor;
		public final ForgeConfigSpec.DoubleValue boatSpeedFactor;
		public Server(ForgeConfigSpec.Builder builder) {
			builder.push("speed_factors");
			vehicleSpeedFactor = builder.defineInRange("vehicleSpeedFactor", 1.0, 0, 10);
			planeSpeedFactor = builder.defineInRange("planeSpeedFactor", 1.0, 0, 10);
			heliSpeedFactor = builder.defineInRange("heliSpeedFactor", 1.0, 0, 10);
			carSpeedFactor = builder.defineInRange("carSpeedFactor", 1.0, 0, 10);
			boatSpeedFactor = builder.defineInRange("boatSpeedFactor", 1.0, 0, 10);
			builder.pop();
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
        		.configure(Config.Common::new);
        commonSpec = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();

		final Pair<Server, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder()
				.configure(Config.Server::new);
		serverSpec = serverSpecPair.getRight();
		SERVER = serverSpecPair.getLeft();
	}
	
}
