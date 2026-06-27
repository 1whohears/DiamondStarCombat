package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.data.radar.RadarSystem.RWRWarning;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;

public class ToClientRWRWarning extends BaseS2CMessage {
	
	public final int id;
	public final RWRWarning warning;
	
	public ToClientRWRWarning(int id, RWRWarning warning) {
		this.id = id;
		this.warning = warning;
	}
	
	public ToClientRWRWarning(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		warning = new RWRWarning(buffer);
	}

    @Override
    public MessageType getType() {
        return PacketHandler.S2C_RWR_WARNING;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		warning.write(buffer);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            com.onewhohears.dscombat.client.util.UtilClientPacket.rwrPacket(id, warning);
		});
	}

}
