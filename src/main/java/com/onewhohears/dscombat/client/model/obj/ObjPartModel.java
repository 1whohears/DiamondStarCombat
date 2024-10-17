package com.onewhohears.dscombat.client.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.KeyframeAnimsEntityModel;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import com.onewhohears.onewholibs.util.math.VectorUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ObjPartModel<T extends EntityPart> extends KeyframeAnimsEntityModel<T> {
	
	public ObjPartModel(String modelId) {
		super(modelId);
	}

	public ObjPartModel(String modelId, String... animDataIds) {
		super(modelId, animDataIds);
	}
	
	@Override
	public void render(T entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks) {
		if (entity.shouldRender()) super.render(entity, poseStack, bufferSource, lightmap, partialTicks);
	}
	
	@Override
	protected void rotate(T entity, float partialTicks, PoseStack poseStack) {
		if (entity.getVehicle() instanceof EntityVehicle plane) {
			Quaternionf q = UtilAngles.lerpQ(partialTicks, plane.getPrevQ(), plane.getClientQ());
			poseStack.mulPose(q);
		}
		poseStack.mulPose(VectorUtils.rotationQuaternion(VectorUtils.POSITIVE_Z, entity.getZRot()));
	}
	
	@Override
	protected int getLight(T entity, int lightmap) {
		if (entity.getVehicle() instanceof EntityVehicle plane && !plane.isOperational()) return 1;
		return lightmap;
	}

}
