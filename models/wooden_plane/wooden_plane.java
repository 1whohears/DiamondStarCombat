// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class wooden_plane<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "wooden_plane"), "main");
	private final ModelPart body;

	public wooden_plane(ModelPart root) {
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition fuselage = body.addOrReplaceChild("fuselage", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wing = fuselage.addOrReplaceChild("wing", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_wing = wing.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(91, 17).addBox(-41.0F, -1.0F, -7.0F, 32.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_wing = wing.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(91, 1).addBox(9.0F, -1.0F, -7.0F, 32.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frame = fuselage.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.0F, -40.0F, 12.0F, 12.0F, 31.0F, new CubeDeformation(0.0F))
		.texOffs(101, 35).addBox(-9.0F, 5.0F, -8.0F, 18.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(87, 77).addBox(8.0F, -6.0F, -8.0F, 1.0F, 11.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(49, 78).addBox(-9.0F, -6.0F, -8.0F, 1.0F, 11.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(7, 96).addBox(-9.0F, -6.0F, -9.0F, 18.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(7, 79).addBox(-9.0F, -6.0F, 8.0F, 18.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(61, 47).addBox(-6.0F, -6.0F, 9.0F, 12.0F, 12.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail = fuselage.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(4, 48).addBox(-22.0F, -1.0F, -40.0F, 16.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(5, 62).addBox(6.0F, -1.0F, -40.0F, 16.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(116, 58).addBox(-0.5F, -18.0F, -40.0F, 1.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition seat = body.addOrReplaceChild("seat", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition gear = body.addOrReplaceChild("gear", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition front_gear = gear.addOrReplaceChild("front_gear", CubeListBuilder.create().texOffs(0, 143).addBox(-6.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(58, 143).addBox(5.0F, 0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(34, 143).addBox(4.5F, 4.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(54, 143).addBox(-6.0F, 0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 143).addBox(-6.5F, 4.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.5F, 4.5F));

		PartDefinition back_gear = gear.addOrReplaceChild("back_gear", CubeListBuilder.create().texOffs(50, 143).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 145).addBox(-1.0F, 5.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -26.5F));

		PartDefinition propeller = body.addOrReplaceChild("propeller", CubeListBuilder.create().texOffs(42, 143).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(6, 115).addBox(-16.0F, -1.0F, 2.0F, 32.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(83, 116).addBox(-1.0F, -16.0F, 2.0F, 2.0F, 32.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 20.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}