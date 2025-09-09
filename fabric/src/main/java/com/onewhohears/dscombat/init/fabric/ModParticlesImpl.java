package com.onewhohears.dscombat.init.fabric;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;

public class ModParticlesImpl {

    public static SimpleParticleType createParticleType(boolean alwaysSpawn) {
        return FabricParticleTypes.simple(alwaysSpawn);
    }

}
