package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.TurretData;
import com.onewhohears.dscombat.data.parts.TurretData.RotBounds;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityTurret extends EntitySeat {
	
	public static final EntityDataAccessor<Integer> AMMO = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Float> MINROTX = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> MAXROTX = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> ROTRATE = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	
	/**
	 * only used on server side
	 */
	private String weaponId;
	/**
	 * only used on server side
	 */
	private WeaponData data;
	
	public EntityTurret(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(AMMO, 0);
		entityData.define(MINROTX, 0f);
		entityData.define(MAXROTX, 0f);
		entityData.define(ROTRATE, 0f);
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		data = UtilParse.parseWeaponFromCompound(tag.getCompound("weapondata"));
		if (data == null) data = WeaponPresets.getNewById(weaponId);
		setXRot(tag.getFloat("xRot"));
		setYRot(tag.getFloat("yRot"));
		setRotBounds(new RotBounds(tag));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (data != null) tag.put("weapondata", data.write());
		tag.putFloat("xRot", getXRot());
		tag.putFloat("yRot", getYRot());
		getRotBounds().write(tag);
	}
	
	public void init() {
		super.init();
		if (data != null) data.setCurrentAmmo(getAmmo());
	}
	
	@Override
	public void tick() {
		super.tick();
		Player player = getPlayer();
		if (player == null) return;
		
	}
	
	@Override
	public boolean shouldRender() {
		return true;
	}
	
	@Override
	public boolean shouldRenderAtSqrDistance(double dist) {
		return dist < 25600;
	}
	
	public void setAmmo(int ammo) {
		entityData.set(AMMO, ammo);
	}
	
	public int getAmmo() {
		return entityData.get(AMMO);
	}
	
	public void setWeaponId(String wid) {
		weaponId = wid;
		data = WeaponPresets.getNewById(weaponId);
	}
	
	public String getWeaponId() {
		return weaponId;
	}
	
	public void shoot(Entity shooter) {
		if (level.isClientSide || data == null) return;
		data.shoot(level, shooter, getLookAngle(), position(), null);
		if (data.isFailedLaunch()) {
			if (shooter instanceof ServerPlayer player) {
				player.displayClientMessage(Component.translatable(data.getFailedLaunchReason()), true);
			}
		} else {
			setAmmo(data.getCurrentAmmo());
			if (getRootVehicle() instanceof EntityAircraft plane) {
				PartSlot slot = plane.partsManager.getSlot(getSlotId());
				if (slot != null && slot.filled() && slot.getPartData().getType() == PartType.TURRENT) { 
					TurretData td = (TurretData) slot.getPartData();
					td.setAmmo(data.getCurrentAmmo());
				}
			}
		}
	}
	
	@Override
    public void positionRider(Entity passenger) {
		/*if (passenger instanceof Player player) {
			player.setPos(position());
		} else if (passenger instanceof EntitySeatCamera camera) {
			if (!(getVehicle() instanceof EntityAircraft craft)) return;
			Vec3 pos = position();
			Quaternion q;
			if (level.isClientSide) {
				q = craft.getClientQ();
				q.mul(Vector3f.YP.rotationDegrees(getYRot()));
				q.mul(Vector3f.XP.rotationDegrees(getXRot()));
			} else q = craft.getQ();
			Vec3 seatPos = UtilAngles.rotateVector(new Vec3(0, 1.62, 0), q);
			camera.setPos(pos.add(seatPos));
		} else {
			super.positionRider(passenger);
		}*/
		super.positionRider(passenger);
	}
	
	@Override
	public PartType getPartType() {
		return PartType.TURRENT;
	}
	
	public void setRotBounds(RotBounds rb) {
		setMinRotX(rb.minRotX);
		setMaxRotX(rb.maxRotX);
		setRotRate(rb.rotRate);
	}
	
	public RotBounds getRotBounds() {
		return new RotBounds(getRotRate(), getMinRotX(), getMaxRotX());
	}
	
	public void setMinRotX(float rot) {
		entityData.set(MINROTX, rot);
	}
	
	public void setMaxRotX(float rot) {
		entityData.set(MAXROTX, rot);
	}
	
	public void setRotRate(float rot) {
		entityData.set(ROTRATE, rot);
	}
	
	public float getMinRotX() {
		return entityData.get(MINROTX);
	}
	
	public float getMaxRotX() {
		return entityData.get(MAXROTX);
	}
	
	public float getRotRate() {
		return entityData.get(ROTRATE);
	}

}
