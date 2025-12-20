package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.stats.TurretStats;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetGenerator;
import com.onewhohears.dscombat.data.parts.stats.BuffStats.BuffType;
import com.onewhohears.dscombat.data.parts.stats.EngineStats.EngineType;
import com.onewhohears.dscombat.data.parts.stats.PartStats;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.phys.Vec3;

public class PartPresetGenerator extends JsonPresetGenerator<PartStats> {

	public static PartPresetGenerator INSTANCE;

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
				.setWeight(100)
				.setFuelTankStats(50)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.HEAVY_FUEL_TANK.getId(), PartType.FUEL_TANK)
				.setCompatibleSlotType(SlotType.INTERNAL)
				.setWeight(200)
				.setFuelTankStats(150)
				.build());
        addPresetToGenerate(PartBuilder.create(ModItems.LIGHT_EXTERNAL_FUEL_TANK.getId(), PartType.EXTERNAL_FUEL_TANK)
                .setCompatibleSlotType(SlotType.PYLON_LIGHT)
                .setWeight(125)
                .setFuelTankStats(50)
                .setExternalEntityType(ModEntities.EXTERNAL_FUEL_TANK.getId())
                .setEntityHitboxSize(0.8f, 0.8f)
                .build());
        addPresetToGenerate(PartBuilder.create(ModItems.HEAVY_EXTERNAL_FUEL_TANK.getId(), PartType.EXTERNAL_FUEL_TANK)
                .setCompatibleSlotType(SlotType.PYLON_MED)
                .setWeight(250)
                .setFuelTankStats(150)
                .setExternalEntityType(ModEntities.EXTERNAL_FUEL_TANK.getId())
                .setEntityHitboxSize(0.8f, 0.8f)
                .build());
		// ENGINES
        addPresetToGenerate(PartBuilder.create(ModItems.C6_ENGINE.getId(), PartType.INTERNAL_ENGINE)
                .setCompatibleSlotType(SlotType.SPIN_ENGINE)
                .setWeight(30)
                .setEngineStats(EngineType.SPIN, 60, 4, 0.005f)
                .build());
        addPresetToGenerate(PartBuilder.create(ModItems.C12_ENGINE.getId(), PartType.INTERNAL_ENGINE)
                .setCompatibleSlotType(SlotType.SPIN_ENGINE)
                .setWeight(70)
                .setEngineStats(EngineType.SPIN, 130, 8, 0.011f)
                .build());
        addPresetToGenerate(PartBuilder.create(ModItems.TURBOFAN_F25.getId(), PartType.INTERNAL_ENGINE)
                .setCompatibleSlotType(SlotType.PUSH_ENGINE)
                .setWeight(50)
                .setEngineStats(EngineType.PUSH, 180, 4, 0.005f)
                .build());
        addPresetToGenerate(PartBuilder.create(ModItems.TURBOFAN_F145.getId(), PartType.INTERNAL_ENGINE)
                .setCompatibleSlotType(SlotType.PUSH_ENGINE)
                .setWeight(110)
                .setEngineStats(EngineType.PUSH, 400, 8, 0.011f)
                .build());
        addPresetToGenerate(PartBuilder.create(ModItems.TURBOFAN_F39.getId(), PartType.INTERNAL_ENGINE)
                .setCompatibleSlotType(SlotType.PUSH_ENGINE)
                .setWeight(80)
                .setEngineStats(EngineType.PUSH, 340, 5, 0.007f)
                .build());
        addPresetToGenerate(PartBuilder.create(ModItems.KLIMOV_RD33.getId(), PartType.INTERNAL_ENGINE)
                .setCompatibleSlotType(SlotType.PUSH_ENGINE)
                .setWeight(60)
                .setEngineStats(EngineType.PUSH, 225, 6.5f, 0.006f)
                .build());
        addPresetToGenerate(PartBuilder.create(ModItems.CM_MANLY_52.getId(), PartType.INTERNAL_ENGINE)
                .setCompatibleSlotType(SlotType.RADIAL_ENGINE)
                .setWeight(20)
                .setEngineStats(EngineType.PUSH, 40, 2, 0.004f)
                .build());
        addPresetToGenerate(PartBuilder.create(ModItems.ALLISON_V_1710.getId(), PartType.INTERNAL_ENGINE)
                .setCompatibleSlotType(SlotType.RADIAL_ENGINE)
                .setWeight(30)
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
				.setExternalEntityType(ModEntities.EXTERNAL_ENGINE.getId())
				.setEntityHitboxSize(0.8f, 0.8f)
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
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "ewr4000", ModItems.AXCEL_TRUCK_RADAR.getId(), PartType.EXTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.MOUNT_TECH)
				.setWeight(1000)
				.setRadarStats("ewr4000")
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.AIR_SCAN_A.getId(), PartType.EXTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.MOUNT_TECH)
				.setWeight(1500)
				.setRadarStats("air_scan_a")
				.setExternalEntityType(ModEntities.EXTERNAL_RADAR.getId())
				.setEntityHitboxSize(1.0f, 1.0f)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.AIR_SCAN_B.getId(), PartType.EXTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.MOUNT_TECH)
				.setWeight(1500)
				.setRadarStats("air_scan_b")
				.setExternalEntityType(ModEntities.EXTERNAL_RADAR.getId())
				.setEntityHitboxSize(1.0f, 1.0f)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.SURVEY_ALL_A.getId(), PartType.EXTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.MOUNT_TECH)
				.setWeight(1500)
				.setRadarStats("survey_all_a")
				.setExternalEntityType(ModEntities.EXTERNAL_RADAR.getId())
				.setEntityHitboxSize(1.0f, 0.5f)
				.build());
		addPresetToGenerate(PartBuilder.create(ModItems.SURVEY_ALL_B.getId(), PartType.EXTERNAL_RADAR)
				.setCompatibleSlotType(SlotType.MOUNT_TECH)
				.setWeight(1500)
				.setRadarStats("survey_all_b")
				.setExternalEntityType(ModEntities.EXTERNAL_RADAR.getId())
				.setEntityHitboxSize(1.0f, 1.0f)
				.build());
		// SEAT
		addPresetToGenerate(PartBuilder.create(ModItems.SEAT.getId(), PartType.SEAT)
				.setCompatibleSlotType(SlotType.SEAT)
				.setWeight(30)
				.setExternalEntityType(ModEntities.SEAT.getId())
				.build());
		// TURRET
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "aa_turret", ModItems.TURRET.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_LIGHT)
				.setWeight(1000)
				.setTurretStats(750, 40, new Vec3(0, 0.5, 0), 1.03125,
						TurretStats.RotBounds.create(3.0f, 40f, 30f),
						1.0f, 1.25f)
				.addIngredient("dscombat:seat", 1)
				.addIngredientTag("dscombat:aluminum_ingot", 6)
				.addIngredient("minecraft:dispenser", 2)
				.addRepairCost("minecraft:iron_ingot", 3)
				.setSortFactor(7)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "minigun_turret", ModItems.TURRET.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_LIGHT)
				.setWeight(1500)
				.setTurretStats(1000, 40, Vec3.ZERO, 0.8,
						TurretStats.RotBounds.create(2.5f,50f, 50f),
						1.0f, 1.5f)
				.addIngredient("dscombat:seat", 1)
				.addIngredient("minecraft:dispenser", 1)
				.addIngredientTag("dscombat:aluminum_ingot", 12)
				.addRepairCost("minecraft:iron_ingot", 4)
				.setSortFactor(8)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "ciws", ModItems.TURRET.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_LIGHT)
				.setWeight(2500)
				.setTurretStats(500, 80, new Vec3(1, 0.5, 0), 0.6875,
						TurretStats.RotBounds.create(2.0f, 75f, 30f),
						1.5f, 2.5f)
				.addIngredient("minecraft:dispenser", 1)
				.addIngredientTag("dscombat:aluminum_ingot", 16)
				.addIngredient("minecraft:gold_ingot", 4)
				.addRepairCost("minecraft:iron_ingot", 5)
				.setSortFactor(9)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "mark45_cannon", ModItems.TURRET.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_LIGHT)
				.setWeight(3000)
				.setTurretStats(50, 120, new Vec3(0, 1.5, 0), 1.5625,
						TurretStats.RotBounds.create(1.1f, 45f, 15f),
						2.0f, 1.5f)
				.addIngredient("dscombat:seat", 1)
				.addIngredientTag("dscombat:aluminum_ingot", 24)
				.addIngredient("minecraft:iron_ingot", 2)
				.addRepairCost("minecraft:iron_ingot", 6)
				.setSortFactor(10)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "heavy_tank_turret", ModItems.TURRET.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_MED)
				.setWeight(4000)
				.setTurretStats(16, 120, Vec3.ZERO, 0.3,
						TurretStats.RotBounds.create(1.0f, 30f, 30f),
						2.0f, 1.0f)
				.addIngredient("dscombat:seat", 1)
				.addIngredientTag("dscombat:aluminum_ingot", 40)
				.addIngredient("minecraft:tnt", 2)
				.addRepairCost("minecraft:iron_ingot", 7)
				.setSortFactor(11)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "mark7_cannon", ModItems.TURRET.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_SUPER_HEAVY)
				.setWeight(4500)
				.setTurretStats(36, 200, new Vec3(0, 1.5, 0), 1.625,
						TurretStats.RotBounds.create(0.9f, 30f, 15f),
						EntityTurret.ShootType.MARK7, 4.0f, 1.7f)
				.addIngredient("dscombat:seat", 1)
				.addIngredientTag("dscombat:aluminum_ingot", 64)
				.addIngredient("minecraft:iron_ingot", 56)
				.addIngredient("minecraft:tnt", 8)
				.addRepairCost("minecraft:iron_ingot", 12)
				.setSortFactor(13)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "steve_up_smash", ModItems.TURRET.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_MED)
				.setWeight(5000)
				.setTurretStats(4, 40, Vec3.ZERO, 3.2,
						TurretStats.RotBounds.create(1.8f, 25f, 25f),
						1.0f, 3.5f)
				.addIngredient("dscombat:seat", 1)
				.addIngredientTag("dscombat:aluminum_ingot", 7)
				.addIngredient("minecraft:magma_block", 1)
				.addRepairCost("minecraft:iron_ingot", 8)
				.setSortFactor(14)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "sam_launcher", ModItems.TURRET.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_HEAVY)
				.setWeight(6000)
				.setTurretStats(4, 60, new Vec3(1.2, 0.4, 0), 2.7,
						TurretStats.RotBounds.create(1.3f, 25f, 25f),
						2.0f, 3.0f)
				.addIngredient("dscombat:seat", 1)
				.addIngredientTag("dscombat:aluminum_ingot", 16)
				.addRepairCost("minecraft:iron_ingot", 9)
				.setSortFactor(17)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "torpedo_tubes", ModItems.TURRET.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_MED)
				.setWeight(5500)
				.setTurretStats(3, 60, new Vec3(0, 1.8, 0), 1,
						TurretStats.RotBounds.create(1.6f, 5f, 5f),
						2.5f, 2.0f)
				.addIngredient("dscombat:seat", 1)
				.addIngredientTag("dscombat:aluminum_ingot", 15)
				.addRepairCost("minecraft:iron_ingot", 8)
				.setSortFactor(16)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "mls", ModItems.TURRET.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_HEAVY)
				.setWeight(6000)
				.setTurretStats(4, 60, new Vec3(1.2, 0, 0), 1,
						TurretStats.RotBounds.create(1.9f, 20f, 20f),
						2.0f, 2.5f)
				.addIngredient("dscombat:seat", 1)
				.addIngredientTag("dscombat:aluminum_ingot", 17)
				.addRepairCost("minecraft:iron_ingot", 9)
				.setSortFactor(15)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "mlrs", ModItems.TURRET.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_SUPER_HEAVY)
				.setWeight(8000)
				.setTurretStats(10, 80, new Vec3(1.8, 0, 0), 2,
						TurretStats.RotBounds.create(0.8f, 35f, 0),
						2.0f, 2.0f)
				.addIngredient("dscombat:seat", 1)
				.addIngredientTag("dscombat:aluminum_ingot", 60)
				.addIngredient("minecraft:iron_ingot", 60)
				.addRepairCost("minecraft:iron_ingot", 15)
				.setSortFactor(18)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "artillery_cannon", ModItems.TURRET.getId(), PartType.TURRENT)
				.setCompatibleSlotType(SlotType.MOUNT_SUPER_HEAVY)
				.setWeight(6000)
				.setTurretStats(50, 180, new Vec3(0, 1.2, 0), 0.79,
						TurretStats.RotBounds.create(1.0f, 35f, 15f),
						2.0f, 1.5f)
				.addIngredient("dscombat:seat", 1)
				.addIngredientTag("dscombat:aluminum_ingot", 50)
				.addIngredient("minecraft:iron_ingot", 2)
				.addIngredient("minecraft:tnt", 2)
				.addRepairCost("minecraft:iron_ingot", 15)
				.setSortFactor(12)
				.build());
		// EXTERNAL WEAPONS
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "xm12", ModItems.EXTERNAL_WEAPON_PART.getId(), PartType.EXTERNAL_WEAPON)
				.setCompatibleSlotType(SlotType.PYLON_LIGHT)
				.setWeight(101)
				.setExternalWeaponStats(300, 0)
				.setSortFactor(1)
				.addIngredientTag("dscombat:aluminum_ingot", 3)
				.addIngredient("minecraft:dispenser", 1)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "light_missile_rack", ModItems.EXTERNAL_WEAPON_PART.getId(), PartType.EXTERNAL_WEAPON)
				.setCompatibleSlotType(SlotType.PYLON_LIGHT)
				.setWeight(15)
				.setExternalWeaponStats(4, 0)
				.setSortFactor(2)
				.addIngredientTag("dscombat:aluminum_ingot", 5)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "heavy_missile_rack", ModItems.EXTERNAL_WEAPON_PART.getId(), PartType.EXTERNAL_WEAPON)
				.setCompatibleSlotType(SlotType.PYLON_MED)
				.setWeight(30)
				.setExternalWeaponStats(2, 0)
				.setSortFactor(3)
				.addIngredientTag("dscombat:aluminum_ingot", 10)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "bomb_rack", ModItems.EXTERNAL_WEAPON_PART.getId(), PartType.EXTERNAL_WEAPON)
				.setCompatibleSlotType(SlotType.PYLON_HEAVY)
				.setWeight(40)
				.setExternalWeaponStats(16, 0)
				.setSortFactor(4)
				.addIngredientTag("dscombat:aluminum_ingot", 15)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "adl", ModItems.EXTERNAL_WEAPON_PART.getId(), PartType.EXTERNAL_WEAPON)
				.setCompatibleSlotType(SlotType.MOUNT_HEAVY)
				.setWeight(6000)
				.setExternalWeaponStats(3, 20)
				.setSortFactor(5)
				.addIngredientTag("dscombat:aluminum_ingot", 10)
				.addIngredient("minecraft:dispenser", 3)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID, "vls", ModItems.EXTERNAL_WEAPON_PART.getId(), PartType.EXTERNAL_WEAPON)
				.setCompatibleSlotType(SlotType.MOUNT_HEAVY)
				.setWeight(6000)
				.setExternalWeaponStats(16, 90)
				.setSortFactor(6)
				.addIngredient("dscombat:ti83", 1)
				.addIngredient("minecraft:dispenser", 8)
				.addIngredientTag("dscombat:aluminum_ingot", 10)
				.build());
		// INTERNAL WEAPONS
		addPresetToGenerate(PartBuilder.create(ModItems.INTERNAL_GUN.getId(), PartType.INTERNAL_WEAPON)
				.setCompatibleSlotType(SlotType.INTERNAL_GUN)
				.setWeight(1000)
				.setWeaponStats(1000)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID,"gau_avenger", ModItems.INTERNAL_GUN.getId(), PartType.INTERNAL_WEAPON)
				.setCompatibleSlotType(SlotType.INTERNAL_GUN)
				.setWeight(281)
				.setWeaponStats(1000)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID,"m61a1_vulcan", ModItems.INTERNAL_GUN.getId(), PartType.INTERNAL_WEAPON)
				.setCompatibleSlotType(SlotType.INTERNAL_GUN)
				.setWeight(112)
				.setWeaponStats(500)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID,"gsh_30_1", ModItems.INTERNAL_GUN.getId(), PartType.INTERNAL_WEAPON)
				.setCompatibleSlotType(SlotType.INTERNAL_GUN)
				.setWeight(46)
				.setWeaponStats(200)
				.build());
		addPresetToGenerate(PartBuilder.create(DSCombatMod.MODID,"m2_browning", ModItems.INTERNAL_GUN.getId(), PartType.INTERNAL_WEAPON)
				.setCompatibleSlotType(SlotType.INTERNAL_GUN)
				.setWeight(38)
				.setWeaponStats(300)
				.build());
		// DISPENSERS
		addPresetToGenerate(PartBuilder.create(ModItems.BASIC_FLARE_DISPENSER.getId(), PartType.FLARE_DISPENSER)
				.setCompatibleSlotType(SlotType.INTERNAL)
				.setWeight(100)
				.setFlareDispenserStats(20, 120, 20)
				.build());
	}
	
	public PartPresetGenerator(DataGenerator output) {
		super(output, "parts");
		INSTANCE = this;
	}

	@Override
	public String getName() {
		return "Parts: "+DSCombatMod.MODID;
	}

}
