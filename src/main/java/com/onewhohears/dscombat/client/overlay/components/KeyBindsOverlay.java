package com.onewhohears.dscombat.client.overlay.components;

import java.util.Objects;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

public class KeyBindsOverlay extends VehicleOverlayComponent {
	
	private static KeyBindsOverlay INSTANCE;
	
	public static void renderIfAllowed(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (Objects.isNull(INSTANCE)) INSTANCE = new KeyBindsOverlay();
        INSTANCE.render(poseStack, screenWidth, screenHeight);
    }
    
    private KeyBindsOverlay() {}
	
	@Override
	protected void render(PoseStack poseStack, int screenWidth, int screenHeight) {
		if (!(getPlayerRootVehicle() instanceof EntityVehicle vehicle)) return;
		// TODO 0.1 until a better way is made, these controls and other info need to be displayed somewhere
        int yPos=2, xPos, leftSpace=3, maxNameWidth=46;
        int color1=0x00ff00, color2=0xffff00, color;
        String text;
        if (maxNameWidth < 50) maxNameWidth = 50;
        // MOUSE MODE
        xPos = leftSpace;
        String key = DSCKeys.mouseModeKey.getKey().getDisplayName().getString();
        text = DSCClientInputs.getMouseMode().name()+"("+key+")";
        if (DSCClientInputs.getMouseMode().isLockedForward()) color = color1;
        else color = color2;
        drawString(poseStack, getFont(),
                text, xPos, yPos, color);
        yPos += 10;
        // FLARES
        if (vehicle.hasFlares()) {
            xPos = leftSpace;
            if (vehicle.inputs.flare) color = color2;
            else color = color1;
            drawString(poseStack, getFont(),
                    "Flares("+ DSCKeys.flareKey.getKey().getDisplayName().getString()+")",
                    xPos, yPos, color);
            xPos += maxNameWidth;
            drawString(poseStack, getFont(),
                    vehicle.getFlareNum()+"",
                    xPos, yPos, color);
            yPos += 10;
        }
        // LANDING GEAR
        if (vehicle.canToggleLandingGear()) {
            xPos = leftSpace;
            if (vehicle.isLandingGear()) {
                text = "OUT";
                color = color2;
            } else {
                text = "IN";
                color = color1;
            }
            drawString(poseStack, getFont(),
                    "Gear("+DSCKeys.landingGear.getKey().getDisplayName().getString()+")",
                    xPos, yPos, color);
            xPos += maxNameWidth;
            drawString(poseStack, getFont(),
                    text, xPos, yPos, color);
            yPos += 10;
        }
        // BREAKS
        if (vehicle.canBrake()) {
            xPos = leftSpace;
            if (vehicle.getAircraftType() == EntityVehicle.AircraftType.PLANE)
                text = DSCKeys.special2Key.getKey().getDisplayName().getString();
            else text = DSCKeys.specialKey.getKey().getDisplayName().getString();
            if (vehicle.isBraking()) color = color2;
            else color = color1;
            drawString(poseStack, getFont(),
                    "Break("+text+")",
                    xPos, yPos, color);
            yPos += 10;
        }
        // FLAPS DOWN
        if (vehicle.canFlapsDown()) {
            xPos = leftSpace;
            text = DSCKeys.specialKey.getKey().getDisplayName().getString();
            if (vehicle.inputs.special) color = color2;
            else color = color1;
            drawString(poseStack, getFont(),
                    "FlapsDown("+text+")",
                    xPos, yPos, color);
            yPos += 10;
        }
        // WEAPON ANGLED DOWN
        if (vehicle.canAngleWeaponDown()) {
            xPos = leftSpace;
            text = DSCKeys.special2Key.getKey().getDisplayName().getString();
            if (vehicle.inputs.special2) color = color2;
            else color = color1;
            drawString(poseStack, getFont(),
                    "AimDown("+text+")",
                    xPos, yPos, color);
            yPos += 10;
        }
        // HOVER
        if (vehicle.canHover()) {
            xPos = leftSpace;
            text = DSCKeys.specialKey.getKey().getDisplayName().getString();
            if (vehicle.inputs.special) color = color2;
            else color = color1;
            drawString(poseStack, getFont(),
                    "Hover("+text+")",
                    xPos, yPos, color);
            yPos += 10;
        }
        // RADAR MODE
        if (vehicle.radarSystem.hasRadar()) {
            xPos = leftSpace;
            if (vehicle.getRadarMode().isOff()) color = color2;
            else color = color1;
            drawString(poseStack, getFont(),
                    "RMode("+DSCKeys.radarModeKey.getKey().getDisplayName().getString()+")",
                    xPos, yPos, color);
            xPos += maxNameWidth;
            text = vehicle.getRadarMode().name();
            drawString(poseStack, getFont(),
                    text, xPos, yPos, color);
            yPos += 10;
        }
        // GIMBAL MODE
        if (vehicle.getGimbalForPilotCamera() != null) {
            xPos = leftSpace;
            if (DSCClientInputs.isGimbalMode()) {
            	color = color2;
            	text = "ON";
            } else {
            	color = color1;
            	text = "OFF";
            }
            drawString(poseStack, getFont(),
                    "Gimbal("+DSCKeys.gimbalKey.getKey().getDisplayName().getString()+")",
                    xPos, yPos, color);
            xPos += maxNameWidth;
            drawString(poseStack, getFont(),
                    text, xPos, yPos, color);
            yPos += 10;
        }
        // DISMOUNT
        if (Config.CLIENT.customDismount.get()) {
        	xPos = leftSpace;
        	color = color1;
        	drawString(poseStack, getFont(),
        		"Dismount("+DSCKeys.dismount.getKey().getDisplayName().getString()+")",
        		xPos, yPos, color);
        	yPos += 10;
        }
        // CHANGE SEAT
        xPos = leftSpace;
        color = color1;
    	drawString(poseStack, getFont(),
        		"ChangeSeat("+DSCKeys.changeSeat.getKey().getDisplayName().getString()+")",
        		xPos, yPos, color);
        yPos += 10;
        // OPEN PLANE MENU
        xPos = leftSpace;
        color = color1;
    	drawString(poseStack, getFont(),
        		"VehicleMenu("+DSCKeys.planeMenuKey.getKey().getDisplayName().getString()+")",
        		xPos, yPos, color);
        yPos += 10;
	}

}
