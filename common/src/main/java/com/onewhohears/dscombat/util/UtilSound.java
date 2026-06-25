package com.onewhohears.dscombat.util;

import com.onewhohears.dscombat.common.network.toclient.ToClientDelayedSound;
import com.onewhohears.dscombat.common.network.toclient.ToClientDistantGunfire;
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
	
	public static void sendDelayedSound(ServerLevel level, SoundEvent sound, Vec3 pos,
                                        float radius, float volume, float pitch) {
        List<ServerPlayer> players = UtilServerPacket.getPlayersWithinRadius(level, pos, radius);
        new ToClientDelayedSound(sound, pos, radius, volume, pitch).sendTo(players);
	}
	
	/**
	 * Sends a distant gunfire/cannon sound to all players within range.
	 * Creates a "cannonade" effect that can be heard from far away.
	 * 
	 * @param level The server level
	 * @param sound The distant gunfire sound to play
	 * @param pos Position where the shot was fired
	 * @param radius Maximum hearing distance (recommended: 800)
	 * @param volume Base volume (will be attenuated by distance)
	 * @param pitch Base pitch (will be slightly lowered for distance)
	 * @param durationTicks How long the sound should play (in ticks, recommended: 20-40)
	 */
	public static void sendDistantGunfire(ServerLevel level, SoundEvent sound, Vec3 pos,
										  float radius, float volume, float pitch, int durationTicks) {
		List<ServerPlayer> players = UtilServerPacket.getPlayersWithinRadius(level, pos, radius);
		new ToClientDistantGunfire(sound, pos, volume, pitch, durationTicks).sendTo(players);
	}
	
}
