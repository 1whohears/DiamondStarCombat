package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.dscombat.data.vehicle.EntityScreenData;
import com.onewhohears.dscombat.data.vehicle.VehicleDecalManager.DecalData;
import com.onewhohears.dscombat.data.weapon.MuzzleSmokeData;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.client.renderer.RendererCustomAnimObjEntity;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.slf4j.Logger;

import java.util.List;

public class RendererObjVehicle extends RendererCustomAnimObjEntity<EntityVehicle> implements RotableHitboxRenderer, VehicleScreenRenderer<EntityVehicle> {
	
	private static final Logger LOGGER = LogUtils.getLogger();
	
	public RendererObjVehicle(Context ctx) {
		super(ctx);
	}

	@Override
	public void render(EntityVehicle entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		super.render(entity, yaw, partialTicks, poseStack, bufferSource, packedLight);
		if (shouldDrawRotableHitboxes(entity)) drawRotableHitboxeOutlines(entity, partialTicks, poseStack, bufferSource);
		if (shouldRenderScreens(entity)) renderVehicleScreens(entity, poseStack, bufferSource, packedLight, partialTicks);
		
		// Render parts debug info
		PartsDebugRenderer.renderPartsDebug(entity, partialTicks, poseStack, bufferSource, packedLight);
		
		// Render afterburner flames
		if (entity.showAfterBurnerParticles()) {
			renderAfterburnerFlames(entity, partialTicks, poseStack, bufferSource, packedLight);
		}
		
		// Render crawler tracks for tanks
		if (entity.isTank()) {
			poseStack.pushPose();
			QuaternionF q = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());
			poseStack.mulPose(q.convert());
			
			// Try to render animated tracks (McHeliCE style), fallback to texture if not available
			boolean objTracksRendered = renderCrawlerTracks(entity, poseStack, bufferSource, packedLight, partialTicks);
			if (!objTracksRendered) {
				TrackTextureRenderer.renderTrackTextures(entity, poseStack, bufferSource, packedLight, partialTicks);
			}
			
			poseStack.popPose();
		}
		renderDecals(entity, partialTicks, poseStack, bufferSource, packedLight);
	}

	/**
	 * Renders OBJ-based crawler tracks with McHeliCE-style animation
	 * @return true if crawler tracks were rendered, false if not available
	 */
	private boolean renderCrawlerTracks(EntityVehicle entity, PoseStack poseStack, 
	                                   MultiBufferSource bufferSource, int packedLight, float partialTicks) {
		var paths = entity.getStats().getCrawlerTrackPaths();
		if (paths.isEmpty()) return false;

		var model = entity.getAssets().getModel();
		if (!(model instanceof com.onewhohears.dscombat.client.model.obj.ObjVehicleModel)) return false;

		@SuppressWarnings("unchecked")
		var typedModel = (com.onewhohears.dscombat.client.model.obj.ObjVehicleModel<EntityVehicle>) model;

		if (!typedModel.hasTracks()) {
			typedModel.initTracks(paths, entity);
			if (!typedModel.hasTracks()) {
				return false;
			}
		}

		// Pass entity and bufferSource to access rotCrawlerTrack for animation
		typedModel.renderTracks(poseStack, packedLight, paths, partialTicks, entity, bufferSource);
		return true;
	}

	protected void renderDecals(EntityVehicle entity, float partialTicks, PoseStack poseStack,
								MultiBufferSource bufferSource, int packedLight) {
		List<DecalData> decals = entity.decalManager.getDecals();
		if (decals.isEmpty()) return;

		QuaternionF q = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());

		for (DecalData decal : decals) {
			poseStack.pushPose();
			// Apply vehicle rotation
			poseStack.mulPose(q.convert());
			// Translate to decal local position
			poseStack.translate(decal.x, decal.y, decal.z);
			// Apply decal rotation (XYZ euler in degrees)
			poseStack.mulPose(new Quaternionf().rotationXYZ(
					(float) Math.toRadians(decal.rotX),
					(float) Math.toRadians(decal.rotY),
					(float) Math.toRadians(decal.rotZ)));
			// Scale
			float s = decal.scale * 0.01f; // font units are large, scale down
			poseStack.scale(s, -s, s);
			// Render text centered
			var font = Minecraft.getInstance().font;
			int tw = font.width(decal.text);
			font.drawInBatch(decal.text, -tw / 2f, 0, decal.color, false,
					poseStack.last().pose(), bufferSource, net.minecraft.client.gui.Font.DisplayMode.NORMAL,
					0, packedLight);
			poseStack.popPose();
		}
	}
	
	@Override
	public void renderVehicleScreens(EntityVehicle vehicle, PoseStack poseStack, 
			MultiBufferSource buffer, int packedLight, float partialTicks) {
		poseStack.pushPose();
		
		QuaternionF q = UtilAngles.lerpQ(partialTicks, vehicle.getPrevQ(), vehicle.getClientQ());
        poseStack.mulPose(q.convert());
        
        for (EntityScreenData screen : getScreens(vehicle)) {
        	renderScreen(vehicle, screen.instanceId, screen.type, 
        			poseStack, buffer, partialTicks, packedLight, 
        			screen.rel_pos, screen.width, screen.height, 
        			screen.xRot, screen.yRot, screen.zRot);
        }
		
		poseStack.popPose();
	}
	
	@Override
	public boolean shouldRenderScreens(EntityVehicle vehicle) {
		if (getScreens(vehicle).isEmpty()) return false;
		Minecraft m = Minecraft.getInstance();
		if (m.player == null) return false;
		Entity seat = vehicle.getPilotSeat();
		if (seat == null) return false;
		return m.player.distanceToSqr(seat) < 64;
	}
	
	protected List<EntityScreenData> getScreens(EntityVehicle vehicle) {
		return vehicle.getAssets().getScreens();
	}
	
	/**
	 * Renders 3D afterburner flames from engine nozzles
	 */
	protected void renderAfterburnerFlames(EntityVehicle entity, float partialTicks, 
	                                      PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		QuaternionF q = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());
		
		for (net.minecraft.world.phys.Vec3 relPos : entity.getAfterBurnerSmokePos()) {
			poseStack.pushPose();
			poseStack.mulPose(q.convert());
			poseStack.translate(relPos.x, relPos.y, relPos.z);
			AfterburnerFlameRenderer.renderFlame(poseStack, bufferSource, packedLight, partialTicks, entity);
			poseStack.popPose();
		}
	}
}
