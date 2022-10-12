package com.onewhohears.dscombat.client.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.KeyInit;
import com.onewhohears.dscombat.client.overlay.PilotOverlay;
import com.onewhohears.dscombat.client.renderer.RendererEntityAbstractAircraft;
import com.onewhohears.dscombat.client.renderer.RendererEntityAbstractWeapon;
import com.onewhohears.dscombat.client.renderer.RendererEntityInvisible;
import com.onewhohears.dscombat.client.renderer.RendererEntityInvisible;
import com.onewhohears.dscombat.client.renderer.model.EntityModelBullet1;
import com.onewhohears.dscombat.client.renderer.model.EntityModelMissile1;
import com.onewhohears.dscombat.client.renderer.model.EntityModelTestPlane;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
	
	private ClientModEvents() {
	}
	
	/*@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		
	}*/
	
	@SubscribeEvent
	public static void clientSetup(RegisterKeyMappingsEvent event) {
		KeyInit.init(event);
	}
	
	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(EntityModelTestPlane.LAYER_LOCATION, EntityModelTestPlane::createBodyLayer);
		event.registerLayerDefinition(EntityModelBullet1.LAYER_LOCATION, EntityModelBullet1::createBodyLayer);
		event.registerLayerDefinition(EntityModelMissile1.LAYER_LOCATION, EntityModelMissile1::createBodyLayer);
	}
	
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntities.TEST_PLANE.get(), RendererEntityAbstractAircraft::new);
		event.registerEntityRenderer(ModEntities.SEAT.get(), RendererEntityInvisible::new);
		event.registerEntityRenderer(ModEntities.CAMERA.get(), RendererEntityInvisible::new);
		event.registerEntityRenderer(ModEntities.BULLET.get(), RendererEntityAbstractWeapon::new);
		event.registerEntityRenderer(ModEntities.MISSILE.get(), RendererEntityAbstractWeapon::new);
		event.registerEntityRenderer(ModEntities.FLARE.get(), RendererEntityInvisible::new);
	}
	
	@SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        //event.registerAboveAll("aircraft_stats", PilotOverlay.HUD_Aircraft_Stats);
        event.registerBelowAll("aircraft_stats", PilotOverlay.HUD_Aircraft_Stats);
    }
}
