package com.onewhohears.dscombat.data.aircraft;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.JsonPresetGenerator;

import net.minecraft.data.DataGenerator;

public class AircraftPresetGenerator extends JsonPresetGenerator<AircraftPreset> {
    
    @Override
	protected void registerPresets() {
    	addPresetToGenerate(DefaultAircraftPresets.EMPTY_ALEXIS_PLANE);
    	addPresetToGenerate(DefaultAircraftPresets.UNARMED_ALEXIS_PLANE);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_ALEXIS_PLANE);
		
		addPresetToGenerate(DefaultAircraftPresets.EMPTY_JAVI_PLANE);
		addPresetToGenerate(DefaultAircraftPresets.UNARMED_JAVI_PLANE);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_JAVI_PLANE);
		
		addPresetToGenerate(DefaultAircraftPresets.EMPTY_NOAH_CHOPPER);
		addPresetToGenerate(DefaultAircraftPresets.UNARMED_NOAH_CHOPPER);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_NOAH_CHOPPER);
		
		// empty
		// unarmed
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_MRBUDGER_TANK);
		
		// empty
		// unarmed
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_SMALL_ROLLER);
		
		// empty
		// unarmed
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_NATHAN_BOAT);
		
		// empty
		// unarmed
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_ANDOLF_SUB);
		
		// empty
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_ORANGE_TESLA);
	}
    
    public AircraftPresetGenerator(DataGenerator output) {
		super(output, "aircraft");
	}
    
    @Override
	public String getName() {
		return "Aircraft: "+DSCombatMod.MODID;
	}
    
}
