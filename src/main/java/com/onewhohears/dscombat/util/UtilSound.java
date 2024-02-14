package com.onewhohears.dscombat.util;

import java.util.NoSuchElementException;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class UtilSound {
	
	public static SoundEvent getSoundById(String id, SoundEvent alt) {
		try {
			return ForgeRegistries.SOUND_EVENTS.getDelegate(new ResourceLocation(id)).get().get();
		} catch(NoSuchElementException e) {
			return alt;
		}
	}
	
}
