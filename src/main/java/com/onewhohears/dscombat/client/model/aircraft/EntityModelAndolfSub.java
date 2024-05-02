package com.onewhohears.dscombat.client.model.aircraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.model.EntityControllableModel;
import com.onewhohears.dscombat.entity.vehicle.EntitySubmarine;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class EntityModelAndolfSub extends EntityControllableModel<EntitySubmarine> {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "andolf_sub"), "main");
	private final ModelPart main;
	private final ModelPart propellor;
	
	public EntityModelAndolfSub(ModelPart root) {
		this.main = root.getChild("main");
		this.propellor = main.getChild("propellor");
	}
	
	@Override
	public void renderToBuffer(EntitySubmarine entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer,
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0, 1.55, 0);
		poseStack.scale(-1.0F, -1.0F, 1.0F);
		propellor.zRot = entity.getMotorRotation(partialTicks, 2);
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition frame = main.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 116).addBox(-24.0F, 22.0F, -56.0F, 48.0F, 4.0F, 112.0F, new CubeDeformation(0.0F))
		.texOffs(448, 0).addBox(-8.0F, 30.0F, -40.0F, 16.0F, 4.0F, 80.0F, new CubeDeformation(0.0F))
		.texOffs(160, 324).addBox(-16.0F, 26.0F, -48.0F, 32.0F, 4.0F, 96.0F, new CubeDeformation(0.0F))
		.texOffs(208, 164).addBox(22.0F, -24.0F, -56.0F, 4.0F, 48.0F, 112.0F, new CubeDeformation(0.0F))
		.texOffs(208, 4).addBox(-26.0F, -24.0F, -56.0F, 4.0F, 48.0F, 112.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-24.0F, -26.0F, -56.0F, 48.0F, 4.0F, 112.0F, new CubeDeformation(0.0F))
		.texOffs(0, 232).addBox(-16.0F, -30.0F, -48.0F, 32.0F, 4.0F, 96.0F, new CubeDeformation(0.0F))
		.texOffs(336, 440).addBox(-8.0F, -34.0F, -40.0F, 16.0F, 4.0F, 80.0F, new CubeDeformation(0.0F))
		.texOffs(424, 232).addBox(26.0F, -16.0F, -48.0F, 4.0F, 32.0F, 96.0F, new CubeDeformation(0.0F))
		.texOffs(448, 444).addBox(30.0F, -8.0F, -40.0F, 4.0F, 16.0F, 80.0F, new CubeDeformation(0.0F))
		.texOffs(136, 424).addBox(-30.0F, -16.0F, -48.0F, 4.0F, 32.0F, 96.0F, new CubeDeformation(0.0F))
		.texOffs(448, 116).addBox(-34.0F, -8.0F, -40.0F, 4.0F, 16.0F, 80.0F, new CubeDeformation(0.0F))
		.texOffs(0, 332).addBox(18.0F, 18.0F, -56.0F, 4.0F, 4.0F, 112.0F, new CubeDeformation(0.0F))
		.texOffs(328, 116).addBox(-22.0F, 18.0F, -56.0F, 4.0F, 4.0F, 112.0F, new CubeDeformation(0.0F))
		.texOffs(328, 0).addBox(18.0F, -22.0F, -56.0F, 4.0F, 4.0F, 112.0F, new CubeDeformation(0.0F))
		.texOffs(304, 324).addBox(-22.0F, -22.0F, -56.0F, 4.0F, 4.0F, 112.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-24.0F, -24.0F, -58.0F, 48.0F, 48.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail = main.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 190).addBox(-16.0F, -32.0F, -96.0F, 32.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 86).addBox(-16.0F, 32.0F, -96.0F, 32.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(36, 250).addBox(31.0F, -15.0F, -96.0F, 2.0F, 32.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 250).addBox(-33.0F, -15.0F, -96.0F, 2.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = tail.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 208).addBox(-13.0F, -34.0F, -96.0F, 24.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r2 = tail.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(208, 201).addBox(-13.0F, -35.0F, -96.0F, 24.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.3562F));

		PartDefinition cube_r3 = tail.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 232).addBox(-11.0F, -35.0F, -96.0F, 24.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.3562F));

		PartDefinition cube_r4 = tail.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(160, 232).addBox(-11.0F, -34.0F, -96.0F, 24.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r5 = tail.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(208, 0).addBox(-1.0F, -8.0F, -32.0F, 2.0F, 16.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 1.0F, -58.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r6 = tail.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(208, 48).addBox(-1.0F, -8.0F, -32.0F, 2.0F, 16.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 1.0F, -58.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r7 = tail.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 52).addBox(-8.0F, -1.0F, -32.0F, 16.0F, 2.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, -58.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r8 = tail.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 116).addBox(-8.0F, -1.0F, -32.0F, 16.0F, 2.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, -58.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition window = main.addOrReplaceChild("window", CubeListBuilder.create().texOffs(96, 116).addBox(18.0F, -22.0F, 56.0F, 4.0F, 44.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(96, 52).addBox(-22.0F, -22.0F, 56.0F, 4.0F, 44.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(208, 96).addBox(-18.0F, 18.0F, 56.0F, 36.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 104).addBox(-18.0F, -22.0F, 56.0F, 36.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(208, 164).addBox(-18.0F, -18.0F, 59.0F, 36.0F, 36.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition propellor = main.addOrReplaceChild("propellor", CubeListBuilder.create().texOffs(0, 150).addBox(-4.0F, -4.0F, -32.0F, 8.0F, 8.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -58.0F));

		PartDefinition cube_r9 = propellor.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(48, 150).addBox(-8.0F, -28.0F, -1.0F, 16.0F, 24.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -27.0F, 0.0F, -0.6109F, -1.5708F));

		PartDefinition cube_r10 = propellor.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(240, 232).addBox(-8.0F, -28.0F, -1.0F, 16.0F, 24.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -27.0F, 0.0F, -0.6109F, -3.1416F));

		PartDefinition cube_r11 = propellor.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(244, 0).addBox(-8.0F, -28.0F, -1.0F, 16.0F, 24.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -27.0F, 0.0F, -0.6109F, 1.5708F));

		PartDefinition cube_r12 = propellor.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(244, 48).addBox(-8.0F, -28.0F, -1.0F, 16.0F, 24.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -27.0F, 0.0F, -0.6109F, 0.0F));

		return LayerDefinition.create(meshdefinition, 1024, 1024);
	}

}
