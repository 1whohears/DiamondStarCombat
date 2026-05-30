package com.onewhohears.dscombat.data.radar;

import com.onewhohears.dscombat.util.UtilVehicleEntity;
import net.minecraft.world.entity.Entity;

public enum PingTerrainType {
    GROUND((byte) 0, 6),
    AIR((byte) 1, 8),
    WATER((byte) 2, 7);
    public final byte id;
    public final int index;

    PingTerrainType(byte id, int index) {
        this.id = id;
        this.index = index;
    }

    public int getIconIndex() {
        return index;
    }

    public boolean isGround() {
        return this == GROUND;
    }

    public boolean isAir() {
        return this == AIR;
    }

    public boolean isWater() {
        return this == WATER;
    }

    public static PingTerrainType getById(byte id) {
        for (int i = 0; i < values().length; ++i)
            if (values()[i].id == id)
                return values()[i];
        return GROUND;
    }

    public static PingTerrainType getByEntity(Entity e) {
        if (e.isInWater()) return WATER;
        if (UtilVehicleEntity.isOnGroundOrWater(e)) return GROUND;
        return AIR;
    }
}
