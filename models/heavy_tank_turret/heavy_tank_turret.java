// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class heavy_tank_turret<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "heavy_tank_turret"), "main");
	private final ModelPart main;

	public heavy_tank_turret(ModelPart root) {
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(64, 22).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 16.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(64, 0).addBox(-16.0F, -16.0F, 10.0F, 32.0F, 16.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(60, 62).addBox(10.0F, -16.0F, -10.0F, 6.0F, 16.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-16.0F, -16.0F, -10.0F, 6.0F, 16.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(0, 60).addBox(-10.0F, -2.0F, -10.0F, 20.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition barrel = main.addOrReplaceChild("barrel", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 56.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 16.0F));

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