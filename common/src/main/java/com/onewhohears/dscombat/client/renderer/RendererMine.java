package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.weapon.EntityMine;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RendererMine extends EntityRenderer<EntityMine> {
	
	private static final ResourceLocation ANTI_PERSONNEL_TEXTURE = 
			new ResourceLocation(DSCombatMod.MODID, "textures/entity/mine/anti_personnel.png");
	private static final ResourceLocation ANTI_TANK_TEXTURE = 
			new ResourceLocation(DSCombatMod.MODID, "textures/entity/mine/anti_tank.png");
	
	public RendererMine(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(EntityMine mine, float entityYaw, float partialTick, 
	                   PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		poseStack.pushPose();
		
		// Scale based on mine type
		float scale = mine.getMineType() == EntityMine.MineType.ANTI_TANK ? 0.6f : 0.4f;
		poseStack.scale(scale, scale * 0.3f, scale);
		
		// Rotate to face up
		poseStack.mulPose(Axis.XP.rotationDegrees(0));
		
		// Get texture based on mine type
		ResourceLocation texture = getTextureLocation(mine);
		VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(texture));
		
		// Render a simple box
		renderBox(poseStack, vertexConsumer, packedLight);
		
		// Visual indicator if armed
		if (mine.isArmed()) {
			poseStack.translate(0, 0.4, 0);
			poseStack.scale(0.2f, 0.2f, 0.2f);
			VertexConsumer redConsumer = buffer.getBuffer(RenderType.entitySolid(
					new ResourceLocation(DSCombatMod.MODID, "textures/entity/mine/armed.png")));
			renderBox(poseStack, redConsumer, packedLight);
		}
		
		poseStack.popPose();
		super.render(mine, entityYaw, partialTick, poseStack, buffer, packedLight);
	}
	
	private void renderBox(PoseStack poseStack, VertexConsumer consumer, int packedLight) {
		PoseStack.Pose pose = poseStack.last();
		Matrix4f matrix4f = pose.pose();
		Matrix3f matrix3f = pose.normal();
		
		// Simple box rendering
		float size = 0.5f;
		
		// Bottom
		vertex(consumer, matrix4f, matrix3f, -size, 0, -size, 0, 0, 0, -1, 0, packedLight);
		vertex(consumer, matrix4f, matrix3f, -size, 0, size, 0, 1, 0, -1, 0, packedLight);
		vertex(consumer, matrix4f, matrix3f, size, 0, size, 1, 1, 0, -1, 0, packedLight);
		vertex(consumer, matrix4f, matrix3f, size, 0, -size, 1, 0, 0, -1, 0, packedLight);
		
		// Top
		vertex(consumer, matrix4f, matrix3f, -size, 1, -size, 0, 0, 0, 1, 0, packedLight);
		vertex(consumer, matrix4f, matrix3f, size, 1, -size, 1, 0, 0, 1, 0, packedLight);
		vertex(consumer, matrix4f, matrix3f, size, 1, size, 1, 1, 0, 1, 0, packedLight);
		vertex(consumer, matrix4f, matrix3f, -size, 1, size, 0, 1, 0, 1, 0, packedLight);
		
		// Sides
		vertex(consumer, matrix4f, matrix3f, -size, 0, -size, 0, 0, -1, 0, 0, packedLight);
		vertex(consumer, matrix4f, matrix3f, size, 0, -size, 1, 0, -1, 0, 0, packedLight);
		vertex(consumer, matrix4f, matrix3f, size, 1, -size, 1, 1, -1, 0, 0, packedLight);
		vertex(consumer, matrix4f, matrix3f, -size, 1, -size, 0, 1, -1, 0, 0, packedLight);
		
		vertex(consumer, matrix4f, matrix3f, size, 0, -size, 0, 0, 1, 0, 0, packedLight);
		vertex(consumer, matrix4f, matrix3f, size, 0, size, 1, 0, 1, 0, 0, packedLight);
		vertex(consumer, matrix4f, matrix3f, size, 1, size, 1, 1, 1, 0, 0, packedLight);
		vertex(consumer, matrix4f, matrix3f, size, 1, -size, 0, 1, 1, 0, 0, packedLight);
		
		vertex(consumer, matrix4f, matrix3f, size, 0, size, 0, 0, 0, 0, 1, packedLight);
		vertex(consumer, matrix4f, matrix3f, -size, 0, size, 1, 0, 0, 0, 1, packedLight);
		vertex(consumer, matrix4f, matrix3f, -size, 1, size, 1, 1, 0, 0, 1, packedLight);
		vertex(consumer, matrix4f, matrix3f, size, 1, size, 0, 1, 0, 0, 1, packedLight);
		
		vertex(consumer, matrix4f, matrix3f, -size, 0, size, 0, 0, -1, 0, 0, packedLight);
		vertex(consumer, matrix4f, matrix3f, -size, 0, -size, 1, 0, -1, 0, 0, packedLight);
		vertex(consumer, matrix4f, matrix3f, -size, 1, -size, 1, 1, -1, 0, 0, packedLight);
		vertex(consumer, matrix4f, matrix3f, -size, 1, size, 0, 1, -1, 0, 0, packedLight);
	}
	
	private void vertex(VertexConsumer consumer, Matrix4f matrix4f, Matrix3f matrix3f,
	                    float x, float y, float z, float u, float v,
	                    float normalX, float normalY, float normalZ, int packedLight) {
		consumer.vertex(matrix4f, x, y, z)
				.color(255, 255, 255, 255)
				.uv(u, v)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(packedLight)
				.normal(matrix3f, normalX, normalY, normalZ)
				.endVertex();
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(EntityMine mine) {
		return mine.getMineType() == EntityMine.MineType.ANTI_TANK ? 
				ANTI_TANK_TEXTURE : ANTI_PERSONNEL_TEXTURE;
	}
}
