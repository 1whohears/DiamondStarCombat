package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.client.overlay.WeaponTabComponent;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Objects;

import static com.onewhohears.dscombat.client.overlay.WeaponTabComponent.TAB_HEIGHT;

public class VehicleWeaponsOverlay extends VehicleOverlayComponent {
    public static final int[] SPACINGS = {24, 21, 18, 12, 0};
    //private static final MutableComponent WEAPON_SELECT = Component.literal("->");
    public static final Component SAFETY = Component.translatable("ui.dscombat.no_weapon");

    private static VehicleWeaponsOverlay INSTANCE;

    protected boolean selectingWeaponState;
    protected boolean weaponChangeQueued;
    protected int weaponChangeCountdown;

    protected WeaponData selectedWeapon;

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

        queueWeaponChange();

        List<WeaponData> weapons = vehicle.weaponSystem.getWeapons();
        WeaponData selectedWeapon = vehicle.weaponSystem.getSelected();

        if (weapons == null || weapons.isEmpty()) return;
        if (selectedWeapon == null) return;

        double yPlacement = screenHeight - TAB_HEIGHT - 13;
        int blitPosition = 1;

        if (!weaponChangeQueued) {
            this.selectedWeapon = selectedWeapon;
            WeaponTabComponent.drawWeaponName(poseStack, selectedWeapon.getDisplayNameComponent(), 13, yPlacement, blitPosition - 2);
            WeaponTabComponent.drawTab(poseStack, 13, yPlacement, blitPosition, 0, false);
            WeaponTabComponent.drawWeapon(poseStack, selectedWeapon, 13, yPlacement, blitPosition + 1, 0, false, false, false);

            poseStack.pushPose();
            poseStack.translate(0, 0, blitPosition + 2);
            if (!selectedWeapon.isNoWeapon()) drawString(poseStack, getFont(), selectedWeapon.getCurrentAmmo() + "/" + selectedWeapon.getMaxAmmo(), 16, (int) (yPlacement + 14), 0xe6e600);
            // renders weapon code - drawString(poseStack, getFont(), selectedWeapon.getWeaponTypeCode(), 16, (int) yPlacement + 4, 0xe6e600);
            poseStack.popPose();
        } else {
            int weaponTabsToRender = Math.min(weapons.size(), 5);
            int indexOfSelectedWeapon = weapons.indexOf(selectedWeapon);

            for (int i = 0; i < weaponTabsToRender; i++) {
                int shiftedIndex = indexOfSelectedWeapon - i;
                if (shiftedIndex < 0) shiftedIndex += weapons.size() - 1;
                int newYPos = (int) (yPlacement - (24 * i));

                WeaponData weaponAt = weapons.get(shiftedIndex);

                WeaponTabComponent.drawTab(poseStack, 13, newYPos, blitPosition, 0, false);
                WeaponTabComponent.drawWeapon(poseStack, weaponAt, 13, newYPos, blitPosition + 1, 0, false, false, false);
                poseStack.pushPose();
                poseStack.translate(0, 0, blitPosition + 2);
                if (!weaponAt.isNoWeapon()) {
                    drawString(poseStack, getFont(), weaponAt.getCurrentAmmo() + "/" + selectedWeapon.getMaxAmmo(), 16, newYPos + 14, 0xe6e600);
                } else {
                    drawString(poseStack, getFont(), SAFETY, 16, newYPos + 14, 0xff5555);
                }
                drawString(poseStack, getFont(), weaponAt.getWeaponTypeCode(), 16, newYPos + 4, 0xe6e600);
                poseStack.popPose();
            }

            WeaponTabComponent.drawWeaponName(poseStack, selectedWeapon.getDisplayNameComponent(), 13, yPlacement - (24 * (weaponTabsToRender - 1)), blitPosition - 2);
        }

        // TODO: icons indicating how the weapon works e.g. infrared, radar
        // TODO: selected weapon highlighting

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

    public static void queueWeaponChange() {
        INSTANCE.weaponChangeCountdown = 4;
        INSTANCE.weaponChangeQueued = true;
    }

    private static void renderSelectedWeapon(PoseStack stack) {

    }

    private static void renderSelectionBox(PoseStack stack) {

    }
}
