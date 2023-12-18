package com.onewhohears.dscombat.client.model.obj;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.model.obj.ObjEntityModels.ModelOverrides;
import com.onewhohears.dscombat.entity.parts.EntityRadar;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.client.model.renderable.CompositeRenderable;
import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class ObjRadarModel extends ObjPartModel<EntityRadar> {
	
	@Nullable protected final String largeModelId;
	protected MastType currentMastType = MastType.NONE;
	
	public ObjRadarModel(String modelId) {
		this(modelId, null);
	}
	
	public ObjRadarModel(String modelId, String largeModelId) {
		super(modelId);
		this.largeModelId = largeModelId;
	}
	
	@Override
	public void render(EntityRadar entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks) {
		if (!entity.shouldRender()) return;
		currentMastType = entity.getVehicleMastType();
		poseStack.translate(0, currentMastType.radarTopPos, 0);
		super.render(entity, poseStack, bufferSource, lightmap, partialTicks);
		if (!currentMastType.isNone()) {
			CompositeRenderable mastModel = ObjEntityModels.get().getBakedModel(currentMastType.modelId);
			if (mastModel != null) {
				poseStack.pushPose();
				poseStack.translate(0, -currentMastType.radarTopPos, 0);
				mastModel.render(poseStack, bufferSource, getTextureRenderTypeLookup(entity), 
					getLight(entity, lightmap), getOverlay(entity), partialTicks, Transforms.EMPTY);
				poseStack.popPose();
			}
		}
	}
	
	@Nullable
	@Override
	public CompositeRenderable getModel() {
		if (currentMastType.isLarge() && largeModelId != null) 
			return ObjEntityModels.get().getBakedModel(largeModelId); 
		return ObjEntityModels.get().getBakedModel(modelId);
	}
	
	@Nullable
	@Override
	public ModelOverrides getModelOverride() {
		if (currentMastType.isLarge() && largeModelId != null) 
			return ObjEntityModels.get().getModelOverride(largeModelId); 
		return ObjEntityModels.get().getModelOverride(modelId);
	}
	
	public static enum MastType {
		NONE("", 0),
		THIN("thin_mast", 3.5f),
		NORMAL("normal_mast", 3.125f),
		LARGE("thick_mast", 3);
		public final String modelId;
		public final float radarTopPos;
		private MastType(String modelId, float radarTopPos) {
			this.modelId = modelId;
			this.radarTopPos = radarTopPos;
		}
		public boolean isLarge() {
			return this == LARGE;
		}
		public boolean isNone() {
			return this == NONE;
		}
	}

}
