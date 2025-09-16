package com.onewhohears.dscombat.common.network.toserver;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ToServerFixHitboxes extends BaseC2SMessage {
	
	private final int id;
	
	public ToServerFixHitboxes(EntityVehicle vehicle) {
		this.id = vehicle.getId();
	}
	
	public ToServerFixHitboxes(FriendlyByteBuf buffer) {
		id = buffer.readInt();
	}

    @Override
    public MessageType getType() {
        return PacketHandler.C2S_FIX_HITBOXES;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            Player player = context.getPlayer();
			if (player == null) return;
			Level level = player.level;
			if (!(level.getEntity(id) instanceof EntityVehicle vehicle)) return;
			vehicle.refreshHitboxes();
		});
	}

}
