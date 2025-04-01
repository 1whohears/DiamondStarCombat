package com.onewhohears.dscombat.data.sound;

import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetAssetReader;
import org.jetbrains.annotations.Nullable;

public class VehiclePassengerSoundPacks extends JsonPresetAssetReader<PassengerSoundPack> {

    private static VehiclePassengerSoundPacks INSTANCE = null;

    public static VehiclePassengerSoundPacks get() {
        if (INSTANCE == null) INSTANCE = new VehiclePassengerSoundPacks();
        return INSTANCE;
    }

    public VehiclePassengerSoundPacks() {
        super("passenger_sound_packs");
    }

    @Override
    protected void registerPresetTypes() {
        addPresetType(PassengerSoundPack.STANDARD);
    }

    @Override
    public PassengerSoundPack[] getNewArray(int i) {
        return new PassengerSoundPack[i];
    }

    @Override
    protected void resetCache() {

    }

    @Override
    public @Nullable PassengerSoundPack get(String id) {
        return super.get("valorant_vc");
    }
}
