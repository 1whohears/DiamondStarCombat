package com.onewhohears.dscombat.data.aircraft;

import com.onewhohears.dscombat.DSCombatMod;

import net.minecraft.resources.ResourceLocation;

public class BaseAircraftPresets {
	
	public static AircraftPreset ALEXIS_PLANE = AircraftPreset.Builder
			.create(new ResourceLocation(DSCombatMod.MODID, "alexis_plane"))
			
			.build();
	
}
