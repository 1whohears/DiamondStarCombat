package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjAircraftModel;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class BroncoPlaneModel extends ObjAircraftModel<EntityPlane> {

	public BroncoPlaneModel() {
		super("bronco-plane");
	}
	
	@Override
	protected Transforms getComponentTransforms(EntityPlane entity, float partialTicks) {
		float bladerot = entity.getPropellerRotation(partialTicks);
		float gearpos = entity.getLandingGearPos(partialTicks);
		Matrix4f blade0rot_mat = UtilAngles.pivotPixelsRotZ(44.5f, 31.5f, 11f, bladerot);
		Matrix4f blade1rot_mat = UtilAngles.pivotPixelsRotZ(-44.5f, 31.5f, 11f, bladerot);
		Matrix4f lg0_mat, lg1_mat, lg2_mat;
		if (gearpos >= 1f) lg0_mat = lg1_mat = lg2_mat = INVISIBLE;
		else {
			float degrees = gearpos*90;
			lg0_mat = UtilAngles.pivotPixelsRotX(-0.5f, 18f, 50.5f, degrees);
			lg1_mat = UtilAngles.pivotPixelsRotX(41.5f, 24f, -24.5f, degrees);
			lg2_mat = UtilAngles.pivotPixelsRotX(-42.5f, 24f, -24.5f, degrees);
		}
		ImmutableMap<String, Matrix4f> transforms = ImmutableMap.<String, Matrix4f>builder()
			.put("rocket", INVISIBLE)
			.put("blade0", blade0rot_mat)
			.put("blade1", blade1rot_mat)
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
