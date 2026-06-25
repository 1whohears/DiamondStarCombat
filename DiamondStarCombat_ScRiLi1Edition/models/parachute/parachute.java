// Made with Blockbench 4.8.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class parachute<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "parachute"), "main");
	private final ModelPart main;

	public parachute(ModelPart root) {
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition seat = main.addOrReplaceChild("seat", CubeListBuilder.create().texOffs(0, 54).addBox(-6.0F, -2.0F, -4.0F, 12.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition chute = main.addOrReplaceChild("chute", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -38.0F, -8.0F, 24.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = chute.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, -8.0F, 24.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.0F, -37.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition cube_r2 = chute.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0F, -1.0F, -8.0F, 24.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0F, -37.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition rope = main.addOrReplaceChild("rope", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r3 = rope.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(40, 54).addBox(-1.0F, -42.0F, 0.0F, 1.0F, 42.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, -4.0F, 0.0873F, 0.0F, 0.7854F));

		PartDefinition cube_r4 = rope.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(40, 54).addBox(-1.0F, -42.0F, -1.0F, 1.0F, 42.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, 4.0F, -0.0873F, 0.0F, 0.7854F));

		PartDefinition cube_r5 = rope.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(40, 54).addBox(0.0F, -42.0F, -1.0F, 1.0F, 42.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 0.0F, 4.0F, -0.0873F, 0.0F, -0.7854F));

		PartDefinition cube_r6 = rope.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(40, 54).addBox(0.0F, -42.0F, 0.0F, 1.0F, 42.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 0.0F, -4.0F, 0.0873F, 0.0F, -0.7854F));

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