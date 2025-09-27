package com.onewhohears.dscombat.client.model.obj.custom;

import com.onewhohears.dscombat.client.model.obj.ObjPartModel;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.onewholibs.util.math.Mat4f;

import java.util.Map;

public class BombRackModel extends ObjPartModel<EntityWeaponRack> {

	public BombRackModel() {
		super("bomb_rack");
	}
	
	@Override
	protected void addComponentTransforms(Map<String, Mat4f> transforms, EntityWeaponRack entity, float partialTicks) {
		int num = entity.getAmmoNum();
		for (int i = num+7; i <= 19; ++i) transforms.put("cube"+i, INVISIBLE);
	}

}
