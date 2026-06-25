package com.onewhohears.dscombat.client.input;

import com.onewhohears.dscombat.data.radar.RadarStats.RadarMode;

import net.minecraft.client.Minecraft;

public class DSCClientInputs {
	
	public static boolean disable3rdPersonVehicle = false;
	public static boolean planeArcadePhysicsMode = false;
	
	private static double mouseCenterX = 0;
	private static double mouseCenterY = 0;
	
	private static int hoverIndex = -1;
	private static double radarDisplayRange = 10000;
	
	public static final long MOUNT_SHOOT_COOLDOWN = 500;
	private static long mountTime;

	private static double LEAN_AMOUNT = 0;
	
	private static MouseMode CURRENT_MOUSE_MODE = MouseMode.FREE_RELATIVE;
	private static RadarMode PREFERRED_RADAR_MODE = RadarMode.ALL;
	private static TargetMode TARGET_MODE = TargetMode.LOOK;
	
	private static boolean GIMBAL_MODE = false;
	private static boolean AFTERBURNER = false;
	private static boolean TURN_ASSIST = true;
    private static boolean CAMERA_TRACK_TARGET = false;
	private static boolean ZOOM_IN = false;

	/** Accumulated raw mouse delta for MOUSE_DIRECT mode. Reset after reading each tick. */
	private static double mouseDeltaX = 0;
	private static double mouseDeltaY = 0;

	/** True while pilot holds look-around key in MOUSE_DIRECT mode. */
	private static boolean LOOK_AROUND = false;

	public static boolean isLookAround() {
		return LOOK_AROUND;
	}

	public static void setLookAround(boolean v) {
		LOOK_AROUND = v;
	}

	public static void accumulateMouseDelta(double dx, double dy) {
		mouseDeltaX += dx;
		mouseDeltaY += dy;
	}

	/** Returns accumulated X delta and resets it. */
	public static double getAndResetMouseDeltaX() {
		double v = mouseDeltaX;
		mouseDeltaX = 0;
		return v;
	}

	/** Returns accumulated Y delta and resets it. */
	public static double getAndResetMouseDeltaY() {
		double v = mouseDeltaY;
		mouseDeltaY = 0;
		return v;
	}

    public static float xRotPreTrack, yRotPreTrack;

    public static boolean isCameraTrackTarget() {
        return CAMERA_TRACK_TARGET;
    }

    public static boolean toggleCameraTrackTarget() {
        CAMERA_TRACK_TARGET = !CAMERA_TRACK_TARGET;
        return CAMERA_TRACK_TARGET;
    }

	public static RadarMode getPreferredRadarMode() {
		return PREFERRED_RADAR_MODE;
	}
	
	public static RadarMode cyclePreferredRadarMode() {
		PREFERRED_RADAR_MODE = PREFERRED_RADAR_MODE.cycle();
		return PREFERRED_RADAR_MODE;
	}
	
	public static void setPreferredRadarMode(RadarMode mode) {
		PREFERRED_RADAR_MODE = mode;
	}
	
	public static boolean isGimbalMode() {
		return GIMBAL_MODE;
	}
	
	public static void toggleGimbalMode() {
		GIMBAL_MODE = !GIMBAL_MODE;
	}

	public static void setGimbalMode(boolean mode) {
		GIMBAL_MODE = mode;
	}
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
		if (range < 10) range = 10;
		radarDisplayRange = range;
	}

	public static void cycleRadarDisplayRange() {
		double range = getRadarDisplayRange();
		if (range <= 250) range = 1000;
		else if (range <= 1000) range = 2000;
		else if (range <= 2000) range = 5000;
		else if (range <= 5000) range = 10000;
        else if (range <= 10000) range = 20000;
        else if (range <= 20000) range = 50000;
        else if (range <= 50000) range = 250;
		else range = 250;
		setRadarDisplayRange(range);
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
	 * @return {@link MouseMode#FREE_RELATIVE}, {@link MouseMode#FREE_GLOBAL}, {@link MouseMode#LOCKED_FORWARD}, or {@link MouseMode#MOUSE_DIRECT}
	 */
	public static MouseMode getMouseMode() {
		return CURRENT_MOUSE_MODE;
	}

	public static void setMouseMode(MouseMode mode) {
		CURRENT_MOUSE_MODE = mode;
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
	/**
	 * MOUSE DELTA EACH TICK DIRECTLY DRIVES PITCH/ROLL.
	 * CAMERA LOCKED TOWARDS VEHICLE FORWARD DIRECTION.
	 */
	public static boolean isMouseDirect() {
		return CURRENT_MOUSE_MODE.isMouseDirect();
	}

	public static boolean isTurnAssist() {
		return TURN_ASSIST;
	}

	public static void toggleTurnAssist() {
		TURN_ASSIST = !TURN_ASSIST;
	}

	public enum MouseMode {
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
		 * Mouse position relative to center acts as a virtual joystick.
		 */
		LOCKED_FORWARD,
		/**
		 * Camera is locked towards the vehicle's forward direction.
		 * Mouse delta each tick directly drives pitch/roll — like War Thunder mouse aim.
		 */
		MOUSE_DIRECT;
		public MouseMode cycle() {
			int index = ordinal();
			if (index == values().length-1) return values()[0];
			else return values()[++index];
		}
		public boolean isLockedForward() {
			return this == LOCKED_FORWARD || this == MOUSE_DIRECT;
		}
		public boolean isMouseDirect() {
			return this == MOUSE_DIRECT;
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

	public enum TargetMode {
		LOOK, COORDS, INDICATOR;
		public String getTranslatable() {
			return "targetmode.dscombat."+name().toLowerCase();
		}
	}

	public static TargetMode getTargetMode() {
		return TARGET_MODE;
	}

	public static void setTargetMode(TargetMode targetMode) {
		TARGET_MODE = targetMode;
	}

	public static void setLeanAmount(double leanAmount) {
		LEAN_AMOUNT = leanAmount;
	}

	public static double getLeanAmount() {
		return LEAN_AMOUNT;
	}

	public static void leanLeft() {
		if (getLeanAmount() < 0) leanNot();
		else setLeanAmount(-0.6);
	}

	public static void leanRight() {
		if (getLeanAmount() > 0) leanNot();
		else setLeanAmount(0.6);
	}

	public static void leanNot() {
		setLeanAmount(0);
	}

	public static boolean isAfterBurner() {
		return AFTERBURNER;
	}

	public static void toggleAfterBurner() {
		AFTERBURNER = !AFTERBURNER;
	}

	public static boolean isZoomIn() {
		return ZOOM_IN;
	}

	public static void setZoomIn(boolean zoom) {
		ZOOM_IN = zoom;
	}
}
