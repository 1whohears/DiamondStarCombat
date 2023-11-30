package com.onewhohears.dscombat.client.input;

import net.minecraft.client.Minecraft;

public class DSCClientInputs {
	
	private static double mouseCenterX = 0;
	private static double mouseCenterY = 0;
	
	private static int hoverIndex = -1;
	private static double radarDisplayRange = 1000;
	
	public static final long MOUNT_SHOOT_COOLDOWN = 500;
	private static long mountTime;
	
	private static MouseMode CURRENT_MOUSE_MODE = MouseMode.FREE_RELATIVE;
	
	public static void centerMousePos() {
		Minecraft m = Minecraft.getInstance();
		mouseCenterX = m.mouseHandler.xpos();
		mouseCenterY = m.mouseHandler.ypos();
	}
	
	public static double getMouseCenterX() {
		return mouseCenterX;
	}
	
	public static double getMouseCenterY() {
		return mouseCenterY;
	}
	
	public static double setMouseCenterX(double x) {
		mouseCenterX = x;
		return mouseCenterX;
	}
	
	public static double setMouseCenterY(double y) {
		mouseCenterY = y;
		return mouseCenterY;
	}
	
	public static int getRadarHoverIndex() {
		return hoverIndex;
	}
	
	public static void setRadarHoverIndex(int index) {
		hoverIndex = index;
	}
	
	public static void resetRadarHoverIndex() {
		hoverIndex = -1;
	}
	
	public static boolean isRadarHovering() {
		return hoverIndex != -1;
	}
	
	public static double getRadarDisplayRange() {
		return radarDisplayRange;
	}
	
	public static void setRadarDisplayRange(double range) {
		radarDisplayRange = range;
	}
	
	public static long getClientMountTime() {
		return mountTime;
	}
	
	public static void setClientMountTime(long time) {
		mountTime = time;
	}
	
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
		FREE_RELATIVE, // FIXME 2.2 give MouseMode FREE_RELATIVE functionality
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
