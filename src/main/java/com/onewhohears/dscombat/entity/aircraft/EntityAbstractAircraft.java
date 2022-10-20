package com.onewhohears.dscombat.entity.aircraft;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.event.ClientForgeEvents;
import com.onewhohears.dscombat.common.container.AircraftMenuContainer;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ServerBoundRequestPlaneDataPacket;
import com.onewhohears.dscombat.data.AircraftPresets;
import com.onewhohears.dscombat.data.parts.PartsManager;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponSystem;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.weapon.EntityFlare;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilMCText;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;

public abstract class EntityAbstractAircraft extends Entity {
	
	public static final EntityDataAccessor<Float> MAX_HEALTH = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> MAX_SPEED = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> THROTTLE = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> THROTTLEUP = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> THROTTLEDOWN = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Quaternion> Q = SynchedEntityData.defineId(EntityAbstractAircraft.class, DataSerializers.QUATERNION);
	public static final EntityDataAccessor<Boolean> FREE_LOOK = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Integer> FLARES = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Float> STEALTH = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> ROLL = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> PITCH = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> YAW = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> IDLEHEAT = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> ENGINEHEAT = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> WEIGHT = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> MAX_THRUST = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> WING_AREA = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Integer> MISSILE_TRACKED_TICKS = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> LOCKED_ONTO_TICKS = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.INT);
	
	public static final double collideSpeedThreshHold = 1d;
	public static final double collideDamageRate = 200d;
	
	public PartsManager partsManager = new PartsManager();
	public WeaponSystem weaponSystem = new WeaponSystem();
	public RadarSystem radarSystem = new RadarSystem();
	public Quaternion prevQ = Quaternion.ONE.copy();
	//public Quaternion clientQ = Quaternion.ONE.copy();
	
	public boolean inputThrottleUp, inputThrottleDown;
	public boolean inputMouseMode, inputFlare, inputShoot, inputSelect;
	public boolean inputOpenMenu;
	public float inputPitch, inputRoll, inputYaw;
	public float prevXRot, prevYRot, zRot, prevZRot;
	public Vec3 prevMotion = Vec3.ZERO;
	
	protected final ResourceLocation TEXTURE;
	
	private int lerpSteps/*, lerpStepsQ*/, newRiderCooldown;
	private double lerpX, lerpY, lerpZ, lerpXRot, lerpYRot/*, lerpZRot*/;
	
	public EntityAbstractAircraft(EntityType<? extends EntityAbstractAircraft> entity, Level level, ResourceLocation texture) {
		super(entity, level);
		this.TEXTURE = texture;
		this.blocksBuilding = true;
	}
	
	@Override
	protected void defineSynchedData() {
		entityData.define(MAX_HEALTH, 100f);
        entityData.define(HEALTH, 100f);
		entityData.define(MAX_SPEED, 1.5f);
		entityData.define(THROTTLE, 0.0f);
		entityData.define(THROTTLEUP, 0.05f);
		entityData.define(THROTTLEDOWN, 0.05f);
		entityData.define(Q, Quaternion.ONE);
		entityData.define(FREE_LOOK, true);
		entityData.define(FLARES, 0);
		entityData.define(STEALTH, 1f);
		entityData.define(ROLL, 1f);
		entityData.define(PITCH, 1f);
		entityData.define(YAW, 1f);
		entityData.define(IDLEHEAT, 1f);
		entityData.define(ENGINEHEAT, 1f);
		entityData.define(WEIGHT, 0.05f);
		entityData.define(MAX_THRUST, 0.1f);
		entityData.define(WING_AREA, 1f);
		entityData.define(MISSILE_TRACKED_TICKS, 0);
		entityData.define(LOCKED_ONTO_TICKS, 0);
	}
	
	@Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (Q.equals(key) && level.isClientSide() && !isControlledByLocalInstance()) {
            if (firstTick) {
                //lerpStepsQ = 0;
                //setClientQ(getQ());
                setPrevQ(getQ());
            } else {
                //lerpStepsQ = 10;
            }
        }
    }
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		String initType = compound.getString("preset");
		System.out.println("aircraft read data client side = "+level.isClientSide+" init type = "+initType);
		if (!initType.isEmpty()) {
			CompoundTag tag = AircraftPresets.getPreset(initType);
			if (tag != null) compound.merge(tag);
		}
		partsManager = new PartsManager(compound);
		System.out.println(partsManager);
		weaponSystem = new WeaponSystem(compound);
		radarSystem = new RadarSystem(compound);
		this.setMaxSpeed(compound.getFloat("max_speed"));
		this.setMaxHealth(compound.getFloat("max_health"));
		this.setHealth(compound.getFloat("health"));
		zRot = compound.getFloat("zRot");
		Quaternion q = new Quaternion(getXRot(), getYRot(), zRot, true);
		setQ(q);
		setPrevQ(q);
		//setClientQ(q);
		this.setFlares(compound.getInt("flares"));
		this.setStealth(compound.getFloat("stealth"));
		this.setMaxDeltaRoll(compound.getFloat("maxroll"));
		this.setMaxDeltaPitch(compound.getFloat("maxpitch"));
		this.setMaxDeltaYaw(compound.getFloat("maxyaw"));
		this.setThrottleIncreaseRate(compound.getFloat("throttleup"));
		this.setThrottleDecreaseRate(compound.getFloat("throttledown"));
		this.setIdleHeat(compound.getFloat("idleheat"));
		this.setEngineHeat(compound.getFloat("engineheat"));
		this.setAircraftWeight(compound.getFloat("weight"));
		this.setMaxThrust(compound.getFloat("maxthrust"));
		this.setSurfaceArea(compound.getFloat("surfacearea"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putString("preset", "");
		partsManager.write(compound);
		weaponSystem.write(compound);
		radarSystem.write(compound);
		compound.putFloat("max_speed", this.getMaxSpeed());
		compound.putFloat("max_health", this.getMaxHealth());
		compound.putFloat("health", this.getHealth());
		compound.putFloat("zRot", zRot);
		compound.putInt("flares", this.getFlares());
		compound.putFloat("stealth", this.getStealth());
		compound.putFloat("maxroll", this.getMaxDeltaRoll());
		compound.putFloat("maxpitch", this.getMaxDeltaPitch());
		compound.putFloat("maxyaw", this.getMaxDeltaYaw());
		compound.putFloat("throttleup", this.getThrottleIncreaseRate());
		compound.putFloat("throttledown", this.getThrottleDecreaseRate());
		compound.putFloat("idleheat", this.getIdleHeat());
		compound.putFloat("engineheat", this.getEngineHeat());
		compound.putFloat("weight", this.getAircraftWeight());
		compound.putFloat("maxthrust", this.getMaxThrust());
		compound.putFloat("surfacearea", this.getSurfaceArea());
	}
	
	public void init() {
		if (!level.isClientSide) setupAircraftParts();
		else clientSetup();
	}
	
	@Override
	public void tick() {
		//System.out.println(this.tickCount+" "+this.level);
		if (this.firstTick) init();
		super.tick();
		if (!level.isClientSide) weaponSystem.tick();
		if (Double.isNaN(getDeltaMovement().length())) {
            setDeltaMovement(Vec3.ZERO);
        }
		if (this.getPassengers().size() == 0) {
			this.setCurrentThrottle(0);
		}
		prevXRot = getXRot();
		prevYRot = getYRot();
		prevZRot = zRot;
		if (level.isClientSide && !isControlledByLocalInstance()) {
			tickLerp();
		}
		Quaternion q = getQ();
		/*Quaternion q;
        if (level.isClientSide) {
            q = getClientQ();
        } else {
            q = getQ();
        }*/
		setPrevQ(q);
		controlDirection(q);
    	tickMovement(q);
    	motionClamp();
    	//System.out.println("MOTION "+getDeltaMovement());
		move(MoverType.SELF, getDeltaMovement());
		if (this.isControlledByLocalInstance()) 
			this.syncPacketPositionCodec(getX(), getY(), getZ());
		q = UtilAngles.normalizeQuaternion(q);
		EulerAngles angles = UtilAngles.toDegrees(q);
		setXRot((float)angles.pitch);
		setYRot((float)angles.yaw);
		zRot = (float)angles.roll;
		/*if (!level.isClientSide) {
			System.out.println("x = "+this.getXRot()+" y = "+this.getYRot());
			System.out.println("roll axis "+UtilAngles.getRollAxis(q));
			System.out.println("yaw  axis "+UtilAngles.getYawAxis(q));
		}*/
		//setPrevQ(getClientQ());
        setQ(q);
        /*if (level.isClientSide && isControlledByLocalInstance()) {
            setClientQ(q);
        }*/
        controlSystem();
		tickLerp();
		/*System.out.println("######### client "+this.level.isClientSide);
		System.out.println("P "+this.getPrevQ());
		System.out.println("C "+this.getClientQ());
		System.out.println("Q "+this.getQ());*/
		if (!this.level.isClientSide) {
			if (this.verticalCollisionBelow || this.verticalCollision) {
				double my = Math.abs(prevMotion.y);
				if (my > 0) System.out.println("COLLISION SPEED "+my);
				if (my > collideSpeedThreshHold) {
					this.addHealth((float)(-(my-collideSpeedThreshHold) * collideDamageRate));
				}
			}
		}
		if (getHealth() <= 0) {
			kill();
			explode(null);
		}
		if (level.isClientSide) clientTick();
		this.prevMotion = this.getDeltaMovement();
		sounds();
	}
	
	public void clientTick() {
		healthSmoke();
	}
	
	private void healthSmoke() {
		float r = getHealth() / getMaxHealth();
		if (r < 0.5f) smoke();
		if (r < 0.3f) for (int i = 0; i < 2; ++i) smoke();
		if (r < 0.1f) for (int i = 0; i < 4; ++i) smoke();
	}
	
	private void smoke() {
		if (Math.random() > 0.4d) return;
		level.addParticle(ParticleTypes.SMOKE, 
				this.getX(), this.getY(), this.getZ(), 
				random.nextGaussian() * 0.08D, 
				0.1D, 
				random.nextGaussian() * 0.08D);
	}
	
	public void motionClamp() {
		Vec3 motion = getDeltaMovement();
		double max = getMaxSpeed();
		Vec3 motionXY = new Vec3(motion.x, 0, motion.z);
		double velXY = motionXY.length();
		if (velXY > max) motionXY = motionXY.scale(max / velXY);
		double maxY = 2d;
		double my = motion.y;
		if (Math.abs(my) > maxY) my = maxY * Math.signum(my);
		setDeltaMovement(motionXY.x, my, motionXY.z);
	}
	
	public void controlDirection(Quaternion q) {
		if (this.getControllingPassenger() == null) this.resetControls();
		if (inputThrottleUp) this.increaseThrottle();
		if (inputThrottleDown) this.decreaseThrottle();
		
		q.mul(new Quaternion(Vector3f.ZP, inputRoll*getMaxDeltaRoll(), true));
		q.mul(new Quaternion(Vector3f.XN, inputPitch*getMaxDeltaPitch(), true));
		q.mul(new Quaternion(Vector3f.YN, inputYaw*getMaxDeltaYaw(), true));
	}
	
	public void tickMovement(Quaternion q) {
		if (onGround) tickGround(q);
		else tickAir(q);
	}
	
	public void tickGround(Quaternion q) {
		Vec3 motion = getDeltaMovement();
		if (motion.y < 0) motion = new Vec3(motion.x, 0, motion.z);
		motion = motion.add(getWeightForce());
		motion = motion.add(getThrustForce(q));
		motion = motion.add(getFrictionForce());
		setDeltaMovement(motion);
	}
	
	public void tickAir(Quaternion q) {
		Vec3 motion = getDeltaMovement();
		motion = motion.add(getWeightForce());
		motion = motion.add(getThrustForce(q));
		motion = motion.add(getDragForce(q));
		setDeltaMovement(motion);
		this.resetFallDistance();
	}
	
	public Vec3 getThrustForce(Quaternion q) {
		Vec3 direction = UtilAngles.getRollAxis(q);
		Vec3 thrustForce = direction.scale(getThrust());
		//System.out.println("thrust force = "+thrustForce);
		return thrustForce;
	}
	
	public double getThrust() {
		float throttle = getCurrentThrottle();
		return throttle * getMaxThrust();
	}
	
	public float getMaxThrust() {
		return entityData.get(MAX_THRUST);
	}
	
	public void setMaxThrust(float thrust) {
		if (thrust < 0) thrust = 0;
		entityData.set(MAX_THRUST, thrust);
	}
	
	public Vec3 getDragForce(Quaternion q) {
		Vec3 direction = getDeltaMovement().normalize();
		Vec3 dragForce = direction.scale(-getDrag(q));
		//System.out.println("drag force = "+dragForce);
		return dragForce;
	}
	
	public double getDrag(Quaternion q) {
		// Drag = (drag coefficient) * (air pressure) * (speed)^2 * (wing surface area) / 2
		double dc = 0.025;
		double air = getAirPressure();
		double speedSqr = getDeltaMovement().lengthSqr();
		double wing = getSurfaceArea();
		return dc * air * speedSqr * wing / 2;
	}
	
	public double getAirPressure() {
		double space = 500;
		double water = 64;
		double scale = 1;
		if (getY() > space) return 0;
		if (getY() < water) return scale;
		return scale/(water-space) * (getY()-water) + scale;
	}
	
	public float getSurfaceArea() {
		return entityData.get(WING_AREA);
	}
	
	public void setSurfaceArea(float area) {
		if (area < 0) area = 0;
		entityData.set(WING_AREA, area);
	}
	
	public Vec3 getFrictionForce() {
		double x = getDeltaMovement().x;
		double z = getDeltaMovement().z;
		Vec3 direction = new Vec3(-x, 0, -z).normalize();
		return direction.scale(getFriction());
	}
	
	public double getFriction() {
		double x = getDeltaMovement().x;
		double z = getDeltaMovement().z;
		double speed = Math.sqrt(x*x + z*z);
		if (speed < 0.05) return speed;
		return 0.05;
	}
	
	public Vec3 getWeightForce() {
		return new Vec3(0, -getTotalWeight(), 0);
	}
	
	public float getTotalWeight() {
		return getAircraftWeight() + partsManager.getPartsWeight();
	}
	
	public float getAircraftWeight() {
		return entityData.get(WEIGHT);
	}
	
	public void setAircraftWeight(float weight) {
		if (weight < 0) weight = 0;
		entityData.set(WEIGHT, weight);
	}
	
	public void controlSystem() {
		Entity controller = this.getControllingPassenger();
		if (controller == null) return;
		boolean isPlayer = controller instanceof ServerPlayer;
		if (!level.isClientSide) {
			radarSystem.tickUpdateTargets();
			if (newRiderCooldown > 0) --newRiderCooldown;
			else {
				if (this.inputShoot) shoot(controller, isPlayer);
				if (this.inputFlare && tickCount % 5 == 0) flare(controller, isPlayer);
			}
			if (this.inputOpenMenu && isPlayer) {
				System.out.println("OPENING MENU "+partsManager);
				NetworkHooks.openScreen((ServerPlayer) controller, 
						new SimpleMenuProvider((windowId, playerInv, player) -> 
								new AircraftMenuContainer(windowId, playerInv), 
						Component.translatable("container.dscombat.plane_menu")));
			}
		}
	}
	
	public void shoot(Entity controller, boolean isPlayer) {
		WeaponData data = weaponSystem.getSelected();
		if (data == null) return;
		data.shoot(level, this, controller, UtilAngles.getRollAxis(getQ()), this.getQ());
		if (data.isFailedLaunch() && data.getFailedLaunchReason() != null) {
			//System.out.println(data.getFailedLaunchReason());
			if (isPlayer) {
				((ServerPlayer)controller).displayClientMessage(
						UtilMCText.simpleText(data.getFailedLaunchReason()), true);
			}
		}
	}
	
	public void flare(Entity controller, boolean isPlayer) {
		int flares = this.getFlares();
		int used = 1;
		if (isPlayer) {
			ServerPlayer p = (ServerPlayer) controller;
			if (p.isCreative()) {
				used = 0;
			}
		} 
		if (flares < used) return;
		EntityFlare flare = new EntityFlare(level, 4.0f, 100, 3);
		flare.setPos(this.position().add(0, -0.1, 0));
		level.addFreshEntity(flare);
		if (used > 0) this.setFlares(flares-used);
	}
	
	@Override
	public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        if (x == getX() && y == getY() && z == getZ()) return;
        lerpX = x; lerpY = y; lerpZ = z;
        lerpYRot = yaw; lerpXRot = pitch;
        lerpSteps = 10;
    }
	
	private void tickLerp() {
		if (this.isControlledByLocalInstance()) {
			this.lerpSteps = 0;
			//this.lerpStepsQ = 0;
		}
		if (this.lerpSteps > 0) {
			double d0 = this.getX() + (this.lerpX - this.getX()) / (double)this.lerpSteps;
	        double d1 = this.getY() + (this.lerpY - this.getY()) / (double)this.lerpSteps;
	        double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double)this.lerpSteps;
	        double d3 = Mth.wrapDegrees(this.lerpYRot - (double)this.getYRot());
	        this.setYRot(this.getYRot() + (float)d3 / (float)this.lerpSteps);
	        this.setXRot(this.getXRot() + (float)(this.lerpXRot - (double)this.getXRot()) / (float)this.lerpSteps);
	        --this.lerpSteps;
	        this.setPos(d0, d1, d2);
	        this.setRot(this.getYRot(), this.getXRot());
		}
		/*if (lerpStepsQ > 0) {
			this.setPrevQ(this.getClientQ());
			this.setClientQ(UtilAngles.lerpQ(1f / lerpSteps, this.getClientQ(), this.getQ()));
			--lerpStepsQ;
		} else if (lerpStepsQ == 0) {
			this.setPrevQ(this.getClientQ());
			this.setClientQ(this.getQ());
			--lerpStepsQ;
		}*/
	}
	
	public void updateControls(boolean throttleUp, boolean throttleDown, 
			float pitch, float roll, float yaw,
			boolean mouseMode, boolean flare, boolean shoot, boolean select,
			boolean openMenu) {
		this.inputThrottleUp = throttleUp;
		this.inputThrottleDown = throttleDown;
		this.inputPitch = pitch;
		this.inputRoll = roll;
		this.inputYaw = yaw;
		this.inputFlare = flare;
		this.inputMouseMode = mouseMode;
		if (inputMouseMode) setFreeLook(!isFreeLook());
		this.inputShoot = shoot;
		this.inputSelect = select;
		if (this.inputSelect && !this.level.isClientSide) weaponSystem.selectNextWeapon();
		this.inputOpenMenu = openMenu;
	}
	
	public void resetControls() {
		this.inputThrottleUp = false;
		this.inputThrottleDown = false;
		this.inputPitch = 0;
		this.inputRoll = 0;
		this.inputYaw = 0;
		this.inputFlare = false;
		this.inputMouseMode = false;
		this.inputShoot = false;
		this.inputSelect = false;
		this.inputOpenMenu = false;
		this.setCurrentThrottle(0);
	}
	
	public boolean isFreeLook() {
    	return entityData.get(FREE_LOOK);
    }
    
    public void setFreeLook(boolean freeLook) {
    	entityData.set(FREE_LOOK, freeLook);
    }
	
    /**
     * register parts before calling super in server side
     */
	public void setupAircraftParts() {
		System.out.println("setting up parts client side = "+level.isClientSide);
		partsManager.setupParts(this);
		weaponSystem.setup(this);
		radarSystem.setup(this);
		/*PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), 
				new ClientBoundPlaneDataPacket(this.getId(), partsManager, weaponSystem, radarSystem));*/
	}
	
	public void clientSetup() {
		//if (!partsManager.isReadData() || !weaponSystem.isReadData() || !radarSystem.isReadData())
		PacketHandler.INSTANCE.sendToServer(new ServerBoundRequestPlaneDataPacket(getId()));
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		//System.out.println("interact with plane client side "+level.isClientSide);
		if (player.isSecondaryUseActive()) {
			//System.out.println("secondary use");
			return InteractionResult.PASS;
		} else if (player.getRootVehicle() != null && player.getRootVehicle().equals(this)) {
			//System.out.println("root vehicle same");
			return InteractionResult.PASS;
		} else if (!this.level.isClientSide) {
			//System.out.println("server side");
			boolean okay = rideSeat(player);
			//System.out.println("rideSeat = "+okay);
			return okay ? InteractionResult.CONSUME : InteractionResult.PASS;
		} else if (this.level.isClientSide) {	
			//System.out.println("client side");
			Minecraft m = Minecraft.getInstance();
			if (m.player.equals(player)) ClientForgeEvents.centerMouse();
			return InteractionResult.SUCCESS;
		} 
		//System.out.println("end");
		return InteractionResult.SUCCESS;
	}
	
	public boolean rideSeat(Entity e) {
		List<Entity> list = getPassengers();
		for (int i = 0; i < list.size(); ++i) {
			System.out.println("can "+e+" ride "+list.get(i));
			if (list.get(i) instanceof EntitySeat seat) {
				if (seat.canAddPassenger(e)) {
					if (e.startRiding(seat)) {
						this.newRiderCooldown = 10;
						return true;
					}
					System.out.println("can't start riding");
				}
				System.out.println("seat can't add passenger");
			}
			System.out.println("not seat");
		}
		return false;
	}
	
	@Override
    public void positionRider(Entity passenger) {
		//if (!this.hasPassenger(passenger)) return;
		//System.out.println("POSITION RIDER "+passenger);
		if (!(passenger instanceof EntitySeat seat)) return;
 		Vec3 pos = position();
		Vec3 seatPos = UtilAngles.rotateVector(seat.getRelativePos(), getQ());
		passenger.setPos(pos.add(seatPos));
	}
	
	/*@Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
		return super.getDismountLocationForPassenger(livingEntity);
	}*/
	
	@Nullable
	@Override
    public Entity getControllingPassenger() {
        List<Entity> list = getPassengers();
        if (list.isEmpty()) return null;
        if (!(list.get(0) instanceof EntitySeat seat)) return null;
        return seat.getPlayer();
    }
	
	@Override
	public boolean isPickable() {
		return !this.isRemoved();
	}
	
	@Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        System.out.println("AIRCRAFT ADDED PASSENGER "+passenger);
	}
	
	@Override
    protected boolean canAddPassenger(Entity passenger) {
		System.out.println("CAN ADD PASSENGER "+passenger);
		return passenger instanceof EntitySeat;
	}
	
	@Override
    protected boolean canRide(Entity entityIn) {
		System.out.println("CAN RIDE "+entityIn);
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
    public boolean hurt(DamageSource source, float amount) {
		if (this.isVehicleOf(source.getEntity())) return false;
		addHealth(-amount);
		return true;
	}
	
	public void explode(DamageSource source) {
		if (!this.level.isClientSide) {
			level.explode(this, source,
					null, getX(), getY(), getZ(), 
					3, true, 
					Explosion.BlockInteraction.BREAK);
		} else {
			level.addParticle(ParticleTypes.LARGE_SMOKE, 
					this.getX(), this.getY()+0.5D, this.getZ(), 
					0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
    public float getMaxSpeed() {
    	return entityData.get(MAX_SPEED);
    }
    
    public void setMaxSpeed(float maxSpeed) {
    	entityData.set(MAX_SPEED, maxSpeed);
    }
    
    public float getCurrentThrottle() {
    	return entityData.get(THROTTLE);
    }
    
    public void setCurrentThrottle(float throttle) {
    	if (throttle > 1f) throttle = 1f;
    	if (throttle < 0) throttle = 0;
    	entityData.set(THROTTLE, throttle);
    }
    
    public float getThrottleIncreaseRate() {
    	return entityData.get(THROTTLEUP);
    }
    
    public void setThrottleIncreaseRate(float value) {
    	if (value < 0) value = 0;
    	entityData.set(THROTTLEUP, value);
    }
    
    public float getThrottleDecreaseRate() {
    	return entityData.get(THROTTLEDOWN);
    }
    
    public void setThrottleDecreaseRate(float value) {
    	if (value < 0) value = 0;
    	entityData.set(THROTTLEDOWN, value);
    }
    
    public float getMaxDeltaPitch() {
    	return entityData.get(PITCH);
    }
    
    public void setMaxDeltaPitch(float degrees) {
    	if (degrees < 0) degrees = 0;
    	entityData.set(PITCH, degrees);
    }
    
    public float getMaxDeltaYaw() {
    	return entityData.get(YAW);
    }
    
    public void setMaxDeltaYaw(float degrees) {
    	if (degrees < 0) degrees = 0;
    	entityData.set(YAW, degrees);
    }
    
    public float getMaxDeltaRoll() {
    	return entityData.get(ROLL);
    }
    
    public void setMaxDeltaRoll(float degrees) {
    	if (degrees < 0) degrees = 0;
    	entityData.set(ROLL, degrees);
    }
    
    public void increaseThrottle() {
    	float current = this.getCurrentThrottle();
    	if (current < 1f) {
    		current += this.getThrottleIncreaseRate();
    		this.setCurrentThrottle(current);
    	}
    }
    
    public void decreaseThrottle() {
    	float current = this.getCurrentThrottle();
    	if (current > 0) {
    		current -= this.getThrottleDecreaseRate();
    		this.setCurrentThrottle(current);
    	}
    }
    
    public Quaternion getQ() {
        return entityData.get(Q).copy();
    }

    public void setQ(Quaternion q) {
        entityData.set(Q, q.copy());
    }

    /*public Quaternion getClientQ() {
        return clientQ.copy();
    }

    public void setClientQ(Quaternion q) {
        clientQ = q.copy();
    }*/

    public Quaternion getPrevQ() {
        return prevQ.copy();
    }

    public void setPrevQ(Quaternion q) {
        prevQ = q.copy();
    }
    
    /**
     * smaller number is better
     * @return value to be multiplied to the range of a radar
     */
    public float getStealth() {
    	return entityData.get(STEALTH);
    }
    
    public void setStealth(float stealth) {
    	if (stealth < 0) stealth = 0;
    	entityData.set(STEALTH, stealth);
    }
    
    public ResourceLocation getTexture() {
    	return TEXTURE;
    }
    
    @Override
	public boolean shouldRenderAtSqrDistance(double dist) {
		return dist < 25000;
	}
    
    public void setMaxHealth(float h) {
    	if (h < 0) h = 0;
    	entityData.set(MAX_HEALTH, h);
    }
    
    public float getMaxHealth() {
    	return entityData.get(MAX_HEALTH);
    }
    
    public void addHealth(float h) {
    	this.setHealth(getHealth()+h);
    }
    
    public void setHealth(float h) {
    	float max = getMaxHealth();
    	if (h > max) h = max;
    	else if (h < 0) h = 0;
    	entityData.set(HEALTH, h);
    }
    
    public float getHealth() {
    	return entityData.get(HEALTH);
    }
    
    /**
     * divide this by distance squared
     * @return heat value
     */
    public float getHeat() {
    	return getIdleHeat() + this.getCurrentThrottle() * getEngineHeat();
    }
    
    public int getFlares() {
    	return entityData.get(FLARES);
    }
    
    public void setFlares(int flares) {
    	if (flares < 0) flares = 0;
    	entityData.set(FLARES, flares);
    }
    
    public float getIdleHeat() {
    	return entityData.get(IDLEHEAT);
    }
    
    public void setIdleHeat(float heat) {
    	entityData.set(IDLEHEAT, heat);
    }
    
    public float getEngineHeat() {
    	return entityData.get(ENGINEHEAT);
    }
    
    public void setEngineHeat(float heat) {
    	entityData.set(ENGINEHEAT, heat);
    }
    
    public List<Player> getRidingPlayers() {
    	List<Player> players = new ArrayList<Player>();
    	for (Entity e : getPassengers()) {
    		if (e instanceof EntitySeat seat) {
    			Player p = seat.getPlayer();
    			if (p != null) players.add(p); 
    		}
    	}
    	return players;
    }
    
    public void sounds() {
    	//System.out.println("SOUNDS client side "+level.isClientSide+" tracked ticks "+getMissileTrackedTicks());
    	if (!this.level.isClientSide) {
    		if (this.getMissileTrackedTicks() > 0) this.addMissileTrackedTicks(-1);
    		if (this.getLockedOntoTicks() > 0) this.addLockedOntoTicks(-1);
    	} else {
    		if (this.getMissileTrackedTicks() > 0 && this.tickCount % 4 == 0) for (Player p : getRidingPlayers()) {
    			level.playSound(p, new BlockPos(p.position()), 
	    			ModSounds.MISSILE_WARNING.get(), SoundSource.PLAYERS, 1f, 1f);
    		} else if (this.getLockedOntoTicks() > 0 && this.tickCount % 8 == 0) for (Player p : getRidingPlayers()) {
    			level.playSound(p, new BlockPos(p.position()), 
    	    		ModSounds.GETTING_LOCKED.get(), SoundSource.PLAYERS, 1f, 1f);
        	}
    	}
    }
    
    public void trackedByMissile() {
    	if (this.level.isClientSide) return;
    	this.setMissileTrackedTicks(10);
    	//System.out.println("AIRCRAFT BEING TRACKED");
    }
    
    public void lockedOnto() {
    	if (this.level.isClientSide) return;
    	this.setLockedOntoTicks(10);
    }
    
    public int getMissileTrackedTicks() {
    	return entityData.get(MISSILE_TRACKED_TICKS);
    }
    
    public void setMissileTrackedTicks(int ticks) {
    	entityData.set(MISSILE_TRACKED_TICKS, ticks);
    }
    
    public void addMissileTrackedTicks(int ticks) {
    	this.setMissileTrackedTicks(ticks+this.getMissileTrackedTicks());
    }
    
    public int getLockedOntoTicks() {
    	return entityData.get(LOCKED_ONTO_TICKS);
    }
    
    public void setLockedOntoTicks(int ticks) {
    	entityData.set(LOCKED_ONTO_TICKS, ticks);
    }
    
    public void addLockedOntoTicks(int ticks) {
    	this.setLockedOntoTicks(ticks+this.getLockedOntoTicks());
    }
    
    @Override
    protected AABB makeBoundingBox() {
    	double pX = getX(), pY = getY(), pZ = getZ();
    	EntityDimensions d = getDimensions(getPose());
    	float f = d.width / 2.0F;
        float f1 = d.height / 2.0F;
        return new AABB(pX-(double)f, pY-(double)f1, pZ-(double)f, pX+(double)f, pY+(double)f1, pZ+(double)f);
    }
}
