package com.onewhohears.dscombat.client.model.weapon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.model.EntityControllableModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

@Deprecated
public class EntityModelMiniGunTurret extends EntityControllableModel<EntityTurret>{
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "minigun_turrent"), "main");
	private final ModelPart main;
	private final ModelPart gun;
	
	public EntityModelMiniGunTurret(ModelPart root) {
		this.main = root.getChild("main");
		this.gun = main.getChild("gun");
	}
	
	@Override
	public void renderToBuffer(EntityTurret entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer,
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0, 1.5, 0);
		poseStack.scale(1.0F, -1.0F, 1.0F);
		gun.xRot = -(float)Math.toRadians(UtilAngles.lerpAngle(partialTicks, entity.xRotRelO, entity.getRelRotX()));
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 10.0F, 0.0F));

		PartDefinition frame = main.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 12.0F, -8.0F, 12.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(-6.0F, -12.0F, -8.0F, 12.0F, 24.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 55).addBox(-1.0F, -2.0F, 6.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition gun = main.addOrReplaceChild("gun", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(40, 52).addBox(-0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(42, 39).addBox(-0.5F, -2.0F, 1.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(42, 26).addBox(-0.5F, 1.0F, 1.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(26, 42).addBox(1.0F, -0.5F, 1.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(42, 13).addBox(-2.0F, -0.5F, 1.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.0F));

		PartDefinition cube_r1 = gun.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(28, 16).addBox(-2.0F, -0.5F, 9.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.0F, 0.0F, -2.3562F));

		PartDefinition cube_r2 = gun.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(28, 29).addBox(-2.0F, -0.5F, 9.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.0F, 0.0F, 2.3562F));

		PartDefinition cube_r3 = gun.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(38, 0).addBox(-2.0F, -0.5F, 9.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r4 = gun.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 42).addBox(-2.0F, -0.5F, 9.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.0F, 0.0F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
