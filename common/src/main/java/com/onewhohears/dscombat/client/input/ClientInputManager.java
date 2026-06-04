package com.onewhohears.dscombat.client.input;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.screen.VehicleMainScreen;
import com.onewhohears.dscombat.client.screen.VehicleScreen;
import com.onewhohears.dscombat.common.core.PositionMarker;
import com.onewhohears.dscombat.common.network.VehicleSyncAction;
import com.onewhohears.dscombat.common.network.toserver.ToServerModifyMarker;
import com.onewhohears.dscombat.common.network.toserver.ToServerSeatPos;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.data.weapon.stats.TargetMode;
import com.onewhohears.dscombat.entity.parts.EntityRidablePart;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilPrint;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.UtilParse;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.Util;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClientInputManager {

    public static final float THROTTLE_CHANGE_RATE = 0.075f;

    private static final Map<String, ActionInputHolder.Button> buttons = new HashMap<>();
    private static final Map<String, ActionInputHolder.Axis> axes = new HashMap<>();

    // MOVE CONTROL
    public static final ActionInputHolder.Axis THROTTLE = registerAxis("throttle", "throttle_down_key", "throttle_up_key");
    public static final ActionInputHolder.Axis PITCH = registerAxis("pitch", "pitch_down_key", "pitch_up_key");
    public static final ActionInputHolder.Axis ROLL = registerAxis("roll", "roll_left_key", "roll_right_key");
    public static final ActionInputHolder.Axis YAW = registerAxis("yaw", "yaw_left_key", "yaw_right_key");

    // CONTROL UTIL
    public static final ActionInputHolder.Button MOUSE_MODE = registerButton("mouse_mode", "mouse_mode_key");
    public static final ActionInputHolder.Button FLIP_CONTROLS = registerButton("flip_controls", "flip_controls_key");
    public static final ActionInputHolder.Button RESET_MOUSE = registerButton("reset_mouse", "reset_mouse_key");
    public static final ActionInputHolder.Button LEAN_LEFT = registerButton("lean_left", "lean_left_key");
    public static final ActionInputHolder.Button LEAN_RIGHT = registerButton("lean_right", "lean_right_key");
    public static final ActionInputHolder.Button TURN_ASSIST = registerButton("turn_assist", "turn_assist_key");
    public static final ActionInputHolder.Button CAMERA_TRACK_TARGET = registerButton("camera_track_target", "camera_track_target_key");

    // PASSENGER CONTROL
    public static final ActionInputHolder.Button VEHICLE_MENU = registerButton("vehicle_menu", "plane_menu_key");
    public static final ActionInputHolder.Button DISMOUNT = registerButton("dismount", "dismount_key");
    public static final ActionInputHolder.Button CHANGE_SEAT = registerButton("change_seat", "change_seat_key");
    public static final ActionInputHolder.Button LANDING_GEAR = registerButton("landing_gear", "landing_gear_key");
    public static final ActionInputHolder.Button GIMBAL = registerButton("gimbal", "gimbal_key");
    public static final ActionInputHolder.Button SPECIAL1 = registerButton("special1", "special_key");
    public static final ActionInputHolder.Button SPECIAL2 = registerButton("special2", "special_2_key");
    public static final ActionInputHolder.Button EJECT = registerButton("eject", "eject_key");

    // COMBAT CONTROL
    public static final ActionInputHolder.Button SHOOT = registerButton("shoot", "shoot_key");
    public static final ActionInputHolder.Button WEAPON_CYCLE = registerButton("weapon_cycle", "weapon_select_key");
    public static final ActionInputHolder.Button WEAPON_CYCLE_INVERSE = registerButton("weapon_cycle_inverse", "weapon_select_up_key");
    public static final ActionInputHolder.Button FLARE = registerButton("flare", "flare_key");
    public static final ActionInputHolder.Button CHAFF = registerButton("chaff", "chaff_key");
    public static final ActionInputHolder.Button RADAR_MODE = registerButton("radar_mode", "radar_mode_key");
    public static final ActionInputHolder.Button PING_CYCLE = registerButton("ping_cycle", "ping_cycle_key");
    public static final ActionInputHolder.Button AFTERBURNER = registerButton("afterburner", "afterburner_toggle_key");
    public static final ActionInputHolder.Button QUICK_MARKER = registerButton("quick_marker", "quick_marker_key");

    private static int leftTicks = 0;
    private static long radarModeUpdateTime = 0;
    private static float currentThrottle = 0;
    private static boolean wasPilot = false;
    // Smoothed heli inputs to reduce touchiness (applied only for helicopters)
    private static float smPitch = 0f, smRoll = 0f, smYaw = 0f;

    private static void pilotTick(@NotNull Minecraft mc, @NotNull Player player, @NotNull EntityVehicle vehicle) {
        if (MOUSE_MODE.isInitPressed()) DSCClientInputs.cycleMouseMode();
        if (RESET_MOUSE.isPressed()) {
            DSCClientInputs.centerMousePos();
            if (vehicle.isTestMode()) {
                player.setXRot(0);
                player.setYRot(0);
                vehicle.setClientQ(QuaternionF.ONE);
            }
        }
        else if (mc.screen != null) DSCClientInputs.centerMousePos();

        boolean flare = FLARE.isPressed();
        boolean chaff = CHAFF.isPressed();
        if (AFTERBURNER.isInitPressed()) DSCClientInputs.toggleAfterBurner();
        if (TURN_ASSIST.isInitPressed()) DSCClientInputs.toggleTurnAssist();
        if (CAMERA_TRACK_TARGET.isInitPressed()) DSCClientInputs.toggleCameraTrackTarget();
        boolean flip = FLIP_CONTROLS.isPressed();
        boolean special = SPECIAL1.isPressed();
        boolean special2 = SPECIAL2.isPressed();

        // should invert pitch
        int invertPitch = Config.CLIENT.invertY.get() ? 1 : -1;
        if (vehicle.getStats().ignoreInvertY()) invertPitch = -1;
        // preliminary axis input collection
        float throttle = THROTTLE.getValue();
        float pitch = PITCH.getValue();
        float roll = ROLL.getValue();
        float yaw = YAW.getValue();
        boolean isBothRoll = ROLL.isNegAndPos();
        // should yaw/roll flip
        if (flip) {
            float temp = yaw;
            yaw = roll;
            roll = temp;
        }
        // should pitch/throttle flip
        boolean type_flip = vehicle.getStats().flipPitchThrottle();
        boolean mode = DSCClientInputs.isCameraLockedForward();
        // I made a truth table and this insane expression is optimal somehow.
        // I wouldn't bother questioning it just move on...unless you are insane. - 1whohears
        boolean flipPitchThrottle = (!type_flip && (flip ^ mode)) || (type_flip && !flip);
        if (flipPitchThrottle) {
            float temp = throttle;
            throttle = pitch;
            pitch = temp;
        }
        // calc mouse mode controls (leaving this very ugly code here for now. will clean up later)
        double mouseX = mc.mouseHandler.xpos() - DSCClientInputs.getMouseCenterX();
        double mouseY = -(mc.mouseHandler.ypos() - DSCClientInputs.getMouseCenterY());
        if (DSCClientInputs.isCameraLockedForward()) {
            // FIXME 2.1 fix mouse control mode
            double ya = Math.abs(mouseY);
            double xa = Math.abs(mouseX);
            float ys = (float) Math.signum(mouseY) * -invertPitch;
            float xs = (float) Math.signum(mouseX);
            double max = Config.CLIENT.mouseModeMaxRadius.get();
            float stickStepsY = Config.CLIENT.mouseYSteps.get();
            float stickStepsX = Config.CLIENT.mouseXSteps.get();
            if (ya >= max) pitch = ys;
            else {
                int step = (int) (ya / max * stickStepsY * ys);
                pitch = ((float) step) / stickStepsY;
            }
            if (xa >= max) roll = xs;
            else {
                int step = (int) (xa / max * stickStepsX * xs);
                roll = ((float) step) / stickStepsX;
            }
            if (getMouseYVelocity(mc) == 0) {
                DSCClientInputs.setMouseCenterY((int) Mth.approach(
                        (float) DSCClientInputs.getMouseCenterY(),
                        (float) mc.mouseHandler.ypos(),
                        Config.CLIENT.mouseYReturnRate.get().floatValue()));
            }
            if (getMouseXVelocity(mc) == 0) {
                DSCClientInputs.setMouseCenterX((int) Mth.approach(
                        (float) DSCClientInputs.getMouseCenterX(),
                        (float) mc.mouseHandler.xpos(),
                        Config.CLIENT.mouseXReturnRate.get().floatValue()));
            }
        } else pitch *= invertPitch;

        // fix throttle
        float zeroThrottle;
        if (vehicle.getStats().negativeThrottle) zeroThrottle = 0;
        else zeroThrottle = -1;
        if (!wasPilot && vehicle.cutThrottleOnNoPilot()) currentThrottle = zeroThrottle;
        if (ActionInput.isWindowActive()) {
            // Use vehicle-defined throttle ramp rates for smoother, more stable thrust changes
            float incRate = vehicle.getThrottleIncreaseRate();
            float decRate = vehicle.getThrottleDecreaseRate();
            if (vehicle.inputs.isThrottleOverride(vehicle)) {
                float goal = vehicle.inputs.getGoalThrottle(vehicle);
                if (!vehicle.getStats().negativeThrottle) goal = goal * 2 - 1;
                // Smoothly approach override goal to avoid sudden jumps
                float step = goal >= currentThrottle ? incRate : decRate;
                currentThrottle = Mth.approach(currentThrottle, goal, step);
            } else if (flipPitchThrottle && type_flip && THROTTLE.isJoystickController()) {
                currentThrottle = THROTTLE.getValue();
            } else if ((!flipPitchThrottle && THROTTLE.isNegAndPos()) || (flipPitchThrottle && PITCH.isNegAndPos())) {
                currentThrottle = Mth.approach(currentThrottle, zeroThrottle, decRate);
            } else if ((!flipPitchThrottle && THROTTLE.isJoystickController()) || (flipPitchThrottle && PITCH.isJoystickController())) {
                currentThrottle = throttle;
            } else if (throttle > 0) {
                currentThrottle = Mth.approach(currentThrottle, 1, incRate);
            } else if (throttle < 0) {
                currentThrottle = Mth.approach(currentThrottle, -1, incRate);
            }
        }
        // Apply input smoothing for helicopters; configurable and optional
        if (vehicle.getStats().isHeli() && Config.CLIENT.enableHeliInputSmoothing.get()) {
            float stepPitch = Config.CLIENT.heliPitchSmoothingStep.get().floatValue();
            float stepRoll  = Config.CLIENT.heliRollSmoothingStep.get().floatValue();
            float stepYaw   = Config.CLIENT.heliYawSmoothingStep.get().floatValue();
            smPitch = Mth.approach(smPitch, pitch, stepPitch);
            smRoll  = Mth.approach(smRoll,  roll,  stepRoll);
            smYaw   = Mth.approach(smYaw,   yaw,   stepYaw);
            pitch = smPitch;
            roll = smRoll;
            yaw = smYaw;
        } else {
            // No smoothing or not a heli: ensure accumulators match raw inputs
            smPitch = pitch;
            smRoll = roll;
            smYaw = yaw;
        }
        float t;
        if (vehicle.getStats().negativeThrottle) t = currentThrottle;
        else t = (currentThrottle + 1) / 2f;

        vehicle.inputs.clientPilotControlsToServer(vehicle,
                t, pitch, roll, yaw,
                flare, chaff, DSCClientInputs.isAfterBurner(),
                special, special2, isBothRoll,
                DSCClientInputs.isCameraLockedForward(), DSCClientInputs.isTurnAssist());

        if (!DSCClientInputs.isCameraLockedForward()) DSCClientInputs.centerMousePos();
        if (LANDING_GEAR.isInitPressed()) {
            sendSyncAction(new VehicleSyncAction.LandingGearAction(vehicle.toggleLandingGear()));
        }
    }

    private static void passengerTick(@NotNull Minecraft mc, @NotNull Player player, @NotNull EntityVehicle vehicle, @NotNull EntityRidablePart seat) {
        boolean isRadarController = player.equals(vehicle.getControllingPlayerOrBot());
        if (DSCClientInputs.disable3rdPersonVehicle) mc.options.setCameraType(CameraType.FIRST_PERSON);
        /*
         * THIS TELLS SERVER WHERE THE SEAT IS IN CASE LAG CAUSES VIOLENCE
         * HOW 4 the culprit of the seat de-sync issue is net.minecraft.server.level.ChunkMap.TrackedEntity.updatePlayer
         * sometimes when the server lags the seat position on the server side doesn't get updated with the plane and the player
         * so the server thinks the seat is outside the player render distance and sends a discard packet to the client
         * is there a way to fix this without the ToServerSeatPos packet?
         */
        new ToServerSeatPos(seat.position()).sendToServer();
        /*if (player.tickCount % Config.CLIENT.syncSeatPosRate.get() == 0) {
            new ToServerSeatPos(seat.position()).sendToServer();
        }*/
        // SWITCH SEAT
        if (CHANGE_SEAT.isInitPressed()) {
            sendSyncAction(new VehicleSyncAction.SwitchSeatAction());
        }
        // CYCLE WEAPON
        int selectNextWeapon = 0;
        if (WEAPON_CYCLE_INVERSE.isInitPressed()) selectNextWeapon = -1;
        else if (WEAPON_CYCLE.isInitPressed()) selectNextWeapon = 1;
        vehicle.weaponSystem.selectNextWeapon(selectNextWeapon);
        // SELECT RADAR PING
        RadarSystem radar = vehicle.radarSystem;
        if (DSCClientInputs.isRadarHovering() && leftTicks == 1) {
            radar.clientSelectTarget(DSCClientInputs.getRadarHoverId());
            DSCClientInputs.setTargetMode(TargetMode.RADAR);
            DSCClientInputs.setSelectedMarkerId(-1);
        }
        // CYCLE PING
        if (PING_CYCLE.isInitPressed()) {
            radar.clientSelectNextTarget();
            DSCClientInputs.setTargetMode(TargetMode.RADAR);
            DSCClientInputs.setSelectedMarkerId(-1);
        }
        // SHOOT PILOT WEAPON OR TURRET
        if (SHOOT.isPressed() && playerCanShoot(player)) {
            WeaponInstance<?> selectedWeapon = vehicle.weaponSystem.getSelected();
            if (DSCClientInputs.getTargetMode().isPosition()) {
                Config.CLIENT.preferredPositionTargetMode.set(DSCClientInputs.getTargetMode());
            }
            TargetMode targetMode = selectedWeapon.fixTargetMode(DSCClientInputs.getTargetMode(),
                    Config.CLIENT.preferredPositionTargetMode.get());
            DSCClientInputs.setTargetMode(targetMode);
            if (targetMode == TargetMode.MARKER && selectedWeapon.getStats().isPosGuided() &&
                    DSCClientInputs.getSelectedMarker() == null) {
                player.displayClientMessage(UtilMCText.translatable("error.dscombat.must_select_marker"), true);
            } else {
                sendSyncAction(new VehicleSyncAction.ShootAction(
                        vehicle.weaponSystem.getSelectedIndex(),
                        radar.getClientSelectedPing(),
                        getShootPos(player, vehicle),
                        targetMode));
            }
        }
        // DISMOUNT
        if (Config.CLIENT.customDismount.get() && DISMOUNT.isPressed()) {
            sendSyncAction(new VehicleSyncAction.DismountAction());
        }
        // EJECT
        if (EJECT.isInitPressed()) {
            if (seat.canEject()) {
                seat.useEject();
                sendSyncAction(new VehicleSyncAction.DismountAction(true));
                UtilEntity.getLevel(player).playLocalSound(player.getX(), player.getY(), player.getZ(),
                        ModSounds.EJECT_WIND, SoundSource.PLAYERS, 0.5f, 1, false);
            } else {
                sendSyncAction(new VehicleSyncAction.DismountAction(false));
            }
        }
        // CYCLE RADAR MODE
        boolean cycleRadarMode = RADAR_MODE.isInitPressed();
        if (cycleRadarMode) {
            DSCClientInputs.cycleRadarFilterMode();
            if (!isRadarController) player.displayClientMessage(UtilMCText.translatable("info.dscombat.not_radar_controller"), true);
        }
        if (isRadarController && DSCClientInputs.getRadarFilterMode() != vehicle.getRadarMode() && Util.getMillis() - radarModeUpdateTime > 500) {
            sendSyncAction(new VehicleSyncAction.SetRadarModeAction(DSCClientInputs.getRadarFilterMode()));
            radarModeUpdateTime = Util.getMillis();
        }
        // USE GIMBAL
        if (GIMBAL.isInitPressed()) {
            DSCClientInputs.toggleGimbalMode();
        }
        // OPEN VEHICLE MENU
        /* the instanceof check was added because for some reason VEHICLE_MENU#isInitPressed was still true
           when opening the VehicleKeyBindsScreen making it immediately go back to VehicleMainScreen.
           this only happened in fabric 1.20.1 no idea why. */
        if (VEHICLE_MENU.isInitPressed() && !(mc.screen instanceof VehicleScreen)) {
            mc.setScreen(new VehicleMainScreen());
        }
        // CAMERA LEAN
        boolean leanLeft = LEAN_LEFT.isInitPressed();
        boolean leanRight = LEAN_RIGHT.isInitPressed();
        if (leanLeft && leanRight) DSCClientInputs.leanNot();
        else if (leanLeft) DSCClientInputs.leanLeft();
        else if (leanRight) DSCClientInputs.leanRight();
    }

    private static void tickAlways() {
        // CREATE QUICK MARKER
        if (QUICK_MARKER.isInitPressed()) {
            new ToServerModifyMarker().sendToServer();
        }
        // SELECT POSITION MARKER
        if (DSCClientInputs.getMarkerHoverId() != -1 && leftTicks == 1) {
            DSCClientInputs.setSelectedMarkerId(DSCClientInputs.getMarkerHoverId());
        }
    }

    private static void tickActions() {
        buttons.forEach((id, action) -> action.tick());
        axes.forEach((id, action) -> action.tick());
    }

    public static void clientTickFirst() {
        tickActions();
        Minecraft mc = Minecraft.getInstance();
        final var player = mc.player;
        if (player == null) {
            if (wasPilot) { smPitch = smRoll = smYaw = 0f; }
            wasPilot = false;
            return;
        }
        tickAlways();
        if (!player.isPassenger() || !(player.getRootVehicle() instanceof EntityVehicle vehicle)) {
            if (wasPilot) { smPitch = smRoll = smYaw = 0f; }
            wasPilot = false;
            return;
        }
        Entity controller = vehicle.getControllingPassenger();
        if (controller == null || !controller.equals(player)) {
            if (wasPilot) { smPitch = smRoll = smYaw = 0f; }
            wasPilot = false;
            return;
        }
        pilotTick(mc, player, vehicle);
        wasPilot = true;
    }

    public static void clientTickSecond() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.mouseHandler.isLeftPressed()) ++leftTicks;
        else leftTicks = 0;
        final var player = mc.player;
        if (player == null || !player.isPassenger()) return;
        if (!(player.getVehicle() instanceof EntityRidablePart seat)) return;
        EntityVehicle vehicle = seat.getParentVehicle();
        if (vehicle == null) return;
        passengerTick(mc, player, vehicle, seat);
    }

    public static void sendSyncAction(VehicleSyncAction action) {
        VehicleSyncAction.sendSyncAction(action);
    }

    public static Vec3 getShootPos(Player player, EntityVehicle vehicle) {
        switch (DSCClientInputs.getTargetMode()) {
            case LOOK -> { return getLookPos(player, vehicle); }
            case COORDS -> {  return Config.CLIENT.getTargetPos(); }
            case MARKER -> {
                PositionMarker marker = DSCClientInputs.getSelectedMarker();
                if (marker == null) return Vec3.ZERO;
                if (!UtilEntity.getLevel(player).dimension().equals(marker.getDimension())) return Vec3.ZERO;
                return marker.getPosition();
            }
        }
        return Vec3.ZERO;
    }

    private static Vec3 lookPos = Vec3.ZERO;
    private static int lookPosCalcTime = -1;

    public static Vec3 getLookPos(Player player, EntityVehicle vehicle) {
        if (player.tickCount != lookPosCalcTime) {
            Entity looker = player;
            if (DSCClientInputs.isGimbalMode()) {
                Entity gimbal = vehicle.getGimbalForPilotCamera();
                if (gimbal != null) {
                    looker = gimbal;
                    looker.setXRot(player.getXRot());
                    looker.setYRot(player.getYRot());
                }
            }
            lookPos = UtilEntity.getLookingAtBlockPos(looker, 1024);
            lookPosCalcTime = player.tickCount;
        }
        return lookPos;
    }

    private static boolean playerCanShoot(Player player) {
        return (System.currentTimeMillis()-DSCClientInputs.getClientMountTime()) > DSCClientInputs.MOUNT_SHOOT_COOLDOWN
                && (!player.isUsingItem() || player.getItemInHand(player.getUsedItemHand()).is(Items.SHIELD));
    }

    public static ActionInputHolder.Button registerButton(String id, String defaultKey) {
        ActionInputHolder.Button holder = new ActionInputHolder.Button(id, new ActionInput.DSCKeyButton(defaultKey));
        buttons.put(id, holder);
        return holder;
    }

    public static ActionInputHolder.Axis registerAxis(String id, String defaultNegKey, String defaultPosKey) {
        ActionInputHolder.Axis holder = new ActionInputHolder.Axis(id, new ActionInput.DSCKeyAxis(defaultNegKey, defaultPosKey));
        axes.put(id, holder);
        return holder;
    }

    public static int getNumButtons() {
        return buttons.size();
    }

    public static int getNumAxes() {
        return axes.size();
    }

    public static int getNumActions() {
        return getNumButtons() + getNumAxes();
    }

    public static Collection<ActionInputHolder.Button> getButtons() {
        return buttons.values();
    }

    public static Collection<ActionInputHolder.Axis> getAxes() {
        return axes.values();
    }

    public static void saveKeyBinds() {
        JsonObject json = new JsonObject();
        buttons.forEach((id, action) -> json.add(id, action.write()));
        axes.forEach((id, action) -> json.add(id, action.write()));
        UtilPrint.printJsonClientDirectory("dscombat/config/action_key_binds.json", json);
    }

    public static void loadKeyBinds() {
        JsonObject json = UtilPrint.readJsonClientDirectory("dscombat/config/action_key_binds.json");
        buttons.forEach((id, action) -> {
            JsonObject data = UtilParse.getJsonSafe(json, id);
            String type = UtilParse.getStringSafe(data, "type", "");
            if (!type.equals("button")) return;
            action.read(data);
        });
        axes.forEach((id, action) -> {
            JsonObject data = UtilParse.getJsonSafe(json, id);
            String type = UtilParse.getStringSafe(data, "type", "");
            if (!type.equals("axis")) return;
            action.read(data);
        });
    }

    @ExpectPlatform
    public static double getMouseYVelocity(Minecraft mc) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static double getMouseXVelocity(Minecraft mc) {
        throw new AssertionError();
    }
}
