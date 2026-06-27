package com.onewhohears.dscombat.client.event;

import com.onewhohears.dscombat.client.entityscreen.EntityScreenIds;
import com.onewhohears.dscombat.client.entityscreen.EntityScreenTypes;
import com.onewhohears.dscombat.client.entityscreen.instance.*;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.model.obj.customanims.DSCAnimControl;
import com.onewhohears.dscombat.client.model.obj.customanims.VehicleModelTransforms;
import com.onewhohears.dscombat.client.overlay.OverlayController;
import com.onewhohears.dscombat.client.overlay.components.EcmStatusOverlay;
import com.onewhohears.dscombat.client.particle.*;
import com.onewhohears.dscombat.client.renderer.EntityScreenRenderer;
import com.onewhohears.dscombat.client.screen.*;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.sound.PassengerSoundPack;
import com.onewhohears.dscombat.entity.vehicle.hitbox.ClientSideHitboxStuckFixer;
import com.onewhohears.dscombat.init.ModContainers;
import com.onewhohears.dscombat.init.ModFluids;
import com.onewhohears.dscombat.init.ModParticles;
import com.onewhohears.dscombat.data.weapon.client.WeaponClientStats;
import com.onewhohears.dscombat.util.ExplosionFireColumn;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.client.model.obj.ObjModelHandler;
import com.onewhohears.onewholibs.client.model.obj.customanims.CustomAnims;
import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.ControllableAnimPlayer;
import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.KFAnimPlayers;
import com.onewhohears.onewholibs.client.renderer.RendererObjModelItems;
import com.onewhohears.onewholibs.common.event.OWLEvents;
import com.onewhohears.onewholibs.util.math.Vec3f;
import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.events.client.*;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;

public class ClientEventHandlers {

    public static void init() {
        ClientLifecycleEvent.CLIENT_SETUP.register(ClientEventHandlers::onClientSetup);
        ClientGuiEvent.RENDER_HUD.register(ClientEventHandlers::onRenderHud);
        OWLEvents.SYNC_BOOL_GAME_RULE.register(ClientEventHandlers::onSyncGameRuleBool);
        // TODO 4.3 thermal camera option
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(ClientInputEventHandlers::onClientPlayerJoin);
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(ClientEventHandlers::onClientPlayerQuit);
        ClientTickEvent.CLIENT_PRE.register(ClientEventHandlers::onClientTickPre);
    }

    public static void onClientTickPre(Minecraft minecraft) {
        ClientInputEventHandlers.clientTickPilotControl(minecraft);
        ClientCameraEventHandlers.clientTickSetMouseCallback(minecraft);
        EcmStatusOverlay.tick();
        if (minecraft.level != null) {
            ExplosionFireColumn.tick(minecraft.level);
        }
        com.onewhohears.dscombat.client.renderer.TrackMarkManager.tick();
    }

    public static void onClientPlayerQuit(@Nullable LocalPlayer localPlayer) {
        EntityScreenRenderer.clearCache();
        ClientSideHitboxStuckFixer.clear();
    }

    public static void onSyncGameRuleBool(String id, boolean value) {
        if (id.equals(DSCGameRules.DISABLE_3RD_PERSON_VEHICLE.getId()))
            DSCClientInputs.disable3rdPersonVehicle = value;
        else if (id.equals(DSCGameRules.PLANE_ARCADE_MODE.getId()))
            DSCClientInputs.planeArcadePhysicsMode = value;
    }

    public static void onRenderHud(GuiGraphics graphics, float partialTicks) {
        OverlayController.onRenderHud(graphics, partialTicks);
    }

    public static void onClientSetup(Minecraft minecraft) {
        registerScreens();
        registerCustomAnims();
        registerKeyframeAnims();
        setFluidRenderLayers();
        registerEntityScreens();
        registerBlockEntityRenderers();
        PassengerSoundPack.registerBuiltInPassengerSoundTriggers();
        registerItemRenderers();
    }

    public static void registerItemRenderers() {
        // Item renderers can be registered here if needed
    }
    
    public static void registerBlockEntityRenderers() {
        dev.architectury.registry.client.rendering.BlockEntityRendererRegistry.register(
            com.onewhohears.dscombat.init.ModBlockEntities.MISSILE_LAUNCH_STATION_ENTITY.get(), 
            com.onewhohears.dscombat.client.renderer.MissileLaunchStationRenderer::new
        );
    }

    public static void registerParticleProvider() {
        ParticleProviderRegistry.register(ModParticles.LARGE_SMOKE_CLOUD.get(), LargeSmokeCloudParticle.Provider::new);
        ParticleProviderRegistry.register(ModParticles.SHRAPNEL.get(), ShrapnelParticle.Provider::new);
        ParticleProviderRegistry.register(ModParticles.BIG_FLAME.get(), BigFlameParticle.Provider::new);
        ParticleProviderRegistry.register(ModParticles.CONTRAIL.get(), ContrailParticle.Provider::new);
        ParticleProviderRegistry.register(ModParticles.AFTER_BURNER.get(), AfterBurnerParticle.Provider::new);
        ParticleProviderRegistry.register(ModParticles.FLARE.get(), FlareParticle.Provider::new);
        ParticleProviderRegistry.register(ModParticles.TRACER.get(), TracerParticle.Provider::new);
        ParticleProviderRegistry.register(ModParticles.TANK_DUST.get(), com.onewhohears.dscombat.client.particle.TankDustParticle.Provider::new);
        ParticleProviderRegistry.register(ModParticles.SMOKE_GRENADE_CLOUD.get(), com.onewhohears.dscombat.client.particle.SmokeGrenadeParticle.Provider::new);
        ParticleProviderRegistry.register(ModParticles.EXPLOSION_CORE.get(), com.onewhohears.dscombat.client.particle.ExplosionCoreParticle.Provider::new);
        ParticleProviderRegistry.register(ModParticles.EXPLOSION_SMOKE.get(), com.onewhohears.dscombat.client.particle.ExplosionSmokeParticle.Provider::new);
    }

