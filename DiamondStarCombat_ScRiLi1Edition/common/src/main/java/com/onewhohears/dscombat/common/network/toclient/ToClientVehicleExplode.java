package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilClientPacket;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class ToClientVehicleExplode extends BaseS2CMessage {
	
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
    public MessageType getType() {
        return PacketHandler.S2C_VEHICLE_EXPLODE;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		DataSerializers.VEC3.write(buffer, pos);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            UtilClientPacket.vehicleExplode(id, pos);
		});
	}

}
