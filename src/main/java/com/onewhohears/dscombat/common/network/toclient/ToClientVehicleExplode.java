package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientVehicleExplode extends IPacket {
	
	public final int id;
	public final Vec3 pos;
	
	public ToClientVehicleExplode(EntityVehicle vehicle) {
		this.id = vehicle.getId();
		this.pos = vehicle.position();
	}
	
	public ToClientVehicleExplode(Vec3 pos) {
		this.id = -1;
		this.pos = pos;
	}
	
	public ToClientVehicleExplode(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		pos = DataSerializers.VEC3.read(buffer);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		DataSerializers.VEC3.write(buffer, pos);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.vehicleExplode(id, pos);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
