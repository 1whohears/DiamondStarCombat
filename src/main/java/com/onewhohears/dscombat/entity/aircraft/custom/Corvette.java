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

public class Corvette extends EntityBoat {
	
	public static final ImmutableVehicleData CORVETTE_DATA = Builder.create(BoatPresets.DEFAULT_CORVETTE)
			.setInteralEngineSound(ModSounds.BOAT_1)
			.setExternalEngineSound(ModSounds.BOAT_1)
			.setNegativeThrottle(true)
			.setRotationalInertia(6, 10, 4)
			.setCrashExplosionRadius(8)
			.setCameraDistance(13)
			.setSpinRate(3.141f)
			.build();
	
	public Corvette(EntityType<? extends EntityBoat> entity, Level level) {
		super(entity, level, BoatPresets.DEFAULT_CORVETTE);
	}
	
	@Override
	public void addHitboxes() {
		hitboxes = new RotableHitbox[1];
		hitboxes[0] = new RotableHitbox(this, "plat0", 
				new Vector3f(7, 3.02f, 16), 
				new Vec3(0, 1.5, -2));
	}
	
	@Override
	public MastType getMastType() {
		return MastType.THIN;
	}
	
}
