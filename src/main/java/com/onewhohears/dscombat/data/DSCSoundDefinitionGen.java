package com.onewhohears.dscombat.data;

import java.util.LinkedHashMap;
import java.util.Map;

import com.onewhohears.dscombat.DSCombatMod;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class DSCSoundDefinitionGen extends SoundDefinitionsProvider {
	
	private static final Map<String, SoundDefinition> soundsFromRegistry = new LinkedHashMap<>();
	
	public DSCSoundDefinitionGen(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, DSCombatMod.MODID, helper);
	}

	@Override
	public void registerSounds() {
		soundsFromRegistry.forEach((soundEvent, definition) -> add(soundEvent, definition));
	}
	
	public static void registerSound(SoundEvent soundEvent, String path, String subtitle) {
		soundsFromRegistry.put(soundEvent.getLocation().getPath(), definition().subtitle(subtitle).with(
				sound(new ResourceLocation(DSCombatMod.MODID, path))));
	}
	
	public static void registerStreamSound(SoundEvent soundEvent, String path, String subtitle) {
		soundsFromRegistry.put(soundEvent.getLocation().getPath(), definition().subtitle(subtitle).with(
				sound(new ResourceLocation(DSCombatMod.MODID, path)).stream()));
	}

}
