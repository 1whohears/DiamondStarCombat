package com.onewhohears.dscombat.entity.parts;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.logging.LogUtils;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.TurretInstance;
import com.onewhohears.dscombat.data.parts.stats.TurretStats;
import com.onewhohears.dscombat.data.parts.stats.TurretStats.RotBounds;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.data.weapon.MuzzleSmokeData;
import com.onewhohears.dscombat.entity.ai.goal.TurretShootGoal;
import com.onewhohears.dscombat.entity.ai.goal.TurretTargetGoal;
import com.onewhohears.dscombat.entity.parts.hitbox.TurretHitbox;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.dscombat.client.util.UtilParticles;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import org.slf4j.Logger;

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

import java.util.ArrayList;
import java.util.List;

public class EntityTurret extends EntityRidablePart<TurretStats, TurretInstance<TurretStats>> {

	private static final Logger LOGGER = LogUtils.getLogger();
	
	public static final EntityDataAccessor<Float> RELROTX = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> RELROTY = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Boolean> IS_EJECTED = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Float> EJECTED_ROTATION_SPEED = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	
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
	private MuzzleSmokeData[] lastSmokeData = null;
	private int flashMaxLifetime = 5;
	
	public EntityTurret(EntityType<?> type, Level level, String defaultPreset) {
		super(type, level, defaultPreset);
	}

