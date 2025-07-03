package com.onewhohears.dscombat.entity.vehicle;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.vehicle.VehiclePresets;
import com.onewhohears.dscombat.data.vehicle.physics.PhysicsComponentInstance;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetReloadListener;
import com.onewhohears.onewholibs.entity.JsonPresetEntity;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.UtilParse;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.UtilGeometry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EntityWindTunnel extends JsonPresetEntity<VehicleStats> {

    public static final EntityDataAccessor<String> PRESET = SynchedEntityData.defineId(EntityWindTunnel.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Vec3> SPEED = SynchedEntityData.defineId(EntityWindTunnel.class, DataSerializers.VEC3);
    public static final EntityDataAccessor<Quaternion> Q = SynchedEntityData.defineId(EntityWindTunnel.class, DataSerializers.QUATERNION);
    public static final EntityDataAccessor<Float> THROTTLE = SynchedEntityData.defineId(EntityWindTunnel.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Boolean> AFTERBURNER = SynchedEntityData.defineId(EntityWindTunnel.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> HIDE_MODEL = SynchedEntityData.defineId(EntityWindTunnel.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Float> ALTITUDE = SynchedEntityData.defineId(EntityWindTunnel.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Vec3> INPUTS = SynchedEntityData.defineId(EntityWindTunnel.class, DataSerializers.VEC3);
    public static final EntityDataAccessor<CompoundTag> OVERRIDES = SynchedEntityData.defineId(EntityWindTunnel.class, EntityDataSerializers.COMPOUND_TAG);

    @Nullable private EntityVehicle vehicle;
    @Nullable private WindTunnelJob job;

    public Vec3 totalAcc = Vec3.ZERO, weightAcc = Vec3.ZERO, thrustAcc = Vec3.ZERO, dragAcc = Vec3.ZERO, liftAcc = Vec3.ZERO, rotAcc = Vec3.ZERO;
    public double windCompAcc, centripetalAcc, yawRate, turnRadius;
    public final List<Float> aoas = new ArrayList<>();

    public EntityWindTunnel(EntityType<?> type, Level level) {
        super(type, level, "alexis_plane_unarmed");
        noPhysics = true;
    }

    public void startFindLiftDragJob(float speed, float turn_rate, float aoa, float altitude) {
        chatToNearbyPlayers("Starting Find Lift/Drag Coefficients Job...", ChatFormatting.LIGHT_PURPLE);
        chatToNearbyPlayers("Speed = "+speed+" | Turn Rate = "+turn_rate+" | AOA = "+aoa+" | Altitude = "+altitude, ChatFormatting.AQUA);
        // initialize parameters
        setSpeed(new Vec3(0, 0, speed));
        setQ(UtilAngles.toQuaternion(aoa, 0, 90));
        setThrottle(1.0f);
        setAfterBurner(true);
        setAltitude(altitude);
        setInputs(new Vec3(0, 0, 0));
        // find optimal pitch
        job = new WindTunnelJob.FindOptimalPitchJob(aoa, 90) {
            @Override
            protected void onJobComplete(EntityWindTunnel tunnel) {
                super.onJobComplete(tunnel);
                job = new FindOptimalLiftC(aoa, 90, this.getPitch(), turn_rate) {
                    @Override
                    protected void onJobComplete(EntityWindTunnel tunnel) {
                        super.onJobComplete(tunnel);
                        job = new FindOptimalDragC(aoa, 90, getPitch(), getLiftC());
                    }
                };
            }
        };
    }

    public void setOverrideValue(String name, CompoundTag value) {
        CompoundTag overrides = entityData.get(OVERRIDES);
        overrides.put(name, value);
        entityData.set(OVERRIDES, overrides);
    }

    public void clearOverrideValue(String name) {
        CompoundTag overrides = entityData.get(OVERRIDES);
        overrides.remove(name);
        entityData.set(OVERRIDES, overrides);
    }

    public CompoundTag getOverrideValue(String name) {
        CompoundTag overrides = entityData.get(OVERRIDES);
        if (overrides.contains(name)) return overrides.getCompound(name);
        return null;
    }

    public void chatToNearbyPlayers(String msg) {
        chatToNearbyPlayers(UtilMCText.literal(msg));
    }

    public void chatToNearbyPlayers(String msg, ChatFormatting color) {
        MutableComponent comp = UtilMCText.literal(msg);
        comp.setStyle(Style.EMPTY.withColor(color));
        chatToNearbyPlayers(comp);
    }

    public void chatToNearbyPlayers(Component msg) {
        AABB bb = getBoundingBox().inflate(16);
        for(Player player : getLevel().players()) {
            if (bb.contains(player.getX(), player.getY(), player.getZ())) {
                player.displayClientMessage(msg, false);
            }
        }
    }

    @Override
    public void tick() {
        if (!getLevel().isClientSide() && job != null) job.tick(this);
        tickSimulate();
    }

    protected void tickSimulate() {
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
        vehicle.updateEulerAngles();
        vehicle.setLandingGear(false);
        vehicle.foldLandingGearNow();
        if (getLevel().isClientSide()) vehicle.clientTick();
        Vec3 i = getInputs();
        vehicle.inputs.pitch = (float) i.x;
        vehicle.inputs.yaw = (float) i.y;
        vehicle.inputs.roll = (float) i.z;
        // calc forces to be rendered in wind tunnel
        totalAcc = vehicle.getAccFromForce(vehicle.getForces());
        weightAcc = vehicle.getAccFromForce(vehicle.getWeightForce());
        thrustAcc = vehicle.getAccFromForce(vehicle.getThrustForce(q));
        dragAcc = vehicle.getAccFromForce(vehicle.getDragForce(q));
        liftAcc = Vec3.ZERO;
        aoas.clear();
        for (PhysicsComponentInstance<?> phy : vehicle.getPhysicsInstances()) {
            dragAcc = dragAcc.add(vehicle.getAccFromForce(phy.getDragForce()));
            liftAcc = liftAcc.add(vehicle.getAccFromForce(phy.getLiftForce()));
            aoas.add(phy.getAOA());
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
        if (vehicle == null || getStatsHolder().getHolderReloads() != getPresets().getReloads()
                || !vehicle.getStatsId().equals(getStatsId())) {
            vehicle = createVehicleToSimulate();
        }
        return vehicle;
    }

    private EntityVehicle createVehicleToSimulate() {
        verifyCurrentPresetId();
        VehicleStats stats = getStats();
        EntityType<? extends EntityVehicle> entityType = stats.getEntityType();
        EntityVehicle vehicle = entityType.create(getLevel());
        vehicle.setPreset(getStatsId());
        vehicle.updatePhysicsInstances();
        vehicle.partsManager.read(stats.getDataAsNBT(), stats.getDataAsNBT());
        if (getLevel().isClientSide()) {
            vehicle.partsManager.clientPartsSetup();
            vehicle.textureManager.setupTextureLocations();
            vehicle.textureManager.setupDynamicTexture();
        } else {
            vehicle.partsManager.setupParts();
        }
        for (PhysicsComponentInstance<?> phy : vehicle.getPhysicsInstances()) {
            phy.setWindTunnel(this);
        }
        return vehicle;
    }

    public void verifyCurrentPresetId() {
        if (!VehiclePresets.get().has(getStatsId()))
            setPreset("wooden_plane");
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(SPEED, Vec3.ZERO);
        entityData.define(Q, Quaternion.ONE);
        entityData.define(THROTTLE, 1f);
        entityData.define(AFTERBURNER, false);
        entityData.define(HIDE_MODEL, false);
        entityData.define(ALTITUDE, 0f);
        entityData.define(INPUTS, Vec3.ZERO);
        entityData.define(OVERRIDES, new CompoundTag());
        entityData.define(PRESET, "wooden_plane");
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        if (key.equals(PRESET)) {
            String sync = entityData.get(PRESET);
            if (!sync.equals(getStatsId())) setPreset(sync);
        }
    }

    @Override
    public void setPreset(@NotNull String preset) {
        super.setPreset(preset);
        entityData.set(PRESET, preset);
    }

    @Override
    public @NotNull JsonPresetReloadListener<VehicleStats> getPresets() {
        return VehiclePresets.get();
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        verifyCurrentPresetId();
        entityData.set(PRESET, getStatsId());
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
        setInputs(UtilParse.readVec3(tag, "inputs"));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
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
        UtilParse.writeVec3(tag, getInputs(), "inputs");
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

    public Vec3 getInputs() {
        return entityData.get(INPUTS);
    }

    public void setInputs(Vec3 inputs) {
        entityData.set(INPUTS, inputs);
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
