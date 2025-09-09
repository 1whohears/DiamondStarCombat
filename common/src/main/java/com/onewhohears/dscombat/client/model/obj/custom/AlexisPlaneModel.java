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
public class AlexisPlaneModel extends ObjVehicleModel<EntityVehicle> {

	public AlexisPlaneModel() {
		super("alexis_plane");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityVehicle entity, float partialTicks) {
		// landing gear
		float gearpos = entity.getLandingGearPos(partialTicks);
		Mat4f lg0_mat, lg1_mat, lg2_mat;
		if (gearpos >= 1f) lg0_mat = lg1_mat = lg2_mat = INVISIBLE;
		else {
			float degrees = gearpos*90;
			lg0_mat = UtilAngles.pivotPixelsRotX(0, 17.9256f, 45.5465f, degrees);
			QuaternionF lg1Rot = Vec3f.XN.rotationDegrees(degrees);
			QuaternionF lg2Rot = lg1Rot.copy();
			lg1Rot.mul(Vec3f.ZP.rotationDegrees(gearpos*-50));
			lg2Rot.mul(Vec3f.ZP.rotationDegrees(gearpos*50));
			lg1_mat = UtilAngles.pivotPixelsRot(10.4f, 23.4316f, -34.5632f, lg1Rot);
			lg2_mat = UtilAngles.pivotPixelsRot(-10.4f, 23.4316f, -34.5632f, lg2Rot);
		}
		// flaps
		QuaternionF rudderRot = Vec3f.YP.rotationDegrees(entity.inputs.yaw*15);
		rudderRot.mul(Vec3f.ZP.rotationDegrees(entity.inputs.yaw*-15));
		Mat4f rudder = UtilAngles.pivotPixelsRot(0, 81.0642f, -125.9015f, rudderRot);
		Mat4f left_elevator = UtilAngles.pivotPixelsRotX(21.7721f, 38.2332f, -119.5741f, entity.inputs.pitch*22);
		Mat4f right_elevator = UtilAngles.pivotPixelsRotX(-21.7721f, 38.2332f, -119.5741f, entity.inputs.pitch*22);
		Mat4f left_flap, right_flap;
		if (entity.isFlapsDown()) {
			left_flap = UtilAngles.pivotPixelsRotX(49.8276f, 38.2332f, -58.5647f, -22);
			right_flap = UtilAngles.pivotPixelsRotX(-49.8276f, 38.2332f, -58.5647f, -22);
		} else {
			left_flap = UtilAngles.pivotPixelsRotX(49.8276f, 38.2332f, -58.5647f, entity.inputs.roll*-22);
			right_flap = UtilAngles.pivotPixelsRotX(-49.8276f, 38.2332f, -58.5647f, entity.inputs.roll*22);
		}
		// controls
		QuaternionF stickRot = Vec3f.XP.rotationDegrees(entity.inputs.pitch*-25);
		stickRot.mul(Vec3f.ZP.rotationDegrees(entity.inputs.roll*25));
		Mat4f stick = UtilAngles.pivotPixelsRot(-7.1778f, 40.7333f, 78.8995f, stickRot);
		Mat4f left_pedal = Mat4f.createTranslateMatrix(0, 0, entity.inputs.yaw*-0.0625f);
		Mat4f right_pedal = Mat4f.createTranslateMatrix(0, 0, entity.inputs.yaw*0.0625f);
		Mat4f throttle = Mat4f.createTranslateMatrix(0, 0, entity.getCurrentThrottle()*0.1875f);
		ImmutableMap<String, Mat4f> transforms = ImmutableMap.<String, Mat4f>builder()
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
	
	private static final Vec3f PIVOT = new Vec3f(0, -2f, 2f);
	
	@Override
	public Vec3f getGlobalPivot() {
		return PIVOT;
	}

}
