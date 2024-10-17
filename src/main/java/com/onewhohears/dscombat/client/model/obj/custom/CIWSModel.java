package com.onewhohears.dscombat.client.model.obj.custom;

import com.mojang.blaze3d.vertex.PoseStack;

import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.VectorUtils;
import org.joml.Matrix4f;

import java.util.Map;

public class CIWSModel extends ObjTurretModel<EntityTurret> {

	public CIWSModel() {
		super("ciws", true, "ciws_shooting", "ciws_shooting_end");
	}

	@Override
	protected void addComponentTransforms(Map<String, Matrix4f> transforms, EntityTurret entity, float partialTicks) {
		float xrothead = entity.getViewXRot(partialTicks);
		Matrix4f xrothead_mat = UtilAngles.pivotPixelsRotX(0, 14f, 0, -xrothead);
		transforms.put("bone2", xrothead_mat);
		super.addComponentTransforms(transforms, entity, partialTicks);
	}

	@Override
	protected void rotate(EntityTurret entity, float partialTicks, PoseStack poseStack) {
		super.rotate(entity, partialTicks, poseStack);
		poseStack.mulPose(VectorUtils.rotationQuaternion(VectorUtils.POSITIVE_Y, 180f));
	}

	@Override
	public boolean globalRotateX() {
		return false;
	}

}
