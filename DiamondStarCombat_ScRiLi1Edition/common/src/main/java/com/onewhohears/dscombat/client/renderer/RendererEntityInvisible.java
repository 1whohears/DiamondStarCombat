package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class RendererEntityInvisible<T extends Entity> extends EntityRenderer<T> {

	public RendererEntityInvisible(Context ctx) {
		super(ctx);
	}

	@Override
	public ResourceLocation getTextureLocation(T pEntity) {
		return null;
	}

	@Override
	public boolean shouldRender(T pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
		return false;
	}

	@Override
	public void render(T pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
	}
	
}
