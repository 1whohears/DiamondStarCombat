package com.onewhohears.dscombat.entity.aircraft;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.client.event.ClientForgeEvents;
import com.onewhohears.dscombat.common.container.AircraftMenuContainer;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ServerBoundRequestPlaneDataPacket;
import com.onewhohears.dscombat.data.AircraftPresets;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.PartsManager;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponSystem;
import com.onewhohears.dscombat.entity.parts.EntityAbstractPart;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.weapon.EntityFlare;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.item.ItemGasCan;
import com.onewhohears.dscombat.util.UtilClientSafeSoundInstance;
import com.onewhohears.dscombat.util.UtilEntity;
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
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.RegistryObject;

public abstract class EntityAbstractAircraft extends Entity {
	
	public static final EntityDataAccessor<Float> MAX_HEALTH = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> MAX_SPEED = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> THROTTLE = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> THROTTLEUP = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> THROTTLEDOWN = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Quaternion> Q = SynchedEntityData.defineId(EntityAbstractAircraft.class, DataSerializers.QUATERNION);
	public static final EntityDataAccessor<Boolean> FREE_LOOK = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Integer> FLARES = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.INT); // TODO allow player to add flares
	public static final EntityDataAccessor<Float> STEALTH = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> ROLL = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> PITCH = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> YAW = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> IDLEHEAT = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> WEIGHT = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> WING_AREA = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Integer> MISSILE_TRACKED_TICKS = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> LOCKED_ONTO_TICKS = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> LANDING_GEAR = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.BOOLEAN);
	
	public static final double collideSpeedThreshHold = 1d;
	public static final double collideDamageRate = 200d;
	
	public PartsManager partsManager = new PartsManager();
	public WeaponSystem weaponSystem = new WeaponSystem();
	public RadarSystem radarSystem = new RadarSystem();
	public Quaternion prevQ = Quaternion.ONE.copy();
	//public Quaternion clientQ = Quaternion.ONE.copy();
	
	public boolean inputMouseMode, inputFlare, inputShoot, inputSelect, inputOpenMenu;
	public float inputThrottle, inputPitch, inputRoll, inputYaw;
	public float prevXRot, prevYRot, zRot, prevZRot;
	public Vec3 prevMotion = Vec3.ZERO;
	
	protected final ResourceLocation TEXTURE;
	protected final RegistryObject<SoundEvent> engineSound;
	
	private int lerpSteps/*, lerpStepsQ*/, newRiderCooldown;
	private double lerpX, lerpY, lerpZ, lerpXRot, lerpYRot/*, lerpZRot*/;
	
	public EntityAbstractAircraft(EntityType<? extends EntityAbstractAircraft> entity, Level level, ResourceLocation texture, RegistryObject<SoundEvent> engineSound) {
		super(entity, level);
		this.TEXTURE = texture;
		this.engineSound = engineSound;
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
		entityData.define(WEIGHT, 0.05f);
		entityData.define(WING_AREA, 1f);
		entityData.define(MISSILE_TRACKED_TICKS, 0);
		entityData.define(LOCKED_ONTO_TICKS, 0);
		entityData.define(LANDING_GEAR, false);
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
		//System.out.println("aircraft read data client side = "+level.isClientSide+" init type = "+initType);
		if (!initType.isEmpty()) {
			CompoundTag tag = AircraftPresets.getPreset(initType);
			if (tag != null) compound.merge(tag);
		}
		partsManager = new PartsManager(compound);
		weaponSystem = new WeaponSystem(compound);
		radarSystem = new RadarSystem(compound);
		this.setMaxSpeed(compound.getFloat("max_speed"));
		this.setMaxHealth(compound.getFloat("max_health"));
		this.setHealth(compound.getFloat("health"));
		this.setFlares(compound.getInt("flares"));
		this.setStealth(compound.getFloat("stealth"));
		this.setMaxDeltaRoll(compound.getFloat("maxroll"));
		this.setMaxDeltaPitch(compound.getFloat("maxpitch"));
		this.setMaxDeltaYaw(compound.getFloat("maxyaw"));
		this.setThrottleIncreaseRate(compound.getFloat("throttleup"));
		this.setThrottleDecreaseRate(compound.getFloat("throttledown"));
		this.setIdleHeat(compound.getFloat("idleheat"));
		this.setAircraftWeight(compound.getFloat("weight"));
		this.setSurfaceArea(compound.getFloat("surfacearea"));
		this.setLandingGear(compound.getBoolean("landing_gear"));
		setXRot(compound.getFloat("xRot"));
		if (compound.contains("yRot")) setYRot(compound.getFloat("yRot"));
		zRot = compound.getFloat("zRot");
		Quaternion q = new Quaternion(getXRot(), getYRot(), zRot, true);
		setQ(q);
		setPrevQ(q);
		//setClientQ(q);
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
		compound.putInt("flares", this.getFlares());
		compound.putFloat("stealth", this.getStealth());
		compound.putFloat("maxroll", this.getMaxDeltaRoll());
		compound.putFloat("maxpitch", this.getMaxDeltaPitch());
		compound.putFloat("maxyaw", this.getMaxDeltaYaw());
		compound.putFloat("throttleup", this.getThrottleIncreaseRate());
		compound.putFloat("throttledown", this.getThrottleDecreaseRate());
		compound.putFloat("idleheat", this.getIdleHeat());
		compound.putFloat("weight", this.getAircraftWeight());
		compound.putFloat("surfacearea", this.getSurfaceArea());
		compound.putBoolean("landing_gear", this.isLandingGear());
		compound.putFloat("xRot", this.getXRot());
		compound.putFloat("yRot", this.getYRot());
		compound.putFloat("zRot", zRot);
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
		if (Double.isNaN(getDeltaMovement().length())) {
            setDeltaMovement(Vec3.ZERO);
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
        tickParts();
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
			if (getHealth() <= 0) {
				kill();
			}
		}
		if (level.isClientSide) clientTick();
		this.prevMotion = this.getDeltaMovement();
		sounds();
	}
	
	public void clientTick() {
		healthSmoke();
		if (this.tickCount == 1) {
			UtilClientSafeSoundInstance.aircraftEngineSound(
					Minecraft.getInstance(), this, getEngineSound());
		}
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
		if (inputThrottle > 0) this.increaseThrottle();
		else if (inputThrottle < 0) this.decreaseThrottle();
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
	
	public abstract Vec3 getThrustForce(Quaternion q);
	
	public double getThrust() {
		if (this.getFuel() <= 0) return 0;
		float throttle = getCurrentThrottle();
		return throttle * getMaxThrust();
	}
	
	public float getMaxThrust() {
		return partsManager.getTotalEngineThrust();
	}
	
	public Vec3 getDragForce(Quaternion q) {
		Vec3 direction = getDeltaMovement().normalize();
		Vec3 dragForce = direction.scale(-getDrag(q));
		//System.out.println("drag force = "+dragForce);
		return dragForce;
	}
	
	public double getDrag(Quaternion q) {
		// Drag = (drag coefficient) * (air pressure) * (speed)^2 * (wing surface area) / 2
		double dc = 0.030;
		double air = UtilEntity.getAirPressure(getY());
		double speedSqr = getDeltaMovement().lengthSqr();
		double wing = getSurfaceArea();
		return dc * air * speedSqr * wing / 2;
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
		double f;
		if (getCurrentThrottle() <= 0) f = 0.05;
		else f = 0.01;
		if (speed < f) return speed;
		return f;
	}
	
	public Vec3 getWeightForce() {
		return new Vec3(0, -getTotalWeight(), 0);
	}
	
	public float getTotalWeight() {
		float w = getAircraftWeight() + partsManager.getPartsWeight();
		//System.out.println("WEIGHT = "+w);
		return w;
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
			weaponSystem.tick();
			radarSystem.tickUpdateTargets();
			if (newRiderCooldown > 0) --newRiderCooldown;
			else {
				if (this.inputShoot) shoot(controller, isPlayer);
				if (this.inputFlare && tickCount % 5 == 0) flare(controller, isPlayer);
			}
			if (this.inputOpenMenu && isPlayer) {
				if (this.isOnGround()) {
					System.out.println("OPENING MENU "+partsManager);
					NetworkHooks.openScreen((ServerPlayer) controller, 
						new SimpleMenuProvider((windowId, playerInv, player) -> 
								new AircraftMenuContainer(windowId, playerInv), 
						Component.translatable("container.dscombat.plane_menu")));
				} else {
					((ServerPlayer)controller).displayClientMessage(
							UtilMCText.simpleText("Can't open plane menu while flying!"), true);
				}
			}
			if (isPlayer && ((ServerPlayer)controller).isCreative()) {
				
			} else {
				this.tickFuel();
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
	
	public void updateControls(float throttle, float pitch, float roll, float yaw,
			boolean mouseMode, boolean flare, boolean shoot, boolean select,
			boolean openMenu) {
		this.inputThrottle = throttle;
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
		this.inputThrottle = 0;
		this.inputPitch = 0;
		this.inputRoll = 0;
		this.inputYaw = 0;
		this.inputFlare = false;
		this.inputMouseMode = false;
		this.inputShoot = false;
		this.inputSelect = false;
		this.inputOpenMenu = false;
		this.decreaseThrottle();
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
		//System.out.println("setting up parts client side = "+level.isClientSide);
		// ORDER MATTERS
		weaponSystem.setup(this);
		partsManager.setupParts(this);
		radarSystem.setup(this);
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
			if (player.getInventory().getSelected().getItem() instanceof ItemGasCan) {
				ItemStack can = player.getInventory().getSelected();
				int md = can.getMaxDamage();
				int d = can.getDamageValue();
				float r = this.addFuel(md-d, true);
				can.setDamageValue(md-(int)r);
				return InteractionResult.SUCCESS;
			}
			boolean okay = rideAvailableSeat(player);
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
	
	private boolean ridePilotSeat(Entity e, List<EntitySeat> seats) {
		for (EntitySeat seat : seats) 
			if (seat.getSlotId().equals(PartSlot.PILOT_SLOT_NAME)) {
				if (e.startRiding(seat)) {
					this.newRiderCooldown = 10;
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
		//System.out.println(e+" switching seats on "+this);
		List<EntitySeat> seats = this.getSeats();
		//System.out.println("GOT SEATS "+seats);
		int seatIndex = -1;
		for (int i = 0; i < seats.size(); ++i) {
			Player p = seats.get(i).getPlayer();
			if (p == null) continue;
			if (p.equals(e)) {
				seatIndex = i;
				break;
			}
		}
		if (seatIndex == -1) {
			//System.out.println("not riding any of the seats");
			return false;
		}
		System.out.println("riding seat "+seatIndex);
		int i = 0, j = seatIndex+1;
		while (i < seats.size()-1) {
			if (j >= seats.size()) j = 0;
			//System.out.println("i = "+i+" j = "+j);
			//System.out.println("checking to ride "+seats.get(j));
			if (e.startRiding(seats.get(j))) {
				//System.out.println("riding");
				return true;
			}
			++i; ++j;
		}
		//System.out.println("could not ride any of them");
		return false;
	}
	
	@Override
    public void positionRider(Entity passenger) {
		//if (!this.hasPassenger(passenger)) return;
		//System.out.println("POSITION RIDER "+passenger);
		if (passenger instanceof EntityAbstractPart part) {
 			Vec3 seatPos = UtilAngles.rotateVector(part.getRelativePos(), getQ());
			passenger.setPos(position().add(seatPos));
		}
	}
	
	/*@Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
		return super.getDismountLocationForPassenger(livingEntity);
	}*/
	
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
		return !this.isRemoved();
	}
	
	@Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        //System.out.println("AIRCRAFT ADDED PASSENGER "+passenger);
	}
	
	@Override
    protected boolean canAddPassenger(Entity passenger) {
		//System.out.println("CAN ADD PASSENGER "+passenger);
		return passenger instanceof EntitySeat;
	}
	
	@Override
    protected boolean canRide(Entity entityIn) {
		//System.out.println("CAN RIDE "+entityIn);
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
		if (level.isClientSide) return true; // TODO should be a temporary fix
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
    
    public SoundEvent getEngineSound() {
    	return engineSound.get();
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
    	return partsManager.getTotalEngineHeat();
    }
    
    public List<Player> getRidingPlayers() {
    	List<Player> players = new ArrayList<Player>();
    	for (EntitySeat seat : getSeats()) {
    		Player p = seat.getPlayer();
			if (p != null) players.add(p); 
    	}
    	return players;
    }
    
    public List<EntitySeat> getSeats() {
    	List<EntitySeat> seats = new ArrayList<EntitySeat>();
    	for (Entity e : getPassengers())
    		if (e instanceof EntitySeat seat) 
    			seats.add(seat);
    	return seats;
    }
    
    public List<EntityAbstractPart> getPartEntities() {
    	List<EntityAbstractPart> parts = new ArrayList<EntityAbstractPart>();
    	for (Entity e : getPassengers())
    		if (e instanceof EntityAbstractPart part)
    			parts.add(part);
    	return parts;
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
    	// TODO change if landing gear is out 
    	double pX = getX(), pY = getY(), pZ = getZ();
    	EntityDimensions d = getDimensions(getPose());
    	float f = d.width / 2.0F;
        float f1 = d.height / 2.0F;
        return new AABB(pX-(double)f, pY-(double)f1, pZ-(double)f, pX+(double)f, pY+(double)f1, pZ+(double)f);
    }
    
    public float getMaxFuel() {
    	return partsManager.getMaxFuel();
    }
    
    public void tickFuel() {
    	if (!level.isClientSide) {
    		this.partsManager.tickFuel(true);
    	}
    }
    
    public float getFuel() {
    	return partsManager.getCurrentFuel();
    }
    
    public float addFuel(float fuel, boolean updateClient) {
    	return this.partsManager.addFuel(fuel, updateClient);
    }
    
    public boolean isLandingGear() {
    	return entityData.get(LANDING_GEAR);
    }
    
    public void setLandingGear(boolean gear) {
    	entityData.set(LANDING_GEAR, gear);
    }
    
    @Override
    public void kill() {
    	explode(null);
    	super.kill();
    }
    
}
