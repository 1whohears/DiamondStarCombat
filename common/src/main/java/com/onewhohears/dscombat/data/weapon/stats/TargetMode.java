package com.onewhohears.dscombat.data.weapon.stats;

public enum TargetMode {
    LOOK, MARKER, COORDS, RADAR, OPTICAL;

    public String getTranslatable() {
        return "targetmode.dscombat." + name().toLowerCase();
    }
}
