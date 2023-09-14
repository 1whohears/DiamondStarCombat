package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Matrix4f;
import com.onewhohears.dscombat.client.model.obj.ObjPartModel;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class HeavyMissileRackModel extends ObjPartModel<EntityWeaponRack> {

	public HeavyMissileRackModel() {
		super("heavy_missile_rack");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityWeaponRack entity, float partialTicks) {
		ImmutableMap.Builder<String, Matrix4f> builder = ImmutableMap.<String, Matrix4f>builder();
		int num = entity.getAmmoNum();
		for (int i = num+4; i <= 6; ++i) builder.put("cube"+i, INVISIBLE);
		return Transforms.of(builder.build());
	}

}
