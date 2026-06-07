package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.DependencySafety;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.overlay.OverlayController;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.data.weapon.stats.TargetMode;
import com.onewhohears.dscombat.entity.parts.EntityRidablePart;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.math.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpticalTargetOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation OPTICAL_TARGET_INDICATOR = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/optical_target_indicator.png");

    @Override
    protected boolean shouldRender(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        return DSCClientInputs.getTargetMode() == TargetMode.OPTICAL;
    }

    @Override
    protected void render(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft m = Minecraft.getInstance();
        if (m.level == null) return;

        int targetId = DSCClientInputs.getOpticalTrackedEntityId();
        if (targetId == -1) return;
        Vec3 targetPos = null;
        Entity targetEntity = m.level.getEntity(targetId);
        if (targetEntity != null) targetPos = targetEntity.position();
        else targetPos = DependencySafety.getClientDistantEntityPos(targetId);
        if (targetPos == null) return;

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
        int size = 10;

        // RENDER
        float[] screen_pos = UtilGeometry.worldToScreenPos(targetPos,
                view_mat, proj_mat, screenWidth, screenHeight);
        if (screen_pos[0] < 0 || screen_pos[1] < 0) return;
        float x_win = screen_pos[0], y_win = screen_pos[1];
        float adj = size*0.5f, x_pos = x_win-adj, y_pos = y_win-adj;

        graphics.pose().pushPose();
        graphics.pose().translate(x_pos, y_pos, 0);
        RenderSystem.enableBlend();
        graphics.blit(OPTICAL_TARGET_INDICATOR, 0, 0, 0, 0, size, size, size, size);
        graphics.pose().popPose();
    }

    @Override
    protected @NotNull String componentId() {
        return "dscombat_optical_target";
    }
}
