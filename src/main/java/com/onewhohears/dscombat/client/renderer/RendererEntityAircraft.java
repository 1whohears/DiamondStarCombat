package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.client.model.EntityControllableModel;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RendererEntityAircraft<T extends EntityVehicle> extends EntityRenderer<T> implements RotableHitboxRenderer, OverlayOnVehicleRenderer<T> {
	
	protected final EntityControllableModel<T> model;
	
	public RendererEntityAircraft(Context context, EntityControllableModel<T> model) {
		super(context);
		this.shadowRadius = 0.8f;
		this.model = model;
	}
	
	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		float red = 1f, green = 1f, blue = 1f;
		if (!entity.isOperational()) {
			float grey = 0.4f;
			red = green = blue = grey;
		}
		
		Quaternion q = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());
		poseStack.pushPose();
        poseStack.mulPose(q);
        
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
		model.renderToBuffer(entity, partialTicks, poseStack, vertexconsumer, packedLight, 
				OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
		
        poseStack.popPose();
        
        if (shouldDrawRotableHitboxes(entity)) drawRotableHitboxeOutlines(entity, partialTicks, poseStack, bufferSource);
        renderOverlayComponents(entity, poseStack, bufferSource, packedLight, partialTicks);
		
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
	}
	
	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return entity.getTexture();
	}
	
}
