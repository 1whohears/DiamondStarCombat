package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.util.UtilClientPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientDamagePart extends IPacket {
	
	public final int id;
	public final String slotId;
	public final boolean damaged;
	
	public ToClientDamagePart(int id, String slotId, boolean damaged) {
		this.id = id;
		this.slotId = slotId;
		this.damaged = damaged;
	}
	
	public ToClientDamagePart(FriendlyByteBuf buffer) {
		super(buffer);
		id = buffer.readInt();
		slotId = buffer.readUtf();
		damaged = buffer.readBoolean();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeUtf(slotId);
		buffer.writeBoolean(damaged);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilClientPacket.damagePartPacket(id, slotId, damaged);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
