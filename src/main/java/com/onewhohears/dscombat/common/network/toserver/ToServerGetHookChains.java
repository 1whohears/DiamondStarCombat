package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.parts.EntityChainHook;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerGetHookChains extends IPacket {
			
	public final int hookId;
	
	public ToServerGetHookChains(EntityChainHook hook) {
		hookId = hook.getId();
	}
	
	public ToServerGetHookChains(FriendlyByteBuf buffer) {
		hookId = buffer.readInt();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(hookId);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			success.set(true);
			if (ctx.get().getSender().level().getEntity(hookId) instanceof EntityChainHook hook) {
				hook.sendAllVehicleChainsToClient(ctx.get().getSender());
			}
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
