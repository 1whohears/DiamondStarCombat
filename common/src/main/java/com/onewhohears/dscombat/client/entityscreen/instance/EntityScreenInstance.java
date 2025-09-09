package com.onewhohears.dscombat.client.entityscreen.instance;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.onewhohears.dscombat.util.UtilRender.drawTextureCentered;

@OnlyIn(Dist.CLIENT)
public abstract class EntityScreenInstance implements AutoCloseable{
	
	@Nullable private final RenderType baseRenderType;
	
	public EntityScreenInstance(int id, @Nullable ResourceLocation baseTexture) {
		if (baseTexture != null) baseRenderType = RenderType.text(baseTexture);
		else baseRenderType = null;
	}
	
	public void draw(Entity entity, PoseStack poseStack, MultiBufferSource buffer, float partialTicks, int packedLight,
			float worldWidth, float worldHeight) {
		Matrix4f matrix4f = poseStack.last().pose();
		draw(entity, matrix4f, buffer, partialTicks, packedLight, worldWidth, worldHeight);
	}
	
	public void draw(Entity entity, Matrix4f matrix4f, MultiBufferSource buffer, float partialTicks, int packedLight,
			float worldWidth, float worldHeight) {
		if (baseRenderType != null) drawTextureCentered(baseRenderType, matrix4f, buffer, packedLight, 0);
	}
	
	@Override
	public void close() {
	}
	
}
