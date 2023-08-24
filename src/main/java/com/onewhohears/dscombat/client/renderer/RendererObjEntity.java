package com.onewhohears.dscombat.client.renderer;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjEntityModels;
import com.onewhohears.dscombat.client.model.obj.ObjEntityModels.ModelOverrides;

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
	
	public RendererObjEntity(Context ctx, String modelId) {
		super(ctx);
		this.modelId = modelId;
	}
	
	@Nullable
	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return null;
	}
	
	@Nullable
	public CompositeRenderable getModel() {
		return ObjEntityModels.get().getBakedModel(modelId);
	}
	
	@Nullable
	public ModelOverrides getModelOverride() {
		return ObjEntityModels.get().getModelOverride(modelId);
	}
	
	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap) {
		poseStack.pushPose();
		handleOverrides(entity, partialTicks, poseStack);
		rotate(entity, partialTicks, poseStack);
		
		getModel().render(poseStack, bufferSource, 
				(texture) -> { return RenderType.entityTranslucent(texture); }, 
				getLight(entity, lightmap), OverlayTexture.NO_OVERLAY, partialTicks, 
				getTransforms(entity, partialTicks));
		
		poseStack.popPose();
		super.render(entity, yaw, partialTicks, poseStack, bufferSource, lightmap);
	}
	
	protected void handleOverrides(T entity, float partialTicks, PoseStack poseStack) {
		ModelOverrides o = getModelOverride();
		if (o == null) return;
		if (o.rot180) poseStack.mulPose(Vector3f.YN.rotationDegrees(180));
		poseStack.scale(o.scale * o.scale3d[0], o.scale * o.scale3d[1], o.scale * o.scale3d[2]);
	}
	
	protected void rotate(T entity, float partialTicks, PoseStack poseStack) {
		poseStack.mulPose(Vector3f.YN.rotationDegrees(entity.getViewYRot(partialTicks)));
		poseStack.mulPose(Vector3f.XP.rotationDegrees(entity.getViewXRot(partialTicks)));
	}
	
	protected Transforms getTransforms(T entity, float partialTicks) {
		return Transforms.EMPTY;
	}
	
	protected int getLight(T entity, int lightmap) {
		return lightmap;
	}

}
