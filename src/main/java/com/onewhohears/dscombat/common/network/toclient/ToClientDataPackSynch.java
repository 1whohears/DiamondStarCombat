package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.aircraft.AircraftPresets;
import com.onewhohears.dscombat.data.radar.RadarPresets;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientDataPackSynch extends IPacket {
	
	public ToClientDataPackSynch() {
		
	}
	
	public ToClientDataPackSynch(FriendlyByteBuf buffer) {
		super(buffer);
		WeaponPresets.get().readBuffer(buffer);
		RadarPresets.get().readBuffer(buffer);
		AircraftPresets.get().readBuffer(buffer);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		WeaponPresets.get().writeToBuffer(buffer);
		RadarPresets.get().writeToBuffer(buffer);
		AircraftPresets.get().writeToBuffer(buffer);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
