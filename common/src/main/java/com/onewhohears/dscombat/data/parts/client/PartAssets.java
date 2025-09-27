package com.onewhohears.dscombat.data.parts.client;

import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetAssetReader;
import com.onewhohears.onewholibs.data.jsonpreset.PresetStatsHolder;

public class PartAssets extends JsonPresetAssetReader<PartClientStats> {

    private static PartAssets instance;

    public static PartAssets get() {
        if (instance == null) instance = new PartAssets();
        return instance;
    }

    public static void close() {
        instance = null;
    }

    @Override
    public PartClientStats get(String id) {
        PartClientStats stats = super.get(id);
        if (stats != null) return stats;
        return addNew(id);
    }

    @Override
    public PresetStatsHolder<PartClientStats> getHolder(String id) {
        if (!has(id)) addNew(id);
        return super.getHolder(id);
    }

    protected PartClientStats addNew(String id) {
        PartClientStats stats = PartClientStats.Builder.createStandard(id).build();
        presetMap.put(id, stats);
        return stats;
    }

    public PartAssets() {
        super("part_client");
    }

    @Override
    protected void registerPresetTypes() {
        addPresetType(PartClientType.STANDARD);
        addPresetType(PartClientType.TURRET);
        addPresetType(PartClientType.RADAR);
        addPresetType(PartClientType.WEAPON_RACK);
    }

    @Override
    public PartClientStats[] getNewArray(int i) {
        return new PartClientStats[i];
    }

    @Override
    protected void resetCache() {

    }
}
