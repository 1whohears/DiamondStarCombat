package com.onewhohears.dscombat.client.model.obj.custom;

import com.mojang.math.Matrix4f;
import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.renderable.ITextureRenderTypeLookup;

import java.util.Map;

public class ArtilleryCannonModel extends ObjTurretModel<EntityTurret> {

	public static final ResourceLocation DESERT_TEXTURE = new ResourceLocation(
			"dscombat:textures/entity/turret/artillery_cannon_desert.png");
	public static final ResourceLocation WOODLAND_TEXTURE = new ResourceLocation(
			"dscombat:textures/entity/turret/artillery_cannon_woodland.png");

	public ArtilleryCannonModel() {
		super("artillery_cannon", true);
	}

	@Override
	protected void addComponentTransforms(Map<String, Matrix4f> transforms, EntityTurret entity, float partialTicks) {
		float xrothead = entity.getViewXRot(partialTicks);
		Matrix4f xrothead_mat = UtilAngles.pivotPixelsRotX(0, 12.65f, 5.8f, xrothead);
		transforms.put("Gun", xrothead_mat);
		super.addComponentTransforms(transforms, entity, partialTicks);
	}

	@Override
	public boolean globalRotateX() {
		return false;
	}

	@Override
	protected ITextureRenderTypeLookup getTextureRenderTypeLookup(EntityTurret entity) {
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
}
