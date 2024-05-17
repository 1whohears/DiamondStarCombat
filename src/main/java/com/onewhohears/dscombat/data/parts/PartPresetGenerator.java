package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetGenerator;
import com.onewhohears.dscombat.data.parts.stats.BuffStats.BuffType;
import com.onewhohears.dscombat.data.parts.stats.EngineStats.EngineType;
import com.onewhohears.dscombat.data.parts.stats.PartStats;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.data.DataGenerator;

public class PartPresetGenerator extends JsonPresetGenerator<PartStats> {
	
	@Override
	protected void registerPresets() {
		// BUFFS
		addPresetToGenerate(PartBuilder.create(ModItems.DATA_LINK.getId(), PartType.BUFF)
				.setCompatibleSlotType(SlotType.TECH_INTERNAL)
				.setWeight(20)
				.setBuffStats(BuffType.DATA_LINK)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.NIGHT_VISION_HUD.getId(), PartType.BUFF)
				.setCompatibleSlotType(SlotType.TECH_INTERNAL)
				.setWeight(10)
				.setBuffStats(BuffType.NIGHT_VISION_HUD)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.RADIO.getId(), PartType.BUFF)
				.setCompatibleSlotType(SlotType.INTERNAL)
				.setWeight(40)
				.setBuffStats(BuffType.RADIO)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.ARMOR_PIECE.getId(), PartType.BUFF)
				.setCompatibleSlotType(SlotType.INTERNAL)
				.setWeight(700)
				.setBuffStats(BuffType.ARMOR)
				.build());
		// GIMBAL
		addPresetToGenerate(PartBuilder.create(ModItems.GIMBAL_CAMERA.getId(), PartType.GIMBAL)
				.setCompatibleSlotType(SlotType.PYLON_LIGHT)
				.setWeight(100)
				.setExternalEntityType(ModEntities.GIMBAL_CAMERA.getId())
				.build());
		// CHAIN HOOK
		addPresetToGenerate(PartBuilder.create(ModItems.CHAIN_HOOK.getId(), PartType.GIMBAL)
				.setCompatibleSlotType(SlotType.EXTERNAL_TOUGH)
				.setWeight(500)
				.setExternalEntityType(ModEntities.CHAIN_HOOK.getId())
				.build());
		// STORAGE
		addPresetToGenerate(PartBuilder.create(ModItems.SMALL_STORAGE_BOX.getId(), PartType.INTERNAL_STORAGE)
				.setCompatibleSlotType(SlotType.INTERNAL)
				.setWeight(1000)
				.setStorageStats(9)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.MED_STORAGE_BOX.getId(), PartType.INTERNAL_STORAGE)
				.setCompatibleSlotType(SlotType.INTERNAL)
				.setWeight(2000)
				.setStorageStats(18)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.LARGE_STORAGE_BOX.getId(), PartType.INTERNAL_STORAGE)
				.setCompatibleSlotType(SlotType.INTERNAL)
				.setWeight(3000)
				.setStorageStats(27)
				.build());
		// FUEL TANK
		addPresetToGenerate(PartBuilder.create(ModItems.LIGHT_FUEL_TANK.getId(), PartType.FUEL_TANK)
				.setCompatibleSlotType(SlotType.INTERNAL)
				.setWeight(500)
				.setFuelTankStats(50)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.HEAVY_FUEL_TANK.getId(), PartType.FUEL_TANK)
				.setCompatibleSlotType(SlotType.INTERNAL)
				.setWeight(1500)
				.setFuelTankStats(150)
				.build());
		// ENGINES
		addPresetToGenerate(PartBuilder.create(ModItems.C6_ENGINE.getId(), PartType.INTERNAL_ENGINE)
				.setCompatibleSlotType(SlotType.SPIN_ENGINE)
				.setWeight(300)
				.setEngineStats(EngineType.SPIN, 60, 4, 0.005f)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.C12_ENGINE.getId(), PartType.INTERNAL_ENGINE)
				.setCompatibleSlotType(SlotType.SPIN_ENGINE)
				.setWeight(700)
				.setEngineStats(EngineType.SPIN, 130, 8, 0.011f)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.TURBOFAN_F25.getId(), PartType.INTERNAL_ENGINE)
				.setCompatibleSlotType(SlotType.PUSH_ENGINE)
				.setWeight(500)
				.setEngineStats(EngineType.PUSH, 180, 4, 0.005f)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.TURBOFAN_F145.getId(), PartType.INTERNAL_ENGINE)
				.setCompatibleSlotType(SlotType.PUSH_ENGINE)
				.setWeight(1100)
				.setEngineStats(EngineType.PUSH, 400, 8, 0.011f)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.TURBOFAN_F39.getId(), PartType.INTERNAL_ENGINE)
				.setCompatibleSlotType(SlotType.PUSH_ENGINE)
				.setWeight(800)
				.setEngineStats(EngineType.PUSH, 340, 5, 0.007f)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.KLIMOV_RD33.getId(), PartType.INTERNAL_ENGINE)
				.setCompatibleSlotType(SlotType.PUSH_ENGINE)
				.setWeight(600)
				.setEngineStats(EngineType.PUSH, 225, 6.5f, 0.006f)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.CM_MANLY_52.getId(), PartType.INTERNAL_ENGINE)
				.setCompatibleSlotType(SlotType.RADIAL_ENGINE)
				.setWeight(200)
				.setEngineStats(EngineType.PUSH, 40, 2, 0.004f)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.ALLISON_V_1710.getId(), PartType.INTERNAL_ENGINE)
				.setCompatibleSlotType(SlotType.RADIAL_ENGINE)
				.setWeight(300)
				.setEngineStats(EngineType.PUSH, 80, 2.5f, 0.009f)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.COMPOUND_TURBINE.getId(), PartType.INTERNAL_ENGINE)
				.setCompatibleSlotType(SlotType.RADIAL_ENGINE)
				.setWeight(3500)
				.setEngineStats(EngineType.PUSH, 500, 8, 0.019f)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.CFM56.getId(), PartType.INTERNAL_ENGINE)
				.setCompatibleSlotType(SlotType.PYLON_HEAVY)
				.setWeight(2000)
				.setEngineStats(EngineType.PUSH, 650, 9, 0.017f)
				.setExternalEntityType(ModEntities.CFM56.getId())
				.build());
		// INTERNAL RADAR
		addPresetToGenerate(PartBuilder.create(ModItems.AR500.getId(), PartType.INTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.TECH_INTERNAL)
				.setWeight(200)
				.setRadarStats("ar500")
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.AR1K.getId(), PartType.INTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.TECH_INTERNAL)
				.setWeight(300)
				.setRadarStats("ar1k")
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.AR2K.getId(), PartType.INTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.TECH_INTERNAL)
				.setWeight(400)
				.setRadarStats("ar2k")
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.GR200.getId(), PartType.INTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.TECH_INTERNAL)
				.setWeight(200)
				.setRadarStats("gr200")
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.GR400.getId(), PartType.INTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.TECH_INTERNAL)
				.setWeight(300)
				.setRadarStats("gr400")
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.WR400.getId(), PartType.INTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.TECH_INTERNAL)
				.setWeight(200)
				.setRadarStats("wr400")
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.WR1K.getId(), PartType.INTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.TECH_INTERNAL)
				.setWeight(350)
				.setRadarStats("wr1k")
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.GPR20.getId(), PartType.INTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.TECH_INTERNAL)
				.setWeight(600)
				.setRadarStats("gpr20")
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.GPR100.getId(), PartType.INTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.TECH_INTERNAL)
				.setWeight(800)
				.setRadarStats("gpr100")
				.build());
		// EXTERNAL RADAR
		addPresetToGenerate(PartBuilder.create(ModItems.AR20K.getId(), PartType.EXTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.MOUNT_HEAVY)
				.setWeight(4000)
				.setRadarStats("ar20k")
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.AXCEL_TRUCK_RADAR.getId(), PartType.EXTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.MOUNT_TECH)
				.setWeight(1000)
				.setRadarStats("axcel_truck_radar")
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.AIR_SCAN_A.getId(), PartType.EXTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.MOUNT_TECH)
				.setWeight(1500)
				.setRadarStats("air_scan_a")
				.setExternalEntityType(ModEntities.AIR_SCAN_A.getId())
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.AIR_SCAN_B.getId(), PartType.EXTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.MOUNT_TECH)
				.setWeight(1500)
				.setRadarStats("air_scan_b")
				.setExternalEntityType(ModEntities.AIR_SCAN_B.getId())
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.SURVEY_ALL_A.getId(), PartType.EXTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.MOUNT_TECH)
				.setWeight(1500)
				.setRadarStats("survey_all_a")
				.setExternalEntityType(ModEntities.SURVEY_ALL_A.getId())
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.SURVEY_ALL_B.getId(), PartType.EXTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.MOUNT_TECH)
				.setWeight(1500)
				.setRadarStats("survey_all_b")
				.setExternalEntityType(ModEntities.SURVEY_ALL_B.getId())
				.build());
		// SEAT
		addPresetToGenerate(PartBuilder.create(ModItems.SEAT.getId(), PartType.SEAT)
				.setCompatibleSlotType(SlotType.SEAT)
				.setWeight(30)
				.setExternalEntityType(ModEntities.SEAT.getId())
				.build());
		// TURRET
		addPresetToGenerate(PartBuilder.create(ModItems.AA_TURRET.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_LIGHT)
				.setWeight(1000)
				.setExternalEntityType(ModEntities.AA_TURRET.getId())
				.setTurretStats(750, 40)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.MINIGUN_TURRET.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_LIGHT)
				.setWeight(1500)
				.setExternalEntityType(ModEntities.MINIGUN_TURRET.getId())
				.setTurretStats(1000, 40)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.CIWS.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_LIGHT)
				.setWeight(2500)
				.setExternalEntityType(ModEntities.CIWS.getId())
				.setTurretStats(500, 80)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.MARK45_CANNON.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_LIGHT)
				.setWeight(3000)
				.setExternalEntityType(ModEntities.MARK45_CANNON.getId())
				.setTurretStats(50, 120)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.HEAVY_TANK_TURRET.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_MED)
				.setWeight(4000)
				.setExternalEntityType(ModEntities.HEAVY_TANK_TURRET.getId())
				.setTurretStats(16, 120)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.MARK7_CANNON.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_HEAVY)
				.setWeight(4500)
				.setExternalEntityType(ModEntities.MARK7_CANNON.getId())
				.setTurretStats(36, 200)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.STEVE_UP_SMASH.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_MED)
				.setWeight(5000)
				.setExternalEntityType(ModEntities.STEVE_UP_SMASH.getId())
				.setTurretStats(4, 40)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.SAM_LAUNCHER.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_HEAVY)
				.setWeight(6000)
				.setExternalEntityType(ModEntities.SAM_LAUNCHER.getId())
				.setTurretStats(4, 60)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.TORPEDO_TUBES.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_MED)
				.setWeight(5500)
				.setExternalEntityType(ModEntities.TORPEDO_TUBES.getId())
				.setTurretStats(3, 60)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.MLS.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_HEAVY)
				.setWeight(6000)
				.setExternalEntityType(ModEntities.MLS.getId())
				.setTurretStats(4, 60)
				.build());
		// DISPENSERS
		addPresetToGenerate(PartBuilder.create(ModItems.BASIC_FLARE_DISPENSER.getId(), PartType.FLARE_DISPENSER)
				.setCompatibleSlotType(SlotType.INTERNAL)
				.setWeight(100)
				.setFlareDispenserStats(20, 120, 20)
				.build());
		// WEAPONS
		addPresetToGenerate(PartBuilder.create(ModItems.XM12.getId(), PartType.EXTERNAL_WEAPON)
				.setCompatibleSlotType(SlotType.PYLON_LIGHT)
				.setWeight(500)
				.setExternalWeaponStats(300, 0)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.LIGHT_MISSILE_RACK.getId(), PartType.EXTERNAL_WEAPON)
				.setCompatibleSlotType(SlotType.PYLON_LIGHT)
				.setWeight(750)
				.setExternalWeaponStats(4, 0)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.HEAVY_MISSILE_RACK.getId(), PartType.EXTERNAL_WEAPON)
				.setCompatibleSlotType(SlotType.PYLON_LIGHT)
				.setWeight(1500)
				.setExternalWeaponStats(2, 0)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.BOMB_RACK.getId(), PartType.EXTERNAL_WEAPON)
				.setCompatibleSlotType(SlotType.PYLON_LIGHT)
				.setWeight(2000)
				.setExternalWeaponStats(16, 0)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.ADL.getId(), PartType.EXTERNAL_WEAPON)
				.setCompatibleSlotType(SlotType.PYLON_LIGHT)
				.setWeight(6000)
				.setExternalWeaponStats(3, 20)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.VLS.getId(), PartType.EXTERNAL_WEAPON)
				.setCompatibleSlotType(SlotType.PYLON_LIGHT)
				.setWeight(6000)
				.setExternalWeaponStats(16, 90)
				.build());
	}
	
	public PartPresetGenerator(DataGenerator output) {
		super(output, "parts");
	}

	@Override
	public String getName() {
		return "Parts: "+DSCombatMod.MODID;
	}

}
