package com.onewhohears.dscombat.client.model.obj;

import com.google.gson.JsonArray;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels.ModelOverrides;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

import com.onewhohears.onewholibs.client.model.obj.ObjEntityModel;
import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.KeyframeAnimsEntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.client.model.renderable.CompositeRenderable;

public class ObjWeaponModel<T extends EntityWeapon<?>> extends KeyframeAnimsEntityModel<T> {

	public ObjWeaponModel(String modelId) {
		super(modelId);
	}

	public ObjWeaponModel(String modelId, String... animDataIds) {
		super(modelId, animDataIds);
	}

	public ObjWeaponModel(String modelId, JsonArray transforms, String... animDataIds) {
		super(modelId, transforms, animDataIds);
	}

}
