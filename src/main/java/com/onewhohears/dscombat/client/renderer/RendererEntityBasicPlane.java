package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.renderer.model.EntityModelBasicPlane;
import com.onewhohears.dscombat.entity.EntityBasicPlane;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RendererEntityBasicPlane<T extends EntityBasicPlane> extends EntityRenderer<T> {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(DSCombatMod.MODID, "textures/entities/basic_plane.png");
	
	protected final EntityModel<T> model;
	
	public RendererEntityBasicPlane(Context context) {
		//super(context, new EntityModelBasicPlane<>(context.bakeLayer(EntityModelBasicPlane.LAYER_LOCATION)), 0.5f);
		super(context);
		this.shadowRadius = 0.8f;
		model = new EntityModelBasicPlane<>(context.bakeLayer(EntityModelBasicPlane.LAYER_LOCATION));
	}
	
	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
		poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
        //poseStack.mulPose(Vector3f.YP.rotationDegrees(-entityYaw));
        //poseStack.mulPose(Vector3f.XP.rotationDegrees(180f-entity.getXRot()));
        //poseStack.mulPose(Vector3f.ZP.rotationDegrees(entity.rotRoll));
		Quaternion q = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());
        poseStack.mulPose(q);
        
        double radius = 1.55;
        poseStack.translate(0, -radius, 0);
		
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
