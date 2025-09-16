package com.onewhohears.dscombat.common.network.toserver;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.entity.parts.EntityRidablePart;
import com.onewhohears.dscombat.init.DataSerializers;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class ToServerSeatPos extends BaseC2SMessage {
			
	public final Vec3 seatPos;
	
	public ToServerSeatPos(Vec3 seatPos) {
		this.seatPos = seatPos;
	}
	
	public ToServerSeatPos(FriendlyByteBuf buffer) {
		seatPos = DataSerializers.VEC3.read(buffer);
	}

    @Override
    public MessageType getType() {
        return PacketHandler.C2S_SEAT_POS;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		DataSerializers.VEC3.write(buffer, seatPos);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            Player player = context.getPlayer();
			if (player == null) return;
			if (player.isPassenger() && player.getVehicle() instanceof EntityRidablePart) {
				player.getVehicle().setPosRaw(seatPos.x, seatPos.y, seatPos.z);
			}
			//System.out.println("ToServerSeatPos = "+seatPos);
		});
	}

}