    public static void registerEntityScreens() {
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

    public static void setFluidRenderLayers() {
        RenderTypeRegistry.register(RenderType.translucent(), ModFluids.getOilFluidSource().get());
        RenderTypeRegistry.register(RenderType.translucent(), ModFluids.getOilFluidFlowing().get());
    }

    public static void registerKeyframeAnims() {
        KFAnimPlayers.addAnimationPlayerFactory("turret_shoot", (data) -> new ControllableAnimPlayer<>(data,
                DSCAnimControl.TURRET_SHOOT_TRIGGER, DSCAnimControl.TURRET_SHOOT_CONTROL));
        KFAnimPlayers.addAnimationPlayerFactory("turret_shoot_loop", (data) -> new ControllableAnimPlayer<>(data,
                DSCAnimControl.TURRET_SHOOT_LOOP_TRIGGER, DSCAnimControl.TURRET_SHOOT_LOOP_CONTROL));
        KFAnimPlayers.addAnimationPlayerFactory("turret_shoot_loop_end", (data) -> new ControllableAnimPlayer<>(data,
                DSCAnimControl.TURRET_SHOOT_LOOP_END_TRIGGER, DSCAnimControl.TURRET_SHOOT_LOOP_END_CONTROL));
        KFAnimPlayers.addAnimationPlayerFactory("vehicle_landing_gear", (data) -> new ControllableAnimPlayer<>(data,
                DSCAnimControl.LANDING_GEAR_TRIGGER, DSCAnimControl.LANDING_GEAR_CONTROL));
    }

    public static void registerCustomAnims() {
        CustomAnims.addAnim("input_bound_translation", VehicleModelTransforms.InputBoundTranslation::new);
        CustomAnims.addAnim("motor_rotation", VehicleModelTransforms.MotorRotation::new);
        CustomAnims.addAnim("wheel_rotation", VehicleModelTransforms.WheelRotation::new);
        CustomAnims.addAnim("input_bound_rotation", VehicleModelTransforms.InputBoundRotation::new);
        CustomAnims.addAnim("spinning_radar", VehicleModelTransforms.SpinningRadar::new);
        CustomAnims.addAnim("landing_gear", VehicleModelTransforms.LandingGear::new);
        CustomAnims.addAnim("hitbox_destroy_part", VehicleModelTransforms.HitboxDestroyPart::new);
        CustomAnims.addAnim("plane_flap_rotation", VehicleModelTransforms.PlaneFlapRotation::new);
        CustomAnims.addAnim("turret_rotation", VehicleModelTransforms.TurretRotation::new);
    }

    public static void registerScreens() {
        MenuRegistry.registerScreenFactory(ModContainers.VEHICLE_PARTS_MENU.get(), VehiclePartsScreen::new);
        MenuRegistry.registerScreenFactory(ModContainers.WEAPONS_BLOCK_MENU.get(), WeaponsBlockScreen::new);
        MenuRegistry.registerScreenFactory(ModContainers.WEAPON_PARTS_BLOCK_MENU.get(), WeaponPartsBlockScreen::new);
        MenuRegistry.registerScreenFactory(ModContainers.AIRCRAFT_BLOCK_MENU.get(), VehicleBlockScreen::new);
        MenuRegistry.registerScreenFactory(ModContainers.MISSILE_LAUNCH_STATION_MENU.get(), com.onewhohears.dscombat.client.screen.MissileLaunchStationScreen::new);
        MenuRegistry.registerScreenFactory(ModContainers.VEHICLE_STORAGE_MENU_9x0.get(), VehicleStorageScreen::new);
        MenuRegistry.registerScreenFactory(ModContainers.VEHICLE_STORAGE_MENU_9x1.get(), VehicleStorageScreen::new);
        MenuRegistry.registerScreenFactory(ModContainers.VEHICLE_STORAGE_MENU_9x2.get(), VehicleStorageScreen::new);
        MenuRegistry.registerScreenFactory(ModContainers.VEHICLE_STORAGE_MENU_9x3.get(), VehicleStorageScreen::new);
        MenuRegistry.registerScreenFactory(ModContainers.VEHICLE_STORAGE_MENU_9x4.get(), VehicleStorageScreen::new);
        MenuRegistry.registerScreenFactory(ModContainers.VEHICLE_STORAGE_MENU_9x5.get(), VehicleStorageScreen::new);
        MenuRegistry.registerScreenFactory(ModContainers.VEHICLE_STORAGE_MENU_9x6.get(), VehicleStorageScreen::new);
    }

}
