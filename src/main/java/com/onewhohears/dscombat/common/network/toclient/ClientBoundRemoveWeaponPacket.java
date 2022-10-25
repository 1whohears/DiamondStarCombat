package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ClientBoundRemoveWeaponPacket extends IPacket {
	
	public final int id;
	public final String wid;
	public final String slotId;
	
	public ClientBoundRemoveWeaponPacket(int id, String wid, String slotId) {
		this.id = id;
		this.wid = wid;
		this.slotId = slotId;
	}
	
	public ClientBoundRemoveWeaponPacket(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		wid = buffer.readUtf();
		slotId = buffer.readUtf();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeUtf(wid);
		buffer.writeUtf(slotId);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.removeWeaponPacket(id, wid, slotId);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
