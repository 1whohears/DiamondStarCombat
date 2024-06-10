package com.onewhohears.dscombat.client.model.obj.customanims;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.math.Matrix4f;
import com.onewhohears.dscombat.client.model.obj.ObjVehicleModel;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class CustomAnimsVehicleModel extends ObjVehicleModel<EntityVehicle> {
	
	private final Map<String, VehicleModelTransform> transforms = new HashMap<>();
	
	public CustomAnimsVehicleModel(String model_id, JsonArray anims) {
		super(model_id);
		for (int i = 0; i < anims.size(); ++i) {
			JsonObject anim = anims.get(i).getAsJsonObject();
			VehicleModelTransform t = VehicleModelTransforms.get(anim);
			if (t == null) continue;
			String model_part_key = t.getKey();
			if (transforms.containsKey(model_part_key)) {
				VehicleModelTransform t0 = transforms.get(model_part_key);
				if (t0.isGroup()) t0.addTransform(t);
				else {
					VehicleModelTransformGroup tg = new VehicleModelTransformGroup(model_part_key, t0, t);
					transforms.put(model_part_key, tg);
				}
			} else transforms.put(model_part_key, t);
		}
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityVehicle entity, float partialTicks) {
		ImmutableMap.Builder<String, Matrix4f> builder = ImmutableMap.<String, Matrix4f>builder();
		for (VehicleModelTransform trans : transforms.values()) 
			builder.put(trans.getKey(), trans.getTransform(entity, partialTicks));
		return Transforms.of(builder.build());
	}
	
}
