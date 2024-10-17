package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;

import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import com.onewhohears.onewholibs.util.math.VectorUtils;
import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;
import org.joml.Matrix4f;

import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class Mark7GunModel extends ObjTurretModel<EntityTurret> {

	public Mark7GunModel() {
		super("naval_cannon", false);
	}

	@Override
	protected Transforms getComponentTransforms(EntityTurret entity, float partialTicks) {
		float yrothead = UtilAngles.lerpAngle180(partialTicks, entity.yRotRelO, entity.getRelRotY());
		float xrothead = UtilAngles.lerpAngle(partialTicks, entity.xRotRelO, entity.getRelRotX());

		Matrix4f yrothead_mat = UtilAngles.pivotRotY(0, 0, 0, -yrothead);
		Matrix4f xrothead_mat = UtilAngles.pivotPixelsRotX(0, 10f, -25f, -xrothead);

		// Copy matrices manually by creating new instances
		Matrix4f head_mat = new Matrix4f(yrothead_mat);
		Matrix4f barrels_mat = new Matrix4f(yrothead_mat);

		// Use JOML `mul` instead of `multiply`
		barrels_mat.mul(xrothead_mat);

		ImmutableMap<String, Matrix4f> transforms = ImmutableMap.<String, Matrix4f>builder()
				.put("head", head_mat)
				.put("barrels", barrels_mat)
				.build();

		return Transforms.of(transforms);
	}

	@Override
	protected void rotate(EntityTurret entity, float partialTicks, PoseStack poseStack) {
		super.rotate(entity, partialTicks, poseStack);
		poseStack.translate(0, 0.5, 0);

		// Use VectorUtils for the rotation
		Quaternionf rotationQuat = VectorUtils.rotationQuaternion(VectorUtils.POSITIVE_Y, 180f);
		poseStack.mulPose(rotationQuat);
	}
}
