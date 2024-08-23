package com.onewhohears.dscombat.client.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels.ModelOverrides;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

import com.onewhohears.onewholibs.client.model.obj.ObjEntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.client.model.renderable.CompositeRenderable;

public class ObjWeaponModel<T extends EntityWeapon<?>> extends ObjEntityModel<T> {
	
	protected String weaponModelId = "";
	
	public ObjWeaponModel() {
		super("");
	}
	
	@Override
	public void render(T entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks) {
		weaponModelId = entity.getModelId();
		super.render(entity, poseStack, bufferSource, lightmap, partialTicks);
	}
	
	public CompositeRenderable getModel() {
		return ObjEntityModels.get().getBakedModel(weaponModelId);
	}
	
	public ModelOverrides getModelOverride() {
		return ObjEntityModels.get().getModelOverride(weaponModelId);
	}

}
