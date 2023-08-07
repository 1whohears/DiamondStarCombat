package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientAircraftControl;
import com.onewhohears.dscombat.data.aircraft.AircraftInputs;
import com.onewhohears.dscombat.data.radar.RadarData.RadarMode;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class ToServerAircraftControl extends IPacket {
	
	public final AircraftInputs inputs;
	public final int weaponIndex;
	public final int radarMode;
	public final boolean isLandingGear;
	public final boolean isFreeLook;
	
	public ToServerAircraftControl(EntityAircraft plane) {
		this.inputs = plane.inputs;
		this.weaponIndex = plane.weaponSystem.getSelectedIndex();
		this.radarMode = plane.getRadarMode().ordinal();
		this.isLandingGear = plane.isLandingGear();
		this.isFreeLook = plane.isFreeLook();
	}
	
	public ToServerAircraftControl(FriendlyByteBuf buffer) {
		inputs = new AircraftInputs(buffer);
		weaponIndex = buffer.readInt();
		radarMode = buffer.readInt();
		isLandingGear = buffer.readBoolean();
		isFreeLook = buffer.readBoolean();
	}
	
	public void encode(FriendlyByteBuf buffer) {
		inputs.write(buffer);
		buffer.writeInt(weaponIndex);
		buffer.writeInt(radarMode);
		buffer.writeBoolean(isLandingGear);
		buffer.writeBoolean(isFreeLook);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			if (player.getRootVehicle() instanceof EntityAircraft plane) {
				if (plane.getControllingPassenger() == player) {
					plane.inputs.copy(this.inputs);
					plane.weaponSystem.setSelected(weaponIndex);
					plane.setRadarMode(RadarMode.byId(radarMode));
					plane.setLandingGear(isLandingGear);
					plane.setFreeLook(isFreeLook);
					PacketHandler.INSTANCE.send(
						PacketDistributor.TRACKING_ENTITY.with(() -> plane), 
						new ToClientAircraftControl(plane));
				}
			}
			success.set(true);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}
	
}
