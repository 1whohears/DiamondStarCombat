package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.client.model.obj.ObjModelHandler;
import com.onewhohears.onewholibs.client.model.obj.ObjModelHandlerImpl;
import com.onewhohears.onewholibs.client.renderer.RendererCustomAnimObjProjectileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;

public class RendererObjWeapon<T extends EntityWeapon<?>> extends RendererCustomAnimObjProjectileEntity<T> {

	private static final String TRACER_MODEL_ID = "traccer";

	public RendererObjWeapon(Context ctx) {
		super(ctx);
	}

	@Override
	public boolean shouldRender(T entity, net.minecraft.client.renderer.culling.Frustum frustum, 
	                           double camX, double camY, double camZ) {
		// Render bullets at extreme distances (even beyond chunk loading)
		// This allows tracers to be visible from very far away
		return true; // Always render if entity exists, ignore distance checks
	}

	@Override
	public void render(T entity, float yaw, float partialTicks,
	                   PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		// Check if model should be rendered
		if (entity.getWeaponStats().shouldRenderModel()) {
			super.render(entity, yaw, partialTicks, poseStack, bufferSource, packedLight);
		}
		
		// Render tracer for bullets (independent of model rendering)
		if (entity.getWeaponStats().isBullet()) {
			renderTracer(entity, partialTicks, poseStack, bufferSource, packedLight);
		}
	}
	
	private void renderTracer(T entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		Vec3 motion = entity.getDeltaMovement();
		double speed = motion.length();
		
		if (speed < 0.1) return; // Don't render tracer for slow/stopped bullets
		
		// Get tracer model
		ObjModelHandler modelHandler = ObjEntityModels.get().getObjModelHandler(TRACER_MODEL_ID);
		if (modelHandler == null) return;
		
		// Calculate distance from camera for LOD
		Vec3 cameraPos = this.entityRenderDispatcher.camera.getPosition();
		double distanceToCamera = entity.position().distanceTo(cameraPos);
		
		poseStack.pushPose();
		
		// Use entity's rotation (yaw/pitch) instead of velocity direction
		// This is the INITIAL firing direction and doesn't change
		float entityYaw = entity.getYRot();
		float entityPitch = entity.getXRot();
		
		// Rotate to align with entity's rotation (firing direction)
		poseStack.mulPose(Axis.YP.rotationDegrees(-entityYaw));
		poseStack.mulPose(Axis.XP.rotationDegrees(entityPitch));
		
		// Rotate 180 degrees to face forward
		poseStack.mulPose(Axis.YP.rotationDegrees(180f));
		
		// LOD System: Scale increases with distance for better visibility
		// Base scale at close range - reduced for smaller tracer
		float baseScale = 0.4f; // Smaller base scale
		
		// Distance-based scale multiplier - more conservative scaling
		// Close range (0-100 blocks): 1x
		// Medium range (100-500 blocks): 1x-2.5x
		// Long range (500-2000 blocks): 2.5x-6x
		// Extreme range (2000+ blocks): 6x-12x
		float distanceScale = 1.0f;
		if (distanceToCamera > 100) {
			if (distanceToCamera < 500) {
				// Medium range: linear scale from 1x to 2.5x
				distanceScale = 1.0f + ((float)(distanceToCamera - 100) / 400.0f) * 1.5f;
			} else if (distanceToCamera < 2000) {
				// Long range: linear scale from 2.5x to 6x
				distanceScale = 2.5f + ((float)(distanceToCamera - 500) / 1500.0f) * 3.5f;
			} else {
				// Extreme range: linear scale from 6x to 12x (capped at 5000 blocks)
				float extremeDist = Math.min((float)distanceToCamera, 5000f);
				distanceScale = 6.0f + ((extremeDist - 2000f) / 3000.0f) * 6.0f;
			}
		}
		
		float finalScale = baseScale * distanceScale;
		poseStack.scale(finalScale, finalScale, finalScale);
		
		// Render OBJ model with full brightness and translucent for glow effect
		if (modelHandler instanceof ObjModelHandlerImpl impl) {
			impl.render(
				poseStack, 
				bufferSource, 
				partialTicks, 
				15728880, // Full brightness
				OverlayTexture.NO_OVERLAY,
				Collections.emptyMap(),  // transforms
				RenderType::entityTranslucentEmissive,  // Emissive render type for glow
				Collections.emptyMap()  // renderData
			);
		}
		
		poseStack.popPose();
	}
}
