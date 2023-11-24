package com.onewhohears.dscombat.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

/**
 * Class permitting simple rendering of tabs for the weapon overlay. Intended to be referenced as a
 * static utility class.
 * @author kawaiicakes
 */
public class WeaponTabComponent extends GuiComponent {
    public static final ResourceLocation WEAPON_TABS = new ResourceLocation(MODID,
            "textures/ui/weapon_icons/weapon_tab.png");
    public static final ResourceLocation SELECTOR = new ResourceLocation(MODID,
            "textures/ui/weapon_icons/selection_box.png");

    public static final byte TAB_WIDTH = 93;
    public static final byte TAB_HEIGHT = 24;
    public static final float[] FRAMES = {0.0F, 5.0F, 10.0F, 13.0F, 16.0F, 18.0F, 20.0F, 22.0F, 23.0F};

    /**
     * Renders a weapon tab. This method provides an easy way to animate the tab.
     * @param frame an <code>int</code> representing the frame of animation. See the return of
     *              <code>#getMaxFrames</code> and subtract one to get the highest allowed frame.
     *              Value must be between 0 and the highest frame inclusive.
     */
    public static void drawTab(PoseStack stack, double x, double y, int blitOffset, int frame, boolean scrollsUpward) {
        if (frame < 0 || frame > getMaxFrames() - 1) throw new IllegalArgumentException("There are only " + getMaxFrames() + " frames!");
        float sign = scrollsUpward ? -1.0F : 1.0F;

        stack.pushPose();
        stack.translate(x, y, blitOffset);

        RenderSystem.setShaderTexture(0, WEAPON_TABS);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        blit(stack, 0, 0, 0, sign * FRAMES[frame], TAB_WIDTH, TAB_HEIGHT, TAB_WIDTH, TAB_HEIGHT);

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();

        stack.popPose();
    }

    /**
     * Renders an icon defined by the <code>ResourceLocation</code> from the return of <code>{@link WeaponData#getWeaponIcon()}</code>.
     * This method provides an easy way to animate the icon.
     * @param frame an <code>int</code> representing the frame of animation. See the return of
     *              <code>#getMaxFrames</code> and subtract one to get the highest allowed frame.
     *              Value must be between 0 and the highest frame inclusive.
     * @param scrollsFromBlank if <code>true</code>, the weapon icon visually "appears" out of thin air
     *                         as one scrolls through the tabs. At present, the y position of a tab
     *                         that scrolls from blank and a scroll that doesn't must be the same to
     *                         maintain visual continuity.
     */
    public static void drawWeapon(PoseStack stack, WeaponData weapon, double x, double y, int blitOffset, int frame, boolean scrollsUpward, boolean scrollsToBlank, boolean scrollsFromBlank) {
        if (frame < 0 || frame > getMaxFrames() - 1) throw new IllegalArgumentException("There are only " + getMaxFrames() + " frames!");
        if (scrollsToBlank && scrollsFromBlank) throw new IllegalArgumentException("Tabs may not scroll to and from blank!");
        if (weapon == null) throw new NullPointerException("Passed weapon is null!");

        int frameValue = (int) FRAMES[frame];

        RenderSystem.setShaderTexture(0, weapon.getWeaponIcon());
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        stack.pushPose();

        if (scrollsUpward) {
            stack.translate(x, y + frameValue, blitOffset);

            if (scrollsFromBlank) blitWithoutTop(stack, TAB_HEIGHT - frameValue);
            if (scrollsToBlank) blitWithoutBottom(stack, frameValue);
        } else {
            stack.translate(x, y - frameValue, blitOffset);

            if (scrollsFromBlank) blitWithoutBottom(stack, TAB_HEIGHT - frameValue);
            if (scrollsToBlank) blitWithoutTop(stack, frameValue);
        }

        if (!scrollsFromBlank && !scrollsToBlank) blitNormal(stack);

        stack.popPose();

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }

