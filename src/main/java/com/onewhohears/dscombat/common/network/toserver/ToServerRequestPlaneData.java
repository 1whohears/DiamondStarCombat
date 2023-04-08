package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientRecievePlaneData;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.PacketDistributor;

public class ToServerRequestPlaneData extends IPacket {
	
	public final int id;
	
	public ToServerRequestPlaneData(int id) {
		this.id = id;
	}
	
	public ToServerRequestPlaneData(FriendlyByteBuf buffer) {
		id = buffer.readInt();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			Level level = ctx.get().getSender().level;
			if (level.getEntity(id) instanceof EntityAircraft plane) {
				PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> plane), 
					new ToClientRecievePlaneData(plane.getId(), 
						plane.partsManager.getSlots(), plane.weaponSystem, plane.radarSystem));
			}
			success.set(true);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
