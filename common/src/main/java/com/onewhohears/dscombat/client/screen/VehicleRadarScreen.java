package com.onewhohears.dscombat.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.data.radar.RadarStats;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.UtilVehicleEntity;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.UtilScreen;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class VehicleRadarScreen extends VehicleSubScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/background/vehicle_radar_screen.png");
    public static final ResourceLocation RADAR_BG = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/radar.png");
    public static final ResourceLocation RADAR_PING = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/hud_ping.png");
    public static final ResourceLocation RADAR_PING_HOVER = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/hud_ping_hover.png");
    public static final ResourceLocation RADAR_PING_SELECT = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/hud_ping_select.png");
    public static final ResourceLocation PING_DATA = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/ping_data_icons_color.png");

    private static final int imageWidth = 240, imageHeight = 180;
    private static final int textureSize = 256;
    private static final int PING_SIZE = 6;

    private int leftTicks = 0;

    protected VehicleRadarScreen() {
        super("screen.dscombat.vehicle_radar_screen",
                BG_TEXTURE, imageWidth, imageHeight, textureSize, textureSize);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderRadar(graphics, mouseX, mouseY, partialTick);
    }

    protected void renderRadar(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, RADAR_BG);
        graphics.blit(RADAR_BG, guiX+4, guiY+66, 110, 110, 0, 0,
                127, 128, 127, 128);
        EntityVehicle vehicle = getVehicle();
        RadarSystem radar = vehicle.radarSystem;
        List<RadarStats.RadarPing> pings = radar.getClientRadarPings();
        if (pings.isEmpty()) return;
        int selected = radar.getClientSelectedPingIndex();
        int hover = DSCClientInputs.getRadarHoverIndex();
        int centerX = guiX + 4 + 55, centerY = guiY + 66 + 55;
        boolean hovering = false;
        for (int i = 0; i < pings.size(); ++i) {
            RadarStats.RadarPing ping = pings.get(i);
            Vec3 dp = ping.getPosForClient().subtract(vehicle.position());
            double dist = dp.horizontalDistance();
            double screen_dist = getScreenDistRatio(dist);
            if (screen_dist > 1) screen_dist = 1;
            float yaw = (UtilAngles.getYaw(dp)-vehicle.getYRot()+180)*Mth.DEG_TO_RAD;
            int x = Mth.clamp((int)(-Mth.sin(yaw)*55*screen_dist), -50, 50) + centerX;
            int y = Mth.clamp((int)(Mth.cos(yaw)*55*screen_dist), -50, 50) + centerY;
            // Draw jammer rings before normal ping
            if (ping.entityType.isJammer()) {
                drawJammerRings(graphics, x, y, ping.isJamming());
                continue;
            }
            if (drawPingAtPos(ping, x, y, i == selected, i == hover,
                    graphics, mouseX, mouseY, partialTick, vehicle)) {
                DSCClientInputs.setRadarHoverIndex(i);
                hovering = true;
            }
        }
        if (!hovering) DSCClientInputs.resetRadarHoverIndex();
    }

    /** Draws pulsing concentric rings for an ECM jammer source.
     *  active=true: all rings pulse outward rapidly (jamming missiles)
     *  active=false: slow blink (idle) */
    private void drawJammerRings(GuiGraphics graphics, int cx, int cy, boolean active) {
        int[] radii = {4, 8, 13, 18};
        if (active) {
            // All rings animate — each ring has its own phase offset, creating a ripple
            long t = System.currentTimeMillis();
            int wave = (int)(t / 120) % radii.length;
            for (int i = 0; i < radii.length; ++i) {
                // brightness cycles: ring closest to wave front is brightest
                int dist = (i - wave + radii.length) % radii.length;
                int alpha = switch (dist) {
                    case 0 -> 0xFF;
                    case 1 -> 0xCC;
                    case 2 -> 0x88;
                    default -> 0x55;
                };
                int color = (alpha << 24) | 0xFFCC00;
                drawHollowCircle(graphics, cx, cy, radii[i], color);
            }
            // Bright center
            graphics.fill(cx - 2, cy - 2, cx + 3, cy + 3, 0xFFFFCC00);
        } else {
            // Slow blink — all rings on/off together every 600ms
            boolean on = (System.currentTimeMillis() / 600) % 2 == 0;
            int color = on ? 0x88FF8800 : 0x33FF6600;
            for (int r : radii) drawHollowCircle(graphics, cx, cy, r, color);
            // Dim center
            graphics.fill(cx - 1, cy - 1, cx + 2, cy + 2, on ? 0xAAFF8800 : 0x33FF6600);
        }
    }

    /** Draws a hollow circle using fill calls */
    private void drawHollowCircle(GuiGraphics graphics, int cx, int cy, int r, int color) {
        int steps = Math.max(16, r * 4);
        for (int i = 0; i < steps; i++) {
            double angle = 2 * Math.PI * i / steps;
            int px = cx + (int)(Math.cos(angle) * r);
            int py = cy + (int)(Math.sin(angle) * r);
            graphics.fill(px, py, px + 1, py + 1, color);
        }
    }

    private static final int HALF_PS = PING_SIZE/2, SQUARE_PS = (PING_SIZE*2)^2, LEFT = PING_SIZE*3/2, UP = HALF_PS+10;

    protected boolean drawPingAtPos(RadarStats.RadarPing ping, int x, int y, boolean selected, boolean hover,
                                 @NotNull GuiGraphics graphics, int mouseX, int mouseY,
                                 float partialTick, EntityVehicle vehicle) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (selected) RenderSystem.setShaderTexture(0, RADAR_PING_SELECT);
        else if (hover) RenderSystem.setShaderTexture(0, RADAR_PING_HOVER);
        else RenderSystem.setShaderTexture(0, RADAR_PING);
        graphics.blit(RADAR_PING, x-HALF_PS, y-HALF_PS, PING_SIZE, PING_SIZE, 0, 0,
                200, 200, 200, 200);
        if (new Vec2(x,y).distanceToSqr(new Vec2(mouseX,mouseY)) > SQUARE_PS) return false;
        int dist = (int) ping.getPosForClient().distanceTo(vehicle.position());
        int alt = UtilVehicleEntity.getDistFromSeaLevel(ping.getPosForClient().y, vehicle.getWorld());
        String text = dist + " | " + alt;
        Component comp = UtilMCText.literal(text);
        int width = font.width(comp);
        graphics.drawString(font, comp, (int)(x-width/2f), y-UP, 0x00FF00);
        RenderSystem.setShaderTexture(0, PING_DATA);
        graphics.blit(PING_DATA, x-LEFT, y-HALF_PS, PING_SIZE, PING_SIZE,
                ping.entityType.getIconIndex()*16, 0,
                16, 16, 240, 16);
        graphics.blit(PING_DATA, x+HALF_PS, y-HALF_PS, PING_SIZE, PING_SIZE,
                ping.terrainType.getIconIndex()*16, 0,
                16, 16, 240, 16);
        if (ping.isFriendly) {
            graphics.blit(PING_DATA, x-PING_SIZE, y+HALF_PS, PING_SIZE, PING_SIZE,
                    16*4, 0, 16, 16, 240, 16);
        }
        if (ping.isShared()) {
            graphics.blit(PING_DATA, x, y+HALF_PS, PING_SIZE, PING_SIZE,
                    16*9, 0, 16, 16, 240, 16);
        }
        return true;
    }

    protected double getScreenDistRatio(double distance) {
        return distance / DSCClientInputs.getRadarDisplayRange();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (DSCClientInputs.isRadarHovering()) {
            RadarSystem radar = getVehicle().radarSystem;
            List<RadarStats.RadarPing> pings = radar.getClientRadarPings();
            if (DSCClientInputs.getRadarHoverIndex() < pings.size()) {
                radar.clientSelectTarget(pings.get(DSCClientInputs.getRadarHoverIndex()));
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void init() {
        vertical_widget_shift = 10;
        super.init();
        // RADAR MODE
        positionWidgetGrid(CycleButton.<RadarStats.RadarMode>builder(value -> UtilMCText.translatable(value.getTranslatable()))
                        .withValues(RadarStats.RadarMode.values())
                        .withInitialValue(DSCClientInputs.getPreferredRadarMode())
                        .create(0, 0, 20, 20,
                                UtilMCText.translatable("ui.dscombat.radar_mode"),
                                onRadarModeCycle()),
                ROWS, COLUMNS, 1, 2);
        // RADAR DISPLAY RANGE FIELD
        vertical_widget_shift = 34;
        COLUMNS = 4;
        EditBox rangeBox = new EditBox(font, 0, 0, 20, 20, UtilMCText.empty());
        positionWidgetGrid(rangeBox, ROWS, COLUMNS, 3, 2);
        rangeBox.setValue(DSCClientInputs.getRadarDisplayRange()+"");
        rangeBox.setTextColor(0xFFFFFF);
        rangeBox.setResponder(onRadarDisplayRangeChange());
        // RADAR DISPLAY RANGE CYCLE
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("ui.dscombat.cycle_radar_display_range"),
                        onPress -> {
                            DSCClientInputs.cycleRadarDisplayRange();
                            rangeBox.setValue(DSCClientInputs.getRadarDisplayRange()+"");
                        }, Supplier::get),
                ROWS, COLUMNS, 0, 2, 3);
        // CYCLE RADAR PING
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("ui.dscombat.cycle_radar_target"),
                        onPress -> getVehicle().radarSystem.clientSelectNextTarget(), Supplier::get),
                ROWS, COLUMNS, 6, 2, 2);
        // CHANGE RADAR PING OVERLAY SIZE BOX
        EditBox pingSizeBox = new EditBox(font, 0, 0, 20, 20, UtilMCText.empty());
        positionWidgetGrid(pingSizeBox, ROWS, 6, 17, 2);
        pingSizeBox.setValue(Config.CLIENT.radarPingOverlaySize.get()+"");
        pingSizeBox.setTextColor(0xFFFFFF);
        pingSizeBox.setResponder(UtilScreen.getIntResponder(
                value -> Config.CLIENT.radarPingOverlaySize.set(Math.max(Math.min(value, 1000), 10))));
        // INCREASE PING SIZE BUTTON
        PageButton increaseSize = new PageButton(0, 0, true, button -> {
            Config.CLIENT.radarPingOverlaySize.set(Math.min(Config.CLIENT.radarPingOverlaySize.get() + 50, 1000));
            pingSizeBox.setValue(Config.CLIENT.radarPingOverlaySize.get()+"");
            }, false);
        positionWidgetGrid(increaseSize, ROWS, 6, 16, 2);
        // DECREASE PING SIZE BUTTON
        PageButton decreaseSize = new PageButton(0, 0, false, button -> {
            Config.CLIENT.radarPingOverlaySize.set(Math.max(Config.CLIENT.radarPingOverlaySize.get() - 50, 10));
            pingSizeBox.setValue(Config.CLIENT.radarPingOverlaySize.get()+"");
            }, false);
        positionWidgetGrid(decreaseSize, ROWS, 6, 15, 2);
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics graphics) {
        super.renderBackground(graphics);
        graphics.drawString(font, UtilMCText.translatable("ui.dscombat.change_ping_size"),
                guiX+left_padding+126, guiY+top_padding+82, 0x555555);
    }

    private CycleButton.OnValueChange<RadarStats.RadarMode> onRadarModeCycle() {
        return (button, value) -> DSCClientInputs.setPreferredRadarMode(value);
    }

    private Consumer<String> onRadarDisplayRangeChange() {
        return range -> {
            try {
                double number = Double.parseDouble(range);
                DSCClientInputs.setRadarDisplayRange(number);
            } catch(NumberFormatException e) {
                DSCClientInputs.setRadarDisplayRange(250);
            }
        };
    }
}
