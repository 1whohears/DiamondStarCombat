package com.onewhohears.dscombat.client.model.obj.custom;

import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import java.util.Map;

public class AATurretModel extends ObjTurretModel<EntityTurret> {

	public AATurretModel() {
		super("aa_turret", false);
	}
	
	@Override
	protected void addComponentTransforms(Map<String, Mat4f> transforms, EntityTurret entity, float partialTicks) {
		float yrothead = UtilAngles.lerpAngle180(partialTicks, entity.yRotRelO, entity.getRelRotY());
		float xrothead = UtilAngles.lerpAngle(partialTicks, entity.xRotRelO, entity.getRelRotX());
		Mat4f yrothead_mat = UtilAngles.pivotRotY(0, 0, 0, -yrothead);
		Mat4f xrothead_mat = UtilAngles.pivotPixelsRotX(-7, 16.5f, 0, xrothead);
        transforms.put("seat", yrothead_mat);
        transforms.put("gun", xrothead_mat);
	}

}
