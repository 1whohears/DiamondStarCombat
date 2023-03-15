package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.AircraftTextures;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public class EntityBoat extends EntityAircraft {

	public EntityBoat(EntityType<? extends EntityBoat> entity, Level level, AircraftTextures textures,
			RegistryObject<SoundEvent> engineSound, RegistryObject<Item> item, boolean negativeThrottle) {
		super(entity, level, textures, engineSound, item, negativeThrottle);
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
	}
	
	@Override
	public void directionAir(Quaternion q) {
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
		
	}
	
	@Override
	public void tickAir(Quaternion q) {
		
	}
	
	@Override
	public void tickWater(Quaternion q) {
		// TODO boat float
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
    protected AABB makeBoundingBox() {
		return getDimensions(getPose()).makeBoundingBox(position());
    }
	
	@Override
	public float getMaxDeltaYaw() {
    	return entityData.get(MAX_YAW);
    }
	
	@Override
	public void updateControls(float throttle, float pitch, float roll, float yaw,
			boolean mouseMode, boolean flare, boolean shoot, boolean select,
			boolean openMenu, boolean special) {
		super.updateControls(throttle, pitch, roll, yaw, mouseMode, flare, shoot, 
				select, openMenu, special);
	}
	
	@Override
	public float getStepHeight() {
		return 0.2f;
	}

}
