package com.onewhohears.dscombat.common.network.toserver;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ToServerOpenParts extends IPacket {

	public ToServerOpenParts() {

	}

	public ToServerOpenParts(FriendlyByteBuf buffer) {

	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {

	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			success.set(true);
			ServerPlayer player = ctx.get().getSender();
			if (player == null) return;
			if (player.getRootVehicle() instanceof EntityVehicle vehicle)
				vehicle.openPartsMenu(player);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
