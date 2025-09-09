package com.onewhohears.dscombat.client.model.obj.custom;

import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import java.util.Map;

public class MinigunTurretModel extends ObjTurretModel<EntityTurret> {

	public MinigunTurretModel() {
		super("minigun_turret", true);
	}

	@Override
	protected void addComponentTransforms(Map<String, Mat4f> transforms, EntityTurret entity, float partialTicks) {
		float xrothead = entity.getViewXRot(partialTicks);
		Mat4f xrothead_mat = UtilAngles.pivotPixelsRotX(0, 14f, 8, xrothead);
		transforms.put("gun", xrothead_mat);
		super.addComponentTransforms(transforms, entity, partialTicks);
	}

	@Override
	public boolean globalRotateX() {
		return false;
	}

}
