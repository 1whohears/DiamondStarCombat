package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Matrix4f;
import com.onewhohears.dscombat.client.model.obj.ObjRadarModel;
import com.onewhohears.dscombat.entity.parts.EntityRadar;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.util.Mth;
import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class Radar1Model extends ObjRadarModel {

	public Radar1Model() {
		super("radar1");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityRadar entity, float partialTicks) {
		float rot_rate = Mth.PI;
		float xrothead = UtilAngles.lerpAngle(partialTicks, entity.tickCount*rot_rate, (entity.tickCount+1)*rot_rate);
		Matrix4f xrothead_mat = UtilAngles.pivotRotY(0, 0, 0, xrothead);
		ImmutableMap<String, Matrix4f> transforms = ImmutableMap.<String, Matrix4f>builder()
			.put("head", xrothead_mat)
			.build();
		return Transforms.of(transforms);
	}

}
