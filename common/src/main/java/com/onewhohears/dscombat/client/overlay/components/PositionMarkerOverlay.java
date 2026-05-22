package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.overlay.OverlayController;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.common.core.MarkerDisplayMode;
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
    public static final Style RED = Style.EMPTY.withColor(ChatFormatting.RED);
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
        //float z_rot = (float) UtilAngles.toDegrees(QuaternionF.from(cam.rotation())).roll;
        graphics.pose().pushPose();
        QuaternionF q = Vec3f.ZP.rotationDegrees(z_rot);
        q.mul(Vec3f.XP.rotationDegrees(cam.getXRot()));
        q.mul(Vec3f.YP.rotationDegrees(cam.getYRot()+180f));
        graphics.pose().mulPose(q.convert());
        /*Matrix4f mat4f = new Matrix4f();
        RenderSystem.getInverseViewRotationMatrix().get(mat4f);
        graphics.pose().mulPoseMatrix(mat4f);*/
        graphics.pose().translate(-view.x, -view.y, -view.z);
        Mat4f view_mat = Mat4f.from(graphics.pose().last().pose());
        graphics.pose().popPose();
        Mat4f proj_mat = OverlayController.PROJECTION_MATRIX;
        int size = 20;
        float min = 0.3f, max = 0.6f, max_dist = 4000;
        MarkerDisplayMode mode = Config.CLIENT.markerMode.get();
        if (mode != MarkerDisplayMode.ALWAYS_BIG) { min *= 0.5f; max *= 0.5f; }
        // RENDER EACH
        Set<Integer> visibleIds = PositionMarkerManager.getClientVisibleMarkerIds(m);
        for (int id : visibleIds) {
            PositionMarker marker = PositionMarkerManager.getClient().getMarker(id);
            if (marker == null) return;
            if (!marker.getDimension().equals(m.level.dimension())) return;
            float[] screen_pos = UtilGeometry.worldToScreenPos(marker.getPosition(),
                    view_mat, proj_mat, screenWidth, screenHeight);
            if (screen_pos[0] < 0 || screen_pos[1] < 0) continue;
            float x_win = screen_pos[0], y_win = screen_pos[1];
            double dist = cam.getPosition().distanceTo(marker.getPosition());
            int distance = (int) dist;
            float scale = (float) Math.max(min, max-(dist/max_dist*(max-min)));
            boolean hover = true;
            graphics.pose().pushPose();
            if (mode == MarkerDisplayMode.ALWAYS_BIG || (mode == MarkerDisplayMode.HOVER_BIG && hover)) {
                renderBig(m, graphics, marker, distance, size, x_win, y_win, scale);
            } else {
                renderSmall(m, graphics, marker, distance, size, x_win, y_win, scale);
            }
            graphics.pose().popPose();
        }
    }

    private void renderSmall(Minecraft m, GuiGraphics graphics, PositionMarker marker,
                             int distance, int size, float x_win, float y_win, float scale) {
        //float adj = size, x_pos = x_win-adj*0.5f, y_pos = y_win-adj*0.5f;
        float x_pos = x_win, y_pos = y_win;
        graphics.pose().translate(x_pos, y_pos, 0);
        graphics.pose().scale(scale, scale, scale);
        RenderSystem.enableBlend();
        graphics.setColor(1, 0, 0, 1);
        graphics.blit(POS_MARKER_SMALL, 0, 0, 0, 0, size, size, size, size);
        graphics.setColor(1, 1, 1, 1);
        /*graphics.pose().translate(adj*0.5f, adj, 0);
        graphics.drawCenteredString(m.font, UtilMCText.literal(marker.getName()).setStyle(RED), 0, 0, 0);
        graphics.pose().translate(0, 10, 0);
        graphics.drawCenteredString(m.font, UtilMCText.literal(distance+"").setStyle(RED), 0, 0, 0);*/
    }

    private void renderBig(Minecraft m, GuiGraphics graphics, PositionMarker marker,
                           int distance, int size, float x_win, float y_win, float scale) {
        float adj = size, x_pos = x_win-adj*0.5f, y_pos = y_win-adj;
        graphics.pose().translate(x_pos, y_pos, 0);
        graphics.pose().scale(scale, scale, scale);
        RenderSystem.enableBlend();
        graphics.setColor(1, 0, 0, 1);
        graphics.blit(POS_MARKER, 0, 0, 0, 0, size, size, size, size);
        graphics.setColor(1, 1, 1, 1);
        graphics.pose().translate(adj*0.5f, adj, 0);
        graphics.drawCenteredString(m.font, UtilMCText.literal(marker.getName()).setStyle(RED), 0, 0, 0);
        graphics.pose().translate(0, 10, 0);
        graphics.drawCenteredString(m.font, UtilMCText.literal(distance+"").setStyle(RED), 0, 0, 0);
    }

    @Override
    protected @NotNull String componentId() {
        return "dscombat_position_markers";
    }
}
