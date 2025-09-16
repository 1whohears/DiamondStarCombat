package com.onewhohears.dscombat.common.network.toserver;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.VehicleSyncAction;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ToServerVehicleSyncAction extends BaseC2SMessage {

	private final VehicleSyncAction action;

	public ToServerVehicleSyncAction(VehicleSyncAction action) {
		this.action = action;
    }

	public ToServerVehicleSyncAction(FriendlyByteBuf buffer) {
		action = VehicleSyncAction.getAction(buffer.readInt());
		if (action != null) action.readData(buffer);
    }

    @Override
    public MessageType getType() {
        return PacketHandler.C2S_VEHICLE_SYNC_ACTION;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(action.getId());
		action.writeData(buffer);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
			if (action == null) return;
            Player player = context.getPlayer();
			if (player == null) return;
			if (!(player.getRootVehicle() instanceof EntityVehicle vehicle)) return;
			if (action.hasPermission(player, vehicle)) {
				action.runServerAction((ServerPlayer) player, vehicle);
			}
		});
	}

}
