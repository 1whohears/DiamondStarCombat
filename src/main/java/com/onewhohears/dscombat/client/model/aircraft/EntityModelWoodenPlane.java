package com.onewhohears.dscombat.client.model.aircraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.model.EntityControllableModel;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;

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

public class EntityModelWoodenPlane extends EntityControllableModel<EntityPlane> {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "wooden_plane"), "main");
	
	private final ModelPart body;
	private final ModelPart propeller;
	private final ModelPart front_gear;
	private final ModelPart back_gear;

	public EntityModelWoodenPlane(ModelPart root) {
		this.body = root.getChild("body");
		this.propeller = body.getChild("propeller");
		this.front_gear = body.getChild("gear").getChild("front_gear");
		this.back_gear = body.getChild("gear").getChild("back_gear");
	}
	
	@Override
	public void renderToBuffer(EntityPlane entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer, 
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0, 1.5, 0);
		poseStack.scale(1.0F, -1.0F, 1.0F);
		propeller.zRot = entity.getMotorRotation(partialTicks, 8);
		float gear = entity.getLandingGearPos(partialTicks);
		float hpi = Mth.PI/2;
		back_gear.xRot = gear * hpi;
		front_gear.xRot = gear * -hpi;
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition fuselage = body.addOrReplaceChild("fuselage", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wing = fuselage.addOrReplaceChild("wing", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_wing = wing.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(91, 17).addBox(-41.0F, -1.0F, -7.0F, 32.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_wing = wing.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(91, 1).addBox(9.0F, -1.0F, -7.0F, 32.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frame = fuselage.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.0F, -40.0F, 12.0F, 12.0F, 31.0F, new CubeDeformation(0.0F))
		.texOffs(101, 35).addBox(-9.0F, 5.0F, -8.0F, 18.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(87, 77).addBox(8.0F, -6.0F, -8.0F, 1.0F, 11.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(49, 78).addBox(-9.0F, -6.0F, -8.0F, 1.0F, 11.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(7, 96).addBox(-9.0F, -6.0F, -9.0F, 18.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(7, 79).addBox(-9.0F, -6.0F, 8.0F, 18.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(61, 47).addBox(-6.0F, -6.0F, 9.0F, 12.0F, 12.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail = fuselage.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(4, 48).addBox(-22.0F, -1.0F, -40.0F, 16.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(5, 62).addBox(6.0F, -1.0F, -40.0F, 16.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(116, 58).addBox(-0.5F, -18.0F, -40.0F, 1.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition seat = body.addOrReplaceChild("seat", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition gear = body.addOrReplaceChild("gear", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition front_gear = gear.addOrReplaceChild("front_gear", CubeListBuilder.create().texOffs(0, 143).addBox(-6.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(58, 143).addBox(5.0F, 0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(34, 143).addBox(4.5F, 4.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(54, 143).addBox(-6.0F, 0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 143).addBox(-6.5F, 4.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.5F, 4.5F));

		PartDefinition back_gear = gear.addOrReplaceChild("back_gear", CubeListBuilder.create().texOffs(50, 143).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 145).addBox(-1.0F, 5.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -26.5F));

		PartDefinition propeller = body.addOrReplaceChild("propeller", CubeListBuilder.create().texOffs(42, 143).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(6, 115).addBox(-16.0F, -1.0F, 2.0F, 32.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(83, 116).addBox(-1.0F, -16.0F, 2.0F, 2.0F, 32.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 20.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

}
