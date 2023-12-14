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

public class Destroyer extends EntityBoat {
	
	public static final ImmutableVehicleData DESTROYER_DATA = Builder.create(BoatPresets.DEFAULT_DESTROYER)
			.setInteralEngineSound(ModSounds.BOAT_1)
			.setExternalEngineSound(ModSounds.BOAT_1)
			.setNegativeThrottle(true)
			.setRotationalInertia(6, 10, 4)
			.setCrashExplosionRadius(8)
			.setCameraDistance(22)
			.setSpinRate(3.141f)
			.build();
	
	public Destroyer(EntityType<? extends EntityBoat> entity, Level level) {
		super(entity, level, DESTROYER_DATA);
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