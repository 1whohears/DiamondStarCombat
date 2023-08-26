package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Matrix4f;
import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class SamLauncherModel extends ObjTurretModel<EntityTurret> {

	public SamLauncherModel() {
		super("samlauncherv3");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityTurret entity, float partialTicks) {
		float xrothead = UtilAngles.lerpAngle(partialTicks, entity.xRotRelO, entity.getRelRotX());
		Matrix4f xrothead_mat = UtilAngles.pivotPixelsRotX(0, 36.6f, -10.2f, xrothead);
		Matrix4f m1_mat = xrothead_mat, m2_mat = xrothead_mat, m3_mat = xrothead_mat, m4_mat = xrothead_mat;
		int ammo = entity.getAmmo();
		if (ammo < 4) m4_mat = INVISIBLE;
		if (ammo < 3) m3_mat = INVISIBLE;
		if (ammo < 2) m2_mat = INVISIBLE;
		if (ammo < 1) m1_mat = INVISIBLE;
		ImmutableMap<String, Matrix4f> transforms = ImmutableMap.<String, Matrix4f>builder()
			.put("launcher", xrothead_mat)
			.put("m1", m1_mat)
			.put("m2", m2_mat)
			.put("m3", m3_mat)
			.put("m4", m4_mat)
			.build();
		return Transforms.of(transforms);
	}

}
