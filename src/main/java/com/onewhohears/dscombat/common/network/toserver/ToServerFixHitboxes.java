package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerFixHitboxes extends IPacket {
	
	private final int id;
	
	public ToServerFixHitboxes(EntityVehicle vehicle) {
		this.id = vehicle.getId();
	}
	
	public ToServerFixHitboxes(FriendlyByteBuf buffer) {
		id = buffer.readInt();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			success.set(true);
			ServerPlayer player = ctx.get().getSender();
			if (player == null) return;
			Level level = player.level();
			if (!(level.getEntity(id) instanceof EntityVehicle vehicle)) return;
			vehicle.refreshHitboxes();
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
