package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.data.vehicle.VehicleInputManager;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;

public class ToClientVehicleControl extends BaseS2CMessage {
	
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

    @Override
    public MessageType getType() {
        return PacketHandler.S2C_VEHICLE_CONTROL;
    }

    public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		inputs.write(buffer);
	}
	
	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            com.onewhohears.dscombat.client.util.UtilClientPacket.aircraftInputsPacket(id, inputs);
		});
	}

}
