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
import com.onewhohears.dscombat.client.model.obj.ObjAircraftModel;
import com.onewhohears.dscombat.client.model.obj.ObjEntityModel;
import com.onewhohears.dscombat.client.model.obj.ObjEntityModels;
import com.onewhohears.dscombat.client.model.obj.ObjPartModel;
import com.onewhohears.dscombat.client.model.obj.custom.AATurretModel;
import com.onewhohears.dscombat.client.model.obj.custom.AlexisPlaneModel;
import com.onewhohears.dscombat.client.model.obj.custom.BallRadarModel;
import com.onewhohears.dscombat.client.model.obj.custom.BombRackModel;
import com.onewhohears.dscombat.client.model.obj.custom.BroncoPlaneModel;
import com.onewhohears.dscombat.client.model.obj.custom.CIWSModel;
import com.onewhohears.dscombat.client.model.obj.custom.CorvetteModel;
import com.onewhohears.dscombat.client.model.obj.custom.FelixPlaneModel;
import com.onewhohears.dscombat.client.model.obj.custom.HeavyMissileRackModel;
import com.onewhohears.dscombat.client.model.obj.custom.JasonPlaneModel;
import com.onewhohears.dscombat.client.model.obj.custom.JaviPlaneModel;
import com.onewhohears.dscombat.client.model.obj.custom.LightMissileRackModel;
import com.onewhohears.dscombat.client.model.obj.custom.MLSModel;
import com.onewhohears.dscombat.client.model.obj.custom.Mark45GunModel;
import com.onewhohears.dscombat.client.model.obj.custom.Mark7GunModel;
import com.onewhohears.dscombat.client.model.obj.custom.Radar1Model;
import com.onewhohears.dscombat.client.model.obj.custom.Radar2Model;
import com.onewhohears.dscombat.client.model.obj.custom.SamLauncherModel;
import com.onewhohears.dscombat.client.model.obj.custom.StickRadarModel;
import com.onewhohears.dscombat.client.model.obj.custom.TorpedoTubesModel;
import com.onewhohears.dscombat.client.model.obj.custom.VLSModel;
import com.onewhohears.dscombat.client.model.weapon.EntityModelBomb1;
import com.onewhohears.dscombat.client.model.weapon.EntityModelBullet1;
import com.onewhohears.dscombat.client.model.weapon.EntityModelGruetzBB;
import com.onewhohears.dscombat.client.model.weapon.EntityModelHeavyTankTurret;
import com.onewhohears.dscombat.client.model.weapon.EntityModelMiniGunTurret;
import com.onewhohears.dscombat.client.model.weapon.EntityModelMissile1;
import com.onewhohears.dscombat.client.model.weapon.EntityModelSteveUpSmash;
import com.onewhohears.dscombat.client.overlay.VehicleOverlay;
import com.onewhohears.dscombat.client.renderer.RendererEntityAircraft;
import com.onewhohears.dscombat.client.renderer.RendererEntityInvisible;
import com.onewhohears.dscombat.client.renderer.RendererEntityParachute;
import com.onewhohears.dscombat.client.renderer.RendererEntityTurret;
import com.onewhohears.dscombat.client.renderer.RendererEntityWeapon;
import com.onewhohears.dscombat.client.renderer.RendererObjAircraft;
import com.onewhohears.dscombat.client.renderer.RendererObjEntity;
import com.onewhohears.dscombat.data.aircraft.AircraftClientPresets;
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
				(context) -> new RendererObjAircraft<>(context,
                        new JaviPlaneModel()));
		event.registerEntityRenderer(ModEntities.ALEXIS_PLANE.get(), 
				(context) -> new RendererObjAircraft<>(context,
                        new AlexisPlaneModel()));
		event.registerEntityRenderer(ModEntities.WOODEN_PLANE.get(), 
				(context) -> new RendererEntityAircraft<>(context,
                        new EntityModelWoodenPlane(models.bakeLayer(EntityModelWoodenPlane.LAYER_LOCATION))));
		event.registerEntityRenderer(ModEntities.E3SENTRY_PLANE.get(), 
				(context) -> new RendererEntityAircraft<>(context,
                        new EntityModelE3Sentry(models.bakeLayer(EntityModelE3Sentry.LAYER_LOCATION))));
		event.registerEntityRenderer(ModEntities.BRONCO_PLANE.get(), 
				(context) -> new RendererObjAircraft<>(context,
                        new BroncoPlaneModel()));
		event.registerEntityRenderer(ModEntities.FELIX_PLANE.get(), 
				(context) -> new RendererObjAircraft<>(context,
                        new FelixPlaneModel()));
		event.registerEntityRenderer(ModEntities.JASON_PLANE.get(), 
				(context) -> new RendererObjAircraft<>(context,
                        new JasonPlaneModel()));
		// HELICOPTERS
		event.registerEntityRenderer(ModEntities.NOAH_CHOPPER.get(), 
				(context) -> new RendererEntityAircraft<>(context,
                        new EntityModelNoahChopper(models.bakeLayer(EntityModelNoahChopper.LAYER_LOCATION))));
		// TANKS
		event.registerEntityRenderer(ModEntities.MRBUDGER_TANK.get(), 
				(context) -> new RendererEntityAircraft<>(context,
                        new EntityModelMrBudgerTank(models.bakeLayer(EntityModelMrBudgerTank.LAYER_LOCATION))));
		event.registerEntityRenderer(ModEntities.SMALL_ROLLER.get(), 
				(context) -> new RendererEntityAircraft<>(context,
                        new EntityModelSmallRoller(models.bakeLayer(EntityModelSmallRoller.LAYER_LOCATION))));
		event.registerEntityRenderer(ModEntities.ORANGE_TESLA.get(), 
				(context) -> new RendererEntityAircraft<>(context,
                        new EntityModelOrangeTesla(models.bakeLayer(EntityModelOrangeTesla.LAYER_LOCATION))));
		event.registerEntityRenderer(ModEntities.AXCEL_TRUCK.get(), 
				(context) -> new RendererEntityAircraft<>(context,
                        new EntityModelAxcelTruck(models.bakeLayer(EntityModelAxcelTruck.LAYER_LOCATION))));
		// BOATS
		event.registerEntityRenderer(ModEntities.NATHAN_BOAT.get(), 
				(context) -> new RendererEntityAircraft<>(context,
                        new EntityModelNathanBoat(models.bakeLayer(EntityModelNathanBoat.LAYER_LOCATION))));
		event.registerEntityRenderer(ModEntities.GRONK_BATTLESHIP.get(), 
				(context) -> new RendererObjAircraft<>(context,
                        new ObjAircraftModel<>("battleship")));
		event.registerEntityRenderer(ModEntities.DESTROYER.get(), 
				(context) -> new RendererObjAircraft<>(context,
                        new ObjAircraftModel<>("destroyer")));
		event.registerEntityRenderer(ModEntities.CRUISER.get(), 
				(context) -> new RendererObjAircraft<>(context,
                        new ObjAircraftModel<>("cruiser")));
		event.registerEntityRenderer(ModEntities.CORVETTE.get(), 
				(context) -> new RendererObjAircraft<>(context,
                        new CorvetteModel()));
		event.registerEntityRenderer(ModEntities.AIRCRAFT_CARRIER.get(), 
				(context) -> new RendererObjAircraft<>(context,
                        new ObjAircraftModel<>("carrier")));
		// SUBMARINES
		event.registerEntityRenderer(ModEntities.ANDOLF_SUB.get(), 
				(context) -> new RendererEntityAircraft<>(context,
                        new EntityModelAndolfSub(models.bakeLayer(EntityModelAndolfSub.LAYER_LOCATION))));
		// BULLETS
		event.registerEntityRenderer(ModEntities.BULLET.get(), 
				(context) -> new RendererEntityWeapon<>(context,
                        new EntityModelBullet1(models.bakeLayer(EntityModelBullet1.LAYER_LOCATION)),
                        new ResourceLocation(DSCombatMod.MODID, "textures/entities/bullet1.png")));
		// BOMBS
		event.registerEntityRenderer(ModEntities.BOMB.get(), 
				(context) -> new RendererEntityWeapon<>(context,
                        new EntityModelBomb1(models.bakeLayer(EntityModelBomb1.LAYER_LOCATION)),
                        new ResourceLocation(DSCombatMod.MODID, "textures/entities/bomb1.png")));
		// BUNKER BUSTERS
		event.registerEntityRenderer(ModEntities.GRUETZ_BUNKER_BUSTER.get(), 
				(context) -> new RendererEntityWeapon<>(context,
                        new EntityModelGruetzBB(models.bakeLayer(EntityModelGruetzBB.LAYER_LOCATION)),
                        new ResourceLocation(DSCombatMod.MODID, "textures/entity/weapon/gruetz_bunker_buster.png")));
		// MISSILES
		event.registerEntityRenderer(ModEntities.AIM9L.get(), 
				(context) -> new RendererObjEntity<EntityMissile>(context, 
						new ObjEntityModel<>("aim9l")));
		event.registerEntityRenderer(ModEntities.AIM9P5.get(), 
				(context) -> new RendererObjEntity<EntityMissile>(context, 
						new ObjEntityModel<>("aim9p5")));
		event.registerEntityRenderer(ModEntities.AIM9X.get(), 
				(context) -> new RendererObjEntity<EntityMissile>(context, 
						new ObjEntityModel<>("aim9x")));
		event.registerEntityRenderer(ModEntities.AIM120B.get(), 
				(context) -> new RendererObjEntity<EntityMissile>(context, 
						new ObjEntityModel<>("aim120b")));
		event.registerEntityRenderer(ModEntities.AIM120C.get(), 
				(context) -> new RendererObjEntity<EntityMissile>(context, 
						new ObjEntityModel<>("aim120c")));
		event.registerEntityRenderer(ModEntities.PAC3.get(), 
				(context) -> new RendererObjEntity<EntityMissile>(context, 
						new ObjEntityModel<>("pac3")));
		event.registerEntityRenderer(ModEntities.AIM7F.get(), 
				(context) -> new RendererObjEntity<EntityMissile>(context, 
						new ObjEntityModel<>("aim7f")));
		event.registerEntityRenderer(ModEntities.AIM7MH.get(), 
				(context) -> new RendererObjEntity<EntityMissile>(context, 
						new ObjEntityModel<>("aim7mh")));
		event.registerEntityRenderer(ModEntities.AGM114K.get(), 
				(context) -> new RendererObjEntity<EntityMissile>(context, 
						new ObjEntityModel<>("agm114k")));
		event.registerEntityRenderer(ModEntities.AGM84E.get(), 
				(context) -> new RendererObjEntity<EntityMissile>(context, 
						new ObjEntityModel<>("agm84e")));
		event.registerEntityRenderer(ModEntities.AGM65L.get(), 
				(context) -> new RendererObjEntity<EntityMissile>(context, 
						new ObjEntityModel<>("agm65l")));
		event.registerEntityRenderer(ModEntities.AGM65G.get(), 
				(context) -> new RendererObjEntity<EntityMissile>(context, 
						new ObjEntityModel<>("agm65l")));
		event.registerEntityRenderer(ModEntities.METEOR.get(), 
				(context) -> new RendererObjEntity<EntityMissile>(context, 
						new ObjEntityModel<>("pac3")));
		event.registerEntityRenderer(ModEntities.MK13.get(), 
				(context) -> new RendererObjEntity<EntityMissile>(context, 
						new ObjEntityModel<>("mk13")));
		event.registerEntityRenderer(ModEntities.AGM88G.get(), 
				(context) -> new RendererObjEntity<EntityMissile>(context, 
						new ObjEntityModel<>("agm84e")));
		event.registerEntityRenderer(ModEntities.POS_MISSILE_1.get(), 
				(context) -> new RendererEntityWeapon<>(context,
                        new EntityModelMissile1(models.bakeLayer(EntityModelMissile1.LAYER_LOCATION)),
                        new ResourceLocation(DSCombatMod.MODID, "textures/entities/missile1.png")));
		event.registerEntityRenderer(ModEntities.IR_MISSILE_1.get(), 
				(context) -> new RendererEntityWeapon<>(context,
                        new EntityModelMissile1(models.bakeLayer(EntityModelMissile1.LAYER_LOCATION)),
                        new ResourceLocation(DSCombatMod.MODID, "textures/entities/missile3.png")));
		event.registerEntityRenderer(ModEntities.TRACK_MISSILE_1.get(), 
				(context) -> new RendererEntityWeapon<>(context,
                        new EntityModelMissile1(models.bakeLayer(EntityModelMissile1.LAYER_LOCATION)),
                        new ResourceLocation(DSCombatMod.MODID, "textures/entities/missile2.png")));
		event.registerEntityRenderer(ModEntities.ANTI_RADAR_MISSILE_1.get(), 
				(context) -> new RendererEntityWeapon<>(context,
                        new EntityModelMissile1(models.bakeLayer(EntityModelMissile1.LAYER_LOCATION)),
                        new ResourceLocation(DSCombatMod.MODID, "textures/entities/missile4.png")));
		event.registerEntityRenderer(ModEntities.TORPEDO_MISSILE_1.get(), 
				(context) -> new RendererEntityWeapon<>(context,
                        new EntityModelMissile1(models.bakeLayer(EntityModelMissile1.LAYER_LOCATION)),
                        new ResourceLocation(DSCombatMod.MODID, "textures/entities/missile5.png")));
		// TURRETS
		event.registerEntityRenderer(ModEntities.MINIGUN_TURRET.get(), 
				(context) -> new RendererEntityTurret<>(context,
                        new EntityModelMiniGunTurret(models.bakeLayer(EntityModelMiniGunTurret.LAYER_LOCATION)),
                        new ResourceLocation(DSCombatMod.MODID, "textures/entities/minigun_turret.png")));
		event.registerEntityRenderer(ModEntities.HEAVY_TANK_TURRET.get(), 
				(context) -> new RendererEntityTurret<>(context,
                        new EntityModelHeavyTankTurret(models.bakeLayer(EntityModelHeavyTankTurret.LAYER_LOCATION)),
                        new ResourceLocation(DSCombatMod.MODID, "textures/entities/heavy_tank_turret.png")));
		event.registerEntityRenderer(ModEntities.STEVE_UP_SMASH.get(), 
				(context) -> new RendererEntityTurret<>(context,
                        new EntityModelSteveUpSmash(models.bakeLayer(EntityModelSteveUpSmash.LAYER_LOCATION)),
                        new ResourceLocation(DSCombatMod.MODID, "textures/entities/steve_up_smash.png")));
		event.registerEntityRenderer(ModEntities.SAM_LAUNCHER.get(), 
				(context) -> new RendererObjEntity<>(context,
                        new SamLauncherModel()));
		event.registerEntityRenderer(ModEntities.MLS.get(), 
				(context) -> new RendererObjEntity<>(context,
                        new MLSModel()));
		event.registerEntityRenderer(ModEntities.TORPEDO_TUBES.get(), 
				(context) -> new RendererObjEntity<>(context,
                        new TorpedoTubesModel()));
		event.registerEntityRenderer(ModEntities.AA_TURRET.get(), 
				(context) -> new RendererObjEntity<>(context,
                        new AATurretModel()));
		event.registerEntityRenderer(ModEntities.CIWS.get(), 
				(context) -> new RendererObjEntity<>(context,
                        new CIWSModel()));
		event.registerEntityRenderer(ModEntities.MARK7_CANNON.get(), 
				(context) -> new RendererObjEntity<>(context,
                        new Mark7GunModel()));
		event.registerEntityRenderer(ModEntities.MARK45_CANNON.get(), 
				(context) -> new RendererObjEntity<>(context,
                        new Mark45GunModel()));
		// RADARS
		event.registerEntityRenderer(ModEntities.AIR_SCAN_A.get(), 
				(context) -> new RendererObjEntity<>(context,
                        new Radar1Model()));
		event.registerEntityRenderer(ModEntities.AIR_SCAN_B.get(), 
				(context) -> new RendererObjEntity<>(context,
                        new Radar2Model()));
		event.registerEntityRenderer(ModEntities.SURVEY_ALL_A.get(), 
				(context) -> new RendererObjEntity<>(context,
                        new StickRadarModel()));
		event.registerEntityRenderer(ModEntities.SURVEY_ALL_B.get(), 
				(context) -> new RendererObjEntity<>(context,
                        new BallRadarModel()));
		// MISSILE RACKS
		event.registerEntityRenderer(ModEntities.XM12.get(), 
				(context) -> new RendererObjEntity<>(context,
                        new ObjPartModel<>("xm12")));
		event.registerEntityRenderer(ModEntities.LIGHT_MISSILE_RACK.get(), 
				(context) -> new RendererObjEntity<>(context,
                        new LightMissileRackModel()));
		event.registerEntityRenderer(ModEntities.HEAVY_MISSILE_RACK.get(), 
				(context) -> new RendererObjEntity<>(context,
                        new HeavyMissileRackModel()));
		event.registerEntityRenderer(ModEntities.BOMB_RACK.get(), 
				(context) -> new RendererObjEntity<>(context,
                        new BombRackModel()));
		event.registerEntityRenderer(ModEntities.ADL.get(), 
				(context) -> new RendererObjEntity<>(context,
                        new ObjPartModel<>("adl")));
		event.registerEntityRenderer(ModEntities.VLS.get(), 
				(context) -> new RendererObjEntity<>(context,
                        new VLSModel()));
		// EXTERNAL ENGINES
		event.registerEntityRenderer(ModEntities.CFM56.get(), 
				(context) -> new RendererObjEntity<>(context,
                        new ObjPartModel<>("cfm56")));
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
        event.registerBelowAll("aircraft_stats", VehicleOverlay.HUD_Aircraft_Stats);
    }
	
	@SubscribeEvent
	public static void registerClientReloadListener(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(AircraftClientPresets.get());
		event.registerReloadListener(ObjEntityModels.get());
	}
}
