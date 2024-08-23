package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class AATurretModel extends ObjTurretModel<EntityTurret> {

	public AATurretModel() {
		super("aa_turret", false);
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityTurret entity, float partialTicks) {
		float yrothead = UtilAngles.lerpAngle180(partialTicks, entity.yRotRelO, entity.getRelRotY());
		float xrothead = UtilAngles.lerpAngle(partialTicks, entity.xRotRelO, entity.getRelRotX());
		Matrix4f yrothead_mat = UtilAngles.pivotRotY(0, 0, 0, -yrothead);
		Matrix4f xrothead_mat = UtilAngles.pivotPixelsRotZ(-7, 16.5f, 0, xrothead);
		Matrix4f seat_mat = yrothead_mat.copy(), gun_mat = yrothead_mat.copy(), 
				barrel1_mat = yrothead_mat.copy(), barrel2_mat = yrothead_mat.copy();
		gun_mat.multiply(xrothead_mat);
		barrel1_mat.multiply(xrothead_mat);
		barrel2_mat.multiply(xrothead_mat);
		ImmutableMap<String, Matrix4f> transforms = ImmutableMap.<String, Matrix4f>builder()
			.put("seat", seat_mat)
			.put("gun", gun_mat)
			.put("barrel1", barrel1_mat)
			.put("barrel2", barrel2_mat)
			.build();
		return Transforms.of(transforms);
	}
	
	@Override
	protected void rotate(EntityTurret entity, float partialTicks, PoseStack poseStack) {
		super.rotate(entity, partialTicks, poseStack);
		poseStack.mulPose(Vector3f.YP.rotationDegrees(90f));
	}

}
