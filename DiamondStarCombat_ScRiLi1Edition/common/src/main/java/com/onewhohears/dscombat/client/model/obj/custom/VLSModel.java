package com.onewhohears.dscombat.client.model.obj.custom;

import com.onewhohears.dscombat.client.model.obj.ObjPartModel;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.Vec3f;

import java.util.Map;

public class VLSModel extends ObjPartModel<EntityWeaponRack> {

	public VLSModel() {
		super("vls");
	}
	
	@Override
    protected void addComponentTransforms(Map<String, Mat4f> transforms, EntityWeaponRack entity, float partialTicks) {
		int num = entity.getAmmoNum();
		for (int i = num+1; i <= 16; ++i) transforms.put("m"+i, INVISIBLE);
	}
	
	private static final Vec3f PIVOT = new Vec3f(0, 1.2f, 0);
	
	@Override
	public Vec3f getGlobalPivot() {
		return PIVOT;
	}

}
