package com.onewhohears.dscombat.client.renderer.model.aircraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.renderer.model.EntityControllableModel;
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

public class EntityModelE3Sentry extends EntityControllableModel<EntityPlane> {
	
public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "e3sentry_plane"), "main");
	
	private final ModelPart body;
	private final ModelPart gear_front;
	private final ModelPart gear_left;
	private final ModelPart gear_right;
	private final ModelPart radar;
	private final ModelPart stick;
	
	public EntityModelE3Sentry(ModelPart root) {
		this.body = root.getChild("body");
		this.gear_front = body.getChild("gear").getChild("front");
		this.gear_left = body.getChild("gear").getChild("back_left");
		this.gear_right = body.getChild("gear").getChild("back_right");
		this.radar = body.getChild("radar");
		this.stick = body.getChild("seat").getChild("s1").getChild("stick");
	}
	
	@Override
	public void renderToBuffer(EntityPlane entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer, 
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0, 1.5, 0);
		poseStack.scale(-1.0F, -1.0F, 1.0F);
		if (entity.radarSystem.hasRadar("ar20k")) {
			radar.yRot = entity.getPropellerRotation(partialTicks);
			radar.visible = true;
		} else {
			radar.visible = false;
		}
		float gear = entity.getLandingGearPos(partialTicks);
		if (gear < 1) {
			float hpi = Mth.PI/2;
			this.gear_front.xRot = gear * -hpi;
			this.gear_left.zRot = gear * hpi;
			this.gear_right.zRot = gear * -hpi;
			this.gear_front.visible = true;
			this.gear_left.visible = true;
			this.gear_right.visible = true;
		} else {
			this.gear_front.visible = false;
			this.gear_left.visible = false;
			this.gear_right.visible = false;
		}
		float ypi = Mth.PI/8;
		float ppi = Mth.PI/12;
		this.stick.zRot = entity.inputs.yaw * -ypi;
		this.stick.xRot = entity.inputs.pitch * ppi;
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition fuselage = body.addOrReplaceChild("fuselage", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wing = fuselage.addOrReplaceChild("wing", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_wing = wing.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(322, 26).addBox(-129.0F, 8.0F, -1.0F, 112.0F, 2.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(0, 143).addBox(-57.0F, 8.0F, 23.0F, 40.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_wing = wing.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(322, 0).addBox(17.0F, 8.0F, -1.0F, 112.0F, 2.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(0, 129).addBox(17.0F, 8.0F, 23.0F, 40.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frame = fuselage.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 129).addBox(-16.0F, 20.0F, -64.0F, 32.0F, 1.0F, 128.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-16.0F, -13.0F, -64.0F, 32.0F, 1.0F, 128.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wallL = frame.addOrReplaceChild("wallL", CubeListBuilder.create().texOffs(192, 141).addBox(-17.0F, 8.0F, -64.0F, 1.0F, 12.0F, 128.0F, new CubeDeformation(0.0F))
		.texOffs(130, 281).addBox(-17.0F, -12.0F, -64.0F, 1.0F, 8.0F, 128.0F, new CubeDeformation(0.0F))
		.texOffs(52, 205).addBox(-17.0F, -4.0F, -64.0F, 1.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(110, 197).addBox(-17.0F, -4.0F, 8.0F, 1.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(192, 193).addBox(-17.0F, -4.0F, -16.0F, 1.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(192, 141).addBox(-17.0F, -4.0F, 56.0F, 1.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(192, 61).addBox(-17.0F, -4.0F, 32.0F, 1.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 157).addBox(-17.0F, -4.0F, -40.0F, 1.0F, 12.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wallR = frame.addOrReplaceChild("wallR", CubeListBuilder.create().texOffs(192, 1).addBox(16.0F, 8.0F, -64.0F, 1.0F, 12.0F, 128.0F, new CubeDeformation(0.0F))
		.texOffs(0, 258).addBox(16.0F, -12.0F, -64.0F, 1.0F, 8.0F, 128.0F, new CubeDeformation(0.0F))
		.texOffs(106, 151).addBox(16.0F, -4.0F, -64.0F, 1.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(104, 26).addBox(16.0F, -4.0F, 8.0F, 1.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(104, 0).addBox(16.0F, -4.0F, -16.0F, 1.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 52).addBox(16.0F, -4.0F, 56.0F, 1.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 26).addBox(16.0F, -4.0F, 32.0F, 1.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(16.0F, -4.0F, -40.0F, 1.0F, 12.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition nose = fuselage.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(0, 52).addBox(-16.0F, 20.0F, 64.0F, 32.0F, 1.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(192, 32).addBox(-8.0F, 8.0F, 88.0F, 16.0F, 13.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(192, 141).addBox(16.0F, 8.0F, 64.0F, 1.0F, 12.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(192, 61).addBox(-17.0F, 8.0F, 64.0F, 1.0F, 12.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(108, 129).addBox(-16.0F, 8.0F, 88.0F, 8.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(104, 52).addBox(8.0F, 8.0F, 88.0F, 8.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(18, 26).addBox(16.0F, -4.0F, 76.0F, 1.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(92, 129).addBox(16.0F, -12.0F, 64.0F, 1.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(28, 157).addBox(-16.0F, -13.0F, 64.0F, 32.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(52, 172).addBox(-16.0F, -12.0F, 63.0F, 32.0F, 32.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(88, 52).addBox(-17.0F, -12.0F, 64.0F, 1.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(18, 0).addBox(-17.0F, -4.0F, 76.0F, 1.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(66, 103).addBox(-8.0F, -13.0F, 78.0F, 16.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail = fuselage.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 77).addBox(-16.0F, -12.0F, 63.0F, 32.0F, 32.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(192, 0).addBox(-12.0F, -8.0F, 55.0F, 24.0F, 24.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(80, 77).addBox(-8.0F, -4.0F, 47.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(192, 177).addBox(-4.0F, 0.0F, 39.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 157).addBox(-1.0F, -40.0F, 44.0F, 2.0F, 40.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(0, 26).addBox(4.0F, 3.0F, 44.0F, 40.0F, 2.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-44.0F, 3.0F, 44.0F, 40.0F, 2.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -135.0F));

		PartDefinition seat = body.addOrReplaceChild("seat", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition s1 = seat.addOrReplaceChild("s1", CubeListBuilder.create().texOffs(0, 282).addBox(-14.0F, 18.0F, 67.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 263).addBox(-14.0F, 4.0F, 67.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition stick = s1.addOrReplaceChild("stick", CubeListBuilder.create().texOffs(0, 305).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 299).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 20.0F, 83.5F));

		PartDefinition s2 = seat.addOrReplaceChild("s2", CubeListBuilder.create().texOffs(0, 282).addBox(-14.0F, 18.0F, 67.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 263).addBox(-14.0F, 4.0F, 67.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(16.0F, 0.0F, 0.0F));

		PartDefinition s3 = seat.addOrReplaceChild("s3", CubeListBuilder.create().texOffs(0, 282).addBox(-14.0F, 18.0F, 67.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 263).addBox(-14.0F, 4.0F, 67.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(16.0F, 0.0F, -27.0F));

		PartDefinition s4 = seat.addOrReplaceChild("s4", CubeListBuilder.create().texOffs(0, 282).addBox(-14.0F, 18.0F, 67.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 263).addBox(-14.0F, 4.0F, 67.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(16.0F, 0.0F, -51.0F));

		PartDefinition s5 = seat.addOrReplaceChild("s5", CubeListBuilder.create().texOffs(0, 282).addBox(-14.0F, 18.0F, 67.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 263).addBox(-14.0F, 4.0F, 67.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(16.0F, 0.0F, -75.0F));

		PartDefinition s6 = seat.addOrReplaceChild("s6", CubeListBuilder.create().texOffs(0, 282).addBox(-14.0F, 18.0F, 67.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 263).addBox(-14.0F, 4.0F, 67.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(16.0F, 0.0F, -99.0F));

		PartDefinition s7 = seat.addOrReplaceChild("s7", CubeListBuilder.create().texOffs(0, 282).addBox(-14.0F, 18.0F, 67.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 263).addBox(-14.0F, 4.0F, 67.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(16.0F, 0.0F, -123.0F));

		PartDefinition s8 = seat.addOrReplaceChild("s8", CubeListBuilder.create().texOffs(0, 282).addBox(-14.0F, 18.0F, 67.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 263).addBox(-14.0F, 4.0F, 67.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -27.0F));

		PartDefinition s9 = seat.addOrReplaceChild("s9", CubeListBuilder.create().texOffs(0, 282).addBox(-14.0F, 18.0F, 67.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 263).addBox(-14.0F, 4.0F, 67.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -51.0F));

		PartDefinition s10 = seat.addOrReplaceChild("s10", CubeListBuilder.create().texOffs(0, 282).addBox(-14.0F, 18.0F, 67.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 263).addBox(-14.0F, 4.0F, 67.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -75.0F));

		PartDefinition s11 = seat.addOrReplaceChild("s11", CubeListBuilder.create().texOffs(0, 282).addBox(-14.0F, 18.0F, 67.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 263).addBox(-14.0F, 4.0F, 67.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -99.0F));

		PartDefinition s12 = seat.addOrReplaceChild("s12", CubeListBuilder.create().texOffs(0, 282).addBox(-14.0F, 18.0F, 67.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 263).addBox(-14.0F, 4.0F, 67.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -123.0F));

		PartDefinition gear = body.addOrReplaceChild("gear", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition front = gear.addOrReplaceChild("front", CubeListBuilder.create().texOffs(88, 52).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(210, 193).addBox(1.0F, 4.0F, -3.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(204, 207).addBox(-3.0F, 4.0F, -3.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, 56.0F));

		PartDefinition back_left = gear.addOrReplaceChild("back_left", CubeListBuilder.create().texOffs(118, 171).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(86, 205).addBox(1.0F, 15.0F, -3.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(70, 205).addBox(-3.0F, 15.0F, -3.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(32.0F, 10.0F, 11.0F));

		PartDefinition back_right = gear.addOrReplaceChild("back_right", CubeListBuilder.create().texOffs(120, 63).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(192, 32).addBox(-3.0F, 14.0F, -3.0F, 2.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(112, 101).addBox(1.0F, 14.0F, -3.0F, 2.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-32.0F, 10.0F, 11.0F));

		PartDefinition radar = body.addOrReplaceChild("radar", CubeListBuilder.create().texOffs(192, 97).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 8.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(260, 281).addBox(-30.0F, -6.0F, -30.0F, 60.0F, 6.0F, 60.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -21.0F, -24.0F));

		return LayerDefinition.create(meshdefinition, 1024, 1024);
	}
	
}
