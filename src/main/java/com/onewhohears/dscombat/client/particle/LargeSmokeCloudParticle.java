package com.onewhohears.dscombat.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LargeSmokeCloudParticle extends TextureSheetParticle {
	
	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;
		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, 
				double x, double y, double z, double dx, double dy, double dz) {
			return new LargeSmokeCloudParticle(level, x, y, z, dx, dy, dz, sprites);
		}
	}
	
	protected final int spreadTime;
	protected final double distanceX;
	protected final double distanceY;
	protected final double distanceZ;
	
	protected LargeSmokeCloudParticle(ClientLevel level, double x, double y, double z, 
			double dx, double dy, double dz, SpriteSet sprites) {
		super(level, x, y, z, dx, dy, dz);
		distanceX = dx + random.nextGaussian() * 0.2;
		distanceY = dy + random.nextGaussian() * 0.2;
		distanceZ = dz + random.nextGaussian() * 0.2;
		quadSize *= (7f + random.nextGaussian() * 1.5);
		lifetime = 500 + (int)(random.nextGaussian() * 200);
		spreadTime = 20 + (int)(random.nextGaussian() * 4);
		roll = random.nextFloat() * 2 * Mth.PI;
		oRoll = roll;
		pickSprite(sprites);
		setColor(1, 1, 1);
	}
	
	@Override
	public void tick() {
		if (age <= spreadTime) {
			updateSpeed();
			gravity = 0;
		} else {
			setParticleSpeed(0, 0, 0);
			gravity = 0.1f;
		}
		super.tick();
		fadeOut();
	}
	
	protected void updateSpeed() {
		setParticleSpeed(spreadComponent(distanceX), 
				spreadComponent(distanceY), 
				spreadComponent(distanceZ));
	}
	
	private double spreadComponent(double d) {
		double inverseTime = 1d / (double)spreadTime;
		double initSpeed = d * 2 * inverseTime;
		double acc = -initSpeed * inverseTime;
		return initSpeed + acc * age;
	}
	
	protected void fadeOut() {
		float life = (float)age / (float)lifetime;
		if (life > 0.95f) quadSize *= 0.9;
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

}
