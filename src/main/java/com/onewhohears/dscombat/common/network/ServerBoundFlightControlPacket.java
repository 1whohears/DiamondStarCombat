package com.onewhohears.dscombat.common.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ServerBoundFlightControlPacket extends IServerBoundPacket {
	
	public final boolean throttleUp;
	public final boolean throttleDown;
	public final boolean pitchUp;
	public final boolean pitchDown;
	public final boolean rollLeft;
	public final boolean rollRight;
	public final boolean yawLeft;
	public final boolean yawRight;
	public final boolean mouseMode;
	public final boolean flare;
	public final boolean shoot;
	public final boolean select;
	
	public ServerBoundFlightControlPacket(boolean throttleUp, boolean throttleDown, boolean pitchUp, boolean pitchDown,
			boolean rollLeft, boolean rollRight, boolean yawLeft, boolean yawRight, 
			boolean mouseMode, boolean flare, boolean shoot, boolean select) {
		this.throttleUp = throttleUp;
		this.throttleDown = throttleDown;
		this.pitchUp = pitchUp;
		this.pitchDown = pitchDown;
		this.rollLeft = rollLeft;
		this.rollRight = rollRight;
		this.yawLeft = yawLeft;
		this.yawRight = yawRight;
		this.mouseMode = mouseMode;
		this.flare = flare;
		this.shoot = shoot;
		this.select = select;
	}
	
	public ServerBoundFlightControlPacket(FriendlyByteBuf buffer) {
		throttleUp = buffer.readBoolean();
		throttleDown = buffer.readBoolean();
		pitchUp = buffer.readBoolean();
		pitchDown = buffer.readBoolean();
		rollLeft = buffer.readBoolean();
		rollRight = buffer.readBoolean();
		yawLeft = buffer.readBoolean();
		yawRight = buffer.readBoolean();
		mouseMode = buffer.readBoolean();
		flare = buffer.readBoolean();
		shoot = buffer.readBoolean();
		select = buffer.readBoolean();
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBoolean(throttleUp);
		buffer.writeBoolean(throttleDown);
		buffer.writeBoolean(pitchUp);
		buffer.writeBoolean(pitchDown);
		buffer.writeBoolean(rollLeft);
		buffer.writeBoolean(rollRight);
		buffer.writeBoolean(yawLeft);
		buffer.writeBoolean(yawRight);
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
					plane.updateControls(throttleUp, throttleDown, pitchUp, pitchDown, 
							rollLeft, rollRight, yawLeft, yawRight,
							mouseMode, flare, shoot, select);
				}
			}
			success.set(true);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}
	
}
