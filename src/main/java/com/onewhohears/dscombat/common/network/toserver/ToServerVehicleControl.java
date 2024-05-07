package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.aircraft.VehicleInputManager;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ToServerVehicleControl extends IPacket {
	
	public final int id;
	public final VehicleInputManager inputs;
	
	public ToServerVehicleControl(EntityVehicle plane) {
		this.id = plane.getId();
		this.inputs = plane.inputs;
	}
	
	public ToServerVehicleControl(FriendlyByteBuf buffer) {
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
				plane.syncControlsToClient();
			}
			success.set(true);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}
	
}
