package com.onewhohears.dscombat.client.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.client.renderer.RendererEntityAircraft;
import com.onewhohears.dscombat.data.aircraft.VehicleTextureManager.TextureLayer;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;
import net.minecraftforge.client.model.renderable.ITextureRenderTypeLookup;

public class ObjAircraftModel<T extends EntityVehicle> extends ObjEntityModel<T> {
	
	public ObjAircraftModel(String modelId) {
		super(modelId);
	}
	
	@Override
	public void render(T entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks) {
		super.render(entity, poseStack, bufferSource, lightmap, partialTicks);
		Transforms context = getComponentTransforms(entity, partialTicks);
		TextureLayer[] layers = entity.textureManager.getTextureLayers();
		for (int i = 0; i < layers.length; ++i) {
			if (!layers[i].canRender()) continue;
			poseStack.pushPose();
			float scale = 1.002f + 0.002f*i;
			poseStack.scale(scale, scale, scale);
			ObjModelColorHolder.setColor(layers[i].getColor());
			getModel().render(poseStack, bufferSource, getLayerTextureRenderTypeLookup(layers[i]), 
					getLight(entity, lightmap), OverlayTexture.NO_OVERLAY, partialTicks, context);
			ObjModelColorHolder.resetColor();
			poseStack.popPose();
		}
	}
	
	@Override
	protected ITextureRenderTypeLookup getTextureRenderTypeLookup(T entity) {
		return (texture) -> RendererEntityAircraft.getBaseRenderType(entity.textureManager.getBaseTexture());
	}
	
	protected ITextureRenderTypeLookup getLayerTextureRenderTypeLookup(TextureLayer layer) {
		return (texture) -> RendererEntityAircraft.getLayerRenderType(layer.getTexture());
	}
	
	@Override
	protected void rotate(T entity, float partialTicks, PoseStack poseStack) {
		Quaternion q = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());
        poseStack.mulPose(q);
	}
	
	@Override
	protected int getLight(T entity, int lightmap) {
		if (!entity.isOperational()) return 1;
		return lightmap;
	}

}
