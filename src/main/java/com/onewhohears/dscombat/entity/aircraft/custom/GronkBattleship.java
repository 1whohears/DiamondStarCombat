package com.onewhohears.dscombat.entity.aircraft.custom;

import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjRadarModel.MastType;
import com.onewhohears.dscombat.data.aircraft.presets.BoatPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityBoat;
import com.onewhohears.dscombat.entity.aircraft.RotableHitbox;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GronkBattleship extends EntityBoat {
	
	public GronkBattleship(EntityType<? extends EntityBoat> entity, Level level) {
		super(entity, level, BoatPresets.DEFAULT_GRONK_BATTLESHIP.getId());
	}
	
	@Override
	public void addHitboxes() {
		hitboxes = new RotableHitbox[5];
		hitboxes[0] = new RotableHitbox(this, "plat0", 
				new Vector3f(14, 4.02f, 34), 
				new Vec3(0, 2, -0.5));
		hitboxes[1] = new RotableHitbox(this, "plat1", 
				new Vector3f(9, 1.02f, 30), 
				new Vec3(0, 4.5, -0.5));
		hitboxes[2] = new RotableHitbox(this, "plat1", 
				new Vector3f(6, 1.02f, 18), 
				new Vec3(0, 5.5, -0.5));
		hitboxes[3] = new RotableHitbox(this, "front", 
				new Vector3f(8, 2.02f, 8), 
				new Vec3(0, 3, 20.5));
		hitboxes[4] = new RotableHitbox(this, "back", 
				new Vector3f(8, 2.02f, 8), 
				new Vec3(0, 3, -21.5));
	}
	
	@Override
	public MastType getMastType() {
		return MastType.NORMAL;
	}

}
