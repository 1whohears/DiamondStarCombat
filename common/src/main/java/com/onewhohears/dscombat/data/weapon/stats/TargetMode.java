package com.onewhohears.dscombat.data.weapon.stats;

public enum TargetMode {
    LOOK, MARKER, COORDS, RADAR, OPTICAL, NONE;
    public boolean isPosition() {
        return this == LOOK || this == MARKER || this == COORDS;
    }
    public String getTranslatable() {
        return "targetmode.dscombat." + name().toLowerCase();
    }
}
