package com.onewhohears.dscombat.data.weapon.instance;

import com.onewhohears.dscombat.DependencySafety;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientOnShoot;
import com.onewhohears.dscombat.common.network.toclient.ToClientWeaponAmmo;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.dscombat.data.weapon.WeaponShootParameters;
import com.onewhohears.dscombat.data.weapon.stats.TargetMode;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.util.UtilSound;
import com.onewhohears.onewholibs.common.core.SimulatedEntityManager;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.entity.SimulatedEntity;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.UtilParse;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class WeaponInstance<T extends WeaponStats> extends JsonPresetInstance<T> {
	
	private int currentAmmo;
	private int recoilTime;
	private int maxAmmo;
	private Vec3 pos = Vec3.ZERO;
	private String failedLaunchReason;
	private String slotId = "";
	private boolean overrideGroundCheck = false;
	protected float changeLaunchPitch = 0;
	@Nullable protected EntityWeapon<?> firedWeapon;
	
	public WeaponInstance(T stats) {
		super(stats);
	}
	
	@Override
	public void readNBT(CompoundTag tag) {
		super.readNBT(tag);
		currentAmmo = tag.getInt("currentAmmo");
		maxAmmo = tag.getInt("maxAmmo");
		slotId = tag.getString("slotId");
		pos = UtilParse.readVec3(tag, "pos");
	}
	
	@Override
	public CompoundTag writeNBT() {
		CompoundTag tag = super.writeNBT();
		tag.putString("weaponId", getStatsId());
		tag.putInt("currentAmmo", getCurrentAmmo());
		tag.putInt("maxAmmo", getMaxAmmo());
		UtilParse.writeVec3(tag, pos, "pos");
		tag.putString("slotId", slotId);
		return tag;
	}
	
	@Nullable 
	public EntityWeapon<?> getEntity(Level level) {
		EntityType<?> type = getStats().getEntityType();
		Entity entity = type.create(level);
		if (entity instanceof EntityWeapon<?> w) {
			w.setPreset(getStatsId());
			return w;
		}
		return null;
	}
	
	public EntityWeapon<?> getShootEntity(WeaponShootParameters params) {
		if (getStats().isNoWeapon()) {
			setLaunchFail(null);
			return null;
		}
		if (!params.ignoreRecoil && !checkRecoil()) {
			setLaunchFail(null);
			return null;
		}
		if (!checkAmmo(1, params.owner)) {
			setLaunchFail("error.dscombat.no_ammo");
			return null;
		}
		if (params.vehicle != null) {
			if (!overrideGroundCheck && !getStats().canShootOnGround() && params.vehicle.isOnGround()) {
				setLaunchFail("error.dscombat.cant_shoot_on_ground");
				return null;
			}
		}
		EntityWeapon<?> w = getEntity(params.level);
		if (w == null) return null;
		w.setOwner(params.owner);
		w.setPos(params.pos);
		setDirection(w, params.direction);
		return w;
	}
	
	public void setDirection(EntityWeapon<?> weapon, Vec3 direction) {
		float pitch = UtilAngles.getPitch(direction);
		float yaw = UtilAngles.getYaw(direction);
		weapon.setXRot(pitch-changeLaunchPitch);
		weapon.setYRot(yaw);
	}
	
	public boolean shootFromVehicle(Level level, Entity owner, Vec3 direction, EntityVehicle vehicle,
									boolean consume, TargetMode targetMode) {
		overrideGroundCheck = false;
		EntityWeapon<?> w = getShootEntity(new WeaponShootParameters(level, owner, 
				vehicle.position().add(UtilAngles.rotateVector(getLaunchPos(), vehicle.getQ())), 
				direction, vehicle, false, false, targetMode));
		if (w == null) return false;
		level.addFreshEntity(w);
        if (w instanceof SimulatedEntity sim) SimulatedEntityManager.get().startSimulatingEntity(sim);
		playShootSound(level, w.position());
		setLaunchSuccess(1, owner, consume);
		updateClientAmmo(vehicle);
		vehicle.lastShootTime = vehicle.tickCount;
		if (vehicle.getPartBySlotId(getSlotId()) instanceof EntityWeaponRack rack) {
			rack.lastShootTime = rack.tickCount;
			ToClientOnShoot.onShootWeaponRack(rack, owner);
		}
		firedWeapon = w;
        DependencySafety.onWeaponShoot(w);
		return true;
	}
	
	public boolean shootFromTurret(Level level, Entity owner, Vec3 direction, Vec3 pos,
								   @Nullable EntityVehicle vehicle, boolean consume, TargetMode targetMode) {
		return shootFromTurret(level, owner, direction, pos, vehicle, consume, false, targetMode);
	}
	
	public boolean shootFromTurret(Level level, Entity owner, Vec3 direction, Vec3 pos,
								   @Nullable EntityVehicle vehicle, boolean consume,
								   boolean ignoreRecoil, TargetMode targetMode) {
		overrideGroundCheck = true;
		EntityWeapon<?> w = getShootEntity(new WeaponShootParameters(level, owner, 
				pos, direction, vehicle, ignoreRecoil, true, targetMode));
		if (w == null) return false;
		level.addFreshEntity(w);
        if (w instanceof SimulatedEntity sim) SimulatedEntityManager.get().startSimulatingEntity(sim);
		playShootSound(level, w.position());
		setLaunchSuccess(1, owner, consume);
		if (vehicle != null && !ignoreRecoil) {
			vehicle.lastShootTime = vehicle.tickCount;
			if (vehicle.getPartBySlotId(getSlotId()) instanceof EntityTurret turret) {
				turret.setLastShootTick(turret.tickCount);
				ToClientOnShoot.onShootTurret(turret, owner);
			}
		}
		firedWeapon = w;
        DependencySafety.onWeaponShoot(w);
		return true;
	}

	@Nullable
	public EntityWeapon<?> getFiredWeapon() {
		return firedWeapon;
	}
	
	public void playShootSound(Level level, Vec3 pos) {
        if (level.isClientSide()) return;
		UtilSound.sendDelayedSound((ServerLevel) level, getStats().getShootSound(level.registryAccess()), pos, 160, 1, 1);
	}
	
	public void updateClientAmmo(EntityVehicle vehicle) {
		if (vehicle == null) return;
		if (vehicle.isClientSide()) return;
        PacketHandler.sendToTrackers(new ToClientWeaponAmmo(vehicle.getId(), getStatsId(), slotId, getCurrentAmmo()), vehicle);
	}
	
	public void tick(@Nullable EntityVehicle parent, boolean isSelected) {
		if (recoilTime > 1) --recoilTime;
	}
	
	/**
	 * called inside the shoot function
	 * @param ammoNum
	 * @return if this weapon can shoot
	 */
	public boolean checkAmmo(int ammoNum, Entity shooter) {
		if (shooter instanceof ServerPlayer p) {
			if (p.isCreative()) return true;
		}
		return getCurrentAmmo() >= ammoNum;
	}
	
	public boolean checkRecoil() {
		return recoilTime <= 1;
	}
	
	public Vec3 getLaunchPos() {
		return pos;
	}

	public void setLaunchPos(Vec3 pos) {
		this.pos = pos;
	}
	
	public int getMaxAmmo() {
		return maxAmmo;
	}
	
	public void setMaxAmmo(int max) {
		maxAmmo = max;
	}
	
	public int getCurrentAmmo() {
		return currentAmmo;
	}
	
	public void forceSetCurrentAmmo(int currentAmmo) {
		this.currentAmmo = currentAmmo;
	}

	public void setCurrentAmmo(int currentAmmo) {
		if (currentAmmo < 0) currentAmmo = 0;
		if (currentAmmo > getMaxAmmo()) currentAmmo = getMaxAmmo();
		this.currentAmmo = currentAmmo;
	}
	
	/**
	 * @param num
	 * @return overflow
	 */
	public int addAmmo(int num) {
		int total = getCurrentAmmo()+num;
		int r = 0;
		if (total > getMaxAmmo()) {
			r = total - getMaxAmmo();
			total = getMaxAmmo();
		} else if (total < 0) {
			r = total;
			total = 0;
		}
		setCurrentAmmo(total);
		return r;
	}
	
	public void setChangeLaunchPitch(float degrees) {
		changeLaunchPitch = degrees;
	}
	
	public boolean isFailedLaunch() {
		return failedLaunchReason != null;
	}
	
	@Nullable
	public String getFailedLaunchReason() {
		return failedLaunchReason;
	}
	
	public void setLaunchSuccess(int ammoNum, Entity shooter, boolean consume) {
		failedLaunchReason = null;
		if (consume) addAmmo(-ammoNum);
		recoilTime = getStats().getFireRate();
	}
	
	public void setLaunchFail(String failedLaunchReason) {
		this.failedLaunchReason = failedLaunchReason;
	}
	
	public String getSlotId() {
		return slotId;
	}
	
	public boolean isInternal() {
		return slotId == "";
	}
	
	public void setSlot(String slotId) {
		this.slotId = slotId;
	}
	
	public void setInternal() {
		this.slotId = "";
	}
	
	public boolean idMatch(String id, String slotId) {
		if (slotId == null) return false;
		if (id == null) return false;
		return getStatsId().equals(id) && getSlotId().equals(slotId);
	}
	
	public boolean couldRadarWeaponTargetEntity(Entity entity, Entity radar) {
		return entity.isAlive() && !entity.isSpectator() && !radar.isAlliedTo(entity)
				&& radar.distanceTo(entity) <= getStats().getMobTurretRange();
	}
	
	@Nullable
	public Vec3 estimateImpactPosition(EntityVehicle vehicle) {
		if (!getStats().isAimAssist()) return null;
		// TODO 5.8 aim assist against air targets on radar
		Vec3 startPos = vehicle.position().add(UtilAngles.rotateVector(getLaunchPos(), vehicle.getQBySide()));
		Vec3 startMove = getStartMove(vehicle);
		Vec3 acc = getAcc(vehicle);
		double distSqr = 0;
		Vec3 pos = startPos;
		Vec3 move = startMove;
		while (distSqr <= 40000) {
			distSqr += move.lengthSqr();
			Vec3 prevPos = pos;
			pos = pos.add(move);
			if (pos.y < -64) pos = new Vec3(pos.x, -64, pos.z);
			move = move.add(acc);
			Vec3 raycast = UtilEntity.raycastBlock(vehicle.getWorld(), prevPos, pos);
			if (raycast == null) continue;
			return raycast;
		}
		return pos;
	}
	
	protected Vec3 getStartMove(EntityVehicle vehicle) {
		return vehicle.getDeltaMovement();
	}
	
	protected Vec3 getAcc(EntityVehicle vehicle) {
		return new Vec3(0, -DSCPhyCons.GRAVITY*DSCPhyCons.ACC_TIME_SCALE, 0);
	}

	public TargetMode fixTargetMode(TargetMode currentTargetMode, TargetMode preferedPosTargetMode) {
		return currentTargetMode;
	}

	public TargetMode getDefaultTargetMode() {
		return TargetMode.LOOK;
	}
}
