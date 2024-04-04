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
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.dscombat.util.UtilMCText;
import com.onewhohears.dscombat.util.UtilParse;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityTurret extends EntitySeat {
	
	public static final EntityDataAccessor<String> WEAPON_ID = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Integer> AMMO = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Float> RELROTX = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> RELROTY = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	
	public final double weaponOffset;
	public final ShootType shootType;
	public final RotBounds rotBounds;
	
	private WeaponData data;
	
	public float xRotRelO, yRotRelO;
	/**
	 * only used on server side
	 */
	private int newRiderCoolDown, lastShootTime;
	
	public EntityTurret(EntityType<?> type, Level level, Vec3 offset, 
			double weaponOffset, RotBounds rotBounds) {
		this(type, level, offset, weaponOffset, rotBounds, ShootType.NORMAL);
	}
	
	public EntityTurret(EntityType<?> type, Level level, Vec3 offset, 
			double weaponOffset, RotBounds rotBounds, ShootType shootType) {
		super(type, level, offset);
		this.weaponOffset = weaponOffset;
		this.shootType = shootType;
		this.rotBounds = rotBounds;
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(WEAPON_ID, "10mm");
		entityData.define(AMMO, 0);
		entityData.define(RELROTX, 0f);
		entityData.define(RELROTY, 0f);
	}
	
	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		super.onSyncedDataUpdated(key);
		if (!level.isClientSide) return;
		if (key.equals(WEAPON_ID)) {
			data = WeaponPresets.get().getPreset(getWeaponId());
		} else if (key.equals(AMMO)) {
			if (data != null) data.setCurrentAmmo(getAmmo());
		}
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		String wid = tag.getString("weaponId");
		data = UtilParse.parseWeaponFromCompound(tag.getCompound("weapondata"));
		if (wid.isEmpty() && data != null) wid = data.getId();
		setWeaponId(wid);
		setXRot(tag.getFloat("xRot"));
		setYRot(tag.getFloat("yRot"));
		setRelRotX(tag.getFloat("relrotx"));
		setRelRotY(tag.getFloat("relroty"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putString("weaponId", getWeaponId());
		if (data != null) tag.put("weapondata", data.writeNbt());
		tag.putFloat("xRot", getXRot());
		tag.putFloat("yRot", getYRot());
		tag.putFloat("relrotx", getRelRotX());
		tag.putFloat("relroty", getRelRotY());
	}
	
	public void init() {
		super.init();
		if (!level.isClientSide) {
			if (data == null) data = WeaponPresets.get().getPreset(getWeaponId());
			if (data != null) data.setCurrentAmmo(getAmmo());
		}
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
			
			// HOW 7 sometimes even in force loaded chunks the mob gunner stops ticking
			/*if (gunner instanceof Mob gunMob) {
				//gunMob.targetSelector.enableControlFlag(Goal.Flag.TARGET);
				System.out.println(gunMob.tickCount+" "+gunMob+" "+gunMob.getVehicle());
				//((ServerLevel)level).entityTickList;
			}*/
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
		float cos = Mth.cos(getRelRotY()*Mth.DEG_TO_RAD), sin = Mth.sin(getRelRotY()*Mth.DEG_TO_RAD);
		return UtilAngles.rotateVector(new Vec3(passengerOffset.x*cos+passengerOffset.z*sin, 
				offset, passengerOffset.z*cos+passengerOffset.x*sin), q)
				.subtract(0, passenger.getEyeHeight(), 0);
	}
	
	protected Goal shootGoal, targetGoal;
	
	protected void addTurretAI(Mob mob) {
		shootGoal = makeShootGoal(mob);
		mob.goalSelector.addGoal(0, shootGoal);
		if (mob.getType().is(ModTags.EntityTypes.TURRET_TARGET_PLAYERS)) {
			targetGoal = makeTargetPlayerGoal(mob);
			mob.targetSelector.addGoal(0, targetGoal);
		} else if (mob.getType().is(ModTags.EntityTypes.TURRET_TARGET_MONSTERS)) {
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
	
	@Override
	public boolean hasAIUsingTurret() {
		return targetGoal != null;
	}
	
	public boolean isBotUsingRadar() {
		if (!hasAIUsingTurret()) return false;
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
	
	public String getWeaponId() {
		return entityData.get(WEAPON_ID);
	}
	
	public void setWeaponId(String weapon) {
		entityData.set(WEAPON_ID, weapon);
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
					UtilMCText.translatable(data.getFailedLaunchReason()), 
					true);
		} else {
			lastShootTime = tickCount;
			setAmmo(data.getCurrentAmmo());
			updateDataAmmo();
		}
	}
	
	protected void specialShoot(Entity shooter, Vec3 pos, EntityVehicle parent, boolean consume) {
		if (shootType == ShootType.NORMAL) return;
		//System.out.println("SPECIAL SHOOT "+shootType);
		if (shootType == ShootType.MARK7) {
			float d = 1;
			float yRad = getYRot() * Mth.DEG_TO_RAD;
			Vec3 posL = pos.add(new Vec3(-d*Mth.cos(yRad), 0, -d*Mth.sign(yRad))); 
			Vec3 posR = pos.add(new Vec3(d*Mth.cos(yRad), 0, d*Mth.sign(yRad)));
			data.shootFromTurret(level, shooter, getLookAngle(), posL, parent, consume, true);
			data.shootFromTurret(level, shooter, getLookAngle(), posR, parent, consume, true);
		}
	}
	
	public int getLastShootTime() {
		return lastShootTime;
	}
	
	@Override
	public PartType getPartType() {
		return PartType.TURRENT;
	}
	
	public RotBounds getRotBounds() {
		return rotBounds;
	}
	
	public float getMinRotX() {
		return getRotBounds().minRotX;
	}
	
	public float getMaxRotX() {
		return getRotBounds().maxRotX;
	}
	
	public float getRotRate() {
		return getRotBounds().rotRate;
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
	
	@Override
	public boolean isTurret() {
		return true;
	}

}
