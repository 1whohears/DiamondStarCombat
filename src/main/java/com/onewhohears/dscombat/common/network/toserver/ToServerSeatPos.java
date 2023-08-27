package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.init.DataSerializers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerSeatPos extends IPacket {
			
	public final Vec3 seatPos;
	
	public ToServerSeatPos(Vec3 seatPos) {
		this.seatPos = seatPos;
	}
	
	public ToServerSeatPos(FriendlyByteBuf buffer) {
		seatPos = DataSerializers.VEC3.read(buffer);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		DataSerializers.VEC3.write(buffer, seatPos);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		//System.out.println("HANDELING PACKET");
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			success.set(true);
			ServerPlayer player = ctx.get().getSender();
			if (player.isPassenger() && player.getVehicle() instanceof EntitySeat) {
				player.getVehicle().setPos(seatPos);
			}
			//System.out.println("ToServerSeatPos = "+seatPos);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
