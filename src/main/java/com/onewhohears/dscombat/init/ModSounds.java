package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.DSCSoundDefinitionGen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {
	
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DSCombatMod.MODID);
	
	public static final SoundEvent ORANGE_TESLA = registerSoundEvent("orange_tesla", "music/orange_tesla", "sounds.dscombat.orange_tesla", true, 1);
	public static final SoundEvent MISSILE_KNOWS_WHERE = registerSoundEvent("missile_knows_where", "music/missile_knows_where", "sounds.dscombat.missile_knows_where", true, 1);
	
	public static final SoundEvent MISSILE_WARNING = registerSoundEvent("missile_warning", "cockpit/radar/missile_warning", "sounds.dscombat.missile_warning");
	public static final SoundEvent GETTING_LOCKED = registerSoundEvent("getting_locked", "cockpit/radar/getting_locked", "sounds.dscombat.rocket_launch_1");
	public static final SoundEvent FOX2_TONE_1 = registerSoundEvent("fox2_tone_1", "cockpit/radar/fox2_tone_1", "sounds.dscombat.fox2_tone_1");
	
	public static final SoundEvent MISSILE_LAUNCH_1 = registerSoundEvent("rocket_launch_1", "weapon/rocket_launch_1", "sounds.dscombat.rocket_launch_1");
	public static final SoundEvent MISSILE_ENGINE_1 = registerSoundEvent("rocket_engine_1", "weapon/rocket_engine_1", "sounds.dscombat.rocket_engine_1");
	public static final SoundEvent BULLET_SHOOT_1 = registerSoundEvent("bullet_shoot_1", "weapon/bullet_shoot_1", "sounds.dscombat.bullet_shoot_1");
	public static final SoundEvent BOMB_SHOOT_1 = registerSoundEvent("bomb_shoot_1", "weapon/bomb_shoot_1", "sounds.dscombat.bomb_shoot_1");
	// TODO 8.9 brrrr sound for javi plane
	public static final SoundEvent BIPLANE_1 = registerSoundEvent("biplane_1", "vehicle/engine/biplane_1", "sounds.dscombat.biplane_1");
	public static final SoundEvent HELI_1 = registerSoundEvent("heli_1", "vehicle/engine/heli_1", "sounds.dscombat.heli_1");
	public static final SoundEvent JET_1 = registerSoundEvent("jet_1", "vehicle/engine/jet_1", "sounds.dscombat.jet_1");
	public static final SoundEvent VEHICLE_HIT_1 = registerSoundEvent("vehicle_hit", "vehicle/vehicle_hit", "sounds.dscombat.vehicle_hit");
	public static final SoundEvent TANK_1 = registerSoundEvent("tank_1", "vehicle/engine/tank_1", "sounds.dscombat.tank_1");
	public static final SoundEvent BOAT_1 = registerSoundEvent("boat_1", "vehicle/engine/boat_1", "sounds.dscombat.boat_1");
	public static final SoundEvent SUB_1 = registerSoundEvent("sub_1", "vehicle/engine/sub_1", "sounds.dscombat.sub_1");
	
	public static final SoundEvent ALEXIS_CP_AFTERBURNER = registerSoundEvent("alexis_cp_afterburner", "vehicle/loop/fighter_jet/alexis_plane/cockpit_afterburner", "sounds.dscombat.afterburner", 0.2f);
	public static final SoundEvent ALEXIS_CP_RPM = registerSoundEvent("alexis_cp_rpm", "vehicle/loop/fighter_jet/alexis_plane/cockpit_rpm", "sounds.dscombat.jet_rpm", 0.05f);
	public static final SoundEvent ALEXIS_CP_WIND_FAST = registerSoundEvent("alexis_cp_wind_fast", "vehicle/loop/fighter_jet/alexis_plane/cockpit_wind_fast", "sounds.dscombat.jet_wind", 0.14f);
	public static final SoundEvent ALEXIS_CP_WIND_SLOW = registerSoundEvent("alexis_cp_wind_slow", "vehicle/loop/fighter_jet/alexis_plane/cockpit_wind_slow", "sounds.dscombat.jet_wind", 0.04f);
	public static final SoundEvent ALEXIS_EXT_AFTERBURNER_CLOSE = registerSoundEvent("alexis_ext_afterburner_close", "vehicle/loop/fighter_jet/alexis_plane/external_afterburner_close", "sounds.dscombat.afterburner");
	public static final SoundEvent ALEXIS_EXT_AFTERBURNER_FAR = registerSoundEvent("alexis_ext_afterburner_far", "vehicle/loop/fighter_jet/alexis_plane/external_afterburner_far", "sounds.dscombat.afterburner");
	public static final SoundEvent ALEXIS_EXT_RPM = registerSoundEvent("alexis_ext_rpm", "vehicle/loop/fighter_jet/alexis_plane/external_rpm", "sounds.dscombat.jet_rpm");
	public static final SoundEvent ALEXIS_EXT_WIND_CLOSE = registerSoundEvent("alexis_ext_wind_close", "vehicle/loop/fighter_jet/alexis_plane/external_wind_close", "sounds.dscombat.jet_wind");
	public static final SoundEvent ALEXIS_EXT_WIND_FAR = registerSoundEvent("alexis_ext_wind_far", "vehicle/loop/fighter_jet/alexis_plane/external_wind_far", "sounds.dscombat.jet_wind");
	
	public static final SoundEvent STALL_ALERT_GM1 = registerSoundEvent("stall_alert_gm1", "cockpit/warnings/generic_male_1/stall_alert", "sounds.dscombat.stall_alert");
	public static final SoundEvent STALL_WARNING_GM1 = registerSoundEvent("stall_warning_gm1", "cockpit/warnings/generic_male_1/stall_warn", "sounds.dscombat.stall_warn");
	public static final SoundEvent ENGINE_FIRE_GM1 = registerSoundEvent("engine_fire_gm1", "cockpit/warnings/generic_male_1/engine_fire", "sounds.dscombat.engine_fire");
	public static final SoundEvent FUEL_LEAK_GM1 = registerSoundEvent("fuel_leak_gm1", "cockpit/warnings/generic_male_1/fuel_leak", "sounds.dscombat.fuel_leak");
	public static final SoundEvent BINGO_GM1 = registerSoundEvent("bingo_gm1", "cockpit/warnings/generic_male_1/bingo", "sounds.dscombat.bingo");
	public static final SoundEvent PULL_UP_GM1 = registerSoundEvent("pull_up_gm1", "cockpit/warnings/generic_male_1/pull_up", "sounds.dscombat.pull_up");
	public static final SoundEvent LOCK_GM1 = registerSoundEvent("lock_gm1", "cockpit/warnings/generic_male_1/lock", "sounds.dscombat.lock");
	public static final SoundEvent FLARE_GM1 = registerSoundEvent("flare_gm1", "cockpit/warnings/generic_male_1/flare", "sounds.dscombat.flare");
	public static final SoundEvent ALTITUDE_GM1 = registerSoundEvent("altitude_gm1", "cockpit/warnings/generic_male_1/altitude", "sounds.dscombat.altitude");
	
	public static final SoundEvent STALL_ALERT_NBG = registerSoundEvent("stall_alert_nbg", "cockpit/warnings/non_binary_goober/stall_alert", "sounds.dscombat.stall_alert");
	public static final SoundEvent STALL_WARNING_NBG = registerSoundEvent("stall_warning_nbg", "cockpit/warnings/non_binary_goober/stall_warn", "sounds.dscombat.stall_warn");
	public static final SoundEvent ENGINE_FIRE_NBG = registerSoundEvent("engine_fire_nbg", "cockpit/warnings/non_binary_goober/engine_fire", "sounds.dscombat.engine_fire");
	public static final SoundEvent FUEL_LEAK_NBG = registerSoundEvent("fuel_leak_nbg", "cockpit/warnings/non_binary_goober/fuel_leak", "sounds.dscombat.fuel_leak");
	public static final SoundEvent BINGO_NBG = registerSoundEvent("bingo_nbg", "cockpit/warnings/non_binary_goober/bingo", "sounds.dscombat.bingo");
	public static final SoundEvent PULL_UP_NBG = registerSoundEvent("pull_up_nbg", "cockpit/warnings/non_binary_goober/pull_up", "sounds.dscombat.pull_up");
	public static final SoundEvent LOCK_NBG = registerSoundEvent("lock_nbg", "cockpit/warnings/non_binary_goober/lock", "sounds.dscombat.lock");
	public static final SoundEvent FLARE_NBG = registerSoundEvent("flare_nbg", "cockpit/warnings/non_binary_goober/flare", "sounds.dscombat.flare");
	public static final SoundEvent ALTITUDE_NBG = registerSoundEvent("altitude_nbg", "cockpit/warnings/non_binary_goober/altitude", "sounds.dscombat.altitude");
	
	public static void register(IEventBus eventBus) {
		SOUND_EVENTS.register(eventBus);
	}
	
	public static SoundEvent registerSoundEvent(String name, String path, String subtitle, boolean stream, float volume) {
		SoundEvent soundEvent = new SoundEvent(new ResourceLocation(DSCombatMod.MODID, name));
		SOUND_EVENTS.register(name, () -> soundEvent);
		if (path != null) {
			if (subtitle == null) subtitle = "";
			if (stream) DSCSoundDefinitionGen.registerStreamSound(soundEvent, path, subtitle, volume);
			else DSCSoundDefinitionGen.registerSound(soundEvent, path, subtitle, volume);
		}
		return soundEvent;
	}
	
	public static SoundEvent registerSoundEvent(String name, String path, String subtitle) {
		return registerSoundEvent(name, path, subtitle, false, 1);
	}
	
	public static SoundEvent registerSoundEvent(String name, String path, String subtitle, float volume) {
		return registerSoundEvent(name, path, subtitle, false, volume);
	}
	
	public static SoundEvent registerSoundEvent(String name, String path) {
		return registerSoundEvent(name, path, "sounds."+DSCombatMod.MODID+"."+name, false, 1);
	}
	
	public static SoundEvent registerSoundEvent(String name) {
		return registerSoundEvent(name, null, null);
	}
	
}
