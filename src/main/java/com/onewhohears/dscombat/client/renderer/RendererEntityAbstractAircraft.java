package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.renderer.model.EntityModelTestPlane;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RendererEntityAbstractAircraft<T extends EntityAbstractAircraft> extends EntityRenderer<T> {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(DSCombatMod.MODID, "textures/entities/basic_plane.png");
	
	protected final EntityModel<T> model;
	
	public RendererEntityAbstractAircraft(Context context) {
		//super(context, new EntityModelBasicPlane<>(context.bakeLayer(EntityModelBasicPlane.LAYER_LOCATION)), 0.5f);
		super(context);
		this.shadowRadius = 0.8f;
		model = new EntityModelTestPlane<>(context.bakeLayer(EntityModelTestPlane.LAYER_LOCATION));
	}
	
	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
		poseStack.pushPose();
		Quaternion q = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());
        poseStack.mulPose(q);
        poseStack.translate(0, 1.55, 0);
        poseStack.scale(1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
		
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
		model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		
		poseStack.pushPose();
        poseStack.popPose();
        poseStack.popPose();
		
        super.render(entity, entityYaw, partialTicks, poseStack, multiBufferSource, packedLight);
	}
	
	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return TEXTURE;
	}
	
}
