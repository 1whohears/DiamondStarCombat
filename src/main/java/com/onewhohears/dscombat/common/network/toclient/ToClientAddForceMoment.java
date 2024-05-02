package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilClientPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientAddForceMoment extends IPacket {
	
	public final int id;
	public final Vec3 force;
	public final Vec3 moment;
	
	public ToClientAddForceMoment(EntityVehicle craft, Vec3 force, Vec3 moment) {
		this.id = craft.getId();
		this.force = force;
		this.moment = moment;
	}
	
	public ToClientAddForceMoment(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		force = DataSerializers.VEC3.read(buffer);
		moment = DataSerializers.VEC3.read(buffer);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		DataSerializers.VEC3.write(buffer, force);
		DataSerializers.VEC3.write(buffer, moment);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilClientPacket.addMomentPacket(id, force, moment);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
