package com.onewhohears.dscombat.entity.vehicle;

import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.event.ClientInputEventHandlers;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.model.obj.ObjRadarModel.MastType;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.common.container.menu.VehiclePartsMenu;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientAddForceMoment;
import com.onewhohears.dscombat.common.network.toclient.ToClientOnShoot;
import com.onewhohears.dscombat.common.network.toclient.ToClientVehicleControl;
import com.onewhohears.dscombat.common.network.toclient.ToClientVehicleExplode;
import com.onewhohears.dscombat.common.network.toserver.*;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.PartsManager;
import com.onewhohears.dscombat.data.parts.instance.StorageInstance;
import com.onewhohears.dscombat.data.parts.instance.TurretInstance;
import com.onewhohears.dscombat.data.radar.RadarStats.RadarMode;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.vehicle.*;
import com.onewhohears.dscombat.data.vehicle.client.VehicleClientPresets;
import com.onewhohears.dscombat.data.vehicle.client.VehicleClientStats;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.dscombat.data.vehicle.physics.PhysicsComponentData;
import com.onewhohears.dscombat.data.vehicle.physics.PhysicsComponentInstance;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.data.weapon.WeaponSystem;
import com.onewhohears.dscombat.entity.CustomExplosion;
import com.onewhohears.dscombat.entity.DrivingBody;
import com.onewhohears.dscombat.entity.IREmitter;
import com.onewhohears.dscombat.entity.TrampleHandler;
import com.onewhohears.dscombat.entity.damagesource.VehicleDamageSource;
import com.onewhohears.dscombat.entity.parts.*;
import com.onewhohears.dscombat.entity.vehicle.hitbox.RotableHitbox;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.dscombat.item.VehicleInteractItem;
import com.onewhohears.dscombat.util.UtilClientPacket;
import com.onewhohears.dscombat.util.UtilParticles;
import com.onewhohears.dscombat.util.UtilServerPacket;
import com.onewhohears.dscombat.util.UtilVehicleEntity;
import com.onewhohears.dscombat.util.math.UtilRandom;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetAssetReader;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetReloadListener;
import com.onewhohears.onewholibs.entity.CustomAnimEntity;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.UtilItem;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.UtilGeometry;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.NameTagItem;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Predicate;

/**
 * the parent class for all vehicle entities in this mod
 * @author 1whohears
 */
// TODO: mouse mode handling has configurable sensitivity; higher by default. inputs have 'inertia'
public abstract class EntityVehicle extends CustomAnimEntity<VehicleStats, VehicleClientStats> implements IREmitter, CustomExplosion, DrivingBody, TrampleHandler {
	
	protected static final Logger LOGGER = LogUtils.getLogger();
	
