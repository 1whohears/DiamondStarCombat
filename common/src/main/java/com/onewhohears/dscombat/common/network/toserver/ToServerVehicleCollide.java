package com.onewhohears.dscombat.common.network.toserver;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import com.onewhohears.onewholibs.util.UtilEntity;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ToServerVehicleCollide extends BaseC2SMessage {
	
	public final int id;
	public final float amount;
	public final boolean isFall;
	
	public ToServerVehicleCollide(int id, float amount, boolean isFall) {
		this.id = id;
		this.amount = amount;
		this.isFall = isFall;
	}
	
	public ToServerVehicleCollide(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		amount = buffer.readFloat();
		isFall = buffer.readBoolean();
	}

    @Override
    public MessageType getType() {
        return PacketHandler.C2S_VEHICLE_COLLIDE;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeFloat(amount);
		buffer.writeBoolean(isFall);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            Player player = context.getPlayer();
            Level level = UtilEntity.getLevel(player);
			if (level.getEntity(id) instanceof EntityVehicle plane) {
				plane.collideHurt(amount, isFall);
			}
		});
	}

}
