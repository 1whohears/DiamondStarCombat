package com.onewhohears.dscombat.entity.vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.model.obj.ObjRadarModel.MastType;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.common.container.menu.VehicleContainerMenu;
import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientAddForceMoment;
import com.onewhohears.dscombat.common.network.toclient.ToClientVehicleControl;
import com.onewhohears.dscombat.common.network.toclient.ToClientVehicleExplode;
import com.onewhohears.dscombat.common.network.toserver.ToServerFixHitboxes;
import com.onewhohears.dscombat.common.network.toserver.ToServerSyncRotBoxPassengerPos;
import com.onewhohears.dscombat.common.network.toserver.ToServerVehicleCollide;
import com.onewhohears.dscombat.common.network.toserver.ToServerVehicleControl;
import com.onewhohears.dscombat.common.network.toserver.ToServerVehicleMoveRot;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.PartsManager;
import com.onewhohears.dscombat.data.parts.instance.StorageInstance;
import com.onewhohears.dscombat.data.radar.RadarStats.RadarMode;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.vehicle.DSCPhyCons;
import com.onewhohears.dscombat.data.vehicle.VehicleInputManager;
import com.onewhohears.dscombat.data.vehicle.VehiclePresets;
import com.onewhohears.dscombat.data.vehicle.VehicleSoundManager;
import com.onewhohears.dscombat.data.vehicle.VehicleTextureManager;
import com.onewhohears.dscombat.data.vehicle.VehicleType;
import com.onewhohears.dscombat.data.vehicle.client.VehicleClientPresets;
import com.onewhohears.dscombat.data.vehicle.client.VehicleClientStats;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.data.weapon.WeaponSystem;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.IREmitter;
import com.onewhohears.dscombat.entity.damagesource.VehicleDamageSource;
import com.onewhohears.dscombat.entity.parts.EntityChainHook;
import com.onewhohears.dscombat.entity.parts.EntityGimbal;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.dscombat.item.VehicleInteractItem;
import com.onewhohears.dscombat.util.UtilClientPacket;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.UtilMCText;
import com.onewhohears.dscombat.util.UtilParticles;
import com.onewhohears.dscombat.util.UtilServerPacket;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilAngles.EulerAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;
import com.onewhohears.dscombat.util.math.UtilRandom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.NameTagItem;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.crafting.Ingredient;
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

/**
 * the parent class for all vehicle entities in this mod
 * @author 1whohears
 */
// TODO: mouse mode handling has configurable sensitivity; higher by default. inputs have 'inertia'
public abstract class EntityVehicle extends Entity implements IEntityAdditionalSpawnData, IREmitter, CustomExplosion {
	
	protected static final Logger LOGGER = LogUtils.getLogger();
	
