package com.onewhohears.dscombat.client.model.obj;

import com.google.gson.JsonArray;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.client.model.obj.ObjModelHandlerImpl;
import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.KeyframeAnimsEntityModel;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ObjVehicleModel<T extends EntityVehicle> extends KeyframeAnimsEntityModel<T> {

    public static RenderType getCullBaseRenderType(ResourceLocation texture) {
        // FIXME 1 why are entities sometimes not seen through transparent stuff?
        //return RenderType.entityTranslucent(texture);
        return RenderType.entityTranslucentCull(texture); // until it's fixed, the cockpit glass will be clear
    }

    public static RenderType getBaseRenderType(ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }

    public static RenderType getLayerRenderType(ResourceLocation texture) {
        //return RenderType.entityTranslucent(texture);
        return RenderType.armorCutoutNoCull(texture); // is this faster?
    }

    private final Map<String, Mat4f> transforms = new HashMap<>();

	public ObjVehicleModel(String modelId) {
		this(modelId, new JsonArray());
	}

	public ObjVehicleModel(String modelId, JsonArray anims) {
		super(modelId, anims);
	}

	public ObjVehicleModel(String modelId, JsonArray anims, String... animDataIds) {
		super(modelId, anims, animDataIds);
	}

	public ObjVehicleModel(String modelId, String[] animDataIds) {
		this(modelId, new JsonArray(), animDataIds);
	}

	@Override
	protected Function<ResourceLocation, RenderType> getTextureRenderTypeLookup(T entity) {
		return (texture) -> {
			if (texture == null) return getCullBaseRenderType(entity.textureManager.getBaseTexture());
			String path = texture.getPath();
			ResourceLocation loc = texture;
			if (path.contains("base")) {
				ResourceLocation dyn = entity.textureManager.getDynamicTexture();
				loc = dyn != null ? dyn : entity.textureManager.getBaseTexture();
			} else if (path.contains("extra_")) {
				int index = path.indexOf("extra_");
				String[] extras = path.substring(index).split("_");
				String newLoc = path.substring(0, index) + extras[0] + "_" + extras[1]
						+ "_" + entity.textureManager.getBaseTextureIndex() + ".png";
				loc = new ResourceLocation(texture.getNamespace(), newLoc);
			}
			if (entity.getAssets().isDontCull())
				return getBaseRenderType(loc);
			return getCullBaseRenderType(loc);
		};
	}	
	@Override
	protected void rotate(T entity, float partialTicks, PoseStack poseStack) {
        QuaternionF q = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());
        poseStack.mulPose(q.convert());
	}
	
	@Override
	protected int getLight(T entity, int lightmap) {
		if (!entity.isOperational()) return 1;
		return lightmap;
	}

	// --- Crawler track helpers ---
	// Note: Track rendering is now handled entirely in DSC via CrawlerTrackRenderer
	// These methods are kept for compatibility but delegate to DSC implementation

	@Nullable
	public ObjModelHandlerImpl getHandlerImpl() {
		return getObjModelHandlerImpl();
	}

	public Function<ResourceLocation, RenderType> getTrackRenderTypeLookup(T entity) {
		return RenderType::entityCutoutNoCull;
	}

	public void initTracks(List<?> paths, T entity) {
		// Track initialization is now handled in CrawlerTrackRenderer
		// No need to call onewholibs methods
	}

	public boolean hasTracks() {
		// Tracks are always available if paths are configured
		return true;
	}

	public void tickTrackAnimation(List<?> paths, float speed) {
		// Animation is now handled in EntityVehicle.tickCrawlerTrackAnimation()
	}

	@Override
	protected void addComponentTransforms(Map<String, Mat4f> transforms, T entity, float partialTicks) {
		super.addComponentTransforms(transforms, entity, partialTicks);
		// Hide crawler_track from main model render - it's rendered separately by CrawlerTrackRenderer
		if (entity.getStats().isTank() && !entity.getStats().getCrawlerTrackPaths().isEmpty()) {
			transforms.put("crawler_track", INVISIBLE);
		}
	}
	public void renderTracks(PoseStack poseStack, int packedLight, List<?> paths, 
	                        float partialTicks, T entity, net.minecraft.client.renderer.MultiBufferSource bufferSource) {
		ObjModelHandlerImpl impl = getObjModelHandlerImpl();
		if (impl == null || paths.isEmpty()) return;
		
		// Use DSC's CrawlerTrackRenderer for all track rendering
		com.onewhohears.dscombat.client.renderer.CrawlerTrackRenderer.renderCrawlerTracks(
			entity, poseStack, bufferSource, packedLight, partialTicks, 
			(List<com.onewhohears.dscombat.data.vehicle.CrawlerTrackPath>) paths, impl
		);
	}

}
