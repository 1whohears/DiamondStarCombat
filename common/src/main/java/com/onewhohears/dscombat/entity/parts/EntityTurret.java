package com.onewhohears.dscombat.entity.parts;

import org.jetbrains.annotations.Nullable;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.TurretInstance;
import com.onewhohears.dscombat.data.parts.stats.TurretStats;
import com.onewhohears.dscombat.data.parts.stats.TurretStats.RotBounds;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.ai.goal.TurretShootGoal;
import com.onewhohears.dscombat.entity.ai.goal.TurretTargetGoal;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityTurret extends EntityRidablePart<TurretStats, TurretInstance<TurretStats>> {

	public static final EntityDataAccessor<Float> RELROTX = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> RELROTY = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	
	public float xRotRelO, yRotRelO;
	/**
	 * only used on server side
	 */
	private int newRiderCoolDown, overrideAnglesTime = -100;
	/**
	 * only used on server side
	 */
	private float overrideRotX, overrideRotY;
	protected int lastShootTick;
	
	public EntityTurret(EntityType<?> type, Level level, String defaultPreset) {
		super(type, level, defaultPreset);
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(RELROTX, 0f);
		entityData.define(RELROTY, 0f);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		setXRot(tag.getFloat("xRot"));
		setYRot(tag.getFloat("yRot"));
		setRelRotX(tag.getFloat("relrotx"));
		setRelRotY(tag.getFloat("relroty"));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putFloat("xRot", getXRot());
		tag.putFloat("yRot", getYRot());
		tag.putFloat("relrotx", getRelRotX());
		tag.putFloat("relroty", getRelRotY());
	}
	
	@Override
	public void tick() {
		super.tick();
		tickRotate();
	}

	public void setOverrideLookAngles(float overrideRotX, float overrideRotY) {
		overrideAnglesTime = tickCount;
		this.overrideRotX = overrideRotX;
		this.overrideRotY = overrideRotY;
	}

	protected void tickRotate() {
		xRotRelO = getRelRotX();
		yRotRelO = getRelRotY();
		float goalRotX, goalRotY;
		LivingEntity gunner = getPassenger();
		if (tickCount - overrideAnglesTime < 100) {
			goalRotX = overrideRotX;
			goalRotY = overrideRotY;
		} else if (gunner != null) {
			goalRotX = gunner.getXRot();
			goalRotY = gunner.getYHeadRot();
		} else return;
		rotateTowards(goalRotX, goalRotY);
	}

	protected void rotateTowards(float goalRotX, float goalRotY) {
		Quaternion ra = Quaternion.ONE;
		EntityVehicle vehicle = getParentVehicle();
		if (vehicle != null) ra = vehicle.getQBySide();
		if (!getLevel().isClientSide()) {
			if (newRiderCoolDown > 0) --newRiderCoolDown;
			float rely = yRotRelO, relx = xRotRelO;
			float rotrate = getRotRate(), minrotx = getMinRotX(), maxrotx = getMaxRotX();

			WeaponInstance<?> data = getWeaponData();
			if (data != null) data.tick(vehicle, true);
			float[] relangles = UtilAngles.globalToRelativeDegrees(goalRotX, goalRotY, ra);

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
		float cos = Mth.cos(getRelRotY()*Mth.DEG_TO_RAD), sin = Mth.sin(getRelRotY()*Mth.DEG_TO_RAD);
		return UtilAngles.rotateVector(new Vec3(getPassengerOffsets().x*cos+getPassengerOffsets().z*sin,
				offset, getPassengerOffsets().z*cos+getPassengerOffsets().x*sin), q)
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
		WeaponInstance<?> wd = getWeaponData();
		if (wd == null) return false;
		return wd.getStats().requiresRadar();
	}
	
	public double getAIHorizontalRange() {
		WeaponInstance<?> wd = getWeaponData();
		if (wd == null) return 300;
		return wd.getStats().getMobTurretRange();
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
	protected double getClientRenderDistance() {
		return Config.CLIENT.renderTurretDistance.get();
	}
	
	public int getAmmo() {
		if (getPartInstance() == null) return 0;
		return (int)getPartInstance().getCurrentAmmo();
	}
	
	public int getMaxAmmo() {
		if (getPartInstance() == null) return 0;
		return (int)getPartInstance().getMaxAmmo();
	}

	public int addAmmo(int ammo) {
		if (getPartInstance() == null) return 0;
		return getPartInstance().addWeaponAmmo(ammo);
	}
	
	public String getWeaponId() {
		if (getPartInstance() == null) return "";
		return getPartInstance().getWeaponId();
	}
	
	@Nullable
	public WeaponInstance<?> getWeaponData() {
		TurretInstance<TurretStats> instance = getPartInstance();
		if (instance == null) return null;
		return instance.getWeaponData();
	}
	
	public void shoot(Entity shooter) {
		WeaponInstance<?> data = getWeaponData();
		if (getLevel().isClientSide() || data == null || newRiderCoolDown > 0) return;
		boolean consume = true;
		Vec3 pos = position();
		EntityVehicle parent = null;
		if (getVehicle() instanceof EntityVehicle craft) {
			if (!craft.isOperational()) return;
			pos = pos.add(UtilAngles.rotateVector(new Vec3(0, getStats().getWeaponOffset(), 0), craft.getQ()));
			if (craft.isNoConsume()) consume = false;
			parent = craft;
		}
		Player p = null;
		if (shooter instanceof ServerPlayer player) {
			if (player.isCreative()) consume = false;
			p = player;
		}
		boolean consumeAmmo = getLevel().getGameRules().getBoolean(DSCGameRules.CONSUME_AMMO);
		boolean couldShoot = data.checkRecoil();
		data.setSlot(getSlotId());
		data.shootFromTurret(getLevel(), shooter, getLookAngle(), pos, parent, consume && consumeAmmo);
		if (couldShoot) specialShoot(shooter, pos, parent, consume && consumeAmmo, data);
		if (data.isFailedLaunch()) {
			if (p != null) p.displayClientMessage(
					UtilMCText.translatable(data.getFailedLaunchReason()), 
					true);
		} else {
			setLastShootTick(tickCount);
			TurretInstance<TurretStats> instance = getPartInstance();
			if (instance != null) instance.setCurrentAmmo(data.getCurrentAmmo());
		}
	}

	@Nullable
	public EntityWeapon<?> getFiredWeapon() {
		WeaponInstance<?> data = getWeaponData();
		if (data == null) return null;
		return data.getFiredWeapon();
	}
	
	protected void specialShoot(Entity shooter, Vec3 pos, EntityVehicle parent, boolean consume, WeaponInstance<?> data) {
		if (getShootType() == ShootType.NORMAL) return;
		//System.out.println("SPECIAL SHOOT "+shootType);
		if (getShootType() == ShootType.MARK7) {
			float d = 1;
			float yRad = getYRot() * Mth.DEG_TO_RAD;
			Vec3 posL = pos.add(new Vec3(-d*Mth.cos(yRad), 0, -d*Mth.sign(yRad))); 
			Vec3 posR = pos.add(new Vec3(d*Mth.cos(yRad), 0, d*Mth.sign(yRad)));
			data.shootFromTurret(getLevel(), shooter, getLookAngle(), posL, parent, consume, true);
			data.shootFromTurret(getLevel(), shooter, getLookAngle(), posR, parent, consume, true);
		}
	}

	public ShootType getShootType() {
		return getStats().getShootType();
	}
	
	public int getLastShootTick() {
		return lastShootTick;
	}

	public void setLastShootTick(int tick) {
		lastShootTick = tick;
	}
	
	@Override
	public PartType getPartType() {
		return PartType.TURRENT;
	}
	
	public RotBounds getRotBounds() {
		return getStats().getRotBounds();
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
	
	public enum ShootType {
		NORMAL,
		MARK7
	}
	
	@Override
	public boolean canGetHurt() {
		return true;
	}
	
	@Override
	public boolean isTurret() {
		return true;
	}
	
	@Override
	public boolean isAttackable() {
		return true;
	}

	@Override
	public boolean isAlive() {
		return true;
	}

	public void onClientShoot() {
		setLastShootTick(tickCount);
	}

}
