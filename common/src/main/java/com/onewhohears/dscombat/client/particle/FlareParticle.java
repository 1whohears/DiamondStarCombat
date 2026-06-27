package com.onewhohears.dscombat.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class FlareParticle extends TextureSheetParticle {

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;
		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, 
				double x, double y, double z, double dx, double dy, double dz) {
			return new FlareParticle(level, x, y, z, dx, dy, dz, sprites);
		}
	}
	
	private final SpriteSet sprites;
	
	protected FlareParticle(ClientLevel level, double x, double y, double z, 
			double dx, double dy, double dz, SpriteSet sprites) {
		super(level, x, y, z, dx, dy, dz);
		this.sprites = sprites;
		friction = 0.999f;
		gravity = 0.01f;
		quadSize = 0.4f + (float)random.nextGaussian() * 0.1f;
		lifetime = 20 + random.nextInt(10);
		roll = random.nextFloat() * 2 * Mth.PI;
		oRoll = roll;
		
		// Colorful flare colors: bright red, orange, yellow, white
		float colorVariant = random.nextFloat();
		if (colorVariant < 0.3f) {
			// Bright red-orange
			setColor(1.0f, 0.3f + random.nextFloat() * 0.3f, 0.1f);
		} else if (colorVariant < 0.6f) {
			// Orange-yellow
			setColor(1.0f, 0.6f + random.nextFloat() * 0.3f, 0.2f);
		} else if (colorVariant < 0.85f) {
			// Yellow-white
			setColor(1.0f, 0.9f + random.nextFloat() * 0.1f, 0.5f + random.nextFloat() * 0.3f);
		} else {
			// Pure bright white
			setColor(1.0f, 1.0f, 1.0f);
		}
		
		setAlpha(1f);
		setSpriteFromAge(sprites);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (removed) return;
		fadeOut();
		
		// Add slight rotation for more dynamic look
		roll += 0.1f;
		
		setSpriteFromAge(sprites);
		setSprite(sprites.get(Math.min(age, 6), 6));
	}
	
	protected void fadeOut() {
		float life = (float)age / (float)lifetime;
		
		// Smooth fade out in last 30% of lifetime
		if (life > 0.7f) {
			float fadeProgress = (life - 0.7f) / 0.3f;
			alpha = 1.0f - fadeProgress;
			quadSize *= 0.95f;
		}
		
		// Slight size pulsing for more dynamic effect
		if (age % 4 == 0) {
			quadSize *= 1.05f;
		} else if (age % 4 == 2) {
			quadSize *= 0.95f;
		}
	}
	
	@Override
	public int getLightColor(float pPartialTick) {
		return 15728880;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_LIT;
	}

}
