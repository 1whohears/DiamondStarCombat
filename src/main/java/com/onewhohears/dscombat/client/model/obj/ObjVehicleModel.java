package com.onewhohears.dscombat.client.model.obj;

import com.google.gson.JsonArray;
import com.mojang.blaze3d.vertex.PoseStack;

import com.onewhohears.dscombat.client.renderer.RendererEntityVehicle;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.KeyframeAnimationPlayer;
import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.KeyframeAnimsEntityModel;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.UtilGeometry;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.client.model.renderable.ITextureRenderTypeLookup;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class ObjVehicleModel<T extends EntityVehicle> extends KeyframeAnimsEntityModel<T> {
	
	public ObjVehicleModel(String modelId) {
		this(modelId, new JsonArray());
	}

	public ObjVehicleModel(String modelId, JsonArray anims) {
		super(modelId, anims);
	}

	public ObjVehicleModel(String modelId, JsonArray anims, String... animDataIds) {
		super(modelId, anims, animDataIds);
	}

	public ObjVehicleModel(String modelId, String[] animDataIds) {
		this(modelId, new JsonArray(), animDataIds);
	}

	@Override
	protected ITextureRenderTypeLookup getTextureRenderTypeLookup(T entity) {
		return (texture) -> RendererEntityVehicle.getCullBaseRenderType(entity.textureManager.getDynamicTexture());
	}
	
	@Override
	public void render(T entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks) {
		// apparently this slightly different render order is needed or else the plane's render position gets messed up
		handleGlobalOverrides(entity, partialTicks, poseStack);
		rotate(entity, partialTicks, poseStack);
		getModel().render(poseStack, bufferSource, getTextureRenderTypeLookup(entity), 
				getLight(entity, lightmap), getOverlay(entity), partialTicks, 
				getComponentTransforms(entity, partialTicks));
	}
	
	@Override
	protected void rotate(T entity, float partialTicks, PoseStack poseStack) {
		Quaternionf q = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());
        Vector3f pivot = getGlobalPivot();
		if (!UtilGeometry.isZero(pivot)) poseStack.mulPoseMatrix(UtilAngles.pivotInvRot(pivot, q));
		else poseStack.mulPose(q);
	}
	
	@Override
	protected int getLight(T entity, int lightmap) {
		if (!entity.isOperational()) return 1;
		return lightmap;
	}

}
