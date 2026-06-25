package com.onewhohears.dscombat.data.radar;

public enum PingEntityType {
    PLAYER((byte) 0, 0),
    HOSTILE_MOB((byte) 1, 1),
    FRIENDLY_MOB((byte) 2, 2),
    VEHICLE((byte) 3, 3),
    VEHICLE_PLAYER((byte) 4, 3),
    VEHICLE_BOT((byte) 5, 3),
    MISSILE((byte) 6, 5);
    public final byte id;
    public final int index;

    PingEntityType(byte id, int index) {
        this.id = id;
        this.index = index;
    }

    public int getIconIndex() {
        return index;
    }

    public boolean isMob() {
        return this == HOSTILE_MOB || this == FRIENDLY_MOB;
    }

    public boolean isVehicle() {
        return this == VEHICLE || this == VEHICLE_PLAYER || this == VEHICLE_BOT;
    }

    public boolean isPlayer() {
        return this == PLAYER || this == VEHICLE_PLAYER;
    }

    public boolean isBot() {
        return this == PLAYER || this == VEHICLE_PLAYER || this == VEHICLE_BOT;
    }

    public boolean isMissile() {
        return this == MISSILE;
    }

    public static PingEntityType getById(byte id) {
        for (int i = 0; i < values().length; ++i)
            if (values()[i].id == id)
                return values()[i];
        return FRIENDLY_MOB;
    }
}
