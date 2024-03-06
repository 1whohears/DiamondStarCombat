package com.onewhohears.dscombat.client.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.model.obj.ObjEntityModels.ModelOverrides;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraftforge.client.model.renderable.CompositeRenderable;
import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class ObjWeaponRackModel<T extends EntityWeaponRack> extends ObjPartModel<T> {
	
	protected final int maxAmmoNum;
	protected final float dX, dY;
	private String prevWeaponModelId = "";
	private CompositeRenderable prevModel;
	private ModelOverrides prevMO;
	
	public ObjWeaponRackModel(String modelId, int maxAmmoNum, float dX, float dY) {
		super(modelId);
		this.maxAmmoNum = maxAmmoNum;
		this.dX = dX;
		this.dY = dY;
	}
	
	@Override
	public void render(T entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks) {
		super.render(entity, poseStack, bufferSource, lightmap, partialTicks);
		int ammo = entity.getAmmoNum();
		// FIXME 0 rendering many missile rack models has performance issues. limit the number of missiles to render?
		String weaponModelId = entity.getWeaponModelId();
		CompositeRenderable model;
		ModelOverrides mo;
		if (weaponModelId.equals(prevWeaponModelId)) {
			model = prevModel;
			mo = prevMO;
		} else {
			model = getWeaponModel(weaponModelId);
			mo = getWeaponModelOverride(weaponModelId);
		}
		for (int i = 0; i < ammo && i < maxAmmoNum; ++i) {
			float x = i % 2 == 0 ? dX : -dX;
			float y = (i/2 + 1) * dY - dY*0.5f;
			if (i+1 == maxAmmoNum && maxAmmoNum%2 == 1) x = 0;
			renderWeapon(entity, poseStack, bufferSource, lightmap, partialTicks, model, mo, x, y, 0);
		}
		prevWeaponModelId = weaponModelId;
		prevModel = model;
		prevMO = mo;
	}
	
	protected void renderWeapon(T entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks,
			CompositeRenderable model, ModelOverrides mo, double x, double y, double z) {
		poseStack.pushPose();
		poseStack.translate(x, y, z);
		mo.apply(poseStack);
		model.render(poseStack, bufferSource, (texture) -> RenderType.entitySolid(texture), 
				lightmap, OverlayTexture.NO_OVERLAY, partialTicks, Transforms.EMPTY);
		poseStack.popPose();
	}
	
	protected CompositeRenderable getWeaponModel(String modelId) {
		return ObjEntityModels.get().getBakedModel(modelId);
	}
	
	protected ModelOverrides getWeaponModelOverride(String modelId) {
		return ObjEntityModels.get().getModelOverride(modelId);
	}

}
