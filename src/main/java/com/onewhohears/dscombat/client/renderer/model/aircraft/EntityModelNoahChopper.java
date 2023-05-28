package com.onewhohears.dscombat.client.renderer.model.aircraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.renderer.model.EntityControllableModel;
import com.onewhohears.dscombat.entity.aircraft.EntityHelicopter;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class EntityModelNoahChopper extends EntityControllableModel<EntityHelicopter> {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "noah_chopper"), "main");
	private final ModelPart body;
	private final ModelPart top_rotor;
	private final ModelPart tail_rotor;

	public EntityModelNoahChopper(ModelPart root) {
		this.body = root.getChild("body");
		this.top_rotor = body.getChild("bladetop");
		this.tail_rotor = body.getChild("tail").getChild("bladetail");
	}
	
	@Override
	public void renderToBuffer(EntityHelicopter entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer,
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0, 1.5, 0);
		poseStack.scale(1.0F, -1.0F, 1.0F);
		top_rotor.yRot = entity.getPropellerRotation(partialTicks);
		tail_rotor.xRot = entity.getPropellerRotation(partialTicks);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 34.0F, 12.0F));

		PartDefinition frame = body.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(128, 62).addBox(-12.0F, 0.0F, -20.0F, 24.0F, 1.0F, 40.0F, new CubeDeformation(0.0F))
		.texOffs(64, 11).addBox(-12.0F, -10.0F, 19.0F, 24.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(40, 43).addBox(-12.0F, -10.0F, -19.0F, 1.0F, 10.0F, 38.0F, new CubeDeformation(0.0F))
		.texOffs(64, 0).addBox(-12.0F, -10.0F, -20.0F, 24.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(11.0F, -10.0F, -19.0F, 1.0F, 10.0F, 38.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -33.0F, -20.0F, 16.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-8.0F, -32.0F, -20.0F, 16.0F, 22.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(72, 126).addBox(10.0F, -1.0F, -19.0F, 1.0F, 1.0F, 38.0F, new CubeDeformation(0.0F))
		.texOffs(0, 81).addBox(-11.0F, -1.0F, -19.0F, 1.0F, 1.0F, 38.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = frame.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(68, 33).addBox(7.0F, -16.0F, 19.0F, 4.0F, 18.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -39.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition cube_r2 = frame.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(68, 52).addBox(-11.0F, -16.0F, 19.0F, 4.0F, 18.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -39.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition cube_r3 = frame.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 126).addBox(-0.5F, -23.0F, -1.0F, 1.0F, 23.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.5F, -10.0F, -19.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition cube_r4 = frame.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(128, 0).addBox(-0.5F, -23.0F, -1.0F, 1.0F, 23.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.5F, -10.0F, -19.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition cube_r5 = frame.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -24.0F, -0.5F, 2.0F, 24.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 19.5F, 0.3491F, 0.0F, 0.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 126).addBox(-4.0F, -20.0F, -76.0F, 8.0F, 8.0F, 56.0F, new CubeDeformation(0.0F))
		.texOffs(40, 33).addBox(-0.5F, -32.0F, -76.0F, 1.0F, 12.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.5F, -12.0F, -76.0F, 1.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bladetail = tail.addOrReplaceChild("bladetail", CubeListBuilder.create().texOffs(0, 4).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(112, 126).addBox(-3.0F, -1.0F, -16.0F, 1.0F, 2.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(62, 33).addBox(-3.0F, -16.0F, -1.0F, 1.0F, 32.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -16.0F, -54.0F));

		PartDefinition gear = body.addOrReplaceChild("gear", CubeListBuilder.create().texOffs(128, 126).addBox(-16.0F, 10.0F, -30.0F, 2.0F, 2.0F, 60.0F, new CubeDeformation(0.0F))
		.texOffs(128, 0).addBox(14.0F, 10.0F, -30.0F, 2.0F, 2.0F, 60.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r6 = gear.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(40, 55).addBox(-2.0F, -11.0F, 23.5F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(48, 55).addBox(-2.0F, -11.0F, -0.5F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 10.0F, -11.5F, 0.0F, 0.0F, 0.3491F));

		PartDefinition cube_r7 = gear.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 56).addBox(0.0F, -11.0F, -0.5F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 56).addBox(0.0F, -11.0F, 23.5F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 10.0F, -11.5F, 0.0F, 0.0F, -0.3491F));

		PartDefinition bladetop = body.addOrReplaceChild("bladetop", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -3.0F, -60.0F, 4.0F, 1.0F, 120.0F, new CubeDeformation(0.0F))
		.texOffs(0, 121).addBox(-60.0F, -3.0F, -2.0F, 120.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -33.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

}
