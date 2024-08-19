package com.onewhohears.dscombat.client.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.onewholibs.util.math.UtilAngles;

public class ObjTurretModel<T extends EntityTurret> extends ObjPartModel<T> {
	
	protected final boolean rotYawAll;
	
	public ObjTurretModel(String modelId, boolean rotYawAll) {
		super(modelId);
		this.rotYawAll = rotYawAll;
	}
	
	@Override
	protected void rotate(T entity, float partialTicks, PoseStack poseStack) {
		super.rotate(entity, partialTicks, poseStack);
		if (rotYawAll) poseStack.mulPose(Vector3f.YN.rotationDegrees(UtilAngles.lerpAngle180(
				partialTicks, entity.yRotRelO, entity.getRelRotY())));
	}

}
