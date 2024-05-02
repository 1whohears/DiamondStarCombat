package com.onewhohears.dscombat.client.model.obj.custom;

import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjVehicleModel;
import com.onewhohears.dscombat.entity.vehicle.EntityBoat;

public class CorvetteModel extends ObjVehicleModel<EntityBoat> {

	public CorvetteModel() {
		super("corvette");
	}
	
	private static final Vector3f PIVOT = new Vector3f(0, 1.5f, 0);
	
	@Override
	public Vector3f getGlobalPivot() {
		return PIVOT;
	}

}
