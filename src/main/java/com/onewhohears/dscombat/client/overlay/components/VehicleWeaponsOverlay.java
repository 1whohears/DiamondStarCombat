package com.onewhohears.dscombat.client.overlay.components;

import static com.onewhohears.dscombat.client.overlay.WeaponTabComponent.TAB_HEIGHT;

import java.util.List;
import java.util.Objects;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.client.overlay.WeaponTabComponent;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

public class VehicleWeaponsOverlay extends VehicleOverlayComponent {
    public static final int[] SPACINGS = {24, 21, 18, 12, 0};
    //private static final MutableComponent WEAPON_SELECT = Component.literal("->");
    public static final Component SAFETY = Component.translatable("ui.dscombat.no_weapon");

    private static VehicleWeaponsOverlay INSTANCE;

    protected boolean weaponChangeState;
    protected boolean weaponChangeQueued;
    protected int weaponChangeCountdown;
    protected int selectedWeapon;

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
        int selectedIndex = vehicle.weaponSystem.getSelectedIndex();

        if (weapons == null || weapons.isEmpty()) return;
        if (selectedWeapon == null) return;

        if (selectedIndex != this.selectedWeapon) enableWeaponChangeState();
        this.selectedWeapon = selectedIndex;

        if (this.weaponChangeCountdown <= 0) this.weaponChangeState = false;

        double yPlacement = screenHeight - TAB_HEIGHT - 13;
        int blitPosition = 1;

        if (!this.weaponChangeState) {
            WeaponTabComponent.drawWeaponName(poseStack, selectedWeapon.getDisplayNameComponent(), 13, yPlacement, blitPosition - 2);
            WeaponTabComponent.drawTab(poseStack, 13, yPlacement, blitPosition, 0, false);
            WeaponTabComponent.drawWeapon(poseStack, selectedWeapon, 13, yPlacement, blitPosition + 1, 0, false, false, false);

            poseStack.pushPose();
            poseStack.translate(0, 0, blitPosition + 2);
            if (!selectedWeapon.isNoWeapon()) drawString(poseStack, getFont(), selectedWeapon.getCurrentAmmo() + "/" + selectedWeapon.getMaxAmmo(), 16, (int) (yPlacement + 14), 0xe6e600);
            drawString(poseStack, getFont(), selectedWeapon.getWeaponTypeCode(), 16, (int) yPlacement + 4, 0xe6e600);
            poseStack.popPose();
        } else {
            int weaponTabsToRender = Math.min(weapons.size(), 5);

            for (int i = 0; i < weaponTabsToRender; i++) {
                int shiftedIndex = selectedIndex - i;
                if (shiftedIndex < 0) shiftedIndex = ((shiftedIndex % weapons.size()) + weapons.size()) % weapons.size();
                int newYPos = (int) (yPlacement - (24 * i));

                WeaponData weaponAt = weapons.get(shiftedIndex);

                WeaponTabComponent.drawTab(poseStack, 13, newYPos, blitPosition, 0, false);
                WeaponTabComponent.drawWeapon(poseStack, weaponAt, 13, newYPos, blitPosition + 1, 0, false, false, false);
                poseStack.pushPose();
                poseStack.translate(0, 0, blitPosition + 3);
                if (!weaponAt.isNoWeapon()) {
                    drawString(poseStack, getFont(), weaponAt.getCurrentAmmo() + "/" + weaponAt.getMaxAmmo(), 16, newYPos + 14, 0xe6e600);
                } else {
                    drawString(poseStack, getFont(), SAFETY, 16, newYPos + 14, 0xff5555);
                }
                drawString(poseStack, getFont(), weaponAt.getDisplayNameComponent(), 16, newYPos + 4, 0xffffff);
                poseStack.popPose();
            }

            WeaponTabComponent.renderSelectionBox(poseStack, 13, yPlacement, blitPosition + 2);
            this.weaponChangeCountdown--;
        }

        // TODO: icons indicating how the weapon works e.g. infrared, radar

        // TODO 0.1 until a better way is made, these controls and other info need to be displayed somewhere
        int yPos=2, xPos, leftSpace=2, maxNameWidth=46;
        int color1=0x7340bf, color2=0x00ff00, color;

        // CONTROLS
        String text;
        if (maxNameWidth < 50) maxNameWidth = 50;
        // MOUSE MODE
        xPos = 1+leftSpace;
        String key = DSCKeys.mouseModeKey.getKey().getDisplayName().getString();
        text = DSCClientInputs.getMouseMode().name()+"("+key+")";
        if (DSCClientInputs.getMouseMode().isLockedForward()) color = color1;
        else color = color2;
        drawString(poseStack, getFont(),
                text, xPos, yPos, color);
        yPos += 10;
        // FLARES
        if (vehicle.hasFlares()) {
            xPos = 1+leftSpace;
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
            xPos = 1+leftSpace;
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
            xPos = 1+leftSpace;
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
            xPos = 1+leftSpace;
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
            xPos = 1+leftSpace;
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
            xPos = 1+leftSpace;
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
            xPos = 1+leftSpace;
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
            xPos = 1+leftSpace;
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
    }

    public static void queueWeaponChange() {
        INSTANCE.weaponChangeQueued = true;
    }

    public static void enableWeaponChangeState() {
        INSTANCE.weaponChangeCountdown = 200;
        INSTANCE.weaponChangeState = true;

        //noinspection DataFlowIssue (the context in which this is called necessarily has a LocalPlayer existing on client)
        getPlayer().playSound(SoundEvents.UI_BUTTON_CLICK);
    }

    private static void renderSelectedWeapon(PoseStack stack) {

    }

    private static void renderSelectionBox(PoseStack stack) {

    }
}
