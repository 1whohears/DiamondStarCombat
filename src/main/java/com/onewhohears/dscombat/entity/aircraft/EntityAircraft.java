package com.onewhohears.dscombat.entity.aircraft;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.event.forgebus.ClientInputEvents;
import com.onewhohears.dscombat.common.container.AircraftMenuContainer;
import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientAddForceMoment;
import com.onewhohears.dscombat.common.network.toclient.ToClientAircraftControl;
import com.onewhohears.dscombat.common.network.toserver.ToServerAircraftCollide;
import com.onewhohears.dscombat.common.network.toserver.ToServerAircraftMoveRot;
import com.onewhohears.dscombat.common.network.toserver.ToServerAircraftThrottle;
import com.onewhohears.dscombat.data.aircraft.AircraftClientPreset;
import com.onewhohears.dscombat.data.aircraft.AircraftClientPresets;
import com.onewhohears.dscombat.data.aircraft.AircraftInputs;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.aircraft.AircraftPresets;
import com.onewhohears.dscombat.data.aircraft.AircraftTextures;
import com.onewhohears.dscombat.data.damagesource.AircraftDamageSource;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.PartsManager;
import com.onewhohears.dscombat.data.radar.RadarData.RadarMode;
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
import com.onewhohears.dscombat.util.UtilParse;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilAngles.EulerAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.NameTagItem;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.scores.Team;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.RegistryObject;

/**
 * the parent class for all vehicle entities in this mod
 * @author 1whohears
 */
public abstract class EntityAircraft extends Entity implements IEntityAdditionalSpawnData {
	
	public static final EntityDataAccessor<Float> MAX_HEALTH = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> MAX_SPEED = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> THROTTLE = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> THROTTLEUP = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> THROTTLEDOWN = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Quaternion> Q = SynchedEntityData.defineId(EntityAircraft.class, DataSerializers.QUATERNION);
	public static final EntityDataAccessor<Vec3> AV = SynchedEntityData.defineId(EntityAircraft.class, DataSerializers.VEC3);
	public static final EntityDataAccessor<Float> STEALTH = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> MAX_ROLL = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> MAX_PITCH = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> MAX_YAW = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> TURN_RADIUS = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> ROLL_TORQUE = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> PITCH_TORQUE = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> YAW_TORQUE = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> IDLEHEAT = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> MASS = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Boolean> TEST_MODE = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Integer> CURRRENT_DYE_ID = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> NO_CONSUME = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Integer> FLARE_NUM = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<String> RADIO_SONG = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Float> CROSS_SEC_AREA = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Boolean> PLAY_IR_TONE = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Float> BASE_ARMOR = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	
	public final AircraftInputs inputs = new AircraftInputs();
	public final PartsManager partsManager = new PartsManager(this);
	public final WeaponSystem weaponSystem = new WeaponSystem(this);
	public final RadarSystem radarSystem = new RadarSystem(this);
	
	public final double ACC_GRAVITY = Config.SERVER.accGravity.get();
	public final double CO_DRAG = Config.SERVER.coDrag.get();
	public final double CO_STATIC_FRICTION = Config.SERVER.coStaticFriction.get();
	public final double CO_KINETIC_FRICTION = Config.SERVER.coKineticFriction.get();
	public final double collideSpeedThreshHold = Config.SERVER.collideSpeedThreshHold.get();
	public final double collideSpeedWithGearThreshHold = Config.SERVER.collideSpeedWithGearThreshHold.get();
	public final double collideDamageRate = Config.SERVER.collideDamageRate.get();
	public final double maxFallSpeed = Config.SERVER.maxFallSpeed.get();
	public final boolean autoDataLink = Config.COMMON.autoDataLink.get();
	
	public final String defaultPreset;
	public final boolean negativeThrottle;
	public final float Ix, Iy, Iz, explodeSize;
	public final double camDist;
	
	protected final RegistryObject<SoundEvent> engineSound;
	
	/**
	 * this vehicle's original preset. 
	 * will be {@link defaultPreset} if it's not defined in its NBT.
	 * synched with client.  
	 */
	public String preset;
	protected ItemStack item;
	/**
	 * CLIENT ONLY
	 */
	public AircraftTextures textures;
	/**
	 * CLIENT ONLY
	 */
	private ResourceLocation currentTexture;
	
	/**
	 * SERVER ONLY
	 */
	public int lastShootTime = -1;
	
	public Quaternion prevQ = Quaternion.ONE.copy();
	public Quaternion clientQ = Quaternion.ONE.copy();
	public Vec3 clientAV = Vec3.ZERO;
	public float clientThrottle;
	
	public float zRot, zRotO; 
	public Vec3 prevMotion = Vec3.ZERO;
	public Vec3 forces = Vec3.ZERO, forcesO = Vec3.ZERO, addForceBetweenTicks = Vec3.ZERO;
	public Vec3 moment = Vec3.ZERO, momentO = Vec3.ZERO, addMomentBetweenTicks = Vec3.ZERO;
	
	public boolean nightVisionHud = false, hasRadio = false;
	
	protected boolean hasFlares;
	protected int xzSpeedDir, hurtByFireTime;
	protected float xzSpeed, totalMass, xzYaw, slideAngle, slideAngleCos, maxPushThrust, maxSpinThrust, currentFuel, maxFuel;
	protected double staticFric, kineticFric, airPressure;
	
	private int lerpSteps, lerpStepsQ, deadTicks;
	private double lerpX, lerpY, lerpZ, lerpXRot, lerpYRot;
	private float landingGearPos, landingGearPosOld;
	
	protected RadarMode radarMode = RadarMode.ALL;
	protected boolean isLandingGear, isFreeLook = true;
	
	// FIXME 1.1 fix aircraft texture/variant texture system
	// TODO 5.1 custom hit box system so players can walk on boats
	// TODO 5.2 allow big boats to have a heli pad and runway
	// TODO 5.4 aircraft breaks apart when damaged
	// TODO 5.6 place and remove external parts from outside the vehicle
	// TODO 2.5 add chaff 
	// FIXME refactor EntityAircraft to EntityVehicle
	
	public EntityAircraft(EntityType<? extends EntityAircraft> entityType, Level level, 
			AircraftPreset defaultPreset, RegistryObject<SoundEvent> engineSound,
			boolean negativeThrottle, float Ix, float Iy, float Iz, 
			float explodeSize, double camDist) {
		super(entityType, level);
		this.defaultPreset = defaultPreset.getId();
		this.preset = this.defaultPreset;
		if (level.isClientSide) {
			AircraftClientPreset acp = AircraftClientPresets.get().getPreset(this.defaultPreset);
			this.textures = acp.getAircraftTextures();
			this.currentTexture = textures.getTexture(defaultPreset.getDefaultPaintJob());
		}
		this.item = defaultPreset.getItem();
		this.engineSound = engineSound;
		this.negativeThrottle = negativeThrottle;
		this.Ix = Ix;
		this.Iy = Iy;
		this.Iz = Iz;
		this.explodeSize = explodeSize;
		this.camDist = camDist;
		this.blocksBuilding = true;
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
		entityData.define(AV, Vec3.ZERO);
		entityData.define(STEALTH, 1f);
		entityData.define(MAX_ROLL, 1f);
		entityData.define(MAX_PITCH, 1f);
		entityData.define(MAX_YAW, 1f);
		entityData.define(ROLL_TORQUE, 1f);
		entityData.define(PITCH_TORQUE, 1f);
		entityData.define(YAW_TORQUE, 1f);
		entityData.define(IDLEHEAT, 1f);
		entityData.define(MASS, 1f);
		entityData.define(TEST_MODE, false);
		entityData.define(CURRRENT_DYE_ID, -1);
		entityData.define(TURN_RADIUS, 0f);
		entityData.define(NO_CONSUME, false);
		entityData.define(FLARE_NUM, 0);
		entityData.define(RADIO_SONG, "");
		entityData.define(CROSS_SEC_AREA, 1f);
		entityData.define(PLAY_IR_TONE, false);
		entityData.define(BASE_ARMOR, 0f);
	}
	
