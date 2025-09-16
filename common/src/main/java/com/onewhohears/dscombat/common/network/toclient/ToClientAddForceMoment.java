package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilClientPacket;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class ToClientAddForceMoment extends BaseS2CMessage {
	
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
    public MessageType getType() {
        return null;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		DataSerializers.VEC3.write(buffer, force);
		DataSerializers.VEC3.write(buffer, moment);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            UtilClientPacket.addMomentPacket(id, force, moment);
		});
	}

}
