package com.onewhohears.dscombat.client.overlay.components;

import java.util.Objects;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;

public class KeyBindsOverlay extends VehicleOverlayComponent {
	
	private static final int DEFAULT_KEY_COLOR = 0x00ff00;
	private static final int USE_KEY_COLOR = 0xffff00;
	private static final int MAPPING_NAME_WIDTH = 80;
	private static final int KEY_NAME_WIDTH = 34;
	
	private static KeyBindsOverlay INSTANCE;
	
	public static void renderIfAllowed(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (Objects.isNull(INSTANCE)) INSTANCE = new KeyBindsOverlay();
        INSTANCE.render(poseStack, screenWidth, screenHeight);
    }
	
	public static Component fixKeyName(KeyMapping key) {
    	switch(key.getKey().getValue()) {
    	case InputConstants.KEY_RCONTROL : return UtilMCText.literal("RCTRL");
    	case InputConstants.KEY_LCONTROL : return UtilMCText.literal("LCTRL");
    	case InputConstants.KEY_LALT : return UtilMCText.literal("LALT");
    	case InputConstants.KEY_RALT : return UtilMCText.literal("RALT");
    	}
    	return key.getKey().getDisplayName();
    }
    
    private KeyBindsOverlay() {}
	
	@Override
	protected void render(PoseStack poseStack, int screenWidth, int screenHeight) {
		if (!(getPlayerVehicle() instanceof EntitySeat seat)) return;
		EntityVehicle vehicle = seat.getParentVehicle();
		if (vehicle == null) return;
		boolean isPilot = seat.isPilotSeat(), isCoPilot = seat.isCoPilotSeat();
		// TODO 0.1 until a better way is made, these controls and other info need to be displayed somewhere
		int index = 0;
		// MOUSE MODE
		if (isPilot) displayMapping(poseStack, screenWidth, screenHeight, index++, DSCKeys.mouseModeKey,
				!DSCClientInputs.getMouseMode().isLockedForward(), DSCClientInputs.getMouseMode().name());
		// OPEN PLANE MENU
		if (isPilot) displayMapping(poseStack, screenWidth, screenHeight, index++, DSCKeys.vehicleMenuKey);
		// OPEN PLANE STORAGE
		if (vehicle.partsManager.hasStorageBoxes()) displayMapping(poseStack, screenWidth, screenHeight, index++, DSCKeys.vehicleStorageKey);
		// DISMOUNT
		if (Config.CLIENT.customDismount.get()) displayMapping(poseStack, screenWidth, screenHeight, index++, DSCKeys.dismount);
		// CHANGE SEAT
		displayMapping(poseStack, screenWidth, screenHeight, index++, DSCKeys.changeSeat);
		// LANDING GEAR
		if (isPilot && vehicle.canToggleLandingGear()) displayMapping(poseStack, screenWidth, screenHeight, index++, DSCKeys.landingGear, 
				vehicle.isLandingGear(), vehicle.isLandingGear() ? "OUT"  : "IN");
		// BREAKS
		if (isPilot && vehicle.canBrake()) displayMapping(poseStack, screenWidth, screenHeight, index++, 
				vehicle.getAircraftType().isPlane() ? DSCKeys.special2Key : DSCKeys.specialKey, 
						UtilMCText.literal("Breaks (S)"), vehicle.isBraking());
		// FLAPS DOWN
		if (isPilot && vehicle.canFlapsDown()) displayMapping(poseStack, screenWidth, screenHeight, index++, 
				DSCKeys.specialKey, UtilMCText.literal("Flaps Down (S1)"));
		// WEAPON ANGLED DOWN
		if (isPilot && vehicle.canAngleWeaponDown()) displayMapping(poseStack, screenWidth, screenHeight, index++, 
				DSCKeys.special2Key, UtilMCText.literal("Nose Down (S2)"));
		// HOVER
		if (isPilot && vehicle.canHover()) displayMapping(poseStack, screenWidth, screenHeight, index++, 
				DSCKeys.specialKey, UtilMCText.literal("Hover (S1)"));
		// FLARES
		if (isPilot && vehicle.hasFlares()) displayMapping(poseStack, screenWidth, screenHeight, index++, 
				DSCKeys.flareKey, vehicle.getFlareNum()+"");
		// CYCLE WEAPON
		if (isPilot || isCoPilot) displayMapping(poseStack, screenWidth, screenHeight, index++, DSCKeys.weaponSelectKey);
		// RADAR MODE
		if (vehicle.radarSystem.hasRadar()) displayMapping(poseStack, screenWidth, screenHeight, index++, DSCKeys.radarModeKey,
				!vehicle.getRadarMode().isOff(), vehicle.getRadarMode().name());
		// SELECT RADAR PING
		if (vehicle.radarSystem.hasRadar()) displayMapping(poseStack, screenWidth, screenHeight, index++, DSCKeys.pingCycleKey);
		// GIMBAL MODE
		if (vehicle.getGimbalForPilotCamera() != null || seat.getCameraYOffset() != 0) displayMapping(poseStack, 
				screenWidth, screenHeight, index++, DSCKeys.gimbalKey,
				DSCClientInputs.isGimbalMode(), DSCClientInputs.isGimbalMode() ? "ON" : "OFF");
	}
	
	protected void displayMapping(PoseStack poseStack, int screenWidth, int screenHeight, int index, KeyMapping key, 
			Component mapName, boolean isUsed, String setting) {
    	int pY = 2 + 10 * index;
    	int pX = 3;
    	int pColor = DEFAULT_KEY_COLOR;
    	if (isUsed) pColor = USE_KEY_COLOR;
    	drawString(poseStack, getFont(), mapName, pX, pY, pColor);
    	pX += MAPPING_NAME_WIDTH;
    	drawString(poseStack, getFont(), fixKeyName(key), pX, pY, pColor);
    	if (setting == null || setting.isEmpty()) return;
    	pX += KEY_NAME_WIDTH;
    	drawString(poseStack, getFont(), setting, pX, pY, pColor);
    }
	
	protected void displayMapping(PoseStack poseStack, int screenWidth, int screenHeight, int index, KeyMapping key, boolean isUsed, String setting) {
		displayMapping(poseStack, screenWidth, screenHeight, index, key, UtilMCText.translatable(key.getName()), isUsed, setting);
	}
    
    protected void displayMapping(PoseStack poseStack, int screenWidth, int screenHeight, int index, KeyMapping key) {
    	displayMapping(poseStack, screenWidth, screenHeight, index, key, key.isDown(), null);
    }
    
    protected void displayMapping(PoseStack poseStack, int screenWidth, int screenHeight, int index, KeyMapping key, String setting) {
    	displayMapping(poseStack, screenWidth, screenHeight, index, key, key.isDown(), setting);
    }
    
    protected void displayMapping(PoseStack poseStack, int screenWidth, int screenHeight, int index, KeyMapping key, Component mapName, boolean isUsed) {
    	displayMapping(poseStack, screenWidth, screenHeight, index, key, mapName, isUsed, null);
    }
    
    protected void displayMapping(PoseStack poseStack, int screenWidth, int screenHeight, int index, KeyMapping key, Component mapName) {
    	displayMapping(poseStack, screenWidth, screenHeight, index, key, mapName, key.isDown(), null);
    }

}
