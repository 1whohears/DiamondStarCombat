// Made with Blockbench 4.8.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class gruetz_bunker_buster<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "gruetz_bunker_buster"), "main");
	private final ModelPart main;

	public gruetz_bunker_buster(ModelPart root) {
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition drill = main.addOrReplaceChild("drill", CubeListBuilder.create().texOffs(120, 65).addBox(-1.0F, 12.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(104, 77).addBox(-3.0F, 8.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(88, 92).addBox(-5.0F, 4.0F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(72, 110).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 4.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -16.0F, 0.0F));

		PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -37.0F, -8.0F, 24.0F, 20.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 56).addBox(-8.0F, -37.0F, 8.0F, 16.0F, 20.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 56).addBox(-8.0F, -37.0F, -12.0F, 16.0F, 20.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 36).addBox(-8.0F, -41.0F, -8.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 108).addBox(-1.0F, -49.0F, 8.0F, 2.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 108).addBox(-1.0F, -49.0F, -16.0F, 2.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(22, 114).addBox(-16.0F, -49.0F, -1.0F, 8.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(22, 114).addBox(8.0F, -49.0F, -1.0F, 8.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

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