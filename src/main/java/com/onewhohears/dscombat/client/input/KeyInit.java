package com.onewhohears.dscombat.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import com.onewhohears.dscombat.DSCombatMod;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;

public final class KeyInit {
	
	private KeyInit() {
	}
	
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
	
	private static RegisterKeyMappingsEvent event;
	
	public static void init(RegisterKeyMappingsEvent e) {
		event = e;
		throttleUpKey = registerKey("throttle_up_key", ModKeyCategories.FLIGHT_CONTROL, InputConstants.KEY_W);
		throttleDownKey = registerKey("throttle_down_key", ModKeyCategories.FLIGHT_CONTROL, InputConstants.KEY_S);
		pitchUpKey = registerKey("pitch_up_key", ModKeyCategories.FLIGHT_CONTROL, InputConstants.KEY_UP);
		pitchDownKey = registerKey("pitch_down_key", ModKeyCategories.FLIGHT_CONTROL, InputConstants.KEY_DOWN);
		rollLeftKey = registerKey("roll_left_key", ModKeyCategories.FLIGHT_CONTROL, InputConstants.KEY_A);
		rollRightKey = registerKey("roll_right_key", ModKeyCategories.FLIGHT_CONTROL, InputConstants.KEY_D);
		yawLeftKey = registerKey("yaw_left_key", ModKeyCategories.FLIGHT_CONTROL, InputConstants.KEY_LEFT);
		yawRightKey = registerKey("yaw_right_key", ModKeyCategories.FLIGHT_CONTROL, InputConstants.KEY_RIGHT);
		flareKey = registerKey("flare_key", ModKeyCategories.FLIGHT_CONTROL, InputConstants.KEY_V);
		mouseModeKey = registerKey("mouse_mode_key", ModKeyCategories.FLIGHT_CONTROL, InputConstants.KEY_LCONTROL);
		shootKey = registerMouse("shoot_key", ModKeyCategories.FLIGHT_CONTROL, InputConstants.MOUSE_BUTTON_RIGHT);
		weaponSelectKey = registerKey("weapon_select_key", ModKeyCategories.FLIGHT_CONTROL, InputConstants.KEY_G);
		resetMouseKey = registerKey("reset_mouse_key", ModKeyCategories.FLIGHT_CONTROL, InputConstants.KEY_LALT);
		planeMenuKey = registerKey("plane_menu_key", ModKeyCategories.FLIGHT_CONTROL, InputConstants.KEY_U);
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
