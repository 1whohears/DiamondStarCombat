package com.onewhohears.dscombat.client.screen;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.onewholibs.client.screen.BackgroundScreen;
import net.minecraft.resources.ResourceLocation;

public class VehicleMainScreen extends BackgroundScreen {

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

    }

}
