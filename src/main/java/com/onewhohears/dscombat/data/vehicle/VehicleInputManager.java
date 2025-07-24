package com.onewhohears.dscombat.data.vehicle;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ToServerVehicleControl;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.network.FriendlyByteBuf;

/**
 * used to centrally organize some vehicle's inputs.
 * see {@link com.onewhohears.dscombat.client.event.forgebus.ClientInputEvents} 
 * to see how these inputs are sent to server.
 * @author 1whohears
 */
public class VehicleInputManager {
	
	public boolean flare, chaff, afterburner, turnAssist = true;
	public boolean special, special2, bothRoll;
	public float throttle, pitch, roll, yaw;
	
	protected boolean isDriverCameraLocked;
	protected int weaponIndex;
	protected float currentThrottle;
	
	public VehicleInputManager() {
		reset();
	}
	
	public VehicleInputManager(FriendlyByteBuf buffer) {
		read(buffer);
	}
	
	public void clientPilotControlsToServer(EntityVehicle parent, 
			float throttle, float pitch, float roll, float yaw,
			boolean flare, boolean chaff, boolean afterburner,
			boolean special, boolean special2, boolean bothRoll,
			boolean isDriverCameraLocked, boolean turnAssist) {
		this.throttle = throttle;
		this.pitch = pitch;
		this.roll = roll;
		this.yaw = yaw;
		this.flare = flare;
		this.chaff = chaff;
		this.afterburner = afterburner;
		this.special = special;
		this.special2 = special2;
		this.bothRoll = bothRoll;
		this.isDriverCameraLocked = isDriverCameraLocked;
		this.turnAssist = turnAssist;
		parent.setDriverCameraLocked(isDriverCameraLocked);
		weaponIndex = parent.weaponSystem.getSelectedIndex();
		currentThrottle = parent.getCurrentThrottle();
		PacketHandler.INSTANCE.sendToServer(new ToServerVehicleControl(parent));
	}
	
	public void updateInputsFromPacket(VehicleInputManager other, EntityVehicle parent) {
		// raw inputs
		this.flare = other.flare;
		this.chaff = other.chaff;
		this.afterburner = other.afterburner;
		this.special = other.special;
		this.special2 = other.special2;
		this.throttle = other.throttle;
		this.pitch = other.pitch;
		this.roll = other.roll;
		this.yaw = other.yaw;
		this.bothRoll = other.bothRoll;
		this.weaponIndex = other.weaponIndex;
		this.currentThrottle = other.currentThrottle;
		this.isDriverCameraLocked = other.isDriverCameraLocked;
		this.turnAssist = other.turnAssist;
		// special inputs
		parent.setCurrentThrottle(currentThrottle);
		parent.weaponSystem.setSelected(weaponIndex);
		parent.setDriverCameraLocked(isDriverCameraLocked);
	}
	
	public void reset() {
		this.throttle = 0;
		this.pitch = 0;
		this.roll = 0;
		this.yaw = 0;
		this.flare = false;
		this.chaff = false;
		this.afterburner = false;
		this.special = false;
		this.special2 = false;
		this.bothRoll = false;
		this.isDriverCameraLocked = false;
		this.turnAssist = true;
	}
	
	public void write(FriendlyByteBuf buffer) {
		// raw inputs
		buffer.writeFloat(throttle);
		buffer.writeFloat(pitch);
		buffer.writeFloat(roll);
		buffer.writeFloat(yaw);
		buffer.writeBoolean(flare);
		buffer.writeBoolean(chaff);
		buffer.writeBoolean(afterburner);
		buffer.writeBoolean(special);
		buffer.writeBoolean(special2);
		buffer.writeBoolean(bothRoll);
		buffer.writeBoolean(turnAssist);
		// special vehicle system inputs
		buffer.writeShort(weaponIndex);
		buffer.writeFloat(currentThrottle);
		buffer.writeBoolean(isDriverCameraLocked);
	}
	
	public void read(FriendlyByteBuf buffer) {
		// raw inputs
		throttle = buffer.readFloat();
		pitch = buffer.readFloat();
		roll = buffer.readFloat();
		yaw = buffer.readFloat();
		flare = buffer.readBoolean();
		chaff = buffer.readBoolean();
		afterburner = buffer.readBoolean();
		special = buffer.readBoolean();
		special2 = buffer.readBoolean();
		bothRoll = buffer.readBoolean();
		turnAssist = buffer.readBoolean();
		// special vehicle system inputs
		weaponIndex = buffer.readShort();
		currentThrottle = buffer.readFloat();
		isDriverCameraLocked = buffer.readBoolean();
	}
	
}
