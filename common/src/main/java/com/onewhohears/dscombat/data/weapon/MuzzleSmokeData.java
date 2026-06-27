package com.onewhohears.dscombat.data.weapon;

import com.google.gson.JsonObject;
import net.minecraft.world.phys.Vec3;

public class MuzzleSmokeData {
    
    public final Vec3 pos;
    public final float particleCount;
    public final float spreadDistance;
    public final float flashScale; // Размер 3D вспышки
    public final int flashLifetime; // Время жизни вспышки в тиках
    
    public MuzzleSmokeData(JsonObject json) {
        JsonObject posObj = json.getAsJsonObject("pos");
        this.pos = new Vec3(
            posObj.get("x").getAsDouble(),
            posObj.get("y").getAsDouble(),
            posObj.get("z").getAsDouble()
        );
        this.particleCount = json.has("particle_count") ? json.get("particle_count").getAsFloat() : 1.0f;
        this.spreadDistance = json.has("spread_distance") ? json.get("spread_distance").getAsFloat() : 1.0f;
        this.flashScale = json.has("flash_scale") ? json.get("flash_scale").getAsFloat() : 1.0f;
        this.flashLifetime = json.has("flash_lifetime") ? json.get("flash_lifetime").getAsInt() : 5;
    }
    
    public MuzzleSmokeData(Vec3 pos, float particleCount, float spreadDistance) {
        this(pos, particleCount, spreadDistance, 1.0f, 5);
    }
    
    public MuzzleSmokeData(Vec3 pos, float particleCount, float spreadDistance, float flashScale, int flashLifetime) {
        this.pos = pos;
        this.particleCount = particleCount;
        this.spreadDistance = spreadDistance;
        this.flashScale = flashScale;
        this.flashLifetime = flashLifetime;
    }
}