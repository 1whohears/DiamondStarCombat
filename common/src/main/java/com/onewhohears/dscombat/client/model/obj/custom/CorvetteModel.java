package com.onewhohears.dscombat.client.model.obj.custom;

import com.onewhohears.onewholibs.util.math.Vec3f;
import com.onewhohears.dscombat.client.model.obj.ObjVehicleModel;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

public class CorvetteModel extends ObjVehicleModel<EntityVehicle> {

	public CorvetteModel() {
		super("corvette");
	}
	
	private static final Vec3f PIVOT = new Vec3f(0, 1.5f, 0);
	
	@Override
	public Vec3f getGlobalPivot() {
		return PIVOT;
	}

}
