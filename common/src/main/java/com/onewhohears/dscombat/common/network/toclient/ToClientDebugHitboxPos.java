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

public class ToClientDebugHitboxPos extends BaseS2CMessage {
	
	private final int id;
	private final String hitbox_name;
	private final Vec3 pos, size;
	
	public ToClientDebugHitboxPos(EntityVehicle vehicle, String hitbox_name, Vec3 pos, Vec3 size) {
		this.id = vehicle.getId();
		this.hitbox_name = hitbox_name;
		this.pos = pos;
		this.size = size;
	}
	
	public ToClientDebugHitboxPos(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		hitbox_name = buffer.readUtf();
		pos = DataSerializers.VEC3.read(buffer);
		size = DataSerializers.VEC3.read(buffer);
	}

    @Override
    public MessageType getType() {
        return PacketHandler.S2C_DEBUG_HITBOX_POS;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeUtf(hitbox_name);
		DataSerializers.VEC3.write(buffer, pos);
		DataSerializers.VEC3.write(buffer, size);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            UtilClientPacket.debugHitboxPos(id, hitbox_name, pos, size);
		});
	}

}
