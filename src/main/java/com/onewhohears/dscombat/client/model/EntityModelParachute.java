package com.onewhohears.dscombat.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.EntityParachute;

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

public class EntityModelParachute extends EntityModel<EntityParachute> {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "parachute"), "main");
	private final ModelPart main;
	
	public EntityModelParachute(ModelPart root) {
		this.main = root.getChild("main");
	}
	
	@Override
	public void setupAnim(EntityParachute pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks,
			float pNetHeadYaw, float pHeadPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0, 1.50, 0);
		poseStack.scale(1.0F, -1.0F, 1.0F);
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition seat = main.addOrReplaceChild("seat", CubeListBuilder.create().texOffs(0, 54).addBox(-6.0F, -2.0F, -4.0F, 12.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition chute = main.addOrReplaceChild("chute", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -38.0F, -8.0F, 24.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = chute.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, -8.0F, 24.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.0F, -37.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition cube_r2 = chute.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0F, -1.0F, -8.0F, 24.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0F, -37.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition rope = main.addOrReplaceChild("rope", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r3 = rope.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(40, 54).addBox(-1.0F, -42.0F, 0.0F, 1.0F, 42.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, -4.0F, 0.0873F, 0.0F, 0.7854F));

		PartDefinition cube_r4 = rope.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(40, 54).addBox(-1.0F, -42.0F, -1.0F, 1.0F, 42.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, 4.0F, -0.0873F, 0.0F, 0.7854F));

		PartDefinition cube_r5 = rope.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(40, 54).addBox(0.0F, -42.0F, -1.0F, 1.0F, 42.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 0.0F, 4.0F, -0.0873F, 0.0F, -0.7854F));

		PartDefinition cube_r6 = rope.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(40, 54).addBox(0.0F, -42.0F, 0.0F, 1.0F, 42.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 0.0F, -4.0F, 0.0873F, 0.0F, -0.7854F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
