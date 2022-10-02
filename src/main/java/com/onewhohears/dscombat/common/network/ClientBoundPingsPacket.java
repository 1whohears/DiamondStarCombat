package com.onewhohears.dscombat.common.network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.data.RadarData;
import com.onewhohears.dscombat.data.RadarData.RadarPing;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ClientBoundPingsPacket extends IPacket {
	
	public final int id;
	public final List<RadarPing> pings;
	
	public ClientBoundPingsPacket(int id, List<RadarPing> pings) {
		this.id = id;
		this.pings = pings;
	}
	
	public ClientBoundPingsPacket(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		pings = new ArrayList<RadarPing>();
		int num = buffer.readInt();
		for (int i = 0; i < num; ++i) pings.add(new RadarPing(buffer));
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeInt(pings.size());
		for (int i = 0; i < pings.size(); ++i) pings.get(i).write(buffer);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				Minecraft m = Minecraft.getInstance();
				Level world = m.level;
				EntityAbstractAircraft plane = (EntityAbstractAircraft) world.getEntity(id);
				if (plane != null) {
					RadarData radar = plane.getRadar();
					radar.readClientPingsFromServer(pings);
				}
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
