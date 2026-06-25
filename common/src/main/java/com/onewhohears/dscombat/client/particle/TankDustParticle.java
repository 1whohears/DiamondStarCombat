package com.onewhohears.dscombat.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

/**
 * Dust cloud kicked up by tank tracks.
 *
 * Encoding via addAlwaysVisibleParticle(type, x, y, z, dx, dy, dz):
 *   dx = red   (0..1)
 *   dy = green (0..1)
 *   dz = blue  (0..1)
 *
 * Actual velocity is set inside the constructor from stored fields,
 * passed via the static thread-local set before spawning.
 */
public class TankDustParticle extends TextureSheetParticle {

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public Provider(SpriteSet sprites) { this.sprites = sprites; }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                double x, double y, double z, double dx, double dy, double dz) {
            // dx/dy/dz carry RGB color; actual velocity comes from pendingVel
            float r = (float) Mth.clamp(dx, 0, 1);
            float g = (float) Mth.clamp(dy, 0, 1);
            float b = (float) Mth.clamp(dz, 0, 1);
            double[] vel = pendingVel;
            pendingVel = ZERO_VEL;
            return new TankDustParticle(level, x, y, z, vel[0], vel[1], vel[2], r, g, b, sprites);
        }
    }

    // Velocity passed before spawning via static field
    private static final double[] ZERO_VEL = {0, 0, 0};
    public static double[] pendingVel = ZERO_VEL;

    // -------------------------------------------------------

    protected TankDustParticle(ClientLevel level, double x, double y, double z,
            double vx, double vy, double vz,
            float r, float g, float b, SpriteSet sprites) {
        super(level, x, y, z, 0, 0, 0);

        quadSize = 0.7f + random.nextFloat() * 0.9f;   // 0.7 – 1.6 blocks
        lifetime  = 60 + random.nextInt(60);             // 60 – 120 ticks
        friction  = 0.91f;
        gravity   = -0.002f;                             // лёгкий подъём
        roll  = random.nextFloat() * 2 * Mth.PI;
        oRoll = roll;

        // Цвет блока + небольшой разброс
        setColor(
            Mth.clamp(r + (random.nextFloat() - 0.5f) * 0.06f, 0, 1),
            Mth.clamp(g + (random.nextFloat() - 0.5f) * 0.06f, 0, 1),
            Mth.clamp(b + (random.nextFloat() - 0.5f) * 0.06f, 0, 1)
        );

        // Fade-in: стартуем с alpha=0, нарастаем до целевой
        alpha = 0f;
        targetAlpha = 0.30f + random.nextFloat() * 0.20f; // 0.30 – 0.50

        setParticleSpeed(vx, vy, vz);
        pickSprite(sprites);
    }

    private final float targetAlpha;
    private static final int FADE_IN_TICKS  = 6;
    private static final float EXPAND_RATE  = 1.010f;

    @Override
    public void tick() {
        xo = x; yo = y; zo = z;

        quadSize *= EXPAND_RATE;

        float life = (float) age / (float) lifetime;

        // Fade-in
        if (age < FADE_IN_TICKS) {
            alpha = Mth.lerp((float) age / FADE_IN_TICKS, 0f, targetAlpha);
        }
        // Fade-out (последние 55% жизни)
        else if (life > 0.45f) {
            alpha = Mth.lerp((life - 0.45f) / 0.55f, targetAlpha, 0f);
        }

        if (alpha <= 0.005f && age >= FADE_IN_TICKS) {
            remove();
            return;
        }

        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
