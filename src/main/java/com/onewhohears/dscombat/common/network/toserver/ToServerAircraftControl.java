package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientAircraftControl;
import com.onewhohears.dscombat.data.aircraft.AircraftInputs;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class ToServerAircraftControl extends IPacket {
	
	public final AircraftInputs inputs;
	public final int weaponIndex;
	
	public ToServerAircraftControl(EntityAircraft plane) {
		this.inputs = plane.inputs;
		this.weaponIndex = plane.weaponSystem.getSelectedIndex();
	}
	
	public ToServerAircraftControl(FriendlyByteBuf buffer) {
		inputs = new AircraftInputs(buffer);
		weaponIndex = buffer.readInt();
	}
	
	public void encode(FriendlyByteBuf buffer) {
		inputs.write(buffer);
		buffer.writeInt(weaponIndex);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			if (player.getRootVehicle() instanceof EntityAircraft plane) {
				if (plane.getControllingPassenger() == player) {
					plane.inputs.copy(this.inputs);
					plane.weaponSystem.setSelected(weaponIndex);
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
