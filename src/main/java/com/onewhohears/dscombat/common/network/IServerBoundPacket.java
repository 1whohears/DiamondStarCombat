package com.onewhohears.dscombat.common.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public interface IServerBoundPacket {
	
	public void encode(FriendlyByteBuf buffer);
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx);
	
}
