package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;

import com.onewhohears.dscombat.client.model.obj.ObjPartModel;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;
import org.joml.Matrix4f;
import org.joml.Vector3f;

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
	public Vector3f getGlobalPivot() {
		return PIVOT;
	}

}
