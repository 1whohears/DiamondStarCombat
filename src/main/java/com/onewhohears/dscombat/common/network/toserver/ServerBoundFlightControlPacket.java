package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ServerBoundFlightControlPacket extends IPacket {
	
	public final float throttle;
	public final float pitch;
	public final float roll;
	public final float yaw;
	public final boolean mouseMode;
	public final boolean flare;
	public final boolean shoot;
	public final boolean select;
	public final boolean openMenu;
	public final boolean gear;
	
	public ServerBoundFlightControlPacket(float throttle, float pitch, float roll, float yaw,
			boolean mouseMode, boolean flare, boolean shoot, boolean select,
			boolean openMenu, boolean gear) {
		this.throttle = throttle;
		this.pitch = pitch;
		this.roll = roll;
		this.yaw = yaw;
		this.mouseMode = mouseMode;
		this.flare = flare;
		this.shoot = shoot;
		this.select = select;
		this.openMenu = openMenu;
		this.gear = gear;
	}
	
	public ServerBoundFlightControlPacket(FriendlyByteBuf buffer) {
		throttle = buffer.readFloat();
		pitch = buffer.readFloat();
		roll = buffer.readFloat();
		yaw = buffer.readFloat();
		mouseMode = buffer.readBoolean();
		flare = buffer.readBoolean();
		shoot = buffer.readBoolean();
		select = buffer.readBoolean();
		openMenu = buffer.readBoolean();
		gear = buffer.readBoolean();
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeFloat(throttle);
		buffer.writeFloat(pitch);
		buffer.writeFloat(roll);
		buffer.writeFloat(yaw);
		buffer.writeBoolean(mouseMode);
		buffer.writeBoolean(flare);
		buffer.writeBoolean(shoot);
		buffer.writeBoolean(select);
		buffer.writeBoolean(openMenu);
		buffer.writeBoolean(gear);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			if (player.getRootVehicle() instanceof EntityAbstractAircraft plane) {
				if (plane.getControllingPassenger() == player) {
					plane.updateControls(throttle, pitch, roll, yaw,
							mouseMode, flare, shoot, select, openMenu);
					if (gear) plane.switchLandingGear();
				}
			}
			success.set(true);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}
	
}
