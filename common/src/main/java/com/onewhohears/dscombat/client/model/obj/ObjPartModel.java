package com.onewhohears.dscombat.client.model.obj;

import com.google.gson.JsonArray;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.Vec3f;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.KeyframeAnimsEntityModel;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraft.client.renderer.MultiBufferSource;

import java.util.HashMap;
import java.util.Map;

public class ObjPartModel<T extends EntityPart> extends KeyframeAnimsEntityModel<T> {

    public static final Map<String, Mat4f> NO_TRANSFORMS = new HashMap<>();

	public ObjPartModel(String modelId) {
		super(modelId);
	}

	public ObjPartModel(String modelId, String... animDataIds) {
		super(modelId, animDataIds);
	}

	public ObjPartModel(String modelId, JsonArray transforms, String... animDataIds) {
		super(modelId, transforms, animDataIds);
	}
	
	@Override
	public void render(T entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks) {
		if (entity.shouldRender()) super.render(entity, poseStack, bufferSource, lightmap, partialTicks);
	}
	
	@Override
	protected void rotate(T entity, float partialTicks, PoseStack poseStack) {
		if (entity.getVehicle() instanceof EntityVehicle plane) {
			QuaternionF q = UtilAngles.lerpQ(partialTicks, plane.getPrevQ(), plane.getClientQ());
			poseStack.mulPose(q.convert());
		}
		poseStack.mulPose(Vec3f.ZP.rotationDegrees(entity.getZRot()).convert());
	}
	
	@Override
	protected int getLight(T entity, int lightmap) {
		if (entity.getVehicle() instanceof EntityVehicle plane && !plane.isOperational()) return 1;
		return lightmap;
	}

}
