// Made with Blockbench 4.4.3
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class javi_plane<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "javi_plane"), "main");
	private final ModelPart fuselage;
	private final ModelPart gear;
	private final ModelPart seat;

	public javi_plane(ModelPart root) {
		this.fuselage = root.getChild("fuselage");
		this.gear = root.getChild("gear");
		this.seat = root.getChild("seat");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition fuselage = partdefinition.addOrReplaceChild("fuselage", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = fuselage.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -10.0F, -40.0F, 18.0F, 16.0F, 64.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-9.0F, 5.0F, 24.0F, 18.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(8.0F, -10.0F, 24.0F, 1.0F, 15.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-9.0F, -10.0F, 24.0F, 1.0F, 15.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -10.0F, 39.0F, 16.0F, 15.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-9.0F, -10.0F, 56.0F, 18.0F, 16.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, -11.0F, -37.0F, 14.0F, 1.0F, 59.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition nose = fuselage.addOrReplaceChild("nose", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wing = fuselage.addOrReplaceChild("wing", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left = wing.addOrReplaceChild("left", CubeListBuilder.create().texOffs(0, 0).addBox(-39.0F, -1.0F, -8.0F, 30.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(44, 167).addBox(-33.0F, -1.0F, 8.0F, 24.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(264, 265).addBox(-11.0F, -10.0F, -8.0F, 2.0F, 9.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(274, 59).addBox(-13.0F, -6.0F, -8.0F, 2.0F, 5.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(268, 33).addBox(-17.0F, -3.0F, -8.0F, 4.0F, 2.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(228, 264).addBox(-23.0F, -2.0F, -8.0F, 6.0F, 1.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(262, 144).addBox(-17.0F, 0.0F, -8.0F, 8.0F, 1.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = left.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-50.0F, -1.0F, -8.0F, 50.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-39.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

		PartDefinition right = wing.addOrReplaceChild("right", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(9.0F, -1.0F, -8.0F, 30.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(44, 167).mirror().addBox(9.0F, -1.0F, 8.0F, 24.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(264, 265).mirror().addBox(9.0F, -10.0F, -8.0F, 2.0F, 9.0F, 24.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(274, 59).mirror().addBox(11.0F, -6.0F, -8.0F, 2.0F, 5.0F, 24.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(268, 33).mirror().addBox(13.0F, -3.0F, -8.0F, 4.0F, 2.0F, 24.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(228, 264).mirror().addBox(17.0F, -2.0F, -8.0F, 6.0F, 1.0F, 24.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(262, 144).mirror().addBox(9.0F, 0.0F, -8.0F, 8.0F, 1.0F, 24.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r2 = right.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -1.0F, -8.0F, 50.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(39.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

		PartDefinition tail = fuselage.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -10.0F, -64.0F, 12.0F, 8.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-30.0F, -7.0F, -64.0F, 24.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-31.0F, -28.0F, -64.0F, 1.0F, 32.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(6.0F, -7.0F, -64.0F, 24.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(30.0F, -28.0F, -64.0F, 1.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition engine = fuselage.addOrReplaceChild("engine", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition eleft = engine.addOrReplaceChild("eleft", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.0F, -8.0F, 2.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -4.0F, -13.0F, 8.0F, 1.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -15.0F, -13.0F, 8.0F, 1.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(5.0F, -13.0F, -13.0F, 1.0F, 8.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -13.0F, -13.0F, 1.0F, 8.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(4.0F, -5.0F, 9.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(4.0F, -14.0F, 9.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, -5.0F, 9.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, -14.0F, 9.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -10.0F, 9.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, -14.0F, -9.0F, 10.0F, 10.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, -14.0F, -13.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(4.0F, -14.0F, -13.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, -5.0F, -13.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(4.0F, -5.0F, -13.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -10.0F, -24.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition eright = engine.addOrReplaceChild("eright", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -3.0F, -8.0F, 2.0F, 4.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(-4.0F, -4.0F, -13.0F, 8.0F, 1.0F, 26.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(-4.0F, -15.0F, -13.0F, 8.0F, 1.0F, 26.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(-6.0F, -13.0F, -13.0F, 1.0F, 8.0F, 26.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(5.0F, -13.0F, -13.0F, 1.0F, 8.0F, 26.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(-5.0F, -5.0F, 9.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(-5.0F, -14.0F, 9.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(4.0F, -5.0F, 9.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(4.0F, -14.0F, 9.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(-1.0F, -10.0F, 9.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(-5.0F, -14.0F, -9.0F, 10.0F, 10.0F, 18.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(4.0F, -14.0F, -13.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(-5.0F, -14.0F, -13.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(4.0F, -5.0F, -13.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(-5.0F, -5.0F, -13.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(9.0F, -10.0F, -24.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition gear = partdefinition.addOrReplaceChild("gear", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition seat = partdefinition.addOrReplaceChild("seat", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		fuselage.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		gear.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		seat.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}