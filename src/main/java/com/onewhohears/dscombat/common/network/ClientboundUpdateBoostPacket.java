package com.onewhohears.dscombat.common.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundUpdateBoostPacket {
	
	public final BlockPos entityPos;
	
	public ClientboundUpdateBoostPacket(BlockPos pos) {
		this.entityPos = pos;
	}
	
	public ClientboundUpdateBoostPacket(FriendlyByteBuf buffer) {
		this(buffer.readBlockPos());
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(this.entityPos);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> success.set(true));
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}
}
