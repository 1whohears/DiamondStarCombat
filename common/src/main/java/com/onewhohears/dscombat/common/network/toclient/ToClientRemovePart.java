package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.util.UtilClientPacket;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;

public class ToClientRemovePart extends BaseS2CMessage {
	
	public final int id;
	public final String slotId;
	
	public ToClientRemovePart(int id, String slotId) {
		this.id = id;
		this.slotId = slotId;
	}
	
	public ToClientRemovePart(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		slotId = buffer.readUtf();
	}

    @Override
    public MessageType getType() {
        return null;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeUtf(slotId);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            UtilClientPacket.removePartPacket(id, slotId);
		});
	}

}
