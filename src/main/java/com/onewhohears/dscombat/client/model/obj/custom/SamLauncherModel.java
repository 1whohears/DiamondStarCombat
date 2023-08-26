package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Matrix4f;
import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class SamLauncherModel extends ObjTurretModel<EntityTurret> {

	public SamLauncherModel() {
		super("sam_launcher_v2");
	}
	
	@Override
	protected Transforms getTransforms(EntityTurret entity, float partialTicks) {
		float xrothead = UtilAngles.lerpAngle(partialTicks, entity.xRotRelO, entity.getRelRotX());
		ImmutableMap<String, Matrix4f> transforms = ImmutableMap.<String, Matrix4f>builder()
			.put("launcher", UtilAngles.pivotRotX(0, 17f/16f, -9f/16f, xrothead))
			.build();
		return Transforms.of(transforms);
	}

}
