package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

public class ModParticles {
	
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(
            DSCombatMod.MODID, Registries.PARTICLE_TYPE);
	
    @ExpectPlatform
    public static SimpleParticleType createParticleType(boolean alwaysSpawn) {
        throw new AssertionError();
    }
    
	public static final RegistrySupplier<SimpleParticleType> LARGE_SMOKE_CLOUD = PARTICLE_TYPES.register(
            "large_smoke_cloud", () -> createParticleType(true));
	
	public static final RegistrySupplier<SimpleParticleType> SHRAPNEL = PARTICLE_TYPES.register(
            "shrapnel", () -> createParticleType(true));
	
	public static final RegistrySupplier<SimpleParticleType> BIG_FLAME = PARTICLE_TYPES.register(
            "big_flame", () -> createParticleType(true));
	
	public static final RegistrySupplier<SimpleParticleType> CONTRAIL = PARTICLE_TYPES.register(
            "contrail", () -> createParticleType(true));
	
	public static final RegistrySupplier<SimpleParticleType> AFTER_BURNER = PARTICLE_TYPES.register(
            "after_burner", () -> createParticleType(true));
	
	public static final RegistrySupplier<SimpleParticleType> FLARE = PARTICLE_TYPES.register(
            "flare", () -> createParticleType(true));

    public static void register() {
        PARTICLE_TYPES.register();
    }
}
