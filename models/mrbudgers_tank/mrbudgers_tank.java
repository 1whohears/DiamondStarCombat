// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class mrbudger_tank<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "mrbudger_tank"), "main");
	private final ModelPart main;

	public mrbudger_tank(ModelPart root) {
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition frame = main.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0F, -22.0F, -40.0F, 48.0F, 14.0F, 80.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = main.addOrReplaceChild("head", CubeListBuilder.create().texOffs(308, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 16.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(308, 22).addBox(-16.0F, -16.0F, 10.0F, 32.0F, 16.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(308, 44).addBox(10.0F, -16.0F, -10.0F, 6.0F, 16.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(308, 80).addBox(-16.0F, -16.0F, -10.0F, 6.0F, 16.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(0, 248).addBox(-10.0F, -2.0F, -10.0F, 20.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -22.0F, 0.0F));

		PartDefinition barrel = head.addOrReplaceChild("barrel", CubeListBuilder.create().texOffs(176, 0).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 56.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 16.0F));

		PartDefinition trackL = main.addOrReplaceChild("trackL", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wheelsL = trackL.addOrReplaceChild("wheelsL", CubeListBuilder.create(), PartPose.offset(-24.0F, -8.0F, 0.0F));

		PartDefinition w1 = wheelsL.addOrReplaceChild("w1", CubeListBuilder.create().texOffs(32, 94).addBox(-4.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 30.0F));

		PartDefinition w2 = wheelsL.addOrReplaceChild("w2", CubeListBuilder.create().texOffs(40, 48).addBox(-4.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 15.0F));

		PartDefinition w3 = wheelsL.addOrReplaceChild("w3", CubeListBuilder.create().texOffs(0, 48).addBox(-4.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition w4 = wheelsL.addOrReplaceChild("w4", CubeListBuilder.create().texOffs(40, 24).addBox(-4.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -15.0F));

		PartDefinition w5 = wheelsL.addOrReplaceChild("w5", CubeListBuilder.create().texOffs(0, 94).addBox(-4.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -30.0F));

		PartDefinition platesL = trackL.addOrReplaceChild("platesL", CubeListBuilder.create().texOffs(90, 172).addBox(-30.0F, -2.0F, -37.0F, 8.0F, 2.0F, 74.0F, new CubeDeformation(0.0F))
		.texOffs(0, 170).addBox(-30.0F, -16.0F, -37.0F, 8.0F, 2.0F, 74.0F, new CubeDeformation(0.0F))
		.texOffs(20, 118).addBox(-30.0F, -15.0F, 36.0F, 8.0F, 14.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 118).addBox(-30.0F, -15.0F, -38.0F, 8.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition trackR = main.addOrReplaceChild("trackR", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wheelsR = trackR.addOrReplaceChild("wheelsR", CubeListBuilder.create(), PartPose.offset(24.0F, -8.0F, 0.0F));

		PartDefinition w6 = wheelsR.addOrReplaceChild("w6", CubeListBuilder.create().texOffs(32, 94).mirror().addBox(0.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 30.0F));

		PartDefinition w7 = wheelsR.addOrReplaceChild("w7", CubeListBuilder.create().texOffs(40, 48).mirror().addBox(0.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 15.0F));

		PartDefinition w8 = wheelsR.addOrReplaceChild("w8", CubeListBuilder.create().texOffs(0, 48).mirror().addBox(0.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition w9 = wheelsR.addOrReplaceChild("w9", CubeListBuilder.create().texOffs(40, 24).mirror().addBox(0.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, -15.0F));

		PartDefinition w10 = wheelsR.addOrReplaceChild("w10", CubeListBuilder.create().texOffs(0, 94).mirror().addBox(0.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, -30.0F));

		PartDefinition platesR = trackR.addOrReplaceChild("platesR", CubeListBuilder.create().texOffs(90, 172).mirror().addBox(22.0F, -2.0F, -37.0F, 8.0F, 2.0F, 74.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 170).mirror().addBox(22.0F, -16.0F, -37.0F, 8.0F, 2.0F, 74.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(20, 118).mirror().addBox(22.0F, -15.0F, 36.0F, 8.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 118).mirror().addBox(22.0F, -15.0F, -38.0F, 8.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}