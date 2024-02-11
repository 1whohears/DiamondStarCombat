package com.onewhohears.dscombat.client.overlay.components;

import java.util.Objects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.event.forgebus.ClientRenderEvents;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.data.aircraft.DSCPhysicsConstants;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.weapon.BulletData;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.UtilEntity;
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
	
	@Override
	protected void render(PoseStack poseStack, int screenWidth, int screenHeight) {
		if (!(getPlayerRootVehicle() instanceof EntityVehicle vehicle)) return;
		RadarSystem radar = vehicle.radarSystem;
		if (!radar.hasRadar()) return;
		WeaponData data = vehicle.weaponSystem.getSelected();
		if (data == null || !data.getType().isAimAssist()) return;
		Vec3 targetWorldPos = null;
		Vec3 startPos = vehicle.position().add(UtilAngles.rotateVector(data.getLaunchPos(), vehicle.getQ()));
		Vec3 startMove = vehicle.getDeltaMovement();
		Vec3 gravity = new Vec3(0, -DSCPhysicsConstants.GRAVITY, 0);
		// FIXME 2.1 bullet/bomb air to ground aim assist only works well on flat terrain
		// FIXME 2.2 should only recalculate the math once per tick/2 ticks
		// FIXME 2.3 aim assist against air targets on radar
		double dist = UtilEntity.getDistFromGround(vehicle);
		if (data.getType().isBomb()) {
			double[] tickRoots = UtilGeometry.rootsNoI(0.5*gravity.y, startMove.y, dist);
			if (tickRoots == null) return;
			double ticks = Math.max(tickRoots[0], tickRoots[1]);
			targetWorldPos = startPos.add(startMove.scale(ticks)).add(gravity.scale(0.5*ticks*ticks));
		} else if (data.getType().isBullet()) {
			startMove = vehicle.getLookAngle().scale(((BulletData)data).getSpeed());
			double[] tickRoots = UtilGeometry.rootsNoI(0.5*gravity.y, startMove.y, dist);
			if (tickRoots == null) return;
			double ticks = Math.max(tickRoots[0], tickRoots[1]);
			targetWorldPos = startPos.add(startMove.scale(ticks)).add(gravity.scale(0.5*ticks*ticks));
		}
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
        poseStack.pushPose();
        poseStack.translate(x_pos, y_pos, 0);
        RenderSystem.setShaderTexture(0, AIM_HUD);
        blit(poseStack,
                0, 0, 0, 0,
                AIM_SIZE, AIM_SIZE,
                AIM_SIZE, AIM_SIZE);
        poseStack.popPose();
	}

}
