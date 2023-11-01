package com.onewhohears.dscombat.entity.parts;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.TurretData;
import com.onewhohears.dscombat.data.parts.TurretData.RotBounds;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.ai.goal.TurretShootGoal;
import com.onewhohears.dscombat.entity.ai.goal.TurretTargetGoal;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityTurret extends EntitySeat {
	
	public static final EntityDataAccessor<Integer> AMMO = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Float> MINROTX = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> MAXROTX = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> ROTRATE = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> RELROTX = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> RELROTY = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	
	public final double weaponOffset;
	public final ShootType shootType;
	
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
	
	public EntityTurret(EntityType<?> type, Level level, Vec3 offset, double weaponOffset) {
		super(type, level, offset);
		this.weaponOffset = weaponOffset;
		this.shootType = ShootType.NORMAL;
	}
	
	public EntityTurret(EntityType<?> type, Level level, Vec3 offset, double weaponOffset, ShootType shootType) {
		super(type, level, offset);
		this.weaponOffset = weaponOffset;
		this.shootType = shootType;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(AMMO, 0);
		entityData.define(MINROTX, 0f);
		entityData.define(MAXROTX, 0f);
		entityData.define(ROTRATE, 0f);
		entityData.define(RELROTX, 0f);
		entityData.define(RELROTY, 0f);
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		data = UtilParse.parseWeaponFromCompound(tag.getCompound("weapondata"));
		if (data == null) data = WeaponPresets.get().getPreset(weaponId);
		setRotBounds(new RotBounds(tag));
		setXRot(tag.getFloat("xRot"));
		setYRot(tag.getFloat("yRot"));
		setRelRotX(tag.getFloat("relrotx"));
		setRelRotY(tag.getFloat("relroty"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (data != null) tag.put("weapondata", data.writeNbt());
		getRotBounds().write(tag);
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
		LivingEntity gunner = getPassenger();
		if (gunner == null) return;
		Quaternion ra = Quaternion.ONE;
		if (!level.isClientSide) {
			if (newRiderCoolDown > 0) --newRiderCoolDown;
			float rely = yRotRelO, relx = xRotRelO;
			float rotrate = getRotRate(), minrotx = getMinRotX(), maxrotx = getMaxRotX();
			EntityVehicle ea = null;
			if (getVehicle() instanceof EntityVehicle plane) {
				ra = plane.getQ();
				ea = plane;
			}  
			if (data != null) data.tick(ea, true);
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
	protected Vec3 getPassengerRelPos(Entity passenger, EntityVehicle craft) {
		Quaternion q;
		if (level.isClientSide) q = craft.getClientQ();
		else q = craft.getQ();
		double offset = getPassengersRidingOffset() + passenger.getMyRidingOffset() + passenger.getEyeHeight();
		return UtilAngles.rotateVector(new Vec3(
					passengerOffset.x*Mth.cos(getRelRotY()*Mth.DEG_TO_RAD)+passengerOffset.z*Mth.sin(getRelRotY()*Mth.DEG_TO_RAD), 
					offset, 
					passengerOffset.z*Mth.cos(getRelRotY()*Mth.DEG_TO_RAD)+passengerOffset.x*Mth.sin(getRelRotY()*Mth.DEG_TO_RAD)
				), q).subtract(0, passenger.getEyeHeight(), 0);
	}
	
	protected Goal shootGoal, targetGoal;
	
	protected void addTurretAI(Mob mob) {
		shootGoal = makeShootGoal(mob);
		mob.goalSelector.addGoal(0, shootGoal);
		if (mob instanceof Enemy) {
			targetGoal = makeTargetPlayerGoal(mob);
			mob.targetSelector.addGoal(0, targetGoal);
		} else if (mob instanceof AbstractGolem) {
			targetGoal = makeTargetEnemyGoal(mob);
			mob.targetSelector.addGoal(0, targetGoal);
		}
	}
	
	protected void removeTurretAI(Mob mob) {
		if (shootGoal != null) {
			mob.goalSelector.removeGoal(shootGoal);
			shootGoal = null;
		}
		if (targetGoal != null) {
			mob.targetSelector.removeGoal(targetGoal);
			targetGoal = null;
		}
	}
	
	protected Goal makeShootGoal(Mob mob) {
		return new TurretShootGoal(mob, this);
	}
	
	protected Goal makeTargetPlayerGoal(Mob mob) {
		return TurretTargetGoal.targetPlayers(mob, this);
	}
	
	protected Goal makeTargetEnemyGoal(Mob mob) {
		return TurretTargetGoal.targetEnemy(mob, this);
	}
	
	public boolean isAIUsingRadar() {
		if (targetGoal == null) return false;
		WeaponData wd = getWeaponData();
		if (wd == null) return false;
		return wd.requiresRadar();
	}
	
	public double getAIHorizontalRange() {
		WeaponData wd = getWeaponData();
		if (wd == null) return 300;
		return wd.getMobTurretRange();
	}
	
	public double getAIVerticalRange() {
		return level.getGameRules().getInt(DSCGameRules.MOB_TURRET_VERTICAL_RANGE);
	}
	
	@Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        newRiderCoolDown = 10;
        if (passenger instanceof Mob m) addTurretAI(m);
	}
	
	@Override
	protected void removePassenger(Entity passenger) {
		super.removePassenger(passenger);
		if (passenger instanceof Mob m) removeTurretAI(m);
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
		if (getRootVehicle() instanceof EntityVehicle plane) {
			PartSlot slot = plane.partsManager.getSlot(getSlotId());
			if (slot != null && slot.filled() && slot.getPartData().getType() == PartType.TURRENT) { 
				TurretData td = (TurretData) slot.getPartData();
				td.setAmmo(getAmmo());
			}
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
		EntityVehicle parent = null;
		if (getVehicle() instanceof EntityVehicle craft) {
			if (!craft.isOperational()) return;
			pos = pos.add(UtilAngles.rotateVector(new Vec3(0, weaponOffset, 0), craft.getQ()));
			if (craft.isNoConsume()) consume = false;
			parent = craft;
		}
		Player p = null;
		if (shooter instanceof ServerPlayer player) {
			if (player.isCreative()) consume = false;
			p = player;
		}
		boolean consumeAmmo = parent.level.getGameRules().getBoolean(DSCGameRules.CONSUME_AMMO);
		boolean couldShoot = data.checkRecoil();
		data.shootFromTurret(level, shooter, getLookAngle(), pos, parent, consume && consumeAmmo);
		if (couldShoot) specialShoot(shooter, pos, parent, consume && consumeAmmo);
		if (data.isFailedLaunch()) {
			if (p != null) p.displayClientMessage(
					Component.translatable(data.getFailedLaunchReason()), 
					true);
		} else {
			setAmmo(data.getCurrentAmmo());
			updateDataAmmo();
		}
	}
	
	protected void specialShoot(Entity shooter, Vec3 pos, EntityVehicle parent, boolean consume) {
		if (shootType == ShootType.NORMAL) return;
		System.out.println("SPECIAL SHOOT "+shootType);
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
	
	public void setRotBounds(RotBounds rb) {
		setMinRotX(rb.minRotX);
		setMaxRotX(rb.maxRotX);
		setRotRate(rb.rotRate);
	}
	
	public RotBounds getRotBounds() {
		return RotBounds.create(getRotRate(), getMinRotX(), getMaxRotX());
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
