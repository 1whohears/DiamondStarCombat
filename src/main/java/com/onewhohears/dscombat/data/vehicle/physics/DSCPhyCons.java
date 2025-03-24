package com.onewhohears.dscombat.data.vehicle.physics;

/**
 * There are 20 minecraft game ticks every second.
 * Thus, all physics must run at this discrete time interval.
 * <p></p>
 * All velocity constants are in Meters / Tick.
 * <p></p>
 * m/t = m/s / 20
 * <p></p>
 * All Acceleration Constants are in Meters / Tick^2
 * <p></p>
 * m/t^2 = m/s^2 / 400
 */
public class DSCPhyCons {

	/**
	 * All force units in stat files are in m/s^2
	 * <p></p>
	 * accelerations are applies 20 times a second so they must be converted is
	 * {@link com.onewhohears.dscombat.entity.vehicle.EntityVehicle#calcAcc()}
	 */
	public static final double ACC_TIME_SCALE = 1.0 / 20.0 / 20.0;

	/**
	 * real life acceleration due to gravity (G) is 9.81 m/s^2
	 * <p></p>
	 * 9.81 / 400 = 0.025 m/t^2
	 * <p></p>
	 * for some reason minecraft G is 0.08 m/t^2 = 32 m/s^2
	 */
	public static final double GRAVITY = 9.81;

	/**
	 * all stats
	 */
	public static final double HORIZONTAL_SPEED_SCALE = 0.125;

	public static final double WATER_FLUID_DENSITY = 1000;

	public static final double INCREASED_LANDING_GEAR_DRAG_AREA = 100;

	public static final float DRAG_SCALE = 0.125f / (float)HORIZONTAL_SPEED_SCALE;

	public static final float ANGULAR_DRAG_C = 1E3f;

	public static final float MAX_SPEED_CHANGE_RATE = 0.05f;
	public static final float AOA_CHANGE_RATE = 0.5f;

	public static final float FLOAT = 150f;
	
	public static final float STATIC_FRICTION = 30f;
	public static final float KINETIC_FRICTION = 20f;
	
	public static final float COLLIDE_SPEED = 0.5f;
	public static final float COLLIDE_SPEED_GEAR = 1.5f;
	public static final float COLLIDE_DAMAGE_RATE = 300f;
	
	public static final double MAX_FALL_SPEED = 2.5;
	public static final double MAX_CLIMB_SPEED = 1.0;
	
	public static final float VEL_SOUND = (float) (17.5 * HORIZONTAL_SPEED_SCALE); // m/t
	
	public static final int EJECT_SAFETY_COOLDOWN = 100;
	
	public static final double EXP_FORCE_FACTOR = 100;
	public static final double EXP_MOMENT_FACTOR = 100;
	
}
