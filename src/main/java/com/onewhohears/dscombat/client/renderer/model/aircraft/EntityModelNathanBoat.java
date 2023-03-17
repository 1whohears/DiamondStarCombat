package com.onewhohears.dscombat.client.renderer.model.aircraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.renderer.model.EntityControllableModel;
import com.onewhohears.dscombat.entity.aircraft.EntityBoat;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.resources.ResourceLocation;

public class EntityModelNathanBoat<T extends EntityBoat> extends EntityControllableModel<T> {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "nathan_boat"), "main");

	public EntityModelNathanBoat(ModelPart root) {
		
	}
	
	@Override
	public void renderToBuffer(T entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer,
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		// TODO Nathan boat model
		return LayerDefinition.create(meshdefinition, 512, 512);
	}

}
