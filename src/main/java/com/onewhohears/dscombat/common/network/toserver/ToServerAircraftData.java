package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.init.DataSerializers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerAircraftData extends IPacket {
	
	public final int id;
	public final Quaternion q;
	//public final Vec3 pos;
	//public final Vec3 move;
	
	public ToServerAircraftData(EntityAircraft e) {
		// TODO should only Q be synched or should Q position and move all be synched like this?
		this.id = e.getId();
		this.q = e.getClientQ();
		//this.pos = e.position();
		//this.move = e.getDeltaMovement();
	}
	
	public ToServerAircraftData(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		q = DataSerializers.QUATERNION.read(buffer);
		//pos = DataSerializers.VEC3.read(buffer);
		//move = DataSerializers.VEC3.read(buffer);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		DataSerializers.QUATERNION.write(buffer, q);
		//DataSerializers.VEC3.write(buffer, pos);
		//DataSerializers.VEC3.write(buffer, move);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			success.set(true);
			ServerPlayer player = ctx.get().getSender();
			ServerLevel level = player.getLevel();
			if (level.getEntity(id) instanceof EntityAircraft plane) {
				plane.setPrevQ(plane.getQ());
				plane.setQ(q);
				//plane.setPos(pos);
				//plane.prevMotion = plane.getDeltaMovement();
				//plane.setDeltaMovement(move);
			}
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
