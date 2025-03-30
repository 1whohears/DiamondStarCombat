package com.onewhohears.dscombat.entity.vehicle;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.vehicle.VehiclePresets;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.onewholibs.util.UtilParse;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class EntityWindTunnel extends Entity {

    public static final EntityDataAccessor<String> PRESET = SynchedEntityData.defineId(EntityWindTunnel.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Vec3> SPEED = SynchedEntityData.defineId(EntityWindTunnel.class, DataSerializers.VEC3);
    public static final EntityDataAccessor<Quaternion> Q = SynchedEntityData.defineId(EntityWindTunnel.class, DataSerializers.QUATERNION);

    private EntityVehicle vehicle;

    public EntityWindTunnel(EntityType<?> type, Level level) {
        super(type, level);
        noPhysics = true;
    }

    @Override
    public void tick() {
        EntityVehicle vehicle = getSimulatedVehicle();
        vehicle.setTestMode(true);
        vehicle.setPos(position().add(0, 4, 0));
        vehicle.setQBySide(getQ());
        vehicle.setDeltaMovement(getSpeed());
        vehicle.tickPhysics();
        // calc forces to be rendered in wind tunnel
    }

    public EntityVehicle getSimulatedVehicle() {
        if (vehicle == null) vehicle = createVehicleToSimulate();
        return vehicle;
    }

    private EntityVehicle createVehicleToSimulate() {
        verifyCurrentPresetId();
        VehicleStats stats = VehiclePresets.get().get(getPresetId());
        EntityType<? extends EntityVehicle> entityType = stats.getEntityType();
        EntityVehicle vehicle = entityType.create(getLevel());
        if (getLevel().isClientSide()) vehicle.updateClientStatsHolder();
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
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        setPresetId(tag.getString("preset"));
        verifyCurrentPresetId();
        setSpeed(UtilParse.readVec3(tag, "speed"));
        double zRot = tag.getDouble("zRot");
        setQ(UtilAngles.toQuaternion(getYRot(), getXRot(), zRot));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        tag.putString("preset", getPresetId());
        UtilParse.writeVec3(tag, getSpeed(), "speed");
        double zRot = UtilAngles.toDegrees(getQ()).roll;
        tag.putDouble("zRot", zRot);
    }

    public String getPresetId() {
        return entityData.get(PRESET);
    }

    public void setPresetId(String id) {
        entityData.set(PRESET, id);
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

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
