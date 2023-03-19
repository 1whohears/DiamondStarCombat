package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.AircraftTextures;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public class EntityBoat extends EntityAircraft {
	
	protected double waterLevel;
	
	public EntityBoat(EntityType<? extends EntityBoat> entity, Level level, 
			AircraftTextures textures, RegistryObject<SoundEvent> engineSound, RegistryObject<Item> item) {
		super(entity, level, textures, engineSound, item, true);
	}
	
	@Override
	public AircraftType getAircraftType() {
		return AircraftType.BOAT;
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
		flatten(q, 5f, 5f);
		addTorqueY(inputYaw * getAccelerationYaw(), true);
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
		if (inputSpecial) throttleToZero();
		super.tickMovement(q);
		//System.out.println("vy = "+getDeltaMovement().y);
	}
	
	@Override
	public void tickGround(Quaternion q) {
		Vec3 motion = getDeltaMovement();
		if (motion.y < 0) motion = motion.multiply(1, 0, 1);
		motion = motion.multiply(0.5, 1, 0.5);
		setDeltaMovement(motion);
	}
	
	@Override
	public void tickAir(Quaternion q) {
		super.tickAir(q);
	}
	
	@Override
	public void tickWater(Quaternion q) {
		Vec3 motion = getDeltaMovement();
		motion = motion.multiply(0.990, 0.900, 0.990);
		if (checkInWater()) {
			double wdiff = waterLevel - getY();
			double f = wdiff * getBbWidth() * 0.20;
			motion = motion.add(0, f-getTotalWeight(), 0);
			//System.out.println("f  = "+f);
			//System.out.println("my = "+motion.y);
		}
		float f = (float)getThrustMag();
		motion = motion.add(
				(double)(Mth.sin(-getYRot()*Mth.DEG_TO_RAD)*f), 
				0.0D, 
				(double)(Mth.cos(getYRot()*Mth.DEG_TO_RAD)*f));
		setDeltaMovement(motion);
	}
	
	@Override
	public void tickGroundWater(Quaternion q) {
		tickWater(q);
		Vec3 motion = getDeltaMovement();
		if (motion.y < 0) motion = motion.multiply(1, 0, 1);
		motion = motion.multiply(0.5, 1, 0.5);
		setDeltaMovement(motion);
	}
	
	protected boolean checkInWater() {
		AABB aabb = getBoundingBox();
		int i = Mth.floor(aabb.minX);
		int j = Mth.ceil(aabb.maxX);
		int k = Mth.floor(aabb.minY);
		int l = Mth.ceil(aabb.maxY);
		int i1 = Mth.floor(aabb.minZ);
		int j1 = Mth.ceil(aabb.maxZ);
		boolean flag = false;
		this.waterLevel = -Double.MAX_VALUE;
		BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();
		for(int k1=i;k1<j;++k1){for(int l1=k;l1<l;++l1){for(int i2=i1;i2<j1;++i2){
			mbp.set(k1, l1, i2);
			FluidState fluidstate = level.getFluidState(mbp);
			if (fluidstate.is(FluidTags.WATER)) {
				float f = (float)l1 + fluidstate.getHeight(level, mbp);
				waterLevel = Math.max((double)f, waterLevel);
				flag = aabb.minY < (double)f;
			}
		}}}
		return flag;
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
	public float getMaxSpeed() {
		if (getCurrentThrottle() < 0) return super.getMaxSpeed() * 0.05f;
    	return entityData.get(MAX_SPEED);
    }
	
	@Override
	public void updateControls(float throttle, float pitch, float roll, float yaw,
			boolean mouseMode, boolean flare, boolean shoot, boolean select,
			boolean openMenu, boolean special) {
		super.updateControls(throttle, pitch, roll, yaw, mouseMode, flare, shoot, 
				select, openMenu, special);
		this.inputThrottle = pitch;
		this.inputPitch = throttle;
	}
	
	@Override
	public float getStepHeight() {
		return 0.2f;
	}
	
	@Override
	public void waterDamage() {
		if (waterLevel > getBoundingBox().maxY) super.waterDamage();
	}

}
