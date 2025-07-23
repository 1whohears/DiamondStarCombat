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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VehicleEditButtonBindScreen extends VehicleSubScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/background/vehicle_other_screen.png");

    private static final int imageWidth = 240, imageHeight = 180;
    private static final int textureSize = 256;

    private final int page;
    private final ActionInputHolder.Button action;

    private final List<Button> priDSCKeyButtons = new ArrayList<>();
    private final List<Button> secDSCKeyButtons = new ArrayList<>();
    private final List<Button> priControllerButtons = new ArrayList<>();
    private final List<Button> secControllerButtons = new ArrayList<>();
    private final List<Button> priControllerAxisButtons = new ArrayList<>();
    private final List<Button> secControllerAxisButtons = new ArrayList<>();
    @Nullable Button priUnboundButton;
    @Nullable Button secUnboundButton;

    public VehicleEditButtonBindScreen(int page, ActionInputHolder.Button action) {
        super(action.getNameString(), BG_TEXTURE, imageWidth, imageHeight, textureSize, textureSize);
        this.page = page;
        this.action = action;
    }

    @Override
    protected void init() {
        super.init();
        // PRIMARY
        // Action Type Cycle
        positionWidgetGrid(new CycleButton.Builder<>(ActionInput.ButtonType::getTypeName)
                        .withValues(ActionInput.ButtonType.values())
                        .withInitialValue(action.getPrimaryAction().getButtonType())
                        .create(0, 0, 20, 20,
                                UtilMCText.translatable("ui.dscombat.primary"),
                                (button, value) -> changePrimaryType(value)),
                ROWS, 1, 1, padding);
        // Unbound
        priUnboundButton = new Button(0, 0, 20, 20,
                UtilMCText.translatable("ui.dscombat.save"),
                onPress -> {
                    action.setPrimaryAction(new ActionInput.UnboundButton());
                    ClientInputManager.saveKeyBinds();
                });
        positionWidgetGrid(priUnboundButton, ROWS, 1, 2, padding);
        // DSC Key Button
        Button PRI_DSC_KEY = new Button(0, 0, 20, 20,
                UtilMCText.translatable("ui.dscombat.edit_dsc_key_button"),
                onPress -> Minecraft.getInstance().setScreen(new SelectBindScreen.DSCKeyButton(page, action, true)));
        positionWidgetGrid(PRI_DSC_KEY, ROWS, 1, 2, padding);
        priDSCKeyButtons.clear();
        priDSCKeyButtons.add(PRI_DSC_KEY);
        // Controller Button
        Button PRI_CONTROLLER = new Button(0, 0, 20, 20,
                UtilMCText.translatable("ui.dscombat.edit_controller_button"),
                onPress -> Minecraft.getInstance().setScreen(new SelectBindScreen.ControllerButton(page, action, true)));
        positionWidgetGrid(PRI_CONTROLLER, ROWS, 1, 2, padding);
        priControllerButtons.clear();
        priControllerButtons.add(PRI_CONTROLLER);
        // Controller Axis Button
        Button PRI_CONTROLLER_AXIS = new Button(0, 0, 20, 20,
                UtilMCText.translatable("ui.dscombat.edit_controller_axis_button"),
                onPress -> Minecraft.getInstance().setScreen(new SelectBindScreen.ControllerAxisButton(page, action, true)));
        positionWidgetGrid(PRI_CONTROLLER_AXIS, ROWS, 1, 2, padding);
        priControllerAxisButtons.clear();
        priControllerAxisButtons.add(PRI_CONTROLLER_AXIS);
        changePrimaryType(action.getPrimaryAction().getButtonType());
        // SECONDARY
        // Action Type Cycle
        positionWidgetGrid(new CycleButton.Builder<>(ActionInput.ButtonType::getTypeName)
                        .withValues(ActionInput.ButtonType.values())
                        .withInitialValue(action.getSecondaryAction().getButtonType())
                        .create(0, 0, 20, 20,
                                UtilMCText.translatable("ui.dscombat.secondary"),
                                (button, value) -> changeSecondaryType(value)),
                ROWS, 1, 3, padding);
        // Unbound
        secUnboundButton = new Button(0, 0, 20, 20,
                UtilMCText.translatable("ui.dscombat.save"),
                onPress -> {
                    action.setSecondaryAction(new ActionInput.UnboundButton());
                    ClientInputManager.saveKeyBinds();
                });
        positionWidgetGrid(secUnboundButton, ROWS, 1, 4, padding);
        // DSC Key Button
        Button SEC_DSC_KEY = new Button(0, 0, 20, 20,
                UtilMCText.translatable("ui.dscombat.edit_dsc_key_button"),
                onPress -> Minecraft.getInstance().setScreen(new SelectBindScreen.DSCKeyButton(page, action, false)));
        positionWidgetGrid(SEC_DSC_KEY, ROWS, 1, 4, padding);
        secDSCKeyButtons.clear();
        secDSCKeyButtons.add(SEC_DSC_KEY);
        // Controller Button
        Button SEC_CONTROLLER = new Button(0, 0, 20, 20,
                UtilMCText.translatable("ui.dscombat.edit_controller_button"),
                onPress -> Minecraft.getInstance().setScreen(new SelectBindScreen.ControllerButton(page, action, false)));
        positionWidgetGrid(SEC_CONTROLLER, ROWS, 1, 4, padding);
        secControllerButtons.clear();
        secControllerButtons.add(SEC_CONTROLLER);
        // Controller Axis Button
        Button SEC_CONTROLLER_AXIS = new Button(0, 0, 20, 20,
                UtilMCText.translatable("ui.dscombat.edit_controller_axis_button"),
                onPress -> Minecraft.getInstance().setScreen(new SelectBindScreen.ControllerAxisButton(page, action, false)));
        positionWidgetGrid(SEC_CONTROLLER_AXIS, ROWS, 1, 4, padding);
        secControllerAxisButtons.clear();
        secControllerAxisButtons.add(SEC_CONTROLLER_AXIS);
        changeSecondaryType(action.getSecondaryAction().getButtonType());
    }

    private void changePrimaryType(ActionInput.ButtonType type) {
        switch (type) {
            case UNBOUND_BUTTON -> {
                if (priUnboundButton != null) priUnboundButton.visible = true;
                priDSCKeyButtons.forEach(button -> button.visible = false);
                priControllerButtons.forEach(button -> button.visible = false);
                priControllerAxisButtons.forEach(button -> button.visible = false);
            }
            case DSC_KEY_BUTTON -> {
                if (priUnboundButton != null) priUnboundButton.visible = false;
                priDSCKeyButtons.forEach(button -> button.visible = true);
                priControllerButtons.forEach(button -> button.visible = false);
                priControllerAxisButtons.forEach(button -> button.visible = false);
            }
            case CONTROLLER_BUTTON -> {
                if (priUnboundButton != null) priUnboundButton.visible = false;
                priDSCKeyButtons.forEach(button -> button.visible = false);
                priControllerButtons.forEach(button -> button.visible = true);
                priControllerAxisButtons.forEach(button -> button.visible = false);
            }
            case CONTROLLER_AXIS_BUTTON -> {
                if (priUnboundButton != null) priUnboundButton.visible = false;
                priDSCKeyButtons.forEach(button -> button.visible = false);
                priControllerButtons.forEach(button -> button.visible = false);
                priControllerAxisButtons.forEach(button -> button.visible = true);
            }
        }
    }

    private void changeSecondaryType(ActionInput.ButtonType type) {
        switch (type) {
            case UNBOUND_BUTTON -> {
                if (secUnboundButton != null) secUnboundButton.visible = true;
                secDSCKeyButtons.forEach(button -> button.visible = false);
                secControllerButtons.forEach(button -> button.visible = false);
                secControllerAxisButtons.forEach(button -> button.visible = false);
            }
            case DSC_KEY_BUTTON -> {
                if (secUnboundButton != null) secUnboundButton.visible = false;
                secDSCKeyButtons.forEach(button -> button.visible = true);
                secControllerButtons.forEach(button -> button.visible = false);
                secControllerAxisButtons.forEach(button -> button.visible = false);
            }
            case CONTROLLER_BUTTON -> {
                if (secUnboundButton != null) secUnboundButton.visible = false;
                secDSCKeyButtons.forEach(button -> button.visible = false);
                secControllerButtons.forEach(button -> button.visible = true);
                secControllerAxisButtons.forEach(button -> button.visible = false);
            }
            case CONTROLLER_AXIS_BUTTON -> {
                if (secUnboundButton != null) secUnboundButton.visible = false;
                secDSCKeyButtons.forEach(button -> button.visible = false);
                secControllerButtons.forEach(button -> button.visible = false);
                secControllerAxisButtons.forEach(button -> button.visible = true);
            }
        }
    }

    @Override
    protected Screen getBackScreen() {
        return new VehicleKeyBindsScreen(page);
    }
}
