package com.onewhohears.dscombat.data.weapon.client;

import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetAssetReader;
import com.onewhohears.onewholibs.data.jsonpreset.PresetStatsHolder;

public class WeaponAssets extends JsonPresetAssetReader<WeaponClientStats> {

    private static WeaponAssets instance;

    public WeaponAssets() {
        super("weapon_client");
    }

    public static WeaponAssets get() {
        if (instance == null) instance = new WeaponAssets();
        return instance;
    }

    public static void close() {
        instance = null;
    }

    @Override
    protected void registerPresetTypes() {
        addPresetType(WeaponClientStats.STANDARD);
    }

    @Override
    public WeaponClientStats get(String id) {
        WeaponClientStats stats = super.get(id);
        if (stats != null) return stats;
        return addNew(id);
    }

    @Override
    public PresetStatsHolder<WeaponClientStats> getHolder(String id) {
        if (!has(id)) addNew(id);
        return super.getHolder(id);
    }

    @Override
    public WeaponClientStats[] getNewArray(int i) {
        return new WeaponClientStats[0];
    }

    @Override
    protected void resetCache() {

    }

    protected WeaponClientStats addNew(String id) {
        WeaponClientStats stats = WeaponClientStats.Builder.createStandard(id).build();
        presetMap.put(id, stats);
        return stats;
    }

}
