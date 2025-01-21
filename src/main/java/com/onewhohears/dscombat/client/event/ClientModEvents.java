package com.onewhohears.dscombat.client.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.entityscreen.EntityScreenIds;
import com.onewhohears.dscombat.client.entityscreen.EntityScreenTypes;
import com.onewhohears.dscombat.client.entityscreen.instance.*;
import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.client.model.obj.HardCodedModelAnims;
import com.onewhohears.dscombat.client.model.obj.custom.*;
import com.onewhohears.dscombat.client.model.obj.customanims.DSCAnimControl;
import com.onewhohears.dscombat.client.model.obj.customanims.VehicleModelTransforms;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.client.particle.AfterBurnerParticle;
import com.onewhohears.dscombat.client.particle.BigFlameParticle;
import com.onewhohears.dscombat.client.particle.ContrailParticle;
import com.onewhohears.dscombat.client.particle.FlareParticle;
import com.onewhohears.dscombat.client.particle.LargeSmokeCloudParticle;
import com.onewhohears.dscombat.client.particle.ShrapnelParticle;
import com.onewhohears.dscombat.client.renderer.RendererEntityInvisible;
import com.onewhohears.dscombat.client.screen.VehicleBlockScreen;
import com.onewhohears.dscombat.client.screen.VehiclePartsScreen;
import com.onewhohears.dscombat.client.screen.VehicleStorageScreen;
import com.onewhohears.dscombat.client.screen.WeaponsBlockScreen;
import com.onewhohears.dscombat.client.screen.WeaponPartsBlockScreen;
import com.onewhohears.dscombat.data.parts.client.PartAssets;
import com.onewhohears.dscombat.init.ModContainers;
import com.onewhohears.dscombat.init.ModFluids;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModel;
import com.onewhohears.onewholibs.client.model.obj.customanims.CustomAnims;
import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.ControllableAnimPlayer;
import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.KFAnimPlayers;
import com.onewhohears.onewholibs.client.renderer.RendererCustomAnimObjEntity;
import com.onewhohears.onewholibs.client.renderer.RendererObjEntity;
import com.onewhohears.dscombat.client.renderer.RendererObjVehicle;
import com.onewhohears.dscombat.client.renderer.RendererObjWeapon;
import com.onewhohears.dscombat.data.vehicle.client.VehicleClientPresets;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModParticles;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
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
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntities.PLANE.get(), RendererObjVehicle::new);
		event.registerEntityRenderer(ModEntities.HELICOPTER.get(), RendererObjVehicle::new);
		event.registerEntityRenderer(ModEntities.CAR.get(), RendererObjVehicle::new);
		event.registerEntityRenderer(ModEntities.BOAT.get(), RendererObjVehicle::new);
		event.registerEntityRenderer(ModEntities.SUBMARINE.get(), RendererObjVehicle::new);
		event.registerEntityRenderer(ModEntities.STATIONARY.get(), RendererObjVehicle::new);
		// BULLETS
		event.registerEntityRenderer(ModEntities.BULLET.get(), RendererObjWeapon::new);
		// BOMBS
		event.registerEntityRenderer(ModEntities.BOMB.get(), RendererObjWeapon::new);
		// BUNKER BUSTERS
		event.registerEntityRenderer(ModEntities.BUNKER_BUSTER.get(), RendererObjWeapon::new);
		// MISSILES
		event.registerEntityRenderer(ModEntities.POS_MISSILE.get(), RendererObjWeapon::new);
		event.registerEntityRenderer(ModEntities.IR_MISSILE.get(), RendererObjWeapon::new);
		event.registerEntityRenderer(ModEntities.TRACK_MISSILE.get(), RendererObjWeapon::new);
		event.registerEntityRenderer(ModEntities.ANTI_RADAR_MISSILE.get(), RendererObjWeapon::new);
		event.registerEntityRenderer(ModEntities.TORPEDO_MISSILE.get(), RendererObjWeapon::new);
		// PARTS
		event.registerEntityRenderer(ModEntities.TURRET.get(), RendererCustomAnimObjEntity::new);
		event.registerEntityRenderer(ModEntities.EXTERNAL_WEAPON_PART.get(), RendererCustomAnimObjEntity::new);
		event.registerEntityRenderer(ModEntities.EXTERNAL_ENGINE.get(), RendererCustomAnimObjEntity::new);
		event.registerEntityRenderer(ModEntities.EXTERNAL_RADAR.get(), RendererCustomAnimObjEntity::new);
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
		event.registerReloadListener(PartAssets.get());
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
		MenuScreens.register(ModContainers.WEAPON_PARTS_BLOCK_MENU.get(), WeaponPartsBlockScreen::new);
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
		EntityScreenTypes.addScreenType(EntityScreenIds.BIG_RADAR_SCREEN, BigRadarScreenInstance::new, "008282");
		EntityScreenTypes.addScreenType(EntityScreenIds.AIR_RADAR_SCREEN, AirRadarScreenInstance::new, "00FFFF");
		EntityScreenTypes.addScreenType(EntityScreenIds.GROUND_RADAR_SCREEN, GroundRadarScreenInstance::new, "4CFF00");
		EntityScreenTypes.addScreenType(EntityScreenIds.FUEL_SCREEN, FuelScreenInstance::new, "7F0000");
		// TODO 1.2 what to do with hud screen?
		EntityScreenTypes.addScreenType(EntityScreenIds.HUD_SCREEN, HudScreenInstance::new, "");
		EntityScreenTypes.addScreenType(EntityScreenIds.RWR_SCREEN, RWRScreenInstance::new, "FF00DC");
		EntityScreenTypes.addScreenType(EntityScreenIds.HEADING_SCREEN, HeadingScreenInstance::new, "0026FF");
		EntityScreenTypes.addScreenType(EntityScreenIds.TURN_COORD_SCREEN, TurnCoordScreenInstance::new, "008718");
		EntityScreenTypes.addScreenType(EntityScreenIds.ATTITUDE_SCREEN, AttitudeScreenInstance::new, "7F3300");
		EntityScreenTypes.addScreenType(EntityScreenIds.AOA_SCREEN, AOAScreenInstance::new, "840084");
		EntityScreenTypes.addScreenType(EntityScreenIds.ALTIMETER_SCREEN, AltimeterScreenInstance::new, "848400");
		EntityScreenTypes.addScreenType(EntityScreenIds.AIR_SPEED_SCREEN, SpeedScreenInstance::new, "FFD800");
	}
	
}
