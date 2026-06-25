package com.onewhohears.dscombat.client.model.obj.custom;

import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import java.util.Map;

public class MLSModel extends ObjTurretModel<EntityTurret> {

	public MLSModel() {
		super("mls", true);
	}
	
	@Override
    protected void addComponentTransforms(Map<String, Mat4f> transforms, EntityTurret entity, float partialTicks) {
		float xrothead = UtilAngles.lerpAngle(partialTicks, entity.xRotRelO, entity.getRelRotX());
		Mat4f xrothead_mat = UtilAngles.pivotPixelsRotX(0, 15.9625f, 0.2283f, xrothead);
        transforms.put("head", xrothead_mat);
	}

}
