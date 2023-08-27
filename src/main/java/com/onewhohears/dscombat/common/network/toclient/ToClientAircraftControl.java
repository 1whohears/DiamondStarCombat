package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.aircraft.AircraftInputs;
import com.onewhohears.dscombat.data.radar.RadarData.RadarMode;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientAircraftControl extends IPacket {
	
	public final int id;
	public final AircraftInputs inputs;
	public final int weaponIndex;
	public final int radarMode;
	public final boolean isFreeLook;
	public final boolean isLandingGear;
	
	public ToClientAircraftControl(EntityAircraft plane) {
		this.id = plane.getId();
		this.inputs = plane.inputs;
		this.weaponIndex = plane.weaponSystem.getSelectedIndex();
		this.radarMode = plane.getRadarMode().ordinal();
		this.isFreeLook = plane.isFreeLook();
		this.isLandingGear = plane.isLandingGear();
	}
	
	public ToClientAircraftControl(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		inputs = new AircraftInputs(buffer);
		weaponIndex = buffer.readInt();
		radarMode = buffer.readInt();
		isFreeLook = buffer.readBoolean();
		isLandingGear = buffer.readBoolean();
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		inputs.write(buffer);
		buffer.writeInt(weaponIndex);
		buffer.writeInt(radarMode);
		buffer.writeBoolean(isFreeLook);
		buffer.writeBoolean(isLandingGear);
	}
	
	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.aircraftInputsPacket(id, inputs, weaponIndex, RadarMode.byId(radarMode), isLandingGear, isFreeLook);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
