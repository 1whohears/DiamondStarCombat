package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjVehicleModel;
import com.onewhohears.dscombat.entity.vehicle.EntityPlane;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class JasonPlaneModel extends ObjVehicleModel<EntityPlane> {
	
	public JasonPlaneModel() {
		super("jason_plane");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityPlane entity, float partialTicks) {
		// blade
		float bladerot = entity.getMotorRotation(partialTicks, 20);
		Matrix4f blade0_mat = UtilAngles.pivotPixelsRotZ(-0.0426f, 42.0581f, 82.8989f, bladerot);
		// landing gear
		float gearpos = entity.getLandingGearPos(partialTicks);
		Matrix4f lg0_mat, lg1_mat, lg2_mat;
		if (gearpos >= 1f) lg0_mat = lg1_mat = lg2_mat = INVISIBLE;
		else {
			float degrees = gearpos*90;
			lg0_mat = UtilAngles.pivotPixelsRotZ(39.5712f, 28.6068f, 25.0786f, -degrees);
			lg1_mat = UtilAngles.pivotPixelsRotZ(-39.5712f, 28.6068f, 25.0786f, degrees);
			lg2_mat = UtilAngles.pivotPixelsRotX(0, 34.8817f, -71.9732f, degrees);
		}
		// flaps
		Matrix4f rudder = UtilAngles.pivotPixelsRotY(0, 55.8305f, -107.139f, entity.inputs.yaw*15);
		Matrix4f left_elevator = UtilAngles.pivotPixelsRotX(20.9161f, 50.9701f, -99.3629f, entity.inputs.pitch*22);
		Matrix4f right_elevator = UtilAngles.pivotPixelsRotX(-20.9161f, 50.9701f, -99.3629f, entity.inputs.pitch*22);
		Matrix4f left_flap, right_flap;
		if (entity.isFlapsDown()) {
			left_flap = UtilAngles.pivotPixelsRotX(94.4596f, 36.7061f, -0.8385f, -22);
			right_flap = UtilAngles.pivotPixelsRotX(-94.4596f, 36.7061f, -0.8385f, -22);
		} else {
			left_flap = UtilAngles.pivotPixelsRotX(94.4596f, 36.7061f, -0.8385f, entity.inputs.roll*-22);
			right_flap = UtilAngles.pivotPixelsRotX(-94.4596f, 36.7061f, -0.8385f, entity.inputs.roll*22);
		}
		// controls
		Quaternion stickRot = Vector3f.XP.rotationDegrees(entity.inputs.pitch*-25);
		stickRot.mul(Vector3f.ZP.rotationDegrees(entity.inputs.roll*25));
		Matrix4f stick = UtilAngles.pivotPixelsRot(0, 33.8882f, 4.717f, stickRot);
		Matrix4f throttle = Matrix4f.createTranslateMatrix(0, 0, entity.getCurrentThrottle()*0.125f);
		Matrix4f left_pedal = Matrix4f.createTranslateMatrix(0, 0, entity.inputs.yaw*-0.0625f);
		Matrix4f right_pedal = Matrix4f.createTranslateMatrix(0, 0, entity.inputs.yaw*0.0625f);
		ImmutableMap<String, Matrix4f> transforms = ImmutableMap.<String, Matrix4f>builder()
			.put("lg0", lg0_mat)
			.put("lg1", lg1_mat)
			.put("lg2", lg2_mat)
			.put("blade0", blade0_mat)
			.put("surface0", rudder)
			.put("surface1", left_flap)
			.put("surface2", right_flap)
			.put("surface3", left_elevator)
			.put("surface4", right_elevator)
			.put("stick", stick)
			.put("throttle", throttle)
			.put("pedal0", left_pedal)
			.put("pedal1", right_pedal)
			.build();
		return Transforms.of(transforms);
	}
	
	private static final Vector3f PIVOT = new Vector3f(0, -2f, 0);
	
	@Override
	public Vector3f getGlobalPivot() {
		return PIVOT;
	}
	
}
