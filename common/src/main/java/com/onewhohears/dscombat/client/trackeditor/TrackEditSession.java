package com.onewhohears.dscombat.client.trackeditor;

import com.onewhohears.dscombat.data.vehicle.CrawlerTrackPath;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple session for editing track points via commands
 */
public class TrackEditSession {
    
    private static TrackEditSession instance;
    
    private EntityVehicle vehicle;
    private List<TrackData> tracks = new ArrayList<>();
    private boolean modified = false;
    
    private TrackEditSession() {}
    
    public static TrackEditSession getInstance() {
        if (instance == null) {
            instance = new TrackEditSession();
        }
        return instance;
    }
    
    public boolean startEditing(EntityVehicle vehicle) {
        if (vehicle == null || !vehicle.getStats().isTank()) {
            return false;
        }
        
        this.vehicle = vehicle;
        this.tracks.clear();
        this.modified = false;
        
        // Load existing tracks
        var paths = vehicle.getStats().getCrawlerTrackPaths();
        for (int i = 0; i < paths.size(); i++) {
            CrawlerTrackPath path = paths.get(i);
            String name = (i == 0) ? "left_track" : "right_track";
            TrackData track = new TrackData(name, path.getZOffset());
            
            double[] cx = path.getControlPointsX();
            double[] cy = path.getControlPointsY();
            for (int j = 0; j < cx.length; j++) {
                track.points.add(new Point(cx[j], cy[j]));
            }
            
            tracks.add(track);
        }
        
        return true;
    }
    
    public boolean isEditing() {
        return vehicle != null;
    }
    
    public EntityVehicle getVehicle() {
        return vehicle;
    }
    
    public int getTrackCount() {
        return tracks.size();
    }
    
    public TrackData getTrack(int index) {
        if (index < 0 || index >= tracks.size()) return null;
        return tracks.get(index);
    }
    
    public void addPoint(int trackIndex, double x, double y) {
        TrackData track = getTrack(trackIndex);
        if (track == null) return;
        track.points.add(new Point(x, y));
        modified = true;
    }
    
    public void insertPoint(int trackIndex, int pointIndex, double x, double y) {
        TrackData track = getTrack(trackIndex);
        if (track == null) return;
        if (pointIndex < 0 || pointIndex > track.points.size()) return;
        track.points.add(pointIndex, new Point(x, y));
        modified = true;
    }
    
    public void movePoint(int trackIndex, int pointIndex, double dx, double dy) {
        TrackData track = getTrack(trackIndex);
        if (track == null) return;
        if (pointIndex < 0 || pointIndex >= track.points.size()) return;
        Point p = track.points.get(pointIndex);
        p.x += dx;
        p.y += dy;
        modified = true;
    }
    
    public void deletePoint(int trackIndex, int pointIndex) {
        TrackData track = getTrack(trackIndex);
        if (track == null) return;
        if (pointIndex < 0 || pointIndex >= track.points.size()) return;
        if (track.points.size() <= 3) return; // Minimum 3 points
        track.points.remove(pointIndex);
        modified = true;
    }
    
    public void scaleTrack(int trackIndex, double scaleX, double scaleY) {
        TrackData track = getTrack(trackIndex);
        if (track == null) return;
        for (Point p : track.points) {
            p.x *= scaleX;
            p.y *= scaleY;
        }
        modified = true;
    }
    
    public void shiftTrack(int trackIndex, double dx, double dy) {
        TrackData track = getTrack(trackIndex);
        if (track == null) return;
        for (Point p : track.points) {
            p.x += dx;
            p.y += dy;
        }
        modified = true;
    }
    
    public void mirrorTrack(int fromIndex, int toIndex) {
        TrackData from = getTrack(fromIndex);
        TrackData to = getTrack(toIndex);
        if (from == null || to == null) return;
        
        to.points.clear();
        for (Point p : from.points) {
            to.points.add(new Point(-p.x, p.y));
        }
        modified = true;
    }
    
    public String exportToJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"crawler_tracks\": [\n");
        
        for (int i = 0; i < tracks.size(); i++) {
            TrackData track = tracks.get(i);
            sb.append("  {\n");
            sb.append("    \"name\": \"").append(track.name).append("\",\n");
            sb.append("    \"reverse\": false,\n");
            sb.append("    \"segment_length\": 0.30,\n");
            sb.append(String.format(java.util.Locale.US, "    \"z_offset\": %.3f,\n", track.zOffset));
            sb.append("    \"control_points\": [\n");
            
            for (int j = 0; j < track.points.size(); j++) {
                Point p = track.points.get(j);
                sb.append(String.format(java.util.Locale.US, "      \"%.3f/%.3f\"", p.x, p.y));
                if (j < track.points.size() - 1) sb.append(",");
                sb.append("\n");
            }
            
            sb.append("    ]\n");
            sb.append("  }");
            if (i < tracks.size() - 1) sb.append(",");
            sb.append("\n");
        }
        
        sb.append("]");
        return sb.toString();
    }
    
    public boolean saveToFile() {
        if (!modified) return false;
        
        String json = exportToJson();
        String filename = vehicle.getStats().getId() + "_tracks.json";
        
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(filename);
            java.nio.file.Files.writeString(path, json);
            Minecraft.getInstance().player.displayClientMessage(
                net.minecraft.network.chat.Component.literal("§aSaved to: " + path.toAbsolutePath()), false);
            modified = false;
            return true;
        } catch (Exception e) {
            Minecraft.getInstance().player.displayClientMessage(
                net.minecraft.network.chat.Component.literal("§cFailed to save: " + e.getMessage()), false);
            return false;
        }
    }
    
    public void stopEditing() {
        vehicle = null;
        tracks.clear();
        modified = false;
    }
    
    public boolean isModified() {
        return modified;
    }
    
    // Data classes
    public static class TrackData {
        public String name;
        public double zOffset;
        public List<Point> points = new ArrayList<>();
        
        public TrackData(String name, double zOffset) {
            this.name = name;
            this.zOffset = zOffset;
        }
    }
    
    public static class Point {
        public double x, y;
        
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
