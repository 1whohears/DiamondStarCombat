package com.onewhohears.dscombat.client.screen;

import com.onewhohears.dscombat.common.network.toserver.ToServerVehicleDecal;
import com.onewhohears.dscombat.data.vehicle.VehicleDecalManager;
import com.onewhohears.dscombat.data.vehicle.VehicleDecalManager.DecalData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DecalScreen extends Screen {

    private static final int PANEL_W   = 360;
    private static final int PANEL_H   = 230;
    private static final int LIST_W    = 130;
    private static final int ROW_H     = 18;
    private static final int PAD       = 6;
    private static final int LIST_ROWS = 8;
    private static final int DEFAULT_COLOR = 0xFFFFFFFF;

    private final int vehicleId;
    private final VehicleDecalManager decalManager;

    // Form fields
    private EditBox textBox;
    private EditBox xBox, yBox, zBox;
    private EditBox rxBox, ryBox, rzBox;
    private EditBox scaleBox, colorBox;

    @Nullable private String editingId = null;

    private int listScroll = 0;
    private int px, py;
    private int listRowH, listStartY;

    public DecalScreen(int vehicleId, VehicleDecalManager decalManager) {
        super(Component.translatable("screen.dscombat.decal_screen"));
        this.vehicleId = vehicleId;
        this.decalManager = decalManager;
    }

    @Override
    protected void init() {
        px = width / 2 - PANEL_W / 2;
        py = height - 98 - PANEL_H;
        buildWidgets();
    }

    private void buildWidgets() {
        clearWidgets();

        int fx = px + LIST_W + PAD * 2;
        int fw = PANEL_W - LIST_W - PAD * 3;
        int fy = py + PAD;

        // Text
        textBox = addBox(fx, fy, fw, "Text...");
        fy += ROW_H + 3;

        // Position
        fy = numRow(fx, fw, fy, "X", xBox != null ? xBox.getValue() : "0");
        xBox = lastBox;
        fy = numRow(fx, fw, fy, "Y", yBox != null ? yBox.getValue() : "0");
        yBox = lastBox;
        fy = numRow(fx, fw, fy, "Z", zBox != null ? zBox.getValue() : "0");
        zBox = lastBox;
        fy += 2;

        // Rotation
        fy = numRow(fx, fw, fy, "Rot X", rxBox != null ? rxBox.getValue() : "0");
        rxBox = lastBox;
        fy = numRow(fx, fw, fy, "Rot Y", ryBox != null ? ryBox.getValue() : "0");
        ryBox = lastBox;
        fy = numRow(fx, fw, fy, "Rot Z", rzBox != null ? rzBox.getValue() : "0");
        rzBox = lastBox;
        fy += 2;

        // Scale
        fy = numRow(fx, fw, fy, "Scale", scaleBox != null ? scaleBox.getValue() : "1");
        scaleBox = lastBox;

        // Color
        colorBox = addBox(fx, fy, fw, "RRGGBB");
        if (colorBox.getValue().isEmpty()) colorBox.setValue("FFFFFF");
        fy += ROW_H + 3;

        // Action buttons
        addRenderableWidget(Button.builder(
                editingId == null ? Component.literal("+ Add") : Component.literal("✔ Save"),
                btn -> onAction())
                .bounds(fx, fy, editingId == null ? fw : fw / 2 - 1, ROW_H).build());
        if (editingId != null) {
            addRenderableWidget(Button.builder(Component.literal("✖ Cancel"), btn -> cancelEdit())
                    .bounds(fx + fw / 2 + 1, fy, fw / 2 - 1, ROW_H).build());
        }

        // Scroll arrows
        int lx = px + PAD;
        addRenderableWidget(Button.builder(Component.literal("▲"), btn -> scroll(-1))
                .bounds(lx + LIST_W - 14, py + PAD + 10, 12, 12).build());
        addRenderableWidget(Button.builder(Component.literal("▼"), btn -> scroll(1))
                .bounds(lx + LIST_W - 14, py + PANEL_H - 16, 12, 12).build());

        // List buttons
        int headerH = 12;
        int availH = PANEL_H - PAD * 2 - headerH - 28;
        listRowH = availH / LIST_ROWS;
        listStartY = py + PAD + headerH;

        java.util.List<DecalData> decals = decalManager.getDecals();
        for (int i = listScroll; i < Math.min(decals.size(), listScroll + LIST_ROWS); i++) {
            final DecalData d = decals.get(i);
            int ry = listStartY + (i - listScroll) * listRowH;
            // Row select button (transparent — we draw text manually on top)
            addRenderableWidget(Button.builder(Component.empty(), btn -> loadDecalIntoForm(d))
                    .bounds(lx, ry, LIST_W - 18, listRowH - 1).build());
            // Delete button
            addRenderableWidget(Button.builder(Component.literal("X"), btn -> deleteDecal(d.id))
                    .bounds(lx + LIST_W - 18, ry, 16, listRowH - 1).build());
        }
    }

    // Helper to create an EditBox and track it
    private EditBox lastBox;
    private EditBox addBox(int x, int y, int w, String hint) {
        EditBox box = new EditBox(font, x, y, w, ROW_H, Component.empty());
        box.setHint(Component.literal(hint));
        addRenderableWidget(box);
        lastBox = box;
        return box;
    }

    /** Creates label + editbox row, returns next Y */
    private int numRow(int fx, int fw, int fy, String label, String currentVal) {
        int lw = font.width(label) + 4;
        int bw = fw - lw - 2;
        EditBox box = new EditBox(font, fx + lw + 2, fy, bw, ROW_H, Component.empty());
        box.setValue(currentVal);
        addRenderableWidget(box);
        lastBox = box;
        return fy + ROW_H + 2;
    }

    private void onAction() {
        String text = textBox.getValue().trim();
        if (text.isEmpty()) return;
        float x  = parseFloat(xBox.getValue(), 0);
        float y  = parseFloat(yBox.getValue(), 0);
        float z  = parseFloat(zBox.getValue(), 0);
        float rx = parseFloat(rxBox.getValue(), 0);
        float ry = parseFloat(ryBox.getValue(), 0);
        float rz = parseFloat(rzBox.getValue(), 0);
        float sc = Math.max(0.1f, parseFloat(scaleBox.getValue(), 1));
        int color = parseColor(colorBox.getValue());

        if (editingId == null) {
            DecalData d = new DecalData(text, x, y, z, rx, ry, rz, sc, color);
            decalManager.addDecal(d);
            new ToServerVehicleDecal(vehicleId, d).sendToServer();
            resetForm();
        } else {
            DecalData d = new DecalData(editingId, text, x, y, z, rx, ry, rz, sc, color);
            decalManager.updateDecal(d);
            new ToServerVehicleDecal(vehicleId, d, true).sendToServer();
            cancelEdit();
        }
    }

    private void deleteDecal(String id) {
        decalManager.removeDecal(id);
        new ToServerVehicleDecal(vehicleId, id).sendToServer();
        if (id.equals(editingId)) editingId = null;
        buildWidgets();
    }

    private void loadDecalIntoForm(DecalData d) {
        editingId = d.id;
        buildWidgets();
        textBox.setValue(d.text);
        xBox.setValue(fmt(d.x)); yBox.setValue(fmt(d.y)); zBox.setValue(fmt(d.z));
        rxBox.setValue(fmt(d.rotX)); ryBox.setValue(fmt(d.rotY)); rzBox.setValue(fmt(d.rotZ));
        scaleBox.setValue(fmt(d.scale));
        colorBox.setValue(String.format("%06X", d.color & 0xFFFFFF));
    }

    private void cancelEdit() {
        editingId = null;
        resetForm();
    }

    private void resetForm() {
        editingId = null;
        buildWidgets();
        textBox.setValue("");
        xBox.setValue("0"); yBox.setValue("0"); zBox.setValue("0");
        rxBox.setValue("0"); ryBox.setValue("0"); rzBox.setValue("0");
        scaleBox.setValue("1");
        colorBox.setValue("FFFFFF");
    }

    private void scroll(int dir) {
        int max = Math.max(0, decalManager.getDecals().size() - LIST_ROWS);
        listScroll = Math.max(0, Math.min(max, listScroll + dir));
        buildWidgets();
    }

    private String fmt(float v) {
        return Math.abs(v % 1) < 0.001f ? String.format("%.0f", v) : String.format("%.2f", v);
    }

    private float parseFloat(String s, float def) {
        try { return Float.parseFloat(s.trim()); } catch (NumberFormatException e) { return def; }
    }

    private int parseColor(String s) {
        try { return (int)(Long.parseLong(s.trim().replace("#",""), 16) | 0xFF000000L); }
        catch (NumberFormatException e) { return DEFAULT_COLOR; }
    }

    // -------------------------------------------------------------------------

    @Override
    public void render(@NotNull GuiGraphics g, int mx, int my, float pt) {
        // Panel
        g.fill(px - 2, py - 2, px + PANEL_W + 2, py + PANEL_H + 2, 0xFF222222);
        g.fill(px, py, px + PANEL_W, py + PANEL_H, 0xFF2D2D2D);
        g.fill(px + LIST_W + PAD, py + 2, px + LIST_W + PAD + 1, py + PANEL_H - 2, 0xFF555555);
        g.drawString(font, title, px + PAD, py - 10, 0xFFFFFF, false);
        g.drawString(font, "Decals (" + decalManager.getDecals().size() + "/" + VehicleDecalManager.MAX_DECALS + ")",
                px + PAD, py + PAD, 0xAAAAAA, false);

        // Draw widgets (buttons, editboxes)
        super.render(g, mx, my, pt);

        // Draw list text ON TOP of buttons (after super.render so it's visible)
        renderListText(g);

        // Form labels
        renderFormLabels(g);

        // Preview
        renderPreview(g);
    }

    private void renderListText(GuiGraphics g) {
        java.util.List<DecalData> decals = decalManager.getDecals();
        int lx = px + PAD;
        for (int i = listScroll; i < Math.min(decals.size(), listScroll + LIST_ROWS); i++) {
            DecalData d = decals.get(i);
            int ry = listStartY + (i - listScroll) * listRowH;
            // Highlight editing row
            if (d.id.equals(editingId)) {
                g.fill(lx, ry, lx + LIST_W - 18, ry + listRowH - 1, 0x883A5A3A);
            }
            String label = font.plainSubstrByWidth(d.text, LIST_W - 24);
            g.drawString(font, label, lx + 3, ry + (listRowH - 8) / 2, d.color, false);
        }
    }

    private void renderFormLabels(GuiGraphics g) {
        int fx = px + LIST_W + PAD * 2;
        int fw = PANEL_W - LIST_W - PAD * 3;
        int fy = py + PAD + ROW_H + 3;

        String[] labels = {"X", "Y", "Z", "Rot X", "Rot Y", "Rot Z", "Scale"};
        for (int i = 0; i < labels.length; i++) {
            g.drawString(font, labels[i], fx, fy + 5, 0xAAAAAA, false);
            fy += ROW_H + 2;
            if (i == 2 || i == 5) fy += 2;
        }
        fy += 2;
        g.drawString(font, "Color", fx, fy + 5, 0xAAAAAA, false);
    }

    private void renderPreview(GuiGraphics g) {
        if (textBox == null || textBox.getValue().isEmpty()) return;
        int color = parseColor(colorBox.getValue());
        float s = Math.min(parseFloat(scaleBox.getValue(), 1f) * 0.5f, 1.5f);
        int cx = px + LIST_W + PAD * 2 + (PANEL_W - LIST_W - PAD * 3) / 2;
        int cy = py + PANEL_H - 10;
        g.pose().pushPose();
        g.pose().translate(cx, cy, 200);
        g.pose().scale(s, s, 1f);
        int tw = font.width(textBox.getValue());
        g.drawString(font, textBox.getValue(), -tw / 2, 0, color, false);
        g.pose().popPose();
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double delta) {
        scroll(delta < 0 ? 1 : -1);
        return true;
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
