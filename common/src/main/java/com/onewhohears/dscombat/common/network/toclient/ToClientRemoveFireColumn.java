package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.util.ExplosionFireColumn;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;

public class ToClientRemoveFireColumn extends BaseS2CMessage {
	
	public final int vehicleId;
	
	public ToClientRemoveFireColumn(int vehicleId) {
		this.vehicleId = vehicleId;
	}
	
	public ToClientRemoveFireColumn(FriendlyByteBuf buffer) {
		vehicleId = buffer.readInt();
	}

    @Override
    public MessageType getType() {
        return PacketHandler.S2C_REMOVE_FIRE_COLUMN;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(vehicleId);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            ExplosionFireColumn.removeFireColumn(vehicleId);
		});
	}

}
