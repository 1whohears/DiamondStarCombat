// Made with Blockbench 4.4.3
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class f16<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "f16"), "main");
	private final ModelPart body;

	public f16(ModelPart root) {
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition nose = body.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(0, 1).addBox(-0.5F, -0.5F, 22.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 24.0F));

		PartDefinition nbs1 = nose.addOrReplaceChild("nbs1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = nbs1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 8).addBox(0.0F, 0.1755F, 17.9962F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 12.6015F, 6.4798F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r2 = nbs1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(57, 59).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 10.6316F, 5.191F, 0.5236F, 0.0F, 0.0F));

		PartDefinition cube_r3 = nbs1.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(59, 59).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 9.4641F, 4.6004F, 0.4363F, 0.0F, 0.0F));

		PartDefinition cube_r4 = nbs1.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(44, 54).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(46, 54).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 8.4914F, 4.214F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r5 = nbs1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(48, 54).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 7.8215F, 4.0187F, 0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r6 = nbs1.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(50, 54).addBox(0.0F, 0.1755F, -0.0038F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 7.4779F, 3.9581F, 0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r7 = nbs1.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(54, 50).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition nbs2 = nose.addOrReplaceChild("nbs2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition cube_r8 = nbs2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 6).addBox(0.0F, 0.1755F, 17.9962F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 12.6015F, 6.4798F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r9 = nbs2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(59, 53).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 10.6316F, 5.191F, 0.5236F, 0.0F, 0.0F));

		PartDefinition cube_r10 = nbs2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(59, 56).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 9.4641F, 4.6004F, 0.4363F, 0.0F, 0.0F));

		PartDefinition cube_r11 = nbs2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(34, 54).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(36, 54).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 8.4914F, 4.214F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r12 = nbs2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(38, 54).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 7.8215F, 4.0187F, 0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r13 = nbs2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(40, 54).addBox(0.0F, 0.1755F, -0.0038F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 7.4779F, 3.9581F, 0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r14 = nbs2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(42, 54).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition nbs3 = nose.addOrReplaceChild("nbs3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition cube_r15 = nbs3.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 3).addBox(0.0F, 0.1755F, 17.9962F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 12.6015F, 6.4798F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r16 = nbs3.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(50, 58).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 10.6316F, 5.191F, 0.5236F, 0.0F, 0.0F));

		PartDefinition cube_r17 = nbs3.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(59, 50).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 9.4641F, 4.6004F, 0.4363F, 0.0F, 0.0F));

		PartDefinition cube_r18 = nbs3.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(24, 54).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(26, 54).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 8.4914F, 4.214F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r19 = nbs3.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(28, 54).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 7.8215F, 4.0187F, 0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r20 = nbs3.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(30, 54).addBox(0.0F, 0.1755F, -0.0038F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 7.4779F, 3.9581F, 0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r21 = nbs3.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(32, 54).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition nbs4 = nose.addOrReplaceChild("nbs4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition cube_r22 = nbs4.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(0, 1).addBox(0.0F, 0.1755F, 17.9962F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 12.6015F, 6.4798F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r23 = nbs4.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(46, 58).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 10.6316F, 5.191F, 0.5236F, 0.0F, 0.0F));

		PartDefinition cube_r24 = nbs4.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(48, 58).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 9.4641F, 4.6004F, 0.4363F, 0.0F, 0.0F));

		PartDefinition cube_r25 = nbs4.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(14, 54).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 54).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 8.4914F, 4.214F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r26 = nbs4.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(18, 54).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 7.8215F, 4.0187F, 0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r27 = nbs4.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(20, 54).addBox(0.0F, 0.1755F, -0.0038F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 7.4779F, 3.9581F, 0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r28 = nbs4.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(22, 54).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition nbs5 = nose.addOrReplaceChild("nbs5", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r29 = nbs5.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(0, 19).addBox(-11.0189F, 5.1631F, 15.2597F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8189F, 12.0369F, 6.2403F, 0.6545F, 0.0F, 0.7854F));

		PartDefinition cube_r30 = nbs5.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(0, 20).addBox(-9.0F, 3.1755F, 15.9962F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5369F, 11.9035F, 6.2403F, 0.6545F, 0.0F, 1.0472F));

		PartDefinition cube_r31 = nbs5.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(0, 21).addBox(-9.0F, 3.1755F, 15.9962F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8189F, 12.0369F, 6.2403F, 0.6545F, 0.0F, 0.5236F));

		PartDefinition cube_r32 = nbs5.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(20, 58).addBox(-1.2582F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9418F, 7.9694F, 5.191F, 0.5236F, 0.0F, 0.7854F));

		PartDefinition cube_r33 = nbs5.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(22, 58).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.6122F, 6.1757F, 5.191F, 0.5236F, 0.0F, 0.8727F));

		PartDefinition cube_r34 = nbs5.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(24, 58).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.2916F, 2.709F, 5.191F, 0.5236F, 0.0F, 1.2654F));

		PartDefinition cube_r35 = nbs5.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(26, 58).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9418F, 7.9694F, 5.191F, 0.5236F, 0.0F, 0.6981F));

		PartDefinition cube_r36 = nbs5.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(28, 58).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6627F, 9.9909F, 5.191F, 0.5236F, 0.0F, 0.3054F));

		PartDefinition cube_r37 = nbs5.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(30, 58).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.3353F, 5.8505F, 4.6003F, 0.4363F, 0.0F, 0.829F));

		PartDefinition cube_r38 = nbs5.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(32, 58).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.3937F, 4.328F, 4.6003F, 0.4363F, 0.0F, 1.0036F));

		PartDefinition cube_r39 = nbs5.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(34, 58).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0768F, 2.6965F, 4.6003F, 0.4363F, 0.0F, 1.2217F));

		PartDefinition cube_r40 = nbs5.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(36, 58).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.4103F, 1.1152F, 4.6003F, 0.4363F, 0.0F, 1.3963F));

		PartDefinition cube_r41 = nbs5.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(38, 58).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.1714F, 7.8564F, 4.6003F, 0.4363F, 0.0F, 0.5672F));

		PartDefinition cube_r42 = nbs5.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(40, 58).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.3986F, 6.8048F, 4.6003F, 0.4363F, 0.0F, 0.7418F));

		PartDefinition cube_r43 = nbs5.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(42, 58).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6362F, 8.7348F, 4.6003F, 0.4363F, 0.0F, 0.3491F));

		PartDefinition cube_r44 = nbs5.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(44, 58).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1F, 9.2367F, 4.6003F, 0.4363F, 0.0F, 0.1745F));

		PartDefinition cube_r45 = nbs5.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(16, 41).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9074F, 4.8324F, 4.214F, 0.3491F, 0.0F, 0.9163F));

		PartDefinition cube_r46 = nbs5.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(18, 41).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(20, 41).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.5733F, 3.8053F, 4.214F, 0.3491F, 0.0F, 1.0036F));

		PartDefinition cube_r47 = nbs5.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(22, 41).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.1628F, 2.3638F, 4.214F, 0.3491F, 0.0F, 1.2217F));

		PartDefinition cube_r48 = nbs5.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(24, 41).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.4524F, 0.9463F, 4.214F, 0.3491F, 0.0F, 1.3963F));

		PartDefinition cube_r49 = nbs5.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(26, 41).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5755F, 5.1919F, 4.214F, 0.3491F, 0.0F, 0.829F));

		PartDefinition cube_r50 = nbs5.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(28, 41).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.7414F, 6.0877F, 4.214F, 0.3491F, 0.0F, 0.7418F));

		PartDefinition cube_r51 = nbs5.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(30, 41).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.6487F, 7.036F, 4.214F, 0.3491F, 0.0F, 0.5672F));

		PartDefinition cube_r52 = nbs5.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(37, 41).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.3035F, 7.8208F, 4.214F, 0.3491F, 0.0F, 0.3491F));

		PartDefinition cube_r53 = nbs5.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(34, 43).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9311F, 8.2787F, 4.214F, 0.3491F, 0.0F, 0.1745F));

		PartDefinition cube_r54 = nbs5.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(39, 43).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.9628F, 4.9494F, 4.0186F, 0.2618F, 0.0F, 0.7854F));

		PartDefinition cube_r55 = nbs5.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(32, 44).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5862F, 4.1393F, 4.0186F, 0.2618F, 0.0F, 0.9163F));

		PartDefinition cube_r56 = nbs5.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(0, 50).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0733F, 3.2925F, 4.0186F, 0.2618F, 0.0F, 1.0472F));

		PartDefinition cube_r57 = nbs5.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(2, 50).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.4457F, 2.3895F, 4.0186F, 0.2618F, 0.0F, 1.1781F));

		PartDefinition cube_r58 = nbs5.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(4, 50).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.6971F, 1.4455F, 4.0186F, 0.2618F, 0.0F, 1.309F));

		PartDefinition cube_r59 = nbs5.addOrReplaceChild("cube_r59", CubeListBuilder.create().texOffs(6, 50).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.8231F, 0.4768F, 4.0186F, 0.2618F, 0.0F, 1.4399F));

		PartDefinition cube_r60 = nbs5.addOrReplaceChild("cube_r60", CubeListBuilder.create().texOffs(8, 50).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4705F, 0.1642F, -1.836F, 0.1745F, 0.0F, 0.7854F));

		PartDefinition cube_r61 = nbs5.addOrReplaceChild("cube_r61", CubeListBuilder.create().texOffs(10, 50).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1301F, -0.2405F, -0.6972F, 0.0873F, 0.0F, 0.7854F));

		PartDefinition cube_r62 = nbs5.addOrReplaceChild("cube_r62", CubeListBuilder.create().texOffs(12, 50).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7487F, 1.1192F, -2.3569F, 0.2618F, 0.0F, 0.7854F));

		PartDefinition cube_r63 = nbs5.addOrReplaceChild("cube_r63", CubeListBuilder.create().texOffs(14, 50).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6446F, 1.2105F, -2.3569F, 0.2618F, 0.0F, 0.6545F));

		PartDefinition cube_r64 = nbs5.addOrReplaceChild("cube_r64", CubeListBuilder.create().texOffs(16, 50).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5294F, 1.2875F, -2.3569F, 0.2618F, 0.0F, 0.5236F));

		PartDefinition cube_r65 = nbs5.addOrReplaceChild("cube_r65", CubeListBuilder.create().texOffs(18, 50).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4052F, 1.3487F, -2.3569F, 0.2618F, 0.0F, 0.3927F));

		PartDefinition cube_r66 = nbs5.addOrReplaceChild("cube_r66", CubeListBuilder.create().texOffs(20, 50).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2741F, 1.3933F, -2.3569F, 0.2618F, 0.0F, 0.2618F));

		PartDefinition cube_r67 = nbs5.addOrReplaceChild("cube_r67", CubeListBuilder.create().texOffs(22, 50).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1382F, 1.4203F, -2.3569F, 0.2618F, 0.0F, 0.1309F));

		PartDefinition cube_r68 = nbs5.addOrReplaceChild("cube_r68", CubeListBuilder.create().texOffs(24, 50).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5163F, 0.1119F, -1.836F, 0.1745F, 0.0F, 0.9163F));

		PartDefinition cube_r69 = nbs5.addOrReplaceChild("cube_r69", CubeListBuilder.create().texOffs(26, 50).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5297F, 0.0919F, -1.836F, 0.1745F, 0.0F, 1.0472F));

		PartDefinition cube_r70 = nbs5.addOrReplaceChild("cube_r70", CubeListBuilder.create().texOffs(28, 50).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5403F, 0.0704F, -1.836F, 0.1745F, 0.0F, 1.1781F));

		PartDefinition cube_r71 = nbs5.addOrReplaceChild("cube_r71", CubeListBuilder.create().texOffs(30, 50).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5481F, 0.0476F, -1.836F, 0.1745F, 0.0F, 1.309F));

		PartDefinition cube_r72 = nbs5.addOrReplaceChild("cube_r72", CubeListBuilder.create().texOffs(32, 50).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5528F, 0.024F, -1.836F, 0.1745F, 0.0F, 1.4399F));

		PartDefinition cube_r73 = nbs5.addOrReplaceChild("cube_r73", CubeListBuilder.create().texOffs(34, 50).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.13F, 0.5005F, -1.836F, 0.1745F, 0.0F, 0.7854F));

		PartDefinition cube_r74 = nbs5.addOrReplaceChild("cube_r74", CubeListBuilder.create().texOffs(36, 50).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1119F, 0.5163F, -1.836F, 0.1745F, 0.0F, 0.6545F));

		PartDefinition cube_r75 = nbs5.addOrReplaceChild("cube_r75", CubeListBuilder.create().texOffs(38, 50).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0919F, 0.5297F, -1.836F, 0.1745F, 0.0F, 0.5236F));

		PartDefinition cube_r76 = nbs5.addOrReplaceChild("cube_r76", CubeListBuilder.create().texOffs(40, 50).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0704F, 0.5403F, -1.836F, 0.1745F, 0.0F, 0.3927F));

		PartDefinition cube_r77 = nbs5.addOrReplaceChild("cube_r77", CubeListBuilder.create().texOffs(42, 50).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0476F, 0.5481F, -1.836F, 0.1745F, 0.0F, 0.2618F));

		PartDefinition cube_r78 = nbs5.addOrReplaceChild("cube_r78", CubeListBuilder.create().texOffs(44, 50).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.024F, 0.5528F, -1.836F, 0.1745F, 0.0F, 0.1309F));

		PartDefinition cube_r79 = nbs5.addOrReplaceChild("cube_r79", CubeListBuilder.create().texOffs(46, 50).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0334F, -0.0444F, -0.6972F, 0.0873F, 0.0F, 1.4399F));

		PartDefinition cube_r80 = nbs5.addOrReplaceChild("cube_r80", CubeListBuilder.create().texOffs(48, 50).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.042F, -0.088F, -0.6972F, 0.0873F, 0.0F, 1.309F));

		PartDefinition cube_r81 = nbs5.addOrReplaceChild("cube_r81", CubeListBuilder.create().texOffs(50, 50).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0563F, -0.1301F, -0.6972F, 0.0873F, 0.0F, 1.1781F));

		PartDefinition cube_r82 = nbs5.addOrReplaceChild("cube_r82", CubeListBuilder.create().texOffs(52, 50).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.076F, -0.17F, -0.6972F, 0.0873F, 0.0F, 1.0472F));

		PartDefinition cube_r83 = nbs5.addOrReplaceChild("cube_r83", CubeListBuilder.create().texOffs(0, 54).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1007F, -0.207F, -0.6972F, 0.0873F, 0.0F, 0.9163F));

		PartDefinition cube_r84 = nbs5.addOrReplaceChild("cube_r84", CubeListBuilder.create().texOffs(2, 54).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0501F, -0.0099F, -0.1991F, 0.0873F, 0.0F, 0.1309F));

		PartDefinition cube_r85 = nbs5.addOrReplaceChild("cube_r85", CubeListBuilder.create().texOffs(4, 54).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0993F, -0.0001F, -0.1991F, 0.0873F, 0.0F, 0.2618F));

		PartDefinition cube_r86 = nbs5.addOrReplaceChild("cube_r86", CubeListBuilder.create().texOffs(6, 54).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1468F, 0.0161F, -0.1991F, 0.0873F, 0.0F, 0.3927F));

		PartDefinition cube_r87 = nbs5.addOrReplaceChild("cube_r87", CubeListBuilder.create().texOffs(8, 54).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1918F, 0.0383F, -0.1991F, 0.0873F, 0.0F, 0.5236F));

		PartDefinition cube_r88 = nbs5.addOrReplaceChild("cube_r88", CubeListBuilder.create().texOffs(10, 54).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2335F, 0.0661F, -0.1991F, 0.0873F, 0.0F, 0.6545F));

		PartDefinition cube_r89 = nbs5.addOrReplaceChild("cube_r89", CubeListBuilder.create().texOffs(12, 54).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2713F, 0.0992F, -0.1991F, 0.0873F, 0.0F, 0.7854F));

		PartDefinition nbs6 = nose.addOrReplaceChild("nbs6", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition cube_r90 = nbs6.addOrReplaceChild("cube_r90", CubeListBuilder.create().texOffs(0, 16).addBox(-11.0189F, 5.1631F, 15.2597F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8189F, 12.0369F, 6.2403F, 0.6545F, 0.0F, 0.7854F));

		PartDefinition cube_r91 = nbs6.addOrReplaceChild("cube_r91", CubeListBuilder.create().texOffs(0, 17).addBox(-9.0F, 3.1755F, 15.9962F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5369F, 11.9035F, 6.2403F, 0.6545F, 0.0F, 1.0472F));

		PartDefinition cube_r92 = nbs6.addOrReplaceChild("cube_r92", CubeListBuilder.create().texOffs(0, 18).addBox(-9.0F, 3.1755F, 15.9962F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8189F, 12.0369F, 6.2403F, 0.6545F, 0.0F, 0.5236F));

		PartDefinition cube_r93 = nbs6.addOrReplaceChild("cube_r93", CubeListBuilder.create().texOffs(57, 53).addBox(-1.2582F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9418F, 7.9694F, 5.191F, 0.5236F, 0.0F, 0.7854F));

		PartDefinition cube_r94 = nbs6.addOrReplaceChild("cube_r94", CubeListBuilder.create().texOffs(55, 57).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.6122F, 6.1757F, 5.191F, 0.5236F, 0.0F, 0.8727F));

		PartDefinition cube_r95 = nbs6.addOrReplaceChild("cube_r95", CubeListBuilder.create().texOffs(57, 56).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.2916F, 2.709F, 5.191F, 0.5236F, 0.0F, 1.2654F));

		PartDefinition cube_r96 = nbs6.addOrReplaceChild("cube_r96", CubeListBuilder.create().texOffs(0, 58).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9418F, 7.9694F, 5.191F, 0.5236F, 0.0F, 0.6981F));

		PartDefinition cube_r97 = nbs6.addOrReplaceChild("cube_r97", CubeListBuilder.create().texOffs(2, 58).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6627F, 9.9909F, 5.191F, 0.5236F, 0.0F, 0.3054F));

		PartDefinition cube_r98 = nbs6.addOrReplaceChild("cube_r98", CubeListBuilder.create().texOffs(4, 58).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.3353F, 5.8505F, 4.6003F, 0.4363F, 0.0F, 0.829F));

		PartDefinition cube_r99 = nbs6.addOrReplaceChild("cube_r99", CubeListBuilder.create().texOffs(6, 58).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.3937F, 4.328F, 4.6003F, 0.4363F, 0.0F, 1.0036F));

		PartDefinition cube_r100 = nbs6.addOrReplaceChild("cube_r100", CubeListBuilder.create().texOffs(8, 58).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0768F, 2.6965F, 4.6003F, 0.4363F, 0.0F, 1.2217F));

		PartDefinition cube_r101 = nbs6.addOrReplaceChild("cube_r101", CubeListBuilder.create().texOffs(10, 58).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.4103F, 1.1152F, 4.6003F, 0.4363F, 0.0F, 1.3963F));

		PartDefinition cube_r102 = nbs6.addOrReplaceChild("cube_r102", CubeListBuilder.create().texOffs(12, 58).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.1714F, 7.8564F, 4.6003F, 0.4363F, 0.0F, 0.5672F));

		PartDefinition cube_r103 = nbs6.addOrReplaceChild("cube_r103", CubeListBuilder.create().texOffs(14, 58).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.3986F, 6.8048F, 4.6003F, 0.4363F, 0.0F, 0.7418F));

		PartDefinition cube_r104 = nbs6.addOrReplaceChild("cube_r104", CubeListBuilder.create().texOffs(16, 58).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6362F, 8.7348F, 4.6003F, 0.4363F, 0.0F, 0.3491F));

		PartDefinition cube_r105 = nbs6.addOrReplaceChild("cube_r105", CubeListBuilder.create().texOffs(18, 58).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1F, 9.2367F, 4.6003F, 0.4363F, 0.0F, 0.1745F));

		PartDefinition cube_r106 = nbs6.addOrReplaceChild("cube_r106", CubeListBuilder.create().texOffs(18, 28).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9074F, 4.8324F, 4.214F, 0.3491F, 0.0F, 0.9163F));

		PartDefinition cube_r107 = nbs6.addOrReplaceChild("cube_r107", CubeListBuilder.create().texOffs(20, 28).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(22, 28).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.5733F, 3.8053F, 4.214F, 0.3491F, 0.0F, 1.0036F));

		PartDefinition cube_r108 = nbs6.addOrReplaceChild("cube_r108", CubeListBuilder.create().texOffs(24, 28).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.1628F, 2.3638F, 4.214F, 0.3491F, 0.0F, 1.2217F));

		PartDefinition cube_r109 = nbs6.addOrReplaceChild("cube_r109", CubeListBuilder.create().texOffs(6, 29).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.4524F, 0.9463F, 4.214F, 0.3491F, 0.0F, 1.3963F));

		PartDefinition cube_r110 = nbs6.addOrReplaceChild("cube_r110", CubeListBuilder.create().texOffs(8, 29).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5755F, 5.1919F, 4.214F, 0.3491F, 0.0F, 0.829F));

		PartDefinition cube_r111 = nbs6.addOrReplaceChild("cube_r111", CubeListBuilder.create().texOffs(10, 29).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.7414F, 6.0877F, 4.214F, 0.3491F, 0.0F, 0.7418F));

		PartDefinition cube_r112 = nbs6.addOrReplaceChild("cube_r112", CubeListBuilder.create().texOffs(12, 29).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.6487F, 7.036F, 4.214F, 0.3491F, 0.0F, 0.5672F));

		PartDefinition cube_r113 = nbs6.addOrReplaceChild("cube_r113", CubeListBuilder.create().texOffs(0, 30).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.3035F, 7.8208F, 4.214F, 0.3491F, 0.0F, 0.3491F));

		PartDefinition cube_r114 = nbs6.addOrReplaceChild("cube_r114", CubeListBuilder.create().texOffs(2, 30).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9311F, 8.2787F, 4.214F, 0.3491F, 0.0F, 0.1745F));

		PartDefinition cube_r115 = nbs6.addOrReplaceChild("cube_r115", CubeListBuilder.create().texOffs(4, 30).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.9628F, 4.9494F, 4.0186F, 0.2618F, 0.0F, 0.7854F));

		PartDefinition cube_r116 = nbs6.addOrReplaceChild("cube_r116", CubeListBuilder.create().texOffs(26, 30).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5862F, 4.1393F, 4.0186F, 0.2618F, 0.0F, 0.9163F));

		PartDefinition cube_r117 = nbs6.addOrReplaceChild("cube_r117", CubeListBuilder.create().texOffs(14, 32).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0733F, 3.2925F, 4.0186F, 0.2618F, 0.0F, 1.0472F));

		PartDefinition cube_r118 = nbs6.addOrReplaceChild("cube_r118", CubeListBuilder.create().texOffs(16, 32).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.4457F, 2.3895F, 4.0186F, 0.2618F, 0.0F, 1.1781F));

		PartDefinition cube_r119 = nbs6.addOrReplaceChild("cube_r119", CubeListBuilder.create().texOffs(18, 32).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.6971F, 1.4455F, 4.0186F, 0.2618F, 0.0F, 1.309F));

		PartDefinition cube_r120 = nbs6.addOrReplaceChild("cube_r120", CubeListBuilder.create().texOffs(20, 32).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.8231F, 0.4768F, 4.0186F, 0.2618F, 0.0F, 1.4399F));

		PartDefinition cube_r121 = nbs6.addOrReplaceChild("cube_r121", CubeListBuilder.create().texOffs(22, 32).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4705F, 0.1642F, -1.836F, 0.1745F, 0.0F, 0.7854F));

		PartDefinition cube_r122 = nbs6.addOrReplaceChild("cube_r122", CubeListBuilder.create().texOffs(24, 32).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1301F, -0.2405F, -0.6972F, 0.0873F, 0.0F, 0.7854F));

		PartDefinition cube_r123 = nbs6.addOrReplaceChild("cube_r123", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7487F, 1.1192F, -2.3569F, 0.2618F, 0.0F, 0.7854F));

		PartDefinition cube_r124 = nbs6.addOrReplaceChild("cube_r124", CubeListBuilder.create().texOffs(33, 36).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6446F, 1.2105F, -2.3569F, 0.2618F, 0.0F, 0.6545F));

		PartDefinition cube_r125 = nbs6.addOrReplaceChild("cube_r125", CubeListBuilder.create().texOffs(0, 37).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5294F, 1.2875F, -2.3569F, 0.2618F, 0.0F, 0.5236F));

		PartDefinition cube_r126 = nbs6.addOrReplaceChild("cube_r126", CubeListBuilder.create().texOffs(2, 37).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4052F, 1.3487F, -2.3569F, 0.2618F, 0.0F, 0.3927F));

		PartDefinition cube_r127 = nbs6.addOrReplaceChild("cube_r127", CubeListBuilder.create().texOffs(4, 37).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2741F, 1.3933F, -2.3569F, 0.2618F, 0.0F, 0.2618F));

		PartDefinition cube_r128 = nbs6.addOrReplaceChild("cube_r128", CubeListBuilder.create().texOffs(6, 37).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1382F, 1.4203F, -2.3569F, 0.2618F, 0.0F, 0.1309F));

		PartDefinition cube_r129 = nbs6.addOrReplaceChild("cube_r129", CubeListBuilder.create().texOffs(8, 37).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5163F, 0.1119F, -1.836F, 0.1745F, 0.0F, 0.9163F));

		PartDefinition cube_r130 = nbs6.addOrReplaceChild("cube_r130", CubeListBuilder.create().texOffs(10, 37).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5297F, 0.0919F, -1.836F, 0.1745F, 0.0F, 1.0472F));

		PartDefinition cube_r131 = nbs6.addOrReplaceChild("cube_r131", CubeListBuilder.create().texOffs(12, 37).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5403F, 0.0704F, -1.836F, 0.1745F, 0.0F, 1.1781F));

		PartDefinition cube_r132 = nbs6.addOrReplaceChild("cube_r132", CubeListBuilder.create().texOffs(14, 37).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5481F, 0.0476F, -1.836F, 0.1745F, 0.0F, 1.309F));

		PartDefinition cube_r133 = nbs6.addOrReplaceChild("cube_r133", CubeListBuilder.create().texOffs(16, 37).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5528F, 0.024F, -1.836F, 0.1745F, 0.0F, 1.4399F));

		PartDefinition cube_r134 = nbs6.addOrReplaceChild("cube_r134", CubeListBuilder.create().texOffs(18, 37).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.13F, 0.5005F, -1.836F, 0.1745F, 0.0F, 0.7854F));

		PartDefinition cube_r135 = nbs6.addOrReplaceChild("cube_r135", CubeListBuilder.create().texOffs(20, 37).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1119F, 0.5163F, -1.836F, 0.1745F, 0.0F, 0.6545F));

		PartDefinition cube_r136 = nbs6.addOrReplaceChild("cube_r136", CubeListBuilder.create().texOffs(22, 37).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0919F, 0.5297F, -1.836F, 0.1745F, 0.0F, 0.5236F));

		PartDefinition cube_r137 = nbs6.addOrReplaceChild("cube_r137", CubeListBuilder.create().texOffs(24, 37).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0704F, 0.5403F, -1.836F, 0.1745F, 0.0F, 0.3927F));

		PartDefinition cube_r138 = nbs6.addOrReplaceChild("cube_r138", CubeListBuilder.create().texOffs(26, 37).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0476F, 0.5481F, -1.836F, 0.1745F, 0.0F, 0.2618F));

		PartDefinition cube_r139 = nbs6.addOrReplaceChild("cube_r139", CubeListBuilder.create().texOffs(28, 37).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.024F, 0.5528F, -1.836F, 0.1745F, 0.0F, 0.1309F));

		PartDefinition cube_r140 = nbs6.addOrReplaceChild("cube_r140", CubeListBuilder.create().texOffs(30, 37).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0334F, -0.0444F, -0.6972F, 0.0873F, 0.0F, 1.4399F));

		PartDefinition cube_r141 = nbs6.addOrReplaceChild("cube_r141", CubeListBuilder.create().texOffs(35, 39).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.042F, -0.088F, -0.6972F, 0.0873F, 0.0F, 1.309F));

		PartDefinition cube_r142 = nbs6.addOrReplaceChild("cube_r142", CubeListBuilder.create().texOffs(32, 40).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0563F, -0.1301F, -0.6972F, 0.0873F, 0.0F, 1.1781F));

		PartDefinition cube_r143 = nbs6.addOrReplaceChild("cube_r143", CubeListBuilder.create().texOffs(0, 41).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.076F, -0.17F, -0.6972F, 0.0873F, 0.0F, 1.0472F));

		PartDefinition cube_r144 = nbs6.addOrReplaceChild("cube_r144", CubeListBuilder.create().texOffs(2, 41).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1007F, -0.207F, -0.6972F, 0.0873F, 0.0F, 0.9163F));

		PartDefinition cube_r145 = nbs6.addOrReplaceChild("cube_r145", CubeListBuilder.create().texOffs(4, 41).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0501F, -0.0099F, -0.1991F, 0.0873F, 0.0F, 0.1309F));

		PartDefinition cube_r146 = nbs6.addOrReplaceChild("cube_r146", CubeListBuilder.create().texOffs(6, 41).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0993F, -0.0001F, -0.1991F, 0.0873F, 0.0F, 0.2618F));

		PartDefinition cube_r147 = nbs6.addOrReplaceChild("cube_r147", CubeListBuilder.create().texOffs(8, 41).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1468F, 0.0161F, -0.1991F, 0.0873F, 0.0F, 0.3927F));

		PartDefinition cube_r148 = nbs6.addOrReplaceChild("cube_r148", CubeListBuilder.create().texOffs(10, 41).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1918F, 0.0383F, -0.1991F, 0.0873F, 0.0F, 0.5236F));

		PartDefinition cube_r149 = nbs6.addOrReplaceChild("cube_r149", CubeListBuilder.create().texOffs(12, 41).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2335F, 0.0661F, -0.1991F, 0.0873F, 0.0F, 0.6545F));

		PartDefinition cube_r150 = nbs6.addOrReplaceChild("cube_r150", CubeListBuilder.create().texOffs(14, 41).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2713F, 0.0992F, -0.1991F, 0.0873F, 0.0F, 0.7854F));

		PartDefinition nbs7 = nose.addOrReplaceChild("nbs7", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition cube_r151 = nbs7.addOrReplaceChild("cube_r151", CubeListBuilder.create().texOffs(0, 13).addBox(-11.0189F, 5.1631F, 15.2597F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8189F, 12.0369F, 6.2403F, 0.6545F, 0.0F, 0.7854F));

		PartDefinition cube_r152 = nbs7.addOrReplaceChild("cube_r152", CubeListBuilder.create().texOffs(0, 14).addBox(-9.0F, 3.1755F, 15.9962F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5369F, 11.9035F, 6.2403F, 0.6545F, 0.0F, 1.0472F));

		PartDefinition cube_r153 = nbs7.addOrReplaceChild("cube_r153", CubeListBuilder.create().texOffs(0, 15).addBox(-9.0F, 3.1755F, 15.9962F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8189F, 12.0369F, 6.2403F, 0.6545F, 0.0F, 0.5236F));

		PartDefinition cube_r154 = nbs7.addOrReplaceChild("cube_r154", CubeListBuilder.create().texOffs(18, 45).addBox(-1.2582F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9418F, 7.9694F, 5.191F, 0.5236F, 0.0F, 0.7854F));

		PartDefinition cube_r155 = nbs7.addOrReplaceChild("cube_r155", CubeListBuilder.create().texOffs(20, 45).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.6122F, 6.1757F, 5.191F, 0.5236F, 0.0F, 0.8727F));

		PartDefinition cube_r156 = nbs7.addOrReplaceChild("cube_r156", CubeListBuilder.create().texOffs(22, 45).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.2916F, 2.709F, 5.191F, 0.5236F, 0.0F, 1.2654F));

		PartDefinition cube_r157 = nbs7.addOrReplaceChild("cube_r157", CubeListBuilder.create().texOffs(24, 45).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9418F, 7.9694F, 5.191F, 0.5236F, 0.0F, 0.6981F));

		PartDefinition cube_r158 = nbs7.addOrReplaceChild("cube_r158", CubeListBuilder.create().texOffs(26, 45).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6627F, 9.9909F, 5.191F, 0.5236F, 0.0F, 0.3054F));

		PartDefinition cube_r159 = nbs7.addOrReplaceChild("cube_r159", CubeListBuilder.create().texOffs(28, 45).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.3353F, 5.8505F, 4.6003F, 0.4363F, 0.0F, 0.829F));

		PartDefinition cube_r160 = nbs7.addOrReplaceChild("cube_r160", CubeListBuilder.create().texOffs(30, 45).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.3937F, 4.328F, 4.6003F, 0.4363F, 0.0F, 1.0036F));

		PartDefinition cube_r161 = nbs7.addOrReplaceChild("cube_r161", CubeListBuilder.create().texOffs(37, 45).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0768F, 2.6965F, 4.6003F, 0.4363F, 0.0F, 1.2217F));

		PartDefinition cube_r162 = nbs7.addOrReplaceChild("cube_r162", CubeListBuilder.create().texOffs(42, 45).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.4103F, 1.1152F, 4.6003F, 0.4363F, 0.0F, 1.3963F));

		PartDefinition cube_r163 = nbs7.addOrReplaceChild("cube_r163", CubeListBuilder.create().texOffs(53, 54).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.1714F, 7.8564F, 4.6003F, 0.4363F, 0.0F, 0.5672F));

		PartDefinition cube_r164 = nbs7.addOrReplaceChild("cube_r164", CubeListBuilder.create().texOffs(55, 54).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.3986F, 6.8048F, 4.6003F, 0.4363F, 0.0F, 0.7418F));

		PartDefinition cube_r165 = nbs7.addOrReplaceChild("cube_r165", CubeListBuilder.create().texOffs(57, 50).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6362F, 8.7348F, 4.6003F, 0.4363F, 0.0F, 0.3491F));

		PartDefinition cube_r166 = nbs7.addOrReplaceChild("cube_r166", CubeListBuilder.create().texOffs(53, 57).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1F, 9.2367F, 4.6003F, 0.4363F, 0.0F, 0.1745F));

		PartDefinition cube_r167 = nbs7.addOrReplaceChild("cube_r167", CubeListBuilder.create().texOffs(18, 8).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9074F, 4.8324F, 4.214F, 0.3491F, 0.0F, 0.9163F));

		PartDefinition cube_r168 = nbs7.addOrReplaceChild("cube_r168", CubeListBuilder.create().texOffs(18, 12).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(18, 16).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.5733F, 3.8053F, 4.214F, 0.3491F, 0.0F, 1.0036F));

		PartDefinition cube_r169 = nbs7.addOrReplaceChild("cube_r169", CubeListBuilder.create().texOffs(20, 0).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.1628F, 2.3638F, 4.214F, 0.3491F, 0.0F, 1.2217F));

		PartDefinition cube_r170 = nbs7.addOrReplaceChild("cube_r170", CubeListBuilder.create().texOffs(20, 4).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.4524F, 0.9463F, 4.214F, 0.3491F, 0.0F, 1.3963F));

		PartDefinition cube_r171 = nbs7.addOrReplaceChild("cube_r171", CubeListBuilder.create().texOffs(20, 8).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5755F, 5.1919F, 4.214F, 0.3491F, 0.0F, 0.829F));

		PartDefinition cube_r172 = nbs7.addOrReplaceChild("cube_r172", CubeListBuilder.create().texOffs(20, 12).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.7414F, 6.0877F, 4.214F, 0.3491F, 0.0F, 0.7418F));

		PartDefinition cube_r173 = nbs7.addOrReplaceChild("cube_r173", CubeListBuilder.create().texOffs(14, 20).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.6487F, 7.036F, 4.214F, 0.3491F, 0.0F, 0.5672F));

		PartDefinition cube_r174 = nbs7.addOrReplaceChild("cube_r174", CubeListBuilder.create().texOffs(16, 20).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.3035F, 7.8208F, 4.214F, 0.3491F, 0.0F, 0.3491F));

		PartDefinition cube_r175 = nbs7.addOrReplaceChild("cube_r175", CubeListBuilder.create().texOffs(20, 16).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9311F, 8.2787F, 4.214F, 0.3491F, 0.0F, 0.1745F));

		PartDefinition cube_r176 = nbs7.addOrReplaceChild("cube_r176", CubeListBuilder.create().texOffs(18, 20).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.9628F, 4.9494F, 4.0186F, 0.2618F, 0.0F, 0.7854F));

		PartDefinition cube_r177 = nbs7.addOrReplaceChild("cube_r177", CubeListBuilder.create().texOffs(20, 20).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5862F, 4.1393F, 4.0186F, 0.2618F, 0.0F, 0.9163F));

		PartDefinition cube_r178 = nbs7.addOrReplaceChild("cube_r178", CubeListBuilder.create().texOffs(6, 21).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0733F, 3.2925F, 4.0186F, 0.2618F, 0.0F, 1.0472F));

		PartDefinition cube_r179 = nbs7.addOrReplaceChild("cube_r179", CubeListBuilder.create().texOffs(8, 21).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.4457F, 2.3895F, 4.0186F, 0.2618F, 0.0F, 1.1781F));

		PartDefinition cube_r180 = nbs7.addOrReplaceChild("cube_r180", CubeListBuilder.create().texOffs(10, 21).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.6971F, 1.4455F, 4.0186F, 0.2618F, 0.0F, 1.309F));

		PartDefinition cube_r181 = nbs7.addOrReplaceChild("cube_r181", CubeListBuilder.create().texOffs(12, 21).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.8231F, 0.4768F, 4.0186F, 0.2618F, 0.0F, 1.4399F));

		PartDefinition cube_r182 = nbs7.addOrReplaceChild("cube_r182", CubeListBuilder.create().texOffs(0, 22).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4705F, 0.1642F, -1.836F, 0.1745F, 0.0F, 0.7854F));

		PartDefinition cube_r183 = nbs7.addOrReplaceChild("cube_r183", CubeListBuilder.create().texOffs(22, 0).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1301F, -0.2405F, -0.6972F, 0.0873F, 0.0F, 0.7854F));

		PartDefinition cube_r184 = nbs7.addOrReplaceChild("cube_r184", CubeListBuilder.create().texOffs(2, 22).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7487F, 1.1192F, -2.3569F, 0.2618F, 0.0F, 0.7854F));

		PartDefinition cube_r185 = nbs7.addOrReplaceChild("cube_r185", CubeListBuilder.create().texOffs(4, 22).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6446F, 1.2105F, -2.3569F, 0.2618F, 0.0F, 0.6545F));

		PartDefinition cube_r186 = nbs7.addOrReplaceChild("cube_r186", CubeListBuilder.create().texOffs(22, 4).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5294F, 1.2875F, -2.3569F, 0.2618F, 0.0F, 0.5236F));

		PartDefinition cube_r187 = nbs7.addOrReplaceChild("cube_r187", CubeListBuilder.create().texOffs(22, 8).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4052F, 1.3487F, -2.3569F, 0.2618F, 0.0F, 0.3927F));

		PartDefinition cube_r188 = nbs7.addOrReplaceChild("cube_r188", CubeListBuilder.create().texOffs(22, 12).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2741F, 1.3933F, -2.3569F, 0.2618F, 0.0F, 0.2618F));

		PartDefinition cube_r189 = nbs7.addOrReplaceChild("cube_r189", CubeListBuilder.create().texOffs(22, 16).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1382F, 1.4203F, -2.3569F, 0.2618F, 0.0F, 0.1309F));

		PartDefinition cube_r190 = nbs7.addOrReplaceChild("cube_r190", CubeListBuilder.create().texOffs(22, 20).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5163F, 0.1119F, -1.836F, 0.1745F, 0.0F, 0.9163F));

		PartDefinition cube_r191 = nbs7.addOrReplaceChild("cube_r191", CubeListBuilder.create().texOffs(24, 0).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5297F, 0.0919F, -1.836F, 0.1745F, 0.0F, 1.0472F));

		PartDefinition cube_r192 = nbs7.addOrReplaceChild("cube_r192", CubeListBuilder.create().texOffs(24, 4).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5403F, 0.0704F, -1.836F, 0.1745F, 0.0F, 1.1781F));

		PartDefinition cube_r193 = nbs7.addOrReplaceChild("cube_r193", CubeListBuilder.create().texOffs(24, 8).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5481F, 0.0476F, -1.836F, 0.1745F, 0.0F, 1.309F));

		PartDefinition cube_r194 = nbs7.addOrReplaceChild("cube_r194", CubeListBuilder.create().texOffs(24, 12).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5528F, 0.024F, -1.836F, 0.1745F, 0.0F, 1.4399F));

		PartDefinition cube_r195 = nbs7.addOrReplaceChild("cube_r195", CubeListBuilder.create().texOffs(14, 24).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.13F, 0.5005F, -1.836F, 0.1745F, 0.0F, 0.7854F));

		PartDefinition cube_r196 = nbs7.addOrReplaceChild("cube_r196", CubeListBuilder.create().texOffs(16, 24).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1119F, 0.5163F, -1.836F, 0.1745F, 0.0F, 0.6545F));

		PartDefinition cube_r197 = nbs7.addOrReplaceChild("cube_r197", CubeListBuilder.create().texOffs(24, 16).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0919F, 0.5297F, -1.836F, 0.1745F, 0.0F, 0.5236F));

		PartDefinition cube_r198 = nbs7.addOrReplaceChild("cube_r198", CubeListBuilder.create().texOffs(18, 24).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0704F, 0.5403F, -1.836F, 0.1745F, 0.0F, 0.3927F));

		PartDefinition cube_r199 = nbs7.addOrReplaceChild("cube_r199", CubeListBuilder.create().texOffs(20, 24).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0476F, 0.5481F, -1.836F, 0.1745F, 0.0F, 0.2618F));

		PartDefinition cube_r200 = nbs7.addOrReplaceChild("cube_r200", CubeListBuilder.create().texOffs(24, 20).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.024F, 0.5528F, -1.836F, 0.1745F, 0.0F, 0.1309F));

		PartDefinition cube_r201 = nbs7.addOrReplaceChild("cube_r201", CubeListBuilder.create().texOffs(22, 24).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0334F, -0.0444F, -0.6972F, 0.0873F, 0.0F, 1.4399F));

		PartDefinition cube_r202 = nbs7.addOrReplaceChild("cube_r202", CubeListBuilder.create().texOffs(24, 24).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.042F, -0.088F, -0.6972F, 0.0873F, 0.0F, 1.309F));

		PartDefinition cube_r203 = nbs7.addOrReplaceChild("cube_r203", CubeListBuilder.create().texOffs(6, 25).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0563F, -0.1301F, -0.6972F, 0.0873F, 0.0F, 1.1781F));

		PartDefinition cube_r204 = nbs7.addOrReplaceChild("cube_r204", CubeListBuilder.create().texOffs(8, 25).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.076F, -0.17F, -0.6972F, 0.0873F, 0.0F, 1.0472F));

		PartDefinition cube_r205 = nbs7.addOrReplaceChild("cube_r205", CubeListBuilder.create().texOffs(10, 25).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1007F, -0.207F, -0.6972F, 0.0873F, 0.0F, 0.9163F));

		PartDefinition cube_r206 = nbs7.addOrReplaceChild("cube_r206", CubeListBuilder.create().texOffs(12, 25).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0501F, -0.0099F, -0.1991F, 0.0873F, 0.0F, 0.1309F));

		PartDefinition cube_r207 = nbs7.addOrReplaceChild("cube_r207", CubeListBuilder.create().texOffs(0, 26).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0993F, -0.0001F, -0.1991F, 0.0873F, 0.0F, 0.2618F));

		PartDefinition cube_r208 = nbs7.addOrReplaceChild("cube_r208", CubeListBuilder.create().texOffs(2, 26).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1468F, 0.0161F, -0.1991F, 0.0873F, 0.0F, 0.3927F));

		PartDefinition cube_r209 = nbs7.addOrReplaceChild("cube_r209", CubeListBuilder.create().texOffs(4, 26).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1918F, 0.0383F, -0.1991F, 0.0873F, 0.0F, 0.5236F));

		PartDefinition cube_r210 = nbs7.addOrReplaceChild("cube_r210", CubeListBuilder.create().texOffs(14, 28).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2335F, 0.0661F, -0.1991F, 0.0873F, 0.0F, 0.6545F));

		PartDefinition cube_r211 = nbs7.addOrReplaceChild("cube_r211", CubeListBuilder.create().texOffs(16, 28).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2713F, 0.0992F, -0.1991F, 0.0873F, 0.0F, 0.7854F));

		PartDefinition nbs8 = nose.addOrReplaceChild("nbs8", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition cube_r212 = nbs8.addOrReplaceChild("cube_r212", CubeListBuilder.create().texOffs(0, 10).addBox(-11.0189F, 5.1631F, 15.2597F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8189F, 12.0369F, 6.2403F, 0.6545F, 0.0F, 0.7854F));

		PartDefinition cube_r213 = nbs8.addOrReplaceChild("cube_r213", CubeListBuilder.create().texOffs(0, 11).addBox(-9.0F, 3.1755F, 15.9962F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5369F, 11.9035F, 6.2403F, 0.6545F, 0.0F, 1.0472F));

		PartDefinition cube_r214 = nbs8.addOrReplaceChild("cube_r214", CubeListBuilder.create().texOffs(0, 12).addBox(-9.0F, 3.1755F, 15.9962F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8189F, 12.0369F, 6.2403F, 0.6545F, 0.0F, 0.5236F));

		PartDefinition cube_r215 = nbs8.addOrReplaceChild("cube_r215", CubeListBuilder.create().texOffs(7, 33).addBox(-1.2582F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9418F, 7.9694F, 5.191F, 0.5236F, 0.0F, 0.7854F));

		PartDefinition cube_r216 = nbs8.addOrReplaceChild("cube_r216", CubeListBuilder.create().texOffs(9, 33).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.6122F, 6.1757F, 5.191F, 0.5236F, 0.0F, 0.8727F));

		PartDefinition cube_r217 = nbs8.addOrReplaceChild("cube_r217", CubeListBuilder.create().texOffs(11, 33).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.2916F, 2.709F, 5.191F, 0.5236F, 0.0F, 1.2654F));

		PartDefinition cube_r218 = nbs8.addOrReplaceChild("cube_r218", CubeListBuilder.create().texOffs(13, 33).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9418F, 7.9694F, 5.191F, 0.5236F, 0.0F, 0.6981F));

		PartDefinition cube_r219 = nbs8.addOrReplaceChild("cube_r219", CubeListBuilder.create().texOffs(0, 45).addBox(0.0F, 0.1755F, 14.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6627F, 9.9909F, 5.191F, 0.5236F, 0.0F, 0.3054F));

		PartDefinition cube_r220 = nbs8.addOrReplaceChild("cube_r220", CubeListBuilder.create().texOffs(2, 45).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.3353F, 5.8505F, 4.6003F, 0.4363F, 0.0F, 0.829F));

		PartDefinition cube_r221 = nbs8.addOrReplaceChild("cube_r221", CubeListBuilder.create().texOffs(4, 45).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.3937F, 4.328F, 4.6003F, 0.4363F, 0.0F, 1.0036F));

		PartDefinition cube_r222 = nbs8.addOrReplaceChild("cube_r222", CubeListBuilder.create().texOffs(6, 45).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0768F, 2.6965F, 4.6003F, 0.4363F, 0.0F, 1.2217F));

		PartDefinition cube_r223 = nbs8.addOrReplaceChild("cube_r223", CubeListBuilder.create().texOffs(8, 45).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.4103F, 1.1152F, 4.6003F, 0.4363F, 0.0F, 1.3963F));

		PartDefinition cube_r224 = nbs8.addOrReplaceChild("cube_r224", CubeListBuilder.create().texOffs(10, 45).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.1714F, 7.8564F, 4.6003F, 0.4363F, 0.0F, 0.5672F));

		PartDefinition cube_r225 = nbs8.addOrReplaceChild("cube_r225", CubeListBuilder.create().texOffs(12, 45).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.3986F, 6.8048F, 4.6003F, 0.4363F, 0.0F, 0.7418F));

		PartDefinition cube_r226 = nbs8.addOrReplaceChild("cube_r226", CubeListBuilder.create().texOffs(14, 45).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6362F, 8.7348F, 4.6003F, 0.4363F, 0.0F, 0.3491F));

		PartDefinition cube_r227 = nbs8.addOrReplaceChild("cube_r227", CubeListBuilder.create().texOffs(16, 45).addBox(0.0F, 0.1755F, 11.9962F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1F, 9.2367F, 4.6003F, 0.4363F, 0.0F, 0.1745F));

		PartDefinition cube_r228 = nbs8.addOrReplaceChild("cube_r228", CubeListBuilder.create().texOffs(2, 1).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9074F, 4.8324F, 4.214F, 0.3491F, 0.0F, 0.9163F));

		PartDefinition cube_r229 = nbs8.addOrReplaceChild("cube_r229", CubeListBuilder.create().texOffs(4, 1).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.5733F, 3.8053F, 4.214F, 0.3491F, 0.0F, 1.0036F));

		PartDefinition cube_r230 = nbs8.addOrReplaceChild("cube_r230", CubeListBuilder.create().texOffs(6, 1).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.1628F, 2.3638F, 4.214F, 0.3491F, 0.0F, 1.2217F));

		PartDefinition cube_r231 = nbs8.addOrReplaceChild("cube_r231", CubeListBuilder.create().texOffs(2, 6).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.4524F, 0.9463F, 4.214F, 0.3491F, 0.0F, 1.3963F));

		PartDefinition cube_r232 = nbs8.addOrReplaceChild("cube_r232", CubeListBuilder.create().texOffs(4, 6).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5755F, 5.1919F, 4.214F, 0.3491F, 0.0F, 0.829F));

		PartDefinition cube_r233 = nbs8.addOrReplaceChild("cube_r233", CubeListBuilder.create().texOffs(6, 5).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.7414F, 6.0877F, 4.214F, 0.3491F, 0.0F, 0.7418F));

		PartDefinition cube_r234 = nbs8.addOrReplaceChild("cube_r234", CubeListBuilder.create().texOffs(8, 1).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.6487F, 7.036F, 4.214F, 0.3491F, 0.0F, 0.5672F));

		PartDefinition cube_r235 = nbs8.addOrReplaceChild("cube_r235", CubeListBuilder.create().texOffs(8, 5).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.3035F, 7.8208F, 4.214F, 0.3491F, 0.0F, 0.3491F));

		PartDefinition cube_r236 = nbs8.addOrReplaceChild("cube_r236", CubeListBuilder.create().texOffs(6, 9).addBox(0.0F, 0.1755F, 7.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9311F, 8.2787F, 4.214F, 0.3491F, 0.0F, 0.1745F));

		PartDefinition cube_r237 = nbs8.addOrReplaceChild("cube_r237", CubeListBuilder.create().texOffs(8, 9).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.9628F, 4.9494F, 4.0186F, 0.2618F, 0.0F, 0.7854F));

		PartDefinition cube_r238 = nbs8.addOrReplaceChild("cube_r238", CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5862F, 4.1393F, 4.0186F, 0.2618F, 0.0F, 0.9163F));

		PartDefinition cube_r239 = nbs8.addOrReplaceChild("cube_r239", CubeListBuilder.create().texOffs(10, 1).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0733F, 3.2925F, 4.0186F, 0.2618F, 0.0F, 1.0472F));

		PartDefinition cube_r240 = nbs8.addOrReplaceChild("cube_r240", CubeListBuilder.create().texOffs(2, 10).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.4457F, 2.3895F, 4.0186F, 0.2618F, 0.0F, 1.1781F));

		PartDefinition cube_r241 = nbs8.addOrReplaceChild("cube_r241", CubeListBuilder.create().texOffs(4, 10).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.6971F, 1.4455F, 4.0186F, 0.2618F, 0.0F, 1.309F));

		PartDefinition cube_r242 = nbs8.addOrReplaceChild("cube_r242", CubeListBuilder.create().texOffs(10, 5).addBox(0.0F, 0.1755F, 3.9962F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.8231F, 0.4768F, 4.0186F, 0.2618F, 0.0F, 1.4399F));

		PartDefinition cube_r243 = nbs8.addOrReplaceChild("cube_r243", CubeListBuilder.create().texOffs(10, 9).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4705F, 0.1642F, -1.836F, 0.1745F, 0.0F, 0.7854F));

		PartDefinition cube_r244 = nbs8.addOrReplaceChild("cube_r244", CubeListBuilder.create().texOffs(12, 1).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1301F, -0.2405F, -0.6972F, 0.0873F, 0.0F, 0.7854F));

		PartDefinition cube_r245 = nbs8.addOrReplaceChild("cube_r245", CubeListBuilder.create().texOffs(12, 5).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7487F, 1.1192F, -2.3569F, 0.2618F, 0.0F, 0.7854F));

		PartDefinition cube_r246 = nbs8.addOrReplaceChild("cube_r246", CubeListBuilder.create().texOffs(12, 9).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6446F, 1.2105F, -2.3569F, 0.2618F, 0.0F, 0.6545F));

		PartDefinition cube_r247 = nbs8.addOrReplaceChild("cube_r247", CubeListBuilder.create().texOffs(6, 13).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5294F, 1.2875F, -2.3569F, 0.2618F, 0.0F, 0.5236F));

		PartDefinition cube_r248 = nbs8.addOrReplaceChild("cube_r248", CubeListBuilder.create().texOffs(8, 13).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4052F, 1.3487F, -2.3569F, 0.2618F, 0.0F, 0.3927F));

		PartDefinition cube_r249 = nbs8.addOrReplaceChild("cube_r249", CubeListBuilder.create().texOffs(10, 13).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2741F, 1.3933F, -2.3569F, 0.2618F, 0.0F, 0.2618F));

		PartDefinition cube_r250 = nbs8.addOrReplaceChild("cube_r250", CubeListBuilder.create().texOffs(12, 13).addBox(-0.5F, 8.0F, 8.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1382F, 1.4203F, -2.3569F, 0.2618F, 0.0F, 0.1309F));

		PartDefinition cube_r251 = nbs8.addOrReplaceChild("cube_r251", CubeListBuilder.create().texOffs(0, 14).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5163F, 0.1119F, -1.836F, 0.1745F, 0.0F, 0.9163F));

		PartDefinition cube_r252 = nbs8.addOrReplaceChild("cube_r252", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5297F, 0.0919F, -1.836F, 0.1745F, 0.0F, 1.0472F));

		PartDefinition cube_r253 = nbs8.addOrReplaceChild("cube_r253", CubeListBuilder.create().texOffs(2, 14).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5403F, 0.0704F, -1.836F, 0.1745F, 0.0F, 1.1781F));

		PartDefinition cube_r254 = nbs8.addOrReplaceChild("cube_r254", CubeListBuilder.create().texOffs(4, 14).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5481F, 0.0476F, -1.836F, 0.1745F, 0.0F, 1.309F));

		PartDefinition cube_r255 = nbs8.addOrReplaceChild("cube_r255", CubeListBuilder.create().texOffs(14, 4).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5528F, 0.024F, -1.836F, 0.1745F, 0.0F, 1.4399F));

		PartDefinition cube_r256 = nbs8.addOrReplaceChild("cube_r256", CubeListBuilder.create().texOffs(14, 8).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.13F, 0.5005F, -1.836F, 0.1745F, 0.0F, 0.7854F));

		PartDefinition cube_r257 = nbs8.addOrReplaceChild("cube_r257", CubeListBuilder.create().texOffs(14, 12).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1119F, 0.5163F, -1.836F, 0.1745F, 0.0F, 0.6545F));

		PartDefinition cube_r258 = nbs8.addOrReplaceChild("cube_r258", CubeListBuilder.create().texOffs(16, 0).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0919F, 0.5297F, -1.836F, 0.1745F, 0.0F, 0.5236F));

		PartDefinition cube_r259 = nbs8.addOrReplaceChild("cube_r259", CubeListBuilder.create().texOffs(16, 4).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0704F, 0.5403F, -1.836F, 0.1745F, 0.0F, 0.3927F));

		PartDefinition cube_r260 = nbs8.addOrReplaceChild("cube_r260", CubeListBuilder.create().texOffs(16, 8).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0476F, 0.5481F, -1.836F, 0.1745F, 0.0F, 0.2618F));

		PartDefinition cube_r261 = nbs8.addOrReplaceChild("cube_r261", CubeListBuilder.create().texOffs(16, 12).addBox(-0.5F, 8.0F, 4.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.024F, 0.5528F, -1.836F, 0.1745F, 0.0F, 0.1309F));

		PartDefinition cube_r262 = nbs8.addOrReplaceChild("cube_r262", CubeListBuilder.create().texOffs(14, 16).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0334F, -0.0444F, -0.6972F, 0.0873F, 0.0F, 1.4399F));

		PartDefinition cube_r263 = nbs8.addOrReplaceChild("cube_r263", CubeListBuilder.create().texOffs(16, 16).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.042F, -0.088F, -0.6972F, 0.0873F, 0.0F, 1.309F));

		PartDefinition cube_r264 = nbs8.addOrReplaceChild("cube_r264", CubeListBuilder.create().texOffs(6, 17).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0563F, -0.1301F, -0.6972F, 0.0873F, 0.0F, 1.1781F));

		PartDefinition cube_r265 = nbs8.addOrReplaceChild("cube_r265", CubeListBuilder.create().texOffs(8, 17).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.076F, -0.17F, -0.6972F, 0.0873F, 0.0F, 1.0472F));

		PartDefinition cube_r266 = nbs8.addOrReplaceChild("cube_r266", CubeListBuilder.create().texOffs(10, 17).addBox(-0.5F, 8.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1007F, -0.207F, -0.6972F, 0.0873F, 0.0F, 0.9163F));

		PartDefinition cube_r267 = nbs8.addOrReplaceChild("cube_r267", CubeListBuilder.create().texOffs(12, 17).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0501F, -0.0099F, -0.1991F, 0.0873F, 0.0F, 0.1309F));

		PartDefinition cube_r268 = nbs8.addOrReplaceChild("cube_r268", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0993F, -0.0001F, -0.1991F, 0.0873F, 0.0F, 0.2618F));

		PartDefinition cube_r269 = nbs8.addOrReplaceChild("cube_r269", CubeListBuilder.create().texOffs(18, 0).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1468F, 0.0161F, -0.1991F, 0.0873F, 0.0F, 0.3927F));

		PartDefinition cube_r270 = nbs8.addOrReplaceChild("cube_r270", CubeListBuilder.create().texOffs(2, 18).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1918F, 0.0383F, -0.1991F, 0.0873F, 0.0F, 0.5236F));

		PartDefinition cube_r271 = nbs8.addOrReplaceChild("cube_r271", CubeListBuilder.create().texOffs(4, 18).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2335F, 0.0661F, -0.1991F, 0.0873F, 0.0F, 0.6545F));

		PartDefinition cube_r272 = nbs8.addOrReplaceChild("cube_r272", CubeListBuilder.create().texOffs(18, 4).addBox(-0.5F, 8.0F, -0.5F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2713F, 0.0992F, -0.1991F, 0.0873F, 0.0F, 0.7854F));

		PartDefinition cockpit = body.addOrReplaceChild("cockpit", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wingleft = body.addOrReplaceChild("wingleft", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bottom = wingleft.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, 0.2F, -22.0F, 1.0F, 0.0F, 45.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-10.0F, 0.2F, -21.5F, 1.0F, 0.0F, 43.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-11.0F, 0.2F, -21.5F, 1.0F, 0.0F, 41.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-12.0F, 0.2F, -21.3F, 1.0F, 0.0F, 39.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-13.0F, 0.2F, -20.8F, 1.0F, 0.0F, 36.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-14.0F, 0.2F, -20.8F, 1.0F, 0.0F, 34.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-15.0F, 0.2F, -20.8F, 1.0F, 0.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-16.0F, 0.2F, -20.3F, 1.0F, 0.0F, 30.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r273 = bottom.addOrReplaceChild("cube_r273", CubeListBuilder.create().texOffs(0, 48).addBox(-39.0F, 0.6F, -0.5F, 40.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-46.0F, -0.3F, -15.0F, 0.3491F, -2.9671F, 0.0F));

		PartDefinition cube_r274 = bottom.addOrReplaceChild("cube_r274", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 0.0F, -2.0F, 8.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-48.0F, -0.3F, -8.0F, 0.3491F, -1.5708F, 0.0F));

		PartDefinition cube_r275 = bottom.addOrReplaceChild("cube_r275", CubeListBuilder.create().texOffs(0, 49).addBox(-34.0F, 0.3F, 0.0F, 34.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.0F, 0.0F, 8.0F, 0.3491F, -0.48F, 0.0F));

		PartDefinition cube_r276 = bottom.addOrReplaceChild("cube_r276", CubeListBuilder.create().texOffs(0, 36).addBox(-17.0F, 0.3F, 0.0F, 18.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 0.0F, 23.0F, 0.3491F, -1.0908F, 0.0F));

		PartDefinition belly = body.addOrReplaceChild("belly", CubeListBuilder.create().texOffs(24, 0).addBox(-0.5F, 8.0F, -24.0F, 1.0F, 0.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left = belly.addOrReplaceChild("left", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r277 = left.addOrReplaceChild("cube_r277", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 8.0F, -24.0F, 1.0F, 0.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1086F, -0.2621F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r278 = left.addOrReplaceChild("cube_r278", CubeListBuilder.create().texOffs(2, 0).addBox(-0.5F, 8.0F, -24.0F, 1.0F, 0.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition cube_r279 = left.addOrReplaceChild("cube_r279", CubeListBuilder.create().texOffs(4, 0).addBox(-0.5F, 8.0F, -24.0F, 1.0F, 0.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0032F, -0.0484F, 0.0F, 0.0F, 0.0F, 1.4399F));

		PartDefinition cube_r280 = left.addOrReplaceChild("cube_r280", CubeListBuilder.create().texOffs(6, 0).addBox(-0.5F, 8.0F, -24.0F, 1.0F, 0.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0126F, -0.0959F, 0.0F, 0.0F, 0.0F, 1.309F));

		PartDefinition cube_r281 = left.addOrReplaceChild("cube_r281", CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 8.0F, -24.0F, 1.0F, 0.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0282F, -0.1418F, 0.0F, 0.0F, 0.0F, 1.1781F));

		PartDefinition cube_r282 = left.addOrReplaceChild("cube_r282", CubeListBuilder.create().texOffs(10, 0).addBox(-0.5F, 8.0F, -24.0F, 1.0F, 0.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0496F, -0.1852F, 0.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition cube_r283 = left.addOrReplaceChild("cube_r283", CubeListBuilder.create().texOffs(12, 0).addBox(-0.5F, 8.0F, -24.0F, 1.0F, 0.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0766F, -0.2255F, 0.0F, 0.0F, 0.0F, 0.9163F));

		PartDefinition cube_r284 = left.addOrReplaceChild("cube_r284", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, 8.0F, -24.0F, 1.0F, 0.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.262F, 0.1085F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r285 = left.addOrReplaceChild("cube_r285", CubeListBuilder.create().texOffs(16, 0).addBox(-0.5F, 8.0F, -24.0F, 1.0F, 0.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2255F, 0.0766F, 0.0F, 0.0F, 0.0F, 0.6545F));

		PartDefinition cube_r286 = left.addOrReplaceChild("cube_r286", CubeListBuilder.create().texOffs(18, 0).addBox(-0.5F, 8.0F, -24.0F, 1.0F, 0.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1852F, 0.0496F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r287 = left.addOrReplaceChild("cube_r287", CubeListBuilder.create().texOffs(20, 0).addBox(-0.5F, 8.0F, -24.0F, 1.0F, 0.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1418F, 0.0282F, 0.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r288 = left.addOrReplaceChild("cube_r288", CubeListBuilder.create().texOffs(22, 0).addBox(-0.5F, 8.0F, -24.0F, 1.0F, 0.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0959F, 0.0126F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition cube_r289 = left.addOrReplaceChild("cube_r289", CubeListBuilder.create().texOffs(26, 0).addBox(0.0485F, 0.0032F, -48.0F, 1.0F, 0.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5392F, 7.86F, 24.0F, 0.0F, 0.0F, 0.1309F));

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