// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class nathan_boat<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "nathan_boat"), "main");
	private final ModelPart main;

	public nathan_boat(ModelPart root) {
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition frame = main.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(176, 0).addBox(-8.0F, -4.0F, -24.0F, 16.0F, 4.0F, 64.0F, new CubeDeformation(0.0F))
		.texOffs(0, 84).addBox(-16.0F, -7.0F, -24.0F, 32.0F, 3.0F, 72.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-24.0F, -11.0F, -24.0F, 48.0F, 4.0F, 80.0F, new CubeDeformation(0.0F))
		.texOffs(88, 180).addBox(-24.0F, -25.0F, 54.0F, 48.0F, 16.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 159).addBox(22.0F, -25.0F, -24.0F, 4.0F, 16.0F, 80.0F, new CubeDeformation(0.0F))
		.texOffs(128, 84).addBox(-26.0F, -25.0F, -24.0F, 4.0F, 16.0F, 80.0F, new CubeDeformation(0.0F))
		.texOffs(0, 104).addBox(8.0F, -25.0F, -26.0F, 16.0F, 16.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 84).addBox(-24.0F, -25.0F, -26.0F, 16.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bench1 = main.addOrReplaceChild("bench1", CubeListBuilder.create().texOffs(0, 24).addBox(-16.0F, -20.0F, 32.0F, 32.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(56, 117).addBox(-15.0F, -16.0F, 33.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(40, 117).addBox(13.0F, -16.0F, 33.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

		PartDefinition bench2 = main.addOrReplaceChild("bench2", CubeListBuilder.create().texOffs(0, 12).addBox(-16.0F, -20.0F, 32.0F, 32.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(56, 106).addBox(-15.0F, -16.0F, 33.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(40, 106).addBox(13.0F, -16.0F, 33.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -16.0F));

		PartDefinition bench3 = main.addOrReplaceChild("bench3", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -20.0F, 32.0F, 32.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(56, 95).addBox(-15.0F, -16.0F, 33.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(40, 95).addBox(13.0F, -16.0F, 33.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -35.0F));

		PartDefinition bench4 = main.addOrReplaceChild("bench4", CubeListBuilder.create().texOffs(40, 60).addBox(4.0F, -20.0F, 32.0F, 12.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(56, 84).addBox(5.0F, -16.0F, 33.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(40, 84).addBox(13.0F, -16.0F, 33.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-24.0F, 0.0F, -54.0F));

		PartDefinition engine = main.addOrReplaceChild("engine", CubeListBuilder.create().texOffs(0, 36).addBox(-6.0F, -17.0F, -18.0F, 12.0F, 18.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(16, 66).addBox(-2.0F, -11.0F, -6.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 124).addBox(-1.0F, -13.0F, -1.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 66).addBox(-1.0F, -15.0F, -1.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(48, 36).addBox(-4.0F, 1.0F, -16.0F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, -20.0F));

		PartDefinition propellor = engine.addOrReplaceChild("propellor", CubeListBuilder.create().texOffs(0, 36).addBox(-1.0F, -1.0F, -4.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, -16.0F));

		PartDefinition cube_r1 = propellor.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.0F, -0.3491F, -1.5708F));

		PartDefinition cube_r2 = propellor.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -5.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.0F, -0.3491F, -3.1416F));

		PartDefinition cube_r3 = propellor.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 24).addBox(-1.0F, -5.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.0F, -0.3491F, 1.5708F));

		PartDefinition cube_r4 = propellor.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(36, 36).addBox(-1.0F, -5.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.0F, -0.3491F, 0.0F));

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