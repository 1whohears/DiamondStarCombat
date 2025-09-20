package com.onewhohears.dscombat.client.model.obj.custom;

import com.onewhohears.dscombat.client.model.obj.ObjVehicleModel;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.Vec3f;

import java.util.Map;

public class JasonPlaneModel extends ObjVehicleModel<EntityVehicle> {
	
	public JasonPlaneModel() {
		super("jason_plane");
	}
	
	@Override
    protected void addComponentTransforms(Map<String, Mat4f> transforms, EntityVehicle entity, float partialTicks) {
		// blade
		float bladerot = entity.getMotorRotation(partialTicks, 20);
		Mat4f blade0_mat = UtilAngles.pivotPixelsRotZ(-0.0426f, 42.0581f, 82.8989f, bladerot);
		// landing gear
		float gearpos = entity.getLandingGearPos(partialTicks);
		Mat4f lg0_mat, lg1_mat, lg2_mat;
		if (gearpos >= 1f) lg0_mat = lg1_mat = lg2_mat = INVISIBLE;
		else {
			float degrees = gearpos*90;
			lg0_mat = UtilAngles.pivotPixelsRotZ(39.5712f, 28.6068f, 25.0786f, -degrees);
			lg1_mat = UtilAngles.pivotPixelsRotZ(-39.5712f, 28.6068f, 25.0786f, degrees);
			lg2_mat = UtilAngles.pivotPixelsRotX(0, 34.8817f, -71.9732f, degrees);
		}
		// flaps
		Mat4f rudder = UtilAngles.pivotPixelsRotY(0, 55.8305f, -107.139f, entity.inputs.yaw*15);
		Mat4f left_elevator = UtilAngles.pivotPixelsRotX(20.9161f, 50.9701f, -99.3629f, entity.inputs.pitch*22);
		Mat4f right_elevator = UtilAngles.pivotPixelsRotX(-20.9161f, 50.9701f, -99.3629f, entity.inputs.pitch*22);
		Mat4f left_flap, right_flap;
		if (entity.isFlapsDown()) {
			left_flap = UtilAngles.pivotPixelsRotX(94.4596f, 36.7061f, -0.8385f, -22);
			right_flap = UtilAngles.pivotPixelsRotX(-94.4596f, 36.7061f, -0.8385f, -22);
		} else {
			left_flap = UtilAngles.pivotPixelsRotX(94.4596f, 36.7061f, -0.8385f, entity.inputs.roll*-22);
			right_flap = UtilAngles.pivotPixelsRotX(-94.4596f, 36.7061f, -0.8385f, entity.inputs.roll*22);
		}
		// controls
		QuaternionF stickRot = Vec3f.XP.rotationDegrees(entity.inputs.pitch*-25);
		stickRot.mul(Vec3f.ZP.rotationDegrees(entity.inputs.roll*25));
		Mat4f stick = UtilAngles.pivotPixelsRot(0, 33.8882f, 4.717f, stickRot);
		Mat4f throttle = Mat4f.createTranslateMatrix(0, 0, entity.getCurrentThrottle()*0.125f);
		Mat4f left_pedal = Mat4f.createTranslateMatrix(0, 0, entity.inputs.yaw*-0.0625f);
		Mat4f right_pedal = Mat4f.createTranslateMatrix(0, 0, entity.inputs.yaw*0.0625f);
        transforms.put("lg0", lg0_mat);
        transforms.put("lg1", lg1_mat);
        transforms.put("lg2", lg2_mat);
        transforms.put("blade0", blade0_mat);
        transforms.put("surface0", rudder);
        transforms.put("surface1", left_flap);
        transforms.put("surface2", right_flap);
        transforms.put("surface3", left_elevator);
        transforms.put("surface4", right_elevator);
        transforms.put("stick", stick);
        transforms.put("throttle", throttle);
        transforms.put("pedal0", left_pedal);
        transforms.put("pedal1", right_pedal);
	}
	
	private static final Vec3f PIVOT = new Vec3f(0, -2f, 0);
	
	@Override
	public Vec3f getGlobalPivot() {
		return PIVOT;
	}
	
}
