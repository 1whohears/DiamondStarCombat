package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.entity.weapon.EntityAbstractWeapon;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class RendererEntityAbstractWeapon<T extends EntityAbstractWeapon> extends EntityRenderer<T> {

	public RendererEntityAbstractWeapon(Context ctx) {
		super(ctx);
	}

	@Override
	public ResourceLocation getTextureLocation(T pEntity) {
		return null;
	}
	
	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
		
		super.render(entity, entityYaw, partialTicks, poseStack, multiBufferSource, packedLight);
	}

}
