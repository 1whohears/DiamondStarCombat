package com.onewhohears.dscombat.client.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.KeyInit;
import com.onewhohears.dscombat.client.overlay.PilotOverlay;
import com.onewhohears.dscombat.client.renderer.RendererEntityAbstractAircraft;
import com.onewhohears.dscombat.client.renderer.RendererEntityAbstractWeapon;
import com.onewhohears.dscombat.client.renderer.RendererEntityInvisible;
import com.onewhohears.dscombat.client.renderer.RendererEntityWeaponRack;
import com.onewhohears.dscombat.client.renderer.model.EntityModelBullet1;
import com.onewhohears.dscombat.client.renderer.model.EntityModelF16;
import com.onewhohears.dscombat.client.renderer.model.EntityModelMissile1;
import com.onewhohears.dscombat.client.renderer.model.EntityModelTestPlane;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.dscombat.entity.weapon.EntityBullet;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
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
	public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(EntityModelTestPlane.LAYER_LOCATION, EntityModelTestPlane::createBodyLayer);
		event.registerLayerDefinition(EntityModelBullet1.LAYER_LOCATION, EntityModelBullet1::createBodyLayer);
		event.registerLayerDefinition(EntityModelMissile1.LAYER_LOCATION, EntityModelMissile1::createBodyLayer);
		event.registerLayerDefinition(EntityModelF16.LAYER_LOCATION, EntityModelF16::createBodyLayer);
	}
	
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		EntityModelSet models = Minecraft.getInstance().getEntityModels();
		event.registerEntityRenderer(ModEntities.TEST_PLANE.get(), 
				(context) -> new RendererEntityAbstractAircraft<EntityPlane>(context, 
						new EntityModelTestPlane<EntityPlane>(models.bakeLayer(EntityModelTestPlane.LAYER_LOCATION))));
		event.registerEntityRenderer(ModEntities.F16.get(), 
				(context) -> new RendererEntityAbstractAircraft<EntityPlane>(context, 
						new EntityModelF16<EntityPlane>(models.bakeLayer(EntityModelF16.LAYER_LOCATION))));
		
		event.registerEntityRenderer(ModEntities.BULLET.get(), 
				(context) -> new RendererEntityAbstractWeapon<EntityBullet>(context, 
						new EntityModelBullet1<EntityBullet>(models.bakeLayer(EntityModelBullet1.LAYER_LOCATION))));
		
		event.registerEntityRenderer(ModEntities.MISSILE1.get(), 
				(context) -> new RendererEntityAbstractWeapon<EntityMissile>(context, 
						new EntityModelMissile1<EntityMissile>(models.bakeLayer(EntityModelMissile1.LAYER_LOCATION))));
		event.registerEntityRenderer(ModEntities.MISSILE2.get(), 
				(context) -> new RendererEntityAbstractWeapon<EntityMissile>(context, 
						new EntityModelMissile1<EntityMissile>(models.bakeLayer(EntityModelMissile1.LAYER_LOCATION))));
		event.registerEntityRenderer(ModEntities.MISSILE3.get(), 
				(context) -> new RendererEntityAbstractWeapon<EntityMissile>(context, 
						new EntityModelMissile1<EntityMissile>(models.bakeLayer(EntityModelMissile1.LAYER_LOCATION))));
		
		event.registerEntityRenderer(ModEntities.WEAPON_RACK.get(), 
				(context) -> new RendererEntityWeaponRack<EntityWeaponRack>(context));
		
		event.registerEntityRenderer(ModEntities.SEAT.get(), RendererEntityInvisible::new);
		event.registerEntityRenderer(ModEntities.CAMERA.get(), RendererEntityInvisible::new);
		event.registerEntityRenderer(ModEntities.FLARE.get(), RendererEntityInvisible::new);
	}
	
	@SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        //event.registerAboveAll("aircraft_stats", PilotOverlay.HUD_Aircraft_Stats);
        event.registerBelowAll("aircraft_stats", PilotOverlay.HUD_Aircraft_Stats);
    }
}
