package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class ServerBoundSwitchSeatPacket extends IPacket {
	
	public final int id;
	
	public ServerBoundSwitchSeatPacket(int id) {
		this.id = id;
		//System.out.println("CREATED PACKET");
	}
	
	public ServerBoundSwitchSeatPacket(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		//System.out.println("DECODING PACKET");
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		//System.out.println("ENCODING PACKET");
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		//System.out.println("HANDELING PACKET");
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			success.set(true);
			ServerPlayer player = ctx.get().getSender();
			ServerLevel level = player.getLevel();
			if (level.getEntity(id) instanceof EntityAbstractAircraft plane) {
				if (!plane.switchSeat(player)) {
					player.displayClientMessage(UtilMCText.simpleText("No Open Seats"), true);
				}
			}
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
