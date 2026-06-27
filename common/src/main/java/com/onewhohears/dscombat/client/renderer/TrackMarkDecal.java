package com.onewhohears.dscombat.client.renderer;

import net.minecraft.world.phys.Vec3;

/**
 * One point in a track strip.
 * The strip is rendered as a continuous quad-strip between consecutive points.
 */
public class TrackMarkDecal {

    /** One segment point: world position + vehicle yaw at that moment */
    public static class Point {
        public final Vec3 pos;
        public final float yawDeg;
        public Point(Vec3 pos, float yawDeg) { this.pos = pos; this.yawDeg = yawDeg; }
    }

    private final java.util.List<Point> points = new java.util.ArrayList<>();
    private int ticksAlive = 0;
    private static final int MAX_LIFETIME = 400; // 20 seconds

    public void addPoint(Vec3 pos, float yawDeg) {
        points.add(new Point(pos, yawDeg));
    }

    public void tick() { ticksAlive++; }

    public boolean shouldRemove() { return ticksAlive >= MAX_LIFETIME; }

    public java.util.List<Point> getPoints() { return points; }

    /** Alpha: fade in first 5 ticks, full until last 3 seconds, then fade out */
    public float getAlpha() {
        if (ticksAlive < 5) return ticksAlive / 5f;
        if (ticksAlive > MAX_LIFETIME - 60) return (MAX_LIFETIME - ticksAlive) / 60f;
        return 1f;
    }
}
