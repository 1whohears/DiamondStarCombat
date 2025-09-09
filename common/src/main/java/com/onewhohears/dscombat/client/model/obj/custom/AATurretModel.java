package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.onewhohears.onewholibs.util.math.Mat4f;
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
		Mat4f yrothead_mat = UtilAngles.pivotRotY(0, 0, 0, -yrothead);
		Mat4f xrothead_mat = UtilAngles.pivotPixelsRotX(-7, 16.5f, 0, xrothead);
		ImmutableMap<String, Mat4f> transforms = ImmutableMap.<String, Mat4f>builder()
			.put("seat", yrothead_mat)
			.put("gun", xrothead_mat)
			.build();
		return Transforms.of(transforms);
	}

}
