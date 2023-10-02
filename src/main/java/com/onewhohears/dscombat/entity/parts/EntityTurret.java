package com.onewhohears.dscombat.entity.parts;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.TurretData;
import com.onewhohears.dscombat.data.parts.TurretData.RotBounds;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.UtilParse;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class EntityTurret extends EntitySeat {
	
	public static final EntityDataAccessor<Integer> AMMO = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Float> RELROTX = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> RELROTY = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	
	public final double weaponOffset;
	public final ShootType shootType;
	public final RotBounds rotBounds;
	
	public float xRotRelO, yRotRelO;
	
	/**
	 * only used on server side
	 */
	private String weaponId;
	/**
	 * only used on server side
	 */
	private WeaponData data;
	/**
	 * only used on server side
	 */
	private int newRiderCoolDown;
	
	public EntityTurret(EntityAircraft parent, String modelId, EntityDimensions size, String slotId, Vec3 pos, float z_rot, 
			Vec3 offset, double weaponOffset, RotBounds rotBounds) {
		super(parent, modelId, size, slotId, pos, z_rot, offset);
		this.weaponOffset = weaponOffset;
		this.shootType = ShootType.NORMAL;
		this.rotBounds = rotBounds;
	}
	
	public EntityTurret(EntityAircraft parent, String modelId, EntityDimensions size, String slotId, Vec3 pos, float z_rot, 
			Vec3 offset, double weaponOffset, RotBounds rotBounds, ShootType shootType) {
		super(parent, modelId, size, slotId, pos, z_rot, offset);
		this.weaponOffset = weaponOffset;
		this.shootType = shootType;
		this.rotBounds = rotBounds;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(AMMO, 0);
		entityData.define(RELROTX, 0f);
		entityData.define(RELROTY, 0f);
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		data = UtilParse.parseWeaponFromCompound(tag.getCompound("weapondata"));
		if (data == null) data = WeaponPresets.get().getPreset(weaponId);
		setXRot(tag.getFloat("xRot"));
		setYRot(tag.getFloat("yRot"));
		setRelRotX(tag.getFloat("relrotx"));
		setRelRotY(tag.getFloat("relroty"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (data != null) tag.put("weapondata", data.writeNbt());
		tag.putFloat("xRot", getXRot());
		tag.putFloat("yRot", getYRot());
		tag.putFloat("relrotx", getRelRotX());
		tag.putFloat("relroty", getRelRotY());
	}
	
	public void init() {
		super.init();
		if (data != null) data.setCurrentAmmo(getAmmo());
	}
	
	@Override
	public void tick() {
		super.tick();
		xRotRelO = getRelRotX();
		yRotRelO = getRelRotY();
		Entity gunner = getPassenger();
		if (gunner == null) return;
		Quaternion ra = Quaternion.ONE;
		if (!level.isClientSide) {
			if (newRiderCoolDown > 0) --newRiderCoolDown;
			float rely = yRotRelO, relx = xRotRelO;
			float rotrate = rotBounds.rotRate, minrotx = rotBounds.minRotX, maxrotx = rotBounds.maxRotX;
			ra = getParent().getQ();
			
			if (data != null) data.tick(getParent(), true);
			float[] relangles = UtilAngles.globalToRelativeDegrees(gunner.getXRot(), gunner.getYHeadRot(), ra);
			
			float rg1 = relangles[1] + 360, rg2 = relangles[1] - 360;
			float d1 = Math.abs(rg1-rely), d2 = Math.abs(rg2-rely), d3 =  Math.abs(relangles[1]-rely);
			if (d1 < d2 && d1 < d3) relangles[1] += 360;
			else if (d2 < d1 && d2 < d3) relangles[1] -= 360;
			
			if (relangles[0] > maxrotx) relangles[0] = maxrotx;
			else if (relangles[0] < minrotx) relangles[0] = minrotx;
			
			float rotdiffx = relangles[0]-relx, rotdiffy = relangles[1]-rely;
			
			float dx, dy;
			
			if (Math.abs(rotdiffx) < rotrate) dx = rotdiffx;
			else dx = rotrate*Math.signum(rotdiffx);
			
			if (Math.abs(rotdiffy) < rotrate) dy = rotdiffy;
			else dy = rotrate*Math.signum(rotdiffy);
			
			setRelRotX(Mth.wrapDegrees(relx+dx));
			setRelRotY(Mth.wrapDegrees(rely+dy));
		}
		float[] global = UtilAngles.relativeToGlobalDegrees(getRelRotX(), getRelRotY(), ra);
		setXRot(global[0]);
		setYRot(global[1]);
	}
	
	@Override
	public Vec3 positionPassenger() {
		Quaternion q;
		if (level.isClientSide) q = getParent().getClientQ();
		else q = getParent().getQ();
		double offset = getPassengersRidingOffset() + getPassenger().getMyRidingOffset() + getPassenger().getEyeHeight();
		return UtilAngles.rotateVector(new Vec3(
					passengerOffset.x*Mth.cos(getRelRotY()*Mth.DEG_TO_RAD)+passengerOffset.z*Mth.sin(getRelRotY()*Mth.DEG_TO_RAD), 
					offset, 
					passengerOffset.z*Mth.cos(getRelRotY()*Mth.DEG_TO_RAD)+passengerOffset.x*Mth.sin(getRelRotY()*Mth.DEG_TO_RAD)
				), q).subtract(0, getPassenger().getEyeHeight(), 0).add(position());
	}
	
	@Override
	public boolean setPassenger(Entity passenger) {
		if (!super.setPassenger(passenger)) return false;
		newRiderCoolDown = 10;
		if (getPassenger() instanceof Mob m) addTurretAI(m);
		return true;
	}
	
	@Override
	public void removePassenger() {
		if (getPassenger() instanceof Mob m) removeTurretAI(m);
		super.removePassenger();
	}
	
	public void addTurretAI(Mob entity) {
		// TODO 6.1 add turret AI goals for mobs
		//entity.goalSelector.addGoal(0, null);
		//entity.targetSelector.addGoal(0, null);
	}
	
	public void removeTurretAI(Mob entity) {
		
	}
	
	@Override
	public boolean shouldRender() {
		return true;
	}
	
	@Override
	public boolean shouldRenderAtSqrDistance(double dist) {
		return dist < 65536;
	}
	
	public void setAmmo(int ammo) {
		entityData.set(AMMO, ammo);
	}
	
	public void updateDataAmmo() {
		PartSlot slot = getParent().partsManager.getSlot(getSlotId());
		if (slot != null && slot.filled() && slot.getPartData().getType() == PartType.TURRENT) { 
			TurretData td = (TurretData) slot.getPartData();
			td.setAmmo(getAmmo());
		}
	}
	
	public int getAmmo() {
		return entityData.get(AMMO);
	}
	
	public void setWeaponId(String wid) {
		weaponId = wid;
		data = WeaponPresets.get().getPreset(weaponId);
	}
	
	public String getWeaponId() {
		return weaponId;
	}
	
	@Nullable
	public WeaponData getWeaponData() {
		return data;
	}
	
	public void shoot(Entity shooter) {
		if (level.isClientSide || data == null || newRiderCoolDown > 0) return;
		boolean consume = true;
		Vec3 pos = position();
		if (!getParent().isOperational()) return;
		pos = pos.add(UtilAngles.rotateVector(new Vec3(0, weaponOffset, 0), getParent().getQ()));
		if (getParent().isNoConsume()) consume = false;
		Player p = null;
		if (shooter instanceof ServerPlayer player) {
			if (player.isCreative()) consume = false;
			p = player;
		}
		boolean couldShoot = data.checkRecoil();
		data.shootFromTurret(level, shooter, getLookAngle(), pos, getParent(), consume);
		if (couldShoot) specialShoot(shooter, pos, getParent(), consume);
		if (data.isFailedLaunch()) {
			if (p != null) p.displayClientMessage(
					Component.translatable(data.getFailedLaunchReason()), 
					true);
		} else {
			setAmmo(data.getCurrentAmmo());
			updateDataAmmo();
		}
	}
	
	protected void specialShoot(Entity shooter, Vec3 pos, EntityAircraft parent, boolean consume) {
		if (shootType == ShootType.NORMAL) return;
		if (shootType == ShootType.MARK7) {
			float d = 1;
			float yRad = getYRot() * Mth.DEG_TO_RAD;
			Vec3 posL = pos.add(new Vec3(-d*Mth.cos(yRad), 0, -d*Mth.sign(yRad))); 
			Vec3 posR = pos.add(new Vec3(d*Mth.cos(yRad), 0, d*Mth.sign(yRad)));
			data.shootFromTurret(level, shooter, getLookAngle(), posL, parent, consume, true);
			data.shootFromTurret(level, shooter, getLookAngle(), posR, parent, consume, true);
		}
	}
	
	@Override
	public PartType getPartType() {
		return PartType.TURRENT;
	}
	
	public RotBounds getRotBounds() {
		return rotBounds;
	}
	
	public float getRelRotX() {
		return entityData.get(RELROTX);
	}
	
	public float getRelRotY() {
		return entityData.get(RELROTY);
	}
	
	public void setRelRotX(float degrees) {
		entityData.set(RELROTX, degrees);
	}
	
	public void setRelRotY(float degrees) {
		entityData.set(RELROTY, degrees);
	}
	
	public static enum ShootType {
		NORMAL,
		MARK7
	}
	
	@Override
	public boolean canGetHurt() {
		return true;
	}
	
	@Override
    public boolean hurt(DamageSource source, float amount) {
		addHealth(-amount);
		if (getHealth() <= 0) kill();
		return true;
	}

}
