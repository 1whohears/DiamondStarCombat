package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.entity.parts.EntityChainHook;
import com.onewhohears.dscombat.entity.parts.EntityChainHook.ChainUpdateType;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class ToClientVehicleChainUpdate extends BaseS2CMessage {
	
	public final int vehicleId, hookId, playerId;
	public final ChainUpdateType type;
	
	public ToClientVehicleChainUpdate(@Nullable EntityVehicle vehicle, @Nullable EntityChainHook hook, @Nullable Player player, ChainUpdateType type) {
		if (vehicle != null) this.vehicleId = vehicle.getId();
		else this.vehicleId = -1;
		if (hook != null) this.hookId = hook.getId();
		else this.hookId = -1;
		if (player != null) this.playerId = player.getId();
		else this.playerId = -1;
		this.type = type;
	}
	
	public ToClientVehicleChainUpdate(FriendlyByteBuf buffer) {
		this.vehicleId = buffer.readInt();
		this.hookId = buffer.readInt();
		this.playerId = buffer.readInt();
		this.type = ChainUpdateType.values()[buffer.readInt()];
	}

    @Override
    public MessageType getType() {
        return PacketHandler.S2C_VEHICLE_CHAIN_UPDATE;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(vehicleId);
		buffer.writeInt(hookId);
		buffer.writeInt(playerId);
		buffer.writeInt(type.ordinal());
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            com.onewhohears.dscombat.client.util.UtilClientPacket.updateVehicleChain(vehicleId, hookId, playerId, type);
		});
	}

}
