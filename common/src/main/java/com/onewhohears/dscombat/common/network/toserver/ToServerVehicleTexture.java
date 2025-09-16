package com.onewhohears.dscombat.common.network.toserver;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientVehicleTexture;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ToServerVehicleTexture extends BaseC2SMessage {
	
	public final int id;
	private EntityVehicle vehicle;
	private ByteBuf buffer;
	
	public ToServerVehicleTexture(EntityVehicle vehicle) {
		this.id = vehicle.getId();
		this.vehicle = vehicle;
	}
	
	public ToServerVehicleTexture(FriendlyByteBuf buffer) {
		this.id = buffer.readInt();
		this.buffer = buffer.copy().asReadOnly();
	}

    @Override
    public MessageType getType() {
        return PacketHandler.C2S_VEHICLE_TEXTURE;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		vehicle.textureManager.write(buffer);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            Player player = context.getPlayer();
			Level level = player.getLevel();
			if (level.getEntity(id) instanceof EntityVehicle vehicle) {
				vehicle.textureManager.read(buffer);
                PacketHandler.sendToTrackers(new ToClientVehicleTexture(player, vehicle), vehicle);
			}
		});
	}

}
