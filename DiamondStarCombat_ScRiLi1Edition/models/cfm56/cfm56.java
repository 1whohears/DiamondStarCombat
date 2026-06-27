// Made with Blockbench 4.7.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class cfm56<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "cfm56"), "main");
	private final ModelPart body;

	public cfm56(ModelPart root) {
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 11).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(22, 22).addBox(5.0F, 1.0F, 0.0F, 1.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, 11.0F, 0.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 22).addBox(-6.0F, 1.0F, 0.0F, 1.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(30, 11).addBox(-5.0F, 1.0F, -6.0F, 10.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(22, 42).addBox(-5.0F, 2.0F, -6.0F, 1.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(30, 0).addBox(-5.0F, 10.0F, -6.0F, 10.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(38, 36).addBox(4.0F, 2.0F, -6.0F, 1.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(36, 18).addBox(-4.0F, 2.0F, -10.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 42).addBox(-5.0F, 1.0F, 4.0F, 10.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}