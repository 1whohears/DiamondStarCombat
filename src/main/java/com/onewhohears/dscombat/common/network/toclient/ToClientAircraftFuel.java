package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientAircraftFuel extends IPacket {
	
	public final int id;
	public final float[] fuels;
	
	public ToClientAircraftFuel(EntityAircraft plane) {
		this.id = plane.getId();
		this.fuels = plane.partsManager.getFuelsForClient();
	}
	
	public ToClientAircraftFuel(FriendlyByteBuf buffer) {
		super(buffer);
		this.id = buffer.readInt();
		int num = buffer.readInt();
		fuels = new float[num];
		for (int i = 0 ; i < num; ++i) fuels[i] = buffer.readFloat();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeInt(fuels.length);
		for (int i = 0; i < fuels.length; ++i) buffer.writeFloat(fuels[i]);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.setAircraftFuel(id, fuels);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
