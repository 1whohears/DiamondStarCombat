package com.onewhohears.dscombat.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

/**
 * White smoke cloud from smoke grenades.
 */
public class SmokeGrenadeParticle extends TextureSheetParticle {

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public Provider(SpriteSet sprites) { this.sprites = sprites; }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                double x, double y, double z, double dx, double dy, double dz) {
            return new SmokeGrenadeParticle(level, x, y, z, dx, dy, dz, sprites);
        }
    }

    protected SmokeGrenadeParticle(ClientLevel level, double x, double y, double z,
            double vx, double vy, double vz, SpriteSet sprites) {
        super(level, x, y, z, 0, 0, 0);

        quadSize = 0.4f + random.nextFloat() * 0.4f;   // 0.4 – 0.8 blocks
        lifetime  = 140 + random.nextInt(80);            // 140 – 220 ticks (~7-11 сек)
        friction  = 0.93f;
        gravity   = -0.005f;
        roll  = random.nextFloat() * 2 * Mth.PI;
        oRoll = roll;

        // Белый цвет с небольшим разбросом
        setColor(
            Mth.clamp(0.95f + (random.nextFloat() - 0.5f) * 0.05f, 0, 1),
            Mth.clamp(0.95f + (random.nextFloat() - 0.5f) * 0.05f, 0, 1),
            Mth.clamp(0.95f + (random.nextFloat() - 0.5f) * 0.05f, 0, 1)
        );

        alpha = 0f;
        targetAlpha = 0.55f + random.nextFloat() * 0.25f; // 0.55 – 0.80

        setParticleSpeed(vx, vy, vz);
        this.sprite = sprites.get(random.nextInt(3), 2);
    }

    private final float targetAlpha;
    private static final int FADE_IN_TICKS = 10;
    private static final float EXPAND_RATE = 1.008f;

    @Override
    public void tick() {
        xo = x; yo = y; zo = z;

        quadSize *= EXPAND_RATE;
        oRoll = roll;
        roll += (random.nextFloat() - 0.5f) * 0.02f;

        float life = (float) age / (float) lifetime;

        if (age < FADE_IN_TICKS) {
            alpha = Mth.lerp((float) age / FADE_IN_TICKS, 0f, targetAlpha);
        } else if (life > 0.6f) {
            // Fade-out starts at 60% of lifetime, very gradual
            alpha = Mth.lerp((life - 0.6f) / 0.4f, targetAlpha, 0f);
        } else {
            alpha = targetAlpha;
        }

        if (alpha <= 0.005f && age >= FADE_IN_TICKS) {
            remove();
            return;
        }

        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }
}
