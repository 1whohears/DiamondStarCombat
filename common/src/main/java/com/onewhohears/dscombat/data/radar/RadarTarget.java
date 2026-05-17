package com.onewhohears.dscombat.data.radar;

import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RadarTarget {
    public final int entityId;
    public final Vec3 pos;
    public final boolean isFriendly;
    public final PingTerrainType terrainType;
    public final PingEntityType entityType;
    private long updateTime;
    private boolean isShared;
    private Vec3 clientPos;

    public RadarTarget(Entity target, boolean isFriendly, PingEntityType entityType) {
        this.entityId = target.getId();
        this.pos = target.getBoundingBox().getCenter();
        this.isFriendly = isFriendly;
        this.isShared = false;
        this.terrainType = PingTerrainType.getByEntity(target);
        this.entityType = entityType;
        this.updateTime = UtilEntity.getLevel(target).getGameTime();
    }

    public RadarTarget(int id, Vec3 pos, boolean isFriendly, boolean isShared,
                       PingTerrainType terrainType, PingEntityType entityType, long gameTime) {
        this.entityId = id;
        this.pos = pos;
        this.isFriendly = isFriendly;
        this.isShared = isShared;
        this.terrainType = terrainType;
        this.entityType = entityType;
        this.updateTime = gameTime;
    }

    public RadarTarget(FriendlyByteBuf buffer) {
        entityId = buffer.readInt();
        pos = DataSerializers.VEC3.read(buffer);
        isFriendly = buffer.readBoolean();
        isShared = buffer.readBoolean();
        terrainType = PingTerrainType.getById(buffer.readByte());
        entityType = PingEntityType.getById(buffer.readByte());
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(entityId);
        DataSerializers.VEC3.write(buffer, pos);
        buffer.writeBoolean(isFriendly);
        buffer.writeBoolean(isShared);
        buffer.writeByte(terrainType.id);
        buffer.writeByte(entityType.id);
    }

    public boolean isShared() {
        return this.isShared;
    }

    public RadarTarget getCopy(boolean isShared) {
        return new RadarTarget(entityId, pos, isFriendly, isShared, terrainType, entityType, updateTime);
    }

    public Vec3 getPosForClient() {
        if (clientPos != null) return clientPos;
        return pos;
    }

    public void setClientPos(Level level) {
        Entity e = level.getEntity(entityId);
        if (e == null) {
            clientPos = null;
            return;
        }
        clientPos = e.getBoundingBox().getCenter();
    }

    @Override
    public String toString() {
        return "PING[" + (int) pos.x + "," + (int) pos.y + "," + (int) pos.z + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RadarTarget ping && ping.entityId == this.entityId) return true;
        return false;
    }

    public boolean dontDisplayByMode(RadarFilterMode mode) {
        if (mode.isOff()) return true;
        if (mode.isAll()) return false;
        if (entityType.isMissile()) return false;
        if (mode.isMobsOnly()) return !entityType.isMob();
        if (mode.isPlayersOnly()) return !entityType.isPlayer();
        if (mode.isPlayersOrBots()) return !entityType.isBot();
        if (mode.isVehiclesOnly()) return !entityType.isVehicle();
        return false;
    }

    public long getUpdateTime() {
        return updateTime;
    }
}
