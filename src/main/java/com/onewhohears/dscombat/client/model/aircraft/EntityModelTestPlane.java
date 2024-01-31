package com.onewhohears.dscombat.client.model.aircraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.model.EntityControllableModel;
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
import net.minecraft.util.Mth;

public class EntityModelTestPlane<T extends EntityPlane> extends EntityControllableModel<T> {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "basic_plane"), "main");
	
	private final ModelPart body;
	private final ModelPart propeller;
	private final ModelPart gearback;
	private final ModelPart gearleft;
	private final ModelPart gearright;

	public EntityModelTestPlane(ModelPart root) {
		this.body = root.getChild("body");
		this.propeller = body.getChild("propeller");
		this.gearback = body.getChild("gearback");
		this.gearleft = body.getChild("gearleft");
		this.gearright = body.getChild("gearright");
	}
	
	@Override
	public void renderToBuffer(T entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0, 1.5, 0);
		poseStack.scale(1.0F, -1.0F, 1.0F);
		propeller.zRot = entity.getMotorRotation(partialTicks, Mth.PI);
		float gear = entity.getLandingGearPos(partialTicks);
		float hpi = (float)Math.PI/2;
		gearback.xRot = gear * -hpi;
		gearleft.zRot = gear * (hpi-0.01f);
		gearright.zRot = gear * -(hpi-0.01f);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 26).addBox(-7.0F, -7.0F, -22.0F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition seat = body.addOrReplaceChild("seat", CubeListBuilder.create().texOffs(58, 12).addBox(-7.0F, 6.0F, -8.0F, 14.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(30, 62).addBox(6.0F, -7.0F, -8.0F, 1.0F, 13.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 54).addBox(-7.0F, -7.0F, -8.0F, 1.0F, 13.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(72, 30).addBox(-6.0F, -7.0F, 5.0F, 12.0F, 13.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wingleft = body.addOrReplaceChild("wingleft", CubeListBuilder.create().texOffs(0, 13).addBox(7.0F, -1.0F, -21.0F, 24.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wingright = body.addOrReplaceChild("wingright", CubeListBuilder.create().texOffs(0, 0).addBox(-31.0F, -1.0F, -21.0F, 24.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(40, 38).addBox(-4.0F, -4.0F, 6.0F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition up = tail.addOrReplaceChild("up", CubeListBuilder.create().texOffs(60, 71).addBox(-1.0F, -20.0F, 14.0F, 2.0F, 16.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left = tail.addOrReplaceChild("left", CubeListBuilder.create().texOffs(46, 62).addBox(-16.0F, 0.0F, 14.0F, 12.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right = tail.addOrReplaceChild("right", CubeListBuilder.create().texOffs(60, 0).addBox(4.0F, 0.0F, 14.0F, 12.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition propeller = body.addOrReplaceChild("propeller", CubeListBuilder.create().texOffs(0, 26).addBox(-1.0F, -1.0F, -24.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(42, 27).addBox(-12.0F, -1.0F, -25.0F, 24.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition gearback = body.addOrReplaceChild("gearback", CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(-1.0F, 5.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 17.0F));

		PartDefinition gearleft = body.addOrReplaceChild("gearleft", CubeListBuilder.create().texOffs(4, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).addBox(-1.0F, 9.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(25.0F, 0.0F, -16.0F));

		PartDefinition gearright = body.addOrReplaceChild("gearright", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 13).addBox(-1.0F, 9.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-25.0F, 0.0F, -16.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}
	
}
