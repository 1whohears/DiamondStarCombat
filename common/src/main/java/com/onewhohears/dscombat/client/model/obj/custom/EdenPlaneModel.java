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
public class EdenPlaneModel extends ObjVehicleModel<EntityVehicle> {

	public EdenPlaneModel() {
		super("eden_plane");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityVehicle entity, float partialTicks) {
		// landing gear
		float gearpos = entity.getLandingGearPos(partialTicks);
		Mat4f lg0_mat, lg1_mat, lg2_mat;
		if (gearpos >= 1f) lg0_mat = lg1_mat = lg2_mat = INVISIBLE;
		else {
			float degrees = -gearpos*90;
			lg0_mat = UtilAngles.pivotPixelsRotX(0, 31.4916f, 29.602f, -degrees);
			lg1_mat = UtilAngles.pivotPixelsRotX(27.3165f, 35.4232f, -55.991f, degrees);
			lg2_mat = UtilAngles.pivotPixelsRotX(-27.3165f, 35.4232f, -55.991f, degrees);
		}
		// flaps
		Mat4f left_rudder = UtilAngles.pivotPixelsRotY(34.24f, 62.9607f, -139.2688f, entity.inputs.yaw*15);
		Mat4f right_rudder = UtilAngles.pivotPixelsRotY(-34.24f, 62.9607f, -139.2688f, entity.inputs.yaw*15);
		Mat4f left_elevator = UtilAngles.pivotPixelsRotX(35.5916f, 40.1339f, -140.6252f, entity.inputs.pitch*22);
		Mat4f right_elevator = UtilAngles.pivotPixelsRotX(-35.5916f, 40.1339f, -140.6252f, entity.inputs.pitch*22);
		Mat4f left_flap, right_flap;
		if (entity.isFlapsDown()) {
			left_flap = UtilAngles.pivotPixelsRotX(85.8748f, 39.8619f, -94.692f, -22);
			right_flap = UtilAngles.pivotPixelsRotX(-85.8748f, 39.8619f, -94.692f, -22);
		} else {
			left_flap = UtilAngles.pivotPixelsRotX(85.8748f, 39.8619f, -94.692f, entity.inputs.roll*-22);
			right_flap = UtilAngles.pivotPixelsRotX(-85.8748f, 39.8619f, -94.692f, entity.inputs.roll*22);
		}
		// controls
		QuaternionF stickRot = Vec3f.XP.rotationDegrees(entity.inputs.pitch*-25);
		stickRot.mul(Vec3f.ZP.rotationDegrees(entity.inputs.roll*25));
		Mat4f stick = UtilAngles.pivotPixelsRot(0, 35.9965f, 89.1234f, stickRot);
		Mat4f left_pedal = Mat4f.createTranslateMatrix(0, 0, entity.inputs.yaw*-0.0625f);
		Mat4f right_pedal = Mat4f.createTranslateMatrix(0, 0, entity.inputs.yaw*0.0625f);
		Mat4f throttle = Mat4f.createTranslateMatrix(0, 0, entity.getCurrentThrottle()*0.1875f);
		ImmutableMap<String, Mat4f> transforms = ImmutableMap.<String, Mat4f>builder()
			.put("lg0", lg0_mat)
			.put("lg1", lg1_mat)
			.put("lg2", lg2_mat)
			.put("surface0", left_rudder)
			.put("surface1", right_rudder)
			.put("surface2", left_flap)
			.put("surface3", right_flap)
			.put("surface4", left_elevator)
			.put("surface5", right_elevator)
			.put("stick", stick)
			.put("pedal0", left_pedal)
			.put("pedal1", right_pedal)
			.put("throttle", throttle)
			.build();
		return Transforms.of(transforms);
	}
	
	private static final Vec3f PIVOT = new Vec3f(0, -2f, 3.5f);
	
	@Override
	public Vec3f getGlobalPivot() {
		return PIVOT;
	}

}
