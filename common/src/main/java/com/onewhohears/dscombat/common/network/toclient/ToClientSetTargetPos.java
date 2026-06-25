package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.init.DataSerializers;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;

public class ToClientSetTargetPos extends BaseS2CMessage {

    public static void setTargetPos(Vec3 targetPos, Collection<ServerPlayer> players) {
        new ToClientSetTargetPos(targetPos).sendTo(players);
    }

    public static void setTargetPos(Vec3 targetPos, ServerPlayer player) {
        new ToClientSetTargetPos(targetPos).sendTo(player);
    }

    private final Vec3 targetPos;

    public ToClientSetTargetPos(Vec3 pos) {
        this.targetPos = pos;
    }

    public ToClientSetTargetPos(FriendlyByteBuf buffer) {
        this.targetPos = DataSerializers.VEC3.read(buffer);
    }

    @Override
    public MessageType getType() {
        return PacketHandler.S2C_SET_TARGET_POS;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        DataSerializers.VEC3.write(buffer, targetPos);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> com.onewhohears.dscombat.client.util.UtilClientPacket.setTargetPos(targetPos));
    }
}
