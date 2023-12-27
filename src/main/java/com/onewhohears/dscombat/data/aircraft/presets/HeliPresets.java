package com.onewhohears.dscombat.data.aircraft.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle.AircraftType;
import com.onewhohears.dscombat.init.ModItems;

public class HeliPresets {
	
	public static final AircraftPreset EMPTY_NOAH_CHOPPER = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "noah_chopper_empty")
			.setSortFactor(4)
			.setAircraftType(AircraftType.HELICOPTER)
			.setItem(ModItems.NOAH_CHOPPER.getId())
			.setMaxHealth(200f)
			.setMass(6000f)
			.setMaxSpeed(0.7f)
			.setStealth(1.2f)
			.setCrossSecArea(8f)
			.setIdleHeat(8f)
			.setBaseArmor(5f)
			.setTurnRadius(0f)
			.setMaxTurnRates(4f, 2f, 4f)
			.setTurnTorques(1f, 1f, 2.5f)
			.setThrottleRate(0.01f, 0.02f)
			.setHeliHoverMovement(0.04f, 0.02f)
			.addPilotSeatSlot(0.4, -0.65, 1.5)
			.addSeatSlot("seat2", SlotType.MED_TURRET, -0.4, -0.65, 1.5)
			.addSeatSlot("seat3", SlotType.MED_TURRET, 0.4, -0.65, 0.3)
			.addSeatSlot("seat4", SlotType.MED_TURRET, -0.4, -0.65, 0.3)
			.addEmptySlot("left_wing_1", SlotType.WING, 0.75, -0.5, 1.8, -90)
			.addEmptySlot("left_wing_2", SlotType.WING, 0.75, -0.5, 0.9, -90)
			.addEmptySlot("left_wing_3", SlotType.WING, 0.75, -0.5, 0, -90)
			.addEmptySlot("left_wing_4", SlotType.WING, 0.65, 0.5, 0, -90)
			.addEmptySlot("right_wing_1", SlotType.WING, -0.75, -0.5, 1.8, 90)
			.addEmptySlot("right_wing_2", SlotType.WING, -0.75, -0.5, 0.9, 90)
			.addEmptySlot("right_wing_3", SlotType.WING, -0.75, -0.5, 0, 90)
			.addEmptySlot("right_wing_4", SlotType.WING, -0.65, 0.5, 0, 90)
			.addEmptySlot("frame_1", SlotType.FRAME, 0, -0.67, 1.9, 180)
			.addEmptySlot("frame_2", SlotType.FRAME, 0, -0.67, 1.1, 180)
			.addEmptySlot("frame_3", SlotType.FRAME, 0, -0.67, 0.3, 180)
			.addEmptySlot("frame_4", SlotType.FRAME, 0, -0.67, -0.5, 180)
			.addEmptySlot("internal_1", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_2", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.INTERNAL)
			.addEmptySlot("internal_5", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_6", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_7", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_8", SlotType.ADVANCED_INTERNAL)
			.addIngredient(ModItems.FUSELAGE.getId())
			.addIngredient(ModItems.LARGE_PROPELLER.getId(), 2)
			.addIngredient(ModItems.PROPELLER.getId())
			.addIngredient(ModItems.COCKPIT.getId())
			.addIngredient(ModItems.SEAT.getId(), 3)
			.addIngredient("minecraft:green_dye", 4)
			.addIngredient("minecraft:gold_ingot", 15)
			.addIngredient("iron_ingot", 4)
			.build();
	
	public static final AircraftPreset UNARMED_NOAH_CHOPPER = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "noah_chopper_unarmed", EMPTY_NOAH_CHOPPER)
			.setCraftable()
			.setSlotItem("internal_1", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_2", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_3", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.addIngredient(ModItems.C12_ENGINE.getId(), 2)
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.build();
	
	public static final AircraftPreset DEFAULT_NOAH_CHOPPER = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "noah_chopper", UNARMED_NOAH_CHOPPER)
			.setSlotItem("seat2", ModItems.MINIGUN_TURRET.getId(), true)
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7f", true)
			.setSlotItem("left_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65g", true)
			.setSlotItem("left_wing_3", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65l", true)
			.setSlotItem("right_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "agm84e", true)
			.setSlotItem("right_wing_3", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9l", true)
			.setSlotItem("frame_1", ModItems.GIMBAL_CAMERA.getId())
			.setSlotItem("frame_2", ModItems.HEAVY_MISSILE_RACK.getId(), "agm88g", true)
			.setSlotItem("internal_5", ModItems.AR500.getId())
			.setSlotItem("internal_6", ModItems.GR400.getId())
			.setSlotItem("internal_4", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addIngredient(ModItems.AR500.getId())
			.addIngredient(ModItems.GR400.getId())
			.addIngredient(ModItems.MINIGUN_TURRET.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 5)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient("minecraft:iron_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.build();
	
}
