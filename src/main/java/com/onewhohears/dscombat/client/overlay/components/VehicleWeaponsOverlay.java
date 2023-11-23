package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.client.overlay.WeaponTabComponent;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import java.util.List;
import java.util.Objects;

import static com.onewhohears.dscombat.client.overlay.WeaponTabComponent.TAB_HEIGHT;

public class VehicleWeaponsOverlay extends VehicleOverlayComponent {
    public static final double[] SPACINGS = {0.0, 8.0, 14.0, 18.0, 20.0, 22.0, 23.0, 24.0};
    //private static final MutableComponent WEAPON_SELECT = Component.literal("->");

    private static VehicleWeaponsOverlay INSTANCE;

    protected boolean selectingWeaponState;
    protected boolean weaponChangeQueued;

    protected float frame;
    protected int superFrame;

    public static void renderIfAllowed(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (Objects.isNull(INSTANCE)) INSTANCE = new VehicleWeaponsOverlay();
        INSTANCE.render(poseStack, screenWidth, screenHeight);
    }
    
    private VehicleWeaponsOverlay() {}

    @Override
    protected void render(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (!(getPlayerRootVehicle() instanceof EntityVehicle vehicle)) return;

        List<WeaponData> weapons = vehicle.weaponSystem.getWeapons();
        WeaponData selectedWeapon = vehicle.weaponSystem.getSelected();

        if (weapons == null || weapons.isEmpty()) return;
        if (selectedWeapon == null) return;

        double yPlacement = screenHeight - TAB_HEIGHT - 13;
        int blitPosition = 1;

        if (!weaponChangeQueued) {
            WeaponTabComponent.drawWeaponName(poseStack, selectedWeapon.getDisplayNameComponent(), 13, yPlacement, blitPosition - 2);
            WeaponTabComponent.drawTab(poseStack, 13, yPlacement, blitPosition, 0, false);
            WeaponTabComponent.drawWeapon(poseStack, selectedWeapon, 13, yPlacement, blitPosition + 1, 0, false, false, false);
        }

        // TODO: weapon display names
        // TODO: weapon type codes
        // TODO: selected weapon highlighting
        // TODO: ammo count

        /* lmao
        int yPos=1, xPos, weaponSelectWidth=getFont().width(WEAPON_SELECT)+1, maxNameWidth=46, maxTypeWidth=10;
        int color1=0x7340bf, color2=0x00ff00, color;

        // CONTROLS
        String text;
        if (maxNameWidth < 50) maxNameWidth = 50;
        // FLARES
        if (vehicle.hasFlares()) {
            xPos = 1+weaponSelectWidth;
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
        // FREE LOOK
        xPos = 1+weaponSelectWidth;
        String key = DSCKeys.mouseModeKey.getKey().getDisplayName().getString();
        if (vehicle.driverCanFreeLook()) {
            text = "MouseMode("+key+")";
            color = color1;
        } else {
            text = "FreeLook("+key+")";
            color = color2;
        }
        drawString(poseStack, getFont(),
                text, xPos, yPos, color);
        yPos += 10;
        // LANDING GEAR
        if (vehicle.canToggleLandingGear()) {
            xPos = 1+weaponSelectWidth;
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
            xPos = 1+weaponSelectWidth;
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
            xPos = 1+weaponSelectWidth;
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
            xPos = 1+weaponSelectWidth;
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
            xPos = 1+weaponSelectWidth;
            text = DSCKeys.specialKey.getKey().getDisplayName().getString();
            if (vehicle.inputs.special) color = color2;
            else color = color1;
            drawString(poseStack, getFont(),
                    "Hover("+text+")",
                    xPos, yPos, color);
            yPos += 10;
        }
        // PLAYERS ONLY
        if (vehicle.radarSystem.hasRadar()) {
            xPos = 1+weaponSelectWidth;
            if (vehicle.getRadarMode().isOff()) color = color2;
            else color = color1;
            drawString(poseStack, getFont(),
                    "RMode("+DSCKeys.radarModeKey.getKey().getDisplayName().getString()+")",
                    xPos, yPos, color);
            xPos += maxNameWidth;
            text = vehicle.getRadarMode().name();
            drawString(poseStack, getFont(),
                    text, xPos, yPos, color);
            // what is this
            yPos += 10;
        }

         */
    }

    private static void renderSelectedWeapon(PoseStack stack) {

    }

    private static void renderSelectionBox(PoseStack stack) {

    }
}
