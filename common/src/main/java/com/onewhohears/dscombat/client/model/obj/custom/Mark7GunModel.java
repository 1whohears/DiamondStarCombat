package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.Vec3f;
import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class Mark7GunModel extends ObjTurretModel<EntityTurret> {

	public Mark7GunModel() {
		super("naval_cannon", false);
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityTurret entity, float partialTicks) {
		float yrothead = UtilAngles.lerpAngle180(partialTicks, entity.yRotRelO, entity.getRelRotY());
		float xrothead = UtilAngles.lerpAngle(partialTicks, entity.xRotRelO, entity.getRelRotX());
		Mat4f yrothead_mat = UtilAngles.pivotRotY(0, 0, 0, -yrothead);
		Mat4f xrothead_mat = UtilAngles.pivotPixelsRotX(0, 10f, -25f, -xrothead);
		Mat4f head_mat = yrothead_mat.copy(), barrels_mat = yrothead_mat.copy();
		barrels_mat.multiply(xrothead_mat);
		ImmutableMap<String, Mat4f> transforms = ImmutableMap.<String, Mat4f>builder()
			.put("head", head_mat)
			.put("barrels", barrels_mat)
			.build();
		return Transforms.of(transforms);
	}
	
	@Override
	protected void rotate(EntityTurret entity, float partialTicks, PoseStack poseStack) {
		super.rotate(entity, partialTicks, poseStack);
		poseStack.translate(0, 0.5, 0);
		poseStack.mulPose(Vec3f.YP.rotationDegrees(180f));
	}

}
