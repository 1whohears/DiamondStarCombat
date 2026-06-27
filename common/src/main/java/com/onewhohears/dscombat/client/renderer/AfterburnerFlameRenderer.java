package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.client.model.obj.ObjModelHandler;
import com.onewhohears.onewholibs.client.model.obj.ObjModelHandlerImpl;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Рендерит 3D форсажное пламя из .obj модели с UV-скроллингом
 */
public class AfterburnerFlameRenderer {
	
	private static final String FLAME_MODEL_ID = "afterburner_flame";
	
	/**
	 * Создает кастомный RenderType для огня с отключенным culling и аддитивным блендингом
	 */
	private static RenderType createFlameRenderType(ResourceLocation texture) {
		if (texture == null) {
			texture = new ResourceLocation("minecraft", "textures/block/white_concrete.png");
		}
		
		RenderType.CompositeState state = RenderType.CompositeState.builder()
			.setShaderState(RenderStateShard.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
			.setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY) // Аддитивное смешивание для свечения
			.setCullState(RenderStateShard.NO_CULL) // Отключаем culling - огонь виден с обеих сторон
			.setLightmapState(RenderStateShard.LIGHTMAP)
			.setOverlayState(RenderStateShard.OVERLAY)
			.setWriteMaskState(RenderStateShard.COLOR_WRITE) // Не пишем в depth buffer
			.createCompositeState(true);
		
		return RenderType.create(
			"afterburner_flame",
			com.mojang.blaze3d.vertex.DefaultVertexFormat.NEW_ENTITY,
			com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS,
			256,
			true,
			true,
			state
		);
	}
	
	/**
	 * Рендерит форсажное пламя из .obj модели с анимацией UV
	 */
	public static void renderFlame(PoseStack poseStack, MultiBufferSource bufferSource, 
	                               int packedLight, float partialTicks, EntityVehicle entity) {
		
		// Получаем обработчик модели
		ObjModelHandler modelHandler = ObjEntityModels.get().getObjModelHandler(FLAME_MODEL_ID);
		if (modelHandler == null) return;
		
		// Поворот на 180° по оси Y (разворот пламени)
		poseStack.mulPose(Axis.YP.rotationDegrees(180f));
		
		// Вычисляем скорость самолета
		float speed = (float) entity.getDeltaMovement().length();
		
		// Слабая пульсация для живости
		float time = (entity.tickCount + partialTicks) * 0.1f;
		float pulse = 1.0f + (float)Math.sin(time * 2.0) * 0.05f; // Пульсация ±5%
		
		// Длина пламени зависит от скорости, но с ограничением
		// Минимум 0.5 (50% от оригинала), максимум 1.5 (150% от оригинала)
		float lengthScale = Math.max(0.5f, Math.min(1.5f, 0.5f + speed * 0.3f)) * pulse;
		
		// Базовый размер
		float baseScale = 0.5f;
		
		// Применяем разный масштаб по осям: Z (длина) зависит от скорости
		poseStack.scale(baseScale, baseScale, baseScale * lengthScale);
		
		// UV скроллинг для эффекта движения пламени - от двигателя
		float uvTime = (entity.tickCount + partialTicks) * (0.01f + speed * 0.005f); // Намного медленнее
		// Отрицательный offset для движения от двигателя по оси U
		float uvOffsetU = -(uvTime % 1.0f);
		
		// Создаем map с UV offset
		Map<String, Object> renderData = new HashMap<>();
		renderData.put("uvOffsetU", uvOffsetU);
		renderData.put("uvOffsetV", 0.0f);
		
		// Рендер модели с прозрачностью и UV-скроллингом
		if (modelHandler instanceof ObjModelHandlerImpl impl) {
			impl.render(
				poseStack, 
				bufferSource, 
				partialTicks, 
				packedLight, 
				OverlayTexture.NO_OVERLAY,
				Collections.emptyMap(),  // transforms
				(tex) -> createFlameRenderType(tex),  // Используем кастомный RenderType
				renderData  // renderData with UV offsets
			);
		}
	}
}
