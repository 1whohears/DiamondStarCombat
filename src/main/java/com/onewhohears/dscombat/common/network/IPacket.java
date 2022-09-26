package com.onewhohears.dscombat.common.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public abstract class IPacket {
	
	public IPacket() {}
	
	public IPacket(FriendlyByteBuf buffer) {}
	
	public abstract void encode(FriendlyByteBuf buffer);
	
	public abstract boolean handle(Supplier<NetworkEvent.Context> ctx);
	
}