	@Override
	public void init() {
		super.init();
		createTurretHitboxes();
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(RELROTX, 0f);
		entityData.define(RELROTY, 0f);
		entityData.define(IS_EJECTED, false);
		entityData.define(EJECTED_ROTATION_SPEED, 0f);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		setXRot(tag.getFloat("xRot"));
		setYRot(tag.getFloat("yRot"));
		setRelRotX(tag.getFloat("relrotx"));
		setRelRotY(tag.getFloat("relroty"));
		setEjected(tag.getBoolean("isEjected"));
		setEjectedRotationSpeed(tag.getFloat("ejectedRotationSpeed"));
		// load hitbox states
		if (tag.contains("turret_hitbox_data")) {
			CompoundTag hbData = tag.getCompound("turret_hitbox_data");
			for (TurretHitbox hb : turretHitboxes) hb.readNbt(hbData);
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putFloat("xRot", getXRot());
		tag.putFloat("yRot", getYRot());
		tag.putFloat("relrotx", getRelRotX());
		tag.putFloat("relroty", getRelRotY());
		tag.putBoolean("isEjected", isEjected());
		tag.putFloat("ejectedRotationSpeed", getEjectedRotationSpeed());
		// save hitbox states
		CompoundTag hbData = new CompoundTag();
		for (TurretHitbox hb : turretHitboxes) hb.writeNbt(hbData);
		tag.put("turret_hitbox_data", hbData);
	}
	
	@Override
	public void tick() {
		// If turret is ejected, skip parent check in super.tick()
		if (isEjected() && getVehicle() == null) {
			// Call base tick without parent check
			if (firstTick) init();
			baseTick();
			
			// Apply falling physics
			if (!onGround()) {
				Vec3 motion = getDeltaMovement();
				motion = motion.add(0, -0.08, 0); // Gravity
				setDeltaMovement(motion.scale(0.98)); // Air friction
				
				// Apply rotation while in air - use yRotO and yRot for smooth rotation
				float rotSpeed = getEjectedRotationSpeed();
				yRotO = getYRot();
				setYRot(getYRot() + rotSpeed);
				
				// Slowly tilt downward
				xRotO = getXRot();
				float currentXRot = getXRot();
				if (currentXRot < 45f) { // Limit downward tilt to 45 degrees
					setXRot(currentXRot + 0.5f);
				}
			} else {
				// On ground - slow down
				setDeltaMovement(getDeltaMovement().multiply(0.8, 0, 0.8));
				// Stop rotation when on ground
				if (!isClientSide()) {
					setEjectedRotationSpeed(getEjectedRotationSpeed() * 0.9f);
				}
			}
			
			// Apply movement
			move(net.minecraft.world.entity.MoverType.SELF, getDeltaMovement());
			
			// Add campfire smoke during flight
			if (level().isClientSide && !onGround()) {
				// Create campfire smoke particles
				for (int i = 0; i < 3; i++) {
					double offsetX = (random.nextDouble() - 0.5) * 0.5;
					double offsetY = (random.nextDouble() - 0.5) * 0.5;
					double offsetZ = (random.nextDouble() - 0.5) * 0.5;
					
					level().addParticle(
						net.minecraft.core.particles.ParticleTypes.CAMPFIRE_COSY_SMOKE,
						getX() + offsetX,
						getY() + offsetY,
						getZ() + offsetZ,
						0, 0.05, 0
					);
				}
			}
			
			// Count ticks without parent
			noParentTicks++;
			
			// Remove after 5 minutes
			if (noParentTicks > MAX_NO_PARENT_TICKS) {
				discard();
			}
			
			// Don't call tickRotate for ejected turrets - it interferes with rotation
			if (!isEjected()) {
				tickRotate();
			}
		} else {
			super.tick();
			tickRotate();
		}
		tickTurretHitboxes();
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
		QuaternionF ra = QuaternionF.ONE;
		EntityVehicle vehicle = getParentVehicle();
		if (vehicle != null) ra = vehicle.getQBySide();
		if (!isClientSide()) {
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
		QuaternionF q;
		if (isClientSide()) q = craft.getClientQ();
		else q = craft.getQ();
		double offset = getPassengersRidingOffset() + passenger.getMyRidingOffset() + passenger.getEyeHeight();
		Vec3 xzOffset;
		if (getStats().isFixedPassengerOffset()) {
			// No rotation around turret — offset stays fixed relative to vehicle
			xzOffset = new Vec3(getPassengerOffsets().x, offset, getPassengerOffsets().z);
		} else {
			// Default: XZ offset rotates with turret yaw
			float cos = Mth.cos(getRelRotY()*Mth.DEG_TO_RAD), sin = Mth.sin(getRelRotY()*Mth.DEG_TO_RAD);
			xzOffset = new Vec3(getPassengerOffsets().x*cos+getPassengerOffsets().z*sin,
					offset, getPassengerOffsets().z*cos+getPassengerOffsets().x*sin);
		}
		return UtilAngles.rotateVector(xzOffset, q)
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
		return getWorld().getGameRules().getInt(DSCGameRules.MOB_TURRET_VERTICAL_RANGE);
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

	/** Returns all weapon ids available on this turret. */
	public java.util.List<String> getAllWeaponIds() {
		TurretInstance<TurretStats> instance = getPartInstance();
		if (instance == null) return java.util.Collections.emptyList();
		return instance.getAllWeaponIds();
	}

	public int getWeaponIndex() {
		TurretInstance<TurretStats> instance = getPartInstance();
		if (instance == null) return 0;
		return instance.getWeaponIndex();
	}

	public int getWeaponCount() {
		TurretInstance<TurretStats> instance = getPartInstance();
		if (instance == null) return 0;
		return instance.getWeaponCount();
	}

	/** Cycle to next/previous weapon. input: +1 or -1. */
	public void selectNextWeapon(int input) {
		TurretInstance<TurretStats> instance = getPartInstance();
		if (instance == null) return;
		instance.selectNextWeapon(input);
	}

	public void setWeaponIndex(int index) {
		TurretInstance<TurretStats> instance = getPartInstance();
		if (instance == null) return;
		instance.setWeaponIndex(index);
	}
	
	@Nullable
	public WeaponInstance<?> getWeaponData() {
		TurretInstance<TurretStats> instance = getPartInstance();
		if (instance == null) return null;
		return instance.getSelectedWeaponData();
	}
	
	public void shoot(Entity shooter) {
		WeaponInstance<?> data = getWeaponData();
		if (isClientSide() || data == null || newRiderCoolDown > 0) return;
		// Check recoil before attempting to shoot
		if (!data.checkRecoil()) return;
		boolean consume = true;
		Vec3 basePos = position();
		EntityVehicle parent = null;
		if (getVehicle() instanceof EntityVehicle craft) {
			if (!craft.isOperational()) return;
			if (craft.isNoConsume()) consume = false;
			parent = craft;
		}
		Player p = null;
		if (shooter instanceof ServerPlayer player) {
			if (player.isCreative()) consume = false;
			p = player;
		}
		boolean consumeAmmo = getWorld().getGameRules().getBoolean(DSCGameRules.CONSUME_AMMO);
		
		// Calculate shoot position
		Vec3 shootPos = basePos;
		TurretInstance<TurretStats> instance = getPartInstance();
		
		// Check if this is an extra weapon (check BEFORE setting slot, as extra weapons have their slot set in TurretInstance.setup)
		boolean isExtraWeapon = false;
		java.util.Set<String> firedWeaponNames = new java.util.HashSet<>(); // Track which weapons have already fired
		if (instance != null && parent != null) {
			for (com.onewhohears.dscombat.data.weapon.ExtraWeaponData extraData : instance.getExtraWeapons()) {
				if (extraData.getName().equals(data.getSlotId())) {
					isExtraWeapon = true;
					// This is an extra weapon, calculate its world position
					Vec3 localOffset = extraData.getPos();
					
					// First rotate by turret's yaw and pitch (инвертируем для правильного направления)
					float turretYaw = -getRelRotY(); // Минус для инверсии направления
					float turretPitch = getRelRotX(); // Pitch без инверсии
					
					// Создаем полный кватернион турели (yaw + pitch)
					com.onewhohears.onewholibs.util.math.QuaternionF turretQYaw = 
						com.onewhohears.onewholibs.util.math.Vec3f.YP.rotationDegrees(turretYaw);
					com.onewhohears.onewholibs.util.math.QuaternionF turretQPitch = 
						com.onewhohears.onewholibs.util.math.Vec3f.XP.rotationDegrees(turretPitch);
					com.onewhohears.onewholibs.util.math.QuaternionF turretQ = turretQYaw.copy();
					turretQ.mul(turretQPitch);
					
					Vec3 rotatedByTurret = UtilAngles.rotateVector(localOffset, turretQ);
					
					// Then rotate by vehicle's quaternion
					Vec3 rotatedByVehicle = UtilAngles.rotateVector(rotatedByTurret, parent.getQ());
					
					// Add to turret position
					shootPos = basePos.add(rotatedByVehicle);
					firedWeaponNames.add(extraData.getName());
					
					// Fire from linked weapons too
					if (extraData.hasLinkedWeapons()) {
						for (String linkedName : extraData.getLinkedWeapons()) {
							if (firedWeaponNames.contains(linkedName)) {
								continue; // Already fired
							}
							// Find linked weapon data
							com.onewhohears.dscombat.data.weapon.ExtraWeaponData linkedData = null;
							for (com.onewhohears.dscombat.data.weapon.ExtraWeaponData ed : instance.getExtraWeapons()) {
								if (ed.getName().equals(linkedName)) {
									linkedData = ed;
									break;
								}
							}
							if (linkedData == null) continue;
							
							// Calculate linked weapon position
							Vec3 linkedLocalOffset = linkedData.getPos();
							Vec3 linkedRotatedByTurret = UtilAngles.rotateVector(linkedLocalOffset, turretQ);
							Vec3 linkedRotatedByVehicle = UtilAngles.rotateVector(linkedRotatedByTurret, parent.getQ());
							Vec3 linkedShootPos = basePos.add(linkedRotatedByVehicle);
							
							// Fire from linked position (don't consume ammo - already consumed by main weapon)
							Vec3 shootDirection = UtilAngles.rotationToVector(getYRot(), getXRot());
							data.shootFromTurret(getWorld(), shooter, shootDirection, linkedShootPos, parent, false, true);
							
							// Spawn muzzle flash for linked weapon
							if (!getWorld().isClientSide && !data.isFailedLaunch()) {
								MuzzleSmokeData[] smokeData = data.getStats().getMuzzleSmokeData();
								if (smokeData != null && smokeData.length > 0) {
									for (MuzzleSmokeData smoke : smokeData) {
										if (smoke.flashLifetime > 0) {
											com.onewhohears.dscombat.entity.weapon.EntityMuzzleFlash flash = 
												new com.onewhohears.dscombat.entity.weapon.EntityMuzzleFlash(
													com.onewhohears.dscombat.init.ModEntities.MUZZLE_FLASH.get(), 
													getWorld()
												);
											flash.setPos(linkedShootPos);
											flash.setXRot(getXRot());
											flash.setYRot(getYRot());
											flash.setShootDirection(shootDirection);
											flash.setScale(smoke.flashScale);
											flash.setMaxLifetime(smoke.flashLifetime);
											getWorld().addFreshEntity(flash);
										}
									}
								}
							}
							firedWeaponNames.add(linkedName);
						}
					}
					break;
				}
			}
		}
		
		// If not extra weapon, set slot to turret slot and use default turret position with weapon offset
		if (!isExtraWeapon) {
			data.setSlot(getSlotId());
		}
		
		// Вычисляем направление выстрела ТАК ЖЕ, как это делают пули в EntityBullet.init()
		// Используем rotationToVector вместо getLookAngle() для точного совпадения
		Vec3 shootDirection = UtilAngles.rotationToVector(getYRot(), getXRot());

		if (!isExtraWeapon && parent != null) {
			// Создаем кватернион турели из её yaw и pitch
			float turretYaw = -getRelRotY();
			float turretPitch = getRelRotX();
			com.onewhohears.onewholibs.util.math.QuaternionF turretQYaw =
				com.onewhohears.onewholibs.util.math.Vec3f.YP.rotationDegrees(turretYaw);
			com.onewhohears.onewholibs.util.math.QuaternionF turretQPitch =
				com.onewhohears.onewholibs.util.math.Vec3f.XP.rotationDegrees(turretPitch);
			com.onewhohears.onewholibs.util.math.QuaternionF turretQ = turretQYaw.copy();
			turretQ.mul(turretQPitch);

			Vec3[] offsets = getStats().getWeaponOffsets();

			if (getStats().isAlternatingBarrels() && offsets.length > 1) {
				// Поочерёдный режим: берём только текущий ствол и переключаем
				int offsetIdx = (instance != null) ? instance.consumeAndAdvanceOffsetIndex() : 0;
				Vec3 rotatedByTurret = UtilAngles.rotateVector(offsets[offsetIdx], turretQ);
				Vec3 rotatedByVehicle = UtilAngles.rotateVector(rotatedByTurret, parent.getQ());
				shootPos = basePos.add(rotatedByVehicle);
			} else {
				// Одновременный режим: первый offset — основной выстрел,
				// остальные — дополнительные (без траты патронов)
				for (int oi = 0; oi < offsets.length; oi++) {
					Vec3 rotatedByTurret = UtilAngles.rotateVector(offsets[oi], turretQ);
					Vec3 rotatedByVehicle = UtilAngles.rotateVector(rotatedByTurret, parent.getQ());
					Vec3 offsetShootPos = basePos.add(rotatedByVehicle);
					if (oi == 0) {
						shootPos = offsetShootPos;
					} else {
						data.shootFromTurret(getWorld(), shooter, shootDirection, offsetShootPos, parent, false, true);
						if (!getWorld().isClientSide && !data.isFailedLaunch()) {
							MuzzleSmokeData[] smokeData = data.getStats().getMuzzleSmokeData();
							if (smokeData != null && smokeData.length > 0) {
								for (MuzzleSmokeData smoke : smokeData) {
									if (smoke.flashLifetime > 0) {
										com.onewhohears.dscombat.entity.weapon.EntityMuzzleFlash flash =
											new com.onewhohears.dscombat.entity.weapon.EntityMuzzleFlash(
												com.onewhohears.dscombat.init.ModEntities.MUZZLE_FLASH.get(),
												getWorld()
											);
										flash.setPos(offsetShootPos);
										flash.setXRot(getXRot());
										flash.setYRot(getYRot());
										flash.setShootDirection(shootDirection);
										flash.setScale(smoke.flashScale);
										flash.setMaxLifetime(smoke.flashLifetime);
										getWorld().addFreshEntity(flash);
									}
								}
							}
						}
					}
				}
			}
		}

		// Основной выстрел (из текущего offset'а)
		data.shootFromTurret(getWorld(), shooter, shootDirection, shootPos, parent, consume && consumeAmmo, true);
		specialShoot(shooter, shootPos, parent, consume && consumeAmmo, data);
		
		// Вспышка для основного ствола
		if (!getWorld().isClientSide && !data.isFailedLaunch()) {
			MuzzleSmokeData[] smokeData = data.getStats().getMuzzleSmokeData();
			if (smokeData != null && smokeData.length > 0) {
				for (MuzzleSmokeData smoke : smokeData) {
					if (smoke.flashLifetime > 0) {
						com.onewhohears.dscombat.entity.weapon.EntityMuzzleFlash flash = 
							new com.onewhohears.dscombat.entity.weapon.EntityMuzzleFlash(
								com.onewhohears.dscombat.init.ModEntities.MUZZLE_FLASH.get(), 
								getWorld()
							);
						flash.setPos(shootPos);
						flash.setXRot(getXRot());
						flash.setYRot(getYRot());
						flash.setShootDirection(shootDirection);
						flash.setScale(smoke.flashScale);
						flash.setMaxLifetime(smoke.flashLifetime);
						getWorld().addFreshEntity(flash);
					}
				}
			}
		}
		
		if (data.isFailedLaunch()) {
			if (p != null) p.displayClientMessage(
					UtilMCText.translatable(data.getFailedLaunchReason()), 
					true);
		} else {
			setLastShootTick(tickCount);
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
			data.shootFromTurret(getWorld(), shooter, getLookAngle(), posL, parent, consume, true);
			data.shootFromTurret(getWorld(), shooter, getLookAngle(), posR, parent, false, true); // Don't consume ammo for second shot
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
		WeaponInstance<?> wi = getWeaponData();
		if (wi != null) {
			MuzzleSmokeData[] smokeData = wi.getStats().getMuzzleSmokeData();
			lastSmokeData = smokeData;
			
			// Вычисляем max lifetime из данных
			if (smokeData != null && smokeData.length > 0) {
				flashMaxLifetime = smokeData[0].flashLifetime;
			}
		}
	}

	public MuzzleSmokeData[] getLastSmokeData() {
		return lastSmokeData;
	}

	public int getFlashMaxLifetime() {
		return flashMaxLifetime;
	}

	/**
	 * World position of the muzzle for particle effects.
	 * Offset is along the barrel (getLookAngle) so particles appear at the muzzle.
	 */
	public Vec3 getMuzzlePosition() {
		Vec3 dir = getLookAngle();
		// Используем длину вектора weaponOffset как смещение вдоль ствола
		double totalOffset = getStats().getWeaponOffset().length() + getStats().getMuzzleParticleOffset();
		return position().add(dir.scale(totalOffset));
	}
	
	public boolean isEjected() {
		return entityData.get(IS_EJECTED);
	}
	
	public void setEjected(boolean ejected) {
		entityData.set(IS_EJECTED, ejected);
		// Set random rotation speed when ejected (5-10 degrees per tick for more visible rotation)
		if (ejected && !isClientSide()) {
			float rotSpeed = 5.0f + random.nextFloat() * 5.0f;
			// Randomly choose clockwise or counterclockwise
			if (random.nextBoolean()) {
				rotSpeed = -rotSpeed;
			}
			setEjectedRotationSpeed(rotSpeed);
		}
	}
	
	public float getEjectedRotationSpeed() {
		return entityData.get(EJECTED_ROTATION_SPEED);
	}
	
	public void setEjectedRotationSpeed(float speed) {
		entityData.set(EJECTED_ROTATION_SPEED, speed);
	}
	
	private int noParentTicks = 0;
	private static final int MAX_NO_PARENT_TICKS = 6000; // 5 минут (6000 тиков = 300 секунд)

	private final List<TurretHitbox> turretHitboxes = new ArrayList<>();
	
	@Override
	protected void onNoParent() {
		// Если турель была выброшена, не удаляем её сразу
		if (!isEjected()) {
			discardTurretHitboxes();
			allowRemoval = true;
			discard();
			return;
		}
		
		// Подсчитываем тики без родителя
		noParentTicks++;
		
		// Удаляем через 10 секунд
		if (noParentTicks > MAX_NO_PARENT_TICKS) {
			discardTurretHitboxes();
			allowRemoval = true;
			discard();
		}
	}
	
	@Override
	public void remove(@NotNull RemovalReason reason) {
		// MOHIST FIX: Block DISCARDED removal for turrets too
		// Allow removal only when parent explicitly allows it
		if (!level().isClientSide() && reason == RemovalReason.DISCARDED && !allowRemoval) {
			return; // Always block DISCARDED unless parent allows
		}
		super.remove(reason);
	}
	
	/**
	 * MOHIST FIX: Called by parent vehicle to allow turret removal
	 * Removed override - using parent's implementation with logging
	 */

	// ---- Turret Hitbox System ----

	private boolean hitboxesCreated = false;

	protected void createTurretHitboxes() {
		if (level().isClientSide()) return;
		if (!isStatsHolderLoaded()) return;
		if (hitboxesCreated) return;
		hitboxesCreated = true;
		List<TurretHitbox> created = getStats().createTurretHitboxes(this);
		turretHitboxes.addAll(created);
		for (TurretHitbox hb : turretHitboxes) {
			hb.setPos(position());
			level().addFreshEntity(hb);
		}
	}

	protected void discardTurretHitboxes() {
		for (TurretHitbox hb : turretHitboxes) hb.discard();
		turretHitboxes.clear();
		hitboxesCreated = false;
	}

	protected void tickTurretHitboxes() {
		// Retry creation if stats weren't loaded on init()
		if (!hitboxesCreated) createTurretHitboxes();
	}

	public List<TurretHitbox> getTurretHitboxes() {
		return turretHitboxes;
	}

}
