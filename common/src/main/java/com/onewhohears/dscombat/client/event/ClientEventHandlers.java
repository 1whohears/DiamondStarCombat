package com.onewhohears.dscombat.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.entityscreen.EntityScreenIds;
import com.onewhohears.dscombat.client.entityscreen.EntityScreenTypes;
import com.onewhohears.dscombat.client.entityscreen.instance.*;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.model.obj.customanims.DSCAnimControl;
import com.onewhohears.dscombat.client.model.obj.customanims.VehicleModelTransforms;
import com.onewhohears.dscombat.client.overlay.OverlayController;
import com.onewhohears.dscombat.client.particle.*;
import com.onewhohears.dscombat.client.renderer.EntityScreenRenderer;
import com.onewhohears.dscombat.client.screen.*;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.sound.PassengerSoundPack;
import com.onewhohears.dscombat.init.ModContainers;
import com.onewhohears.dscombat.init.ModFluids;
import com.onewhohears.dscombat.init.ModParticles;
import com.onewhohears.onewholibs.client.model.obj.customanims.CustomAnims;
import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.ControllableAnimPlayer;
import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.KFAnimPlayers;
import com.onewhohears.onewholibs.common.event.OWLEvents;
import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.events.client.*;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ClientEventHandlers {

    public static void init() {
        ClientLifecycleEvent.CLIENT_SETUP.register(ClientEventHandlers::onClientSetup);
        ClientGuiEvent.RENDER_HUD.register(ClientEventHandlers::onRenderHud);
        OWLEvents.SYNC_BOOL_GAME_RULE.register(ClientEventHandlers::onSyncGameRuleBool);
        ClientChatEvent.RECEIVED.register(ClientEventHandlers::receivedChat);
        // TODO 4.3 thermal camera option
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(ClientInputEventHandlers::onClientPlayerJoin);
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(ClientEventHandlers::onClientPlayerQuit);
        ClientTickEvent.CLIENT_PRE.register(ClientInputEventHandlers::clientTickPilotControl);
    }

    public static void onClientPlayerQuit(@Nullable LocalPlayer localPlayer) {
        EntityScreenRenderer.clearCache();
    }

    public static CompoundEventResult<Component> receivedChat(ChatType.Bound bound, Component message) {
        ResourceLocation typeId = Minecraft.getInstance().level.registryAccess()
                .registryOrThrow(Registry.CHAT_TYPE_REGISTRY)
                .getKey(bound.chatType());
        if (typeId == null) return CompoundEventResult.pass();
        if (typeId.getPath().equals("chat")) return CompoundEventResult.pass();
        Minecraft m = Minecraft.getInstance();
        if (!(m.screen instanceof VehicleScreen screen)) return CompoundEventResult.pass();
        screen.setInfoFromMessage(message, 60);
        return CompoundEventResult.pass();
    }

    public static void onSyncGameRuleBool(String id, boolean value) {
        if (id.equals(DSCGameRules.DISABLE_3RD_PERSON_VEHICLE.getId()))
            DSCClientInputs.disable3rdPersonVehicle = value;
        else if (id.equals(DSCGameRules.PLANE_ARCADE_MODE.getId()))
            DSCClientInputs.planeArcadePhysicsMode = value;
    }

    public static void onRenderHud(PoseStack poseStack, float partialTicks) {
        OverlayController.onRenderHud(poseStack, partialTicks);
    }

    public static void onClientSetup(Minecraft minecraft) {
        registerScreens();
        registerCustomAnims();
        registerKeyframeAnims();
        setFluidRenderLayers();
        registerEntityScreens();
        PassengerSoundPack.registerBuiltInPassengerSoundTriggers();
    }

    public static void registerParticleProvider() {
        ParticleProviderRegistry.register(ModParticles.LARGE_SMOKE_CLOUD.get(), LargeSmokeCloudParticle.Provider::new);
        ParticleProviderRegistry.register(ModParticles.SHRAPNEL.get(), ShrapnelParticle.Provider::new);
        ParticleProviderRegistry.register(ModParticles.BIG_FLAME.get(), BigFlameParticle.Provider::new);
        ParticleProviderRegistry.register(ModParticles.CONTRAIL.get(), ContrailParticle.Provider::new);
        ParticleProviderRegistry.register(ModParticles.AFTER_BURNER.get(), AfterBurnerParticle.Provider::new);
        ParticleProviderRegistry.register(ModParticles.FLARE.get(), FlareParticle.Provider::new);
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
        RenderTypeRegistry.register(RenderType.translucent(), ModFluids.OIL_FLUID_SOURCE.get());
        RenderTypeRegistry.register(RenderType.translucent(), ModFluids.OIL_FLUID_FLOWING.get());
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
        MenuRegistry.registerScreenFactory(ModContainers.VEHICLE_STORAGE_MENU_9x0.get(), VehicleStorageScreen::new);
        MenuRegistry.registerScreenFactory(ModContainers.VEHICLE_STORAGE_MENU_9x1.get(), VehicleStorageScreen::new);
        MenuRegistry.registerScreenFactory(ModContainers.VEHICLE_STORAGE_MENU_9x2.get(), VehicleStorageScreen::new);
        MenuRegistry.registerScreenFactory(ModContainers.VEHICLE_STORAGE_MENU_9x3.get(), VehicleStorageScreen::new);
        MenuRegistry.registerScreenFactory(ModContainers.VEHICLE_STORAGE_MENU_9x4.get(), VehicleStorageScreen::new);
        MenuRegistry.registerScreenFactory(ModContainers.VEHICLE_STORAGE_MENU_9x5.get(), VehicleStorageScreen::new);
        MenuRegistry.registerScreenFactory(ModContainers.VEHICLE_STORAGE_MENU_9x6.get(), VehicleStorageScreen::new);
    }

}
