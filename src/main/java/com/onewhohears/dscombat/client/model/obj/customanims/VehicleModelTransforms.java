package com.onewhohears.dscombat.client.model.obj.customanims;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.onewhohears.minigames.util.UtilParse;

public class VehicleModelTransforms {
	
	private static Map<String, AnimationFactory> map = new HashMap<>();
	
	static {
		map.put("continuous_rotation", (json) -> new VehicleModelTransform.ContinuousRotation(json));
		map.put("motor_rotation", (json) -> new VehicleModelTransform.MotorRotation(json));
		map.put("wheel_rotation", (json) -> new VehicleModelTransform.WheelRotation(json));
		map.put("input_bound_rotation", (json) -> new VehicleModelTransform.InputBoundRotation(json));
		map.put("spinning_radar", (json) -> new VehicleModelTransform.SpinningRadar(json));
		map.put("landing_gear", (json) -> new VehicleModelTransform.LandingGear(json));
	}
	
	@Nullable
	public static VehicleModelTransform get(JsonObject json) {
		String anim_id = UtilParse.getStringSafe(json, "anim_id", "");
		if (anim_id.isEmpty() || !map.containsKey(anim_id)) return null;
		return map.get(anim_id).create(json);
	}
	
	public interface AnimationFactory {
		VehicleModelTransform create(JsonObject json);
	}
}
