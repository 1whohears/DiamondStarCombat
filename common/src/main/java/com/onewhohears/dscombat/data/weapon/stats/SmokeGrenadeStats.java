package com.onewhohears.dscombat.data.weapon.stats;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.instance.SmokeGrenadeInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Stats for a smoke grenade launcher.
 * <p>
 * JSON fields (in addition to base WeaponStats):
 * <ul>
 *   <li>{@code smoke_duration}   – ticks the smoke cloud lasts after detonation (default 200)</li>
 *   <li>{@code fuse_ticks}       – ticks in flight before mid-air detonation (default 40)</li>
 *   <li>{@code smoke_radius}     – radius in blocks of the smoke cloud (default 4.0)</li>
 *   <li>{@code deploy_sound_key} – sound played when the grenade detonates and releases smoke</li>
 *   <li>{@code launchers}        – array of launcher definitions, each with:
 *     <ul>
 *       <li>{@code pos}   – relative position {x,y,z} on the vehicle</li>
 *       <li>{@code yaw}   – horizontal launch angle offset in degrees (0 = forward)</li>
 *       <li>{@code pitch} – vertical launch angle offset in degrees (0 = horizontal)</li>
 *     </ul>
 *   </li>
 * </ul>
 */
public class SmokeGrenadeStats extends WeaponStats {

    public static class LauncherDef {
        public final Vec3 pos;
        /** Yaw offset in degrees relative to vehicle forward (0 = forward, 90 = right). */
        public final float yaw;
        /** Pitch offset in degrees (0 = horizontal, positive = upward). */
        public final float pitch;

        public LauncherDef(Vec3 pos, float yaw, float pitch) {
            this.pos = pos;
            this.yaw = yaw;
            this.pitch = pitch;
        }
    }

    private final int smokeDuration;
    private final int fuseTicks;
    private final float smokeRadius;
    private final String deploySoundKey;
    private final LauncherDef[] launchers;

    public SmokeGrenadeStats(ResourceLocation key, JsonObject json) {
        super(key, json);
        this.smokeDuration = UtilParse.getIntSafe(json, "smoke_duration", 200);
        this.fuseTicks     = UtilParse.getIntSafe(json, "fuse_ticks", 40);
        this.smokeRadius   = UtilParse.getFloatSafe(json, "smoke_radius", 4f);
        this.deploySoundKey = UtilParse.getStringSafe(json, "deploy_sound_key", "");

        if (json.has("launchers")) {
            JsonArray arr = json.getAsJsonArray("launchers");
            List<LauncherDef> list = new ArrayList<>();
            for (int i = 0; i < arr.size(); i++) {
                JsonObject o = arr.get(i).getAsJsonObject();
                Vec3 pos = UtilParse.readVec3(o, "pos");
                float yaw   = UtilParse.getFloatSafe(o, "yaw", 0f);
                float pitch = UtilParse.getFloatSafe(o, "pitch", 10f);
                list.add(new LauncherDef(pos, yaw, pitch));
            }
            this.launchers = list.toArray(new LauncherDef[0]);
        } else {
            // Default: single forward launcher
            this.launchers = new LauncherDef[]{ new LauncherDef(Vec3.ZERO, 0f, 10f) };
        }
    }

    @Override
    public JsonPresetInstance<?> createPresetInstance() {
        return new SmokeGrenadeInstance(this);
    }

    @Override
    public double getMobTurretRange() { return 0; }

    @Override
    public String getWeaponTypeCode() { return "smoke"; }

    @Override
    public JsonPresetType getType() { return WeaponType.SMOKE_GRENADE; }

    public int getSmokeDuration() { return smokeDuration; }
    public int getFuseTicks()     { return fuseTicks; }
    public float getSmokeRadius() { return smokeRadius; }
    public String getDeploySoundKey() { return deploySoundKey; }
    public LauncherDef[] getLaunchers() { return launchers; }
}
