package com.onewhohears.dscombat.client.overlay.components;

import static com.onewhohears.dscombat.client.overlay.WeaponTabComponent.TAB_HEIGHT;

import java.util.List;
import java.util.Objects;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.client.overlay.WeaponTabComponent;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityTurret;

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
    	double yPlacement = screenHeight - TAB_HEIGHT - 13;
        int blitPosition = 1;
    	if (getPlayerVehicle() instanceof EntityTurret turret && turret.getWeaponData() != null) {
    		drawWeapon(poseStack, turret.getWeaponData(), yPlacement, blitPosition);
    		return;
    	}
        if (!(getPlayerRootVehicle() instanceof EntityVehicle vehicle)) return;

        List<WeaponData> weapons = vehicle.weaponSystem.getWeapons();
        WeaponData selectedWeapon = vehicle.weaponSystem.getSelected();
        int selectedIndex = vehicle.weaponSystem.getSelectedIndex();

        if (weapons == null || weapons.isEmpty()) return;
        if (selectedWeapon == null) return;

        if (selectedIndex != this.selectedWeapon) enableWeaponChangeState();
        this.selectedWeapon = selectedIndex;

        if (this.weaponChangeCountdown <= 0) this.weaponChangeState = false;

        if (!this.weaponChangeState) {
        	drawWeapon(poseStack, selectedWeapon, yPlacement, blitPosition);
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
    }

    public static void queueWeaponChange() {
        INSTANCE.weaponChangeQueued = true;
    }

    public static void enableWeaponChangeState() {
        INSTANCE.weaponChangeCountdown = 200;
        INSTANCE.weaponChangeState = true;

        // no inspection DataFlowIssue (the context in which this is called necessarily has a LocalPlayer existing on client)
        getPlayer().playSound(SoundEvents.UI_BUTTON_CLICK);
    }
    
    private static void drawWeapon(PoseStack poseStack, WeaponData selectedWeapon, double yPlacement, int blitPosition) {
    	WeaponTabComponent.drawWeaponName(poseStack, selectedWeapon.getDisplayNameComponent(), 13, yPlacement, blitPosition - 2);
        WeaponTabComponent.drawTab(poseStack, 13, yPlacement, blitPosition, 0, false);
        WeaponTabComponent.drawWeapon(poseStack, selectedWeapon, 13, yPlacement, blitPosition + 1, 0, false, false, false);

        poseStack.pushPose();
        poseStack.translate(0, 0, blitPosition + 2);
        if (!selectedWeapon.isNoWeapon()) drawString(poseStack, getFont(), selectedWeapon.getCurrentAmmo() + "/" + selectedWeapon.getMaxAmmo(), 16, (int) (yPlacement + 14), 0xe6e600);
        drawString(poseStack, getFont(), selectedWeapon.getWeaponTypeCode(), 16, (int) yPlacement + 4, 0xe6e600);
        poseStack.popPose();
    }
}
