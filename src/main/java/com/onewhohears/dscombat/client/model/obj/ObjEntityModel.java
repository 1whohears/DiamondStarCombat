package com.onewhohears.dscombat.client.model.obj;

import javax.annotation.Nullable;

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
	
	public ObjEntityModel(String modelId) {
		this.modelId = modelId;
	}
	
	public void render(T entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks) {
		handleGlobalOverrides(entity, partialTicks, poseStack);
		rotate(entity, partialTicks, poseStack);
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
		ModelOverrides o = getModelOverride();
		if (o == null) return;
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
	
	protected Transforms getComponentTransforms(T entity, float partialTicks) {
		return Transforms.EMPTY;
	}
	
	protected ITextureRenderTypeLookup getTextureRenderTypeLookup(T entity) {
		return (texture) -> { return RenderType.entityTranslucent(texture); };
	}
	
	protected int getLight(T entity, int lightmap) {
		return lightmap;
	}
	
	protected int getOverlay(T entity) {
		return OverlayTexture.NO_OVERLAY;
	}
	
	protected Vector3f getGlobalPivot() {
		return Vector3f.ZERO;
	}
	
}
