package com.onewhohears.dscombat.client.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.client.model.EntityModelParachute;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelAndolfSub;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelAxcelTruck;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelE3Sentry;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelMrBudgerTank;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelNathanBoat;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelNoahChopper;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelOrangeTesla;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelSmallRoller;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelTestPlane;
import com.onewhohears.dscombat.client.model.aircraft.EntityModelWoodenPlane;
import com.onewhohears.dscombat.client.model.obj.ObjEntityModels;
import com.onewhohears.dscombat.client.model.obj.custom.RandomModel;
import com.onewhohears.dscombat.client.model.weapon.EntityModelBomb1;
import com.onewhohears.dscombat.client.model.weapon.EntityModelBullet1;
import com.onewhohears.dscombat.client.model.weapon.EntityModelGruetzBB;
import com.onewhohears.dscombat.client.model.weapon.EntityModelHeavyTankTurret;
import com.onewhohears.dscombat.client.model.weapon.EntityModelMiniGunTurret;
import com.onewhohears.dscombat.client.model.weapon.EntityModelMissile1;
import com.onewhohears.dscombat.client.model.weapon.EntityModelSteveUpSmash;
import com.onewhohears.dscombat.client.overlay.VehicleOverlay;
import com.onewhohears.dscombat.client.particle.AfterBurnerParticle;
import com.onewhohears.dscombat.client.particle.BigFlameParticle;
import com.onewhohears.dscombat.client.particle.ContrailParticle;
import com.onewhohears.dscombat.client.particle.FlareParticle;
import com.onewhohears.dscombat.client.particle.LargeSmokeCloudParticle;
import com.onewhohears.dscombat.client.particle.ShrapnelParticle;
import com.onewhohears.dscombat.client.renderer.RendererEntityInvisible;
import com.onewhohears.dscombat.client.renderer.RendererObjEntity;
import com.onewhohears.dscombat.data.aircraft.AircraftClientPresets;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModParticles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
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
		//event.registerLayerDefinition(EntityModelAlexisPlane.LAYER_LOCATION, EntityModelAlexisPlane::createBodyLayer);
		//event.registerLayerDefinition(EntityModelJaviPlane.LAYER_LOCATION, EntityModelJaviPlane::createBodyLayer);
		//event.registerLayerDefinition(EntityModelLightMissileRack.LAYER_LOCATION, EntityModelLightMissileRack::createBodyLayer);
		//event.registerLayerDefinition(EntityModelHeavyMissileRack.LAYER_LOCATION, EntityModelHeavyMissileRack::createBodyLayer);
		//event.registerLayerDefinition(EntityModelXM12.LAYER_LOCATION, EntityModelXM12::createBodyLayer);
		event.registerLayerDefinition(EntityModelMiniGunTurret.LAYER_LOCATION, EntityModelMiniGunTurret::createBodyLayer);
		event.registerLayerDefinition(EntityModelMrBudgerTank.LAYER_LOCATION, EntityModelMrBudgerTank::createBodyLayer);
		event.registerLayerDefinition(EntityModelHeavyTankTurret.LAYER_LOCATION, EntityModelHeavyTankTurret::createBodyLayer);
		event.registerLayerDefinition(EntityModelSmallRoller.LAYER_LOCATION, EntityModelSmallRoller::createBodyLayer);
		event.registerLayerDefinition(EntityModelSteveUpSmash.LAYER_LOCATION, EntityModelSteveUpSmash::createBodyLayer);
		event.registerLayerDefinition(EntityModelNathanBoat.LAYER_LOCATION, EntityModelNathanBoat::createBodyLayer);
		event.registerLayerDefinition(EntityModelAndolfSub.LAYER_LOCATION, EntityModelAndolfSub::createBodyLayer);
		event.registerLayerDefinition(EntityModelOrangeTesla.LAYER_LOCATION, EntityModelOrangeTesla::createBodyLayer);
		event.registerLayerDefinition(EntityModelBomb1.LAYER_LOCATION, EntityModelBomb1::createBodyLayer);
		//event.registerLayerDefinition(EntityModelBombRack.LAYER_LOCATION, EntityModelBombRack::createBodyLayer);
		event.registerLayerDefinition(EntityModelWoodenPlane.LAYER_LOCATION, EntityModelWoodenPlane::createBodyLayer);
		event.registerLayerDefinition(EntityModelE3Sentry.LAYER_LOCATION, EntityModelE3Sentry::createBodyLayer);
		event.registerLayerDefinition(EntityModelAxcelTruck.LAYER_LOCATION, EntityModelAxcelTruck::createBodyLayer);
		//event.registerLayerDefinition(EntityModelSAMLauncher.LAYER_LOCATION, EntityModelSAMLauncher::createBodyLayer);
		//event.registerLayerDefinition(EntityModelCFM56.LAYER_LOCATION, EntityModelCFM56::createBodyLayer);
		event.registerLayerDefinition(EntityModelGruetzBB.LAYER_LOCATION, EntityModelGruetzBB::createBodyLayer);
		event.registerLayerDefinition(EntityModelParachute.LAYER_LOCATION, EntityModelParachute::createBodyLayer);
		//event.registerLayerDefinition(InWorldScreenModel.LAYER_LOCATION, InWorldScreenModel::createBodyLayer);
	}
	
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		EntityModelSet models = Minecraft.getInstance().getEntityModels();
		// PLANES
		event.registerEntityRenderer(ModEntities.JAVI_PLANE.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.ALEXIS_PLANE.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.WOODEN_PLANE.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.E3SENTRY_PLANE.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.BRONCO_PLANE.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.FELIX_PLANE.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.JASON_PLANE.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.EDEN_PLANE.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		// HELICOPTERS
		event.registerEntityRenderer(ModEntities.NOAH_CHOPPER.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		// TANKS
		event.registerEntityRenderer(ModEntities.MRBUDGER_TANK.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.SMALL_ROLLER.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.ORANGE_TESLA.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.AXCEL_TRUCK.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		// BOATS
		event.registerEntityRenderer(ModEntities.NATHAN_BOAT.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.GRONK_BATTLESHIP.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.DESTROYER.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.CRUISER.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.CORVETTE.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.AIRCRAFT_CARRIER.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		// SUBMARINES
		event.registerEntityRenderer(ModEntities.ANDOLF_SUB.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.GOOGLE_SUB.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		// BULLETS
		event.registerEntityRenderer(ModEntities.BULLET.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		// BOMBS
		event.registerEntityRenderer(ModEntities.BOMB.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.ANM30.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.MARK77.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		// BUNKER BUSTERS
		event.registerEntityRenderer(ModEntities.GRUETZ_BUNKER_BUSTER.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		// MISSILES
		event.registerEntityRenderer(ModEntities.AIM9L.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.AIM9P5.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.AIM9X.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.AIM120B.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.AIM120C.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.PAC3.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.AIM7F.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.AIM7MH.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.AGM114K.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.AGM84E.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.AGM65L.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.AGM65G.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.METEOR.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.MK13.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.AGM88G.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.POS_MISSILE_1.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.IR_MISSILE_1.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.TRACK_MISSILE_1.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.ANTI_RADAR_MISSILE_1.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.TORPEDO_MISSILE_1.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		// TURRETS
		event.registerEntityRenderer(ModEntities.MINIGUN_TURRET.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.HEAVY_TANK_TURRET.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.STEVE_UP_SMASH.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.SAM_LAUNCHER.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.MLS.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.TORPEDO_TUBES.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.AA_TURRET.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.CIWS.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.MARK7_CANNON.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.MARK45_CANNON.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		// RADARS
		event.registerEntityRenderer(ModEntities.AIR_SCAN_A.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.AIR_SCAN_B.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.SURVEY_ALL_A.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.SURVEY_ALL_B.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		// MISSILE RACKS
		event.registerEntityRenderer(ModEntities.XM12.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.LIGHT_MISSILE_RACK.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.HEAVY_MISSILE_RACK.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.BOMB_RACK.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.ADL.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.VLS.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		// EXTERNAL ENGINES
		event.registerEntityRenderer(ModEntities.CFM56.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		// OTHER
		event.registerEntityRenderer(ModEntities.SEAT.get(), RendererEntityInvisible::new);
		event.registerEntityRenderer(ModEntities.FLARE.get(), RendererEntityInvisible::new);
		event.registerEntityRenderer(ModEntities.GIMBAL_CAMERA.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
		event.registerEntityRenderer(ModEntities.PARACHUTE.get(), 
				(context) -> new RendererObjEntity<>(context, new RandomModel()));
	}
	
	@SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerBelowAll("aircraft_stats", VehicleOverlay.HUD_Aircraft_Stats);
    }
	
	@SubscribeEvent
	public static void registerClientReloadListener(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(AircraftClientPresets.get());
		event.registerReloadListener(ObjEntityModels.get());
	}
	
	@SubscribeEvent
	public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
		event.register(ModParticles.LARGE_SMOKE_CLOUD.get(), LargeSmokeCloudParticle.Provider::new);
		event.register(ModParticles.SHRAPNEL.get(), ShrapnelParticle.Provider::new);
		event.register(ModParticles.BIG_FLAME.get(), BigFlameParticle.Provider::new);
		event.register(ModParticles.CONTRAIL.get(), ContrailParticle.Provider::new);
		event.register(ModParticles.AFTER_BURNER.get(), AfterBurnerParticle.Provider::new);
		event.register(ModParticles.FLARE.get(), FlareParticle.Provider::new);
	}
}
