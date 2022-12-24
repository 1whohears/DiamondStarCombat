package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.radar.RadarData.RadarPing;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerPingSelect extends IPacket {
	
	public final int id;
	public final RadarPing ping;
	
	public ToServerPingSelect(int planeId, RadarPing ping) {
		this.id = planeId;
		this.ping = ping;
	}
	
	public ToServerPingSelect(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		ping = new RadarPing(buffer);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		ping.write(buffer);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			success.set(true);
			ServerPlayer player = ctx.get().getSender();
			ServerLevel level = player.getLevel();
			if (level.getEntity(id) instanceof EntityAircraft plane) {
				plane.radarSystem.selectTarget(ping);
			}
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
