package com.onewhohears.dscombat.client.model.obj.customanims;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.customanims.VehicleModelTransform.InputAxis;
import com.onewhohears.dscombat.client.model.obj.customanims.VehicleModelTransform.RotationAxis;
import com.onewhohears.dscombat.util.UtilParse;

public class CustomAnimsBuilder {
	
	public static CustomAnimsBuilder create() {
		return new CustomAnimsBuilder();
	}
	
	private final JsonArray anims = new JsonArray();
	
	private CustomAnimsBuilder() {
	}
	
	public JsonArray build() {
		return anims;
	}
	
	private JsonObject createAnimJson(String model_part_key) {
		JsonObject anim = new JsonObject();
		anim.addProperty("model_part_key", model_part_key);
		anims.add(anim);
		return anim;
	}
	
	private void fillAxisRotationParams(JsonObject anim, float pivotX, float pivotY, float pivotZ, RotationAxis rot_axis) {
		UtilParse.writeVec3f(anim, "pivot", new Vector3f(pivotX, pivotY, pivotZ));
		UtilParse.writeEnum(anim, "rot_axis", rot_axis);
	}
	
	public CustomAnimsBuilder addContinuousRotAnim(String model_part_key, float pivotX, float pivotY, float pivotZ, RotationAxis rot_axis, float rot_rate) {
		JsonObject anim = createAnimJson(model_part_key);
		anim.addProperty("anim_id", "continuous_rotation");
		fillAxisRotationParams(anim, pivotX, pivotY, pivotZ, rot_axis);
		anim.addProperty("rot_rate", rot_rate);
		return this;
	}
	
	public CustomAnimsBuilder addMotorRotAnim(String model_part_key, float pivotX, float pivotY, float pivotZ, RotationAxis rot_axis, float rot_rate) {
		JsonObject anim = createAnimJson(model_part_key);
		anim.addProperty("anim_id", "motor_rotation");
		fillAxisRotationParams(anim, pivotX, pivotY, pivotZ, rot_axis);
		anim.addProperty("rot_rate", rot_rate);
		return this;
	}
	
	public CustomAnimsBuilder addWheelRotAnim(String model_part_key, float pivotX, float pivotY, float pivotZ, RotationAxis rot_axis, float rot_rate) {
		JsonObject anim = createAnimJson(model_part_key);
		anim.addProperty("anim_id", "wheel_rotation");
		fillAxisRotationParams(anim, pivotX, pivotY, pivotZ, rot_axis);
		anim.addProperty("rot_rate", rot_rate);
		return this;
	}
	
	public CustomAnimsBuilder addInputBoundRotAnim(String model_part_key, float pivotX, float pivotY, float pivotZ, RotationAxis rot_axis, 
			InputAxis input_axis, float bound) {
		JsonObject anim = createAnimJson(model_part_key);
		anim.addProperty("anim_id", "input_bound_rotation");
		fillAxisRotationParams(anim, pivotX, pivotY, pivotZ, rot_axis);
		UtilParse.writeEnum(anim, "input_axis", input_axis);
		anim.addProperty("bound", bound);
		return this;
	}
	
	public CustomAnimsBuilder addPlaneFlapRotAnim(String model_part_key, float pivotX, float pivotY, float pivotZ, RotationAxis rot_axis, 
			InputAxis input_axis, float bound) {
		JsonObject anim = createAnimJson(model_part_key);
		anim.addProperty("anim_id", "plane_flap_rotation");
		fillAxisRotationParams(anim, pivotX, pivotY, pivotZ, rot_axis);
		UtilParse.writeEnum(anim, "input_axis", input_axis);
		anim.addProperty("bound", bound);
		return this;
	}
	
	public CustomAnimsBuilder addJoystickAnim(String model_part_key, float pivotX, float pivotY, float pivotZ, float boundX, float boundZ) {
		addInputBoundRotAnim(model_part_key, pivotX, pivotY, pivotZ, RotationAxis.X, InputAxis.PITCH, -boundX);
		addInputBoundRotAnim(model_part_key, pivotX, pivotY, pivotZ, RotationAxis.Z, InputAxis.ROLL, boundZ);
		return this;
	}
	
	public CustomAnimsBuilder addSpinningRadarAnim(String model_part_key, float pivotX, float pivotY, float pivotZ, RotationAxis rot_axis, 
			float rot_rate, String radar_id) {
		JsonObject anim = createAnimJson(model_part_key);
		anim.addProperty("anim_id", "spinning_radar");
		fillAxisRotationParams(anim, pivotX, pivotY, pivotZ, rot_axis);
		anim.addProperty("rot_rate", rot_rate);
		anim.addProperty("radar_id", radar_id);
		return this;
	}
	
	public CustomAnimsBuilder addLandingGearAnim(String model_part_key, float pivotX, float pivotY, float pivotZ, RotationAxis rot_axis, float fold_angle) {
		JsonObject anim = createAnimJson(model_part_key);
		anim.addProperty("anim_id", "landing_gear");
		fillAxisRotationParams(anim, pivotX, pivotY, pivotZ, rot_axis);
		anim.addProperty("fold_angle", fold_angle);
		return this;
	}
	
	public CustomAnimsBuilder addHitboxDestroyPartAnim(String hitbox_name, String model_part_key) {
		JsonObject anim = createAnimJson(model_part_key);
		anim.addProperty("anim_id", "hitbox_destroy_part");
		anim.addProperty("hitbox_name", hitbox_name);
		return this;
	}
	
	public CustomAnimsBuilder addHitboxDestroyPartsAnim(String hitbox_name, String... model_part_keys) {
		for (int i = 0; i < model_part_keys.length; ++i) 
			addHitboxDestroyPartAnim(hitbox_name, model_part_keys[i]);
		return this;
	}
	
	public CustomAnimsBuilder addInputBoundTransAnim(String model_part_key, float boundX, float boundY, float boundZ, InputAxis input_axis) {
		JsonObject anim = createAnimJson(model_part_key);
		anim.addProperty("anim_id", "input_bound_translation");
		UtilParse.writeEnum(anim, "input_axis", input_axis);
		UtilParse.writeVec3f(anim, "bounds", new Vector3f(boundX, boundY, boundZ));
		return this;
	}
	
}
