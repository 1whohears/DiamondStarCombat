package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
	
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DSCombatMod.MODID);
	
	public static final SoundEvent VEHICLE_HIT_1 = registerCreateSoundEvent("vehicle_hit");
	public static final SoundEvent MISSILE_WARNING = registerCreateSoundEvent("missile_warning");
	public static final SoundEvent GETTING_LOCKED = registerCreateSoundEvent("getting_locked");
	public static final SoundEvent MISSILE_LAUNCH_1 = registerCreateSoundEvent("rocket_launch_1");
	public static final SoundEvent MISSILE_ENGINE_1 = registerCreateSoundEvent("rocket_engine_1");
	public static final SoundEvent BULLET_SHOOT_1 = registerCreateSoundEvent("bullet_shoot_1");
	public static final SoundEvent BOMB_SHOOT_1 = registerCreateSoundEvent("bomb_shoot_1");
	public static final SoundEvent BIPLANE_1 = registerCreateSoundEvent("biplane_1");
	public static final SoundEvent HELI_1 = registerCreateSoundEvent("heli_1");
	public static final SoundEvent JET_1 = registerCreateSoundEvent("jet_1");
	public static final SoundEvent ORANGE_TESLA = registerCreateSoundEvent("orange_tesla");
	public static final SoundEvent TANK_1 = registerCreateSoundEvent("tank_1");
	public static final SoundEvent BOAT_1 = registerCreateSoundEvent("boat_1");
	public static final SoundEvent SUB_1 = registerCreateSoundEvent("sub_1");
	public static final SoundEvent FOX2_TONE_1 = registerCreateSoundEvent("fox2_tone_1");
	public static final SoundEvent MISSILE_KNOWS_WHERE = registerCreateSoundEvent("missile_knows_where");
	public static final SoundEvent STALL_ALERT_GM1 = registerCreateSoundEvent("stall_alert_gm1");
	public static final SoundEvent STALL_WARNING_GM1 = registerCreateSoundEvent("stall_warning_gm1");
	public static final SoundEvent ENGINE_FIRE_GM1 = registerCreateSoundEvent("engine_fire_gm1");
	public static final SoundEvent FUEL_LEAK_GM1 = registerCreateSoundEvent("fuel_leak_gm1");
	public static final SoundEvent BINGO_GM1 = registerCreateSoundEvent("bingo_gm1");
	public static final SoundEvent PULL_UP_GM1 = registerCreateSoundEvent("pull_up_gm1");
	public static final SoundEvent LOCK_GM1 = registerCreateSoundEvent("lock_gm1");
	public static final SoundEvent FLARE_GM1 = registerCreateSoundEvent("flare_gm1");
	public static final SoundEvent ALTITUDE_GM1 = registerCreateSoundEvent("altitude_gm1");
	public static final SoundEvent STALL_ALERT_NBG = registerCreateSoundEvent("stall_alert_nbg");
	public static final SoundEvent STALL_WARNING_NBG = registerCreateSoundEvent("stall_warning_nbg");
	public static final SoundEvent ENGINE_FIRE_NBG = registerCreateSoundEvent("engine_fire_nbg");
	public static final SoundEvent FUEL_LEAK_NBG = registerCreateSoundEvent("fuel_leak_nbg");
	public static final SoundEvent BINGO_NBG = registerCreateSoundEvent("bingo_nbg");
	public static final SoundEvent PULL_UP_NBG = registerCreateSoundEvent("pull_up_nbg");
	public static final SoundEvent LOCK_NBG = registerCreateSoundEvent("lock_nbg");
	public static final SoundEvent FLARE_NBG = registerCreateSoundEvent("flare_nbg");
	public static final SoundEvent ALTITUDE_NBG = registerCreateSoundEvent("altitude_nbg");
	// TODO 8.9 brrrr sound for javi plane
	
	public static void register(IEventBus eventBus) {
		SOUND_EVENTS.register(eventBus);
	}
	
	public static SoundEvent registerCreateSoundEvent(String name) {
		SoundEvent sound = new SoundEvent(new ResourceLocation(DSCombatMod.MODID, name));
		SOUND_EVENTS.register(name, () -> sound);
		return sound;
	}
	
	public static RegistryObject<SoundEvent> registerSoundEvent(String name) {
		return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(DSCombatMod.MODID, name)));
	}
	
}
