package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.Vec3f;
import com.onewhohears.dscombat.client.model.obj.ObjVehicleModel;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

@Deprecated
public class FelixPlaneModel extends ObjVehicleModel<EntityVehicle> {

	public FelixPlaneModel() {
		super("felix_plane");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityVehicle entity, float partialTicks) {
		// landing gear
		float gearpos = entity.getLandingGearPos(partialTicks);
		Mat4f lg0_mat, lg1_mat, lg2_mat;
		if (gearpos >= 1f) lg0_mat = lg1_mat = lg2_mat = INVISIBLE;
		else {
			float degrees = gearpos*90;
			lg0_mat = UtilAngles.pivotPixelsRotX(0, 19.2001f, 44.0178f, degrees);
			lg1_mat = UtilAngles.pivotPixelsRotZ(27.9838f, 26.168f, -47.1885f, -degrees);
			lg2_mat = UtilAngles.pivotPixelsRotZ(-27.9838f, 26.168f, -47.1885f, degrees);
		}
		// flaps
		QuaternionF rudderRot = Vec3f.YP.rotationDegrees(entity.inputs.yaw*15);
		rudderRot.mul(Vec3f.ZP.rotationDegrees(entity.inputs.yaw*-15));
		Mat4f rudder = UtilAngles.pivotPixelsRot(0, 59.4055f, -145.1443f, rudderRot);
		QuaternionF leftEleRot = Vec3f.XP.rotationDegrees(entity.inputs.pitch*22);
		leftEleRot.mul(Vec3f.ZP.rotationDegrees(entity.inputs.pitch*-16));
		leftEleRot.mul(Vec3f.YP.rotationDegrees(entity.inputs.pitch*-2.5f));
		Mat4f left_elevator = UtilAngles.pivotPixelsRot(21.9134f, 31.5469f, -151.6768f, leftEleRot);
		QuaternionF rightEleRot = Vec3f.XP.rotationDegrees(entity.inputs.pitch*22);
		rightEleRot.mul(Vec3f.ZP.rotationDegrees(entity.inputs.pitch*16));
		rightEleRot.mul(Vec3f.YP.rotationDegrees(entity.inputs.pitch*2.5f));
		Mat4f right_elevator = UtilAngles.pivotPixelsRot(-21.9134f, 31.5469f, -151.6768f, rightEleRot);
		Mat4f left_flap, right_flap;
		if (entity.isFlapsDown()) {
			left_flap = UtilAngles.pivotPixelsRotX(53.4288f, 27.5388f, -77.9424f, -22);
			right_flap = UtilAngles.pivotPixelsRotX(-53.4288f, 27.5388f, -77.9424f, -22);
		} else {
			left_flap = UtilAngles.pivotPixelsRotX(53.4288f, 27.5388f, -77.9424f, entity.inputs.roll*-22);
			right_flap = UtilAngles.pivotPixelsRotX(-53.4288f, 27.5388f, -77.9424f, entity.inputs.roll*22);
		}
		// controls
		QuaternionF stickRot = Vec3f.XP.rotationDegrees(entity.inputs.pitch*-25);
		stickRot.mul(Vec3f.ZP.rotationDegrees(entity.inputs.roll*25));
		Mat4f stick = UtilAngles.pivotPixelsRot(0, 24.1638f, 35.2462f, stickRot);
		Mat4f left_pedal = Mat4f.createTranslateMatrix(0, 0, entity.inputs.yaw*-0.0625f);
		Mat4f right_pedal = Mat4f.createTranslateMatrix(0, 0, entity.inputs.yaw*0.0625f);
		Mat4f throttle = Mat4f.createTranslateMatrix(0, 0, entity.getCurrentThrottle()*0.1875f);
		ImmutableMap<String, Mat4f> transforms = ImmutableMap.<String, Mat4f>builder()
			.put("gun", INVISIBLE)
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
	
	private static final Vec3f PIVOT = new Vec3f(0, -2f, 3f);
	
	@Override
	public Vec3f getGlobalPivot() {
		return PIVOT;
	}

}
