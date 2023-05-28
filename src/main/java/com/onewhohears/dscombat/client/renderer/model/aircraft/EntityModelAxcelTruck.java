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
import net.minecraft.util.Mth;

public class EntityModelAxcelTruck extends EntityControllableModel<EntityGroundVehicle> {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "axcel_truck"), "main");
	
	private final ModelPart body;
	private final ModelPart radar;
	private final ModelPart stearing;
	private final ModelPart[] wheels = new ModelPart[6];

	public EntityModelAxcelTruck(ModelPart root) {
		this.body = root.getChild("body");
		this.radar = body.getChild("radar");
		this.stearing = body.getChild("stearing");
		ModelPart w = body.getChild("wheels");
		wheels[0] = w.getChild("w1");
		wheels[1] = w.getChild("w3");
		wheels[2] = w.getChild("w4");
		wheels[3] = w.getChild("w2");
		wheels[4] = w.getChild("w5");
		wheels[5] = w.getChild("w6");
	}
	
	@Override
	public void renderToBuffer(EntityGroundVehicle entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer, 
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0, 1.5, 0);
		poseStack.scale(-1.0F, -1.0F, 1.0F);
		float yf = Mth.PI*0.25f;
		if (entity.radarSystem.hasRadar()) {
			radar.visible = true;
			radar.yRot = Mth.lerp(partialTicks, (entity.tickCount-1)*0.1f, entity.tickCount*0.1f);
		} else radar.visible = false;
		stearing.zRot = entity.inputs.yaw * yf;
		wheels[0].yRot = entity.inputs.yaw * yf;
		wheels[3].yRot = entity.inputs.yaw * yf;
		for (int i = 0; i < 3; ++i) wheels[i].xRot = -entity.getWheelLeftRotation(partialTicks);
		for (int i = 3; i < 6; ++i) wheels[i].xRot = -entity.getWheelRightRotation(partialTicks);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition frame = body.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -15.0F, -37.0F, 32.0F, 2.0F, 67.0F, new CubeDeformation(0.0F))
		.texOffs(93, 103).addBox(-16.0F, -13.0F, 21.0F, 32.0F, 8.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(151, 16).addBox(-10.0F, -8.0F, 12.0F, 20.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 69).addBox(-9.0F, -13.0F, -36.0F, 18.0F, 5.0F, 57.0F, new CubeDeformation(0.0F))
		.texOffs(142, 132).addBox(-10.0F, -8.0F, -16.0F, 20.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 118).addBox(-10.0F, -8.0F, -31.0F, 20.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(131, 0).addBox(-16.0F, -27.0F, 26.0F, 32.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(80, 131).addBox(-16.0F, -27.0F, 8.0F, 1.0F, 12.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(131, 16).addBox(15.0F, -27.0F, 8.0F, 1.0F, 12.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(0, 131).addBox(-16.0F, -42.0F, 0.0F, 32.0F, 27.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(93, 69).addBox(-16.0F, -46.0F, 0.0F, 32.0F, 4.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(8, 0).addBox(14.0F, -42.0F, 28.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-16.0F, -42.0F, 28.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 69).addBox(-16.0F, -42.0F, 8.0F, 1.0F, 15.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(15.0F, -42.0F, 8.0F, 1.0F, 15.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(0, 50).addBox(-14.0F, -42.0F, 29.0F, 28.0F, 15.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wheels = body.addOrReplaceChild("wheels", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition w1 = wheels.addOrReplaceChild("w1", CubeListBuilder.create().texOffs(0, 166).addBox(-3.0F, -6.0F, -6.0F, 6.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, -6.0F, 14.0F));

		PartDefinition w2 = wheels.addOrReplaceChild("w2", CubeListBuilder.create().texOffs(163, 108).addBox(-3.0F, -6.0F, -6.0F, 6.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(13.0F, -6.0F, 14.0F));

		PartDefinition w3 = wheels.addOrReplaceChild("w3", CubeListBuilder.create().texOffs(68, 161).addBox(-3.0F, -6.0F, -6.0F, 6.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, -6.0F, -14.0F));

		PartDefinition w4 = wheels.addOrReplaceChild("w4", CubeListBuilder.create().texOffs(106, 155).addBox(-3.0F, -6.0F, -6.0F, 6.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, -6.0F, -29.0F));

		PartDefinition w5 = wheels.addOrReplaceChild("w5", CubeListBuilder.create().texOffs(142, 143).addBox(-3.0F, -6.0F, -6.0F, 6.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(13.0F, -6.0F, -14.0F));

		PartDefinition w6 = wheels.addOrReplaceChild("w6", CubeListBuilder.create().texOffs(118, 131).addBox(-3.0F, -6.0F, -6.0F, 6.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(13.0F, -6.0F, -29.0F));

		PartDefinition radar = body.addOrReplaceChild("radar", CubeListBuilder.create().texOffs(0, 248).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(21, 250).addBox(-12.0F, -6.0F, -2.0F, 24.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -46.0F, 15.0F));

		PartDefinition seats = body.addOrReplaceChild("seats", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition seat1 = seats.addOrReplaceChild("seat1", CubeListBuilder.create().texOffs(131, 46).addBox(-13.0F, -17.0F, 10.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(93, 69).addBox(-13.0F, -31.0F, 10.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition seat2 = seats.addOrReplaceChild("seat2", CubeListBuilder.create().texOffs(0, 104).addBox(1.0F, -17.0F, 10.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(22, 69).addBox(1.0F, -31.0F, 10.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition stearing = body.addOrReplaceChild("stearing", CubeListBuilder.create().texOffs(22, 4).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(22, 2).addBox(-3.0F, -0.5F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(22, 0).addBox(-3.0F, -4.0F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).addBox(-3.0F, 3.0F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 7).addBox(-4.0F, -3.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 0).addBox(3.0F, -3.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, -27.0F, 26.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

}
