package com.onewhohears.dscombat.client.screen;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.ActionInputHolder;
import com.onewhohears.dscombat.client.input.ClientInputManager;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

public class VehicleKeyBindsScreen extends VehicleSubScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/background/vehicle_other_screen.png");

    private static final int imageWidth = 240, imageHeight = 180;
    private static final int textureSize = 256;

    private final int page;

    public VehicleKeyBindsScreen(int page) {
        super("screen.dscombat.vehicle_key_binds_screen",
                BG_TEXTURE, imageWidth, imageHeight, textureSize, textureSize);
        this.page = page;
    }

    @Override
    protected void init() {
        super.init();
        int maxPage = ClientInputManager.getNumActions() / 12;
        // LEFT RIGHT ARROWS
        // PAGE DOWN
        Button leftButton = new Button(0, 0, 20, 20,
                UtilMCText.literal("<-"), onPress -> {
            int p = page - 1;
            if (p < 0) p = maxPage;
            getMinecraft().setScreen(new VehicleKeyBindsScreen(p));
        });
        positionWidgetGrid(leftButton, 7, 4, 2, 2);
        // PAGE UP
        Button rightButton = new Button(0, 0, 20, 20,
                UtilMCText.literal("->"), onPress -> {
            int p = page + 1;
            if (p > maxPage) p = 0;
            getMinecraft().setScreen(new VehicleKeyBindsScreen(p));
        });
        positionWidgetGrid(rightButton, ROWS, 4, 3, 2);
        // DISPLAY 12 BIND BUTTONS BASED ON PAGE
        int firstBindIndex = page * 12, lastBindIndex = firstBindIndex + 12;
        int k = 0, a = 0;
        for (ActionInputHolder.Axis action : ClientInputManager.getAxes()) {
            if (k < firstBindIndex || k >= lastBindIndex) { ++k; continue; }
            Button button = new Button(0, 0, 20, 20,
                    action.getName(), onPress -> {
                getMinecraft().setScreen(new VehicleEditAxisBindScreen(page, action));
            });
            positionWidgetGrid(button, ROWS, COLUMNS, a + 2, 2);
            ++k; ++a;
        }
        for (ActionInputHolder.Button action : ClientInputManager.getButtons()) {
            if (k < firstBindIndex || k >= lastBindIndex) { ++k; continue; }
            Button button = new Button(0, 0, 20, 20,
                    action.getName(), onPress -> {
                getMinecraft().setScreen(new VehicleEditButtonBindScreen(page, action));
            });
            positionWidgetGrid(button, ROWS, COLUMNS, a + 2, 2);
            ++k; ++a;
        }
    }

    @Override
    protected Screen getBackScreen() {
        return new VehicleOtherScreen();
    }

}
