package com.onewhohears.dscombat.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class AfterBurnerParticle extends TextureSheetParticle {

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;
		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, 
				double x, double y, double z, double dx, double dy, double dz) {
			return new AfterBurnerParticle(level, x, y, z, dx, dy, dz, sprites);
		}
	}
	
	private final SpriteSet sprites;
	
	protected AfterBurnerParticle(ClientLevel level, double x, double y, double z, 
			double dx, double dy, double dz, SpriteSet sprites) {
		super(level, x, y, z, dx, dy, dz);
		this.sprites = sprites;
		setSpriteFromAge(sprites);
		this.quadSize *= (3f + random.nextGaussian()*0.1);
		this.lifetime = 8 + (int)(random.nextGaussian()*2);
		this.xd += (random.nextGaussian()*2-1)*0.00001;
		this.yd += (random.nextGaussian()*2-1)*0.00001;
		this.zd += (random.nextGaussian()*2-1)*0.00001;
		this.gravity = 0.1f;
	}
	
	@Override
	public void tick() {
		super.tick();
		if (removed) return;
		setSpriteFromAge(sprites);
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
