package com.onewhohears.dscombat.entity.aircraft;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.event.forgebus.ClientInputEvents;
import com.onewhohears.dscombat.common.container.AircraftMenuContainer;
import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ToServerAircraftQ;
import com.onewhohears.dscombat.common.network.toserver.ToServerRequestPlaneData;
import com.onewhohears.dscombat.data.AircraftPresets;
import com.onewhohears.dscombat.data.AircraftTextures;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.PartsManager;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponSystem;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.item.ItemAmmo;
import com.onewhohears.dscombat.item.ItemCreativeWand;
import com.onewhohears.dscombat.item.ItemGasCan;
import com.onewhohears.dscombat.item.ItemRepairTool;
import com.onewhohears.dscombat.util.UtilClientSafeSoundInstance;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilAngles.EulerAngles;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.RegistryObject;

/**
 * the parent class for planes, helicopters, boats, and ground vehicles
 * @author 1whohears
 */
public abstract class EntityAircraft extends Entity {
	
	public static final EntityDataAccessor<Float> MAX_HEALTH = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> MAX_SPEED = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> THROTTLE = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> THROTTLEUP = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> THROTTLEDOWN = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Quaternion> Q = SynchedEntityData.defineId(EntityAircraft.class, DataSerializers.QUATERNION);
	public static final EntityDataAccessor<Boolean> FREE_LOOK = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Float> STEALTH = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> MAX_ROLL = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> MAX_PITCH = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> MAX_YAW = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> TURN_RADIUS = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> ACC_ROLL = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> ACC_PITCH = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> ACC_YAW = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> IDLEHEAT = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> WEIGHT = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> WING_AREA = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Boolean> LANDING_GEAR = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> TEST_MODE = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Integer> CURRRENT_DYE_ID = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> NO_CONSUME = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> PLAYERS_ONLY_RADAR = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.BOOLEAN);
	
	public static final double ACC_GRAVITY = 0.025;
	public static final double CO_DRAG = 0.015;
	public static final double CO_STATIC_FRICTION = 4.50;
	public static final double CO_KINETIC_FRICTION = 1.50;
	public static final float CO_SLIDE_TORQUE = 1.00f;
	public static final double collideSpeedThreshHold = 0.8d;
	public static final double collideSpeedWithGearThreshHold = 1.8d;
	public static final double collideDamageRate = 200d;
	
	public final PartsManager partsManager = new PartsManager(this);
	public final WeaponSystem weaponSystem = new WeaponSystem(this);
	public final RadarSystem radarSystem = new RadarSystem(this);
	
	public final boolean negativeThrottle;
	
	protected final RegistryObject<SoundEvent> engineSound;
	protected final RegistryObject<Item> item;
	protected final AircraftTextures textures;
	
	public Quaternion prevQ = Quaternion.ONE.copy();
	public Quaternion clientQ = Quaternion.ONE.copy();
	
	public boolean inputMouseMode, inputFlare, inputShoot, inputSelect, inputOpenMenu, inputSpecial, inputRadarMode, inputBothRoll;
	public float inputThrottle, inputPitch, inputRoll, inputYaw;
	public float zRot, zRotO; 
	public float torqueX, torqueY, torqueZ, torqueXO, torqueYO, torqueZO;
	public Vec3 prevMotion = Vec3.ZERO;
	public Vec3 forces = Vec3.ZERO;
	
	public boolean nightVisionHud = false;
	
	protected int xzSpeedDir, flareNum;
	protected float xzSpeed, totalMass, xzYaw, slideAngle, slideAngleCos, maxThrust, currentFuel, maxFuel;
	protected double staticFric, kineticFric;
	
	private ResourceLocation currentTexture;
	private int lerpSteps, newRiderCooldown, deadTicks;
	private double lerpX, lerpY, lerpZ, lerpXRot, lerpYRot;
	private float landingGearPos, landingGearPosOld;
	
	public EntityAircraft(EntityType<? extends EntityAircraft> entity, Level level, 
			AircraftTextures textures, RegistryObject<SoundEvent> engineSound, RegistryObject<Item> item,
			boolean negativeThrottle) {
		super(entity, level);
		this.textures = textures;
		this.currentTexture = textures.getDefaultTexture();
		this.engineSound = engineSound;
		this.blocksBuilding = true;
		this.item = item;
		this.negativeThrottle = negativeThrottle;
		// FIXME player can punch and push aircraft
		// FIXME aircraft don't push entities (add collisions)
	}
	
	@Override
	protected void defineSynchedData() {
		entityData.define(MAX_HEALTH, 100f);
        entityData.define(HEALTH, 100f);
		entityData.define(MAX_SPEED, 1.0f);
		entityData.define(THROTTLE, 0.0f);
		entityData.define(THROTTLEUP, 0.05f);
		entityData.define(THROTTLEDOWN, 0.05f);
		entityData.define(Q, Quaternion.ONE);
		entityData.define(FREE_LOOK, true);
		entityData.define(STEALTH, 1f);
		entityData.define(MAX_ROLL, 1f);
		entityData.define(MAX_PITCH, 1f);
		entityData.define(MAX_YAW, 1f);
		entityData.define(ACC_ROLL, 0.5f);
		entityData.define(ACC_PITCH, 0.5f);
		entityData.define(ACC_YAW, 0.5f);
		entityData.define(IDLEHEAT, 1f);
		entityData.define(WEIGHT, 0.05f);
		entityData.define(WING_AREA, 1f);
		entityData.define(LANDING_GEAR, false);
		entityData.define(TEST_MODE, false);
		entityData.define(CURRRENT_DYE_ID, 0);
		entityData.define(TURN_RADIUS, 0f);
		entityData.define(NO_CONSUME, false);
		entityData.define(PLAYERS_ONLY_RADAR, false);
	}
	
	@Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        // if this entity is on the client side and receiving the quaternion of the plane from the server 
        if (level.isClientSide()) {
        	if (Q.equals(key)) {
        		if (isControlledByLocalInstance()) {
        			// if this entity is piloted by the client's player send the client quaternion to the server
        			PacketHandler.INSTANCE.sendToServer(new ToServerAircraftQ(this));
        		} else {
        			// if the client player is not controlling this plane then client quaternion = server quaternion
        			setPrevQ(getClientQ());
        			setClientQ(getQ());
        		}
        	} else if (CURRRENT_DYE_ID.equals(key)) {
        		currentTexture = textures.getTexture(getCurrentColorId());
        	}
        }
    }
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		// this function is called on the server side only
		setTestMode(compound.getBoolean("test_mode"));
		setNoConsume(compound.getBoolean("no_consume"));
		int color = -1;
		if (compound.contains("dyecolor")) color = compound.getInt("dyecolor");
		String initType = compound.getString("preset");
		if (!initType.isEmpty()) {
			CompoundTag tag = AircraftPresets.getPreset(initType);
			if (tag != null) compound.merge(tag);
		}
		partsManager.read(compound);
		weaponSystem.read(compound);
		radarSystem.read(compound);
		setMaxSpeed(compound.getFloat("max_speed"));
		setMaxHealth(compound.getFloat("max_health"));
		setHealth(compound.getFloat("health"));
		setStealth(compound.getFloat("stealth"));
		setTurnRadius(compound.getFloat("turn_radius"));
		setMaxDeltaRoll(compound.getFloat("maxroll"));
		setMaxDeltaPitch(compound.getFloat("maxpitch"));
		setMaxDeltaYaw(compound.getFloat("maxyaw"));
		setAccelerationRoll(compound.getFloat("accroll"));
		setAccelerationPitch(compound.getFloat("accpitch"));
		setAccelerationYaw(compound.getFloat("accyaw"));
		setThrottleIncreaseRate(compound.getFloat("throttleup"));
		setThrottleDecreaseRate(compound.getFloat("throttledown"));
		setIdleHeat(compound.getFloat("idleheat"));
		setAircraftWeight(compound.getFloat("weight"));
		setWingSurfaceArea(compound.getFloat("surfacearea"));
		setLandingGear(compound.getBoolean("landing_gear"));
		setCurrentThrottle(compound.getFloat("current_throttle"));
		setXRot(compound.getFloat("xRot"));
		setYRot(compound.getFloat("yRot"));
		zRot = compound.getFloat("zRot");
		Quaternion q = UtilAngles.toQuaternion(getYRot(), getXRot(), zRot);
		setQ(q);
		setPrevQ(q);
		setClientQ(q);
		if (color == -1) color = compound.getInt("dyecolor");
		setCurrentColor(DyeColor.byId(color));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putBoolean("test_mode", isTestMode());
		compound.putBoolean("no_consume", isNoConsume());
		compound.putString("preset", "");
		partsManager.write(compound);
		weaponSystem.write(compound);
		radarSystem.write(compound);
		compound.putFloat("max_speed", getMaxSpeed());
		compound.putFloat("max_health", getMaxHealth());
		compound.putFloat("health", getHealth());
		compound.putFloat("stealth", getStealth());
		compound.putFloat("turn_radius", getTurnRadius());
		compound.putFloat("maxroll", getMaxDeltaRoll());
		compound.putFloat("maxpitch", getMaxDeltaPitch());
		compound.putFloat("maxyaw", getMaxDeltaYaw());
		compound.putFloat("accroll", getAccelerationRoll());
		compound.putFloat("accpitch", getAccelerationPitch());
		compound.putFloat("accyaw", getAccelerationYaw());
		compound.putFloat("throttleup", getThrottleIncreaseRate());
		compound.putFloat("throttledown", getThrottleDecreaseRate());
		compound.putFloat("idleheat", getIdleHeat());
		compound.putFloat("weight", getAircraftWeight());
		compound.putFloat("surfacearea", getWingSurfaceArea());
		compound.putBoolean("landing_gear", isLandingGear());
		compound.putFloat("current_throttle", getCurrentThrottle());
		compound.putFloat("xRot", getXRot());
		compound.putFloat("yRot", getYRot());
		compound.putFloat("zRot", zRot);
		compound.putInt("dyecolor", getCurrentColorId());
	}
	
	public static enum AircraftType {
		PLANE,
		HELICOPTER,
		CAR,
		BOAT,
		SUBMARINE
	}
	
	public abstract AircraftType getAircraftType();
	
	/**
	 * called on this entities first tick on client and server side
	 * this function fires clientSetup() and serverSetup()
	 */
	public void init() {
		if (!level.isClientSide) serverSetup();
		else clientSetup();
	}
	
	/**
	 * fires every tick server and client side
	 */
	@Override
	public void tick() {
		if (firstTick) init(); // MUST BE CALLED BEFORE SUPER
		super.tick();
		if (Double.isNaN(getDeltaMovement().length())) 
            setDeltaMovement(Vec3.ZERO);
		// SET PREV/OLD
		prevMotion = getDeltaMovement();
		torqueXO = torqueX;
		torqueYO = torqueY;
		torqueZO = torqueZ;
		// SET DIRECTION
		zRotO = zRot;
		Quaternion q;
		if (level.isClientSide) q = getClientQ();
		else q = getQ();
		setPrevQ(q);
		controlDirection(q);
		q.normalize();
		if (level.isClientSide) setClientQ(q);
		else setQ(q);
		EulerAngles angles = UtilAngles.toDegrees(q);
		setXRot((float)angles.pitch);
		setYRot((float)angles.yaw);
		zRot = (float)angles.roll;
		// MOVEMENT
		forces = Vec3.ZERO;
		tickThrottle();
		if (!isTestMode()) {
			calcMoveStatsPre(q);
			tickMovement(q);
			calcAcc();
			motionClamp();
			move(MoverType.SELF, getDeltaMovement());
			calcMoveStatsPost(q);
		}
		if (isControlledByLocalInstance()) syncPacketPositionCodec(getX(), getY(), getZ());
        // OTHER
		controlSystem();
        tickParts();
		sounds();
		if (level.isClientSide) clientTick();
		else serverTick();
	}
	
	/**
	 * called on server side
	 */
	public void serverTick() {
		tickCollisions();
		waterDamage();
		if (!isTestMode() && !isOperational()) tickNoHealth();
	}
	
	/**
	 * called on server tick 
	 * damages plane if it falls 
	 */
	public void tickCollisions() {
		if (verticalCollisionBelow || verticalCollision) {
			double my = Math.abs(prevMotion.y);
			double th = collideSpeedThreshHold;
			if (isOperational() && isLandingGear() 
					&& (getXRot() < 15f && getXRot() > -15f)
					&& (zRot < 15f && zRot > -15f)) {
				th = collideSpeedWithGearThreshHold;
			}
			if (my > th && tickCount > 300) {
				hurt(DamageSource.FLY_INTO_WALL, 
					(float)((my-th)*collideDamageRate));
				if (!isOperational()) explode(null);
			}
		}
	}
	
	public void waterDamage() {
		if (tickCount % 20 == 0 && isInWater() && isOperational()) 
			hurt(DamageSource.DROWN, 5);
	}
	
	public void tickNoHealth() {
		if (deadTicks++ > 400) {
			kill();
			return;
		}
	}
	
	public boolean isOperational() {
		return getHealth() > 0;
	}
	
	public int getDeadTicks() {
		return deadTicks;
	}
	
	/**
	 * called on client side
	 */
	public void clientTick() {
		tickLerp(); 
		tickHealthSmoke();
		tickClientLandingGear();
	}
	
	private void tickHealthSmoke() {
		float r = getHealth() / getMaxHealth();
		if (r < 0.5f) smoke();
		if (r < 0.3f) for (int i = 0; i < 2; ++i) smoke();
		if (r < 0.1f) for (int i = 0; i < 4; ++i) smoke();
	}
	
	private void smoke() {
		if (Math.random() > 0.4d) return;
		level.addParticle(ParticleTypes.SMOKE, 
				getX(), getY(), getZ(), 
				random.nextGaussian() * 0.08D, 
				0.1D, 
				random.nextGaussian() * 0.08D);
	}
	
	/**
	 * restricts the motion of the craft based on max speed
	 */
	public void motionClamp() {
		Vec3 move = getDeltaMovement();
		double maxXZ = getMaxSpeedForMotion();
		
		Vec3 motionXZ = new Vec3(move.x, 0, move.z);
		double velXZ = motionXZ.length();
		if (velXZ > maxXZ) motionXZ = motionXZ.scale(maxXZ / velXZ);
		
		double maxY = 2.5;
		double my = move.y;
		if (Math.abs(my) > maxY) my = maxY * Math.signum(my);
		else if (Math.abs(my) < 0.001) my = 0;
		
		if (onGround && my < 0) my = -0.01; // THIS MUST BE BELOW ZERO
		setDeltaMovement(motionXZ.x, my, motionXZ.z);
	}
	
	public double getMaxSpeedForMotion() {
		return getMaxSpeed();
	}
	
	/**
	 * changes this craft's throttle every tick based on inputThrottle 
	 * also resets the controls if there isn't a controlling passenger
	 */
	public void tickThrottle() {
		if (getControllingPassenger() == null || !isOperational()) resetControls();
		if (inputThrottle > 0) increaseThrottle();
		else if (inputThrottle < 0) decreaseThrottle();
	}
	
	/**
	 * reads player input to change the direction of the craft
	 * @param q the current direction of the craft
	 */
	public void controlDirection(Quaternion q) {
		if (onGround) directionGround(q);
		else if (isInWater()) directionWater(q);
		else directionAir(q);
		q.mul(Vector3f.XN.rotationDegrees(torqueX));
		q.mul(Vector3f.YN.rotationDegrees(torqueY));
		q.mul(Vector3f.ZP.rotationDegrees(torqueZ));
		if (torqueX > getMaxDeltaPitch() || inputPitch == 0) torqueX = torqueDrag(torqueX);
		if (torqueY > getMaxDeltaYaw() || inputYaw == 0) torqueY = torqueDrag(torqueY);
		if (torqueZ > getMaxDeltaRoll() || inputRoll == 0) torqueZ = torqueDrag(torqueZ);
	}
	
	public void directionGround(Quaternion q) {
		if (!isOperational()) return;
		flatten(q, 5f, 5f);
		float max_tr = getTurnRadius();
		if (inputYaw == 0 || max_tr == 0) return;
		float tr = 1 / inputYaw * max_tr;
		float turn = xzSpeed / tr * xzSpeedDir;
		float turnDeg = turn * Mth.RAD_TO_DEG;
		if (isSliding()) {
			torqueY = turnDeg*slideAngleCos*CO_SLIDE_TORQUE;
		} else {
			torqueY = turnDeg;
		}
	}
	
	public void directionAir(Quaternion q) {
		
	}
	
	public void directionWater(Quaternion q) {
		directionAir(q);
	}
	
	public void flatten(Quaternion q, float dPitch, float dRoll) {
		torqueX = torqueZ = 0;
		EulerAngles angles = UtilAngles.toDegrees(q);
		float roll, pitch;
		if (dRoll != 0) {
			if (Math.abs(angles.roll) < dRoll) roll = (float) -angles.roll;
			else roll = -(float)Math.signum(angles.roll) * dRoll;
			q.mul(Vector3f.ZP.rotationDegrees(roll));
		}
		if (dPitch != 0) {
			if (Math.abs(angles.pitch) < dPitch) pitch = (float) -angles.pitch;
			else pitch = -(float)Math.signum(angles.pitch) * dPitch;
			q.mul(Vector3f.XP.rotationDegrees(pitch));
		}
	}
	
	private float torqueDrag(float torque) {
		float td = getTorqueDragMag();
		float abs = Math.abs(torque);
		if (abs < td) return 0;
		return (abs - td) * Math.signum(torque);
	}
	
	protected float getTorqueDragMag() {
		if (onGround) return 1.0f;
		else if (isInWater()) return 0.3f;
		else return 0.2f;
 	}
	
	public void resetTorque() {
		torqueX = torqueY = torqueZ = 0;
	}
	
	public void addTorqueX(float torque, boolean control) {
		if (control) {
			float tabs = Math.abs(torqueX);
			if (tabs > getMaxDeltaPitch()) torque = 0;
			else if (Math.abs(torqueX+torque) > getMaxDeltaPitch()) torque = (getMaxDeltaPitch() - tabs) * Math.signum(torqueX);
		}
		torqueX += torque;
	}
	
	public void addTorqueY(float torque, boolean control) {
		if (control) addTorqueY(torque, getMaxDeltaYaw());
		else torqueY += torque;
	}
	
	public void addTorqueY(float torque, float maxDeltaYaw) {
		maxDeltaYaw = Mth.abs(maxDeltaYaw);
		float tabs = Math.abs(torqueY);
		if (tabs > maxDeltaYaw) torque = 0;
		else if (Math.abs(torqueY+torque) > maxDeltaYaw) torque = (maxDeltaYaw-tabs)*Math.signum(torqueY);
		torqueY += torque;
	}
	
	public void addTorqueZ(float torque, boolean control) {
		if (control) {
			float tabs = Math.abs(torqueZ);
			if (tabs > getMaxDeltaRoll()) torque = 0;
			else if (Math.abs(torqueZ+torque) > getMaxDeltaRoll()) torque = (getMaxDeltaRoll() - tabs) * Math.signum(torqueZ);
		}
		torqueZ += torque;
	}
	
	protected void calcMoveStatsPre(Quaternion q) {
		totalMass = getAircraftWeight() + partsManager.getPartsWeight();
		staticFric = totalMass * ACC_GRAVITY * CO_STATIC_FRICTION;
		kineticFric = totalMass * ACC_GRAVITY * CO_KINETIC_FRICTION;
		maxThrust = partsManager.getTotalEngineThrust();
		flareNum = partsManager.getNumFlares();
		currentFuel = partsManager.getCurrentFuel();
		maxFuel = partsManager.getMaxFuel();
	}
	
	protected void calcMoveStatsPost(Quaternion q) {
		Vec3 m = getDeltaMovement();
		float y = getYRot();
		xzSpeed = (float) Math.sqrt(m.x*m.x + m.z*m.z);
		if (xzSpeed == 0) {
			xzYaw = y;
			slideAngle = 0;
		} else {
			xzYaw = UtilAngles.getYaw(m);
			slideAngle = Mth.degreesDifference(xzYaw, y);
			slideAngleCos = (float) Math.abs(Math.cos(Math.toRadians(slideAngle)));
		}
		xzSpeedDir = 1;
		if (Math.abs(slideAngle) > 90) xzSpeedDir = -1;
	}
	
	public void calcAcc() {
		setDeltaMovement(getDeltaMovement().add(forces.scale(1/totalMass)));
	}
	
	/**
	 * called on both client and server side every tick to change the craft's movement 
	 * a force is a Vec3 to be added to this entities motion or getDeltaMovement()
	 * in Minecraft an entitie's motion is the blocks an entity will move per tick
	 * @param q the plane's current rotation
	 */
	public void tickMovement(Quaternion q) {
		tickAlways(q);
		if (onGround && isInWater()) tickGroundWater(q);
		else if (onGround) tickGround(q);
		else if (isInWater()) tickWater(q);
		else tickAir(q);
	}
	
	public void tickAlways(Quaternion q) {
		forces = forces.add(getWeightForce());
		forces = forces.add(getThrustForce(q));
	}
	
	/**
	 * called on both client and server side every tick when the craft is on the ground
	 * @param q the plane's current rotation
	 */
	public void tickGround(Quaternion q) {
		Vec3 n = UtilAngles.rotationToVector(getYRot(), 0);
		if (isSliding() || willSlideFromTurn()) {
			setDeltaMovement(getDeltaMovement().add(n.scale(
				getDriveAcc() * slideAngleCos
			)));
			addFrictionForce(kineticFric);
			//debug("SLIDING");
		} else {
			setDeltaMovement(n.scale(xzSpeed*xzSpeedDir + getDriveAcc()));
			if (getCurrentThrottle() == 0 && xzSpeed != 0) addFrictionForce(0.1);
		}
	}
	
	public double getDriveAcc() {
		return getThrustMag()/totalMass;
	}
	
	protected void addFrictionForce(double f) {
		Vec3 m = getDeltaMovement();
		if (m.x == 0 && m.z == 0) return;
		Vec3 mn = m.normalize();
 		Vec3 force = mn.scale(-f);
		Vec3 acc = force.scale(1/totalMass);
		if (m.x != 0 && Math.signum(m.x+acc.x) != Math.signum(m.x)) {
			force = force.multiply(0, 1, 1);
			m = m.multiply(0, 1, 1);
		}
		if (m.z != 0 && Math.signum(m.z+acc.z) != Math.signum(m.z)) {
			force = force.multiply(1, 1, 0);
			m = m.multiply(1, 1, 0);
		}
		setDeltaMovement(m);
		forces = forces.add(force);
	}
	
	protected boolean willSlideFromTurn() {
		double max_tr = getTurnRadius();
		if (inputYaw != 0 && max_tr != 0) { // IF TURNING
			double tr = max_tr * 1 / Math.abs(inputYaw); // inputed turn radius
			double cen_acc = xzSpeed * xzSpeed / tr; // cen_acc needed to complete turn
			double cen_force = cen_acc * totalMass; // friction force needed to not slide
			//debug(cen_force+" >? "+staticFric);
			if (cen_force >= staticFric) return true; // if cen_force >= static-friction-threshold slide
		}
		return false;
	}
	
	public boolean isSlideAngleNearZero() {
		if (xzSpeedDir == -1) return Mth.abs(Mth.abs(slideAngle)-180) < 2;
		return Mth.abs(slideAngle) < 2;
	}
	
	public boolean isSliding() {
		return !isLandingGear() || !isSlideAngleNearZero();
	}
	
	/**
	 * called on both client and server side every tick when the craft is in the air
	 * @param q the plane's current rotation
	 */
	public void tickAir(Quaternion q) {
		forces = forces.add(getDragForce(q));
		resetFallDistance();
	}
	
	public void tickWater(Quaternion q) {
		tickAir(q);
	}
	
	public void tickGroundWater(Quaternion q) {
		tickGround(q);
	}
	
	/**
	 * @param q the plane's current rotation
	 * @return a force vector as the plane's thrust force this tick
	 */
	public abstract Vec3 getThrustForce(Quaternion q);
	
	/**
	 * @return the magnitude of the thrust force based on the engines, throttle, and 0 if no fuel
	 */
	public double getThrustMag() {
		if (getCurrentFuel() <= 0 || !isOperational()) return 0;
		return getCurrentThrottle() * getMaxThrust();
	}
	
	/**
	 * @return the total thrust from the engines in the parts manager
	 */
	public float getMaxThrust() {
		return maxThrust;
	}
	
	/**
	 * will be the inverse of some proportion of the plane's current movement
	 * @param q the plane's current rotation
	 * @return a force vector as the plane's drag this tick
	 */
	public Vec3 getDragForce(Quaternion q) {
		Vec3 direction = getDeltaMovement().normalize().scale(-1);
		Vec3 dragForce = direction.scale(getDragMag());
		return dragForce;
	}
	
	public double getDragMag() {
		// Drag = (drag coefficient) * (air pressure) * (speed)^2 * (wing surface area) / 2
		double air = UtilEntity.getAirPressure(getY());
		double speedSqr = getDeltaMovement().lengthSqr();
		return air * speedSqr * getSurfaceArea() * CO_DRAG;
	}
	
	public double getSurfaceArea() {
		return getBbHeight() * getBbWidth();
	}
	
	/**
	 * this is a super simplified aircraft parameter
	 * probably should be called the wing surface area as it's used in the lift force equation
	 * but it is also used to calculate drag so maybe I should make separate variables
	 * @return the surface area of the plane
	 */
	public final float getWingSurfaceArea() {
		return entityData.get(WING_AREA);
	}
	
	public final void setWingSurfaceArea(float area) {
		if (area < 0) area = 0;
		entityData.set(WING_AREA, area);
	}
	
	public Vec3 getWeightForce() {
		return new Vec3(0, -getTotalMass() * ACC_GRAVITY, 0);
	}
	
	/**
	 * @return this mass of the aircraft plus the mass of the parts
	 */
	public float getTotalMass() {
		return totalMass;
	}
	
	/**
	 * this is NOT the total weight
	 * @return the weight of the fuselage 
	 */
	public final float getAircraftWeight() {
		return entityData.get(WEIGHT);
	}
	
	public final void setAircraftWeight(float weight) {
		if (weight < 0) weight = 0;
		entityData.set(WEIGHT, weight);
	}
	
	/**
	 * fired on both client and server side to control the plane's weapons, flares, open menu
	 */
	public void controlSystem() {
		Entity controller = getControllingPassenger();
		if (controller == null) return;
		if (!level.isClientSide) {
			if (!isOperational()) return;
			weaponSystem.tick();
			radarSystem.tickUpdateTargets();
			boolean consume = !isNoConsume();
			if (controller instanceof ServerPlayer player) {
				if (inputOpenMenu) openMenu(player);
				if (player.isCreative()) consume = false;
			}
			if (consume) tickFuel();
			if (newRiderCooldown > 0) --newRiderCooldown;
			else if (inputShoot) weaponSystem.shootSelected(controller, consume);
			if (inputFlare && tickCount % 5 == 0) flare(controller, consume);
		} else {
			radarSystem.clientTick();
		}
	}
	
	public void openMenu(ServerPlayer player) {
		if (canOpenMenu()) {
			NetworkHooks.openScreen(player, 
				new SimpleMenuProvider((windowId, playerInv, p) -> 
						new AircraftMenuContainer(windowId, playerInv), 
				Component.translatable("container.dscombat.plane_menu")));
		} else player.displayClientMessage(
				Component.translatable(getOpenMenuError()), 
				true);
	}
	
	public boolean canOpenMenu() {
		return isOnGround() || isTestMode();
	}
	
	public String getOpenMenuError() {
		return "dscombat.no_menu_in_air";
	}
	
	public void flare(Entity controller, boolean consume) {
		partsManager.useFlares(consume);
	}
	
	/**
	 * ticks the parts manager
	 */
	public void tickParts() {
		if (level.isClientSide) partsManager.clientTickParts();
		else partsManager.tickParts();
	}
	
	@Override
	public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        if (x == getX() && y == getY() && z == getZ()) return;
        lerpX = x; lerpY = y; lerpZ = z;
        lerpYRot = yaw; lerpXRot = pitch;
        lerpSteps = 10;
    }
	
	/**
	 * this is used on the client side and fills in the positions and rotations of the entity between ticks
	 */
	private void tickLerp() {
		if (isControlledByLocalInstance()) {
			lerpSteps = 0;
			return;
		}
		if (lerpSteps > 0) {
			double d0 = getX() + (lerpX - getX()) / (double)lerpSteps;
	        double d1 = getY() + (lerpY - getY()) / (double)lerpSteps;
	        double d2 = getZ() + (lerpZ - getZ()) / (double)lerpSteps;
	        double d3 = Mth.wrapDegrees(lerpYRot - (double)getYRot());
	        setYRot(getYRot() + (float)d3 / (float)lerpSteps);
	        setXRot(getXRot() + (float)(lerpXRot - (double)getXRot()) / (float)lerpSteps);
	        --lerpSteps;
	        setPos(d0, d1, d2);
	        setRot(getYRot(), getXRot());
		}
	}
	
	public void updateControls(float throttle, float pitch, float roll, float yaw,
			boolean mouseMode, boolean flare, boolean shoot, boolean select,
			boolean openMenu, boolean special, boolean radarMode, boolean bothRoll) {
		this.inputThrottle = throttle;
		this.inputPitch = pitch;
		this.inputRoll = roll;
		this.inputYaw = yaw;
		this.inputFlare = flare;
		this.inputMouseMode = mouseMode;
		if (inputMouseMode) setFreeLook(!isFreeLook());
		this.inputShoot = shoot;
		this.inputSelect = select;
		if (inputSelect && !level.isClientSide) weaponSystem.selectNextWeapon();
		this.inputOpenMenu = openMenu;
		this.inputSpecial = special;
		this.inputRadarMode = radarMode;
		if (inputRadarMode) setRadarPlayersOnly(!isRadarPlayersOnly());
		this.inputBothRoll = bothRoll;
	}
	
	public void resetControls() {
		this.inputThrottle = 0;
		this.inputPitch = 0;
		this.inputRoll = 0;
		this.inputYaw = 0;
		this.inputFlare = false;
		this.inputMouseMode = false;
		this.inputShoot = false;
		this.inputSelect = false;
		this.inputOpenMenu = false;
		this.inputSpecial = false;
		this.inputRadarMode = false;
		this.inputBothRoll = false;
		this.throttleToZero();
	}
	
	public final boolean isFreeLook() {
    	return entityData.get(FREE_LOOK);
    }
    
    public final void setFreeLook(boolean freeLook) {
    	entityData.set(FREE_LOOK, freeLook);
    }
    
    public final boolean isRadarPlayersOnly() {
    	return entityData.get(PLAYERS_ONLY_RADAR);
    }
    
    public final void setRadarPlayersOnly(boolean playersOnly) {
    	entityData.set(PLAYERS_ONLY_RADAR, playersOnly);
    }
	
    /**
     * register parts before calling super in server side
     */
	public void serverSetup() {
		// ORDER MATTERS
		weaponSystem.setup();
		radarSystem.setup();
		partsManager.setupParts();
	}
	
	/**
	 * gets the part manager, weapon system, and radar system data from the server
	 */
	public void clientSetup() {
		UtilClientSafeSoundInstance.aircraftEngineSound(
				Minecraft.getInstance(), this, getEngineSound());
		PacketHandler.INSTANCE.sendToServer(new ToServerRequestPlaneData(getId()));
	}
	
	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (player.isSecondaryUseActive()) {
			return InteractionResult.PASS;
		} else if (player.getRootVehicle() != null && player.getRootVehicle().equals(this)) {
			return InteractionResult.PASS;
		} else if (!level.isClientSide) {
			if (!isOperational()) return InteractionResult.PASS;
			ItemStack stack = player.getInventory().getSelected();
			if (!stack.isEmpty()) {
				Item item = stack.getItem();
				// REFUEL
				if (item instanceof ItemGasCan) {
					int md = stack.getMaxDamage();
					int d = stack.getDamageValue();
					int r = (int)addFuel(md-d);
					stack.setDamageValue(md-r);
					return InteractionResult.SUCCESS;
				// REPAIR
				} else if (item instanceof ItemRepairTool tool) {
					if (isMaxHealth()) {
						level.playSound(null, this, 
								SoundEvents.ANVIL_USE, 
								getSoundSource(), 0.5f, 0.9f);
						return InteractionResult.PASS;
					}
					addHealth(tool.repair);
					stack.hurtAndBreak(1, player, 
						(p) -> { p.broadcastBreakEvent(hand); });
					return InteractionResult.SUCCESS;
				// RELOAD
				} else if (item instanceof ItemAmmo ammo) {
					String ammoId = ammo.getAmmoId();
					for (EntityTurret t : getTurrets()) {
						WeaponData wd = t.getWeaponData();
						if (wd == null) continue;
						if (!wd.getId().equals(ammoId)) continue;
						int o = wd.addAmmo(stack.getCount());
						t.setAmmo(wd.getCurrentAmmo());
						stack.setCount(o);
						if (stack.getCount() == 0) return InteractionResult.SUCCESS;
					}
					int o = weaponSystem.addAmmo(ammoId, stack.getCount(), true);
					stack.setCount(o);
					return InteractionResult.SUCCESS;
				// CHANGE COLOR
				} else if (item instanceof DyeItem dye) {
					if (setCurrentColor(dye.getDyeColor())) {
						stack.shrink(1);
						return InteractionResult.CONSUME;
					}
					return InteractionResult.PASS;
				// CREATIVE WAND
				} else if (item instanceof ItemCreativeWand wand) {
					if (wand.modifyAircraft(this)) return InteractionResult.SUCCESS;
					return InteractionResult.PASS;
				}
			}
			boolean okay = rideAvailableSeat(player);
			return okay ? InteractionResult.CONSUME : InteractionResult.PASS;
		} else if (level.isClientSide) {	
			Minecraft m = Minecraft.getInstance();
			if (m.player.equals(player)) ClientInputEvents.centerMouse();
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.SUCCESS;
	}
	
	private boolean ridePilotSeat(Entity e, List<EntitySeat> seats) {
		for (EntitySeat seat : seats) 
			if (seat.getSlotId().equals(PartSlot.PILOT_SLOT_NAME)) {
				if (e.startRiding(seat)) {
					newRiderCooldown = 10;
					return true;
				} else return false;
			}
		return false;
	}
	
	public boolean rideAvailableSeat(Entity e) {
		List<EntitySeat> seats = getSeats();
		if (ridePilotSeat(e, seats)) return true;
		for (EntitySeat seat : seats) 
			if (e.startRiding(seat)) return true;
		return false;
	}
	
	public boolean switchSeat(Entity e) {
		List<EntitySeat> seats = getSeats();
		int seatIndex = -1;
		for (int i = 0; i < seats.size(); ++i) {
			Player p = seats.get(i).getPlayer();
			if (p == null) continue;
			if (p.equals(e)) {
				seatIndex = i;
				break;
			}
		}
		if (seatIndex == -1) return false; // player not riding seat
		System.out.println("riding seat "+seatIndex);
		int i = 0, j = seatIndex+1;
		while (i < seats.size()-1) {
			if (j >= seats.size()) j = 0;
			if (e.startRiding(seats.get(j))) {
				return true;
			}
			++i; ++j;
		}
		return false;
	}
	
	@Override
    public void positionRider(Entity passenger) {
		if (passenger instanceof EntityPart part) {
			Quaternion q;
			if (level.isClientSide) q = getClientQ();
			else q = getQ();
 			Vec3 seatPos = UtilAngles.rotateVector(part.getRelativePos(), q);
			passenger.setPos(position().add(seatPos));
		}
	}
	
	@Nullable
	@Override
    public Entity getControllingPassenger() {
        for (EntitySeat seat : getSeats()) 
        	if (seat.getSlotId().equals(PartSlot.PILOT_SLOT_NAME)) 
        		return seat.getPlayer();
        return null;
    }
	
	@Override
	public boolean isPickable() {
		return !isRemoved();
	}
	
	@Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
	}
	
	@Override
    protected boolean canAddPassenger(Entity passenger) {
		return passenger instanceof EntityPart;
	}
	
	@Override
    protected boolean canRide(Entity entityIn) {
        return false;
    }
	
	public boolean isVehicleOf(Entity e) {
		if (e == null) return false;
		List<Entity> list = getPassengers();
		if (list.contains(e)) return true;
		for (Entity l : list) {
			if (l instanceof EntitySeat seat) {
				List<Entity> list2 = seat.getPassengers();
				if (list2.contains(e)) return true;
			}
		}
		return false;
	}

	@Override
    public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
        return true;
    }
    
    @Override
    public double getPassengersRidingOffset() {
        return 0;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
	
    @Override
    public boolean isInvulnerableTo(DamageSource source) {
    	if (isTestMode()) return true;
    	if (super.isInvulnerableTo(source)) return true;
    	if (isVehicleOf(source.getEntity())) return true;
    	return false;
    }
    
	@Override
    public boolean hurt(DamageSource source, float amount) {
		if (isInvulnerableTo(source)) return false;
		addHealth(-amount);
		// TODO if damage is explosion, add torque to craft causing it to spin out of control
		if (source.isExplosion() && !isOnGround()) {
			Vec3 delta = source.getSourcePosition().subtract(position());
			
		}
		if (!level.isClientSide && isOperational()) level.playSound(null, 
				blockPosition(), ModSounds.VEHICLE_HIT_1.get(), 
				SoundSource.PLAYERS, 0.5f, 1.0f);
		return true;
	}
	
	public void explode(DamageSource source) {
		if (!level.isClientSide) {
			level.explode(this, source,
					null, getX(), getY(), getZ(), 
					3, true, 
					Explosion.BlockInteraction.BREAK);
		} else {
			level.addParticle(ParticleTypes.LARGE_SMOKE, 
					getX(), getY()+0.5D, getZ(), 
					0.0D, 0.0D, 0.0D);
		}
	}
	
	/**
	 * @return the max speed of the craft along the x and z axis
	 */
    public final float getMaxSpeed() {
    	return entityData.get(MAX_SPEED);
    }
    
    public final void setMaxSpeed(float maxSpeed) {
    	entityData.set(MAX_SPEED, maxSpeed);
    }
    
    /**
     * @return between 1 and 0 or 1 and -1 if negativeThrottle
     */
    public final float getCurrentThrottle() {
    	return entityData.get(THROTTLE);
    }
    
    public final void setCurrentThrottle(float throttle) {
    	if (throttle > 1) throttle = 1;
    	else if (negativeThrottle && throttle < -1) throttle = -1;
    	else if (!negativeThrottle && throttle < 0) throttle = 0;
    	entityData.set(THROTTLE, throttle);
    }
    
    public void throttleToZero() {
    	float th = getCurrentThrottle();
    	if (th == 0) return;
    	float r = getThrottleDecreaseRate();
    	th -= r * Math.signum(th);
    	if (Math.abs(th) < r) th = 0;
    	setCurrentThrottle(th);
    }
    
    public final float getThrottleIncreaseRate() {
    	return entityData.get(THROTTLEUP);
    }
    
    public final void setThrottleIncreaseRate(float value) {
    	if (value < 0) value = 0;
    	entityData.set(THROTTLEUP, value);
    }
    
    public final float getThrottleDecreaseRate() {
    	return entityData.get(THROTTLEDOWN);
    }
    
    public final void setThrottleDecreaseRate(float value) {
    	if (value < 0) value = 0;
    	entityData.set(THROTTLEDOWN, value);
    }
    
    public final float getMaxDeltaPitch() {
    	return entityData.get(MAX_PITCH);
    }
    
    public final void setMaxDeltaPitch(float degrees) {
    	if (degrees < 0) degrees = 0;
    	entityData.set(MAX_PITCH, degrees);
    }
    
    public final float getMaxDeltaYaw() {
    	return entityData.get(MAX_YAW);
    }
    
    public final void setMaxDeltaYaw(float degrees) {
    	if (degrees < 0) degrees = 0;
    	entityData.set(MAX_YAW, degrees);
    }
    
    public final float getMaxDeltaRoll() {
    	return entityData.get(MAX_ROLL);
    }
    
    public final void setMaxDeltaRoll(float degrees) {
    	if (degrees < 0) degrees = 0;
    	entityData.set(MAX_ROLL, degrees);
    }
    
    public final float getAccelerationPitch() {
    	return entityData.get(ACC_PITCH);
    }
    
    public final void setAccelerationPitch(float degrees) {
    	if (degrees < 0) degrees = 0;
    	entityData.set(ACC_PITCH, degrees);
    }
    
    public final float getAccelerationYaw() {
    	return entityData.get(ACC_YAW);
    }
    
    public final void setAccelerationYaw(float degrees) {
    	if (degrees < 0) degrees = 0;
    	entityData.set(ACC_YAW, degrees);
    }
    
    public final float getAccelerationRoll() {
    	return entityData.get(ACC_ROLL);
    }
    
    public final void setAccelerationRoll(float degrees) {
    	if (degrees < 0) degrees = 0;
    	entityData.set(ACC_ROLL, degrees);
    }
    
    public void increaseThrottle() {
    	setCurrentThrottle(getCurrentThrottle() + getThrottleIncreaseRate());
    }
    
    public void decreaseThrottle() {
    	setCurrentThrottle(getCurrentThrottle() - getThrottleDecreaseRate());
    }
    
    /**
     * @return server side quaternion
     */
    public final Quaternion getQ() {
        return entityData.get(Q).copy();
    }
    
    /**
     * @param q set server side quaternion
     */
    public final void setQ(Quaternion q) {
        entityData.set(Q, q.copy());
    }
    
    /**
     * @return the client side rotation
     */
    public final Quaternion getClientQ() {
        return clientQ.copy();
    }
    
    /**
     * @param q set client side rotation
     */
    public final void setClientQ(Quaternion q) {
        clientQ = q.copy();
    }
    
    /**
     * @return the rotation on the previous tick for both client and server side
     */
    public final Quaternion getPrevQ() {
        return prevQ.copy();
    }
    
    /**
     * @param q the rotation for both client and server side
     */
    public final void setPrevQ(Quaternion q) {
        prevQ = q.copy();
    }
    
    /**
     * 1 is no stealth
     * 0 is invisible
     * 0.5 is a radar with a range of 1000 can only see this craft within 500
     * @return value to be multiplied to the range of a radar
     */
    public final float getStealth() {
    	return entityData.get(STEALTH);
    }
    
    /**
     * 1 is no stealth
     * 0 is invisible
     * 0.5 is a radar with a range of 1000 can only see this craft within 500
     * @param stealth value to be multiplied to the range of a radar
     */
    public final void setStealth(float stealth) {
    	if (stealth < 0) stealth = 0;
    	entityData.set(STEALTH, stealth);
    }
    
    public ResourceLocation getTexture() {
    	return currentTexture;
    }
    
    public SoundEvent getEngineSound() {
    	return engineSound.get();
    }
    
    /**
     * @return the item stack with all of this plane's data 
     */
    public ItemStack getItem() {
    	ItemStack stack = new ItemStack(item.get());
    	CompoundTag tag = new CompoundTag();
    	addAdditionalSaveData(tag);
    	CompoundTag eTag = new CompoundTag();
    	eTag.put("EntityTag", tag);
    	stack.setTag(eTag);
    	return stack;
    }
    
    @Override
	public boolean shouldRenderAtSqrDistance(double dist) {
		return dist < 65536;
	}
    
    public final void setMaxHealth(float h) {
    	if (h < 0) h = 0;
    	entityData.set(MAX_HEALTH, h);
    }
    
    public final float getMaxHealth() {
    	return entityData.get(MAX_HEALTH);
    }
    
    public final void addHealth(float h) {
    	setHealth(getHealth()+h);
    }
    
    public final void setHealth(float h) {
    	float max = getMaxHealth();
    	if (h > max) h = max;
    	else if (h < 0) h = 0;
    	entityData.set(HEALTH, h);
    }
    
    public final float getHealth() {
    	return entityData.get(HEALTH);
    }
    
    public boolean isMaxHealth() {
    	return getHealth() >= getMaxHealth();
    }
    
    /**
     * divide this by distance squared when ir missile compares this heat value with others
     * @return the total heat value
     */
    public float getHeat() {
    	return getIdleHeat() + Math.abs(getCurrentThrottle()) * getEngineHeat();
    }
    
    /**
     * @return the heat value this aircraft always emits 
     */
    public final float getIdleHeat() {
    	return entityData.get(IDLEHEAT);
    }
    
    /**
     * @param heat the heat value this aircraft always emits 
     */
    public final void setIdleHeat(float heat) {
    	entityData.set(IDLEHEAT, heat);
    }
    
    /**
     * @return the heat this plane's engines emit at max throttle 
     */
    public float getEngineHeat() {
    	return partsManager.getTotalEngineHeat();
    }
    
    public List<Player> getRidingPlayers() {
    	List<Player> players = new ArrayList<>();
    	for (EntitySeat seat : getSeats()) {
    		Player p = seat.getPlayer();
			if (p != null) players.add(p); 
    	}
    	return players;
    }
    
    public List<EntitySeat> getSeats() {
    	List<EntitySeat> seats = new ArrayList<>();
    	for (Entity e : getPassengers())
    		if (e instanceof EntitySeat seat) 
    			seats.add(seat);
    	return seats;
    }
    
    public List<EntityTurret> getTurrets() {
    	List<EntityTurret> turrets = new ArrayList<>();
    	for (Entity e : getPassengers())
    		if (e instanceof EntityTurret turret)
    			turrets.add(turret);
    	return turrets;
    }
    
    public List<EntityPart> getPartEntities() {
    	List<EntityPart> parts = new ArrayList<>();
    	for (Entity e : getPassengers())
    		if (e instanceof EntityPart part)
    			parts.add(part);
    	return parts;
    }
    
    /**
     * plays all the sounds for the players in the plane
     */
    public void sounds() {
    	if (level.isClientSide) {
    		if (tickCount % 4 == 0 && radarSystem.isTrackedByMissile()) for (Player p : getRidingPlayers()) {
    			level.playSound(p, new BlockPos(p.position()), 
	    			ModSounds.MISSILE_WARNING.get(), SoundSource.PLAYERS, 1f, 1f);
    		} else if (tickCount % 8 == 0 && radarSystem.isTrackedByRadar()) for (Player p : getRidingPlayers()) {
    			level.playSound(p, new BlockPos(p.position()), 
    	    		ModSounds.GETTING_LOCKED.get(), SoundSource.PLAYERS, 1f, 1f);
        	}
    	}
    } 
    
    /**
     * entity tracking missile calls this server side when tracking this plane
     * @param pos the position of the missile
     */
    public void trackedByMissile(Vec3 pos) {
    	radarSystem.addRWRWarning(pos, true, level.isClientSide);
    }
    
    /**
     * another radar system calls this server side when tracking this craft
     * @param pos the position of the radar
     */
    public void lockedOnto(Vec3 pos) {
    	radarSystem.addRWRWarning(pos, false, level.isClientSide);
    }
    
    @Override
    protected AABB makeBoundingBox() {
    	if (isCustomBoundingBox()) return makeCustomBoundingBox();
     	return super.makeBoundingBox();
    }
    
    protected AABB makeCustomBoundingBox() {
    	double pX = getX(), pY = getY(), pZ = getZ();
    	EntityDimensions d = getDimensions(getPose());
    	float f = d.width / 2.0F;
        float f1 = d.height / 2.0F;
        return new AABB(pX-(double)f, pY-(double)f1, pZ-(double)f, 
        		pX+(double)f, pY+(double)f1, pZ+(double)f);
    }
    
    public boolean isCustomBoundingBox() {
    	return false;
    }
    
    /**
     * @return the total fuel this aircraft can hold
     */
    public float getMaxFuel() {
    	return maxFuel;
    }
    
    /**
     * consume fuel every server tick
     */
    public void tickFuel() {
    	partsManager.tickFuel(!level.isClientSide);
    }
    
    /**
     * @return the current amount of fuel left
     */
    public float getCurrentFuel() {
    	return currentFuel;
    }
    
    /**
     * @param fuel
     * @return the left over fuel
     */
    public float addFuel(float fuel) {
    	return partsManager.addFuel(fuel);
    }
    
    /**
     * @return true if landing gear is out false if folded
     */
    public boolean isLandingGear() {
    	return entityData.get(LANDING_GEAR);
    }
    
    /**
     * @param gear true if landing gear is out false if folded
     */
    public void setLandingGear(boolean gear) {
    	entityData.set(LANDING_GEAR, gear);
    }
    
    public boolean isTestMode() {
    	return entityData.get(TEST_MODE);
    }
    
    public void setTestMode(boolean testMode) {
    	entityData.set(TEST_MODE, testMode);
    }
    
    public void toggleLandingGear() {
    	setLandingGear(!isLandingGear());
    }
    
    /**
     * update landingGearPos between 0 and 1 so the model knows where in the animation the landing gear is
     */
    public void tickClientLandingGear() {
    	landingGearPosOld = landingGearPos;
    	if (isLandingGear()) {
    		if (landingGearPos > 0f) landingGearPos -= 0.02f;
    		else if (landingGearPos < 0f) landingGearPos = 0f;
    	} else {
    		if (landingGearPos < 1f) landingGearPos += 0.02f;
    		else if (landingGearPos > 1f) landingGearPos = 1f;
    	}
    }
    
    /**
     * @param partialTicks
     * @return 0 (landing gear out) 1 (landing gear folded)
     */
    public float getLandingGearPos(float partialTicks) {
		return Mth.lerp(partialTicks, landingGearPosOld, landingGearPos);
	}
    
    @Override
    public void kill() {
    	super.kill();
    }
    
    public int getCurrentColorId() {
    	return entityData.get(CURRRENT_DYE_ID);
    }
    
    public boolean setCurrentColor(DyeColor color) {
    	int id = color.getId();
    	if (id == getCurrentColorId()) return false;
    	if (!textures.hasTexture(id)) return false;
    	entityData.set(CURRRENT_DYE_ID, id);
    	return true;
    }
    
    public final float getTurnRadius() {
    	return entityData.get(TURN_RADIUS);
    }
    
    public final void setTurnRadius(float turn) {
    	entityData.set(TURN_RADIUS, turn);
    }
    
    @Override
	public float getStepHeight() {
		return 0.6f;
	}
    
    public boolean isNoConsume() {
    	return entityData.get(NO_CONSUME);
    }
    
    public void setNoConsume(boolean noConsume) {
    	entityData.set(NO_CONSUME, noConsume);
    }
    
    @Override
    public boolean isPushable() {
    	return false;
    }
    
    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
		return super.getDismountLocationForPassenger(livingEntity);
	}
    
    public int getFlareNum() {
    	return flareNum;
    }
    
    protected void debug(String debug) {
    	if (hasControllingPassenger()) System.out.println(debug);
    }
    
    public void toClientPassengers(IPacket packet) {
    	if (level.isClientSide) return;
    	for (Player p : getRidingPlayers()) {
    		PacketHandler.INSTANCE.send(
    			PacketDistributor.PLAYER.with(() -> (ServerPlayer)p),
    			packet);
    	}
    }
    
}
