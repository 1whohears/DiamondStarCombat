package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerOpenStorage extends IPacket {

	private final int index;

	public ToServerOpenStorage(int index) {
		this.index = index;
	}
	
	public ToServerOpenStorage(FriendlyByteBuf buffer) {
		index = buffer.readInt();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(index);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			success.set(true);
			ServerPlayer player = ctx.get().getSender();
			if (player == null) return;
			if (player.getRootVehicle() instanceof EntityVehicle vehicle) 
				vehicle.openStorage(player, index);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
