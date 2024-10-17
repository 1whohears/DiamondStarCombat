package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;

import com.onewhohears.dscombat.client.model.obj.ObjVehicleModel;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Deprecated
public class JaviPlaneModel extends ObjVehicleModel<EntityVehicle> {

	public JaviPlaneModel() {
		super("javi_plane");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityVehicle entity, float partialTicks) {
		// landing gear
		float gearpos = entity.getLandingGearPos(partialTicks);
		Matrix4f lg0_mat, lg1_mat, lg2_mat;
		if (gearpos >= 1f) lg0_mat = lg1_mat = lg2_mat = INVISIBLE;
		else {
			float degrees = -gearpos*90;
			lg0_mat = UtilAngles.pivotPixelsRotX(0, 25.5f, 85.5f, degrees);
			lg1_mat = UtilAngles.pivotPixelsRotX(46.5f, 26f, -8.5f, degrees);
			lg2_mat = UtilAngles.pivotPixelsRotX(-47.5f, 26f, -8.5f, degrees);
		}
		// flaps
		Matrix4f left_rudder = UtilAngles.pivotPixelsRotY(54.2496f, 52.5873f, -129.8593f, entity.inputs.yaw*15);
		Matrix4f right_rudder = UtilAngles.pivotPixelsRotY(-54.2496f, 52.5873f, -129.8593f, entity.inputs.yaw*15);
		Matrix4f elevator = UtilAngles.pivotPixelsRotX(0, 41.3725f, -130.5063f, entity.inputs.pitch*22);
		Matrix4f left_flap, right_flap;
		if (entity.isFlapsDown()) {
			left_flap = UtilAngles.pivotPixelsRotX(116.5282f, 37.3854f, -14.9395f, -22);
			right_flap = UtilAngles.pivotPixelsRotX(-116.5282f, 37.3854f, -14.9395f, -22);
		} else {
			left_flap = UtilAngles.pivotPixelsRotX(116.5282f, 37.3854f, -14.9395f, entity.inputs.roll*-22);
			right_flap = UtilAngles.pivotPixelsRotX(-116.5282f, 37.3854f, -14.9395f, entity.inputs.roll*22);
		}
		// controls
		Quaternionf stickRot = new Quaternionf().rotateX((float) Math.toRadians(entity.inputs.pitch * -25));
		stickRot.mul(new Quaternionf().rotateZ((float) Math.toRadians(entity.inputs.roll * 25)));
		Matrix4f stick = UtilAngles.pivotPixelsRot(0, 42.1418f, 94.8353f, stickRot);
		Matrix4f left_pedal = new Matrix4f().identity().translate(0, 0, entity.inputs.yaw * -0.0625f);
		Matrix4f right_pedal = new Matrix4f().identity().translate(0, 0, entity.inputs.yaw * 0.0625f);
		Matrix4f throttle = new Matrix4f().identity().translate(0, 0, entity.getCurrentThrottle() * 0.125f);
		ImmutableMap<String, Matrix4f> transforms = ImmutableMap.<String, Matrix4f>builder()
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
	
	private static final Vector3f PIVOT = new Vector3f(0, -2f, 1f);
	
	@Override
	public Vector3f getGlobalPivot() {
		return PIVOT;
	}

}
