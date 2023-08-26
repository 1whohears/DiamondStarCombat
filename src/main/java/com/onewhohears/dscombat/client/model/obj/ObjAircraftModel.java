package com.onewhohears.dscombat.client.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.math.UtilAngles;

public class ObjAircraftModel<T extends EntityAircraft> extends ObjEntityModel<T> {

	public ObjAircraftModel(String modelId) {
		super(modelId);
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
