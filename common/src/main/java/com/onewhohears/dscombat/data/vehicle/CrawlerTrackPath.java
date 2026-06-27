package com.onewhohears.dscombat.data.vehicle;

/**
 * Crawler track path configuration
 * Stores control points and parameters for track rendering
 * Replaces onewholibs.client.model.obj.crawlertrack.CrawlerTrackPath
 */
public class CrawlerTrackPath {
    private final double[] controlPointsX;
    private final double[] controlPointsY;
    private final float segmentLength;
    private final float zOffset;
    private final boolean reverse;

    public CrawlerTrackPath(double[] cx, double[] cy, float segmentLength, float zOffset, boolean reverse) {
        this.controlPointsX = cx;
        this.controlPointsY = cy;
        this.segmentLength = segmentLength;
        this.zOffset = zOffset;
        this.reverse = reverse;
    }

    public double[] getControlPointsX() {
        return controlPointsX;
    }

    public double[] getControlPointsY() {
        return controlPointsY;
    }

    public float getSegmentLength() {
        return segmentLength;
    }

    public float getZOffset() {
        return zOffset;
    }

    public boolean isReverse() {
        return reverse;
    }
}
