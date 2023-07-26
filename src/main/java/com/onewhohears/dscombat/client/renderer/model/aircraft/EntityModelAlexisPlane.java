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

public class EntityModelAlexisPlane extends EntityControllableModel<EntityPlane> {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "alexis_plane"), "main");
	
	private final ModelPart Plane;
	private final ModelPart gfront, gleft, gright;
	private final ModelPart stick;

	public EntityModelAlexisPlane(ModelPart root) {
		this.Plane = root.getChild("Plane");
		this.gfront = Plane.getChild("gear").getChild("set1");
		this.gleft = Plane.getChild("gear").getChild("setL");
		this.gright = Plane.getChild("gear").getChild("setR");
		this.stick = Plane.getChild("Cockpit").getChild("Controls").getChild("controlstick");
	}
	
	@Override
	public void renderToBuffer(EntityPlane entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer,
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0, 0.4, 0);
		poseStack.scale(0.9F, -0.9F, -0.9F);
		float gear = entity.getLandingGearPos(partialTicks);
		if (gear < 1) {
			float hpi = Mth.PI/2;
			this.gfront.xRot = gear * -hpi;
			this.gleft.xRot = gear * hpi;
			this.gright.xRot = gear * hpi;
			this.gfront.visible = true;
			this.gleft.visible = true;
			this.gright.visible = true;
		} else {
			this.gfront.visible = false;
			this.gleft.visible = false;
			this.gright.visible = false;
		}
		float ypi = Mth.PI/8;
		float ppi = Mth.PI/12;
		this.stick.zRot = entity.inputs.yaw * -ypi;
		this.stick.xRot = entity.inputs.pitch * ppi;
		Plane.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Plane = partdefinition.addOrReplaceChild("Plane", CubeListBuilder.create(), PartPose.offset(-0.2517F, 24.0F, 0.0F));

		PartDefinition Cockpit = Plane.addOrReplaceChild("Cockpit", CubeListBuilder.create().texOffs(0, 309).addBox(-12.2483F, -44.0834F, -49.0F, 3.0F, 24.0F, 66.0F, new CubeDeformation(0.0F))
		.texOffs(320, 0).addBox(7.7517F, -44.0834F, -49.0F, 3.0F, 24.0F, 66.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -18.0F, 0.0F));

		PartDefinition dash_r1 = Cockpit.addOrReplaceChild("dash_r1", CubeListBuilder.create().texOffs(0, 69).addBox(-8.5F, -1.0F, -4.0F, 17.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7483F, -40.3764F, -40.1949F, -0.8727F, 0.0F, 0.0F));

		PartDefinition Seat = Cockpit.addOrReplaceChild("Seat", CubeListBuilder.create().texOffs(146, 95).addBox(-49.7483F, -25.0F, -5.0F, 12.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-49.7483F, -39.0F, 2.0F, 12.0F, 18.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(84, 230).addBox(-46.7483F, -49.0F, 3.0F, 6.0F, 12.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(43.0F, -9.0F, -23.0F));

		PartDefinition Controls = Cockpit.addOrReplaceChild("Controls", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition controlstick = Controls.addOrReplaceChild("controlstick", CubeListBuilder.create().texOffs(0, 26).addBox(-1.9983F, -44.2532F, -32.2861F, 2.5F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(68, 50).addBox(-1.7483F, -40.2532F, -31.7861F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition RightRudder = Controls.addOrReplaceChild("RightRudder", CubeListBuilder.create(), PartPose.offset(-5.6817F, -32.4398F, -36.2997F));

		PartDefinition rudder_right_r1 = RightRudder.addOrReplaceChild("rudder_right_r1", CubeListBuilder.create().texOffs(14, 21).addBox(-1.5F, -0.5F, -2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, -0.4363F, 0.0F, 0.0F));

		PartDefinition LeftRudder = Controls.addOrReplaceChild("LeftRudder", CubeListBuilder.create(), PartPose.offset(4.1852F, -32.4398F, -36.2997F));

		PartDefinition rudder_right_r2 = LeftRudder.addOrReplaceChild("rudder_right_r2", CubeListBuilder.create().texOffs(0, 21).addBox(-1.5F, -0.5F, -2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, -0.4363F, 0.0F, 0.0F));

		PartDefinition Canopy = Cockpit.addOrReplaceChild("Canopy", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition port_c_r1 = Canopy.addOrReplaceChild("port_c_r1", CubeListBuilder.create().texOffs(0, 309).addBox(-1.02F, -7.0F, -13.5F, 2.0F, 14.0F, 27.0F, new CubeDeformation(0.0F))
		.texOffs(320, 0).addBox(-21.98F, -7.0F, -13.5F, 2.0F, 14.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.7517F, -46.0325F, 10.4116F, -0.6109F, 0.0F, 0.0F));

		PartDefinition port_b_r1 = Canopy.addOrReplaceChild("port_b_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.01F, -9.5F, -16.5F, 2.0F, 19.0F, 33.0F, new CubeDeformation(0.0F))
		.texOffs(0, 230).addBox(-21.99F, -9.5F, -16.5F, 2.0F, 19.0F, 33.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.7517F, -51.4841F, -13.8971F, -0.0873F, 0.0F, 0.0F));

		PartDefinition port_a_r1 = Canopy.addOrReplaceChild("port_a_r1", CubeListBuilder.create().texOffs(0, 95).addBox(-1.02F, -14.0F, -6.5F, 2.0F, 28.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 143).addBox(-21.98F, -14.0F, -6.5F, 2.0F, 28.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.7517F, -48.1354F, -35.4369F, -0.829F, 0.0F, 0.0F));

		PartDefinition cockpit_r1 = Canopy.addOrReplaceChild("cockpit_r1", CubeListBuilder.create().texOffs(569, 150).addBox(-9.5F, -1.0F, -13.5F, 19.0F, 2.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7483F, -50.9474F, 13.853F, -0.6109F, 0.0F, 0.0F));

		PartDefinition cockpit_r2 = Canopy.addOrReplaceChild("cockpit_r2", CubeListBuilder.create().texOffs(506, 320).addBox(-9.5F, -1.0F, -16.5F, 19.0F, 2.0F, 33.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7483F, -59.9517F, -13.1563F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cockpitfront_r1 = Canopy.addOrReplaceChild("cockpitfront_r1", CubeListBuilder.create().texOffs(37, 0).addBox(-9.5F, -14.0F, -1.0F, 19.0F, 28.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7483F, -52.1905F, -39.1527F, -0.829F, 0.0F, 0.0F));

		PartDefinition Body = Plane.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(320, 90).addBox(-9.2483F, -30.0834F, -42.0F, 17.0F, 10.0F, 59.0F, new CubeDeformation(0.0F))
		.texOffs(0, 230).addBox(-12.2483F, -44.0834F, 17.0F, 23.0F, 28.0F, 211.0F, new CubeDeformation(0.0F))
		.texOffs(257, 325).addBox(-9.2483F, -44.0834F, -49.0F, 17.0F, 24.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -18.0F, 0.0F));

		PartDefinition intake_left = Body.addOrReplaceChild("intake_left", CubeListBuilder.create().texOffs(506, 283).addBox(-9.2483F, -4.0834F, -17.3F, 17.0F, 2.0F, 35.0F, new CubeDeformation(0.0F))
		.texOffs(506, 246).addBox(-0.9853F, -16.5684F, -17.3F, 20.5F, 2.0F, 35.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = intake_left.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 513).addBox(-1.0F, -5.0F, -19.75F, 2.0F, 10.0F, 39.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.7737F, -19.5684F, 1.096F, 0.0F, 0.5236F, 0.0F));

		PartDefinition cube_r2 = intake_left.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(569, 39).addBox(-6.0F, -1.0F, -17.5F, 12.0F, 2.0F, 35.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.5673F, -9.9404F, 0.2F, 0.0F, 0.0F, -1.0297F));

		PartDefinition cube_r3 = intake_left.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(544, 591).addBox(-3.0F, -1.0F, -17.5F, 6.0F, 2.0F, 35.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.1765F, -4.1133F, 0.2F, 0.0F, 0.0F, -0.3752F));

		PartDefinition intake_right = Body.addOrReplaceChild("intake_right", CubeListBuilder.create().texOffs(413, 90).addBox(-19.5147F, -16.5684F, -17.3F, 20.5F, 2.0F, 35.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.4966F, 0.0F, 0.0F));

		PartDefinition cube_r4 = intake_right.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(506, 355).addBox(-1.0F, -5.0F, -19.75F, 2.0F, 10.0F, 39.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.7737F, -19.5684F, 1.096F, 0.0F, -0.5236F, 0.0F));

		PartDefinition cube_r5 = intake_right.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(550, 355).addBox(-6.0F, -1.0F, -17.5F, 12.0F, 2.0F, 35.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.5673F, -9.9404F, 0.2F, 0.0F, 0.0F, 1.0297F));

		PartDefinition cube_r6 = intake_right.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(554, 535).addBox(-3.0F, -1.0F, -17.5F, 6.0F, 2.0F, 35.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.1765F, -4.1133F, 0.2F, 0.0F, 0.0F, 0.3752F));

		PartDefinition hull = Body.addOrReplaceChild("hull", CubeListBuilder.create().texOffs(454, 479).addBox(-9.2483F, -15.0834F, -10.3F, 17.0F, 13.0F, 186.0F, new CubeDeformation(0.0F))
		.texOffs(0, 479).addBox(-20.9983F, -24.5684F, -10.3F, 40.5F, 10.0F, 186.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 28.0F));

		PartDefinition cube_r7 = hull.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(506, 221).addBox(-6.0F, -5.5F, -93.0F, 12.0F, 11.0F, 186.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.2066F, -12.2581F, 82.7F, 0.0F, 0.0F, 1.0297F));

		PartDefinition cube_r8 = hull.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(384, 757).addBox(-3.0F, -3.0F, -93.0F, 6.0F, 6.0F, 186.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.94F, -5.9741F, 82.7F, 0.0F, 0.0F, 0.3752F));

		PartDefinition cube_r9 = hull.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(165, 0).addBox(-0.5F, -8.0F, -14.0F, 1.0F, 16.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-18.5257F, 0.5967F, 126.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r10 = hull.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(160, 170).addBox(-0.5F, -8.0F, -14.0F, 1.0F, 16.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.0291F, 0.5967F, 126.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition cube_r11 = hull.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(569, 0).addBox(-6.0F, -5.5F, -93.0F, 12.0F, 11.0F, 186.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.71F, -12.2581F, 82.7F, 0.0F, 0.0F, -1.0297F));

		PartDefinition cube_r12 = hull.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 757).addBox(-3.0F, -3.0F, -93.0F, 6.0F, 6.0F, 186.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.4435F, -5.9741F, 82.7F, 0.0F, 0.0F, -0.3752F));

		PartDefinition nose = Plane.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(341, 246).addBox(-12.2483F, -1.0834F, -30.0F, 23.0F, 13.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -50.0F, -51.0F));

		PartDefinition x_centered_r1 = nose.addOrReplaceChild("x_centered_r1", CubeListBuilder.create().texOffs(84, 230).addBox(-11.525F, -4.5F, -20.0F, 23.05F, 9.0F, 40.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7483F, 0.1299F, -15.1198F, 0.3071F, 0.0F, 0.0F));

		PartDefinition cube_r13 = nose.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 52).addBox(-11.475F, -2.0F, -6.5F, 22.95F, 4.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7483F, 5.9176F, -34.7183F, -0.3527F, 0.0F, 0.0F));

		PartDefinition cube_r14 = nose.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(419, 246).addBox(-11.5F, -5.0F, -1.0F, 23.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7483F, 4.1555F, -39.3844F, -1.2637F, 0.0F, 0.0F));

		PartDefinition cube_r15 = nose.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(84, 279).addBox(-11.475F, -1.0F, -9.5F, 22.95F, 2.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7483F, 7.6967F, -38.5699F, -0.3527F, 0.0F, 0.0F));

		PartDefinition cube_r16 = nose.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(257, 325).addBox(-11.525F, -1.0F, -26.0F, 23.05F, 2.0F, 52.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7483F, -3.4291F, -22.0537F, 0.3071F, 0.0F, 0.0F));

		PartDefinition wings = Plane.addOrReplaceChild("wings", CubeListBuilder.create().texOffs(0, 0).addBox(-24.7483F, -29.7008F, 3.8978F, 48.0F, 6.0F, 224.0F, new CubeDeformation(0.0F))
		.texOffs(0, 675).addBox(-79.2483F, -29.775F, 85.4611F, 157.0F, 6.0F, 36.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -18.0F, 0.0F));

		PartDefinition cube_r17 = wings.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(190, 182).addBox(-3.0F, -0.5F, -5.5F, 6.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.8229F, -29.3943F, 13.0059F, 0.6981F, 0.0F, -0.8552F));

		PartDefinition cube_r18 = wings.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 282).addBox(-6.5F, -0.5F, -7.5F, 13.0F, 1.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.6201F, -35.7206F, 11.4738F, 0.6981F, 0.0F, -0.8552F));

		PartDefinition cube_r19 = wings.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(257, 258).addBox(-9.5F, -5.0F, -105.5F, 19.0F, 10.0F, 211.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.7422F, -33.5903F, 122.3978F, 0.0F, 0.0F, -0.8552F));

		PartDefinition cube_r20 = wings.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(37, 230).addBox(-3.0F, -0.5F, -5.5F, 6.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(18.3263F, -29.3943F, 13.0059F, 0.6981F, 0.0F, 0.8552F));

		PartDefinition cube_r21 = wings.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(413, 127).addBox(-6.5F, -0.5F, -7.5F, 13.0F, 1.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.1235F, -35.7206F, 11.4738F, 0.6981F, 0.0F, 0.8552F));

		PartDefinition cube_r22 = wings.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(320, 0).addBox(-9.5F, -5.0F, -105.5F, 19.0F, 10.0F, 211.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.2456F, -33.5903F, 122.3978F, 0.0F, 0.0F, 0.8552F));

		PartDefinition starboard_forward_r1 = wings.addOrReplaceChild("starboard_forward_r1", CubeListBuilder.create().texOffs(569, 76).addBox(-6.0F, -1.625F, -17.0F, 12.0F, 3.25F, 34.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.1768F, -26.7041F, -42.8543F, -0.0873F, -0.1309F, 0.0F));

		PartDefinition starboard_forward_r2 = wings.addOrReplaceChild("starboard_forward_r2", CubeListBuilder.create().texOffs(583, 221).addBox(-6.0F, -1.5F, -17.0F, 12.0F, 3.0F, 34.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.1702F, -28.1808F, -43.0577F, 0.0F, -0.1309F, 0.0F));

		PartDefinition starboard_forward_r3 = wings.addOrReplaceChild("starboard_forward_r3", CubeListBuilder.create().texOffs(51, 549).addBox(-6.5F, -3.0F, -16.5F, 13.0F, 6.0F, 33.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.1993F, -26.6908F, -10.3577F, 0.0F, -0.2618F, 0.0F));

		PartDefinition port_forward3_r1 = wings.addOrReplaceChild("port_forward3_r1", CubeListBuilder.create().texOffs(569, 113).addBox(-6.0F, -1.625F, -17.0F, 12.0F, 3.25F, 34.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.6802F, -26.7041F, -42.8543F, -0.0873F, 0.1309F, 0.0F));

		PartDefinition port_forward2_r1 = wings.addOrReplaceChild("port_forward2_r1", CubeListBuilder.create().texOffs(54, 588).addBox(-6.0F, -1.5F, -17.0F, 12.0F, 3.0F, 34.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.6736F, -28.1808F, -43.0577F, 0.0F, 0.1309F, 0.0F));

		PartDefinition port_forward_r1 = wings.addOrReplaceChild("port_forward_r1", CubeListBuilder.create().texOffs(569, 0).addBox(-6.5F, -3.0F, -16.5F, 13.0F, 6.0F, 33.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.7027F, -26.6908F, -10.3577F, 0.0F, 0.2618F, 0.0F));

		PartDefinition Lwing = wings.addOrReplaceChild("Lwing", CubeListBuilder.create().texOffs(0, 469).addBox(77.5085F, -25.7708F, 92.4611F, 35.0F, 2.0F, 42.0F, new CubeDeformation(0.0F))
		.texOffs(392, 0).addBox(77.5085F, -29.7008F, 102.4611F, 35.0F, 4.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(257, 246).addBox(112.4335F, -30.3508F, 67.4611F, 6.0F, 7.0F, 72.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Rupper_lead_edge_r1 = Lwing.addOrReplaceChild("Rupper_lead_edge_r1", CubeListBuilder.create().texOffs(268, 637).addBox(-67.0F, -1.0F, -7.0F, 134.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(62.414F, -26.5926F, 58.818F, 0.3054F, -0.6981F, 0.0F));

		PartDefinition Rsuperlead_r1 = Lwing.addOrReplaceChild("Rsuperlead_r1", CubeListBuilder.create().texOffs(268, 591).addBox(-58.5F, -2.0F, -21.0F, 117.0F, 4.0F, 42.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(47.027F, -27.6608F, 81.0448F, 0.0F, -0.6981F, 0.0F));

		PartDefinition Runder_lead_edge_r1 = Lwing.addOrReplaceChild("Runder_lead_edge_r1", CubeListBuilder.create().texOffs(268, 535).addBox(-58.0F, -1.0F, -27.0F, 116.0F, 2.0F, 54.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(50.7226F, -24.6F, 75.8627F, 0.0F, -0.6981F, 0.0F));

		PartDefinition left_flaperons = Lwing.addOrReplaceChild("left_flaperons", CubeListBuilder.create().texOffs(392, 36).addBox(-27.0F, -3.0F, -18.5F, 54.0F, 6.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(50.5085F, -26.7008F, 139.9611F));

		PartDefinition Rwing = wings.addOrReplaceChild("Rwing", CubeListBuilder.create().texOffs(257, 379).addBox(-112.5085F, -25.7708F, 92.4611F, 35.0F, 2.0F, 42.0F, new CubeDeformation(0.0F))
		.texOffs(72, 309).addBox(-112.5085F, -29.7008F, 102.4611F, 35.0F, 4.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(0, 230).addBox(-118.4335F, -30.3508F, 67.4611F, 6.0F, 7.0F, 72.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.4966F, 0.0F, 0.0F));

		PartDefinition Rupper_lead_edge_r2 = Rwing.addOrReplaceChild("Rupper_lead_edge_r2", CubeListBuilder.create().texOffs(257, 230).addBox(-67.0F, -1.0F, -7.0F, 134.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-62.414F, -26.5926F, 58.818F, 0.3054F, 0.6981F, 0.0F));

		PartDefinition Rsuperlead_r2 = Rwing.addOrReplaceChild("Rsuperlead_r2", CubeListBuilder.create().texOffs(506, 418).addBox(-58.5F, -2.0F, -21.0F, 117.0F, 4.0F, 42.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-47.027F, -27.6608F, 81.0448F, 0.0F, 0.6981F, 0.0F));

		PartDefinition Runder_lead_edge_r2 = Rwing.addOrReplaceChild("Runder_lead_edge_r2", CubeListBuilder.create().texOffs(268, 479).addBox(-58.0F, -1.0F, -27.0F, 116.0F, 2.0F, 54.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-50.7226F, -24.6F, 75.8627F, 0.0F, 0.6981F, 0.0F));

		PartDefinition right_flaperons = Rwing.addOrReplaceChild("right_flaperons", CubeListBuilder.create().texOffs(72, 345).addBox(-27.0F, -3.0F, -18.5F, 54.0F, 6.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(-50.5085F, -26.7008F, 139.9611F));

		PartDefinition tail = Plane.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(94, 0).addBox(-8.4317F, 5.215F, -43.4791F, 7.5F, 15.0F, 55.0F, new CubeDeformation(0.0F))
		.texOffs(320, 90).addBox(-8.1817F, 5.215F, 11.5209F, 7.0F, 13.0F, 21.0F, new CubeDeformation(0.0F))
		.texOffs(268, 479).addBox(14.7139F, 34.5975F, 13.457F, 5.0F, 6.0F, 22.0F, new CubeDeformation(0.0F))
		.texOffs(156, 279).addBox(-29.0773F, 34.5975F, 13.457F, 5.0F, 6.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(4.9334F, -82.2983F, 214.4791F));

		PartDefinition cube_r23 = tail.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(0, 415).addBox(-4.0F, -2.0F, -11.0F, 8.0F, 4.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-25.4967F, 32.9451F, 24.5209F, 0.0F, 0.0F, -1.0603F));

		PartDefinition cube_r24 = tail.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(60, 415).addBox(-4.0F, -2.0F, -11.0F, 8.0F, 4.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.1332F, 32.9451F, 24.5209F, 0.0F, 0.0F, 1.0603F));

		PartDefinition cube_r25 = tail.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -7.5F, -40.0F, 7.0F, 15.0F, 80.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.6817F, 20.2095F, -81.3132F, 0.192F, 0.0F, 0.0F));

		PartDefinition cube_r26 = tail.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(355, 325).addBox(-1.25F, -16.5F, -5.0F, 2.5F, 33.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.6817F, -58.0666F, 29.5462F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r27 = tail.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(407, 299).addBox(-1.0F, -40.0F, -13.0F, 2.0F, 80.0F, 26.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.6817F, -9.3943F, -7.6392F, -0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r28 = tail.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(133, 492).addBox(-1.5F, -34.5F, -10.5F, 3.0F, 69.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.6817F, -23.3991F, 14.7678F, -0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r29 = tail.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(188, 70).addBox(-0.5F, -45.0F, -5.0F, 1.0F, 90.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.6817F, -24.3219F, -10.3813F, -0.6545F, 0.0F, 0.0F));

		PartDefinition Laft = tail.addOrReplaceChild("Laft", CubeListBuilder.create(), PartPose.offset(19.503F, 42.5935F, -15.3453F));

		PartDefinition Runder_lead_edge_r3 = Laft.addOrReplaceChild("Runder_lead_edge_r3", CubeListBuilder.create().texOffs(576, 678).addBox(-34.0F, -1.0F, -27.0F, 68.0F, 2.0F, 54.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.8478F, -1.08F, 6.3019F, 0.0F, -0.6981F, 0.2182F));

		PartDefinition Rupper_lead_edge_r3 = Laft.addOrReplaceChild("Rupper_lead_edge_r3", CubeListBuilder.create().texOffs(0, 399).addBox(-35.0F, -1.0F, -7.0F, 70.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(21.4797F, -0.0389F, -9.808F, 0.3054F, -0.6981F, 0.2182F));

		PartDefinition cube_r30 = Laft.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(320, 159).addBox(-30.0F, -0.5F, -15.0F, 60.0F, 1.0F, 30.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.3588F, -4.6817F, 9.7804F, 0.0F, -0.6981F, 0.2182F));

		PartDefinition cube_r31 = Laft.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(190, 170).addBox(-4.0F, -1.5F, -4.5F, 8.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(43.766F, 5.2945F, 15.9234F, 0.0F, 0.48F, 0.3927F));

		PartDefinition cube_r32 = Laft.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(0, 143).addBox(-26.0F, -3.0F, -21.0F, 52.0F, 6.0F, 42.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(24.5477F, 0.5062F, 37.8023F, 0.0F, 0.0F, 0.2182F));

		PartDefinition Raft = tail.addOrReplaceChild("Raft", CubeListBuilder.create(), PartPose.offset(-28.8665F, 42.5935F, -15.3453F));

		PartDefinition Runder_lead_edge_r4 = Raft.addOrReplaceChild("Runder_lead_edge_r4", CubeListBuilder.create().texOffs(332, 678).addBox(-34.0F, -1.0F, -27.0F, 68.0F, 2.0F, 54.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.8478F, -1.08F, 6.3019F, 0.0F, 0.6981F, -0.2182F));

		PartDefinition Rupper_lead_edge_r4 = Raft.addOrReplaceChild("Rupper_lead_edge_r4", CubeListBuilder.create().texOffs(320, 190).addBox(-35.0F, -1.0F, -7.0F, 70.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-21.4797F, -0.0389F, -9.808F, 0.3054F, 0.6981F, -0.2182F));

		PartDefinition cube_r33 = Raft.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(0, 191).addBox(-30.0F, -0.5F, -15.0F, 60.0F, 1.0F, 30.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.3588F, -4.6817F, 9.7804F, 0.0F, 0.6981F, -0.2182F));

		PartDefinition cube_r34 = Raft.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(146, 163).addBox(-4.0F, -1.5F, -4.5F, 8.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-43.766F, 5.2945F, 15.9234F, 0.0F, -0.48F, -0.3927F));

		PartDefinition cube_r35 = Raft.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(0, 95).addBox(-26.0F, -3.0F, -21.0F, 52.0F, 6.0F, 42.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-24.5477F, 0.5062F, 37.8023F, 0.0F, 0.0F, -0.2182F));

		PartDefinition Rudder = tail.addOrReplaceChild("Rudder", CubeListBuilder.create(), PartPose.offset(-4.6817F, -29.3339F, 26.5093F));

		PartDefinition cube_r36 = Rudder.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(146, 582).addBox(-1.0F, -32.0F, -7.0F, 2.0F, 64.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 6.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition gear = Plane.addOrReplaceChild("gear", CubeListBuilder.create(), PartPose.offset(0.2517F, 0.0F, 0.0F));

		PartDefinition set1 = gear.addOrReplaceChild("set1", CubeListBuilder.create().texOffs(16, 230).addBox(-2.0F, -6.5F, -2.0F, 4.0F, 25.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(124, 0).addBox(-6.0F, 12.5F, -4.0F, 4.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(94, 0).addBox(2.0F, 12.5F, -4.0F, 4.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -20.5F, 23.0F));

		PartDefinition setL = gear.addOrReplaceChild("setL", CubeListBuilder.create().texOffs(146, 143).addBox(8.5F, 14.25F, -33.0F, 4.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(11.2483F, -24.25F, 141.0F));

		PartDefinition cube_r37 = setL.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(0, 230).addBox(-2.0F, -12.5F, -2.0F, 4.0F, 25.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, 9.75F, -28.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition setR = gear.addOrReplaceChild("setR", CubeListBuilder.create().texOffs(146, 106).addBox(-12.5F, 14.25F, -33.0F, 4.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-11.2483F, -24.25F, 141.0F));

		PartDefinition cube_r38 = setR.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(0, 191).addBox(-2.0F, -12.5F, -2.0F, 4.0F, 25.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, 9.75F, -28.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition Afterburner2 = Plane.addOrReplaceChild("Afterburner2", CubeListBuilder.create(), PartPose.offset(0.2517F, 0.0F, 0.0F));

		PartDefinition hexadecagon = Afterburner2.addOrReplaceChild("hexadecagon", CubeListBuilder.create().texOffs(577, 324).addBox(-3.5F, 12.5957F, -10.0F, 7.0F, 4.0F, 22.0F, new CubeDeformation(0.0F))
		.texOffs(665, 86).addBox(13.5957F, -4.5F, -12.0F, 4.0F, 7.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(628, 0).addBox(-17.5957F, -4.5F, -12.0F, 4.0F, 7.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -41.0F, 225.0F));

		PartDefinition hexadecagon_r1 = hexadecagon.addOrReplaceChild("hexadecagon_r1", CubeListBuilder.create().texOffs(165, 0).addBox(-17.5957F, -3.5F, 3.0F, 4.0F, 7.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(661, 7).addBox(13.5957F, -3.5F, -12.0F, 4.0F, 7.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(320, 41).addBox(-3.5F, 13.5957F, -9.0F, 7.0F, 4.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition hexadecagon_r2 = hexadecagon.addOrReplaceChild("hexadecagon_r2", CubeListBuilder.create().texOffs(637, 155).addBox(-17.5957F, -3.5F, -12.0F, 4.0F, 7.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(195, 0).addBox(13.5957F, -3.5F, 3.0F, 4.0F, 7.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(320, 124).addBox(-3.5F, 13.5957F, -9.0F, 7.0F, 4.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition hexadecagon_r3 = hexadecagon.addOrReplaceChild("hexadecagon_r3", CubeListBuilder.create().texOffs(94, 34).addBox(-5.5F, -2.0F, -6.5F, 11.0F, 4.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.5392F, -13.7565F, 7.7399F, -0.48F, 0.0F, -0.8552F));

		PartDefinition hexadecagon_r4 = hexadecagon.addOrReplaceChild("hexadecagon_r4", CubeListBuilder.create().texOffs(257, 356).addBox(-5.5F, -2.0F, -6.5F, 11.0F, 4.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.5392F, -13.7565F, 7.7399F, -0.48F, 0.0F, 0.8552F));

		PartDefinition hexadecagon_r5 = hexadecagon.addOrReplaceChild("hexadecagon_r5", CubeListBuilder.create().texOffs(38, 415).addBox(-5.5F, -2.0F, -5.5F, 11.0F, 4.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.9464F, 7.1395F, -0.4363F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r6 = hexadecagon.addOrReplaceChild("hexadecagon_r6", CubeListBuilder.create().texOffs(0, 350).addBox(-3.5F, 13.5957F, -8.0F, 7.0F, 4.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition hexadecagon_r7 = hexadecagon.addOrReplaceChild("hexadecagon_r7", CubeListBuilder.create().texOffs(268, 507).addBox(-3.5F, 13.5957F, -8.0F, 7.0F, 4.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition hull_connector = Afterburner2.addOrReplaceChild("hull_connector", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r39 = hull_connector.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(609, 359).addBox(-3.0F, -1.5F, -12.0F, 6.0F, 3.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.6219F, -25.4207F, 214.9029F, 0.2618F, 0.0F, 0.3752F));

		PartDefinition cube_r40 = hull_connector.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(382, 405).addBox(-6.0F, -1.5F, -12.0F, 12.0F, 3.0F, 29.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.2686F, -29.7712F, 214.9029F, 0.2618F, 0.0F, 1.0297F));

		PartDefinition cube_r41 = hull_connector.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(94, 0).addBox(-1.5F, -5.0F, -12.0F, 3.0F, 10.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.6953F, -37.5684F, 214.9029F, 0.0F, 0.2618F, 0.0F));

		PartDefinition cube_r42 = hull_connector.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(40, 625).addBox(-3.0F, -1.5F, -14.0F, 6.0F, 3.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.4322F, -25.9023F, 216.8347F, 0.2618F, 0.0F, -0.3752F));

		PartDefinition cube_r43 = hull_connector.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(44, 513).addBox(-6.0F, -1.5F, -14.5F, 12.0F, 3.0F, 29.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.714F, -30.1045F, 217.3177F, 0.2618F, 0.0F, -1.0297F));

		PartDefinition cube_r44 = hull_connector.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(470, 135).addBox(-1.5F, -5.0F, -12.0F, 3.0F, 10.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.6953F, -37.5684F, 214.9029F, 0.0F, -0.2618F, 0.0F));

		PartDefinition x_centered_r2 = hull_connector.addOrReplaceChild("x_centered_r2", CubeListBuilder.create().texOffs(341, 291).addBox(-8.5F, -1.5F, -12.0F, 17.0F, 3.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -24.6381F, 214.9029F, 0.2618F, 0.0F, 0.0F));

		PartDefinition hexadecagon2 = Afterburner2.addOrReplaceChild("hexadecagon", CubeListBuilder.create(), PartPose.offset(8.0F, -34.0F, 249.0F));

		PartDefinition hexadecagon_r8 = hexadecagon2.addOrReplaceChild("hexadecagon_r8", CubeListBuilder.create().texOffs(627, 76).addBox(-17.5957F, -3.5F, -20.0F, 4.0F, 7.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.3861F, -6.5031F, 3.7175F, 0.0F, 0.2182F, 0.3927F));

		PartDefinition hexadecagon_r9 = hexadecagon2.addOrReplaceChild("hexadecagon_r9", CubeListBuilder.create().texOffs(627, 113).addBox(-17.5957F, -3.5F, -20.0F, 4.0F, 7.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0883F, -8.0F, 3.7175F, 0.0F, 0.2182F, 0.0F));

		PartDefinition hexadecagon_r10 = hexadecagon2.addOrReplaceChild("hexadecagon_r10", CubeListBuilder.create().texOffs(628, 39).addBox(-17.5957F, -3.5F, -20.0F, 4.0F, 7.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.3861F, -9.4969F, 3.7175F, 0.0F, 0.2182F, -0.3927F));

		PartDefinition hexadecagon_r11 = hexadecagon2.addOrReplaceChild("hexadecagon_r11", CubeListBuilder.create().texOffs(628, 258).addBox(13.5957F, -3.5F, -20.0F, 4.0F, 7.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.6139F, -9.4969F, 3.7175F, 0.0F, -0.2182F, 0.3927F));

		PartDefinition hexadecagon_r12 = hexadecagon2.addOrReplaceChild("hexadecagon_r12", CubeListBuilder.create().texOffs(559, 628).addBox(13.5957F, -3.5F, -20.0F, 4.0F, 7.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.9117F, -8.0F, 3.7175F, 0.0F, -0.2182F, 0.0F));

		PartDefinition hexadecagon_r13 = hexadecagon2.addOrReplaceChild("hexadecagon_r13", CubeListBuilder.create().texOffs(81, 629).addBox(13.5957F, -3.5F, -20.0F, 4.0F, 7.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.6139F, -6.5031F, 3.7175F, 0.0F, -0.2182F, -0.3927F));

		PartDefinition hexadecagon_r14 = hexadecagon2.addOrReplaceChild("hexadecagon_r14", CubeListBuilder.create().texOffs(257, 246).addBox(-3.5F, -17.5957F, -20.0F, 7.0F, 4.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.766F, -5.234F, 3.7175F, -0.2182F, 0.0F, 0.7854F));

		PartDefinition hexadecagon_r15 = hexadecagon2.addOrReplaceChild("hexadecagon_r15", CubeListBuilder.create().texOffs(0, 563).addBox(-3.5F, -17.5957F, -30.0F, 7.0F, 4.0F, 37.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.4969F, -4.3861F, 3.7175F, -0.2182F, 0.0F, 0.3927F));

		PartDefinition hexadecagon_r16 = hexadecagon2.addOrReplaceChild("hexadecagon_r16", CubeListBuilder.create().texOffs(554, 479).addBox(-3.5F, -17.5957F, -28.0F, 7.0F, 4.0F, 35.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -4.0883F, 3.7175F, -0.2182F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r17 = hexadecagon2.addOrReplaceChild("hexadecagon_r17", CubeListBuilder.create().texOffs(577, 283).addBox(-3.5F, -17.5957F, -30.0F, 7.0F, 4.0F, 37.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5031F, -4.3861F, 3.7175F, -0.2182F, 0.0F, -0.3927F));

		PartDefinition hexadecagon_r18 = hexadecagon2.addOrReplaceChild("hexadecagon_r18", CubeListBuilder.create().texOffs(257, 277).addBox(-3.5F, -17.5957F, -20.0F, 7.0F, 4.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.234F, -5.234F, 3.7175F, -0.2182F, 0.0F, -0.7854F));

		PartDefinition hexadecagon_r19 = hexadecagon2.addOrReplaceChild("hexadecagon_r19", CubeListBuilder.create().texOffs(138, 364).addBox(-3.5F, 13.5957F, -20.0F, 7.0F, 4.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.234F, -10.766F, 3.7175F, 0.2182F, 0.0F, 0.7854F));

		PartDefinition hexadecagon_r20 = hexadecagon2.addOrReplaceChild("hexadecagon_r20", CubeListBuilder.create().texOffs(141, 395).addBox(-3.5F, 13.5957F, -20.0F, 7.0F, 4.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5031F, -11.6139F, 3.7175F, 0.2182F, 0.0F, 0.3927F));

		PartDefinition hexadecagon_r21 = hexadecagon2.addOrReplaceChild("hexadecagon_r21", CubeListBuilder.create().texOffs(458, 55).addBox(-3.5F, 13.5957F, -20.0F, 7.0F, 4.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -11.9117F, 3.7175F, 0.2182F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r22 = hexadecagon2.addOrReplaceChild("hexadecagon_r22", CubeListBuilder.create().texOffs(0, 604).addBox(-3.5F, 13.5957F, -20.0F, 7.0F, 4.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.4969F, -11.6139F, 3.7175F, 0.2182F, 0.0F, -0.3927F));

		PartDefinition hexadecagon_r23 = hexadecagon2.addOrReplaceChild("hexadecagon_r23", CubeListBuilder.create().texOffs(609, 328).addBox(-3.5F, 13.5957F, -20.0F, 7.0F, 4.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.766F, -10.766F, 3.7175F, 0.2182F, 0.0F, -0.7854F));

		return LayerDefinition.create(meshdefinition, 1024, 1024);
	}

}
