package com.onewhohears.dscombat.data.weapon.instance;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientWeaponAmmo;
import com.onewhohears.dscombat.data.aircraft.DSCPhyCons;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.weapon.WeaponShootParameters;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.UtilParse;
import com.onewhohears.dscombat.util.UtilSound;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public abstract class WeaponInstance<T extends WeaponStats> extends JsonPresetInstance<T> {
	
	private int currentAmmo;
	private int recoilTime;
	private Vec3 pos = Vec3.ZERO;
	private String failedLaunchReason;
	private String slotId = "";
	private boolean overrideGroundCheck = false;
	protected float changeLaunchPitch = 0;
	
	public WeaponInstance(T stats) {
		super(stats);
	}
	
	@Override
	public void readNBT(CompoundTag tag) {
		currentAmmo = tag.getInt("currentAmmo");
		slotId = tag.getString("slotId");
		pos = UtilParse.readVec3(tag, "pos");
	}
	
	@Override
	public CompoundTag writeNbt() {
		CompoundTag tag = super.writeNbt();
		tag.putString("weaponId", getStatsId());
		tag.putInt("currentAmmo", getCurrentAmmo());
		UtilParse.writeVec3(tag, pos, "pos");
		tag.putString("slotId", slotId);
		return tag;
	}
	
	@Nullable 
	public EntityWeapon<?> getEntity(Level level) {
		EntityType<?> type = getStats().getEntityType();
		Entity entity = type.create(level);
		if (entity instanceof EntityWeapon<?> w) {
			w.setWeaponData(getStats());
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
	
	public boolean shootFromVehicle(Level level, Entity owner, Vec3 direction, EntityVehicle vehicle, boolean consume) {
		overrideGroundCheck = false;
		EntityWeapon<?> w = getShootEntity(new WeaponShootParameters(level, owner, 
				vehicle.position().add(UtilAngles.rotateVector(getLaunchPos(), vehicle.getQ())), 
				direction, vehicle, false, false));
		if (w == null) return false;
		level.addFreshEntity(w);
		playShootSound(level, w.position());
		setLaunchSuccess(1, owner, consume);
		updateClientAmmo(vehicle);
		vehicle.lastShootTime = vehicle.tickCount;
		return true;
	}
	
	public boolean shootFromTurret(Level level, Entity owner, Vec3 direction, Vec3 pos, @Nullable EntityVehicle vehicle, boolean consume) {
		return shootFromTurret(level, owner, direction, pos, vehicle, consume, false);
	}
	
	public boolean shootFromTurret(Level level, Entity owner, Vec3 direction, Vec3 pos, @Nullable EntityVehicle vehicle, boolean consume, boolean ignoreRecoil) {
		overrideGroundCheck = true;
		EntityWeapon<?> w = getShootEntity(new WeaponShootParameters(level, owner, 
				pos, direction, vehicle, ignoreRecoil, true));
		if (w == null) return false;
		level.addFreshEntity(w);
		playShootSound(level, w.position());
		setLaunchSuccess(1, owner, consume);
		if (vehicle != null) vehicle.lastShootTime = vehicle.tickCount;
		return true;
	}
	
	public void playShootSound(Level level, Vec3 pos) {
		UtilSound.sendDelayedSound(getStats().getShootSound(), pos, 160, level.dimension(), 1, 1);
	}
	
	public void updateClientAmmo(EntityVehicle vehicle) {
		if (vehicle == null) return;
		if (vehicle.level.isClientSide) return;
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> vehicle), 
				new ToClientWeaponAmmo(vehicle.getId(), getStatsId(), slotId, getCurrentAmmo()));
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
	
	public int getCurrentAmmo() {
		return currentAmmo;
	}

	public void setCurrentAmmo(int currentAmmo) {
		if (currentAmmo < 0) currentAmmo = 0;
		if (currentAmmo > getStats().getMaxAmmo()) currentAmmo = getStats().getMaxAmmo();
		this.currentAmmo = currentAmmo;
	}
	
	/**
	 * @param num
	 * @return overflow
	 */
	public int addAmmo(int num) {
		int total = getCurrentAmmo()+num;
		int r = 0;
		if (total > getStats().getMaxAmmo()) {
			r = total - getStats().getMaxAmmo();
			total = getStats().getMaxAmmo();
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
		return entity.isAttackable() && !entity.isSpectator() && !entity.isRemoved() && !radar.isAlliedTo(entity) 
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
			Vec3 raycast = UtilEntity.raycastBlock(vehicle.level, prevPos, pos);
			if (raycast == null) continue;
			return raycast;
		}
		return pos;
	}
	
	protected Vec3 getStartMove(EntityVehicle vehicle) {
		return vehicle.getDeltaMovement();
	}
	
	protected Vec3 getAcc(EntityVehicle vehicle) {
		return new Vec3(0, -DSCPhyCons.GRAVITY, 0);
	}

}
