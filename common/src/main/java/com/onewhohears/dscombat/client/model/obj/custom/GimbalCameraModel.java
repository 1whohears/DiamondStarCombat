package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.Vec3f;
import com.onewhohears.dscombat.client.model.obj.ObjPartModel;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityGimbal;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class GimbalCameraModel extends ObjPartModel<EntityGimbal> {

	public GimbalCameraModel() {
		super("gimbal_camera");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityGimbal entity, float partialTicks) {
		EntityVehicle vehicle = entity.getParentVehicle();
		if (vehicle == null) return Transforms.EMPTY;
		float[] relangles = UtilAngles.globalToRelativeDegrees(entity.getViewXRot(partialTicks), 
				entity.getViewYRot(partialTicks), vehicle.getClientQ());
		QuaternionF rot = Vec3f.XN.rotationDegrees(relangles[0]);
		rot.mul(Vec3f.YP.rotationDegrees(relangles[1]));
		Mat4f head_mat = UtilAngles.pivotPixelsRot(0, 2f, 3.5f, rot);
		ImmutableMap<String, Mat4f> transforms = ImmutableMap.<String, Mat4f>builder()
			.put("head", head_mat)
			.build();
		return Transforms.of(transforms);
	}

}
