package com.onewhohears.dscombat.entity.vehicle;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.vehicle.VehiclePresets;
import com.onewhohears.dscombat.data.vehicle.physics.PhysicsComponentInstance;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.UtilParse;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.UtilGeometry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityWindTunnel extends Entity {

    public static final EntityDataAccessor<String> PRESET = SynchedEntityData.defineId(EntityWindTunnel.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Vec3> SPEED = SynchedEntityData.defineId(EntityWindTunnel.class, DataSerializers.VEC3);
    public static final EntityDataAccessor<Quaternion> Q = SynchedEntityData.defineId(EntityWindTunnel.class, DataSerializers.QUATERNION);
    public static final EntityDataAccessor<Float> THROTTLE = SynchedEntityData.defineId(EntityWindTunnel.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Boolean> AFTERBURNER = SynchedEntityData.defineId(EntityWindTunnel.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> HIDE_MODEL = SynchedEntityData.defineId(EntityWindTunnel.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Float> ALTITUDE = SynchedEntityData.defineId(EntityWindTunnel.class, EntityDataSerializers.FLOAT);

    @Nullable private EntityVehicle vehicle;

    public Vec3 totalAcc = Vec3.ZERO, weightAcc = Vec3.ZERO, thrustAcc = Vec3.ZERO, dragAcc = Vec3.ZERO, liftAcc = Vec3.ZERO, rotAcc = Vec3.ZERO;
    public double windCompAcc, centripetalAcc, yawRate, turnRadius;

    public EntityWindTunnel(EntityType<?> type, Level level) {
        super(type, level);
        noPhysics = true;
    }

    @Override
    public void tick() {
        if (getLevel().isClientSide()) clientTick();
    }

    protected void clientTick() {
        Vec3 speed = getSpeed();
        Quaternion q = getQ();
        EntityVehicle vehicle = getSimulatedVehicle();
        vehicle.setTestMode(true);
        vehicle.setPos(position().multiply(1, 0, 1)
                .add(0, getAltitude()+UtilEntity.getSeaLevel(getLevel()), 0));
        vehicle.setQBySide(q);
        vehicle.setDeltaMovement(speed);
        vehicle.setCurrentThrottle(getThrottle());
        vehicle.setUseAfterBurnerOverride(getAfterBurner());
        vehicle.tickPhysics();
        vehicle.setLandingGear(false);
        vehicle.clientTick();
        // calc forces to be rendered in wind tunnel
        totalAcc = vehicle.getAccFromForce(vehicle.getForces());
        weightAcc = vehicle.getAccFromForce(vehicle.getWeightForce());
        thrustAcc = vehicle.getAccFromForce(vehicle.getThrustForce(q));
        dragAcc = vehicle.getAccFromForce(vehicle.getDragForce(q));
        liftAcc = Vec3.ZERO;
        for (PhysicsComponentInstance<?> phy : vehicle.getPhysicsInstances()) {
            dragAcc = dragAcc.add(vehicle.getAccFromForce(phy.getDragForce()));
            liftAcc = liftAcc.add(vehicle.getAccFromForce(phy.getLiftForce()));
        }
        windCompAcc = UtilGeometry.vecCompMagDirByAxis(totalAcc, speed);
        Vec3 cenAxis = UtilAngles.getRollAxis(0, (vehicle.getYRot()+90)*Mth.DEG_TO_RAD);
        centripetalAcc = UtilGeometry.vecCompMagDirByNormAxis(liftAcc, cenAxis);
        yawRate = centripetalAcc / vehicle.xzSpeed * Mth.RAD_TO_DEG;
        turnRadius = vehicle.xzSpeed / (yawRate * Mth.DEG_TO_RAD);
        Vec3 m = vehicle.getMoment();
        Vec3 I = vehicle.getTotalRotInertia();
        rotAcc = new Vec3(m.x/I.x, m.y/I.y, m.z/I.z).scale(vehicle.getAccTimeScale());
    }

    @NotNull
    public EntityVehicle getSimulatedVehicle() {
        if (vehicle == null || !vehicle.getStatsId().equals(getPresetId())) {
            vehicle = createVehicleToSimulate();
        }
        return vehicle;
    }

    private EntityVehicle createVehicleToSimulate() {
        verifyCurrentPresetId();
        VehicleStats stats = VehiclePresets.get().get(getPresetId());
        EntityType<? extends EntityVehicle> entityType = stats.getEntityType();
        EntityVehicle vehicle = entityType.create(getLevel());
        vehicle.setPreset(getPresetId());
        vehicle.updatePhysicsInstances();
        vehicle.partsManager.read(stats.getDataAsNBT(), stats.getDataAsNBT());
        if (getLevel().isClientSide()) {
            vehicle.partsManager.clientPartsSetup();
            vehicle.textureManager.setupTextureLocations();
            vehicle.textureManager.setupDynamicTexture();
        } else {
            vehicle.partsManager.setupParts();
        }
        return vehicle;
    }

    public void verifyCurrentPresetId() {
        if (!VehiclePresets.get().has(getPresetId()))
            setPresetId("wooden_plane");
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(PRESET, "wooden_plane");
        entityData.define(SPEED, Vec3.ZERO);
        entityData.define(Q, Quaternion.ONE);
        entityData.define(THROTTLE, 1f);
        entityData.define(AFTERBURNER, false);
        entityData.define(HIDE_MODEL, false);
        entityData.define(ALTITUDE, 0f);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        setPresetId(tag.getString("preset"));
        verifyCurrentPresetId();
        setSpeed(UtilParse.readVec3(tag, "speed"));
        float qi = tag.getFloat("qi");
        float qj = tag.getFloat("qj");
        float qk = tag.getFloat("qk");
        float qr = tag.getFloat("qr");
        setQ(new Quaternion(qi, qj, qk, qr));
        setThrottle(tag.getFloat("throttle"));
        setAfterBurner(tag.getBoolean("afterburner"));
        setHideModel(tag.getBoolean("hide_model"));
        setAltitude(tag.getFloat("altitude"));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        tag.putString("preset", getPresetId());
        UtilParse.writeVec3(tag, getSpeed(), "speed");
        Quaternion q = getQ();
        tag.putFloat("qi", q.i());
        tag.putFloat("qj", q.j());
        tag.putFloat("qk", q.k());
        tag.putFloat("qr", q.r());
        tag.putFloat("throttle", getThrottle());
        tag.putBoolean("afterburner", getAfterBurner());
        tag.putBoolean("hide_model", getHideModel());
        tag.putFloat("altitude", getAltitude());
    }

    public String getPresetId() {
        return entityData.get(PRESET);
    }

    public void setPresetId(String id) {
        entityData.set(PRESET, id);
        verifyCurrentPresetId();
    }

    public Vec3 getSpeed() {
        return entityData.get(SPEED);
    }

    public void setSpeed(Vec3 speed) {
        entityData.set(SPEED, speed);
    }

    public Quaternion getQ() {
        return entityData.get(Q);
    }

    public void setQ(Quaternion q) {
        entityData.set(Q, q);
    }

    public float getThrottle() {
        return entityData.get(THROTTLE);
    }

    public void setThrottle(float throttle) {
        entityData.set(THROTTLE, throttle);
    }

    public boolean getAfterBurner() {
        return entityData.get(AFTERBURNER);
    }

    public void setAfterBurner(boolean enable) {
        entityData.set(AFTERBURNER, enable);
    }

    public boolean getHideModel() {
        return entityData.get(HIDE_MODEL);
    }

    public void setHideModel(boolean enable) {
        entityData.set(HIDE_MODEL, enable);
    }

    public float getAltitude() {
        return entityData.get(ALTITUDE);
    }

    public void setAltitude(float alt) {
        entityData.set(ALTITUDE, alt);
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
