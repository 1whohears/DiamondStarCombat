package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerOpenStorage extends IPacket {
	
	public ToServerOpenStorage() {
	}
	
	public ToServerOpenStorage(FriendlyByteBuf buffer) {
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		//System.out.println("HANDELING PACKET");
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			success.set(true);
			ServerPlayer player = ctx.get().getSender();
			if (player.getRootVehicle() instanceof EntityVehicle vehicle) 
				vehicle.openStorage(player);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
