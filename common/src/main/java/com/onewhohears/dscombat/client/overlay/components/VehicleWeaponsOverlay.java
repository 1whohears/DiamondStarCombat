package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.data.weapon.stats.BulletStats;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.parts.EntityRidablePart;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.List;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

// TODO: finish this lol
/**
 * @author kawaiicakes
 */
public class VehicleWeaponsOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation WEAPON_TABS = new ResourceLocation(MODID,
            "textures/ui/weapon_icons/weapon_tab.png");
    public static final ResourceLocation SELECTOR = new ResourceLocation(MODID,
            "textures/ui/weapon_icons/selection_box.png");
    public static final byte TAB_WIDTH = 93;
    public static final byte TAB_HEIGHT = 24;
    public static final float[] FRAMES = {0.0F, 5.0F, 10.0F, 13.0F, 16.0F, 18.0F, 20.0F, 22.0F, 23.0F};
    public static final int[] SPACINGS = {24, 21, 18, 12, 0};
    public static final Component SAFETY = UtilMCText.translatable("ui.dscombat.no_weapon");

    protected boolean weaponChangeState;
    protected boolean weaponChangeQueued;
    protected int weaponChangeCountdown;
    protected int selectedWeapon;

    protected float frame;
    protected int superFrame;

    @Override
    protected boolean shouldRender(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        return getPlayerVehicle() instanceof EntityRidablePart;
    }

    @Override
    protected void render(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        EntityRidablePart seat = (EntityRidablePart) getPlayerVehicle();
        assert seat != null;

        double yPlacement = screenHeight - TAB_HEIGHT - 13;
        int blitPosition = 1;
        if (seat.isTurret()) {
            EntityTurret turret = (EntityTurret) seat;
            int turretWeaponCount = turret.getWeaponCount();
            int turretWeaponIdx = turret.getWeaponIndex();
            if (turretWeaponCount <= 1) {
                drawFinishedTab(graphics, turret.getWeaponData(), yPlacement, blitPosition);
                drawRangeMeter(graphics, turret, yPlacement, blitPosition);
            } else {
                if (turretWeaponIdx != this.selectedWeapon) enableWeaponChangeState();
                this.selectedWeapon = turretWeaponIdx;
                if (this.weaponChangeCountdown <= 0) this.weaponChangeState = false;
                if (!this.weaponChangeState) {
                    drawFinishedTab(graphics, turret.getWeaponData(), yPlacement, blitPosition);
                    drawRangeMeter(graphics, turret, yPlacement, blitPosition);
                } else {
                    java.util.List<String> ids = turret.getAllWeaponIds();
                    int tabsToRender = Math.min(ids.size(), 5);
                    for (int i = 0; i < tabsToRender; i++) {
                        int shiftedIndex = turretWeaponIdx - i;
                        if (shiftedIndex < 0) shiftedIndex = ((shiftedIndex % ids.size()) + ids.size()) % ids.size();
                        int newYPos = (int) (yPlacement - (24 * i));
                        com.onewhohears.dscombat.data.weapon.stats.WeaponStats ws = com.onewhohears.dscombat.data.weapon.WeaponPresets.get().get(ids.get(shiftedIndex));
                        drawTab(graphics, 13, newYPos, blitPosition, 0, false);
                        graphics.pose().pushPose();
                        graphics.pose().translate(0, 0, blitPosition + 3);
                        if (ws != null) graphics.drawString(FONT, ws.getDisplayNameComponent(), 16, newYPos + 4, 0xffffff);
                        graphics.pose().popPose();
                    }
                    renderSelectionBox(graphics, 13, yPlacement, blitPosition + 2);
                    this.weaponChangeCountdown--;
                }
            }
            return;
        }
        if (!seat.canPassengerShootParentWeapon()) return;
        EntityVehicle vehicle = seat.getParentVehicle();
        if (vehicle == null) return;

        List<WeaponInstance<?>> weapons = vehicle.weaponSystem.getWeapons();
        WeaponInstance<?> selectedWeapon = vehicle.weaponSystem.getSelected();
        int selectedIndex = vehicle.weaponSystem.getSelectedIndex();

        if (weapons == null || weapons.isEmpty()) return;
        if (selectedWeapon == null) return;

        if (selectedIndex != this.selectedWeapon) enableWeaponChangeState();
        this.selectedWeapon = selectedIndex;

        if (this.weaponChangeCountdown <= 0) this.weaponChangeState = false;

        if (!this.weaponChangeState) {
            drawFinishedTab(graphics, selectedWeapon, yPlacement, blitPosition);
        } else {
            int weaponTabsToRender = Math.min(weapons.size(), 5);

            for (int i = 0; i < weaponTabsToRender; i++) {
                int shiftedIndex = selectedIndex - i;
                if (shiftedIndex < 0) shiftedIndex = ((shiftedIndex % weapons.size()) + weapons.size()) % weapons.size();
                int newYPos = (int) (yPlacement - (24 * i));

                WeaponInstance<?> weaponAt = weapons.get(shiftedIndex);

                drawTab(graphics, 13, newYPos, blitPosition, 0, false);
                drawWeapon(graphics, weaponAt, 13, newYPos, blitPosition + 1, 0, false, false, false);
                graphics.pose().pushPose();
                graphics.pose().translate(0, 0, blitPosition + 3);
                if (!weaponAt.getStats().isNoWeapon()) {
                    graphics.drawString(FONT, weaponAt.getCurrentAmmo() + "/" + weaponAt.getMaxAmmo(), 16, newYPos + 14, 0xe6e600);
                } else {
                    graphics.drawString(FONT, SAFETY, 16, newYPos + 14, 0xff5555);
                }
                graphics.drawString(FONT, weaponAt.getStats().getDisplayNameComponent(), 16, newYPos + 4, 0xffffff);
                graphics.pose().popPose();
            }

            renderSelectionBox(graphics, 13, yPlacement, blitPosition + 2);
            this.weaponChangeCountdown--;
        }
    }

    @Override
    protected @NotNull String componentId() {
        return "dscombat_weapons";
    }

    public void queueWeaponChange() {
        this.weaponChangeQueued = true;
    }

    public void enableWeaponChangeState() {
        this.weaponChangeCountdown = 200;
        this.weaponChangeState = true;

        // (the context in which this is called necessarily has a LocalPlayer existing on client)
        //noinspection DataFlowIssue
        getPlayer().playSound(SoundEvents.UI_BUTTON_CLICK.value());
    }
    
    protected static void drawFinishedTab(GuiGraphics graphics, WeaponInstance<?> selectedWeapon, double yPlacement, int blitPosition) {
    	if (selectedWeapon == null) return;
    	drawWeaponName(graphics, selectedWeapon.getStats().getDisplayNameComponent(), 13, yPlacement, blitPosition - 2);
        drawTab(graphics, 13, yPlacement, blitPosition, 0, false);
        drawWeapon(graphics, selectedWeapon, 13, yPlacement, blitPosition + 1, 0, false, false, false);

        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, blitPosition + 2);
        if (!selectedWeapon.getStats().isNoWeapon()) graphics.drawString(FONT, selectedWeapon.getCurrentAmmo() + "/" + selectedWeapon.getMaxAmmo(), 16, (int) (yPlacement + 14), 0xe6e600);
        graphics.drawString(FONT, selectedWeapon.getStats().getWeaponTypeCode(), 16, (int) yPlacement + 4, 0xe6e600);
        graphics.pose().popPose();
    }


    /**
     * Renders a weapon tab. This method provides an easy way to animate the tab.
     * @param frame an <code>int</code> representing the frame of animation. See the return of
     *              <code>#getMaxFrames</code> and subtract one to get the highest allowed frame.
     *              Value must be between 0 and the highest frame inclusive.
     */
    protected static void drawTab(GuiGraphics graphics, double x, double y, int blitOffset, int frame, boolean scrollsUpward) {
        if (frame < 0 || frame > getMaxFrames() - 1) throw new IllegalArgumentException("There are only " + getMaxFrames() + " frames!");
        float sign = scrollsUpward ? -1.0F : 1.0F;

        graphics.pose().pushPose();
        graphics.pose().translate(x, y, blitOffset);

        RenderSystem.setShaderTexture(0, WEAPON_TABS);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        graphics.blit(WEAPON_TABS, 0, 0, 0, sign * FRAMES[frame], TAB_WIDTH, TAB_HEIGHT, TAB_WIDTH, TAB_HEIGHT);

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();

        graphics.pose().popPose();
    }

    /**
     * Renders an icon defined by the <code>ResourceLocation</code> from the return of <code>{@link WeaponStats#getWeaponIcon()}</code>.
     * This method provides an easy way to animate the icon.
     * @param frame an <code>int</code> representing the frame of animation. See the return of
     *              <code>#getMaxFrames</code> and subtract one to get the highest allowed frame.
     *              Value must be between 0 and the highest frame inclusive.
     * @param scrollsFromBlank if <code>true</code>, the weapon icon visually "appears" out of thin air
     *                         as one scrolls through the tabs. At present, the y position of a tab
     *                         that scrolls from blank and a scroll that doesn't must be the same to
     *                         maintain visual continuity.
     */
    protected static void drawWeapon(GuiGraphics graphics, WeaponInstance<?> weapon, double x, double y, int blitOffset, int frame, boolean scrollsUpward, boolean scrollsToBlank, boolean scrollsFromBlank) {
        if (frame < 0 || frame > getMaxFrames() - 1) throw new IllegalArgumentException("There are only " + getMaxFrames() + " frames!");
        if (scrollsToBlank && scrollsFromBlank) throw new IllegalArgumentException("Tabs may not scroll to and from blank!");
        if (weapon == null) throw new NullPointerException("Passed weapon is null!");

        int frameValue = (int) FRAMES[frame];

        ResourceLocation rl = weapon.getStats().getWeaponIcon();
        RenderSystem.setShaderTexture(0, rl);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        graphics.pose().pushPose();

        if (scrollsUpward) {
            graphics.pose().translate(x, y + frameValue, blitOffset);

            if (scrollsFromBlank) blitWithoutTop(graphics, TAB_HEIGHT - frameValue, rl);
            if (scrollsToBlank) blitWithoutBottom(graphics, frameValue, rl);
        } else {
            graphics.pose().translate(x, y - frameValue, blitOffset);

            if (scrollsFromBlank) blitWithoutBottom(graphics, TAB_HEIGHT - frameValue, rl);
            if (scrollsToBlank) blitWithoutTop(graphics, frameValue, rl);
        }

        if (!scrollsFromBlank && !scrollsToBlank) blitNormal(graphics, rl);

        graphics.pose().popPose();

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }

    /**
     * 0x10 is the default value Minecraft uses for tooltip alpha. Pass the same coordinates as the tab this name
     * is associated with. Offsets are already accounted for
     */
    protected static void drawWeaponName(GuiGraphics graphics, Component name, double x, double y, int blitOffset) {
        // 0x505000FF, 0x5028007f (colours for border start and end, respectively) 0xf0100010 (bg)

        graphics.pose().pushPose();

        /*Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);*/
        Matrix4f matrix4f = graphics.pose().last().pose();

        int nameWidth = FONT.width(name);
        int nameHeight = FONT.lineHeight;
        int blitOffsetUnder = blitOffset - 1;

        int xPosInitial = (int) x + 1;
        int xPosFinal = xPosInitial + nameWidth + 6;
        int yPosInitial = (int) y - 12;
        int yPosFinal = yPosInitial + nameHeight + 5;

        RenderType renderType = RenderType.guiOverlay();
        // leftmost vertical black
        graphics.fillGradient(renderType, xPosInitial - 1, yPosInitial, xPosInitial, yPosFinal + 1, blitOffsetUnder, 0xf0100010, 0xf0100010);
        // internal rectangle
        graphics.fillGradient(renderType, xPosInitial, yPosInitial, xPosFinal, yPosFinal, blitOffsetUnder, 0xf0100010, 0xf0100010);
        // topmost horizontal black
        graphics.fillGradient(renderType, xPosInitial, yPosInitial - 1, xPosFinal, yPosInitial, blitOffsetUnder, 0xf0100010, 0xf0100010);
        // bottommost horizontal black
        graphics.fillGradient(renderType, xPosInitial, yPosFinal, xPosFinal, yPosFinal + 1, blitOffsetUnder, 0xf0100010, 0xf0100010);
        // rightmost vertical black
        graphics.fillGradient(renderType, xPosFinal, yPosInitial, xPosFinal + 1, yPosFinal, blitOffsetUnder, 0xf0100010, 0xf0100010);

        // purple left column
        graphics.fillGradient(renderType, xPosInitial, yPosInitial + 1, xPosInitial + 1, yPosFinal - 1, blitOffsetUnder, 0x505000ff, 0x5028007f);
        // purple right column
        graphics.fillGradient(renderType, xPosFinal - 1, yPosInitial + 1, xPosFinal, yPosFinal - 1, blitOffsetUnder, 0x505000ff, 0x5028007f);
        // purple up row
        graphics.fillGradient(renderType, xPosInitial, yPosInitial, xPosFinal, yPosInitial + 1, blitOffsetUnder, 0x505000ff, 0x505000ff);
        // purple down row
        // the bottom bits are rendered despite being hidden by the tab so that if the weapon name happens to be longer
        // than about 90 pixels, it doesn't look completely ugly when it sticks out the other end
        graphics.fillGradient(renderType, xPosInitial, yPosFinal - 1, xPosFinal, yPosFinal, blitOffsetUnder, 0x5028007f, 0x5028007f);

        /*RenderSystem.enableDepthTest();
        //RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.disableBlend();
        //RenderSystem.enableTexture();*/
        MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        graphics.pose().translate(x, y - 11, blitOffset);

        FONT.drawInBatch(name, 4, 2, -1, true, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);

        bufferSource.endBatch();
        graphics.pose().popPose();
    }

    protected static void renderSelectionBox(GuiGraphics graphics, double x, double y, int blitOffset) {
        RenderSystem.setShaderTexture(0, SELECTOR);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        graphics.pose().pushPose();
        graphics.pose().translate(x, y, blitOffset);

        graphics.blit(SELECTOR, 0, 0, 0, 0, TAB_WIDTH, TAB_HEIGHT, TAB_WIDTH, TAB_HEIGHT);

        graphics.pose().popPose();

        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
    }

    /*
        HELPER METHODS BELOW
     */

    protected static void blitNormal(GuiGraphics graphics, ResourceLocation rl) {
        graphics.blit(rl, 0, 0, 0, 0, TAB_WIDTH, TAB_HEIGHT, TAB_WIDTH, TAB_HEIGHT);
    }

    protected static void blitWithoutTop(GuiGraphics graphics, int trimPixels, ResourceLocation rl) {
        graphics.blit(rl, 0, trimPixels, 0, trimPixels, TAB_WIDTH, TAB_HEIGHT - trimPixels, TAB_WIDTH, TAB_HEIGHT);
    }

    protected static void blitWithoutBottom(GuiGraphics graphics, int trimPixels, ResourceLocation rl) {
        graphics.blit(rl, 0, 0, 0, 0, TAB_WIDTH, TAB_HEIGHT - trimPixels, TAB_WIDTH, TAB_HEIGHT);
    }

    protected static int getMaxFrames() {
        return FRAMES.length;
    }

    /**
     * Calculates horizontal ballistic range in blocks.
     * Uses standard projectile motion: range = v² * sin(2θ) / g
     * Clamped by maxAge (projectile lifetime in ticks).
     *
     * @param pitchDeg turret pitch in degrees (Minecraft convention: negative = up)
     * @param speed    projectile speed in m/tick
     * @param maxAge   max lifetime in ticks (0 = unlimited)
     * @return horizontal range in blocks, or -1 if not applicable
     */
    public static int calcBallisticRange(float pitchDeg, double speed, int maxAge) {
        double elevationRad = Math.toRadians(-pitchDeg);
        double g = DSCPhyCons.GRAVITY * DSCPhyCons.ACC_TIME_SCALE; // m/tick²
        double vx = speed * Math.cos(elevationRad);
        double vy = speed * Math.sin(elevationRad);

        // time of flight until projectile returns to same height: t = 2*vy/g
        double tof = (vy > 0) ? (2.0 * vy / g) : 0;

        // clamp by maxAge if set
        if (maxAge > 0 && tof > maxAge) tof = maxAge;

        double range = vx * tof;
        return (int) Math.round(range);
    }

    /** @deprecated use {@link #calcBallisticRange(float, double, int)} */
    public static int calcBallisticRange(float pitchDeg, double speed) {
        return calcBallisticRange(pitchDeg, speed, 0);
    }

    /**
     * Draws the ballistic range meter below the weapon tab for turrets.
     */
    protected static void drawRangeMeter(GuiGraphics graphics, EntityTurret turret, double yPlacement, int blitPosition) {
        WeaponInstance<?> weaponData = turret.getWeaponData();
        if (weaponData == null) return;
        WeaponStats stats = weaponData.getStats();
        if (!(stats instanceof BulletStats bulletStats)) return;

        // Check if this turret has range meter enabled in its JSON
        if (turret.getStats() == null || !turret.getStats().isShowRangeMeter()) return;

        float pitch = turret.getXRot();
        double speed = bulletStats.getSpeed();
        int range = calcBallisticRange(pitch, speed, bulletStats.getMaxAge());

        // elevation angle: negative pitch = up in Minecraft
        int elevation = (int) Math.round(-pitch);

        String rangeText = range >= 0 ? range + " бл." : "---";
        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, blitPosition + 2);
        graphics.drawString(FONT, "Метрометр: " + rangeText + "  " + elevation + "°", 16, (int)(yPlacement + 26), 0x00ff88);
        graphics.pose().popPose();
    }
}
