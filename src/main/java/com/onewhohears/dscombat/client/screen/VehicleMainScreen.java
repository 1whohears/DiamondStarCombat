package com.onewhohears.dscombat.client.screen;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ToServerOpenParts;
import com.onewhohears.dscombat.common.network.toserver.ToServerOpenStorage;
import com.onewhohears.dscombat.common.network.toserver.ToServerVehicleToItem;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class VehicleMainScreen extends VehicleScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/vehicle_main_screen.png");

    private static final int imageWidth = 176, imageHeight = 136;
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

        // Open Storage Inventory
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("screen.dscombat.vehicle_inventory_screen"),
                        onPress -> { PacketHandler.INSTANCE.sendToServer(new ToServerOpenStorage(0)); }),
                ROWS, COLUMNS, index++, 2);
        // Open Parts Screen
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("screen.dscombat.vehicle_parts_screen"),
                        onPress -> { PacketHandler.INSTANCE.sendToServer(new ToServerOpenParts()); }),
                ROWS, COLUMNS, index++, 2);
        // Open Parts/Weapons Reload Screen
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("screen.dscombat.vehicle_reload_screen"),
                        onPress -> { getMinecraft().setScreen(new VehicleHealthScreen()); }),
                ROWS, COLUMNS, index++, 2);
        // Open Jetesin Parts Screen
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("screen.dscombat.vehicle_jetesin_screen"),
                        onPress -> { getMinecraft().setScreen(new VehicleHealthScreen()); }),
                ROWS, COLUMNS, index++, 2);
        // Open Vehicle Health Screen
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("screen.dscombat.vehicle_health_screen"),
                        onPress -> { getMinecraft().setScreen(new VehicleHealthScreen()); }),
                ROWS, COLUMNS, index++, 2);
        // Open Weapon Settings Screen (Weapon Select, Radar Settings)

        // Open Keybinds screen (include option to display reminder on top left)

        // Landing Gear Toggle

        // Cycle Vehicle Permission Mode

        // Turn Vehicle to Item
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("ui.dscombat.shrink_plane_button"),
                        onPress -> onPlaneItemButton()),
                ROWS, COLUMNS, index++, 2);
    }

    private void onPlaneItemButton() {
        PacketHandler.INSTANCE.sendToServer(new ToServerVehicleToItem(getVehicle().getId()));
    }

}
