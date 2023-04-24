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

public class EntityModelSteveUpSmash<T extends EntityTurret> extends EntityControllableModel<T>{
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "steve_up_smash"), "main");
	private final ModelPart main;
	private final ModelPart gun;
	private final ModelPart[] covers = new ModelPart[4];
	
	public EntityModelSteveUpSmash(ModelPart root) {
		this.main = root.getChild("main");
		this.gun = main.getChild("gun");
		covers[0] = gun.getChild("cover1");
		covers[1] = gun.getChild("cover2");
		covers[2] = gun.getChild("cover3");
		covers[3] = gun.getChild("cover4");
	}
	
	@Override
	public void renderToBuffer(T entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer,
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0, 1.5, 0);
		poseStack.scale(1.0F, -1.0F, 1.0F);
		gun.xRot = -(float)Math.toRadians(UtilAngles.lerpAngle(partialTicks, entity.xRotRelO, entity.getRelRotX()));
		int ammo = entity.getAmmo();
		for (int i = 0; i < covers.length; ++i) {
			if (i < covers.length-ammo) covers[i].xRot = (float)Math.PI/2;
			else covers[i].xRot = 0;
		}
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition frame = main.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 32).addBox(-6.0F, -2.0F, -8.0F, 12.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 48).addBox(-6.0F, -26.0F, -8.0F, 12.0F, 24.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 48).addBox(6.0F, -32.0F, -1.0F, 2.0F, 32.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(28, 48).addBox(-8.0F, -32.0F, -1.0F, 2.0F, 32.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(38, 32).addBox(-1.0F, -32.0F, -8.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(38, 32).addBox(-4.0F, -43.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = frame.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -32.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition cube_r2 = frame.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -32.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r3 = frame.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -32.0F, -7.0F, -0.5236F, 0.0F, 0.0F));

		PartDefinition gun = main.addOrReplaceChild("gun", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -43.0F, 0.0F));

		PartDefinition cover1 = gun.addOrReplaceChild("cover1", CubeListBuilder.create().texOffs(44, 48).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -14.0F, 8.0F));

		PartDefinition cover2 = gun.addOrReplaceChild("cover2", CubeListBuilder.create().texOffs(48, 10).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -14.0F, 8.0F));

		PartDefinition cover3 = gun.addOrReplaceChild("cover3", CubeListBuilder.create().texOffs(48, 5).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -6.0F, 8.0F));

		PartDefinition cover4 = gun.addOrReplaceChild("cover4", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -6.0F, 8.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
