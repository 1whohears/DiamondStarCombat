package com.onewhohears.dscombat.client.model.obj.custom;

import com.mojang.math.Matrix4f;
import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import java.util.Map;

public class SteveUpSmashModel extends ObjTurretModel<EntityTurret> {

	public SteveUpSmashModel() {
		super("steve_up_smash", true);
	}

	@Override
	protected void addComponentTransforms(Map<String, Matrix4f> transforms, EntityTurret entity, float partialTicks) {
		float xrothead = entity.getViewXRot(partialTicks);
		Matrix4f xrothead_mat = UtilAngles.pivotPixelsRotX(0, 43, 0, xrothead);
		transforms.put("gun", xrothead_mat);
		int ammo = entity.getAmmo();
		for (int i = 0; i < 4; ++i) {
			if (i < 4-ammo) {
				Matrix4f open_cover;
				if (i < 2) open_cover = UtilAngles.pivotPixelsRotX(0, 57, 8, -90);
				else open_cover = UtilAngles.pivotPixelsRotX(0, 49, 8, -90);
				transforms.put("cover"+(i+1), open_cover);
			}
		}
		super.addComponentTransforms(transforms, entity, partialTicks);
	}

	@Override
	public boolean globalRotateX() {
		return false;
	}

}
