package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.UtilClientPacket;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;

public class ToClientSyncPart extends BaseS2CMessage {
	
	private final int id;
	private final String slotId;
	private PartInstance<?> instance;
	private FriendlyByteBuf buffer;
	
	public ToClientSyncPart(EntityVehicle vehicle, PartInstance<?> instance) {
		this.id = vehicle.getId();
		this.slotId = instance.getSlotId();
		this.instance = instance;
	}
	
	public ToClientSyncPart(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		slotId = buffer.readUtf();
		buffer.readUtf(); // read preset id cause it ain't needed
		this.buffer = buffer;
	}

    @Override
    public MessageType getType() {
        return PacketHandler.S2C_SYNC_PART;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeUtf(slotId);
		instance.writeBuffer(buffer);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            UtilClientPacket.syncPartPacket(id, slotId, this.buffer);
		});
	}

}
