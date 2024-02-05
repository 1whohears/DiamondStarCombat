package com.onewhohears.dscombat.client.model.obj.custom;

import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjAircraftModel;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;

public class EdenPlaneModel extends ObjAircraftModel<EntityPlane> {

	public EdenPlaneModel() {
		super("eden_plane");
	}
	
	private static final Vector3f PIVOT = new Vector3f(0, -2f, 3.5f);
	
	@Override
	protected Vector3f getGlobalPivot() {
		return PIVOT;
	}

}
