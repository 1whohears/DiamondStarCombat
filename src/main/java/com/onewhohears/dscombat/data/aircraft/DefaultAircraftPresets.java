package com.onewhohears.dscombat.data.aircraft;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft.AircraftType;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.world.item.DyeColor;

public class DefaultAircraftPresets {
	
	public static final AircraftPreset EMPTY_ALEXIS_PLANE = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "alexis_plane_empty")
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
			.setPlaneWingArea(12f)
			.setDefaultColor(DyeColor.WHITE)
			.addIngredient(ModItems.FUSELAGE.getId(), 2)
			.addIngredient(ModItems.WING.getId(), 2)
			.addIngredient(ModItems.ADVANCED_COCKPIT.getId())
			.addIngredient("minecraft:gold_ingot", 10)
			.addPilotSeatSlot(0, 0.50, 6.35)
			.addEmptySlot("left_wing_1", SlotType.WING, 2.3, 0.15, 0, 180)
			.addEmptySlot("left_wing_2", SlotType.WING, 4.5, 0.15, 0, 180)
			.addEmptySlot("right_wing_1", SlotType.WING, -2.3, 0.15, 0, 180)
			.addEmptySlot("right_wing_2", SlotType.WING, -4.5, 0.15, 0, 180)
			.addEmptySlot("frame_1", SlotType.FRAME, 0, -0.04, 8.5, 180)
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
			.setSlotItem("internal_3", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addIngredient(ModItems.TURBOFAN_F145.getId())
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.build();
	
	public static final AircraftPreset DEFAULT_ALEXIS_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "alexis_plane", UNARMED_ALEXIS_PLANE)
			.setCraftable()
			// actual weapon costs: 63 copper, 88 iron, 130 gunpowder, 140 redstone
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("left_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120c", true)
			.setSlotItem("right_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
			.setSlotItem("frame_1", ModItems.XM12.getId(), "20mm", true)
			.setSlotItem("internal_4", ModItems.AR2K.getId())
			.addIngredient(ModItems.AR2K.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.XM12.getId())
			.addIngredient("minecraft:copper_ingot", 63)
			.addIngredient("minecraft:iron_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.build();
	
	public static final AircraftPreset SUPPORT_ALEXIS_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "alexis_plane_support", UNARMED_ALEXIS_PLANE)
			.setCraftable()
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm84e", true)
			.setSlotItem("left_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "rifel1", true)
			.setSlotItem("right_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.setSlotItem("frame_1", ModItems.XM12.getId(), "50mmhe", true)
			.setSlotItem("internal_4", ModItems.AR2K.getId())
			.setSlotItem("internal_5", ModItems.GR400.getId())
			.addIngredient(ModItems.AR2K.getId())
			.addIngredient(ModItems.GR400.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 3)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 1)
			.addIngredient(ModItems.XM12.getId())
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient("minecraft:iron_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.build();
	
	public static final AircraftPreset SNIPER_ALEXIS_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "alexis_plane_sniper", UNARMED_ALEXIS_PLANE)
			.setCraftable()
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7mh", true)
			.setSlotItem("left_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120c", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7f", true)
			.setSlotItem("right_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("frame_1", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
			.setSlotItem("internal_4", ModItems.AR2K.getId())
			.addIngredient(ModItems.AR2K.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 4)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 1)
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient("minecraft:iron_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.build();
	
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
			.setCraftable()
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
			.setCraftable()
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
			.setCraftable()
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
	
	public static final AircraftPreset EMPTY_NOAH_CHOPPER = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "noah_chopper_empty")
			.setAircraftType(AircraftType.HELICOPTER)
			.setItem(ModItems.NOAH_CHOPPER.getId())
			.setMaxHealth(300f)
			.setMass(30f)
			.setMaxSpeed(0.8f)
			.setStealth(1.2f)
			.setCrossSecArea(7.84f)
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
			.setDefaultColor(DyeColor.BROWN)
			.addIngredient(ModItems.FUSELAGE.getId())
			.addIngredient(ModItems.LARGE_PROPELLER.getId(), 2)
			.addIngredient(ModItems.PROPELLER.getId())
			.addIngredient(ModItems.COCKPIT.getId())
			.addIngredient(ModItems.SEAT.getId(), 3)
			.addIngredient("minecraft:gold_ingot", 15)
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
			.setCraftable()
			.setSlotItem("seat2", ModItems.MINIGUN_TURRET.getId())
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("left_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65g", true)
			.setSlotItem("left_wing_3", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65l", true)
			.setSlotItem("right_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "agm84e", true)
			.setSlotItem("right_wing_3", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9l", true)
			.setSlotItem("frame_1", ModItems.HEAVY_MISSILE_RACK.getId(), "rifel1", true)
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
	
	public static final AircraftPreset EMPTY_MRBUDGER_TANK = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "mrbudger_tank_empty")
			.setAircraftType(AircraftType.CAR)
			.setItem(ModItems.MRBUDGER_TANK.getId())
			.setMaxHealth(500f)
			.setMass(50f)
			.setMaxSpeed(0.3f)
			.setStealth(1.0f)
			.setCrossSecArea(7.5f)
			.setIdleHeat(10f)
			.setBaseArmor(16f)
			.setTurnRadius(0f)
			.setMaxTurnRates(0f, 0f, 2f)
			.setTurnTorques(0f, 0f, 8f)
			.setThrottleRate(0.04f, 0.04f)
			.setDefaultColor(DyeColor.YELLOW)
			.addIngredient("minecraft:minecart", 4)
			.addIngredient(ModItems.FUSELAGE.getId())
			.addIngredient(ModItems.SEAT.getId(), 4)
			.addIngredient("minecraft:gold_ingot", 5)
			.addSeatSlot(PartSlot.PILOT_SLOT_NAME, SlotType.HEAVY_TURRET, 0, 1.4, 0)
			.addSeatSlot("seat1", SlotType.HEAVY_TURRET, 1, 1.4, 2)
			.addSeatSlot("seat2", SlotType.HEAVY_TURRET, -1, 1.4, 2)
			.addSeatSlot("seat3", SlotType.HEAVY_TURRET, 1, 1.4, -2)
			.addSeatSlot("seat4", SlotType.HEAVY_TURRET, -1, 1.4, -2)
			.addEmptySlot("internal_1", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_2", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.INTERNAL)
			.build();
	
	public static final AircraftPreset UNARMED_MRBUDGER_TANK = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "mrbudger_tank_unarmed", EMPTY_MRBUDGER_TANK)
			.setCraftable()
			.setSlotItem("internal_1", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_3", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.addIngredient(ModItems.C12_ENGINE.getId(), 1)
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.build();
	
	public static final AircraftPreset DEFAULT_MRBUDGER_TANK = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "mrbudger_tank", UNARMED_MRBUDGER_TANK)
			.setCraftable()
			.setSlotItem(PartSlot.PILOT_SLOT_NAME, ModItems.HEAVY_TANK_TURRET.getId(), true)
			.addIngredient(ModItems.HEAVY_TANK_TURRET.getId())
			.build();
	
	public static final AircraftPreset EMPTY_SMALL_ROLLER = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "small_roller_empty")
			.setAircraftType(AircraftType.CAR)
			.setItem(ModItems.SMALL_ROLLER.getId())
			.setMaxHealth(30f)
			.setMass(5f)
			.setMaxSpeed(0.2f)
			.setStealth(1.0f)
			.setCrossSecArea(1.2f)
			.setIdleHeat(1f)
			.setBaseArmor(0f)
			.setTurnRadius(0f)
			.setMaxTurnRates(0f, 0f, 3f)
			.setTurnTorques(0f, 0f, 10f)
			.setThrottleRate(0.08f, 0.08f)
			.setDefaultColor(DyeColor.GRAY)
			.addIngredient("minecraft:minecart", 2)
			.addIngredient(ModItems.SEAT.getId(), 1)
			.addIngredient("minecraft:iron_ingot", 2)
			.addSeatSlot(PartSlot.PILOT_SLOT_NAME, SlotType.HEAVY_TURRET, 0, 0.6, 0)
			.addEmptySlot("internal_1", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.build();
	
	public static final AircraftPreset UNARMED_SMALL_ROLLER = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "small_roller_unarmed", EMPTY_SMALL_ROLLER)
			.setCraftable()
			.addIngredient(ModItems.C6_ENGINE.getId())
			.addIngredient(ModItems.LIGHT_FUEL_TANK.getId())
			.setSlotItem("internal_1", ModItems.C6_ENGINE.getId())
			.setSlotItem("internal_2", ModItems.LIGHT_FUEL_TANK.getId(), true)
			.build();
	
	public static final AircraftPreset DEFAULT_SMALL_ROLLER = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "small_roller", UNARMED_SMALL_ROLLER)
			.setCraftable()
			.setSlotItem(PartSlot.PILOT_SLOT_NAME, ModItems.STEVE_UP_SMASH.getId(), true)
			.addIngredient(ModItems.STEVE_UP_SMASH.getId())
			.build();
	
	public static final AircraftPreset TANK_SMALL_ROLLER = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "small_roller_tank", UNARMED_SMALL_ROLLER)
			.setCraftable()
			.setSlotItem(PartSlot.PILOT_SLOT_NAME, ModItems.HEAVY_TANK_TURRET.getId(), true)
			.addIngredient(ModItems.HEAVY_TANK_TURRET.getId())
			.build();
	
	public static final AircraftPreset EMPTY_NATHAN_BOAT = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "nathan_boat_empty")
			.setAircraftType(AircraftType.BOAT)
			.setItem(ModItems.NATHAN_BOAT.getId())
			.setMaxHealth(100f)
			.setMass(10f)
			.setMaxSpeed(0.85f)
			.setStealth(1.0f)
			.setCrossSecArea(4.5f)
			.setIdleHeat(2f)
			.setBaseArmor(1f)
			.setTurnRadius(0f)
			.setMaxTurnRates(0f, 0f, 3.5f)
			.setTurnTorques(0f, 0f, 2f)
			.setThrottleRate(0.05f, 0.10f)
			.setDefaultColor(DyeColor.WHITE)
			.addIngredient("minecraft:oak_boat", 4)
			.addIngredient("minecraft:iron_ingot", 10)
			.addIngredient(ModItems.SEAT.getId(), 7)
			.addPilotSeatSlot(0.8, 1.2, -1.13)
			.addSeatSlot("seat1", SlotType.MED_TURRET, 0.65, 1.2, 2.44)
			.addSeatSlot("seat2", SlotType.MED_TURRET, -0.65, 1.2, 2.44)
			.addSeatSlot("seat3", SlotType.MED_TURRET, 0.65, 1.2, 1.25)
			.addSeatSlot("seat4", SlotType.MED_TURRET, -0.65, 1.2, 1.25)
			.addSeatSlot("seat5", SlotType.MED_TURRET, 0.65, 1.2, -0.06)
			.addSeatSlot("seat6", SlotType.MED_TURRET, -0.65, 1.2, -0.06)
			.addEmptySlot("frame_1", SlotType.FRAME, 1.5, 1, 0, -90)
			.addEmptySlot("frame_2", SlotType.FRAME, -1.5, 1, 0, 90)
			.addEmptySlot("internal_1", SlotType.PUSH_ENGINE)
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.ADVANCED_INTERNAL)
			.build();
	
	public static final AircraftPreset UNARMED_NATHAN_BOAT = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "nathan_boat_unarmed", EMPTY_NATHAN_BOAT)
			.setCraftable()
			.addIngredient(ModItems.TURBOFAN_F145.getId())
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.setSlotItem("internal_1", ModItems.TURBOFAN_F145.getId())
			.setSlotItem("internal_2", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.setSlotItem("internal_4", ModItems.WR400.getId())
			.build();
	
	public static final AircraftPreset DEFAULT_NATHAN_BOAT = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "nathan_boat", UNARMED_NATHAN_BOAT)
			.setCraftable()
			.addIngredient(ModItems.MINIGUN_TURRET.getId())
			.addIngredient(ModItems.WR400.getId())
			.setSlotItem("seat1", ModItems.MINIGUN_TURRET.getId())
			.setSlotItem("frame_1", ModItems.HEAVY_MISSILE_RACK.getId(), "torpedo1", true)
			.setSlotItem("frame_2", ModItems.HEAVY_MISSILE_RACK.getId(), "torpedo1", true)
			.addIngredient("minecraft:iron_ingot", 64)
			.addIngredient("minecraft:redstone", 24)
			.addIngredient("minecraft:gunpowder", 60)
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 2)
			.build();
	
	public static final AircraftPreset EMPTY_ANDOLF_SUB = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "andolf_sub_empty")
			.setAircraftType(AircraftType.SUBMARINE)
			.setItem(ModItems.ANDOLF_SUB.getId())
			.setMaxHealth(800f)
			.setMass(200f)
			.setMaxSpeed(0.5f)
			.setStealth(0.05f)
			.setCrossSecArea(18f)
			.setIdleHeat(10f)
			.setBaseArmor(20f)
			.setTurnRadius(0f)
			.setMaxTurnRates(2f, 2f, 2f)
			.setTurnTorques(1f, 1f, 1f)
			.setThrottleRate(0.04f, 0.04f)
			.setDefaultColor(DyeColor.BLUE)
			.addIngredient(ModItems.LARGE_PROPELLER.getId(), 1)
			.addIngredient(ModItems.LARGE_FUSELAGE.getId(), 2)
			.addIngredient(ModItems.SEAT.getId(), 6)
			.addIngredient("minecraft:gold_ingot", 10)
			.addPilotSeatSlot(0.6, -1.5, 2.8)
			.addSeatSlot("seat1", -0.6, -1.5, 2.8)
			.addSeatSlot("seat2", 0.6, -1.5, 1.6)
			.addSeatSlot("seat3", -0.6, -1.5, 1.6)
			.addSeatSlot("seat4", 0.6, -1.5, 0.4)
			.addSeatSlot("seat5", -0.6, -1.5, 0.4)
			.addEmptySlot("nose_1", SlotType.FRAME, 0, -1.6, 3.5, 180)
			.addEmptySlot("frame_1", SlotType.FRAME, 2, 0, 1, -90)
			.addEmptySlot("frame_2", SlotType.FRAME, 2, 0, 0, -90)
			.addEmptySlot("frame_3", SlotType.FRAME, 2, 0, -1, -90)
			.addEmptySlot("frame_4", SlotType.FRAME, -2, 0, 1, 90)
			.addEmptySlot("frame_5", SlotType.FRAME, -2, 0, 0, 90)
			.addEmptySlot("frame_6", SlotType.FRAME, -2, 0, -1, 90)
			.addEmptySlot("internal_1", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_2", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_5", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_6", SlotType.ADVANCED_INTERNAL)
			.build();
			
	public static final AircraftPreset UNARMED_ANDOLF_SUB = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "andolf_sub_unarmed", EMPTY_ANDOLF_SUB)
			.setCraftable()
			.addIngredient(ModItems.C12_ENGINE.getId(), 2)
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.setSlotItem("internal_1", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_2", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_3", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.build();
	
	public static final AircraftPreset DEFAULT_ANDOLF_SUB = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "andolf_sub", UNARMED_ANDOLF_SUB)
			.setCraftable()
			.addIngredient(ModItems.WR1K.getId())
			.setSlotItem("internal_4", ModItems.WR1K.getId())
			.setSlotItem("nose_1", ModItems.XM12.getId(), "20mm", true)
			.setSlotItem("frame_1", ModItems.HEAVY_MISSILE_RACK.getId(), "torpedo1", true)
			.setSlotItem("frame_2", ModItems.HEAVY_MISSILE_RACK.getId(), "torpedo1", true)
			.setSlotItem("frame_3", ModItems.HEAVY_MISSILE_RACK.getId(), "torpedo1", true)
			.setSlotItem("frame_4", ModItems.HEAVY_MISSILE_RACK.getId(), "torpedo1", true)
			.setSlotItem("frame_5", ModItems.HEAVY_MISSILE_RACK.getId(), "torpedo1", true)
			.setSlotItem("frame_6", ModItems.HEAVY_MISSILE_RACK.getId(), "torpedo1", true)
			.addIngredient("minecraft:copper_ingot", 63)
			.addIngredient("minecraft:iron_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 6)
			.addIngredient(ModItems.XM12.getId(), 1)
			.build();
	
	public static final AircraftPreset DEFAULT_ORANGE_TESLA = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "orange_tesla")
			.setAircraftType(AircraftType.CAR)
			.setItem(ModItems.ORANGE_TESLA.getId())
			.setCraftable()
			.setMaxHealth(40f)
			.setMass(8f)
			.setMaxSpeed(0.65f)
			.setStealth(1.0f)
			.setCrossSecArea(5.375f)
			.setIdleHeat(4f)
			.setBaseArmor(1f)
			.setTurnRadius(7f)
			.setMaxTurnRates(0f, 0f, 4f)
			.setTurnTorques(0f, 0f, 1f)
			.setThrottleRate(0.07f, 0.07f)
			.addPilotSeatSlot(0.5, 0.45, 0.3)
			.addSeatSlot("seat1", -0.5, 0.45, 0.3)
			.addSeatSlot("seat2", 0.5, 0.45, -0.85)
			.addSeatSlot("seat3", -0.5, 0.45, -0.85)
			.addItemSlot("internal_1", SlotType.SPIN_ENGINE, ModItems.C6_ENGINE.getId())
			.addItemSlot("internal_2", SlotType.INTERNAL, ModItems.LIGHT_FUEL_TANK.getId(), true)
			.setDefaultColor(DyeColor.ORANGE)
			.addIngredient("minecraft:minecart", 2)
			.addIngredient(ModItems.SEAT.getId(), 4)
			.addIngredient(ModItems.C6_ENGINE.getId())
			.addIngredient(ModItems.LIGHT_FUEL_TANK.getId())
			.build();
	
	public static final AircraftPreset EMPTY_WOODEN_PLANE = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "wooden_plane_empty")
			.setAircraftType(AircraftType.PLANE)
			.setItem(ModItems.WOODEN_PLANE.getId())
			.setMaxHealth(40f)
			.setMass(6f)
			.setMaxSpeed(0.9f)
			.setStealth(1.0f)
			.setCrossSecArea(2.89f)
			.setIdleHeat(1f)
			.setBaseArmor(0f)
			.setTurnRadius(16f)
			.setMaxTurnRates(5f, 3.0f, 2.0f)
			.setTurnTorques(1.5f, 2.5f, 4.5f)
			.setThrottleRate(0.02f, 0.06f)
			.setPlaneWingArea(6f)
			.setDefaultColor(DyeColor.BROWN)
			.addIngredient("minecraft:oak_planks", 30)
			.addIngredient(ModItems.COCKPIT.getId())
			.addIngredient(ModItems.PROPELLER.getId())
			.addPilotSeatSlot(0, -0.4, 0)
			.addEmptySlot("left_wing_1", SlotType.WING, 1.5, 0, 0, 180)
			.addEmptySlot("right_wing_1", SlotType.WING, -1.5, 0, 0, 180)
			.addEmptySlot("internal_1", SlotType.RADIAL_ENGINE)
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.build();
	
	public static final AircraftPreset DEFAULT_WOODEN_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "wooden_plane", EMPTY_WOODEN_PLANE)
			.setCraftable()
			.addIngredient(ModItems.CM_MANLY_52.getId())
			.addIngredient(ModItems.LIGHT_FUEL_TANK.getId())
			.setSlotItem("internal_1", ModItems.CM_MANLY_52.getId())
			.setSlotItem("internal_2", ModItems.LIGHT_FUEL_TANK.getId(), true)
			.build();
	
	public static final AircraftPreset BOMBER_WOODEN_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "wooden_plane_bomber", DEFAULT_WOODEN_PLANE)
			.setCraftable()
			.setSlotItem("left_wing_1", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.setSlotItem("right_wing_1", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.addIngredient("minecraft:tnt", 12)
			.addIngredient("minecraft:iron_ingot", 36)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 2)
			.build();
	
	public static final AircraftPreset FIGHTER_WOODEN_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "wooden_plane_fighter", DEFAULT_WOODEN_PLANE)
			.setCraftable()
			.setSlotItem("left_wing_1", ModItems.XM12.getId(), "20mm", true)
			.setSlotItem("right_wing_1", ModItems.XM12.getId(), "20mm", true)
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient(ModItems.XM12.getId(), 2)
			.build();
	
	public static final AircraftPreset EMPTY_E3SENTRY_PLANE = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "e3sentry_plane_empty")
			.setAircraftType(AircraftType.PLANE)
			.setItem(ModItems.E3SENTRY_PLANE.getId())
			.setMaxHealth(400f)
			.setMass(60f)
			.setMaxSpeed(1.0f)
			.setStealth(1.3f)
			.setCrossSecArea(16f)
			.setIdleHeat(20f)
			.setBaseArmor(4f)
			.setTurnRadius(40f)
			.setMaxTurnRates(3f, 2.0f, 2.0f)
			.setTurnTorques(2.0f, 2.0f, 2.0f)
			.setThrottleRate(0.01f, 0.04f)
			.setPlaneWingArea(30f)
			.setDefaultColor(DyeColor.GRAY)
			.addIngredient(ModItems.ADVANCED_COCKPIT.getId())
			.addIngredient(ModItems.SEAT.getId(), 11)
			.addIngredient(ModItems.LARGE_FUSELAGE.getId(), 2)
			.addIngredient("minecraft:gold_ingot", 25)
			.addPilotSeatSlot(0.5, -1.35, 4.7)
			.addSeatSlot("seat1", -0.5, -1.35, 4.7)
			.addSeatSlot("seat2", 0.5, -1.35, 3)
			.addSeatSlot("seat3", 0.5, -1.35, 1.5)
			.addSeatSlot("seat4", 0.5, -1.35, 0)
			.addSeatSlot("seat5", 0.5, -1.35, -1.5)
			.addSeatSlot("seat6", 0.5, -1.35, -3)
			.addSeatSlot("seat7", -0.5, -1.35, 3)
			.addSeatSlot("seat8", -0.5, -1.35, 1.5)
			.addSeatSlot("seat9", -0.5, -1.35, 0)
			.addSeatSlot("seat10", -0.5, -1.35, -1.5)
			.addSeatSlot("seat11", -0.5, -1.35, -3)
			.addEmptySlot("left_wing_1", SlotType.WING, 4, -0.6, 0.5, 180)
			.addEmptySlot("right_wing_1", SlotType.WING, -4, -0.6, 0.5, 180)
			.addEmptySlot("left_wing_2", SlotType.WING, 6,-0.6, 0.5, 180)
			.addEmptySlot("right_wing_2", SlotType.WING, -6, -0.6, 0.5, 180)
			.addEmptySlot("frame_1", SlotType.HEAVY_FRAME, 0, 0.8, -1.3, 0)
			.addEmptySlot("internal_1", SlotType.INTERNAL)
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.INTERNAL)
			.addEmptySlot("internal_5", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_6", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_7", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_8", SlotType.ADVANCED_INTERNAL)
			.build();
	
	public static final AircraftPreset DEFAULT_E3SENTRY_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "e3sentry_plane", EMPTY_E3SENTRY_PLANE)
			.setCraftable()
			.addIngredient(ModItems.CFM56.getId(), 2)
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId(), 2)
			.addIngredient(ModItems.AR20K.getId())
			.addIngredient(ModItems.DATA_LINK.getId())
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.setSlotItem("left_wing_1", ModItems.CFM56.getId())
			.setSlotItem("right_wing_1", ModItems.CFM56.getId())
			.setSlotItem("frame_1", ModItems.AR20K.getId())
			.setSlotItem("internal_1", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.setSlotItem("internal_2", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.setSlotItem("internal_3", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.setSlotItem("internal_5", ModItems.DATA_LINK.getId())
			.build();
	
	public static final AircraftPreset EMPTY_AXCEL_TRUCK = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "axcel_truck_empty")
			.setAircraftType(AircraftType.CAR)
			.setItem(ModItems.AXCEL_TRUCK.getId())
			.setMaxHealth(80f)
			.setMass(15f)
			.setMaxSpeed(0.5f)
			.setStealth(1.0f)
			.setCrossSecArea(7.5f)
			.setIdleHeat(6f)
			.setBaseArmor(8f)
			.setTurnRadius(11f)
			.setMaxTurnRates(0f, 0f, 4f)
			.setTurnTorques(0f, 0f, 1f)
			.setThrottleRate(0.05f, 0.05f)
			.addPilotSeatSlot(0.5, 0.9, 1)
			.addSeatSlot("seat2", -0.5, 0.9, 1)
			.setDefaultColor(DyeColor.YELLOW)
			.addIngredient("minecraft:minecart", 2)
			.addIngredient(ModItems.FUSELAGE.getId(), 1)
			.addIngredient(ModItems.SEAT.getId(), 2)
			.addIngredient("minecraft:gold_ingot", 3)
			.addEmptySlot("cargo_bed_1", SlotType.HEAVY_TURRET, 0, 1, -2.75, 0)
			.addEmptySlot("frame_1", SlotType.ADVANCED_FRAME, 0, 2.95, 1, 0)
			.addEmptySlot("internal_1", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.addEmptySlot("internal_3", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_4", SlotType.ADVANCED_INTERNAL)
			.build();
	
	public static final AircraftPreset DEFAULT_AXCEL_TRUCK = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "axcel_truck", EMPTY_AXCEL_TRUCK)
			.setCraftable()
			.addIngredient(ModItems.SAM_LAUNCHER.getId())
			.addIngredient(ModItems.C12_ENGINE.getId())
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.addIngredient(ModItems.AR2K.getId())
			.setSlotItem("frame_1", ModItems.AXCEL_TRUCK_RADAR.getId())
			.setSlotItem("cargo_bed_1", ModItems.SAM_LAUNCHER.getId())
			.setSlotItem("internal_1", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_2", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.build();
	
	public static final AircraftPreset EMPTY_GRONK_BATTLESHIP = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "gronk_battleship_empty")
			.setAircraftType(AircraftType.BOAT)
			.setItem(ModItems.GRONK_BATTLESHIP.getId())
			.setMaxHealth(600f)
			.setMass(150f)
			.setMaxSpeed(0.6f)
			.setStealth(0.9f)
			.setCrossSecArea(7.5f)
			.setIdleHeat(4f)
			.setBaseArmor(15f)
			.setTurnRadius(0f)
			.setMaxTurnRates(0f, 0f, 1.5f)
			.setTurnTorques(0f, 0f, 2f)
			.setThrottleRate(0.05f, 0.10f)
			.setDefaultColor(DyeColor.WHITE)
			.addIngredient(ModItems.LARGE_FUSELAGE.getId(), 3)
			.addIngredient("minecraft:oak_boat", 4)
			.addIngredient(ModItems.SEAT.getId(), 4)
			.addPilotSeatSlot(0.8, 6, 4.5)
			.addSeatSlot("seat2", SlotType.SEAT, -0.8, 6, 4.5)
			.addSeatSlot("seat3", SlotType.SEAT, 0.8, 6, 2.2)
			.addSeatSlot("seat4", SlotType.SEAT, -0.8, 6, 2.2)
			.addSeatSlot("seat5", SlotType.HEAVY_TURRET, 0, 5, 13)
			.addSeatSlot("seat6", SlotType.HEAVY_TURRET, 0, 5, -13)
			.addSeatSlot("seat7", SlotType.HEAVY_TURRET, 0, 4, 22)
			.addSeatSlot("seat8", SlotType.HEAVY_TURRET, 0, 4, -22)
			.addEmptySlot("seat9", SlotType.MED_TURRET, 0, 6, 7, 0)
			.addEmptySlot("seat10", SlotType.MED_TURRET, 0, 6, -7, 0)
			.addEmptySlot("frame_1", SlotType.ADVANCED_FRAME, 0, 9, 3, 0)
			.addEmptySlot("frame_2", SlotType.LIGHT_TURRET, 6, 4, 4, 0)
			.addEmptySlot("frame_3", SlotType.LIGHT_TURRET, -6, 4, 4, 0)
			.addEmptySlot("frame_4", SlotType.LIGHT_TURRET, 6, 4, -4, 0)
			.addEmptySlot("frame_5", SlotType.LIGHT_TURRET, -6, 4, -4, 0)
			.addEmptySlot("internal_1", SlotType.PUSH_ENGINE)
			.addEmptySlot("internal_2", SlotType.PUSH_ENGINE)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.INTERNAL)
			.addEmptySlot("internal_5", SlotType.INTERNAL)
			.addEmptySlot("internal_6", SlotType.INTERNAL)
			.addEmptySlot("internal_7", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_8", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_9", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_10", SlotType.ADVANCED_INTERNAL)
			.build();
	
	public static final AircraftPreset UNARMED_GRONK_BATTLESHIP = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "gronk_battleship_unarmed", EMPTY_GRONK_BATTLESHIP)
			.setCraftable()
			.addIngredient(ModItems.TURBOFAN_F145.getId(), 2)
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.addIngredient(ModItems.WR1K.getId())
			.addIngredient(ModItems.AR1K.getId())
			.setSlotItem("frame_1", ModItems.AIR_SCAN_A.getId())
			.setSlotItem("internal_1", ModItems.TURBOFAN_F145.getId())
			.setSlotItem("internal_2", ModItems.TURBOFAN_F145.getId())
			.setSlotItem("internal_3", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.setSlotItem("internal_4", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.setSlotItem("internal_7", ModItems.WR1K.getId())
			.build();
	
	public static final AircraftPreset DEFAULT_GRONK_BATTLESHIP = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "gronk_battleship", UNARMED_GRONK_BATTLESHIP)
			.setCraftable()
			.setSlotItem("seat5", ModItems.MARK7_CANNON.getId())
			.setSlotItem("seat6", ModItems.MARK7_CANNON.getId())
			.setSlotItem("seat7", ModItems.MARK7_CANNON.getId())
			.setSlotItem("seat8", ModItems.MARK7_CANNON.getId())
			.setSlotItem("seat9", ModItems.MARK45_CANNON.getId())
			.setSlotItem("seat10", ModItems.MARK45_CANNON.getId())
			.setSlotItem("frame_2", ModItems.AA_TURRET.getId())
			.setSlotItem("frame_3", ModItems.AA_TURRET.getId())
			.setSlotItem("frame_4", ModItems.AA_TURRET.getId())
			.setSlotItem("frame_5", ModItems.AA_TURRET.getId())
			.addIngredient(ModItems.MARK7_CANNON.getId(), 4)
			.addIngredient(ModItems.AA_TURRET.getId(), 4)
			.addIngredient(ModItems.MARK45_CANNON.getId(), 2)
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient("minecraft:iron_ingot", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.build();
			
}
