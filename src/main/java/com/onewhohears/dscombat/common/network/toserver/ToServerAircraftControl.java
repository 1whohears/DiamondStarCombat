package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.aircraft.VehicleInputManager;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ToServerAircraftControl extends IPacket {
	
	public final int id;
	public final VehicleInputManager inputs;
	
	public ToServerAircraftControl(EntityVehicle plane) {
		this.id = plane.getId();
		this.inputs = plane.inputs;
	}
	
	public ToServerAircraftControl(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		inputs = new VehicleInputManager(buffer);
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		inputs.write(buffer);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			ServerLevel level = player.getLevel();
			if (level.getEntity(id) instanceof EntityVehicle plane) {
				plane.inputs.updateInputsFromPacket(inputs, plane);
			}
			success.set(true);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}
	
}
