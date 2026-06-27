package com.onewhohears.dscombat.data.vehicle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Data class for crawler track configuration loaded from vehicle JSON
 */
public class CrawlerTrackData {
    public final String name;
    public final boolean reverse;
    public final float segmentLength;
    public final float zOffset;
    public final List<CrawlerTrackPoint> points;

    public CrawlerTrackData(String name, boolean reverse, float segmentLength, float zOffset, List<CrawlerTrackPoint> points) {
        this.name = name;
        this.reverse = reverse;
        this.segmentLength = segmentLength;
        this.zOffset = zOffset;
        this.points = points;
    }

    /**
     * Parse crawler track data from JSON
     */
    public static CrawlerTrackData fromJson(JsonObject json) {
        String name = json.get("name").getAsString();
        boolean reverse = json.has("reverse") && json.get("reverse").getAsBoolean();
        float segmentLength = json.has("segment_length") ? json.get("segment_length").getAsFloat() : 0.3f;
        float zOffset = json.has("z_offset") ? json.get("z_offset").getAsFloat() : 0.0f;
        
        List<CrawlerTrackPoint> points = new ArrayList<>();
        if (json.has("control_points")) {
            JsonArray controlPoints = json.getAsJsonArray("control_points");
            for (JsonElement element : controlPoints) {
                String pointStr = element.getAsString();
                String[] parts = pointStr.split("/");
                if (parts.length == 2) {
                    double y = Double.parseDouble(parts[0]);
                    double z = Double.parseDouble(parts[1]);
                    points.add(new CrawlerTrackPoint(y, z, 0.0));
                }
            }
        }
        
        return new CrawlerTrackData(name, reverse, segmentLength, zOffset, points);
    }

    /**
     * Parse list of crawler tracks from JSON array
     */
    public static List<CrawlerTrackData> listFromJson(JsonArray array) {
        List<CrawlerTrackData> tracks = new ArrayList<>();
        for (JsonElement element : array) {
            if (element.isJsonObject()) {
                tracks.add(fromJson(element.getAsJsonObject()));
            }
        }
        return tracks;
    }

    /**
     * Single control point for crawler track path
     */
    public static class CrawlerTrackPoint {
        public final double x; // Y in model space (height)
        public final double y; // Z in model space (forward/back)
        public final double rotation; // Rotation angle in degrees

        public CrawlerTrackPoint(double x, double y, double rotation) {
            this.x = x;
            this.y = y;
            this.rotation = rotation;
        }
    }
}
