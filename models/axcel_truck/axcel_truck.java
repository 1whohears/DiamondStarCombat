// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class axcel_truck<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "axcel_truck"), "main");
	private final ModelPart body;

	public axcel_truck(ModelPart root) {
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition frame = body.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -15.0F, -37.0F, 32.0F, 2.0F, 67.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-16.0F, -13.0F, 21.0F, 32.0F, 8.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-10.0F, -8.0F, 12.0F, 20.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-9.0F, -13.0F, -36.0F, 18.0F, 5.0F, 57.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-10.0F, -8.0F, -16.0F, 20.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-10.0F, -8.0F, -31.0F, 20.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wheels = body.addOrReplaceChild("wheels", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition w1 = wheels.addOrReplaceChild("w1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -6.0F, 6.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, -6.0F, 14.0F));

		PartDefinition w2 = wheels.addOrReplaceChild("w2", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -6.0F, 6.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(13.0F, -6.0F, 14.0F));

		PartDefinition w3 = wheels.addOrReplaceChild("w3", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -6.0F, 6.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, -6.0F, -14.0F));

		PartDefinition w4 = wheels.addOrReplaceChild("w4", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -6.0F, 6.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, -6.0F, -29.0F));

		PartDefinition w5 = wheels.addOrReplaceChild("w5", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -6.0F, 6.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(13.0F, -6.0F, -14.0F));

		PartDefinition w6 = wheels.addOrReplaceChild("w6", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -6.0F, 6.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(13.0F, -6.0F, -29.0F));

		PartDefinition radar = body.addOrReplaceChild("radar", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition seats = body.addOrReplaceChild("seats", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition seat1 = seats.addOrReplaceChild("seat1", CubeListBuilder.create().texOffs(0, 282).addBox(-14.0F, -17.0F, 10.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 263).addBox(-14.0F, -31.0F, 10.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition seat2 = seats.addOrReplaceChild("seat2", CubeListBuilder.create().texOffs(0, 282).addBox(2.0F, -17.0F, 10.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 263).addBox(2.0F, -31.0F, 10.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}