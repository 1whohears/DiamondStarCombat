package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.core.PositionMarkerManager;
import com.onewhohears.dscombat.common.network.PacketHandler;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ToClientPlayerMarkers extends BaseS2CMessage {

    private final UUID uuid;
    private final FriendlyByteBuf bufferCopy;

    public ToClientPlayerMarkers(@NotNull UUID playerUUID) {
        uuid = playerUUID;
        bufferCopy = new FriendlyByteBuf(Unpooled.buffer());
        PositionMarkerManager.getServer().getPlayerData(uuid).writePacket(bufferCopy);
    }

    public ToClientPlayerMarkers(FriendlyByteBuf buffer) {
        uuid = buffer.readUUID();
        int bytes = buffer.readInt();
        ByteBuf buf = buffer.readBytes(bytes);
        bufferCopy = new FriendlyByteBuf(buf);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(uuid);
        buffer.writeInt(bufferCopy.readableBytes());
        buffer.writeBytes(bufferCopy);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            if (context.getEnvironment().toPlatform() != EnvType.CLIENT) return;
            PositionMarkerManager.getClient().getPlayerData(uuid).readPacket(bufferCopy);
        });
    }

    @Override
    public MessageType getType() {
        return PacketHandler.S2C_PLAYER_MARKERS;
    }

}
