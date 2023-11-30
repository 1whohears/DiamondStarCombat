package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjAircraftModel;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class JaviPlaneModel extends ObjAircraftModel<EntityPlane> {

	public JaviPlaneModel() {
		super("javi_plane");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityPlane entity, float partialTicks) {
		// TODO 0.2 animate javi plane model (flaps, landing gear)
		float gearpos = entity.getLandingGearPos(partialTicks);
		Matrix4f lg0_mat, lg1_mat, lg2_mat;
		if (gearpos >= 1f) lg0_mat = lg1_mat = lg2_mat = INVISIBLE;
		else {
			float degrees = -gearpos*90;
			lg0_mat = UtilAngles.pivotPixelsRotX(0, 25.5f, 85.5f, degrees);
			lg1_mat = UtilAngles.pivotPixelsRotX(46.5f, 26f, -8.5f, degrees);
			lg2_mat = UtilAngles.pivotPixelsRotX(-47.5f, 26f, -8.5f, degrees);
		}
		ImmutableMap<String, Matrix4f> transforms = ImmutableMap.<String, Matrix4f>builder()
			.put("gun", INVISIBLE)
			.put("lg0", lg0_mat)
			.put("lg1", lg1_mat)
			.put("lg2", lg2_mat)
			.build();
		return Transforms.of(transforms);
	}
	
	private static final Vector3f PIVOT = new Vector3f(0, -2f, 0);
	
	@Override
	protected Vector3f getGlobalPivot() {
		return PIVOT;
	}

}
