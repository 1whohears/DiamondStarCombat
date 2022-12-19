package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;

public class ServerBoundAircraftToItemPacket extends IPacket {
	
	public final int id;
	
	public ServerBoundAircraftToItemPacket(int id) {
		this.id = id;
	}
	
	public ServerBoundAircraftToItemPacket(FriendlyByteBuf buffer) {
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
			Level level = ctx.get().getSender().level;
			if (level.getEntity(id) instanceof EntityAircraft plane) {
				ItemStack stack = plane.getItem();
				ItemEntity e = new ItemEntity(level, plane.getX(), plane.getY(), plane.getZ(), stack);
				level.addFreshEntity(e);
				plane.discard();
			}
			success.set(true);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
