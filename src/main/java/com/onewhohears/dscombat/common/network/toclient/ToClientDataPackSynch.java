package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.event.custom.RegisterPresetTypesEvent;
import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.graph.StatGraphs;
import com.onewhohears.dscombat.data.parts.PartPresets;
import com.onewhohears.dscombat.data.radar.RadarPresets;
import com.onewhohears.dscombat.data.vehicle.VehiclePresets;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientDataPackSynch extends IPacket {
	
	public ToClientDataPackSynch() {
		
	}
	
	public ToClientDataPackSynch(FriendlyByteBuf buffer) {
		super(buffer);
		MinecraftForge.EVENT_BUS.post(new RegisterPresetTypesEvent());
		StatGraphs.get().readBuffer(buffer);
		PartPresets.get().readBuffer(buffer);
		WeaponPresets.get().readBuffer(buffer);
		RadarPresets.get().readBuffer(buffer);
		VehiclePresets.get().readBuffer(buffer);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		StatGraphs.get().writeToBuffer(buffer);
		PartPresets.get().writeToBuffer(buffer);
		WeaponPresets.get().writeToBuffer(buffer);
		RadarPresets.get().writeToBuffer(buffer);
		VehiclePresets.get().writeToBuffer(buffer);
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
