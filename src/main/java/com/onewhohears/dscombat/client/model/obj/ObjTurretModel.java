package com.onewhohears.dscombat.client.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.util.math.UtilAngles;

public class ObjTurretModel<T extends EntityTurret> extends ObjPartModel<T> {

	public ObjTurretModel(String modelId) {
		super(modelId);
	}
	
	@Override
	protected void rotate(T entity, float partialTicks, PoseStack poseStack) {
		super.rotate(entity, partialTicks, poseStack);
		poseStack.mulPose(Vector3f.YN.rotationDegrees(UtilAngles.lerpAngle180(partialTicks, entity.yRotRelO, entity.getRelRotY())));
	}

}
