package com.onewhohears.dscombat;

import java.util.List;

import com.onewhohears.dscombat.common.core.MarkerDisplayMode;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.dscombat.data.weapon.stats.TargetMode;
import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

import com.onewhohears.dscombat.data.radar.RadarFilterMode;

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
		public final ForgeConfigSpec.EnumValue<TargetMode> targetMode;
		public final ForgeConfigSpec.EnumValue<TargetMode> preferredPositionTargetMode;
		// VOLUME/SOUND
		public final ForgeConfigSpec.DoubleValue rwrWarningVol, missileWarningVol, irTargetToneVol;
		public final ForgeConfigSpec.DoubleValue cockpitVoiceLineVol;
		public final ForgeConfigSpec.ConfigValue<String> passengerSoundPack;
		// DISPLAY
		public final ForgeConfigSpec.IntValue radarPingOverlaySize;
		public final ForgeConfigSpec.EnumValue<RadarFilterMode> radarFilterMode;
		public final ForgeConfigSpec.EnumValue<MarkerDisplayMode> markerMode;
		// RENDER DISTANCES
		public final ForgeConfigSpec.IntValue maxRenderRackMissileNum;
		public final ForgeConfigSpec.DoubleValue renderWeaponRackDistance;
		public final ForgeConfigSpec.DoubleValue renderTurretDistance;
		public final ForgeConfigSpec.DoubleValue renderEngineDistance;
		public final ForgeConfigSpec.DoubleValue renderRadarDistance;
		public final ForgeConfigSpec.DoubleValue renderOtherExternalPartDistance;
		// OTHER
		public final ForgeConfigSpec.BooleanValue debugMode;
		//public final ForgeConfigSpec.IntValue syncSeatPosRate;
		// HUD
		public final ForgeConfigSpec.BooleanValue enableModernHUD;
		public final ForgeConfigSpec.DoubleValue modernHudOpacity;
		public final ForgeConfigSpec.DoubleValue modernHudScale;
		public final ForgeConfigSpec.ConfigValue<String> hudSpeedUnit;
		public final ForgeConfigSpec.ConfigValue<String> hudAltUnit;
		public final ForgeConfigSpec.BooleanValue showControlsInModernHUD;
		// HELICOPTER INPUT SMOOTHING
		public final ForgeConfigSpec.BooleanValue enableHeliInputSmoothing;
		public final ForgeConfigSpec.DoubleValue heliPitchSmoothingStep;
		public final ForgeConfigSpec.DoubleValue heliRollSmoothingStep;
		public final ForgeConfigSpec.DoubleValue heliYawSmoothingStep;
		
		public Client(ForgeConfigSpec.Builder builder) {
			builder.push("display");
			radarPingOverlaySize = builder
					.defineInRange("radarPingOverlaySize", 100, 10, 1000);
			radarFilterMode = builder
					.defineEnum("defaultRadarMode", RadarFilterMode.ALL);
			markerMode = builder
					.defineEnum("markerDisplayMode", MarkerDisplayMode.SELECT_BIG);
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
			builder.push("targetMode");
			targetMode = builder
					.defineEnum("targetMode", TargetMode.LOOK);
			preferredPositionTargetMode = builder
					.defineEnum("preferredPositionTargetMode", TargetMode.LOOK);
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
			/*syncSeatPosRate = builder
					.comment("Sometimes when the server lags, the server side position of the player's seat " +
							"entity doesn't get updated. Eventually the server thinks the seat is outside " +
							"of the player's render distance and sends a discard packet. This causes the player " +
							"to randomly fall out of their plane. This is solved by having the client tell the " +
							"server explicitly where the seat actually is on the client side. This config controls " +
							"how often (in ticks) this sync packet is sent from the client to the server. This used " +
							"to be set to 40 for everyone, but some have exceptionally poor connections and may " +
							"need this value to be lowered.")
					.defineInRange("syncSeatPosRate", 10, 0, 200);*/
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
			// Helicopter input smoothing
			builder.push("helicopter-input");
			enableHeliInputSmoothing = builder
				.comment("Enable helicopter-specific input smoothing for pitch/roll/yaw.")
				.define("enableHeliInputSmoothing", true);
			heliPitchSmoothingStep = builder
				.comment("Per-tick approach step for heli pitch input smoothing. Smaller = smoother (0-1).")
				.defineInRange("heliPitchSmoothingStep", 0.06d, 0.0d, 1.0d);
			heliRollSmoothingStep = builder
				.comment("Per-tick approach step for heli roll input smoothing. Smaller = smoother (0-1).")
				.defineInRange("heliRollSmoothingStep", 0.06d, 0.0d, 1.0d);
			heliYawSmoothingStep = builder
				.comment("Per-tick approach step for heli yaw input smoothing. Smaller = smoother (0-1).")
				.defineInRange("heliYawSmoothingStep", 0.04d, 0.0d, 1.0d);
			builder.pop();
			// Modern HUD settings
			builder.push("hud");
			enableModernHUD = builder
				.comment("Enable the modern, consolidated vehicle HUD.")
				.define("enableModernHUD", false);
			modernHudOpacity = builder
				.comment("Opacity of the modern HUD panels (0.0-1.0).")
				.defineInRange("modernHudOpacity", 0.85d, 0.0d, 1.0d);
			modernHudScale = builder
				.comment("Scale of the modern HUD panels (0.5-1.5).")
				.defineInRange("modernHudScale", 1.0d, 0.5d, 1.5d);
			hudSpeedUnit = builder
				.comment("Speed units: mps (m/s), kph (km/h), knots (kt)")
				.define("hudSpeedUnit", "mps");
			hudAltUnit = builder
				.comment("Altitude units: m (meters), ft (feet)")
				.define("hudAltUnit", "m");
			showControlsInModernHUD = builder
				.comment("If true, keeps the stick/rudder control overlay visible when Modern HUD is enabled.")
				.define("showControlsInModernHUD", false);
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
        public final ForgeConfigSpec.DoubleValue universalIRLScale;
		public final ForgeConfigSpec.DoubleValue universalTopSpeed;
		public final ForgeConfigSpec.DoubleValue vehicleSpeedFactor;
		public final ForgeConfigSpec.DoubleValue planeSpeedFactor;
		public final ForgeConfigSpec.DoubleValue heliSpeedFactor;
		public final ForgeConfigSpec.DoubleValue carSpeedFactor;
		public final ForgeConfigSpec.DoubleValue boatSpeedFactor;
		public final ForgeConfigSpec.DoubleValue chainLength;
		// HELICOPTER HANDLING
		public final ForgeConfigSpec.DoubleValue heliLateralDampingXZ;
		public final ForgeConfigSpec.DoubleValue heliHoverDamping;
		// HELICOPTER PHYSICS (ground effect, ETL, VRS)
		public final ForgeConfigSpec.BooleanValue enableGroundEffect;
		public final ForgeConfigSpec.DoubleValue groundEffectStrength;
		public final ForgeConfigSpec.DoubleValue groundEffectMaxHeight;
		public final ForgeConfigSpec.BooleanValue enableTranslationalLift;
		public final ForgeConfigSpec.DoubleValue translationalLiftMaxBonus;
		public final ForgeConfigSpec.DoubleValue translationalLiftFullSpeed;
		public final ForgeConfigSpec.BooleanValue enableVRS;
		public final ForgeConfigSpec.DoubleValue vrsDescentTrigger;
		public final ForgeConfigSpec.DoubleValue vrsHorizMaxSpeed;
		public final ForgeConfigSpec.DoubleValue vrsMaxPenalty;
		// Anti-torque & yaw damper
		public final ForgeConfigSpec.BooleanValue enableAntiTorque;
		public final ForgeConfigSpec.DoubleValue antiTorqueCoeff;
		public final ForgeConfigSpec.DoubleValue antiTorqueDirection;
		public final ForgeConfigSpec.DoubleValue yawDamperGain;
		// Rotor spool dynamics
		public final ForgeConfigSpec.DoubleValue rotorSpoolUpRate;
		public final ForgeConfigSpec.DoubleValue rotorSpoolDownRate;
		// Control authority scaling and IGE damping
		public final ForgeConfigSpec.DoubleValue minControlAuthorityAtIdle;
		public final ForgeConfigSpec.DoubleValue igeExtraLateralDamping;
		public final ForgeConfigSpec.DoubleValue igeDampingMaxHeight;
		// Coupling effects
		public final ForgeConfigSpec.BooleanValue enableETLPitchUp;
		public final ForgeConfigSpec.DoubleValue etlPitchGain;
		public final ForgeConfigSpec.BooleanValue enableTailRotorRollCoupling;
		public final ForgeConfigSpec.DoubleValue tailRotorRollCoeff;
		// Authority scaling behavior
		public final ForgeConfigSpec.BooleanValue scaleAuthorityPreClamp;
		// ETL trim assist (auto pitch-down to counter pitch-up)
		public final ForgeConfigSpec.BooleanValue enableETLTrimAssist;
		public final ForgeConfigSpec.DoubleValue etlTrimGain;
		public final ForgeConfigSpec.BooleanValue scaleTorqueWithRotorPower;
		public Server(ForgeConfigSpec.Builder builder) {
            chainLength = builder.defineInRange("chainLength", 10.0, 1.0, Double.MAX_VALUE);
			builder.push("speed_factors");
            universalIRLScale = builder.comment("The percent of the IRL top speed vehicle's travel at. " +
                            "1/8th (0.125) by default.")
                    .defineInRange("universalIRLScale", DSCPhyCons.HORIZONTAL_SPEED_SCALE, 0.01, 1);
			universalTopSpeed = builder.comment("The absolute max horizontal speed for all vehicles in blocks/second. ",
							"Lower this value if your playing on a server that doesn't have pre-generated chunks.")
					.defineInRange("universalTopSpeed", 200.0, 1, 1000);
			vehicleSpeedFactor = builder.defineInRange("vehicleSpeedFactor", 1.0, 0, 10);
			planeSpeedFactor = builder.defineInRange("planeSpeedFactor", 1.0, 0, 10);
			heliSpeedFactor = builder.defineInRange("heliSpeedFactor", 1.0, 0, 10);
			carSpeedFactor = builder.defineInRange("carSpeedFactor", 1.0, 0, 10);
			boatSpeedFactor = builder.defineInRange("boatSpeedFactor", 1.0, 0, 10);
			builder.pop();
			// Helicopter handling tuning knobs
			builder.push("helicopter_handling");
			heliLateralDampingXZ = builder
				.comment("Per-tick lateral damping multiplier for helicopters while airborne and not in hover assist. 1 = no damping.")
				.defineInRange("heliLateralDampingXZ", 0.99d, 0.90d, 1.0d);
			heliHoverDamping = builder
				.comment("Per-tick velocity damping multiplier applied in heli hover assist mode (all axes). 1 = no damping.")
				.defineInRange("heliHoverDamping", 0.95d, 0.80d, 1.0d);
			builder.pop();
			// Helicopter physics effects
			builder.push("helicopter_physics");
			enableGroundEffect = builder
				.comment("Enable ground effect: increased lift near ground (IGE).")
				.define("enableGroundEffect", true);
			groundEffectStrength = builder
				.comment("Max additional lift near ground (as a fraction). 0.2 = up to +20%.")
				.defineInRange("groundEffectStrength", 0.2d, 0.0d, 1.0d);
			groundEffectMaxHeight = builder
				.comment("Height above ground (blocks) where ground effect fades to zero.")
				.defineInRange("groundEffectMaxHeight", 5.0d, 0.0d, 64.0d);
			enableTranslationalLift = builder
				.comment("Enable ETL: increased lift with forward airspeed up to a limit.")
				.define("enableTranslationalLift", true);
			translationalLiftMaxBonus = builder
				.comment("Max additional lift at or above ETL full speed (fraction). 0.15 = +15%.")
				.defineInRange("translationalLiftMaxBonus", 0.15d, 0.0d, 1.0d);
			translationalLiftFullSpeed = builder
				.comment("Horizontal speed (blocks/tick) where ETL bonus reaches max.")
				.defineInRange("translationalLiftFullSpeed", 0.35d, 0.0d, 5.0d);
			enableVRS = builder
				.comment("Enable VRS: lift degradation during high-rate vertical descents with low forward speed.")
				.define("enableVRS", true);
			vrsDescentTrigger = builder
				.comment("Downward speed (blocks/tick) above which VRS begins.")
				.defineInRange("vrsDescentTrigger", 0.15d, 0.0d, 5.0d);
			vrsHorizMaxSpeed = builder
				.comment("Max horizontal speed (blocks/tick) for VRS to apply (above this, no VRS).")
				.defineInRange("vrsHorizMaxSpeed", 0.20d, 0.0d, 5.0d);
			vrsMaxPenalty = builder
				.comment("Max lift reduction from VRS (fraction). 0.5 = up to -50% lift.")
				.defineInRange("vrsMaxPenalty", 0.5d, 0.0d, 1.0d);
			enableAntiTorque = builder
				.comment("Enable anti-torque/yaw coupling: simulates main rotor torque and tail rotor compensation.")
				.define("enableAntiTorque", true);
			antiTorqueCoeff = builder
				.comment("Yaw moment per unit rotor thrust (tune small).")
				.defineInRange("antiTorqueCoeff", 0.0025d, 0.0d, 1.0d);
			antiTorqueDirection = builder
				.comment("Direction sign of main rotor torque (+1 or -1). Use to flip depending on rotor spin.")
				.defineInRange("antiTorqueDirection", 1.0d, -1.0d, 1.0d);
			yawDamperGain = builder
				.comment("Additional yaw rate damping gain to stabilize heading (applied as moment proportional to yaw rate).")
				.defineInRange("yawDamperGain", 0.02d, 0.0d, 1.0d);
			rotorSpoolUpRate = builder
				.comment("Per-tick rotor power increase step towards throttle command.")
				.defineInRange("rotorSpoolUpRate", 0.02d, 0.0d, 1.0d);
			rotorSpoolDownRate = builder
				.comment("Per-tick rotor power decrease step towards throttle command.")
				.defineInRange("rotorSpoolDownRate", 0.04d, 0.0d, 1.0d);
			minControlAuthorityAtIdle = builder
				.comment("Minimum fraction of control authority (pitch/roll/yaw) when rotor power is near zero.")
				.defineInRange("minControlAuthorityAtIdle", 0.3d, 0.0d, 1.0d);
			igeExtraLateralDamping = builder
				.comment("Additional lateral damping multiplier applied near ground (IGE). 0 = none; 0.03 ~ mild.")
				.defineInRange("igeExtraLateralDamping", 0.0d, 0.0d, 0.2d);
			igeDampingMaxHeight = builder
				.comment("Max height above ground (blocks) over which extra IGE lateral damping fades.")
				.defineInRange("igeDampingMaxHeight", 3.0d, 0.0d, 16.0d);
			enableETLPitchUp = builder
				.comment("Enable ETL-induced pitch-up moment that grows with forward speed.")
				.define("enableETLPitchUp", true);
			etlPitchGain = builder
				.comment("Pitch-up moment gain vs normalized forward speed (tune small, e.g., 0.02).")
				.defineInRange("etlPitchGain", 0.02d, 0.0d, 0.2d);
			enableTailRotorRollCoupling = builder
				.comment("Enable roll moment proportional to anti-torque (tail rotor) thrust.")
				.define("enableTailRotorRollCoupling", true);
			tailRotorRollCoeff = builder
				.comment("Roll moment per unit of anti-torque yaw moment (tune small, e.g., 0.15).")
				.defineInRange("tailRotorRollCoeff", 0.15d, 0.0d, 1.0d);
			scaleAuthorityPreClamp = builder
				.comment("Scale control max deltas by rotor power (pre-clamp). If false, authority scales after clamping.")
				.define("scaleAuthorityPreClamp", true);
			enableETLTrimAssist = builder
				.comment("Apply small auto pitch-down with forward speed to reduce manual trim.")
				.define("enableETLTrimAssist", true);
			etlTrimGain = builder
				.comment("Pitch-down trim gain vs normalized forward speed (tune very small, e.g., 0.01).")
				.defineInRange("etlTrimGain", 0.01d, 0.0d, 0.1d);
			scaleTorqueWithRotorPower = builder
				.comment("Scale applied control torques by rotor power (alternative to control moment scaling).")
				.define("scaleTorqueWithRotorPower", false);
			builder.pop();
		}
	}
	
	public static final ForgeConfigSpec clientSpec;
	public static final Config.Client CLIENT;
	
	public static final ForgeConfigSpec commonSpec;
	public static final Config.Common COMMON;

	public static final ForgeConfigSpec serverSpec;
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
