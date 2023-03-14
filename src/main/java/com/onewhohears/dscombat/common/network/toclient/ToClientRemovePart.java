package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

@Deprecated
public class ToClientRemovePart extends IPacket {
	
	public final int id;
	public final String slotName;
	
	public ToClientRemovePart(int id, String slotName) {
		this.id = id;
		this.slotName = slotName;
	}
	
	public ToClientRemovePart(FriendlyByteBuf buffer) {
		super(buffer);
		id = buffer.readInt();
		slotName = buffer.readUtf();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeUtf(slotName);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.removePartPacket(id, slotName);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
