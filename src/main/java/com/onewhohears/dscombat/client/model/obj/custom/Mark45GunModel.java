package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class Mark45GunModel extends ObjTurretModel<EntityTurret> {

	public Mark45GunModel() {
		super("naval_gun", true);
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityTurret entity, float partialTicks) {
		float xrothead = UtilAngles.lerpAngle(partialTicks, entity.xRotRelO, entity.getRelRotX());
		Matrix4f xrothead_mat = UtilAngles.pivotPixelsRotZ(0, 9f, -11f, xrothead);
		ImmutableMap<String, Matrix4f> transforms = ImmutableMap.<String, Matrix4f>builder()
			.put("cube", xrothead_mat)
			.build();
		return Transforms.of(transforms);
	}
	
	@Override
	protected void rotate(EntityTurret entity, float partialTicks, PoseStack poseStack) {
		super.rotate(entity, partialTicks, poseStack);
		poseStack.mulPose(Vector3f.YP.rotationDegrees(180f));
	}

}
