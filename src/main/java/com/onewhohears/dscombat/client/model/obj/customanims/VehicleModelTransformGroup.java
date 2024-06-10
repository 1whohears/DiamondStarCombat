package com.onewhohears.dscombat.client.model.obj.customanims;

import java.util.ArrayList;
import java.util.List;

import com.mojang.math.Matrix4f;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

public class VehicleModelTransformGroup extends VehicleModelTransform {
	
	private final List<VehicleModelTransform> transforms = new ArrayList<>();
	
	public VehicleModelTransformGroup(String model_part_key, VehicleModelTransform t1, VehicleModelTransform t2) {
		super(model_part_key);
		transforms.add(t1);
		transforms.add(t2);
	}
	
	@Override
	public void addTransform(VehicleModelTransform transform) {
		transforms.add(transform);
	}

	@Override
	public Matrix4f getTransform(EntityVehicle entity, float partialTicks) {
		Matrix4f trans = transforms.get(0).getTransform(entity, partialTicks).copy();
		for (int i = 1; i < transforms.size(); ++i) 
			trans.multiply(transforms.get(i).getTransform(entity, partialTicks));
		return trans;
	}
	
	@Override
	public boolean isGroup() {
		return true;
	}

}
