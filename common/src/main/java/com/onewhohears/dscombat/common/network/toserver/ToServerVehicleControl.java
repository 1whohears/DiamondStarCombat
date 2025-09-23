package com.onewhohears.dscombat.common.network.toserver;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.data.vehicle.VehicleInputManager;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import com.onewhohears.onewholibs.util.UtilEntity;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ToServerVehicleControl extends BaseC2SMessage {
	
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

    @Override
    public MessageType getType() {
        return PacketHandler.C2S_VEHICLE_CONTROL;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		inputs.write(buffer);
	}

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            Player player = context.getPlayer();
			Level level = UtilEntity.getLevel(player);
			if (level.getEntity(id) instanceof EntityVehicle plane) {
				plane.inputs.updateInputsFromPacket(inputs, plane);
				plane.syncControlsToClient();
			}
		});
	}
	
}
