package com.onewhohears.dscombat.data.sound.forge;

import com.onewhohears.dscombat.DSCombatMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

import java.util.LinkedHashMap;
import java.util.Map;

public class DSCSoundDefinitionGenImpl extends SoundDefinitionsProvider {
	
	private static final Map<String, SoundDefinition> soundsFromRegistry = new LinkedHashMap<>();
	
	public DSCSoundDefinitionGenImpl(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, DSCombatMod.MODID, helper);
	}

	@Override
	public void registerSounds() {
		soundsFromRegistry.forEach(this::add);
	}
	
	public static void registerSound(SoundEvent soundEvent, String path, String subtitle, float volume) {
        ResourceLocation rl = ResourceLocation.tryBuild(DSCombatMod.MODID, path);
        if (rl == null) return;
		soundsFromRegistry.put(soundEvent.getLocation().getPath(), definition().subtitle(subtitle).with(
				sound(rl).volume(volume)));
	}
	
	public static void registerStreamSound(SoundEvent soundEvent, String path, String subtitle, float volume) {
        ResourceLocation rl = ResourceLocation.tryBuild(DSCombatMod.MODID, path);
        if (rl == null) return;
		soundsFromRegistry.put(soundEvent.getLocation().getPath(), definition().subtitle(subtitle).with(
				sound(ResourceLocation.tryBuild(DSCombatMod.MODID, path)).stream().volume(volume)));
	}

	public static void registerSound(SoundEvent soundEvent, String subtitle, float volume, String... paths) {
		SoundDefinition def = definition().subtitle(subtitle);
		for (String path : paths) {
            ResourceLocation rl = ResourceLocation.tryBuild(DSCombatMod.MODID, path);
            if (rl == null) continue;
            def.with(sound(rl).volume(volume));
        }
		soundsFromRegistry.put(soundEvent.getLocation().getPath(), def);
	}

}
