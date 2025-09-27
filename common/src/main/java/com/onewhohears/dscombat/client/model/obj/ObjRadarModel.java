package com.onewhohears.dscombat.client.model.obj;

import com.google.gson.JsonArray;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.entity.parts.EntityRadar;
import com.onewhohears.onewholibs.client.model.obj.ObjBakedModel;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels.ModelOverrides;
import com.onewhohears.onewholibs.client.model.obj.ObjModelHandler;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;

public class ObjRadarModel extends ObjPartModel<EntityRadar> {
	
	@Nullable protected final String largeModelId;
	@Nullable private ObjModelHandler largeModel;
	@Nullable private ModelOverrides largeOverride;
	protected MastType currentMastType = MastType.NONE;
	
	public ObjRadarModel(String modelId) {
		this(modelId, null);
	}

	public ObjRadarModel(String modelId, String largeModelId, String... animDataIds) {
		this(modelId, largeModelId, new JsonArray(), animDataIds);
	}

	public ObjRadarModel(String modelId, String largeModelId, JsonArray customAnims, String... animDataIds) {
		super(modelId, customAnims, animDataIds);
		this.largeModelId = largeModelId;
	}
	
	@Override
	public void render(EntityRadar entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks) {
		if (!entity.shouldRender()) return;
		currentMastType = entity.getVehicleMastType();
		poseStack.translate(0, currentMastType.radarTopPos, 0);
		super.render(entity, poseStack, bufferSource, lightmap, partialTicks);
		if (!currentMastType.isNone()) {
            ObjBakedModel mastModel = ObjEntityModels.get().getBakedModel(currentMastType.modelId);
			if (mastModel != null) {
				poseStack.pushPose();
				poseStack.translate(0, -currentMastType.radarTopPos, 0);
				mastModel.render(poseStack, bufferSource, RenderType::entitySolid,
					getLight(entity, lightmap), getOverlay(entity), partialTicks, NO_TRANSFORMS);
				poseStack.popPose();
			}
		}
	}
	
	@Override
	public ObjModelHandler getObjModelHandler() {
		if (currentMastType.isLarge() && largeModelId != null && !largeModelId.isEmpty()) {
			if (largeModel == null) largeModel = ObjEntityModels.get().getObjModelHandler(largeModelId);
			return largeModel;
		}
		return super.getObjModelHandler();
	}
	
	@Override
	public ModelOverrides getModelOverride() {
		if (currentMastType.isLarge() && largeModelId != null && !largeModelId.isEmpty()) {
			if (largeOverride == null) largeOverride = ObjEntityModels.get().getModelOverride(largeModelId); 
			return largeOverride;
		}
		return super.getModelOverride();
	}
	
	public enum MastType {
		NONE("", 0),
		THIN("thin_mast", 3.5f),
		NORMAL("normal_mast", 3.125f),
		LARGE("thick_mast", 3);
		public final String modelId;
		public final float radarTopPos;
		MastType(String modelId, float radarTopPos) {
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
