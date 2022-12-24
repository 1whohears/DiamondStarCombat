package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.renderer.model.EntityControllableModel;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilAngles.EulerAngles;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class RendererEntityTurret<T extends EntityTurret> extends EntityRenderer<T> {
	
	protected final EntityControllableModel<T> model;
	protected final ResourceLocation texture;
	
	public RendererEntityTurret(Context ctx, EntityControllableModel<T> model, ResourceLocation texture) {
		super(ctx);
		this.model = model;
		this.texture = texture;
	}

	@Override
	public ResourceLocation getTextureLocation(T pEntity) {
		return texture;
	}
	
	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
		if (!entity.shouldRender()) return;
		poseStack.pushPose();
		if (entity.getVehicle() instanceof EntityAircraft plane) {
			Quaternion q = UtilAngles.lerpQ(partialTicks, plane.getPrevQ(), plane.getClientQ());
			poseStack.mulPose(q);
			Player player = entity.getPlayer();
			if (player != null) {
				EulerAngles a = UtilAngles.toDegrees(q);
				float y = (float)a.yaw-player.getYRot();
				float x = player.getXRot()-(float)a.pitch;
				entity.setYRot(y);
				entity.setXRot(x);
				poseStack.mulPose(Vector3f.YP.rotationDegrees(y));
				poseStack.mulPose(Vector3f.XP.rotationDegrees(x));
			}
		}
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(entity.getZRot()));
		VertexConsumer vertexconsumer = multiBufferSource.getBuffer(model.renderType(getTextureLocation(entity)));
		model.renderToBuffer(entity, partialTicks, poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
		super.render(entity, entityYaw, partialTicks, poseStack, multiBufferSource, packedLight);
	}

}
