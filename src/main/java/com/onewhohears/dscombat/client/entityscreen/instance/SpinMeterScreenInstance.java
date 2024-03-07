package com.onewhohears.dscombat.client.entityscreen.instance;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public abstract class SpinMeterScreenInstance extends EntityScreenInstance {
	
    protected final RenderType spinRenderType;
    
	public SpinMeterScreenInstance(int id, ResourceLocation baseTexture, ResourceLocation spinTexture) {
		super(id, baseTexture);
		spinRenderType = RenderType.text(spinTexture);
	}
	
	@Override
	public void draw(Entity entity, PoseStack poseStack, MultiBufferSource buffer, 
			float partialTicks, int packedLight, float worldWidth, float worldHeight) {
		super.draw(entity, poseStack, buffer, partialTicks, packedLight, worldWidth, worldHeight);
		poseStack.pushPose();
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(getAngleDegrees(entity)));
		Matrix4f matrix4f = poseStack.last().pose();
		drawTextureCentered(spinRenderType, matrix4f, buffer, packedLight, -0.001f);
		poseStack.popPose();
	}
	
	protected abstract float getAngleDegrees(Entity entity);

}
