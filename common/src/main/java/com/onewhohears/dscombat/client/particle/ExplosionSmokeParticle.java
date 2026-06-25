package com.onewhohears.dscombat.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class ExplosionSmokeParticle extends TextureSheetParticle {

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;
		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, 
				double x, double y, double z, double dx, double dy, double dz) {
			return new ExplosionSmokeParticle(level, x, y, z, dx, dy, dz, sprites);
		}
	}
	
	private final SpriteSet sprites;
	private final float initialSize;
	
	protected ExplosionSmokeParticle(ClientLevel level, double x, double y, double z, 
			double dx, double dy, double dz, SpriteSet sprites) {
		super(level, x, y, z, dx, dy, dz);
		this.sprites = sprites;
		
		// Быстрое движение в стороны, МЕДЛЕННОЕ вверх
		this.xd = dx * 1.2; // Быстрее в стороны
		this.yd = Math.abs(dy) * 0.6 + 0.02; // МЕДЛЕННЕЕ вверх (было 2.0 + 0.04)
		this.zd = dz * 1.2; // Быстрее в стороны
		
		friction = 0.98f; // Меньше трения = дольше летит
		gravity = 0.01f; // Положительная гравитация = тянет вниз (было -0.003)
		
		// Меньшие размеры дыма
		float sizeVariation = 0.4f + random.nextFloat() * 0.4f; // 0.4 - 0.8
		this.initialSize = sizeVariation;
		quadSize = initialSize * 0.6f; // Начинаем с меньшего размера
		
		// Дым живет НАМНОГО дольше - 4-8 секунд
		lifetime = 80 + random.nextInt(80);
		
		// Случайное начальное вращение
		roll = random.nextFloat() * 2 * Mth.PI;
		oRoll = roll;
		
		// Белый/светло-серый цвет для дыма с вариациями
		float grayVariation = 0.85f + random.nextFloat() * 0.15f; // 0.85 - 1.0
		setColor(grayVariation, grayVariation, grayVariation);
		
		// Начинаем с низкой прозрачности
		setAlpha(0.3f);
		pickSprite(sprites); // Выбираем случайную текстуру из твоих дым1, дым2, дым3
	}
	
	@Override
	public void tick() {
		super.tick();
		if (removed) return;
		
		// Плавное расширение дыма
		float ageRatio = (float)age / (float)lifetime;
		
		// Дым расширяется быстрее в начале, затем замедляется
		float expansionFactor;
		if (ageRatio < 0.3f) {
			// Быстрое расширение в начале
			expansionFactor = 0.6f + ageRatio * 4.0f;
		} else {
			// Медленное расширение потом
			expansionFactor = 1.8f + (ageRatio - 0.3f) * 1.5f;
		}
		quadSize = initialSize * expansionFactor;
		
		// Плавное затухание с более мягкой кривой
		if (ageRatio < 0.15f) {
			// Плавное появление
			alpha = 0.3f * (ageRatio / 0.15f);
		} else if (ageRatio < 0.4f) {
			// Пик видимости
			alpha = 0.3f + (ageRatio - 0.15f) * 0.4f;
		} else {
			// Постепенное затухание
			float fadeProgress = (ageRatio - 0.4f) / 0.6f;
			alpha = (0.3f + 0.1f) * (1.0f - fadeProgress * fadeProgress); // квадратичное затухание
		}
		
		// Медленное вращение с вариацией
		roll += 0.002f + random.nextFloat() * 0.002f;
		
		// Не даем дыму опускаться слишком низко - замедляем падение
		if (yd < 0 && y < 1.0) {
			yd *= 0.5; // Замедляем падение вниз
		}
	}
	
	@Override
	public int getLightColor(float pPartialTick) {
		// Дым слегка освещен от взрыва
		int baseLight = super.getLightColor(pPartialTick);
		float ageRatio = ((float)age + pPartialTick) / (float)lifetime;
		if (ageRatio < 0.3f) {
			// В начале дым освещен ярче от ядра
			return 15728880;
		}
		return baseLight;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
}
