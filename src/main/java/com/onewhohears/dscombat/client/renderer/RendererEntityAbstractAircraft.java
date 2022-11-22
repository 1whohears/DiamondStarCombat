package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.client.renderer.model.EntityAircraftModel;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RendererEntityAbstractAircraft<T extends EntityAbstractAircraft> extends EntityRenderer<T> {
	
	protected final EntityAircraftModel<T> model;
	
	public RendererEntityAbstractAircraft(Context context, EntityAircraftModel<T> model) {
		super(context);
		this.shadowRadius = 0.8f;
		this.model = model;
	}
	
	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
		//System.out.println("RENDER "+entity);
		Quaternion q = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());
		poseStack.pushPose();
        poseStack.mulPose(q);
		
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
		model.renderToBuffer(entity, partialTicks, poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		
        poseStack.popPose();
		
        super.render(entity, entityYaw, partialTicks, poseStack, multiBufferSource, packedLight);
	}
	
	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return entity.getTexture();
	}
	
}
