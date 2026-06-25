package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.DataSerializers;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class ToClientVehicleExplode extends BaseS2CMessage {
	
	public final int id;
	public final Vec3 pos;
	public final boolean isAmmoExplosion;
	
	public ToClientVehicleExplode(EntityVehicle vehicle, boolean isAmmoExplosion) {
		this.id = vehicle.getId();
		this.pos = vehicle.position();
		this.isAmmoExplosion = isAmmoExplosion;
	}
	
	public ToClientVehicleExplode(Vec3 pos, boolean isAmmoExplosion) {
		this.id = -1;
		this.pos = pos;
		this.isAmmoExplosion = isAmmoExplosion;
	}
	
	public ToClientVehicleExplode(Vec3 pos) {
		this(pos, false);
	}
	
	public ToClientVehicleExplode(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		pos = DataSerializers.VEC3.read(buffer);
		isAmmoExplosion = buffer.readBoolean();
	}

    @Override
    public MessageType getType() {
        return PacketHandler.S2C_VEHICLE_EXPLODE;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		DataSerializers.VEC3.write(buffer, pos);
		buffer.writeBoolean(isAmmoExplosion);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            com.onewhohears.dscombat.client.util.UtilClientPacket.vehicleExplode(id, pos, isAmmoExplosion);
		});
	}

}
