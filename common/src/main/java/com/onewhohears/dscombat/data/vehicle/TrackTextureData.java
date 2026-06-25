package com.onewhohears.dscombat.data.vehicle;

import com.google.gson.JsonObject;
import net.minecraft.world.phys.Vec3;

/**
 * Данные о позициях гусениц для динамических текстур
 */
public class TrackTextureData {
    private final Vec3 leftTrackPos;
    private final Vec3 rightTrackPos;
    private final boolean enabled;
    
    public TrackTextureData(JsonObject json) {
        this.enabled = json.has("track_textures") && json.get("track_textures").getAsBoolean();
        
        if (json.has("left_track_pos")) {
            JsonObject left = json.getAsJsonObject("left_track_pos");
            this.leftTrackPos = new Vec3(
                left.has("x") ? left.get("x").getAsDouble() : -1.5,
                left.has("y") ? left.get("y").getAsDouble() : -1.0,
                left.has("z") ? left.get("z").getAsDouble() : 0.0
            );
        } else {
            this.leftTrackPos = new Vec3(-1.5, -1.0, 0.0);
        }
        
        if (json.has("right_track_pos")) {
            JsonObject right = json.getAsJsonObject("right_track_pos");
            this.rightTrackPos = new Vec3(
                right.has("x") ? right.get("x").getAsDouble() : 1.5,
                right.has("y") ? right.get("y").getAsDouble() : -1.0,
                right.has("z") ? right.get("z").getAsDouble() : 0.0
            );
        } else {
            this.rightTrackPos = new Vec3(1.5, -1.0, 0.0);
        }
    }
    
    public TrackTextureData(Vec3 leftPos, Vec3 rightPos, boolean enabled) {
        this.leftTrackPos = leftPos;
        this.rightTrackPos = rightPos;
        this.enabled = enabled;
    }
    
    public Vec3 getLeftTrackPos() {
        return leftTrackPos;
    }
    
    public Vec3 getRightTrackPos() {
        return rightTrackPos;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public static TrackTextureData getDefault() {
        return new TrackTextureData(new Vec3(-1.5, -1.0, 0.0), new Vec3(1.5, -1.0, 0.0), false);
    }
}
