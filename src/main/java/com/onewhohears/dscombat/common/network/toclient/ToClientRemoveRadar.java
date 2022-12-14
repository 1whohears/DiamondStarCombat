package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientRemoveRadar extends IPacket {
	
	public final int id;
	public final String rid;
	public final String slotId;
	
	public ToClientRemoveRadar(int id, String rid, String slotId) {
		this.id = id;
		this.rid = rid;
		this.slotId = slotId;
	}
	
	public ToClientRemoveRadar(FriendlyByteBuf buffer) {
		super(buffer);
		id = buffer.readInt();
		rid = buffer.readUtf();
		slotId = buffer.readUtf();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeUtf(rid);
		buffer.writeUtf(slotId);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.removeRadarPacket(id, rid, slotId);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
