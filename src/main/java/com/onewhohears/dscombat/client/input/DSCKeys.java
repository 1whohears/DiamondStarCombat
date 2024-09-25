package com.onewhohears.dscombat.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import com.onewhohears.dscombat.DSCombatMod;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;

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
	public static KeyMapping mouseModeKey, resetMouseKey, gimbalKey;
	public static KeyMapping shootKey, landingGear, flareKey;
	public static KeyMapping vehicleMenuKey, vehicleStorageKey;
	public static KeyMapping pingCycleKey, radarModeKey, radarDisplayRangeKey;
	public static KeyMapping changeSeat, dismount, eject;
	public static KeyMapping specialKey, special2Key;
	public static KeyMapping flipControlsKey;
	// IDEA 4.2 temp burner boost key
	
	private static RegisterKeyMappingsEvent event;
	
	public static void init(RegisterKeyMappingsEvent e) {
		event = e;
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
		resetMouseKey = registerKey("reset_mouse_key", VEHICLE_CONTROL_UTIL, InputConstants.KEY_RALT);
		// PASSENGER CONTROL
		vehicleMenuKey = registerKey("plane_menu_key", VEHICLE_PASSENGER_CONTROL, InputConstants.KEY_U);
		vehicleStorageKey = registerKey("vehicle_storage_key", VEHICLE_PASSENGER_CONTROL, InputConstants.KEY_EQUALS);
		dismount = registerKey("dismount_key", VEHICLE_PASSENGER_CONTROL, InputConstants.KEY_H);
		changeSeat = registerKey("change_seat_key", VEHICLE_PASSENGER_CONTROL, InputConstants.KEY_Y);
		landingGear = registerKey("landing_gear_key", VEHICLE_PASSENGER_CONTROL, InputConstants.KEY_K);
		gimbalKey = registerKey("gimbal_key", VEHICLE_PASSENGER_CONTROL, InputConstants.KEY_SEMICOLON);
		specialKey = registerKey("special_key", VEHICLE_PASSENGER_CONTROL, InputConstants.KEY_SPACE);
		special2Key = registerKey("special_2_key", VEHICLE_PASSENGER_CONTROL, InputConstants.KEY_LALT);
		eject = registerKey("eject", VEHICLE_PASSENGER_CONTROL, InputConstants.KEY_RBRACKET);
		// COMBAT CONTROL
		shootKey = registerMouse("shoot_key", VEHICLE_COMBAT_CONTROL, InputConstants.MOUSE_BUTTON_RIGHT);
		weaponSelectKey = registerKey("weapon_select_key", VEHICLE_COMBAT_CONTROL, InputConstants.KEY_G);
		weaponSelect2Key = registerKey("weapon_select_up_key", VEHICLE_COMBAT_CONTROL, InputConstants.UNKNOWN.getValue());
		flareKey = registerKey("flare_key", VEHICLE_COMBAT_CONTROL, InputConstants.KEY_V);
		radarModeKey = registerKey("radar_mode_key", VEHICLE_COMBAT_CONTROL, InputConstants.KEY_O);
		pingCycleKey = registerKey("ping_cycle_key", VEHICLE_COMBAT_CONTROL, InputConstants.KEY_I);
		radarDisplayRangeKey = registerKey("radar_display_range_key", VEHICLE_COMBAT_CONTROL, InputConstants.KEY_NUMPAD1);
	}
	
	private static KeyMapping registerKey(String name, String category, int keycode) {
		final var key = new KeyMapping("key."+DSCombatMod.MODID+"."+name, 
				KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, keycode, category);
		event.register(key);
		return key;
	}
	
	private static KeyMapping registerMouse(String name, String category, int keycode) {
		final var key = new KeyMapping("key."+DSCombatMod.MODID+"."+name, 
				KeyConflictContext.IN_GAME, InputConstants.Type.MOUSE, keycode, category);
		event.register(key);
		return key;
	}
	
}
