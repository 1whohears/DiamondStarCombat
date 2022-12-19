package com.onewhohears.dscombat.client.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.KeyInit;
import com.onewhohears.dscombat.client.overlay.PilotOverlay;
import com.onewhohears.dscombat.client.renderer.RendererEntityAbstractAircraft;
import com.onewhohears.dscombat.client.renderer.RendererEntityAbstractWeapon;
import com.onewhohears.dscombat.client.renderer.RendererEntityInvisible;
import com.onewhohears.dscombat.client.renderer.RendererEntityWeaponRack;
import com.onewhohears.dscombat.client.renderer.model.aircraft.EntityModelAlexisPlane;
import com.onewhohears.dscombat.client.renderer.model.aircraft.EntityModelF16;
import com.onewhohears.dscombat.client.renderer.model.aircraft.EntityModelJaviPlane;
import com.onewhohears.dscombat.client.renderer.model.aircraft.EntityModelNoahChopper;
import com.onewhohears.dscombat.client.renderer.model.aircraft.EntityModelTestPlane;
import com.onewhohears.dscombat.client.renderer.model.weapon.EntityModelBullet1;
import com.onewhohears.dscombat.client.renderer.model.weapon.EntityModelLightMissileRack;
import com.onewhohears.dscombat.client.renderer.model.weapon.EntityModelMissile1;
import com.onewhohears.dscombat.entity.aircraft.EntityHelicopter;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.dscombat.entity.weapon.EntityBullet;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
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
		event.registerLayerDefinition(EntityModelNoahChopper.LAYER_LOCATION, EntityModelNoahChopper::createBodyLayer);
		event.registerLayerDefinition(EntityModelAlexisPlane.LAYER_LOCATION, EntityModelAlexisPlane::createBodyLayer);
		event.registerLayerDefinition(EntityModelJaviPlane.LAYER_LOCATION, EntityModelJaviPlane::createBodyLayer);
		event.registerLayerDefinition(EntityModelLightMissileRack.LAYER_LOCATION, EntityModelLightMissileRack::createBodyLayer);
	}
	
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		EntityModelSet models = Minecraft.getInstance().getEntityModels();
		/*event.registerEntityRenderer(ModEntities.TEST_PLANE.get(), 
				(context) -> new RendererEntityAbstractAircraft<EntityPlane>(context, 
						new EntityModelTestPlane<EntityPlane>(models.bakeLayer(EntityModelTestPlane.LAYER_LOCATION))));*/
		
		event.registerEntityRenderer(ModEntities.JAVI_PLANE.get(), 
				(context) -> new RendererEntityAbstractAircraft<EntityPlane>(context, 
						new EntityModelJaviPlane<EntityPlane>(models.bakeLayer(EntityModelJaviPlane.LAYER_LOCATION))));
		
		event.registerEntityRenderer(ModEntities.ALEXIS_PLANE.get(), 
				(context) -> new RendererEntityAbstractAircraft<EntityPlane>(context, 
						new EntityModelAlexisPlane<EntityPlane>(models.bakeLayer(EntityModelAlexisPlane.LAYER_LOCATION))));
		
		/*event.registerEntityRenderer(ModEntities.F16.get(), 
				(context) -> new RendererEntityAbstractAircraft<EntityPlane>(context, 
						new EntityModelF16<EntityPlane>(models.bakeLayer(EntityModelF16.LAYER_LOCATION))));*/
		
		event.registerEntityRenderer(ModEntities.NOAH_CHOPPER.get(), 
				(context) -> new RendererEntityAbstractAircraft<EntityHelicopter>(context, 
						new EntityModelNoahChopper<EntityHelicopter>(models.bakeLayer(EntityModelNoahChopper.LAYER_LOCATION))));
		
		event.registerEntityRenderer((EntityType<EntityBullet>)ModEntities.BULLET.get(), 
				(context) -> new RendererEntityAbstractWeapon<EntityBullet>(context, 
						new EntityModelBullet1<EntityBullet>(models.bakeLayer(EntityModelBullet1.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/bullet1.png")));
		
		event.registerEntityRenderer(ModEntities.POS_MISSILE_1.get(), 
				(context) -> new RendererEntityAbstractWeapon<EntityMissile>(context, 
						new EntityModelMissile1<EntityMissile>(models.bakeLayer(EntityModelMissile1.LAYER_LOCATION)), 
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/missile1.png")));
		event.registerEntityRenderer(ModEntities.IR_MISSILE_1.get(), 
				(context) -> new RendererEntityAbstractWeapon<EntityMissile>(context, 
						new EntityModelMissile1<EntityMissile>(models.bakeLayer(EntityModelMissile1.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/missile3.png")));
		event.registerEntityRenderer(ModEntities.TRACK_MISSILE_1.get(), 
				(context) -> new RendererEntityAbstractWeapon<EntityMissile>(context, 
						new EntityModelMissile1<EntityMissile>(models.bakeLayer(EntityModelMissile1.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/missile2.png")));
		
		event.registerEntityRenderer(ModEntities.LIGHT_MISSILE_RACK.get(), 
				(context) -> new RendererEntityWeaponRack<EntityWeaponRack>(context,
						new EntityModelLightMissileRack<EntityWeaponRack>(models.bakeLayer(EntityModelLightMissileRack.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/light_missile_rack.png")));
		event.registerEntityRenderer(ModEntities.HEAVY_MISSILE_RACK.get(), 
				(context) -> new RendererEntityWeaponRack<EntityWeaponRack>(context,
						new EntityModelLightMissileRack<EntityWeaponRack>(models.bakeLayer(EntityModelLightMissileRack.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/light_missile_rack.png")));
		
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
