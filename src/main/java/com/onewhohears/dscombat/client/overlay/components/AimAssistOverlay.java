package com.onewhohears.dscombat.client.overlay.components;

import java.util.Objects;

import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.event.forgebus.ClientRenderEvents;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class AimAssistOverlay extends VehicleOverlayComponent {
	
	public static final ResourceLocation AIM_HUD = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/aim_hud.png");
	
	public static final int AIM_SIZE = 10;
	
	protected static float PARTIAL_TICK;
    private static AimAssistOverlay INSTANCE;

    public static void renderIfAllowed(PoseStack poseStack, int screenWidth, int screenHeight, float partialTick) {
        if (Objects.isNull(INSTANCE)) INSTANCE = new AimAssistOverlay();
        PARTIAL_TICK = partialTick;
        INSTANCE.render(poseStack, screenWidth, screenHeight);
    }

    private AimAssistOverlay() {}
    
    private Vec3 targetWorldPos = null;
    private int prevTick = 0;
	
	@Override
	protected void render(PoseStack poseStack, int screenWidth, int screenHeight) {
		if (!(getPlayerRootVehicle() instanceof EntityVehicle vehicle)) return;
		RadarSystem radar = vehicle.radarSystem;
		if (!radar.hasRadar()) return;
		WeaponData data = vehicle.weaponSystem.getSelected();
		if (data == null) return;
		if (!data.getType().isAimAssist()) return;
		if (vehicle.tickCount != prevTick && vehicle.tickCount % 2 == 0) 
			targetWorldPos = calcTargetWorldPos(vehicle, data);
		prevTick = vehicle.tickCount;
		if (targetWorldPos == null) return;
		Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 view = cam.getPosition();
        float z_rot = UtilAngles.lerpAngle(PARTIAL_TICK, vehicle.zRotO, vehicle.zRot);
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(z_rot));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(cam.getXRot()));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(cam.getYRot()+180f));
        poseStack.translate(-view.x, -view.y, -view.z);
        Matrix4f view_mat = poseStack.last().pose().copy();
        poseStack.popPose();
        Matrix4f proj_mat = ClientRenderEvents.getProjMatrix();
        float[] screen_pos = UtilGeometry.worldToScreenPos(targetWorldPos,
                view_mat, proj_mat, screenWidth, screenHeight);
        if (screen_pos[0] < 0 || screen_pos[1] < 0) return;
        float x_win = screen_pos[0], y_win = screen_pos[1];
        float adj = AIM_SIZE * 0.5f, x_pos = x_win - adj, y_pos = y_win - adj;
        drawCrossHair(poseStack, x_pos, y_pos);
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
	protected Vec3 calcTargetWorldPos(EntityVehicle vehicle, WeaponData data) {
		return data.estimateImpactPosition(vehicle);
	}

}
