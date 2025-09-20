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
import com.onewhohears.dscombat.client.overlay.WindTunnelOverlay;
import com.onewhohears.dscombat.client.particle.AfterBurnerParticle;
import com.onewhohears.dscombat.client.particle.BigFlameParticle;
import com.onewhohears.dscombat.client.particle.ContrailParticle;
import com.onewhohears.dscombat.client.particle.FlareParticle;
import com.onewhohears.dscombat.client.particle.LargeSmokeCloudParticle;
import com.onewhohears.dscombat.client.particle.ShrapnelParticle;
import com.onewhohears.dscombat.client.renderer.RendererEntityInvisible;
import com.onewhohears.dscombat.client.renderer.RendererWindTunnel;
import com.onewhohears.dscombat.client.screen.VehicleBlockScreen;
import com.onewhohears.dscombat.client.screen.VehiclePartsScreen;
import com.onewhohears.dscombat.client.screen.VehicleStorageScreen;
import com.onewhohears.dscombat.client.screen.WeaponsBlockScreen;
import com.onewhohears.dscombat.client.screen.WeaponPartsBlockScreen;
import com.onewhohears.dscombat.data.parts.client.PartAssets;
import com.onewhohears.dscombat.data.sound.PassengerSoundPack;
import com.onewhohears.dscombat.data.sound.VehiclePassengerSoundPacks;
import com.onewhohears.dscombat.data.weapon.client.WeaponAssets;
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
		event.registerEntityRenderer(ModEntities.DUMB_TORPEDO_MISSILE.get(), RendererObjWeapon::new);
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
		event.registerEntityRenderer(ModEntities.WIND_TUNNEL.get(), RendererWindTunnel::new);
	}
	
	@SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        VehicleOverlayComponent.registerOverlays(event);
		WindTunnelOverlay.register(event);
    }
	
}
