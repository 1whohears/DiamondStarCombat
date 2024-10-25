package com.onewhohears.dscombat.client.screen;

import com.onewhohears.dscombat.DSCombatMod;
import net.minecraft.resources.ResourceLocation;

public class VehicleKeybindScreen extends VehicleSubScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/vehicle_main_screen.png");

    private static final int imageWidth = 176, imageHeight = 136;
    private static final int textureSize = 256;

    protected VehicleKeybindScreen() {
        super("screen.dscombat.vehicle_keybinds_screen",
                BG_TEXTURE, imageWidth, imageHeight, textureSize, textureSize);
    }

    @Override
    protected void init() {
        super.init();
    }
}
