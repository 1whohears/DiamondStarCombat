package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.aircraft.VehicleInputManager;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.UtilClientPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientVehicleControl extends IPacket {
	
	public final int id;
	public final VehicleInputManager inputs;
	
	public ToClientVehicleControl(EntityVehicle plane) {
		this.id = plane.getId();
		this.inputs = plane.inputs;
	}
	
	public ToClientVehicleControl(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		inputs = new VehicleInputManager(buffer);
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		inputs.write(buffer);
	}
	
	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilClientPacket.aircraftInputsPacket(id, inputs);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