	public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(EntityVehicle.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> ARMOR = SynchedEntityData.defineId(EntityVehicle.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Quaternion> Q = SynchedEntityData.defineId(EntityVehicle.class, DataSerializers.QUATERNION);
	public static final EntityDataAccessor<Vec3> AV = SynchedEntityData.defineId(EntityVehicle.class, DataSerializers.VEC3);
	public static final EntityDataAccessor<Boolean> TEST_MODE = SynchedEntityData.defineId(EntityVehicle.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> NO_CONSUME = SynchedEntityData.defineId(EntityVehicle.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Integer> FLARE_NUM = SynchedEntityData.defineId(EntityVehicle.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<String> RADIO_SONG = SynchedEntityData.defineId(EntityVehicle.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Boolean> PLAY_IR_TONE = SynchedEntityData.defineId(EntityVehicle.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<RadarMode> RADAR_MODE = SynchedEntityData.defineId(EntityVehicle.class, DataSerializers.RADAR_MODE);
	
	public final String defaultPreset;
	public final VehicleInputManager inputs;
	public final VehicleSoundManager soundManager;
	public final VehicleTextureManager textureManager;
	public final PartsManager partsManager;
	public final WeaponSystem weaponSystem;
	public final RadarSystem radarSystem;
	
	protected final List<RotableHitbox> hitboxes = new ArrayList<>();
	protected final Set<Integer> hitboxCollidedIds = new HashSet<>();
	
	private final Map<Integer, Integer> formerPassengersServer = new HashMap<>();
	/**
	 * CLIENT ONLY
	 */
	private VehicleClientStats vehicleClientStats;
	private VehicleStats vehicleStats;
	
	/**
	 * this vehicle's original preset. 
	 * will be {@link defaultPreset} if it's not defined in its NBT.
	 * synched with client.  
	 */
	public String preset;
	private String assetId;
	
	/**
	 * SERVER ONLY
	 */
	public int lastShootTime = -1, ingredientDropIndex = -1;
	
	public Quaternion prevQ = Quaternion.ONE.copy();
	public Quaternion clientQ = Quaternion.ONE.copy();
	public Vec3 clientAV = Vec3.ZERO;
	
	public float zRot, zRotO; 
	public Vec3 prevMotion = Vec3.ZERO;
	public Vec3 forces = Vec3.ZERO, forcesO = Vec3.ZERO, addForceBetweenTicks = Vec3.ZERO;
	public Vec3 moment = Vec3.ZERO, momentO = Vec3.ZERO, addMomentBetweenTicks = Vec3.ZERO;
	
	public boolean nightVisionHud = false, hasRadio = false;
	
	protected boolean hasFlares;
	protected int xzSpeedDir, hurtByFireTime, flareTicks;
	protected float xzSpeed, totalMass, xzYaw, slideAngle, slideAngleCos, maxPushThrust, maxSpinThrust, currentFuel, maxFuel;
	protected double staticFric, kineticFric, airPressure;
	
	private int lerpSteps, deadTicks, stallWarnTicks, stallTicks, engineFireTicks, fuelLeakTicks, bingoTicks, groundTicks, hitboxRefreshAttempts;
	private double lerpX, lerpY, lerpZ;
	private float landingGearPos, landingGearPosOld, motorRot, wheelRot;
	
	protected boolean isLandingGear, isDriverCameraLocked = false;
	protected float throttle;
	
	@Nullable protected EntityGimbal pilotGimbal;
	@Nullable protected Player chainHolderPlayer;
	@Nullable protected EntityChainHook chainHolderHook;
	
	// TODO 5.4 vehicle visually breaks apart when damaged
	// TODO 5.6 place and remove external parts from outside the vehicle
	// TODO 5.7 an additional vehicle gui to control certain auxiliary functions (landing gear, jettison tanks/weapons)
	// TODO 2.5 add chaff
	// TODO 2.8 external fuel tanks
	
	public EntityVehicle(EntityType<? extends EntityVehicle> entityType, Level level, String defaultPreset) {
		super(entityType, level);
		this.defaultPreset = defaultPreset;
		preset = defaultPreset;
		vehicleStats = VehiclePresets.get().get(defaultPreset);
		assetId = vehicleStats.getAssetId();
		if (level.isClientSide) vehicleClientStats = VehicleClientPresets.get().get(assetId);
		else vehicleClientStats = null;
		blocksBuilding = true;
		inputs = new VehicleInputManager();
		soundManager = new VehicleSoundManager(this);
		textureManager = new VehicleTextureManager(this);
		partsManager = new PartsManager(this);
		weaponSystem = new WeaponSystem(this);
		radarSystem = new RadarSystem(this);
	}
	
	@Override
	protected void defineSynchedData() {
        entityData.define(HEALTH, 100f);
        entityData.define(ARMOR, 100f);
		entityData.define(Q, Quaternion.ONE);
		entityData.define(AV, Vec3.ZERO);
		entityData.define(TEST_MODE, false);
		entityData.define(NO_CONSUME, false);
		entityData.define(FLARE_NUM, 0);
		entityData.define(RADIO_SONG, "");
		entityData.define(PLAY_IR_TONE, false);
		entityData.define(RADAR_MODE, RadarMode.ALL);
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
    	} else if (RADIO_SONG.equals(key)) {
    		soundManager.onRadioSongUpdate(getRadioSong());
    	}
    }
	
	/**
	 * if this is a brand new entity and has no nbt custom data then the fresh entity nbt will
	 * merge with this vehicle's preset nbt. see {@link VehiclePresets}.
	 * you could summon a vehicle with nbt {preset:"some preset name"} to override the {@link EntityVehicle#defaultPreset}
 	 */
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		// ORDER MATTERS
		setTestMode(nbt.getBoolean("test_mode"));
		setNoConsume(nbt.getBoolean("no_consume"));
		// check if preset was defined
		preset = nbt.getString("preset");
		// if not use the default preset
		if (preset.isEmpty()) preset = defaultPreset;
		// check if the preset exists
		else if (!VehiclePresets.get().has(preset)) {
			preset = defaultPreset;
			LOGGER.warn("ERROR: preset "+preset+" doesn't exist!");
		}
		// get the preset data
		vehicleStats = VehiclePresets.get().get(preset);
		assetId = vehicleStats.getAssetId();
		CompoundTag presetNbt = vehicleStats.getDataAsNBT();
		// merge if this entity hasn't merged yet
		if (!nbt.getBoolean("merged_preset")) nbt.merge(presetNbt);
		partsManager.read(nbt, presetNbt);
		textureManager.read(nbt);
		soundManager.read(nbt);
		setHealth(nbt.getFloat("health"));
		setArmor(nbt.getFloat("armor"));
		setLandingGear(nbt.getBoolean("landing_gear"));
		setCurrentThrottle(nbt.getFloat("current_throttle"));
		setXRotNoQ(nbt.getFloat("xRot"));
		setYRotNoQ(nbt.getFloat("yRot"));
		zRot = nbt.getFloat("zRot");
		Quaternion q = UtilAngles.toQuaternion(getYRot(), getXRot(), zRot);
		setQ(q);
		setPrevQ(q);
		setClientQ(q);
		setRadarMode(RadarMode.values()[nbt.getInt("radar_mode")]);
		setRadioSong(nbt.getString("radio_song"));
		createRotableHitboxes(nbt);
		if (nbt.contains("ingredientDropIndex")) ingredientDropIndex = nbt.getInt("ingredientDropIndex");
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
		nbt.putString("preset", preset);
		nbt.putBoolean("test_mode", isTestMode());
		nbt.putBoolean("no_consume", isNoConsume());
		nbt.putBoolean("merged_preset", true);
		partsManager.write(nbt);
		textureManager.write(nbt);
		soundManager.write(nbt);
		nbt.putFloat("health", getHealth());
		nbt.putFloat("armor", getArmor());
		nbt.putBoolean("landing_gear", isLandingGear());
		nbt.putFloat("current_throttle", getCurrentThrottle());
		nbt.putFloat("xRot", getXRot());
		nbt.putFloat("yRot", getYRot());
		nbt.putFloat("zRot", zRot);
		nbt.putInt("radar_mode", getRadarMode().ordinal());
		nbt.putString("radio_song", getRadioSong());
		Entity c = getControllingPassenger();
		if (c != null) nbt.putString("owner", c.getScoreboardName());
		Component name = getCustomName();
        if (name != null) nbt.putString("CustomName", Component.Serializer.toJson(name));
        if (isCustomNameVisible()) nbt.putBoolean("CustomNameVisible", isCustomNameVisible());
        nbt.putFloat("fuel", getCurrentFuel());
        nbt.putFloat("flares", getFlareNum());
        saveRotableHitboxes(nbt);
        nbt.putInt("ingredientDropIndex", ingredientDropIndex);
	}
	
	@Override
	public void readSpawnData(FriendlyByteBuf buffer) {
		preset = buffer.readUtf();
		int weaponIndex = buffer.readInt();
		boolean gear = buffer.readBoolean();
		boolean freeLook = buffer.readBoolean();
		float throttle = buffer.readFloat();
		List<PartSlot> slots = PartsManager.readSlotsFromBuffer(buffer);
		// ORDER MATTERS
		// PRESET STUFF
		if (VehiclePresets.get().has(preset)) {
			vehicleStats = VehiclePresets.get().get(preset);
			assetId = vehicleStats.getAssetId();
			vehicleClientStats = VehicleClientPresets.get().get(assetId);
		}
		textureManager.read(buffer);
		soundManager.write(buffer);
		// ORDER MATTERS
		weaponSystem.setSelected(weaponIndex);
		partsManager.setPartSlots(slots);
		partsManager.clientPartsSetup();
		// OTHER
		setLandingGear(gear);
		setDriverCameraLocked(freeLook);
		setCurrentThrottle(throttle);
	}
	
	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeUtf(preset);
		buffer.writeInt(weaponSystem.getSelectedIndex());
		buffer.writeBoolean(isLandingGear());
		buffer.writeBoolean(isDriverCameraLocked());
		buffer.writeFloat(getCurrentThrottle());
		PartsManager.writeSlotsToBuffer(buffer, partsManager.getSlots());
		textureManager.write(buffer);
		soundManager.write(buffer);
	}
	
	@Override
	public void onRemovedFromWorld() {
		super.onRemovedFromWorld();
	}
	
	@Override
	public void onAddedToWorld() {
		super.onAddedToWorld();
	}
	
	public VehicleStats getStats() {
		return vehicleStats;
	}
	
	public abstract VehicleType getVehicleType();
	
	/**
	 * called on this entities first tick on client and server side
	 */
	public void init() {
		refreshDimensions();
		if (!level.isClientSide) serverSetup();
		else clientSetup();
		soundManager.loadSounds(vehicleStats);
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
		Quaternion q = getQBySide();
		setPrevQ(q);
		controlDirection(q);
		//q.normalize(); // this was causing horrendous precision errors in the hitbox colliders
		setQBySide(q);
		EulerAngles angles = UtilAngles.toDegrees(q);
		setXRotNoQ((float)angles.pitch);
		setYRotNoQ((float)angles.yaw);
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
		// HITBOXES
		tickHitboxes();
        // OTHER
		controlSystem();
        tickParts();
        tickWarnings();
		soundManager.onTick();
		textureManager.onTick();
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
	 * called externally to sync a vehicle's inputs with client's that aren't controlling this vehicle.
	 * normally used by {@link ToServerVehicleControl}
	 * which is called by {@link com.onewhohears.dscombat.client.event.forgebus.ClientInputEvents}
	 * to the controlling player can sync their inputs with every other client.
	 * could also be used by a server side AI to sync a vehicle's inputs with all client's.
	 */
	public void syncControlsToClient() {
		if (level.isClientSide) return;
		PacketHandler.INSTANCE.send(
			PacketDistributor.TRACKING_ENTITY.with(() -> this),
			new ToClientVehicleControl(this));
	}
	
	/**
	 * called on both client and server side
	 * damages plane if it falls or collides with a wall at speeds defined in config.
	 */
	public void tickCollisions() {
		// TODO 9.1 break grass and leaves or just weak blocks when driving through them
		if (!level.isClientSide) {
			knockBack(level.getEntities(this, 
					getBoundingBox(), 
					getKnockbackPredicate()));
			tickDismountSafety();
			if (!hasControllingPassenger()) wallCollisions();
		} else {
			if (isControlledByLocalInstance()) wallCollisions();
		}
	}
	
	protected void wallCollisions() {
		if (verticalCollision) verticalCollision();
		if (horizontalCollision && !minorHorizontalCollision) horizontalCollision();
	}
	
	protected void horizontalCollision() {
		double speed = prevMotion.horizontalDistance();
		double th = DSCPhyCons.COLLIDE_SPEED;
		if (speed > th) {
			float amount = (float)((speed-th)*DSCPhyCons.COLLIDE_DAMAGE_RATE);
			collideHurt(amount, false);
		}
	}
	
	protected void verticalCollision() {
		double my = Math.abs(prevMotion.y);
		double th = DSCPhyCons.COLLIDE_SPEED;
		if (isOperational() && isLandingGear() 
				&& Mth.abs(getXRot()) < 15f
				&& Mth.abs(zRot) < 15f) {
			th = DSCPhyCons.COLLIDE_SPEED_GEAR;
		}
		if (my > th) {
			float amount = (float)((my-th)*DSCPhyCons.COLLIDE_DAMAGE_RATE);
			collideHurt(amount, true);
		}
		if (isOperational() && isOnGround() && !isLandingGear() && getXZSpeed() > DSCPhyCons.COLLIDE_SPEED) {
			collideHurt(1, false);
		}
	}
	
	@Override
	public boolean causeFallDamage(float dist, float mult, DamageSource source) {
		verticalCollision();
		return true;
	}
	
	/**
	 * hurt the vehicle if there is a collision
	 * @param amount amount of damage done by collision
	 * @param isFall true if vertical collision, false if horizontal
	 */
	public void collideHurt(float amount, boolean isFall) {
		if (!level.isClientSide && tickCount > 200) {
			if (isFall) hurt(DamageSource.FALL, amount);
			else hurt(DamageSource.FLY_INTO_WALL, amount);
		} else if (level.isClientSide && isControlledByLocalInstance()) {
			PacketHandler.INSTANCE.sendToServer(new ToServerVehicleCollide(getId(), amount, isFall));
		}
	}
	
	protected Predicate<? super Entity> getKnockbackPredicate() {
		return ((entity) -> {
			if (entity.noPhysics) return false;
			if (entity.isSpectator()) return false;
			if (this.equals(entity.getRootVehicle())) return false;
			if (!(entity instanceof LivingEntity)) return false;
			if (entity instanceof Player p && p.isCreative()) return false;
			if (isFormerPassenger(entity)) return false;
 			return true;
		});
	}
	
	private void tickDismountSafety() {
		formerPassengersServer.forEach((id, time) -> { if (time > 0) --time; });
	}
	
	public boolean isFormerPassenger(Entity passenger) {
		return formerPassengersServer.containsKey(passenger.getId()) && formerPassengersServer.get(passenger.getId()) > 0;
	}
	
	public void onSeatDismount(Entity entity) {
		if (!level.isClientSide) formerPassengersServer.put(entity.getId(), DSCPhyCons.EJECT_SAFETY_COOLDOWN);
	}
	
	protected void knockBack(List<Entity> entities) {
		double push_factor = 10 * xzSpeed;
		if (push_factor < 2) push_factor = 2;
		double d0 = getBoundingBox().getCenter().x;
		double d1 = getBoundingBox().getCenter().z;
		for(Entity entity : entities) {
			if (entity.noPhysics) continue;
			double d2 = entity.getX() - d0;
			double d3 = entity.getZ() - d1;
			double d4 = Math.max(d2 * d2 + d3 * d3, 0.1);
			entity.push(d2/d4*push_factor, 0.2, d3/d4*push_factor);
			if (push_factor > 2) {
				entity.hurt(VehicleDamageSource.roadKill(this), (float)push_factor*2f);
			}
		}
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
	 */
	public void tickNoHealth() {
		++deadTicks;
		if (isFullyLooted()) {
			kill();
		}
	}
	
	public boolean isFullyLooted() {
		return !isOperational() && partsManager.isEmpty() && !canDropIngredients();
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
		UtilParticles.vehicleParticles(this);
		tickClientLandingGear();
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
		
		double my = move.y;
		if (my > getMaxClimbSpeed()) my = getMaxClimbSpeed();
		else if (my < -getMaxFallSpeed()) my = -getMaxFallSpeed();
		else if (Math.abs(my) < 0.001) my = 0;
		
		if (onGround && my < 0) my = -0.01; // THIS MUST BE BELOW ZERO
		setDeltaMovement(motionXZ.x, my, motionXZ.z);
	}
	
	public double getMaxClimbSpeed() {
		return DSCPhyCons.MAX_CLIMB_SPEED;
	}
	
	public double getMaxFallSpeed() {
		return DSCPhyCons.MAX_FALL_SPEED;
	}
	
	/**
	 * @return the max speed {@link EntityVehicle#motionClamp} tests for.
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
		if (!isTestMode()) {
			if (!isOperational()
					|| (cutThrottleOnNoPilot() && !hasControllingPassenger())
					|| (cutThrottleOnNoPassengers() && !isPlayerOrBotRiding())) {
				resetControls();
				return;
			}
			if (currentFuel <= 0 || isAllEnginesDamaged()) {
				throttleToZero();
				return;
			}
		}
		if (inputs.throttle > 0) increaseThrottle();
		else if (inputs.throttle < 0) decreaseThrottle();
	}
	
	public boolean cutThrottleOnNoPilot() {
		return true;
	}
	
	public boolean cutThrottleOnNoPassengers() {
		return true;
	}
	
	/**
	 * called every tick.
	 * reads player input to change the direction of the craft.
	 * also calls {@link EntityVehicle#directionGround(Quaternion)}, {@link EntityVehicle#directionWater(Quaternion)},
	 * and {@link EntityVehicle#directionAir(Quaternion)} to change direction based on the named conditions.
	 * @param q the current direction of the vehicle
	 */
	public void controlDirection(Quaternion q) {
		if (onGround) directionGround(q);
		else if (isInWater()) directionWater(q);
		else directionAir(q);
		Vec3 m = getMoment().add(addMomentBetweenTicks), av = getAngularVel();
		if (!UtilGeometry.isZero(m)) {
			av = av.add(m.x/vehicleStats.Ix, m.y/vehicleStats.Iy, m.z/vehicleStats.Iz);
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
				getADComponent(av.x, dx, vehicleStats.Ix),
				getADComponent(av.y, dy, vehicleStats.Iy),
				getADComponent(av.z, dz, vehicleStats.Iz)));
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
			float goalPitch = 0;
			if (isOnGround()) goalPitch = -vehicleStats.groundXTilt;
			float diff = (float)angles.pitch - goalPitch;
			if (Math.abs(diff) < dPitch) pitch = diff;
			else pitch = Math.signum(diff) * dPitch;
			x += pitch;
		}
		setAngularVel(new Vec3(x, av.y, z));
	}
	
	public void addMoment(Vec3 moment, boolean control) {
		Vec3 m = getMoment();
		double x = moment.x, y = moment.y, z = moment.z;
		if (control) {
			Vec3 av = getAngularVel();
			if (moment.x != 0) x = getControlMomentComponent(m.x, moment.x, av.x, getControlMaxDeltaPitch(), vehicleStats.Ix);
			if (moment.y != 0) y = getControlMomentComponent(m.y, moment.y, av.y, getControlMaxDeltaYaw(), vehicleStats.Iy);
			if (moment.z != 0) z = getControlMomentComponent(m.z, moment.z, av.z, getControlMaxDeltaRoll(), vehicleStats.Iz);
		}
		setMoment(m.add(x, y, z));
	}
	
	private double getControlMomentComponent(double cm, double m, double v, float max, float I) {
		if (Math.abs(v) > max && Math.signum(v) == Math.signum(m)) return 0;
		double m2 = cm + m;
		double a2 = m2 / I;
		double v2 = v + a2;
		double vd = Math.abs(v2) - max;
		if (vd > 0) m -= vd * Math.signum(v2) * I;
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
		totalMass = getEmptyVehicleMass() + partsManager.getPartsWeight();
		staticFric = totalMass * DSCPhyCons.GRAVITY * DSCPhyCons.STATIC_FRICTION;
		kineticFric = totalMass * DSCPhyCons.GRAVITY * DSCPhyCons.KINETIC_FRICTION;
		maxPushThrust = partsManager.getTotalPushThrust();
		maxSpinThrust = partsManager.getTotalSpinThrust();
		currentFuel = partsManager.getCurrentFuel();
		maxFuel = partsManager.getMaxFuel();
		hasFlares = partsManager.getFlares().size() > 0;
		airPressure = UtilEntity.getAirPressure(this);
		if (isOnGround()) ++groundTicks;
		else groundTicks = 0;
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
	
	public float getXZSpeed() {
		return xzSpeed;
	}
	
	public int getXZSpeedDir() {
		return xzSpeedDir;
	}
	
	/**
	 * called every tick on server and client side.
	 * after forces are calculated. see {@link EntityVehicle#tickMovement(Quaternion)}.
	 * F=M*A -> A=F/M then A is added to current velocity
	 */
	public void calcAcc() {
		//if (!addForceBetweenTicks.equals(Vec3.ZERO)) System.out.println("add force "+addForceBetweenTicks+" "+this);
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
	 * {@link EntityVehicle#tickAlways(Quaternion)},
	 * {@link EntityVehicle#tickGround(Quaternion)},
	 * {@link EntityVehicle#tickGroundWater(Quaternion)},
	 * {@link EntityVehicle#tickWater(Quaternion)},
	 * {@link EntityVehicle#tickAir(Quaternion)}.
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
	 * called by {@link EntityVehicle#tickMovement(Quaternion)}.
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
	 * called by {@link EntityVehicle#tickMovement(Quaternion)}.
	 * @param q the plane's current rotation
	 */
	public void tickGround(Quaternion q) {
		Vec3 n = UtilAngles.rotationToVector(getYRot(), 0);
		if (isSliding() || willSlideFromTurn()) {
			setDeltaMovement(getDeltaMovement().add(n.scale(getDriveAcc() * slideAngleCos)));
			addFrictionForce(kineticFric);
			//debug("SLIDING");
		} else {
			setDeltaMovement(n.scale(xzSpeed*xzSpeedDir + getDriveAcc()));
			if (getCurrentThrottle() == 0 && xzSpeed != 0) 
				addFrictionForce(DSCPhyCons.DRIVE_FRICTION);
		}
		if (isBraking() && isOperational()) applyBreaks();
	}
	
	/**
	 * @return the acceleration while driving 
	 */
	public double getDriveAcc() {
		return getSpinThrustMag()/getTotalMass();
	}
	
	public boolean isBraking() {
		return false;
	}
	
	public abstract boolean canBrake();
	
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
	 * called by {@link EntityVehicle#tickMovement(Quaternion)}.
	 * @param q the plane's current rotation
	 */
	public void tickAir(Quaternion q) {
		setForces(getForces().add(getDragForce(q)));
		resetFallDistance();
	}
	
	/**
	 * called on both client and server side every tick to calculate the vehicle's forces in when floating in water.
	 * called by {@link EntityVehicle#tickMovement(Quaternion)}.
	 * @param q the plane's current rotation
	 */
	public void tickWater(Quaternion q) {
		setForces(getForces().add(getDragForce(q)));
	}
	
	/**
	 * called on both client and server side every tick to calculate the vehicle's forces when at the bottom of water.
	 * called by {@link EntityVehicle#tickMovement(Quaternion)}.
	 * @param q the plane's current rotation
	 */
	public void tickGroundWater(Quaternion q) {
		tickGround(q);
		tickWater(q);
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
		double d = airPressure * speedSqr * getCrossSectionArea();
		if (isInWater()) d *= DSCPhyCons.DRAG_WATER;
		else d *= DSCPhyCons.DRAG;
		return d;
	}
	
	/**
	 * modifies {@link EntityVehicle#getBaseCrossSecArea()} for physics.
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
		return new Vec3(0, -getTotalMass() * DSCPhyCons.GRAVITY, 0);
	}
	
	/**
	 * @return this mass of the vehicle plus the mass of the parts
	 */
	public float getTotalMass() {
		if (Float.isNaN(totalMass)) {
			LOGGER.warn("ERROR: NAN MASS? setting to 10000 | "+this);
			totalMass = 10000;
		} else if (totalMass == 0) {
			LOGGER.warn("ERROR: 0 MASS? setting to 10000 | "+this);
			totalMass = 10000;
		}
		return totalMass;
	}
	
	/**
	 * this is NOT the total mass. 
	 * see {@link EntityVehicle#getTotalMass()}
	 * @return the mass of the vehicle. not including parts.
	 */
	public final float getEmptyVehicleMass() {
		return vehicleStats.mass;
	}
	
	/**
	 * fired on both client and server side to control the plane's weapons, flares, open menu
	 */
	public void controlSystem() {
		if (!isOperational()) return;
		radarSystem.tick();
		Entity controller = getControllingPassenger();
		if (!level.isClientSide) {
			weaponSystem.serverTick();
			if (controller == null) return;
			boolean consume = !isNoConsume();
			if (controller instanceof ServerPlayer player) {
				if (inputs.openMenu) openMenu(player);
				if (player.isCreative()) consume = false;
			}
			boolean consumeFuel = level.getGameRules().getBoolean(DSCGameRules.CONSUME_FULE);
			if (consume && consumeFuel) tickFuel();
			setFlareNum(partsManager.getNumFlares());
			if (inputs.flare && tickCount - flareTicks >= 10) {
				boolean consumeFlares = level.getGameRules().getBoolean(DSCGameRules.CONSUME_FLARES);
				flare(controller, consume && consumeFlares);
			}
		}
	}
	
	public void openMenu(ServerPlayer player) {
		if (!canOpenMenu()) {
			player.displayClientMessage(UtilMCText.translatable(getOpenMenuError()), true);
			return;
		}
		NetworkHooks.openScreen(player, 
			new SimpleMenuProvider((windowId, playerInv, p) -> 
				new VehicleContainerMenu(windowId, playerInv), 
				UtilMCText.translatable("container.dscombat.plane_menu")));
	}
	
	public void openStorage(ServerPlayer player) {
		if (!canOpenMenu()) {
			player.displayClientMessage(UtilMCText.translatable(getOpenMenuError()), true);
			return;
		}
		StorageInstance<?> box = partsManager.cycleStorageData();
		if (box == null) {
			player.displayClientMessage(UtilMCText.translatable("error.dscombat.no_storage_boxes"), true);
			return;
		}
		NetworkHooks.openScreen(player, 
			new SimpleMenuProvider((windowId, playerInventory, p) -> 
				box.createMenu(windowId, playerInventory), 
				UtilMCText.translatable("container.dscombat.vehicle_storage")));
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
		flareTicks = tickCount;
	}
	
	/**
	 * ticks the parts manager on client and server side
	 */
	public void tickParts() {
		findGimbalForPilotCamera();
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
		if (teleport) lerpSteps = posRotationIncrements;
		else lerpSteps = 1;
    }
	
	private void tickLerp() {
		if (isControlledByLocalInstance()) {
			syncPacketPositionCodec(getX(), getY(), getZ());
			syncMoveRot();
			lerpSteps = 0;
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
	
	private void syncMoveRot() {
		if (!level.isClientSide || tickCount % 10 != 0 || firstTick) return;
		PacketHandler.INSTANCE.sendToServer(new ToServerVehicleMoveRot(this));
	}
	
	/**
	 * called to reset controls if there is no pilot
	 */
	public void resetControls() {
		inputs.reset();
		throttleToZero();
	}
	
	public final boolean isDriverCameraLocked() {
    	return this.isDriverCameraLocked;
    }
    
    public final void setDriverCameraLocked(boolean driverCameraLocked) {
    	this.isDriverCameraLocked = driverCameraLocked;
    }
    
    public RadarMode getRadarMode() {
    	return entityData.get(RADAR_MODE);
    }
    
    public void setRadarMode(RadarMode mode) {
    	entityData.set(RADAR_MODE, mode);
    }
    
    public void cycleRadarMode() {
    	setRadarMode(getRadarMode().cycle());
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
     * called in {@link EntityVehicle#init()} by server side
     */
	public void serverSetup() {
		partsManager.setupParts();
	}
	
	/**
	 * called in {@link EntityVehicle#init()} by client side
	 */
	public void clientSetup() {
		soundManager.onClientInit();
	}
	
	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (xzSpeed > 0.2) return InteractionResult.PASS;
		if (player.isSecondaryUseActive()) return InteractionResult.PASS;
		if (player.getRootVehicle() != null && player.getRootVehicle().equals(this)) return InteractionResult.PASS;
		ItemStack stack = player.getInventory().getSelected();
		if (!stack.isEmpty()) {
			InteractionResult result = onItemInteract(player, hand, stack);
			if (result != InteractionResult.FAIL) return result;
		}
		if (!isOperational()) return onDestroyedInteract(player, hand);
		if (!level.isClientSide) return rideAvailableSeat(player) ? InteractionResult.CONSUME : InteractionResult.PASS;
		else {
			Minecraft m = Minecraft.getInstance();
			if (m.player.equals(player)) DSCClientInputs.centerMousePos();
		}
		return InteractionResult.SUCCESS;
	}
	
	protected InteractionResult onItemInteract(Player player, InteractionHand hand, ItemStack stack) {
		if (stack.is(ModTags.Items.SPRAY_CAN)) return onSprayCanInteract(player, hand, stack);
		if (!level.isClientSide) {
			Item item = stack.getItem();
			// INTERACT ITEMS
			if (item instanceof VehicleInteractItem vii) 
				return vii.onServerInteract(this, stack, player, hand);
			// CHECK OPERATIONAL
			else if (!isOperational()) return InteractionResult.FAIL;
			// GAS CAN
			else if (stack.is(ModTags.Items.GAS_CAN)) 
				return onGasCanInteract(player, hand, stack);
			// OIL BUCKET
			else if (stack.is(ModTags.Items.FORGE_OIL_BUCKET)) 
				return onOilBucketInteract(player, hand, stack);
			// VEHICLE CHAIN
			else if (stack.is(ModTags.Items.VEHICLE_CHAIN)) 
				return onChainInteract(player, hand, stack);
			// RADIO
			else if (item instanceof RecordItem disk) {
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
			}
		} 
		return InteractionResult.FAIL;
	}
	
	protected InteractionResult onGasCanInteract(Player player, InteractionHand hand, ItemStack stack) {
		int md = stack.getMaxDamage();
		int d = stack.getDamageValue();
		int r = (int)addFuel(md-d);
		stack.setDamageValue(md-r);
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
	
	protected InteractionResult onOilBucketInteract(Player player, InteractionHand hand, ItemStack stack) {
		float fuelPerBucket = (float)DSCGameRules.getFuelPerOilBlock(level);
		if (addFuel(fuelPerBucket) == fuelPerBucket) return InteractionResult.PASS;
		ItemStack remain = stack.getCraftingRemainingItem();
		player.getInventory().setItem(player.getInventory().selected, remain);
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
	
	protected InteractionResult onSprayCanInteract(Player player, InteractionHand hand, ItemStack stack) {
		if (level.isClientSide) UtilClientPacket.openVehicleTextureScreen(textureManager);
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
	
	protected InteractionResult onChainInteract(Player player, InteractionHand hand, ItemStack stack) {
		List<EntityChainHook> hooks = level.getEntitiesOfClass(EntityChainHook.class, 
				getBoundingBox().inflate(EntityChainHook.CHAIN_LENGTH), hook -> hook.isPlayerConnected(player));
		/*if (hooks.size() == 0) {
			chainToPlayer(player);
			return InteractionResult.sidedSuccess(level.isClientSide);
		}*/
		for (EntityChainHook hook : hooks) {
			if (hook.addVehicleConnection(player, this)) {
				chainToHook(hook);
				stack.shrink(1);
				break;
			}
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
	
	protected InteractionResult onDestroyedInteract(Player player, InteractionHand hand) {
		if (partsManager.dropPartItem()) {
			playTheftSound();
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		if (dropIngredient()) {
			playTheftSound();
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return InteractionResult.PASS;
	}
	
	public boolean dropIngredient() {
		if (level.isClientSide) return false;
		while (canDropIngredients()) {
			++ingredientDropIndex;
			Ingredient ing = getStats().getIngredients().get(ingredientDropIndex);
			for (int i = 0; i < ing.getItems().length; ++i) {
				if (ing.getItems()[i].is(ModTags.Items.RECOVERABLE)) {
					ItemStack stack = ing.getItems()[i].copy();
					stack.setCount(UtilRandom.weightedRandomInt(stack.getCount(), Config.COMMON.recoverPartWeight.get()));
					UtilEntity.dropItemStack(this, stack);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean canDropIngredients() {
		return ingredientDropIndex < getStats().getIngredients().size() - 1;
	}
	
	public void playTheftSound() {
		level.playSound(null, this, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, 
				getSoundSource(), 0.5f, 1.0f);
	}
	
	@Nullable
	public Player getChainHolderPlayer() {
		return chainHolderPlayer;
	}
	
	@Nullable
	public EntityChainHook getChainHolderHook() {
		return chainHolderHook;
	}
	/**
	 * packets are handled by hook
	 */
	public void chainToHook(EntityChainHook hook) {
		chainHolderPlayer = null;
		chainHolderHook = hook;
	}
	// FIXME 7 chainToPlayer(Player player) does nothing right now
	public boolean chainToPlayer(Player player) {
		chainHolderPlayer = player;
		chainHolderHook = null;
		if (!level.isClientSide) UtilServerPacket.sendVehicleAddPlayer(this, player);
		return true;
	}
	/**
	 * packets are handled by hook
	 */
	public void disconnectChain() {
		chainHolderPlayer = null;
		chainHolderHook = null;
	}
	
	public boolean isChainConnectedToPlayer(Player player) {
		return getChainHolderPlayer() != null && getChainHolderPlayer().equals(player);
	}
	
	public void playRepairSound() {
		SoundEvent sound;
		if (isMaxHealth()) sound = SoundEvents.ANVIL_USE;
		else sound = SoundEvents.ANVIL_PLACE;
		level.playSound(null, this, sound, 
				getSoundSource(), 0.5f, 1.0f);
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
			passenger.setPos(convertRelPos(part.getRelativePos()));
			return;
		}
	}
	
	public Vec3 convertRelPos(Vec3 rel_pos) {
		return UtilAngles.rotateVector(rel_pos, getQBySide()).add(position());
	}
	
	@Nullable
	@Override
    public Entity getControllingPassenger() {
        for (EntitySeat seat : getSeats()) 
        	if (seat.isPilotSeat()) 
        		return seat.getPlayer();
        return null;
    }
	
	@Nullable
	public Entity getControllingPlayerOrBot() {
		Player playerAlt = null;
		Entity alt = null;
		for (EntitySeat seat : getSeats()) {
			if (seat.isPilotSeat()) {
				Player player = seat.getPlayer();
				if (player != null) return player;
			}
			if (playerAlt == null) {
				Player player = seat.getPlayer();
				if (player != null) playerAlt = player;
			}
			if (alt == null && seat.hasAIUsingTurret()) {
				Entity mob = seat.getPassenger();
				if (mob != null) alt = mob;
			}
		}
		return playerAlt != null ? playerAlt : alt;
	}
	
	public boolean isPlayerRiding() {
		for (EntitySeat seat : getSeats()) 
			if (seat.getPlayer() != null) 
				return true;
		return false;
	}
	
	public boolean isBotUsingRadar() {
		for (EntityTurret turret : getTurrets()) 
			if (turret.isBotUsingRadar()) 
				return true;
		return false;
	}
	
	public boolean isPlayerOrBotRiding() {
		for (EntitySeat seat : getSeats()) 
			if (seat.isPlayerOrBotRiding()) 
				return true;
		return false;
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
	
	@Nullable
	public EntitySeat getPassengerSeat(Entity p) {
		for (EntitySeat seat : getSeats()) 
			if (p.equals(seat.getPassenger())) 
				return seat;
		return null;
	}

	// TODO: explore potential override of #getPassengers and just do this there (or even just rename this method)
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
	public boolean hasPassenger(@NotNull Predicate<Entity> pPredicate) {
		for (EntitySeat seat : this.getSeats()) {
			if (pPredicate.test(seat.getPassenger())) return true;
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
	public boolean isPickable() {
		return rootHitboxEntityInteract();
	}
	
	@Override
	public boolean isAttackable() {
		return rootHitboxEntityInteract();
	}

	@Override
	public boolean isAlive() {
		return rootHitboxEntityInteract();
	}

    @Override
    public boolean canBeCollidedWith() {
    	return rootHitboxEntityInteract();
    }
    
    @Override
    public boolean canCollideWith(Entity entity) {
    	if (!super.canCollideWith(entity)) return false;
    	if (entity.isPushable()) return false;
    	if (isHitboxParent(entity)) return false;
    	return true;
    }
    
	@Override
    public boolean hurt(DamageSource source, float amount) {
		return hurtLogic(source, amount, null);
	}
	
	public boolean hurtHitbox(DamageSource source, float amount, RotableHitbox hitbox) {
		return hurtLogic(source, amount, hitbox, hitbox.getHitboxData().isDamageRoot());
	}
	
	public boolean hurtLogic(DamageSource source, float amount, @Nullable RotableHitbox hitbox, boolean hurtRoot) {
		if (isInvulnerableTo(source)) return false;
		if (source.isFire()) hurtByFireTime = tickCount;
		soundManager.onHurt(source, amount);
		damage(source, amount, hitbox, hurtRoot);
		if (!level.isClientSide) {
			if (!isOperational()) {
				partsManager.damageAllParts();
				checkExplodeWhenKilled(source);
			} else {
				damageParts(source, amount, hitbox, hurtRoot);
			}
		}
		return true;
	}
	
	public boolean hurtLogic(DamageSource source, float amount, @Nullable RotableHitbox hitbox) {
		return hurtLogic(source, amount, hitbox, true);
	}
	
	protected void damage(DamageSource source, float amount, @Nullable RotableHitbox hitbox) {
		damage(source, amount, hitbox, true);
	}
	
	protected void damage(DamageSource source, float amount, @Nullable RotableHitbox hitbox, boolean hurtRoot) {
		if (shouldDebug(source)) 
			System.out.println("amount "+amount+" "+level.isClientSide+" attacked by "+source.getDirectEntity()+" as "+source+" hitbox "+hitbox);
		if (source.getDirectEntity() != null && source.getDirectEntity().getType().is(ModTags.EntityTypes.PROJECTILE)) 
			amount = calcDamageFromBullet(source, amount);
		float armorDamage = calcDamageToArmor(amount);
		float healthDamageWithArmorPercent = getHealthDamageWithArmorPercent(source);
		float healthDamage = armorDamage * healthDamageWithArmorPercent;
		armorDamage *= (1 - healthDamageWithArmorPercent);
		// damage root
		if (hurtRoot) { 
			if (getArmor() > 0) {
				float remainingArmor = Math.min(getArmor() - armorDamage, 0);
				addArmor(-armorDamage);
				addHealth(-healthDamage + remainingArmor);
			} else {
				addHealth(-amount);
			}
		}
		// damage hitbox
		if (hitbox != null) {
			if (hitbox.getArmor() > 0) {
				float remainingArmor = Math.min(hitbox.getArmor() - armorDamage, 0);
				hitbox.addArmor(-armorDamage);
				hitbox.addHealth(-healthDamage + remainingArmor);
			} else {
				hitbox.addHealth(-amount);
			}
		}
		if (shouldDebug(source)) {
			System.out.println("vehicle health: "+getHealth()+" armor "+getArmor());
			if (hitbox != null) System.out.println("hitbox health: "+hitbox.getHealth()+" armor "+hitbox.getArmor());
		}
	}
	
	public static float getHealthDamageWithArmorPercent(DamageSource source) {
		if (source.isExplosion()) return 0.2f;
		else if (source.isBypassArmor()) return 0.8f;
		return 0; 
	}
	
	public float calcDamageToArmor(float amount) {
		return Math.max(0, reduceByPercent(amount, 
				vehicleStats.armor_damage_absorbtion * DSCGameRules.getVehicleArmorStrengthFactor(level)) 
				- vehicleStats.armor_damage_threshold);
	}
	
	public float calcDamageToInside(DamageSource source, float amount) {
		return calcDamageToArmor(amount) * getHealthDamageWithArmorPercent(source);
	}
	
	private boolean shouldDebug(DamageSource source) {
		//return source.getMsgId().equals("flyIntoWall");
		return !source.isFire();
	}
	
	protected float calcDamageFromBullet(DamageSource source, float amount) {
		return amount * DSCGameRules.getBulletDamageVehicleFactor(level);
	}
	
	private static float reduceByPercent(float amount, float percent) {
		return Math.max(amount - amount * percent, 0);
	}
	
	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		if (isTestMode()) return true;
		if (super.isInvulnerableTo(source)) return true;
		if (source.isFire() && (tickCount-hurtByFireTime) < 10) return true;
		if (isVehicleOf(source.getEntity())) return true;
		return false;
	}
	
	public void damageParts(DamageSource source, float amount, @Nullable RotableHitbox hitbox, boolean hurtRoot) {
		if (hitbox != null && hitbox.getHitboxData().isDamageParts() && hitbox.isDestroyed()) {
			partsManager.damageAllHitboxParts(hitbox.getHitboxName());
			return;
		}
		boolean damageRoot = shouldDamageRoot(hitbox, hurtRoot);
		float healthPercent;
		if (damageRoot) healthPercent = getHealth() / getMaxHealth();
		else {
			if (!hitbox.getHitboxData().isDamageParts()) return;
			healthPercent = hitbox.getHealth() / hitbox.getMaxHealth();
		}
		if (healthPercent > 0.5f) return;
		float damagePercent = (1f-healthPercent*2f)*amount*0.1f;
		if (random.nextFloat() > damagePercent) return;
		if (damageRoot) partsManager.damageRootPart();
		else partsManager.damageHitboxPart(hitbox.getHitboxName());
	}
	
	private boolean shouldDamageRoot(@Nullable RotableHitbox hitbox, boolean hurtRoot) {
		return hurtRoot && (hitbox == null || hitbox.isDestroyed());
	}
	
	protected boolean checkExplodeWhenKilled(DamageSource source) {
		if (source.getMsgId().equals(DamageSource.FALL.getMsgId())) {
			explode(VehicleDamageSource.fall(this));
			return true;
		} else if (source.getMsgId().equals(DamageSource.FLY_INTO_WALL.getMsgId())) {
			explode(VehicleDamageSource.collide(this));
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
		addForceBetweenTicks = addForceBetweenTicks.add(force);
		addMomentBetweenTicks = addMomentBetweenTicks.add(moment);
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), 
				new ToClientAddForceMoment(this, force, moment));
	}
	
	public void explode(DamageSource source) {
		if (level.isClientSide) return;
		level.explode(this, source,
			null, getX(), getY(), getZ(), 
			vehicleStats.crashExplosionRadius, true, 
			Explosion.BlockInteraction.BREAK);
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), 
				new ToClientVehicleExplode(this));
	}
	
	/**
	 * ignoring vanilla explosion effects.
	 * see {@link EntityVehicle#customExplosionHandler(Explosion)}
	 * for custom explosion handling.
	 */
	@Override
	public boolean ignoreExplosion() {
		return true;
	}
	
	public void customExplosionHandler(Explosion exp, @Nullable RotableHitbox hitbox) {
		Entity entity = (hitbox == null) ? this : hitbox;
		Vec3 s = exp.getPosition();
		Vec3 b = getClosest(s, hitbox);
		Vec3 r = b.subtract(entity.position());
		
		float diameter = exp.radius * 2f;
		double dist_check = Math.sqrt(b.distanceToSqr(s)) / (double)diameter;
		if (dist_check > 1.0d) return;
		
		Entity exp_entity = exp.getDamageSource().getDirectEntity();
		double dx = b.x - s.x;
        double dy = b.y - s.y;
        double dz = b.z - s.z;
        double d = Math.sqrt(dx*dx + dy*dy + dz*dz);
        if (Double.isNaN(d) || Double.isInfinite(d)) return;
        if (d == 0) {
        	if (exp_entity != null) {
        		Vec3 dir = exp_entity.getLookAngle();
        		dx = dir.x; dy = dir.y; dz = dir.z;
        	} else { dx = 0; dy = 1; dz = 0; }
        } else { dx /= d; dy /= d; dz /= d; }
        
        double seen_percent = Explosion.getSeenPercent(s, entity);
        double exp_factor = (1.0D - dist_check) * seen_percent;
        
        float amount = (float)((int)((exp_factor*exp_factor+exp_factor)*3.5d*(double)diameter+1d));
        amount *= DSCGameRules.getExplodeDamagerVehicleFactor(level);
        
        if (hitbox != null) hurtLogic(exp.getDamageSource(), amount, hitbox, false);
        else hurtLogic(exp.getDamageSource(), amount, null);
        
        Vec3 force = new Vec3(dx*exp_factor, dy*exp_factor, dz*exp_factor).scale(DSCPhyCons.EXP_FORCE_FACTOR);
        
		Vec3 f;
		if (s.equals(b) && exp_entity != null) 
			f = exp_entity.getDeltaMovement().normalize().scale(exp_factor*DSCPhyCons.EXP_MOMENT_FACTOR);
		else f = s.subtract(b).normalize().scale(exp_factor*DSCPhyCons.EXP_MOMENT_FACTOR);
		Vec3 moment = r.cross(f);
		
		addForceMomentToClient(force, moment);
	}
	
	@Override
	public void customExplosionHandler(Explosion exp) {
		customExplosionHandler(exp, null);
	}
	
	private Vec3 getClosest(Vec3 pos, @Nullable RotableHitbox hitbox) {
		if (hitbox == null) return UtilGeometry.getClosestPointOnAABB(pos, getBoundingBox());
		Optional<Vec3> clip = hitbox.getHitbox().clip(pos, hitbox.position());
		return clip.orElseGet(() -> hitbox.position());
	}
	
	/**
	 * @return the max speed of the craft along the x and z axis
	 */
    public final float getMaxSpeed() {
    	return vehicleStats.max_speed;
    }
    
    /**
     * @return between 1 and 0 or 1 and -1 if negativeThrottle
     */
    public final float getCurrentThrottle() {
    	return throttle;
    }
    
    /**
     * @param throttle between 1 and 0 or 1 and -1 if negativeThrottle
     */
    public final void setCurrentThrottle(float throttle) {
    	if (throttle > 1) throttle = 1;
    	else if (vehicleStats.negativeThrottle && throttle < -1) throttle = -1;
    	else if (!vehicleStats.negativeThrottle && throttle < 0) throttle = 0;
    	this.throttle = throttle;
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
    
    public void throttleTowards(float throttle) {
    	setCurrentThrottle(Mth.approach(getCurrentThrottle(), throttle, getThrottleIncreaseRate()));
    }
    
    public final float getThrottleIncreaseRate() {
    	return vehicleStats.throttleup;
    }
    
    public final float getThrottleDecreaseRate() {
    	return vehicleStats.throttledown;
    }
    
    public final float getMaxDeltaPitch() {
    	return vehicleStats.maxpitch;
    }
    
    public final float getMaxDeltaYaw() {
    	return vehicleStats.maxyaw;
    }
    
    public final float getMaxDeltaRoll() {
    	return vehicleStats.maxroll;
    }
    
    public final float getPitchTorque() {
    	return vehicleStats.torquepitch;
    }
    
    public final float getYawTorque() {
    	return vehicleStats.torqueyaw;
    }
    
    public final float getRollTorque() {
    	return vehicleStats.torqueroll;
    }
    
    public void increaseThrottle() {
    	setCurrentThrottle(getCurrentThrottle() + getThrottleIncreaseRate());
    }
    
    public void decreaseThrottle() {
    	setCurrentThrottle(getCurrentThrottle() - getThrottleDecreaseRate());
    }
    
    public final Quaternion getQBySide() {
    	if (level.isClientSide) return getClientQ();
    	else return getQ();
    }
    
    public final void setQBySide(Quaternion q) {
    	if (level.isClientSide) setClientQ(q);
    	else setQ(q);
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
    
    public Quaternion getClientQ(float partialTicks) {
    	return UtilAngles.lerpQ(partialTicks, getPrevQ(), getClientQ());
    }
    
    public final float getBaseCrossSecArea() {
    	return vehicleStats.cross_sec_area;
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
    	return vehicleStats.stealth;
    }
    
    /**
     * @return the item stack with all of this plane's data 
     */
    public ItemStack getItem() {
    	ItemStack stack = vehicleStats.getItem();
    	CompoundTag tag = new CompoundTag();
    	addAdditionalSaveData(tag);
    	CompoundTag eTag = new CompoundTag();
    	eTag.put("EntityTag", tag);
    	stack.setTag(eTag);
    	return stack;
    }
    
    public boolean canBecomeItem() {
    	int fresh = level.getGameRules().getInt(DSCGameRules.ITEM_COOLDOWN_VEHICLE_FRESH);
    	int shoot = level.getGameRules().getInt(DSCGameRules.ITEM_COOLDOWN_VEHICLE_SHOOT);
    	return tickCount/20 > fresh && (lastShootTime == -1 || (tickCount-lastShootTime)/20 > shoot);
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
    	if (level.isClientSide) return;
    	becomeItem(position());
    }
    
    @Override
    public ItemStack getPickResult() {
    	return getItem();
    }
    
    @Override
	public boolean shouldRenderAtSqrDistance(double dist) {
		return dist < 102400;
	}
    
    public final float getArmor() {
    	return entityData.get(ARMOR);
    }
    
    public final void setArmor(float armor) {
    	float max = getMaxTotalArmor();
    	if (armor > max) armor = max;
    	else if (armor < 0) armor = 0;
    	entityData.set(ARMOR, armor);
    }
    
    public final void addArmor(float armor) {
    	setArmor(getArmor()+armor);
    }
    
    public final float getMaxHealth() {
    	return vehicleStats.max_health;
    }
    
    public final void addHealth(float h) {
    	setHealth(getHealth()+h);
    }
    
    public final void setHealth(float h) {
    	float max = getMaxHealth();
    	if (h > max) h = max;
    	else if (h < 0) h = 0;
    	if (h > 0) {
    		deadTicks = 0;
    		ingredientDropIndex = -1;
    	}
    	entityData.set(HEALTH, h);
    }
    
    public final float getHealth() {
    	return entityData.get(HEALTH);
    }
    
    public boolean isMaxHealth() {
    	return getHealth() >= getMaxHealth();
    }
    
    public void repairAll() {
    	if (level.isClientSide) return;
    	addHealth(100000);
		addArmor(100000);
		repairAllHitboxes();
    	repairAllParts();
		playRepairSound();
    }
    
    public int onRepairTool(float repair) {
    	if (level.isClientSide) return 0;
    	int damage = 0;
    	if (getHealth() < getMaxHealth()) {
    		addHealth(repair);
    		damage = 1;
    	} else if (repairOneHitbox(repair)) {
    		damage = 1;
    	} else if (getArmor() < getMaxTotalArmor()) {
    		addArmor(repair);
    		damage = 1;
    	}
    	else return 0;
    	playRepairSound();
    	return damage;
    }
    
    public void repairAllParts() {
    	if (level.isClientSide) return;
    	partsManager.repairAllParts();
    }
    
    public void repairAllHitboxes() {
    	if (level.isClientSide) return;
    	for (RotableHitbox h : hitboxes) h.fullyRepair();
    }
    
    public boolean repairOneHitbox(float repair) {
    	for (RotableHitbox h : hitboxes) {
    		if (h.isDamaged()) {
    			h.repair(repair);
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * divide this by distance squared when ir missile compares this heat value with others
     * @return the total heat value
     */
    public float getIRHeat() {
    	return getIdleHeat() + Math.abs(getCurrentThrottle()) * getEngineHeat();
    }
    
    /**
     * @return the heat value this vehicle always emits
     */
    public final float getIdleHeat() {
    	return vehicleStats.idleheat;
    }
    
    /**
     * @return the heat this plane's engines emit at max throttle 
     */
    public float getEngineHeat() {
    	return partsManager.getTotalEngineHeat();
    }
    
    public final float getBaseArmor() {
    	return vehicleStats.base_armor;
    }
    
    public float getMaxTotalArmor() {
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
    
    public EntityPart getPilotSeat() {
    	return getPartBySlotId(PartSlot.PILOT_SLOT_NAME);
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
    
    public List<EntityGimbal> getGimbals() {
    	List<EntityGimbal> gimbals = new ArrayList<>();
    	for (Entity e : getPassengers())
    		if (e instanceof EntityGimbal gimbal)
    			gimbals.add(gimbal);
    	return gimbals;
    }
    
    protected void findGimbalForPilotCamera() {
    	List<EntityGimbal> gimbals = getGimbals();
    	if (gimbals.size() == 0) pilotGimbal = null;
    	else pilotGimbal = gimbals.get(0);
    }
    
    @Nullable
    public EntityGimbal getGimbalForPilotCamera() {
    	return pilotGimbal;
    }
    
    @Nullable
    public EntityPart getPartBySlotId(String slotId) {
    	for (Entity e : getPassengers())
    		if (e instanceof EntityPart part && part.getSlotId().equals(slotId)) 
    			return part;
    	return null;
    }
    
    /**
     * entity tracking missile calls this server side when tracking this plane
     * @param pos the position of the missile
     */
    public void trackedByMissile(Entity missile) {
    	if (hasControllingPassenger()) radarSystem.addRWRWarning(
    			missile.getId(), missile.position(), true, false);
    }
    
    /**
     * another radar system calls this server side when tracking this craft
     * @param pos the position of the radar
     */
    public void lockedOnto(Entity radar) {
    	if (hasControllingPassenger()) radarSystem.addRWRWarning(
    			radar.getId(), radar.position(), false, 
    			UtilEntity.isOnGroundOrWater(radar));
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
    public EntityDimensions getDimensions(Pose pose) {
    	if (vehicleStats == null) return super.getDimensions(pose);
    	return vehicleStats.dimensions;
    }
    
    @Override
    protected AABB makeBoundingBox() {
    	if (isCustomBoundingBox()) return makeCustomBoundingBox();
     	return getDimensions(getPose()).makeBoundingBox(position());
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
     * @return if this entity should use {@link EntityVehicle#makeCustomBoundingBox()}
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
    	return getBoundingBox().inflate(vehicleStats.cameraDistance);
    }
    
    @Override
    public double getEyeY() {
    	return getBoundingBox().getCenter().y;
    }
    
    /**
     * @return the total fuel this vehicle can hold
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
    
    public void refillAll() {
    	if (level.isClientSide) return;
    	refillFuel();
		refillAllWeapons();
    }
    
    public void refillFlares() {
    	if (level.isClientSide) return;
    	partsManager.addFlares(100000);
    }
    
    public void refillFuel() {
    	if (level.isClientSide) return;
    	addFuel(100000);
    	level.playSound(null, this, SoundEvents.BREWING_STAND_BREW, 
    			SoundSource.PLAYERS, 1f, 1f);
    }
    
    public void refillAllWeapons() {
    	if (level.isClientSide) return;
    	refillFlares();
    	weaponSystem.refillAll();
		for (EntityTurret t : getTurrets()) {
			WeaponInstance<?> wd = t.getWeaponData();
			if (wd == null) continue;
			wd.addAmmo(100000);
			t.setAmmo(wd.getCurrentAmmo());
			t.updateDataAmmo();
		}
		level.playSound(null, this, SoundEvents.VILLAGER_WORK_TOOLSMITH, 
    			SoundSource.PLAYERS, 1f, 1f);
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
    
    public final float getTurnRadius() {
    	return vehicleStats.turn_radius;
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
     * @param flares a number of flares to sync with the client
     */
    public void setFlareNum(int flares) {
    	entityData.set(FLARE_NUM, flares);
    }
    
    public boolean hasFlares() {
    	return hasFlares;
    }
    
    public void debug(String debug) {
    	debug(debug, true);
    }
    
    public void debug(String debug, boolean passengerCheck) {
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
    
    public boolean isFlapsDown() {
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
    	Entity c = getControllingPlayerOrBot();
		if (c != null) return team.isAlliedTo(c.getTeam());
    	return super.isAlliedTo(team);
    }
	
	public List<RotableHitbox> getHitboxes() {
		return hitboxes;
	}
	
	@Nullable
	public RotableHitbox getHitboxByName(String name) {
		for (int i = 0; i < hitboxes.size(); ++i) 
			if (hitboxes.get(i).getHitboxName().equals(name)) 
				return hitboxes.get(i);
		return null;
	}
	
	public boolean isHitboxParent(Entity hitbox) {
		for (int i = 0; i < hitboxes.size(); ++i) 
			if (hitboxes.get(i).equals(hitbox)) 
				return true;
		return false;
	}
	
	protected void createRotableHitboxes(CompoundTag nbt) {
		CompoundTag hitbox_data = nbt.getCompound("hitbox_data");
		hitboxes.clear();
		hitboxes.addAll(vehicleStats.createRotableHitboxes(this));
		for (int i = 0; i < hitboxes.size(); ++i) {
			RotableHitbox hitbox = hitboxes.get(i); 
			hitbox.setPos(position());
			hitbox.readNbt(hitbox_data);
			hitbox.setId(ENTITY_COUNTER.incrementAndGet());
			level.addFreshEntity(hitbox);
		}
	}
	
	protected void saveRotableHitboxes(CompoundTag nbt) {
		CompoundTag hitbox_data = new CompoundTag();
		for (int i = 0; i < hitboxes.size(); ++i) 
			hitboxes.get(i).writeNbt(hitbox_data);
		nbt.put("hitbox_data", hitbox_data);
	}
	
	public void refreshHitboxes() {
		if (level.isClientSide) return;
		CompoundTag nbt = new CompoundTag();
		saveRotableHitboxes(nbt);
		for (int i = 0; i < hitboxes.size(); ++i) 
			hitboxes.get(i).discard();
		createRotableHitboxes(nbt);
	}
	
	public void addRotableHitboxForClient(RotableHitbox hitbox) {
		if (!level.isClientSide) return;
		String name = hitbox.getHitboxName();
		for (int i = 0; i < hitboxes.size(); ++i) {
			if (hitboxes.get(i).getHitboxName().equals(name)) {
				hitboxes.remove(i);
				break;
			}
		}
		hitboxes.add(hitbox);
	}
	
	public void tickHitboxes() {
		if (level.isClientSide && hitboxes.size() < vehicleStats.getHitboxNum() && tickCount % 200 == 20) {
			LOGGER.debug("Vehicle "+getId()+" on client side has "+hitboxes.size()+"/"+vehicleStats.getHitboxNum()
					+" hitboxes. Sending hitbox refresh packet. Attempt "+(++hitboxRefreshAttempts));
			PacketHandler.INSTANCE.sendToServer(new ToServerFixHitboxes(this));
		}
		for (RotableHitbox box : hitboxes) box.tick();
		//syncHitboxCollidePositions();
		hitboxCollidedIds.clear();
	}
	
	private void syncHitboxCollidePositions() {
		if (!level.isClientSide || hitboxCollidedIds.size() == 0 || !isControlledByLocalInstance()) return;
		int[] ids = new int[hitboxCollidedIds.size()];
		Vec3[] pos = new Vec3[hitboxCollidedIds.size()];
		int i = 0;
		for (Integer id : hitboxCollidedIds) {
			ids[i] = id;
			Entity entity = level.getEntity(id);
			if (entity != null) pos[i] = entity.position();
			else pos[i] = new Vec3(0, -1000, 0);
			++i;
		}
		PacketHandler.INSTANCE.sendToServer(new ToServerSyncRotBoxPassengerPos(ids, pos));
	}
	
	public void addHitboxCollider(Entity entity) {
		hitboxCollidedIds.add(entity.getId());
	}
	
	public boolean didEntityAlreadyCollide(Entity entity) {
		return hitboxCollidedIds.contains(entity.getId());
	}
	
	public MastType getMastType() {
		return vehicleStats.mastType;
	}
	
	public boolean isFuelLeak() {
    	return partsManager.isFuelTankDamaged();
    }
    
    public boolean isEngineFire() {
    	return partsManager.isEngineDamaged();
    }
    
    public boolean isAllEnginesDamaged() {
    	return partsManager.isAllEnginesDamaged();
    }
    
    public boolean showAfterBurnerParticles() {
    	return getCurrentThrottle() > 0.5;
    }
    
    public boolean showMoreAfterBurnerParticles() {
    	return getCurrentThrottle() > 0.85;
    }
    
    public boolean showContrailParticles() {
    	return showAfterBurnerParticles() && position().y > 128;
    }
    
    public float getMotorRotation(float partialTicks, float spinRate) {
    	motorRot += spinRate * getCurrentThrottle() * partialTicks;
		return motorRot;
	}
    
    public float getWheelRotation(float partialTicks, float spinRate) {
    	wheelRot += spinRate * xzSpeed * xzSpeedDir * partialTicks * 0.05f;
    	return wheelRot;
    }
    
    /**
     * override this in a custom vehicle entity.
     * @return relative positions where smoke should appear
     */
    public Vec3[] getAfterBurnerSmokePos() {
    	return vehicleStats.afterBurnerSmokePos;
    }
    
    public List<Vec3> getEngineFirePos() {
    	Set<String> hitboxNames = partsManager.getEngineFireHitboxNames();
    	List<Vec3> pos = new ArrayList<>();
    	for (String name : hitboxNames) {
    		if (name.isEmpty()) {
    			pos.add(position());
    			continue;
    		}
    		RotableHitbox hitbox = getHitboxByName(name);
    		if (hitbox == null) continue;
    		pos.add(hitbox.position());
    	}
    	return pos;
    }
    
    public boolean liftLost() {
    	return false;
    }
    
    public boolean isStalling() {
		return false;
    }
    
    public int getStallTicks() {
    	return stallTicks;
    }
    
    public boolean isAboutToStall() {
    	return false;
    }
    
    public int getAboutToStallTicks() {
    	return stallWarnTicks;
    }
    
    public boolean isBingoFuelWarning() {
    	if (getMaxFuel() <= 0) return true;
    	return getCurrentFuel() / getMaxFuel() < 0.1;
    }
    
    public int getEngineFireTicks() {
    	return engineFireTicks;
    }
    
    public int getFuelLeakTicks() {
    	return fuelLeakTicks;
    }
    
    public int getBingoTicks() {
    	return bingoTicks;
    }
    
    public void tickWarnings() {
    	if (isStalling()) ++stallTicks;
    	else stallTicks = 0;
    	if (isAboutToStall()) ++stallWarnTicks;
    	else stallWarnTicks = 0;
    	if (isEngineFire()) ++engineFireTicks;
    	else engineFireTicks = 0;
    	if (isFuelLeak()) ++fuelLeakTicks;
    	else fuelLeakTicks = 0;
    	if (isBingoFuelWarning()) ++bingoTicks;
    	else bingoTicks = 0;
    }
    
    @Override
    public void setYRot(float yRot) {
        super.setYRot(yRot);
        Quaternion q = UtilAngles.toQuaternion(getYRot(), getXRot(), zRot);
        setQBySide(q);
    }
    
    @Override
    public void setXRot(float yRot) {
        super.setXRot(yRot);
        Quaternion q = UtilAngles.toQuaternion(getYRot(), getXRot(), zRot);
        setQBySide(q);
    }
    
    @Override
	public void absMoveTo(double pX, double pY, double pZ, float pYRot, float pXRot) {
    	absMoveTo(pX, pY, pZ);
    	// setting rotation here messes up rotation syncing
	}

	public void setYRotNoQ(float yRot) {
        super.setYRot(yRot);
    }
    
    public void setXRotNoQ(float yRot) {
        super.setXRot(yRot);
    }
    
    public boolean isAircraft() {
    	return getStats().isAircraft();
    }
    
    /**
     * CLIENT ONLY
     * @return null if server side
     */
    @Nullable
    public VehicleClientStats getClientStats() {
    	return vehicleClientStats;
    }
    
    public String getClientStatsId() {
    	return assetId;
    }
    
    public int getGroundTicks() {
    	return groundTicks;
    }
    
    public boolean rootHitboxEntityInteract() {
    	return !vehicleStats.rootHitboxNoCollide;
    }
    
}
