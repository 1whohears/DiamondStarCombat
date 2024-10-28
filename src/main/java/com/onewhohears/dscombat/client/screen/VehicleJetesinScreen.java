package com.onewhohears.dscombat.client.screen;

import com.onewhohears.dscombat.DSCombatMod;
import net.minecraft.resources.ResourceLocation;

public class VehicleJetesinScreen extends VehicleSubScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/background/vehicle_jetesin_screen.png");

    private static final int imageWidth = 240, imageHeight = 180;
    private static final int textureSize = 256;

    protected VehicleJetesinScreen() {
        super("screen.dscombat.vehicle_jetesin_screen",
                BG_TEXTURE, imageWidth, imageHeight, textureSize, textureSize);
    }

    @Override
    protected void init() {
        super.init();
    }
}
