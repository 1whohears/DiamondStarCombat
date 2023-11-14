package com.onewhohears.dscombat.client.renderer;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.client.model.EntityControllableModel;
import com.onewhohears.dscombat.data.aircraft.VehicleTextureManager.TextureLayer;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RendererEntityAircraft<T extends EntityVehicle> extends EntityRenderer<T> implements RotableHitboxRenderer, VehicleScreenRenderer<T> {
	
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
		
		TextureLayer[] layers = entity.textureManager.getTextureLayers();
		for (int i = 0; i < layers.length; ++i) {
			if (!layers[i].canRender()) continue;
			poseStack.pushPose();
			float scale = 1.001f + 0.001f*i;
			poseStack.scale(scale, scale, scale);
			VertexConsumer layerconsumer = bufferSource.getBuffer(RenderType.entityTranslucent(layers[i].getTexture()));
			Color color = new Color(layers[i].getColor());
			model.renderToBuffer(entity, partialTicks, poseStack, layerconsumer, 
					packedLight, OverlayTexture.NO_OVERLAY, 
					color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, 1.0F);
			poseStack.popPose();
		}
		
        poseStack.popPose();
        
        if (shouldDrawRotableHitboxes(entity)) drawRotableHitboxeOutlines(entity, partialTicks, poseStack, bufferSource);
        if (shouldRenderScreens(entity)) renderVehicleScreens(entity, poseStack, bufferSource, packedLight, partialTicks);
		
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
	}
	
	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return entity.textureManager.getBaseTexture();
	}
	
}
