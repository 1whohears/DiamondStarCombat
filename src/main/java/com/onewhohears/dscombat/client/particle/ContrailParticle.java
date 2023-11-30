package com.onewhohears.dscombat.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContrailParticle extends TextureSheetParticle {
	
	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;
		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, 
				double x, double y, double z, double dx, double dy, double dz) {
			return new ContrailParticle(level, x, y, z, dx, dy, dz, sprites);
		}
	}
	
	protected ContrailParticle(ClientLevel level, double x, double y, double z, 
			double dx, double dy, double dz, SpriteSet sprites) {
		super(level, x, y, z, dx, dy, dz);
		friction = 0.95f;
		gravity = 0.001f;
		quadSize = 0.3f + (float)random.nextGaussian() * 0.08f;
		lifetime = 600 + (int)(random.nextGaussian() * 100);
		pickSprite(sprites);
		setColor(1, 1, 1);
		setAlpha(1f);
		setParticleSpeed(dx+random.nextGaussian()*0.01,
				  		 dy+random.nextGaussian()*0.01, 
				  		 dz+random.nextGaussian()*0.01);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

}
