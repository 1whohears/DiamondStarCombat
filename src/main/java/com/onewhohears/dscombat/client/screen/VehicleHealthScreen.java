package com.onewhohears.dscombat.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class VehicleHealthScreen extends VehicleSubScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/vehicle_main_screen.png");

    private static final int imageWidth = 176, imageHeight = 136;
    private static final int textureSize = 256;

    protected VehicleHealthScreen() {
        super("screen.dscombat.vehicle_health_screen",
                BG_TEXTURE, imageWidth, imageHeight, textureSize, textureSize);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void renderBackground(@NotNull PoseStack poseStack) {
        super.renderBackground(poseStack);
        // VEHICLE BASE HEALTH / ARMOR

        // VEHICLE HITBOX HEALTH / ARMOR

        // FUEL
    }
}
