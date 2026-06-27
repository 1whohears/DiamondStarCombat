// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class small_roller<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "small_roller"), "main");
	private final ModelPart main;

	public small_roller(ModelPart root) {
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 55).addBox(-8.0F, -10.0F, -8.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(48, 59).addBox(-10.0F, -7.0F, 5.0F, 20.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(48, 55).addBox(-10.0F, -7.0F, -7.0F, 20.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition trackL = main.addOrReplaceChild("trackL", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition platesL = trackL.addOrReplaceChild("platesL", CubeListBuilder.create().texOffs(38, 28).addBox(-14.0F, -1.0F, -13.0F, 6.0F, 1.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(38, 1).addBox(-14.0F, -13.0F, -13.0F, 6.0F, 1.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(14, 75).addBox(-14.0F, -12.0F, 12.0F, 6.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 75).addBox(-14.0F, -12.0F, -13.0F, 6.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wheelsL = trackL.addOrReplaceChild("wheelsL", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wheel1 = wheelsL.addOrReplaceChild("wheel1", CubeListBuilder.create().texOffs(38, 28).addBox(-1.0F, -5.5F, -5.5F, 2.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-11.0F, -6.5F, 6.5F));

		PartDefinition wheel2 = wheelsL.addOrReplaceChild("wheel2", CubeListBuilder.create().texOffs(38, 0).addBox(-1.0F, -5.5F, -5.5F, 2.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-11.0F, -6.5F, -6.5F));

		PartDefinition trackR = main.addOrReplaceChild("trackR", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition platesR = trackR.addOrReplaceChild("platesR", CubeListBuilder.create().texOffs(0, 27).addBox(8.0F, -1.0F, -13.0F, 6.0F, 1.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(8.0F, -13.0F, -13.0F, 6.0F, 1.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(64, 63).addBox(8.0F, -12.0F, 12.0F, 6.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 54).addBox(8.0F, -12.0F, -13.0F, 6.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wheelsR = trackR.addOrReplaceChild("wheelsR", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wheel3 = wheelsR.addOrReplaceChild("wheel3", CubeListBuilder.create().texOffs(0, 27).addBox(-1.0F, -5.5F, -5.5F, 2.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(11.0F, -6.5F, 6.5F));

		PartDefinition wheel4 = wheelsR.addOrReplaceChild("wheel4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.5F, -5.5F, 2.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(11.0F, -6.5F, -6.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}