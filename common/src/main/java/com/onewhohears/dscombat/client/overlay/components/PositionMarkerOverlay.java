package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.overlay.OverlayController;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.common.core.PositionMarker;
import com.onewhohears.dscombat.common.core.PositionMarkerManager;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.radar.RadarTarget;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.parts.EntityRidablePart;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.UtilVehicleEntity;
import com.onewhohears.onewholibs.util.math.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public class PositionMarkerOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation PING_HUD = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/ping_hud.png");
    public static final ResourceLocation PING_DATA = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/ping_data.png");
    public static final ResourceLocation PING_ICONS = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/ping_data_icons_color.png");
    public static final int ICON_SIZE = 16, ICON_WIDTH = 240, DEFAULT_SIZE = 100;
    protected static final int[] HUD_PING_ANIM = new int[] {0,1,2,3,2,1};
    protected static float PARTIAL_TICK;

    @Override
    protected boolean shouldRender(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        return !PositionMarkerManager.getClientVisibleMarkerIds(Minecraft.getInstance()).isEmpty();
    }

    @Override
    protected void render(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft m = Minecraft.getInstance();
        if (m.level == null) return;
        // CALC PRE RENDER MATH
        Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 view = cam.getPosition();
        //float z_rot = UtilAngles.lerpAngle(PARTIAL_TICK, vehicle.zRotO, vehicle.zRot);
        float z_rot = 0; // TODO determine z_rot;
        graphics.pose().pushPose();
        QuaternionF q = Vec3f.ZP.rotationDegrees(z_rot);
        q.mul(Vec3f.XP.rotationDegrees(cam.getXRot()));
        q.mul(Vec3f.YP.rotationDegrees(cam.getYRot()+180f));
        graphics.pose().mulPose(q.convert());
        graphics.pose().translate(-view.x, -view.y, -view.z);
        Mat4f view_mat = Mat4f.from(graphics.pose().last().pose());
        graphics.pose().popPose();
        Mat4f proj_mat = OverlayController.PROJECTION_MATRIX;
        int size = 25;
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
            float scale = 1;
            float adj = size*scale/2f, x_pos = x_win-adj, y_pos = y_win-adj;
            graphics.pose().pushPose();
            graphics.pose().translate(x_pos, y_pos, 0);
            graphics.pose().scale(scale, scale, scale);
            graphics.blit(PING_HUD,
                    0, 0, 0, 0,
                    size, size, size, size * 5);
            graphics.pose().popPose();
        }
    }

    @Override
    protected @NotNull String componentId() {
        return "dscombat_radar";
    }
}
