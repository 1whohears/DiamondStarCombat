package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.onewhohears.dscombat.entity.weapon.EntityMuzzleFlash;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Рендерер для entity вспышки дульного огня
 */
public class RendererEntityMuzzleFlash extends EntityRenderer<EntityMuzzleFlash> {
	
	public RendererEntityMuzzleFlash(EntityRendererProvider.Context context) {
		super(context);
	}
	
	@Override
	public void render(@NotNull EntityMuzzleFlash entity, float yaw, float partialTicks,
	                   @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
		
		poseStack.pushPose();
		
		// Применяем повороты entity (как в ObjEntityModel.rotate())
		// Используем getViewYRot и getViewXRot для интерполяции
		float entityYaw = entity.getViewYRot(partialTicks);
		float entityPitch = entity.getViewXRot(partialTicks);
		
		poseStack.mulPose(Axis.YP.rotationDegrees(-entityYaw)); // Минус для инверсии
		poseStack.mulPose(Axis.XP.rotationDegrees(entityPitch));
		
		// Коррекция ориентации модели: поворот на 180° по Y (как у afterburner flame)
		poseStack.mulPose(Axis.YP.rotationDegrees(180f));
		
		// Добавляем случайный поворот по X для живого эффекта
		poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getRandomYaw()));
		
		// Рендерим вспышку
		MuzzleFlashRenderer.renderFlash(
			poseStack,
			bufferSource,
			packedLight,
			partialTicks,
			entity.getScale(),
			entity.getLifetime(),
			entity.getMaxLifetime()
		);
		
		poseStack.popPose();
		
		super.render(entity, yaw, partialTicks, poseStack, bufferSource, packedLight);
	}
	
	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull EntityMuzzleFlash entity) {
		return new ResourceLocation("dscombat", "textures/entity/muzzle_flash.png");
	}
}
