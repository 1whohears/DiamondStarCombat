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
	
	public static final RegistryObject<SimpleParticleType> GIANT_EXPLOSION = PARTICLE_TYPES.register("giant_explosion", 
			() -> new SimpleParticleType(true));
	
}
