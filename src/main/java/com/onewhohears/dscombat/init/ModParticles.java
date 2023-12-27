package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
	
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, DSCombatMod.MODID);
	
	public static void register(IEventBus eventBus) {
		PARTICLE_TYPES.register(eventBus);
	}
	
	public static final RegistryObject<SimpleParticleType> LARGE_SMOKE_CLOUD = PARTICLE_TYPES.register("large_smoke_cloud", 
			() -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> SHRAPNEL = PARTICLE_TYPES.register("shrapnel", 
			() -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> BIG_FLAME = PARTICLE_TYPES.register("big_flame", 
			() -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> CONTRAIL = PARTICLE_TYPES.register("contrail", 
			() -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> AFTER_BURNER = PARTICLE_TYPES.register("after_burner", 
			() -> new SimpleParticleType(true));
	
}
