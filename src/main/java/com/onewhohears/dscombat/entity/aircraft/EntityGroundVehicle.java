package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public class EntityGroundVehicle extends EntityAircraft {
	
	public final boolean isTank;
	
	protected final float wheelRate = 1.5f;
	private float wheelLRot, wheelLRotOld, wheelRRot, wheelRRotOld;
	
	public EntityGroundVehicle(EntityType<? extends EntityGroundVehicle> entity, Level level,
			AircraftPreset defaultPreset,
			RegistryObject<SoundEvent> engineSound, boolean isTank, float explodeSize) {
		super(entity, level, defaultPreset, engineSound,
				true, 8, 12, 8, explodeSize);
		this.isTank = isTank;
	}
	
	@Override
	public AircraftType getAircraftType() {
		return AircraftType.CAR;
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
		if (isTank && isOperational()) {
			flatten(q, 4f, 4f, true);
			addMomentY(inputYaw * getYawTorque(), true);
		} else super.directionGround(q);
	}
	
	@Override
	public void directionAir(Quaternion q) {
		super.directionAir(q);
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	public void tickGround(Quaternion q) {
		super.tickGround(q);
	}
	
	@Override
	public boolean isBreaking() {
		return inputSpecial;
	}
	
	@Override
	public void applyBreaks() {
		throttleToZero();
		super.applyBreaks();
	}
	
	@Override
	public double getThrustMag() {
		return super.getThrustMag();
	}
	
	@Override
	public Vec3 getThrustForce(Quaternion q) {
		return Vec3.ZERO;
	}
	
	@Override
	public void clientTick() {
		super.clientTick();
		wheelLRotOld = wheelLRot;
		wheelRRotOld = wheelRRot;
		wheelLRot += xzSpeed * wheelRate * xzSpeedDir;
		wheelRRot += xzSpeed * wheelRate * xzSpeedDir;
	}
	
	public float getWheelLeftRotation(float partialTicks) {
		return Mth.lerp(partialTicks, wheelLRotOld, wheelLRot);
	}
	
	public float getWheelRightRotation(float partialTicks) {
		return Mth.lerp(partialTicks, wheelRRotOld, wheelRRot);
	}
	
	@Override
	public boolean isLandingGear() {
		return true;
    }
	
	@Override
	public void updateControls(float throttle, float pitch, float roll, float yaw,
			boolean mouseMode, boolean flare, boolean shoot, int select,
			boolean openMenu, boolean special, boolean special2, boolean radarMode, 
			boolean bothRoll) {
		super.updateControls(throttle, pitch, roll, yaw, mouseMode, flare, shoot, 
				select, openMenu, special, special2, radarMode, bothRoll);
		this.inputThrottle = pitch;
		this.inputPitch = throttle;
	}
	
	@Override
	public float getStepHeight() {
		return 1.0f;
	}
	
	@Override
	public String getOpenMenuError() {
		return "dscombat.no_menu_moving";
	}

	@Override
	public boolean canBreak() {
		return true;
	}

	@Override
	public boolean canToggleLandingGear() {
		return false;
	}

}
