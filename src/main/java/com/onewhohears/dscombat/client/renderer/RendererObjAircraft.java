package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

public class RendererObjAircraft<T extends EntityAircraft> extends RendererObjEntity<T> {

	public RendererObjAircraft(Context ctx, String modelId) {
		super(ctx, modelId);
	}
	
	@Override
	protected void rotate(T entity, float partialTicks, PoseStack poseStack) {
		Quaternion q = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());
        poseStack.mulPose(q);
	}
	
	@Override
	protected int getLight(T entity, int lightmap) {
		if (!entity.isOperational()) return 1;
		return lightmap;
	}

}
