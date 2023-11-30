package com.onewhohears.dscombat.data.aircraft;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ToServerAircraftControl;
import com.onewhohears.dscombat.data.radar.RadarData.RadarMode;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.network.FriendlyByteBuf;

/**
 * used to centrally organise some vehicle's inputs.
 * see {@link com.onewhohears.dscombat.client.event.forgebus.ClientInputEvents} 
 * to see how these inputs are sent to server.
 * @author 1whohears
 */
public class VehicleInputManager {
	
	public boolean flare, shoot, openMenu;
	public boolean special, special2, bothRoll;
	public float throttle, pitch, roll, yaw;
	
	protected boolean isLandingGear, isDriverCameraLocked;
	protected int weaponIndex, radarModeOrdinal;
	protected float currentThrottle;
	
	public VehicleInputManager() {
		reset();
	}
	
	public VehicleInputManager(FriendlyByteBuf buffer) {
		read(buffer);
	}
	
	public void clientUpdateServerControls(EntityVehicle parent, 
			float throttle, float pitch, float roll, float yaw,
			boolean flare, boolean shoot, boolean openMenu, 
			boolean special, boolean special2, boolean bothRoll,
			int selectNextWeapon, boolean cycleRadarMode, boolean toggleGear,
			boolean isDriverCameraLocked) {
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
		this.isDriverCameraLocked = isDriverCameraLocked;
		parent.setDriverCameraLocked(isDriverCameraLocked);
		parent.weaponSystem.selectNextWeapon(selectNextWeapon);
		weaponIndex = parent.weaponSystem.getSelectedIndex();
		if (cycleRadarMode) parent.cycleRadarMode();
		radarModeOrdinal = parent.getRadarMode().ordinal();
		if (toggleGear) parent.toggleLandingGear();
		isLandingGear = parent.isLandingGear();
		currentThrottle = parent.getCurrentThrottle();
		PacketHandler.INSTANCE.sendToServer(new ToServerAircraftControl(parent));
	}
	
	public void updateInputsFromPacket(VehicleInputManager other, EntityVehicle parent) {
		// raw inputs
		this.flare = other.flare;
		this.shoot = other.shoot;
		this.openMenu = other.openMenu;
		this.special = other.special;
		this.special2 = other.special2;
		this.throttle = other.throttle;
		this.pitch = other.pitch;
		this.roll = other.roll;
		this.yaw = other.yaw;
		this.isLandingGear = other.isLandingGear;
		this.weaponIndex = other.weaponIndex;
		this.radarModeOrdinal = other.radarModeOrdinal;
		this.currentThrottle = other.currentThrottle;
		this.isDriverCameraLocked = other.isDriverCameraLocked;
		// special inputs
		parent.setRadarMode(RadarMode.byId(radarModeOrdinal));
		parent.setLandingGear(isLandingGear);
		parent.setCurrentThrottle(currentThrottle);
		parent.weaponSystem.setSelected(weaponIndex);
		parent.setDriverCameraLocked(isDriverCameraLocked);
		if (!parent.level.isClientSide && shoot) 
			parent.weaponSystem.shootSelected(parent.getControllingPassenger());
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
		this.isDriverCameraLocked = false;
	}
	
	public void write(FriendlyByteBuf buffer) {
		// raw inputs
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
		// special vehicle system inputs
		buffer.writeBoolean(isLandingGear);
		buffer.writeByte(radarModeOrdinal);
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
		shoot = buffer.readBoolean();
		openMenu = buffer.readBoolean();
		special = buffer.readBoolean();
		special2 = buffer.readBoolean();
		bothRoll = buffer.readBoolean();
		// special vehicle system inputs
		isLandingGear = buffer.readBoolean();
		radarModeOrdinal = buffer.readByte();
		weaponIndex = buffer.readShort();
		currentThrottle = buffer.readFloat();
		isDriverCameraLocked = buffer.readBoolean();
	}
	
}
