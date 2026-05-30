package com.onewhohears.dscombat.common.network.toserver;

import com.onewhohears.dscombat.common.core.PositionMarkerManager;
import com.onewhohears.dscombat.common.network.PacketHandler;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.fabricmc.api.EnvType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class ToServerModifyMarker extends BaseC2SMessage {

    public ToServerModifyMarker() {

    }

    public ToServerModifyMarker(FriendlyByteBuf buffer) {

    }

    @Override
    public void write(FriendlyByteBuf buffer) {

    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            if (context.getEnvironment().toPlatform() != EnvType.SERVER) return;
            ServerPlayer player = (ServerPlayer) context.getPlayer();
            PositionMarkerManager.getServer().addQuickTempMarker(player);
        });
    }

    @Override
    public MessageType getType() {
        return PacketHandler.C2S_MODIFY_MARKER;
    }
}
