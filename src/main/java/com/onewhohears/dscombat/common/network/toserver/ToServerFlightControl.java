package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ToServerFlightControl extends IPacket {
	
	public final float throttle;
	public final float pitch;
	public final float roll;
	public final float yaw;
	public final boolean mouseMode;
	public final boolean flare;
	public final boolean shoot;
	public final int select;
	public final boolean openMenu;
	public final boolean gear;
	public final boolean special;
	public final boolean special2;
	public final boolean radarMode;
	public final boolean bothRoll;
	
	public ToServerFlightControl(float throttle, float pitch, float roll, float yaw,
			boolean mouseMode, boolean flare, boolean shoot, int select,
			boolean openMenu, boolean gear, boolean special, boolean special2, 
			boolean radarMode, boolean bothRoll) {
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
		this.special = special;
		this.special2 = special2;
		this.radarMode = radarMode;
		this.bothRoll = bothRoll;
	}
	
	public ToServerFlightControl(FriendlyByteBuf buffer) {
		throttle = buffer.readFloat();
		pitch = buffer.readFloat();
		roll = buffer.readFloat();
		yaw = buffer.readFloat();
		mouseMode = buffer.readBoolean();
		flare = buffer.readBoolean();
		shoot = buffer.readBoolean();
		select = buffer.readInt();
		openMenu = buffer.readBoolean();
		gear = buffer.readBoolean();
		special = buffer.readBoolean();
		special2 = buffer.readBoolean();
		radarMode = buffer.readBoolean();
		bothRoll = buffer.readBoolean();
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeFloat(throttle);
		buffer.writeFloat(pitch);
		buffer.writeFloat(roll);
		buffer.writeFloat(yaw);
		buffer.writeBoolean(mouseMode);
		buffer.writeBoolean(flare);
		buffer.writeBoolean(shoot);
		buffer.writeInt(select);
		buffer.writeBoolean(openMenu);
		buffer.writeBoolean(gear);
		buffer.writeBoolean(special);
		buffer.writeBoolean(special2);
		buffer.writeBoolean(radarMode);
		buffer.writeBoolean(bothRoll);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			if (player.getRootVehicle() instanceof EntityAircraft plane) {
				if (plane.getControllingPassenger() == player) {
					plane.updateControls(throttle, pitch, roll, yaw,
							mouseMode, flare, shoot, select, openMenu, 
							special, special2, radarMode, bothRoll);
					if (gear) plane.toggleLandingGear();
				}
			}
			success.set(true);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}
	
}
