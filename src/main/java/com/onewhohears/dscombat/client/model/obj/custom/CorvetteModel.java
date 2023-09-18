package com.onewhohears.dscombat.client.model.obj.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.model.obj.ObjAircraftModel;
import com.onewhohears.dscombat.entity.aircraft.EntityBoat;

public class CorvetteModel extends ObjAircraftModel<EntityBoat> {

	public CorvetteModel() {
		super("corvette");
	}
	
	@Override
	protected void handleGlobalOverrides(EntityBoat entity, float partialTicks, PoseStack poseStack) {
		super.handleGlobalOverrides(entity, partialTicks, poseStack);
		poseStack.translate(0, 1.5, 0);
	}

}
