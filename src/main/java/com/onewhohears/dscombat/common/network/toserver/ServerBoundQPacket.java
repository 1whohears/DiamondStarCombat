package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.init.DataSerializers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class ServerBoundQPacket extends IPacket {
	
	public final int id;
	public final Quaternion q;
	
	public ServerBoundQPacket(int id, Quaternion q) {
		this.id = id;
		this.q = q;
	}
	
	public ServerBoundQPacket(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		q = DataSerializers.QUATERNION.read(buffer);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		DataSerializers.QUATERNION.write(buffer, q);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			success.set(true);
			ServerPlayer player = ctx.get().getSender();
			ServerLevel level = player.getLevel();
			if (level.getEntity(id) instanceof EntityAbstractAircraft plane) {
				plane.setPrevQ(plane.getQ());
				plane.setQ(q);
			}
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
