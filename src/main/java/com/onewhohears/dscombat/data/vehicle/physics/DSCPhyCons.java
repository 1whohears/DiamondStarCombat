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
	public static final double VERTICAL_UP_ACC_SCALE = 0.125;
	public static final double VERTICAL_DOWN_ACC_SCALE = 1;

	public static final double WATER_FLUID_DENSITY = 1000;

	public static final double INCREASED_DRAG_AREA_LANDING_GEAR = 100;
	public static final double INCREASED_DRAG_AREA_DESTROYED = 1000;

	public static final float DRAG_SCALE = 0.125f / (float)HORIZONTAL_SPEED_SCALE;

	public static final float ANGULAR_DRAG_C = 4E3f;

	public static final float MAX_SPEED_CHANGE_RATE = 0.01f;
	public static final float AOA_CHANGE_RATE = 0.5f;

	public static final float FLOAT = 8000f;

	public static final double PART_ROT_INERTIA_SCALE = 0.5;

	public static final float STATIC_FRICTION = 2.0f;
	public static final float KINETIC_FRICTION = 1.0f;

	public static final float DRIFT_FACTOR = 1E4f;
	
	public static final float COLLIDE_SPEED = 0.5f;
	public static final float COLLIDE_SPEED_GEAR = 1.5f;
	public static final float COLLIDE_DAMAGE_RATE = 300f;
	
	public static final double MAX_FALL_SPEED = 5;
	public static final double MAX_CLIMB_SPEED = 2.5;
	public static final double MAX_HELICOPTER_CLIMB_SPEED = 1.0;
	
	public static final float VEL_SOUND = (float) (17.0145 * HORIZONTAL_SPEED_SCALE); // m/t
	
	public static final int EJECT_SAFETY_COOLDOWN = 100;
	
	public static final double EXP_FORCE_FACTOR = 1E7;
	public static final double EXP_MOMENT_FACTOR = 1E8;

	public static final double MISSILE_BLEED_SCALE = 0.4;
	public static final double MISSILE_AIR_RES_SCALE = 0.01;
	public static final double MISSILE_GRAV_ACC_SCALE = 0.05;
}
