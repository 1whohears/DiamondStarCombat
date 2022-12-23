package com.onewhohears.dscombat.common.network.toclient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.radar.RadarData.RadarPing;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientRadarPings extends IPacket {
	
	public final int id;
	public final List<RadarPing> pings;
	
	public ToClientRadarPings(int id, List<RadarPing> pings) {
		this.id = id;
		this.pings = pings;
	}
	
	public ToClientRadarPings(FriendlyByteBuf buffer) {
		super(buffer);
		id = buffer.readInt();
		pings = new ArrayList<RadarPing>();
		int num = buffer.readInt();
		for (int i = 0; i < num; ++i) pings.add(new RadarPing(buffer));
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeInt(pings.size());
		for (int i = 0; i < pings.size(); ++i) pings.get(i).write(buffer);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.pingsPacket(id, pings);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
