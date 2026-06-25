package com.onewhohears.dscombat.client.model.obj.custom;

import com.onewhohears.dscombat.client.model.obj.ObjPartModel;
import com.onewhohears.dscombat.entity.parts.EntityGimbal;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.Vec3f;

import java.util.Map;

public class GimbalCameraModel extends ObjPartModel<EntityGimbal> {

	public GimbalCameraModel() {
		super("gimbal_camera");
	}
	
	@Override
    protected void addComponentTransforms(Map<String, Mat4f> transforms, EntityGimbal entity, float partialTicks) {
		EntityVehicle vehicle = entity.getParentVehicle();
		if (vehicle == null) return;
		float[] relangles = UtilAngles.globalToRelativeDegrees(entity.getViewXRot(partialTicks), 
				entity.getViewYRot(partialTicks), vehicle.getClientQ());
		QuaternionF rot = Vec3f.XN.rotationDegrees(relangles[0]);
		rot.mul(Vec3f.YP.rotationDegrees(relangles[1]));
		Mat4f head_mat = UtilAngles.pivotPixelsRot(0, 2f, 3.5f, rot);
        transforms.put("head", head_mat);
	}

}
