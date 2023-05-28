package com.onewhohears.dscombat.client.renderer.model.weapon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.renderer.model.EntityControllableModel;
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

public class EntityModelHeavyTankTurret extends EntityControllableModel<EntityTurret>{
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "heavy_tank_turret"), "main");
	private final ModelPart main;
	private final ModelPart gun;
	
	public EntityModelHeavyTankTurret(ModelPart root) {
		this.main = root.getChild("main");
		this.gun = main.getChild("barrel");
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

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(64, 22).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 16.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(64, 0).addBox(-16.0F, -16.0F, 10.0F, 32.0F, 16.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(60, 62).addBox(10.0F, -16.0F, -10.0F, 6.0F, 16.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-16.0F, -16.0F, -10.0F, 6.0F, 16.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(0, 60).addBox(-10.0F, -2.0F, -10.0F, 20.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition barrel = main.addOrReplaceChild("barrel", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 56.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 16.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

}
