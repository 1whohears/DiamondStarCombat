package com.onewhohears.dscombat.client.model.obj.custom;

import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class TorpedoTubesModel extends ObjTurretModel<EntityTurret> {

	public TorpedoTubesModel() {
		super("torpedo_tubes", true);
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityTurret entity, float partialTicks) {
		return Transforms.EMPTY;
	}

}
