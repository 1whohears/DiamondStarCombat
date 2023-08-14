package com.onewhohears.dscombat.client.model.parts;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.model.EntityControllableModel;
import com.onewhohears.dscombat.entity.parts.EntityEngine;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class EntityModelCFM56 extends EntityControllableModel<EntityEngine> {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "cfm56"), "main");
	private final ModelPart body;
	
	public EntityModelCFM56(ModelPart root) {
		this.body = root.getChild("body");
	}
	
	@Override
	public void renderToBuffer(EntityEngine entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer, 
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0, 1.50, 0);
		poseStack.scale(1.0F, -1.0F, 1.0F);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 11).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(22, 22).addBox(5.0F, 1.0F, 0.0F, 1.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, 11.0F, 0.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 22).addBox(-6.0F, 1.0F, 0.0F, 1.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(30, 11).addBox(-5.0F, 1.0F, -6.0F, 10.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(22, 42).addBox(-5.0F, 2.0F, -6.0F, 1.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(30, 0).addBox(-5.0F, 10.0F, -6.0F, 10.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(38, 36).addBox(4.0F, 2.0F, -6.0F, 1.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(36, 18).addBox(-4.0F, 2.0F, -10.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 42).addBox(-5.0F, 1.0F, 4.0F, 10.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

}
