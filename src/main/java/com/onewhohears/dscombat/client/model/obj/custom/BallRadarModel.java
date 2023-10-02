package com.onewhohears.dscombat.client.model.obj.custom;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.client.model.obj.ObjEntityModels.ModelOverrides;
import com.onewhohears.dscombat.client.model.obj.ObjEntityModels;
import com.onewhohears.dscombat.client.model.obj.ObjRadarModel;

import net.minecraftforge.client.model.renderable.CompositeRenderable;

public class BallRadarModel extends ObjRadarModel {

	public BallRadarModel() {
		super("ball_radar");
	}
	
	@Nullable
	@Override
	public CompositeRenderable getModel() {
		return ObjEntityModels.get().getBakedModel(getModelId()); // maybe use ball_radar_triple
	}
	
	@Nullable
	@Override
	public ModelOverrides getModelOverride() {
		return ObjEntityModels.get().getModelOverride(getModelId()); // maybe use ball_radar_triple
	}

}
