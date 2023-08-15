package com.onewhohears.dscombat.client.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.client.model.EntityModelParachute;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelAlexisPlane;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelAndolfSub;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelAxcelTruck;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelE3Sentry;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelJaviPlane;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelMrBudgerTank;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelNathanBoat;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelNoahChopper;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelOrangeTesla;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelSmallRoller;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelTestPlane;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelWoodenPlane;
import com.onewhohears.dscombat.client.model.parts.EntityModelCFM56;
import com.onewhohears.dscombat.client.model.weapon.EntityModelBomb1;
import com.onewhohears.dscombat.client.model.weapon.EntityModelBombRack;
import com.onewhohears.dscombat.client.model.weapon.EntityModelBullet1;
import com.onewhohears.dscombat.client.model.weapon.EntityModelGruetzBB;
import com.onewhohears.dscombat.client.model.weapon.EntityModelHeavyMissileRack;
import com.onewhohears.dscombat.client.model.weapon.EntityModelHeavyTankTurret;
import com.onewhohears.dscombat.client.model.weapon.EntityModelLightMissileRack;
import com.onewhohears.dscombat.client.model.weapon.EntityModelMiniGunTurret;
import com.onewhohears.dscombat.client.model.weapon.EntityModelMissile1;
import com.onewhohears.dscombat.client.model.weapon.EntityModelSAMLauncher;
import com.onewhohears.dscombat.client.model.weapon.EntityModelSteveUpSmash;
import com.onewhohears.dscombat.client.model.weapon.EntityModelXM12;
import com.onewhohears.dscombat.client.overlay.PilotOverlay;
import com.onewhohears.dscombat.client.renderer.RendererEntityAircraft;
import com.onewhohears.dscombat.client.renderer.RendererEntityInvisible;
import com.onewhohears.dscombat.client.renderer.RendererEntityParachute;
import com.onewhohears.dscombat.client.renderer.RendererEntityPart;
import com.onewhohears.dscombat.client.renderer.RendererEntityTurret;
import com.onewhohears.dscombat.client.renderer.RendererEntityWeapon;
import com.onewhohears.dscombat.data.aircraft.AircraftClientPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityBoat;
import com.onewhohears.dscombat.entity.aircraft.EntityGroundVehicle;
import com.onewhohears.dscombat.entity.aircraft.EntityHelicopter;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.entity.aircraft.EntitySubmarine;
import com.onewhohears.dscombat.entity.parts.EntityEngine;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.dscombat.entity.weapon.EntityBomb;
import com.onewhohears.dscombat.entity.weapon.EntityBullet;
import com.onewhohears.dscombat.entity.weapon.EntityBunkerBuster;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
	
	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		DSCKeys.init(event);
	}
	
	@SubscribeEvent
	public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(EntityModelTestPlane.LAYER_LOCATION, EntityModelTestPlane::createBodyLayer);
		event.registerLayerDefinition(EntityModelBullet1.LAYER_LOCATION, EntityModelBullet1::createBodyLayer);
		event.registerLayerDefinition(EntityModelMissile1.LAYER_LOCATION, EntityModelMissile1::createBodyLayer);
		//event.registerLayerDefinition(EntityModelF16.LAYER_LOCATION, EntityModelF16::createBodyLayer);
		event.registerLayerDefinition(EntityModelNoahChopper.LAYER_LOCATION, EntityModelNoahChopper::createBodyLayer);
		event.registerLayerDefinition(EntityModelAlexisPlane.LAYER_LOCATION, EntityModelAlexisPlane::createBodyLayer);
		event.registerLayerDefinition(EntityModelJaviPlane.LAYER_LOCATION, EntityModelJaviPlane::createBodyLayer);
		event.registerLayerDefinition(EntityModelLightMissileRack.LAYER_LOCATION, EntityModelLightMissileRack::createBodyLayer);
		event.registerLayerDefinition(EntityModelHeavyMissileRack.LAYER_LOCATION, EntityModelHeavyMissileRack::createBodyLayer);
		event.registerLayerDefinition(EntityModelXM12.LAYER_LOCATION, EntityModelXM12::createBodyLayer);
		event.registerLayerDefinition(EntityModelMiniGunTurret.LAYER_LOCATION, EntityModelMiniGunTurret::createBodyLayer);
		event.registerLayerDefinition(EntityModelMrBudgerTank.LAYER_LOCATION, EntityModelMrBudgerTank::createBodyLayer);
		event.registerLayerDefinition(EntityModelHeavyTankTurret.LAYER_LOCATION, EntityModelHeavyTankTurret::createBodyLayer);
		event.registerLayerDefinition(EntityModelSmallRoller.LAYER_LOCATION, EntityModelSmallRoller::createBodyLayer);
		event.registerLayerDefinition(EntityModelSteveUpSmash.LAYER_LOCATION, EntityModelSteveUpSmash::createBodyLayer);
		event.registerLayerDefinition(EntityModelNathanBoat.LAYER_LOCATION, EntityModelNathanBoat::createBodyLayer);
		event.registerLayerDefinition(EntityModelAndolfSub.LAYER_LOCATION, EntityModelAndolfSub::createBodyLayer);
		event.registerLayerDefinition(EntityModelOrangeTesla.LAYER_LOCATION, EntityModelOrangeTesla::createBodyLayer);
		event.registerLayerDefinition(EntityModelBomb1.LAYER_LOCATION, EntityModelBomb1::createBodyLayer);
		event.registerLayerDefinition(EntityModelBombRack.LAYER_LOCATION, EntityModelBombRack::createBodyLayer);
		event.registerLayerDefinition(EntityModelWoodenPlane.LAYER_LOCATION, EntityModelWoodenPlane::createBodyLayer);
		event.registerLayerDefinition(EntityModelE3Sentry.LAYER_LOCATION, EntityModelE3Sentry::createBodyLayer);
		event.registerLayerDefinition(EntityModelAxcelTruck.LAYER_LOCATION, EntityModelAxcelTruck::createBodyLayer);
		event.registerLayerDefinition(EntityModelSAMLauncher.LAYER_LOCATION, EntityModelSAMLauncher::createBodyLayer);
		event.registerLayerDefinition(EntityModelCFM56.LAYER_LOCATION, EntityModelCFM56::createBodyLayer);
		event.registerLayerDefinition(EntityModelGruetzBB.LAYER_LOCATION, EntityModelGruetzBB::createBodyLayer);
		event.registerLayerDefinition(EntityModelParachute.LAYER_LOCATION, EntityModelParachute::createBodyLayer);
	}
	
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		EntityModelSet models = Minecraft.getInstance().getEntityModels();
		// PLANES
		event.registerEntityRenderer(ModEntities.JAVI_PLANE.get(), 
				(context) -> new RendererEntityAircraft<EntityPlane>(context, 
						new EntityModelJaviPlane(models.bakeLayer(EntityModelJaviPlane.LAYER_LOCATION))));
		event.registerEntityRenderer(ModEntities.ALEXIS_PLANE.get(), 
				(context) -> new RendererEntityAircraft<EntityPlane>(context, 
						new EntityModelAlexisPlane(models.bakeLayer(EntityModelAlexisPlane.LAYER_LOCATION))));
		event.registerEntityRenderer(ModEntities.WOODEN_PLANE.get(), 
				(context) -> new RendererEntityAircraft<EntityPlane>(context, 
						new EntityModelWoodenPlane(models.bakeLayer(EntityModelWoodenPlane.LAYER_LOCATION))));
		event.registerEntityRenderer(ModEntities.E3SENTRY_PLANE.get(), 
				(context) -> new RendererEntityAircraft<EntityPlane>(context, 
						new EntityModelE3Sentry(models.bakeLayer(EntityModelE3Sentry.LAYER_LOCATION))));
		// HELICOPTERS
		event.registerEntityRenderer(ModEntities.NOAH_CHOPPER.get(), 
				(context) -> new RendererEntityAircraft<EntityHelicopter>(context, 
						new EntityModelNoahChopper(models.bakeLayer(EntityModelNoahChopper.LAYER_LOCATION))));
		// TANKS
		event.registerEntityRenderer(ModEntities.MRBUDGER_TANK.get(), 
				(context) -> new RendererEntityAircraft<EntityGroundVehicle>(context, 
						new EntityModelMrBudgerTank(models.bakeLayer(EntityModelMrBudgerTank.LAYER_LOCATION))));
		event.registerEntityRenderer(ModEntities.SMALL_ROLLER.get(), 
				(context) -> new RendererEntityAircraft<EntityGroundVehicle>(context, 
						new EntityModelSmallRoller(models.bakeLayer(EntityModelSmallRoller.LAYER_LOCATION))));
		event.registerEntityRenderer(ModEntities.ORANGE_TESLA.get(), 
				(context) -> new RendererEntityAircraft<EntityGroundVehicle>(context, 
						new EntityModelOrangeTesla(models.bakeLayer(EntityModelOrangeTesla.LAYER_LOCATION))));
		event.registerEntityRenderer(ModEntities.AXCEL_TRUCK.get(), 
				(context) -> new RendererEntityAircraft<EntityGroundVehicle>(context, 
						new EntityModelAxcelTruck(models.bakeLayer(EntityModelAxcelTruck.LAYER_LOCATION))));
		// BOATS
		event.registerEntityRenderer(ModEntities.NATHAN_BOAT.get(), 
				(context) -> new RendererEntityAircraft<EntityBoat>(context, 
						new EntityModelNathanBoat(models.bakeLayer(EntityModelNathanBoat.LAYER_LOCATION))));
		// SUBMARINES
		event.registerEntityRenderer(ModEntities.ANDOLF_SUB.get(), 
				(context) -> new RendererEntityAircraft<EntitySubmarine>(context, 
						new EntityModelAndolfSub(models.bakeLayer(EntityModelAndolfSub.LAYER_LOCATION))));
		// BULLETS
		event.registerEntityRenderer(ModEntities.BULLET.get(), 
				(context) -> new RendererEntityWeapon<EntityBullet>(context, 
						new EntityModelBullet1(models.bakeLayer(EntityModelBullet1.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/bullet1.png")));
		// BOMBS
		event.registerEntityRenderer(ModEntities.BOMB.get(), 
				(context) -> new RendererEntityWeapon<EntityBomb>(context, 
						new EntityModelBomb1(models.bakeLayer(EntityModelBomb1.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/bomb1.png")));
		// BUNKER BUSTERS
		event.registerEntityRenderer(ModEntities.GRUETZ_BUNKER_BUSTER.get(), 
				(context) -> new RendererEntityWeapon<EntityBunkerBuster>(context, 
						new EntityModelGruetzBB(models.bakeLayer(EntityModelGruetzBB.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entity/weapon/gruetz_bunker_buster.png")));
		// MISSILES
		event.registerEntityRenderer(ModEntities.POS_MISSILE_1.get(), 
				(context) -> new RendererEntityWeapon<EntityMissile>(context, 
						new EntityModelMissile1(models.bakeLayer(EntityModelMissile1.LAYER_LOCATION)), 
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/missile1.png")));
		event.registerEntityRenderer(ModEntities.IR_MISSILE_1.get(), 
				(context) -> new RendererEntityWeapon<EntityMissile>(context, 
						new EntityModelMissile1(models.bakeLayer(EntityModelMissile1.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/missile3.png")));
		event.registerEntityRenderer(ModEntities.TRACK_MISSILE_1.get(), 
				(context) -> new RendererEntityWeapon<EntityMissile>(context, 
						new EntityModelMissile1(models.bakeLayer(EntityModelMissile1.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/missile2.png")));
		event.registerEntityRenderer(ModEntities.ANTI_RADAR_MISSILE_1.get(), 
				(context) -> new RendererEntityWeapon<EntityMissile>(context, 
						new EntityModelMissile1(models.bakeLayer(EntityModelMissile1.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/missile4.png")));
		event.registerEntityRenderer(ModEntities.TORPEDO_MISSILE_1.get(), 
				(context) -> new RendererEntityWeapon<EntityMissile>(context,
						new EntityModelMissile1(models.bakeLayer(EntityModelMissile1.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/missile5.png")));
		// TURRETS
		event.registerEntityRenderer(ModEntities.MINIGUN_TURRET.get(), 
				(context) -> new RendererEntityTurret<EntityTurret>(context, 
						new EntityModelMiniGunTurret(models.bakeLayer(EntityModelMiniGunTurret.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/minigun_turret.png")));
		event.registerEntityRenderer(ModEntities.HEAVY_TANK_TURRET.get(), 
				(context) -> new RendererEntityTurret<EntityTurret>(context, 
						new EntityModelHeavyTankTurret(models.bakeLayer(EntityModelHeavyTankTurret.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/heavy_tank_turret.png")));
		event.registerEntityRenderer(ModEntities.STEVE_UP_SMASH.get(), 
				(context) -> new RendererEntityTurret<EntityTurret>(context, 
						new EntityModelSteveUpSmash(models.bakeLayer(EntityModelSteveUpSmash.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/steve_up_smash.png")));
		event.registerEntityRenderer(ModEntities.SAM_LAUNCHER.get(), 
				(context) -> new RendererEntityTurret<EntityTurret>(context, 
						new EntityModelSAMLauncher(models.bakeLayer(EntityModelSAMLauncher.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/sam_launcher.png")));
		// MISSILE RACKS
		event.registerEntityRenderer(ModEntities.LIGHT_MISSILE_RACK.get(), 
				(context) -> new RendererEntityPart<EntityWeaponRack>(context,
						new EntityModelLightMissileRack(models.bakeLayer(EntityModelLightMissileRack.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/light_missile_rack.png")));
		event.registerEntityRenderer(ModEntities.HEAVY_MISSILE_RACK.get(), 
				(context) -> new RendererEntityPart<EntityWeaponRack>(context,
						new EntityModelHeavyMissileRack(models.bakeLayer(EntityModelHeavyMissileRack.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/heavy_missile_rack.png")));
		event.registerEntityRenderer(ModEntities.XM12.get(), 
				(context) -> new RendererEntityPart<EntityWeaponRack>(context,
						new EntityModelXM12(models.bakeLayer(EntityModelXM12.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/xm12.png")));
		event.registerEntityRenderer(ModEntities.BOMB_RACK.get(), 
				(context) -> new RendererEntityPart<EntityWeaponRack>(context,
						new EntityModelBombRack(models.bakeLayer(EntityModelBombRack.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/bomb_rack.png")));
		// EXTERNAL ENGINES
		event.registerEntityRenderer(ModEntities.CFM56.get(), 
				(context) -> new RendererEntityPart<EntityEngine>(context,
						new EntityModelCFM56(models.bakeLayer(EntityModelCFM56.LAYER_LOCATION)),
						new ResourceLocation(DSCombatMod.MODID, "textures/entities/cfm56.png")));
		// OTHER
		event.registerEntityRenderer(ModEntities.SEAT.get(), RendererEntityInvisible::new);
		event.registerEntityRenderer(ModEntities.FLARE.get(), RendererEntityInvisible::new);
		event.registerEntityRenderer(ModEntities.PARACHUTE.get(), 
				(context) -> new RendererEntityParachute(context, 
						new EntityModelParachute(models.bakeLayer(EntityModelParachute.LAYER_LOCATION)), 
						new ResourceLocation(DSCombatMod.MODID, "textures/entity/part/parachute.png")));
	}
	
	@SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerBelowAll("aircraft_stats", PilotOverlay.HUD_Aircraft_Stats);
    }
	
	@SubscribeEvent
	public static void registerClientReloadListener(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(AircraftClientPresets.get());
	}
}
