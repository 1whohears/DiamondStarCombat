package com.onewhohears.dscombat.client.model.obj.custom;

import com.onewhohears.dscombat.client.model.obj.ObjRadarModel;
import com.onewhohears.dscombat.entity.parts.EntityRadar;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.util.Mth;

import java.util.Map;

public class StickRadarModel extends ObjRadarModel {

	public StickRadarModel() {
		super("stick_radar");
	}
	
	@Override
    protected void addComponentTransforms(Map<String, Mat4f> transforms, EntityRadar entity, float partialTicks) {
		float rot_rate = Mth.PI*3;
		float xrothead = UtilAngles.lerpAngle(partialTicks, entity.tickCount*rot_rate, (entity.tickCount+1)*rot_rate);
		Mat4f xrothead_mat = UtilAngles.pivotRotY(0, 0, 0, xrothead);
        transforms.put("cube1", xrothead_mat);
	}

}
