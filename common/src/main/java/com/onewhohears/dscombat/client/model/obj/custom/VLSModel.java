package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.Vec3f;
import com.onewhohears.dscombat.client.model.obj.ObjPartModel;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class VLSModel extends ObjPartModel<EntityWeaponRack> {

	public VLSModel() {
		super("vls");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityWeaponRack entity, float partialTicks) {
		ImmutableMap.Builder<String, Mat4f> builder = ImmutableMap.<String, Mat4f>builder();
		int num = entity.getAmmoNum();
		for (int i = num+1; i <= 16; ++i) builder.put("m"+i, INVISIBLE);
		return Transforms.of(builder.build());
	}
	
	private static final Vec3f PIVOT = new Vec3f(0, 1.2f, 0);
	
	@Override
	public Vec3f getGlobalPivot() {
		return PIVOT;
	}

}
