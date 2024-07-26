package com.onewhohears.dscombat.client.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjEntityModels.ModelOverrides;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.model.renderable.CompositeRenderable;
import net.minecraftforge.client.model.renderable.CompositeRenderable.Transforms;
import net.minecraftforge.client.model.renderable.ITextureRenderTypeLookup;

public class ObjEntityModel<T extends Entity> {
	
	public static final Matrix4f INVISIBLE = Matrix4f.createScaleMatrix(0, 0, 0);
	
	public final String modelId;
	
	private CompositeRenderable model;
	private ModelOverrides modelOverride;
	
	public ObjEntityModel(String modelId) {
		this.modelId = modelId;
	}
	
	public void render(T entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks) {
		rotate(entity, partialTicks, poseStack);
		handleGlobalOverrides(entity, partialTicks, poseStack);
		getModel().render(poseStack, bufferSource, getTextureRenderTypeLookup(entity), 
				getLight(entity, lightmap), getOverlay(entity), partialTicks, 
				getComponentTransforms(entity, partialTicks));
	}
	
	protected void rotate(T entity, float partialTicks, PoseStack poseStack) {
		Vector3f pivot = getGlobalPivot();
		Quaternion yRot = Vector3f.YN.rotationDegrees(entity.getViewYRot(partialTicks));
		Quaternion xRot = Vector3f.XP.rotationDegrees(entity.getViewXRot(partialTicks));
		if (!UtilGeometry.isZero(pivot)) {
			poseStack.mulPoseMatrix(UtilAngles.pivotInvRot(pivot, yRot));
			poseStack.mulPoseMatrix(UtilAngles.pivotInvRot(pivot, xRot));
		} else {
			poseStack.mulPose(yRot);
			poseStack.mulPose(xRot);
		}
	}
	
	protected void handleGlobalOverrides(T entity, float partialTicks, PoseStack poseStack) {
		Vector3f pivot = getGlobalPivot();
		if (!UtilGeometry.isZero(pivot)) poseStack.translate(pivot.x(), pivot.y(), pivot.z());
		getModelOverride().applyNoTranslate(poseStack);
	}
	
	public CompositeRenderable getModel() {
		if (model == null) model = ObjEntityModels.get().getBakedModel(modelId);
		return model;
	}
	
	public ModelOverrides getModelOverride() {
		if (modelOverride == null) modelOverride = ObjEntityModels.get().getModelOverride(modelId);
		return modelOverride;
	}
	
	protected Transforms getComponentTransforms(T entity, float partialTicks) {
		return Transforms.EMPTY;
	}
	
	protected ITextureRenderTypeLookup getTextureRenderTypeLookup(T entity) {
		return (texture) -> RenderType.entityTranslucent(texture);
	}
	
	protected int getLight(T entity, int lightmap) {
		return lightmap;
	}
	
	protected int getOverlay(T entity) {
		return OverlayTexture.NO_OVERLAY;
	}
	
	public Vector3f getGlobalPivot() {
		return getModelOverride().translate;
	}
	
}
