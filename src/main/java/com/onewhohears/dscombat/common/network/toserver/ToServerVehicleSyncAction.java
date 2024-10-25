package com.onewhohears.dscombat.common.network.toserver;

import com.onewhohears.dscombat.common.network.VehicleSyncAction;
import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ToServerVehicleSyncAction extends IPacket {

	private final VehicleSyncAction action;

	public ToServerVehicleSyncAction(VehicleSyncAction action) {
		this.action = action;
    }

	public ToServerVehicleSyncAction(FriendlyByteBuf buffer) {
		action = VehicleSyncAction.getAction(buffer.readInt());
		if (action != null) action.readData(buffer);
    }
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(action.getId());
		action.writeData(buffer);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			success.set(true);
			if (action == null) return;
			ServerPlayer player = ctx.get().getSender();
			if (player == null) return;
			if (!(player.getRootVehicle() instanceof EntityVehicle vehicle)) return;
			if (action.hasPermission(player, vehicle)) {
				action.runServerAction(player, vehicle);
			}
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
