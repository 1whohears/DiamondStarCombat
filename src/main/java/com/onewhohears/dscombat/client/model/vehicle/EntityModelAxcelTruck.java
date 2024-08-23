package com.onewhohears.dscombat.client.model.vehicle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.model.EntityControllableModel;
import com.onewhohears.dscombat.entity.vehicle.EntityGroundVehicle;

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
		if (entity.radarSystem.hasRadar("axcel_truck_radar")) {
			radar.visible = true;
			radar.yRot = Mth.lerp(partialTicks, (entity.tickCount-1)*0.1f, entity.tickCount*0.1f);
		} else radar.visible = false;
		stearing.zRot = entity.inputs.yaw * yf;
		wheels[0].yRot = entity.inputs.yaw * yf;
		wheels[3].yRot = entity.inputs.yaw * yf;
		for (int i = 0; i < 3; ++i) wheels[i].xRot = -entity.getWheelRotation(partialTicks, 1.5f);
		for (int i = 3; i < 6; ++i) wheels[i].xRot = -entity.getWheelRotation(partialTicks, 1.5f);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition frame = body.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -15.0F, -62.0F, 32.0F, 2.0F, 92.0F, new CubeDeformation(0.0F))
		.texOffs(0, 94).addBox(-16.0F, -16.0F, -62.0F, 32.0F, 1.0F, 62.0F, new CubeDeformation(0.0F))
		.texOffs(80, 173).addBox(-14.0F, -13.0F, 23.0F, 28.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(80, 394).addBox(-17.548F, -13.0F, 20.8229F, 3.548F, 8.0F, 8.3301F, new CubeDeformation(0.0F))
		.texOffs(56, 394).addBox(14.0F, -13.0F, 20.8229F, 3.548F, 8.0F, 8.3301F, new CubeDeformation(0.0F))
		.texOffs(28, 394).addBox(-17.548F, -15.0828F, 8.7823F, 3.548F, 1.9147F, 10.4353F, new CubeDeformation(0.0F))
		.texOffs(0, 192).addBox(-17.548F, -15.0828F, -55.2177F, 3.548F, 1.9147F, 25.4353F, new CubeDeformation(0.0F))
		.texOffs(90, 449).addBox(-17.548F, -13.2442F, -46.2177F, 3.548F, 1.0761F, 7.4325F, new CubeDeformation(0.0F))
		.texOffs(50, 485).addBox(-17.548F, -13.2442F, -43.2222F, 3.548F, 7.7251F, 1.3993F, new CubeDeformation(0.0F))
		.texOffs(34, 362).addBox(9.0F, -13.5191F, -1.153F, 8.548F, 8.0F, 8.3301F, new CubeDeformation(0.0F))
		.texOffs(68, 449).addBox(-17.548F, -13.5191F, -28.1771F, 8.548F, 8.0F, 2.3301F, new CubeDeformation(0.0F))
		.texOffs(10, 475).addBox(-17.548F, -12.1462F, -58.1543F, 8.548F, 6.6271F, 1.3073F, new CubeDeformation(0.0F))
		.texOffs(10, 467).addBox(9.0F, -12.1462F, -58.1543F, 8.548F, 6.6271F, 1.3073F, new CubeDeformation(0.0F))
		.texOffs(40, 485).addBox(14.0F, -13.2442F, -43.2222F, 3.548F, 7.7251F, 1.3993F, new CubeDeformation(0.0F))
		.texOffs(68, 459).addBox(14.0F, -13.2442F, -46.2177F, 3.548F, 1.0761F, 7.4325F, new CubeDeformation(0.0F))
		.texOffs(152, 157).addBox(14.0F, -15.0828F, -55.2177F, 3.548F, 1.9147F, 25.4353F, new CubeDeformation(0.0F))
		.texOffs(46, 449).addBox(9.0F, -13.5191F, -28.1771F, 8.548F, 8.0F, 2.3301F, new CubeDeformation(0.0F))
		.texOffs(0, 362).addBox(-17.548F, -13.5191F, -1.153F, 8.548F, 8.0F, 8.3301F, new CubeDeformation(0.0F))
		.texOffs(0, 394).addBox(14.0F, -15.0828F, 8.7823F, 3.548F, 1.9147F, 10.4353F, new CubeDeformation(0.0F))
		.texOffs(144, 282).addBox(-10.0F, -8.0F, 12.0F, 20.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(248, 0).addBox(-9.0F, -13.0F, -61.0F, 18.0F, 5.0F, 84.0F, new CubeDeformation(0.0F))
		.texOffs(96, 290).addBox(-10.0F, -8.0F, -37.0F, 20.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(96, 282).addBox(-10.0F, -8.0F, -52.0F, 20.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(80, 157).addBox(-16.0F, -27.0F, 26.0F, 32.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(68, 485).addBox(-15.5F, -24.0F, 29.5F, 3.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(70, 501).addBox(-15.5F, -15.5F, -62.5F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(70, 498).addBox(9.5F, -15.5F, -62.5F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(60, 485).addBox(12.5F, -24.0F, 29.5F, 3.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(224, 248).addBox(-16.0F, -27.0F, 8.0F, 1.0F, 12.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(186, 248).addBox(15.0F, -27.0F, 8.0F, 1.0F, 12.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(0, 157).addBox(-16.0F, -42.0F, 0.0F, 32.0F, 27.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(120, 394).addBox(-21.0F, -33.0F, 1.0F, 4.0F, 19.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 485).addBox(-20.0F, -44.0F, 2.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(84, 510).addBox(-20.0F, -14.0F, 2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 485).addBox(18.0F, -44.0F, 2.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(104, 394).addBox(17.0F, -33.0F, 1.0F, 4.0F, 19.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(76, 510).addBox(18.0F, -14.0F, 2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(188, 94).addBox(-16.0F, -46.0F, 4.0F, 32.0F, 4.0F, 22.0F, new CubeDeformation(0.0F))
		.texOffs(0, 467).addBox(14.0F, -42.0F, 27.0F, 2.0F, 15.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(178, 449).addBox(-16.0F, -42.0F, 27.0F, 2.0F, 15.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(102, 467).addBox(-21.0F, -38.0F, 25.0F, 4.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(152, 518).addBox(-20.0F, -33.0F, 26.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(144, 518).addBox(-18.5858F, -33.0F, 27.4142F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(112, 467).addBox(17.0F, -38.0F, 25.0F, 4.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(136, 515).addBox(18.0F, -33.0F, 26.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(144, 515).addBox(15.5858F, -33.0F, 27.4142F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(68, 372).addBox(-11.0F, -25.0F, 30.0F, 22.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(100, 498).addBox(11.0F, -25.0F, 30.0F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(96, 498).addBox(-12.0F, -25.0F, 30.0F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(68, 370).addBox(-11.0F, -23.0F, 30.0F, 22.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(68, 366).addBox(-11.0F, -21.0F, 30.0F, 22.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(68, 368).addBox(-11.0F, -19.0F, 30.0F, 22.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(68, 362).addBox(-11.0F, -17.0F, 30.0F, 22.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(68, 364).addBox(-11.0F, -15.0F, 30.0F, 22.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = frame.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(152, 515).addBox(-20.0F, -21.0F, 26.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.9411F, -12.0F, 22.3431F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r2 = frame.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(136, 518).addBox(18.0F, -21.0F, 26.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.9411F, -12.0F, 22.3431F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r3 = frame.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(58, 192).addBox(-16.0F, -19.0F, 1.3431F, 32.0F, 3.6569F, 5.6569F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -33.5147F, 11.6152F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r4 = frame.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(58, 219).addBox(-16.0F, -19.0F, 4.0F, 32.0F, 5.6569F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -35.3934F, -12.2635F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r5 = frame.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(70, 504).addBox(-3.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(18.5858F, -12.0F, 3.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r6 = frame.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(68, 510).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(92, 510).addBox(-39.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.0F, -44.0F, 2.5858F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r7 = frame.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(84, 498).addBox(-1.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-18.5858F, -12.0F, 3.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r8 = frame.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(156, 449).addBox(7.452F, -3.8104F, -3.5F, 8.548F, 7.8104F, 1.7315F, new CubeDeformation(0.0F))
		.texOffs(134, 449).addBox(-19.0961F, -3.8104F, -3.5F, 8.548F, 7.8104F, 1.7315F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.548F, -10.1818F, 1.3983F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r9 = frame.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(122, 467).addBox(-2.0F, -4.0F, 2.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.927F, -9.0F, 26.5285F, 0.0F, 0.48F, 0.0F));

		PartDefinition cube_r10 = frame.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(134, 467).addBox(-2.0F, -4.0F, 2.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.927F, -9.0F, 26.5285F, 0.0F, -0.48F, 0.0F));

		PartDefinition cube_r11 = frame.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(48, 467).addBox(-1.774F, -12.0F, -16.0765F, 3.548F, 3.0F, 5.153F, new CubeDeformation(0.0F))
		.texOffs(48, 475).addBox(-33.3221F, -12.0F, -16.0765F, 3.548F, 3.0F, 5.153F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.774F, 4.7703F, 22.1002F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r12 = frame.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(50, 510).addBox(-1.774F, -10.0F, -15.0765F, 3.548F, 3.0F, 1.153F, new CubeDeformation(0.0F))
		.texOffs(40, 510).addBox(-33.3221F, -11.0F, -15.0765F, 3.548F, 4.0F, 1.153F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.774F, -6.8215F, 37.4305F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r13 = frame.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(42, 498).addBox(-1.774F, -10.0F, -15.0765F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F))
		.texOffs(56, 504).addBox(-33.3221F, -10.0F, -15.0765F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.774F, 0.8402F, 25.6996F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r14 = frame.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(28, 504).addBox(-1.774F, -10.0F, 11.9235F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F))
		.texOffs(42, 502).addBox(-33.3221F, -10.0F, 11.9235F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.774F, 0.8402F, 2.3004F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r15 = frame.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(66, 477).addBox(-1.774F, -12.0F, 11.9235F, 3.548F, 3.0F, 4.153F, new CubeDeformation(0.0F))
		.texOffs(86, 467).addBox(-33.3221F, -12.0F, 11.9235F, 3.548F, 3.0F, 4.153F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.774F, 4.7703F, 5.8998F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r16 = frame.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(28, 498).addBox(-1.774F, -11.0F, 12.9235F, 3.548F, 4.0F, 2.153F, new CubeDeformation(0.0F))
		.texOffs(56, 498).addBox(-33.3221F, -11.0F, 12.9235F, 3.548F, 4.0F, 2.153F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.774F, -6.8215F, -9.4305F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r17 = frame.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(90, 457).addBox(7.452F, -3.8104F, 1.7685F, 8.548F, 7.8104F, 1.7315F, new CubeDeformation(0.0F))
		.texOffs(112, 449).addBox(-19.0961F, -3.8104F, 1.7685F, 8.548F, 7.8104F, 1.7315F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.548F, -10.1818F, -28.3983F, -0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r18 = frame.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 510).addBox(-1.774F, -11.0F, -15.0765F, 3.548F, 4.0F, 1.153F, new CubeDeformation(0.0F))
		.texOffs(30, 510).addBox(-33.3221F, -11.0F, -15.0765F, 3.548F, 4.0F, 1.153F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.774F, -6.8215F, -11.5695F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r19 = frame.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(30, 467).addBox(-1.774F, -12.0F, -16.0765F, 3.548F, 3.0F, 5.153F, new CubeDeformation(0.0F))
		.texOffs(30, 475).addBox(-33.3221F, -12.0F, -16.0765F, 3.548F, 3.0F, 5.153F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.774F, 4.7703F, -26.8998F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r20 = frame.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(104, 489).addBox(-1.774F, -10.0F, -15.0765F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F))
		.texOffs(14, 502).addBox(-33.3221F, -10.0F, -15.0765F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.774F, 0.8402F, -23.3004F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r21 = frame.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(104, 485).addBox(-1.774F, -10.0F, 11.9235F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F))
		.texOffs(14, 498).addBox(-33.3221F, -10.0F, 11.9235F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.774F, 0.8402F, -46.6996F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r22 = frame.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(90, 493).addBox(-1.774F, -11.0F, 11.9235F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F))
		.texOffs(90, 489).addBox(-1.774F, -10.0F, 11.9235F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F))
		.texOffs(118, 485).addBox(-33.3221F, -11.0F, 11.9235F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F))
		.texOffs(0, 506).addBox(-33.3221F, -10.0F, 11.9235F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.774F, 4.7703F, -43.1002F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r23 = frame.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(90, 485).addBox(-1.774F, -11.0F, -15.0765F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F))
		.texOffs(76, 493).addBox(-1.774F, -10.0F, -15.0765F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F))
		.texOffs(104, 493).addBox(-33.3221F, -11.0F, -15.0765F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F))
		.texOffs(0, 502).addBox(-33.3221F, -10.0F, -15.0765F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.774F, 4.7703F, -41.8998F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r24 = frame.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(118, 498).addBox(-1.774F, -11.0F, 13.9235F, 3.548F, 4.0F, 1.153F, new CubeDeformation(0.0F))
		.texOffs(20, 510).addBox(-33.3221F, -11.0F, 13.9235F, 3.548F, 4.0F, 1.153F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.774F, -6.8215F, -58.4305F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r25 = frame.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(104, 504).addBox(-1.774F, -11.0F, -15.0765F, 3.548F, 4.0F, 1.153F, new CubeDeformation(0.0F))
		.texOffs(10, 510).addBox(-33.3221F, -11.0F, -15.0765F, 3.548F, 4.0F, 1.153F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.774F, -6.8215F, -26.5695F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r26 = frame.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(76, 489).addBox(-1.774F, -10.0F, -15.0765F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F))
		.texOffs(0, 498).addBox(-33.3221F, -10.0F, -15.0765F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.774F, 0.8402F, -38.3004F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r27 = frame.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(20, 449).addBox(-6.774F, -12.0F, 11.9235F, 8.548F, 3.0F, 4.153F, new CubeDeformation(0.0F))
		.texOffs(20, 456).addBox(-33.3221F, -12.0F, 11.9235F, 8.548F, 3.0F, 4.153F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.774F, 4.7703F, -58.1002F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r28 = frame.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(76, 485).addBox(-1.774F, -10.0F, 11.9235F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F))
		.texOffs(118, 489).addBox(-33.3221F, -10.0F, 11.9235F, 3.548F, 1.0F, 3.153F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.774F, 0.8402F, -61.6996F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r29 = frame.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(66, 467).addBox(-6.774F, -11.0F, 13.9235F, 8.548F, 4.0F, 1.153F, new CubeDeformation(0.0F))
		.texOffs(66, 472).addBox(-33.3221F, -11.0F, 13.9235F, 8.548F, 4.0F, 1.153F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.774F, -6.8215F, -73.4305F, -0.3927F, 0.0F, 0.0F));

		PartDefinition wheels = body.addOrReplaceChild("wheels", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition w1 = wheels.addOrReplaceChild("w1", CubeListBuilder.create().texOffs(200, 433).addBox(-0.625F, -6.4918F, -1.2779F, 7.0F, 13.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(76, 532).addBox(-1.125F, -3.4918F, -0.4921F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(72, 532).addBox(-1.125F, 2.5082F, -0.4921F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(68, 532).addBox(-1.125F, -0.4918F, -3.4921F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(64, 532).addBox(-1.125F, -0.4918F, 2.5079F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(160, 346).addBox(-0.625F, -1.2847F, -6.485F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(90, 527).addBox(-1.625F, -1.2847F, -6.485F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(60, 527).addBox(-1.625F, -1.2847F, 4.515F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(120, 515).addBox(-1.625F, 4.5082F, -1.2779F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(96, 515).addBox(-1.625F, -6.4918F, -1.2779F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offset(-15.875F, -6.0082F, 13.985F));

		PartDefinition hexadecagon_r1 = w1.addOrReplaceChild("hexadecagon_r1", CubeListBuilder.create().texOffs(102, 527).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(72, 527).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(240, 346).addBox(0.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.125F, 0.0082F, 0.015F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r2 = w1.addOrReplaceChild("hexadecagon_r2", CubeListBuilder.create().texOffs(88, 515).addBox(-0.5F, -6.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(112, 515).addBox(-0.5F, 4.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(54, 527).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(84, 527).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(120, 346).addBox(0.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(180, 433).addBox(0.5F, -6.5F, -1.2929F, 7.0F, 13.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.125F, 0.0082F, 0.015F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r3 = w1.addOrReplaceChild("hexadecagon_r3", CubeListBuilder.create().texOffs(104, 515).addBox(-0.5F, -6.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(128, 515).addBox(-0.5F, 4.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(66, 527).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(96, 527).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(200, 346).addBox(0.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 449).addBox(0.5F, -6.5F, -1.2929F, 7.0F, 13.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.125F, 0.0082F, 0.015F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r4 = w1.addOrReplaceChild("hexadecagon_r4", CubeListBuilder.create().texOffs(48, 527).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(78, 527).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(80, 346).addBox(0.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.125F, 0.0082F, 0.015F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r30 = w1.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(48, 532).addBox(-1.0F, 4.65F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(52, 532).addBox(-1.0F, -1.35F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 532).addBox(-1.0F, 1.65F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(60, 532).addBox(-1.0F, 1.65F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.125F, -2.9918F, 0.0079F, -0.7854F, 0.0F, 0.0F));

		PartDefinition w2 = wheels.addOrReplaceChild("w2", CubeListBuilder.create().texOffs(140, 433).addBox(-6.375F, -6.4918F, -1.2779F, 7.0F, 13.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(44, 532).addBox(0.125F, -3.4918F, -0.4921F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 532).addBox(0.125F, 2.5082F, -0.4921F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 532).addBox(0.125F, -0.4918F, -3.4921F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 532).addBox(0.125F, -0.4918F, 2.5079F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(160, 330).addBox(-6.375F, -1.2847F, -6.485F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(36, 527).addBox(0.625F, -1.2847F, -6.485F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(6, 527).addBox(0.625F, -1.2847F, 4.515F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(72, 515).addBox(0.625F, 4.5082F, -1.2779F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(48, 515).addBox(0.625F, -6.4918F, -1.2779F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offset(15.875F, -6.0082F, 13.985F));

		PartDefinition hexadecagon_r5 = w2.addOrReplaceChild("hexadecagon_r5", CubeListBuilder.create().texOffs(198, 522).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(18, 527).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 346).addBox(-7.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.125F, 0.0082F, 0.015F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r6 = w2.addOrReplaceChild("hexadecagon_r6", CubeListBuilder.create().texOffs(40, 515).addBox(-0.5F, -6.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(64, 515).addBox(-0.5F, 4.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 527).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(30, 527).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(200, 330).addBox(-7.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(120, 433).addBox(-7.5F, -6.5F, -1.2929F, 7.0F, 13.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.125F, 0.0082F, 0.015F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r7 = w2.addOrReplaceChild("hexadecagon_r7", CubeListBuilder.create().texOffs(56, 515).addBox(-0.5F, -6.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(80, 515).addBox(-0.5F, 4.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(12, 527).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(42, 527).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(240, 330).addBox(-7.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(160, 433).addBox(-7.5F, -6.5F, -1.2929F, 7.0F, 13.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.125F, 0.0082F, 0.015F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r8 = w2.addOrReplaceChild("hexadecagon_r8", CubeListBuilder.create().texOffs(204, 522).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 527).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(40, 346).addBox(-7.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.125F, 0.0082F, 0.015F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r31 = w2.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(28, 532).addBox(0.0F, 4.65F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 532).addBox(0.0F, -1.35F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(36, 532).addBox(0.0F, 1.65F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(40, 532).addBox(0.0F, 1.65F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.125F, -2.9918F, 0.0079F, -0.7854F, 0.0F, 0.0F));

		PartDefinition w3 = wheels.addOrReplaceChild("w3", CubeListBuilder.create().texOffs(80, 433).addBox(-0.625F, -6.4918F, -1.2779F, 7.0F, 13.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(12, 532).addBox(-1.125F, -3.4918F, -0.4921F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(164, 527).addBox(-1.125F, 2.5082F, -0.4921F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(160, 529).addBox(-1.125F, -0.4918F, -3.4921F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(160, 527).addBox(-1.125F, -0.4918F, 2.5079F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(240, 314).addBox(-0.625F, -1.2847F, -6.485F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(186, 522).addBox(-1.625F, -1.2847F, -6.485F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(156, 522).addBox(-1.625F, -1.2847F, 4.515F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 515).addBox(-1.625F, 4.5082F, -1.2779F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 515).addBox(-1.625F, -6.4918F, -1.2779F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offset(-15.875F, -6.0082F, -35.015F));

		PartDefinition hexadecagon_r9 = w3.addOrReplaceChild("hexadecagon_r9", CubeListBuilder.create().texOffs(120, 330).addBox(0.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(144, 522).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(174, 522).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.125F, 0.0082F, 0.015F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r10 = w3.addOrReplaceChild("hexadecagon_r10", CubeListBuilder.create().texOffs(138, 522).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(168, 522).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(80, 330).addBox(0.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.125F, 0.0082F, 0.015F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r11 = w3.addOrReplaceChild("hexadecagon_r11", CubeListBuilder.create().texOffs(244, 510).addBox(-0.5F, -6.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(16, 515).addBox(-0.5F, 4.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(150, 522).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(180, 522).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 330).addBox(0.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(60, 433).addBox(0.5F, -6.5F, -1.2929F, 7.0F, 13.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.125F, 0.0082F, 0.015F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r12 = w3.addOrReplaceChild("hexadecagon_r12", CubeListBuilder.create().texOffs(8, 515).addBox(-0.5F, -6.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(32, 515).addBox(-0.5F, 4.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(162, 522).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(192, 522).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(40, 330).addBox(0.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(100, 433).addBox(0.5F, -6.5F, -1.2929F, 7.0F, 13.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.125F, 0.0082F, 0.015F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r32 = w3.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(164, 529).addBox(-1.0F, 4.65F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 532).addBox(-1.0F, -1.35F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 532).addBox(-1.0F, 1.65F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 532).addBox(-1.0F, 1.65F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.125F, -2.9918F, 0.0079F, -0.7854F, 0.0F, 0.0F));

		PartDefinition w4 = wheels.addOrReplaceChild("w4", CubeListBuilder.create().texOffs(20, 433).addBox(-0.625F, -6.4918F, -1.2779F, 7.0F, 13.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(156, 529).addBox(-1.125F, -3.4918F, -0.4921F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(148, 527).addBox(-1.125F, 2.5082F, -0.4921F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(144, 529).addBox(-1.125F, -0.4918F, -3.4921F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(144, 527).addBox(-1.125F, -0.4918F, 2.5079F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(80, 314).addBox(-0.625F, -1.2847F, -6.485F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(126, 522).addBox(-1.625F, -1.2847F, -6.485F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(96, 522).addBox(-1.625F, -1.2847F, 4.515F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(228, 510).addBox(-1.625F, 4.5082F, -1.2779F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(204, 510).addBox(-1.625F, -6.4918F, -1.2779F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offset(-15.875F, -6.0082F, -50.015F));

		PartDefinition hexadecagon_r13 = w4.addOrReplaceChild("hexadecagon_r13", CubeListBuilder.create().texOffs(40, 314).addBox(0.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(84, 522).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(114, 522).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.125F, 0.0082F, 0.015F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r14 = w4.addOrReplaceChild("hexadecagon_r14", CubeListBuilder.create().texOffs(78, 522).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(108, 522).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(200, 314).addBox(0.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.125F, 0.0082F, 0.015F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r15 = w4.addOrReplaceChild("hexadecagon_r15", CubeListBuilder.create().texOffs(196, 510).addBox(-0.5F, -6.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(220, 510).addBox(-0.5F, 4.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(90, 522).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(120, 522).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(120, 314).addBox(0.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 433).addBox(0.5F, -6.5F, -1.2929F, 7.0F, 13.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.125F, 0.0082F, 0.015F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r16 = w4.addOrReplaceChild("hexadecagon_r16", CubeListBuilder.create().texOffs(212, 510).addBox(-0.5F, -6.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(236, 510).addBox(-0.5F, 4.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(102, 522).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(132, 522).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(160, 314).addBox(0.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(40, 433).addBox(0.5F, -6.5F, -1.2929F, 7.0F, 13.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.125F, 0.0082F, 0.015F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r33 = w4.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(148, 529).addBox(-1.0F, 4.65F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(152, 527).addBox(-1.0F, -1.35F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(152, 529).addBox(-1.0F, 1.65F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(156, 527).addBox(-1.0F, 1.65F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.125F, -2.9918F, 0.0079F, -0.7854F, 0.0F, 0.0F));

		PartDefinition w5 = wheels.addOrReplaceChild("w5", CubeListBuilder.create().texOffs(144, 417).addBox(-6.375F, -6.4918F, -1.2779F, 7.0F, 13.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(140, 529).addBox(0.125F, -3.4918F, -0.4921F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(132, 527).addBox(0.125F, 2.5082F, -0.4921F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(128, 529).addBox(0.125F, -0.4918F, -3.4921F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(128, 527).addBox(0.125F, -0.4918F, 2.5079F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(160, 298).addBox(-6.375F, -1.2847F, -6.485F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(66, 522).addBox(0.625F, -1.2847F, -6.485F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 522).addBox(0.625F, -1.2847F, 4.515F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(180, 510).addBox(0.625F, 4.5082F, -1.2779F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(156, 510).addBox(0.625F, -6.4918F, -1.2779F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offset(15.875F, -6.0082F, -35.015F));

		PartDefinition hexadecagon_r17 = w5.addOrReplaceChild("hexadecagon_r17", CubeListBuilder.create().texOffs(120, 298).addBox(-7.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(24, 522).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(54, 522).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.125F, 0.0082F, 0.015F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r18 = w5.addOrReplaceChild("hexadecagon_r18", CubeListBuilder.create().texOffs(18, 522).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(48, 522).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 314).addBox(-7.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.125F, 0.0082F, 0.015F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r19 = w5.addOrReplaceChild("hexadecagon_r19", CubeListBuilder.create().texOffs(148, 510).addBox(-0.5F, -6.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(172, 510).addBox(-0.5F, 4.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(30, 522).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(60, 522).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(200, 298).addBox(-7.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(124, 417).addBox(-7.5F, -6.5F, -1.2929F, 7.0F, 13.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.125F, 0.0082F, 0.015F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r20 = w5.addOrReplaceChild("hexadecagon_r20", CubeListBuilder.create().texOffs(164, 510).addBox(-0.5F, -6.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(188, 510).addBox(-0.5F, 4.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(42, 522).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(72, 522).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(240, 298).addBox(-7.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(164, 417).addBox(-7.5F, -6.5F, -1.2929F, 7.0F, 13.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.125F, 0.0082F, 0.015F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r34 = w5.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(132, 529).addBox(0.0F, 4.65F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(136, 527).addBox(0.0F, -1.35F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(136, 529).addBox(0.0F, 1.65F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(140, 527).addBox(0.0F, 1.65F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.125F, -2.9918F, 0.0079F, -0.7854F, 0.0F, 0.0F));

		PartDefinition w6 = wheels.addOrReplaceChild("w6", CubeListBuilder.create().texOffs(84, 417).addBox(-6.375F, -6.4918F, -1.2779F, 7.0F, 13.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(124, 529).addBox(0.125F, -3.4918F, -0.4921F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(116, 527).addBox(0.125F, 2.5082F, -0.4921F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(112, 529).addBox(0.125F, -0.4918F, -3.4921F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(112, 527).addBox(0.125F, -0.4918F, 2.5079F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(232, 282).addBox(-6.375F, -1.2847F, -6.485F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(6, 522).addBox(0.625F, -1.2847F, -6.485F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(190, 515).addBox(0.625F, -1.2847F, 4.515F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(132, 510).addBox(0.625F, 4.5082F, -1.2779F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(108, 510).addBox(0.625F, -6.4918F, -1.2779F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offset(15.875F, -6.0082F, -50.015F));

		PartDefinition hexadecagon_r21 = w6.addOrReplaceChild("hexadecagon_r21", CubeListBuilder.create().texOffs(104, 417).addBox(-7.5F, -6.5F, -1.2929F, 7.0F, 13.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(116, 510).addBox(-0.5F, -6.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(140, 510).addBox(-0.5F, 4.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(196, 515).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 522).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(40, 298).addBox(-7.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.125F, 0.0082F, 0.015F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r22 = w6.addOrReplaceChild("hexadecagon_r22", CubeListBuilder.create().texOffs(192, 282).addBox(-7.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(178, 515).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(208, 515).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.125F, 0.0082F, 0.015F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r23 = w6.addOrReplaceChild("hexadecagon_r23", CubeListBuilder.create().texOffs(172, 515).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(202, 515).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(80, 298).addBox(-7.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.125F, 0.0082F, 0.015F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r24 = w6.addOrReplaceChild("hexadecagon_r24", CubeListBuilder.create().texOffs(100, 510).addBox(-0.5F, -6.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(124, 510).addBox(-0.5F, 4.5F, -1.2929F, 1.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(184, 515).addBox(-0.5F, -1.2929F, 4.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 522).addBox(-0.5F, -1.2929F, -6.5F, 1.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 298).addBox(-7.5F, -1.2929F, -6.5F, 7.0F, 2.5859F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(64, 417).addBox(-7.5F, -6.5F, -1.2929F, 7.0F, 13.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.125F, 0.0082F, 0.015F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r35 = w6.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(116, 529).addBox(0.0F, 4.65F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(120, 527).addBox(0.0F, -1.35F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(120, 529).addBox(0.0F, 1.65F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 527).addBox(0.0F, 1.65F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.125F, -2.9918F, 0.0079F, -0.7854F, 0.0F, 0.0F));

		PartDefinition radar = body.addOrReplaceChild("radar", CubeListBuilder.create().texOffs(32, 429).addBox(-7.0F, -8.6488F, 10.9694F, 14.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(60, 510).addBox(-1.0F, -2.6488F, -0.0306F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(168, 378).addBox(-7.0F, -8.0F, -4.0F, 14.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 384).addBox(-8.0F, -12.0F, -6.0F, 16.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 417).addBox(-6.0F, -4.0F, -2.0F, 12.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(168, 394).addBox(-6.0F, -20.0F, -2.0F, 12.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(132, 384).addBox(-7.0F, -16.0F, -4.0F, 14.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -46.0F, 15.0F));

		PartDefinition cube_r36 = radar.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(96, 378).addBox(-6.0F, -1.0F, -2.0F, 14.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.3986F, -11.0F, -1.4555F, 0.0F, -0.48F, 0.0F));

		PartDefinition cube_r37 = radar.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(136, 394).addBox(-6.0F, -1.0F, -2.0F, 12.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(136, 400).addBox(-6.0F, 7.0F, -2.0F, 12.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.5926F, -15.0F, 0.3483F, 0.0F, -0.4363F, 0.0F));

		PartDefinition cube_r38 = radar.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(96, 384).addBox(-7.0F, -1.0F, -2.0F, 14.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(132, 378).addBox(-7.0F, 7.0F, -2.0F, 14.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.4989F, -15.0F, 0.7709F, 0.0F, 0.4363F, 0.0F));

		PartDefinition cube_r39 = radar.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(32, 417).addBox(-6.0F, -1.0F, -2.0F, 11.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 423).addBox(-6.0F, 15.0F, -2.0F, 11.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.7779F, -19.0F, 2.1439F, 0.0F, -0.3927F, 0.0F));

		PartDefinition cube_r40 = radar.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(136, 406).addBox(-6.0F, -1.0F, -2.0F, 12.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 423).addBox(-6.0F, 15.0F, -2.0F, 12.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.7779F, -19.0F, 2.1439F, 0.0F, 0.3927F, 0.0F));

		PartDefinition cube_r41 = radar.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(0, 378).addBox(-9.0F, -1.0F, -2.0F, 16.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.2856F, -11.0F, -0.9937F, 0.0F, 0.48F, 0.0F));

		PartDefinition cube_r42 = radar.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(24, 485).addBox(-6.0F, -7.0F, -2.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -12.3547F, -2.015F, -0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r43 = radar.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(16, 485).addBox(-6.0F, -8.0F, -2.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -3.4552F, 5.5642F, -1.1345F, 0.0F, 0.0F));

		PartDefinition cube_r44 = radar.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(32, 485).addBox(-6.0F, -7.0F, -2.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -6.4575F, 0.2793F, 0.6109F, 0.0F, 0.0F));

		PartDefinition seats = body.addOrReplaceChild("seats", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition seat1 = seats.addOrReplaceChild("seat1", CubeListBuilder.create().texOffs(48, 282).addBox(-13.0F, -17.0F, 10.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(68, 378).addBox(-13.0F, -31.0F, 10.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition seat2 = seats.addOrReplaceChild("seat2", CubeListBuilder.create().texOffs(0, 282).addBox(1.0F, -17.0F, 10.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(40, 378).addBox(1.0F, -31.0F, 10.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition stearing = body.addOrReplaceChild("stearing", CubeListBuilder.create().texOffs(168, 515).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(104, 502).addBox(-3.0F, -0.5F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(104, 500).addBox(-3.0F, -4.0F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(104, 498).addBox(-3.0F, 3.0F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(164, 515).addBox(-4.0F, -3.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(160, 515).addBox(3.0F, -3.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, -27.0F, 26.0F));

		PartDefinition fuel = partdefinition.addOrReplaceChild("fuel", CubeListBuilder.create().texOffs(134, 192).addBox(15.5F, -9.864F, -10.5F, 9.0F, 3.7279F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(128, 219).addBox(18.136F, -12.5F, -10.5F, 3.7279F, 9.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(224, 219).addBox(-9.864F, -12.5F, -10.5F, 3.7279F, 9.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(250, 192).addBox(-12.5F, -9.864F, -10.5F, 9.0F, 3.7279F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 24.0F, -13.0F));

		PartDefinition cube_r45 = fuel.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(108, 527).addBox(4.75F, -1.736F, -19.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(20.0F, -6.5858F, 10.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r46 = fuel.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(108, 529).addBox(-5.75F, -1.736F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -6.5858F, 10.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r47 = fuel.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(0, 219).addBox(-4.5F, -1.864F, -18.5F, 9.0F, 3.7279F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(0, 248).addBox(-1.864F, -4.5F, -18.5F, 3.7279F, 9.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -8.0F, 8.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r48 = fuel.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(176, 219).addBox(-1.864F, -4.5F, -18.5F, 3.7279F, 9.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(192, 192).addBox(-4.5F, -1.864F, -18.5F, 9.0F, 3.7279F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(20.0F, -8.0F, 8.0F, 0.0F, 0.0F, -0.7854F));

		return LayerDefinition.create(meshdefinition, 1024, 1024);
	}

}
