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
public class JaviPlaneModel extends ObjVehicleModel<EntityVehicle> {

	public JaviPlaneModel() {
		super("javi_plane");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityVehicle entity, float partialTicks) {
		// landing gear
		float gearpos = entity.getLandingGearPos(partialTicks);
		Mat4f lg0_mat, lg1_mat, lg2_mat;
		if (gearpos >= 1f) lg0_mat = lg1_mat = lg2_mat = INVISIBLE;
		else {
			float degrees = -gearpos*90;
			lg0_mat = UtilAngles.pivotPixelsRotX(0, 25.5f, 85.5f, degrees);
			lg1_mat = UtilAngles.pivotPixelsRotX(46.5f, 26f, -8.5f, degrees);
			lg2_mat = UtilAngles.pivotPixelsRotX(-47.5f, 26f, -8.5f, degrees);
		}
		// flaps
		Mat4f left_rudder = UtilAngles.pivotPixelsRotY(54.2496f, 52.5873f, -129.8593f, entity.inputs.yaw*15);
		Mat4f right_rudder = UtilAngles.pivotPixelsRotY(-54.2496f, 52.5873f, -129.8593f, entity.inputs.yaw*15);
		Mat4f elevator = UtilAngles.pivotPixelsRotX(0, 41.3725f, -130.5063f, entity.inputs.pitch*22);
		Mat4f left_flap, right_flap;
		if (entity.isFlapsDown()) {
			left_flap = UtilAngles.pivotPixelsRotX(116.5282f, 37.3854f, -14.9395f, -22);
			right_flap = UtilAngles.pivotPixelsRotX(-116.5282f, 37.3854f, -14.9395f, -22);
		} else {
			left_flap = UtilAngles.pivotPixelsRotX(116.5282f, 37.3854f, -14.9395f, entity.inputs.roll*-22);
			right_flap = UtilAngles.pivotPixelsRotX(-116.5282f, 37.3854f, -14.9395f, entity.inputs.roll*22);
		}
		// controls
		QuaternionF stickRot = Vec3f.XP.rotationDegrees(entity.inputs.pitch*-25);
		stickRot.mul(Vec3f.ZP.rotationDegrees(entity.inputs.roll*25));
		Mat4f stick = UtilAngles.pivotPixelsRot(0, 42.1418f, 94.8353f, stickRot);
		Mat4f left_pedal = Mat4f.createTranslateMatrix(0, 0, entity.inputs.yaw*-0.0625f);
		Mat4f right_pedal = Mat4f.createTranslateMatrix(0, 0, entity.inputs.yaw*0.0625f);
		Mat4f throttle = Mat4f.createTranslateMatrix(0, 0, entity.getCurrentThrottle()*0.125f);
		ImmutableMap<String, Mat4f> transforms = ImmutableMap.<String, Mat4f>builder()
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
	
	private static final Vec3f PIVOT = new Vec3f(0, -2f, 1f);
	
	@Override
	public Vec3f getGlobalPivot() {
		return PIVOT;
	}

}
