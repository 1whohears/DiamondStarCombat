package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.data.radar.RadarSystem.RWRWarning;
import com.onewhohears.dscombat.util.UtilClientPacket;
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
        return null;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		warning.write(buffer);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            UtilClientPacket.rwrPacket(id, warning);
		});
	}

}
