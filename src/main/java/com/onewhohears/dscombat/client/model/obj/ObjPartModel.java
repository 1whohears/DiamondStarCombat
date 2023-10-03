package com.onewhohears.dscombat.client.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntityVehiclePart;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.renderer.MultiBufferSource;

public class ObjPartModel<T extends EntityVehiclePart> extends ObjEntityModel<T> {
	
	public ObjPartModel(String modelId) {
		super(modelId);
	}
	
	@Override
	public void render(T entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks) {
		if (entity.shouldRender()) super.render(entity, poseStack, bufferSource, lightmap, partialTicks);
	}
	
	@Override
	protected void rotate(T entity, float partialTicks, PoseStack poseStack) {
		Quaternion q = UtilAngles.lerpQ(partialTicks, entity.getParent().getPrevQ(), entity.getParent().getClientQ());
		poseStack.mulPose(q);
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(entity.getZRot()));
	}
	
	@Override
	protected int getLight(T entity, int lightmap) {
		if (entity.getParent().isOperational()) return 1;
		return lightmap;
	}

}
