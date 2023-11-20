package com.onewhohears.dscombat.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

public class WeaponTabComponent extends GuiComponent {
    public static final ResourceLocation WEAPON_TABS = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/weapon_tab.png");
    public static final byte FILE_HEIGHT = 48;
    public static final byte TAB_WIDTH = 93;
    public static final byte TAB_HEIGHT = 24;
    public static final float[] FRAMES = {0.0F, 5.0F, 10.0F, 13.0F, 16.0F, 18.0F, 20.0F, 22.0F, 23.0F};

    /**
     * Call to allow the tab(s) to render correctly. This exists to avoid redundant calls to <code>RenderSystem</code>.
     * Make sure to call <code>#prepareForTakedown</code> after finishing.
     */
    public static void prepareForRender() {
        RenderSystem.setShaderTexture(0, WEAPON_TABS);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
    }

    public static void prepareForTakedown() {
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }

    /**
     * To avoid redundant calls to <code>RenderSystem</code>, the client code must call
     * <code>WeaponTabComponent#prepareForRender</code> wherever in the code the tab is to be rendered.
     * @param frame an <code>int</code> from 0 to 8 inclusive representing the frame of animation.
     */
    public static void drawTab(PoseStack stack, double x, double y, int blitOffset, int frame, boolean scrollsUpward) {
        if (frame < 0 || frame > 8) throw new IllegalArgumentException("There are only 9 frames!");
        float sign = scrollsUpward ? -1.0F : 1.0F;

        stack.pushPose();
        stack.translate(x, y, blitOffset);
        blit(stack, 0, 0, 0, sign * FRAMES[frame], TAB_WIDTH, TAB_HEIGHT, TAB_WIDTH, FILE_HEIGHT);
        stack.popPose();
    }
}
