package com.onewhohears.dscombat.data.sound;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.sounds.SoundEvent;

public class DSCSoundDefinitionGen {

    @ExpectPlatform
	public static void registerSound(SoundEvent soundEvent, String path, String subtitle, float volume) {
		throw new AssertionError();
	}

    @ExpectPlatform
	public static void registerStreamSound(SoundEvent soundEvent, String path, String subtitle, float volume) {
        throw new AssertionError();
	}

    @ExpectPlatform
	public static void registerSound(SoundEvent soundEvent, String subtitle, float volume, String... paths) {
        throw new AssertionError();
	}

}
