package com.onewhohears.dscombat.client.screen;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.VehicleSyncAction;
import com.onewhohears.dscombat.crafting.PartItemUnloadRecipe;
import com.onewhohears.dscombat.data.parts.ReloadablePartInstance;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

public class VehicleReloadScreen extends VehicleSubScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/background/vehicle_reload_screen.png");

    private static final int imageWidth = 240, imageHeight = 180;
    private static final int textureSize = 256;

    protected VehicleReloadScreen() {
        super("screen.dscombat.vehicle_reload_screen",
                BG_TEXTURE, imageWidth, imageHeight, textureSize, textureSize);
    }

    @Override
    protected void init() {
        COLUMNS = 3;
        vertical_widget_shift = 10;
        super.init();
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("ui.dscombat.reload_all"),
                        onPress -> onReloadAllButton()),
                ROWS, COLUMNS, 1, 2);
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("ui.dscombat.unload_all"),
                        onPress -> onUnloadAllButton()),
                ROWS, COLUMNS, 2, 2);
        vertical_widget_shift = 34;
        for (PartSlot slot : getVehicle().partsManager.getReloadableParts()) {
            ReloadablePartInstance part = (ReloadablePartInstance) slot.getPartData();
            if (part == null) continue;

        }
    }

    private void onReloadAllButton() {
        sendSyncAction(new VehicleSyncAction.LoadPartAction(false));
    }

    private void onUnloadAllButton() {
        sendSyncAction(new VehicleSyncAction.LoadPartAction(true));
    }
}
