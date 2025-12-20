package com.onewhohears.dscombat.util;

import com.onewhohears.dscombat.common.network.toclient.ToClientDelayedSound;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilSound {

    private static final Map<String, SoundEvent> cachedCustomSoundEvents = new HashMap<>();

	public static SoundEvent getSoundById(String id, SoundEvent alt) {
        if (id == null || id.isEmpty()) return alt;
        if (cachedCustomSoundEvents.containsKey(id)) return cachedCustomSoundEvents.get(id);
        SoundEvent event = getRegisteredSoundById(id, null);
        if (event == null) {
            SoundEvent sound = new SoundEvent(new ResourceLocation(id));
            cachedCustomSoundEvents.put(id, sound);
            return sound;
        }
        return event;
	}

    @ExpectPlatform
    public static SoundEvent getRegisteredSoundById(String id, SoundEvent alt) {
        throw new AssertionError();
    }
	
	public static void sendDelayedSound(ServerLevel level, SoundEvent sound, Vec3 pos,
                                        float radius, float volume, float pitch) {
        List<ServerPlayer> players = UtilServerPacket.getPlayersWithinRadius(level, pos, radius);
        new ToClientDelayedSound(sound, pos, radius, volume, pitch).sendTo(players);
	}
	
}
