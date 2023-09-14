package com.onewhohears.dscombat.client.model.weapon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.model.EntityControllableModel;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@Deprecated
public class EntityModelBombRack extends EntityControllableModel<EntityWeaponRack>{
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DSCombatMod.MODID, "bomb_rack"), "main");
	private final ModelPart main;
	private final ModelPart[] m = new ModelPart[13];
	
	public EntityModelBombRack(ModelPart root) {
		this.main = root.getChild("main");
		for (int i = 0; i < m.length; ++i) this.m[i] = main.getChild("m"+(i+1));
	}
	
	@Override
	public void renderToBuffer(EntityWeaponRack entity, float partialTicks, PoseStack poseStack, VertexConsumer vertexConsumer,
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0, 1.50, 0);
		poseStack.scale(1.0F, -1.0F, 1.0F);
		int mNum = entity.getAmmoNum();
		for (int i = 0; i < m.length; ++i) m[i].visible = i < mNum;
		main.zRot = Mth.PI;
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 2.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 16).addBox(-6.0F, 2.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 9).addBox(-6.0F, 8.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 6).addBox(-6.0F, 5.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 3).addBox(1.0F, 8.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 0).addBox(1.0F, 5.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(1.0F, 2.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.0F, 0.0F));

		PartDefinition m1 = main.addOrReplaceChild("m1", CubeListBuilder.create().texOffs(32, 0).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition m2 = main.addOrReplaceChild("m2", CubeListBuilder.create().texOffs(16, 30).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.0F, 0.0F));

		PartDefinition m3 = main.addOrReplaceChild("m3", CubeListBuilder.create().texOffs(0, 28).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 0.0F, 0.0F));

		PartDefinition m4 = main.addOrReplaceChild("m4", CubeListBuilder.create().texOffs(16, 44).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 0.0F, 0.0F));

		PartDefinition m5 = main.addOrReplaceChild("m5", CubeListBuilder.create().texOffs(0, 42).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

		PartDefinition m6 = main.addOrReplaceChild("m6", CubeListBuilder.create().texOffs(32, 28).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 3.0F, 0.0F));

		PartDefinition m7 = main.addOrReplaceChild("m7", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 3.0F, 0.0F));

		PartDefinition m8 = main.addOrReplaceChild("m8", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 3.0F, 0.0F));

		PartDefinition m9 = main.addOrReplaceChild("m9", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition m10 = main.addOrReplaceChild("m10", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 6.0F, 0.0F));

		PartDefinition m11 = main.addOrReplaceChild("m11", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 6.0F, 0.0F));

		PartDefinition m12 = main.addOrReplaceChild("m12", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 6.0F, 0.0F));

		PartDefinition m13 = main.addOrReplaceChild("m13", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, 3.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 8.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

}
