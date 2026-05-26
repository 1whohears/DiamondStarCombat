package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.overlay.OverlayController;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.common.core.MarkerDisplayMode;
import com.onewhohears.dscombat.common.core.MarkerType;
import com.onewhohears.dscombat.common.core.PositionMarker;
import com.onewhohears.dscombat.common.core.PositionMarkerManager;
import com.onewhohears.dscombat.entity.parts.EntityRidablePart;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.math.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class PositionMarkerOverlay extends VehicleOverlayComponent {
    public static final Style WHITE = Style.EMPTY.withColor(ChatFormatting.WHITE);
    public static final ResourceLocation POS_MARKER = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/pos_marker.png");
    public static final ResourceLocation POS_MARKER_SMALL = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/pos_marker_small.png");

    @Override
    protected boolean shouldRender(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        return !PositionMarkerManager.getClientVisibleMarkerIds(Minecraft.getInstance()).isEmpty();
    }

    @Override
    protected void render(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft m = Minecraft.getInstance();
        if (m.level == null) return;

        @Nullable EntityRidablePart seat = null;
        if (getPlayerVehicle() instanceof EntityRidablePart s) seat = s;
        @Nullable EntityVehicle vehicle = null;
        if (seat != null) vehicle = seat.getParentVehicle();

        // CALC PRE RENDER MATH
        Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 view = cam.getPosition();
        float z_rot = 0;
        if (vehicle != null) z_rot = UtilAngles.lerpAngle(partialTick, vehicle.zRotO, vehicle.zRot);
        graphics.pose().pushPose();
        QuaternionF q = Vec3f.ZP.rotationDegrees(z_rot);
        q.mul(Vec3f.XP.rotationDegrees(cam.getXRot()));
        q.mul(Vec3f.YP.rotationDegrees(cam.getYRot()+180f));
        graphics.pose().mulPose(q.convert());
        graphics.pose().translate(-view.x, -view.y, -view.z);
        Mat4f view_mat = Mat4f.from(graphics.pose().last().pose());
        graphics.pose().popPose();
        Mat4f proj_mat = OverlayController.PROJECTION_MATRIX;
        int size = 20;
        float min = 0.1f, max = 0.4f, max_dist = 4000;
        MarkerDisplayMode mode = Config.CLIENT.markerMode.get();
        int sw2 = screenWidth / 2, sh2 = screenHeight / 2;
        int hoverId = -1;
        // RENDER EACH
        Set<Integer> visibleIds = PositionMarkerManager.getClientVisibleMarkerIds(m);
        for (int id : visibleIds) {
            PositionMarker marker = PositionMarkerManager.getClient().getMarker(id);
            if (marker == null) continue;
            if (!marker.getDimension().equals(m.level.dimension())) continue;
            float[] screen_pos = UtilGeometry.worldToScreenPos(marker.getPosition(),
                    view_mat, proj_mat, screenWidth, screenHeight);
            if (screen_pos[0] < 0 || screen_pos[1] < 0) continue;
            float x_win = screen_pos[0], y_win = screen_pos[1];
            double dist = cam.getPosition().distanceTo(marker.getPosition());
            int distance = (int) dist;
            float scale = (float) Math.max(min, max-(dist/max_dist*(max-min)));
            int scaledSize = (int) (size * scale);
            boolean hover = hoverId == -1 && x_win < sw2 + scaledSize && x_win > sw2 - scaledSize
                    && y_win < sh2 + scaledSize && y_win > sh2 - scaledSize;
            if (hover) {
                scale *= 2;
                hoverId = id;
            }
            boolean selected = id == DSCClientInputs.getSelectedMarkerId();
            if (mode == MarkerDisplayMode.ALWAYS_BIG || (mode == MarkerDisplayMode.SELECT_BIG && (hover || selected))) {
                renderBig(m, graphics, marker, distance, size, x_win, y_win, scale, hover, selected);
            } else {
                renderSmall(m, graphics, marker, distance, size, x_win, y_win, scale, hover, selected);
            }
        }
        DSCClientInputs.setMarkerHoverId(hoverId);
    }

    private void renderSmall(Minecraft m, GuiGraphics graphics, PositionMarker marker,
                             int distance, int size, float x_win, float y_win, float scale,
                             boolean hover, boolean selected) {
        graphics.pose().pushPose();
        float adj = size, x_pos = x_win-adj*0.5f*scale, y_pos = y_win-adj*0.5f*scale;
        graphics.pose().translate(x_pos, y_pos, 0);
        graphics.pose().scale(scale, scale, scale);
        RenderSystem.enableBlend();
        float r = 0, g = 0, b = 0;
        if (selected) {
            r = 1;
        } else if (hover) {
            r = 1; g = 1;
        } else if (marker.getType() == MarkerType.TEMP) {
            r = 1; b = 1;
        } else {
            b = 1;
        }
        graphics.setColor(r, g, b, 1);
        graphics.blit(POS_MARKER_SMALL, 0, 0, 0, 0, size, size, size, size);
        graphics.setColor(1, 1, 1, 1);
        graphics.pose().popPose();
    }

    private void renderBig(Minecraft m, GuiGraphics graphics, PositionMarker marker,
                           int distance, int size, float x_win, float y_win, float scale,
                           boolean hover, boolean selected) {
        float adj = size, x_pos = x_win-adj*0.5f*scale, y_pos = y_win-adj*scale;
        graphics.pose().pushPose();
        graphics.pose().translate(x_pos, y_pos, 0);
        graphics.pose().scale(scale, scale, scale);
        RenderSystem.enableBlend();
        float r = 0, g = 0, b = 0;
        if (selected) {
            r = 1;
        } else if (hover) {
            r = 1; g = 1;
        } else if (marker.getType() == MarkerType.TEMP) {
            r = 1; b = 1;
        }else {
            g = 1;
        }
        graphics.setColor(r, g, b, 1);
        graphics.blit(POS_MARKER, 0, 0, 0, 0, size, size, size, size);
        graphics.pose().translate(adj*0.5f, adj, 0);
        graphics.setColor(r, g, b, 0.5f);
        graphics.drawCenteredString(m.font, UtilMCText.literal(marker.getName()).setStyle(WHITE), 0, 0, 0);
        graphics.pose().translate(0, 10, 0);
        graphics.drawCenteredString(m.font, UtilMCText.literal(distance+"").setStyle(WHITE), 0, 0, 0);
        graphics.setColor(1, 1, 1, 1);
        graphics.pose().popPose();
    }

    @Override
    protected @NotNull String componentId() {
        return "dscombat_position_markers";
    }
}
