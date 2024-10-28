package com.onewhohears.dscombat.client.screen;

import com.onewhohears.dscombat.DSCombatMod;
import net.minecraft.resources.ResourceLocation;

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
        super.init();
    }
}
