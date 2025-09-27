package com.onewhohears.dscombat.util.fabric;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class UtilSoundImpl {

    public static SoundEvent getSoundById(String id, SoundEvent alt) {
        return Registry.SOUND_EVENT.getOptional(ResourceLocation.tryParse(id)).orElse(alt);
    }

}
