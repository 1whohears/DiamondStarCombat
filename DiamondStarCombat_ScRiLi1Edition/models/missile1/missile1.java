// Made with Blockbench 4.4.3
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class missile1<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "missile1"), "main");
	private final ModelPart bone;

	public missile1(ModelPart root) {
		this.bone = root.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 33).addBox(1.0F, -1.0F, -7.0F, 1.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(20, 0).addBox(-2.0F, -1.0F, -7.0F, 1.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(18, 19).addBox(-1.0F, -2.0F, -7.0F, 2.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 18).addBox(-1.0F, 1.0F, -7.0F, 2.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(0.0F, -4.0F, -7.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(0.0F, 2.0F, -7.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, 0.0F, -7.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(2.0F, 0.0F, -7.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(2.0F, 0.0F, 2.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, 0.0F, 2.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(0.0F, -4.0F, 2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(0.0F, 2.0F, 2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(17, 38).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}