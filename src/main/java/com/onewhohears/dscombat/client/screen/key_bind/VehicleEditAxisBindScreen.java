package com.onewhohears.dscombat.client.screen.key_bind;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.ActionInput;
import com.onewhohears.dscombat.client.input.ActionInputHolder;
import com.onewhohears.dscombat.client.input.ClientInputManager;
import com.onewhohears.dscombat.client.screen.VehicleSubScreen;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class VehicleEditAxisBindScreen extends VehicleSubScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/background/vehicle_other_screen.png");

    private static final int imageWidth = 240, imageHeight = 180;
    private static final int textureSize = 256;

    private final int page;
    private final ActionInputHolder.Axis action;

    private final List<Button> priDSCKeyAxisButtons = new ArrayList<>();
    private final List<Button> secDSCKeyAxisButtons = new ArrayList<>();
    private final List<Button> priControllerButtons = new ArrayList<>();
    private final List<Button> secControllerButtons = new ArrayList<>();

    public VehicleEditAxisBindScreen(int page, ActionInputHolder.Axis action) {
        super(action.getNameString(), BG_TEXTURE, imageWidth, imageHeight, textureSize, textureSize);
        this.page = page;
        this.action = action;
    }

    @Override
    protected void init() {
        super.init();
        // PRIMARY
        // Action Type Cycle
        positionWidgetGrid(new CycleButton.Builder<>(ActionInput.AxisType::getTypeName)
                        .withValues(ActionInput.AxisType.values())
                        .withInitialValue(action.getPrimaryAction().getAxisType())
                        .create(0, 0, 20, 20,
                                UtilMCText.translatable("ui.dscombat.primary"),
                                (button, value) -> changePrimaryType(value)),
                ROWS, 1, 1, padding);
        changePrimaryType(action.getPrimaryAction().getAxisType());
        // DSC Key Axis
        // negative
        Button DSC_KEY_AXIS_NEG_PRI = new Button(0, 0, 20, 20,
                UtilMCText.translatable("ui.dscombat.negative_key"),
                onPress -> Minecraft.getInstance().setScreen(new SelectBindScreen.DSCKeyAxis(page, action, true, false)));
        positionWidgetGrid(DSC_KEY_AXIS_NEG_PRI, ROWS, COLUMNS, 4, padding);
        // positive
        Button DSC_KEY_AXIS_POS_PRI = new Button(0, 0, 20, 20,
                UtilMCText.translatable("ui.dscombat.positive_key"),
                onPress -> Minecraft.getInstance().setScreen(new SelectBindScreen.DSCKeyAxis(page, action, true, true)));
        positionWidgetGrid(DSC_KEY_AXIS_POS_PRI, ROWS, COLUMNS, 5, padding);
        priDSCKeyAxisButtons.clear();
        priDSCKeyAxisButtons.add(DSC_KEY_AXIS_NEG_PRI);
        priDSCKeyAxisButtons.add(DSC_KEY_AXIS_POS_PRI);
        // Controller Axis

        priControllerButtons.clear();
        // SECONDARY
        // Action Type Cycle
        positionWidgetGrid(new CycleButton.Builder<>(ActionInput.AxisType::getTypeName)
                        .withValues(ActionInput.AxisType.values())
                        .withInitialValue(action.getSecondaryAction().getAxisType())
                        .create(0, 0, 20, 20,
                                UtilMCText.translatable("ui.dscombat.secondary"),
                                (button, value) -> changeSecondaryType(value)),
                ROWS, 1, 3, padding);
        changeSecondaryType(action.getSecondaryAction().getAxisType());
        // DSC Key Axis
        // negative
        Button DSC_KEY_AXIS_NEG_SEC = new Button(0, 0, 20, 20,
                UtilMCText.translatable("ui.dscombat.negative_key"),
                onPress -> Minecraft.getInstance().setScreen(new SelectBindScreen.DSCKeyAxis(page, action, false, false)));
        positionWidgetGrid(DSC_KEY_AXIS_NEG_SEC, ROWS, COLUMNS, 8, padding);
        // positive
        Button DSC_KEY_AXIS_POS_SEC = new Button(0, 0, 20, 20,
                UtilMCText.translatable("ui.dscombat.positive_key"),
                onPress -> Minecraft.getInstance().setScreen(new SelectBindScreen.DSCKeyAxis(page, action, false, true)));
        positionWidgetGrid(DSC_KEY_AXIS_POS_SEC, ROWS, COLUMNS, 9, padding);
        secDSCKeyAxisButtons.clear();
        secDSCKeyAxisButtons.add(DSC_KEY_AXIS_NEG_SEC);
        secDSCKeyAxisButtons.add(DSC_KEY_AXIS_POS_SEC);
        // Controller Axis

        secControllerButtons.clear();
    }

    private void changePrimaryType(ActionInput.AxisType type) {
        switch (type) {
            case UNBOUND_AXIS -> {
                priDSCKeyAxisButtons.forEach(button -> button.visible = false);
                priControllerButtons.forEach(button -> button.visible = false);
            }
            case DSC_KEY_AXIS -> {
                priDSCKeyAxisButtons.forEach(button -> button.visible = true);
                priControllerButtons.forEach(button -> button.visible = false);
            }
            case CONTROLLER_AXIS -> {
                priDSCKeyAxisButtons.forEach(button -> button.visible = false);
                priControllerButtons.forEach(button -> button.visible = true);
            }
        }
    }

    private void changeSecondaryType(ActionInput.AxisType type) {
        switch (type) {
            case UNBOUND_AXIS -> {
                secDSCKeyAxisButtons.forEach(button -> button.visible = false);
                secControllerButtons.forEach(button -> button.visible = false);
            }
            case DSC_KEY_AXIS -> {
                secDSCKeyAxisButtons.forEach(button -> button.visible = true);
                secControllerButtons.forEach(button -> button.visible = false);
            }
            case CONTROLLER_AXIS -> {
                secDSCKeyAxisButtons.forEach(button -> button.visible = false);
                secControllerButtons.forEach(button -> button.visible = true);
            }
        }
    }

    @Override
    protected Screen getBackScreen() {
        return new VehicleKeyBindsScreen(page);
    }

}
