package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjPartModel;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class VLSModel extends ObjPartModel<EntityWeaponRack> {

	public VLSModel() {
		super("vls");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityWeaponRack entity, float partialTicks) {
		ImmutableMap.Builder<String, Matrix4f> builder = ImmutableMap.<String, Matrix4f>builder();
		int num = entity.getAmmoNum();
		for (int i = num+1; i <= 16; ++i) builder.put("m"+i, INVISIBLE);
		return Transforms.of(builder.build());
	}
	
	private static final Vector3f PIVOT = new Vector3f(0, 1.2f, 0);
	
	@Override
	protected Vector3f getGlobalPivot() {
		return PIVOT;
	}

}
