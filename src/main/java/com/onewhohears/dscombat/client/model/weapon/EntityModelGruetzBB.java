package com.onewhohears.dscombat.client.model.weapon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.weapon.EntityBunkerBuster;

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
import net.minecraft.util.Mth;

public class EntityModelGruetzBB extends EntityModel<EntityBunkerBuster<?>> {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "gruetz_bunker_buster"), "main");
	private final ModelPart main;
	private final ModelPart drill;

	public EntityModelGruetzBB(ModelPart root) {
		this.main = root.getChild("main");
		this.drill = main.getChild("drill");
	}
	
	@Override
	public void setupAnim(EntityBunkerBuster<?> entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		drill.yRot = ageInTicks * Mth.PI / 32;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.scale(1.0f, -1.0f, 1.0f);
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition drill = main.addOrReplaceChild("drill", CubeListBuilder.create().texOffs(120, 65).addBox(-1.0F, 12.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(104, 77).addBox(-3.0F, 8.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(88, 92).addBox(-5.0F, 4.0F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(72, 110).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 4.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -16.0F, 0.0F));

		PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -37.0F, -8.0F, 24.0F, 20.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 56).addBox(-8.0F, -37.0F, 8.0F, 16.0F, 20.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 56).addBox(-8.0F, -37.0F, -12.0F, 16.0F, 20.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 36).addBox(-8.0F, -41.0F, -8.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 108).addBox(-1.0F, -49.0F, 8.0F, 2.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 108).addBox(-1.0F, -49.0F, -16.0F, 2.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(22, 114).addBox(-16.0F, -49.0F, -1.0F, 8.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(22, 114).addBox(8.0F, -49.0F, -1.0F, 8.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}
}
