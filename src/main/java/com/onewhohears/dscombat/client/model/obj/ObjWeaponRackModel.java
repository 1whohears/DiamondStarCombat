package com.onewhohears.dscombat.client.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.Config;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels.ModelOverrides;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;

import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraftforge.client.model.renderable.CompositeRenderable;
import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class ObjWeaponRackModel<T extends EntityWeaponRack> extends ObjPartModel<T> {
	
	public static final int maxRenderedRackWeaponNum = 30;
	public static int renderedRackWeaponNum = 0;
	
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
		if (renderedRackWeaponNum > Config.CLIENT.maxRenderRackMissileNum.get()) return;
		// FIXME 0 rendering many missile rack models has performance issues
		String weaponModelId = entity.getWeaponModelId();
		if (weaponModelId == null || weaponModelId.isEmpty()) return;
		CompositeRenderable model;
		ModelOverrides mo;
		if (weaponModelId.equals(prevWeaponModelId)) {
			model = prevModel;
			mo = prevMO;
		} else {
			model = getWeaponModel(weaponModelId);
			mo = getWeaponModelOverride(weaponModelId);
		}
		int ammo = entity.getAmmoNum();
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
		// it has been tested that RenderType#entitySolid is faster than RenderType#entityTranslucentCull (+10fps on my machine)
		model.render(poseStack, bufferSource, (texture) -> RenderType.entitySolid(texture),
				lightmap, OverlayTexture.NO_OVERLAY, partialTicks, Transforms.EMPTY);
		poseStack.popPose();
		++renderedRackWeaponNum;
	}
	
	protected CompositeRenderable getWeaponModel(String modelId) {
		return ObjEntityModels.get().getBakedModel(modelId);
	}
	
	protected ModelOverrides getWeaponModelOverride(String modelId) {
		return ObjEntityModels.get().getModelOverride(modelId);
	}

}
