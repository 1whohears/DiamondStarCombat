package com.onewhohears.dscombat.client.model.obj.customanims;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.math.Matrix4f;
import com.onewhohears.dscombat.client.model.obj.ObjVehicleModel;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class CustomAnimsVehicleModel extends ObjVehicleModel<EntityVehicle> {
	
	private final List<VehicleModelTransform> transforms = new ArrayList<>();
	
	public CustomAnimsVehicleModel(String model_id, JsonArray anims) {
		super(model_id);
		for (int i = 0; i < anims.size(); ++i) {
			JsonObject anim = anims.get(i).getAsJsonObject();
			VehicleModelTransform t = VehicleModelTransforms.get(anim);
			if (t == null) continue;
			transforms.add(t);
		}
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityVehicle entity, float partialTicks) {
		ImmutableMap.Builder<String, Matrix4f> builder = ImmutableMap.<String, Matrix4f>builder();
		for (VehicleModelTransform trans : transforms) 
			builder.put(trans.getKey(), trans.getTransform(entity, partialTicks));
		return Transforms.of(builder.build());
	}
	
}
