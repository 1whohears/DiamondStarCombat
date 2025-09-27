package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.client.overlay.HudLayoutManager;
import com.onewhohears.dscombat.data.vehicle.VehicleType;
import com.onewhohears.dscombat.entity.vehicle.EntityGroundVehicle;
import com.onewhohears.dscombat.entity.vehicle.EntityHelicopter;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.vehicle.EntityPlane;
import com.onewhohears.dscombat.util.UtilVehicleEntity;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Modern consolidated vehicle HUD. Minimal, readable panels with key flight metrics.
 */
public class ModernHudOverlay extends VehicleOverlayComponent {
    // HUD state (client-only, simple static cache with edge detection on keypress)
    private static boolean lastF7 = false, lastF8 = false;
    // 0 = Minimal, 1 = Full
    private static int hudMode = 0;
    // hints: 0 = Off, 1 = Compact row, 2 = Full panel
    private static int hintsMode = 1;
    // Layout edit
    private static boolean lastF6 = false;
    private static boolean editMode = false; // true when HudLayoutEditScreen is open
    private static String draggingId = null; private static boolean resizing = false;
    private static double dragDX = 0, dragDY = 0; // normalized offset within rect for move
    private static final double RESIZE_HANDLE = 14; // px

    private int argb(float a, int r, int g, int b) {
        int ai = (int) (a * 255) & 0xFF;
        return (ai << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    private String vehicleTypeId(EntityVehicle v) {
        if (v instanceof EntityHelicopter) return "HELICOPTER";
        if (v instanceof EntityPlane) return "PLANE";
        if (v.getStats().isBoat()) return "BOAT";
        if (v.getStats().getType() == VehicleType.CAR) {
            if (v.getStats().asCar().isTank()) return "TANK";
            return "CAR";
        }
        return "GENERIC";
    }

    private HudLayoutManager.Rect getRect(EntityVehicle v, String id, int px, int py, int pw, int ph, int sw, int sh) {
        String vt = vehicleTypeId(v);
        HudLayoutManager.Rect fb = new HudLayoutManager.Rect(px/(double)sw, py/(double)sh, pw/(double)sw, ph/(double)sh);
        return HudLayoutManager.get(vt, id, fb);
    }

    private void setRect(EntityVehicle v, String id, HudLayoutManager.Rect r) {
        HudLayoutManager.set(vehicleTypeId(v), id, r);
    }

    private void drawEditHandle(PoseStack ps, int x, int y, int w, int h, String label, int text) {
        int border = 1;
        fill(ps, x - border, y - border, x + w + border, y + h + border, withAlpha(new Color(255,255,255).getRGB(), 0.12f));
        drawString(ps, FONT, label, x + 2, y - 10, text);
        // resize handle (triangle at bottom-right)
        fill(ps, x + w - 8, y + h - 2, x + w + 2, y + h + 2, withAlpha(new Color(255,255,255).getRGB(), 0.3f));
    }

    private void handleDragging(EntityVehicle v, int sw, int sh) {
        var mc = net.minecraft.client.Minecraft.getInstance();
        double mxWin = mc.mouseHandler.xpos();
        double myWin = mc.mouseHandler.ypos();
        double mxGui = mxWin * mc.getWindow().getGuiScaledWidth() / mc.getWindow().getScreenWidth();
        double myGui = myWin * mc.getWindow().getGuiScaledHeight() / mc.getWindow().getScreenHeight();
        float scale = com.onewhohears.dscombat.Config.CLIENT.modernHudScale.get().floatValue();
        double mx = mxGui / scale, my = myGui / scale;

        boolean left = mc.mouseHandler.isLeftPressed();

        // Check selection start
        if (draggingId == null && left) {
            // Iterate known ids
            selectIfInside(v, "att", mx, my, sw, sh);
            selectIfInside(v, "head", mx, my, sw, sh);
            selectIfInside(v, "br", mx, my, sw, sh);
            selectIfInside(v, "bl", mx, my, sw, sh);
        }
        // Drag
        if (draggingId != null && left) {
            HudLayoutManager.Rect r = HudLayoutManager.get(vehicleTypeId(v), draggingId, new HudLayoutManager.Rect(0,0,0.3,0.2));
            if (resizing) {
                r.w = clamp01(mx / sw - r.x); r.h = clamp01(my / sh - r.y);
            } else {
                r.x = clamp01(mx / sw - dragDX); r.y = clamp01(my / sh - dragDY);
            }
            setRect(v, draggingId, r);
        }
        if (!left) draggingId = null;
    }

    private void selectIfInside(EntityVehicle v, String id, double mx, double my, int sw, int sh) {
        HudLayoutManager.Rect r = HudLayoutManager.get(vehicleTypeId(v), id, new HudLayoutManager.Rect(0.4,0.4,0.2,0.1));
        double x = r.x * sw, y = r.y * sh, w = r.w * sw, h = r.h * sh;
        if (mx >= x && mx <= x + w && my >= y && my <= y + h) {
            draggingId = id; resizing = (mx >= x + w - RESIZE_HANDLE && my >= y + h - RESIZE_HANDLE);
            dragDX = (mx - x) / sw; dragDY = (my - y) / sh;
        }
    }

    private double clamp01(double v) { return v < 0 ? 0 : (v > 1 ? 1 : v); }

    // Minimal row of key hints
    private void drawHintsCompact(PoseStack ps, int sw, int sh, int text) {
        String s = "[Space] Hover  [K] Gear  [V] Flares  [O] Radar  [F8] Hints  [F7] HUD";
        int y = sh - 14;
        drawCenteredString(ps, FONT, s, sw / 2, y, text);
    }

    // Full legacy-like hints panel on left
    private void drawHintsFull(PoseStack ps, int sw, int sh, int text) {
        int x = 8, y = 8, w = 220, h = sh - 16;
        fill(ps, x, y, x + w, y + h, withAlpha(new Color(20,20,20).getRGB(), 0.85f));
        int yy = y + 8;
        drawString(ps, FONT, "Controls (F8 to hide)", x + 8, yy, text); yy += 12;
        // List a curated set of important bindings
        drawString(ps, FONT, "Hover: Space", x + 8, yy, text); yy += 10;
        drawString(ps, FONT, "Gear: K", x + 8, yy, text); yy += 10;
        drawString(ps, FONT, "Flares: V", x + 8, yy, text); yy += 10;
        drawString(ps, FONT, "Radar Mode: O", x + 8, yy, text); yy += 10;
        drawString(ps, FONT, "Track Target: RSHIFT", x + 8, yy, text); yy += 10;
        drawString(ps, FONT, "Gimbal: ;", x + 8, yy, text); yy += 10;
        drawString(ps, FONT, "Throttle: Up/Down", x + 8, yy, text); yy += 10;
        drawString(ps, FONT, "Yaw: A/D", x + 8, yy, text); yy += 10;
        drawString(ps, FONT, "Roll: Left/Right", x + 8, yy, text); yy += 10;
        drawString(ps, FONT, "Pitch: W/S", x + 8, yy, text); yy += 10;
        drawString(ps, FONT, "HUD Mode: F7", x + 8, yy, text); yy += 10;
    }

    private int withAlpha(int rgb, float a) {
        int ai = (int) (a * 255) & 0xFF;
        return (ai << 24) | (rgb & 0xFFFFFF);
    }

    private static String speedUnitLabel() {
        String u = com.onewhohears.dscombat.Config.CLIENT.hudSpeedUnit.get();
        return switch (u) { case "kph" -> "km/h"; case "knots" -> "kt"; default -> "m/s"; };
    }

    private static double speedConverted(double mps) {
        String u = com.onewhohears.dscombat.Config.CLIENT.hudSpeedUnit.get();
        return switch (u) {
            case "kph" -> mps * 3.6;
            case "knots" -> mps * 1.943844492;
            default -> mps;
        };
    }

    private static String altUnitLabel() {
        String u = com.onewhohears.dscombat.Config.CLIENT.hudAltUnit.get();
        return u.equals("ft") ? "ft" : "m";
    }

    private static double altConverted(double meters) {
        String u = com.onewhohears.dscombat.Config.CLIENT.hudAltUnit.get();
        return u.equals("ft") ? meters * 3.280839895 : meters;
    }

    @Override
    protected boolean shouldRender(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        if (!com.onewhohears.dscombat.Config.CLIENT.enableModernHUD.get()) return false;
        return getPlayerRootVehicle() instanceof EntityVehicle;
    }

    @Override
    protected void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        updateToggles();
        final float scale = com.onewhohears.dscombat.Config.CLIENT.modernHudScale.get().floatValue();
        final float alpha = com.onewhohears.dscombat.Config.CLIENT.modernHudOpacity.get().floatValue();
        poseStack.pushPose();
        poseStack.scale(scale, scale, 1f);

        int sw = (int) (screenWidth / scale);
        int sh = (int) (screenHeight / scale);

        EntityVehicle v = (EntityVehicle) getPlayerRootVehicle();
        assert v != null;
        boolean isHeli = v instanceof EntityHelicopter;
        boolean isPlane = v instanceof EntityPlane;

        // Show-all hold (F9): temporarily force Full HUD and Full hints
        boolean showAllHold = DSCKeys.hudShowAllKey != null && DSCKeys.hudShowAllKey.isDown();
        int localHudMode = showAllHold ? 1 : hudMode;
        int localHintsMode = showAllHold ? 2 : hintsMode;

        // Colors
        int panel = withAlpha(new Color(24, 24, 24).getRGB(), alpha);
        int text = new Color(220, 255, 220).getRGB();
        int accent = new Color(88, 220, 88).getRGB();
        int warn = new Color(255, 170, 0).getRGB();
        int danger = new Color(255, 80, 80).getRGB();

        // Center: Attitude + Heading ribbon
        int defAttW = (localHudMode == 0) ? 150 : 180;
        int defAttH = (localHudMode == 0) ? 90 : 110;
        int defAttX = sw / 2 - defAttW / 2;
        int defAttY = sh / 2 - defAttH / 2 + 12;
        HudLayoutManager.Rect attR = getRect(v, "att", defAttX, defAttY, defAttW, defAttH, sw, sh);
        int attX = (int)Math.round(attR.x * sw), attY = (int)Math.round(attR.y * sh);
        int attW = (int)Math.round(attR.w * sw), attH = (int)Math.round(attR.h * sh);
        if (localHudMode == 1) {
            fill(poseStack, attX, attY, attX + attW, attY + attH, withAlpha(new Color(16, 16, 16).getRGB(), alpha * 0.6f));
        }

        // Heading ribbon above attitude
        int defHrW = (localHudMode == 0) ? 180 : 220, hrH = 18;
        int defHrX = sw / 2 - defHrW / 2; int defHrY = attY - hrH - 4;
        HudLayoutManager.Rect headR = getRect(v, "head", defHrX, defHrY, defHrW, hrH, sw, sh);
        int hrX = (int)Math.round(headR.x * sw), hrY = (int)Math.round(headR.y * sh), hrW = (int)Math.round(headR.w * sw);
        if (localHudMode == 1) fill(poseStack, hrX, hrY, hrX + hrW, hrY + hrH, withAlpha(new Color(16, 16, 16).getRGB(), alpha * 0.6f));
        drawHeadingRibbon(poseStack, v, hrX, hrY, hrW, hrH, text, accent);

        // Draw attitude ladder (simple horizon with pitch offset and bank rotation)
        drawAttitude(poseStack, v, attX, attY, attW, attH, text, accent);

        // Plane-only: show AOA near attitude
        if (isPlane) {
            try {
                double aoa = ((EntityPlane) v).getAOA();
                drawString(poseStack, FONT, String.format("AOA %3.1f", aoa), attX + attW + 6, attY + attH/2 - 4, text);
            } catch (Throwable ignored) {}
        }

        // Bottom-right: Throttle + RPM + Speed
        int defPrW = 190, defPrH = 96;
        int defPrX = sw - defPrW - PADDING - 6;
        int defPrY = sh - defPrH - PADDING - 4;
        HudLayoutManager.Rect brR = getRect(v, "br", defPrX, defPrY, defPrW, defPrH, sw, sh);
        int prX = (int)Math.round(brR.x * sw), prY = (int)Math.round(brR.y * sh), prW = (int)Math.round(brR.w * sw), prH = (int)Math.round(brR.h * sh);
        if (localHudMode == 1) fill(poseStack, prX, prY, prX + prW, prY + prH, panel);

        float throttleGoal = v.inputs.getGoalThrottle(v);
        float throttlePct = v.getStats().negativeThrottle ? (throttleGoal + 1f) / 2f : throttleGoal;
        int barX = prX + 10;
        int barY = prY + 10;
        int barW = 14;
        int barH = prH - 20;
        // Track background
        if (localHudMode == 1) fill(poseStack, barX, barY, barX + barW, barY + barH, withAlpha(new Color(60, 60, 60).getRGB(), alpha));
        // Fill
        int fillH = (int) (barH * Math.max(0f, Math.min(1f, throttlePct)));
        if (localHudMode == 1) {
            fill(poseStack, barX, barY + (barH - fillH), barX + barW, barY + barH, withAlpha(accent, alpha));
            drawString(poseStack, FONT, "THR", barX - 2, prY + prH - 12, text);
        }

        // RPM bar for heli
        double rpmPct = 0.0;
        if (isHeli) {
            EntityHelicopter heli = (EntityHelicopter)v; rpmPct = Math.max(0.0, Math.min(1.0, heli.getRotorPower()));
        }
        int rbX = barX + barW + 16;
        int rbY = barY;
        int rbW = barW;
        int rbH = barH;
        if (localHudMode == 1 && isHeli) fill(poseStack, rbX, rbY, rbX + rbW, rbY + rbH, withAlpha(new Color(60, 60, 60).getRGB(), alpha));
        int rpmFill = (int) (rbH * rpmPct);
        if (localHudMode == 1 && isHeli) {
            fill(poseStack, rbX, rbY + (rbH - rpmFill), rbX + rbW, rbY + rbH, withAlpha(warn, alpha));
            drawString(poseStack, FONT, "RPM", rbX - 2, prY + prH - 12, text);
        }

        // Speed & climb & heading (with units)
        double speed = speedConverted(v.getDeltaMovement().length() * 20.0);
        double climb = speedConverted(v.getDeltaMovement().y * 20.0);
        int metricsX = rbX + rbW + 12;
        int metricsY = barY + (localHudMode == 0 ? -8 : 0);
        drawString(poseStack, FONT, String.format("spd  %4.1f %s", speed, speedUnitLabel()), metricsX, metricsY, text);
        if (localHudMode == 1) drawString(poseStack, FONT, String.format("clb  %4.1f %s", climb, speedUnitLabel()), metricsX, metricsY + 10, climb >= 0 ? text : warn);
        drawString(poseStack, FONT, String.format("hdg  %3.0f°", (v.getYRot() + 360f) % 360f), metricsX, metricsY + (localHudMode == 1 ? 20 : 10), text);

        // Fuel / Health / Armor bars under metrics (only in Full mode)
        float fuelPct = 0f;
        try {
            fuelPct = Math.max(0f, Math.min(1f, v.getCurrentFuel() / Math.max(1f, v.getMaxFuel())));
        } catch (Throwable ignored) {}
        int barsX = metricsX;
        int barsY = metricsY + (hudMode == 1 ? 36 : 22);
        int bw = 120, bh = 6;
        if (localHudMode == 1) {
            fill(poseStack, barsX, barsY, barsX + bw, barsY + bh, withAlpha(new Color(60,60,60).getRGB(), alpha));
            fill(poseStack, barsX, barsY, barsX + (int)(bw * fuelPct), barsY + bh, withAlpha(new Color(0, 180, 255).getRGB(), alpha));
            drawString(poseStack, FONT, String.format("fuel %3.0f%%", fuelPct * 100f), barsX + bw + 6, barsY - 1, text);
        }
        // Health
        barsY += bh + 4;
        float hp = v.getHealth(), hpMax = v.getMaxHealth();
        float hpPct = Math.max(0f, Math.min(1f, hpMax <= 0 ? 0 : hp / hpMax));
        if (localHudMode == 1) {
            fill(poseStack, barsX, barsY, barsX + bw, barsY + bh, withAlpha(new Color(60,60,60).getRGB(), alpha));
            fill(poseStack, barsX, barsY, barsX + (int)(bw * hpPct), barsY + bh, withAlpha(new Color(80, 220, 80).getRGB(), alpha));
            drawString(poseStack, FONT, String.format("hp %3.0f%%", hpPct * 100f), barsX + bw + 6, barsY - 1, text);
        }
        // Armor
        barsY += bh + 4;
        float ar = v.getArmor(), arMax = v.getMaxTotalArmor();
        float arPct = Math.max(0f, Math.min(1f, arMax <= 0 ? 0 : ar / arMax));
        if (localHudMode == 1) {
            fill(poseStack, barsX, barsY, barsX + bw, barsY + bh, withAlpha(new Color(60,60,60).getRGB(), alpha));
            fill(poseStack, barsX, barsY, barsX + (int)(bw * arPct), barsY + bh, withAlpha(new Color(180, 180, 60).getRGB(), alpha));
            drawString(poseStack, FONT, String.format("arm %3.0f%%", arPct * 100f), barsX + bw + 6, barsY - 1, text);
        }

        // Bottom-left: Altitude + IGE/ETL/VRS chips + simple alt tape
        int plX = PADDING + 6;
        int defPlW = (localHudMode == 0) ? 180 : 220;
        int defPlH = (localHudMode == 0) ? 82 : 110;
        int defPlY = sh - defPlH - PADDING;
        HudLayoutManager.Rect blR = getRect(v, "bl", plX, defPlY, defPlW, defPlH, sw, sh);
        int plY = (int)Math.round(blR.y * sh); int plW = (int)Math.round(blR.w * sw); int plH = (int)Math.round(blR.h * sh);
        plX = (int)Math.round(blR.x * sw);
        fill(poseStack, plX, plY, plX + plW, plY + plH, withAlpha(new Color(24,24,24).getRGB(), alpha * (localHudMode == 0 ? 0.5f : 0.85f)));

        // Altitude tape
        int tapeX = plX + 10;
        int tapeY = plY + 10;
        int tapeW = 14;
        int tapeH = plH - 20;
        fill(poseStack, tapeX, tapeY, tapeX + tapeW, tapeY + tapeH, withAlpha(new Color(60,60,60).getRGB(), alpha));
        double alt = altConverted(v.getAltitude());
        drawString(poseStack, FONT, String.format("alt %4.0f %s", alt, altUnitLabel()), tapeX + tapeW + 8, tapeY, text);
        // Pointer
        int pty = tapeY + tapeH / 2;
        fill(poseStack, tapeX + tapeW + 2, pty - 1, tapeX + tapeW + 10, pty + 1, withAlpha(accent, alpha));

        // Chips
        int chipY = plY + (hudMode == 0 ? 28 : 34);
        int chipH = 14;
        int chipW = 50;
        float chipAlpha = alpha;
        if (isHeli) {
            // IGE
            boolean ige = false;
            double hMax = com.onewhohears.dscombat.Config.SERVER.groundEffectMaxHeight.get();
            if (com.onewhohears.dscombat.Config.SERVER.enableGroundEffect.get() && hMax > 0) {
                int limit = (int) Math.ceil(hMax) + 1;
                int aglBlocks = UtilVehicleEntity.getDistFromGround(v, limit, true);
                ige = aglBlocks < hMax;
            }
            int c1 = withAlpha(new Color(ige ? 88 : 60, ige ? 220 : 60, 88).getRGB(), chipAlpha);
            fill(poseStack, plX + 10, chipY, plX + 10 + chipW, chipY + chipH, c1);
            drawString(poseStack, FONT, "IGE", plX + 12, chipY + 3, Color.BLACK.getRGB());

            // ETL
            double speedXZ = v.getDeltaMovement().horizontalDistance();
            double full = Math.max(1e-6, com.onewhohears.dscombat.Config.SERVER.translationalLiftFullSpeed.get());
            double etlFactor = Math.min(1.0, speedXZ / full);
            boolean etl = com.onewhohears.dscombat.Config.SERVER.enableTranslationalLift.get() && etlFactor > 0.05;
            int c2 = withAlpha(new Color(etl ? 220 : 60, etl ? 180 : 60, 88).getRGB(), chipAlpha);
            fill(poseStack, plX + 10 + chipW + 6, chipY, plX + 10 + chipW * 2 + 6, chipY + chipH, c2);
            drawString(poseStack, FONT, "ETL", plX + 12 + chipW + 6, chipY + 3, Color.BLACK.getRGB());

            // VRS
            double vy = v.getDeltaMovement().y;
            double descent = vy < 0 ? -vy : 0.0;
            double trig = com.onewhohears.dscombat.Config.SERVER.vrsDescentTrigger.get();
            double cap = com.onewhohears.dscombat.Config.SERVER.vrsHorizMaxSpeed.get();
            boolean vrs = com.onewhohears.dscombat.Config.SERVER.enableVRS.get() && descent > trig && speedXZ < cap;
            int c3 = withAlpha(new Color(vrs ? 220 : 60, 88, 88).getRGB(), chipAlpha);
            fill(poseStack, plX + 10 + (chipW + 6) * 2, chipY, plX + 10 + (chipW + 6) * 3, chipY + chipH, c3);
            drawString(poseStack, FONT, "VRS", plX + 12 + (chipW + 6) * 2, chipY + 3, Color.BLACK.getRGB());
        } else if (isPlane) {
            // Plane: GEAR / FLAPS
            boolean gearOut = v.canToggleLandingGear() && v.isLandingGear();
            int cg = withAlpha(new Color(gearOut ? 88 : 60, gearOut ? 220 : 60, 88).getRGB(), chipAlpha);
            fill(poseStack, plX + 10, chipY, plX + 10 + chipW, chipY + chipH, cg);
            drawString(poseStack, FONT, "GEAR", plX + 12, chipY + 3, Color.BLACK.getRGB());

            boolean flaps = v.isFlapsDown();
            int cf = withAlpha(new Color(flaps ? 220 : 60, flaps ? 180 : 60, 88).getRGB(), chipAlpha);
            fill(poseStack, plX + 10 + chipW + 6, chipY, plX + 10 + chipW * 2 + 6, chipY + chipH, cf);
            drawString(poseStack, FONT, "FLAPS", plX + 12 + chipW + 6, chipY + 3, Color.BLACK.getRGB());
        }

        // Speed tape (left of attitude) - only in Full mode
        int stW = 54, stH = 90;
        int stX = attX - stW - 8;
        int stY = attY + (attH - stH) / 2;
        if (localHudMode == 1) fill(poseStack, stX, stY, stX + stW, stY + stH, withAlpha(new Color(16, 16, 16).getRGB(), alpha * 0.6f));
        double spd = speedConverted(v.getDeltaMovement().length() * 20.0);
        if (localHudMode == 1) drawString(poseStack, FONT, String.format("%4.1f %s", spd, speedUnitLabel()), stX + 4, stY + stH/2 - 4, text);

        // Alt tape (right of attitude)
        int atW = 54, atH = 90;
        int atX = attX + attW + 8;
        int atY = attY + (attH - atH) / 2;
        if (localHudMode == 1) {
            fill(poseStack, atX, atY, atX + atW, atY + atH, withAlpha(new Color(16, 16, 16).getRGB(), alpha * 0.6f));
            drawString(poseStack, FONT, String.format("%4.0f %s", alt, altUnitLabel()), atX + 4, atY + atH/2 - 4, text);
        }

        // Compact minimal numeric (top corners) to reduce clutter
        if (localHudMode == 0) {
            drawString(poseStack, FONT, String.format("%4.1f %s", speed, speedUnitLabel()), 12, 12, text);
            drawString(poseStack, FONT, String.format("%4.0f %s", alt, altUnitLabel()), sw - 90, 12, text);
        }

        // Draw compact key-hints row or full legacy-like list on demand
        if (localHintsMode == 2) drawHintsFull(poseStack, sw, sh, text);
        else if (localHintsMode == 1) drawHintsCompact(poseStack, sw, sh, text);

        // Layout edit overlay and interaction
        if (editMode) {
            drawEditHandle(poseStack, attX, attY, attW, attH, "att", text);
            drawEditHandle(poseStack, hrX, hrY, hrW, hrH, "head", text);
            drawEditHandle(poseStack, prX, prY, prW, prH, "br", text);
            drawEditHandle(poseStack, plX, plY, plW, plH, "bl", text);
            handleDragging(v, sw, sh);
        }

        poseStack.popPose();
    }

    private void updateToggles() {
        boolean f7 = DSCKeys.hudModeToggleKey != null && DSCKeys.hudModeToggleKey.isDown();
        boolean f8 = DSCKeys.hudHintsToggleKey != null && DSCKeys.hudHintsToggleKey.isDown();
        boolean f6 = DSCKeys.hudLayoutEditKey != null && DSCKeys.hudLayoutEditKey.isDown();
        if (f7 && !lastF7) {
            hudMode = (hudMode + 1) % 2; // Minimal <-> Full
        }
        if (f8 && !lastF8) {
            hintsMode = (hintsMode + 1) % 3; // Off -> Compact -> Full -> Off
        }
        if (f6 && !lastF6) {
            var mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.screen instanceof com.onewhohears.dscombat.client.screen.HudLayoutEditScreen s) {
                s.closeAndSave();
            } else {
                mc.setScreen(new com.onewhohears.dscombat.client.screen.HudLayoutEditScreen(() -> {
                    HudLayoutManager.save();
                }));
            }
        }
        lastF7 = f7; lastF8 = f8; lastF6 = f6;
        // Update edit mode based on whether the edit screen is open
        var mc = net.minecraft.client.Minecraft.getInstance();
        editMode = mc.screen instanceof com.onewhohears.dscombat.client.screen.HudLayoutEditScreen;
    }

