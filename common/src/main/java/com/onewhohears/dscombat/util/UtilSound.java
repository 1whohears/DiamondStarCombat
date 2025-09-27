package com.onewhohears.dscombat.util;

import com.onewhohears.dscombat.common.network.toclient.ToClientDelayedSound;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class UtilSound {

    @ExpectPlatform
	public static SoundEvent getSoundById(String id, SoundEvent alt) {
		throw new AssertionError();
	}
	
	public static void sendDelayedSound(ServerLevel level, SoundEvent sound, Vec3 pos,
                                        float radius, float volume, float pitch) {
        List<ServerPlayer> players = UtilServerPacket.getPlayersWithinRadius(level, pos, radius);
        new ToClientDelayedSound(sound, pos, radius, volume, pitch).sendTo(players);
	}
	
}
