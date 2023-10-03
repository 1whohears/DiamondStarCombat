package com.onewhohears.dscombat.client.renderer;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.model.obj.ObjPartModel;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntityVehiclePart;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;

public class RenderVehiclePartsManager {
	
	private static final Map<String, ObjPartModel<EntityVehiclePart>> models = new HashMap<>();
	
	public static void clear() {
		models.clear();
	}
	
	@SuppressWarnings("unchecked")
	public static boolean addModel(String modelId, ObjPartModel<? extends EntityVehiclePart> model) {
		if (models.containsKey(modelId)) return false;
		models.put(modelId, (ObjPartModel<EntityVehiclePart>) model);
		return true;
 	}
	
	public static void renderVehicleParts(EntityAircraft entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks) {
		for (EntityVehiclePart part : entity.getVehicleParts()) {
			if (!part.shouldRender()) continue;
			System.out.println("trying to render "+part);
			ObjPartModel<EntityVehiclePart> model = models.get(part.getModelId());
			if (model == null) {
				System.out.println(part.getModelId()+" isnt here dumb dumb");
				continue;
			}
			Vec3 rel = part.position().subtract(entity.position());
			poseStack.translate(rel.x, rel.y, rel.z);
			model.render(part, poseStack, bufferSource, lightmap, partialTicks);
			poseStack.translate(-rel.x, -rel.y, -rel.z);
		}
	}
	
}
