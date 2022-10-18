package com.onewhohears.dscombat.client.renderer.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;

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

public class EntityModelMissile1<T extends EntityMissile> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "missile1"), "main");
	private final ModelPart bone;

	public EntityModelMissile1(ModelPart root) {
		this.bone = root.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 33).addBox(1.0F, -1.0F, -7.0F, 1.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(20, 0).addBox(-2.0F, -1.0F, -7.0F, 1.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(18, 19).addBox(-1.0F, 1.0F, -7.0F, 2.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 18).addBox(-1.0F, -2.0F, -7.0F, 2.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(0.0F, 2.0F, -7.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(0.0F, -4.0F, -7.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, 0.0F, -7.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(2.0F, 0.0F, -7.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(2.0F, 0.0F, 2.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, 0.0F, 2.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(0.0F, 2.0F, 2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(0.0F, -4.0F, 2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(17, 38).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
