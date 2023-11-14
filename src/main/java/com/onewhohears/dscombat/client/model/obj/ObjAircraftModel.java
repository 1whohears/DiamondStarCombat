package com.onewhohears.dscombat.client.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.aircraft.VehicleTextureManager.TextureLayer;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;
import net.minecraftforge.client.model.renderable.ITextureRenderTypeLookup;

public class ObjAircraftModel<T extends EntityVehicle> extends ObjEntityModel<T> {
	
	public ObjAircraftModel(String modelId) {
		super(modelId);
	}
	
	@Override
	public void render(T entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks) {
		super.render(entity, poseStack, bufferSource, lightmap, partialTicks);
		Transforms transforms = getComponentTransforms(entity, partialTicks);
		TextureLayer[] layers = entity.textureManager.getTextureLayers();
		for (int i = 0; i < layers.length; ++i) {
			if (!layers[i].canRender()) continue;
			poseStack.pushPose();
			float scale = 1.001f + 0.001f*i;
			poseStack.scale(scale, scale, scale);
			getModel().render(poseStack, bufferSource, getLayerTextureRenderTypeLookup(layers[i]), 
					getLight(entity, lightmap), getLayerOverlay(layers[i]), partialTicks, 
					transforms);
			poseStack.popPose();
		}
	}
	
	@Override
	protected ITextureRenderTypeLookup getTextureRenderTypeLookup(T entity) {
		return (texture) -> RenderType.entityTranslucent(entity.textureManager.getBaseTexture());
	}
	
	protected ITextureRenderTypeLookup getLayerTextureRenderTypeLookup(TextureLayer layer) {
		return (texture) -> RenderType.entityTranslucent(layer.getTexture());
	}
	
	protected int getLayerOverlay(TextureLayer layer) {
		return layer.getColor();
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
