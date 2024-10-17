package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.jetbrains.annotations.NotNull;

public class KeyBindsOverlay extends VehicleOverlayComponent {
	
	private static final int DEFAULT_KEY_COLOR = 0x00ff00;
	private static final int USE_KEY_COLOR = 0xffff00;
	private static final int MAPPING_NAME_WIDTH = 80;
	private static final int KEY_NAME_WIDTH = 34;
	
	public static Component fixKeyName(KeyMapping key) {
        return switch (key.getKey().getValue()) {
            case InputConstants.KEY_RCONTROL -> UtilMCText.literal("R-CTRL");
            case InputConstants.KEY_LCONTROL -> UtilMCText.literal("L-CTRL");
            case InputConstants.KEY_LALT -> UtilMCText.literal("L-ALT");
            case InputConstants.KEY_RALT -> UtilMCText.literal("R-ALT");
            default -> key.getKey().getDisplayName();
        };
    }

	protected void displayMapping(GuiGraphics graphics, int screenWidth, int screenHeight, int index, KeyMapping key,
								  Component mapName, boolean isUsed, String setting) {
		int pY = 2 + 10 * index;
		int pX = 3;
		int pColor = DEFAULT_KEY_COLOR;
		if (isUsed) pColor = USE_KEY_COLOR;
		graphics.drawString(FONT, mapName, pX, pY, pColor, false);
		pX += MAPPING_NAME_WIDTH;
		graphics.drawString(FONT, fixKeyName(key), pX, pY, pColor, false);
		if (setting == null || setting.isEmpty()) return;
		pX += KEY_NAME_WIDTH;
		graphics.drawString(FONT, setting, pX, pY, pColor, false);
	}

	protected void displayMapping(GuiGraphics graphics, int screenWidth, int screenHeight, int index, KeyMapping key, boolean isUsed, String setting) {
		displayMapping(graphics, screenWidth, screenHeight, index, key, UtilMCText.translatable(key.getName()), isUsed, setting);
	}

	protected void displayMapping(GuiGraphics graphics, int screenWidth, int screenHeight, int index, KeyMapping key) {
		displayMapping(graphics, screenWidth, screenHeight, index, key, key.isDown(), null);
	}

	protected void displayMapping(GuiGraphics graphics, int screenWidth, int screenHeight, int index, KeyMapping key, String setting) {
		displayMapping(graphics, screenWidth, screenHeight, index, key, key.isDown(), setting);
	}

	protected void displayMapping(GuiGraphics graphics, int screenWidth, int screenHeight, int index, KeyMapping key, Component mapName, boolean isUsed) {
		displayMapping(graphics, screenWidth, screenHeight, index, key, mapName, isUsed, null);
	}

	protected void displayMapping(GuiGraphics graphics, int screenWidth, int screenHeight, int index, KeyMapping key, Component mapName) {
		displayMapping(graphics, screenWidth, screenHeight, index, key, mapName, key.isDown(), null);
	}


	@Override
	protected boolean shouldRender(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
		if (defaultRenderConditions()) return false;
		if (!(getPlayerVehicle() instanceof EntitySeat seat)) return false;

		EntityVehicle vehicle = seat.getParentVehicle();
		return vehicle != null;
	}

	@Override
	protected void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
		EntitySeat seat = (EntitySeat) getPlayerVehicle();
		assert seat != null;

		EntityVehicle vehicle = seat.getParentVehicle();
		assert vehicle != null;

		boolean isPilot = seat.isPilotSeat(), isCoPilot = seat.isCoPilotSeat();
		// TODO 0.1 until a better way is made, these controls and other info need to be displayed somewhere
		int index = 0;

		// MOUSE MODE
		if (isPilot) displayMapping(graphics, screenWidth, screenHeight, index++, DSCKeys.mouseModeKey,
				!DSCClientInputs.getMouseMode().isLockedForward(), DSCClientInputs.getMouseMode().name());

		// OPEN PLANE MENU
		if (isPilot) displayMapping(graphics, screenWidth, screenHeight, index++, DSCKeys.vehicleMenuKey);

		// OPEN PLANE STORAGE
		if (vehicle.partsManager.hasStorageBoxes()) displayMapping(graphics, screenWidth, screenHeight, index++, DSCKeys.vehicleStorageKey);

		// DISMOUNT
		if (Config.CLIENT.customDismount.get()) displayMapping(graphics, screenWidth, screenHeight, index++, DSCKeys.dismount);

		// EJECT
		if (seat.canEject()) displayMapping(graphics, screenWidth, screenHeight, index++, DSCKeys.eject);

		// CHANGE SEAT
		displayMapping(graphics, screenWidth, screenHeight, index++, DSCKeys.changeSeat);

		// LANDING GEAR
		if (isPilot && vehicle.canToggleLandingGear()) displayMapping(graphics, screenWidth, screenHeight, index++, DSCKeys.landingGear,
				vehicle.isLandingGear(), vehicle.isLandingGear() ? "OUT"  : "IN");

		// BRAKES
		if (isPilot && vehicle.canBrake()) displayMapping(graphics, screenWidth, screenHeight, index++,
				vehicle.getStats().isPlane() ? DSCKeys.special2Key : DSCKeys.specialKey,
				UtilMCText.literal("Brakes (S)"), vehicle.isBraking());

		// FLAPS DOWN
		if (isPilot && vehicle.canFlapsDown()) displayMapping(graphics, screenWidth, screenHeight, index++,
				DSCKeys.specialKey, UtilMCText.literal("Flaps Down (S1)"));

		// WEAPON ANGLED DOWN
		if (isPilot && vehicle.canAngleWeaponDown()) displayMapping(graphics, screenWidth, screenHeight, index++,
				DSCKeys.special2Key, UtilMCText.literal("Nose Down (S2)"));

		// HOVER
		if (isPilot && vehicle.canHover()) displayMapping(graphics, screenWidth, screenHeight, index++,
				DSCKeys.specialKey, UtilMCText.literal("Hover (S1)"));

		// FLARES
		if (isPilot && vehicle.hasFlares()) displayMapping(graphics, screenWidth, screenHeight, index++,
				DSCKeys.flareKey, vehicle.getFlareNum()+"");

		// CYCLE WEAPON
		if (isPilot || isCoPilot) displayMapping(graphics, screenWidth, screenHeight, index++, DSCKeys.weaponSelectKey);

		// RADAR MODE
		if (vehicle.radarSystem.hasRadar()) {
			boolean warning = DSCClientInputs.getPreferredRadarMode() != vehicle.getRadarMode();
			displayMapping(graphics, screenWidth, screenHeight, index++, DSCKeys.radarModeKey,
					warning, DSCClientInputs.getPreferredRadarMode().name());
		}

		// SELECT RADAR PING
		if (vehicle.radarSystem.hasRadar()) displayMapping(graphics, screenWidth, screenHeight, index++, DSCKeys.pingCycleKey);

		// GIMBAL MODE
		if (vehicle.getGimbalForPilotCamera() != null || seat.getCameraYOffset() != 0) displayMapping(graphics,
				screenWidth, screenHeight, index++, DSCKeys.gimbalKey,
				DSCClientInputs.isGimbalMode(), DSCClientInputs.isGimbalMode() ? "ON" : "OFF");
	}




	@Override
	protected @NotNull String componentId() {
		return "dscombat_key_binds";
	}
}
