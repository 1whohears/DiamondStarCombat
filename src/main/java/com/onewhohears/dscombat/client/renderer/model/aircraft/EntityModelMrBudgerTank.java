package com.onewhohears.dscombat.client.renderer.model.aircraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.renderer.model.EntityControllableModel;
import com.onewhohears.dscombat.entity.aircraft.EntityGroundVehicle;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class EntityModelMrBudgerTank<T extends EntityGroundVehicle> extends EntityControllableModel<T> {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "mrbudger_tank"), "main");
	private final ModelPart main;
	private final ModelPart[] wheelsL = new ModelPart[5];
	private final ModelPart[] wheelsR = new ModelPart[5];

	public EntityModelMrBudgerTank(ModelPart root) {
		this.main = root.getChild("main");
		ModelPart wL = main.getChild("trackL").getChild("wheelsL");
		wheelsL[0] = wL.getChild("w1");
		wheelsL[1] = wL.getChild("w2");
		wheelsL[2] = wL.getChild("w3");
		wheelsL[3] = wL.getChild("w4");
		wheelsL[4] = wL.getChild("w5");
		ModelPart wR = main.getChild("trackR").getChild("wheelsR");
		wheelsR[0] = wR.getChild("w6");
		wheelsR[1] = wR.getChild("w7");
		wheelsR[2] = wR.getChild("w8");
		wheelsR[3] = wR.getChild("w9");
		wheelsR[4] = wR.getChild("w10");
	}
	
	@Override
	public void renderToBuffer(T entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer,
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0, 1.55, 0);
		poseStack.scale(1.0F, -1.0F, 1.0F);
		for (int i = 0; i < wheelsL.length; ++i) wheelsL[i].xRot = -entity.getWheelLeftRotation(partialTicks);
		for (int i = 0; i < wheelsR.length; ++i) wheelsR[i].xRot = -entity.getWheelRightRotation(partialTicks);
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition frame = main.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0F, -22.0F, -40.0F, 48.0F, 14.0F, 80.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition trackL = main.addOrReplaceChild("trackL", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wheelsL = trackL.addOrReplaceChild("wheelsL", CubeListBuilder.create(), PartPose.offset(-24.0F, -8.0F, 0.0F));

		PartDefinition w1 = wheelsL.addOrReplaceChild("w1", CubeListBuilder.create().texOffs(32, 94).addBox(-4.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 30.0F));

		PartDefinition w2 = wheelsL.addOrReplaceChild("w2", CubeListBuilder.create().texOffs(40, 48).addBox(-4.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 15.0F));

		PartDefinition w3 = wheelsL.addOrReplaceChild("w3", CubeListBuilder.create().texOffs(0, 48).addBox(-4.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition w4 = wheelsL.addOrReplaceChild("w4", CubeListBuilder.create().texOffs(40, 24).addBox(-4.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -15.0F));

		PartDefinition w5 = wheelsL.addOrReplaceChild("w5", CubeListBuilder.create().texOffs(0, 94).addBox(-4.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -30.0F));

		PartDefinition platesL = trackL.addOrReplaceChild("platesL", CubeListBuilder.create().texOffs(90, 172).addBox(-30.0F, -2.0F, -37.0F, 8.0F, 2.0F, 74.0F, new CubeDeformation(0.0F))
		.texOffs(0, 170).addBox(-30.0F, -16.0F, -37.0F, 8.0F, 2.0F, 74.0F, new CubeDeformation(0.0F))
		.texOffs(20, 118).addBox(-30.0F, -15.0F, 36.0F, 8.0F, 14.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 118).addBox(-30.0F, -15.0F, -38.0F, 8.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition trackR = main.addOrReplaceChild("trackR", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wheelsR = trackR.addOrReplaceChild("wheelsR", CubeListBuilder.create(), PartPose.offset(24.0F, -8.0F, 0.0F));

		PartDefinition w6 = wheelsR.addOrReplaceChild("w6", CubeListBuilder.create().texOffs(32, 94).mirror().addBox(0.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 30.0F));

		PartDefinition w7 = wheelsR.addOrReplaceChild("w7", CubeListBuilder.create().texOffs(40, 48).mirror().addBox(0.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 15.0F));

		PartDefinition w8 = wheelsR.addOrReplaceChild("w8", CubeListBuilder.create().texOffs(0, 48).mirror().addBox(0.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition w9 = wheelsR.addOrReplaceChild("w9", CubeListBuilder.create().texOffs(40, 24).mirror().addBox(0.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, -15.0F));

		PartDefinition w10 = wheelsR.addOrReplaceChild("w10", CubeListBuilder.create().texOffs(0, 94).mirror().addBox(0.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, -30.0F));

		PartDefinition platesR = trackR.addOrReplaceChild("platesR", CubeListBuilder.create().texOffs(90, 172).mirror().addBox(22.0F, -2.0F, -37.0F, 8.0F, 2.0F, 74.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 170).mirror().addBox(22.0F, -16.0F, -37.0F, 8.0F, 2.0F, 74.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(20, 118).mirror().addBox(22.0F, -15.0F, 36.0F, 8.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 118).mirror().addBox(22.0F, -15.0F, -38.0F, 8.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}
	
}
