package com.onewhohears.dscombat.client.model.vehicle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.model.EntityControllableModel;
import com.onewhohears.dscombat.entity.vehicle.EntityBoat;

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
public class EntityModelNathanBoat extends EntityControllableModel<EntityBoat> {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "nathan_boat"), "main");
	private final ModelPart main;
	private final ModelPart engine;
	private final ModelPart propellor;
	
	public EntityModelNathanBoat(ModelPart root) {
		this.main = root.getChild("main");
		this.engine = main.getChild("engine");
		this.propellor = engine.getChild("propellor");
	}
	
	@Override
	public void renderToBuffer(EntityBoat entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer,
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0, 1.55, 0);
		poseStack.scale(-1.0F, -1.0F, 1.0F);
		float ypi = Mth.PI/8;
		this.engine.yRot = -entity.inputs.yaw * ypi;
		this.propellor.zRot = entity.getMotorRotation(partialTicks, 5);
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition frame = main.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(176, 0).addBox(-8.0F, -4.0F, -24.0F, 16.0F, 4.0F, 64.0F, new CubeDeformation(0.0F))
		.texOffs(0, 84).addBox(-16.0F, -7.0F, -24.0F, 32.0F, 3.0F, 72.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-24.0F, -11.0F, -24.0F, 48.0F, 4.0F, 80.0F, new CubeDeformation(0.0F))
		.texOffs(88, 180).addBox(-24.0F, -25.0F, 54.0F, 48.0F, 16.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 159).addBox(22.0F, -25.0F, -24.0F, 4.0F, 16.0F, 80.0F, new CubeDeformation(0.0F))
		.texOffs(128, 84).addBox(-26.0F, -25.0F, -24.0F, 4.0F, 16.0F, 80.0F, new CubeDeformation(0.0F))
		.texOffs(0, 104).addBox(8.0F, -25.0F, -26.0F, 16.0F, 16.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 84).addBox(-24.0F, -25.0F, -26.0F, 16.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bench1 = main.addOrReplaceChild("bench1", CubeListBuilder.create().texOffs(0, 24).addBox(-16.0F, -20.0F, 32.0F, 32.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(56, 117).addBox(-15.0F, -16.0F, 33.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(40, 117).addBox(13.0F, -16.0F, 33.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

		PartDefinition bench2 = main.addOrReplaceChild("bench2", CubeListBuilder.create().texOffs(0, 12).addBox(-16.0F, -20.0F, 32.0F, 32.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(56, 106).addBox(-15.0F, -16.0F, 33.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(40, 106).addBox(13.0F, -16.0F, 33.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -16.0F));

		PartDefinition bench3 = main.addOrReplaceChild("bench3", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -20.0F, 32.0F, 32.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(56, 95).addBox(-15.0F, -16.0F, 33.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(40, 95).addBox(13.0F, -16.0F, 33.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -35.0F));

		PartDefinition bench4 = main.addOrReplaceChild("bench4", CubeListBuilder.create().texOffs(40, 60).addBox(4.0F, -20.0F, 32.0F, 12.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(56, 84).addBox(5.0F, -16.0F, 33.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(40, 84).addBox(13.0F, -16.0F, 33.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-24.0F, 0.0F, -54.0F));

		PartDefinition engine = main.addOrReplaceChild("engine", CubeListBuilder.create().texOffs(0, 36).addBox(-6.0F, -17.0F, -18.0F, 12.0F, 18.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(16, 66).addBox(-2.0F, -11.0F, -6.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 124).addBox(-1.0F, -13.0F, -1.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 66).addBox(-1.0F, -15.0F, -1.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(48, 36).addBox(-4.0F, 1.0F, -16.0F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, -20.0F));

		PartDefinition propellor = engine.addOrReplaceChild("propellor", CubeListBuilder.create().texOffs(0, 36).addBox(-1.0F, -1.0F, -4.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, -16.0F));

		PartDefinition cube_r1 = propellor.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.0F, -0.3491F, -1.5708F));

		PartDefinition cube_r2 = propellor.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -5.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.0F, -0.3491F, -3.1416F));

		PartDefinition cube_r3 = propellor.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 24).addBox(-1.0F, -5.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.0F, -0.3491F, 1.5708F));

		PartDefinition cube_r4 = propellor.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(36, 36).addBox(-1.0F, -5.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.0F, -0.3491F, 0.0F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

}
