package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerAircraftThrottle extends IPacket {
	
	public final int id;
	public final float th;
	
	public ToServerAircraftThrottle(EntityAircraft e) {
		this.id = e.getId();
		this.th = e.getCurrentThrottle();
	}
	
	public ToServerAircraftThrottle(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		th = buffer.readFloat();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeFloat(th);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			success.set(true);
			ServerPlayer player = ctx.get().getSender();
			ServerLevel level = player.getLevel();
			if (level.getEntity(id) instanceof EntityAircraft plane) {
				plane.setCurrentThrottle(th);
			}
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