    private void drawHeadingRibbon(PoseStack ps, EntityVehicle v, int x, int y, int w, int h, int text, int accent) {
        double hdg = (v.getYRot() + 360.0) % 360.0;
        // center caret
        fill(ps, x + w/2 - 1, y, x + w/2 + 1, y + h, accent);
        // scale: 4 px/deg ~ 55° span
        double pxPerDeg = 4.0;
        int spanDeg = (int)(w / pxPerDeg);
        int start = (int)(hdg - spanDeg / 2);
        for (int deg = start - (start % 10); deg <= start + spanDeg; deg += 10) {
            int d = (int)Math.floor((deg - hdg) * pxPerDeg) + w/2;
            if (d < 0 || d > w) continue;
            int vlen = (deg % 30 == 0) ? h - 4 : h - 8;
            fill(ps, x + d, y + 2, x + d + 1, y + vlen, withAlpha(text, 0.5f));
            if (deg % 30 == 0) {
                int label = ((deg % 360) + 360) % 360;
                String s = Integer.toString(label);
                drawCenteredString(ps, FONT, s, x + d, y + h - 12, text);
            }
        }
    }

    private void drawAttitude(PoseStack ps, EntityVehicle v, int x, int y, int w, int h, int text, int accent) {
        float pitch = v.getXRot();   // degrees nose up positive
        float roll = v.zRot;         // degrees right wing down positive
        // horizon scale: 2 px per degree
        float pxPerDeg = 2.0f;
        ps.pushPose();
        ps.translate(x + w / 2.0, y + h / 2.0, 0);
        ps.mulPose(Vector3f.ZP.rotationDegrees(-roll));
        int bandW = w - 10;
        int cy = (int)(pitch * pxPerDeg);
        // horizon line
        fill(ps, -bandW/2, cy - 1, bandW/2, cy + 1, text);
        // bank ticks every 15° of pitch around center (visual reference)
        for (int pd = -45; pd <= 45; pd += 15) {
            int yy = (int)(pd * pxPerDeg) + cy;
            int len = (pd % 30 == 0) ? 12 : 8;
            fill(ps, -len, yy, -2, yy + 1, withAlpha(text, 0.6f));
            fill(ps, 2, yy, len, yy + 1, withAlpha(text, 0.6f));
        }
        // center caret
        fill(ps, -6, -1, 6, 1, accent);
        fill(ps, -1, -6, 1, 6, accent);
        ps.popPose();
    }

    @Override
    protected @NotNull String componentId() {
        return "dscombat_modern_hud";
    }
}
