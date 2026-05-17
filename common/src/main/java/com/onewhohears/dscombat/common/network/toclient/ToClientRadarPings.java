package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.data.radar.RadarTarget;
import com.onewhohears.dscombat.util.UtilClientPacket;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.minecraft.network.FriendlyByteBuf;

public class ToClientRadarPings extends BaseS2CMessage {
	
	public final int id;
	public final IntObjectMap<RadarTarget> pings;
	
	public ToClientRadarPings(int id, IntObjectMap<RadarTarget> pings) {
		this.id = id;
		this.pings = pings;
	}
	
	public ToClientRadarPings(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		pings = new IntObjectHashMap<>();
		int num = buffer.readInt();
		for (int i = 0; i < num; ++i) {
			RadarTarget target = new RadarTarget(buffer);
			pings.put(target.entityId, target);
		}
	}

    @Override
    public MessageType getType() {
        return PacketHandler.S2C_RADAR_PINGS;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeInt(pings.size());
		for (int i = 0; i < pings.size(); ++i) pings.get(i).write(buffer);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            UtilClientPacket.pingsPacket(id, pings);
		});
	}

}
