package com.onewhohears.dscombat.mixin;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerChunkCache.class)
public class ServerChunkCacheMixin {

    private static final int MIN_DEBUG_DISTANCE_CHUNKS = 20;

    @Inject(
            method = "getChunk(IILnet/minecraft/world/level/chunk/ChunkStatus;Z)Lnet/minecraft/world/level/chunk/ChunkAccess;",
            at = @At("HEAD")
    )
    private void debugFarChunkLoads(
            int x,
            int z,
            ChunkStatus status,
            boolean load,
            CallbackInfoReturnable<ChunkAccess> cir
    ) {

        if (!load) {
            return;
        }

        ServerChunkCache cache = (ServerChunkCache)(Object)this;
        ServerLevel level = (ServerLevel) cache.getLevel();

        boolean farFromPlayers = true;

        for (ServerPlayer player : level.players()) {

            int playerChunkX = player.chunkPosition().x;
            int playerChunkZ = player.chunkPosition().z;

            int dx = Math.abs(playerChunkX - x);
            int dz = Math.abs(playerChunkZ - z);

            if (dx <= MIN_DEBUG_DISTANCE_CHUNKS &&
                    dz <= MIN_DEBUG_DISTANCE_CHUNKS) {

                farFromPlayers = false;
                break;
            }
        }

        if (!farFromPlayers) {
            return;
        }

        System.out.println(
                "Chunk: " + x + ", " + z + " Status: " + status
        );

        Thread.dumpStack();
    }
}
