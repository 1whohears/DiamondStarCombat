package com.onewhohears.dscombat.client.model.obj.custom;

import com.onewhohears.onewholibs.util.math.Vec3f;
import com.onewhohears.dscombat.client.model.obj.ObjVehicleModel;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

public class GoogleSubModel extends ObjVehicleModel<EntityVehicle> {
	
	public GoogleSubModel() {
		super("google_sub");
	}
	
	private static final Vec3f PIVOT = new Vec3f(0, -4f, 1f);
	
	@Override
	public Vec3f getGlobalPivot() {
		return PIVOT;
	}

}
