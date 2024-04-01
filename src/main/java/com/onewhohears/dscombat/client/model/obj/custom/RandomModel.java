package com.onewhohears.dscombat.client.model.obj.custom;

import com.onewhohears.dscombat.client.model.obj.ObjEntityModel;
import com.onewhohears.dscombat.client.model.obj.ObjEntityModels;
import com.onewhohears.dscombat.client.model.obj.ObjEntityModels.ModelOverrides;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.model.renderable.CompositeRenderable;

public class RandomModel extends ObjEntityModel<Entity> {
	
	private CompositeRenderable model;
	private ModelOverrides modelOverride;
	private String randomModelId;
	
	public RandomModel() {
		super("");
	}
	
	@Override
	public CompositeRenderable getModel() {
		if (randomModelId == null) randomModelId = ObjEntityModels.get().getRandomModelId();
		if (model == null) model = ObjEntityModels.get().getBakedModel(randomModelId);
		return model;
	}
	
	@Override
	public ModelOverrides getModelOverride() {
		if (randomModelId == null) randomModelId = ObjEntityModels.get().getRandomModelId();
		if (modelOverride == null) modelOverride = ObjEntityModels.get().getModelOverride(randomModelId);
		return modelOverride;
	}

}
