// Made with Blockbench 4.4.3
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class basic_plane<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "basic_plane"), "main");
	private final ModelPart body;

	public basic_plane(ModelPart root) {
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 26).addBox(-7.0F, -7.0F, -22.0F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition seat = body.addOrReplaceChild("seat", CubeListBuilder.create().texOffs(58, 12).addBox(-7.0F, 6.0F, -8.0F, 14.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(30, 62).addBox(6.0F, -7.0F, -8.0F, 1.0F, 13.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 54).addBox(-7.0F, -7.0F, -8.0F, 1.0F, 13.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(72, 30).addBox(-6.0F, -7.0F, 5.0F, 12.0F, 13.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wingleft = body.addOrReplaceChild("wingleft", CubeListBuilder.create().texOffs(0, 13).addBox(7.0F, -1.0F, -21.0F, 24.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wingright = body.addOrReplaceChild("wingright", CubeListBuilder.create().texOffs(0, 0).addBox(-31.0F, -1.0F, -21.0F, 24.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(40, 38).addBox(-4.0F, -4.0F, 6.0F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition up = tail.addOrReplaceChild("up", CubeListBuilder.create().texOffs(60, 71).addBox(-1.0F, -20.0F, 14.0F, 2.0F, 16.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left = tail.addOrReplaceChild("left", CubeListBuilder.create().texOffs(46, 62).addBox(-16.0F, 0.0F, 14.0F, 12.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right = tail.addOrReplaceChild("right", CubeListBuilder.create().texOffs(60, 0).addBox(4.0F, 0.0F, 14.0F, 12.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition propeller = body.addOrReplaceChild("propeller", CubeListBuilder.create().texOffs(0, 26).addBox(-1.0F, -1.0F, -24.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(42, 27).addBox(-12.0F, -1.0F, -25.0F, 24.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition gearback = body.addOrReplaceChild("gearback", CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(-1.0F, 5.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 17.0F));

		PartDefinition gearleft = body.addOrReplaceChild("gearleft", CubeListBuilder.create().texOffs(4, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).addBox(-1.0F, 9.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(25.0F, 0.0F, -16.0F));

		PartDefinition gearright = body.addOrReplaceChild("gearright", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 13).addBox(-1.0F, 9.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-25.0F, 0.0F, -16.0F));

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