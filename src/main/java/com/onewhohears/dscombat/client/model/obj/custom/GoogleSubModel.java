package com.onewhohears.dscombat.client.model.obj.custom;

import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjAircraftModel;
import com.onewhohears.dscombat.entity.aircraft.EntitySubmarine;

public class GoogleSubModel extends ObjAircraftModel<EntitySubmarine> {
	
	public GoogleSubModel() {
		super("google_sub");
	}
	
	private static final Vector3f PIVOT = new Vector3f(0, -4f, 1f);
	
	@Override
	public Vector3f getGlobalPivot() {
		return PIVOT;
	}

}
