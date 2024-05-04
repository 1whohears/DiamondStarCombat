package com.onewhohears.dscombat.data.aircraft;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.aircraft.presets.AlexisPresets;
import com.onewhohears.dscombat.data.aircraft.presets.BoatPresets;
import com.onewhohears.dscombat.data.aircraft.presets.BroncoPresets;
import com.onewhohears.dscombat.data.aircraft.presets.CarPresets;
import com.onewhohears.dscombat.data.aircraft.presets.EdenPresets;
import com.onewhohears.dscombat.data.aircraft.presets.FelixPresets;
import com.onewhohears.dscombat.data.aircraft.presets.HeliPresets;
import com.onewhohears.dscombat.data.aircraft.presets.JasonPresets;
import com.onewhohears.dscombat.data.aircraft.presets.JaviPresets;
import com.onewhohears.dscombat.data.aircraft.presets.PlanePresets;
import com.onewhohears.dscombat.data.aircraft.presets.SubPresets;
import com.onewhohears.dscombat.data.aircraft.presets.TankPresets;
import com.onewhohears.dscombat.data.aircraft.stats.VehicleStats;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetGenerator;

import net.minecraft.data.DataGenerator;

public class AircraftPresetGenerator extends JsonPresetGenerator<VehicleStats> {
	
	public static AircraftPresetGenerator INSTANCE;
	
