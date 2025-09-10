package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.input.ActionInputHolder;
import com.onewhohears.dscombat.client.input.ClientInputManager;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.parts.EntityRidablePart;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.jetbrains.annotations.NotNull;

public class KeyBindsOverlay extends VehicleOverlayComponent {
	
	private static final int DEFAULT_KEY_COLOR = 0x00ff00;
	private static final int USE_KEY_COLOR = 0xffff00;
	private static final int MAPPING_NAME_WIDTH = 80;
	private static final int KEY_NAME_WIDTH = 40;
	
	public static Component fixKeyName(ActionInputHolder.Button key) {
        if (key.getPrimaryAction().isKeyBind()) {
            KeyMapping map = DSCKeys.getKey(key.getPrimaryAction().getId());
            if (map == null) return UtilMCText.literal(key.getPrimaryAction().getId());
            return switch (map.getKey().getValue()) {
                case InputConstants.KEY_RCONTROL -> UtilMCText.literal("R-CTRL");
                case InputConstants.KEY_LCONTROL -> UtilMCText.literal("L-CTRL");
                case InputConstants.KEY_LALT -> UtilMCText.literal("L-ALT");
                case InputConstants.KEY_RALT -> UtilMCText.literal("R-ALT");
                case InputConstants.KEY_LSHIFT -> UtilMCText.literal("LSHIFT");
                case InputConstants.KEY_RSHIFT -> UtilMCText.literal("RSHIFT");
                default -> map.getKey().getDisplayName();
            };
        }
        return key.getName();
    }
	
	protected void displayMapping(PoseStack poseStack, int screenWidth, int screenHeight, int index,
                                  ActionInputHolder.Button key, Component mapName, boolean isUsed, String setting) {
    	int pY = 2 + 10 * index;
    	int pX = 3;
    	int pColor = DEFAULT_KEY_COLOR;
    	if (isUsed) pColor = USE_KEY_COLOR;
    	drawString(poseStack, FONT, mapName, pX, pY, pColor);
    	pX += MAPPING_NAME_WIDTH;
    	drawString(poseStack, FONT, fixKeyName(key), pX, pY, pColor);
    	if (setting == null || setting.isEmpty()) return;
    	pX += KEY_NAME_WIDTH;
    	drawString(poseStack, FONT, setting, pX, pY, pColor);
    }
	
	protected void displayMapping(PoseStack poseStack, int screenWidth, int screenHeight, int index,
                                  ActionInputHolder.Button key, boolean isUsed, String setting) {
		displayMapping(poseStack, screenWidth, screenHeight, index, key,
                key.getName(), isUsed, setting);
	}

	protected void displayMapping(PoseStack poseStack, int screenWidth, int screenHeight, int index,
                                  ActionInputHolder.Button key, boolean isUsed) {
		displayMapping(poseStack, screenWidth, screenHeight, index, key,
                key.getName(), isUsed, null);
	}
    
    protected void displayMapping(PoseStack poseStack, int screenWidth, int screenHeight, int index,
                                  ActionInputHolder.Button key) {
    	displayMapping(poseStack, screenWidth, screenHeight, index, key, key.isPressed(), null);
    }
    
    protected void displayMapping(PoseStack poseStack, int screenWidth, int screenHeight, int index,
                                  ActionInputHolder.Button key, String setting) {
    	displayMapping(poseStack, screenWidth, screenHeight, index, key, key.isPressed(), setting);
    }
    
    protected void displayMapping(PoseStack poseStack, int screenWidth, int screenHeight, int index,
                                  ActionInputHolder.Button key, Component mapName, boolean isUsed) {
    	displayMapping(poseStack, screenWidth, screenHeight, index, key, mapName, isUsed, null);
    }
    
    protected void displayMapping(PoseStack poseStack, int screenWidth, int screenHeight, int index,
                                  ActionInputHolder.Button key, Component mapName) {
    	displayMapping(poseStack, screenWidth, screenHeight, index, key, mapName, key.isPressed(), null);
    }

	@Override
	protected boolean shouldRender(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
		if (defaultRenderConditions()) return false;
		if (!(getPlayerVehicle() instanceof EntityRidablePart seat)) return false;

		EntityVehicle vehicle = seat.getParentVehicle();
		return vehicle != null;
	}

	@Override
	protected void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
		EntityRidablePart seat = (EntityRidablePart) getPlayerVehicle();
		assert seat != null;

		EntityVehicle vehicle = seat.getParentVehicle();
		assert vehicle != null;

