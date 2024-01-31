package com.onewhohears.dscombat.client.model.aircraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.model.EntityControllableModel;
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

public class EntityModelSmallRoller extends EntityControllableModel<EntityGroundVehicle> {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "small_roller"), "main");
	private final ModelPart main;
	private final ModelPart[] wheelsL = new ModelPart[2];
	private final ModelPart[] wheelsR = new ModelPart[2];

	public EntityModelSmallRoller(ModelPart root) {
		this.main = root.getChild("main");
		ModelPart wL = main.getChild("trackL").getChild("wheelsL");
		wheelsL[0] = wL.getChild("wheel1");
		wheelsL[1] = wL.getChild("wheel2");
		ModelPart wR = main.getChild("trackR").getChild("wheelsR");
		wheelsR[0] = wR.getChild("wheel3");
		wheelsR[1] = wR.getChild("wheel4");
	}
	
	@Override
	public void renderToBuffer(EntityGroundVehicle entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer,
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0, 1.55, 0);
		poseStack.scale(1.0F, -1.0F, 1.0F);
		for (int i = 0; i < wheelsL.length; ++i) wheelsL[i].xRot = -entity.getWheelRotation(partialTicks, 1.7f);
		for (int i = 0; i < wheelsR.length; ++i) wheelsR[i].xRot = -entity.getWheelRotation(partialTicks, 1.7f);
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 55).addBox(-8.0F, -10.0F, -8.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(48, 59).addBox(-10.0F, -7.0F, 5.0F, 20.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(48, 55).addBox(-10.0F, -7.0F, -7.0F, 20.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition trackL = main.addOrReplaceChild("trackL", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition platesL = trackL.addOrReplaceChild("platesL", CubeListBuilder.create().texOffs(38, 28).addBox(-14.0F, -1.0F, -13.0F, 6.0F, 1.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(38, 1).addBox(-14.0F, -13.0F, -13.0F, 6.0F, 1.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(14, 75).addBox(-14.0F, -12.0F, 12.0F, 6.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 75).addBox(-14.0F, -12.0F, -13.0F, 6.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wheelsL = trackL.addOrReplaceChild("wheelsL", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wheel1 = wheelsL.addOrReplaceChild("wheel1", CubeListBuilder.create().texOffs(38, 28).addBox(-1.0F, -5.5F, -5.5F, 2.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-11.0F, -6.5F, 6.5F));

		PartDefinition wheel2 = wheelsL.addOrReplaceChild("wheel2", CubeListBuilder.create().texOffs(38, 0).addBox(-1.0F, -5.5F, -5.5F, 2.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-11.0F, -6.5F, -6.5F));

		PartDefinition trackR = main.addOrReplaceChild("trackR", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition platesR = trackR.addOrReplaceChild("platesR", CubeListBuilder.create().texOffs(0, 27).addBox(8.0F, -1.0F, -13.0F, 6.0F, 1.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(8.0F, -13.0F, -13.0F, 6.0F, 1.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(64, 63).addBox(8.0F, -12.0F, 12.0F, 6.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 54).addBox(8.0F, -12.0F, -13.0F, 6.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wheelsR = trackR.addOrReplaceChild("wheelsR", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wheel3 = wheelsR.addOrReplaceChild("wheel3", CubeListBuilder.create().texOffs(0, 27).addBox(-1.0F, -5.5F, -5.5F, 2.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(11.0F, -6.5F, 6.5F));

		PartDefinition wheel4 = wheelsR.addOrReplaceChild("wheel4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.5F, -5.5F, 2.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(11.0F, -6.5F, -6.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}
	
}
