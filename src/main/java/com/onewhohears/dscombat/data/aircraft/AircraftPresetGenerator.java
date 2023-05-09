package com.onewhohears.dscombat.data.aircraft;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.JsonPresetGenerator;

import net.minecraft.data.DataGenerator;

public class AircraftPresetGenerator extends JsonPresetGenerator<AircraftPreset> {
    
    public AircraftPresetGenerator(DataGenerator output) {
		super(output, "aircraft");
	}
    
    @Override
	public String getName() {
		return "Aircraft: "+DSCombatMod.MODID;
	}
    
    @Override
	protected void registerPresets() {
    	addPresetToGenerate(DefaultAircraftPresets.EMPTY_ALEXIS_PLANE);
    	addPresetToGenerate(DefaultAircraftPresets.UNARMED_ALEXIS_PLANE);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_ALEXIS_PLANE);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_JAVI_PLANE);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_NOAH_CHOPPER);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_MRBUDGER_TANK);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_SMALL_ROLLER);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_NATHAN_BOAT);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_ANDOLF_SUB);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_ORANGE_TESLA);
	}
    
}
