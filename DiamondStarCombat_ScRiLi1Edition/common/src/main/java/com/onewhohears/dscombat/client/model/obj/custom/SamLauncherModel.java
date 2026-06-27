package com.onewhohears.dscombat.client.model.obj.custom;

import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import java.util.Map;

public class SamLauncherModel extends ObjTurretModel<EntityTurret> {

	public SamLauncherModel() {
		super("samlauncherv3", true);
	}
	
	@Override
    protected void addComponentTransforms(Map<String, Mat4f> transforms, EntityTurret entity, float partialTicks) {
		float xrothead = UtilAngles.lerpAngle(partialTicks, entity.xRotRelO, entity.getRelRotX());
		Mat4f xrothead_mat = UtilAngles.pivotPixelsRotX(0, 36.6f, -10.2f, xrothead);
		Mat4f m1_mat = xrothead_mat, m2_mat = xrothead_mat, m3_mat = xrothead_mat, m4_mat = xrothead_mat;
		int ammo = entity.getAmmo();
		if (ammo < 4) m4_mat = INVISIBLE;
		if (ammo < 3) m3_mat = INVISIBLE;
		if (ammo < 2) m2_mat = INVISIBLE;
		if (ammo < 1) m1_mat = INVISIBLE;
        transforms.put("launcher", xrothead_mat);
        transforms.put("m1", m1_mat);
        transforms.put("m2", m2_mat);
        transforms.put("m3", m3_mat);
        transforms.put("m4", m4_mat);
	}

}
