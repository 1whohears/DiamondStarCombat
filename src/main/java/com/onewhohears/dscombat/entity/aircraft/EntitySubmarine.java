package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.AircraftTextures;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public class EntitySubmarine extends EntityBoat {
	
	public EntitySubmarine(EntityType<? extends EntitySubmarine> entity, Level level, 
			AircraftTextures textures, RegistryObject<SoundEvent> engineSound, RegistryObject<Item> item) {
		super(entity, level, textures, engineSound, item);
	}
	
	@Override
	public AircraftType getAircraftType() {
		return AircraftType.SUBMARINE;
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
	}
	
	@Override
	public void controlDirection(Quaternion q) {
		super.controlDirection(q);
	}
	
	@Override
	public void directionGround(Quaternion q) {
		super.directionGround(q);
	}
	
	@Override
	public void directionAir(Quaternion q) {
		super.directionAir(q);
	}
	
	@Override
	public void directionWater(Quaternion q) {
		if (isFreeLook()) flatten(q, 5f, 5f);
		else {
			addTorqueX(inputPitch * getAccelerationPitch(), true);
			addTorqueZ(inputRoll * getAccelerationRoll(), true);
		}
		addTorqueY(inputYaw * getAccelerationYaw(), true);
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	public void tickMovement(Quaternion q) {
		super.tickMovement(q);
	}
	
	@Override
	public void tickGround(Quaternion q) {
		super.tickGround(q);
	}
	
	@Override
	public void tickAir(Quaternion q) {
		super.tickAir(q);
	}
	
	@Override
	public void tickWater(Quaternion q) {
		Vec3 move = getDeltaMovement();
		if (inputSpecial) move = move.scale(0.75);
		else move = move.scale(0.925);
		if (isFreeLook()) {
			move = move.add(0, inputPitch * 0.04, 0);
			double max = 0.2;
			if (Math.abs(move.y) > 0.5) move.multiply(1, max/move.y, 1);
		}
		move = move.add(UtilAngles.getRollAxis(q).scale(getThrustMag()));
		setDeltaMovement(move);
	}
	
	@Override
	public void tickGroundWater(Quaternion q) {
		super.tickGroundWater(q);
	}
	
	@Override
	public double getThrustMag() {
		return super.getThrustMag();
	}

	@Override
	protected float getTorqueDragMag() {
		return 0.35f;
	}

	@Override
	public Vec3 getThrustForce(Quaternion q) {
		return Vec3.ZERO;
	}
	
	@Override
	public boolean isLandingGear() {
		return false;
    }
	
	@Override
	public boolean isCustomBoundingBox() {
    	return true;
    }
	
	@Override
	public void updateControls(float throttle, float pitch, float roll, float yaw,
			boolean mouseMode, boolean flare, boolean shoot, boolean select,
			boolean openMenu, boolean special, boolean radarMode, boolean bothRoll) {
		super.updateControls(throttle, pitch, roll, yaw, mouseMode, flare, shoot, 
				select, openMenu, special, radarMode, bothRoll);
		if (!isFreeLook()) {
			this.inputThrottle = throttle;
			this.inputPitch = pitch;
		}
	}
	
	@Override
	public float getStepHeight() {
		return 0.2f;
	}
	
	@Override
	public void waterDamage() {
	}

}
