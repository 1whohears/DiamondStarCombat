package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.radar.RadarData.RadarMode;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerSetRadarMode extends IPacket {
	
	public final RadarMode mode;
	
	public ToServerSetRadarMode(RadarMode mode) {
		this.mode = mode;
	}
	
	public ToServerSetRadarMode(FriendlyByteBuf buffer) {
		mode = RadarMode.byId(buffer.readByte());
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeByte(mode.ordinal());
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			if (player.getRootVehicle() instanceof EntityVehicle vehicle) {
				vehicle.setRadarMode(mode);
			}
			success.set(true);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
