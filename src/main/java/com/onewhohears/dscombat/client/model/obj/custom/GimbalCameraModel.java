package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;

import com.onewhohears.dscombat.client.model.obj.ObjPartModel;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityGimbal;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

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
		Quaternionf rot = new Quaternionf().rotateX((float) Math.toRadians(relangles[0]));
		rot.mul(new Quaternionf().rotateY((float) Math.toRadians(relangles[1])));
		Matrix4f head_mat = UtilAngles.pivotPixelsRot(0, 2f, 3.5f, rot);
		ImmutableMap<String, Matrix4f> transforms = ImmutableMap.<String, Matrix4f>builder()
			.put("head", head_mat)
			.build();
		return Transforms.of(transforms);
	}

}
