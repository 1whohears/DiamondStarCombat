package com.onewhohears.dscombat.client.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.Config;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels.ModelOverrides;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;

import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.renderable.CompositeRenderable;
import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class ObjWeaponRackModel<T extends EntityWeaponRack> extends ObjPartModel<T> {
	
	public static final int maxRenderedRackWeaponNum = 30;
	public static int renderedRackWeaponNum = 0;
	
	protected final int maxAmmoNum;
	protected final Vec3[] weapon_pos;
	private String prevWeaponModelId = "";
	private CompositeRenderable prevModel;
	private ModelOverrides prevMO;
	
	public ObjWeaponRackModel(String modelId, int maxAmmoNum, Vec3[] weapon_pos) {
		super(modelId);
		this.maxAmmoNum = maxAmmoNum;
		this.weapon_pos = weapon_pos;
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
			Vec3 v = weapon_pos[i];
			renderWeapon(entity, poseStack, bufferSource, lightmap, partialTicks, model, mo, v.x, v.y, v.z);
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
		model.render(poseStack, bufferSource, RenderType::entitySolid,
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
