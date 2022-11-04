package com.onewhohears.dscombat.client.renderer;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.item.ItemWeaponPart;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;

public class RendererWeaponPart extends BlockEntityWithoutLevelRenderer {
	
	public RendererWeaponPart(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
		super(blockEntityRenderDispatcher, entityModelSet);
	}
	
	@Override
	public void onResourceManagerReload(ResourceManager resourceManager) {
		
	}
	
	@Override
	public void renderByItem(ItemStack stack, ItemTransforms.@NotNull TransformType transformType, @NotNull PoseStack pose, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
		if (!(stack.getItem() instanceof ItemWeaponPart)) return;
		/*pose.pushPose();
		EntityModel<?> model;
		ResourceLocation layerLocation;
		VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, model.renderType(layerLocation), false, stack.hasFoil());
		model.renderToBuffer(pose, vertexConsumer, packedLight, packedOverlay, packedOverlay, packedOverlay, packedLight, packedOverlay);
		pose.popPose();*/
		// TODO change weapon part model based on weapon id
		super.renderByItem(stack, transformType, pose, buffer, packedLight, packedOverlay);
	}
	
	

}
