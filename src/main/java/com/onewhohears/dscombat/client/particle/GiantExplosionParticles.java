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

public class GiantExplosionParticles extends TextureSheetParticle {
	
	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;
		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, 
				double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new GiantExplosionParticles(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
		}
	}
	
	protected GiantExplosionParticles(ClientLevel level, double x, double y, double z, 
			double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed);
		this.friction = 0.8f;
		this.gravity = 0.5f;
		this.xd = xSpeed;
		this.yd = ySpeed;
		this.zd = zSpeed;
		this.quadSize *= 8f;
		this.lifetime = 300;
		this.setSpriteFromAge(sprites);
		this.setColor(1, 1, 1);
	}
	
	@Override
	public void tick() {
		super.tick();
		fadeOut();
	}
	
	protected void fadeOut() {
		setAlpha(1f - (float)age / (float)lifetime);
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

}
