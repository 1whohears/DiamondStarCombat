// Made with Blockbench 4.4.3
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class custom_model<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "custom_model"), "main");
	private final ModelPart body;

	public custom_model(ModelPart root) {
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition fuselage = body.addOrReplaceChild("fuselage", CubeListBuilder.create().texOffs(164, 120).addBox(-9.0F, 1.0F, 25.0F, 18.0F, 1.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(77, 34).addBox(8.0F, -12.0F, 25.0F, 1.0F, 13.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(0, 34).addBox(-9.0F, -12.0F, 25.0F, 1.0F, 13.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(0, 34).addBox(-9.0F, -12.0F, -16.0F, 18.0F, 14.0F, 41.0F, new CubeDeformation(0.0F))
		.texOffs(248, 126).addBox(-9.0F, -12.0F, 40.0F, 18.0F, 14.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(123, 0).addBox(-7.0F, -13.0F, -14.0F, 14.0F, 1.0F, 37.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition nose = fuselage.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(62, 112).addBox(-6.0F, -11.0F, 44.0F, 12.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(18, 89).addBox(-5.0F, -10.0F, 48.0F, 10.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(17, 34).addBox(-4.0F, -9.0F, 52.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(120, 100).addBox(-3.0F, -8.0F, 56.0F, 6.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(120, 90).addBox(-2.0F, -7.0F, 60.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(94, 34).addBox(-1.0F, -6.0F, 66.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wing = fuselage.addOrReplaceChild("wing", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 2.0F));

		PartDefinition left = wing.addOrReplaceChild("left", CubeListBuilder.create().texOffs(0, 17).addBox(-73.0F, -1.0F, -8.0F, 64.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(44, 167).addBox(-33.0F, -1.0F, 8.0F, 24.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(202, 173).addBox(-53.0F, -1.0F, 8.0F, 20.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(264, 265).addBox(-11.0F, -10.0F, -8.0F, 2.0F, 9.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(274, 59).addBox(-13.0F, -6.0F, -8.0F, 2.0F, 5.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(268, 33).addBox(-17.0F, -3.0F, -8.0F, 4.0F, 2.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(228, 264).addBox(-23.0F, -2.0F, -8.0F, 6.0F, 1.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(262, 144).addBox(-17.0F, 0.0F, -8.0F, 8.0F, 1.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right = wing.addOrReplaceChild("right", CubeListBuilder.create().texOffs(0, 0).addBox(9.0F, -1.0F, -8.0F, 64.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(134, 77).addBox(9.0F, -1.0F, 8.0F, 24.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(202, 168).addBox(33.0F, -1.0F, 8.0F, 20.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(176, 263).addBox(9.0F, -10.0F, -8.0F, 2.0F, 9.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(268, 228).addBox(11.0F, -6.0F, -8.0F, 2.0F, 5.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(110, 254).addBox(13.0F, -3.0F, -8.0F, 4.0F, 2.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(136, 221).addBox(17.0F, -2.0F, -8.0F, 6.0F, 1.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(262, 0).addBox(9.0F, 0.0F, -8.0F, 8.0F, 1.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail = fuselage.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(202, 180).addBox(-33.0F, -1.0F, -40.0F, 26.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 158).addBox(-17.0F, -1.0F, -24.0F, 10.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(202, 151).addBox(7.0F, -1.0F, -40.0F, 26.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(76, 154).addBox(7.0F, -1.0F, -24.0F, 10.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 89).addBox(-0.5F, -38.0F, -40.0F, 1.0F, 26.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.5F, -22.0F, -24.0F, 1.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition intake = fuselage.addOrReplaceChild("intake", CubeListBuilder.create().texOffs(188, 39).addBox(-4.0F, 7.0F, 0.0F, 8.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(202, 120).addBox(-4.0F, 6.0F, -16.0F, 8.0F, 1.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(134, 38).addBox(-4.0F, 5.0F, -24.0F, 8.0F, 1.0F, 38.0F, new CubeDeformation(0.0F))
		.texOffs(62, 90).addBox(-4.0F, 4.0F, -28.0F, 8.0F, 1.0F, 42.0F, new CubeDeformation(0.0F))
		.texOffs(0, 89).addBox(-4.0F, 3.0F, -32.0F, 8.0F, 1.0F, 46.0F, new CubeDeformation(0.0F))
		.texOffs(68, 39).addBox(-4.0F, 2.0F, -36.0F, 8.0F, 1.0F, 50.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right2 = intake.addOrReplaceChild("right2", CubeListBuilder.create().texOffs(234, 231).addBox(4.0F, 6.0F, 0.0F, 1.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(200, 230).addBox(5.0F, 5.0F, 0.0F, 1.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(228, 0).addBox(6.0F, 4.0F, 0.0F, 1.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(102, 221).addBox(7.0F, 3.0F, 0.0F, 1.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(68, 220).addBox(8.0F, 2.0F, 0.0F, 1.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(138, 252).addBox(4.0F, 5.0F, -16.0F, 1.0F, 1.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(32, 252).addBox(5.0F, 4.0F, -16.0F, 1.0F, 1.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(0, 251).addBox(6.0F, 3.0F, -16.0F, 1.0F, 1.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(248, 95).addBox(7.0F, 2.0F, -16.0F, 1.0F, 1.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(188, 0).addBox(4.0F, 4.0F, -24.0F, 1.0F, 1.0F, 38.0F, new CubeDeformation(0.0F))
		.texOffs(120, 182).addBox(5.0F, 3.0F, -24.0F, 1.0F, 1.0F, 38.0F, new CubeDeformation(0.0F))
		.texOffs(80, 181).addBox(6.0F, 2.0F, -24.0F, 1.0F, 1.0F, 38.0F, new CubeDeformation(0.0F))
		.texOffs(164, 77).addBox(4.0F, 3.0F, -28.0F, 1.0F, 1.0F, 42.0F, new CubeDeformation(0.0F))
		.texOffs(158, 137).addBox(5.0F, 2.0F, -28.0F, 1.0F, 1.0F, 42.0F, new CubeDeformation(0.0F))
		.texOffs(62, 133).addBox(4.0F, 2.0F, -32.0F, 1.0F, 1.0F, 46.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left2 = intake.addOrReplaceChild("left2", CubeListBuilder.create().texOffs(166, 219).addBox(-5.0F, 6.0F, 0.0F, 1.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(34, 219).addBox(-6.0F, 5.0F, 0.0F, 1.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(0, 218).addBox(-7.0F, 4.0F, 0.0F, 1.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(208, 197).addBox(-8.0F, 3.0F, 0.0F, 1.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(208, 72).addBox(-9.0F, 2.0F, 0.0F, 1.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(242, 197).addBox(-5.0F, 5.0F, -16.0F, 1.0F, 1.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(242, 64).addBox(-6.0F, 4.0F, -16.0F, 1.0F, 1.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(236, 33).addBox(-7.0F, 3.0F, -16.0F, 1.0F, 1.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(44, 136).addBox(-8.0F, 2.0F, -16.0F, 1.0F, 1.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(162, 180).addBox(-5.0F, 4.0F, -24.0F, 1.0F, 1.0F, 38.0F, new CubeDeformation(0.0F))
		.texOffs(40, 180).addBox(-6.0F, 3.0F, -24.0F, 1.0F, 1.0F, 38.0F, new CubeDeformation(0.0F))
		.texOffs(0, 179).addBox(-7.0F, 2.0F, -24.0F, 1.0F, 1.0F, 38.0F, new CubeDeformation(0.0F))
		.texOffs(114, 138).addBox(-5.0F, 3.0F, -28.0F, 1.0F, 1.0F, 42.0F, new CubeDeformation(0.0F))
		.texOffs(0, 136).addBox(-6.0F, 2.0F, -28.0F, 1.0F, 1.0F, 42.0F, new CubeDeformation(0.0F))
		.texOffs(116, 90).addBox(-5.0F, 2.0F, -32.0F, 1.0F, 1.0F, 46.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition nozzel = fuselage.addOrReplaceChild("nozzel", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition out = nozzel.addOrReplaceChild("out", CubeListBuilder.create().texOffs(76, 145).addBox(-4.0F, 1.0F, -44.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(76, 136).addBox(-4.0F, -10.0F, -44.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(84, 90).addBox(5.0F, -8.0F, -44.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(62, 90).addBox(-6.0F, -8.0F, -44.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(142, 99).addBox(4.0F, 0.0F, -44.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(142, 90).addBox(4.0F, -9.0F, -44.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(140, 121).addBox(-5.0F, 0.0F, -44.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(136, 137).addBox(-5.0F, -9.0F, -44.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition base = nozzel.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 282).addBox(-1.0F, -12.0F, -36.0F, 2.0F, 1.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(16, 136).addBox(-1.0F, -11.0F, -44.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(26, 123).addBox(-1.0F, 2.0F, -44.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(74, 263).addBox(-4.0F, -11.0F, -36.0F, 8.0F, 13.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(274, 177).addBox(-7.0F, -8.0F, -36.0F, 3.0F, 8.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(110, 280).addBox(-6.0F, -10.0F, -36.0F, 2.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(274, 205).addBox(-7.0F, 0.0F, -36.0F, 3.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(120, 90).addBox(-8.0F, -5.0F, -36.0F, 1.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(110, 137).addBox(-7.0F, -5.0F, -44.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(110, 137).addBox(4.0F, -8.0F, -36.0F, 3.0F, 8.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(280, 95).addBox(4.0F, -10.0F, -36.0F, 2.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(204, 263).addBox(4.0F, 0.0F, -36.0F, 3.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(62, 90).addBox(7.0F, -5.0F, -36.0F, 1.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(86, 120).addBox(6.0F, -5.0F, -44.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition seat = body.addOrReplaceChild("seat", CubeListBuilder.create().texOffs(24, 283).addBox(-6.0F, 0.0F, 25.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(120, 112).addBox(-6.0F, -15.0F, 25.0F, 12.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition stick = seat.addOrReplaceChild("stick", CubeListBuilder.create().texOffs(0, 17).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 38.5F));

		PartDefinition window = seat.addOrReplaceChild("window", CubeListBuilder.create().texOffs(77, 62).addBox(-8.0F, -9.0F, 15.0F, 16.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 62).addBox(-8.0F, -9.0F, 0.0F, 16.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 136).addBox(7.0F, -9.0F, 1.0F, 1.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(134, 38).addBox(-8.0F, -9.0F, 1.0F, 1.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(77, 71).addBox(-7.0F, -12.0F, 14.0F, 14.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 71).addBox(-7.0F, -12.0F, 1.0F, 14.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(44, 136).addBox(6.0F, -12.0F, 2.0F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(134, 60).addBox(-7.0F, -12.0F, 2.0F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(208, 105).addBox(-6.0F, -13.0F, 2.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 24.0F));

		PartDefinition gear = body.addOrReplaceChild("gear", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition gfront = gear.addOrReplaceChild("gfront", CubeListBuilder.create().texOffs(0, 334).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(5, 334).addBox(-1.5F, 6.0F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(14, 334).addBox(0.5F, 6.0F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 42.5F));

		PartDefinition gleft = gear.addOrReplaceChild("gleft", CubeListBuilder.create().texOffs(0, 311).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(5, 311).addBox(-1.5F, 7.0F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(14, 311).addBox(0.5F, 7.0F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.5F, 1.0F, 1.5F));

		PartDefinition gright = gear.addOrReplaceChild("gright", CubeListBuilder.create().texOffs(0, 323).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(5, 323).addBox(-1.5F, 7.0F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(14, 323).addBox(0.5F, 7.0F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(10.5F, 1.0F, 1.5F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}