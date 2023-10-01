package com.onewhohears.dscombat.data.aircraft;

import net.minecraft.network.FriendlyByteBuf;

/**
 * used to centrally organize some vehicle's inputs.
 * see {@link com.onewhohears.dscombat.client.event.forgebus.ClientInputEvents} 
 * to see how these inputs are sent to server.
 * @author 1whohears
 */
public class AircraftInputs {
	
	public boolean flare, shoot, openMenu;
	public boolean special, special2, bothRoll;
	public float throttle, pitch, roll, yaw;
	
	public AircraftInputs() {
		reset();
	}
	
	public AircraftInputs(FriendlyByteBuf buffer) {
		read(buffer);
	}
	
	public void update(float throttle, float pitch, float roll, float yaw,
			boolean flare, boolean shoot, boolean openMenu, 
			boolean special, boolean special2, boolean bothRoll) {
		this.throttle = throttle;
		this.pitch = pitch;
		this.roll = roll;
		this.yaw = yaw;
		this.flare = flare;
		this.shoot = shoot;
		this.openMenu = openMenu;
		this.special = special;
		this.special2 = special2;
		this.bothRoll = bothRoll;
	}
	
	public void copy(AircraftInputs other) {
		update(other.throttle, other.pitch, other.roll, other.yaw, 
			other.flare, other.shoot, other.openMenu, 
			other.special, other.special2, other.bothRoll);
	}
	
	public void reset() {
		this.throttle = 0;
		this.pitch = 0;
		this.roll = 0;
		this.yaw = 0;
		this.flare = false;
		this.shoot = false;
		this.openMenu = false;
		this.special = false;
		this.special2 = false;
		this.bothRoll = false;
	}
	
	public void read(FriendlyByteBuf buffer) {
		throttle = buffer.readFloat();
		pitch = buffer.readFloat();
		roll = buffer.readFloat();
		yaw = buffer.readFloat();
		flare = buffer.readBoolean();
		shoot = buffer.readBoolean();
		openMenu = buffer.readBoolean();
		special = buffer.readBoolean();
		special2 = buffer.readBoolean();
		bothRoll = buffer.readBoolean();
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeFloat(throttle);
		buffer.writeFloat(pitch);
		buffer.writeFloat(roll);
		buffer.writeFloat(yaw);
		buffer.writeBoolean(flare);
		buffer.writeBoolean(shoot);
		buffer.writeBoolean(openMenu);
		buffer.writeBoolean(special);
		buffer.writeBoolean(special2);
		buffer.writeBoolean(bothRoll);
	}
	
}
