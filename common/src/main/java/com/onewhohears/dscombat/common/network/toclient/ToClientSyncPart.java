package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;

public class ToClientSyncPart extends BaseS2CMessage {
	
	private final int id;
	private final String slotId;
    private final FriendlyByteBuf buffer;
	
	public ToClientSyncPart(EntityVehicle vehicle, PartInstance<?> instance) {
		this.id = vehicle.getId();
		this.slotId = instance.getSlotId();
        buffer = new FriendlyByteBuf(Unpooled.buffer());
        instance.writeBuffer(buffer);
	}
	
	public ToClientSyncPart(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		slotId = buffer.readUtf();
        int bytes = buffer.readInt();
        ByteBuf buf = buffer.readBytes(bytes);
        this.buffer = new FriendlyByteBuf(buf);
	}

    @Override
    public MessageType getType() {
        return PacketHandler.S2C_SYNC_PART;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeUtf(slotId);
        buffer.writeInt(this.buffer.readableBytes());
        buffer.writeBytes(this.buffer);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            com.onewhohears.dscombat.client.util.UtilClientPacket.syncPartPacket(id, slotId, this.buffer);
		});
	}

}
