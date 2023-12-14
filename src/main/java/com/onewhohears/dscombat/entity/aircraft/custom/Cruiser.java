package com.onewhohears.dscombat.entity.aircraft.custom;

import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjRadarModel.MastType;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData.Builder;
import com.onewhohears.dscombat.data.aircraft.presets.BoatPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityBoat;
import com.onewhohears.dscombat.entity.aircraft.RotableHitbox;
import com.onewhohears.dscombat.init.ModSounds;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Cruiser extends EntityBoat {
	
	public static final ImmutableVehicleData CRUISER_DATA = Builder.create(BoatPresets.DEFAULT_NATHAN_BOAT)
			.setInteralEngineSound(ModSounds.BOAT_1)
			.setExternalEngineSound(ModSounds.BOAT_1)
			.setNegativeThrottle(true)
			.setRotationalInertia(6, 10, 4)
			.setCrashExplosionRadius(8)
			.setCameraDistance(26)
			.setSpinRate(3.141f)
			.build();
	
	public Cruiser(EntityType<? extends EntityBoat> entity, Level level) {
		super(entity, level, CRUISER_DATA);
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
