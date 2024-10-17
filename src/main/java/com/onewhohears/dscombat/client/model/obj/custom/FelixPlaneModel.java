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
public class FelixPlaneModel extends ObjVehicleModel<EntityVehicle> {

	public FelixPlaneModel() {
		super("felix_plane");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityVehicle entity, float partialTicks) {
		// landing gear
		float gearpos = entity.getLandingGearPos(partialTicks);
		Matrix4f lg0_mat, lg1_mat, lg2_mat;
		if (gearpos >= 1f) lg0_mat = lg1_mat = lg2_mat = INVISIBLE;
		else {
			float degrees = gearpos*90;
			lg0_mat = UtilAngles.pivotPixelsRotX(0, 19.2001f, 44.0178f, degrees);
			lg1_mat = UtilAngles.pivotPixelsRotZ(27.9838f, 26.168f, -47.1885f, -degrees);
			lg2_mat = UtilAngles.pivotPixelsRotZ(-27.9838f, 26.168f, -47.1885f, degrees);
		}
		// flaps
		Quaternionf rudderRot = new Quaternionf().rotateY((float) Math.toRadians(entity.inputs.yaw * 15));
		rudderRot.mul(new Quaternionf().rotateZ((float) Math.toRadians(entity.inputs.yaw * -15)));
		Matrix4f rudder = UtilAngles.pivotPixelsRot(0, 59.4055f, -145.1443f, rudderRot);
		Quaternionf leftEleRot = new Quaternionf().rotateX((float) Math.toRadians(entity.inputs.pitch * 22));
		leftEleRot.mul(new Quaternionf().rotateZ((float) Math.toRadians(entity.inputs.pitch * -16)));
		leftEleRot.mul(new Quaternionf().rotateY((float) Math.toRadians(entity.inputs.pitch * -2.5f)));
		Matrix4f left_elevator = UtilAngles.pivotPixelsRot(21.9134f, 31.5469f, -151.6768f, leftEleRot);
		Quaternionf rightEleRot = new Quaternionf().rotateX((float) Math.toRadians(entity.inputs.pitch * 22));
		rightEleRot.mul(new Quaternionf().rotateZ((float) Math.toRadians(entity.inputs.pitch * 16)));
		rightEleRot.mul(new Quaternionf().rotateY((float) Math.toRadians(entity.inputs.pitch * 2.5f)));
		Matrix4f right_elevator = UtilAngles.pivotPixelsRot(-21.9134f, 31.5469f, -151.6768f, rightEleRot);
		Matrix4f left_flap, right_flap;
		if (entity.isFlapsDown()) {
			left_flap = UtilAngles.pivotPixelsRotX(53.4288f, 27.5388f, -77.9424f, -22);
			right_flap = UtilAngles.pivotPixelsRotX(-53.4288f, 27.5388f, -77.9424f, -22);
		} else {
			left_flap = UtilAngles.pivotPixelsRotX(53.4288f, 27.5388f, -77.9424f, entity.inputs.roll*-22);
			right_flap = UtilAngles.pivotPixelsRotX(-53.4288f, 27.5388f, -77.9424f, entity.inputs.roll*22);
		}
		// controls
		Quaternionf stickRot = new Quaternionf().rotateX((float) Math.toRadians(entity.inputs.pitch * -25));
		stickRot.mul(new Quaternionf().rotateZ((float) Math.toRadians(entity.inputs.roll * 25)));
		Matrix4f stick = UtilAngles.pivotPixelsRot(0, 24.1638f, 35.2462f, stickRot);

		Matrix4f left_pedal = new Matrix4f().identity().translate(0, 0, entity.inputs.yaw * -0.0625f);
		Matrix4f right_pedal = new Matrix4f().identity().translate(0, 0, entity.inputs.yaw * 0.0625f);
		Matrix4f throttle = new Matrix4f().identity().translate(0, 0, entity.getCurrentThrottle() * 0.1875f);
		ImmutableMap<String, Matrix4f> transforms = ImmutableMap.<String, Matrix4f>builder()
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
	
	private static final Vector3f PIVOT = new Vector3f(0, -2f, 3f);
	
	@Override
	public Vector3f getGlobalPivot() {
		return PIVOT;
	}

}
