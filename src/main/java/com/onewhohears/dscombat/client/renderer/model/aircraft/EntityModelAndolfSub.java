package com.onewhohears.dscombat.client.renderer.model.aircraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.renderer.model.EntityControllableModel;
import com.onewhohears.dscombat.entity.aircraft.EntitySubmarine;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.resources.ResourceLocation;

public class EntityModelAndolfSub<T extends EntitySubmarine> extends EntityControllableModel<T> {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "andolf_sub"), "main");

	public EntityModelAndolfSub(ModelPart root) {
		
	}
	
	@Override
	public void renderToBuffer(T entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer,
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		// TODO Andolf submarine model
		return LayerDefinition.create(meshdefinition, 512, 512);
	}

}
