// Made with Blockbench 4.7.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class sam_launcher<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "sam_launcher"), "main");
	private final ModelPart body;

	public sam_launcher(ModelPart root) {
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition frame = body.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -2.0F, -8.0F, 12.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 38).addBox(-6.0F, -26.0F, -8.0F, 12.0F, 24.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(54, 79).addBox(6.0F, -32.0F, -1.0F, 2.0F, 32.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(46, 79).addBox(-8.0F, -32.0F, -1.0F, 2.0F, 32.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(32, 18).addBox(-1.0F, -32.0F, -8.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = frame.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -32.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition cube_r2 = frame.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(38, 0).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -32.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r3 = frame.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(28, 40).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -32.0F, -7.0F, -0.5236F, 0.0F, 0.0F));

		PartDefinition gun = body.addOrReplaceChild("gun", CubeListBuilder.create().texOffs(0, 16).addBox(-5.0F, -12.0F, -5.0F, 10.0F, 12.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(40, 18).addBox(-8.0F, -8.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(46, 40).addBox(-1.0F, -15.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(44, 22).addBox(5.0F, -8.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -41.0F, 0.0F));

		PartDefinition m1 = gun.addOrReplaceChild("m1", CubeListBuilder.create().texOffs(16, 73).addBox(1.0F, -1.0F, -7.0F, 1.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 71).addBox(-2.0F, -1.0F, -7.0F, 1.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(62, 15).addBox(-1.0F, -2.0F, -7.0F, 2.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(14, 56).addBox(-1.0F, 1.0F, -7.0F, 2.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(44, 29).addBox(0.0F, -4.0F, -7.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(44, 26).addBox(0.0F, 2.0F, -7.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(26, 16).addBox(-4.0F, 0.0F, -7.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 20).addBox(2.0F, 0.0F, -7.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(34, 42).addBox(2.0F, 0.0F, 2.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(42, 0).addBox(-4.0F, 0.0F, 2.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(44, 33).addBox(0.0F, -4.0F, 2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 42).addBox(0.0F, 2.0F, 2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(28, 40).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -17.0F, 0.0F));

		PartDefinition m2 = gun.addOrReplaceChild("m2", CubeListBuilder.create().texOffs(64, 46).addBox(1.0F, -1.0F, -7.0F, 1.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(64, 30).addBox(-2.0F, -1.0F, -7.0F, 1.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(56, 0).addBox(-1.0F, -2.0F, -7.0F, 2.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(46, 48).addBox(-1.0F, 1.0F, -7.0F, 2.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(44, 24).addBox(0.0F, -4.0F, -7.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(44, 22).addBox(0.0F, 2.0F, -7.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(-4.0F, 0.0F, -7.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(4, 8).addBox(2.0F, 0.0F, -7.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(38, 22).addBox(2.0F, 0.0F, 2.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 18).addBox(-4.0F, 0.0F, 2.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 20).addBox(0.0F, -4.0F, 2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 18).addBox(0.0F, 2.0F, 2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(38, 2).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, -7.0F, 0.0F));

		PartDefinition m3 = gun.addOrReplaceChild("m3", CubeListBuilder.create().texOffs(62, 63).addBox(1.0F, -1.0F, -7.0F, 1.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(32, 63).addBox(-2.0F, -1.0F, -7.0F, 1.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(46, 33).addBox(-1.0F, -2.0F, -7.0F, 2.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(44, 18).addBox(-1.0F, 1.0F, -7.0F, 2.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(34, 36).addBox(0.0F, -4.0F, -7.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 20).addBox(0.0F, 2.0F, -7.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(4, 4).addBox(-4.0F, 0.0F, -7.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(4, 0).addBox(2.0F, 0.0F, -7.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 16).addBox(2.0F, 0.0F, 2.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(6, 12).addBox(-4.0F, 0.0F, 2.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(0.0F, -4.0F, 2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(0.0F, 2.0F, 2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(26, 24).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(10.0F, -7.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}