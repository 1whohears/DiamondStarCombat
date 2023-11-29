package com.onewhohears.dscombat.common.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.particle.GiantExplosionParticles;
import com.onewhohears.dscombat.data.weapon.RadarTargetTypes;
import com.onewhohears.dscombat.init.ModParticles;

import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.MOD)
public final class CommonModEvents {
	
	@SubscribeEvent
	public static void loadModConfigEvent(ModConfigEvent.Loading event) {
		if (event.getConfig().getType() == Type.COMMON) RadarTargetTypes.get().readConfig();
	}
	
	@SubscribeEvent
	public static void reloadModConfigEvent(ModConfigEvent.Reloading event) {
		if (event.getConfig().getType() == Type.COMMON) RadarTargetTypes.get().readConfig();
	}
	
	@SubscribeEvent
	public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
		event.register(ModParticles.GIANT_EXPLOSION.get(), GiantExplosionParticles.Provider::new);
	}
	
}
