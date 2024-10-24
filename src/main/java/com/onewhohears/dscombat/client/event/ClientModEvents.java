package com.onewhohears.dscombat.client.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.entityscreen.EntityScreenIds;
import com.onewhohears.dscombat.client.entityscreen.EntityScreenTypes;
import com.onewhohears.dscombat.client.entityscreen.instance.*;
import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.client.model.obj.HardCodedModelAnims;
import com.onewhohears.dscombat.client.model.obj.ObjPartModel;
import com.onewhohears.dscombat.client.model.obj.custom.AATurretModel;
import com.onewhohears.dscombat.client.model.obj.custom.BallRadarModel;
import com.onewhohears.dscombat.client.model.obj.custom.BombRackModel;
import com.onewhohears.dscombat.client.model.obj.custom.CIWSModel;
import com.onewhohears.dscombat.client.model.obj.custom.ChainHookModel;
import com.onewhohears.dscombat.client.model.obj.custom.GimbalCameraModel;
import com.onewhohears.dscombat.client.model.obj.custom.HeavyMissileRackModel;
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
import com.onewhohears.dscombat.client.model.obj.customanims.DSCAnimControl;
import com.onewhohears.dscombat.client.model.obj.customanims.VehicleModelTransforms;
import com.onewhohears.dscombat.client.model.weapon.EntityModelBomb1;
import com.onewhohears.dscombat.client.model.weapon.EntityModelBullet1;
import com.onewhohears.dscombat.client.model.weapon.EntityModelGruetzBB;
import com.onewhohears.dscombat.client.model.weapon.EntityModelHeavyTankTurret;
import com.onewhohears.dscombat.client.model.weapon.EntityModelMiniGunTurret;
import com.onewhohears.dscombat.client.model.weapon.EntityModelMissile1;
import com.onewhohears.dscombat.client.model.weapon.EntityModelSteveUpSmash;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.client.particle.AfterBurnerParticle;
import com.onewhohears.dscombat.client.particle.BigFlameParticle;
import com.onewhohears.dscombat.client.particle.ContrailParticle;
import com.onewhohears.dscombat.client.particle.FlareParticle;
import com.onewhohears.dscombat.client.particle.LargeSmokeCloudParticle;
import com.onewhohears.dscombat.client.particle.ShrapnelParticle;
import com.onewhohears.dscombat.client.renderer.RendererEntityInvisible;
import com.onewhohears.dscombat.client.renderer.RendererEntityTurret;
import com.onewhohears.dscombat.client.renderer.RendererEntityWeapon;
import com.onewhohears.dscombat.client.screen.VehicleBlockScreen;
import com.onewhohears.dscombat.client.screen.VehiclePartsScreen;
import com.onewhohears.dscombat.client.screen.VehicleStorageScreen;
import com.onewhohears.dscombat.client.screen.WeaponsBlockScreen;
import com.onewhohears.dscombat.init.ModContainers;
import com.onewhohears.dscombat.init.ModFluids;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModel;
import com.onewhohears.onewholibs.client.model.obj.customanims.CustomAnims;
import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.ControllableAnimPlayer;
import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.KFAnimPlayers;
import com.onewhohears.onewholibs.client.renderer.RendererObjEntity;
import com.onewhohears.dscombat.client.renderer.RendererObjVehicle;
import com.onewhohears.dscombat.client.renderer.RendererObjWeapon;
import com.onewhohears.dscombat.data.vehicle.client.VehicleClientPresets;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModParticles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
	
	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		DSCKeys.init(event);
	}
	
	@SubscribeEvent
	public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		//event.registerLayerDefinition(EntityModelTestPlane.LAYER_LOCATION, EntityModelTestPlane::createBodyLayer);
		event.registerLayerDefinition(EntityModelBullet1.LAYER_LOCATION, EntityModelBullet1::createBodyLayer);
		event.registerLayerDefinition(EntityModelMissile1.LAYER_LOCATION, EntityModelMissile1::createBodyLayer);
		//event.registerLayerDefinition(EntityModelF16.LAYER_LOCATION, EntityModelF16::createBodyLayer);
		//event.registerLayerDefinition(EntityModelNoahChopper.LAYER_LOCATION, EntityModelNoahChopper::createBodyLayer);
		//event.registerLayerDefinition(EntityModelAlexisPlane.LAYER_LOCATION, EntityModelAlexisPlane::createBodyLayer);
		//event.registerLayerDefinition(EntityModelJaviPlane.LAYER_LOCATION, EntityModelJaviPlane::createBodyLayer);
		//event.registerLayerDefinition(EntityModelLightMissileRack.LAYER_LOCATION, EntityModelLightMissileRack::createBodyLayer);
		//event.registerLayerDefinition(EntityModelHeavyMissileRack.LAYER_LOCATION, EntityModelHeavyMissileRack::createBodyLayer);
		//event.registerLayerDefinition(EntityModelXM12.LAYER_LOCATION, EntityModelXM12::createBodyLayer);
		event.registerLayerDefinition(EntityModelMiniGunTurret.LAYER_LOCATION, EntityModelMiniGunTurret::createBodyLayer);
		//event.registerLayerDefinition(EntityModelMrBudgerTank.LAYER_LOCATION, EntityModelMrBudgerTank::createBodyLayer);
		event.registerLayerDefinition(EntityModelHeavyTankTurret.LAYER_LOCATION, EntityModelHeavyTankTurret::createBodyLayer);
		//event.registerLayerDefinition(EntityModelSmallRoller.LAYER_LOCATION, EntityModelSmallRoller::createBodyLayer);
		event.registerLayerDefinition(EntityModelSteveUpSmash.LAYER_LOCATION, EntityModelSteveUpSmash::createBodyLayer);
		//event.registerLayerDefinition(EntityModelNathanBoat.LAYER_LOCATION, EntityModelNathanBoat::createBodyLayer);
		//event.registerLayerDefinition(EntityModelAndolfSub.LAYER_LOCATION, EntityModelAndolfSub::createBodyLayer);
		//event.registerLayerDefinition(EntityModelOrangeTesla.LAYER_LOCATION, EntityModelOrangeTesla::createBodyLayer);
		event.registerLayerDefinition(EntityModelBomb1.LAYER_LOCATION, EntityModelBomb1::createBodyLayer);
		//event.registerLayerDefinition(EntityModelBombRack.LAYER_LOCATION, EntityModelBombRack::createBodyLayer);
		//event.registerLayerDefinition(EntityModelWoodenPlane.LAYER_LOCATION, EntityModelWoodenPlane::createBodyLayer);
		//event.registerLayerDefinition(EntityModelE3Sentry.LAYER_LOCATION, EntityModelE3Sentry::createBodyLayer);
		//event.registerLayerDefinition(EntityModelAxcelTruck.LAYER_LOCATION, EntityModelAxcelTruck::createBodyLayer);
		//event.registerLayerDefinition(EntityModelSAMLauncher.LAYER_LOCATION, EntityModelSAMLauncher::createBodyLayer);
		//event.registerLayerDefinition(EntityModelCFM56.LAYER_LOCATION, EntityModelCFM56::createBodyLayer);
		event.registerLayerDefinition(EntityModelGruetzBB.LAYER_LOCATION, EntityModelGruetzBB::createBodyLayer);
		//event.registerLayerDefinition(EntityModelParachute.LAYER_LOCATION, EntityModelParachute::createBodyLayer);
		//event.registerLayerDefinition(InWorldScreenModel.LAYER_LOCATION, InWorldScreenModel::createBodyLayer);
	}
	
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		EntityModelSet models = Minecraft.getInstance().getEntityModels();
		event.registerEntityRenderer(ModEntities.PLANE.get(), RendererObjVehicle::new);
		event.registerEntityRenderer(ModEntities.HELICOPTER.get(), RendererObjVehicle::new);
		event.registerEntityRenderer(ModEntities.CAR.get(), RendererObjVehicle::new);
		event.registerEntityRenderer(ModEntities.BOAT.get(), RendererObjVehicle::new);
		event.registerEntityRenderer(ModEntities.SUBMARINE.get(), RendererObjVehicle::new);
		// BULLETS
		event.registerEntityRenderer(ModEntities.BULLET.get(), RendererObjWeapon::new);
		// BOMBS
		event.registerEntityRenderer(ModEntities.BOMB.get(), RendererObjWeapon::new);
		// BUNKER BUSTERS
		event.registerEntityRenderer(ModEntities.BUNKER_BUSTER.get(), 
				(context) -> new RendererEntityWeapon<>(context,
                        new EntityModelGruetzBB(models.bakeLayer(EntityModelGruetzBB.LAYER_LOCATION)),
                        new ResourceLocation(DSCombatMod.MODID, "textures/entity/weapon/gruetz_bunker_buster.png")));
		// MISSILES
		event.registerEntityRenderer(ModEntities.POS_MISSILE.get(), RendererObjWeapon::new);
		event.registerEntityRenderer(ModEntities.IR_MISSILE.get(), RendererObjWeapon::new);
		event.registerEntityRenderer(ModEntities.TRACK_MISSILE.get(), RendererObjWeapon::new);
		event.registerEntityRenderer(ModEntities.ANTI_RADAR_MISSILE.get(), RendererObjWeapon::new);
		event.registerEntityRenderer(ModEntities.TORPEDO_MISSILE.get(), RendererObjWeapon::new);
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
		event.registerEntityRenderer(ModEntities.ROTABLE_HITBOX.get(), RendererEntityInvisible::new);
		event.registerEntityRenderer(ModEntities.CHAIN_HOOK.get(), 
				(context) -> new RendererObjEntity<>(context, new ChainHookModel("chain_hook")));
		event.registerEntityRenderer(ModEntities.GIMBAL_CAMERA.get(), 
				(context) -> new RendererObjEntity<>(context, new GimbalCameraModel()));
		event.registerEntityRenderer(ModEntities.PARACHUTE.get(), 
				(context) -> new RendererObjEntity<>(context, new ObjEntityModel<>("parachute")));
	}
	
	@SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        VehicleOverlayComponent.registerOverlays(event);
    }
	
	@SubscribeEvent
	public static void registerClientReloadListener(RegisterClientReloadListenersEvent event) {
		HardCodedModelAnims.reload();
		event.registerReloadListener(VehicleClientPresets.get());
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

	@SubscribeEvent
	public static void registerItemMenuScreens(FMLClientSetupEvent event) {
		MenuScreens.register(ModContainers.VEHICLE_PARTS_MENU.get(), VehiclePartsScreen::new);
		MenuScreens.register(ModContainers.WEAPONS_BLOCK_MENU.get(), WeaponsBlockScreen::new);
		MenuScreens.register(ModContainers.AIRCRAFT_BLOCK_MENU.get(), VehicleBlockScreen::new);
		MenuScreens.register(ModContainers.VEHICLE_STORAGE_MENU_9x0.get(), VehicleStorageScreen::new);
		MenuScreens.register(ModContainers.VEHICLE_STORAGE_MENU_9x1.get(), VehicleStorageScreen::new);
		MenuScreens.register(ModContainers.VEHICLE_STORAGE_MENU_9x2.get(), VehicleStorageScreen::new);
		MenuScreens.register(ModContainers.VEHICLE_STORAGE_MENU_9x3.get(), VehicleStorageScreen::new);
		MenuScreens.register(ModContainers.VEHICLE_STORAGE_MENU_9x4.get(), VehicleStorageScreen::new);
		MenuScreens.register(ModContainers.VEHICLE_STORAGE_MENU_9x5.get(), VehicleStorageScreen::new);
		MenuScreens.register(ModContainers.VEHICLE_STORAGE_MENU_9x6.get(), VehicleStorageScreen::new);
	}

	@SubscribeEvent
	public static void registerCustomAnims(FMLClientSetupEvent event) {
		CustomAnims.addAnim("input_bound_translation", VehicleModelTransforms.InputBoundTranslation::new);
		CustomAnims.addAnim("motor_rotation", VehicleModelTransforms.MotorRotation::new);
		CustomAnims.addAnim("wheel_rotation", VehicleModelTransforms.WheelRotation::new);
		CustomAnims.addAnim("input_bound_rotation", VehicleModelTransforms.InputBoundRotation::new);
		CustomAnims.addAnim("spinning_radar", VehicleModelTransforms.SpinningRadar::new);
		CustomAnims.addAnim("landing_gear", VehicleModelTransforms.LandingGear::new);
		CustomAnims.addAnim("hitbox_destroy_part", VehicleModelTransforms.HitboxDestroyPart::new);
		CustomAnims.addAnim("plane_flap_rotation", VehicleModelTransforms.PlaneFlapRotation::new);
	}

	@SubscribeEvent
	public static void registerKeyframeAnimationPlayers(FMLClientSetupEvent event) {
		KFAnimPlayers.addAnimationPlayerFactory("turret_shoot", (data) -> new ControllableAnimPlayer<>(data,
				DSCAnimControl.TURRET_SHOOT_TRIGGER, DSCAnimControl.TURRET_SHOOT_CONTROL));
		KFAnimPlayers.addAnimationPlayerFactory("turret_shoot_loop", (data) -> new ControllableAnimPlayer<>(data,
				DSCAnimControl.TURRET_SHOOT_LOOP_TRIGGER, DSCAnimControl.TURRET_SHOOT_LOOP_CONTROL));
		KFAnimPlayers.addAnimationPlayerFactory("turret_shoot_loop_end", (data) -> new ControllableAnimPlayer<>(data,
				DSCAnimControl.TURRET_SHOOT_LOOP_END_TRIGGER, DSCAnimControl.TURRET_SHOOT_LOOP_END_CONTROL));
		KFAnimPlayers.addAnimationPlayerFactory("vehicle_landing_gear", (data) -> new ControllableAnimPlayer<>(data,
				DSCAnimControl.LANDING_GEAR_TRIGGER, DSCAnimControl.LANDING_GEAR_CONTROL));
	}

	@SubscribeEvent
	public static void setFluidRenderLayer(FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(ModFluids.OIL_FLUID_SOURCE.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ModFluids.OIL_FLUID_FLOWING.get(), RenderType.translucent());
	}

	@SubscribeEvent
	public static void registerEntityScreens(FMLClientSetupEvent event) {
		// TODO 1.0 make a big radar screen for boats, ground vehicles (axcel truck), and stationary radars
		EntityScreenTypes.addScreenType(EntityScreenIds.AIR_RADAR_SCREEN, AirRadarScreenInstance::new, "00FFFF");
		EntityScreenTypes.addScreenType(EntityScreenIds.GROUND_RADAR_SCREEN, GroundRadarScreenInstance::new, "4CFF00");
		EntityScreenTypes.addScreenType(EntityScreenIds.FUEL_SCREEN, FuelScreenInstance::new, "7F0000");
		// TODO 1.2 what to do with hud screen?
		EntityScreenTypes.addScreenType(EntityScreenIds.HUD_SCREEN, HudScreenInstance::new, "");
		EntityScreenTypes.addScreenType(EntityScreenIds.RWR_SCREEN, RWRScreenInstance::new, "FF00DC");
		EntityScreenTypes.addScreenType(EntityScreenIds.HEADING_SCREEN, HeadingScreenInstance::new, "0026FF");
		// TODO 1.6.1 create turn coordinator screen
		// TODO 1.6.2 create attitude indicator screen
		// TODO 1.6.3 create aoa meter screen
		// TODO 1.7 create altitude screen
		EntityScreenTypes.addScreenType(EntityScreenIds.AIR_SPEED_SCREEN, SpeedScreenInstance::new, "FFD800");
		// TODO 1.9 create vertical speed screen
	}
	
}
