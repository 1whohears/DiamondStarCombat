package com.onewhohears.dscombat.common.network.toserver;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientVehicleDecal;
import com.onewhohears.dscombat.data.vehicle.VehicleDecalManager.DecalData;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilEntity;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ToServerVehicleDecal extends BaseC2SMessage {

    public static final byte ACTION_ADD    = 0;
    public static final byte ACTION_REMOVE = 1;
    public static final byte ACTION_UPDATE = 2;

    private final int vehicleId;
    private final byte action;
    // For ADD
    private DecalData decalData;
    // For REMOVE
    private String decalId;

    /** Constructor for ADD */
    public ToServerVehicleDecal(int vehicleId, DecalData data) {
        this.vehicleId = vehicleId;
        this.action = ACTION_ADD;
        this.decalData = data;
    }

    /** Constructor for REMOVE */
    public ToServerVehicleDecal(int vehicleId, String decalId) {
        this.vehicleId = vehicleId;
        this.action = ACTION_REMOVE;
        this.decalId = decalId;
    }

    /** Constructor for UPDATE */
    public ToServerVehicleDecal(int vehicleId, DecalData data, boolean update) {
        this.vehicleId = vehicleId;
        this.action = ACTION_UPDATE;
        this.decalData = data;
    }

    public ToServerVehicleDecal(FriendlyByteBuf buf) {
        this.vehicleId = buf.readInt();
        this.action = buf.readByte();
        if (action == ACTION_ADD || action == ACTION_UPDATE) {
            this.decalData = DecalData.fromBuf(buf);
        } else {
            this.decalId = buf.readUtf();
        }
    }

    @Override
    public MessageType getType() {
        return PacketHandler.C2S_VEHICLE_DECAL;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(vehicleId);
        buf.writeByte(action);
        if (action == ACTION_ADD || action == ACTION_UPDATE) {
            decalData.toBuf(buf);
        } else {
            buf.writeUtf(decalId);
        }
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            Player player = context.getPlayer();
            Level level = UtilEntity.getLevel(player);
            if (!(level.getEntity(vehicleId) instanceof EntityVehicle vehicle)) return;
            if (action == ACTION_ADD) {
                vehicle.decalManager.addDecal(decalData);
            } else if (action == ACTION_UPDATE) {
                vehicle.decalManager.updateDecal(decalData);
            } else {
                vehicle.decalManager.removeDecal(decalId);
            }
            PacketHandler.sendToTrackers(new ToClientVehicleDecal(vehicleId, action,
                    decalData, decalId, player.getId()), vehicle);
        });
    }
}
