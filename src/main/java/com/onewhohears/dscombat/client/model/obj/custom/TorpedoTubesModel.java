package com.onewhohears.dscombat.client.model.obj.custom;

import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class TorpedoTubesModel extends ObjTurretModel<EntityTurret> {

	public TorpedoTubesModel() {
		super("torpedo_tubes");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityTurret entity, float partialTicks) {
		/*float xrothead = UtilAngles.lerpAngle(partialTicks, entity.xRotRelO, entity.getRelRotX());
		Matrix4f xrothead_mat = UtilAngles.pivotPixelsRotX(0, 36.6f, -10.2f, xrothead);
		ImmutableMap<String, Matrix4f> transforms = ImmutableMap.<String, Matrix4f>builder()
			.put("head", xrothead_mat)
			.build();
		return Transforms.of(transforms);*/
		return Transforms.EMPTY;
	}

}
