package com.onewhohears.dscombat.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

/**
 * Class permitting simple rendering of tabs for the weapon overlay. Intended to be referenced as a
 * static utility class.
 * @author kawaiicakes
 */
public class WeaponTabComponent extends GuiComponent {
    public static final ResourceLocation WEAPON_TABS = new ResourceLocation(MODID,
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
     * @param frame an <code>int</code> representing the frame of animation. See the return of
     *              <code>#getMaxFrames</code> and subtract one to get the highest allowed frame.
     *              Value must be between 0 and the highest frame inclusive.
     */
    public static void drawTab(PoseStack stack, double x, double y, int blitOffset, int frame, boolean scrollsUpward) {
        if (frame < 0 || frame > getMaxFrames() - 1) throw new IllegalArgumentException("There are only " + getMaxFrames() + " frames!");
        float sign = scrollsUpward ? -1.0F : 1.0F;

        stack.pushPose();
        stack.translate(x, y, blitOffset);
        blit(stack, 0, 0, 0, sign * FRAMES[frame], TAB_WIDTH, TAB_HEIGHT, TAB_WIDTH, FILE_HEIGHT);
        stack.popPose();
    }

    /**
     * To avoid redundant calls to <code>RenderSystem</code>, the client code must call
     * <code>WeaponTabComponent#prepareForRender</code> wherever in the code the tab is to be rendered.
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

        float frameValue = FRAMES[frame];
        int sign = scrollsUpward ? 1 : -1;
        int toBlankMultiplier = scrollsToBlank ? 1 : 0;
        int fromBlankMultiplier = scrollsFromBlank ? 1 : 0;
        int upScrollMultiplier = scrollsUpward ? 0 : 1;
        int upScrollMultiplierInverse = scrollsUpward ? 1 : 0;

        float vOffset = scrollsFromBlank
                ? -frameValue * upScrollMultiplierInverse
                : frameValue * toBlankMultiplier * upScrollMultiplier;
        int pHeight = scrollsFromBlank
                ? (int) frameValue
                : TAB_HEIGHT - (int) (toBlankMultiplier * frameValue);

        RenderSystem.setShaderTexture(0, weapon.getWeaponIcon());

        stack.pushPose();
        stack.translate(x, y + (sign * frameValue) - (sign * frameValue * fromBlankMultiplier * upScrollMultiplierInverse), blitOffset);

        blit(stack,
                0, 0,
                0, vOffset,
                TAB_WIDTH, pHeight,
                TAB_WIDTH, TAB_HEIGHT);

        stack.popPose();

        RenderSystem.setShaderTexture(0, WEAPON_TABS);
    }

    public static int getMaxFrames() {
        return FRAMES.length;
    }
}
