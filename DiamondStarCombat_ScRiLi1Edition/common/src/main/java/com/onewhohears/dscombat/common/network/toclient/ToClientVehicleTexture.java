package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.UtilClientPacket;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class ToClientVehicleTexture extends BaseS2CMessage {
	
	public final int ignore_player_id;
	public final int vehicle_id;
	private EntityVehicle vehicle;
	private ByteBuf buffer;
	
	public ToClientVehicleTexture(Player ignorePlayer, EntityVehicle vehicle) {
		this.ignore_player_id = ignorePlayer.getId();
		this.vehicle_id = vehicle.getId();
		this.vehicle = vehicle;
	}
	
	public ToClientVehicleTexture(FriendlyByteBuf buffer) {
		this.ignore_player_id = buffer.readInt();
		this.vehicle_id = buffer.readInt();
		this.buffer = buffer.copy().asReadOnly();
	}

    @Override
    public MessageType getType() {
        return PacketHandler.S2C_VEHICLE_TEXTURE;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(ignore_player_id);
		buffer.writeInt(vehicle_id);
		vehicle.textureManager.write(buffer);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            UtilClientPacket.vehicleTexturePacket(ignore_player_id, vehicle_id, buffer);
		});
	}

}
