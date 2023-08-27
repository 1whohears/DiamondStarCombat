package com.onewhohears.dscombat.client.model.aircraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.model.EntityControllableModel;
import com.onewhohears.dscombat.entity.aircraft.EntityHelicopter;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class EntityModelNoahChopper extends EntityControllableModel<EntityHelicopter> {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "noah_chopper"), "main");
	private final ModelPart body;
	private final ModelPart top_rotor;
	private final ModelPart tail_rotor;

	public EntityModelNoahChopper(ModelPart root) {
		this.body = root.getChild("body");
		this.top_rotor = body.getChild("bladetop");
		this.tail_rotor = body.getChild("tail").getChild("bladetail");
	}
	
	@Override
	public void renderToBuffer(EntityHelicopter entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer,
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0, 1.5, 0);
		poseStack.scale(1.0F, -1.0F, 1.0F);
		top_rotor.yRot = entity.getPropellerRotation(partialTicks);
		tail_rotor.xRot = entity.getPropellerRotation(partialTicks);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 34.0F, 12.0F));

		PartDefinition frame = body.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(128, 68).addBox(-12.0F, -0.0349F, -16.5427F, 24.0F, 1.0F, 38.5427F, new CubeDeformation(0.0F))
		.texOffs(40, 10).addBox(-12.0F, -10.0F, -16.0F, 1.0F, 10.0F, 38.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(11.0F, -10.0F, -16.0F, 1.0F, 10.0F, 38.0F, new CubeDeformation(0.0F))
		.texOffs(0, 58).addBox(-8.0F, -30.0F, -9.8433F, 16.0521F, 1.0F, 22.8433F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = frame.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -0.0483F, -12.0075F, 8.0F, 8.009F, 4.01F, new CubeDeformation(0.0F))
		.texOffs(80, 36).addBox(-2.5F, -0.033F, 8.0025F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0025F, -17.967F, -19.5F, 1.5708F, -1.309F, -1.5708F));

		PartDefinition cube_r2 = frame.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(30, 29).addBox(29.0F, 2.0F, 11.5F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(40, 0).addBox(29.0F, 2.0F, -11.5F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -20.0F, -30.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r3 = frame.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(128, 16).addBox(-12.0F, -7.2011F, 0.9573F, 24.01F, 12.6662F, 4.4157F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5F, -17.5F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r4 = frame.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(128, 33).addBox(-12.0F, -5.5948F, -0.703F, 24.01F, 11.0948F, 3.9955F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5F, -17.5F, 0.3054F, 0.0F, 0.0F));

		PartDefinition cube_r5 = frame.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(128, 0).addBox(-8.9981F, -9.113F, -2.5F, 17.9911F, 1.08F, 14.9819F, new CubeDeformation(0.0F))
		.texOffs(70, 126).addBox(-9.0025F, -9.033F, -2.5F, 18.0F, 17.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0025F, -17.967F, -19.5F, 0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r6 = frame.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(63, 92).addBox(-0.5F, -5.9904F, 14.0F, 1.1046F, 12.6491F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.4266F, -24.1605F, -15.0F, 0.0F, 0.0F, 0.3229F));

		PartDefinition cube_r7 = frame.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(80, 0).addBox(-11.4247F, -5.7869F, -2.5F, 4.1222F, 9.5641F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0025F, -17.967F, -19.5F, 0.2488F, -0.0822F, 0.3126F));

		PartDefinition cube_r8 = frame.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(69, 92).addBox(-0.5F, -5.9904F, 14.0F, 1.1046F, 12.6378F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.3274F, -24.1273F, -15.0F, 0.0F, 0.0F, -0.3229F));

		PartDefinition cube_r9 = frame.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(80, 18).addBox(7.2977F, -5.7885F, -2.5F, 4.1222F, 9.5528F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0025F, -17.967F, -19.5F, 0.2488F, 0.0822F, -0.3126F));

		PartDefinition cube_r10 = frame.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 59).addBox(16.9859F, -0.5F, -12.0545F, -0.9443F, 1.0F, 22.8238F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.5F, -29.5F, 1.5F, 0.0F, -0.0436F, 0.0F));

		PartDefinition cube_r11 = frame.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(55, 92).addBox(-0.0021F, -0.5F, -11.3129F, 1.0437F, 1.0F, 22.8238F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.5F, -29.5F, 1.5F, 0.0F, 0.0436F, 0.0F));

		PartDefinition cube_r12 = frame.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(14, 56).addBox(0.055F, -7.8585F, -1.0075F, 1.7496F, 16.0501F, 1.0307F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.2525F, -22.8653F, 16.7056F, 0.4659F, 0.1198F, -0.2159F));

		PartDefinition cube_r13 = frame.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(69, 58).addBox(-2.7496F, -8.1359F, -1.4324F, 1.7496F, 16.0503F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.3514F, -22.6051F, 17.3643F, 0.4659F, -0.1198F, 0.2246F));

		PartDefinition cube_r14 = frame.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(128, 48).addBox(-12.0F, -11.5F, -12.0F, 24.0F, 18.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 92).addBox(-12.0F, -11.5F, -13.0F, 24.0F, 1.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.2947F, 27.5072F, -0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r15 = frame.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(124, 193).addBox(-12.0F, -1.7272F, -3.4599F, 24.0F, 1.0F, 11.9599F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 25.5F, 0.3491F, 0.0F, 0.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(140, 132).addBox(-3.0F, -20.0F, -76.0F, 6.0F, 5.0F, 56.0F, new CubeDeformation(0.0F))
		.texOffs(0, 83).addBox(-0.5F, -32.0F, -76.0F, 1.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(80, 58).addBox(-0.5F, -15.0F, -76.0F, 1.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(40, 19).addBox(-12.0F, -11.4172F, 22.0F, 1.0F, 8.3266F, 11.3198F, new CubeDeformation(0.0F))
		.texOffs(40, 0).addBox(11.0F, -11.4172F, 22.0F, 1.0F, 8.3266F, 11.3198F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r16 = tail.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -2.3808F, -12.5262F, 0.0F, 8.2902F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(12.0F, -2.3808F, -12.5262F, 0.0F, 8.2902F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.8236F, 31.712F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r17 = tail.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 14).addBox(12.5F, -2.3808F, -15.5262F, 0.0F, 8.2902F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -9.0F, 33.0F, -0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r18 = tail.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 33).addBox(-12.0F, -2.3808F, -15.5262F, 0.0F, 8.2902F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, 33.0F, -0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r19 = tail.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(0, 126).addBox(-0.5F, -2.4172F, -12.0F, 1.0F, 8.3266F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, 33.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r20 = tail.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(0, 56).addBox(-0.5F, -6.0F, -3.0F, 1.0F, 13.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.6039F, -70.4249F, -0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r21 = tail.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(55, 58).addBox(-0.5F, -6.0F, -3.0F, 1.0F, 13.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -25.3358F, -70.767F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r22 = tail.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(70, 126).addBox(-3.0F, 0.5F, -28.0F, 6.0F, 5.0F, 57.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -18.0577F, -48.063F, -0.0873F, 0.0F, 0.0F));

		PartDefinition bladetail = tail.addOrReplaceChild("bladetail", CubeListBuilder.create().texOffs(40, 26).addBox(-3.0F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(46, 58).addBox(-4.0F, -1.0F, -16.0F, 1.0F, 2.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(64, 0).addBox(-4.0F, -16.0F, -1.0F, 1.0F, 32.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -17.0F, -71.0F));

		PartDefinition gear = body.addOrReplaceChild("gear", CubeListBuilder.create().texOffs(128, 0).addBox(-16.2214F, 10.2557F, -32.0F, 2.0F, 2.0F, 66.0F, new CubeDeformation(0.0F))
		.texOffs(0, 126).addBox(14.2214F, 10.2557F, -32.0F, 2.0F, 2.0F, 66.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r23 = gear.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(70, 0).addBox(-2.0F, -11.0F, 23.5F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(70, 14).addBox(-2.0F, -11.0F, -0.5F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 10.0F, -11.5F, 0.0F, 0.0F, 0.3491F));

		PartDefinition cube_r24 = gear.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 14.0F, -14.5F, 2.0F, 0.0F, 29.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7532F, -11.6496F, 2.5F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r25 = gear.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(4, 0).addBox(0.8284F, -3.8284F, 7.0F, 2.0F, 0.0F, 29.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.2378F, 1.0776F, -21.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r26 = gear.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(80, 76).addBox(0.0F, -11.0F, -0.5F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(88, 76).addBox(0.0F, -11.0F, 23.5F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 10.0F, -11.5F, 0.0F, 0.0F, -0.3491F));

		PartDefinition bladetop = body.addOrReplaceChild("bladetop", CubeListBuilder.create().texOffs(40, 19).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -3.0F, -60.0F, 4.0F, 1.0F, 120.0F, new CubeDeformation(0.0F))
		.texOffs(0, 121).addBox(-60.0F, -3.0F, -2.0F, 120.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -33.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

}
