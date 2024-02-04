package com.onewhohears.dscombat.entity.aircraft.custom;

import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjRadarModel.MastType;
import com.onewhohears.dscombat.data.aircraft.presets.BoatPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityBoat;
import com.onewhohears.dscombat.entity.aircraft.RotableHitbox;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AircraftCarrier extends EntityBoat {
	
	public AircraftCarrier(EntityType<? extends EntityBoat> entity, Level level) {
		super(entity, level, BoatPresets.DEFAULT_AIRCRAFT_CARRIER.getId());
	}
	
	@Override
	public void addHitboxes() {
		hitboxes = new RotableHitbox[2];
		hitboxes[0] = new RotableHitbox(this, "runway", 
				new Vector3f(16, 6.06f, 50), 
				new Vec3(0, 3, 0));
		hitboxes[1] = new RotableHitbox(this, "side_plat", 
				new Vector3f(25, 3.06f, 24), 
				new Vec3(0, 4.5, 0));
	}
	
	@Override
	public MastType getMastType() {
		return MastType.LARGE;
	}
	
}
