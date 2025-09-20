package com.onewhohears.dscombat.client.model.obj.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.Vec3f;

import java.util.Map;

public class Mark45GunModel extends ObjTurretModel<EntityTurret> {

	public Mark45GunModel() {
		super("naval_gun_v2", true);
	}
	
	@Override
    protected void addComponentTransforms(Map<String, Mat4f> transforms, EntityTurret entity, float partialTicks) {
		float xrothead = UtilAngles.lerpAngle(partialTicks, entity.xRotRelO, entity.getRelRotX());
		Mat4f xrothead_mat = UtilAngles.pivotPixelsRotX(0, 9f, -11f, -xrothead);
        transforms.put("cube", xrothead_mat);
	}
	
	@Override
	protected void rotate(EntityTurret entity, float partialTicks, PoseStack poseStack) {
		super.rotate(entity, partialTicks, poseStack);
		poseStack.translate(0, 0.5, 0);
		poseStack.mulPose(Vec3f.YP.rotationDegrees(180f).convert());
	}

}
