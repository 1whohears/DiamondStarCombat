package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ToServerVehicleDecal;
import com.onewhohears.dscombat.data.vehicle.VehicleDecalManager.DecalData;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilEntity;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ToClientVehicleDecal extends BaseS2CMessage {

    private final int vehicleId;
    private final int senderPlayerId;
    private final byte action;
    @Nullable private DecalData decalData;
    @Nullable private String decalId;

    public ToClientVehicleDecal(int vehicleId, byte action,
                                @Nullable DecalData decalData, @Nullable String decalId,
                                int senderPlayerId) {
        this.vehicleId = vehicleId;
        this.action = action;
        this.decalData = decalData;
        this.decalId = decalId;
        this.senderPlayerId = senderPlayerId;
    }

    public ToClientVehicleDecal(FriendlyByteBuf buf) {
        this.vehicleId = buf.readInt();
        this.senderPlayerId = buf.readInt();
        this.action = buf.readByte();
        if (action == ToServerVehicleDecal.ACTION_ADD || action == ToServerVehicleDecal.ACTION_UPDATE) {
            this.decalData = DecalData.fromBuf(buf);
        } else {
            this.decalId = buf.readUtf();
        }
    }

    @Override
    public MessageType getType() {
        return PacketHandler.S2C_VEHICLE_DECAL;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(vehicleId);
        buf.writeInt(senderPlayerId);
        buf.writeByte(action);
        if (action == ToServerVehicleDecal.ACTION_ADD || action == ToServerVehicleDecal.ACTION_UPDATE) {
            decalData.toBuf(buf);
        } else {
            buf.writeUtf(decalId);
        }
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            com.onewhohears.dscombat.client.util.UtilClientPacket.vehicleDecal(
                vehicleId, senderPlayerId, action, decalData, decalId);
        });
    }
}
