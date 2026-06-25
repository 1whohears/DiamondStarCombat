package com.onewhohears.dscombat.client.entityscreen.instance;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.onewholibs.util.math.Vec3f;

import com.onewhohears.onewholibs.util.math.Mat4f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import static com.onewhohears.dscombat.client.util.UtilRender.drawTextureCentered;

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
		poseStack.mulPose(Vec3f.ZP.rotationDegrees(getAngleDegrees(entity)).convert());
		Mat4f matrix4f = Mat4f.from(poseStack.last().pose());
		drawTextureCentered(spinRenderType, matrix4f, buffer, packedLight, -0.001f);
		poseStack.popPose();
	}
	
	protected abstract float getAngleDegrees(Entity entity);

}
