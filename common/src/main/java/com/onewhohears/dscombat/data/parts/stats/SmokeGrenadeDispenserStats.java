package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.SmokeGrenadeDispenserInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Part stats for a smoke grenade dispenser.
 * Placed in a vehicle slot via the parts menu.
 *
 * JSON fields (beyond base PartStats):
 *   max_grenades   – total grenades in the dispenser (default 12)
 *   fuse_ticks     – ticks in flight before mid-air detonation (default 40)
 *   smoke_duration – ticks the smoke cloud lasts (default 300)
 *   smoke_radius   – radius in blocks of the smoke cloud (default 5.0)
 *   deploy_sound_key – optional override for the deploy sound
 *   launchers      – array of {pos:{x,y,z}, yaw, pitch} launcher definitions
 */
public class SmokeGrenadeDispenserStats extends PartStats {

    public static class LauncherDef {
        public final Vec3 pos;
        public final float yaw;
        public final float pitch;
        public LauncherDef(Vec3 pos, float yaw, float pitch) {
            this.pos = pos; this.yaw = yaw; this.pitch = pitch;
        }
    }

    private final int maxGrenades;
    private final int fuseTicks;
    private final int smokeDuration;
    private final float smokeRadius;
    private final String deploySoundKey;
    private final LauncherDef[] launchers;

    public SmokeGrenadeDispenserStats(ResourceLocation key, JsonObject json) {
        super(key, json);
        maxGrenades   = UtilParse.getIntSafe(json, "max_grenades", 12);
        fuseTicks     = UtilParse.getIntSafe(json, "fuse_ticks", 40);
        smokeDuration = UtilParse.getIntSafe(json, "smoke_duration", 300);
        smokeRadius   = UtilParse.getFloatSafe(json, "smoke_radius", 5f);
        deploySoundKey = UtilParse.getStringSafe(json, "deploy_sound_key", "");

        if (json.has("launchers")) {
            JsonArray arr = json.getAsJsonArray("launchers");
            List<LauncherDef> list = new ArrayList<>();
            for (int i = 0; i < arr.size(); i++) {
                JsonObject o = arr.get(i).getAsJsonObject();
                Vec3 pos   = UtilParse.readVec3(o, "pos");
                float yaw  = UtilParse.getFloatSafe(o, "yaw", 0f);
                float pitch = UtilParse.getFloatSafe(o, "pitch", 15f);
                list.add(new LauncherDef(pos, yaw, pitch));
            }
            launchers = list.toArray(new LauncherDef[0]);
        } else {
            launchers = new LauncherDef[]{
                new LauncherDef(new Vec3( 1.5, 0.5, 0), 90f, 15f),
                new LauncherDef(new Vec3(-1.5, 0.5, 0), -90f, 15f)
            };
        }
    }

    @Override
    public JsonPresetType getType() { return PartType.SMOKE_GRENADE_DISPENSER; }

    @Override
    public JsonPresetInstance<?> createPresetInstance() {
        return new SmokeGrenadeDispenserInstance(this);
    }

    public int getMaxGrenades()   { return maxGrenades; }
    public int getFuseTicks()     { return fuseTicks; }
    public int getSmokeDuration() { return smokeDuration; }
    public float getSmokeRadius() { return smokeRadius; }
    public String getDeploySoundKey() { return deploySoundKey; }
    public LauncherDef[] getLaunchers() { return launchers; }
}
