package com.onewhohears.dscombat.data.weapon.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.instance.DumbTorpedoInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import net.minecraft.resources.ResourceLocation;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

public class DumbTorpedoStats extends MissileStats {

    public DumbTorpedoStats(ResourceLocation key, JsonObject json) {
        super(key, json);
    }

    @Override
    public JsonPresetType getType() {
        return WeaponType.DUMB_TORPEDO;
    }

    @Override
    public JsonPresetInstance<?> createPresetInstance() {
        return new DumbTorpedoInstance<>(this);
    }

    public TrackMissileStats.TargetType getTargetType() {
        return TrackMissileStats.TargetType.WATER;
    }

    @Override
    public String getWeaponTypeCode() {
        return "DTR";
    }

    @Override
    public String getDefaultIconLocation() {
        return MODID+":textures/ui/weapon_icons/torpedo.png";
    }
}