	@Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        // if this entity is on the client side and receiving the quaternion of the plane from the server 
        if (!level.isClientSide()) return;
        if (Q.equals(key)) {
    		if (!isControlledByLocalInstance()) {
    			setPrevQ(getClientQ());
    			setClientQ(getQ());
    		}
    	} else if (AV.equals(key)) {
    		if (!isControlledByLocalInstance()) {
    			clientAV = entityData.get(AV);
    		} 
    	} else if (THROTTLE.equals(key)) {
    		if (!firstTick && isControlledByLocalInstance()) {
    			PacketHandler.INSTANCE.sendToServer(new ToServerAircraftThrottle(this));
    		} else clientThrottle = entityData.get(THROTTLE);
    	} else if (CURRRENT_DYE_ID.equals(key)) {
    		currentTexture = textures.getTexture(getCurrentColorId());
    	} else if (RADIO_SONG.equals(key)) {
    		String sound = getRadioSong();
    		//System.out.println("SONG UPDATE "+sound);
    		if (!sound.isEmpty()) UtilClientSafeSoundInstance.aircraftRadio(
    				Minecraft.getInstance(), this, sound);
    	}
    }
	
	/**
	 * if this is a brand new entity and has no nbt custom data then the fresh entity nbt will
	 * merge with this vehicle's preset nbt. see {@link AircraftPresets}.
	 * you could summon a vehicle with nbt {preset:"some preset name"} to override the {@link EntityAircraft#defaultPreset}
 	 */
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		// ORDER MATTERS
		setTestMode(nbt.getBoolean("test_mode"));
		setNoConsume(nbt.getBoolean("no_consume"));
		int color = -1;
		if (nbt.contains("dyecolor")) color = nbt.getInt("dyecolor");
		else if (nbt.contains("paintjob_color")) color = nbt.getInt("paintjob_color");
		// check if preset was defined
		preset = nbt.getString("preset");
		// if not use the default preset
		if (preset.isEmpty()) preset = defaultPreset;
		// check if the preset exists
		else if (!AircraftPresets.get().has(preset)) {
			preset = defaultPreset;
			System.out.println("ERROR: preset "+preset+" doesn't exist!");
		}
		// get the preset data
		AircraftPreset ap = AircraftPresets.get().getPreset(preset);
		item = ap.getItem();
		CompoundTag presetNbt = ap.getDataAsNBT();
		// merge if this entity hasn't merged yet
		if (!nbt.getBoolean("merged_preset")) nbt.merge(presetNbt);
		partsManager.read(nbt, presetNbt);
		setMaxSpeed(nbt.getFloat("max_speed"));
		setMaxHealth(nbt.getFloat("max_health"));
		setHealth(nbt.getFloat("health"));
		setStealth(nbt.getFloat("stealth"));
		setBaseCrossSecArea(UtilParse.fixFloatNbt(nbt, "cross_sec_area", presetNbt, 1));
		setTurnRadius(nbt.getFloat("turn_radius"));
		setMaxDeltaRoll(nbt.getFloat("maxroll"));
		setMaxDeltaPitch(nbt.getFloat("maxpitch"));
		setMaxDeltaYaw(nbt.getFloat("maxyaw"));
		setRollTorque(UtilParse.fixFloatNbt(nbt, "rolltorque", presetNbt, 1));
		setPitchTorque(UtilParse.fixFloatNbt(nbt, "pitchtorque", presetNbt, 1));
		setYawTorque(UtilParse.fixFloatNbt(nbt, "yawtorque", presetNbt, 1));
		setThrottleIncreaseRate(nbt.getFloat("throttleup"));
		setThrottleDecreaseRate(nbt.getFloat("throttledown"));
		setIdleHeat(nbt.getFloat("idleheat"));
		setAircraftMass(UtilParse.fixFloatNbt(nbt, "mass", presetNbt, 1));
		setBaseArmor(UtilParse.fixFloatNbt(nbt, "base_armor", presetNbt, 0));
		setLandingGear(nbt.getBoolean("landing_gear"));
		setCurrentThrottle(nbt.getFloat("current_throttle"));
		setXRot(nbt.getFloat("xRot"));
		setYRot(nbt.getFloat("yRot"));
		zRot = nbt.getFloat("zRot");
		Quaternion q = UtilAngles.toQuaternion(getYRot(), getXRot(), zRot);
		setQ(q);
		setPrevQ(q);
		setClientQ(q);
		if (color == -1) color = nbt.getInt("paintjob_color");
		setCurrentColor(DyeColor.byId(color));
		setRadarMode(RadarMode.values()[nbt.getInt("radar_mode")]);
		setRadioSong(nbt.getString("radio_song"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
		nbt.putString("preset", preset);
		nbt.putBoolean("test_mode", isTestMode());
		nbt.putBoolean("no_consume", isNoConsume());
		nbt.putBoolean("merged_preset", true);
		partsManager.write(nbt);
		nbt.putFloat("max_speed", getMaxSpeed());
		nbt.putFloat("max_health", getMaxHealth());
		nbt.putFloat("health", getHealth());
		nbt.putFloat("stealth", getStealth());
		nbt.putFloat("cross_sec_area", getBaseCrossSecArea());
		nbt.putFloat("turn_radius", getTurnRadius());
		nbt.putFloat("maxroll", getMaxDeltaRoll());
		nbt.putFloat("maxpitch", getMaxDeltaPitch());
		nbt.putFloat("maxyaw", getMaxDeltaYaw());
		nbt.putFloat("rolltorque", getRollTorque());
		nbt.putFloat("pitchtorque", getPitchTorque());
		nbt.putFloat("yawtorque", getYawTorque());
		nbt.putFloat("throttleup", getThrottleIncreaseRate());
		nbt.putFloat("throttledown", getThrottleDecreaseRate());
		nbt.putFloat("idleheat", getIdleHeat());
		nbt.putFloat("mass", getAircraftMass());
		nbt.putFloat("base_armor", getBaseArmor());
		nbt.putBoolean("landing_gear", isLandingGear());
		nbt.putFloat("current_throttle", getCurrentThrottle());
		nbt.putFloat("xRot", getXRot());
		nbt.putFloat("yRot", getYRot());
		nbt.putFloat("zRot", zRot);
		nbt.putInt("paintjob_color", getCurrentColorId());
		nbt.putInt("radar_mode", getRadarMode().ordinal());
		nbt.putString("radio_song", getRadioSong());
		Entity c = getControllingPassenger();
		if (c != null) nbt.putString("owner", c.getScoreboardName());
		Component name = getCustomName();
        if (name != null) nbt.putString("CustomName", Component.Serializer.toJson(name));
        if (isCustomNameVisible()) nbt.putBoolean("CustomNameVisible", isCustomNameVisible());
        nbt.putFloat("fuel", getCurrentFuel());
        nbt.putFloat("flares", getFlareNum());
	}
	
	public static enum AircraftType {
		PLANE(true, false, false),
		HELICOPTER(true, false, true),
		CAR(false, true, true),
		BOAT(false, true, true),
		SUBMARINE(false, true, true);
		
		public final boolean isAircraft, flipPitchThrottle, ignoreInvertY;
		
		private AircraftType(boolean isAircraft, boolean flipPitchThrottle, boolean ignoreInvertY) {
			this.isAircraft = isAircraft;
			this.flipPitchThrottle = flipPitchThrottle;
			this.ignoreInvertY = ignoreInvertY;
		}
		
		public boolean isTank() {
			return this == CAR;
		}
		
		public boolean isHeli() {
			return this == HELICOPTER;
		}
		
		public boolean isPlane() {
			return this == PLANE;
		}
		
		public boolean isBoat() {
			return this == BOAT || this == SUBMARINE;
		}
	}
	
	public abstract AircraftType getAircraftType();
	
	public boolean isAircraft() {
		return getAircraftType().isAircraft;
	}
	
	/**
	 * called on this entities first tick on client and server side
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
		if (UtilGeometry.vec3NAN(getDeltaMovement())) setDeltaMovement(Vec3.ZERO);
		if (firstTick) init(); // MUST BE CALLED BEFORE SUPER
		super.tick();
		// SET PREV/OLD
		prevMotion = getDeltaMovement();
		forcesO = getForces();
		momentO = getMoment();
		setForces(Vec3.ZERO);
		setMoment(Vec3.ZERO);
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
		tickThrottle();
		if (!isTestMode()) {
			calcMoveStatsPre(q);
			tickMovement(q);
			calcAcc();
			motionClamp();
			move(MoverType.SELF, getDeltaMovement());
			calcMoveStatsPost(q);
			tickCollisions();
		}
		tickLerp();
        // OTHER
		controlSystem();
        tickParts();
		sounds();
		if (level.isClientSide) clientTick();
		else serverTick();
	}
	
	/**
	 * called on server side every tick
	 */
	public void serverTick() {
		waterDamage();
		if (!isTestMode() && !isOperational()) tickNoHealth();
		if (hasRadioSong() && (!isOperational() || !hasRadio)) turnRadioOff();
	}
	
	/**
	 * SERVER SIDE ONLY
	 * called externally to synch a vehicle's inputs with client's that aren't controlling this vehicle.
	 * normally used by {@link com.onewhohears.dscombat.common.network.toserver.ToServerAircraftControl}
	 * which is called by {@link com.onewhohears.dscombat.client.event.forgebus.ClientInputEvents}
	 * to the controlling player can synch their inputs with every other client.
	 * could also be used by a server side AI to synch a vehicle's inputs with all client's.
	 */
	public void synchControlsToClient() {
		if (level.isClientSide) return;
		PacketHandler.INSTANCE.send(
			PacketDistributor.TRACKING_ENTITY.with(() -> this), 
			new ToClientAircraftControl(this));
	}
	
	/**
	 * called on both client and server side
	 * damages plane if it falls or collides with a wall at speeds defined in config.
	 */
	public void tickCollisions() {
		// TODO 9.1 break grass and leaves or just weak blocks when driving through them
		if (!level.isClientSide) {
			if (!hasControllingPassenger()) wallCollisions();
			if (xzSpeed < 0.1) ride(level.getEntities(this, 
					getBoundingBox().inflate(0.1), 
					getRidePredicate()));
			knockBack(level.getEntities(this, 
					getBoundingBox(), 
					getKnockbackPredicate()));
		} else {
			if (isControlledByLocalInstance()) wallCollisions();
		}
	}
	
	protected void wallCollisions() {
		if (verticalCollision) {
			double my = Math.abs(prevMotion.y);
			double th = collideSpeedThreshHold;
			if (isOperational() && isLandingGear() 
					&& Mth.abs(getXRot()) < 15f
					&& Mth.abs(zRot) < 15f) {
				th = collideSpeedWithGearThreshHold;
			}
			if (my > th) {
				float amount = (float)((my-th)*collideDamageRate);
				collideHurt(amount, true);
			}
		}
		if (horizontalCollision && !minorHorizontalCollision) {
			double speed = prevMotion.horizontalDistance();
			if (speed > collideSpeedThreshHold) {
				float amount = (float)((speed-collideSpeedThreshHold)*collideDamageRate);
				collideHurt(amount, false);
			}
		}
	}
	
	/**
	 * hurt the vehicle if there is a collision
	 * @param amount amount of damage done by collision
	 * @param isFall true if vertical collision, false if horizontal
	 */
	public void collideHurt(float amount, boolean isFall) {
		if (!level.isClientSide) {
			if (tickCount < 200) return;
			if (isFall) hurt(DamageSource.FALL, amount);
			else hurt(DamageSource.FLY_INTO_WALL, amount);
		} else {
			PacketHandler.INSTANCE.sendToServer(
				new ToServerAircraftCollide(getId(), amount, isFall));
		}
	}
	
	protected Predicate<? super Entity> getKnockbackPredicate() {
		return ((entity) -> {
			if (this.equals(entity.getRootVehicle())) return false;
			if (entity.isSpectator()) return false;
			if (!(entity instanceof LivingEntity)) return false;
			if (entity instanceof Player p && p.isCreative()) return false;
			return true;
		});
	}
	
	protected void knockBack(List<Entity> entities) {
		double push_factor = 10 * xzSpeed;
		if (push_factor < 2) push_factor = 2;
		double d0 = getBoundingBox().getCenter().x;
		double d1 = getBoundingBox().getCenter().z;
		for(Entity entity : entities) {
			double d2 = entity.getX() - d0;
			double d3 = entity.getZ() - d1;
			double d4 = Math.max(d2 * d2 + d3 * d3, 0.1);
			entity.push(d2/d4*push_factor, 0.2, d3/d4*push_factor);
			if (push_factor > 2) {
				entity.hurt(AircraftDamageSource.roadKill(this), (float)push_factor*2f);
			}
		}
	}
	
	protected Predicate<? super Entity> getRidePredicate() {
		return ((entity) -> {
			if (this.equals(entity.getRootVehicle())) return false;
			if (entity.isSpectator()) return false;
			if (!(entity instanceof Mob)) return false;
			return true;
		});
	}
	
	protected void ride(List<Entity> entities) {
		for(Entity entity : entities) ridePassengerSeat(entity);
	}
	
	/**
	 * called on server tick.
	 * damages the vehicle if in water.
	 */
	public void waterDamage() {
		if (tickCount % 20 == 0 && isInWater() && isOperational()) 
			hurt(DamageSource.DROWN, 5);
	}
	
	/**
	 * called on server tick if health = 0.
	 * removed the entity if the vehicle has no health for 500 ticks or 25 seconds
	 */
	public void tickNoHealth() {
		if (deadTicks++ > 400) {
			dropInventory();
			kill();
			return;
		}
	}
	
	/**
	 * @return if health is greater than 0
	 */
	public boolean isOperational() {
		return getHealth() > 0;
	}
	
	public int getDeadTicks() {
		return deadTicks;
	}
	
	/**
	 * called on client side every tick.
	 */
	public void clientTick() {
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
	 * called every tick after movement is calculated.
	 * restricts the motion of the craft based on max horizontal speed and max vertical speed.
	 */
	public void motionClamp() {
		Vec3 move = getDeltaMovement();
		double maxXZ = getMaxSpeedForMotion();
		
		Vec3 motionXZ = new Vec3(move.x, 0, move.z);
		double velXZ = motionXZ.length();
		if (velXZ > maxXZ) motionXZ = motionXZ.scale(maxXZ / velXZ);
		
		double maxY = maxFallSpeed;
		double my = move.y;
		if (Math.abs(my) > maxY) my = maxY * Math.signum(my);
		else if (Math.abs(my) < 0.001) my = 0;
		
		if (onGround && my < 0) my = -0.01; // THIS MUST BE BELOW ZERO
		setDeltaMovement(motionXZ.x, my, motionXZ.z);
	}
	
	/**
	 * @return the max speed {@link EntityAircraft#motionClamp} tests for.
	 */
	public double getMaxSpeedForMotion() {
		return getMaxSpeed();
	}
	
	/**
	 * called on every tick.
	 * changes this craft's throttle every tick based on inputThrottle.
	 * also resets the controls if there isn't a controlling passenger.
	 */
	public void tickThrottle() {
		if (currentFuel <= 0) {
			throttleToZero();
			return;
		}
		if (getControllingPassenger() == null || !isOperational()) {
			resetControls();
			return;
		}
		if (inputs.throttle > 0) increaseThrottle();
		else if (inputs.throttle < 0) decreaseThrottle();
	}
	
	/**
	 * called every tick.
	 * reads player input to change the direction of the craft.
	 * also calls {@link EntityAircraft#directionGround(Quaternion)}, {@link EntityAircraft#directionWater(Quaternion)},
	 * and {@link EntityAircraft#directionAir(Quaternion)} to change direction based on the named conditions.
	 * @param q the current direction of the vehicle
	 */
	public void controlDirection(Quaternion q) {
		if (onGround) directionGround(q);
		else if (isInWater()) directionWater(q);
		else directionAir(q);
		Vec3 m = getMoment().add(addMomentBetweenTicks), av = getAngularVel();
		if (!UtilGeometry.isZero(m)) {
			av = av.add(m.x/Ix, m.y/Iy, m.z/Iz);
			setAngularVel(av);
		}
		q.mul(Vector3f.XN.rotationDegrees((float)av.x));
		q.mul(Vector3f.YN.rotationDegrees((float)av.y));
		q.mul(Vector3f.ZP.rotationDegrees((float)av.z));
		applyAngularDrag();
		addMomentBetweenTicks = Vec3.ZERO;
	}
	
	public void applyAngularDrag() {
		Vec3 av = getAngularVel();
		float d = getAngularDrag();
		float dx = d, dy = d, dz = d;
		if (!onGround) {
			if (inputs.pitch != 0) dx = 0;
			if (inputs.yaw != 0) dy = 0;
			if (inputs.roll != 0) dz = 0;
		}
		setAngularVel(new Vec3(
				getADComponent(av.x, dx, Ix),
				getADComponent(av.y, dy, Iy),
				getADComponent(av.z, dz, Iz)));
	}
	
	private double getADComponent(double v, float d, float I) {
		double a = Math.abs(v) - d/I;
		if (a < 0) return 0;
		return a * Math.signum(v);
	}
	
	protected float getAngularDrag() {
		if (onGround) return 2.5f;
		else if (isInWater()) return 1.5f;
		else return 1.0f;
 	}
	
	/**
	 * called every tick to change the vehicle direction if on the ground. 
	 * @param q the current direction of the vehicle
	 */
	public void directionGround(Quaternion q) {
		if (!isOperational()) return;
		flatten(q, 4f, 4f, true);
		float max_tr = getTurnRadius();
		Vec3 av = getAngularVel();
		if (inputs.yaw == 0 || max_tr == 0) {
			if (!isSliding()) setAngularVel(av.multiply(1, 0, 1));
			return;
		}
		float tr = 1 / inputs.yaw * max_tr;
		float turn = xzSpeed / tr * xzSpeedDir;
		float turnDeg = turn * Mth.RAD_TO_DEG;
		if (!isSliding()) av = av
				.multiply(1, 0, 1)
				.add(0, turnDeg, 0);
		else addMomentY(turnDeg*slideAngleCos, false);
		setAngularVel(av);
	}
	
	/**
	 * called every tick to change the vehicle direction if in the air.
	 * @param q the current direction of the vehicle
	 */
	public void directionAir(Quaternion q) {
		
	}
	
	/**
	 * called every tick to change the vehicle direction if in the water.
	 * @param q the current direction of the vehicle
	 */
	public void directionWater(Quaternion q) {
		directionAir(q);
	}
	
	/**
	 * used to make the vehicle level with the ground or water.
	 * @param q the current direction of the vehicle
	 * @param dPitch
	 * @param dRoll
	 * @param forced
	 */
	public void flatten(Quaternion q, float dPitch, float dRoll, boolean forced) {
		Vec3 av = getAngularVel();
		float x = (float)av.x, z = (float)av.z;
		if (!forced) {
			if (Math.abs(av.x) <= dPitch) x = 0;
			if (Math.abs(av.z) <= dRoll) z = 0;
		} else x = z = 0;
		EulerAngles angles = UtilAngles.toDegrees(q);
		float roll, pitch;
		if (dRoll != 0) {
			if (Math.abs(angles.roll) < dRoll) roll = (float) -angles.roll;
			else roll = -(float)Math.signum(angles.roll) * dRoll;
			z += roll;
		}
		if (dPitch != 0) {
			if (Math.abs(angles.pitch) < dPitch) pitch = (float) angles.pitch;
			else pitch = (float)Math.signum(angles.pitch) * dPitch;
			x += pitch;
		}
		setAngularVel(new Vec3(x, av.y, z));
	}
	
	public void addMoment(Vec3 moment, boolean control) {
		Vec3 m = getMoment();
		double x = moment.x, y = moment.y, z = moment.z;
		if (control) {
			Vec3 av = getAngularVel();
			if (moment.x != 0) x = getControlMomentComponent(m.x, moment.x, av.x, getControlMaxDeltaPitch(), Ix);
			if (moment.y != 0) y = getControlMomentComponent(m.y, moment.y, av.y, getControlMaxDeltaYaw(), Iy);
			if (moment.z != 0) z = getControlMomentComponent(m.z, moment.z, av.z, getControlMaxDeltaRoll(), Iz);
		}
		setMoment(m.add(x, y, z));
	}
	
	private double getControlMomentComponent(double cm, double m, double v, float max, float I) {
		if (Math.abs(v) > max && Math.signum(v) == Math.signum(m)) return 0;
		double m2 = cm + m;
		double a2 = m2 / I;
		double v2 = v + a2;
		double vd = Math.abs(v2) - max;
		if (vd > 0) m = (max*Math.signum(v) - v)*I - cm;
		return m;
	}
	
	public void addMomentX(float moment, boolean control) {
		addMoment(Vec3.ZERO.add(moment, 0, 0), control);
	}
	
	public void addMomentY(float moment, boolean control) {
		addMoment(Vec3.ZERO.add(0, moment, 0), control);
	}
	
	public void addMomentZ(float moment, boolean control) {
		addMoment(Vec3.ZERO.add(0, 0, moment), control);
	}
	
	public float getControlMaxDeltaPitch() {
		return getMaxDeltaPitch() * Mth.abs(inputs.pitch);
	}
	
	public float getControlMaxDeltaYaw() {
		return getMaxDeltaYaw() * Mth.abs(inputs.yaw);
	}
	
	public float getControlMaxDeltaRoll() {
		return getMaxDeltaRoll() * Mth.abs(inputs.roll);
	}
	
	/**
	 * cache all needed physics values BEFORE calculating movement.
	 * instead of calculating values multiple times per tick.
	 * @param q the current direction of the vehicle
	 */
	protected void calcMoveStatsPre(Quaternion q) {
		totalMass = getAircraftMass() + partsManager.getPartsWeight();
		staticFric = totalMass * ACC_GRAVITY * CO_STATIC_FRICTION;
		kineticFric = totalMass * ACC_GRAVITY * CO_KINETIC_FRICTION;
		maxPushThrust = partsManager.getTotalPushThrust();
		maxSpinThrust = partsManager.getTotalSpinThrust();
		currentFuel = partsManager.getCurrentFuel();
		maxFuel = partsManager.getMaxFuel();
		hasFlares = partsManager.getFlares().size() > 0;
		airPressure = UtilEntity.getAirPressure(this);
	}
	
	/**
	 * cache some needed physics values AFTER calculating movement.
	 * instead of calculating values multiple times per tick.
	 * @param q the current direction of the vehicle
	 */
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
	
	/**
	 * called every tick on server and client side.
	 * after forces are calculated. see {@link EntityAircraft#tickMovement(Quaternion)}.
	 * F=M*A -> A=F/M then A is added to current velocity
	 */
	public void calcAcc() {
		if (!addForceBetweenTicks.equals(Vec3.ZERO)) System.out.println("add force "+addForceBetweenTicks+" "+this);
		Vec3 f = getForces().add(addForceBetweenTicks);
		double s = 1/getTotalMass();
		setDeltaMovement(getDeltaMovement().add(f.scale(s)));
		addForceBetweenTicks = Vec3.ZERO;
	}
	
	@Override
	public void move(MoverType type, Vec3 move) {
		super.move(type, move);
		if (!noPhysics && isOnGround() && getDeltaMovement().y == 0) stepDown(move);
	}
	
	/**
	 * custom physics used to "step down" from a stair step.
	 * appears like the entity teleport down from small ledges.
	 * added to avoid vehicles floating down from stair step ledges. 
	 * @param move current velocity
	 */
	protected void stepDown(Vec3 move) {
		AABB aabb = getBoundingBox();
		Vec3 down = new Vec3(0,-getStepHeight()-0.1, 0); // this -0.1 is needed trust me
		List<VoxelShape> list = level.getEntityCollisions(this, aabb.expandTowards(down));
		Vec3 collide = collideBoundingBox(this, down, aabb, level, list);
		if (collide.y < 0 && collide.y >= -getStepHeight()) {
			setPos(getX(), getY()+collide.y, getZ());
		}
	}
	
	/**
	 * called on both client and server side every tick to calculate the vehicle's forces this tick.
	 * calls the following based on entity's current state: 
	 * {@link EntityAircraft#tickAlways(Quaternion)},
	 * {@link EntityAircraft#tickGround(Quaternion)},
	 * {@link EntityAircraft#tickGroundWater(Quaternion)},
	 * {@link EntityAircraft#tickWater(Quaternion)},
	 * {@link EntityAircraft#tickAir(Quaternion)}.
	 * @param q the plane's current rotation
	 */
	public void tickMovement(Quaternion q) {
		tickAlways(q);
		if (onGround && isInWater()) tickGroundWater(q);
		else if (onGround) tickGround(q);
		else if (isInWater()) tickWater(q);
		else tickAir(q);
	}
	
	/**
	 * called on both client and server side every tick to calculate the vehicle's forces in any context.
	 * called by {@link EntityAircraft#tickMovement(Quaternion)}.
	 * @param q the plane's current rotation
	 */
	public void tickAlways(Quaternion q) {
		Vec3 f = getForces();
		f = f.add(getWeightForce());
		f = f.add(getThrustForce(q));
		setForces(f);
	}
	
	/**
	 * called on both client and server side every tick to calculate the vehicle's forces when on the ground.
	 * called by {@link EntityAircraft#tickMovement(Quaternion)}.
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
		if (isBreaking() && isOperational()) applyBreaks();
	}
	
	/**
	 * @return the acceleration while driving 
	 */
	public double getDriveAcc() {
		return getSpinThrustMag()/getTotalMass();
	}
	
	public boolean isBreaking() {
		return false;
	}
	
	public abstract boolean canBreak();
	
	public void applyBreaks() {
		addFrictionForce(kineticFric);
	}
	
	protected void addFrictionForce(double f) {
		Vec3 m = getDeltaMovement();
		if (m.x == 0 && m.z == 0) return;
		Vec3 mn = m.normalize();
 		Vec3 force = mn.scale(-f);
		Vec3 acc = force.scale(1/getTotalMass());
		if (m.x != 0 && Math.signum(m.x+acc.x) != Math.signum(m.x)) {
			force = force.multiply(0, 1, 1);
			m = m.multiply(0, 1, 1);
		}
		if (m.z != 0 && Math.signum(m.z+acc.z) != Math.signum(m.z)) {
			force = force.multiply(1, 1, 0);
			m = m.multiply(1, 1, 0);
		}
		setDeltaMovement(m);
		setForces(getForces().add(force));
	}
	
	protected boolean willSlideFromTurn() {
		double max_tr = getTurnRadius();
		if (inputs.yaw != 0 && max_tr != 0) { // IF TURNING
			double tr = max_tr * 1 / Math.abs(inputs.yaw); // inputed turn radius
			double cen_acc = xzSpeed * xzSpeed / tr; // cen_acc needed to complete turn
			double cen_force = cen_acc * getTotalMass(); // friction force needed to not slide
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
	 * called on both client and server side every tick to calculate the vehicle's forces when in air.
	 * called by {@link EntityAircraft#tickMovement(Quaternion)}.
	 * @param q the plane's current rotation
	 */
	public void tickAir(Quaternion q) {
		setForces(getForces().add(getDragForce(q)));
		resetFallDistance();
	}
	
	/**
	 * called on both client and server side every tick to calculate the vehicle's forces in when floating in water.
	 * called by {@link EntityAircraft#tickMovement(Quaternion)}.
	 * @param q the plane's current rotation
	 */
	public void tickWater(Quaternion q) {
		tickAir(q);
	}
	
	/**
	 * called on both client and server side every tick to calculate the vehicle's forces when at the bottom of water.
	 * called by {@link EntityAircraft#tickMovement(Quaternion)}.
	 * @param q the plane's current rotation
	 */
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
	public double getPushThrustMag() {
		if (getCurrentFuel() <= 0 || !isOperational()) return 0;
		return getCurrentThrottle() * getMaxPushThrust();
	}
	
	/**
	 * @return max possible push force magnitude assuming max throttle. 
	 */
	public float getMaxPushThrust() {
		return maxPushThrust;
	}
	
	/**
	 * @return the magnitude of the spin force based on the engines, throttle, and 0 if no fuel
	 */
	public double getSpinThrustMag() {
		if (getCurrentFuel() <= 0 || !isOperational()) return 0;
		return getCurrentThrottle() * getMaxSpinThrust();
	}
	
	/**
	 * @return max possible spin force magnitude assuming max throttle. 
	 */
	public float getMaxSpinThrust() {
		return maxSpinThrust;
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
	
	/**
	 * @return the magnitude of the drag force
	 */
	public double getDragMag() {
		// Drag = (drag coefficient) * (air pressure) * (speed)^2 * (wing surface area) / 2
		double speedSqr = getDeltaMovement().lengthSqr();
		return airPressure * speedSqr * getCrossSectionArea() * CO_DRAG;
	}
	
	/**
	 * modifies {@link EntityAircraft#getBaseCrossSecArea()} for physics.
	 * this function returns a larger value if a plane's flaps are down for example.
	 * @return this vehicle's cross sectional area for drag and radar.
	 */
	public double getCrossSectionArea() {
		double a = getBaseCrossSecArea();
		if (!isOperational()) a += 4;
		return a;
	}
	
	/**
	 * @return srly bro you dont know what this is?
	 */
	public Vec3 getWeightForce() {
		return new Vec3(0, -getTotalMass() * ACC_GRAVITY, 0);
	}
	
	/**
	 * @return this mass of the aircraft plus the mass of the parts
	 */
	public float getTotalMass() {
		if (Float.isNaN(totalMass)) {
			System.out.println("ERROR: NAN MASS? setting to 100 | "+this);
			totalMass = 100;
		} else if (totalMass == 0) {
			System.out.println("ERROR: 0 MASS? setting to 100 | "+this);
			totalMass = 100;
		}
		return totalMass;
	}
	
	/**
	 * this is NOT the total mass. 
	 * see {@link EntityAircraft#getTotalMass()}
	 * @return the mass of the vehicle. not including parts.
	 */
	public final float getAircraftMass() {
		return entityData.get(MASS);
	}
	
	/**
	 * @param mass the mass of the vehicle not including parts.
	 */
	public final void setAircraftMass(float mass) {
		if (mass < 0) mass = 0;
		entityData.set(MASS, mass);
	}
	
	/**
	 * fired on both client and server side to control the plane's weapons, flares, open menu
	 */
	public void controlSystem() {
		if (!isOperational()) return;
		Entity controller = getControllingPassenger();
		if (controller == null && !isPlayerRiding()) return;
		if (!level.isClientSide) {
			weaponSystem.tick();
			radarSystem.tickUpdateTargets();
			if (controller == null) return;
			boolean consume = !isNoConsume();
			if (controller instanceof ServerPlayer player) {
				if (inputs.openMenu) openMenu(player);
				if (player.isCreative()) consume = false;
			}
			if (consume) tickFuel();
			setFlareNum(partsManager.getNumFlares());
			if (inputs.flare && tickCount % 5 == 0) flare(controller, consume);
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
		return (isOnGround() && xzSpeed < 0.1) || isTestMode();
	}
	
	public String getOpenMenuError() {
		if (!isOnGround()) return "error.dscombat.no_menu_in_air";
		return "error.dscombat.no_menu_moving";
	}
	
	public void flare(Entity controller, boolean consume) {
		partsManager.useFlares(consume);
	}
	
	/**
	 * ticks the parts manager on client and server side
	 */
	public void tickParts() {
		if (level.isClientSide) partsManager.clientTickParts();
		else partsManager.tickParts();
	}
	
	/**
	 * Override to stop lag backs from causing planes to crash.
	 */
	@Override
	public void lerpMotion(double x, double y, double z) {
		if (!isControlledByLocalInstance()) {
			super.lerpMotion(x, y, z);
			return;
		}
	}
	
	@Override
	public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
		lerpX = x; lerpY = y; lerpZ = z;
		lerpSteps = posRotationIncrements;
    }
	
	private void tickLerp() {
		if (isControlledByLocalInstance()) {
			syncPacketPositionCodec(getX(), getY(), getZ());
			synchMoveRot();
			lerpSteps = 0;
			lerpStepsQ = 0;
			return;
		}
		if (lerpSteps > 0) {
			double d0 = getX() + (lerpX - getX()) / (double)lerpSteps;
	        double d1 = getY() + (lerpY - getY()) / (double)lerpSteps;
	        double d2 = getZ() + (lerpZ - getZ()) / (double)lerpSteps;
	        --lerpSteps;
	        setPos(d0, d1, d2);
		}
	}
	
	private void synchMoveRot() {
		if (!level.isClientSide || tickCount % 20 != 0) return;
		PacketHandler.INSTANCE.sendToServer(new ToServerAircraftMoveRot(this));
	}
	
	/**
	 * called to reset controls if there is no pilot
	 */
	public void resetControls() {
		inputs.reset();
		this.throttleToZero();
	}
	
	public final boolean isFreeLook() {
    	return isFreeLook;
    }
    
    public final void setFreeLook(boolean freeLook) {
    	this.isFreeLook = freeLook;
    }
    
    public void toggleFreeLook() {
    	setFreeLook(!isFreeLook());
    }
    
    public RadarMode getRadarMode() {
    	return radarMode;
    }
    
    public void setRadarMode(RadarMode mode) {
    	this.radarMode = mode;
    }
    
    public void cycleRadarMode() {
    	this.radarMode = this.radarMode.cycle();
    }
    
    public final String getRadioSong() {
    	return entityData.get(RADIO_SONG);
    }
    
    public boolean hasRadioSong() {
    	return !getRadioSong().isEmpty();
    }
    
    public final void setRadioSong(String song) {
    	entityData.set(RADIO_SONG, song);
    }
    
    public void turnRadioOff() {
    	setRadioSong("");
    }
	
    /**
     * called in {@link EntityAircraft#init()} by server side
     */
	public void serverSetup() {
		partsManager.setupParts();
	}
	
	/**
	 * called in {@link EntityAircraft#init()} by client side
	 */
	public void clientSetup() {
		UtilClientSafeSoundInstance.aircraftEngineSound(
				Minecraft.getInstance(), this, getEngineSound());
	}
	
	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	public void readSpawnData(FriendlyByteBuf buffer) {
		preset = buffer.readUtf();
		int weaponIndex = buffer.readInt();
		int radarMode = buffer.readInt();
		boolean gear = buffer.readBoolean();
		boolean freeLook = buffer.readBoolean();
		List<PartSlot> slots = PartsManager.readSlotsFromBuffer(buffer);
		// ORDER MATTERS
		weaponSystem.setSelected(weaponIndex);
		partsManager.setPartSlots(slots);
		partsManager.clientPartsSetup();
		// PRESET STUFF
		if (!AircraftPresets.get().has(preset)) return;
		AircraftPreset ap = AircraftPresets.get().getPreset(preset);
		item = ap.getItem();
		// OTHER
		setRadarMode(RadarMode.byId(radarMode));
		setLandingGear(gear);
		setFreeLook(freeLook);
	}
	
	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeUtf(preset);
		buffer.writeInt(weaponSystem.getSelectedIndex());
		buffer.writeInt(getRadarMode().ordinal());
		buffer.writeBoolean(isLandingGear());
		buffer.writeBoolean(isFreeLook());
		PartsManager.writeSlotsToBuffer(buffer, partsManager.getSlots());
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (xzSpeed > 0.2) return InteractionResult.PASS;
		if (player.isSecondaryUseActive()) return InteractionResult.PASS;
		if (!isOperational()) return InteractionResult.PASS;
		if (player.getRootVehicle() != null && player.getRootVehicle().equals(this)) return InteractionResult.PASS;
		if (!level.isClientSide) {
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
					String ammoId = ItemAmmo.getWeaponId(stack);
					for (EntityTurret t : getTurrets()) {
						WeaponData wd = t.getWeaponData();
						if (wd == null) continue;
						if (!wd.getId().equals(ammoId)) continue;
						int o = wd.addAmmo(stack.getCount());
						t.setAmmo(wd.getCurrentAmmo());
						t.updateDataAmmo();
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
				// RADIO
				} else if (item instanceof RecordItem disk) {
					if (!hasRadio) return InteractionResult.PASS;
					setRadioSong(disk.getSound().getLocation().toString());
					return InteractionResult.SUCCESS;
				// CUSTOM NAME
				} else if (item instanceof NameTagItem name) {
					if (stack.hasCustomHoverName()) {
						setCustomName(stack.getHoverName());
						setCustomNameVisible(true);
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
			return rideAvailableSeat(player) ? InteractionResult.CONSUME : InteractionResult.PASS;
		} else if (level.isClientSide) {	
			Minecraft m = Minecraft.getInstance();
			if (m.player.equals(player)) ClientInputEvents.centerMouse();
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.SUCCESS;
	}
	
	private boolean ridePilotSeat(Entity e, List<EntitySeat> seats) {
		for (EntitySeat seat : seats) 
			if (seat.isPilotSeat()) 
				return e.startRiding(seat);
		return false;
	}
	
	public boolean ridePassengerSeat(Entity e) {
		List<EntitySeat> seats = getSeats();
		for (EntitySeat seat : seats) 
			if (!seat.isPilotSeat() && e.startRiding(seat)) 
				return true;
		return false;
	}
	
	public boolean rideAvailableSeat(Entity e) {
		List<EntitySeat> seats = getSeats();
		if (ridePilotSeat(e, seats)) return true;
		for (EntitySeat seat : seats) 
			if (e.startRiding(seat)) {
				return true;
			}
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
		//System.out.println("riding seat "+seatIndex);
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
	
	/**
	 * all part entities ride the vehicle using the vanilla passenger system.
	 * the part's position is set based on the vehicle's rotation.
	 */
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
        for (EntitySeat seat : getSeats()) {
        	if (seat.isPilotSeat()) {
        		return seat.getPlayer();
        	}
        }
        return null;
    }
	
	public boolean isPlayerRiding() {
		for (EntitySeat seat : getSeats()) {
			if (seat.getPlayer() != null) return true;
		}
		return false;
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
    public boolean isPushable() {
    	return false;
    }
    
    @Override
    public boolean isPushedByFluid() {
    	return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    
    @Override
    public boolean canCollideWith(Entity entity) {
    	if (!super.canCollideWith(entity)) return false;
    	if (entity.isPushable()) return false;
    	return true;
    }
	
    @Override
    public boolean isInvulnerableTo(DamageSource source) {
    	if (isTestMode()) return true;
    	if (super.isInvulnerableTo(source)) return true;
    	if (source.isFire() && (tickCount-hurtByFireTime) < 10) return true;
    	if (isVehicleOf(source.getEntity())) return true;
    	return false;
    }
    
	@Override
    public boolean hurt(DamageSource source, float amount) {
		if (isInvulnerableTo(source)) return false;
		if (source.isFire()) hurtByFireTime = tickCount;
		addHealth(-calcDamageBySource(source, amount));
		if (!level.isClientSide && isOperational()) level.playSound(null, 
			blockPosition(), ModSounds.VEHICLE_HIT_1.get(), 
			SoundSource.PLAYERS, 0.5f, 1.0f);
		if (!level.isClientSide && !isOperational()) {
			checkExplodeWhenKilled(source);
		}
		return true;
	}
	
	public float calcDamageBySource(DamageSource source, float amount) {
		if (source.isProjectile()) amount = calcProjDamageBySource(source, amount);
		if (!source.isBypassArmor()) amount -= amount*getTotalArmor()*0.01f*Config.COMMON.armorStrength.get();
		if (amount < 0) amount = 0;
		return amount;
	}
	
	public float calcProjDamageBySource(DamageSource source, float amount) {
		if (!source.isExplosion()) amount *= Config.COMMON.bulletDamageFactor.get().floatValue();
		return amount;
	}
	
	protected boolean checkExplodeWhenKilled(DamageSource source) {
		if (source.getMsgId().equals(DamageSource.FALL.getMsgId())) {
			explode(AircraftDamageSource.fall(this));
			return true;
		} else if (source.getMsgId().equals(DamageSource.FLY_INTO_WALL.getMsgId())) {
			explode(AircraftDamageSource.collide(this));
			return true;
		}
		return false;
	}
	
	public void dropInventory() {
		if (level.isClientSide) return;
		if (!level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) return;
		partsManager.dropAllItems();
	}
	
	public void addForceMomentToClient(Vec3 force, Vec3 moment) {
		if (level.isClientSide) return;
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), 
				new ToClientAddForceMoment(this, force, moment));
	}
	
	public void explode(DamageSource source) {
		if (!level.isClientSide) {
			level.explode(this, source,
				null, getX(), getY(), getZ(), 
				explodeSize, true, 
				Explosion.BlockInteraction.BREAK);
		} else {
			level.addParticle(ParticleTypes.LARGE_SMOKE, 
				getX(), getY()+0.5D, getZ(), 
				0.0D, 0.0D, 0.0D);
		}
	}
	
	/**
	 * ignoring vanilla explosion effects.
	 * see {@link EntityAircraft#customExplosionHandler(Explosion)}
	 * for custom explosion handling.
	 */
	@Override
	public boolean ignoreExplosion() {
		return true;
	}
	
	public static final double EXP_FORCE_FACTOR = 100;
	public static final double EXP_MOMENT_FACTOR = 100;
	
	public void customExplosionHandler(Explosion exp) {
		Vec3 s = exp.getPosition();
		Vec3 b = UtilGeometry.getClosestPointOnAABB(s, getBoundingBox());
		Vec3 r = b.subtract(position());
		
		float diameter = 6f;
		if (UtilEntity.getExplosionRadiusField() != null) {
			try {
				diameter = (float)UtilEntity.getExplosionRadiusField().get(exp) * 2;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		double dist_check = Math.sqrt(this.distanceToSqr(s)) / (double)diameter;
		if (dist_check > 1.0d) return;
		
		Entity exp_entity = exp.getDamageSource().getDirectEntity();
		double dx = getX() - s.x;
        double dy = getY() - s.y;
        double dz = getZ() - s.z;
        double d = Math.sqrt(dx*dx + dy*dy + dz*dz);
        if (Double.isNaN(d) || Double.isInfinite(d)) return;
        if (d == 0) {
        	if (exp_entity != null) {
        		Vec3 dir = exp_entity.getLookAngle();
        		dx = dir.x; dy = dir.y; dz = dir.z;
        	} else { dx = 0; dy = 1; dz = 0; }
        } else { dx /= d; dy /= d; dz /= d; }
        
        double seen_percent = Explosion.getSeenPercent(s, this);
        double exp_factor = (1.0D - dist_check) * seen_percent;
        
        float amount = (float)((int)((exp_factor*exp_factor+exp_factor)/2d*7d*(double)diameter+1d));
        hurt(exp.getDamageSource(), amount*Config.COMMON.explodeDamageFactor.get().floatValue());
        
        Vec3 force = new Vec3(dx*exp_factor, dy*exp_factor, dz*exp_factor).scale(EXP_FORCE_FACTOR);
        addForceBetweenTicks = addForceBetweenTicks.add(force);
        
		Vec3 f;
		if (s.equals(b) && exp_entity != null) 
			f = exp_entity.getDeltaMovement().normalize().scale(exp_factor*EXP_MOMENT_FACTOR);
		else f = s.subtract(b).normalize().scale(exp_factor*EXP_MOMENT_FACTOR);
		Vec3 moment = r.cross(f);
		addMomentBetweenTicks = addMomentBetweenTicks.add(moment);
		
		addForceMomentToClient(force, moment);
	}
	
	/**
	 * @return the max speed of the craft along the x and z axis
	 */
    public final float getMaxSpeed() {
    	return entityData.get(MAX_SPEED);
    }
    
    /**
     * @param maxSpeed the max speed of the craft along the x and z axis
     */
    public final void setMaxSpeed(float maxSpeed) {
    	entityData.set(MAX_SPEED, maxSpeed);
    }
    
    /**
     * @return between 1 and 0 or 1 and -1 if negativeThrottle
     */
    public final float getCurrentThrottle() {
    	if (level.isClientSide) return clientThrottle;
    	else return entityData.get(THROTTLE);
    }
    
    /**
     * @param throttle between 1 and 0 or 1 and -1 if negativeThrottle
     */
    public final void setCurrentThrottle(float throttle) {
    	if (throttle > 1) throttle = 1;
    	else if (negativeThrottle && throttle < -1) throttle = -1;
    	else if (!negativeThrottle && throttle < 0) throttle = 0;
    	if (level.isClientSide) clientThrottle = throttle;
    	else entityData.set(THROTTLE, throttle);
    }
    
    /**
     * call this every tick to bring throttle back to zero.
     */
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
    
    public final float getPitchTorque() {
    	return entityData.get(PITCH_TORQUE);
    }
    
    public final void setPitchTorque(float degrees) {
    	if (degrees < 0) degrees = 0;
    	entityData.set(PITCH_TORQUE, degrees);
    }
    
    public final float getYawTorque() {
    	return entityData.get(YAW_TORQUE);
    }
    
    public final void setYawTorque(float degrees) {
    	if (degrees < 0) degrees = 0;
    	entityData.set(YAW_TORQUE, degrees);
    }
    
    public final float getRollTorque() {
    	return entityData.get(ROLL_TORQUE);
    }
    
    public final void setRollTorque(float degrees) {
    	if (degrees < 0) degrees = 0;
    	entityData.set(ROLL_TORQUE, degrees);
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
    
    public final float getBaseCrossSecArea() {
    	return entityData.get(CROSS_SEC_AREA);
    }
    
    public final void setBaseCrossSecArea(float area) {
    	entityData.set(CROSS_SEC_AREA, area);
    }
    
    public final Vec3 getMoment() {
    	return moment;
    }
    
    public final Vec3 getForces() {
    	return forces;
    }
    
    public final Vec3 getAngularVel() {
    	if (level.isClientSide) return clientAV;
    	return entityData.get(AV);
    }
    
    public final void setMoment(Vec3 m) {
    	moment = m;
    }
    
    public final void setForces(Vec3 f) {
    	forces = f;
    }
    
    public final void setAngularVel(Vec3 av) {
    	if (level.isClientSide) clientAV = av;
    	else entityData.set(AV, av);
    }
    
    /**
     * 1 is no stealth
     * 0 is invisible
     * @return value to be multiplied to the cross sectional area
     */
    public final float getStealth() {
    	return entityData.get(STEALTH);
    }
    
    /**
     * 1 is no stealth
     * 0 is invisible
     * @param stealth value to be multiplied to the cross sectional area
     */
    public final void setStealth(float stealth) {
    	if (stealth < 0) stealth = 0;
    	entityData.set(STEALTH, stealth);
    }
    
    /**
     * CLIENT ONLY
     */
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
    	ItemStack stack = item.copy();
    	CompoundTag tag = new CompoundTag();
    	addAdditionalSaveData(tag);
    	CompoundTag eTag = new CompoundTag();
    	eTag.put("EntityTag", tag);
    	stack.setTag(eTag);
    	return stack;
    }
    
    public boolean canBecomeItem() {
    	return tickCount/20 > Config.COMMON.freshEntityToItemCooldown.get() 
    			&& (lastShootTime == -1 || (tickCount-lastShootTime)/20 > Config.COMMON.usedWeaponToItemCooldown.get());
    }
    
    /**
     * SERVER SIDE ONLY
     */
    public void becomeItem(Vec3 pos) {
    	if (level.isClientSide) return;
    	ItemStack stack = getItem();
		ItemEntity e = new ItemEntity(level, pos.x, pos.y, pos.z, stack);
		level.addFreshEntity(e);
		discard();
    }
    
    /**
     * SERVER SIDE ONLY
     */
    public void becomeItem() {
    	becomeItem(position());
    }
    
    @Override
    public ItemStack getPickResult() {
    	return getItem();
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
    
    public final float getBaseArmor() {
    	return entityData.get(BASE_ARMOR);
    }
    
    public final void setBaseArmor(float armor) {
    	entityData.set(BASE_ARMOR, armor);
    }
    
    public float getTotalArmor() {
    	return getBaseArmor() + partsManager.getTotalExtraArmor();
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
     * called every tick on client and server side.
     * plays all the sounds for the players in the plane.
     */
    public void sounds() {
    	if (level.isClientSide && isOperational()) {
    		// THE PLAYER FOR LOOP IS REQUIRED OTHERWISE YOU GET FUCKED BY JAVA CLASS LOADER
    		if (tickCount % 4 == 0 && radarSystem.isTrackedByMissile()) for (Player p : getRidingPlayers()) {
    			level.playSound(p, new BlockPos(p.position()), 
	    			ModSounds.MISSILE_WARNING.get(), SoundSource.PLAYERS, 
	    			Config.CLIENT.rwrWarningVol.get().floatValue(), 1f);
    		} else if (tickCount % 8 == 0 && radarSystem.isTrackedByRadar()) for (Player p : getRidingPlayers()) {
    			level.playSound(p, new BlockPos(p.position()), 
    	    		ModSounds.GETTING_LOCKED.get(), SoundSource.PLAYERS, 
    	    		Config.CLIENT.missileWarningVol.get().floatValue(), 1f);
        	}
    		if (tickCount % 10 == 0 && shouldPlayIRTone()) for (Player p : getRidingPlayers()) {
    			level.playSound(p, new BlockPos(p.position()), 
    	    			ModSounds.FOX2_TONE_1.get(), SoundSource.PLAYERS, 
    	    			Config.CLIENT.irTargetToneVol.get().floatValue(), 1f);
    		}
    	}
    } 
    
    /**
     * entity tracking missile calls this server side when tracking this plane
     * @param pos the position of the missile
     */
    public void trackedByMissile(Vec3 pos) {
    	if (hasControllingPassenger()) radarSystem.addRWRWarning(pos, true, level.isClientSide);
    }
    
    /**
     * another radar system calls this server side when tracking this craft
     * @param pos the position of the radar
     */
    public void lockedOnto(Vec3 pos) {
    	if (hasControllingPassenger()) radarSystem.addRWRWarning(pos, false, level.isClientSide);
    }
    
    public void playIRTone() {
    	entityData.set(PLAY_IR_TONE, true);
    }
    
    public void stopIRTone() {
    	entityData.set(PLAY_IR_TONE, false);
    }
    
    public boolean shouldPlayIRTone() {
    	return entityData.get(PLAY_IR_TONE);
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
    
    /**
     * the custom bounding box draws the box so that the entity position is in the middle of the box in all 3 dimensions
     * @return if this entity should use {@link EntityAircraft#makeCustomBoundingBox()}
     */
    public boolean isCustomBoundingBox() {
    	return false;
    }
    
    @Override
    protected AABB getBoundingBoxForPose(Pose pose) {
    	return makeBoundingBox();
    }
    
    @Override
    public AABB getBoundingBoxForCulling() {
    	// FIXME 3 big vehicles wont render at certain camera angles
    	return getBoundingBox();
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
    
    public abstract boolean canToggleLandingGear();
    
    /**
     * @return true if landing gear is out false if folded
     */
    public boolean isLandingGear() {
    	return isLandingGear;
    }
    
    /**
     * @param gear true if landing gear is out false if folded
     */
    public void setLandingGear(boolean gear) {
    	this.isLandingGear = gear;
    }
    
    /**
     * @return if this vehicle will update it's forces and move
     */
    public boolean isTestMode() {
    	return entityData.get(TEST_MODE);
    }
    
    /**
     * @param testMode if this vehicle will update it's forces and move
     */
    public void setTestMode(boolean testMode) {
    	entityData.set(TEST_MODE, testMode);
    }
    
    public void toggleLandingGear() {
    	if (!canToggleLandingGear()) return;
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
    
    /**
     * @return if this vehicle will consume fuel or ammo
     */
    public boolean isNoConsume() {
    	return entityData.get(NO_CONSUME);
    }
    
    /**
     * @param noConsume if this vehicle will consume fuel or ammo
     */
    public void setNoConsume(boolean noConsume) {
    	entityData.set(NO_CONSUME, noConsume);
    }
    
    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
		return super.getDismountLocationForPassenger(livingEntity);
	}
    
    public int getFlareNum() {
    	return entityData.get(FLARE_NUM);
    }
    
    /**
     * outside mods shouldn't use this. does not change the actual number of flares.
     * @param flares a number of flares to synch with the client
     */
    public void setFlareNum(int flares) {
    	entityData.set(FLARE_NUM, flares);
    }
    
    public boolean hasFlares() {
    	return hasFlares;
    }
    
    protected void debug(String debug) {
    	debug(debug, true);
    }
    
    protected void debug(String debug, boolean passengerCheck) {
    	if (!passengerCheck || (passengerCheck && hasControllingPassenger())) 
    		System.out.println(debug);
    }
    
    protected void debugTick() {
		String side = "SERVER";
		if (level.isClientSide) side = "CLIENT";
		System.out.println(side+" TICK "+tickCount+" "+this);
	}
    
    public void toClientPassengers(IPacket packet) {
    	if (level.isClientSide) return;
    	for (Player p : getRidingPlayers()) {
    		PacketHandler.INSTANCE.send(
    			PacketDistributor.PLAYER.with(() -> (ServerPlayer)p),
    			packet);
    	}
    }
    
    public boolean isWeaponAngledDown() {
    	return false;
    }
    
    public boolean canAngleWeaponDown() {
    	return false;
    }
    
    public boolean canFlapsDown() {
    	return false;
    }
    
    public boolean canHover() {
    	return false;
    }
    
    @Override
    public boolean canChangeDimensions() {
    	return isOperational();
    }
    
    @Override
    public boolean canRiderInteract() {
    	return false;
    }
    
    @Override
    public boolean canTrample(BlockState state, BlockPos pos, float fallDistance) {
    	return true;
    }
    
    @Override
    public boolean isAlliedTo(Entity entity) {
    	if (entity == null) return false;
    	Entity c = entity.getControllingPassenger();
    	if (c != null) return isAlliedTo(c.getTeam());
    	return super.isAlliedTo(entity);
    }
    
    @Override
    public boolean isAlliedTo(Team team) {
    	if (team == null) return false;
    	Entity c = getControllingPassenger();
		if (c != null) return team.isAlliedTo(c.getTeam());
    	return super.isAlliedTo(team);
    }
    
}
