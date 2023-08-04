package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerDismount extends IPacket {
	
	public ToServerDismount() {
		//System.out.println("CREATED PACKET");
	}
	
	public ToServerDismount(FriendlyByteBuf buffer) {
		//System.out.println("DECODING PACKET");
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		//System.out.println("ENCODING PACKET");
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		//System.out.println("HANDELING PACKET");
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			player.stopRiding();
			success.set(true);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
