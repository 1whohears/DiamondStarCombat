package com.onewhohears.dscombat.client.renderer.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class EntityModelF16<T extends EntityAbstractAircraft> extends EntityModel<T> {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "f16"), "main");
	private final ModelPart body;

	public EntityModelF16(ModelPart root) {
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -12.0F, -32.0F, 24.0F, 24.0F, 64.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition nose = body.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, 29.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 32.0F));

		PartDefinition nose1 = nose.addOrReplaceChild("nose1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition nose11 = nose1.addOrReplaceChild("nose11", CubeListBuilder.create().texOffs(40, 36).addBox(-1.0F, 12.0F, 0.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = nose11.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(36, 18).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.4835F));

		PartDefinition cube_r2 = nose11.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(20, 36).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.3963F));

		PartDefinition cube_r3 = nose11.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(24, 36).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.309F));

		PartDefinition cube_r4 = nose11.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(36, 24).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.2217F));

		PartDefinition cube_r5 = nose11.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(28, 36).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.1345F));

		PartDefinition cube_r6 = nose11.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(36, 30).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition cube_r7 = nose11.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(32, 36).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.9599F));

		PartDefinition cube_r8 = nose11.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(36, 36).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.8727F));

		PartDefinition cube_r9 = nose11.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 39).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r10 = nose11.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(4, 39).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.6981F));

		PartDefinition cube_r11 = nose11.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(8, 39).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.6109F));

		PartDefinition cube_r12 = nose11.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(40, 0).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r13 = nose11.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(40, 6).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r14 = nose11.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(40, 12).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition cube_r15 = nose11.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(40, 18).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition cube_r16 = nose11.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(40, 24).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition cube_r17 = nose11.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(40, 30).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition cube_r18 = nose11.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(16, 36).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition nose12 = nose1.addOrReplaceChild("nose12", CubeListBuilder.create().texOffs(12, 36).addBox(-1.0F, 12.0F, 0.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition cube_r19 = nose12.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(28, 24).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.4835F));

		PartDefinition cube_r20 = nose12.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(12, 30).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.3963F));

		PartDefinition cube_r21 = nose12.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(16, 30).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.309F));

		PartDefinition cube_r22 = nose12.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(20, 30).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.2217F));

		PartDefinition cube_r23 = nose12.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(24, 30).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.1345F));

		PartDefinition cube_r24 = nose12.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(28, 30).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition cube_r25 = nose12.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.9599F));

		PartDefinition cube_r26 = nose12.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(32, 6).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.8727F));

		PartDefinition cube_r27 = nose12.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(32, 12).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r28 = nose12.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(32, 18).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.6981F));

		PartDefinition cube_r29 = nose12.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(32, 24).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.6109F));

		PartDefinition cube_r30 = nose12.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(32, 30).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r31 = nose12.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(0, 33).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r32 = nose12.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(4, 33).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition cube_r33 = nose12.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(8, 33).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition cube_r34 = nose12.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(36, 0).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition cube_r35 = nose12.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(36, 6).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition cube_r36 = nose12.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(36, 12).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition nose13 = nose1.addOrReplaceChild("nose13", CubeListBuilder.create().texOffs(28, 12).addBox(-1.0F, 12.0F, 0.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition cube_r37 = nose13.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(20, 18).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.4835F));

		PartDefinition cube_r38 = nose13.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(0, 21).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.3963F));

		PartDefinition cube_r39 = nose13.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(4, 21).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.309F));

		PartDefinition cube_r40 = nose13.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(8, 21).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.2217F));

		PartDefinition cube_r41 = nose13.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.1345F));

		PartDefinition cube_r42 = nose13.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(24, 6).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition cube_r43 = nose13.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(12, 24).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.9599F));

		PartDefinition cube_r44 = nose13.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(24, 12).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.8727F));

		PartDefinition cube_r45 = nose13.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(16, 24).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r46 = nose13.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(24, 18).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.6981F));

		PartDefinition cube_r47 = nose13.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(20, 24).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.6109F));

		PartDefinition cube_r48 = nose13.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(24, 24).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r49 = nose13.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(0, 27).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r50 = nose13.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(4, 27).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition cube_r51 = nose13.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(8, 27).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition cube_r52 = nose13.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(28, 0).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition cube_r53 = nose13.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(28, 6).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition cube_r54 = nose13.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(28, 18).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition nose14 = nose1.addOrReplaceChild("nose14", CubeListBuilder.create().texOffs(20, 6).addBox(-1.0F, 12.0F, 0.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition cube_r55 = nose14.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(4, 0).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.4835F));

		PartDefinition cube_r56 = nose14.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.3963F));

		PartDefinition cube_r57 = nose14.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(0, 9).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.309F));

		PartDefinition cube_r58 = nose14.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(4, 9).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.2217F));

		PartDefinition cube_r59 = nose14.addOrReplaceChild("cube_r59", CubeListBuilder.create().texOffs(8, 9).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.1345F));

		PartDefinition cube_r60 = nose14.addOrReplaceChild("cube_r60", CubeListBuilder.create().texOffs(12, 0).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition cube_r61 = nose14.addOrReplaceChild("cube_r61", CubeListBuilder.create().texOffs(12, 6).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.9599F));

		PartDefinition cube_r62 = nose14.addOrReplaceChild("cube_r62", CubeListBuilder.create().texOffs(12, 12).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.8727F));

		PartDefinition cube_r63 = nose14.addOrReplaceChild("cube_r63", CubeListBuilder.create().texOffs(0, 15).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r64 = nose14.addOrReplaceChild("cube_r64", CubeListBuilder.create().texOffs(4, 15).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.6981F));

		PartDefinition cube_r65 = nose14.addOrReplaceChild("cube_r65", CubeListBuilder.create().texOffs(8, 15).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.6109F));

		PartDefinition cube_r66 = nose14.addOrReplaceChild("cube_r66", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r67 = nose14.addOrReplaceChild("cube_r67", CubeListBuilder.create().texOffs(16, 6).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r68 = nose14.addOrReplaceChild("cube_r68", CubeListBuilder.create().texOffs(16, 12).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition cube_r69 = nose14.addOrReplaceChild("cube_r69", CubeListBuilder.create().texOffs(12, 18).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition cube_r70 = nose14.addOrReplaceChild("cube_r70", CubeListBuilder.create().texOffs(16, 18).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition cube_r71 = nose14.addOrReplaceChild("cube_r71", CubeListBuilder.create().texOffs(20, 0).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition cube_r72 = nose14.addOrReplaceChild("cube_r72", CubeListBuilder.create().texOffs(20, 12).addBox(-1.0F, 12.0F, 32.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -32.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition nose2 = nose.addOrReplaceChild("nose2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 6.0F));

		PartDefinition coneq1 = nose2.addOrReplaceChild("coneq1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cone1 = coneq1.addOrReplaceChild("cone1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r73 = cone1.addOrReplaceChild("cube_r73", CubeListBuilder.create().texOffs(108, 124).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r74 = cone1.addOrReplaceChild("cube_r74", CubeListBuilder.create().texOffs(124, 108).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r75 = cone1.addOrReplaceChild("cube_r75", CubeListBuilder.create().texOffs(112, 124).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r76 = cone1.addOrReplaceChild("cube_r76", CubeListBuilder.create().texOffs(124, 112).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r77 = cone1.addOrReplaceChild("cube_r77", CubeListBuilder.create().texOffs(116, 124).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r78 = cone1.addOrReplaceChild("cube_r78", CubeListBuilder.create().texOffs(124, 116).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r79 = cone1.addOrReplaceChild("cube_r79", CubeListBuilder.create().texOffs(120, 124).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone2 = coneq1.addOrReplaceChild("cone2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition cube_r80 = cone2.addOrReplaceChild("cube_r80", CubeListBuilder.create().texOffs(124, 92).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r81 = cone2.addOrReplaceChild("cube_r81", CubeListBuilder.create().texOffs(96, 124).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r82 = cone2.addOrReplaceChild("cube_r82", CubeListBuilder.create().texOffs(124, 96).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r83 = cone2.addOrReplaceChild("cube_r83", CubeListBuilder.create().texOffs(100, 124).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r84 = cone2.addOrReplaceChild("cube_r84", CubeListBuilder.create().texOffs(124, 100).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r85 = cone2.addOrReplaceChild("cube_r85", CubeListBuilder.create().texOffs(104, 124).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r86 = cone2.addOrReplaceChild("cube_r86", CubeListBuilder.create().texOffs(124, 104).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone3 = coneq1.addOrReplaceChild("cone3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition cube_r87 = cone3.addOrReplaceChild("cube_r87", CubeListBuilder.create().texOffs(72, 124).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r88 = cone3.addOrReplaceChild("cube_r88", CubeListBuilder.create().texOffs(76, 124).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r89 = cone3.addOrReplaceChild("cube_r89", CubeListBuilder.create().texOffs(80, 124).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r90 = cone3.addOrReplaceChild("cube_r90", CubeListBuilder.create().texOffs(84, 124).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r91 = cone3.addOrReplaceChild("cube_r91", CubeListBuilder.create().texOffs(88, 124).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r92 = cone3.addOrReplaceChild("cube_r92", CubeListBuilder.create().texOffs(124, 88).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r93 = cone3.addOrReplaceChild("cube_r93", CubeListBuilder.create().texOffs(92, 124).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone18 = coneq1.addOrReplaceChild("cone18", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.4835F));

		PartDefinition cube_r94 = cone18.addOrReplaceChild("cube_r94", CubeListBuilder.create().texOffs(84, 116).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r95 = cone18.addOrReplaceChild("cube_r95", CubeListBuilder.create().texOffs(88, 116).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r96 = cone18.addOrReplaceChild("cube_r96", CubeListBuilder.create().texOffs(116, 88).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r97 = cone18.addOrReplaceChild("cube_r97", CubeListBuilder.create().texOffs(92, 116).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r98 = cone18.addOrReplaceChild("cube_r98", CubeListBuilder.create().texOffs(116, 92).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r99 = cone18.addOrReplaceChild("cube_r99", CubeListBuilder.create().texOffs(96, 116).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r100 = cone18.addOrReplaceChild("cube_r100", CubeListBuilder.create().texOffs(116, 96).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone17 = coneq1.addOrReplaceChild("cone17", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

		PartDefinition cube_r101 = cone17.addOrReplaceChild("cube_r101", CubeListBuilder.create().texOffs(100, 116).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r102 = cone17.addOrReplaceChild("cube_r102", CubeListBuilder.create().texOffs(116, 100).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r103 = cone17.addOrReplaceChild("cube_r103", CubeListBuilder.create().texOffs(104, 116).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r104 = cone17.addOrReplaceChild("cube_r104", CubeListBuilder.create().texOffs(116, 104).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r105 = cone17.addOrReplaceChild("cube_r105", CubeListBuilder.create().texOffs(108, 116).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r106 = cone17.addOrReplaceChild("cube_r106", CubeListBuilder.create().texOffs(116, 108).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r107 = cone17.addOrReplaceChild("cube_r107", CubeListBuilder.create().texOffs(112, 116).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone16 = coneq1.addOrReplaceChild("cone16", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.309F));

		PartDefinition cube_r108 = cone16.addOrReplaceChild("cube_r108", CubeListBuilder.create().texOffs(116, 112).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r109 = cone16.addOrReplaceChild("cube_r109", CubeListBuilder.create().texOffs(116, 116).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r110 = cone16.addOrReplaceChild("cube_r110", CubeListBuilder.create().texOffs(0, 120).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r111 = cone16.addOrReplaceChild("cube_r111", CubeListBuilder.create().texOffs(120, 0).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r112 = cone16.addOrReplaceChild("cube_r112", CubeListBuilder.create().texOffs(4, 120).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r113 = cone16.addOrReplaceChild("cube_r113", CubeListBuilder.create().texOffs(120, 4).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r114 = cone16.addOrReplaceChild("cube_r114", CubeListBuilder.create().texOffs(8, 120).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone15 = coneq1.addOrReplaceChild("cone15", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.2217F));

		PartDefinition cube_r115 = cone15.addOrReplaceChild("cube_r115", CubeListBuilder.create().texOffs(120, 8).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r116 = cone15.addOrReplaceChild("cube_r116", CubeListBuilder.create().texOffs(12, 120).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r117 = cone15.addOrReplaceChild("cube_r117", CubeListBuilder.create().texOffs(120, 12).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r118 = cone15.addOrReplaceChild("cube_r118", CubeListBuilder.create().texOffs(16, 120).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r119 = cone15.addOrReplaceChild("cube_r119", CubeListBuilder.create().texOffs(120, 16).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r120 = cone15.addOrReplaceChild("cube_r120", CubeListBuilder.create().texOffs(20, 120).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r121 = cone15.addOrReplaceChild("cube_r121", CubeListBuilder.create().texOffs(120, 20).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone14 = coneq1.addOrReplaceChild("cone14", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.1345F));

		PartDefinition cube_r122 = cone14.addOrReplaceChild("cube_r122", CubeListBuilder.create().texOffs(24, 120).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r123 = cone14.addOrReplaceChild("cube_r123", CubeListBuilder.create().texOffs(120, 24).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r124 = cone14.addOrReplaceChild("cube_r124", CubeListBuilder.create().texOffs(28, 120).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r125 = cone14.addOrReplaceChild("cube_r125", CubeListBuilder.create().texOffs(120, 28).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r126 = cone14.addOrReplaceChild("cube_r126", CubeListBuilder.create().texOffs(32, 120).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r127 = cone14.addOrReplaceChild("cube_r127", CubeListBuilder.create().texOffs(120, 32).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r128 = cone14.addOrReplaceChild("cube_r128", CubeListBuilder.create().texOffs(36, 120).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone13 = coneq1.addOrReplaceChild("cone13", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition cube_r129 = cone13.addOrReplaceChild("cube_r129", CubeListBuilder.create().texOffs(120, 36).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r130 = cone13.addOrReplaceChild("cube_r130", CubeListBuilder.create().texOffs(40, 120).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r131 = cone13.addOrReplaceChild("cube_r131", CubeListBuilder.create().texOffs(120, 40).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r132 = cone13.addOrReplaceChild("cube_r132", CubeListBuilder.create().texOffs(44, 120).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r133 = cone13.addOrReplaceChild("cube_r133", CubeListBuilder.create().texOffs(120, 44).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r134 = cone13.addOrReplaceChild("cube_r134", CubeListBuilder.create().texOffs(48, 120).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r135 = cone13.addOrReplaceChild("cube_r135", CubeListBuilder.create().texOffs(120, 48).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone12 = coneq1.addOrReplaceChild("cone12", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.9599F));

		PartDefinition cube_r136 = cone12.addOrReplaceChild("cube_r136", CubeListBuilder.create().texOffs(52, 120).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r137 = cone12.addOrReplaceChild("cube_r137", CubeListBuilder.create().texOffs(120, 52).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r138 = cone12.addOrReplaceChild("cube_r138", CubeListBuilder.create().texOffs(56, 120).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r139 = cone12.addOrReplaceChild("cube_r139", CubeListBuilder.create().texOffs(120, 56).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r140 = cone12.addOrReplaceChild("cube_r140", CubeListBuilder.create().texOffs(60, 120).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r141 = cone12.addOrReplaceChild("cube_r141", CubeListBuilder.create().texOffs(120, 60).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r142 = cone12.addOrReplaceChild("cube_r142", CubeListBuilder.create().texOffs(64, 120).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone11 = coneq1.addOrReplaceChild("cone11", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.8727F));

		PartDefinition cube_r143 = cone11.addOrReplaceChild("cube_r143", CubeListBuilder.create().texOffs(68, 120).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r144 = cone11.addOrReplaceChild("cube_r144", CubeListBuilder.create().texOffs(72, 120).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r145 = cone11.addOrReplaceChild("cube_r145", CubeListBuilder.create().texOffs(76, 120).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r146 = cone11.addOrReplaceChild("cube_r146", CubeListBuilder.create().texOffs(80, 120).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r147 = cone11.addOrReplaceChild("cube_r147", CubeListBuilder.create().texOffs(84, 120).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r148 = cone11.addOrReplaceChild("cube_r148", CubeListBuilder.create().texOffs(88, 120).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r149 = cone11.addOrReplaceChild("cube_r149", CubeListBuilder.create().texOffs(120, 88).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone10 = coneq1.addOrReplaceChild("cone10", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r150 = cone10.addOrReplaceChild("cube_r150", CubeListBuilder.create().texOffs(92, 120).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r151 = cone10.addOrReplaceChild("cube_r151", CubeListBuilder.create().texOffs(120, 92).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r152 = cone10.addOrReplaceChild("cube_r152", CubeListBuilder.create().texOffs(96, 120).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r153 = cone10.addOrReplaceChild("cube_r153", CubeListBuilder.create().texOffs(120, 96).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r154 = cone10.addOrReplaceChild("cube_r154", CubeListBuilder.create().texOffs(100, 120).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r155 = cone10.addOrReplaceChild("cube_r155", CubeListBuilder.create().texOffs(120, 100).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r156 = cone10.addOrReplaceChild("cube_r156", CubeListBuilder.create().texOffs(104, 120).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone9 = coneq1.addOrReplaceChild("cone9", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		PartDefinition cube_r157 = cone9.addOrReplaceChild("cube_r157", CubeListBuilder.create().texOffs(120, 104).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r158 = cone9.addOrReplaceChild("cube_r158", CubeListBuilder.create().texOffs(108, 120).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r159 = cone9.addOrReplaceChild("cube_r159", CubeListBuilder.create().texOffs(120, 108).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r160 = cone9.addOrReplaceChild("cube_r160", CubeListBuilder.create().texOffs(112, 120).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r161 = cone9.addOrReplaceChild("cube_r161", CubeListBuilder.create().texOffs(120, 112).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r162 = cone9.addOrReplaceChild("cube_r162", CubeListBuilder.create().texOffs(116, 120).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r163 = cone9.addOrReplaceChild("cube_r163", CubeListBuilder.create().texOffs(120, 116).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone8 = coneq1.addOrReplaceChild("cone8", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6109F));

		PartDefinition cube_r164 = cone8.addOrReplaceChild("cube_r164", CubeListBuilder.create().texOffs(120, 120).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r165 = cone8.addOrReplaceChild("cube_r165", CubeListBuilder.create().texOffs(0, 124).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r166 = cone8.addOrReplaceChild("cube_r166", CubeListBuilder.create().texOffs(124, 0).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r167 = cone8.addOrReplaceChild("cube_r167", CubeListBuilder.create().texOffs(4, 124).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r168 = cone8.addOrReplaceChild("cube_r168", CubeListBuilder.create().texOffs(124, 4).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r169 = cone8.addOrReplaceChild("cube_r169", CubeListBuilder.create().texOffs(8, 124).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r170 = cone8.addOrReplaceChild("cube_r170", CubeListBuilder.create().texOffs(124, 8).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone7 = coneq1.addOrReplaceChild("cone7", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r171 = cone7.addOrReplaceChild("cube_r171", CubeListBuilder.create().texOffs(12, 124).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r172 = cone7.addOrReplaceChild("cube_r172", CubeListBuilder.create().texOffs(124, 12).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r173 = cone7.addOrReplaceChild("cube_r173", CubeListBuilder.create().texOffs(16, 124).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r174 = cone7.addOrReplaceChild("cube_r174", CubeListBuilder.create().texOffs(124, 16).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r175 = cone7.addOrReplaceChild("cube_r175", CubeListBuilder.create().texOffs(20, 124).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r176 = cone7.addOrReplaceChild("cube_r176", CubeListBuilder.create().texOffs(124, 20).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r177 = cone7.addOrReplaceChild("cube_r177", CubeListBuilder.create().texOffs(24, 124).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone6 = coneq1.addOrReplaceChild("cone6", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r178 = cone6.addOrReplaceChild("cube_r178", CubeListBuilder.create().texOffs(124, 24).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r179 = cone6.addOrReplaceChild("cube_r179", CubeListBuilder.create().texOffs(28, 124).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r180 = cone6.addOrReplaceChild("cube_r180", CubeListBuilder.create().texOffs(124, 28).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r181 = cone6.addOrReplaceChild("cube_r181", CubeListBuilder.create().texOffs(32, 124).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r182 = cone6.addOrReplaceChild("cube_r182", CubeListBuilder.create().texOffs(124, 32).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r183 = cone6.addOrReplaceChild("cube_r183", CubeListBuilder.create().texOffs(36, 124).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r184 = cone6.addOrReplaceChild("cube_r184", CubeListBuilder.create().texOffs(124, 36).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone5 = coneq1.addOrReplaceChild("cone5", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition cube_r185 = cone5.addOrReplaceChild("cube_r185", CubeListBuilder.create().texOffs(40, 124).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r186 = cone5.addOrReplaceChild("cube_r186", CubeListBuilder.create().texOffs(124, 40).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r187 = cone5.addOrReplaceChild("cube_r187", CubeListBuilder.create().texOffs(44, 124).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r188 = cone5.addOrReplaceChild("cube_r188", CubeListBuilder.create().texOffs(124, 44).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r189 = cone5.addOrReplaceChild("cube_r189", CubeListBuilder.create().texOffs(48, 124).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r190 = cone5.addOrReplaceChild("cube_r190", CubeListBuilder.create().texOffs(124, 48).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r191 = cone5.addOrReplaceChild("cube_r191", CubeListBuilder.create().texOffs(52, 124).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone4 = coneq1.addOrReplaceChild("cone4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition cube_r192 = cone4.addOrReplaceChild("cube_r192", CubeListBuilder.create().texOffs(124, 52).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r193 = cone4.addOrReplaceChild("cube_r193", CubeListBuilder.create().texOffs(56, 124).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r194 = cone4.addOrReplaceChild("cube_r194", CubeListBuilder.create().texOffs(124, 56).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r195 = cone4.addOrReplaceChild("cube_r195", CubeListBuilder.create().texOffs(60, 124).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r196 = cone4.addOrReplaceChild("cube_r196", CubeListBuilder.create().texOffs(124, 60).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r197 = cone4.addOrReplaceChild("cube_r197", CubeListBuilder.create().texOffs(64, 124).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r198 = cone4.addOrReplaceChild("cube_r198", CubeListBuilder.create().texOffs(68, 124).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition coneq2 = nose2.addOrReplaceChild("coneq2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition cone19 = coneq2.addOrReplaceChild("cone19", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r199 = cone19.addOrReplaceChild("cube_r199", CubeListBuilder.create().texOffs(60, 116).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r200 = cone19.addOrReplaceChild("cube_r200", CubeListBuilder.create().texOffs(116, 60).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r201 = cone19.addOrReplaceChild("cube_r201", CubeListBuilder.create().texOffs(64, 116).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r202 = cone19.addOrReplaceChild("cube_r202", CubeListBuilder.create().texOffs(68, 116).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r203 = cone19.addOrReplaceChild("cube_r203", CubeListBuilder.create().texOffs(72, 116).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r204 = cone19.addOrReplaceChild("cube_r204", CubeListBuilder.create().texOffs(76, 116).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r205 = cone19.addOrReplaceChild("cube_r205", CubeListBuilder.create().texOffs(80, 116).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone20 = coneq2.addOrReplaceChild("cone20", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition cube_r206 = cone20.addOrReplaceChild("cube_r206", CubeListBuilder.create().texOffs(116, 44).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r207 = cone20.addOrReplaceChild("cube_r207", CubeListBuilder.create().texOffs(48, 116).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r208 = cone20.addOrReplaceChild("cube_r208", CubeListBuilder.create().texOffs(116, 48).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r209 = cone20.addOrReplaceChild("cube_r209", CubeListBuilder.create().texOffs(52, 116).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r210 = cone20.addOrReplaceChild("cube_r210", CubeListBuilder.create().texOffs(116, 52).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r211 = cone20.addOrReplaceChild("cube_r211", CubeListBuilder.create().texOffs(56, 116).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r212 = cone20.addOrReplaceChild("cube_r212", CubeListBuilder.create().texOffs(116, 56).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone21 = coneq2.addOrReplaceChild("cone21", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition cube_r213 = cone21.addOrReplaceChild("cube_r213", CubeListBuilder.create().texOffs(32, 116).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r214 = cone21.addOrReplaceChild("cube_r214", CubeListBuilder.create().texOffs(116, 32).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r215 = cone21.addOrReplaceChild("cube_r215", CubeListBuilder.create().texOffs(36, 116).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r216 = cone21.addOrReplaceChild("cube_r216", CubeListBuilder.create().texOffs(116, 36).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r217 = cone21.addOrReplaceChild("cube_r217", CubeListBuilder.create().texOffs(40, 116).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r218 = cone21.addOrReplaceChild("cube_r218", CubeListBuilder.create().texOffs(116, 40).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r219 = cone21.addOrReplaceChild("cube_r219", CubeListBuilder.create().texOffs(44, 116).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone22 = coneq2.addOrReplaceChild("cone22", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.4835F));

		PartDefinition cube_r220 = cone22.addOrReplaceChild("cube_r220", CubeListBuilder.create().texOffs(116, 16).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r221 = cone22.addOrReplaceChild("cube_r221", CubeListBuilder.create().texOffs(20, 116).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r222 = cone22.addOrReplaceChild("cube_r222", CubeListBuilder.create().texOffs(116, 20).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r223 = cone22.addOrReplaceChild("cube_r223", CubeListBuilder.create().texOffs(24, 116).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r224 = cone22.addOrReplaceChild("cube_r224", CubeListBuilder.create().texOffs(116, 24).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r225 = cone22.addOrReplaceChild("cube_r225", CubeListBuilder.create().texOffs(28, 116).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r226 = cone22.addOrReplaceChild("cube_r226", CubeListBuilder.create().texOffs(116, 28).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone23 = coneq2.addOrReplaceChild("cone23", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

		PartDefinition cube_r227 = cone23.addOrReplaceChild("cube_r227", CubeListBuilder.create().texOffs(4, 116).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r228 = cone23.addOrReplaceChild("cube_r228", CubeListBuilder.create().texOffs(116, 4).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r229 = cone23.addOrReplaceChild("cube_r229", CubeListBuilder.create().texOffs(8, 116).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r230 = cone23.addOrReplaceChild("cube_r230", CubeListBuilder.create().texOffs(116, 8).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r231 = cone23.addOrReplaceChild("cube_r231", CubeListBuilder.create().texOffs(12, 116).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r232 = cone23.addOrReplaceChild("cube_r232", CubeListBuilder.create().texOffs(116, 12).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r233 = cone23.addOrReplaceChild("cube_r233", CubeListBuilder.create().texOffs(16, 116).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone24 = coneq2.addOrReplaceChild("cone24", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.309F));

		PartDefinition cube_r234 = cone24.addOrReplaceChild("cube_r234", CubeListBuilder.create().texOffs(104, 112).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r235 = cone24.addOrReplaceChild("cube_r235", CubeListBuilder.create().texOffs(112, 104).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r236 = cone24.addOrReplaceChild("cube_r236", CubeListBuilder.create().texOffs(108, 112).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r237 = cone24.addOrReplaceChild("cube_r237", CubeListBuilder.create().texOffs(112, 108).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r238 = cone24.addOrReplaceChild("cube_r238", CubeListBuilder.create().texOffs(112, 112).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r239 = cone24.addOrReplaceChild("cube_r239", CubeListBuilder.create().texOffs(0, 116).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r240 = cone24.addOrReplaceChild("cube_r240", CubeListBuilder.create().texOffs(116, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone25 = coneq2.addOrReplaceChild("cone25", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.2217F));

		PartDefinition cube_r241 = cone25.addOrReplaceChild("cube_r241", CubeListBuilder.create().texOffs(112, 88).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r242 = cone25.addOrReplaceChild("cube_r242", CubeListBuilder.create().texOffs(92, 112).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r243 = cone25.addOrReplaceChild("cube_r243", CubeListBuilder.create().texOffs(112, 92).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r244 = cone25.addOrReplaceChild("cube_r244", CubeListBuilder.create().texOffs(96, 112).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r245 = cone25.addOrReplaceChild("cube_r245", CubeListBuilder.create().texOffs(112, 96).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r246 = cone25.addOrReplaceChild("cube_r246", CubeListBuilder.create().texOffs(100, 112).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r247 = cone25.addOrReplaceChild("cube_r247", CubeListBuilder.create().texOffs(112, 100).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone26 = coneq2.addOrReplaceChild("cone26", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.1345F));

		PartDefinition cube_r248 = cone26.addOrReplaceChild("cube_r248", CubeListBuilder.create().texOffs(64, 112).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r249 = cone26.addOrReplaceChild("cube_r249", CubeListBuilder.create().texOffs(68, 112).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r250 = cone26.addOrReplaceChild("cube_r250", CubeListBuilder.create().texOffs(72, 112).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r251 = cone26.addOrReplaceChild("cube_r251", CubeListBuilder.create().texOffs(76, 112).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r252 = cone26.addOrReplaceChild("cube_r252", CubeListBuilder.create().texOffs(80, 112).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r253 = cone26.addOrReplaceChild("cube_r253", CubeListBuilder.create().texOffs(84, 112).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r254 = cone26.addOrReplaceChild("cube_r254", CubeListBuilder.create().texOffs(88, 112).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone27 = coneq2.addOrReplaceChild("cone27", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition cube_r255 = cone27.addOrReplaceChild("cube_r255", CubeListBuilder.create().texOffs(112, 48).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r256 = cone27.addOrReplaceChild("cube_r256", CubeListBuilder.create().texOffs(52, 112).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r257 = cone27.addOrReplaceChild("cube_r257", CubeListBuilder.create().texOffs(112, 52).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r258 = cone27.addOrReplaceChild("cube_r258", CubeListBuilder.create().texOffs(56, 112).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r259 = cone27.addOrReplaceChild("cube_r259", CubeListBuilder.create().texOffs(112, 56).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r260 = cone27.addOrReplaceChild("cube_r260", CubeListBuilder.create().texOffs(60, 112).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r261 = cone27.addOrReplaceChild("cube_r261", CubeListBuilder.create().texOffs(112, 60).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone28 = coneq2.addOrReplaceChild("cone28", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.9599F));

		PartDefinition cube_r262 = cone28.addOrReplaceChild("cube_r262", CubeListBuilder.create().texOffs(36, 112).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r263 = cone28.addOrReplaceChild("cube_r263", CubeListBuilder.create().texOffs(112, 36).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r264 = cone28.addOrReplaceChild("cube_r264", CubeListBuilder.create().texOffs(40, 112).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r265 = cone28.addOrReplaceChild("cube_r265", CubeListBuilder.create().texOffs(112, 40).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r266 = cone28.addOrReplaceChild("cube_r266", CubeListBuilder.create().texOffs(44, 112).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r267 = cone28.addOrReplaceChild("cube_r267", CubeListBuilder.create().texOffs(112, 44).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r268 = cone28.addOrReplaceChild("cube_r268", CubeListBuilder.create().texOffs(48, 112).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone29 = coneq2.addOrReplaceChild("cone29", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.8727F));

		PartDefinition cube_r269 = cone29.addOrReplaceChild("cube_r269", CubeListBuilder.create().texOffs(112, 20).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r270 = cone29.addOrReplaceChild("cube_r270", CubeListBuilder.create().texOffs(24, 112).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r271 = cone29.addOrReplaceChild("cube_r271", CubeListBuilder.create().texOffs(112, 24).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r272 = cone29.addOrReplaceChild("cube_r272", CubeListBuilder.create().texOffs(28, 112).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r273 = cone29.addOrReplaceChild("cube_r273", CubeListBuilder.create().texOffs(112, 28).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r274 = cone29.addOrReplaceChild("cube_r274", CubeListBuilder.create().texOffs(32, 112).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r275 = cone29.addOrReplaceChild("cube_r275", CubeListBuilder.create().texOffs(112, 32).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone30 = coneq2.addOrReplaceChild("cone30", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r276 = cone30.addOrReplaceChild("cube_r276", CubeListBuilder.create().texOffs(8, 112).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r277 = cone30.addOrReplaceChild("cube_r277", CubeListBuilder.create().texOffs(112, 8).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r278 = cone30.addOrReplaceChild("cube_r278", CubeListBuilder.create().texOffs(12, 112).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r279 = cone30.addOrReplaceChild("cube_r279", CubeListBuilder.create().texOffs(112, 12).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r280 = cone30.addOrReplaceChild("cube_r280", CubeListBuilder.create().texOffs(16, 112).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r281 = cone30.addOrReplaceChild("cube_r281", CubeListBuilder.create().texOffs(112, 16).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r282 = cone30.addOrReplaceChild("cube_r282", CubeListBuilder.create().texOffs(20, 112).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone31 = coneq2.addOrReplaceChild("cone31", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		PartDefinition cube_r283 = cone31.addOrReplaceChild("cube_r283", CubeListBuilder.create().texOffs(104, 108).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r284 = cone31.addOrReplaceChild("cube_r284", CubeListBuilder.create().texOffs(108, 104).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r285 = cone31.addOrReplaceChild("cube_r285", CubeListBuilder.create().texOffs(108, 108).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r286 = cone31.addOrReplaceChild("cube_r286", CubeListBuilder.create().texOffs(0, 112).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r287 = cone31.addOrReplaceChild("cube_r287", CubeListBuilder.create().texOffs(112, 0).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r288 = cone31.addOrReplaceChild("cube_r288", CubeListBuilder.create().texOffs(4, 112).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r289 = cone31.addOrReplaceChild("cube_r289", CubeListBuilder.create().texOffs(112, 4).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone32 = coneq2.addOrReplaceChild("cone32", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6109F));

		PartDefinition cube_r290 = cone32.addOrReplaceChild("cube_r290", CubeListBuilder.create().texOffs(108, 88).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r291 = cone32.addOrReplaceChild("cube_r291", CubeListBuilder.create().texOffs(92, 108).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r292 = cone32.addOrReplaceChild("cube_r292", CubeListBuilder.create().texOffs(108, 92).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r293 = cone32.addOrReplaceChild("cube_r293", CubeListBuilder.create().texOffs(96, 108).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r294 = cone32.addOrReplaceChild("cube_r294", CubeListBuilder.create().texOffs(108, 96).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r295 = cone32.addOrReplaceChild("cube_r295", CubeListBuilder.create().texOffs(100, 108).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r296 = cone32.addOrReplaceChild("cube_r296", CubeListBuilder.create().texOffs(108, 100).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone33 = coneq2.addOrReplaceChild("cone33", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r297 = cone33.addOrReplaceChild("cube_r297", CubeListBuilder.create().texOffs(64, 108).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r298 = cone33.addOrReplaceChild("cube_r298", CubeListBuilder.create().texOffs(68, 108).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r299 = cone33.addOrReplaceChild("cube_r299", CubeListBuilder.create().texOffs(72, 108).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r300 = cone33.addOrReplaceChild("cube_r300", CubeListBuilder.create().texOffs(76, 108).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r301 = cone33.addOrReplaceChild("cube_r301", CubeListBuilder.create().texOffs(80, 108).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r302 = cone33.addOrReplaceChild("cube_r302", CubeListBuilder.create().texOffs(84, 108).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r303 = cone33.addOrReplaceChild("cube_r303", CubeListBuilder.create().texOffs(88, 108).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone34 = coneq2.addOrReplaceChild("cone34", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r304 = cone34.addOrReplaceChild("cube_r304", CubeListBuilder.create().texOffs(108, 48).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r305 = cone34.addOrReplaceChild("cube_r305", CubeListBuilder.create().texOffs(52, 108).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r306 = cone34.addOrReplaceChild("cube_r306", CubeListBuilder.create().texOffs(108, 52).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r307 = cone34.addOrReplaceChild("cube_r307", CubeListBuilder.create().texOffs(56, 108).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r308 = cone34.addOrReplaceChild("cube_r308", CubeListBuilder.create().texOffs(108, 56).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r309 = cone34.addOrReplaceChild("cube_r309", CubeListBuilder.create().texOffs(60, 108).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r310 = cone34.addOrReplaceChild("cube_r310", CubeListBuilder.create().texOffs(108, 60).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone35 = coneq2.addOrReplaceChild("cone35", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition cube_r311 = cone35.addOrReplaceChild("cube_r311", CubeListBuilder.create().texOffs(36, 108).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r312 = cone35.addOrReplaceChild("cube_r312", CubeListBuilder.create().texOffs(108, 36).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r313 = cone35.addOrReplaceChild("cube_r313", CubeListBuilder.create().texOffs(40, 108).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r314 = cone35.addOrReplaceChild("cube_r314", CubeListBuilder.create().texOffs(108, 40).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r315 = cone35.addOrReplaceChild("cube_r315", CubeListBuilder.create().texOffs(44, 108).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r316 = cone35.addOrReplaceChild("cube_r316", CubeListBuilder.create().texOffs(108, 44).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r317 = cone35.addOrReplaceChild("cube_r317", CubeListBuilder.create().texOffs(48, 108).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone36 = coneq2.addOrReplaceChild("cone36", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition cube_r318 = cone36.addOrReplaceChild("cube_r318", CubeListBuilder.create().texOffs(108, 20).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r319 = cone36.addOrReplaceChild("cube_r319", CubeListBuilder.create().texOffs(24, 108).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r320 = cone36.addOrReplaceChild("cube_r320", CubeListBuilder.create().texOffs(108, 24).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r321 = cone36.addOrReplaceChild("cube_r321", CubeListBuilder.create().texOffs(28, 108).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r322 = cone36.addOrReplaceChild("cube_r322", CubeListBuilder.create().texOffs(108, 28).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r323 = cone36.addOrReplaceChild("cube_r323", CubeListBuilder.create().texOffs(32, 108).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r324 = cone36.addOrReplaceChild("cube_r324", CubeListBuilder.create().texOffs(108, 32).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition coneq3 = nose2.addOrReplaceChild("coneq3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition cone37 = coneq3.addOrReplaceChild("cone37", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r325 = cone37.addOrReplaceChild("cube_r325", CubeListBuilder.create().texOffs(8, 108).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r326 = cone37.addOrReplaceChild("cube_r326", CubeListBuilder.create().texOffs(108, 8).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r327 = cone37.addOrReplaceChild("cube_r327", CubeListBuilder.create().texOffs(12, 108).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r328 = cone37.addOrReplaceChild("cube_r328", CubeListBuilder.create().texOffs(108, 12).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r329 = cone37.addOrReplaceChild("cube_r329", CubeListBuilder.create().texOffs(16, 108).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r330 = cone37.addOrReplaceChild("cube_r330", CubeListBuilder.create().texOffs(108, 16).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r331 = cone37.addOrReplaceChild("cube_r331", CubeListBuilder.create().texOffs(20, 108).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone38 = coneq3.addOrReplaceChild("cone38", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition cube_r332 = cone38.addOrReplaceChild("cube_r332", CubeListBuilder.create().texOffs(100, 104).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r333 = cone38.addOrReplaceChild("cube_r333", CubeListBuilder.create().texOffs(104, 100).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r334 = cone38.addOrReplaceChild("cube_r334", CubeListBuilder.create().texOffs(104, 104).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r335 = cone38.addOrReplaceChild("cube_r335", CubeListBuilder.create().texOffs(0, 108).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r336 = cone38.addOrReplaceChild("cube_r336", CubeListBuilder.create().texOffs(108, 0).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r337 = cone38.addOrReplaceChild("cube_r337", CubeListBuilder.create().texOffs(4, 108).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r338 = cone38.addOrReplaceChild("cube_r338", CubeListBuilder.create().texOffs(108, 4).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone39 = coneq3.addOrReplaceChild("cone39", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition cube_r339 = cone39.addOrReplaceChild("cube_r339", CubeListBuilder.create().texOffs(84, 104).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r340 = cone39.addOrReplaceChild("cube_r340", CubeListBuilder.create().texOffs(88, 104).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r341 = cone39.addOrReplaceChild("cube_r341", CubeListBuilder.create().texOffs(104, 88).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r342 = cone39.addOrReplaceChild("cube_r342", CubeListBuilder.create().texOffs(92, 104).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r343 = cone39.addOrReplaceChild("cube_r343", CubeListBuilder.create().texOffs(104, 92).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r344 = cone39.addOrReplaceChild("cube_r344", CubeListBuilder.create().texOffs(96, 104).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r345 = cone39.addOrReplaceChild("cube_r345", CubeListBuilder.create().texOffs(104, 96).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone40 = coneq3.addOrReplaceChild("cone40", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.4835F));

		PartDefinition cube_r346 = cone40.addOrReplaceChild("cube_r346", CubeListBuilder.create().texOffs(56, 104).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r347 = cone40.addOrReplaceChild("cube_r347", CubeListBuilder.create().texOffs(60, 104).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r348 = cone40.addOrReplaceChild("cube_r348", CubeListBuilder.create().texOffs(64, 104).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r349 = cone40.addOrReplaceChild("cube_r349", CubeListBuilder.create().texOffs(68, 104).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r350 = cone40.addOrReplaceChild("cube_r350", CubeListBuilder.create().texOffs(72, 104).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r351 = cone40.addOrReplaceChild("cube_r351", CubeListBuilder.create().texOffs(76, 104).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r352 = cone40.addOrReplaceChild("cube_r352", CubeListBuilder.create().texOffs(80, 104).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone41 = coneq3.addOrReplaceChild("cone41", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

		PartDefinition cube_r353 = cone41.addOrReplaceChild("cube_r353", CubeListBuilder.create().texOffs(28, 104).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r354 = cone41.addOrReplaceChild("cube_r354", CubeListBuilder.create().texOffs(32, 104).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r355 = cone41.addOrReplaceChild("cube_r355", CubeListBuilder.create().texOffs(36, 104).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r356 = cone41.addOrReplaceChild("cube_r356", CubeListBuilder.create().texOffs(40, 104).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r357 = cone41.addOrReplaceChild("cube_r357", CubeListBuilder.create().texOffs(44, 104).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r358 = cone41.addOrReplaceChild("cube_r358", CubeListBuilder.create().texOffs(48, 104).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r359 = cone41.addOrReplaceChild("cube_r359", CubeListBuilder.create().texOffs(52, 104).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone42 = coneq3.addOrReplaceChild("cone42", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.309F));

		PartDefinition cube_r360 = cone42.addOrReplaceChild("cube_r360", CubeListBuilder.create().texOffs(0, 104).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r361 = cone42.addOrReplaceChild("cube_r361", CubeListBuilder.create().texOffs(4, 104).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r362 = cone42.addOrReplaceChild("cube_r362", CubeListBuilder.create().texOffs(8, 104).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r363 = cone42.addOrReplaceChild("cube_r363", CubeListBuilder.create().texOffs(12, 104).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r364 = cone42.addOrReplaceChild("cube_r364", CubeListBuilder.create().texOffs(16, 104).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r365 = cone42.addOrReplaceChild("cube_r365", CubeListBuilder.create().texOffs(20, 104).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r366 = cone42.addOrReplaceChild("cube_r366", CubeListBuilder.create().texOffs(24, 104).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone43 = coneq3.addOrReplaceChild("cone43", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.2217F));

		PartDefinition cube_r367 = cone43.addOrReplaceChild("cube_r367", CubeListBuilder.create().texOffs(88, 100).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r368 = cone43.addOrReplaceChild("cube_r368", CubeListBuilder.create().texOffs(100, 88).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r369 = cone43.addOrReplaceChild("cube_r369", CubeListBuilder.create().texOffs(92, 100).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r370 = cone43.addOrReplaceChild("cube_r370", CubeListBuilder.create().texOffs(100, 92).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r371 = cone43.addOrReplaceChild("cube_r371", CubeListBuilder.create().texOffs(96, 100).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r372 = cone43.addOrReplaceChild("cube_r372", CubeListBuilder.create().texOffs(100, 96).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r373 = cone43.addOrReplaceChild("cube_r373", CubeListBuilder.create().texOffs(100, 100).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone44 = coneq3.addOrReplaceChild("cone44", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.1345F));

		PartDefinition cube_r374 = cone44.addOrReplaceChild("cube_r374", CubeListBuilder.create().texOffs(60, 100).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r375 = cone44.addOrReplaceChild("cube_r375", CubeListBuilder.create().texOffs(64, 100).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r376 = cone44.addOrReplaceChild("cube_r376", CubeListBuilder.create().texOffs(68, 100).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r377 = cone44.addOrReplaceChild("cube_r377", CubeListBuilder.create().texOffs(72, 100).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r378 = cone44.addOrReplaceChild("cube_r378", CubeListBuilder.create().texOffs(76, 100).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r379 = cone44.addOrReplaceChild("cube_r379", CubeListBuilder.create().texOffs(80, 100).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r380 = cone44.addOrReplaceChild("cube_r380", CubeListBuilder.create().texOffs(84, 100).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone45 = coneq3.addOrReplaceChild("cone45", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition cube_r381 = cone45.addOrReplaceChild("cube_r381", CubeListBuilder.create().texOffs(32, 100).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r382 = cone45.addOrReplaceChild("cube_r382", CubeListBuilder.create().texOffs(36, 100).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r383 = cone45.addOrReplaceChild("cube_r383", CubeListBuilder.create().texOffs(40, 100).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r384 = cone45.addOrReplaceChild("cube_r384", CubeListBuilder.create().texOffs(44, 100).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r385 = cone45.addOrReplaceChild("cube_r385", CubeListBuilder.create().texOffs(48, 100).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r386 = cone45.addOrReplaceChild("cube_r386", CubeListBuilder.create().texOffs(52, 100).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r387 = cone45.addOrReplaceChild("cube_r387", CubeListBuilder.create().texOffs(56, 100).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone46 = coneq3.addOrReplaceChild("cone46", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.9599F));

		PartDefinition cube_r388 = cone46.addOrReplaceChild("cube_r388", CubeListBuilder.create().texOffs(4, 100).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r389 = cone46.addOrReplaceChild("cube_r389", CubeListBuilder.create().texOffs(8, 100).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r390 = cone46.addOrReplaceChild("cube_r390", CubeListBuilder.create().texOffs(12, 100).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r391 = cone46.addOrReplaceChild("cube_r391", CubeListBuilder.create().texOffs(16, 100).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r392 = cone46.addOrReplaceChild("cube_r392", CubeListBuilder.create().texOffs(20, 100).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r393 = cone46.addOrReplaceChild("cube_r393", CubeListBuilder.create().texOffs(24, 100).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r394 = cone46.addOrReplaceChild("cube_r394", CubeListBuilder.create().texOffs(28, 100).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone47 = coneq3.addOrReplaceChild("cone47", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.8727F));

		PartDefinition cube_r395 = cone47.addOrReplaceChild("cube_r395", CubeListBuilder.create().texOffs(84, 96).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r396 = cone47.addOrReplaceChild("cube_r396", CubeListBuilder.create().texOffs(88, 96).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r397 = cone47.addOrReplaceChild("cube_r397", CubeListBuilder.create().texOffs(96, 88).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r398 = cone47.addOrReplaceChild("cube_r398", CubeListBuilder.create().texOffs(92, 96).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r399 = cone47.addOrReplaceChild("cube_r399", CubeListBuilder.create().texOffs(96, 92).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r400 = cone47.addOrReplaceChild("cube_r400", CubeListBuilder.create().texOffs(96, 96).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r401 = cone47.addOrReplaceChild("cube_r401", CubeListBuilder.create().texOffs(0, 100).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone48 = coneq3.addOrReplaceChild("cone48", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r402 = cone48.addOrReplaceChild("cube_r402", CubeListBuilder.create().texOffs(56, 96).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r403 = cone48.addOrReplaceChild("cube_r403", CubeListBuilder.create().texOffs(60, 96).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r404 = cone48.addOrReplaceChild("cube_r404", CubeListBuilder.create().texOffs(64, 96).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r405 = cone48.addOrReplaceChild("cube_r405", CubeListBuilder.create().texOffs(68, 96).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r406 = cone48.addOrReplaceChild("cube_r406", CubeListBuilder.create().texOffs(72, 96).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r407 = cone48.addOrReplaceChild("cube_r407", CubeListBuilder.create().texOffs(76, 96).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r408 = cone48.addOrReplaceChild("cube_r408", CubeListBuilder.create().texOffs(80, 96).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone49 = coneq3.addOrReplaceChild("cone49", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		PartDefinition cube_r409 = cone49.addOrReplaceChild("cube_r409", CubeListBuilder.create().texOffs(28, 96).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r410 = cone49.addOrReplaceChild("cube_r410", CubeListBuilder.create().texOffs(32, 96).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r411 = cone49.addOrReplaceChild("cube_r411", CubeListBuilder.create().texOffs(36, 96).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r412 = cone49.addOrReplaceChild("cube_r412", CubeListBuilder.create().texOffs(40, 96).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r413 = cone49.addOrReplaceChild("cube_r413", CubeListBuilder.create().texOffs(44, 96).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r414 = cone49.addOrReplaceChild("cube_r414", CubeListBuilder.create().texOffs(48, 96).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r415 = cone49.addOrReplaceChild("cube_r415", CubeListBuilder.create().texOffs(52, 96).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone50 = coneq3.addOrReplaceChild("cone50", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6109F));

		PartDefinition cube_r416 = cone50.addOrReplaceChild("cube_r416", CubeListBuilder.create().texOffs(0, 96).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r417 = cone50.addOrReplaceChild("cube_r417", CubeListBuilder.create().texOffs(4, 96).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r418 = cone50.addOrReplaceChild("cube_r418", CubeListBuilder.create().texOffs(8, 96).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r419 = cone50.addOrReplaceChild("cube_r419", CubeListBuilder.create().texOffs(12, 96).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r420 = cone50.addOrReplaceChild("cube_r420", CubeListBuilder.create().texOffs(16, 96).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r421 = cone50.addOrReplaceChild("cube_r421", CubeListBuilder.create().texOffs(20, 96).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r422 = cone50.addOrReplaceChild("cube_r422", CubeListBuilder.create().texOffs(24, 96).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone51 = coneq3.addOrReplaceChild("cone51", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r423 = cone51.addOrReplaceChild("cube_r423", CubeListBuilder.create().texOffs(72, 92).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r424 = cone51.addOrReplaceChild("cube_r424", CubeListBuilder.create().texOffs(76, 92).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r425 = cone51.addOrReplaceChild("cube_r425", CubeListBuilder.create().texOffs(80, 92).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r426 = cone51.addOrReplaceChild("cube_r426", CubeListBuilder.create().texOffs(84, 92).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r427 = cone51.addOrReplaceChild("cube_r427", CubeListBuilder.create().texOffs(88, 92).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r428 = cone51.addOrReplaceChild("cube_r428", CubeListBuilder.create().texOffs(92, 88).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r429 = cone51.addOrReplaceChild("cube_r429", CubeListBuilder.create().texOffs(92, 92).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone52 = coneq3.addOrReplaceChild("cone52", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r430 = cone52.addOrReplaceChild("cube_r430", CubeListBuilder.create().texOffs(44, 92).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r431 = cone52.addOrReplaceChild("cube_r431", CubeListBuilder.create().texOffs(48, 92).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r432 = cone52.addOrReplaceChild("cube_r432", CubeListBuilder.create().texOffs(52, 92).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r433 = cone52.addOrReplaceChild("cube_r433", CubeListBuilder.create().texOffs(56, 92).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r434 = cone52.addOrReplaceChild("cube_r434", CubeListBuilder.create().texOffs(60, 92).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r435 = cone52.addOrReplaceChild("cube_r435", CubeListBuilder.create().texOffs(64, 92).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r436 = cone52.addOrReplaceChild("cube_r436", CubeListBuilder.create().texOffs(68, 92).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone53 = coneq3.addOrReplaceChild("cone53", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition cube_r437 = cone53.addOrReplaceChild("cube_r437", CubeListBuilder.create().texOffs(16, 92).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r438 = cone53.addOrReplaceChild("cube_r438", CubeListBuilder.create().texOffs(20, 92).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r439 = cone53.addOrReplaceChild("cube_r439", CubeListBuilder.create().texOffs(24, 92).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r440 = cone53.addOrReplaceChild("cube_r440", CubeListBuilder.create().texOffs(28, 92).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r441 = cone53.addOrReplaceChild("cube_r441", CubeListBuilder.create().texOffs(32, 92).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r442 = cone53.addOrReplaceChild("cube_r442", CubeListBuilder.create().texOffs(36, 92).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r443 = cone53.addOrReplaceChild("cube_r443", CubeListBuilder.create().texOffs(40, 92).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone54 = coneq3.addOrReplaceChild("cone54", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition cube_r444 = cone54.addOrReplaceChild("cube_r444", CubeListBuilder.create().texOffs(80, 88).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r445 = cone54.addOrReplaceChild("cube_r445", CubeListBuilder.create().texOffs(84, 88).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r446 = cone54.addOrReplaceChild("cube_r446", CubeListBuilder.create().texOffs(88, 88).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r447 = cone54.addOrReplaceChild("cube_r447", CubeListBuilder.create().texOffs(0, 92).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r448 = cone54.addOrReplaceChild("cube_r448", CubeListBuilder.create().texOffs(4, 92).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r449 = cone54.addOrReplaceChild("cube_r449", CubeListBuilder.create().texOffs(8, 92).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r450 = cone54.addOrReplaceChild("cube_r450", CubeListBuilder.create().texOffs(12, 92).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition coneq4 = nose2.addOrReplaceChild("coneq4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition cone55 = coneq4.addOrReplaceChild("cone55", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r451 = cone55.addOrReplaceChild("cube_r451", CubeListBuilder.create().texOffs(52, 88).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r452 = cone55.addOrReplaceChild("cube_r452", CubeListBuilder.create().texOffs(56, 88).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r453 = cone55.addOrReplaceChild("cube_r453", CubeListBuilder.create().texOffs(60, 88).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r454 = cone55.addOrReplaceChild("cube_r454", CubeListBuilder.create().texOffs(64, 88).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r455 = cone55.addOrReplaceChild("cube_r455", CubeListBuilder.create().texOffs(68, 88).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r456 = cone55.addOrReplaceChild("cube_r456", CubeListBuilder.create().texOffs(72, 88).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r457 = cone55.addOrReplaceChild("cube_r457", CubeListBuilder.create().texOffs(76, 88).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone56 = coneq4.addOrReplaceChild("cone56", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition cube_r458 = cone56.addOrReplaceChild("cube_r458", CubeListBuilder.create().texOffs(24, 88).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r459 = cone56.addOrReplaceChild("cube_r459", CubeListBuilder.create().texOffs(28, 88).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r460 = cone56.addOrReplaceChild("cube_r460", CubeListBuilder.create().texOffs(32, 88).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r461 = cone56.addOrReplaceChild("cube_r461", CubeListBuilder.create().texOffs(36, 88).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r462 = cone56.addOrReplaceChild("cube_r462", CubeListBuilder.create().texOffs(40, 88).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r463 = cone56.addOrReplaceChild("cube_r463", CubeListBuilder.create().texOffs(44, 88).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r464 = cone56.addOrReplaceChild("cube_r464", CubeListBuilder.create().texOffs(48, 88).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone57 = coneq4.addOrReplaceChild("cone57", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition cube_r465 = cone57.addOrReplaceChild("cube_r465", CubeListBuilder.create().texOffs(56, 60).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r466 = cone57.addOrReplaceChild("cube_r466", CubeListBuilder.create().texOffs(0, 88).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r467 = cone57.addOrReplaceChild("cube_r467", CubeListBuilder.create().texOffs(4, 88).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r468 = cone57.addOrReplaceChild("cube_r468", CubeListBuilder.create().texOffs(8, 88).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r469 = cone57.addOrReplaceChild("cube_r469", CubeListBuilder.create().texOffs(12, 88).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r470 = cone57.addOrReplaceChild("cube_r470", CubeListBuilder.create().texOffs(16, 88).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r471 = cone57.addOrReplaceChild("cube_r471", CubeListBuilder.create().texOffs(20, 88).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone58 = coneq4.addOrReplaceChild("cone58", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.4835F));

		PartDefinition cube_r472 = cone58.addOrReplaceChild("cube_r472", CubeListBuilder.create().texOffs(28, 58).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r473 = cone58.addOrReplaceChild("cube_r473", CubeListBuilder.create().texOffs(32, 58).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r474 = cone58.addOrReplaceChild("cube_r474", CubeListBuilder.create().texOffs(36, 58).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r475 = cone58.addOrReplaceChild("cube_r475", CubeListBuilder.create().texOffs(40, 58).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r476 = cone58.addOrReplaceChild("cube_r476", CubeListBuilder.create().texOffs(44, 58).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r477 = cone58.addOrReplaceChild("cube_r477", CubeListBuilder.create().texOffs(48, 60).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r478 = cone58.addOrReplaceChild("cube_r478", CubeListBuilder.create().texOffs(52, 60).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone59 = coneq4.addOrReplaceChild("cone59", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

		PartDefinition cube_r479 = cone59.addOrReplaceChild("cube_r479", CubeListBuilder.create().texOffs(0, 57).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r480 = cone59.addOrReplaceChild("cube_r480", CubeListBuilder.create().texOffs(4, 57).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r481 = cone59.addOrReplaceChild("cube_r481", CubeListBuilder.create().texOffs(8, 57).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r482 = cone59.addOrReplaceChild("cube_r482", CubeListBuilder.create().texOffs(12, 58).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r483 = cone59.addOrReplaceChild("cube_r483", CubeListBuilder.create().texOffs(16, 58).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r484 = cone59.addOrReplaceChild("cube_r484", CubeListBuilder.create().texOffs(20, 58).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r485 = cone59.addOrReplaceChild("cube_r485", CubeListBuilder.create().texOffs(24, 58).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone60 = coneq4.addOrReplaceChild("cone60", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.309F));

		PartDefinition cube_r486 = cone60.addOrReplaceChild("cube_r486", CubeListBuilder.create().texOffs(44, 54).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r487 = cone60.addOrReplaceChild("cube_r487", CubeListBuilder.create().texOffs(56, 44).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r488 = cone60.addOrReplaceChild("cube_r488", CubeListBuilder.create().texOffs(48, 56).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r489 = cone60.addOrReplaceChild("cube_r489", CubeListBuilder.create().texOffs(56, 48).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r490 = cone60.addOrReplaceChild("cube_r490", CubeListBuilder.create().texOffs(52, 56).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r491 = cone60.addOrReplaceChild("cube_r491", CubeListBuilder.create().texOffs(56, 52).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r492 = cone60.addOrReplaceChild("cube_r492", CubeListBuilder.create().texOffs(56, 56).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone61 = coneq4.addOrReplaceChild("cone61", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.2217F));

		PartDefinition cube_r493 = cone61.addOrReplaceChild("cube_r493", CubeListBuilder.create().texOffs(54, 28).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r494 = cone61.addOrReplaceChild("cube_r494", CubeListBuilder.create().texOffs(32, 54).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r495 = cone61.addOrReplaceChild("cube_r495", CubeListBuilder.create().texOffs(54, 32).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r496 = cone61.addOrReplaceChild("cube_r496", CubeListBuilder.create().texOffs(36, 54).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r497 = cone61.addOrReplaceChild("cube_r497", CubeListBuilder.create().texOffs(54, 36).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r498 = cone61.addOrReplaceChild("cube_r498", CubeListBuilder.create().texOffs(40, 54).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r499 = cone61.addOrReplaceChild("cube_r499", CubeListBuilder.create().texOffs(54, 40).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone62 = coneq4.addOrReplaceChild("cone62", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.1345F));

		PartDefinition cube_r500 = cone62.addOrReplaceChild("cube_r500", CubeListBuilder.create().texOffs(16, 54).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r501 = cone62.addOrReplaceChild("cube_r501", CubeListBuilder.create().texOffs(54, 16).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r502 = cone62.addOrReplaceChild("cube_r502", CubeListBuilder.create().texOffs(20, 54).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r503 = cone62.addOrReplaceChild("cube_r503", CubeListBuilder.create().texOffs(54, 20).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r504 = cone62.addOrReplaceChild("cube_r504", CubeListBuilder.create().texOffs(24, 54).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r505 = cone62.addOrReplaceChild("cube_r505", CubeListBuilder.create().texOffs(54, 24).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r506 = cone62.addOrReplaceChild("cube_r506", CubeListBuilder.create().texOffs(28, 54).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone63 = coneq4.addOrReplaceChild("cone63", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition cube_r507 = cone63.addOrReplaceChild("cube_r507", CubeListBuilder.create().texOffs(4, 53).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r508 = cone63.addOrReplaceChild("cube_r508", CubeListBuilder.create().texOffs(8, 53).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r509 = cone63.addOrReplaceChild("cube_r509", CubeListBuilder.create().texOffs(54, 0).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r510 = cone63.addOrReplaceChild("cube_r510", CubeListBuilder.create().texOffs(54, 4).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r511 = cone63.addOrReplaceChild("cube_r511", CubeListBuilder.create().texOffs(54, 8).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r512 = cone63.addOrReplaceChild("cube_r512", CubeListBuilder.create().texOffs(12, 54).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r513 = cone63.addOrReplaceChild("cube_r513", CubeListBuilder.create().texOffs(54, 12).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone64 = coneq4.addOrReplaceChild("cone64", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.9599F));

		PartDefinition cube_r514 = cone64.addOrReplaceChild("cube_r514", CubeListBuilder.create().texOffs(50, 40).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r515 = cone64.addOrReplaceChild("cube_r515", CubeListBuilder.create().texOffs(44, 50).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r516 = cone64.addOrReplaceChild("cube_r516", CubeListBuilder.create().texOffs(52, 44).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r517 = cone64.addOrReplaceChild("cube_r517", CubeListBuilder.create().texOffs(48, 52).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r518 = cone64.addOrReplaceChild("cube_r518", CubeListBuilder.create().texOffs(52, 48).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r519 = cone64.addOrReplaceChild("cube_r519", CubeListBuilder.create().texOffs(52, 52).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r520 = cone64.addOrReplaceChild("cube_r520", CubeListBuilder.create().texOffs(0, 53).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone65 = coneq4.addOrReplaceChild("cone65", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.8727F));

		PartDefinition cube_r521 = cone65.addOrReplaceChild("cube_r521", CubeListBuilder.create().texOffs(28, 50).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r522 = cone65.addOrReplaceChild("cube_r522", CubeListBuilder.create().texOffs(50, 28).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r523 = cone65.addOrReplaceChild("cube_r523", CubeListBuilder.create().texOffs(32, 50).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r524 = cone65.addOrReplaceChild("cube_r524", CubeListBuilder.create().texOffs(50, 32).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r525 = cone65.addOrReplaceChild("cube_r525", CubeListBuilder.create().texOffs(36, 50).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r526 = cone65.addOrReplaceChild("cube_r526", CubeListBuilder.create().texOffs(50, 36).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r527 = cone65.addOrReplaceChild("cube_r527", CubeListBuilder.create().texOffs(40, 50).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone66 = coneq4.addOrReplaceChild("cone66", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r528 = cone66.addOrReplaceChild("cube_r528", CubeListBuilder.create().texOffs(50, 12).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r529 = cone66.addOrReplaceChild("cube_r529", CubeListBuilder.create().texOffs(16, 50).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r530 = cone66.addOrReplaceChild("cube_r530", CubeListBuilder.create().texOffs(50, 16).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r531 = cone66.addOrReplaceChild("cube_r531", CubeListBuilder.create().texOffs(20, 50).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r532 = cone66.addOrReplaceChild("cube_r532", CubeListBuilder.create().texOffs(50, 20).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r533 = cone66.addOrReplaceChild("cube_r533", CubeListBuilder.create().texOffs(24, 50).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r534 = cone66.addOrReplaceChild("cube_r534", CubeListBuilder.create().texOffs(50, 24).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone67 = coneq4.addOrReplaceChild("cone67", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		PartDefinition cube_r535 = cone67.addOrReplaceChild("cube_r535", CubeListBuilder.create().texOffs(0, 49).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r536 = cone67.addOrReplaceChild("cube_r536", CubeListBuilder.create().texOffs(4, 49).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r537 = cone67.addOrReplaceChild("cube_r537", CubeListBuilder.create().texOffs(8, 49).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r538 = cone67.addOrReplaceChild("cube_r538", CubeListBuilder.create().texOffs(50, 0).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r539 = cone67.addOrReplaceChild("cube_r539", CubeListBuilder.create().texOffs(50, 4).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r540 = cone67.addOrReplaceChild("cube_r540", CubeListBuilder.create().texOffs(50, 8).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r541 = cone67.addOrReplaceChild("cube_r541", CubeListBuilder.create().texOffs(12, 50).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone68 = coneq4.addOrReplaceChild("cone68", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6109F));

		PartDefinition cube_r542 = cone68.addOrReplaceChild("cube_r542", CubeListBuilder.create().texOffs(36, 46).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r543 = cone68.addOrReplaceChild("cube_r543", CubeListBuilder.create().texOffs(46, 36).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r544 = cone68.addOrReplaceChild("cube_r544", CubeListBuilder.create().texOffs(40, 46).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r545 = cone68.addOrReplaceChild("cube_r545", CubeListBuilder.create().texOffs(46, 40).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r546 = cone68.addOrReplaceChild("cube_r546", CubeListBuilder.create().texOffs(44, 46).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r547 = cone68.addOrReplaceChild("cube_r547", CubeListBuilder.create().texOffs(48, 44).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r548 = cone68.addOrReplaceChild("cube_r548", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone69 = coneq4.addOrReplaceChild("cone69", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r549 = cone69.addOrReplaceChild("cube_r549", CubeListBuilder.create().texOffs(46, 20).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r550 = cone69.addOrReplaceChild("cube_r550", CubeListBuilder.create().texOffs(24, 46).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r551 = cone69.addOrReplaceChild("cube_r551", CubeListBuilder.create().texOffs(46, 24).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r552 = cone69.addOrReplaceChild("cube_r552", CubeListBuilder.create().texOffs(28, 46).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r553 = cone69.addOrReplaceChild("cube_r553", CubeListBuilder.create().texOffs(46, 28).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r554 = cone69.addOrReplaceChild("cube_r554", CubeListBuilder.create().texOffs(32, 46).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r555 = cone69.addOrReplaceChild("cube_r555", CubeListBuilder.create().texOffs(46, 32).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone70 = coneq4.addOrReplaceChild("cone70", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r556 = cone70.addOrReplaceChild("cube_r556", CubeListBuilder.create().texOffs(46, 4).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r557 = cone70.addOrReplaceChild("cube_r557", CubeListBuilder.create().texOffs(46, 8).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r558 = cone70.addOrReplaceChild("cube_r558", CubeListBuilder.create().texOffs(12, 46).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r559 = cone70.addOrReplaceChild("cube_r559", CubeListBuilder.create().texOffs(46, 12).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r560 = cone70.addOrReplaceChild("cube_r560", CubeListBuilder.create().texOffs(16, 46).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r561 = cone70.addOrReplaceChild("cube_r561", CubeListBuilder.create().texOffs(46, 16).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r562 = cone70.addOrReplaceChild("cube_r562", CubeListBuilder.create().texOffs(20, 46).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone71 = coneq4.addOrReplaceChild("cone71", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition cube_r563 = cone71.addOrReplaceChild("cube_r563", CubeListBuilder.create().texOffs(34, 42).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r564 = cone71.addOrReplaceChild("cube_r564", CubeListBuilder.create().texOffs(38, 42).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r565 = cone71.addOrReplaceChild("cube_r565", CubeListBuilder.create().texOffs(42, 42).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r566 = cone71.addOrReplaceChild("cube_r566", CubeListBuilder.create().texOffs(0, 45).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r567 = cone71.addOrReplaceChild("cube_r567", CubeListBuilder.create().texOffs(4, 45).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r568 = cone71.addOrReplaceChild("cube_r568", CubeListBuilder.create().texOffs(8, 45).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r569 = cone71.addOrReplaceChild("cube_r569", CubeListBuilder.create().texOffs(46, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cone72 = coneq4.addOrReplaceChild("cone72", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition cube_r570 = cone72.addOrReplaceChild("cube_r570", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, 20.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.7067F, 5.6519F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r571 = cone72.addOrReplaceChild("cube_r571", CubeListBuilder.create().texOffs(0, 4).addBox(-1.0F, 0.0F, 16.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.3871F, 6.1981F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r572 = cone72.addOrReplaceChild("cube_r572", CubeListBuilder.create().texOffs(14, 42).addBox(-1.0F, 0.0F, 12.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.2798F, 5.3483F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r573 = cone72.addOrReplaceChild("cube_r573", CubeListBuilder.create().texOffs(18, 42).addBox(-1.0F, 0.0F, 8.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.938F, 4.5339F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r574 = cone72.addOrReplaceChild("cube_r574", CubeListBuilder.create().texOffs(22, 42).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.9802F, 4.1123F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r575 = cone72.addOrReplaceChild("cube_r575", CubeListBuilder.create().texOffs(26, 42).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.4779F, 3.9658F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r576 = cone72.addOrReplaceChild("cube_r576", CubeListBuilder.create().texOffs(30, 42).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0, 1.5, 0);
		poseStack.scale(1.0F, -1.0F, 1.0F);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
