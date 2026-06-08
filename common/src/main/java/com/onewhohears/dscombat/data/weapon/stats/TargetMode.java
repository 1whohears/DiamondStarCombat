package com.onewhohears.dscombat.data.weapon.stats;

public enum TargetMode {
    LOOK, MARKER, COORDS, RADAR, OPTICAL, NONE;
    public TargetMode cycle() {
        int index = ordinal();
        if (index == values().length-1) return LOOK;
        return values()[index+1];
    }
    public boolean isPosition() {
        return this == LOOK || this == MARKER || this == COORDS;
    }
    public String getTranslatable() {
        return "targetmode.dscombat." + name().toLowerCase();
    }
}
