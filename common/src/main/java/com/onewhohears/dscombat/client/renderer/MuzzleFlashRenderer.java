package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.client.model.obj.ObjModelHandler;
import com.onewhohears.onewholibs.client.model.obj.ObjModelHandlerImpl;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Рендерит 3D вспышку дульного огня из .obj модели
 */
public class MuzzleFlashRenderer {
	
	private static final String FLASH_MODEL_ID = "muzzle_flash";
	
	/**
	 * Создает кастомный RenderType для вспышки с отключенным culling и аддитивным блендингом
	 */
	private static RenderType createFlashRenderType(ResourceLocation texture) {
		if (texture == null) {
			texture = new ResourceLocation("dscombat", "textures/entity/muzzle_flash.png");
		}
		
		RenderType.CompositeState state = RenderType.CompositeState.builder()
			.setShaderState(RenderStateShard.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
			.setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY) // Аддитивное смешивание для свечения
			.setCullState(RenderStateShard.NO_CULL) // Отключаем culling - вспышка видна с обеих сторон
			.setLightmapState(RenderStateShard.LIGHTMAP)
			.setOverlayState(RenderStateShard.OVERLAY)
			.setWriteMaskState(RenderStateShard.COLOR_WRITE) // Не пишем в depth buffer
			.createCompositeState(true);
		
		return RenderType.create(
			"muzzle_flash",
			com.mojang.blaze3d.vertex.DefaultVertexFormat.NEW_ENTITY,
			com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS,
			256,
			true,
			true,
			state
		);
	}
	
	/**
	 * Рендерит вспышку дульного огня из .obj модели.
	 * Поворот в направлении выстрела должен быть уже применён в PoseStack вызывающим кодом.
	 *
	 * @param scale масштаб вспышки (1.0 = нормальный размер)
	 * @param lifetime текущее время жизни в тиках
	 * @param maxLifetime максимальное время жизни в тиках
	 */
	public static void renderFlash(PoseStack poseStack, MultiBufferSource bufferSource, 
	                               int packedLight, float partialTicks,
	                               float scale, int lifetime, int maxLifetime) {
		
		// Получаем обработчик модели
		ObjModelHandler modelHandler = ObjEntityModels.get().getObjModelHandler(FLASH_MODEL_ID);
		if (modelHandler == null) {
			return;
		}
		
		// Небольшая пульсация для живости
		float pulse = 1.0f + (float)Math.sin(lifetime * 0.5f) * 0.1f;
		
		// Применяем масштаб с пульсацией
		float finalScale = scale * pulse;
		poseStack.scale(finalScale, finalScale, finalScale);
		
		// Рендер модели без прозрачности, с обычным освещением
		if (modelHandler instanceof ObjModelHandlerImpl impl) {
			impl.render(
				poseStack, 
				bufferSource, 
				partialTicks, 
				packedLight,  // Используем обычное освещение
				OverlayTexture.NO_OVERLAY,
				Collections.emptyMap(),  // transforms
				(tex) -> createFlashRenderType(tex),  // Используем кастомный RenderType
				Collections.emptyMap()  // Пустой renderData
			);
		}
	}
	
	/**
	 * Упрощенная версия для быстрого рендеринга с параметрами по умолчанию
	 */
	public static void renderFlash(PoseStack poseStack, MultiBufferSource bufferSource, 
	                               int packedLight, float partialTicks,
	                               float scale) {
		renderFlash(poseStack, bufferSource, packedLight, partialTicks, scale, 0, 5);
	}
}
