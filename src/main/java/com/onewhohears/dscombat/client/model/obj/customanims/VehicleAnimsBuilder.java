package com.onewhohears.dscombat.client.model.obj.customanims;

import com.google.gson.JsonObject;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.customanims.VehicleModelTransforms.InputAxis;
import com.onewhohears.onewholibs.client.model.obj.customanims.CustomAnimsBuilder;
import com.onewhohears.onewholibs.client.model.obj.customanims.EntityModelTransform.RotationAxis;
import com.onewhohears.onewholibs.util.UtilParse;

public class VehicleAnimsBuilder extends CustomAnimsBuilder {
	
	public static VehicleAnimsBuilder create() {
		return new VehicleAnimsBuilder();
	}
	
	protected VehicleAnimsBuilder() {
	}
	
	public VehicleAnimsBuilder addMotorRotPixelAnim(String model_part_key, float pivotX, float pivotY, float pivotZ, RotationAxis rot_axis, float rot_rate) {
		JsonObject anim = createAnimJson(model_part_key);
		anim.addProperty("anim_id", "motor_rotation");
		fillAxisRotationPixelParams(anim, pivotX, pivotY, pivotZ, rot_axis);
		anim.addProperty("rot_rate", rot_rate);
		return this;
	}
	
	public VehicleAnimsBuilder addWheelRotPixelAnim(String model_part_key, float pivotX, float pivotY, float pivotZ, RotationAxis rot_axis, float rot_rate) {
		JsonObject anim = createAnimJson(model_part_key);
		anim.addProperty("anim_id", "wheel_rotation");
		fillAxisRotationPixelParams(anim, pivotX, pivotY, pivotZ, rot_axis);
		anim.addProperty("rot_rate", rot_rate);
		return this;
	}
	
	public VehicleAnimsBuilder addInputBoundRotPixelAnim(String model_part_key, float pivotX, float pivotY, float pivotZ, RotationAxis rot_axis,
														 InputAxis input_axis, float bound) {
		JsonObject anim = createAnimJson(model_part_key);
		anim.addProperty("anim_id", "input_bound_rotation");
		fillAxisRotationPixelParams(anim, pivotX, pivotY, pivotZ, rot_axis);
		UtilParse.writeEnum(anim, "input_axis", input_axis);
		anim.addProperty("bound", bound);
		return this;
	}
	
	public VehicleAnimsBuilder addPlaneFlapRotPixelAnim(String model_part_key, float pivotX, float pivotY, float pivotZ, RotationAxis rot_axis,
														InputAxis input_axis, float bound) {
		JsonObject anim = createAnimJson(model_part_key);
		anim.addProperty("anim_id", "plane_flap_rotation");
		fillAxisRotationPixelParams(anim, pivotX, pivotY, pivotZ, rot_axis);
		UtilParse.writeEnum(anim, "input_axis", input_axis);
		anim.addProperty("bound", bound);
		return this;
	}
	
	public VehicleAnimsBuilder addJoystickPixelAnim(String model_part_key, float pivotX, float pivotY, float pivotZ, float boundX, float boundZ) {
		addInputBoundRotPixelAnim(model_part_key, pivotX, pivotY, pivotZ, RotationAxis.X, InputAxis.PITCH, -boundX);
		addInputBoundRotPixelAnim(model_part_key, pivotX, pivotY, pivotZ, RotationAxis.Z, InputAxis.ROLL, boundZ);
		return this;
	}
	
	public VehicleAnimsBuilder addSpinningRadarPixelAnim(String model_part_key, float pivotX, float pivotY, float pivotZ, RotationAxis rot_axis,
														 float rot_rate, String radar_id) {
		JsonObject anim = createAnimJson(model_part_key);
		anim.addProperty("anim_id", "spinning_radar");
		fillAxisRotationPixelParams(anim, pivotX, pivotY, pivotZ, rot_axis);
		anim.addProperty("rot_rate", rot_rate);
		anim.addProperty("radar_id", radar_id);
		return this;
	}
	
	public VehicleAnimsBuilder addLandingGearPixelAnim(String model_part_key, float pivotX, float pivotY, float pivotZ, RotationAxis rot_axis, float fold_angle) {
		JsonObject anim = createAnimJson(model_part_key);
		anim.addProperty("anim_id", "landing_gear");
		fillAxisRotationPixelParams(anim, pivotX, pivotY, pivotZ, rot_axis);
		anim.addProperty("fold_angle", fold_angle);
		return this;
	}
	
	public VehicleAnimsBuilder addHitboxDestroyPartAnim(String hitbox_name, String model_part_key) {
		JsonObject anim = createAnimJson(model_part_key);
		anim.addProperty("anim_id", "hitbox_destroy_part");
		anim.addProperty("hitbox_name", hitbox_name);
		return this;
	}
	
	public VehicleAnimsBuilder addHitboxDestroyPartsAnim(String hitbox_name, String... model_part_keys) {
		for (int i = 0; i < model_part_keys.length; ++i) 
			addHitboxDestroyPartAnim(hitbox_name, model_part_keys[i]);
		return this;
	}
	
	public VehicleAnimsBuilder addInputBoundTransAnim(String model_part_key, float boundX, float boundY, float boundZ, InputAxis input_axis) {
		JsonObject anim = createAnimJson(model_part_key);
		anim.addProperty("anim_id", "input_bound_translation");
		UtilParse.writeEnum(anim, "input_axis", input_axis);
		UtilParse.writeVec3f(anim, "bounds", new Vector3f(boundX, boundY, boundZ));
		return this;
	}
	
}
