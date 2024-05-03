package com.onewhohears.dscombat.data.radar;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetGenerator;

import net.minecraft.data.DataGenerator;

public class RadarPresetGenerator extends JsonPresetGenerator<RadarStats> {

	@Override
	protected void registerPresets() {
		addPresetToGenerate(RadarStats.Builder
				.create(DSCombatMod.MODID, "ar500")
				.setRange(500f)
				.setThroGroundRange(0f)
				.setThroWaterRange(0f)
				.setSensitivity(0.3f)
				.setFieldOfView(70f)
				.setScanRate(20)
				.setScanAircraft(true)
				.setScanPlayers(true)
				.setScanMobs(false)
				.setScanGround(false)
				.setScanAir(true)
				.build());
		addPresetToGenerate(RadarStats.Builder
				.create(DSCombatMod.MODID, "ar1k")
				.setRange(1000f)
				.setThroGroundRange(0f)
				.setThroWaterRange(0f)
				.setSensitivity(0.3f)
				.setFieldOfView(60f)
				.setScanRate(20)
				.setScanAircraft(true)
				.setScanPlayers(true)
				.setScanMobs(false)
				.setScanGround(false)
				.setScanAir(true)
				.build());
		addPresetToGenerate(RadarStats.Builder
				.create(DSCombatMod.MODID, "ar2k")
				.setRange(2000f)
				.setThroGroundRange(0f)
				.setThroWaterRange(0f)
				.setSensitivity(0.3f)
				.setFieldOfView(60f)
				.setScanRate(20)
				.setScanAircraft(true)
				.setScanPlayers(true)
				.setScanMobs(false)
				.setScanGround(false)
				.setScanAir(true)
				.build());
		addPresetToGenerate(RadarStats.Builder
				.create(DSCombatMod.MODID, "axcel_truck_radar")
				.setRange(1000f)
				.setThroGroundRange(0f)
				.setThroWaterRange(0f)
				.setSensitivity(0.3f)
				.setFieldOfView(-1)
				.setScanRate(30)
				.setScanAircraft(true)
				.setScanPlayers(true)
				.setScanMobs(false)
				.setScanGround(false)
				.setScanAir(true)
				.build());
		addPresetToGenerate(RadarStats.Builder
				.create(DSCombatMod.MODID, "ar20k")
				.setRange(20000f)
				.setThroGroundRange(0f)
				.setThroWaterRange(0f)
				.setSensitivity(0.5f)
				.setFieldOfView(-1f)
				.setScanRate(80)
				.setScanAircraft(true)
				.setScanPlayers(true)
				.setScanMobs(false)
				.setScanGround(false)
				.setScanAir(true)
				.build());
		addPresetToGenerate(RadarStats.Builder
				.create(DSCombatMod.MODID, "wr400")
				.setRange(400f)
				.setThroGroundRange(0f)
				.setThroWaterRange(400f)
				.setSensitivity(0.5f)
				.setFieldOfView(-1)
				.setScanRate(30)
				.setScanAircraft(true)
				.setScanPlayers(true)
				.setScanMobs(false)
				.setScanGround(false)
				.setScanAir(false)
				.build());
		addPresetToGenerate(RadarStats.Builder
				.create(DSCombatMod.MODID, "wr1k")
				.setRange(1000f)
				.setThroGroundRange(0f)
				.setThroWaterRange(1000f)
				.setSensitivity(0.5f)
				.setFieldOfView(-1)
				.setScanRate(30)
				.setScanAircraft(true)
				.setScanPlayers(true)
				.setScanMobs(false)
				.setScanGround(false)
				.setScanAir(false)
				.build());
		addPresetToGenerate(RadarStats.Builder
				.create(DSCombatMod.MODID, "gr200")
				.setRange(200f)
				.setThroGroundRange(0f)
				.setThroWaterRange(0f)
				.setSensitivity(0.8f)
				.setFieldOfView(-1f)
				.setScanRate(40)
				.setScanAircraft(true)
				.setScanPlayers(true)
				.setScanMobs(true)
				.setScanGround(true)
				.setScanAir(false)
				.build());
		addPresetToGenerate(RadarStats.Builder
				.create(DSCombatMod.MODID, "gr400")
				.setRange(400f)
				.setThroGroundRange(0f)
				.setThroWaterRange(0f)
				.setSensitivity(0.8f)
				.setFieldOfView(-1f)
				.setScanRate(40)
				.setScanAircraft(true)
				.setScanPlayers(true)
				.setScanMobs(true)
				.setScanGround(true)
				.setScanAir(false)
				.build());
		addPresetToGenerate(RadarStats.Builder
				.create(DSCombatMod.MODID, "gpr20")
				.setRange(500f)
				.setThroGroundRange(20f)
				.setThroWaterRange(0f)
				.setSensitivity(0.8f)
				.setFieldOfView(-1f)
				.setScanRate(50)
				.setScanAircraft(true)
				.setScanPlayers(true)
				.setScanMobs(true)
				.setScanGround(true)
				.setScanAir(false)
				.build());
		addPresetToGenerate(RadarStats.Builder
				.create(DSCombatMod.MODID, "gpr100")
				.setRange(500f)
				.setThroGroundRange(100f)
				.setThroWaterRange(0f)
				.setSensitivity(0.8f)
				.setFieldOfView(-1f)
				.setScanRate(50)
				.setScanAircraft(true)
				.setScanPlayers(true)
				.setScanMobs(true)
				.setScanGround(true)
				.setScanAir(false)
				.build());
		addPresetToGenerate(RadarStats.Builder
				.create(DSCombatMod.MODID, "air_scan_a")
				.setRange(1400f)
				.setThroGroundRange(0f)
				.setThroWaterRange(0f)
				.setSensitivity(0.8f)
				.setFieldOfView(-1f)
				.setScanRate(20)
				.setScanAircraft(true)
				.setScanPlayers(true)
				.setScanMobs(true)
				.setScanGround(false)
				.setScanAir(true)
				.build());
		addPresetToGenerate(RadarStats.Builder
				.create(DSCombatMod.MODID, "air_scan_b")
				.setRange(1800f)
				.setThroGroundRange(0f)
				.setThroWaterRange(0f)
				.setSensitivity(0.7f)
				.setFieldOfView(-1f)
				.setScanRate(20)
				.setScanAircraft(true)
				.setScanPlayers(true)
				.setScanMobs(true)
				.setScanGround(false)
				.setScanAir(true)
				.build());
		addPresetToGenerate(RadarStats.Builder
				.create(DSCombatMod.MODID, "survey_all_a")
				.setRange(600f)
				.setThroGroundRange(0f)
				.setThroWaterRange(10f)
				.setSensitivity(0.9f)
				.setFieldOfView(-1f)
				.setScanRate(20)
				.setScanAircraft(true)
				.setScanPlayers(true)
				.setScanMobs(true)
				.setScanGround(true)
				.setScanAir(true)
				.build());
		addPresetToGenerate(RadarStats.Builder
				.create(DSCombatMod.MODID, "survey_all_b")
				.setRange(1200f)
				.setThroGroundRange(0f)
				.setThroWaterRange(10f)
				.setSensitivity(0.8f)
				.setFieldOfView(-1f)
				.setScanRate(20)
				.setScanAircraft(true)
				.setScanPlayers(true)
				.setScanMobs(true)
				.setScanGround(true)
				.setScanAir(true)
				.build());
	}
	
	public RadarPresetGenerator(DataGenerator output) {
		super(output, "radars");
	}

	@Override
	public String getName() {
		return "Radars: "+DSCombatMod.MODID;
	}

}
