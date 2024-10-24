package com.onewhohears.dscombat.client.screen;

import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class VehicleSubScreen extends VehicleScreen {
    protected VehicleSubScreen(Component title, ResourceLocation backgroundTexture, int imageWidth, int imageHeight, int textureWidth, int textureHeight) {
        super(title, backgroundTexture, imageWidth, imageHeight, textureWidth, textureHeight);
    }
    protected VehicleSubScreen(String translatableScreenName, ResourceLocation backgroundTexture, int imageWidth, int imageHeight, int textureWidth, int textureHeight) {
        super(translatableScreenName, backgroundTexture, imageWidth, imageHeight, textureWidth, textureHeight);
    }
    @Override
    protected void init() {
        super.init();
        Button backButton = new Button(0, 0, 20, 20,
                UtilMCText.translatable("ui.dscombat.back"),
                onPress -> { getMinecraft().setScreen(new VehicleMainScreen()); });
        positionWidgetGrid(backButton, ROWS, COLUMNS, 0, 2);
    }
}
