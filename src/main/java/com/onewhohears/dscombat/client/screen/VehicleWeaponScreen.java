package com.onewhohears.dscombat.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.screen.widget.WeaponButton;
import com.onewhohears.dscombat.data.weapon.WeaponSystem;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class VehicleWeaponScreen extends VehicleSubScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/background/vehicle_weapons_screen.png");

    private static final int imageWidth = 240, imageHeight = 180;
    private static final int textureSize = 256;

    protected VehicleWeaponScreen() {
        super("screen.dscombat.vehicle_weapon_screen",
                BG_TEXTURE, imageWidth, imageHeight, textureSize, textureSize);
    }

    @Override
    protected void init() {
        vertical_widget_shift = 10;
        COLUMNS = 7;
        padding = 0;
        super.init();
        // TARGET MODE
        positionWidgetGrid(CycleButton.<DSCClientInputs.TargetMode>builder(value -> UtilMCText.translatable(value.getTranslatable()))
                        .withValues(DSCClientInputs.TargetMode.values())
                        .withInitialValue(DSCClientInputs.getTargetMode())
                        .create(0, 0, 20, 20,
                                UtilMCText.translatable("ui.dscombat.target_mode"),
                                onTargetModeCycle()),
                ROWS, COLUMNS, 1, padding, 4);
        // GIMBAL MODE
        positionWidgetGrid(CycleButton.onOffBuilder(DSCClientInputs.isGimbalMode())
                        .create(0, 0, 20, 20,
                                UtilMCText.translatable("ui.dscombat.gimbal_mode"),
                                onGimbalToggle()),
                ROWS, COLUMNS, 5, padding, 2);
        // TARGET POSITION X
        vertical_widget_shift = 48;
        EditBox xPosBox = new EditBox(getMinecraft().font, 0, 0, 20, 20, UtilMCText.empty());
        positionWidgetGrid(xPosBox, 9, 3, 0, 2);
        xPosBox.setValue((int)getVehicle().weaponSystem.getTargetPos().x()+"");
        xPosBox.setTextColor(0xFFFFFF);
        xPosBox.setResponder(onTargetPosCoordChange(0));
        // TARGET POSITION Y
        EditBox yPosBox = new EditBox(getMinecraft().font, 0, 0, 20, 20, UtilMCText.empty());
        positionWidgetGrid(yPosBox, 9, 3, 1, 2);
        yPosBox.setValue((int)getVehicle().weaponSystem.getTargetPos().y()+"");
        yPosBox.setTextColor(0xFFFFFF);
        yPosBox.setResponder(onTargetPosCoordChange(1));
        // TARGET POSITION Z
        EditBox zPosBox = new EditBox(getMinecraft().font, 0, 0, 20, 20, UtilMCText.empty());
        positionWidgetGrid(zPosBox, 9, 3, 2, 2);
        zPosBox.setValue((int)getVehicle().weaponSystem.getTargetPos().z()+"");
        zPosBox.setTextColor(0xFFFFFF);
        zPosBox.setResponder(onTargetPosCoordChange(2));
        // SELECT WEAPON
        vertical_widget_shift = 72;
        WeaponSystem system = getVehicle().weaponSystem;
        List<WeaponInstance<?>> weapons = system.getWeapons();
        for (int i = 0; i < weapons.size(); ++i) {
            WeaponStats stats = weapons.get(i).getStats();
            positionWidgetGrid(new WeaponButton(0, 0, 20, 20, stats.getWeaponIcon(),
                            onSelectWeapon(i), stats.getDisplayNameComponent(), system, i),
                    9, 3, i, 0, 1);
        }
    }

    private Button.OnPress onSelectWeapon(int weaponIndex) {
        return button -> getVehicle().weaponSystem.setSelected(weaponIndex);
    }

    private CycleButton.OnValueChange<DSCClientInputs.TargetMode> onTargetModeCycle() {
        return (button, value) -> DSCClientInputs.setTargetMode(value);
    }

    private Consumer<String> onTargetPosCoordChange(int axis) {
        return coord -> {
            try {
                double number = Double.parseDouble(coord);
                setTargetPos(axis, number);
            } catch(NumberFormatException e) {
                setTargetPos(axis, 0);
            }
        };
    }

    private void setTargetPos(int axis, double number) {
        WeaponSystem system = getVehicle().weaponSystem;
        Vec3 pos = system.getTargetPos();
        if (axis == 0) system.setTargetPos(new Vec3(number, pos.y, pos.z));
        else if (axis == 1) system.setTargetPos(new Vec3(pos.x, number, pos.z));
        else if (axis == 2) system.setTargetPos(new Vec3(pos.x, pos.y, number));
    }

    private CycleButton.OnValueChange<Boolean> onGimbalToggle() {
        return (button, value) -> DSCClientInputs.setGimbalMode(value);
    }

    @Override
    public void renderBackground(@NotNull PoseStack poseStack) {
        super.renderBackground(poseStack);
        getMinecraft().font.draw(poseStack, UtilMCText.translatable("info.dscombat.target_mode_pos"),
                guiX + left_padding, guiY + top_padding + 38, infoColor);
    }
}
