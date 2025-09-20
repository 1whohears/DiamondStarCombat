package com.onewhohears.dscombat.util;

import com.onewhohears.dscombat.common.network.toclient.ToClientDelayedSound;
import com.onewhohears.onewholibs.common.event.ServerHolder;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class UtilSound {

    @ExpectPlatform
	public static SoundEvent getSoundById(String id, SoundEvent alt) {
		throw new AssertionError();
	}
	
	public static void sendDelayedSound(SoundEvent sound, Vec3 pos, float radius,
                                        ResourceKey<Level> dim, float volume, float pitch) {
        MinecraftServer server = ServerHolder.get();
        if (server == null) return;
        new ToClientDelayedSound(sound, pos, radius, volume, pitch)
                .sendTo(UtilServerPacket.getPlayersWithinRadius(server, dim, pos, radius));
	}
	
}
