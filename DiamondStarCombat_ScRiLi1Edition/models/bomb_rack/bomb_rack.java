// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class bomb_rack<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "bomb_rack"), "main");
	private final ModelPart main;

	public bomb_rack(ModelPart root) {
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 2.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 16).addBox(-6.0F, 2.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 9).addBox(-6.0F, 8.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 6).addBox(-6.0F, 5.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 3).addBox(1.0F, 8.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 0).addBox(1.0F, 5.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(1.0F, 2.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.0F, 0.0F));

		PartDefinition m1 = main.addOrReplaceChild("m1", CubeListBuilder.create().texOffs(32, 0).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition m2 = main.addOrReplaceChild("m2", CubeListBuilder.create().texOffs(16, 30).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.0F, 0.0F));

		PartDefinition m3 = main.addOrReplaceChild("m3", CubeListBuilder.create().texOffs(0, 28).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 0.0F, 0.0F));

		PartDefinition m4 = main.addOrReplaceChild("m4", CubeListBuilder.create().texOffs(16, 44).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 0.0F, 0.0F));

		PartDefinition m5 = main.addOrReplaceChild("m5", CubeListBuilder.create().texOffs(0, 42).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

		PartDefinition m6 = main.addOrReplaceChild("m6", CubeListBuilder.create().texOffs(32, 28).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 3.0F, 0.0F));

		PartDefinition m7 = main.addOrReplaceChild("m7", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 3.0F, 0.0F));

		PartDefinition m8 = main.addOrReplaceChild("m8", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 3.0F, 0.0F));

		PartDefinition m9 = main.addOrReplaceChild("m9", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition m10 = main.addOrReplaceChild("m10", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 6.0F, 0.0F));

		PartDefinition m11 = main.addOrReplaceChild("m11", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 6.0F, 0.0F));

		PartDefinition m12 = main.addOrReplaceChild("m12", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 6.0F, 0.0F));

		PartDefinition m13 = main.addOrReplaceChild("m13", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 8.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}