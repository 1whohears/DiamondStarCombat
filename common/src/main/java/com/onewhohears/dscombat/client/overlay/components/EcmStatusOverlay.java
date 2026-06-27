package com.onewhohears.dscombat.client.overlay.components;

import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

/**
 * HUD overlay showing ECM jammer activity as an animated bar diagram.
 * Bars are tall and animated when jamming, short and dim when idle.
 * Drawn in the bottom-left corner.
 */
public class EcmStatusOverlay extends VehicleOverlayComponent {

    // How many ticks the "jam active" animation lasts after a jam event
    private static final int JAM_DISPLAY_TICKS = 15;

    private static int lastJamTick = -1000;
    private static int clientTick = 0;

    /** Called from ToClientEcmJam when a jam event is received */
    public static void onJamEvent() {
        lastJamTick = clientTick;
    }

    /** Called every client tick from ClientEventHandlers */
    public static void tick() {
        clientTick++;
    }

    /** Returns true if the jammer is currently actively jamming missiles */
    public static boolean isActive() {
        return getActivity() > 0.01f;
    }

    // Layout
    private static final int BAR_COUNT  = 7;
    private static final int BAR_WIDTH  = 4;
    private static final int BAR_GAP    = 3;
    private static final int MAX_HEIGHT = 22;
    private static final int MIN_HEIGHT = 3;
    private static final int PANEL_W    = BAR_COUNT * (BAR_WIDTH + BAR_GAP) + 8;
    private static final int PANEL_H    = MAX_HEIGHT + 18; // bars + label
    private static final int MARGIN_X   = 100;
    private static final int MARGIN_Y   = 6;

    // Colors (ARGB)
    private static final int COLOR_BG_ACTIVE = 0xCC001A0F;  // dark green bg when active
    private static final int COLOR_BG_IDLE   = 0x88111111;  // dark grey bg when idle
    private static final int COLOR_BAR_ON    = 0xFF00FF88;  // bright cyan-green bar
    private static final int COLOR_BAR_MID   = 0xFF00AA55;  // mid green
    private static final int COLOR_BAR_OFF   = 0xFF1A4A2A;  // dim green when idle
    private static final int COLOR_LABEL_ON  = 0xFF00FF88;
    private static final int COLOR_LABEL_OFF = 0xFF336644;
    private static final int COLOR_BORDER    = 0xFF00FF88;

    @Override
    protected boolean shouldRender(Gui gui, GuiGraphics graphics, float partialTick,
                                   int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        if (!(getPlayerRootVehicle() instanceof EntityVehicle vehicle)) return false;
        return vehicle.partsManager.getActiveJammerStrength() > 0f;
    }

    @Override
    protected void render(Gui gui, GuiGraphics graphics, float partialTick,
                          int screenWidth, int screenHeight) {
        int x = MARGIN_X;
        int y = screenHeight - PANEL_H - MARGIN_Y;

        float activity = getActivity();
        boolean active = activity > 0.01f;

        // Background panel
        graphics.fill(x, y, x + PANEL_W, y + PANEL_H, active ? COLOR_BG_ACTIVE : COLOR_BG_IDLE);

        // Border (only when active)
        if (active) {
            int bc = withAlpha(COLOR_BORDER, activity * 0.8f);
            graphics.fill(x,                  y,                  x + PANEL_W,     y + 1,          bc); // top
            graphics.fill(x,                  y + PANEL_H - 1,    x + PANEL_W,     y + PANEL_H,    bc); // bottom
            graphics.fill(x,                  y,                  x + 1,           y + PANEL_H,    bc); // left
            graphics.fill(x + PANEL_W - 1,    y,                  x + PANEL_W,     y + PANEL_H,    bc); // right
        }

        // Label "ECM" + status
        String label = active ? "ECM JAM" : "ECM";
        int labelColor = active ? COLOR_LABEL_ON : COLOR_LABEL_OFF;
        graphics.drawString(FONT, label, x + 3, y + 3, labelColor, false);

        // Bars
        int barsBaseY = y + PANEL_H - 2; // bottom of bars area
        for (int i = 0; i < BAR_COUNT; i++) {
            float barActivity = computeBarActivity(i, activity);
            int barH = MIN_HEIGHT + (int) ((MAX_HEIGHT - MIN_HEIGHT) * barActivity);

            int bx = x + 4 + i * (BAR_WIDTH + BAR_GAP);
            int by = barsBaseY - barH;

            int color = getBarColor(barActivity);
            graphics.fill(bx, by, bx + BAR_WIDTH, barsBaseY, color);
        }
    }

    /**
     * Each bar gets a slightly different activity level based on a sine wave,
     * creating a "spectrum analyzer" wave effect when active.
     */
    private float computeBarActivity(int barIndex, float globalActivity) {
        if (globalActivity < 0.01f) return 0f;

        // Phase offset per bar — creates a travelling wave
        double phase = (clientTick * 0.18) + barIndex * (Math.PI / (BAR_COUNT - 1));
        float wave = (float)(0.5 + 0.5 * Math.sin(phase)); // 0..1

        // Center bars slightly taller on average
        float centerBias = (float)(0.6 + 0.4 * Math.sin(Math.PI * barIndex / (BAR_COUNT - 1)));

        return globalActivity * (0.4f + 0.6f * wave * centerBias);
    }

    private int getBarColor(float barActivity) {
        if (barActivity < 0.01f) return COLOR_BAR_OFF;
        if (barActivity > 0.6f)  return withAlpha(COLOR_BAR_ON, 0.5f + 0.5f * barActivity);
        return withAlpha(COLOR_BAR_MID, 0.4f + 0.6f * barActivity);
    }

    /** Returns 0.0 (idle) to 1.0 (actively jamming) */
    private static float getActivity() {
        int ticksSince = clientTick - lastJamTick;
        if (ticksSince >= JAM_DISPLAY_TICKS) return 0f;
        // Smooth fade-out
        return 1f - (float) ticksSince / JAM_DISPLAY_TICKS;
    }

    private static int withAlpha(int rgb, float alpha) {
        int a = Math.max(0, Math.min(255, (int)(alpha * 255)));
        return (a << 24) | (rgb & 0x00FFFFFF);
    }

    @Override
    protected @NotNull String componentId() {
        return "dscombat_ecm_status";
    }
}
