package com.onewhohears.dscombat.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Particle that renders track marks on the ground
 * Lasts for 20 seconds (400 ticks) and fades out
 */
public class TrackMarkParticle extends TextureSheetParticle {
    
    private final float rotationAngle;
    
    protected TrackMarkParticle(ClientLevel level, double x, double y, double z, 
                               double xSpeed, double ySpeed, double zSpeed, 
                               float rotationAngle) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        
        this.rotationAngle = rotationAngle;
        
        // Particle properties
        this.lifetime = 400; // 20 seconds (20 * 20 ticks)
        this.gravity = 0.0F; // No gravity - stays on ground
        this.hasPhysics = false; // No collision
        
        // Size and appearance
        this.quadSize = 0.5F; // Size of track mark
        this.alpha = 0.6F; // Semi-transparent
        
        // Dark color for track mark
        this.rCol = 0.2F;
        this.gCol = 0.2F;
        this.bCol = 0.2F;
        
        // No movement
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
    }
    
    @Override
    public void tick() {
        super.tick();
        
        // Fade out over time
        if (this.age > this.lifetime - 100) { // Last 5 seconds
            float fadeProgress = (float)(this.lifetime - this.age) / 100.0F;
            this.alpha = 0.6F * fadeProgress;
        }
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    
    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        // Render flat on ground with rotation
        Vec3 cameraPos = camera.getPosition();
        float x = (float)(this.x - cameraPos.x);
        float y = (float)(this.y - cameraPos.y);
        float z = (float)(this.z - cameraPos.z);
        
        // Render as flat quad on ground
        super.render(buffer, camera, partialTicks);
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        
        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }
        
        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                      double x, double y, double z,
                                      double xSpeed, double ySpeed, double zSpeed) {
            TrackMarkParticle particle = new TrackMarkParticle(level, x, y, z, 
                                                               xSpeed, ySpeed, zSpeed, 
                                                               (float)xSpeed); // Use xSpeed as rotation
            particle.pickSprite(this.sprites);
            return particle;
        }
    }
}
