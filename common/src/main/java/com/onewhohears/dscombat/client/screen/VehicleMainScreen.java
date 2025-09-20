package com.onewhohears.dscombat.client.screen;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.VehicleSyncAction;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class VehicleMainScreen extends VehicleScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/background/vehicle_main_screen.png");

    private static final int imageWidth = 240, imageHeight = 180;
    private static final int textureSize = 256;

    public VehicleMainScreen() {
        super("screen.dscombat.vehicle_main_screen", BG_TEXTURE,
                imageWidth, imageHeight, textureSize, textureSize);
    }

    @Override
    protected void init() {
        super.init();
        int index = 0;
        // Vehicle Name
        EditBox rangeBox = new EditBox(minecraft.font, 0, 0, 20, 20, UtilMCText.empty());
        positionWidgetGrid(rangeBox, ROWS, COLUMNS, index++, 2);
        rangeBox.setValue(getVehicle().getCustomName() != null ? getVehicle().getCustomName().getString() : "");
        rangeBox.setTextColor(0xFFFFFF);
        rangeBox.setResponder(onCustomNameChange());
        // Cycle Vehicle Permission Mode
        positionWidgetGrid(CycleButton.<EntityVehicle.PermMode>builder(value -> UtilMCText.translatable(value.getTranslatable()))
                        .withValues(EntityVehicle.PermMode.values())
                        .withInitialValue(getVehicle().getPermMode())
                        .create(0, 0, 20, 20,
                                UtilMCText.translatable("ui.dscombat.permission_mode"),
                                onPermModeCycle()),
                ROWS, COLUMNS, index++, 2);
        // Open Parts Screen
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("screen.dscombat.vehicle_parts_screen"),
                        onPress -> sendSyncAction(new VehicleSyncAction.OpenPartsAction())),
                ROWS, COLUMNS, index++, 2);
        // Open Storage Inventory
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("screen.dscombat.vehicle_inventory_screen"),
                        onPress -> sendSyncAction(new VehicleSyncAction.OpenStorageAction(0))),
                ROWS, COLUMNS, index++, 2);
        // Open Weapon Settings Screen
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("screen.dscombat.vehicle_weapon_screen"),
                        onPress -> minecraft.setScreen(new VehicleWeaponScreen())),
                ROWS, COLUMNS, index++, 2);
        // Open Radar Settings Screen
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("screen.dscombat.vehicle_radar_screen"),
                        onPress -> minecraft.setScreen(new VehicleRadarScreen())),
                ROWS, COLUMNS, index++, 2);
        // Open Parts/Weapons Reload Screen
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("screen.dscombat.vehicle_reload_screen"),
                        onPress -> minecraft.setScreen(new VehicleReloadScreen())),
                ROWS, COLUMNS, index++, 2);
        // Open Jetesin Parts Screen
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("screen.dscombat.vehicle_jetesin_screen"),
                        onPress -> minecraft.setScreen(new VehicleJetesinScreen())),
                ROWS, COLUMNS, index++, 2);
        // Open Vehicle Health Screen
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("screen.dscombat.vehicle_health_screen"),
                        onPress -> minecraft.setScreen(new VehicleHealthScreen())),
                ROWS, COLUMNS, index++, 2);
        // Open Keybinds screen (include option to display reminder on top left)
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("screen.dscombat.vehicle_other_screen"),
                        onPress -> minecraft.setScreen(new VehicleOtherScreen())),
                ROWS, COLUMNS, index++, 2);
        // Landing Gear Toggle
        positionWidgetGrid(CycleButton.onOffBuilder(getVehicle().isLandingGear())
                        .create(0, 0, 20, 20,
                                UtilMCText.translatable("ui.dscombat.landing_gear"),
                                (button, value) -> sendSyncAction(new VehicleSyncAction.LandingGearAction(value))),
                ROWS, COLUMNS, index++, 2);
        // Turn Vehicle to Item
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("ui.dscombat.shrink_plane_button"),
                        onPress -> onPlaneItemButton()),
                ROWS, COLUMNS, index++, 2);
    }

    private void onPlaneItemButton() {
        sendSyncAction(new VehicleSyncAction.ToItemAction());
    }

    private CycleButton.OnValueChange<EntityVehicle.PermMode> onPermModeCycle() {
        return (button, value) -> sendSyncAction(new VehicleSyncAction.SetPermModeAction(value));
    }

    private Consumer<String> onCustomNameChange() {
        return name -> {
            sendSyncAction(new VehicleSyncAction.SetCustomNameAction(Component.literal(name)));
        };
    }

}
