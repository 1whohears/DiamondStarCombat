package com.onewhohears.dscombat.client.model.obj;

import com.google.gson.JsonArray;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.KeyframeAnimsEntityModel;

public class ObjWeaponModel<T extends EntityWeapon<?>> extends KeyframeAnimsEntityModel<T> {

	public ObjWeaponModel(String modelId) {
		super(modelId);
	}

	public ObjWeaponModel(String modelId, String... animDataIds) {
		super(modelId, animDataIds);
	}

	public ObjWeaponModel(String modelId, JsonArray transforms, String... animDataIds) {
		super(modelId, transforms, animDataIds);
	}

}
