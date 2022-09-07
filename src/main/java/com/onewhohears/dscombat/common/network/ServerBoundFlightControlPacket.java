package com.onewhohears.dscombat.common.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.entity.EntityBasicPlane;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ServerBoundFlightControlPacket {
	
	public final boolean throttleUp;
	public final boolean throttleDown;
	public final boolean pitchUp;
	public final boolean pitchDown;
	public final boolean rollLeft;
	public final boolean rollRight;
	public final boolean yawLeft;
	public final boolean yawRight;
	
	public ServerBoundFlightControlPacket(boolean throttleUp, boolean throttleDown, boolean pitchUp, boolean pitchDown,
			boolean rollLeft, boolean rollRight, boolean yawLeft, boolean yawRight) {
		this.throttleUp = throttleUp;
		this.throttleDown = throttleDown;
		this.pitchUp = pitchUp;
		this.pitchDown = pitchDown;
		this.rollLeft = rollLeft;
		this.rollRight = rollRight;
		this.yawLeft = yawLeft;
		this.yawRight = yawRight;
	}
	
	public ServerBoundFlightControlPacket(FriendlyByteBuf buffer) {
		this.throttleUp = buffer.readBoolean();
		this.throttleDown = buffer.readBoolean();
		this.pitchUp = buffer.readBoolean();
		this.pitchDown = buffer.readBoolean();
		this.rollLeft = buffer.readBoolean();
		this.rollRight = buffer.readBoolean();
		this.yawLeft = buffer.readBoolean();
		this.yawRight = buffer.readBoolean();
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
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			if (player.getRootVehicle() instanceof EntityBasicPlane plane) {
				if (plane.getControllingPassenger() == player) {
					plane.updateControls(throttleUp, throttleDown, pitchUp, pitchDown, rollLeft, rollRight, yawLeft, yawRight);
				}
			}
			success.set(true);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}
	
}
