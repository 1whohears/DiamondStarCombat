package com.onewhohears.dscombat.client.model.obj.custom;

import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.renderable.ITextureRenderTypeLookup;

import java.util.Map;

public class AutoloadingTurretModel extends ObjTurretModel<EntityTurret> {

	public static final ResourceLocation DESERT_TEXTURE = new ResourceLocation(
			"dscombat:textures/entity/turret/autoloading_turret_desert.png");

	public AutoloadingTurretModel() {
		super("autoloaderturret", true, "autoloader_turret_shoot");
	}

	@Override
	protected void addComponentTransforms(Map<String, Mat4f> transforms, EntityTurret entity, float partialTicks) {
		float xrothead = entity.getViewXRot(partialTicks);
		Mat4f xrothead_mat = UtilAngles.pivotPixelsRotX(0, 5.9686f, 20.5643f, xrothead);
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
			if (vehicle.getStats().getId().equals("mrbudger_tank")) {
				if (vehicle.textureManager.getBaseTextureIndex() == 1)
					return RenderType.entityTranslucent(DESERT_TEXTURE);
			}
			return RenderType.entityTranslucent(texture);
		};
	}
}
