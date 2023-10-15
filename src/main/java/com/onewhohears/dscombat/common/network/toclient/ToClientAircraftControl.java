package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.aircraft.AircraftInputs;
import com.onewhohears.dscombat.data.radar.RadarData.RadarMode;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientAircraftControl extends IPacket {
	
	public final int id;
	public final AircraftInputs inputs;
	public final byte weaponIndex;
	public final byte radarMode;
	public final boolean isFreeLook;
	public final boolean isLandingGear;
	public final float throttle;
	
	public ToClientAircraftControl(EntityVehicle plane) {
		this.id = plane.getId();
		this.inputs = plane.inputs;
		this.weaponIndex = (byte) plane.weaponSystem.getSelectedIndex();
		this.radarMode = (byte) plane.getRadarMode().ordinal();
		this.isFreeLook = plane.onlyFreeLook();
		this.isLandingGear = plane.isLandingGear();
		this.throttle = plane.getCurrentThrottle();
	}
	
	public ToClientAircraftControl(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		inputs = new AircraftInputs(buffer);
		weaponIndex = buffer.readByte();
		radarMode = buffer.readByte();
		isFreeLook = buffer.readBoolean();
		isLandingGear = buffer.readBoolean();
		throttle = buffer.readFloat();
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		inputs.write(buffer);
		buffer.writeByte(weaponIndex);
		buffer.writeByte(radarMode);
		buffer.writeBoolean(isFreeLook);
		buffer.writeBoolean(isLandingGear);
		buffer.writeFloat(throttle);
	}
	
	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.aircraftInputsPacket(id, inputs, weaponIndex, RadarMode.byId(radarMode), isLandingGear, isFreeLook, throttle);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
