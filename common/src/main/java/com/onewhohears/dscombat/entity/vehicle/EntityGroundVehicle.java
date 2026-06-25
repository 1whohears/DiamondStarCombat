package com.onewhohears.dscombat.entity.vehicle;

import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.vehicle.VehicleType;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityGroundVehicle extends EntityVehicle {
	
	public EntityGroundVehicle(EntityType<? extends EntityGroundVehicle> entity, Level level, String defaultPreset) {
		super(entity, level, defaultPreset);
        maxUpStep = 1.0f;
	}
	
	@Override
	public VehicleType getVehicleType() {
		return VehicleType.CAR;
	}
	
	@Override
	public boolean isGroundBraking() {
		return inputs.special;
	}

	@Override
	public void applyGroundBreaks() {
		throttleToZero();
        inputs.setThrottleOverride(getCurrentThrottle(), this);
		super.applyGroundBreaks();
	}
	
	@Override
	public Vec3 getThrustForce(QuaternionF q) {
		return Vec3.ZERO;
	}
	
	@Override
	public boolean isLandingGear() {
		return true;
    }
	
	@Override
	public String getOpenMenuError() {
		return "error.dscombat.no_menu_moving";
	}

	@Override
	public boolean canToggleLandingGear() {
		return false;
	}

	@Override
	public double getMaxSpeedFactor() {
		return super.getMaxSpeedFactor() * Config.SERVER.carSpeedFactor.get();
	}

	@Override
	public double getMaxSpeedForMotion() {
		double max = super.getMaxSpeedForMotion();
		if (getCurrentThrottle() < 0) return max * getStats().reverseSpeedMultiplier;
		return max;
	}

	@Override
	public boolean isPitchControllable() {
		return false;
	}

	@Override
	public boolean isRollControllable() {
		return false;
	}

	@Override
	public boolean canTurnViaTorque() {
		return isOperational() && isOnGround() && getStats().asCar().isTank;
	}

	@Override
	public boolean dontUseDriveTurnPhysics() {
		return getStats().asCar().isTank;
	}

	@Override
	public void tick() {
		super.tick();
		// ОТКЛЮЧЕНО: накопление грязи на технике
		// if (!level().isClientSide()) tickDirt();
	}

	@Override
	public void clientTick() {
		super.clientTick();
		if (!isTank()) return;
		var paths = getStats().getCrawlerTrackPaths();
		if (paths.isEmpty()) return;
		var model = getAssets().getModel();
		if (!(model instanceof com.onewhohears.dscombat.client.model.obj.ObjVehicleModel)) return;
		@SuppressWarnings("unchecked")
		var typedModel = (com.onewhohears.dscombat.client.model.obj.ObjVehicleModel<EntityVehicle>) model;
		if (typedModel.hasTracks()) {
			float speed = getXZSpeed() * getXZSpeedDir() * 0.05f;
			typedModel.tickTrackAnimation(paths, speed);
		}
	}

	private static final float DIRT_GAIN = 0.01f;   // ~5 сек до максимума (20 тиков/сек * 5 сек = 100 тиков, 0.01 * 100 = 1.0)
	private static final float DIRT_RAIN_LOSS = 0.005f;
	private static final float DIRT_SYNC_THRESHOLD = 0.03f;
	private float lastSyncedDirt = 0f;

	private void tickDirt() {
		Vec3 motion = getDeltaMovement();
		double speed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
		float dirt = getDirtLevel();

		if (isOnGround() && speed > 0.01) {
			dirt = Math.min(1f, dirt + DIRT_GAIN);
		}
		if (level().isRaining() && level().canSeeSky(blockPosition())) {
			dirt = Math.max(0f, dirt - DIRT_RAIN_LOSS);
		}

		setDirtLevel(dirt);

		// синхронизируем только при значимом изменении
		if (Math.abs(dirt - lastSyncedDirt) >= DIRT_SYNC_THRESHOLD) {
			lastSyncedDirt = dirt;
		}
	}

}
