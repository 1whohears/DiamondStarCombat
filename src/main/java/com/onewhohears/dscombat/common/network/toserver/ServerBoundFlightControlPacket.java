package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ServerBoundFlightControlPacket extends IPacket {
	
	public final boolean throttleUp;
	public final boolean throttleDown;
	public final float pitch;
	public final float roll;
	public final float yaw;
	public final boolean mouseMode;
	public final boolean flare;
	public final boolean shoot;
	public final boolean select;
	
	public ServerBoundFlightControlPacket(boolean throttleUp, boolean throttleDown,
			float pitch, float roll, float yaw,
			boolean mouseMode, boolean flare, boolean shoot, boolean select) {
		this.throttleUp = throttleUp;
		this.throttleDown = throttleDown;
		this.pitch = pitch;
		this.roll = roll;
		this.yaw = yaw;
		this.mouseMode = mouseMode;
		this.flare = flare;
		this.shoot = shoot;
		this.select = select;
	}
	
	public ServerBoundFlightControlPacket(FriendlyByteBuf buffer) {
		throttleUp = buffer.readBoolean();
		throttleDown = buffer.readBoolean();
		pitch = buffer.readFloat();
		roll = buffer.readFloat();
		yaw = buffer.readFloat();
		mouseMode = buffer.readBoolean();
		flare = buffer.readBoolean();
		shoot = buffer.readBoolean();
		select = buffer.readBoolean();
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBoolean(throttleUp);
		buffer.writeBoolean(throttleDown);
		buffer.writeFloat(pitch);
		buffer.writeFloat(roll);
		buffer.writeFloat(yaw);
		buffer.writeBoolean(mouseMode);
		buffer.writeBoolean(flare);
		buffer.writeBoolean(shoot);
		buffer.writeBoolean(select);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			if (player.getRootVehicle() instanceof EntityAbstractAircraft plane) {
				if (plane.getControllingPassenger() == player) {
					plane.updateControls(throttleUp, throttleDown,
							pitch, roll, yaw,
							mouseMode, flare, shoot, select);
				}
			}
			success.set(true);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}
	
}
