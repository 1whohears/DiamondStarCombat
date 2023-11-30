package com.onewhohears.dscombat.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BigFlameParticle extends TextureSheetParticle {
	
	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;
		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, 
				double x, double y, double z, double dx, double dy, double dz) {
			return new BigFlameParticle(level, x, y, z, dx, dy, dz, sprites);
		}
	}
	
	private final SpriteSet sprites;
	
	protected BigFlameParticle(ClientLevel level, double x, double y, double z, 
			double dx, double dy, double dz, SpriteSet sprites) {
		super(level, x, y, z, dx, dy, dz);
		this.sprites = sprites;
		friction = 0.95f;
		gravity = 0.85f;
		quadSize = 0.4f + (float)random.nextGaussian() * 0.6f;
		lifetime = 200 + (int)(random.nextGaussian() * 100);
		setColor(1, 1, 1);
		setAlpha(1f);
		setParticleSpeed(dx+random.nextGaussian()*0.05,
				  		 dy+random.nextGaussian()*0.05, 
				  		 dz+random.nextGaussian()*0.05);
		setSpriteFromAge(sprites);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (removed) return;
		setSprite(sprites.get(age%12, 12));
		if (random.nextFloat() < 0.05) level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, 
				x, y+0.2, z, xd, 0.07, zd);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

}
