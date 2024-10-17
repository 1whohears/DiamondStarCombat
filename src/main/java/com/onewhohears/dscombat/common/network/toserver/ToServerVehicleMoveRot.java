package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.DataSerializers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent.Context;
import org.joml.Quaternionf;

/**
 * this packet synchronizes the vehicle's speed and rotation from the pilot client's perspective with the server.
 * this is done to the client side doesn't have to worry about server lag-backs causing sudden 
 * drastic changes in the vehicle's position, speed and/or rotation.
 * @author 1whohears
 */
public class ToServerVehicleMoveRot extends IPacket {
	
	public final int id;
	public final Vec3 motion;
	public final Quaternionf q;
	public final Vec3 av;
	
	public ToServerVehicleMoveRot(EntityVehicle e) {
		this.id = e.getId();
		this.motion = e.getDeltaMovement();
		this.q = e.getClientQ();
		this.av = e.clientAV;
	}
	
	public ToServerVehicleMoveRot(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		motion = DataSerializers.VEC3.read(buffer);
		q = DataSerializers.QUATERNION.read(buffer);
		av = DataSerializers.VEC3.read(buffer);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		DataSerializers.VEC3.write(buffer, motion);
		DataSerializers.QUATERNION.write(buffer, q);
		DataSerializers.VEC3.write(buffer, av);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			success.set(true);
			ServerPlayer player = ctx.get().getSender();
			ServerLevel level = (ServerLevel) player.level();
			if (level.getEntity(id) instanceof EntityVehicle plane) {
				plane.setDeltaMovement(motion);
				plane.setPrevQ(plane.getQ());
				plane.setQ(q);
				plane.setAngularVel(av);
			}
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
