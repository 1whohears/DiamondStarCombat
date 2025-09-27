package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.overlay.OverlayController;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.math.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AimAssistOverlay extends VehicleOverlayComponent {
	public static final ResourceLocation AIM_HUD = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/aim_hud.png");
	public static final int AIM_SIZE = 10;
	protected static float PARTIAL_TICK;

    private Vec3 targetWorldPos = null;
    private int prevTick = 0;

    @Override
    protected boolean shouldRender(Gui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        if (!(getPlayerRootVehicle() instanceof EntityVehicle vehicle)) return false;

        RadarSystem radar = vehicle.radarSystem;
        if (!radar.hasRadar()) return false;

        WeaponInstance<?> data = vehicle.weaponSystem.getSelected();
        if (data == null) return false;

        return data.getStats().isAimAssist();
    }

    @Override
    protected void render(Gui gui, PoseStack stack, float partialTick, int screenWidth, int screenHeight) {
        if (!(getPlayerRootVehicle() instanceof EntityVehicle vehicle)) return;

        WeaponInstance<?> data = vehicle.weaponSystem.getSelected();

        if (vehicle.tickCount != prevTick /*&& vehicle.tickCount % 2 == 0*/) {
            assert data != null;
            targetWorldPos = calcTargetWorldPos(vehicle, data);
        }
        prevTick = vehicle.tickCount;
        if (targetWorldPos == null) return;
        Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 view = cam.getPosition();
        float z_rot = UtilAngles.lerpAngle(PARTIAL_TICK, vehicle.zRotO, vehicle.zRot);
        stack.pushPose();
        QuaternionF q = Vec3f.ZP.rotationDegrees(z_rot);
        q.mul(Vec3f.XP.rotationDegrees(cam.getXRot()));
        q.mul(Vec3f.YP.rotationDegrees(cam.getYRot()+180f));
        stack.mulPose(q.convert());
        stack.translate(-view.x, -view.y, -view.z);
        Mat4f view_mat = Mat4f.from(stack.last().pose().copy());
        stack.popPose();
        Mat4f proj_mat = OverlayController.PROJECTION_MATRIX;
        float[] screen_pos = UtilGeometry.worldToScreenPos(targetWorldPos,
                view_mat, proj_mat, screenWidth, screenHeight);
        if (screen_pos[0] < 0 || screen_pos[1] < 0) return;
        float x_win = screen_pos[0], y_win = screen_pos[1];
        float adj = AIM_SIZE * 0.5f, x_pos = x_win - adj, y_pos = y_win - adj;
        drawCrossHair(stack, x_pos, y_pos);
    }

    @Override
    protected @NotNull String componentId() {
        return "dscombat_aim_assist";
    }
	
	protected void drawCrossHair(PoseStack poseStack, float x_pos, float y_pos) {
		poseStack.pushPose();
        poseStack.translate(x_pos, y_pos, 0);
        RenderSystem.setShaderTexture(0, AIM_HUD);
        blit(poseStack,
                0, 0, 0, 0,
                AIM_SIZE, AIM_SIZE,
                AIM_SIZE, AIM_SIZE);
        poseStack.popPose();
	}
	
	@Nullable
	protected Vec3 calcTargetWorldPos(EntityVehicle vehicle, WeaponInstance<?> data) {
		return data.estimateImpactPosition(vehicle);
	}

}
