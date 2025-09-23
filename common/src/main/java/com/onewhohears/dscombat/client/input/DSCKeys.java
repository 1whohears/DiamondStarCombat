package com.onewhohears.dscombat.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import com.onewhohears.dscombat.DSCombatMod;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class DSCKeys {
	
	private DSCKeys() {
	}
	
	public static final String VEHICLE_MOVE_CONTROL = "key.categories.vehicle_move_control"; 
	public static final String VEHICLE_CONTROL_UTIL = "key.categories.vehicle_control_util"; 
	public static final String VEHICLE_PASSENGER_CONTROL = "key.categories.vehicle_passenger_control"; 
	public static final String VEHICLE_COMBAT_CONTROL = "key.categories.vehicle_combat_control"; 
	
	public static KeyMapping throttleUpKey, throttleDownKey;
	public static KeyMapping pitchUpKey, pitchDownKey;
	public static KeyMapping rollLeftKey, rollRightKey;
	public static KeyMapping yawLeftKey, yawRightKey;
	public static KeyMapping weaponSelectKey, weaponSelect2Key;
	public static KeyMapping mouseModeKey, resetMouseKey;
    public static KeyMapping cameraTrackTargetKey, gimbalKey;
	public static KeyMapping shootKey, landingGear, flareKey, chaffKey;
	public static KeyMapping vehicleMenuKey;
	public static KeyMapping pingCycleKey, radarModeKey;
	public static KeyMapping changeSeat, dismount, eject;
	public static KeyMapping specialKey, special2Key;
	public static KeyMapping flipControlsKey;
	public static KeyMapping leanLeftKey, leanRightKey;
	public static KeyMapping afterBurnerKey, turnAssistKey;

	private static final Map<String, KeyMapping> keys = new HashMap<>();
	
	public static void init() {
		// MOVE CONTROL
		throttleUpKey = registerKey("throttle_up_key", VEHICLE_MOVE_CONTROL, InputConstants.KEY_UP);
		throttleDownKey = registerKey("throttle_down_key", VEHICLE_MOVE_CONTROL, InputConstants.KEY_DOWN);
		pitchUpKey = registerKey("pitch_up_key", VEHICLE_MOVE_CONTROL, InputConstants.KEY_W);
		pitchDownKey = registerKey("pitch_down_key", VEHICLE_MOVE_CONTROL, InputConstants.KEY_S);
		rollLeftKey = registerKey("roll_left_key", VEHICLE_MOVE_CONTROL, InputConstants.KEY_LEFT);
		rollRightKey = registerKey("roll_right_key", VEHICLE_MOVE_CONTROL, InputConstants.KEY_RIGHT);
		yawLeftKey = registerKey("yaw_left_key", VEHICLE_MOVE_CONTROL, InputConstants.KEY_A);
		yawRightKey = registerKey("yaw_right_key", VEHICLE_MOVE_CONTROL, InputConstants.KEY_D);
		// CONTROL UTIL
		mouseModeKey = registerKey("mouse_mode_key", VEHICLE_CONTROL_UTIL, InputConstants.KEY_LCONTROL);
		flipControlsKey = registerKey("flip_controls_key", VEHICLE_CONTROL_UTIL, InputConstants.KEY_LSHIFT);
		resetMouseKey = registerKey("reset_mouse_key", VEHICLE_CONTROL_UTIL, InputConstants.KEY_RCONTROL);
		leanLeftKey = registerKey("lean_left_key", VEHICLE_CONTROL_UTIL, InputConstants.UNKNOWN.getValue());
		leanRightKey = registerKey("lean_right_key", VEHICLE_CONTROL_UTIL, InputConstants.UNKNOWN.getValue());
		turnAssistKey = registerKey("turn_assist_key", VEHICLE_CONTROL_UTIL, InputConstants.KEY_RALT);
        cameraTrackTargetKey = registerKey("camera_track_target_key", VEHICLE_CONTROL_UTIL, InputConstants.KEY_RSHIFT);
		// PASSENGER CONTROL
		vehicleMenuKey = registerKey("plane_menu_key", VEHICLE_PASSENGER_CONTROL, InputConstants.KEY_U);
		dismount = registerKey("dismount_key", VEHICLE_PASSENGER_CONTROL, InputConstants.KEY_H);
		changeSeat = registerKey("change_seat_key", VEHICLE_PASSENGER_CONTROL, InputConstants.KEY_Y);
		landingGear = registerKey("landing_gear_key", VEHICLE_PASSENGER_CONTROL, InputConstants.KEY_K);
		gimbalKey = registerKey("gimbal_key", VEHICLE_PASSENGER_CONTROL, InputConstants.KEY_SEMICOLON);
		specialKey = registerKey("special_key", VEHICLE_PASSENGER_CONTROL, InputConstants.KEY_SPACE);
		special2Key = registerKey("special_2_key", VEHICLE_PASSENGER_CONTROL, InputConstants.KEY_LALT);
		eject = registerKey("eject_key", VEHICLE_PASSENGER_CONTROL, InputConstants.KEY_RBRACKET);
		// COMBAT CONTROL
		shootKey = registerMouse("shoot_key", VEHICLE_COMBAT_CONTROL, InputConstants.MOUSE_BUTTON_RIGHT);
		weaponSelectKey = registerKey("weapon_select_key", VEHICLE_COMBAT_CONTROL, InputConstants.KEY_G);
		weaponSelect2Key = registerKey("weapon_select_up_key", VEHICLE_COMBAT_CONTROL, InputConstants.UNKNOWN.getValue());
		flareKey = registerKey("flare_key", VEHICLE_COMBAT_CONTROL, InputConstants.KEY_V);
		chaffKey = registerKey("chaff_key", VEHICLE_COMBAT_CONTROL, InputConstants.KEY_V);
		radarModeKey = registerKey("radar_mode_key", VEHICLE_COMBAT_CONTROL, InputConstants.KEY_O);
		pingCycleKey = registerKey("ping_cycle_key", VEHICLE_COMBAT_CONTROL, InputConstants.KEY_I);
		afterBurnerKey = registerKey("afterburner_toggle_key", VEHICLE_COMBAT_CONTROL, InputConstants.KEY_B);
	}

    public static KeyMapping registerKey(String name, String category, int keycode) {
        var key = registerKeyImpl(name, category, keycode);
        keys.put(name, key);
        return key;
    }

    public static KeyMapping registerMouse(String name, String category, int keycode) {
        var key = registerMouseImpl(name, category, keycode);
        keys.put(name, key);
        return key;
    }

    @ExpectPlatform
	public static KeyMapping registerKeyImpl(String name, String category, int keycode) {
        throw new AssertionError();
	}

    @ExpectPlatform
	public static KeyMapping registerMouseImpl(String name, String category, int keycode) {
		throw new AssertionError();
	}

	public static @Nullable KeyMapping getKey(@NotNull String name) {
		return keys.get(name);
	}

	public static boolean isKeyPressed(String id) {
		KeyMapping key = DSCKeys.getKey(id);
		if (key == null) return false;
		return key.isDown();
	}

	public static Set<String> getKeyIds() {
		return keys.keySet();
	}

	public static boolean hasKey(String id) {
		return keys.containsKey(id);
	}
	
}
