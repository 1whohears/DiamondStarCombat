package com.onewhohears.dscombat.entity.aircraft.custom;

import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjRadarModel.MastType;
import com.onewhohears.dscombat.data.aircraft.presets.BoatPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityBoat;
import com.onewhohears.dscombat.entity.aircraft.RotableHitbox;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Destroyer extends EntityBoat {
	
	public Destroyer(EntityType<? extends EntityBoat> entity, Level level) {
		super(entity, level, BoatPresets.DEFAULT_DESTROYER);
	}
	
	@Override
	public void addHitboxes() {
		hitboxes = new RotableHitbox[1];
		hitboxes[0] = new RotableHitbox(this, "plat0", 
			new Vector3f(10, 4.02f, 27.6f), 
			new Vec3(0, 2, 0));
	}
	
	@Override
	public MastType getMastType() {
		return MastType.NORMAL;
	}
	
}
