package com.onewhohears.dscombat.common.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.particle.BigFlameParticle;
import com.onewhohears.dscombat.client.particle.ContrailParticle;
import com.onewhohears.dscombat.client.particle.LargeSmokeCloudParticle;
import com.onewhohears.dscombat.client.particle.ShrapnelParticle;
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
		event.register(ModParticles.LARGE_SMOKE_CLOUD.get(), LargeSmokeCloudParticle.Provider::new);
		event.register(ModParticles.SHRAPNEL.get(), ShrapnelParticle.Provider::new);
		event.register(ModParticles.BIG_FLAME.get(), BigFlameParticle.Provider::new);
		event.register(ModParticles.CONTRAIL.get(), ContrailParticle.Provider::new);
	}
	
}
