package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilClientPacket;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;

public class ToClientAddPart extends BaseS2CMessage {
	
	public final int id;
	public final String slotId;
	public final PartInstance<?> data;
	
	public ToClientAddPart(int id, String slotId, PartInstance<?> data) {
		this.id = id;
		this.slotId = slotId;
		this.data = data;
	}
	
	public ToClientAddPart(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		slotId = buffer.readUtf();
		data = DataSerializers.PART_DATA.read(buffer);
	}

    @Override
    public MessageType getType() {
        return null;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeUtf(slotId);
		DataSerializers.PART_DATA.write(buffer, data);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            UtilClientPacket.addPartPacket(id, slotId, data);
		});
	}

}
