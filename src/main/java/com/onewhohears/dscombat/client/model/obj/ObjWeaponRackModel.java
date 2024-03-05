package com.onewhohears.dscombat.client.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.model.obj.ObjEntityModels.ModelOverrides;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.client.model.renderable.CompositeRenderable;
import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class ObjWeaponRackModel<T extends EntityWeaponRack> extends ObjPartModel<T> {
	
	protected final int maxAmmoNum;
	protected final float dX, dY;
	
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
		
		//EntityType<?> weaponType = entity.getWeaponEntityType();
		//Minecraft m = Minecraft.getInstance();
		//EntityRenderer<?> weaponRenderer = m.getEntityRenderDispatcher().renderers.get(weaponType);
		
		String weaponModelId = entity.getWeaponModelId();
		CompositeRenderable model = getWeaponModel(weaponModelId);
		poseStack.pushPose();
		getWeaponModelOverride(weaponModelId).apply(poseStack);
		// FIXME 9 missile models are not centered so all the missiles are positioned wacko
		for (int i = 0; i < ammo && i < maxAmmoNum; ++i) {
			float x = i % 2 == 0 ? dX : -dY;
			float y = (i/2 + 1) * dY;
			if (i+1 == maxAmmoNum && maxAmmoNum%2 == 1) x = 0;
			renderWeapon(entity, poseStack, bufferSource, lightmap, partialTicks, model, x, y, 0);
		}
		poseStack.popPose();
	}
	
	protected void renderWeapon(T entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks,
			CompositeRenderable model, double x, double y, double z) {
		poseStack.pushPose();
		poseStack.translate(x, y, z);
		model.render(poseStack, bufferSource, getTextureRenderTypeLookup(entity), 
			getLight(entity, lightmap), getOverlay(entity), partialTicks, Transforms.EMPTY);
		poseStack.popPose();
	}
	
	protected CompositeRenderable getWeaponModel(String modelId) {
		return ObjEntityModels.get().getBakedModel(modelId);
	}
	
	protected ModelOverrides getWeaponModelOverride(String modelId) {
		return ObjEntityModels.get().getModelOverride(modelId);
	}

}
