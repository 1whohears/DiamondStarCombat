package com.onewhohears.dscombat.client.model.obj;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjEntityModels.ModelOverrides;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;
import net.minecraftforge.client.model.renderable.CompositeRenderable;
import net.minecraftforge.client.model.renderable.ITextureRenderTypeLookup;

public class ObjEntityModel<T extends Entity> {
	
	public final String modelId;
	
	public ObjEntityModel(String modelId) {
		this.modelId = modelId;
	}
	
	public void render(T entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks) {
		handleOverrides(entity, partialTicks, poseStack);
		rotate(entity, partialTicks, poseStack);
		getModel().render(poseStack, bufferSource, getTextureRenderTypeLookup(), 
				getLight(entity, lightmap), getOverlay(entity), partialTicks, 
				getTransforms(entity, partialTicks));
	}
	
	protected void rotate(T entity, float partialTicks, PoseStack poseStack) {
		poseStack.mulPose(Vector3f.YN.rotationDegrees(entity.getViewYRot(partialTicks)));
		poseStack.mulPose(Vector3f.XP.rotationDegrees(entity.getViewXRot(partialTicks)));
	}
	
	protected void handleOverrides(T entity, float partialTicks, PoseStack poseStack) {
		ModelOverrides o = getModelOverride();
		if (o == null) return;
		if (o.rot180) poseStack.mulPose(Vector3f.YN.rotationDegrees(180));
		poseStack.scale(o.scale * o.scale3d[0], o.scale * o.scale3d[1], o.scale * o.scale3d[2]);
	}
	
	@Nullable
	public CompositeRenderable getModel() {
		return ObjEntityModels.get().getBakedModel(modelId);
	}
	
	@Nullable
	public ModelOverrides getModelOverride() {
		return ObjEntityModels.get().getModelOverride(modelId);
	}
	
	protected Transforms getTransforms(T entity, float partialTicks) {
		return Transforms.EMPTY;
	}
	
	protected ITextureRenderTypeLookup getTextureRenderTypeLookup() {
		return (texture) -> { return RenderType.entityTranslucent(texture); };
	}
	
	protected int getLight(T entity, int lightmap) {
		return lightmap;
	}
	
	protected int getOverlay(T entity) {
		return OverlayTexture.NO_OVERLAY;
	}
	
}