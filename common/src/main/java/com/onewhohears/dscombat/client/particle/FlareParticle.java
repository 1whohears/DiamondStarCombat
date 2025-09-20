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
		quadSize = 0.35f + (float)random.nextGaussian() * 0.05f;
		lifetime = 15;
		roll = random.nextFloat() * 2 * Mth.PI;
		oRoll = roll;
		setColor(1, 1, 1);
		setAlpha(1f);
		setSpriteFromAge(sprites);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (removed) return;
		fadeOut();
		setSpriteFromAge(sprites);
		setSprite(sprites.get(Math.min(age, 6), 6));
	}
	
	protected void fadeOut() {
		float life = (float)age / (float)lifetime;
		if (life > 0.98f) quadSize *= 0.9;
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
