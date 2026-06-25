package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.data.radar.RadarStats.RadarPing;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

public class ToClientRadarPings extends BaseS2CMessage {
	
	public final int id;
	public final List<RadarPing> pings;
	
	public ToClientRadarPings(int id, List<RadarPing> pings) {
		this.id = id;
		this.pings = pings;
	}
	
	public ToClientRadarPings(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		pings = new ArrayList<RadarPing>();
		int num = buffer.readInt();
		for (int i = 0; i < num; ++i) pings.add(new RadarPing(buffer));
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
            com.onewhohears.dscombat.client.util.UtilClientPacket.pingsPacket(id, pings);
		});
	}

}
