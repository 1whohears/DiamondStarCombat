package com.onewhohears.dscombat.data.vehicle.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.SlotType;
import com.onewhohears.dscombat.data.vehicle.VehicleSoundManager.PassengerSoundPack;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;

public class KraitChopperPresets {
	
	public static final VehicleStats EMPTY_KRAIT_CHOPPER = VehicleStats.Builder
			.createHelicopter(DSCombatMod.MODID, "krait_chopper_empty")
			.setAssetId("krait_chopper")
			.setSortFactor(4)
			.setItem(ModItems.KRAIT_CHOPPER.getId())
			.setMaxHealth(120f)
			.setBaseArmor(60f)
			.setArmorDamageThreshold(4f)
			.setArmorAbsorbtionPercent(0.25f)
			.setMass(8000f)
			.setMaxSpeed(0.85f)
			.setStealth(1.2f)
			.setCrossSecArea(8f)
			.setIdleHeat(8f)
			.setMaxAltitude(430)
			.setTurnRadius(0f)
			.setMaxTurnRates(4f, 2f, 4f)
			.setTurnTorques(1f, 1f, 2.5f)
			.setThrottleRate(0.01f, 0.02f)
			.setHeliHoverMovement(0.04f, 0.02f)
			.setBasicEngineSounds(ModSounds.HELI_1, ModSounds.HELI_1)
			.setRotationalInertia(8, 6, 4)
			.setCrashExplosionRadius(4)
			.set3rdPersonCamDist(6)
			.setHeliAlwaysLandingGear(true)
			.setHeliLiftFactor(15)
			.setDefultPassengerSoundPack(PassengerSoundPack.ENG_NON_BINARY_GOOBER)
			.setBaseTextureNum(4)
			.setLayerTextureNum(4)
			.addPilotSeatSlot(0, -0.05, 2, true)
			.addSeatSlot("copilot_seat", 0, -0.4, 3.5)
			.addEmptySlot("nose_gun", SlotType.PYLON_LIGHT, 0, -0.5, 3.75, 180)
			.addEmptySlot("left_wing_1", SlotType.PYLON_HEAVY, 1.15, -0.15, 0, 180)
			.addEmptySlot("left_wing_2", SlotType.PYLON_MED, 2.08, 0.15, 0, -90)
			.addEmptySlot("right_wing_1", SlotType.PYLON_HEAVY, -1.15, -0.15, 0, 180)
			.addEmptySlot("right_wing_2", SlotType.PYLON_MED, -2.08, 0.15, 0, 90)
			.addEmptySlot("internal_1", SlotType.SPIN_ENGINE, "engine")
			.addEmptySlot("internal_2", SlotType.SPIN_ENGINE, "engine")
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.INTERNAL)
			.addEmptySlot("internal_5", SlotType.TECH_INTERNAL, "nose")
			.addEmptySlot("internal_6", SlotType.TECH_INTERNAL, "nose")
			.addEmptySlot("internal_7", SlotType.TECH_INTERNAL, "nose")
			.addEmptySlot("internal_8", SlotType.TECH_INTERNAL, "nose")
			.addIngredient(ModItems.FUSELAGE.getId())
			.addIngredient(ModItems.LARGE_PROPELLER.getId())
			.addIngredient(ModItems.PROPELLER.getId())
			.addIngredient(ModItems.ADVANCED_COCKPIT.getId(), 2)
			.addIngredient("minecraft:gold_ingot", 12)
			.setEntityMainHitboxSize(2.8f, 2.8f)
			.setRootHitboxNoCollide(true)
			.addRotableHitbox("base", 1.1, 1.5, 4, 0, -0.05, -0.6,
					0, 0, false, false, true)
			.addRotableHitbox("tail", 0.7, 0.7, 6.5, 0, 0.08, -5.75,
					20, 20, true, true, false)
			.addRotableHitbox("engine", 1.1, 1, 4, 0, 1.2, -0.6,
					30, 30, true, true, false)
			.addRotableHitbox("nose", 1, 1.3, 3, 0, -0.2, 2.9,
					20, 20, true, true, false)
			.setHitboxesControlYaw("tail")
			.setHitboxesControlRoll("tail")
			.setHitboxesControlPitch("tail")
			.build();
	
	public static final VehicleStats UNARMED_KRAIT_CHOPPER = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "krait_chopper_unarmed", EMPTY_KRAIT_CHOPPER)
			.setCraftable()
			.setSlotItem("internal_1", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_2", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_3", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.addIngredient(ModItems.C12_ENGINE.getId(), 2)
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.build();
	
	public static final VehicleStats DEFAULT_KRAIT_CHOPPER = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "krait_chopper", UNARMED_KRAIT_CHOPPER)
			.setSlotItem("nose_gun", ModItems.XM12.getId(), "20mmhe", true)
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm84e", true)
			.setSlotItem("left_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65l", true)
			.setSlotItem("right_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9l", true)
			.setSlotItem("internal_5", ModItems.AR500.getId())
			.setSlotItem("internal_6", ModItems.GR400.getId())
			.setSlotItem("internal_4", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addIngredient(ModItems.AR500.getId())
			.addIngredient(ModItems.GR400.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient("dscombat:aluminum_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.build();

	public static final VehicleStats DOG_FIGHT_KRAIT_CHOPPER = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "krait_chopper_dog_fighter", UNARMED_KRAIT_CHOPPER)
			.setSlotItem("nose_gun", ModItems.XM12.getId(), "20mmhe", true)
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("left_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9p5", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("right_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9p5", true)
			.setSlotItem("internal_5", ModItems.AR1K.getId())
			.setSlotItem("internal_6", ModItems.GR400.getId())
			.setSlotItem("internal_4", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addIngredient(ModItems.AR1K.getId())
			.addIngredient(ModItems.GR400.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient("dscombat:aluminum_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.build();
}
