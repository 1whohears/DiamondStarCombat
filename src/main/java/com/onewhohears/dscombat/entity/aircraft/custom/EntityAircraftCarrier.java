package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.data.aircraft.presets.BoatPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityBoat;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModSounds;

import net.minecraft.world.level.Level;

public class EntityAircraftCarrier extends EntityBoat {

	public EntityAircraftCarrier(Level level) {
		super(ModEntities.AIRCRAFT_CARRIER.get(), level, 
			BoatPresets.DEFAULT_AIRCRAFT_CARRIER, 
			ModSounds.BOAT_1, 10, 34);
	}
	
	@Override
	public void addHitboxes() {
		/*hitboxes = new RotableHitbox[1];
		hitboxes[0] = new RotableHitbox(this, "runway", 
				new Vec3(25, 1, 50), new Vec3(0, 5.25, 0), 1f);*/
	}

}
