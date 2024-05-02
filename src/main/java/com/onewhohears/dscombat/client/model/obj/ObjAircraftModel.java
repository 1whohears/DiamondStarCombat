package com.onewhohears.dscombat.client.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.renderer.RendererEntityAircraft;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.client.model.renderable.ITextureRenderTypeLookup;

public class ObjAircraftModel<T extends EntityVehicle> extends ObjEntityModel<T> {
	
	public ObjAircraftModel(String modelId) {
		super(modelId);
	}
	
	@Override
	protected ITextureRenderTypeLookup getTextureRenderTypeLookup(T entity) {
		return (texture) -> RendererEntityAircraft.getCullBaseRenderType(entity.textureManager.getDynamicTexture());
	}
	
	@Override
	public void render(T entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks) {
		// apparently this slightly different render order is needed or else the plane's render position gets messed up
		handleGlobalOverrides(entity, partialTicks, poseStack);
		rotate(entity, partialTicks, poseStack);
		getModel().render(poseStack, bufferSource, getTextureRenderTypeLookup(entity), 
				getLight(entity, lightmap), getOverlay(entity), partialTicks, 
				getComponentTransforms(entity, partialTicks));
	}
	
	@Override
	protected void rotate(T entity, float partialTicks, PoseStack poseStack) {
		Quaternion q = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());
        Vector3f pivot = getGlobalPivot();
		if (!UtilGeometry.isZero(pivot)) poseStack.mulPoseMatrix(UtilAngles.pivotInvRot(pivot, q));
		else poseStack.mulPose(q);
	}
	
	@Override
	protected int getLight(T entity, int lightmap) {
		if (!entity.isOperational()) return 1;
		return lightmap;
	}

}
