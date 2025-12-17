package com.onewhohears.dscombat.util;

import com.onewhohears.dscombat.common.network.toclient.ToClientDelayedSound;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UtilSound {

    private static final Map<String, SoundEvent> cachedCustomSoundEvents = new HashMap<>();

	public static SoundEvent getSoundById(String id, SoundEvent alt, RegistryAccess ra) {
        if (id == null || id.isEmpty()) return alt;
        if (cachedCustomSoundEvents.containsKey(id)) return cachedCustomSoundEvents.get(id);
		Registry<SoundEvent> reg = ra.registryOrThrow(Registries.SOUND_EVENT);
        ResourceLocation rl = ResourceLocation.tryParse(id);
        Optional<SoundEvent> sound = reg.getOptional(rl);
        if (sound.isEmpty()) {
            SoundEvent event = SoundEvent.createVariableRangeEvent(rl);
            cachedCustomSoundEvents.put(id, event);
            return event;
        }
        return sound.get();
	}

    public static SoundEvent getSoundByIdClient(String id, SoundEvent alt) {
        return getSoundById(id, alt, Minecraft.getInstance().level.registryAccess());
    }
	
	public static void sendDelayedSound(ServerLevel level, SoundEvent sound, Vec3 pos,
                                        float radius, float volume, float pitch) {
        List<ServerPlayer> players = UtilServerPacket.getPlayersWithinRadius(level, pos, radius);
        new ToClientDelayedSound(sound, pos, radius, volume, pitch).sendTo(players);
	}
	
}
