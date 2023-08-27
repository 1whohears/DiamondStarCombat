// Made with Blockbench 4.8.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class javi_plane<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "javi_plane"), "main");
	private final ModelPart fuselage;
	private final ModelPart seat;
	private final ModelPart gear;
	private final ModelPart hexadecagon;

	public javi_plane(ModelPart root) {
		this.fuselage = root.getChild("fuselage");
		this.seat = root.getChild("seat");
		this.gear = root.getChild("gear");
		this.hexadecagon = root.getChild("hexadecagon");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition fuselage = partdefinition.addOrReplaceChild("fuselage", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = fuselage.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 105).addBox(-9.0F, -2.1375F, -31.5583F, 18.0F, 16.0F, 51.0F, new CubeDeformation(0.0F))
		.texOffs(93, 16).addBox(9.0F, -2.1375F, -31.5583F, 1.5663F, 16.0F, 89.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-10.5663F, -2.1375F, -31.5583F, 1.5663F, 16.0F, 89.0F, new CubeDeformation(0.0F))
		.texOffs(186, 62).addBox(-9.0F, 12.8625F, 19.4417F, 18.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(0, 247).addBox(8.0F, -2.1375F, 19.4417F, 1.0F, 15.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(93, 14).addBox(-9.0F, -2.1375F, 19.4417F, 1.0F, 15.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(279, 146).addBox(-8.0F, -2.1375F, 34.4417F, 16.0F, 15.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 105).addBox(-9.0F, -2.1375F, 51.4417F, 18.0F, 16.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(93, 31).addBox(-7.0249F, 1.5091F, 75.8084F, 14.0F, 8.7068F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(256, 207).addBox(-0.5F, -0.5F, -8.5F, 11.0F, 9.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.6354F, -0.1691F, 65.513F, -0.1745F, 0.1745F, 0.0F));

		PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(121, 260).addBox(-10.5F, -0.5F, -8.5F, 11.0F, 9.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.6354F, -0.1691F, 65.513F, -0.1745F, -0.1745F, 0.0F));

		PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(256, 177).addBox(-10.5F, -8.5F, -8.5F, 11.0F, 9.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.6354F, 11.8941F, 65.513F, 0.1745F, -0.1745F, 0.0F));

		PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(164, 279).addBox(-0.5F, -8.5F, -8.5F, 11.0F, 9.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.6354F, 11.8941F, 65.513F, 0.1745F, 0.1745F, 0.0F));

		PartDefinition cube_r5 = body.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(200, 195).addBox(-3.5663F, 2.0F, -64.0F, 4.5663F, 7.5232F, 45.7606F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.396F, 5.9652F, -12.5328F, -0.0873F, 0.0436F, 0.0F));

		PartDefinition cube_r6 = body.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(241, 75).addBox(-1.0F, 2.0F, -64.0F, 4.5663F, 7.5232F, 45.7606F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.396F, 5.9652F, -12.5328F, -0.0873F, -0.0436F, 0.0F));

		PartDefinition cube_r7 = body.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 191).addBox(9.0F, -31.0F, -4.0F, 1.0F, 25.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(87, 121).addBox(-9.0F, -31.0F, 7.0F, 18.0F, 25.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(172, 121).addBox(-9.0F, -31.0F, -4.0F, 1.0F, 25.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.6505F, 53.6699F, 1.8326F, 0.0F, 0.0F));

		PartDefinition cube_r8 = body.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(125, 141).addBox(9.0F, -3.0F, -1.5F, 1.0F, 9.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(186, 95).addBox(-9.0F, -3.0F, 2.5F, 18.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(125, 127).addBox(-9.0F, -3.0F, -1.5F, 1.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5663F, 20.1826F, 2.3998F, 0.0F, 0.0F));

		PartDefinition cube_r9 = body.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(72, 53).addBox(-9.0F, -9.0F, -8.0F, 1.0F, 16.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(74, 11).addBox(9.0F, -9.0F, -8.0F, 1.0F, 16.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(207, 279).addBox(-9.0F, -9.0F, -4.0F, 18.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.1733F, 53.4234F, 0.6109F, 0.0F, 0.0F));

		PartDefinition nose = fuselage.addOrReplaceChild("nose", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 14.0F));

		PartDefinition wing = fuselage.addOrReplaceChild("wing", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left = wing.addOrReplaceChild("left", CubeListBuilder.create().texOffs(186, 0).addBox(8.465F, 5.1518F, -27.9583F, 36.0F, 2.4F, 28.8F, new CubeDeformation(0.0F))
		.texOffs(186, 31).addBox(-44.465F, 5.1518F, -27.9583F, 36.0F, 2.4F, 28.8F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r10 = left.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(9, 211).addBox(-0.525F, -8.075F, -7.35F, 1.05F, 6.25F, 16.8F, new CubeDeformation(0.0F))
		.texOffs(181, 141).addBox(63.525F, -8.075F, -7.35F, 1.05F, 6.25F, 16.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-32.025F, 8.1321F, -68.3387F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r11 = left.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(172, 249).addBox(-3.2F, 10.8F, -13.9F, 6.4F, 2.4F, 27.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-45.2834F, -5.7702F, -13.8323F, 0.0F, 0.0873F, 0.0873F));

		PartDefinition cube_r12 = left.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(74, 251).addBox(24.9218F, -1.8F, -18.2F, 6.4F, 2.4F, 27.8F, new CubeDeformation(0.0F))
		.texOffs(93, 7).addBox(-25.2F, -1.8F, 4.8F, 56.4F, 2.4F, 4.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-74.6468F, 4.3136F, -11.4411F, 0.0F, -0.0873F, 0.0873F));

		PartDefinition cube_r13 = left.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(59, 42).addBox(20.8F, -1.8F, -9.6F, 4.4F, 2.4F, 8.8F, new CubeDeformation(0.0F))
		.texOffs(127, 14).addBox(-25.2F, -1.8F, -9.6F, 21.4F, 2.4F, 5.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-74.6468F, 4.3136F, -15.6756F, 0.0F, 0.0873F, 0.0873F));

		PartDefinition cube_r14 = left.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(299, 208).addBox(-1.8F, -1.8F, -4.8F, 3.6F, 2.4F, 14.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-100.5975F, 2.0432F, -13.725F, 0.0F, 0.1745F, 0.0873F));

		PartDefinition cube_r15 = left.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(100, 233).addBox(-58.8F, -2.4F, -14.6188F, 3.5005F, 2.4F, 5.0188F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-44.6698F, 7.5385F, -8.5396F, 0.0F, 0.0F, 0.0873F));

		PartDefinition cube_r16 = left.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(93, 0).addBox(-31.2F, -1.8F, 4.8F, 56.4F, 2.4F, 4.8F, new CubeDeformation(0.0F))
		.texOffs(240, 249).addBox(-31.3218F, -1.8F, -18.2F, 6.4F, 2.4F, 27.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(74.6468F, 4.3136F, -11.4411F, 0.0F, 0.0873F, -0.0873F));

		PartDefinition cube_r17 = left.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(127, 41).addBox(-10.4099F, -0.432F, -5.3165F, 22.4F, 2.4F, 2.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-66.4801F, 3.6548F, -20.5189F, 0.0F, 0.0873F, 0.0873F));

		PartDefinition cube_r18 = left.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(164, 251).addBox(-1.8F, -1.8F, -4.8F, 3.6F, 2.4F, 14.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(100.5975F, 2.0432F, -13.725F, 0.0F, -0.1745F, -0.0873F));

		PartDefinition cube_r19 = left.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(186, 87).addBox(55.2995F, -2.4F, -14.6188F, 3.5005F, 2.4F, 5.0188F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(44.6698F, 7.5385F, -8.5396F, 0.0F, 0.0F, -0.0873F));

		PartDefinition cube_r20 = left.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(34, 247).addBox(-3.2F, 10.8F, -13.9F, 6.4F, 2.4F, 27.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(45.2834F, -5.7702F, -13.8323F, 0.0F, -0.0873F, -0.0873F));

		PartDefinition cube_r21 = left.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(0, 173).addBox(0.0F, -2.4F, -11.4F, 56.4F, 2.4F, 16.2F, new CubeDeformation(0.0F))
		.texOffs(200, 150).addBox(33.0F, -2.4F, -16.1495F, 1.8668F, 2.4F, 4.9495F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(44.5696F, 7.5473F, -8.7583F, 0.0F, 0.0F, -0.0873F));

		PartDefinition cube_r22 = left.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(93, 78).addBox(3.8F, -1.8F, -9.6F, 21.4F, 2.4F, 5.8F, new CubeDeformation(0.0F))
		.texOffs(60, 0).addBox(-25.2F, -1.8F, -9.6F, 4.4F, 2.4F, 8.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(74.6468F, 4.3136F, -15.6756F, 0.0F, -0.0873F, -0.0873F));

		PartDefinition cube_r23 = left.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(204, 72).addBox(-34.8668F, -2.4F, -16.1495F, 1.8668F, 2.4F, 4.9495F, new CubeDeformation(0.0F))
		.texOffs(128, 177).addBox(-56.4F, -2.4F, -11.4F, 56.4F, 2.4F, 16.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-44.5696F, 7.5473F, -8.7583F, 0.0F, 0.0F, 0.0873F));

		PartDefinition cube_r24 = left.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(250, 249).addBox(33.0F, -2.4F, -16.1495F, 1.8668F, 2.4F, 6.9495F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(20.1565F, 9.6832F, -10.9024F, 0.0F, 0.0F, -0.0873F));

		PartDefinition cube_r25 = left.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(45, 2).addBox(-11.8059F, -0.432F, -6.3413F, 0.3248F, 2.4F, 6.1413F, new CubeDeformation(0.0F))
		.texOffs(125, 121).addBox(-11.8059F, -0.432F, -4.0001F, 22.6396F, 2.4F, 4.344F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(66.4801F, 3.6548F, -20.5189F, 0.0F, 0.0F, -0.0873F));

		PartDefinition cube_r26 = left.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(0, 84).addBox(-11.9901F, -0.432F, -5.3165F, 22.4F, 2.4F, 2.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(66.4801F, 3.6548F, -20.5189F, 0.0F, -0.0873F, -0.0873F));

		PartDefinition cube_r27 = left.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(45, 4).addBox(11.4811F, -0.432F, -6.3413F, 0.3248F, 2.4F, 6.1413F, new CubeDeformation(0.0F))
		.texOffs(127, 35).addBox(-10.8337F, -0.432F, -4.0001F, 22.6396F, 2.4F, 4.344F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-66.4801F, 3.6548F, -20.5189F, 0.0F, 0.0F, 0.0873F));

		PartDefinition cube_r28 = left.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(136, 251).addBox(-34.8668F, -2.4F, -16.1495F, 1.8668F, 2.4F, 6.9495F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-20.1565F, 9.6832F, -10.9024F, 0.0F, 0.0F, 0.0873F));

		PartDefinition right = wing.addOrReplaceChild("right", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail = fuselage.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(93, 127).addBox(-8.6021F, -2.1375F, -76.5155F, 17.18F, 1.0F, 44.9572F, new CubeDeformation(0.0F))
		.texOffs(0, 42).addBox(-32.55F, -20.5375F, -70.6083F, 1.05F, 26.25F, 13.8F, new CubeDeformation(0.0F))
		.texOffs(30, 52).addBox(-32.55F, -13.9613F, -75.0F, 1.05F, 13.4086F, 4.35F, new CubeDeformation(0.0F))
		.texOffs(30, 10).addBox(31.5F, -13.9613F, -75.0F, 1.05F, 13.4086F, 4.35F, new CubeDeformation(0.0F))
		.texOffs(28, 35).addBox(-32.55F, -14.3113F, -74.1531F, 1.05F, 0.35F, 3.8F, new CubeDeformation(0.0F))
		.texOffs(26, 35).addBox(-32.55F, -20.5375F, -73.6083F, 1.05F, 0.35F, 3.8F, new CubeDeformation(0.0F))
		.texOffs(76, 42).addBox(31.5F, -0.5527F, -74.957F, 1.05F, 1.25F, 4.8F, new CubeDeformation(0.0F))
		.texOffs(32, 8).addBox(31.5F, -14.3113F, -74.1531F, 1.05F, 0.35F, 3.8F, new CubeDeformation(0.0F))
		.texOffs(34, 8).addBox(31.5F, -20.5375F, -73.6083F, 1.05F, 0.35F, 3.8F, new CubeDeformation(0.0F))
		.texOffs(77, 0).addBox(-32.55F, -0.5527F, -74.957F, 1.05F, 1.25F, 4.8F, new CubeDeformation(0.0F))
		.texOffs(241, 129).addBox(-31.5F, 1.5125F, -72.5583F, 25.2F, 1.05F, 15.75F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(31.5F, -20.5375F, -70.6083F, 1.05F, 26.25F, 13.8F, new CubeDeformation(0.0F))
		.texOffs(155, 212).addBox(6.3F, 1.5125F, -72.5583F, 25.2F, 1.05F, 15.75F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r29 = tail.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(44, 42).addBox(-0.525F, -12.075F, -3.35F, 1.05F, 25.2F, 12.8F, new CubeDeformation(0.0F))
		.texOffs(30, 35).addBox(-0.525F, 13.125F, 5.25F, 1.05F, 1.05F, 4.2F, new CubeDeformation(0.0F))
		.texOffs(44, 0).addBox(-64.575F, -12.075F, -4.35F, 1.05F, 25.2F, 13.8F, new CubeDeformation(0.0F))
		.texOffs(44, 50).addBox(-64.575F, 13.125F, 5.25F, 1.05F, 1.05F, 4.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(32.025F, -7.6848F, -65.17F, 0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r30 = tail.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(60, 4).addBox(-0.525F, 2.625F, -1.575F, 1.05F, 3.15F, 2.1F, new CubeDeformation(0.0F))
		.texOffs(81, 11).addBox(-64.575F, 2.625F, -1.575F, 1.05F, 3.15F, 2.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(32.025F, 3.2105F, -53.339F, -0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r31 = tail.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(230, 254).addBox(-0.525F, -1.05F, -9.45F, 1.05F, 2.1F, 17.85F, new CubeDeformation(0.0F))
		.texOffs(259, 151).addBox(-64.575F, -1.05F, -9.45F, 1.05F, 2.1F, 17.85F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(32.025F, 5.9742F, -64.6292F, -0.1309F, 0.0F, 0.0F));

		PartDefinition cube_r32 = tail.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(241, 146).addBox(-0.525F, -12.075F, -7.35F, 1.05F, 6.25F, 16.8F, new CubeDeformation(0.0F))
		.texOffs(212, 249).addBox(-64.575F, -12.075F, -7.35F, 1.05F, 6.25F, 16.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(32.025F, -7.8679F, -67.3387F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r33 = tail.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(55, 191).addBox(-12.6F, -0.525F, -7.875F, 26.25F, 1.05F, 15.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.7239F, 2.0375F, -66.9909F, 0.0F, 0.1745F, 0.0F));

		PartDefinition cube_r34 = tail.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(55, 208).addBox(-13.65F, -0.525F, -7.875F, 26.25F, 1.05F, 15.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.7239F, 2.0375F, -66.9909F, 0.0F, -0.1745F, 0.0F));

		PartDefinition cube_r35 = tail.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(171, 127).addBox(-6.0F, -6.0F, -11.75F, 12.0F, 4.0F, 45.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.8625F, -65.5583F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r36 = tail.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(0, 140).addBox(-8.0256F, -7.6002F, -1.0F, 17.2069F, 11.6002F, 3.1718F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5779F, 5.7526F, -76.9909F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r37 = tail.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(0, 191).addBox(-2.0F, -8.1027F, -64.0F, 4.5663F, 11.0F, 45.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.397F, 5.9652F, -12.4892F, 0.0F, -0.0436F, 0.0F));

		PartDefinition cube_r38 = tail.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(100, 195).addBox(-2.5663F, -8.1027F, -64.0F, 4.5663F, 11.0F, 45.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.397F, 5.9652F, -12.4892F, 0.0F, 0.0436F, 0.0F));

		PartDefinition engine = fuselage.addOrReplaceChild("engine", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition eleft = engine.addOrReplaceChild("eleft", CubeListBuilder.create(), PartPose.offsetAndRotation(-9.0F, -10.0F, -24.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition eright = engine.addOrReplaceChild("eright", CubeListBuilder.create(), PartPose.offsetAndRotation(9.0F, -10.0F, -24.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition seat = partdefinition.addOrReplaceChild("seat", CubeListBuilder.create().texOffs(127, 22).addBox(-6.0F, 11.8625F, 20.4417F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(186, 31).addBox(-6.0F, -3.1375F, 20.4417F, 12.0F, 15.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 127).addBox(-6.0F, 11.8625F, 36.4417F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(93, 14).addBox(-6.0F, -3.1375F, 36.4417F, 12.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, -1.0F));

		PartDefinition stick = seat.addOrReplaceChild("stick", CubeListBuilder.create().texOffs(52, 42).addBox(-0.5F, -0.1375F, -5.0583F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(60, 0).addBox(-1.0F, -2.1375F, -5.5583F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 54.5F));

		PartDefinition window = seat.addOrReplaceChild("window", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, 25.0F));

		PartDefinition gear = partdefinition.addOrReplaceChild("gear", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition gfront = gear.addOrReplaceChild("gfront", CubeListBuilder.create().texOffs(59, 42).addBox(-0.5F, 7.8625F, -5.0583F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(17, 250).addBox(-2.5F, 11.8625F, -7.0583F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(231, 249).addBox(0.5F, 11.8625F, -7.0583F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 42.5F));

		PartDefinition gleft = gear.addOrReplaceChild("gleft", CubeListBuilder.create().texOffs(87, 105).addBox(-0.5F, 7.8625F, -12.0583F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(212, 249).addBox(-2.5F, 17.8625F, -14.0583F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(241, 146).addBox(0.5F, 17.8625F, -14.0583F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-31.5F, 0.0F, -3.5F));

		PartDefinition gright = gear.addOrReplaceChild("gright", CubeListBuilder.create().texOffs(84, 52).addBox(-0.5F, 7.8625F, -12.0583F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(204, 62).addBox(-2.5F, 17.8625F, -14.0583F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(36, 127).addBox(0.5F, 17.8625F, -14.0583F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(30.5F, 0.0F, -3.5F));

		PartDefinition cube_r39 = gright.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(84, 67).addBox(-31.0F, -2.3114F, 2.7663F, 1.0F, 13.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(121, 14).addBox(31.0F, -2.3114F, 2.7663F, 1.0F, 13.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-31.5F, 6.8625F, -11.0583F, -0.3491F, 0.0F, 0.0F));

		PartDefinition hexadecagon = partdefinition.addOrReplaceChild("hexadecagon", CubeListBuilder.create().texOffs(271, 321).addBox(12.7103F, -4.6745F, -17.1479F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(322, 1).addBox(31.0401F, -4.6745F, -17.1479F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(280, 237).addBox(20.5766F, -12.5408F, -17.1479F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(287, 146).addBox(20.5766F, 5.7891F, -17.1479F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(236, 308).addBox(-12.4831F, 5.7891F, -17.1479F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(298, 265).addBox(-12.4831F, -12.5408F, -17.1479F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(29, 333).addBox(-2.0196F, -4.6745F, -17.1479F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(329, 321).addBox(-20.3494F, -4.6745F, -17.1479F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 16.0F, -42.0F));

		PartDefinition hexadecagon_r1 = hexadecagon.addOrReplaceChild("hexadecagon_r1", CubeListBuilder.create().texOffs(0, 172).addBox(-16.572F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(47, 77).addBox(1.0652F, -0.9183F, -4.8983F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(54, 2).addBox(-12.5247F, -0.9396F, -27.1521F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, 0.0F, -0.3491F, 0.3927F));

		PartDefinition hexadecagon_r2 = hexadecagon.addOrReplaceChild("hexadecagon_r2", CubeListBuilder.create().texOffs(224, 95).addBox(-8.8196F, -3.9532F, -1.4742F, 17.3093F, 7.9065F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(300, 325).addBox(-9.8196F, -1.9532F, -21.4742F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(332, 63).addBox(8.5103F, -1.9532F, -21.4742F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(16, 42).addBox(-3.9532F, -6.8196F, -27.5258F, 7.9065F, 13.3093F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(186, 14).addBox(-6.8196F, -3.9532F, -27.4742F, 13.3093F, 7.9065F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(297, 59).addBox(-1.9532F, -9.8196F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(186, 62).addBox(-3.9532F, -8.8196F, -1.5258F, 7.9065F, 17.3093F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(201, 307).addBox(-1.9532F, 8.5103F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, 0.0F, 0.0F, 0.3927F));

		PartDefinition hexadecagon_r3 = hexadecagon.addOrReplaceChild("hexadecagon_r3", CubeListBuilder.create().texOffs(163, 22).addBox(-16.572F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(44, 73).addBox(1.1754F, -0.9408F, -4.9384F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(36, 52).addBox(-12.4106F, -0.9408F, -27.1936F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, 0.0F, -0.3491F, 0.0F));

		PartDefinition hexadecagon_r4 = hexadecagon.addOrReplaceChild("hexadecagon_r4", CubeListBuilder.create().texOffs(200, 139).addBox(-16.572F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(63, 77).addBox(1.2853F, -0.9167F, -4.9784F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(54, 4).addBox(-12.3046F, -0.8954F, -27.2322F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, 0.0F, -0.3491F, -0.3927F));

		PartDefinition hexadecagon_r5 = hexadecagon.addOrReplaceChild("hexadecagon_r5", CubeListBuilder.create().texOffs(332, 32).addBox(-9.8196F, -1.9532F, -21.4742F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(186, 80).addBox(-6.4897F, -2.9532F, -27.5258F, 13.3093F, 5.9065F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 42).addBox(-2.9532F, -6.8196F, -27.4742F, 5.9065F, 13.3093F, 0.9484F, new CubeDeformation(0.0F))
		.texOffs(55, 225).addBox(-8.4897F, -2.9532F, -1.5258F, 17.3093F, 5.9065F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(333, 241).addBox(8.5103F, -1.9532F, -21.4742F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(198, 121).addBox(-2.9532F, -8.8196F, -1.4742F, 5.9065F, 17.3093F, 0.9484F, new CubeDeformation(0.0F))
		.texOffs(298, 293).addBox(-1.9532F, -9.8196F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(70, 309).addBox(-1.9532F, 8.5103F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, 0.0F, 0.0F, -0.3927F));

		PartDefinition hexadecagon_r6 = hexadecagon.addOrReplaceChild("hexadecagon_r6", CubeListBuilder.create().texOffs(28, 209).addBox(15.2627F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(59, 46).addBox(-1.7072F, -0.9183F, -4.9771F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(44, 51).addBox(11.9792F, -0.9396F, -27.1957F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, 0.0F, 0.3491F, 0.3927F));

		PartDefinition hexadecagon_r7 = hexadecagon.addOrReplaceChild("hexadecagon_r7", CubeListBuilder.create().texOffs(155, 195).addBox(15.2627F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(44, 72).addBox(-1.5971F, -0.9408F, -4.937F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(54, 0).addBox(12.0934F, -0.9408F, -27.1542F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, 0.0F, 0.3491F, 0.0F));

		PartDefinition hexadecagon_r8 = hexadecagon.addOrReplaceChild("hexadecagon_r8", CubeListBuilder.create().texOffs(241, 129).addBox(15.2627F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(60, 3).addBox(-1.4872F, -0.9167F, -4.897F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(44, 50).addBox(12.1993F, -0.8954F, -27.1156F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, 0.0F, 0.3491F, -0.3927F));

		PartDefinition hexadecagon_r9 = hexadecagon.addOrReplaceChild("hexadecagon_r9", CubeListBuilder.create().texOffs(177, 229).addBox(-1.9532F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(28, 27).addBox(-0.8497F, 0.9688F, -4.8632F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(42, 12).addBox(-0.8104F, -12.7064F, -27.0859F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, 0.3491F, 0.0F, 0.7854F));

		PartDefinition hexadecagon_r10 = hexadecagon.addOrReplaceChild("hexadecagon_r10", CubeListBuilder.create().texOffs(297, 87).addBox(-1.9532F, -9.8196F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(35, 305).addBox(-1.9532F, 8.5103F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, 0.0F, 0.0F, 0.7854F));

		PartDefinition hexadecagon_r11 = hexadecagon.addOrReplaceChild("hexadecagon_r11", CubeListBuilder.create().texOffs(155, 229).addBox(-1.9532F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(42, 0).addBox(-0.75F, 0.9071F, -4.8408F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(46, 12).addBox(-0.6986F, -12.7511F, -27.0697F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, 0.3491F, 0.0F, 0.3927F));

		PartDefinition hexadecagon_r12 = hexadecagon.addOrReplaceChild("hexadecagon_r12", CubeListBuilder.create().texOffs(30, 27).addBox(-0.6327F, -1.8865F, -5.0424F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(36, 35).addBox(-0.5772F, 11.7517F, -27.2785F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(114, 251).addBox(-1.9532F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(34, 247).addBox(31.1065F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(34, 35).addBox(32.3673F, 11.7517F, -27.2785F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(26, 27).addBox(32.4228F, -1.8865F, -5.0424F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, -0.3491F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r13 = hexadecagon.addOrReplaceChild("hexadecagon_r13", CubeListBuilder.create().texOffs(40, 42).addBox(-0.75F, -1.8654F, -5.0347F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(52, 0).addBox(-0.6986F, 11.7528F, -27.2781F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(123, 208).addBox(-1.9532F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, -0.3491F, 0.0F, 0.3927F));

		PartDefinition hexadecagon_r14 = hexadecagon.addOrReplaceChild("hexadecagon_r14", CubeListBuilder.create().texOffs(36, 42).addBox(-0.8497F, -1.8037F, -5.0122F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(50, 12).addBox(-0.8104F, 11.7976F, -27.2618F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(223, 195).addBox(-1.9532F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, -0.3491F, 0.0F, 0.7854F));

		PartDefinition hexadecagon_r15 = hexadecagon.addOrReplaceChild("hexadecagon_r15", CubeListBuilder.create().texOffs(30, 42).addBox(-0.417F, 0.9715F, -4.8642F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(42, 50).addBox(-0.3777F, -12.6297F, -27.1138F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(123, 216).addBox(-1.9532F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, 0.3491F, 0.0F, -0.7854F));

		PartDefinition hexadecagon_r16 = hexadecagon.addOrReplaceChild("hexadecagon_r16", CubeListBuilder.create().texOffs(44, 0).addBox(-0.5158F, 0.9086F, -4.8413F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(48, 50).addBox(-0.4644F, -12.7096F, -27.0848F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(221, 212).addBox(-1.9532F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, 0.3491F, 0.0F, -0.3927F));

		PartDefinition hexadecagon_r17 = hexadecagon.addOrReplaceChild("hexadecagon_r17", CubeListBuilder.create().texOffs(30, 0).addBox(-0.6327F, 0.8859F, -4.8331F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(34, 37).addBox(-0.5772F, -12.7523F, -27.0692F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(221, 229).addBox(-1.9532F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(121, 226).addBox(31.1065F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(36, 12).addBox(32.3673F, -12.7523F, -27.0692F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(24, 27).addBox(32.4228F, 0.8859F, -4.8331F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, 0.3491F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r18 = hexadecagon.addOrReplaceChild("hexadecagon_r18", CubeListBuilder.create().texOffs(28, 0).addBox(-0.417F, -1.8009F, -5.0112F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(34, 12).addBox(-0.3777F, 11.8742F, -27.234F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(199, 229).addBox(-1.9532F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, -0.3491F, 0.0F, -0.7854F));

		PartDefinition hexadecagon_r19 = hexadecagon.addOrReplaceChild("hexadecagon_r19", CubeListBuilder.create().texOffs(40, 0).addBox(-0.5158F, -1.8639F, -5.0341F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F))
		.texOffs(34, 50).addBox(-0.4644F, 11.7943F, -27.263F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(74, 247).addBox(-1.9532F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, -0.3491F, 0.0F, -0.3927F));

		PartDefinition hexadecagon_r20 = hexadecagon.addOrReplaceChild("hexadecagon_r20", CubeListBuilder.create().texOffs(299, 180).addBox(-1.9532F, -9.8196F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(0, 42).addBox(-2.9532F, 9.5103F, -26.4742F, 1.9065F, 2.3093F, 39.9484F, new CubeDeformation(0.0F))
		.texOffs(144, 309).addBox(-1.9532F, 8.5103F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, 0.0F, 0.0F, -0.7854F));

		PartDefinition hexadecagon_r21 = hexadecagon.addOrReplaceChild("hexadecagon_r21", CubeListBuilder.create().texOffs(93, 72).addBox(-2.9532F, 25.4449F, -11.9953F, 1.9065F, 2.3093F, 2.9484F, new CubeDeformation(0.0F))
		.texOffs(30, 74).addBox(-2.9532F, -2.8029F, 16.2525F, 1.9065F, 2.3093F, 2.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5298F, -2.7212F, 4.3263F, -0.7854F, 0.0F, -0.7854F));

		PartDefinition hexadecagon_r22 = hexadecagon.addOrReplaceChild("hexadecagon_r22", CubeListBuilder.create().texOffs(0, 294).addBox(-1.9532F, 8.5103F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(74, 281).addBox(-1.9532F, -9.8196F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(1.0468F, 9.5103F, -26.4742F, 1.9065F, 2.3093F, 39.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, 0.0F, 0.0F, 0.7854F));

		PartDefinition hexadecagon_r23 = hexadecagon.addOrReplaceChild("hexadecagon_r23", CubeListBuilder.create().texOffs(109, 290).addBox(-1.9532F, 8.5103F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(263, 280).addBox(-1.9532F, -9.8196F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(322, 119).addBox(8.5103F, -1.9532F, -21.4742F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(26, 191).addBox(-2.9532F, -8.8196F, -1.4742F, 5.9065F, 17.3093F, 0.9484F, new CubeDeformation(0.0F))
		.texOffs(172, 164).addBox(-8.8196F, -2.9532F, -1.5258F, 17.3093F, 5.9065F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(186, 48).addBox(-6.8196F, -2.9532F, -27.5258F, 13.3093F, 5.9065F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.9532F, -6.8196F, -27.4742F, 5.9065F, 13.3093F, 0.9484F, new CubeDeformation(0.0F))
		.texOffs(315, 210).addBox(-9.8196F, -1.9532F, -21.4742F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, 0.0F, 0.0F, 0.3927F));

		PartDefinition hexadecagon_r24 = hexadecagon.addOrReplaceChild("hexadecagon_r24", CubeListBuilder.create().texOffs(287, 31).addBox(-1.9532F, 8.5103F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(161, 46).addBox(-3.9532F, -8.8196F, -1.5258F, 7.9065F, 17.3093F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 322).addBox(8.5103F, -1.9532F, -21.4742F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(87, 147).addBox(-8.4897F, -3.9532F, -1.4742F, 17.3093F, 7.9065F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(39, 277).addBox(-1.9532F, -9.8196F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(16, 0).addBox(-3.9532F, -6.8196F, -27.5258F, 7.9065F, 13.3093F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(147, 78).addBox(-6.4897F, -3.9532F, -27.4742F, 13.3093F, 7.9065F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(105, 318).addBox(-9.8196F, -1.9532F, -21.4742F, 1.3093F, 3.9065F, 26.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, 0.0F, 0.0F, -0.3927F));

		PartDefinition hexadecagon_r25 = hexadecagon.addOrReplaceChild("hexadecagon_r25", CubeListBuilder.create().texOffs(287, 0).addBox(-1.9532F, 8.5103F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F))
		.texOffs(228, 279).addBox(-1.9532F, -9.8196F, -21.4742F, 3.9065F, 1.3093F, 26.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, 0.0F, 0.0F, -0.7854F));

		PartDefinition hexadecagon_r26 = hexadecagon.addOrReplaceChild("hexadecagon_r26", CubeListBuilder.create().texOffs(123, 195).addBox(-1.9532F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(44, 50).addBox(-0.8052F, -12.7096F, -27.0848F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(42, 42).addBox(-0.7538F, 0.9086F, -4.8413F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, 0.3491F, 0.0F, 0.3927F));

		PartDefinition hexadecagon_r27 = hexadecagon.addOrReplaceChild("hexadecagon_r27", CubeListBuilder.create().texOffs(65, 74).addBox(-1.9532F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(36, 50).addBox(-0.8919F, -12.6297F, -27.1138F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(26, 42).addBox(-0.8526F, 0.9715F, -4.8642F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, 0.3491F, 0.0F, 0.7854F));

		PartDefinition hexadecagon_r28 = hexadecagon.addOrReplaceChild("hexadecagon_r28", CubeListBuilder.create().texOffs(161, 64).addBox(15.2627F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(54, 1).addBox(12.0992F, -0.9396F, -27.1521F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(47, 76).addBox(-1.4907F, -0.9183F, -4.8983F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, 0.0F, 0.3491F, -0.3927F));

		PartDefinition hexadecagon_r29 = hexadecagon.addOrReplaceChild("hexadecagon_r29", CubeListBuilder.create().texOffs(93, 61).addBox(15.2627F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(50, 51).addBox(11.985F, -0.9408F, -27.1936F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(60, 5).addBox(-1.6009F, -0.9408F, -4.9384F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, 0.0F, 0.3491F, 0.0F));

		PartDefinition hexadecagon_r30 = hexadecagon.addOrReplaceChild("hexadecagon_r30", CubeListBuilder.create().texOffs(55, 191).addBox(15.2627F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(54, 3).addBox(11.8791F, -0.8954F, -27.2322F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(63, 76).addBox(-1.7108F, -0.9167F, -4.9784F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, 0.0F, 0.3491F, 0.3927F));

		PartDefinition hexadecagon_r31 = hexadecagon.addOrReplaceChild("hexadecagon_r31", CubeListBuilder.create().texOffs(221, 220).addBox(-1.9532F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(48, 12).addBox(-0.4592F, 11.7976F, -27.2618F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(28, 42).addBox(-0.4199F, -1.8037F, -5.0122F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, -0.3491F, 0.0F, -0.7854F));

		PartDefinition hexadecagon_r32 = hexadecagon.addOrReplaceChild("hexadecagon_r32", CubeListBuilder.create().texOffs(67, 32).addBox(-1.9532F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(50, 51).addBox(-0.571F, 11.7528F, -27.2781F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(38, 42).addBox(-0.5196F, -1.8654F, -5.0347F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, -0.3491F, 0.0F, -0.3927F));

		PartDefinition hexadecagon_r33 = hexadecagon.addOrReplaceChild("hexadecagon_r33", CubeListBuilder.create().texOffs(0, 247).addBox(-1.9532F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(32, 50).addBox(-0.8052F, 11.7943F, -27.263F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(36, 0).addBox(-0.7538F, -1.8639F, -5.0341F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, -0.3491F, 0.0F, 0.3927F));

		PartDefinition hexadecagon_r34 = hexadecagon.addOrReplaceChild("hexadecagon_r34", CubeListBuilder.create().texOffs(106, 225).addBox(-1.9532F, 15.2627F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(32, 8).addBox(-0.8919F, 11.8742F, -27.234F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(26, 0).addBox(-0.8526F, -1.8009F, -5.0112F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, -0.3491F, 0.0F, 0.7854F));

		PartDefinition hexadecagon_r35 = hexadecagon.addOrReplaceChild("hexadecagon_r35", CubeListBuilder.create().texOffs(55, 208).addBox(-16.572F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(36, 51).addBox(-12.4047F, -0.9396F, -27.1957F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(44, 31).addBox(1.2817F, -0.9183F, -4.9771F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, 0.0F, -0.3491F, -0.3927F));

		PartDefinition hexadecagon_r36 = hexadecagon.addOrReplaceChild("hexadecagon_r36", CubeListBuilder.create().texOffs(128, 173).addBox(-16.572F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(36, 53).addBox(-12.5189F, -0.9408F, -27.1542F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(60, 4).addBox(1.1715F, -0.9408F, -4.937F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, 0.0F, -0.3491F, 0.0F));

		PartDefinition hexadecagon_r37 = hexadecagon.addOrReplaceChild("hexadecagon_r37", CubeListBuilder.create().texOffs(155, 212).addBox(-16.572F, -1.9532F, -23.7691F, 1.3093F, 3.9065F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(36, 50).addBox(-12.6248F, -0.8954F, -27.1156F, 0.4255F, 1.2696F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(59, 45).addBox(1.0617F, -0.9167F, -4.897F, 0.4255F, 1.2696F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, 0.0F, -0.3491F, 0.3927F));

		PartDefinition hexadecagon_r38 = hexadecagon.addOrReplaceChild("hexadecagon_r38", CubeListBuilder.create().texOffs(84, 225).addBox(-1.9532F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(36, 37).addBox(-0.4592F, -12.7064F, -27.0859F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(22, 27).addBox(-0.4199F, 0.9688F, -4.8632F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, 0.3491F, 0.0F, -0.7854F));

		PartDefinition hexadecagon_r39 = hexadecagon.addOrReplaceChild("hexadecagon_r39", CubeListBuilder.create().texOffs(223, 203).addBox(-1.9532F, -16.572F, -23.7691F, 3.9065F, 1.3093F, 6.9484F, new CubeDeformation(0.0F))
		.texOffs(44, 12).addBox(-0.571F, -12.7511F, -27.0697F, 1.2696F, 0.4255F, 2.2582F, new CubeDeformation(0.0F))
		.texOffs(38, 0).addBox(-0.5196F, 0.9071F, -4.8408F, 1.2696F, 0.4255F, 8.2582F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, 0.3491F, 0.0F, -0.3927F));

		PartDefinition hexadecagon_r40 = hexadecagon.addOrReplaceChild("hexadecagon_r40", CubeListBuilder.create().texOffs(30, 69).addBox(1.0468F, -2.8029F, 16.2525F, 1.9065F, 2.3093F, 2.9484F, new CubeDeformation(0.0F))
		.texOffs(76, 84).addBox(1.0468F, 25.4449F, -11.9953F, 1.9065F, 2.3093F, 2.9484F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5298F, -2.7212F, 4.3263F, -0.7854F, 0.0F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		fuselage.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		seat.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		gear.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		hexadecagon.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}