package com.onewhohears.dscombat.client.model.obj.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.Vec3f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.Function;

public class MLRSModel extends ObjTurretModel<EntityTurret> {

	public static final ResourceLocation DESERT_TEXTURE = new ResourceLocation(
			"dscombat:textures/entity/turret/mlrs1.png");
	public static final ResourceLocation WOODLAND_TEXTURE = new ResourceLocation(
			"dscombat:textures/entity/turret/mlrs2.png");

	public MLRSModel() {
		super("mlrs", true);
	}

	@Override
	protected void addComponentTransforms(Map<String, Mat4f> transforms, EntityTurret entity, float partialTicks) {
		float xrothead = entity.getViewXRot(partialTicks);
		Mat4f xrothead_mat = UtilAngles.pivotPixelsRotX(0, 8f, 22f, -xrothead);
		transforms.put("RocketLauncher", xrothead_mat);
		super.addComponentTransforms(transforms, entity, partialTicks);
	}

	@Override
	public boolean globalRotateX() {
		return false;
	}

	@Override
	protected Function<ResourceLocation, RenderType> getTextureRenderTypeLookup(EntityTurret entity) {
		return (texture) -> {
			EntityVehicle vehicle = entity.getParentVehicle();
			if (vehicle == null) return RenderType.entityTranslucent(texture);
			if (vehicle.getStats().getId().equals("eric_truck")) {
				if (vehicle.textureManager.getBaseTextureIndex() == 1)
					return RenderType.entityTranslucent(DESERT_TEXTURE);
				else if (vehicle.textureManager.getBaseTextureIndex() == 2)
					return RenderType.entityTranslucent(WOODLAND_TEXTURE);
			}
			return RenderType.entityTranslucent(texture);
		};
	}

	@Override
	protected void rotate(EntityTurret entity, float partialTicks, PoseStack poseStack) {
		super.rotate(entity, partialTicks, poseStack);
		poseStack.mulPose(Vec3f.YP.rotationDegrees(180f).convert());
	}
}
