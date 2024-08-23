package com.onewhohears.dscombat.client.model.vehicle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.model.EntityControllableModel;
import com.onewhohears.dscombat.entity.vehicle.EntityPlane;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@Deprecated
public class EntityModelJaviPlane extends EntityControllableModel<EntityPlane> {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "javi_plane"), "main");
	
	private final ModelPart root;
	private final ModelPart gfront, gleft, gright;
	private final ModelPart stick;

	public EntityModelJaviPlane(ModelPart root) {
		this.root = root;
		this.gfront = root.getChild("gear").getChild("gfront");
		this.gleft = root.getChild("gear").getChild("gleft");
		this.gright = root.getChild("gear").getChild("gright");
		this.stick = root.getChild("seat").getChild("stick");
	}
	
	@Override
	public void renderToBuffer(EntityPlane entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer,
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0, 1.20, 0);
		poseStack.scale(-1.0F, -1.0F, 1.0F);
		float gear = entity.getLandingGearPos(partialTicks);
		if (gear < 1) {
			float hpi = Mth.PI/2;
			this.gfront.xRot = gear * -hpi;
			this.gleft.xRot = gear * hpi;
			this.gright.xRot = gear * hpi;
			this.gfront.visible = true;
			this.gleft.visible = true;
			this.gright.visible = true;
		} else {
			this.gfront.visible = false;
			this.gleft.visible = false;
			this.gright.visible = false;
		}
		float ypi = Mth.PI/8;
		float ppi = Mth.PI/12;
		this.stick.zRot = entity.inputs.yaw * -ypi;
		this.stick.xRot = entity.inputs.pitch * ppi;
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition fuselage = partdefinition.addOrReplaceChild("fuselage", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = fuselage.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 105).addBox(-9.0F, -10.0F, -18.0F, 18.0F, 16.0F, 51.0F, new CubeDeformation(0.0F))
		.texOffs(93, 16).addBox(9.0F, -10.0F, -18.0F, 1.5663F, 16.0F, 89.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-10.5663F, -10.0F, -18.0F, 1.5663F, 16.0F, 89.0F, new CubeDeformation(0.0F))
		.texOffs(241, 31).addBox(-9.0F, 5.0F, 33.0F, 18.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(256, 131).addBox(8.0F, -10.0F, 33.0F, 1.0F, 15.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(93, 14).addBox(-9.0F, -10.0F, 33.0F, 1.0F, 15.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(151, 263).addBox(-8.0F, -10.0F, 48.0F, 16.0F, 15.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(127, 14).addBox(-9.0F, -10.0F, 65.0F, 18.0F, 16.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(93, 31).addBox(-7.0249F, -6.3534F, 89.3668F, 14.0F, 8.7068F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(172, 267).addBox(-0.5F, -0.5F, -8.5F, 11.0F, 9.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.6354F, -8.0316F, 79.0714F, -0.1745F, 0.1745F, 0.0F));

		PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(236, 267).addBox(-10.5F, -0.5F, -8.5F, 11.0F, 9.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.6354F, -8.0316F, 79.0714F, -0.1745F, -0.1745F, 0.0F));

		PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(108, 263).addBox(-10.5F, -8.5F, -8.5F, 11.0F, 9.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.6354F, 4.0316F, 79.0714F, 0.1745F, -0.1745F, 0.0F));

		PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(279, 276).addBox(-0.5F, -8.5F, -8.5F, 11.0F, 9.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.6354F, 4.0316F, 79.0714F, 0.1745F, 0.1745F, 0.0F));

		PartDefinition cube_r5 = body.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(100, 209).addBox(-3.5663F, 2.0F, -64.0F, 4.5663F, 7.5232F, 45.7606F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.396F, -1.8973F, 1.0255F, -0.0873F, 0.0436F, 0.0F));

		PartDefinition cube_r6 = body.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(202, 213).addBox(-1.0F, 2.0F, -64.0F, 4.5663F, 7.5232F, 45.7606F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.396F, -1.8973F, 1.0255F, -0.0873F, -0.0436F, 0.0F));

		PartDefinition canopy = body.addOrReplaceChild("canopy", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r7 = canopy.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(57, 442).addBox(9.0F, -3.0F, -1.5F, 1.0F, 9.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(298, 498).addBox(-9.0F, -3.0F, -1.5F, 1.0F, 9.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(137, 502).addBox(-9.0F, -3.0F, 2.5F, 18.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.4288F, 33.7409F, 2.3998F, 0.0F, 0.0F));

		PartDefinition cube_r8 = canopy.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(237, 475).addBox(9.0F, -31.0F, -4.0F, 1.0F, 25.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(56, 475).addBox(-9.0F, -31.0F, -4.0F, 1.0F, 25.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 453).addBox(-9.0F, -31.0F, 7.0F, 18.0F, 25.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -15.513F, 67.2282F, 1.8326F, 0.0F, 0.0F));

		PartDefinition cube_r9 = canopy.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(196, 491).addBox(9.0F, -9.0F, -8.0F, 1.0F, 16.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(100, 491).addBox(-9.0F, -9.0F, -8.0F, 1.0F, 16.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 495).addBox(-9.0F, -9.0F, -4.0F, 18.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0358F, 66.9817F, 0.6109F, 0.0F, 0.0F));

		PartDefinition nose = fuselage.addOrReplaceChild("nose", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 14.0F));

		PartDefinition wing = fuselage.addOrReplaceChild("wing", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left = wing.addOrReplaceChild("left", CubeListBuilder.create().texOffs(186, 0).addBox(-44.465F, -2.7107F, -14.4F, 36.0F, 2.4F, 28.8F, new CubeDeformation(0.0F))
		.texOffs(0, 172).addBox(8.465F, -2.7107F, -14.4F, 36.0F, 2.4F, 28.8F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r10 = left.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(93, 7).addBox(-31.2F, -1.8F, -9.6F, 56.4F, 2.4F, 4.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(74.6468F, -3.5489F, -2.1173F, 0.0F, -0.0873F, -0.0873F));

		PartDefinition cube_r11 = left.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(87, 121).addBox(0.0F, -2.4F, -14.4F, 56.4F, 2.4F, 19.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(44.5696F, -0.3152F, 4.8F, 0.0F, 0.0F, -0.0873F));

		PartDefinition cube_r12 = left.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(93, 0).addBox(-31.2F, -1.8F, 4.8F, 56.4F, 2.4F, 4.8F, new CubeDeformation(0.0F))
		.texOffs(247, 101).addBox(-31.3218F, -1.8F, -18.2F, 6.4F, 2.4F, 27.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(74.6468F, -3.5489F, 2.1173F, 0.0F, 0.0873F, -0.0873F));

		PartDefinition cube_r13 = left.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(101, 172).addBox(-1.8F, -1.8F, -4.8F, 3.6F, 2.4F, 14.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(100.5975F, -5.8193F, -0.1667F, 0.0F, -0.1745F, -0.0873F));

		PartDefinition cube_r14 = left.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(138, 151).addBox(56.2995F, -2.4F, -14.6188F, 2.5005F, 2.4F, 5.0188F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(44.6698F, -0.324F, 5.0188F, 0.0F, 0.0F, -0.0873F));

		PartDefinition cube_r15 = left.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(258, 213).addBox(-3.2F, 10.8F, -13.9F, 6.4F, 2.4F, 27.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(45.2834F, -13.6327F, -0.274F, 0.0F, -0.0873F, -0.0873F));

		PartDefinition cube_r16 = left.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 259).addBox(-3.2F, 10.8F, -13.9F, 6.4F, 2.4F, 27.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-45.2834F, -13.6327F, -0.274F, 0.0F, 0.0873F, 0.0873F));

		PartDefinition cube_r17 = left.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(40, 261).addBox(24.9218F, -1.8F, -18.2F, 6.4F, 2.4F, 27.8F, new CubeDeformation(0.0F))
		.texOffs(186, 94).addBox(-25.2F, -1.8F, 4.8F, 56.4F, 2.4F, 4.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-74.6468F, -3.5489F, 2.1173F, 0.0F, -0.0873F, 0.0873F));

		PartDefinition cube_r18 = left.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(186, 87).addBox(-25.2F, -1.8F, -9.6F, 56.4F, 2.4F, 4.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-74.6468F, -3.5489F, -2.1173F, 0.0F, 0.0873F, 0.0873F));

		PartDefinition cube_r19 = left.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(218, 121).addBox(-1.8F, -1.8F, -4.8F, 3.6F, 2.4F, 14.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-100.5975F, -5.8193F, -0.1667F, 0.0F, 0.1745F, 0.0873F));

		PartDefinition cube_r20 = left.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(186, 22).addBox(-58.8F, -2.4F, -14.6188F, 2.5005F, 2.4F, 5.0188F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-44.6698F, -0.324F, 5.0188F, 0.0F, 0.0F, 0.0873F));

		PartDefinition cube_r21 = left.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(138, 142).addBox(-56.4F, -2.4F, -14.4F, 56.4F, 2.4F, 19.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-44.5696F, -0.3152F, 4.8F, 0.0F, 0.0F, 0.0873F));

		PartDefinition right = wing.addOrReplaceChild("right", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail = fuselage.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(93, 163).addBox(-8.6021F, -10.0F, -62.9572F, 17.18F, 1.0F, 44.9572F, new CubeDeformation(0.0F))
		.texOffs(0, 203).addBox(-32.55F, -28.4F, -60.05F, 1.05F, 26.25F, 16.8F, new CubeDeformation(0.0F))
		.texOffs(241, 178).addBox(-31.5F, -6.35F, -59.0F, 25.2F, 1.05F, 15.75F, new CubeDeformation(0.0F))
		.texOffs(186, 31).addBox(31.5F, -28.4F, -60.05F, 1.05F, 26.25F, 16.8F, new CubeDeformation(0.0F))
		.texOffs(156, 230).addBox(6.3F, -6.35F, -59.0F, 25.2F, 1.05F, 15.75F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r22 = tail.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(344, 11).addBox(-0.525F, -12.075F, -7.35F, 1.05F, 25.2F, 16.8F, new CubeDeformation(0.0F))
		.texOffs(0, 46).addBox(-0.525F, 13.125F, 5.25F, 1.05F, 1.05F, 4.2F, new CubeDeformation(0.0F))
		.texOffs(0, 345).addBox(-64.575F, -12.075F, -7.35F, 1.05F, 25.2F, 16.8F, new CubeDeformation(0.0F))
		.texOffs(78, 26).addBox(-64.575F, 13.125F, 5.25F, 1.05F, 1.05F, 4.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(32.025F, -15.5473F, -51.6116F, 0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r23 = tail.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(30, 84).addBox(-0.525F, 2.625F, -1.575F, 1.05F, 3.15F, 2.1F, new CubeDeformation(0.0F))
		.texOffs(36, 84).addBox(-64.575F, 2.625F, -1.575F, 1.05F, 3.15F, 2.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(32.025F, -4.652F, -39.7807F, -0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r24 = tail.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(44, 42).addBox(-0.525F, -1.05F, -9.45F, 1.05F, 2.1F, 17.85F, new CubeDeformation(0.0F))
		.texOffs(44, 62).addBox(-64.575F, -1.05F, -9.45F, 1.05F, 2.1F, 17.85F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(32.025F, -1.8883F, -51.0708F, -0.1309F, 0.0F, 0.0F));

		PartDefinition cube_r25 = tail.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(0, 105).addBox(-0.525F, -12.075F, -7.35F, 1.05F, 26.25F, 16.8F, new CubeDeformation(0.0F))
		.texOffs(172, 163).addBox(-64.575F, -12.075F, -7.35F, 1.05F, 26.25F, 16.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(32.025F, -15.7304F, -53.7804F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r26 = tail.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(55, 209).addBox(-12.6F, -0.525F, -7.875F, 26.25F, 1.05F, 15.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.7239F, -5.825F, -53.4326F, 0.0F, 0.1745F, 0.0F));

		PartDefinition cube_r27 = tail.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(55, 226).addBox(-13.65F, -0.525F, -7.875F, 26.25F, 1.05F, 15.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.7239F, -5.825F, -53.4326F, 0.0F, -0.1745F, 0.0F));

		PartDefinition cube_r28 = tail.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(171, 163).addBox(-6.0F, -6.0F, -11.75F, 12.0F, 4.0F, 45.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, -52.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r29 = tail.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(258, 243).addBox(-8.0256F, -7.6002F, -1.0F, 17.2069F, 11.6002F, 3.1718F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5779F, -2.1099F, -63.4325F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r30 = tail.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(186, 31).addBox(-2.0F, -8.1027F, -64.0F, 4.5663F, 11.0F, 45.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.397F, -1.8973F, 1.0692F, 0.0F, -0.0436F, 0.0F));

		PartDefinition cube_r31 = tail.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(0, 203).addBox(-2.5663F, -8.1027F, -64.0F, 4.5663F, 11.0F, 45.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.397F, -1.8973F, 1.0692F, 0.0F, 0.0436F, 0.0F));

		PartDefinition engine = fuselage.addOrReplaceChild("engine", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition eleft = engine.addOrReplaceChild("eleft", CubeListBuilder.create(), PartPose.offsetAndRotation(-9.0F, -10.0F, -24.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition eright = engine.addOrReplaceChild("eright", CubeListBuilder.create(), PartPose.offsetAndRotation(9.0F, -10.0F, -24.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition seat = partdefinition.addOrReplaceChild("seat", CubeListBuilder.create().texOffs(241, 195).addBox(-6.0F, 4.0F, 34.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(19, 105).addBox(-6.0F, -11.0F, 34.0F, 12.0F, 15.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(87, 142).addBox(-6.0F, 4.0F, 50.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(93, 14).addBox(-6.0F, -11.0F, 50.0F, 12.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, -1.0F));

		PartDefinition stick = seat.addOrReplaceChild("stick", CubeListBuilder.create().texOffs(87, 105).addBox(-0.5F, -8.0F, 8.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 42).addBox(-1.0F, -10.0F, 8.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 54.5F));

		PartDefinition window = seat.addOrReplaceChild("window", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, 25.0F));

		PartDefinition gear = partdefinition.addOrReplaceChild("gear", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition gfront = gear.addOrReplaceChild("gfront", CubeListBuilder.create().texOffs(47, 105).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(203, 17).addBox(-1.5F, 4.0F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(125, 198).addBox(0.5F, 4.0F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 51.5F));

		PartDefinition gleft = gear.addOrReplaceChild("gleft", CubeListBuilder.create().texOffs(82, 42).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(17, 189).addBox(-1.5F, 10.0F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(123, 172).addBox(0.5F, 10.0F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-31.5F, 0.0F, -3.5F));

		PartDefinition gright = gear.addOrReplaceChild("gright", CubeListBuilder.create().texOffs(82, 9).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(87, 142).addBox(-1.5F, 10.0F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(36, 136).addBox(0.5F, 10.0F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(30.5F, 0.0F, -3.5F));

		PartDefinition cube_r32 = gright.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(82, 57).addBox(-1.5F, -10.0F, 0.5F, 1.0F, 13.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(84, 71).addBox(60.5F, -10.0F, 0.5F, 1.0F, 13.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-61.0F, 7.0F, 0.0F, -0.3491F, 0.0F, 0.0F));

		PartDefinition hexadecagon = partdefinition.addOrReplaceChild("hexadecagon", CubeListBuilder.create().texOffs(322, 56).addBox(12.7103F, -12.537F, -3.5896F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(325, 118).addBox(31.0401F, -12.537F, -3.5896F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(287, 74).addBox(20.5766F, -20.4033F, -3.5896F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(70, 292).addBox(20.5766F, -2.0734F, -3.5896F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(314, 224).addBox(-12.4831F, -2.0734F, -3.5896F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(298, 196).addBox(-12.4831F, -20.4033F, -3.5896F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(261, 334).addBox(-2.0196F, -12.537F, -3.5896F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(232, 326).addBox(-20.3494F, -12.537F, -3.5896F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 16.0F, -42.0F));

		PartDefinition hexadecagon_r1 = hexadecagon.addOrReplaceChild("hexadecagon_r1", CubeListBuilder.create().texOffs(186, 31).addBox(-16.572F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(44, 31).addBox(1.0652F, -0.9183F, -4.8983F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(0, 49).addBox(-12.5247F, -0.9396F, -27.1521F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, 0.0F, -0.3491F, 0.3927F));

		PartDefinition hexadecagon_r2 = hexadecagon.addOrReplaceChild("hexadecagon_r2", CubeListBuilder.create().texOffs(127, 36).addBox(-8.8196F, -3.9532F, -1.4742F, 17.3093F, 7.9065F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(176, 325).addBox(-9.8196F, -1.9532F, -21.4742F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(333, 180).addBox(8.5103F, -1.9532F, -21.4742F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(64, 62).addBox(-3.9532F, -6.8196F, -27.5258F, 7.9065F, 13.3093F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(123, 142).addBox(-6.8196F, -3.9532F, -27.4742F, 13.3093F, 7.9065F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(140, 297).addBox(-1.9532F, -9.8196F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(44, 62).addBox(-3.9532F, -8.8196F, -1.5258F, 7.9065F, 17.3093F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(309, 28).addBox(-1.9532F, 8.5103F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, 0.0F, 0.0F, 0.3927F));

		PartDefinition hexadecagon_r3 = hexadecagon.addOrReplaceChild("hexadecagon_r3", CubeListBuilder.create().texOffs(172, 163).addBox(-16.572F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(16, 31).addBox(1.1754F, -0.9408F, -4.9384F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(48, 9).addBox(-12.4106F, -0.9408F, -27.1936F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, 0.0F, -0.3491F, 0.0F));

		PartDefinition hexadecagon_r4 = hexadecagon.addOrReplaceChild("hexadecagon_r4", CubeListBuilder.create().texOffs(55, 214).addBox(-16.572F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(58, 1).addBox(1.2853F, -0.9167F, -4.9784F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(22, 48).addBox(-12.3046F, -0.8954F, -27.2322F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, 0.0F, -0.3491F, -0.3927F));

		PartDefinition hexadecagon_r5 = hexadecagon.addOrReplaceChild("hexadecagon_r5", CubeListBuilder.create().texOffs(331, 149).addBox(-9.8196F, -1.9532F, -21.4742F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(0, 179).addBox(-6.4897F, -2.9532F, -27.5258F, 13.3093F, 5.9065F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 105).addBox(-2.9532F, -6.8196F, -27.4742F, 5.9065F, 13.3093F, 0.9484F, new CubeDeformation(0.0F))
		.texOffs(0, 148).addBox(-8.4897F, -2.9532F, -1.5258F, 17.3093F, 5.9065F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(317, 335).addBox(8.5103F, -1.9532F, -21.4742F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(87, 121).addBox(-2.9532F, -8.8196F, -1.4742F, 5.9065F, 17.3093F, 0.9484F, new CubeDeformation(0.0F))
		.texOffs(237, 298).addBox(-1.9532F, -9.8196F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(316, 279).addBox(-1.9532F, 8.5103F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, 0.0F, 0.0F, -0.3927F));

		PartDefinition hexadecagon_r6 = hexadecagon.addOrReplaceChild("hexadecagon_r6", CubeListBuilder.create().texOffs(156, 230).addBox(15.2627F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(0, 29).addBox(-1.7072F, -0.9183F, -4.9771F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(0, 47).addBox(11.9792F, -0.9396F, -27.1957F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, 0.0F, 0.3491F, 0.3927F));

		PartDefinition hexadecagon_r7 = hexadecagon.addOrReplaceChild("hexadecagon_r7", CubeListBuilder.create().texOffs(156, 209).addBox(15.2627F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(0, 31).addBox(-1.5971F, -0.9408F, -4.937F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(48, 8).addBox(12.0934F, -0.9408F, -27.1542F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, 0.0F, 0.3491F, 0.0F));

		PartDefinition hexadecagon_r8 = hexadecagon.addOrReplaceChild("hexadecagon_r8", CubeListBuilder.create().texOffs(231, 234).addBox(15.2627F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(16, 29).addBox(-1.4872F, -0.9167F, -4.897F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(0, 46).addBox(12.1993F, -0.8954F, -27.1156F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, 0.0F, 0.3491F, -0.3927F));

		PartDefinition hexadecagon_r9 = hexadecagon.addOrReplaceChild("hexadecagon_r9", CubeListBuilder.create().texOffs(205, 39).addBox(-1.9532F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(36, 0).addBox(-0.8497F, 0.9688F, -4.8632F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(32, 38).addBox(-0.8104F, -12.7064F, -27.0859F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, 0.3491F, 0.0F, 0.7854F));

		PartDefinition hexadecagon_r10 = hexadecagon.addOrReplaceChild("hexadecagon_r10", CubeListBuilder.create().texOffs(202, 297).addBox(-1.9532F, -9.8196F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(307, 307).addBox(-1.9532F, 8.5103F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, 0.0F, 0.0F, 0.7854F));

		PartDefinition hexadecagon_r11 = hexadecagon.addOrReplaceChild("hexadecagon_r11", CubeListBuilder.create().texOffs(205, 31).addBox(-1.9532F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(6, 42).addBox(-0.75F, 0.9071F, -4.8408F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(24, 42).addBox(-0.6986F, -12.7511F, -27.0697F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, 0.3491F, 0.0F, 0.3927F));

		PartDefinition hexadecagon_r12 = hexadecagon.addOrReplaceChild("hexadecagon_r12", CubeListBuilder.create().texOffs(40, 0).addBox(-0.6327F, -1.8865F, -5.0424F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(4, 42).addBox(-0.5772F, 11.7517F, -27.2785F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(123, 226).addBox(-1.9532F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(224, 213).addBox(31.1065F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(34, 36).addBox(32.3673F, 11.7517F, -27.2785F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(30, 24).addBox(32.4228F, -1.8865F, -5.0424F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, -0.3491F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r13 = hexadecagon.addOrReplaceChild("hexadecagon_r13", CubeListBuilder.create().texOffs(30, 42).addBox(-0.75F, -1.8654F, -5.0347F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(0, 46).addBox(-0.6986F, 11.7528F, -27.2781F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(158, 78).addBox(-1.9532F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, -0.3491F, 0.0F, 0.3927F));

		PartDefinition hexadecagon_r14 = hexadecagon.addOrReplaceChild("hexadecagon_r14", CubeListBuilder.create().texOffs(14, 42).addBox(-0.8497F, -1.8037F, -5.0122F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(44, 10).addBox(-0.8104F, 11.7976F, -27.2618F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(101, 188).addBox(-1.9532F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, -0.3491F, 0.0F, 0.7854F));

		PartDefinition hexadecagon_r15 = hexadecagon.addOrReplaceChild("hexadecagon_r15", CubeListBuilder.create().texOffs(12, 42).addBox(-0.417F, 0.9715F, -4.8642F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(24, 44).addBox(-0.3777F, -12.6297F, -27.1138F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(159, 46).addBox(-1.9532F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, 0.3491F, 0.0F, -0.7854F));

		PartDefinition hexadecagon_r16 = hexadecagon.addOrReplaceChild("hexadecagon_r16", CubeListBuilder.create().texOffs(46, 0).addBox(-0.5158F, 0.9086F, -4.8413F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(31, 44).addBox(-0.4644F, -12.7096F, -27.0848F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(0, 186).addBox(-1.9532F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, 0.3491F, 0.0F, -0.3927F));

		PartDefinition hexadecagon_r17 = hexadecagon.addOrReplaceChild("hexadecagon_r17", CubeListBuilder.create().texOffs(38, 0).addBox(-0.6327F, 0.8859F, -4.8331F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(42, 8).addBox(-0.5772F, -12.7523F, -27.0692F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(19, 211).addBox(-1.9532F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(19, 203).addBox(31.1065F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(30, 38).addBox(32.3673F, -12.7523F, -27.0692F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(30, 16).addBox(32.4228F, 0.8859F, -4.8331F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, 0.3491F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r18 = hexadecagon.addOrReplaceChild("hexadecagon_r18", CubeListBuilder.create().texOffs(30, 32).addBox(-0.417F, -1.8009F, -5.0112F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(34, 38).addBox(-0.3777F, 11.8742F, -27.234F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(123, 209).addBox(-1.9532F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, -0.3491F, 0.0F, -0.7854F));

		PartDefinition hexadecagon_r19 = hexadecagon.addOrReplaceChild("hexadecagon_r19", CubeListBuilder.create().texOffs(2, 42).addBox(-0.5158F, -1.8639F, -5.0341F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(31, 42).addBox(-0.4644F, 11.7943F, -27.263F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(224, 221).addBox(-1.9532F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, -0.3491F, 0.0F, -0.3927F));

		PartDefinition hexadecagon_r20 = hexadecagon.addOrReplaceChild("hexadecagon_r20", CubeListBuilder.create().texOffs(272, 306).addBox(-1.9532F, -9.8196F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(0, 42).addBox(-2.9532F, 9.5103F, -26.4742F, 1.9065F, 2.3093F, 39.9484F, new CubeDeformation(0.0F))
		.texOffs(0, 317).addBox(-1.9532F, 8.5103F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, 0.0F, 0.0F, -0.7854F));

		PartDefinition hexadecagon_r21 = hexadecagon.addOrReplaceChild("hexadecagon_r21", CubeListBuilder.create().texOffs(10, 84).addBox(-2.9532F, 25.4449F, -11.9953F, 1.9065F, 2.3093F, 2.9484F, new CubeDeformation(0.0F))
		.texOffs(20, 84).addBox(-2.9532F, -2.8029F, 16.2525F, 1.9065F, 2.3093F, 2.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -10.5837F, 17.8847F, -0.7854F, 0.0F, -0.7854F));

		PartDefinition hexadecagon_r22 = hexadecagon.addOrReplaceChild("hexadecagon_r22", CubeListBuilder.create().texOffs(296, 168).addBox(-1.9532F, 8.5103F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(290, 104).addBox(-1.9532F, -9.8196F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(1.0468F, 9.5103F, -26.4742F, 1.9065F, 2.3093F, 39.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, 0.0F, 0.0F, 0.7854F));

		PartDefinition hexadecagon_r23 = hexadecagon.addOrReplaceChild("hexadecagon_r23", CubeListBuilder.create().texOffs(105, 293).addBox(-1.9532F, 8.5103F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(0, 289).addBox(-1.9532F, -9.8196F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(120, 325).addBox(8.5103F, -1.9532F, -21.4742F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(26, 63).addBox(-2.9532F, -8.8196F, -1.4742F, 5.9065F, 17.3093F, 0.9484F, new CubeDeformation(0.0F))
		.texOffs(129, 78).addBox(-8.8196F, -2.9532F, -1.5258F, 17.3093F, 5.9065F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 172).addBox(-6.8196F, -2.9532F, -27.5258F, 13.3093F, 5.9065F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(93, 61).addBox(-2.9532F, -6.8196F, -27.4742F, 5.9065F, 13.3093F, 0.9484F, new CubeDeformation(0.0F))
		.texOffs(35, 319).addBox(-9.8196F, -1.9532F, -21.4742F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, 0.0F, 0.0F, 0.3927F));

		PartDefinition hexadecagon_r24 = hexadecagon.addOrReplaceChild("hexadecagon_r24", CubeListBuilder.create().texOffs(35, 291).addBox(-1.9532F, 8.5103F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(44, 42).addBox(-3.9532F, -8.8196F, -1.5258F, 7.9065F, 17.3093F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(325, 87).addBox(8.5103F, -1.9532F, -21.4742F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(93, 78).addBox(-8.4897F, -3.9532F, -1.4742F, 17.3093F, 7.9065F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(279, 243).addBox(-1.9532F, -9.8196F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(64, 42).addBox(-3.9532F, -6.8196F, -27.5258F, 7.9065F, 13.3093F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(58, 0).addBox(-6.4897F, -3.9532F, -27.4742F, 13.3093F, 7.9065F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(91, 321).addBox(-9.8196F, -1.9532F, -21.4742F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, 0.0F, 0.0F, -0.3927F));

		PartDefinition hexadecagon_r25 = hexadecagon.addOrReplaceChild("hexadecagon_r25", CubeListBuilder.create().texOffs(290, 132).addBox(-1.9532F, 8.5103F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(287, 0).addBox(-1.9532F, -9.8196F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, 0.0F, 0.0F, -0.7854F));

		PartDefinition hexadecagon_r26 = hexadecagon.addOrReplaceChild("hexadecagon_r26", CubeListBuilder.create().texOffs(156, 38).addBox(-1.9532F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(26, 44).addBox(-0.8052F, -12.7096F, -27.0848F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(44, 0).addBox(-0.7538F, 0.9086F, -4.8413F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, 0.3491F, 0.0F, 0.3927F));

		PartDefinition hexadecagon_r27 = hexadecagon.addOrReplaceChild("hexadecagon_r27", CubeListBuilder.create().texOffs(29, 148).addBox(-1.9532F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(44, 8).addBox(-0.8919F, -12.6297F, -27.1138F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(8, 42).addBox(-0.8526F, 0.9715F, -4.8642F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, 0.3491F, 0.0F, 0.7854F));

		PartDefinition hexadecagon_r28 = hexadecagon.addOrReplaceChild("hexadecagon_r28", CubeListBuilder.create().texOffs(161, 65).addBox(15.2627F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(18, 48).addBox(12.0992F, -0.9396F, -27.1521F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(44, 29).addBox(-1.4907F, -0.9183F, -4.8983F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, 0.0F, 0.3491F, -0.3927F));

		PartDefinition hexadecagon_r29 = hexadecagon.addOrReplaceChild("hexadecagon_r29", CubeListBuilder.create().texOffs(161, 54).addBox(15.2627F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(48, 7).addBox(11.985F, -0.9408F, -27.1936F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(16, 30).addBox(-1.6009F, -0.9408F, -4.9384F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, 0.0F, 0.3491F, 0.0F));

		PartDefinition hexadecagon_r30 = hexadecagon.addOrReplaceChild("hexadecagon_r30", CubeListBuilder.create().texOffs(55, 203).addBox(15.2627F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(14, 48).addBox(11.8791F, -0.8954F, -27.2322F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(44, 30).addBox(-1.7108F, -0.9167F, -4.9784F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, 0.0F, 0.3491F, 0.3927F));

		PartDefinition hexadecagon_r31 = hexadecagon.addOrReplaceChild("hexadecagon_r31", CubeListBuilder.create().texOffs(186, 14).addBox(-1.9532F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(33, 42).addBox(-0.4592F, 11.7976F, -27.2618F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(10, 42).addBox(-0.4199F, -1.8037F, -5.0122F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, -0.3491F, 0.0F, -0.7854F));

		PartDefinition hexadecagon_r32 = hexadecagon.addOrReplaceChild("hexadecagon_r32", CubeListBuilder.create().texOffs(63, 30).addBox(-1.9532F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(33, 44).addBox(-0.571F, 11.7528F, -27.2781F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(16, 42).addBox(-0.5196F, -1.8654F, -5.0347F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, -0.3491F, 0.0F, -0.3927F));

		PartDefinition hexadecagon_r33 = hexadecagon.addOrReplaceChild("hexadecagon_r33", CubeListBuilder.create().texOffs(123, 217).addBox(-1.9532F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(26, 42).addBox(-0.8052F, 11.7943F, -27.263F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(0, 42).addBox(-0.7538F, -1.8639F, -5.0341F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, -0.3491F, 0.0F, 0.3927F));

		PartDefinition hexadecagon_r34 = hexadecagon.addOrReplaceChild("hexadecagon_r34", CubeListBuilder.create().texOffs(191, 171).addBox(-1.9532F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(32, 36).addBox(-0.8919F, 11.8742F, -27.234F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(30, 0).addBox(-0.8526F, -1.8009F, -5.0112F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, -0.3491F, 0.0F, 0.7854F));

		PartDefinition hexadecagon_r35 = hexadecagon.addOrReplaceChild("hexadecagon_r35", CubeListBuilder.create().texOffs(55, 226).addBox(-16.572F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(33, 44).addBox(-12.4047F, -0.9396F, -27.1957F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(0, 28).addBox(1.2817F, -0.9183F, -4.9771F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, 0.0F, -0.3491F, -0.3927F));

		PartDefinition hexadecagon_r36 = hexadecagon.addOrReplaceChild("hexadecagon_r36", CubeListBuilder.create().texOffs(0, 203).addBox(-16.572F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(48, 6).addBox(-12.5189F, -0.9408F, -27.1542F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(0, 30).addBox(1.1715F, -0.9408F, -4.937F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, 0.0F, -0.3491F, 0.0F));

		PartDefinition hexadecagon_r37 = hexadecagon.addOrReplaceChild("hexadecagon_r37", CubeListBuilder.create().texOffs(222, 230).addBox(-16.572F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(26, 44).addBox(-12.6248F, -0.8954F, -27.1156F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(16, 28).addBox(1.0617F, -0.9167F, -4.897F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, 0.0F, -0.3491F, 0.3927F));

		PartDefinition hexadecagon_r38 = hexadecagon.addOrReplaceChild("hexadecagon_r38", CubeListBuilder.create().texOffs(191, 163).addBox(-1.9532F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(30, 36).addBox(-0.4592F, -12.7064F, -27.0859F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(30, 8).addBox(-0.4199F, 0.9688F, -4.8632F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, 0.3491F, 0.0F, -0.7854F));

		PartDefinition hexadecagon_r39 = hexadecagon.addOrReplaceChild("hexadecagon_r39", CubeListBuilder.create().texOffs(116, 189).addBox(-1.9532F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(42, 10).addBox(-0.571F, -12.7511F, -27.0697F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(42, 0).addBox(-0.5196F, 0.9071F, -4.8408F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, 0.3491F, 0.0F, -0.3927F));

		PartDefinition hexadecagon_r40 = hexadecagon.addOrReplaceChild("hexadecagon_r40", CubeListBuilder.create().texOffs(0, 84).addBox(1.0468F, -2.8029F, 16.2525F, 1.9065F, 2.3093F, 2.9484F, new CubeDeformation(0.0F))
		.texOffs(78, 31).addBox(1.0468F, 25.4449F, -11.9953F, 1.9065F, 2.3093F, 2.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -10.5837F, 17.8847F, -0.7854F, 0.0F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}
	
}
