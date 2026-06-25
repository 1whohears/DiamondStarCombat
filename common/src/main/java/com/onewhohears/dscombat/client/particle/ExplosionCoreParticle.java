package com.onewhohears.dscombat.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class ExplosionCoreParticle extends TextureSheetParticle {

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;
		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, 
				double x, double y, double z, double dx, double dy, double dz) {
			return new ExplosionCoreParticle(level, x, y, z, dx, dy, dz, sprites);
		}
	}
	
	private final SpriteSet sprites;
	private final float initialSize;
	
	protected ExplosionCoreParticle(ClientLevel level, double x, double y, double z, 
			double dx, double dy, double dz, SpriteSet sprites) {
		super(level, x, y, z, dx, dy, dz);
		this.sprites = sprites;
		
		// Ядро взрыва не двигается или двигается очень медленно
		this.xd = dx * 0.1;
		this.yd = dy * 0.1;
		this.zd = dz * 0.1;
		
		friction = 0.95f;
		gravity = 0.0f;
		
		// Меньшие размеры текстур
		float sizeVariation = 0.3f + random.nextFloat() * 0.4f; // 0.3 - 0.7 (уменьшено в 2 раза)
		this.initialSize = sizeVariation;
		quadSize = initialSize * 0.5f; // Начинаем с меньшего размера
		
		// Очень короткая жизнь - ядро мгновенно исчезает (1-2 тика)
		lifetime = 1 + random.nextInt(2);
		
		// Случайное вращение для разнообразия
		roll = random.nextFloat() * 2 * Mth.PI;
		oRoll = roll;
		
		// Яркие цвета взрыва - оранжевый/желтый/белый
		float colorVariant = random.nextFloat();
		if (colorVariant < 0.4f) {
			// Ярко-оранжевый
			setColor(1.0f, 0.5f + random.nextFloat() * 0.2f, 0.1f);
		} else if (colorVariant < 0.7f) {
			// Желто-оранжевый
			setColor(1.0f, 0.8f + random.nextFloat() * 0.2f, 0.3f + random.nextFloat() * 0.2f);
		} else {
			// Яркий белый центр
			setColor(1.0f, 1.0f, 0.9f + random.nextFloat() * 0.1f);
		}
		
		// Начинаем с полупрозрачности для более плавного появления
		setAlpha(0.8f);
		setSpriteFromAge(sprites);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (removed) return;
		
		// Расширение ядра с плавной анимацией
		float ageRatio = (float)age / (float)lifetime;
		
		// Плавное расширение на протяжении всей жизни
		float expansionFactor = 1.0f + ageRatio * 2.5f;
		quadSize = initialSize * expansionFactor;
		
		// Плавное затухание - начинается раньше и более постепенное
		if (ageRatio < 0.2f) {
			// Быстрое появление
			alpha = 0.8f * (ageRatio / 0.2f);
		} else if (ageRatio < 0.6f) {
			// Полная яркость
			alpha = 0.8f;
		} else {
			// Плавное затухание
			float fadeProgress = (ageRatio - 0.6f) / 0.4f;
			alpha = 0.8f * (1.0f - fadeProgress);
		}
		
		// Медленное вращение с небольшим ускорением
		roll += 0.003f + ageRatio * 0.004f;
		
		setSpriteFromAge(sprites);
	}
	
	@Override
	public int getLightColor(float pPartialTick) {
		// Ядро светится очень ярко
		return 15728880;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_LIT;
	}
}
