package com.onewhohears.dscombat.util.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.NoSuchElementException;

public class UtilSoundImpl {

    public static SoundEvent getRegisteredSoundById(String id, SoundEvent alt) {
        try {
            return ForgeRegistries.SOUND_EVENTS.getDelegate(ResourceLocation.tryParse(id)).get().get();
        } catch(NoSuchElementException e) {
            return alt;
        }
    }

}
