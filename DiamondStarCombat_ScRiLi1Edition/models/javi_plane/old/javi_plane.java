// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class javi_plane<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "javi_plane"), "main");
	private final ModelPart fuselage;
	private final ModelPart seat;
	private final ModelPart gear;

	public javi_plane(ModelPart root) {
		this.fuselage = root.getChild("fuselage");
		this.seat = root.getChild("seat");
		this.gear = root.getChild("gear");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition fuselage = partdefinition.addOrReplaceChild("fuselage", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = fuselage.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -10.0F, -40.0F, 18.0F, 16.0F, 64.0F, new CubeDeformation(0.0F))
		.texOffs(100, 0).addBox(-9.0F, 5.0F, 24.0F, 18.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(0, 140).addBox(8.0F, -10.0F, 24.0F, 1.0F, 15.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(114, 114).addBox(-9.0F, -10.0F, 24.0F, 1.0F, 15.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(224, 141).addBox(-8.0F, -10.0F, 39.0F, 16.0F, 15.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 204).addBox(-9.0F, -10.0F, 56.0F, 18.0F, 16.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 80).addBox(-7.0F, -11.0F, -37.0F, 14.0F, 1.0F, 59.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition nose = fuselage.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(32, 0).addBox(-6.0F, -11.0F, 44.0F, 12.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 16).addBox(-5.0F, -10.0F, 48.0F, 10.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-4.0F, -9.0F, 52.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 45).addBox(-3.0F, -8.0F, 56.0F, 6.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 14.0F));

		PartDefinition wing = fuselage.addOrReplaceChild("wing", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left = wing.addOrReplaceChild("left", CubeListBuilder.create().texOffs(148, 114).addBox(-39.0F, -1.0F, -8.0F, 30.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(168, 17).addBox(-33.0F, -1.0F, 8.0F, 24.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(220, 201).addBox(-11.0F, -10.0F, -8.0F, 2.0F, 9.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(200, 234).addBox(-13.0F, -6.0F, -8.0F, 2.0F, 5.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(58, 226).addBox(-17.0F, -3.0F, -8.0F, 4.0F, 2.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(212, 46).addBox(-23.0F, -2.0F, -8.0F, 6.0F, 1.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(74, 201).addBox(-17.0F, 0.0F, -8.0F, 8.0F, 1.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = left.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(87, 97).addBox(-50.0F, -1.0F, -8.0F, 50.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-39.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

		PartDefinition right = wing.addOrReplaceChild("right", CubeListBuilder.create().texOffs(34, 145).addBox(9.0F, -1.0F, -8.0F, 30.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(164, 71).addBox(9.0F, -1.0F, 8.0F, 24.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(114, 215).addBox(9.0F, -10.0F, -8.0F, 2.0F, 9.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(233, 81).addBox(11.0F, -6.0F, -8.0F, 2.0F, 5.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(224, 158).addBox(13.0F, -3.0F, -8.0F, 4.0F, 2.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(212, 17).addBox(17.0F, -2.0F, -8.0F, 6.0F, 1.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(9.0F, 0.0F, -8.0F, 8.0F, 1.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r2 = right.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(87, 80).addBox(0.0F, -1.0F, -8.0F, 50.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(39.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

		PartDefinition tail = fuselage.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(164, 38).addBox(-6.0F, -10.0F, -64.0F, 12.0F, 8.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(128, 171).addBox(-30.0F, -7.0F, -64.0F, 24.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(166, 225).addBox(-31.0F, -28.0F, -64.0F, 1.0F, 32.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(168, 0).addBox(6.0F, -7.0F, -64.0F, 24.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 222).addBox(30.0F, -28.0F, -64.0F, 1.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition engine = fuselage.addOrReplaceChild("engine", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition eleft = engine.addOrReplaceChild("eleft", CubeListBuilder.create().texOffs(142, 215).addBox(-1.0F, -3.0F, -8.0F, 2.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(176, 198).addBox(-4.0F, -4.0F, -13.0F, 8.0F, 1.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(134, 188).addBox(-4.0F, -15.0F, -13.0F, 8.0F, 1.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(28, 204).addBox(5.0F, -13.0F, -13.0F, 1.0F, 8.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(203, 71).addBox(-6.0F, -13.0F, -13.0F, 1.0F, 8.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(0, 95).addBox(4.0F, -5.0F, 9.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(93, 91).addBox(4.0F, -14.0F, 9.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(93, 86).addBox(-5.0F, -5.0F, 9.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(93, 81).addBox(-5.0F, -14.0F, 9.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(34, 58).addBox(-1.0F, -10.0F, 9.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(224, 113).addBox(-5.0F, -14.0F, -9.0F, 10.0F, 10.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(87, 90).addBox(-5.0F, -14.0F, -13.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(14, 90).addBox(4.0F, -14.0F, -13.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(4, 90).addBox(-5.0F, -5.0F, -13.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(87, 85).addBox(4.0F, -5.0F, -13.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -10.0F, -24.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition eright = engine.addOrReplaceChild("eright", CubeListBuilder.create().texOffs(152, 33).addBox(-1.0F, -3.0F, -8.0F, 2.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(182, 171).addBox(-4.0F, -4.0F, -13.0F, 8.0F, 1.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(182, 131).addBox(-4.0F, -15.0F, -13.0F, 8.0F, 1.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(0, 187).addBox(-6.0F, -13.0F, -13.0F, 1.0F, 8.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(0, 80).addBox(5.0F, -13.0F, -13.0F, 1.0F, 8.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(87, 80).addBox(-5.0F, -5.0F, 9.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(14, 85).addBox(-5.0F, -14.0F, 9.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(4, 85).addBox(4.0F, -5.0F, 9.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(14, 80).addBox(4.0F, -14.0F, 9.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(22, 58).addBox(-1.0F, -10.0F, 9.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(68, 162).addBox(-5.0F, -14.0F, -9.0F, 10.0F, 10.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(4, 80).addBox(4.0F, -14.0F, -13.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(52, 59).addBox(-5.0F, -14.0F, -13.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(46, 58).addBox(4.0F, -5.0F, -13.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(54, 44).addBox(-5.0F, -5.0F, -13.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, -10.0F, -24.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition seat = partdefinition.addOrReplaceChild("seat", CubeListBuilder.create().texOffs(87, 114).addBox(-6.0F, 4.0F, 25.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(28, 80).addBox(-6.0F, -11.0F, 25.0F, 12.0F, 15.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 114).addBox(-6.0F, 4.0F, 41.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -11.0F, 41.0F, 12.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, -1.0F));

		PartDefinition stick = seat.addOrReplaceChild("stick", CubeListBuilder.create().texOffs(60, 16).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(40, 53).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 54.5F));

		PartDefinition window = seat.addOrReplaceChild("window", CubeListBuilder.create().texOffs(94, 161).addBox(7.0F, -8.0F, 0.0F, 1.0F, 8.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(148, 131).addBox(-8.0F, -8.0F, 0.0F, 1.0F, 8.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(28, 97).addBox(-7.0F, -8.0F, 0.0F, 14.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).addBox(-7.0F, -8.0F, 31.0F, 14.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(36, 171).addBox(6.0F, -11.0F, 1.0F, 1.0F, 3.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, -11.0F, 1.0F, 1.0F, 3.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(0, 58).addBox(-6.0F, -11.0F, 30.0F, 12.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 26).addBox(-6.0F, -11.0F, 1.0F, 12.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(100, 33).addBox(-6.0F, -12.0F, 2.0F, 12.0F, 1.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 25.0F));

		PartDefinition gear = partdefinition.addOrReplaceChild("gear", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition gfront = gear.addOrReplaceChild("gfront", CubeListBuilder.create().texOffs(20, 45).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(95, 97).addBox(-1.5F, 6.0F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(87, 97).addBox(0.5F, 6.0F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 42.5F));

		PartDefinition gleft = gear.addOrReplaceChild("gleft", CubeListBuilder.create().texOffs(0, 80).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(18, 95).addBox(-1.5F, 12.0F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(10, 95).addBox(0.5F, 12.0F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-31.5F, 0.0F, -3.5F));

		PartDefinition gright = gear.addOrReplaceChild("gright", CubeListBuilder.create().texOffs(60, 33).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(52, 33).addBox(-1.5F, 12.0F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(40, 33).addBox(0.5F, 12.0F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(30.5F, 0.0F, -3.5F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		fuselage.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		seat.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		gear.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}