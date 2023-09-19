package com.onewhohears.dscombat.data.aircraft.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft.AircraftType;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.world.item.DyeColor;

public class JaviPresets {
	
	public static final AircraftPreset EMPTY_JAVI_PLANE = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "javi_plane_empty")
			.setAircraftType(AircraftType.PLANE)
			.setItem(ModItems.JAVI_PLANE.getId())
			.setMaxHealth(200f)
			.setMass(25f)
			.setMaxSpeed(1.20f)
			.setStealth(1.1f)
			.setCrossSecArea(6.25f)
			.setIdleHeat(5f)
			.setBaseArmor(9f)
			.setTurnRadius(12f)
			.setMaxTurnRates(4f, 2.1f, 1.1f)
			.setTurnTorques(1.5f, 2.5f, 4.5f)
			.setThrottleRate(0.04f, 0.08f)
			.setPlaneWingArea(18f)
			.setDefaultColor(DyeColor.GREEN)
			.addIngredient(ModItems.FUSELAGE.getId(), 2)
			.addIngredient(ModItems.LARGE_WING.getId(), 2)
			.addIngredient(ModItems.COCKPIT.getId())
			.addIngredient(ModItems.SEAT.getId())
			.addIngredient("minecraft:gold_ingot", 10)
			.addPilotSeatSlot(0, -0.55, 3.35)
			.addSeatSlot("seat2", SlotType.MED_TURRET, 0, -0.55, 2.35)
			.addEmptySlot("left_wing_1", SlotType.WING, 1.5, -0.3, 0, 180)
			.addEmptySlot("left_wing_2", SlotType.WING, 3.0, -0.2, 0, 180)
			.addEmptySlot("left_wing_3", SlotType.WING, 4.0, -0.07, 0, 180) 
			.addEmptySlot("left_wing_4", SlotType.WING, 5.0, -0.05, 0, 180) 
			.addEmptySlot("right_wing_1", SlotType.WING, -1.5, -0.3, 0, 180)
			.addEmptySlot("right_wing_2", SlotType.WING, -3.0, -0.2, 0, 180)
			.addEmptySlot("right_wing_3", SlotType.WING, -4.0, -0.07, 0, 180) 
			.addEmptySlot("right_wing_4", SlotType.WING, -5.0, -0.05, 0, 180) 
			.addEmptySlot("frame_1", SlotType.FRAME, 0, -0.7, 3, 180)
			.addEmptySlot("frame_2", SlotType.FRAME, 0, -0.7, 0, 180)
			.addEmptySlot("internal_1", SlotType.PUSH_ENGINE)
			.addEmptySlot("internal_2", SlotType.PUSH_ENGINE)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_5", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_6", SlotType.ADVANCED_INTERNAL)
			.build();
	
	public static final AircraftPreset UNARMED_JAVI_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "javi_plane_unarmed", EMPTY_JAVI_PLANE)
			.setCraftable()
			.setSlotItem("internal_1", ModItems.TURBOFAN_F25.getId())
			.setSlotItem("internal_2", ModItems.TURBOFAN_F25.getId())
			.setSlotItem("internal_3", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.setSlotItem("internal_4", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addIngredient(ModItems.TURBOFAN_F25.getId(), 2)
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.build();
	
	public static final AircraftPreset DEFAULT_JAVI_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "javi_plane", UNARMED_JAVI_PLANE)
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm84e", true)
			.setSlotItem("left_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "rifel1", true)
			.setSlotItem("right_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9p5", true)
			.setSlotItem("frame_1", ModItems.XM12.getId(), "50mmhe", true)
			.setSlotItem("frame_2", ModItems.BOMB_RACK.getId(), "anm57", true)
			.setSlotItem("internal_5", ModItems.GR400.getId())
			.addIngredient(ModItems.GR400.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.BOMB_RACK.getId())
			.addIngredient(ModItems.XM12.getId())
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient("minecraft:iron_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.build();
	
	public static final AircraftPreset BOMBER_JAVI_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "javi_plane_bomber", UNARMED_JAVI_PLANE)
			.setSlotItem("left_wing_1", ModItems.BOMB_RACK.getId(), "anm30", true)
			.setSlotItem("left_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "agm84e", true)
			.setSlotItem("left_wing_3", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.setSlotItem("left_wing_4", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9l", true)
			.setSlotItem("right_wing_1", ModItems.BOMB_RACK.getId(), "anm64", true)
			.setSlotItem("right_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65g", true)
			.setSlotItem("right_wing_3", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65l", true)
			.setSlotItem("frame_1", ModItems.XM12.getId(), "50mmhe", true)
			.setSlotItem("frame_2", ModItems.HEAVY_MISSILE_RACK.getId(), "rifel1", true)
			.setSlotItem("internal_5", ModItems.GPR20.getId())
			.addIngredient(ModItems.GPR20.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 4)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.BOMB_RACK.getId(), 2)
			.addIngredient(ModItems.XM12.getId())
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient("minecraft:iron_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.build();
	
	public static final AircraftPreset TRUCK_JAVI_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "javi_plane_truck", UNARMED_JAVI_PLANE)
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7mh", true)
			.setSlotItem("left_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7f", true)
			.setSlotItem("left_wing_3", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120c", true)
			.setSlotItem("left_wing_4", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7mh", true)
			.setSlotItem("right_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7f", true)
			.setSlotItem("right_wing_3", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120c", true)
			.setSlotItem("right_wing_4", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("frame_1", ModItems.XM12.getId(), "20mm", true)
			.setSlotItem("frame_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9p5", true)
			.setSlotItem("internal_5", ModItems.AR2K.getId())
			.addIngredient(ModItems.AR2K.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 8)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 1)
			.addIngredient(ModItems.XM12.getId())
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient("minecraft:iron_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.build();
	
}
