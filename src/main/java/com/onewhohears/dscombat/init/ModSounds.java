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
	public static final RegistryObject<SoundEvent> MISSILE_WARNING = registerSoundEvent("missile_warning");
	public static final RegistryObject<SoundEvent> GETTING_LOCKED = registerSoundEvent("getting_locked");
	public static final RegistryObject<SoundEvent> MISSILE_LAUNCH_1 = registerSoundEvent("rocket_launch_1");
	public static final RegistryObject<SoundEvent> MISSILE_ENGINE_1 = registerSoundEvent("rocket_engine_1");
	public static final RegistryObject<SoundEvent> BULLET_SHOOT_1 = registerSoundEvent("bullet_shoot_1");
	public static final RegistryObject<SoundEvent> BOMB_SHOOT_1 = registerSoundEvent("bomb_shoot_1");
	public static final RegistryObject<SoundEvent> BIPLANE_1 = registerSoundEvent("biplane_1");
	public static final RegistryObject<SoundEvent> HELI_1 = registerSoundEvent("heli_1");
	public static final RegistryObject<SoundEvent> JET_1 = registerSoundEvent("jet_1");
	public static final RegistryObject<SoundEvent> ORANGE_TESLA = registerSoundEvent("orange_tesla");
	public static final RegistryObject<SoundEvent> TANK_1 = registerSoundEvent("tank_1");
	public static final RegistryObject<SoundEvent> BOAT_1 = registerSoundEvent("boat_1");
	public static final RegistryObject<SoundEvent> SUB_1 = registerSoundEvent("sub_1");
	public static final RegistryObject<SoundEvent> FOX2_TONE_1 = registerSoundEvent("fox2_tone_1");
	public static final RegistryObject<SoundEvent> MISSILE_KNOWS_WHERE = registerSoundEvent("missile_knows_where");
	public static final RegistryObject<SoundEvent> STALL_ALERT_GM1 = registerSoundEvent("stall_alert_gm1");
	public static final RegistryObject<SoundEvent> STALL_WARNING_GM1 = registerSoundEvent("stall_warning_gm1");
	public static final RegistryObject<SoundEvent> ENGINE_FIRE_GM1 = registerSoundEvent("engine_fire_gm1");
	public static final RegistryObject<SoundEvent> FUEL_LEAK_GM1 = registerSoundEvent("fuel_leak_gm1");
	public static final RegistryObject<SoundEvent> BINGO_GM1 = registerSoundEvent("bingo_gm1");
	public static final RegistryObject<SoundEvent> PULL_UP_GM1 = registerSoundEvent("pull_up_gm1");
	public static final RegistryObject<SoundEvent> LOCK_GM1 = registerSoundEvent("lock_gm1");
	public static final RegistryObject<SoundEvent> FLARE_GM1 = registerSoundEvent("flare_gm1");
	public static final RegistryObject<SoundEvent> ALTITUDE_GM1 = registerSoundEvent("altitude_gm1");
	public static final RegistryObject<SoundEvent> STALL_ALERT_NBG = registerSoundEvent("stall_alert_nbg");
	public static final RegistryObject<SoundEvent> STALL_WARNING_NBG = registerSoundEvent("stall_warning_nbg");
	public static final RegistryObject<SoundEvent> ENGINE_FIRE_NBG = registerSoundEvent("engine_fire_nbg");
	public static final RegistryObject<SoundEvent> FUEL_LEAK_NBG = registerSoundEvent("fuel_leak_nbg");
	public static final RegistryObject<SoundEvent> BINGO_NBG = registerSoundEvent("bingo_nbg");
	public static final RegistryObject<SoundEvent> PULL_UP_NBG = registerSoundEvent("pull_up_nbg");
	public static final RegistryObject<SoundEvent> LOCK_NBG = registerSoundEvent("lock_nbg");
	public static final RegistryObject<SoundEvent> FLARE_NBG = registerSoundEvent("flare_nbg");
	public static final RegistryObject<SoundEvent> ALTITUDE_NBG = registerSoundEvent("altitude_nbg");
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