    @Override
	protected void registerPresets() {
    	addPresetToGenerate(AlexisPresets.EMPTY_ALEXIS_PLANE);
    	addPresetToGenerate(AlexisPresets.UNARMED_ALEXIS_PLANE);
		addPresetToGenerate(AlexisPresets.DEFAULT_ALEXIS_PLANE);
		addPresetToGenerate(AlexisPresets.SUPPORT_ALEXIS_PLANE);
		addPresetToGenerate(AlexisPresets.SNIPER_ALEXIS_PLANE);
		
		addPresetToGenerate(JaviPresets.EMPTY_JAVI_PLANE);
		addPresetToGenerate(JaviPresets.UNARMED_JAVI_PLANE);
		addPresetToGenerate(JaviPresets.DEFAULT_JAVI_PLANE);
		addPresetToGenerate(JaviPresets.BOMBER_JAVI_PLANE);
		addPresetToGenerate(JaviPresets.TRUCK_JAVI_PLANE);
		
		addPresetToGenerate(HeliPresets.EMPTY_NOAH_CHOPPER);
		addPresetToGenerate(HeliPresets.UNARMED_NOAH_CHOPPER);
		addPresetToGenerate(HeliPresets.DEFAULT_NOAH_CHOPPER);
		
		addPresetToGenerate(TankPresets.EMPTY_MRBUDGER_TANK);
		addPresetToGenerate(TankPresets.UNARMED_MRBUDGER_TANK);
		addPresetToGenerate(TankPresets.DEFAULT_MRBUDGER_TANK);
		
		addPresetToGenerate(TankPresets.EMPTY_SMALL_ROLLER);
		addPresetToGenerate(TankPresets.UNARMED_SMALL_ROLLER);
		addPresetToGenerate(TankPresets.DEFAULT_SMALL_ROLLER);
		addPresetToGenerate(TankPresets.TANK_SMALL_ROLLER);
		
		addPresetToGenerate(BoatPresets.EMPTY_NATHAN_BOAT);
		addPresetToGenerate(BoatPresets.UNARMED_NATHAN_BOAT);
		addPresetToGenerate(BoatPresets.DEFAULT_NATHAN_BOAT);
		
		addPresetToGenerate(SubPresets.EMPTY_ANDOLF_SUB);
		addPresetToGenerate(SubPresets.UNARMED_ANDOLF_SUB);
		addPresetToGenerate(SubPresets.DEFAULT_ANDOLF_SUB);
		
		addPresetToGenerate(CarPresets.DEFAULT_ORANGE_TESLA);
		
		addPresetToGenerate(PlanePresets.EMPTY_WOODEN_PLANE);
		addPresetToGenerate(PlanePresets.DEFAULT_WOODEN_PLANE);
		addPresetToGenerate(PlanePresets.BOMBER_WOODEN_PLANE);
		addPresetToGenerate(PlanePresets.FIGHTER_WOODEN_PLANE);
		
		addPresetToGenerate(PlanePresets.EMPTY_E3SENTRY_PLANE);
		addPresetToGenerate(PlanePresets.DEFAULT_E3SENTRY_PLANE);
		
		addPresetToGenerate(CarPresets.EMPTY_AXCEL_TRUCK);
		addPresetToGenerate(CarPresets.UNARMED_AXCEL_TRUCK);
		addPresetToGenerate(CarPresets.DEFAULT_AXCEL_TRUCK);
		
		addPresetToGenerate(BoatPresets.EMPTY_GRONK_BATTLESHIP);
		addPresetToGenerate(BoatPresets.UNARMED_GRONK_BATTLESHIP);
		addPresetToGenerate(BoatPresets.DEFAULT_GRONK_BATTLESHIP);
		
		addPresetToGenerate(BoatPresets.EMPTY_DESTROYER);
		addPresetToGenerate(BoatPresets.UNARMED_DESTROYER);
		addPresetToGenerate(BoatPresets.DEFAULT_DESTROYER);
		
		addPresetToGenerate(BoatPresets.EMPTY_CRUISER);
		addPresetToGenerate(BoatPresets.UNARMED_CRUISER);
		addPresetToGenerate(BoatPresets.DEFAULT_CRUISER);
		
		addPresetToGenerate(BoatPresets.EMPTY_CORVETTE);
		addPresetToGenerate(BoatPresets.UNARMED_CORVETTE);
		addPresetToGenerate(BoatPresets.DEFAULT_CORVETTE);
		
		addPresetToGenerate(BoatPresets.EMPTY_AIRCRAFT_CARRIER);
		addPresetToGenerate(BoatPresets.UNARMED_AIRCRAFT_CARRIER);
		addPresetToGenerate(BoatPresets.DEFAULT_AIRCRAFT_CARRIER);
		
		addPresetToGenerate(BroncoPresets.EMPTY_BRONCO_PLANE);
		addPresetToGenerate(BroncoPresets.UNARMED_BRONCO_PLANE);
		addPresetToGenerate(BroncoPresets.DEFAULT_BRONCO_PLANE);
		
		addPresetToGenerate(FelixPresets.EMPTY_FELIX_PLANE);
		addPresetToGenerate(FelixPresets.UNARMED_FELIX_PLANE);
		addPresetToGenerate(FelixPresets.DEFAULT_FELIX_PLANE);
		addPresetToGenerate(FelixPresets.AIR_SUPPORT_FELIX_PLANE);
		
		addPresetToGenerate(JasonPresets.EMPTY_JASON_PLANE);
		addPresetToGenerate(JasonPresets.UNARMED_JASON_PLANE);
		addPresetToGenerate(JasonPresets.DEFAULT_JASON_PLANE);
		
		addPresetToGenerate(EdenPresets.EMPTY_EDEN_PLANE);
		addPresetToGenerate(EdenPresets.UNARMED_EDEN_PLANE);
		addPresetToGenerate(EdenPresets.DEFAULT_EDEN_PLANE);
		addPresetToGenerate(EdenPresets.SUPPORT_EDEN_PLANE);
		
		addPresetToGenerate(SubPresets.EMPTY_GOOGLE_SUB);
		addPresetToGenerate(SubPresets.UNARMED_GOOGLE_SUB);
		addPresetToGenerate(SubPresets.DEFAULT_GOOGLE_SUB);
	}
    
    public AircraftPresetGenerator(DataGenerator output) {
		super(output, "aircraft");
		INSTANCE = this;
	}
    
    @Override
	public String getName() {
		return "Aircraft: "+DSCombatMod.MODID;
	}
    
}
