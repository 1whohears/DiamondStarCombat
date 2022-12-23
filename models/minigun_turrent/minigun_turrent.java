// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class minigun_turrent<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "minigun_turrent"), "main");
	private final ModelPart main;

	public minigun_turrent(ModelPart root) {
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition frame = main.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 12.0F, -8.0F, 12.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(-6.0F, -12.0F, -8.0F, 12.0F, 24.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 55).addBox(-1.0F, -2.0F, 6.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition gun = main.addOrReplaceChild("gun", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, 8.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(40, 52).addBox(-0.5F, -0.5F, 9.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(42, 39).addBox(-0.5F, -2.0F, 9.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(42, 26).addBox(-0.5F, 1.0F, 9.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(26, 42).addBox(1.0F, -0.5F, 9.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(42, 13).addBox(-2.0F, -0.5F, 9.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = gun.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(28, 16).addBox(-2.0F, -0.5F, 9.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.3562F));

		PartDefinition cube_r2 = gun.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(28, 29).addBox(-2.0F, -0.5F, 9.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.3562F));

		PartDefinition cube_r3 = gun.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(38, 0).addBox(-2.0F, -0.5F, 9.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r4 = gun.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 42).addBox(-2.0F, -0.5F, 9.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

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