    /**
     * 0x10 is the default value Minecraft uses for tooltip alpha. Pass the same coordinates as the tab this name
     * is associated with. Offsets are already accounted for
     */
    public static void drawWeaponName(PoseStack stack, Component name, double x, double y, int blitOffset) {
        // 0x505000FF, 0x5028007f (colours for border start and end, respectively) 0xf0100010 (bg)

        stack.pushPose();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix4f = stack.last().pose();

        int nameWidth = getFont().width(name);
        int nameHeight = getFont().lineHeight;
        int blitOffsetUnder = blitOffset - 1;

        int xPosInitial = (int) x + 1;
        int xPosFinal = xPosInitial + nameWidth + 6;
        int yPosInitial = (int) y - 12;
        int yPosFinal = yPosInitial + nameHeight + 5;

        // leftmost vertical black
        fillGradient(matrix4f, bufferbuilder, xPosInitial - 1, yPosInitial, xPosInitial, yPosFinal + 1, blitOffsetUnder, 0xf0100010, 0xf0100010);
        // internal rectangle
        fillGradient(matrix4f, bufferbuilder, xPosInitial, yPosInitial, xPosFinal, yPosFinal, blitOffsetUnder, 0xf0100010, 0xf0100010);
        // topmost horizontal black
        fillGradient(matrix4f, bufferbuilder, xPosInitial, yPosInitial - 1, xPosFinal, yPosInitial, blitOffsetUnder, 0xf0100010, 0xf0100010);
        // bottommost horizontal black
        fillGradient(matrix4f, bufferbuilder, xPosInitial, yPosFinal, xPosFinal, yPosFinal + 1, blitOffsetUnder, 0xf0100010, 0xf0100010);
        // rightmost vertical black
        fillGradient(matrix4f, bufferbuilder, xPosFinal, yPosInitial, xPosFinal + 1, yPosFinal, blitOffsetUnder, 0xf0100010, 0xf0100010);

        // purple left column
        fillGradient(matrix4f, bufferbuilder, xPosInitial, yPosInitial + 1, xPosInitial + 1, yPosFinal - 1, blitOffsetUnder, 0x505000ff, 0x5028007f);
        // purple right column
        fillGradient(matrix4f, bufferbuilder, xPosFinal - 1, yPosInitial + 1, xPosFinal, yPosFinal - 1, blitOffsetUnder, 0x505000ff, 0x5028007f);
        // purple up row
        fillGradient(matrix4f, bufferbuilder, xPosInitial, yPosInitial, xPosFinal, yPosInitial + 1, blitOffsetUnder, 0x505000ff, 0x505000ff);
        // purple down row
        // the bottom bits are rendered despite being hidden by the tab so that if the weapon name happens to be longer
        // than about 90 pixels, it doesn't look completely ugly when it sticks out the other end
        fillGradient(matrix4f, bufferbuilder, xPosInitial, yPosFinal - 1, xPosFinal, yPosFinal, blitOffsetUnder, 0x5028007f, 0x5028007f);

        RenderSystem.enableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
        MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        stack.translate(x, y - 11, blitOffset);

        getFont().drawInBatch(name, 4, 2, -1, true, matrix4f, bufferSource, false, 0, 15728880);

        bufferSource.endBatch();
        stack.popPose();
    }

    public static void renderSelectionBox(PoseStack stack, double x, double y, int blitOffset) {
        RenderSystem.setShaderTexture(0, SELECTOR);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        stack.pushPose();
        stack.translate(x, y, blitOffset);

        blit(stack, 0, 0, 0, 0, TAB_WIDTH, TAB_HEIGHT, TAB_WIDTH, TAB_HEIGHT);

        stack.popPose();

        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
    }

    public static int getMaxFrames() {
        return FRAMES.length;
    }

    private static Font getFont() {
        return Minecraft.getInstance().font;
    }

    /*
        WEAPON RENDERING HELPER METHODS BELOW
     */

    private static void blitNormal(PoseStack stack) {
        blit(stack, 0, 0, 0, 0, TAB_WIDTH, TAB_HEIGHT, TAB_WIDTH, TAB_HEIGHT);
    }

    private static void blitWithoutTop(PoseStack stack, int trimPixels) {
        blit(stack, 0, trimPixels, 0, trimPixels, TAB_WIDTH, TAB_HEIGHT - trimPixels, TAB_WIDTH, TAB_HEIGHT);
    }

    private static void blitWithoutBottom(PoseStack stack, int trimPixels) {
        blit(stack, 0, 0, 0, 0, TAB_WIDTH, TAB_HEIGHT - trimPixels, TAB_WIDTH, TAB_HEIGHT);
    }
}
