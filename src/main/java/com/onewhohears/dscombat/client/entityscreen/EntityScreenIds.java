package com.onewhohears.dscombat.client.entityscreen;

import com.onewhohears.dscombat.data.aircraft.EntityScreenData;

import net.minecraft.world.phys.Vec3;

/**
 * must exist for safety between client and server side
 * @author 1whoh
 */
public class EntityScreenIds {
	
	public static final int AIR_RADAR_SCREEN = 0;
	public static final int FUEL_SCREEN = 1;
	public static final int HUD_SCREEN = 2;
	public static final int RWR_SCREEN = 3;
	public static final int GROUND_RADAR_SCREEN = 4;
	public static final int HEADING_SCREEN = 5;
	public static final int AIR_SPEED_SCREEN = 6;
	
	// THIS HAS TO BE HERE OTHERWISE CLIENT SIDE ONLY CLASS GETS CALLED CRASHING SERVER
	/**
	 * @param xPos a <code>double</code> corresponding to the x offset of the screen.
	 *             At present this number is largely arbitrary and needs to be found
	 *             by trial & error. For aircraft whose pilot seat lines up with the
	 *             origin of the aircraft, this value will be close to 0.
	 */
	public static EntityScreenData getDefaultHUDData(double xPos, double seatY, double seatZ) {
	    return new EntityScreenData(
	        EntityScreenIds.HUD_SCREEN,
	            new Vec3(xPos, seatY + 1.27, seatZ + 0.13),
	            0.1f, 0.1f,
	            0, 0, 0
	    );
	}
	
}
