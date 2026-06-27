// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class orange_tesla<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "orange_tesla"), "main");
	private final ModelPart main;

	public orange_tesla(ModelPart root) {
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition frame = main.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -10.0F, -32.0F, 32.0F, 2.0F, 64.0F, new CubeDeformation(0.0F))
		.texOffs(120, 70).addBox(-16.0F, -8.0F, 20.0F, 32.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(120, 66).addBox(-16.0F, -8.0F, -20.0F, 32.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 93).addBox(-16.0F, -20.0F, 13.0F, 32.0F, 10.0F, 19.0F, new CubeDeformation(0.0F))
		.texOffs(104, 111).addBox(-16.0F, -20.0F, -32.0F, 32.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-9.0F, -21.0F, 13.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(67, 111).addBox(-16.0F, -20.0F, -22.0F, 1.0F, 10.0F, 35.0F, new CubeDeformation(0.0F))
		.texOffs(83, 66).addBox(15.0F, -20.0F, -22.0F, 1.0F, 10.0F, 35.0F, new CubeDeformation(0.0F))
		.texOffs(0, 66).addBox(-16.0F, -37.0F, -19.0F, 32.0F, 1.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(54, 37).addBox(-16.0F, -36.0F, -5.0F, 1.0F, 16.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(54, 19).addBox(15.0F, -36.0F, -5.0F, 1.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

		PartDefinition cube_r1 = frame.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(48, 40).addBox(29.0F, -17.0F, -0.5F, 2.0F, 18.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(54, 0).addBox(-1.0F, -17.0F, -0.5F, 2.0F, 18.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, -20.0F, -22.5F, -0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r2 = frame.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(48, 0).addBox(29.0F, -18.0F, -0.5F, 2.0F, 19.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(48, 20).addBox(-1.0F, -18.0F, -0.5F, 2.0F, 19.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, -20.0F, 13.5F, 0.3927F, 0.0F, 0.0F));

		PartDefinition wheels = main.addOrReplaceChild("wheels", CubeListBuilder.create(), PartPose.offset(0.0F, 9.0F, 0.0F));

		PartDefinition fl = wheels.addOrReplaceChild("fl", CubeListBuilder.create().texOffs(68, 122).addBox(-5.0F, -6.0F, -6.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-16.0F, -7.0F, 21.0F));

		PartDefinition fr = wheels.addOrReplaceChild("fr", CubeListBuilder.create().texOffs(34, 122).addBox(0.0F, -6.0F, -6.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(16.0F, -7.0F, 21.0F));

		PartDefinition bl = wheels.addOrReplaceChild("bl", CubeListBuilder.create().texOffs(120, 74).addBox(-5.0F, -6.0F, -6.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-16.0F, -7.0F, -19.0F));

		PartDefinition br = wheels.addOrReplaceChild("br", CubeListBuilder.create().texOffs(0, 122).addBox(0.0F, -6.0F, -6.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(16.0F, -7.0F, -19.0F));

		PartDefinition seats = main.addOrReplaceChild("seats", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition s1 = seats.addOrReplaceChild("s1", CubeListBuilder.create().texOffs(0, 42).addBox(-14.0F, -3.0F, -3.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(128, 34).addBox(-14.0F, -18.0F, -3.0F, 12.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition s2 = seats.addOrReplaceChild("s2", CubeListBuilder.create().texOffs(0, 28).addBox(-14.0F, -3.0F, -3.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(128, 17).addBox(-14.0F, -18.0F, -3.0F, 12.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(15.0F, 0.0F, 0.0F));

		PartDefinition s3 = seats.addOrReplaceChild("s3", CubeListBuilder.create().texOffs(0, 14).addBox(-14.0F, -3.0F, -3.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(128, 0).addBox(-14.0F, -18.0F, -3.0F, 12.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -17.0F));

		PartDefinition s4 = seats.addOrReplaceChild("s4", CubeListBuilder.create().texOffs(0, 0).addBox(-14.0F, -3.0F, -3.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(90, 66).addBox(-14.0F, -18.0F, -3.0F, 12.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(15.0F, 0.0F, -17.0F));

		PartDefinition stearing = main.addOrReplaceChild("stearing", CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 56).addBox(-3.0F, -0.5F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 56).addBox(-3.0F, -4.0F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 56).addBox(-3.0F, 3.0F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 3).addBox(-4.0F, -3.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 3).addBox(3.0F, -3.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -11.0F, 13.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}