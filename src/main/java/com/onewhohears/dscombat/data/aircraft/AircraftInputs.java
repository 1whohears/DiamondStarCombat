package com.onewhohears.dscombat.data.aircraft;

import net.minecraft.network.FriendlyByteBuf;

public class AircraftInputs {
	
	public boolean mouseMode, flare, shoot, openMenu, gear;
	public boolean special, special2, bothRoll;
	public float throttle, pitch, roll, yaw;
	
	public AircraftInputs() {
		reset();
	}
	
	public AircraftInputs(FriendlyByteBuf buffer) {
		read(buffer);
	}
	
	public void update(float throttle, float pitch, float roll, float yaw,
			boolean mouseMode, boolean flare, boolean shoot,
			boolean openMenu, boolean special, boolean special2, 
			boolean bothRoll, boolean gear) {
		this.throttle = throttle;
		this.pitch = pitch;
		this.roll = roll;
		this.yaw = yaw;
		this.mouseMode = mouseMode;
		this.flare = flare;
		this.shoot = shoot;
		this.openMenu = openMenu;
		this.special = special;
		this.special2 = special2;
		this.bothRoll = bothRoll;
		this.gear = gear;
	}
	
	public void copy(AircraftInputs other) {
		update(other.throttle, other.pitch, other.roll, other.yaw, 
			other.mouseMode, other.flare, other.shoot, 
			other.openMenu, other.special, other.special2, 
			other.bothRoll, other.gear);
	}
	
	public void reset() {
		this.throttle = 0;
		this.pitch = 0;
		this.roll = 0;
		this.yaw = 0;
		this.mouseMode = false;
		this.flare = false;
		this.shoot = false;
		this.openMenu = false;
		this.special = false;
		this.special2 = false;
		this.bothRoll = false;
		this.gear = false;
	}
	
	public void read(FriendlyByteBuf buffer) {
		throttle = buffer.readFloat();
		pitch = buffer.readFloat();
		roll = buffer.readFloat();
		yaw = buffer.readFloat();
		mouseMode = buffer.readBoolean();
		flare = buffer.readBoolean();
		shoot = buffer.readBoolean();
		openMenu = buffer.readBoolean();
		gear = buffer.readBoolean();
		special = buffer.readBoolean();
		special2 = buffer.readBoolean();
		bothRoll = buffer.readBoolean();
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeFloat(throttle);
		buffer.writeFloat(pitch);
		buffer.writeFloat(roll);
		buffer.writeFloat(yaw);
		buffer.writeBoolean(mouseMode);
		buffer.writeBoolean(flare);
		buffer.writeBoolean(shoot);
		buffer.writeBoolean(openMenu);
		buffer.writeBoolean(gear);
		buffer.writeBoolean(special);
		buffer.writeBoolean(special2);
		buffer.writeBoolean(bothRoll);
	}
	
}
