package com.onewhohears.dscombat.client.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.dscombat.util.math.UtilAngles;

public class ObjPartModel<T extends EntityPart> extends ObjEntityModel<T> {

	public ObjPartModel(String modelId) {
		super(modelId);
	}
	
	@Override
	protected void rotate(T entity, float partialTicks, PoseStack poseStack) {
		if (entity.getVehicle() instanceof EntityAircraft plane) {
			Quaternion q = UtilAngles.lerpQ(partialTicks, plane.getPrevQ(), plane.getClientQ());
			poseStack.mulPose(q);
		}
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(entity.getZRot()));
	}

}