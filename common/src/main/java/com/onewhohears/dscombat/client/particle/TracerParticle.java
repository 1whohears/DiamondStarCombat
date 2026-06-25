package com.onewhohears.dscombat.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TracerParticle extends TextureSheetParticle {

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;
		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, 
				double x, double y, double z, double dx, double dy, double dz) {
			return new TracerParticle(level, x, y, z, dx, dy, dz, sprites);
		}
	}
	
	private final SpriteSet sprites;
	
	protected TracerParticle(ClientLevel level, double x, double y, double z, 
			double dx, double dy, double dz, SpriteSet sprites) {
		super(level, x, y, z, dx, dy, dz); // Move with bullet
		this.sprites = sprites;
		
		// Tracer characteristics - single particle per bullet
		friction = 1.0f; // No friction - moves with bullet
		gravity = 0f; // No gravity
		quadSize = 0.25f; // Smaller size for more realistic appearance
		lifetime = 3; // Very short lifetime - only 3 ticks (0.15 seconds)
		
		// Store motion direction for rendering orientation
		this.xd = dx;
		this.yd = dy;
		this.zd = dz;
		
		// Bright yellow-white color for tracer visibility
		setColor(1.0f, 1.0f, 0.8f);
		
		setAlpha(1f);
		setSpriteFromAge(sprites);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (removed) return;
		
		// Keep full alpha - no fading
		alpha = 1.0f;
		
		setSpriteFromAge(sprites);
	}
	
	@Override
	public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
		// Calculate orientation based on motion direction
		Vec3 motion = new Vec3(xd, yd, zd);
		double motionLength = motion.length();
		
		if (motionLength < 0.001) {
			// No motion, use default billboard rendering
			super.render(buffer, camera, partialTicks);
			return;
		}
		
		// Normalize motion
		Vec3 motionNorm = motion.normalize();
		
		// Calculate yaw and pitch from motion vector
		float yaw = (float) Math.toDegrees(Mth.atan2(motionNorm.x, motionNorm.z));
		float pitch = (float) Math.toDegrees(Math.asin(-motionNorm.y));
		
		// Get camera position
		Vec3 cameraPos = camera.getPosition();
		float x = (float)(Mth.lerp(partialTicks, this.xo, this.x) - cameraPos.x());
		float y = (float)(Mth.lerp(partialTicks, this.yo, this.y) - cameraPos.y());
		float z = (float)(Mth.lerp(partialTicks, this.zo, this.z) - cameraPos.z());
		
		float size = this.getQuadSize(partialTicks);
		
		float u0 = this.getU0();
		float u1 = this.getU1();
		float v0 = this.getV0();
		float v1 = this.getV1();
		int light = this.getLightColor(partialTicks);
		
		// Render two perpendicular quads for visibility from all angles
		for (int i = 0; i < 2; i++) {
			// Create rotation quaternion
			Quaternionf rotation = new Quaternionf();
			rotation.rotateY((float) Math.toRadians(-yaw));
			rotation.rotateX((float) Math.toRadians(pitch));
			
			// Second quad rotated 90 degrees around the motion axis
			if (i == 1) {
				rotation.rotateZ((float) Math.toRadians(90));
			}
			
			// Render oriented quad
			Vector3f[] vertices = new Vector3f[]{
				new Vector3f(-1.0F, -1.0F, 0.0F),
				new Vector3f(-1.0F, 1.0F, 0.0F),
				new Vector3f(1.0F, 1.0F, 0.0F),
				new Vector3f(1.0F, -1.0F, 0.0F)
			};
			
			for(int j = 0; j < 4; ++j) {
				Vector3f vertex = vertices[j];
				vertex.rotate(rotation);
				vertex.mul(size);
				vertex.add(x, y, z);
			}
			
			buffer.vertex(vertices[0].x(), vertices[0].y(), vertices[0].z())
				.uv(u1, v1)
				.color(this.rCol, this.gCol, this.bCol, this.alpha)
				.uv2(light)
				.endVertex();
			buffer.vertex(vertices[1].x(), vertices[1].y(), vertices[1].z())
				.uv(u1, v0)
				.color(this.rCol, this.gCol, this.bCol, this.alpha)
				.uv2(light)
				.endVertex();
			buffer.vertex(vertices[2].x(), vertices[2].y(), vertices[2].z())
				.uv(u0, v0)
				.color(this.rCol, this.gCol, this.bCol, this.alpha)
				.uv2(light)
				.endVertex();
			buffer.vertex(vertices[3].x(), vertices[3].y(), vertices[3].z())
				.uv(u0, v1)
				.color(this.rCol, this.gCol, this.bCol, this.alpha)
				.uv2(light)
				.endVertex();
		}
	}
	
	@Override
	public int getLightColor(float pPartialTick) {
		// Full brightness like a real tracer round
		return 15728880;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_LIT;
	}

}
