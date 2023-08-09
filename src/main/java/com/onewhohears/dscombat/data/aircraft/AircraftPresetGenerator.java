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
		addPresetToGenerate(DefaultAircraftPresets.SUPPORT_ALEXIS_PLANE);
		addPresetToGenerate(DefaultAircraftPresets.SNIPER_ALEXIS_PLANE);
		
		addPresetToGenerate(DefaultAircraftPresets.EMPTY_JAVI_PLANE);
		addPresetToGenerate(DefaultAircraftPresets.UNARMED_JAVI_PLANE);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_JAVI_PLANE);
		addPresetToGenerate(DefaultAircraftPresets.BOMBER_JAVI_PLANE);
		addPresetToGenerate(DefaultAircraftPresets.TRUCK_JAVI_PLANE);
		
		addPresetToGenerate(DefaultAircraftPresets.EMPTY_NOAH_CHOPPER);
		addPresetToGenerate(DefaultAircraftPresets.UNARMED_NOAH_CHOPPER);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_NOAH_CHOPPER);
		
		addPresetToGenerate(DefaultAircraftPresets.EMPTY_MRBUDGER_TANK);
		addPresetToGenerate(DefaultAircraftPresets.UNARMED_MRBUDGER_TANK);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_MRBUDGER_TANK);
		
		addPresetToGenerate(DefaultAircraftPresets.EMPTY_SMALL_ROLLER);
		addPresetToGenerate(DefaultAircraftPresets.UNARMED_SMALL_ROLLER);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_SMALL_ROLLER);
		addPresetToGenerate(DefaultAircraftPresets.TANK_SMALL_ROLLER);
		
		addPresetToGenerate(DefaultAircraftPresets.EMPTY_NATHAN_BOAT);
		addPresetToGenerate(DefaultAircraftPresets.UNARMED_NATHAN_BOAT);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_NATHAN_BOAT);
		
		addPresetToGenerate(DefaultAircraftPresets.EMPTY_ANDOLF_SUB);
		addPresetToGenerate(DefaultAircraftPresets.UNARMED_ANDOLF_SUB);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_ANDOLF_SUB);
		
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_ORANGE_TESLA);
		
		addPresetToGenerate(DefaultAircraftPresets.EMPTY_WOODEN_PLANE);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_WOODEN_PLANE);
		
		addPresetToGenerate(DefaultAircraftPresets.EMPTY_E3SENTRY_PLANE);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_E3SENTRY_PLANE);
		
		addPresetToGenerate(DefaultAircraftPresets.EMPTY_AXCEL_TRUCK);
		addPresetToGenerate(DefaultAircraftPresets.DEFAULT_AXCEL_TRUCK);
	}
    
    public AircraftPresetGenerator(DataGenerator output) {
		super(output, "aircraft");
	}
    
    @Override
	public String getName() {
		return "Aircraft: "+DSCombatMod.MODID;
	}
    
}
