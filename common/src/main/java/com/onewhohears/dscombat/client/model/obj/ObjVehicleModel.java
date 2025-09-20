package com.onewhohears.dscombat.client.model.obj;

import com.google.gson.JsonArray;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.KeyframeAnimsEntityModel;
import com.onewhohears.onewholibs.util.math.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
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
			String path = texture.getPath();
			ResourceLocation loc = texture;
			if (path.contains("base")) {
				loc = entity.textureManager.getDynamicTexture();
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
	public void render(T entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks) {
		// apparently this slightly different render order is needed or else the plane's render position gets messed up
		handleGlobalOverrides(entity, partialTicks, poseStack);
		rotate(entity, partialTicks, poseStack);
        transforms.clear();
        addComponentTransforms(transforms, entity, partialTicks);
		getObjModelHandler().render(poseStack, bufferSource, partialTicks,
				getLight(entity, lightmap), getOverlay(entity),
                transforms, getTextureRenderTypeLookup(entity));
	}
	
	@Override
	protected void rotate(T entity, float partialTicks, PoseStack poseStack) {
		QuaternionF q = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());
        Vec3f pivot = getGlobalPivot();
		if (!UtilGeometry.isZero(pivot)) poseStack.mulPoseMatrix(UtilAngles.pivotInvRot(pivot, q).convert());
		else poseStack.mulPose(q.convert());
	}
	
	@Override
	protected int getLight(T entity, int lightmap) {
		if (!entity.isOperational()) return 1;
		return lightmap;
	}

}
