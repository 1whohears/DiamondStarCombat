package com.onewhohears.dscombat.client.screen;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.client.overlay.HudLayoutManager;
import com.onewhohears.dscombat.entity.vehicle.EntityHelicopter;
import com.onewhohears.dscombat.entity.vehicle.EntityPlane;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Simple layout edit screen that pauses camera and enables mouse.
 * Drag panels to move, drag the lower-right corner to resize. Positions are saved on close.
 */
public class HudLayoutEditScreen extends Screen {
    private final Runnable onSave;

    private float scale;
    private int sw, sh; // scaled HUD space width/height (after applying scale)

    private EntityVehicle vehicle;
    private boolean isHeli, isPlane;

    private static final String[] IDS = new String[]{"att","head","br","bl"};
    private final Map<String, HudLayoutManager.Rect> rects = new HashMap<>();

    private String draggingId = null;
    private boolean resizing = false;
    private double dragDX = 0, dragDY = 0; // normalized offset within rect for move
    private static final int HANDLE = 12;

    public HudLayoutEditScreen(Runnable onSave) {
        super(Component.translatable("screen.dscombat.hud_layout_edit"));
        this.onSave = onSave;
    }

    @Override
    protected void init() {
        Minecraft mc = Minecraft.getInstance();
        scale = Config.CLIENT.modernHudScale.get().floatValue();
        sw = (int) (this.width / scale);
        sh = (int) (this.height / scale);

        // Resolve current vehicle type and default rects
        vehicle = null;
        if (mc.player != null && mc.player.getRootVehicle() instanceof EntityVehicle v) vehicle = v;
        if (vehicle == null) {
            onClose();
            return;
        }
        isHeli = vehicle instanceof EntityHelicopter;
        isPlane = vehicle instanceof EntityPlane;

        // Default positions roughly matching ModernHudOverlay defaults
        int defAttW = 180, defAttH = 110;
        int defAttX = sw/2 - defAttW/2, defAttY = sh/2 - defAttH/2 + 12;
        int defHrW = 220, defHrH = 18;
        int defHrX = sw/2 - defHrW/2, defHrY = defAttY - defHrH - 4;
        int defBrW = 190, defBrH = 96;
        int defBrX = sw - defBrW - 10, defBrY = sh - defBrH - 10;
        int defBlW = 220, defBlH = 110;
        int defBlX = 10, defBlY = sh - defBlH - 10;

        rects.put("att", HudLayoutManager.get(vt(), "att", new HudLayoutManager.Rect(defAttX/(double)sw, defAttY/(double)sh, defAttW/(double)sw, defAttH/(double)sh)));
        rects.put("head", HudLayoutManager.get(vt(), "head", new HudLayoutManager.Rect(defHrX/(double)sw, defHrY/(double)sh, defHrW/(double)sw, defHrH/(double)sh)));
        rects.put("br", HudLayoutManager.get(vt(), "br", new HudLayoutManager.Rect(defBrX/(double)sw, defBrY/(double)sh, defBrW/(double)sw, defBrH/(double)sh)));
        rects.put("bl", HudLayoutManager.get(vt(), "bl", new HudLayoutManager.Rect(defBlX/(double)sw, defBlY/(double)sh, defBlW/(double)sw, defBlH/(double)sh)));

        // Save & Close button (use constructor for cross-version compatibility)
        addRenderableWidget(new Button(this.width - 130, 10, 120, 20, Component.literal("Save & Close"), b -> closeAndSave(), Supplier::get));
    }

    private String vt() {
        if (vehicle instanceof EntityHelicopter) return "HELICOPTER";
        if (vehicle instanceof EntityPlane) return "PLANE";
        if (vehicle.getStats().isBoat()) return "BOAT";
        //if (vehicle.getStats().isCar()) return "CAR";
        return "GENERIC";
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        // Draw scaled HUD-space overlays
        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, 1f);

        // Title & instructions
        graphics.drawString(this.font, Component.literal("HUD Layout Edit"), 8, 8, Color.WHITE.getRGB());
        graphics.drawString(this.font, Component.literal("Drag to move, drag corner to resize. F6 or button to save."), 8, 20, 0xCCCCCC);

        // Draw rectangles
        drawRect(graphics, "att", 0x66FFFFFF, 0xAAFFFFFF);
        drawRect(graphics, "head", 0x66FFFFFF, 0xAAFFFFFF);
        drawRect(graphics, "br", 0x66FFFFFF, 0xAAFFFFFF);
        drawRect(graphics, "bl", 0x66FFFFFF, 0xAAFFFFFF);

        graphics.pose().popPose();
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void drawRect(GuiGraphics graphics, String id, int border, int handle) {
        HudLayoutManager.Rect r = rects.get(id);
        int x = (int) Math.round(r.x * sw), y = (int) Math.round(r.y * sh);
        int w = (int) Math.round(r.w * sw), h = (int) Math.round(r.h * sh);
        // border
        graphics.fill(x-1, y-1, x+w+1, y, border);
        graphics.fill(x-1, y+h, x+w+1, y+h+1, border);
        graphics.fill(x-1, y, x, y+h, border);
        graphics.fill(x+w, y, x+w+1, y+h, border);
        // label
        graphics.drawString(this.font, id, x+2, y-10, Color.WHITE.getRGB());
        // resize handle square
        graphics.fill(x+w-HANDLE/2, y+h-HANDLE/2, x+w+HANDLE/2, y+h+HANDLE/2, handle);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        double hx = mx / scale, hy = my / scale;
        for (String id : IDS) {
            HudLayoutManager.Rect r = rects.get(id);
            int x = (int) Math.round(r.x * sw), y = (int) Math.round(r.y * sh);
            int w = (int) Math.round(r.w * sw), h = (int) Math.round(r.h * sh);
            boolean inside = hx >= x && hx <= x + w && hy >= y && hy <= y + h;
            if (inside) {
                draggingId = id;
                boolean onHandle = hx >= x + w - HANDLE && hy >= y + h - HANDLE;
                resizing = onHandle;
                dragDX = (hx - x) / sw; dragDY = (hy - y) / sh;
                return true;
            }
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (draggingId != null) {
            double hx = mx / scale, hy = my / scale;
            HudLayoutManager.Rect r = rects.get(draggingId);
            if (resizing) {
                r.w = clamp01(hx / sw - r.x); r.h = clamp01(hy / sh - r.y);
            } else {
                r.x = clamp01(hx / sw - dragDX); r.y = clamp01(hy / sh - dragDY);
            }
            return true;
        }
        return super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        if (draggingId != null) {
            HudLayoutManager.set(vt(), draggingId, rects.get(draggingId));
            draggingId = null; resizing = false;
            return true;
        }
        return super.mouseReleased(mx, my, button);
    }

    private double clamp01(double v) { return v < 0 ? 0 : (v > 1 ? 1 : v); }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Close on F6 as well
        if (DSCKeys.hudLayoutEditKey != null && DSCCmp(keyCode, DSCKeys.hudLayoutEditKey)) {
            closeAndSave();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private boolean DSCCmp(int keyCode, net.minecraft.client.KeyMapping mapping) {
        return mapping.getDefaultKey().getValue() == keyCode; // simple compare
    }

    public void closeAndSave() {
        // Save current rects
        for (Map.Entry<String, HudLayoutManager.Rect> e : rects.entrySet()) {
            HudLayoutManager.set(vt(), e.getKey(), e.getValue());
        }
        HudLayoutManager.save();
        onSave.run();
        onClose();
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(null);
    }
}
