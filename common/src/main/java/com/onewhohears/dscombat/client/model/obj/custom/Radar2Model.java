package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.dscombat.client.model.obj.ObjRadarModel;
import com.onewhohears.dscombat.entity.parts.EntityRadar;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraft.util.Mth;
import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class Radar2Model extends ObjRadarModel {

	public Radar2Model() {
		super("radar2");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityRadar entity, float partialTicks) {
		float rot_rate = Mth.PI;
		float xrothead = UtilAngles.lerpAngle(partialTicks, entity.tickCount*rot_rate, (entity.tickCount+1)*rot_rate);
		Mat4f xrothead_mat = UtilAngles.pivotRotY(0, 0, 0, xrothead);
		ImmutableMap<String, Mat4f> transforms = ImmutableMap.<String, Mat4f>builder()
			.put("head", xrothead_mat)
			.build();
		return Transforms.of(transforms);
	}

}
