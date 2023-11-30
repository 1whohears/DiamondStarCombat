package com.onewhohears.dscombat.client.input;

public class DSCClientInputs {
	
	private static MouseMode CURRENT_MOUSE_MODE = MouseMode.FREE_RELATIVE;
	
	public static MouseMode getMouseMode() {
		return CURRENT_MOUSE_MODE;
	}
	
	public static MouseMode cycleMouseMode() {
		CURRENT_MOUSE_MODE = CURRENT_MOUSE_MODE.cycle();
		return CURRENT_MOUSE_MODE;
	}
	/**
	 * MOUSE INPUTS CONTROL VEHICLE.
	 * CAMERA LOCKED TOWARDS VEHICLE FORWARD DIRECTION. 
	 */
	public static boolean isCameraLockedForward() {
		return CURRENT_MOUSE_MODE.isLockedForward();
	}
	/**
	 * MOUSE INPUTS DONT CONTROL VEHICLE.
	 * CAMERA CAN MOVE FREELY.
	 * TRUE IF FREE RELATIVE OR FREE GLOBAL.
	 */
	public static boolean isCameraFree() {
		return CURRENT_MOUSE_MODE.isFree();
	}
	
	public static enum MouseMode {
		FREE_RELATIVE,
		FREE_GLOBAL,
		LOCKED_FORWARD;
		public MouseMode cycle() {
			int index = ordinal();
			if (index == values().length-1) return values()[0];
			else return values()[++index];
		}
		public boolean isLockedForward() {
			return this == LOCKED_FORWARD;
		}
		public boolean isFree() {
			return this == FREE_RELATIVE || this == FREE_GLOBAL;
		}
	}
	
}
