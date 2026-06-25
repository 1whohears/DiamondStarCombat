package com.onewhohears.dscombat.init.forge;

import net.minecraft.core.particles.SimpleParticleType;

public class ModParticlesImpl {

    public static SimpleParticleType createParticleType(boolean alwaysSpawn) {
        return new SimpleParticleType(alwaysSpawn);
    }

}
