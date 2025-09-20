package com.onewhohears.dscombat.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class ShrapnelParticle extends TextureSheetParticle {

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;
		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, 
				double x, double y, double z, double dx, double dy, double dz) {
			return new ShrapnelParticle(level, x, y, z, dx, dy, dz, sprites);
		}
	}
	
	protected ShrapnelParticle(ClientLevel level, double x, double y, double z, 
			double dx, double dy, double dz, SpriteSet sprites) {
		super(level, x, y, z, dx, dy, dz);
		friction = 0.95f;
		gravity = 0.8f;
		quadSize = 0.2f + (float)random.nextGaussian() * 0.3f;
		lifetime = 200 + (int)(random.nextGaussian() * 50);
		roll = random.nextFloat() * 2 * Mth.PI;
		oRoll = roll;
		pickSprite(sprites);
		setColor(1, 1, 1);
		setAlpha(1f);
		setParticleSpeed(dx+random.nextGaussian()*0.05,
				  		 dy+random.nextGaussian()*0.05, 
				  		 dz+random.nextGaussian()*0.05);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

}
