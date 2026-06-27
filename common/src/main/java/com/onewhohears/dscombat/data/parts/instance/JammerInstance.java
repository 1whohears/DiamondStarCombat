package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.JammerStats;

/**
 * Instance of the ECM Jammer part.
 * The jammer is always active while installed and not damaged.
 */
public class JammerInstance<T extends JammerStats> extends PartInstance<T> {

    public JammerInstance(T stats) {
        super(stats);
    }

    public float getJamRadius() {
        return getStats().getJamRadius();
    }

    public float getJamStrength() {
        return getStats().getJamStrength();
    }
}
