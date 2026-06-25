package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;

/**
 * Sent to the target vehicle's passengers when an ECM jam event occurs.
 * Triggers the HUD overlay animation.
 */
public class ToClientEcmJam extends BaseS2CMessage {

    public ToClientEcmJam() {}

    public ToClientEcmJam(FriendlyByteBuf buffer) {}

    @Override
    public MessageType getType() {
        return PacketHandler.S2C_ECM_JAM;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {}

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            com.onewhohears.dscombat.client.util.UtilClientPacket.ecmJam();
        });
    }
}
