package com.onewhohears.dscombat.client.screen.key_bind;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.ActionInputHolder;
import com.onewhohears.dscombat.client.input.ClientInputManager;
import com.onewhohears.dscombat.client.screen.VehicleSubScreen;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

public class VehicleEditButtonBindScreen extends VehicleSubScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/background/vehicle_other_screen.png");

    private static final int imageWidth = 240, imageHeight = 180;
    private static final int textureSize = 256;

    private final int page;
    private final ActionInputHolder.Button action;

    public VehicleEditButtonBindScreen(int page, ActionInputHolder.Button action) {
        super(action.getNameString(), BG_TEXTURE, imageWidth, imageHeight, textureSize, textureSize);
        this.page = page;
        this.action = action;
    }

    @Override
    protected void init() {
        super.init();

    }

    @Override
    protected Screen getBackScreen() {
        return new VehicleKeyBindsScreen(page);
    }
}
