package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.aircraft.AircraftPresets;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientDataPackSynch extends IPacket {
	
	public final AircraftPreset[] presets;
	
	public ToClientDataPackSynch() {
		presets = AircraftPresets.get().getAllPresets();
	}
	
	public ToClientDataPackSynch(FriendlyByteBuf buffer) {
		super(buffer);
		presets = AircraftPresets.readBuffer(buffer);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		AircraftPresets.get().writeBuffer(buffer);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.dataPackSynch(presets);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
