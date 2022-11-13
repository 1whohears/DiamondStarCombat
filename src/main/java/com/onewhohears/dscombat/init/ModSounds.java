package com.onewhohears.dscombat.init;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.DSCombatMod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
	
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DSCombatMod.MODID);
	
	public static final RegistryObject<SoundEvent> MISSILE_WARNING = registerSoundEvent("missile_warning");
	public static final RegistryObject<SoundEvent> GETTING_LOCKED = registerSoundEvent("getting_locked");
	public static final RegistryObject<SoundEvent> MISSILE_LAUNCH_1 = registerSoundEvent("rocket_launch_1");
	public static final RegistryObject<SoundEvent> MISSILE_ENGINE_1 = registerSoundEvent("rocket_engine_1");
	public static final RegistryObject<SoundEvent> BULLET_SHOOT_1 = registerSoundEvent("bullet_shoot_1");
	public static final RegistryObject<SoundEvent> BIPLANE_1 = registerSoundEvent("biplane_1");
	
	public static void register(IEventBus eventBus) {
		SOUND_EVENTS.register(eventBus);
	}
	
	@Nullable
	public static RegistryObject<SoundEvent> getObjectByKey(String key) {
		for (RegistryObject<SoundEvent> sound : SOUND_EVENTS.getEntries()) 
			if(sound.getId().toString().equals(key)) return sound;
		return null;
	}
	
	private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
		return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(DSCombatMod.MODID, name)));
	}
	
}
