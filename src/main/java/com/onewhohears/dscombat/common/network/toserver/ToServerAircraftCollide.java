package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerAircraftCollide extends IPacket {
	
	public final int id;
	public final float amount;
	public final boolean isFall;
	
	public ToServerAircraftCollide(int id, float amount, boolean isFall) {
		this.id = id;
		this.amount = amount;
		this.isFall = isFall;
	}
	
	public ToServerAircraftCollide(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		amount = buffer.readFloat();
		isFall = buffer.readBoolean();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeFloat(amount);
		buffer.writeBoolean(isFall);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			Level level = ctx.get().getSender().level;
			if (level.getEntity(id) instanceof EntityAircraft plane) {
				plane.collideHurt(amount, isFall);
			}
			success.set(true);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
