package com.onewhohears.dscombat.data.radar;

public enum RadarFilterMode {
    ALL,
    PLAYERS,
    BOTS,
    VEHICLES,
    MOBS,
    OFF;

    public RadarFilterMode cycle() {
        int i = this.ordinal();
        ++i;
        if (i >= RadarFilterMode.values().length) i = 0;
        return RadarFilterMode.values()[i];
    }

    public boolean canScan(RadarFilterMode mode) {
        if (this == ALL) return true;
        return this == mode;
    }

    public boolean isPlayersOnly() {
        return this == PLAYERS;
    }

    public boolean isPlayersOrBots() {
        return isPlayersOnly() || this == BOTS;
    }

    public boolean isMobsOnly() {
        return this == MOBS;
    }

    public boolean isVehiclesOnly() {
        return this == VEHICLES;
    }

    public boolean isOff() {
        return this == OFF;
    }

    public boolean isOn() {
        return !isOff();
    }

    public boolean isAll() {
        return this == ALL;
    }

    public String getTranslatable() {
        return "radarmode.dscombat." + name().toLowerCase();
    }

    public static RadarFilterMode byId(int id) {
        if (id < 0 || id >= values().length) return ALL;
        return values()[id];
    }
}