	public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(EntityVehicle.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> ARMOR = SynchedEntityData.defineId(EntityVehicle.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<QuaternionF> Q = SynchedEntityData.defineId(EntityVehicle.class, DataSerializers.QUATERNION);
	public static final EntityDataAccessor<Vec3> AV = SynchedEntityData.defineId(EntityVehicle.class, DataSerializers.VEC3);
	public static final EntityDataAccessor<Boolean> TEST_MODE = SynchedEntityData.defineId(EntityVehicle.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> NO_CONSUME = SynchedEntityData.defineId(EntityVehicle.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<String> RADIO_SONG = SynchedEntityData.defineId(EntityVehicle.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Boolean> PLAY_IR_TONE = SynchedEntityData.defineId(EntityVehicle.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<RadarMode> RADAR_MODE = SynchedEntityData.defineId(EntityVehicle.class, DataSerializers.RADAR_MODE);
	public static final EntityDataAccessor<Boolean> LANDING_GEAR = SynchedEntityData.defineId(EntityVehicle.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<PermMode> PERM_MODE = SynchedEntityData.defineId(EntityVehicle.class, DataSerializers.PERM_MODE);

	public static final int HITBOX_PUSH_COOLDOWN = 4;

	public final VehicleInputManager inputs;
	public final VehicleSoundManager soundManager;
	public final VehicleTextureManager textureManager;
	public final PartsManager partsManager;
	public final WeaponSystem weaponSystem;
	public final RadarSystem radarSystem;
	
	protected final List<RotableHitbox> hitboxes = new ArrayList<>();
	private final Set<Integer> collidedEntityIds = new HashSet<>();
	private final Map<Integer, Integer> hitboxEntityCoolDown = new HashMap<>();
	private final Map<Integer, EntityCollideInfo> entityCollideInfo = new HashMap<>();
	protected final List<PhysicsComponentInstance<?>> physicsInstances = new ArrayList<>();
	
	private final Map<Integer, Integer> formerPassengersServer = new HashMap<>();
	
	/**
	 * SERVER ONLY
	 */
	public int lastShootTime = -1, ingredientDropIndex = -1;
	
	public QuaternionF prevQ = QuaternionF.ONE.copy();
	public QuaternionF clientQ = QuaternionF.ONE.copy();
	public Vec3 clientAV = Vec3.ZERO;
	
	public float zRot, zRotO; 
	public Vec3 prevMotion = Vec3.ZERO;
	public Vec3 forces = Vec3.ZERO, forcesO = Vec3.ZERO, addForceBetweenTicks = Vec3.ZERO;
	public Vec3 moment = Vec3.ZERO, momentO = Vec3.ZERO, addMomentBetweenTicks = Vec3.ZERO;
	protected Vec3 controlMoment = Vec3.ZERO, additionalRotInertia = Vec3.ZERO;
	
	public boolean nightVisionHud = false, hasRadio = false;
	
	protected boolean hasFlares;
	protected int xzSpeedDir, hurtByFireTime, flareTicks;
	protected float xzSpeed, totalMass, xzYaw, slideAngle, slideAngleCos, maxPushThrust, maxSpinThrust;
    protected float currentFuel, maxFuel, afterburnerMaxPushThrust;
	protected double staticFric, kineticFric, airDensity, currentAltitude, maxXZ;
	
	private int lerpSteps, deadTicks, stallWarnTicks, stallTicks, engineFireTicks, fuelLeakTicks, bingoTicks;
	private int groundTicks, hitboxRefreshAttempts, numFlares, hydraulicsFailureTicks;
	private int missileTicks, trackedTicks;
	private double lerpX, lerpY, lerpZ;
	private float landingGearPos, landingGearPosOld, motorRot, wheelRot, previousThrottle;
	private boolean wasInWater, hadControllingPassenger, wasPlayerOrBotRiding;
	private boolean ignoreSyncMoveRot = false;
	
	protected boolean isDriverCameraLocked = false;
	protected float throttle;
	
	@Nullable protected EntityGimbal pilotGimbal;
	@Nullable protected Player chainHolderPlayer;
	@Nullable protected EntityChainHook chainHolderHook;

	/**
	 * SERVER SIDE ONLY
	 */
	@Nullable private Entity owner;
	@Nullable private UUID owner_uuid;
	private int owner_id = -1;
	
	// TODO 5.4 vehicle visually breaks apart when damaged
	// TODO 5.6 place and remove external parts from outside the vehicle
	// TODO 2.5 add chaff
	// TODO 2.8 external fuel tanks
	
	public EntityVehicle(EntityType<? extends EntityVehicle> entityType, Level level, String defaultPreset) {
		super(entityType, level, defaultPreset);
		blocksBuilding = true;
		inputs = new VehicleInputManager();
		soundManager = new VehicleSoundManager(this);
		textureManager = new VehicleTextureManager(this);
		partsManager = new PartsManager(this);
		weaponSystem = new WeaponSystem(this);
		radarSystem = new RadarSystem(this);
		updatePhysicsInstances();
        maxUpStep = 0.6f;
	}

	@Override
	public void updateStatsHolder(@NotNull String preset) {
		super.updateStatsHolder(preset);
		if (isStatsHolderLoaded()) updatePhysicsInstances();
	}

	public void updatePhysicsInstances() {
		physicsInstances.clear();
		for (PhysicsComponentData data : getStats().getPhysicsComponents()) {
			if (data == null) continue;
			physicsInstances.add(data.createInstance());
		}
	}

	@Override
	protected void defineSynchedData() {
        entityData.define(HEALTH, 100f);
        entityData.define(ARMOR, 100f);
		entityData.define(Q, QuaternionF.ONE);
		entityData.define(AV, Vec3.ZERO);
		entityData.define(TEST_MODE, false);
		entityData.define(NO_CONSUME, false);
		entityData.define(RADIO_SONG, "");
		entityData.define(PLAY_IR_TONE, false);
		entityData.define(RADAR_MODE, RadarMode.ALL);
		entityData.define(LANDING_GEAR, true);
		entityData.define(PERM_MODE, PermMode.PUBLIC);
	}
	
	@Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        // if this entity is on the client side and receiving the quaternion of the plane from the server 
        if (!isClientSide()) return;
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
	 * if this is a brand-new entity and has no nbt custom data then the fresh entity nbt will
	 * merge with this vehicle's preset nbt. see {@link VehiclePresets}.
	 * you could summon a vehicle with nbt {preset:"some preset name"} to override the {@link CustomAnimEntity#getDefaultStatsId()}
 	 */
	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		// ORDER MATTERS
		setTestMode(nbt.getBoolean("test_mode"));
		setNoConsume(nbt.getBoolean("no_consume"));
		// get stats nbt
		CompoundTag presetNbt = getStats().getDataAsNBT();
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
		setZRot(nbt.getFloat("zRot"));
		QuaternionF q = UtilAngles.toQuaternionF(getYRot(), getXRot(), getZRot());
		setQ(q);
		setPrevQ(q);
		setClientQ(q);
		setRadarMode(RadarMode.values()[nbt.getInt("radar_mode")]);
		setRadioSong(nbt.getString("radio_song"));
		createRotableHitboxes(nbt);
		if (nbt.contains("ingredientDropIndex")) ingredientDropIndex = nbt.getInt("ingredientDropIndex");
		if (nbt.contains("owner_id")) owner_uuid = nbt.getUUID("owner_id");
		setPermMode(PermMode.values()[nbt.getInt("perm_mode")]);
		maxXZ = nbt.getDouble("maxXZ");
		ignoreSyncMoveRot = true;
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
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
		nbt.putDouble("maxXZ", maxXZ);
		nbt.putInt("radar_mode", getRadarMode().ordinal());
		nbt.putString("radio_song", getRadioSong());
		Entity own = getOwner();
		if (own != null) {
			nbt.putString("owner_name", own.getScoreboardName());
			nbt.putUUID("owner_id", own.getUUID());
		} else if (owner_uuid != null) {
			nbt.putUUID("owner_id", owner_uuid);
		}
		Component name = getCustomName();
        if (name != null) nbt.putString("CustomName", Component.Serializer.toJson(name));
        if (isCustomNameVisible()) nbt.putBoolean("CustomNameVisible", isCustomNameVisible());
        nbt.putFloat("fuel", getCurrentFuel());
        nbt.putFloat("flares", getFlareNum());
        saveRotableHitboxes(nbt);
        nbt.putInt("ingredientDropIndex", ingredientDropIndex);
		nbt.putInt("perm_mode", getPermMode().ordinal());
	}
	
	@Override
	public void readSpawnData(FriendlyByteBuf buffer) {
		super.readSpawnData(buffer);
		int weaponIndex = buffer.readInt();
		boolean gear = buffer.readBoolean();
		boolean freeLook = buffer.readBoolean();
		float throttle = buffer.readFloat();
		maxXZ = buffer.readDouble();
		List<PartSlot> slots = PartsManager.readSlotsFromBuffer(buffer);
		// ORDER MATTERS
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
		super.writeSpawnData(buffer);
		buffer.writeInt(weaponSystem.getSelectedIndex());
		buffer.writeBoolean(isLandingGear());
		buffer.writeBoolean(isDriverCameraLocked());
		buffer.writeFloat(getCurrentThrottle());
		buffer.writeDouble(maxXZ);
		PartsManager.writeSlotsToBuffer(buffer, partsManager.getSlots());
		textureManager.write(buffer);
		soundManager.write(buffer);
	}
	
	public abstract VehicleType getVehicleType();
	
	/**
	 * called on this entities first tick on client and server side
	 */
	public void init() {
		refreshDimensions();
		if (!isClientSide()) serverSetup();
		else clientSetup();
		soundManager.loadSounds(getStats());
	}
	
	/**
	 * fires every tick server and client side
	 */
	@Override
	public void tick() {
		if (UtilGeometry.vec3NAN(getDeltaMovement())) setDeltaMovement(Vec3.ZERO);
		if (firstTick) init(); // MUST BE CALLED BEFORE SUPER
		super.tick();
		// HANDLE SPECIAL INPUTS
		controlSystem();
		// PHYSICS
		tickPhysics();
		tickCollisions();
		if (!getWorld().isClientSide() && canTrample()) tickTrample();
		tickLerp();
		// HITBOXES
		tickHitboxes();
        // OTHER
        tickParts();
        tickWarnings();
		soundManager.onTick();
		textureManager.onTick();
		if (isClientSide()) clientTick();
		else serverTick();
	}

	@Override
	public void calcAirMovement(QuaternionF q) {
		DrivingBody.super.calcAirMovement(q);
		resetFallDistance();
	}

	public boolean canDriveOnGround() {
		return !canToggleLandingGear() || isLandingGear();
	}

	public void calcWaterMovement(QuaternionF q) {

	}

	@Override
	public void addControllingTorques(QuaternionF q) {
		if (canTurnViaTorque()) {
			if (canControlPitch()) {
				if (isHardCodedRotAcc()) hardCodedAccPitch();
				else addMomentX(inputs.pitch * getPitchTorque(), true);
			}
			if (canControlYaw()) {
				if (isHardCodedRotAcc()) hardCodedAccYaw();
				else addMomentY(inputs.yaw * getYawTorque(), true);
			}
			if (canControlRoll()) {
				if (isHardCodedRotAcc()) hardCodedAccRoll();
				else{
					if (inputs.bothRoll) flatten(q, 0, getMaxDeltaRoll(), false);
					else addMomentZ(inputs.roll * getRollTorque(), true);
				}
			}
		}
	}

	public boolean canTurnViaTorque() {
		return isOperational() && !isOnGround();
	}

	public Vec3 getTotalRotInertia() {
		return new Vec3(getStats().Ix, getStats().Iy, getStats().Iz).add(getAdditionalRotInertia());
	}

	protected Vec3 getAdditionalRotInertia() {
		return additionalRotInertia;
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
	 * which is called by {@link com.onewhohears.dscombat.client.event.ClientInputEventHandlers}
	 * to the controlling player can sync their inputs with every other client.
	 * could also be used by a server side AI to sync a vehicle's inputs with all client's.
	 */
	public void syncControlsToClient() {
		if (isClientSide()) return;
        PacketHandler.sendToTrackers(new ToClientVehicleControl(this), this);
	}
	
	/**
	 * called on both client and server side
	 * damages plane if it falls or collides with a wall at speeds defined in config.
	 */
	public void tickCollisions() {
		if (!isClientSide()) {
			knockBack(getWorld().getEntities(this,
					getBoundingBox(), 
					getKnockbackPredicate()));
			tickDismountSafety();
			if (!hasControllingPassenger()) wallCollisions();
		} else {
			if (isControlledByLocalInstance()) wallCollisions();
		}
	}

	public boolean canTrample() {
		return (isOnGround() || isInWater()) && xzSpeed > 0 && getWorld().getGameRules().getBoolean(DSCGameRules.VEHICLE_TRAMPLE);
	}

	protected void tickTrample() {
		AABB box = getBoundingBox().move(getDeltaMovement()).inflate(0.01);
		Entity controller = getControllingPassenger();
		for (double x = box.minX; x < box.maxX+1; ++x) {
			for (double z = box.minZ; z < box.maxZ+1; ++z) {
				for (double y = box.minY; y < box.maxY+1; ++y) {
					BlockPos pos = UtilGeometry.toBlockPos(new Vec3(x, y, z));
					BlockState state = getWorld().getBlockState(pos);
					if (!state.is(ModTags.Blocks.VEHICLE_TRAMPLE)) continue;
					if (UtilVehicleEntity.vehicleHasPermissionToTrample(pos, state, getWorld(), controller))
						getWorld().destroyBlock(pos, true, this);
				}
			}
		}
	}
	
	protected void wallCollisions() {
		if (verticalCollision) verticalCollision();
		if (horizontalCollision && !minorHorizontalCollision) horizontalCollision();
		if (!wasInWater() && isInWater()) waterCollision();
		wasInWater = isInWater();
	}

	protected void waterCollision() {
		double speed = prevMotion.length();
		double th = DSCPhyCons.COLLIDE_SPEED;
		if (speed > th) {
			float amount = (float)((speed-th)*DSCPhyCons.COLLIDE_DAMAGE_RATE);
			collideHurt(amount, false);
		}
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
		if (isOperational() && isOnGround() && !isLandingGear()
				&& getXZSpeed() > DSCPhyCons.COLLIDE_SPEED && tickCount % 8 == 0 ) {
			collideHurt(10, false);
		}
	}
	
	@Override
	public boolean causeFallDamage(float dist, float mult, @NotNull DamageSource source) {
		verticalCollision();
		return true;
	}
	
	/**
	 * hurt the vehicle if there is a collision
	 * @param amount amount of damage done by collision
	 * @param isFall true if vertical collision, false if horizontal
	 */
	public void collideHurt(float amount, boolean isFall) {
		if (!isClientSide() && tickCount > 200) {
			if (isFall) hurt(damageSources().fall(), amount);
			else hurt(damageSources().flyIntoWall(), amount);
		} else if (isClientSide() && isControlledByLocalInstance()) {
            new ToServerVehicleCollide(getId(), amount, isFall).sendToServer();
		}
	}
	
	protected Predicate<? super Entity> getKnockbackPredicate() {
		return ((entity) -> {
			if (entity.noPhysics) return false;
			if (entity.isSpectator()) return false;
			if (this.equals(entity.getRootVehicle())) return false;
			if (!(entity instanceof LivingEntity)) return false;
			if (entity instanceof Player p && p.isCreative()) return false;
            return !isFormerPassenger(entity);
        });
	}
	
	private void tickDismountSafety() {
		formerPassengersServer.forEach((id, time) -> { if (time > 0) --time; });
	}
	
	public boolean isFormerPassenger(Entity passenger) {
		return formerPassengersServer.containsKey(passenger.getId()) && formerPassengersServer.get(passenger.getId()) > 0;
	}
	
	public void onSeatDismount(Entity entity) {
		if (!isClientSide()) formerPassengersServer.put(entity.getId(), DSCPhyCons.EJECT_SAFETY_COOLDOWN);
        else ClientInputEventHandlers.onEntityDismountVehicle(entity);
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
			hurt(damageSources().drown(), 5);
	}
	
	/**
	 * called on server tick if health = 0.
	 */
	public void tickNoHealth() {
		++deadTicks;
		if (isFullyLooted()) kill();
		int removeTicks = getWorld().getGameRules().getInt(DSCGameRules.REMOVE_DEAD_VEHICLES_TIME) * 20;
		if (removeTicks >= 0 && deadTicks >= removeTicks) {
			if (getWorld().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) dropAllItems();
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
		double max;
		if (isOnGround()) max = getMaxGroundSpeed();
		else max = getMaxSpeed();
		if (applyHorizontalSpeedScale()) max *= getHorizontalSpeedScale();
		return max * getMaxSpeedFactor();
	}

	public double getMaxSpeedFactor() {
		return Config.SERVER.vehicleSpeedFactor.get();
	}

	public double getMaxGroundSpeed() {
		return getStats().max_ground_speed;
	}
	
	/**
	 * called on every tick.
	 * changes this craft's throttle every tick based on inputThrottle.
	 * also resets the controls if there isn't a controlling passenger.
	 */
	public void tickThrottle() {
		if (!isTestMode()) {
            boolean hasControllingPassenger = hasControllingPassenger();
            boolean isPlayerOrBotRiding = isPlayerOrBotRiding();
			if (!isOperational()
					|| (cutThrottleOnNoPilot() && !hasControllingPassenger)
					|| (cutThrottleOnNoPassengers() && !isPlayerOrBotRiding)) {
				resetControls();
                inputs.setThrottleOverride(getCurrentThrottle(), this);
                previousThrottle = 0;
				return;
			}
            if ((!cutThrottleOnNoPilot() && (!hasControllingPassenger || !hadControllingPassenger))
                    || (!cutThrottleOnNoPassengers() && (!isPlayerOrBotRiding || !wasPlayerOrBotRiding))) {
                inputs.setThrottleOverride(previousThrottle, this);
            }
			if (currentFuel <= 0 || isAllEnginesDamaged()) {
				throttleToZero();
                inputs.setThrottleOverride(getCurrentThrottle(), this);
				return;
			}
		}
		throttleTowards(inputs.getGoalThrottle(this));
	}
	
	public boolean cutThrottleOnNoPilot() {
		return true;
	}
	
	public boolean cutThrottleOnNoPassengers() {
		return true;
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
	public void calcMoveStatsPre(QuaternionF q) {
		totalMass = getEmptyVehicleMass() + partsManager.getPartsWeight();
		staticFric = totalMass * DSCPhyCons.GRAVITY * DSCPhyCons.STATIC_FRICTION;
		kineticFric = totalMass * DSCPhyCons.GRAVITY * DSCPhyCons.KINETIC_FRICTION;
		maxPushThrust = partsManager.getTotalPushThrust();
        afterburnerMaxPushThrust = partsManager.getAfterburnerTotalPushThrust();
		maxSpinThrust = partsManager.getTotalSpinThrust();
		currentFuel = partsManager.getCurrentFuel();
		maxFuel = partsManager.getMaxFuel();
		hasFlares = !partsManager.getFlares().isEmpty();
		numFlares = partsManager.getNumFlares();
		airDensity = UtilVehicleEntity.getAirDensity(this);
		if (isOnGround()) ++groundTicks;
		else groundTicks = 0;
		additionalRotInertia = partsManager.calcRotInertialFromParts();
	}
	
	/**
	 * cache some needed physics values AFTER calculating movement.
	 * instead of calculating values multiple times per tick.
	 * @param q the current direction of the vehicle
	 */
	public void calcMoveStatsPost(QuaternionF q) {
		DrivingBody.super.calcMoveStatsPost(q);
		currentAltitude = UtilVehicleEntity.getDistFromSeaLevel(this);
	}
	
	public float getXZSpeed() {
		return xzSpeed;
	}
	
	public int getXZSpeedDir() {
		return xzSpeedDir;
	}

	public boolean applyHorizontalSpeedScale() {
		return getStats().use_horizontal_speed_scale;
	}

	public double getHorizontalSpeedScale() {
		return DSCPhyCons.getIRLScale();
	}

	public boolean applyVerticalAccScale() {
		return getStats().use_vertical_speed_scale;
	}

	public double getVerticalAccScale(double verticalForce) {
		if (verticalForce < 0) return DSCPhyCons.VERTICAL_DOWN_ACC_SCALE;
		return DSCPhyCons.VERTICAL_UP_ACC_SCALE;
	}
	
	@Override
	public void move(@NotNull MoverType type, @NotNull Vec3 move) {
        move = verifyLoadedChunk(move);
		super.move(type, move);
		if (!noPhysics && isOnGround() && getDeltaMovement().y == 0) stepDown(move);
	}

    protected Vec3 verifyLoadedChunk(@NotNull Vec3 move) {
        Entity controller = getControllingPassenger();
        if (controller == null) return move;
        Vec3 nextPos = controller.position().add(move.normalize().scale(64));
        ChunkPos nextChunk = new ChunkPos(UtilGeometry.toBlockPos(nextPos));
        if (getWorld().hasChunk(nextChunk.x, nextChunk.z)) return move;
        LOGGER.warn("CHUNK AHEAD VEHICLE DOES NOT EXIST STOPPING MOVE FOR PILOT: {} | SPEED: {}",
                controller.getScoreboardName(), move.length());
        // FIXME this seems to prevent players from getting ejected during lag, but lag back looks wierd.
        return Vec3.ZERO;
    }
	
	/**
	 * custom physics used to "step down" from a stair step.
	 * appears like the entity teleport down from small ledges.
	 * added to avoid vehicles floating down from stair step ledges. 
	 * @param move current velocity
	 */
	protected void stepDown(Vec3 move) {
		AABB aabb = getBoundingBox();
		Vec3 down = new Vec3(0,-maxUpStep-0.1, 0); // this -0.1 is needed trust me
		List<VoxelShape> list = getWorld().getEntityCollisions(this, aabb.expandTowards(down));
		Vec3 collide = collideBoundingBox(this, down, aabb, getWorld(), list);
		if (collide.y < 0 && collide.y >= -maxUpStep) {
			setPos(getX(), getY()+collide.y, getZ());
		}
	}
	
	/**
	 * @return the acceleration while driving 
	 */
	public double getDriveAcc() {
		return getSpinThrustMag()/getTotalMass();
	}
	
	public boolean isGroundBraking() {
		return false;
	}
	
	public boolean canGroundBrake() {
		return isOnGround() && getStats().break_deacc_ground > 0 && isOperational();
	}

	public boolean canAirBrake() {
		return !isOnGround() && getStats().break_deacc_air > 0 && isOperational();
	}

	public boolean isAirBreaking() {
		return isGroundBraking();
	}
	
	/**
	 * @return the magnitude of the thrust force based on the engines, throttle, and 0 if no fuel
	 */
	public double getPushThrustMag() {
		if (getCurrentFuel() <= 0 || !isOperational()) return 0;
		return getCurrentThrottle() * getMaxPushThrust();
	}

	public float getMaxPushThrust() {
		return maxPushThrust;
	}

    public float getAfterburnerMaxPushThrust() {
        return afterburnerMaxPushThrust;
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

	public double getDragArea() {
		double a = getStats().drag_area;
		if (canToggleLandingGear() && isLandingGear() && !isOnGround())
			a += DSCPhyCons.INCREASED_DRAG_AREA_LANDING_GEAR;
		if (!isOperational())
			a += DSCPhyCons.INCREASED_DRAG_AREA_DESTROYED;
		return a;
	}

	public double getDragCoefficient() {
		return DSCPhyCons.getDragScale();
	}

	public double getRadarArea(Vec3 radarPos) {
		double a = getBaseCrossSecArea();
		if (canToggleLandingGear() && isLandingGear()) a += 1;
		return a;
	}
	
	/**
	 * @return this mass of the vehicle plus the mass of the parts
	 */
	public float getTotalMass() {
		if (Float.isNaN(totalMass)) {
            LOGGER.warn("ERROR: NAN MASS? setting to 10000 | {}", this);
			totalMass = 10000;
		} else if (totalMass == 0) {
            LOGGER.warn("ERROR: 0 MASS? setting to 10000 | {}", this);
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
		return getStats().mass;
	}
	
	/**
	 * fired on both client and server side to control the plane's weapons, flares, open menu
	 */
	public void controlSystem() {
		tickThrottle();
		if (!isOperational()) return;
		radarSystem.tick();
		Entity controller = getControllingPassenger();
		if (!isClientSide()) {
			weaponSystem.serverTick();
			if (controller == null) return;
			boolean consume = !isNoConsume();
			if (controller instanceof ServerPlayer player) {
				if (player.isCreative()) consume = false;
			}
			boolean consumeFuel = getWorld().getGameRules().getBoolean(DSCGameRules.CONSUME_FULE);
			if (consume && consumeFuel) tickFuel();
			if (inputs.flare && tickCount - flareTicks >= 10) {
				boolean consumeFlares = getWorld().getGameRules().getBoolean(DSCGameRules.CONSUME_FLARES);
				flare(controller, consume && consumeFlares);
			}
		}
        hadControllingPassenger = hasControllingPassenger();
        wasPlayerOrBotRiding = isPlayerOrBotRiding();
        if (hadControllingPassenger) previousThrottle = getCurrentThrottle();
	}

	public void openPartsMenu(ServerPlayer player) {
        MenuRegistry.openExtendedMenu(player, new ExtendedMenuProvider() {
            @Override public void saveExtraData(FriendlyByteBuf buf) {}
            @Override
            public @NotNull Component getDisplayName() {
                return UtilMCText.translatable("screen.dscombat.vehicle_parts_screen");
            }
            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
                return new VehiclePartsMenu(windowId, inventory);
            }
        });
	}

	public void openStorage(ServerPlayer player, int index) {
		StorageInstance<?> box = partsManager.getStorageData(index);
		if (box == null) {
			player.displayClientMessage(UtilMCText.translatable("error.dscombat.no_storage_boxes"), true);
			return;
		}
        MenuRegistry.openExtendedMenu(player, new ExtendedMenuProvider() {
            @Override
            public void saveExtraData(FriendlyByteBuf buf) {
                buf.writeInt(partsManager.getStorageIndex());
            }
            @Override
            public @NotNull Component getDisplayName() {
                return UtilMCText.translatable("screen.dscombat.vehicle_inventory_screen");
            }
            @Override
            public @Nullable AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
                return box.createMenu(windowId, inventory);
            }
        });
	}
	
	public boolean canOpenPartsMenu() {
		return (isOnGround() && xzSpeed < 0.1) || isTestMode();
	}
	
	public String getOpenMenuError() {
		if (!isOnGround()) return "error.dscombat.no_menu_in_air";
		return "error.dscombat.no_menu_moving";
	}
	
	public void flare(Entity controller, boolean consume) {
		if (partsManager.useFlares(consume)) {
			ToClientOnShoot.onShootFlareRack(this, controller);
			flareTicks = tickCount;
		}
	}
	
	/**
	 * ticks the parts manager on client and server side
	 */
	public void tickParts() {
		findGimbalForPilotCamera();
		if (isClientSide()) partsManager.clientTickParts();
		else partsManager.serverTickParts();
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
		if (!isClientSide() || tickCount % 10 != 0 || firstTick) return;
		if (ignoreSyncMoveRot) {
			ignoreSyncMoveRot = false;
			return;
		}
        new ToServerVehicleMoveRot(this).sendToServer();
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
	public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
		if (xzSpeed > 0.2 && !isTestMode()) return InteractionResult.PASS;
		if (player.isSecondaryUseActive()) return InteractionResult.PASS;
		if (player.getRootVehicle().equals(this)) return InteractionResult.PASS;
		ItemStack stack = player.getInventory().getSelected();
		if (!stack.isEmpty()) {
			InteractionResult result = onItemInteract(player, hand, stack);
			if (result != InteractionResult.FAIL) return result;
		}
		if (!isOperational()) return onDestroyedInteract(player, hand);
		if (!isClientSide()) return rideAvailableSeat(player) ? InteractionResult.CONSUME : InteractionResult.PASS;
		else {
			Minecraft m = Minecraft.getInstance();
			if (m.player != null && m.player.equals(player)) DSCClientInputs.centerMousePos();
		}
		return InteractionResult.SUCCESS;
	}
	
	protected InteractionResult onItemInteract(Player player, InteractionHand hand, ItemStack stack) {
		if (stack.is(ModTags.Items.SPRAY_CAN)) return onSprayCanInteract(player, hand, stack);
		if (!isClientSide()) {
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
			else if (stack.is(ModTags.Items.OIL_BUCKET))
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
		return InteractionResult.sidedSuccess(isClientSide());
	}
	
	protected InteractionResult onOilBucketInteract(Player player, InteractionHand hand, ItemStack stack) {
		float fuelPerBucket = (float)DSCGameRules.getFuelPerOilBlock(getWorld());
		if (addFuel(fuelPerBucket) == fuelPerBucket) return InteractionResult.PASS;
		ItemStack remain = UtilItem.getCraftingRemainingItem(stack);
		player.getInventory().setItem(player.getInventory().selected, remain);
		return InteractionResult.sidedSuccess(isClientSide());
	}
	
	protected InteractionResult onSprayCanInteract(Player player, InteractionHand hand, ItemStack stack) {
		if (isClientSide()) UtilClientPacket.openVehicleTextureScreen(textureManager);
		return InteractionResult.sidedSuccess(isClientSide());
	}
	
	protected InteractionResult onChainInteract(Player player, InteractionHand hand, ItemStack stack) {
		List<EntityChainHook> hooks = getWorld().getEntitiesOfClass(EntityChainHook.class,
				getBoundingBox().inflate(EntityChainHook.CHAIN_LENGTH), hook -> hook.isPlayerConnected(player));
		/*if (hooks.size() == 0) {
			chainToPlayer(player);
			return InteractionResult.sidedSuccess(isClientSide());
		}*/
		for (EntityChainHook hook : hooks) {
			if (hook.addVehicleConnection(player, this)) {
				chainToHook(hook);
				stack.shrink(1);
				break;
			}
		}
		return InteractionResult.sidedSuccess(isClientSide());
	}
	
	protected InteractionResult onDestroyedInteract(Player player, InteractionHand hand) {
		if (partsManager.dropPartItem()) {
			playTheftSound();
			return InteractionResult.sidedSuccess(isClientSide());
		}
		if (dropIngredient()) {
			playTheftSound();
			return InteractionResult.sidedSuccess(isClientSide());
		}
		return InteractionResult.PASS;
	}
	
	public boolean dropIngredient() {
		if (isClientSide()) return false;
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
		getWorld().playSound(null, this, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR,
				getSoundSource(), 0.5f, 1.0f);
	}

	public void dropAllItems() {
		dropAllParts();
		dropAllIngredients();
	}

	public void dropAllParts() {
		if (getWorld().isClientSide) return;
		partsManager.dropAllItems();
	}

	public void dropAllIngredients() {
		if (getWorld().isClientSide) return;
		while (true) if (!dropIngredient()) break;
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
		if (!isClientSide()) UtilServerPacket.sendVehicleAddPlayer(this, player);
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
		getWorld().playSound(null, this, sound,
				getSoundSource(), 0.5f, 1.0f);
	}
	
	private boolean ridePilotSeat(Entity e, List<EntityRidablePart> seats) {
		for (EntityRidablePart seat : seats)
			if (seat.isPilotSeat()) 
				return e.startRiding(seat);
		return false;
	}
	
	public boolean ridePassengerSeat(Entity e) {
		List<EntityRidablePart> seats = getSeats();
		for (EntityRidablePart seat : seats)
			if (!seat.isPilotSeat() && e.startRiding(seat)) 
				return true;
		return false;
	}
	
	public boolean rideAvailableSeat(Entity e) {
		List<EntityRidablePart> seats = getSeats();
		if (ridePilotSeat(e, seats)) return true;
		for (EntityRidablePart seat : seats)
			if (e.startRiding(seat)) {
				return true;
			}
		return false;
	}
	
	public boolean switchSeat(Entity e) {
		List<EntityRidablePart> seats = getSeats();
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

	public boolean hasOpenPassengerSeat() {
		List<EntityRidablePart> seats = getSeats();
		for (EntityRidablePart seat : seats)
			if (seat.getPassenger() == null)
				return true;
		return false;
	}
	
	/**
	 * all part entities ride the vehicle using the vanilla passenger system.
	 * the part's position is set based on the vehicle's rotation.
	 */
	@Override
    public void positionRider(@NotNull Entity passenger, MoveFunction moveFunction) {
		if (passenger instanceof EntityPart part) {
            Vec3 pos = convertRelPos(part.getRelativePos());
            moveFunction.accept(passenger, pos.x, pos.y, pos.z);
			return;
		}
	}
	
	public Vec3 convertRelPos(Vec3 rel_pos) {
		return UtilAngles.rotateVector(rel_pos, getQBySide()).add(position());
	}
	
	@Nullable
	@Override
    public LivingEntity getControllingPassenger() {
        for (EntityRidablePart seat : getSeats())
        	if (seat.isPilotSeat()) 
        		return seat.getPlayer();
        return null;
    }
	
	@Nullable
	public Entity getControllingPlayerOrBot() {
		Player playerAlt = null;
		Entity alt = null;
		for (EntityRidablePart seat : getSeats()) {
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
		for (EntityRidablePart seat : getSeats())
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
		for (EntityRidablePart seat : getSeats())
			if (seat.isPlayerOrBotRiding()) 
				return true;
		return false;
	}

	@Override
    protected void addPassenger(@NotNull Entity passenger) {
        if (passenger instanceof EntityPart part && getPartBySlotId(part.getSlot().getSlotId()) != null) return;
		super.addPassenger(passenger);
	}
	
	@Override
    protected boolean canAddPassenger(@NotNull Entity passenger) {
		return passenger instanceof EntityPart;
	}
	
	@Override
    protected boolean canRide(@NotNull Entity entityIn) {
        return false;
    }
	
	@Nullable
	public EntityRidablePart getPassengerSeat(Entity p) {
		for (EntityRidablePart seat : getSeats())
			if (p.equals(seat.getPassenger())) 
				return seat;
		return null;
	}

	public boolean isVehicleOf(Entity e) {
		if (e == null) return false;
		List<Entity> list = getPassengers();
		if (list.contains(e)) return true;
		for (Entity l : list) {
			if (l instanceof EntityRidablePart seat) {
				List<Entity> list2 = seat.getPassengers();
				if (list2.contains(e)) return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasPassenger(@NotNull Predicate<Entity> pPredicate) {
		for (EntityRidablePart seat : this.getSeats()) {
			if (pPredicate.test(seat.getPassenger())) return true;
		}
		return false;
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
    public boolean canBeCollidedWith() {
    	return rootHitboxEntityInteract();
    }
    
    @Override
    public boolean canCollideWith(@NotNull Entity entity) {
    	if (!super.canCollideWith(entity)) return false;
    	if (entity.isPushable()) return false;
        return !isHitboxParent(entity);
    }
    
	@Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
		return hurtLogic(source, amount, null);
	}
	
	public boolean hurtHitbox(DamageSource source, float amount, RotableHitbox hitbox) {
		return hurtLogic(source, amount, hitbox, hitbox.getHitboxData().isDamageRoot());
	}
	
	public boolean hurtLogic(DamageSource source, float amount, @Nullable RotableHitbox hitbox, boolean hurtRoot) {
		if (isInvulnerableTo(source)) return false;
		if (UtilVehicleEntity.isFire(source)) hurtByFireTime = tickCount;
		soundManager.onHurt(source, amount);
		damage(source, amount, hitbox, hurtRoot);
		if (!isClientSide()) {
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
		/*if (shouldDebug(source)) 
			System.out.println("D="+amount+" C?"+isClientSide()+" R?"+hurtRoot+" H="+hitbox+" source "+source);*/
		if (!UtilVehicleEntity.isExplosion(source) && source.getDirectEntity() != null
				&& source.getDirectEntity().getType().is(ModTags.EntityTypes.PROJECTILE)) {
			amount = calcDamageFromBullet(source, amount);
		}
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
		/*if (shouldDebug(source)) {
			System.out.println("vehicle health: "+getHealth()+" armor "+getArmor());
			if (hitbox != null) System.out.println("hitbox health: "+hitbox.getHealth()+" armor "+hitbox.getArmor());
		}*/
	}
	
	public static float getHealthDamageWithArmorPercent(DamageSource source) {
		if (UtilVehicleEntity.isExplosion(source)) return 0.2f;
		else if (UtilVehicleEntity.isBypassArmor(source)) return 0.8f;
		else if (UtilVehicleEntity.isFire(source)) return 0.7f;
		return 0; 
	}
	
	public float calcDamageToArmor(float amount) {
		return Math.max(0, reduceByPercent(amount, 
				getStats().armor_damage_absorbtion * DSCGameRules.getVehicleArmorStrengthFactor(getWorld()))
				- getStats().armor_damage_threshold);
	}
	
	public float calcDamageToInside(DamageSource source, float amount) {
		return calcDamageToArmor(amount) * getHealthDamageWithArmorPercent(source);
	}

	public float calcDamageToRider(DamageSource source, float amount) {
		if (getArmor() > 0) return calcDamageToInside(source, amount);
		return amount;
	}
	
	private boolean shouldDebug(DamageSource source) {
		return !UtilVehicleEntity.isFire(source);
	}
	
	protected float calcDamageFromBullet(DamageSource source, float amount) {
		return amount * DSCGameRules.getBulletDamageVehicleFactor(getWorld());
	}
	
	private static float reduceByPercent(float amount, float percent) {
		return Math.max(amount - amount * percent, 0);
	}
	
	@Override
	public boolean isInvulnerableTo(@NotNull DamageSource source) {
		if (isTestMode()) return true;
		if (super.isInvulnerableTo(source)) return true;
		if (UtilVehicleEntity.isFire(source) && (tickCount-hurtByFireTime) < 10) return true;
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
			if (hitbox == null) return;
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
		if (source.is(DamageTypes.FALL)) {
			explode(VehicleDamageSource.fall(this));
			return true;
		} else if (source.is(DamageTypes.FLY_INTO_WALL)) {
			explode(VehicleDamageSource.collide(this));
			return true;
		}
		return false;
	}
	
	public void addForceMomentToClient(Vec3 force, Vec3 moment) {
		if (isClientSide()) return;
		addForceBetweenTicks = addForceBetweenTicks.add(force);
		addMomentBetweenTicks = addMomentBetweenTicks.add(moment);
        PacketHandler.sendToTrackers(new ToClientAddForceMoment(this, force, moment), this);
	}
	
	public void explode(DamageSource source) {
		if (isClientSide()) return;
		getWorld().explode(this, source,
			null, getX(), getY(), getZ(), 
			getStats().crashExplosionRadius, true,
			Level.ExplosionInteraction.TNT);
        explodeSeats(source);
        PacketHandler.sendToTrackers(new ToClientVehicleExplode(this), this);
	}

    public void explodeSeats(DamageSource source) {
        for (EntityRidablePart<?,?> seat : getSeats())
            seat.explode(source, this);
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
		Vec3 s = new Vec3(exp.x, exp.y, exp.z);
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
        amount *= DSCGameRules.getExplodeDamagerVehicleFactor(getWorld());
        
        if (hitbox != null) hurtLogic(exp.getDamageSource(), amount, hitbox, false);
        else hurtLogic(exp.getDamageSource(), amount, null);
        
        Vec3 force = new Vec3(dx*exp_factor, dy*exp_factor, dz*exp_factor).scale(DSCPhyCons.EXP_FORCE_FACTOR);
        
		Vec3 f;
		if (s.equals(b) && exp_entity != null) 
			f = exp_entity.getDeltaMovement().normalize().scale(exp_factor*DSCPhyCons.EXP_MOMENT_FACTOR);
		else f = s.subtract(b).normalize().scale(exp_factor*DSCPhyCons.EXP_MOMENT_FACTOR);

		Vec3 moment = r.cross(UtilAngles.rotateVectorInverse(f, getQBySide()));
		
		addForceMomentToClient(force, moment);
	}
	
	@Override
	public void customExplosionHandler(Explosion exp) {
		customExplosionHandler(exp, null);
	}
	
	private Vec3 getClosest(Vec3 pos, @Nullable RotableHitbox hitbox) {
		if (hitbox == null) return UtilGeometry.getClosestPointOnAABB(pos, getBoundingBox());
		Optional<Vec3> clip = hitbox.getHitbox().clip(pos, hitbox.position());
		return clip.orElseGet(hitbox::position);
	}
	
	/**
	 * @return the max speed of the craft along the x and z axis
	 */
    public final float getMaxSpeed() {
		if (isUsingAfterburner()) return getStats().max_speed;
		return getStats().cruise_speed;
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
    	else if (getStats().negativeThrottle && throttle < -1) throttle = -1;
    	else if (!getStats().negativeThrottle && throttle < 0) throttle = 0;
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
    	return getStats().throttleup;
    }
    
    public final float getThrottleDecreaseRate() {
    	return getStats().throttledown;
    }
    
    public final float getMaxDeltaPitch() {
    	return getStats().maxpitch;
    }
    
    public final float getMaxDeltaYaw() {
    	return getStats().maxyaw;
    }
    
    public final float getMaxDeltaRoll() {
    	return getStats().maxroll;
    }
    
    public final float getPitchTorque() {
    	return getStats().torquepitch;
    }
    
    public final float getYawTorque() {
		return getStats().torqueyaw;
    }
    
    public final float getRollTorque() {
		return getStats().torqueroll;
    }
    
    public void increaseThrottle() {
    	setCurrentThrottle(getCurrentThrottle() + getThrottleIncreaseRate());
    }
    
    public void decreaseThrottle() {
    	setCurrentThrottle(getCurrentThrottle() - getThrottleDecreaseRate());
    }
    
    public final QuaternionF getQBySide() {
    	if (isClientSide()) return getClientQ();
    	else return getQ();
    }
    
    public final void setQBySide(QuaternionF q) {
    	if (isClientSide()) setClientQ(q);
    	else setQ(q);
    }
    
    /**
     * @return server side quaternion
     */
    public final QuaternionF getQ() {
        return entityData.get(Q).copy();
    }
    
    /**
     * @param q set server side quaternion
     */
    public final void setQ(QuaternionF q) {
        entityData.set(Q, q.copy());
    }
    
    /**
     * @return the client side rotation
     */
    public final QuaternionF getClientQ() {
        return clientQ.copy();
    }
    
    /**
     * @param q set client side rotation
     */
    public final void setClientQ(QuaternionF q) {
        clientQ = q.copy();
    }
    
    /**
     * @return the rotation on the previous tick for both client and server side
     */
    public final QuaternionF getPrevQ() {
        return prevQ.copy();
    }
    
    /**
     * @param q the rotation for both client and server side
     */
    public final void setPrevQ(QuaternionF q) {
        prevQ = q.copy();
    }
    
    public QuaternionF getClientQ(float partialTicks) {
    	return UtilAngles.lerpQ(partialTicks, getPrevQ(), getClientQ());
    }
    
    public final float getBaseCrossSecArea() {
    	return getStats().cross_sec_area;
    }
    
    public final Vec3 getMoment() {
    	return moment;
    }
    
    public final Vec3 getForces() {
    	return forces;
    }
    
    public final Vec3 getAngularVel() {
    	if (isClientSide()) return clientAV;
    	return entityData.get(AV);
    }
    
    public final void setMoment(Vec3 m) {
    	moment = m;
    }
    
    public final void setForces(Vec3 f) {
    	forces = f;
    }
    
    public final void setAngularVel(Vec3 av) {
    	if (isClientSide()) clientAV = av;
    	else entityData.set(AV, av);
    }
    
    /**
     * 1 is no stealth
     * 0 is invisible
     * @return value to be multiplied to the cross sectional area
     */
    public final float getStealth() {
    	return getStats().stealth;
    }
    
    /**
     * @return the item stack with all of this plane's data 
     */
    public ItemStack getItem() {
    	ItemStack stack = getStats().getItem();
    	CompoundTag tag = new CompoundTag();
    	addAdditionalSaveData(tag);
    	CompoundTag eTag = new CompoundTag();
    	eTag.put("EntityTag", tag);
		eTag.putString("preset", getStatsId());
    	stack.setTag(eTag);
    	return stack;
    }
    
    public boolean canBecomeItem() {
    	int fresh = getWorld().getGameRules().getInt(DSCGameRules.ITEM_COOLDOWN_VEHICLE_FRESH);
    	int shoot = getWorld().getGameRules().getInt(DSCGameRules.ITEM_COOLDOWN_VEHICLE_SHOOT);
    	return tickCount/20 > fresh && (lastShootTime == -1 || (tickCount-lastShootTime)/20 > shoot);
    }

	/**
	 * @return null if can become item
	 */
	@Nullable
	public Component getCantBecomeItemReason(Player player) {
		boolean canItemWhileMoving = getWorld().getGameRules().getBoolean(DSCGameRules.CAN_ITEM_WHILE_MOVING);
		if (!canItemWhileMoving && !isOnGround() && !ignoreToItemFlyCheck())
			return UtilMCText.translatable("error.dscombat.cant_item_while_flying");
		if (!canItemWhileMoving && getDeltaMovement().lengthSqr() > 0.01)
			return UtilMCText.translatable("error.dscombat.cant_item_while_moving");
		EntityRidablePart seat = getPassengerSeat(player);
		if (seat == null)
			return UtilMCText.translatable("error.dscombat.not_a_passenger");
		if (!seat.isPilotSeat())
			return UtilMCText.translatable("error.dscombat.not_a_pilot");
		int fresh = getWorld().getGameRules().getInt(DSCGameRules.ITEM_COOLDOWN_VEHICLE_FRESH);
		int fresh_diff = fresh - tickCount/20;
		if (fresh_diff > 0)
			return UtilMCText.translatable("error.dscombat.cant_item_yet_fresh", fresh_diff);
		if (lastShootTime == -1)
			return null;
		int shoot = getWorld().getGameRules().getInt(DSCGameRules.ITEM_COOLDOWN_VEHICLE_SHOOT);
		int shoot_diff = shoot - (tickCount-lastShootTime)/20;
		if (shoot_diff > 0)
			return UtilMCText.translatable("error.dscombat.cant_item_yet_shoot", shoot_diff);
		return null;
	}
    
    /**
     * SERVER SIDE ONLY
     */
    public void becomeItem(Vec3 pos) {
    	if (isClientSide()) return;
    	ItemStack stack = getItem();
		ItemEntity e = new ItemEntity(getWorld(), pos.x, pos.y, pos.z, stack);
		getWorld().addFreshEntity(e);
		discard();
    }
    
    /**
     * SERVER SIDE ONLY
     */
    public void becomeItem() {
    	if (isClientSide()) return;
    	becomeItem(position());
    }

	/**
	 * SERVER SIDE ONLY
	 */
	public void becomeItem(@NotNull ServerPlayer player) {
		ItemStack item = getItem();
		if (player.getInventory().getFreeSlot() != -1 && player.addItem(item)) {
			discard();
			return;
		}
		becomeItem(player.position());
	}
    
    @Override
    public ItemStack getPickResult() {
    	return getItem();
    }
    
    @Override
	public boolean shouldRenderAtSqrDistance(double dist) {
		return dist < 102400;
	}

	public boolean ignoreToItemFlyCheck() {
		return false;
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
    	return getStats().max_health;
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
    	if (isClientSide()) return;
    	addHealth(100000);
		addArmor(100000);
		repairAllHitboxes();
    	repairAllParts();
		playRepairSound();
    }
    
    public int onRepairTool(float repair) {
    	if (isClientSide()) return 0;
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
    	if (isClientSide()) return;
    	partsManager.repairAllParts();
    }
    
    public void repairAllHitboxes() {
    	if (isClientSide()) return;
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
    	return getStats().idleheat;
    }
    
    /**
     * @return the heat this plane's engines emit at max throttle 
     */
    public float getEngineHeat() {
    	return partsManager.getTotalEngineHeat();
    }
    
    public final float getBaseArmor() {
    	return getStats().base_armor;
    }
    
    public float getMaxTotalArmor() {
    	return getBaseArmor() + partsManager.getTotalExtraArmor();
    }
    
    public List<Player> getRidingPlayers() {
    	List<Player> players = new ArrayList<>();
    	for (EntityRidablePart seat : getSeats()) {
    		Player p = seat.getPlayer();
			if (p != null) players.add(p); 
    	}
    	return players;
    }
    
    public EntityPart getPilotSeat() {
    	return getPartBySlotId(PartSlot.PILOT_SLOT_NAME);
    }
    
    public List<EntityRidablePart> getSeats() {
    	List<EntityRidablePart> seats = new ArrayList<>();
    	for (Entity e : getPassengers())
    		if (e instanceof EntityRidablePart seat)
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
    	if (gimbals.isEmpty()) pilotGimbal = null;
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
     * @param missile the missile this vehicle is being tracked by
     */
    public void trackedByMissile(Entity missile) {
    	if (hasControllingPassenger()) radarSystem.addRWRWarning(
    			missile.getId(), missile.position(), true, false);
    }
    
    /**
     * another radar system calls this server side when tracking this craft
     * @param radar the entity that is tracking this vehicle with radar
     */
    public void lockedOnto(Entity radar) {
    	if (hasControllingPassenger()) radarSystem.addRWRWarning(
    			radar.getId(), radar.position(), false,
				UtilVehicleEntity.isOnGroundOrWater(radar));
    }
    
    public void playIRTone() {
    	entityData.set(PLAY_IR_TONE, true);
    }
    
    public void stopIRTone() {
    	entityData.set(PLAY_IR_TONE, false);
    }

	public boolean shouldPlayLowIRTone() {
		return weaponSystem.getSelected().getStats().isIRMissile();
	}

    public boolean shouldPlayHighIRTone() {
    	return entityData.get(PLAY_IR_TONE);
    }
    
    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
    	if (!isStatsHolderLoaded()) return super.getDimensions(pose);
    	return getStats().dimensions;
    }
    
    @Override
    protected @NotNull AABB makeBoundingBox() {
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
    protected @NotNull AABB getBoundingBoxForPose(@NotNull Pose pose) {
    	return makeBoundingBox();
    }
    
    @Override
    public @NotNull AABB getBoundingBoxForCulling() {
    	return getBoundingBox().inflate(getStats().cameraDistance);
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
    	partsManager.tickFuel();
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
    	if (isClientSide()) return;
    	refillFuel();
		refillAllWeapons();
    }
    
    public void refillFlares() {
    	if (isClientSide()) return;
    	partsManager.addFlares(100000);
    }
    
    public void refillFuel() {
    	if (isClientSide()) return;
    	addFuel(100000);
    	getWorld().playSound(null, this, SoundEvents.BREWING_STAND_BREW,
    			SoundSource.PLAYERS, 1f, 1f);
    }
    
    public void refillAllWeapons() {
    	if (isClientSide()) return;
    	refillFlares();
    	weaponSystem.refillAll();
		for (EntityTurret t : getTurrets()) {
			TurretInstance<?> ti = t.getPartInstance();
			if (ti != null) ti.setWeaponAmmo(100000);
		}
		getWorld().playSound(null, this, SoundEvents.VILLAGER_WORK_TOOLSMITH,
    			SoundSource.PLAYERS, 1f, 1f);
    }
    
    public abstract boolean canToggleLandingGear();
    
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
    
    public boolean toggleLandingGear() {
    	if (!canToggleLandingGear()) return isLandingGear();
    	setLandingGear(!isLandingGear());
		return isLandingGear();
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
     * @return 0 (landing gear out) 1 (landing gear folded)
     */
    public float getLandingGearPos(float partialTicks) {
		return Mth.lerp(partialTicks, landingGearPosOld, landingGearPos);
	}

	public void foldLandingGearNow() {
		landingGearPos = 1;
		landingGearPosOld = 1;
	}
    
    @Override
    public void kill() {
    	super.kill();
    }
    
    public final float getTurnRadius() {
    	return getStats().turn_radius;
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
    public @NotNull Vec3 getDismountLocationForPassenger(@NotNull LivingEntity livingEntity) {
		return super.getDismountLocationForPassenger(livingEntity);
	}
    
    public int getFlareNum() {
		return numFlares;
    }
    
    public boolean hasFlares() {
    	return hasFlares;
    }
    
    public void debug(String debug) {
    	debug(debug, true);
    }
    
    public void debug(String debug, boolean passengerCheck) {
    	if (!passengerCheck || hasControllingPassenger())
    		System.out.println(debug);
    }

	public void debugIf(String debug, boolean condition) {
		if (condition) {
			System.out.println(debug);
		}
	}
    
    protected void debugTick() {
		String side = "SERVER";
		if (isClientSide()) side = "CLIENT";
		System.out.println(side+" TICK "+tickCount+" "+this);
	}
    
    public void toClientPassengers(BaseS2CMessage packet) {
    	if (isClientSide()) return;
		// somehow class cast exception happened here while playing single player on a modded v0.10 client?
		// LocalPlayer cannot be cast to ServerPlayer
    	for (Player p : getRidingPlayers()) if (!UtilEntity.getLevel(p).isClientSide()) // this additional client side check should fix?
            packet.sendTo((ServerPlayer) p);
    }

	public void toTrackers(BaseS2CMessage packet) {
		if (isClientSide()) return;
        PacketHandler.sendToTrackers(packet, this);
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
    public boolean canTrample(@NotNull BlockState state, @NotNull BlockPos pos, float fallDistance) {
    	return true;
    }
    
    @Override
    public boolean isAlliedTo(@NotNull Entity entity) {
    	if (entity == null) return false;
    	Entity c = entity.getControllingPassenger();
    	if (c != null && c.getTeam() != null) return isAlliedTo(c.getTeam());
    	return super.isAlliedTo(entity);
    }
    
    @Override
    public boolean isAlliedTo(@NotNull Team team) {
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
        for (RotableHitbox hitbox : hitboxes)
            if (hitbox.getHitboxName().equals(name))
                return hitbox;
		return null;
	}
	
	@Nullable
	public RotableHitbox getHitboxById(int id) {
        for (RotableHitbox hitbox : hitboxes)
            if (hitbox.getId() == id)
                return hitbox;
		return null;
	}
	
	public boolean isHitboxParent(Entity hitbox) {
        for (RotableHitbox rotableHitbox : hitboxes)
            if (rotableHitbox.equals(hitbox))
                return true;
		return false;
	}
	
	protected void createRotableHitboxes(CompoundTag nbt) {
		CompoundTag hitbox_data = nbt.getCompound("hitbox_data");
		for (RotableHitbox hitbox : hitboxes) hitbox.discard();
		hitboxes.clear();
		hitboxes.addAll(getStats().createRotableHitboxes(this));
        for (RotableHitbox hitbox : hitboxes) {
            hitbox.setPos(position());
            hitbox.readNbt(hitbox_data);
            hitbox.setId(ENTITY_COUNTER.incrementAndGet());
            getWorld().addFreshEntity(hitbox);
        }
	}
	
	protected void saveRotableHitboxes(CompoundTag nbt) {
		CompoundTag hitbox_data = new CompoundTag();
        for (RotableHitbox hitbox : hitboxes) hitbox.writeNbt(hitbox_data);
		nbt.put("hitbox_data", hitbox_data);
	}
	
	public void refreshHitboxes() {
		if (isClientSide()) return;
		CompoundTag nbt = new CompoundTag();
		saveRotableHitboxes(nbt);
        for (RotableHitbox hitbox : hitboxes) hitbox.discard();
		createRotableHitboxes(nbt);
	}
	
	public void addRotableHitboxForClient(RotableHitbox hitbox) {
		if (!isClientSide()) return;
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
		if (isClientSide() && hitboxes.size() < getStats().getHitboxNum() && tickCount % 200 == 20) {
            LOGGER.debug("Vehicle {} on client side has {}/{} hitboxes. Sending hitbox refresh packet. Attempt {}",
					getId(), hitboxes.size(), getStats().getHitboxNum(), ++hitboxRefreshAttempts);
            new ToServerFixHitboxes(this).sendToServer();
		}
		for (RotableHitbox box : hitboxes) box.tick();
		//syncHitboxCollidePositions();
		collidedEntityIds.clear();
		hitboxEntityCoolDown.forEach((id, time) -> {
			if (time > 0) hitboxEntityCoolDown.put(id, --time);
		});
	}
	
	public boolean isEntityHitboxCooldown(Entity entity) {
		return hitboxEntityCoolDown.containsKey(entity.getId()) && hitboxEntityCoolDown.get(entity.getId()) > 0;
	}
	
	public void addEntityToHitboxCooldown(Entity entity) {
		hitboxEntityCoolDown.put(entity.getId(), HITBOX_PUSH_COOLDOWN);
		//System.out.println("adding to hitbox cooldown "+entity);
	}
	
	public void addEntityCollideInfo(Entity entity, RotableHitbox hitbox, Vec3 pos) {
		EntityCollideInfo info = entityCollideInfo.get(entity.getId());
		if (info == null) {
			info = new EntityCollideInfo();
			entityCollideInfo.put(entity.getId(), info);
		}
		info.addPush(hitbox.getId(), tickCount, pos);
	}
	
	public boolean isStuckInHitbox(Entity entity) {
		EntityCollideInfo info = entityCollideInfo.get(entity.getId());
		if (info == null || info.collides.isEmpty()) return false;
		//System.out.println("entity collide info: "+info);
		CollideInfo currentPush = info.collides.get(0);
		if (currentPush.time != tickCount) return false;
		int prevTime = tickCount - 1;
		boolean multiCollideSameTick = false;
		for (int i = 1; i < info.collides.size(); ++i) {
			CollideInfo push = info.collides.get(i);
			if (!multiCollideSameTick) {
				if (push.time == tickCount && push.hitboxId != currentPush.hitboxId) multiCollideSameTick = true;
				else return false;
			}
            if (push.time == prevTime && push.hitboxId == currentPush.hitboxId) {
                return UtilGeometry.isEqual(push.pos, currentPush.pos, 0.001);
            }
        }
		return false;
	}
	
	public double getMaxHitboxY() {
		double max = getY();
        for (RotableHitbox hitbox : hitboxes) {
            double y = hitbox.getMaxY();
            if (y > max) max = y;
        }
		return max;
	}

	public boolean canUseTurnAssist() {
		return getStats().has_turn_assist;
	}

	public boolean isUsingTurnAssist() {
		return canUseTurnAssist() && inputs.turnAssist;
	}

	private static class EntityCollideInfo {
		private final List<CollideInfo> collides = new ArrayList<>();
		EntityCollideInfo() {}
		private void addPush(int hitboxId, int time, Vec3 pos) {
			collides.add(0, new CollideInfo(hitboxId, time, pos));
			while (collides.size() > 10) collides.remove(collides.size()-1);
		}
		@Override
		public String toString() {
			return collides.toString();
		}
	}
	
	private static class CollideInfo {
		private final int hitboxId, time;
		private final Vec3 pos;
		CollideInfo(int hitboxId, int time, Vec3 pos) {
			this.hitboxId = hitboxId;
			this.time = time;
			this.pos = pos;
		}
		@Override
		public String toString() {
			return hitboxId+","+time+","+pos;
		}
	}
	
	private void syncHitboxCollidePositions() {
		if (!isClientSide() || collidedEntityIds.isEmpty() || !isControlledByLocalInstance()) return;
		int[] ids = new int[collidedEntityIds.size()];
		Vec3[] pos = new Vec3[collidedEntityIds.size()];
		int i = 0;
		for (Integer id : collidedEntityIds) {
			ids[i] = id;
			Entity entity = getWorld().getEntity(id);
			if (entity != null) pos[i] = entity.position();
			else pos[i] = new Vec3(0, -1000, 0);
			++i;
		}
        new ToServerSyncRotBoxPassengerPos(ids, pos).sendToServer();
	}
	
	public void addEntityCollidedHitbox(Entity entity) {
		collidedEntityIds.add(entity.getId());
	}
	
	public boolean didEntityAlreadyCollide(Entity entity) {
		return collidedEntityIds.contains(entity.getId());
	}
	
	public boolean areAllHitboxesDead(String... hitbox_names) {
		return hitbox_names.length > 0 && getNumberOfAliveHitboxes(hitbox_names) == 0;
	}
	
	public int getNumberOfAliveHitboxes(String... hitbox_names) {
		int num = 0;
        for (String hitboxName : hitbox_names) {
            RotableHitbox box = getHitboxByName(hitboxName);
            if (box == null) continue;
            if (!box.isDestroyed()) ++num;
        }
		return num;
	}
	
	public MastType getMastType() {
		return getStats().mastType;
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
    	return isUsingAfterburner();
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
    	return getStats().afterBurnerSmokePos;
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
		if (isHydraulicsFailure()) ++hydraulicsFailureTicks;
		else hydraulicsFailureTicks = 0;
		if (isTrackedByMissile()) ++missileTicks;
		else missileTicks = 0;
		if (isTrackedByRadar()) ++trackedTicks;
		else trackedTicks = 0;
    }
    
    @Override
    public void setYRot(float yRot) {
        super.setYRot(yRot);
        DrivingBody.super.setYRot(yRot);
    }
    
    @Override
    public void setXRot(float xRot) {
        super.setXRot(xRot);
		DrivingBody.super.setXRot(xRot);
    }
    
    @Override
	public void absMoveTo(double pX, double pY, double pZ, float pYRot, float pXRot) {
    	absMoveTo(pX, pY, pZ);
    	// setting rotation here messes up rotation syncing
	}

	public void setYRotNoQ(float yRot) {
        super.setYRot(yRot);
    }
    
    public void setXRotNoQ(float xRot) {
        super.setXRot(xRot);
    }
    
    public boolean isAircraft() {
    	return getStats().isAircraft();
    }
    
    public int getGroundTicks() {
    	return groundTicks;
    }
    
    public boolean rootHitboxEntityInteract() {
    	return !getStats().rootHitboxNoCollide;
    }
    
    public boolean canControlPitch() {
		return isPitchControllable() && !areAllHitboxesDead(getStats().controllPitchHitboxNames);
	}
	
	public boolean canControlYaw() {
		return isYawControllable() && !areAllHitboxesDead(getStats().controllYawHitboxNames);
	}
	
	public boolean canControlRoll() {
		return isRollControllable() && !areAllHitboxesDead(getStats().controllRollHitboxNames);
	}

	public boolean isPitchControllable() {
		return true;
	}

	public boolean isYawControllable() {
		return true;
	}

	public boolean isRollControllable() {
		return true;
	}

	public float getYawRate() {
		return getYRot() - yRotO;
	}

	public float getActualTurnRadius() {
		return xzSpeed / (getYawRate() * Mth.DEG_TO_RAD);
	}

	public double getMaxAltitude() {
		return getStats().max_altitude;
	}

	public double getAltitude() {
		return currentAltitude;
	}

	public boolean canReload(Player player) {
		if (!isPilotOrCopilot(player)) {
			player.displayClientMessage(
					UtilMCText.translatable("error.dscombat.not_a_pilot"),
					true);
			return false;
		} else if (xzSpeed > 0.1) {
			player.displayClientMessage(
					UtilMCText.translatable("error.dscombat.cant_load_while_moving"),
					true);
			return false;
		} else if (!isOnGround()) {
			player.displayClientMessage(
					UtilMCText.translatable("error.dscombat.cant_load_while_flying"),
					true);
			return false;
		}
		return true;
	}

	public boolean isPilotOrCopilot(Entity entity) {
		EntityRidablePart seat = getPassengerSeat(entity);
		if (seat == null) return false;
		return seat.canPassengerShootParentWeapon();
	}

	public boolean jetesinPart(String slotId) {
		return partsManager.dropPartInSlot(slotId);
	}
	/**
	 * SERVER SIDE ONLY
	 */
	public void setOwner(Entity owner) {
		this.owner = owner;
		this.owner_uuid = owner.getUUID();
	}
	/**
	 * SERVER SIDE ONLY
	 */
	@Nullable
	public Entity getOwner() {
		if (owner_uuid == null) return null;
		if (owner == null || getWorld().getEntity(owner_id) == null) {
			owner = getWorld().getPlayerByUUID(owner_uuid);
			if (owner != null) owner_id = owner.getId();
			else owner_id = -1;
		}
		return owner;
	}

	public enum PermMode {
		PUBLIC, ALLIES, PRIVATE;
		public String getTranslatable() {
			return "permmode.dscombat."+name().toLowerCase();
		}
	}

	public PermMode getPermMode() {
		return entityData.get(PERM_MODE);
	}

	public void setPermMode(PermMode mode) {
		entityData.set(PERM_MODE, mode);
	}
	/**
	 * SERVER SIDE ONLY
	 */
	public boolean hasPermission(@NotNull Entity entity) {
		if (getWorld().isClientSide()) return false;
		if (DSCGameRules.isForcePublicPerm(getWorld())) return true;
		if (getPermMode() == PermMode.PUBLIC) return true;
		Entity owner = getOwner();
		if (getPermMode() == PermMode.ALLIES) {
			if (entity.equals(owner)) return true;
			else if (owner != null) return UtilEntity.areEntitiesAllied(owner, entity);
			else return false;
		} else {
			return entity.equals(owner);
		}
	}
	/**
	 * SERVER SIDE ONLY
	 */
	public boolean isOwner(Entity entity) {
		return entity.equals(getOwner());
	}
	/**
	 * SERVER SIDE ONLY
	 */
	public boolean hasOwner() {
		return owner_uuid != null;
	}

	public boolean isStationaryRadar() {
		return getStats().isStationaryRadar();
	}

	@Override
	public @Nullable String getAssetId() {
		return getStats().getAssetId();
	}

	@Override
	public @NotNull JsonPresetReloadListener<VehicleStats> getPresets() {
		return VehiclePresets.get();
	}

	@Override
	public @Nullable JsonPresetAssetReader<VehicleClientStats> getClientPresets() {
		return VehicleClientPresets.get();
	}

	@Override
	public @NotNull Component getName() {
		Component name = getCustomName();
		if (name != null) return name;
		Entity owner = getOwner();
		if (owner != null) return UtilMCText.empty().append(owner.getDisplayName())
					.append("'s ").append(getStats().getBaseDisplayName());
		return getStats().getBaseDisplayName();
	}

	public int getPullUpWarningTicks() {
		return 0;
	}

	public int getAltitudeWarningTicks() {
		return 0;
	}

	public boolean isHydraulicsFailure() {
		return !(canControlPitch() && canControlRoll() && canControlYaw());
	}

	public int getHydraulicsFailureTicks() {
		return hydraulicsFailureTicks;
	}

	public boolean isTrackedByMissile() {
		return radarSystem.isTrackedByMissile();
	}

	public int getMissileTicks() {
		return missileTicks;
	}

	public boolean isTrackedByRadar() {
		return radarSystem.isTrackedByRadar();
	}

	public int getTrackedTicks() {
		return trackedTicks;
	}

	public boolean isAfterBurnerEnabled() {
		return inputs.afterburner;
	}

	public boolean canUseAfterburner() {
		return getStats().canUseAfterBurner();
	}

	private boolean afterBurnerOverride = false;

	public boolean isUsingAfterburner() {
		return afterBurnerOverride || (canUseAfterburner() && isAfterBurnerEnabled() && getCurrentThrottle() > 0.8);
	}

	public void setUseAfterBurnerOverride(boolean enable) {
		afterBurnerOverride = enable;
	}

	public double getFluidDensity() {
		if (isInWater()) return DSCPhyCons.WATER_FLUID_DENSITY;
		return getAirDensity();
	}

	public double getAirDensity() {
		return airDensity;
	}

	@Override
	public float getAngularDragScale() {
		return DSCPhyCons.ANGULAR_DRAG_C;
	}

	@Override
	public List<PhysicsComponentInstance<?>> getPhysicsInstances() {
		return physicsInstances;
	}

	@Override
	public double getAccTimeScale() {
		return DSCPhyCons.ACC_TIME_SCALE;
	}

	@Override
	public double getLerpMaxXZ() {
		return maxXZ;
	}

	@Override
	public void setLerpMaxXZ(double maxXZ) {
		this.maxXZ = maxXZ;
	}

	@Override
	public double getAccGravity() {
		return DSCPhyCons.GRAVITY;
	}

	@Override
	public float getZRot() {
		return zRot;
	}

	@Override
	public void setZRot(float rot) {
		this.zRot = rot;
	}

	@Override
	public float getPrevZRot() {
		return zRotO;
	}

	@Override
	public void setPrevZRot(float rot) {
		this.zRotO = rot;
	}

	@Override
	public Vec3 getPrevDeltaMove() {
		return prevMotion;
	}

	@Override
	public void setPrevDeltaMove(Vec3 move) {
		this.prevMotion = move;
	}

	@Override
	public Vec3 getForcesBetweenTicks() {
		return addForceBetweenTicks;
	}

	@Override
	public void setForcesBetweenTicks(Vec3 forces) {
		this.addForceBetweenTicks = forces;
	}

	@Override
	public Vec3 getPrevForces() {
		return forcesO;
	}

	@Override
	public void setPrevForces(Vec3 forces) {
		this.forcesO = forces;
	}

	@Override
	public Vec3 getPrevMoment() {
		return momentO;
	}

	@Override
	public void setPrevMoment(Vec3 moment) {
		this.momentO = moment;
	}

	@Override
	public Vec3 getControlMoment() {
		return controlMoment;
	}

	@Override
	public void setControlMoment(Vec3 moment) {
		this.controlMoment = moment;
	}

	@Override
	public Vec3 getMomentBetweenTicks() {
		return addMomentBetweenTicks;
	}

	@Override
	public void setMomentBetweenTicks(Vec3 moment) {
		this.addMomentBetweenTicks = moment;
	}

	@Override
	public float getPitchInput() {
		return inputs.pitch;
	}

	@Override
	public float getYawInput() {
		return inputs.yaw;
	}

	@Override
	public float getRollInput() {
		return inputs.roll;
	}

	@Override
	public double getKineticFriction() {
		return kineticFric;
	}

	@Override
	public double getStaticFriction() {
		return staticFric;
	}

	@Override
	public double getGroundBreaksDeAcceleration() {
		return getStats().break_deacc_ground * getHorizontalSpeedScaleOrOne();
	}

    @Override
    public double getMinDriveAcc() {
        return getStats().min_drive_acc * getHorizontalSpeedScaleOrOne() * getCurrentThrottle();
    }

	@Override
	public double getAirBreaksDeAcceleration() {
		return getStats().break_deacc_air * getHorizontalSpeedScaleOrOne();
	}

	@Override
	public void setXZSpeed(float speed) {
		xzSpeed = speed;
	}

	@Override
	public void setXZSpeedDir(int direction) {
		xzSpeedDir = direction;
	}

	@Override
	public float getXZYaw() {
		return xzYaw;
	}

	@Override
	public void setXZYaw(float angle) {
		xzYaw = angle;
	}

	@Override
	public float getSlideAngle() {
		return slideAngle;
	}

	@Override
	public void setSlideAngle(float angle) {
		slideAngle = angle;
	}

	@Override
	public float getSlideAngleCos() {
		return slideAngleCos;
	}

	@Override
	public void setSlideAngleCos(float angle) {
		slideAngleCos = angle;
	}

	public float getGroundXTilt() {
		return getStats().groundXTilt;
	}

	public boolean canFlattenOnGround() {
		return isOperational();
	}

	@Override
	public int getAge() {
		return tickCount;
	}

	@Override
	public boolean isClientSide() {
		return getWorld().isClientSide();
	}

	public boolean wasInWater() {
		return wasInWater;
	}

	public boolean isArcadeMode() {
		return false;
	}

	public boolean isHardCodedRotAcc() {
		return getStats().is_hard_coded_rot_acc;
	}

	public Vec3 getHardCodedRotAcc() {
		return getStats().hard_coded_rot_acc;
	}

	public float getHardCodedRotDecel() {
		return getStats().hard_coded_rot_decel;
	}

	@Override
	public void remove(@NotNull RemovalReason reason) {
		radarSystem.onParentRemove();
		super.remove(reason);
	}

    /**
     * DO NOT REFACTOR THIS TO getLevel OR ELSE IT WILL LOOP INFINITELY
     */
    public @NotNull Level getWorld() {
        return UtilEntity.getLevel(this);
    }

    @Override
    public boolean isOnGround() {
        return onGround();
    }

    @Override
    public boolean isInWater() {
        return super.isInWater();
    }

    @Override
    public float getXRot() {
        return super.getXRot();
    }

    @Override
    public float getYRot() {
        return super.getYRot();
    }

    @Override
    public @NotNull Vec3 getDeltaMovement() {
        return super.getDeltaMovement();
    }

    @Override
    public void setDeltaMovement(Vec3 move) {
        super.setDeltaMovement(move);
    }

    @Override
    public void setDeltaMovement(double x, double y, double z) {
        super.setDeltaMovement(x, y, z);
    }

    @Override
    public @NotNull Vec3 getLookAngle() {
        return super.getLookAngle();
    }
}
