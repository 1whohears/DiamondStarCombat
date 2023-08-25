package com.onewhohears.dscombat.client.renderer;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.model.obj.ObjEntityModel;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class RendererObjEntity<T extends Entity> extends EntityRenderer<T> {
	
	protected final ObjEntityModel<T> model;
	
	public RendererObjEntity(Context ctx, ObjEntityModel<T> model) {
		super(ctx);
		this.model = model;
	}
	
	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap) {
		poseStack.pushPose();
		model.render(entity, poseStack, bufferSource, lightmap, partialTicks);
		poseStack.popPose();
		super.render(entity, yaw, partialTicks, poseStack, bufferSource, lightmap);
	}
	
	@Nullable
	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return null;
	}

}
