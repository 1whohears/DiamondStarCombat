package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientSynchTorque extends IPacket {
	
	public final int id;
	public final float tx, ty, tz;
	
	public ToClientSynchTorque(EntityAircraft craft) {
		this.id = craft.getId();
		this.tx = 0;
		this.ty = 0;
		this.tz = 0;
	}
	
	public ToClientSynchTorque(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		tx = buffer.readFloat();
		ty = buffer.readFloat();
		tz = buffer.readFloat();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeFloat(tx);
		buffer.writeFloat(ty);
		buffer.writeFloat(tz);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.synchTorquePacket(id, tx, ty, tz);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
