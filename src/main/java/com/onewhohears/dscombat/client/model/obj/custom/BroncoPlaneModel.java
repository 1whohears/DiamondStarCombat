package com.onewhohears.dscombat.client.model.obj.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
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
		float rot_rate = entity.vehicleData.spinRate;
		float bladerot = UtilAngles.lerpAngle(partialTicks, entity.tickCount*rot_rate, (entity.tickCount+1)*rot_rate);
		Matrix4f blade0rot_mat = UtilAngles.pivotPixelsRotZ(44.5f, 31.5f, 11f, bladerot);
		Matrix4f blade1rot_mat = UtilAngles.pivotPixelsRotZ(-44.5f, 31.5f, 11f, bladerot);
		ImmutableMap<String, Matrix4f> transforms = ImmutableMap.<String, Matrix4f>builder()
			.put("rocket", INVISIBLE)
			.put("blade0", blade0rot_mat)
			.put("blade1", blade1rot_mat)
			.build();
		return Transforms.of(transforms);
	}
	
	@Override
	protected void handleGlobalOverrides(EntityPlane entity, float partialTicks, PoseStack poseStack) {
		super.handleGlobalOverrides(entity, partialTicks, poseStack);
		poseStack.translate(0, -2, 0);
	}

}
