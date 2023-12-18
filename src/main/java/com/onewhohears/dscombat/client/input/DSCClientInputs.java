package com.onewhohears.dscombat.client.input;

import net.minecraft.client.Minecraft;

public class DSCClientInputs {
	
	public static boolean disable3rdPersonVehicle = false;
	
	private static double mouseCenterX = 0;
	private static double mouseCenterY = 0;
	
	private static int hoverIndex = -1;
	private static double radarDisplayRange = 1000;
	
	public static final long MOUNT_SHOOT_COOLDOWN = 500;
	private static long mountTime;
	
	private static MouseMode CURRENT_MOUSE_MODE = MouseMode.FREE_RELATIVE;
	/**
	 * set mouseCenterX and mouseCenterY to the mouse's current position.
	 * used to move the "joystick" back to the middle when in LOCKED_FORWARD MouseMode.
	 */
	public static void centerMousePos() {
		Minecraft m = Minecraft.getInstance();
		mouseCenterX = m.mouseHandler.xpos();
		mouseCenterY = m.mouseHandler.ypos();
	}
	/**
	 * @return the mouse x position related to the "joystick's" origin when in LOCKED_FORWARD MouseMode.
	 */
	public static double getMouseCenterX() {
		return mouseCenterX;
	}
	/**
	 * @return the mouse y position related to the "joystick's" origin when in LOCKED_FORWARD MouseMode.
	 */
	public static double getMouseCenterY() {
		return mouseCenterY;
	}
	/**
	 * @param x the mouse x position related to the "joystick's" origin when in LOCKED_FORWARD MouseMode.
	 */
	public static void setMouseCenterX(double x) {
		mouseCenterX = x;
	}
	/**
	 * @param y the mouse y position related to the "joystick's" origin when in LOCKED_FORWARD MouseMode.
	 */
	public static void setMouseCenterY(double y) {
		mouseCenterY = y;
	}
	/**
	 * @return the index of the ping the client's mouse is hovering over. -1 if {@link DSCClientInputs#isRadarHovering} is true.
	 */
	public static int getRadarHoverIndex() {
		return hoverIndex;
	}
	/**
	 * @param index the index of the ping the client's mouse is hovering over
	 */
	public static void setRadarHoverIndex(int index) {
		hoverIndex = index;
	}
	/**
	 * called if the client's mouse isn't hovering over any pings on the hud
	 */
	public static void resetRadarHoverIndex() {
		hoverIndex = -1;
	}
	/**
	 * @return is the client's mouse hovering over a radar ping on the hud
	 */
	public static boolean isRadarHovering() {
		return hoverIndex != -1;
	}
	/**
	 * @return the max distance of a radar ping client radar screens will display
	 */
	public static double getRadarDisplayRange() {
		return radarDisplayRange;
	}
	
	public static void setRadarDisplayRange(double range) {
		radarDisplayRange = range;
	}
	/**
	 * @return the last time in millis the client mounted a vehicle
 	 */
	public static long getClientMountTime() {
		return mountTime;
	}
	/**
	 * @param time the last time in millis the client mounted a vehicle
	 */
	public static void setClientMountTime(long time) {
		mountTime = time;
	}
	/**
	 * @return {@link MouseMode#FREE_RELATIVE}, {@link MouseMode#FREE_GLOBAL}, or {@link MouseMode#LOCKED_FORWARD}
	 */
	public static MouseMode getMouseMode() {
		return CURRENT_MOUSE_MODE;
	}
	/**
	 * sets CURRENT_MOUSE_MODE to the next MouseMode option.
	 * @return the new current Mouse Mode option.
	 */
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
	/**
	 * MOUSE INPUTS DONT CONTROL VEHICLE.
	 * CAMERA CAN MOVE FREELY.
	 * CAMERA WILL MOVE WITH PLANE.
	 */
	public static boolean isCameraFreeRelative() {
		return CURRENT_MOUSE_MODE.isFreeRelative();
	}
	/**
	 * MOUSE INPUTS DONT CONTROL VEHICLE.
	 * CAMERA CAN MOVE FREELY.
	 * NOT EFFECTED BY PLANE ROTATING.
	 */
	public static boolean isCameraFreeGlobal() {
		return CURRENT_MOUSE_MODE.isFreeGlobal();
	}
	
	public static enum MouseMode {
		/**
		 * Camera can move freely but turns when the vehicle turns.
		 * Keeps the camera's angle the same relative angle to the vehicle. 
		 */
		FREE_RELATIVE, 
		/**
		 * Camera moves freely. Is not effected by the vehicle's rotation.
		 */
		FREE_GLOBAL,
		/**
		 * Camera is locked towards the vehicle's forward direction.
		 */
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
		public boolean isFreeRelative() {
			return this == FREE_RELATIVE;
		}
		public boolean isFreeGlobal() {
			return this == FREE_GLOBAL;
		}
	}
	
}
