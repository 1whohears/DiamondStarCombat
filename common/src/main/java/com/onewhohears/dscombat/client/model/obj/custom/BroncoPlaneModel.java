package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.Vec3f;
import com.onewhohears.dscombat.client.model.obj.ObjVehicleModel;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class BroncoPlaneModel extends ObjVehicleModel<EntityVehicle> {

	public BroncoPlaneModel() {
		super("bronco-plane");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityVehicle entity, float partialTicks) {
		// landing gear and blades
		float bladerot = entity.getMotorRotation(partialTicks, 30);
		float gearpos = entity.getLandingGearPos(partialTicks);
		Mat4f blade0rot_mat = UtilAngles.pivotPixelsRotZ(44.8001f, 32.256f, 11.0114f, bladerot);
		Mat4f blade1rot_mat = UtilAngles.pivotPixelsRotZ(-44.8001f, 32.256f, 11.0114f, bladerot);
		Mat4f lg0_mat, lg1_mat, lg2_mat;
		if (gearpos >= 1f) lg0_mat = lg1_mat = lg2_mat = INVISIBLE;
		else {
			float degrees = gearpos*90;
			lg0_mat = UtilAngles.pivotPixelsRotX(-0.5f, 18f, 50.5f, degrees);
			lg1_mat = UtilAngles.pivotPixelsRotX(41.5f, 24f, -24.5f, degrees);
			lg2_mat = UtilAngles.pivotPixelsRotX(-42.5f, 24f, -24.5f, degrees);
		}
		// flaps
		QuaternionF rudderRot = Vec3f.YP.rotationDegrees(entity.inputs.yaw*15);
		rudderRot.mul(Vec3f.ZP.rotationDegrees(entity.inputs.yaw*-10));
		Mat4f left_rudder = UtilAngles.pivotPixelsRot(44.7787f, 52.3335f, -127.6889f, rudderRot);
		Mat4f right_rudder = UtilAngles.pivotPixelsRot(-44.7787f, 52.3335f, -127.6889f, rudderRot);
		Mat4f elevator = UtilAngles.pivotPixelsRotX(0, 73.6614f, -140.7049f, entity.inputs.pitch*22);
		Mat4f left_flap, right_flap;
		if (entity.isFlapsDown()) {
			left_flap = UtilAngles.pivotPixelsRotX(98.9282f, 43.4165f, -34.7215f, -22);
			right_flap = UtilAngles.pivotPixelsRotX(-98.9282f, 43.4165f, -34.7215f, -22);
		} else {
			left_flap = UtilAngles.pivotPixelsRotX(98.9282f, 43.4165f, -34.7215f, entity.inputs.roll*-22);
			right_flap = UtilAngles.pivotPixelsRotX(-98.9282f, 43.4165f, -34.7215f, entity.inputs.roll*22);
		}
		// controls
		QuaternionF stickRot = Vec3f.XP.rotationDegrees(entity.inputs.pitch*-25);
		stickRot.mul(Vec3f.ZP.rotationDegrees(entity.inputs.roll*25));
		Mat4f stick = UtilAngles.pivotPixelsRot(0, 26.1123f, 47f, stickRot);
		Mat4f left_pedal = Mat4f.createTranslateMatrix(0, 0, entity.inputs.yaw*-0.0625f);
		Mat4f right_pedal = Mat4f.createTranslateMatrix(0, 0, entity.inputs.yaw*0.0625f);
		Mat4f throttle = Mat4f.createTranslateMatrix(0, 0, entity.getCurrentThrottle()*0.125f);
		ImmutableMap<String, Mat4f> transforms = ImmutableMap.<String, Mat4f>builder()
			.put("blade0", blade0rot_mat)
			.put("blade1", blade1rot_mat)
			.put("lg0", lg0_mat)
			.put("lg1", lg1_mat)
			.put("lg2", lg2_mat)
			.put("surface0", left_flap)
			.put("surface1", right_flap)
			.put("surface2", left_rudder)
			.put("surface3", right_rudder)
			.put("surface4", elevator)
			.put("stick", stick)
			.put("pedal0", left_pedal)
			.put("pedal1", right_pedal)
			.put("throttle", throttle)
			.build();
		return Transforms.of(transforms);
	}
	
	private static final Vec3f PIVOT = new Vec3f(0, -2f, 2f);
	
	@Override
	public Vec3f getGlobalPivot() {
		return PIVOT;
	}

}
