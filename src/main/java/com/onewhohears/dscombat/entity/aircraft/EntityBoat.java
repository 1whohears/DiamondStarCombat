package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.util.math.UtilAngles;

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
	
	private final float propellerRate = 3.141f;
	private float propellerRot = 0, propellerRotOld = 0;
	
	protected double waterLevel;
	
	public EntityBoat(EntityType<? extends EntityBoat> entity, Level level, 
			RegistryObject<SoundEvent> engineSound, RegistryObject<Item> item) {
		super(entity, level, engineSound, item, true, 6, 10, 4);
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
		super.controlDirection(q);
	}
	
	@Override
	public void directionGround(Quaternion q) {
		flatten(q, 4f, 4f, true);
		if (!isOperational()) return;
		addMomentY(inputYaw * getYawTorque() * 0.1f, true);
	}
	
	@Override
	public void directionAir(Quaternion q) {
		super.directionAir(q);
	}
	
	@Override
	public void directionWater(Quaternion q) {
		if (!isOperational()) return;
		flatten(q, 2f, 2f, true);
		addMomentY(inputYaw * getYawTorque(), true);
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	public void tickMovement(Quaternion q) {
		if (inputSpecial) throttleToZero();
		super.tickMovement(q);
	}
	
	@Override
	public void tickGround(Quaternion q) {
		addFrictionForce(kineticFric);
	}
	
	@Override
	public double getDriveAcc() {
		return 0;
	}
	
	@Override
	public void tickAir(Quaternion q) {
		super.tickAir(q);
	}
	
	@Override
	public void tickWater(Quaternion q) {
		Vec3 move = getDeltaMovement();
		move = move.multiply(0.950, 0.900, 0.950);
		if (checkInWater()) {
			if (willFloat()) {
				double bbh = getBbHeight();
				double goalY = waterLevel - bbh/2.5;
				double dy = goalY - getY();
				if (Math.abs(move.y) < 0.01 && Math.abs(dy) < 0.01) move = move.multiply(1, 0, 1);
				else move = move.add(0, 0.01 * Math.signum(dy), 0);
			} 
			else move = move.add(0, -0.01, 0);
		}
		move = move.add(UtilAngles.rotationToVector(getYRot(), 0, getThrustMag()));
		setDeltaMovement(move);
	}
	
	public boolean willFloat() {
		if (!isOperational()) return false;
		float w = getTotalMass();
		float fc = getBbWidth() * getBbWidth() * 0.05f;
		return fc > w;
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
	public double getMaxSpeedForMotion() {
		double max = getMaxSpeed();
		if (getCurrentThrottle() < 0) return max * 0.2;
    	return max;
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
	public boolean isLandingGear() {
		return false;
    }
	
	@Override
	public void updateControls(float throttle, float pitch, float roll, float yaw,
			boolean mouseMode, boolean flare, boolean shoot, boolean select,
			boolean openMenu, boolean special, boolean special2, boolean radarMode, 
			boolean bothRoll) {
		super.updateControls(throttle, pitch, roll, yaw, mouseMode, flare, shoot, 
				select, openMenu, special, special2, radarMode, bothRoll);
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
	
	@Override
	public void clientTick() {
		super.clientTick();
		float th = getCurrentThrottle();
		propellerRotOld = propellerRot;
		propellerRot += th * propellerRate;
	}
	
	public float getPropellerRotation(float partialTicks) {
		return Mth.lerp(partialTicks, propellerRotOld, propellerRot);
	}
	
	@Override
	public boolean canOpenMenu() {
		return xzSpeed < 0.1;
	}
	
	@Override
	public String getOpenMenuError() {
		return "dscombat.no_menu_moving";
	}

}
