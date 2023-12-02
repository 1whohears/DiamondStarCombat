package com.onewhohears.dscombat.data.aircraft.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle.AircraftType;
import com.onewhohears.dscombat.init.ModItems;

public class AlexisPresets {
	
	public static final AircraftPreset EMPTY_ALEXIS_PLANE = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "alexis_plane_empty")
			.setSortFactor(10)
			.setAircraftType(AircraftType.PLANE)
			.setItem(ModItems.ALEXIS_PLANE.getId())
			.setMaxHealth(150f)
			.setMass(20f)
			.setMaxSpeed(1.45f)
			.setStealth(0.8f)
			.setCrossSecArea(4f)
			.setIdleHeat(4f)
			.setBaseArmor(2f)
			.setTurnRadius(10f)
			.setMaxTurnRates(6f, 2.5f, 1.5f)
			.setTurnTorques(1f, 2f, 4f)
			.setThrottleRate(0.04f, 0.08f)
			.setPlaneWingArea(14f)
			.addIngredient(ModItems.FUSELAGE.getId(), 1)
			.addIngredient(ModItems.WING.getId(), 2)
			.addIngredient(ModItems.ADVANCED_COCKPIT.getId())
			.addIngredient(ModItems.WHEEL.getId(), 3)
			.addIngredient("minecraft:light_gray_dye", 5)
			.addIngredient("minecraft:gold_ingot", 20)
			.addPilotSeatSlot(0, 0.1, 4.5)
			.addEmptySlot("left_wing_1", SlotType.WING, 2.3, -0.08, -2.1, 180)
			.addEmptySlot("left_wing_2", SlotType.WING, 3.6, -0.08, -2.7, 180)
			.addEmptySlot("left_wing_3", SlotType.WING, 4.95, -0.08, -3.1, 180)
			.addEmptySlot("left_wing_4", SlotType.WING, 6.05, 0.4, -3.5, -90)
			.addEmptySlot("right_wing_1", SlotType.WING, -2.3, -0.08, -2.1, 180)
			.addEmptySlot("right_wing_2", SlotType.WING, -3.6, -0.08, -2.7, 180)
			.addEmptySlot("right_wing_3", SlotType.WING, -4.95, -0.08, -3.1, 180)
			.addEmptySlot("right_wing_4", SlotType.WING, -6.05, 0.4, -3.5, 90)
			.addEmptySlot("frame_1", SlotType.FRAME, 0, -0.15, 6, 180)
			.addEmptySlot("internal_1", SlotType.PUSH_ENGINE)
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_5", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_6", SlotType.ADVANCED_INTERNAL)
			.build();
	
	public static final AircraftPreset UNARMED_ALEXIS_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "alexis_plane_unarmed", EMPTY_ALEXIS_PLANE)
			.setCraftable()
			.setSlotItem("internal_1", ModItems.TURBOFAN_F145.getId())
			.setSlotItem("internal_2", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.addIngredient(ModItems.TURBOFAN_F145.getId())
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.build();
	
	public static final AircraftPreset DEFAULT_ALEXIS_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "alexis_plane", UNARMED_ALEXIS_PLANE)
			// actual weapon costs: 63 copper, 88 iron, 130 gunpowder, 140 redstone
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("left_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120c", true)
			.setSlotItem("right_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
			.setSlotItem("frame_1", ModItems.XM12.getId(), "20mm", true)
			.setSlotItem("internal_4", ModItems.AR2K.getId())
			.setSlotItem("internal_3", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addIngredient(ModItems.AR2K.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.XM12.getId())
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.addIngredient("minecraft:copper_ingot", 63)
			.addIngredient("minecraft:iron_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.build();
	
	public static final AircraftPreset SUPPORT_ALEXIS_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "alexis_plane_support", UNARMED_ALEXIS_PLANE)
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm84e", true)
			.setSlotItem("left_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "rifel1", true)
			.setSlotItem("right_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.setSlotItem("frame_1", ModItems.XM12.getId(), "50mmhe", true)
			.setSlotItem("internal_4", ModItems.AR2K.getId())
			.setSlotItem("internal_5", ModItems.GR400.getId())
			.setSlotItem("internal_3", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addIngredient(ModItems.AR2K.getId())
			.addIngredient(ModItems.GR400.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 3)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 1)
			.addIngredient(ModItems.XM12.getId())
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient("minecraft:iron_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.build();
	
	public static final AircraftPreset SNIPER_ALEXIS_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "alexis_plane_sniper", UNARMED_ALEXIS_PLANE)
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7mh", true)
			.setSlotItem("left_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120c", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7f", true)
			.setSlotItem("right_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("frame_1", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
			.setSlotItem("internal_4", ModItems.AR2K.getId())
			.setSlotItem("internal_3", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addIngredient(ModItems.AR2K.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 4)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 1)
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient("minecraft:iron_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.build();
	
}
