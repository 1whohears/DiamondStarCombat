package com.onewhohears.dscombat.common.network;

import java.util.function.Supplier;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public abstract class IPacket {
	
	protected static final Logger LOGGER = LogUtils.getLogger();
	
	public IPacket() {}
	
	public IPacket(FriendlyByteBuf buffer) {
		LOGGER.debug("DECODING PACKET "+getClass().getName());
	}
	
	public abstract void encode(FriendlyByteBuf buffer);
	
	public abstract boolean handle(Supplier<NetworkEvent.Context> ctx);
	
}
