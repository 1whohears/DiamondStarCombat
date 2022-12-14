package com.onewhohears.dscombat.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import com.onewhohears.dscombat.DSCombatMod;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;

public final class KeyInit {
	
	private KeyInit() {
	}
	
	public static final String FLIGHT_CONTROL = "key.categories.flight_control"; 
	
	public static KeyMapping throttleUpKey;
	public static KeyMapping throttleDownKey;
	public static KeyMapping pitchUpKey;
	public static KeyMapping pitchDownKey;
	public static KeyMapping rollLeftKey;
	public static KeyMapping rollRightKey;
	public static KeyMapping yawLeftKey;
	public static KeyMapping yawRightKey;
	public static KeyMapping flareKey;
	public static KeyMapping mouseModeKey;
	public static KeyMapping shootKey;
	public static KeyMapping weaponSelectKey;
	public static KeyMapping resetMouseKey;
	public static KeyMapping planeMenuKey;
	public static KeyMapping flipControlsKey;
	public static KeyMapping changeSeat;
	public static KeyMapping landingGear;
	public static KeyMapping sharePos;
	public static KeyMapping specialKey;
	
	private static RegisterKeyMappingsEvent event;
	
	public static void init(RegisterKeyMappingsEvent e) {
		event = e;
		throttleUpKey = registerKey("throttle_up_key", FLIGHT_CONTROL, InputConstants.KEY_UP);
		throttleDownKey = registerKey("throttle_down_key", FLIGHT_CONTROL, InputConstants.KEY_DOWN);
		pitchUpKey = registerKey("pitch_up_key", FLIGHT_CONTROL, InputConstants.KEY_W);
		pitchDownKey = registerKey("pitch_down_key", FLIGHT_CONTROL, InputConstants.KEY_S);
		rollLeftKey = registerKey("roll_left_key", FLIGHT_CONTROL, InputConstants.KEY_LEFT);
		rollRightKey = registerKey("roll_right_key", FLIGHT_CONTROL, InputConstants.KEY_RIGHT);
		yawLeftKey = registerKey("yaw_left_key", FLIGHT_CONTROL, InputConstants.KEY_A);
		yawRightKey = registerKey("yaw_right_key", FLIGHT_CONTROL, InputConstants.KEY_D);
		flareKey = registerKey("flare_key", FLIGHT_CONTROL, InputConstants.KEY_V);
		mouseModeKey = registerKey("mouse_mode_key", FLIGHT_CONTROL, InputConstants.KEY_LCONTROL);
		shootKey = registerMouse("shoot_key", FLIGHT_CONTROL, InputConstants.MOUSE_BUTTON_RIGHT);
		weaponSelectKey = registerKey("weapon_select_key", FLIGHT_CONTROL, InputConstants.KEY_G);
		resetMouseKey = registerKey("reset_mouse_key", FLIGHT_CONTROL, InputConstants.KEY_RALT);
		planeMenuKey = registerKey("plane_menu_key", FLIGHT_CONTROL, InputConstants.KEY_U);
		flipControlsKey = registerKey("flip_controls_key", FLIGHT_CONTROL, InputConstants.KEY_LALT);
		changeSeat = registerKey("change_seat_key", FLIGHT_CONTROL, InputConstants.KEY_SEMICOLON);
		landingGear = registerKey("landing_gear_key", FLIGHT_CONTROL, InputConstants.KEY_L);
		sharePos = registerKey("share_pos_key", FLIGHT_CONTROL, InputConstants.KEY_O);
		specialKey = registerKey("special_key", FLIGHT_CONTROL, InputConstants.KEY_SPACE);
	}
	
	private static KeyMapping registerKey(String name, String category, int keycode) {
		final var key = new KeyMapping("key."+DSCombatMod.MODID+"."+name, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, keycode, category);
		event.register(key);
		return key;
	}
	
	private static KeyMapping registerMouse(String name, String category, int keycode) {
		final var key = new KeyMapping("key."+DSCombatMod.MODID+"."+name, KeyConflictContext.IN_GAME, InputConstants.Type.MOUSE, keycode, category);
		event.register(key);
		return key;
	}
	
}
