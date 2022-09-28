package com.onewhohears.dscombat.client.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.KeyInit;
import com.onewhohears.dscombat.client.renderer.RendererEntityAbstractAircraft;
import com.onewhohears.dscombat.client.renderer.RendererEntityAbstractWeapon;
import com.onewhohears.dscombat.client.renderer.RendererEntitySeat;
import com.onewhohears.dscombat.client.renderer.RendererEntitySeatCamera;
import com.onewhohears.dscombat.client.renderer.model.EntityModelTestPlane;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
	
	private ClientModEvents() {
	}
	
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		KeyInit.init();
	}
	
	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(EntityModelTestPlane.LAYER_LOCATION, EntityModelTestPlane::createBodyLayer);
	}
	
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntities.TEST_PLANE.get(), RendererEntityAbstractAircraft::new);
		event.registerEntityRenderer(ModEntities.SEAT.get(), RendererEntitySeat::new);
		event.registerEntityRenderer(ModEntities.CAMERA.get(), RendererEntitySeatCamera::new);
		event.registerEntityRenderer(ModEntities.BULLET.get(), RendererEntityAbstractWeapon::new);
		event.registerEntityRenderer(ModEntities.MISSILE.get(), RendererEntityAbstractWeapon::new);
	}
}
