package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjPartModel;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityGimbal;
import com.onewhohears.dscombat.util.math.UtilAngles;

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
		Quaternion rot = Vector3f.XN.rotationDegrees(relangles[0]);
		rot.mul(Vector3f.YP.rotationDegrees(relangles[1]));
		Matrix4f head_mat = UtilAngles.pivotPixelsRot(0, 2f, 3.5f, rot);
		ImmutableMap<String, Matrix4f> transforms = ImmutableMap.<String, Matrix4f>builder()
			.put("head", head_mat)
			.build();
		return Transforms.of(transforms);
	}

}
