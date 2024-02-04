package com.onewhohears.dscombat.entity.aircraft.custom;

import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjRadarModel.MastType;
import com.onewhohears.dscombat.data.aircraft.presets.BoatPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityBoat;
import com.onewhohears.dscombat.entity.aircraft.RotableHitbox;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Cruiser extends EntityBoat {
	
	public Cruiser(EntityType<? extends EntityBoat> entity, Level level) {
		super(entity, level, BoatPresets.DEFAULT_CRUISER.getId());
	}
	
	@Override
	public void addHitboxes() {
		hitboxes = new RotableHitbox[2];
		hitboxes[0] = new RotableHitbox(this, "plat0", 
				new Vector3f(12, 4.02f, 35.6f), 
				new Vec3(0, 2, -2));
		hitboxes[1] = new RotableHitbox(this, "plat1", 
				new Vector3f(7, 1.02f, 21), 
				new Vec3(0, 4.5, 0.8));
	}
	
	@Override
	public MastType getMastType() {
		return MastType.LARGE;
	}
	
}
