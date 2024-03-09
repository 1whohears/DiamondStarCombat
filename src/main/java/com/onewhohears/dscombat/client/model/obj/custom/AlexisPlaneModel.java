package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjAircraftModel;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class AlexisPlaneModel extends ObjAircraftModel<EntityPlane> {

	public AlexisPlaneModel() {
		super("alexis_plane");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityPlane entity, float partialTicks) {
		// landing gear
		float gearpos = entity.getLandingGearPos(partialTicks);
		Matrix4f lg0_mat, lg1_mat, lg2_mat;
		if (gearpos >= 1f) lg0_mat = lg1_mat = lg2_mat = INVISIBLE;
		else {
			float degrees = gearpos*90;
			lg0_mat = UtilAngles.pivotPixelsRotX(0, 17.9256f, 45.5465f, degrees);
			Quaternion lg1Rot = Vector3f.XN.rotationDegrees(degrees);
			Quaternion lg2Rot = lg1Rot.copy();
			lg1Rot.mul(Vector3f.ZP.rotationDegrees(gearpos*-50));
			lg2Rot.mul(Vector3f.ZP.rotationDegrees(gearpos*50));
			lg1_mat = UtilAngles.pivotPixelsRot(10.4f, 23.4316f, -34.5632f, lg1Rot);
			lg2_mat = UtilAngles.pivotPixelsRot(-10.4f, 23.4316f, -34.5632f, lg2Rot);
		}
		// flaps
		Quaternion rudderRot = Vector3f.YP.rotationDegrees(entity.inputs.yaw*15);
		rudderRot.mul(Vector3f.ZP.rotationDegrees(entity.inputs.yaw*-15));
		Matrix4f rudder = UtilAngles.pivotPixelsRot(0, 81.0642f, -125.9015f, rudderRot);
		Matrix4f left_elevator = UtilAngles.pivotPixelsRotX(21.7721f, 38.2332f, -119.5741f, entity.inputs.pitch*22);
		Matrix4f right_elevator = UtilAngles.pivotPixelsRotX(-21.7721f, 38.2332f, -119.5741f, entity.inputs.pitch*22);
		Matrix4f left_flap, right_flap;
		if (entity.isFlapsDown()) {
			left_flap = UtilAngles.pivotPixelsRotX(49.8276f, 38.2332f, -58.5647f, -22);
			right_flap = UtilAngles.pivotPixelsRotX(-49.8276f, 38.2332f, -58.5647f, -22);
		} else {
			left_flap = UtilAngles.pivotPixelsRotX(49.8276f, 38.2332f, -58.5647f, entity.inputs.roll*-22);
			right_flap = UtilAngles.pivotPixelsRotX(-49.8276f, 38.2332f, -58.5647f, entity.inputs.roll*22);
		}
		// controls
		Quaternion stickRot = Vector3f.XP.rotationDegrees(entity.inputs.pitch*-25);
		stickRot.mul(Vector3f.ZP.rotationDegrees(entity.inputs.roll*25));
		Matrix4f stick = UtilAngles.pivotPixelsRot(-7.1778f, 40.7333f, 78.8995f, stickRot);
		Matrix4f left_pedal = Matrix4f.createTranslateMatrix(0, 0, entity.inputs.yaw*-0.0625f);
		Matrix4f right_pedal = Matrix4f.createTranslateMatrix(0, 0, entity.inputs.yaw*0.0625f);
		Matrix4f throttle = Matrix4f.createTranslateMatrix(0, 0, entity.getCurrentThrottle()*0.1875f);
		ImmutableMap<String, Matrix4f> transforms = ImmutableMap.<String, Matrix4f>builder()
			.put("lg0", lg0_mat)
			.put("lg1", lg1_mat)
			.put("lg2", lg2_mat)
			.put("surface0", rudder)
			.put("surface1", left_flap)
			.put("surface2", right_flap)
			.put("surface3", left_elevator)
			.put("surface4", right_elevator)
			.put("stick", stick)
			.put("pedal0", left_pedal)
			.put("pedal1", right_pedal)
			.put("throttle", throttle)
			.build();
		return Transforms.of(transforms);
	}
	
	private static final Vector3f PIVOT = new Vector3f(0, -2f, 2f);
	
	@Override
	public Vector3f getGlobalPivot() {
		return PIVOT;
	}

}