		boolean isPilot = seat.isPilotSeat(), isCoPilot = seat.isCoPilotSeat();
		int index = 0;
		// MOUSE MODE
		if (isPilot) displayMapping(poseStack, screenWidth, screenHeight, index++, ClientInputManager.MOUSE_MODE,
				!DSCClientInputs.getMouseMode().isLockedForward(), DSCClientInputs.getMouseMode().name());
		// OPEN PLANE MENU
		displayMapping(poseStack, screenWidth, screenHeight, index++, ClientInputManager.VEHICLE_MENU);
		// DISMOUNT
		if (Config.CLIENT.customDismount.get()) displayMapping(poseStack, screenWidth, screenHeight, index++, ClientInputManager.DISMOUNT);
		// EJECT
		if (seat.canEject()) displayMapping(poseStack, screenWidth, screenHeight, index++, ClientInputManager.EJECT);
		// CHANGE SEAT
		displayMapping(poseStack, screenWidth, screenHeight, index++, ClientInputManager.CHANGE_SEAT);
		// LANDING GEAR
		if (isPilot && vehicle.canToggleLandingGear()) displayMapping(poseStack, screenWidth, screenHeight, index++,
                ClientInputManager.LANDING_GEAR, vehicle.isLandingGear(), vehicle.isLandingGear() ? "OUT"  : "IN");
		// BREAKS
		if (isPilot && vehicle.canGroundBrake()) displayMapping(poseStack, screenWidth, screenHeight, index++,
				vehicle.getStats().isPlane() ? ClientInputManager.SPECIAL2 : ClientInputManager.SPECIAL1,
				UtilMCText.translatable("info.dscombat.breaks"), vehicle.isGroundBraking());
		if (isPilot && vehicle.canAirBrake()) displayMapping(poseStack, screenWidth, screenHeight, index++,
				vehicle.getStats().isPlane() ? ClientInputManager.SPECIAL2 : ClientInputManager.SPECIAL1,
				UtilMCText.translatable("info.dscombat.breaks"), vehicle.isAirBreaking());
		// FLAPS DOWN
		if (isPilot && vehicle.canFlapsDown()) displayMapping(poseStack, screenWidth, screenHeight, index++,
                ClientInputManager.SPECIAL1, UtilMCText.translatable("info.dscombat.flaps_down"));
		// WEAPON ANGLED DOWN
		if (isPilot && vehicle.canAngleWeaponDown()) displayMapping(poseStack, screenWidth, screenHeight, index++,
                ClientInputManager.SPECIAL2, UtilMCText.translatable("info.dscombat.nose_down"));
		// HOVER
		if (isPilot && vehicle.canHover()) displayMapping(poseStack, screenWidth, screenHeight, index++,
                ClientInputManager.SPECIAL1, UtilMCText.translatable("info.dscombat.hover"));
		// FLARES
		if (isPilot && vehicle.hasFlares()) displayMapping(poseStack, screenWidth, screenHeight, index++,
                ClientInputManager.FLARE, vehicle.getFlareNum()+"");
		// CHAFF
		//if (isPilot && vehicle.hasChaff()) displayMapping(poseStack, screenWidth, screenHeight, index++,
		//		DSCKeys.chaffKey, vehicle.getChaffNum()+"");
		// CYCLE WEAPON
		if (isPilot || isCoPilot) displayMapping(poseStack, screenWidth, screenHeight, index++, ClientInputManager.WEAPON_CYCLE);
		// RADAR MODE
		if (vehicle.radarSystem.hasRadar()) {
			boolean warning = DSCClientInputs.getPreferredRadarMode() != vehicle.getRadarMode();
			displayMapping(poseStack, screenWidth, screenHeight, index++, ClientInputManager.RADAR_MODE,
					warning, DSCClientInputs.getPreferredRadarMode().name());
		}
		// SELECT RADAR PING
		if (vehicle.radarSystem.hasRadar()) displayMapping(poseStack, screenWidth, screenHeight, index++, ClientInputManager.PING_CYCLE);
		// GIMBAL MODE
		if (vehicle.getGimbalForPilotCamera() != null || seat.getCameraYOffset() != 0) displayMapping(poseStack,
				screenWidth, screenHeight, index++, ClientInputManager.GIMBAL,
				DSCClientInputs.isGimbalMode(), DSCClientInputs.isGimbalMode() ? "ON" : "OFF");
		// AFTERBURNER
		if (vehicle.canUseAfterburner()) displayMapping(poseStack, screenWidth, screenHeight, index++,
                ClientInputManager.AFTERBURNER, DSCClientInputs.isAfterBurner(),
                DSCClientInputs.isAfterBurner() ? "ON" : "OFF");
		// TURN ASSIST
		if (vehicle.canUseTurnAssist()) displayMapping(poseStack, screenWidth, screenHeight, index++,
                ClientInputManager.TURN_ASSIST, DSCClientInputs.isTurnAssist(),
                DSCClientInputs.isTurnAssist() ? "ON" : "OFF");
        // CAMERA TRACK
        if (vehicle.radarSystem.hasRadar()) displayMapping(poseStack, screenWidth, screenHeight, index++,
                ClientInputManager.CAMERA_TRACK_TARGET, DSCClientInputs.isCameraTrackTarget(),
                DSCClientInputs.isCameraTrackTarget() ? "ON" : "OFF");
	}

	@Override
	protected @NotNull String componentId() {
		return "dscombat_key_binds";
	}
}
