package com.onewhohears.dscombat.client.renderer.model.aircraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.renderer.model.EntityControllableModel;
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

public class EntityModelE3Sentry extends EntityControllableModel<EntityPlane> {
	
public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "e3sentry_plane"), "main");
	
	private final ModelPart body;
	
	public EntityModelE3Sentry(ModelPart root) {
		this.body = root.getChild("body");
		
	}
	
	@Override
	public void renderToBuffer(EntityPlane entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer, 
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition fuselage = body.addOrReplaceChild("fuselage", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wing = fuselage.addOrReplaceChild("wing", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_wing = wing.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 43).addBox(-41.0F, -1.0F, -7.0F, 32.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_wing = wing.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(86, 0).addBox(9.0F, -1.0F, -7.0F, 32.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frame = fuselage.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.0F, -40.0F, 12.0F, 12.0F, 31.0F, new CubeDeformation(0.0F))
		.texOffs(92, 43).addBox(-9.0F, 5.0F, -8.0F, 18.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(86, 83).addBox(8.0F, -6.0F, -8.0F, 1.0F, 11.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(52, 83).addBox(-9.0F, -6.0F, -8.0F, 1.0F, 11.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 123).addBox(-9.0F, -6.0F, -9.0F, 18.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 110).addBox(-9.0F, -6.0F, 8.0F, 18.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(66, 60).addBox(-6.0F, -6.0F, 9.0F, 12.0F, 12.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail = fuselage.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 83).addBox(-22.0F, -1.0F, -40.0F, 16.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(112, 60).addBox(6.0F, -1.0F, -40.0F, 16.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(38, 110).addBox(-0.5F, -18.0F, -40.0F, 1.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

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
		.texOffs(0, 60).addBox(-16.0F, -1.0F, 2.0F, 32.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(60, 110).addBox(-1.0F, -16.0F, 2.0F, 2.0F, 32.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 20.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}
	
}
