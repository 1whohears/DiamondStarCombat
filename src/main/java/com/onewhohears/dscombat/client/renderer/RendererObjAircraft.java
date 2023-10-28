package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.model.obj.ObjAircraftModel;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

public class RendererObjAircraft<T extends EntityVehicle> extends RendererObjEntity<T> implements RotableHitboxRenderer, OverlayOnVehicleRenderer<T> {

	public RendererObjAircraft(Context ctx, ObjAircraftModel<T> model) {
		super(ctx, model);
	}
	
	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		super.render(entity, yaw, partialTicks, poseStack, bufferSource, packedLight);
		if (shouldDrawRotableHitboxes(entity)) drawRotableHitboxeOutlines(entity, partialTicks, poseStack, bufferSource);
		renderOverlayComponents(entity, poseStack, bufferSource, packedLight, partialTicks);
	}

}
