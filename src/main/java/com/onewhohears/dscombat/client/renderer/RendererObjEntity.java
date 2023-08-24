package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.data.model.ObjEntityModels;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.model.renderable.CompositeRenderable;
import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;

public class RendererObjEntity<T extends Entity> extends EntityRenderer<T> {
	
	protected final String modelId;
	protected CompositeRenderable model;
	
	public RendererObjEntity(Context ctx, String modelId) {
		super(ctx);
		this.modelId = modelId;
	}
	
	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return null;
	}
	
	public CompositeRenderable getModel() {
		if (model == null) model = ObjEntityModels.get().getBakedModel(modelId);
		return model;
	}
	
	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap) {
		poseStack.pushPose();
		//poseStack.translate(entity.getX(), entity.getY(), entity.getZ());
		poseStack.mulPose(Vector3f.YN.rotationDegrees(entity.getViewYRot(partialTicks)));
		poseStack.mulPose(Vector3f.XP.rotationDegrees(entity.getViewXRot(partialTicks)));
		
		getModel().render(poseStack, bufferSource, 
				(texture) -> { return RenderType.entityTranslucent(texture); }, 
				lightmap, OverlayTexture.NO_OVERLAY, partialTicks, 
				Transforms.EMPTY);
		
		poseStack.popPose();
		super.render(entity, yaw, partialTicks, poseStack, bufferSource, lightmap);
	}

}
