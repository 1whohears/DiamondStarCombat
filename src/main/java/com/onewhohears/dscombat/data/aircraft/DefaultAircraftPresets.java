package com.onewhohears.dscombat.data.aircraft;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.world.item.DyeColor;

public class DefaultAircraftPresets {
	
	public static final AircraftPreset DEFAULT_ALEXIS_PLANE = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "alexis_plane")
			.setItem(ModItems.ALEXIS_PLANE.getId())
			.setMaxHealth(150f)
			.setMass(20f)
			.setMaxSpeed(1.45f)
			.setStealth(0.8f)
			.setIdleHeat(4f)
			.setTurnRadius(10f)
			.setMaxTurnRates(6f, 2.5f, 1.5f)
			.setTurnTorques(1f, 2f, 4f)
			.setThrottleRate(0.04f, 0.08f)
			.setPlaneWingArea(12f)
			.addPilotSeatSlot(0, -0.3, 2, 48, 20)
			.addItemSlot("dscombat.left_wing_1", SlotType.WING, 1.5, -0.3, 0.5, 0, 48, 40, 
					ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.addItemSlot("dscombat.left_wing_2", SlotType.WING, 2.5, -0.3, 0.5, 0, 68, 40, 
					ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
			.addEmptySlot("dscombat.left_wing_3", SlotType.WING, 3.5, -0.3, 0.5, 0, 88, 40) 
			.addItemSlot("dscombat.right_wing_1", SlotType.WING, -1.5, -0.3, 0.5, 0, 48, 60, 
					ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.addItemSlot("dscombat.right_wing_2", SlotType.WING, -2.5, -0.3, 0.5, 0, 68, 60, 
					ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
			.addEmptySlot("dscombat.right_wing_3", SlotType.WING, -3.5, -0.3, 0.5, 0, 88, 60) 
			.addItemSlot("dscombat.frame_1", SlotType.FRAME, 0, -0.4, 2.5, 0, 48, 80, 
					ModItems.XM12.getId(), "20mm", true)
			.addItemSlot("dscombat.internal_1", SlotType.INTERNAL, 48, 100, ModItems.C12_ENGINE.getId())
			.addItemSlot("dscombat.internal_2", SlotType.INTERNAL, 68, 100, ModItems.HEAVY_FUEL_TANK.getId(), true)
			.addItemSlot("dscombat.internal_3", SlotType.INTERNAL, 88, 100, ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addItemSlot("dscombat.internal_4", SlotType.ADVANCED_INTERNAL, 108, 100, ModItems.AR2K.getId())
			.addEmptySlot("dscombat.internal_5", SlotType.ADVANCED_INTERNAL, 128, 100)
			.addEmptySlot("dscombat.internal_6", SlotType.ADVANCED_INTERNAL, 148, 100)
			.setDefaultTexture(DyeColor.WHITE, "dscombat:textures/entities/alexis_plane/white.png")
			.setAltTexture(DyeColor.RED, "dscombat:textures/entities/alexis_plane/red.png")
			.setAltTexture(DyeColor.CYAN, "dscombat:textures/entities/alexis_plane/cyan.png")
			.setAltTexture(DyeColor.BLACK, "dscombat:textures/entities/alexis_plane/black.png")
			.setAltTexture(DyeColor.ORANGE, "dscombat:textures/entities/alexis_plane/orange.png")
			.setAltTexture(DyeColor.BLUE, "dscombat:textures/entities/alexis_plane/blue.png")
			.setAltTexture(DyeColor.PURPLE, "dscombat:textures/entities/alexis_plane/purple.png")
			.addIngredient(ModItems.FUSELAGE.getId(), 2)
			.addIngredient(ModItems.WING.getId(), 2)
			.addIngredient(ModItems.ADVANCED_COCKPIT.getId())
			.addIngredient("minecraft:gold_ingot", 4)
			.addIngredient(ModItems.C12_ENGINE.getId())
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.addIngredient(ModItems.AR2K.getId())
			.build();
	
	public static final AircraftPreset DEFAULT_JAVI_PLANE = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "javi_plane")
			.setItem(ModItems.JAVI_PLANE.getId())
			.setMaxHealth(200f)
			.setMass(25f)
			.setMaxSpeed(1.20f)
			.setStealth(1.1f)
			.setIdleHeat(5f)
			.setTurnRadius(12f)
			.setMaxTurnRates(4f, 2f, 1f)
			.setTurnTorques(1f, 2f, 4f)
			.setThrottleRate(0.04f, 0.08f)
			.setPlaneWingArea(12f)
			.addPilotSeatSlot(0, -0.4, 3, 48, 20)
			.addSeatSlot("dscombat.seat2", SlotType.TURRET, 0, -0.4, 2, 68, 20)
			// left wing
			.addItemSlot("dscombat.left_wing_1", SlotType.WING, 1.5, -0.3, 0, 0, 48, 40, 
					ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.addItemSlot("dscombat.left_wing_2", SlotType.WING, 3.0, -0.2, 0, 0, 68, 40, 
					ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.addEmptySlot("dscombat.left_wing_3", SlotType.WING, 4.0, -0.07, 0, 0, 88, 40) 
			.addEmptySlot("dscombat.left_wing_4", SlotType.WING, 5.0, 0.05, 0, 0, 108, 40) 
			// right wing
			.addItemSlot("dscombat.right_wing_1", SlotType.WING, -1.5, -0.3, 0, 0, 48, 60, 
					ModItems.HEAVY_MISSILE_RACK.getId(), "agm65l", true)
			.addItemSlot("dscombat.right_wing_2", SlotType.WING, -3.0, -0.2, 0, 0, 68, 60, 
					ModItems.LIGHT_MISSILE_RACK.getId(), "aim9p5", true)
			.addEmptySlot("dscombat.right_wing_3", SlotType.WING, -4.0, -0.07, 0, 0, 88, 60) 
			.addEmptySlot("dscombat.right_wing_4", SlotType.WING, -5.0, 0.05, 0, 0, 108, 60) 
			// other
			.addItemSlot("dscombat.frame_1", SlotType.FRAME, 0, -0.65, 3, 0, 48, 80, 
					ModItems.XM12.getId(), "50mmhe", true)
			.addItemSlot("dscombat.frame_2", SlotType.FRAME, 0, -0.65, 0, 0, 68, 80, 
					ModItems.HEAVY_MISSILE_RACK.getId(), "agm84e", true)
			.addItemSlot("dscombat.internal_1", SlotType.INTERNAL, 48, 100, ModItems.C6_ENGINE.getId())
			.addItemSlot("dscombat.internal_2", SlotType.INTERNAL, 68, 100, ModItems.C6_ENGINE.getId())
			.addItemSlot("dscombat.internal_3", SlotType.INTERNAL, 88, 100, ModItems.HEAVY_FUEL_TANK.getId(), true)
			.addItemSlot("dscombat.internal_4", SlotType.ADVANCED_INTERNAL, 108, 100, ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addItemSlot("dscombat.internal_5", SlotType.ADVANCED_INTERNAL, 128, 100, ModItems.AR1K.getId())
			.addItemSlot("dscombat.internal_6", SlotType.ADVANCED_INTERNAL, 148, 100, ModItems.GR400.getId())
			.setDefaultTexture(DyeColor.GREEN, "dscombat:textures/entities/javi_plane/green.png")
			.setAltTexture(DyeColor.BLUE, "dscombat:textures/entities/javi_plane/blue.png")
			.setAltTexture(DyeColor.RED, "dscombat:textures/entities/javi_plane/red.png")
			.setAltTexture(DyeColor.BLACK, "dscombat:textures/entities/javi_plane/black.png")
			.setAltTexture(DyeColor.PURPLE, "dscombat:textures/entities/javi_plane/purple.png")
			.addIngredient(ModItems.FUSELAGE.getId(), 2)
			.addIngredient(ModItems.LARGE_WING.getId(), 2)
			.addIngredient(ModItems.COCKPIT.getId())
			.addIngredient("minecraft:gold_ingot", 4)
			.addIngredient(ModItems.C6_ENGINE.getId(), 2)
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.addIngredient(ModItems.AR1K.getId())
			.addIngredient(ModItems.GR400.getId())
			.build();
	
	public static final AircraftPreset DEFAULT_NOAH_CHOPPER = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "noah_chopper")
			.setItem(ModItems.NOAH_CHOPPER.getId())
			.setMaxHealth(300f)
			.setMass(30f)
			.setMaxSpeed(0.8f)
			.setStealth(1.2f)
			.setIdleHeat(8f)
			.setTurnRadius(0f)
			.setMaxTurnRates(4f, 2f, 4f)
			.setTurnTorques(1f, 1f, 2.5f)
			.setThrottleRate(0.01f, 0.02f)
			.setHeliHoverMovement(0.04f, 0.02f)
			.addPilotSeatSlot(0.4, -0.65, 1.5, 48, 20)
			.addItemSlot("dscombat.seat2", SlotType.TURRET, -0.4, -0.65, 1.5, 0, 68, 20,
					ModItems.MINIGUN_TURRET.getId())
			.addSeatSlot("dscombat.seat3", SlotType.TURRET, 0.4, -0.65, 0.3, 88, 20)
			.addSeatSlot("dscombat.seat4", SlotType.TURRET, -0.4, -0.65, 0.3, 108, 20)
			// FIXME 1.1 finish noah chopper preset
			.setDefaultTexture(DyeColor.BROWN, "dscombat:textures/entities/noah_chopper/brown.png")
			.setAltTexture(DyeColor.BLUE, "dscombat:textures/entities/noah_chopper/blue.png")
			.setAltTexture(DyeColor.RED, "dscombat:textures/entities/noah_chopper/red.png")
			.setAltTexture(DyeColor.BLACK, "dscombat:textures/entities/noah_chopper/black.png")
			.addIngredient(ModItems.FUSELAGE.getId())
			.addIngredient(ModItems.LARGE_PROPELLER.getId(), 2)
			.addIngredient(ModItems.PROPELLER.getId())
			.addIngredient(ModItems.COCKPIT.getId())
			.addIngredient("minecraft:gold_ingot", 4)
			.addIngredient(ModItems.C12_ENGINE.getId(), 2)
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.addIngredient(ModItems.AR500.getId())
			.addIngredient(ModItems.GR400.getId())
			.addIngredient(ModItems.MINIGUN_TURRET.getId())
			.build();
	
	public static final AircraftPreset DEFAULT_MRBUDGER_TANK = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "mrbudger_tank")
			.setItem(ModItems.MRBUDGER_TANK.getId())
			.setMaxHealth(500f)
			.setMass(50f)
			.setMaxSpeed(0.3f)
			.setStealth(1.0f)
			.setIdleHeat(10f)
			.setTurnRadius(0f)
			.setMaxTurnRates(0f, 0f, 2f)
			.setTurnTorques(0f, 0f, 8f)
			.setThrottleRate(0.04f, 0.04f)
			.addItemSlot(PartSlot.PILOT_SLOT_NAME, SlotType.HEAVY_TURRET, 0, 1.4, 0, 0, 48, 20,
					ModItems.HEAVY_TANK_TURRET.getId())
			// FIXME 1.2 finish budger tank preset
			.setDefaultTexture(DyeColor.YELLOW, "dscombat:textures/entities/noah_chopper/yellow.png")
			.setAltTexture(DyeColor.BLUE, "dscombat:textures/entities/noah_chopper/blue.png")
			.setAltTexture(DyeColor.RED, "dscombat:textures/entities/noah_chopper/red.png")
			.setAltTexture(DyeColor.BLACK, "dscombat:textures/entities/noah_chopper/black.png")
			.addIngredient(ModItems.FUSELAGE.getId())
			.build();
	
	public static final AircraftPreset DEFAULT_SMALL_ROLLER = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "small_roller")
			.setItem(ModItems.SMALL_ROLLER.getId())
			.setMaxHealth(30f)
			.setMass(5f)
			.setMaxSpeed(0.2f)
			.setStealth(1.0f)
			.setIdleHeat(1f)
			.setTurnRadius(0f)
			.setMaxTurnRates(0f, 0f, 3f)
			.setTurnTorques(0f, 0f, 10f)
			.setThrottleRate(0.08f, 0.08f)
			.addItemSlot(PartSlot.PILOT_SLOT_NAME, SlotType.HEAVY_TURRET, 0, 0.6, 0, 0, 48, 20,
					ModItems.STEVE_UP_SMASH.getId())
			// FIXME 1.3 finish small roller preset
			.setDefaultTexture(DyeColor.GRAY, "dscombat:textures/entities/small_roller.png")
			.addIngredient(ModItems.FUSELAGE.getId())
			.build();
	
	public static final AircraftPreset DEFAULT_NATHAN_BOAT = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "nathan_boat")
			.setItem(ModItems.NATHAN_BOAT.getId())
			.setMaxHealth(100f)
			.setMass(10f)
			.setMaxSpeed(0.9f)
			.setStealth(1.0f)
			.setIdleHeat(2f)
			.setTurnRadius(0f)
			.setMaxTurnRates(0f, 0f, 3.5f)
			.setTurnTorques(0f, 0f, 2f)
			.setThrottleRate(0.05f, 0.10f)
			.addPilotSeatSlot(0.8, 1.2, -1.13, 48, 20)
			// FIXME 1.4 finish nathan boat preset
			.setDefaultTexture(DyeColor.WHITE, "dscombat:textures/entities/nathan_boat/white.png")
			.addIngredient(ModItems.FUSELAGE.getId())
			.build();
	
	public static final AircraftPreset DEFAULT_ANDOLF_SUB = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "andolf_sub")
			.setItem(ModItems.ANDOLF_SUB.getId())
			.setMaxHealth(800f)
			.setMass(200f)
			.setMaxSpeed(0.6f)
			.setStealth(0.6f)
			.setIdleHeat(10f)
			.setTurnRadius(0f)
			.setMaxTurnRates(2f, 2f, 2f)
			.setTurnTorques(1f, 1f, 1f)
			.setThrottleRate(0.04f, 0.04f)
			.addPilotSeatSlot(0.6, -1.5, 2.8, 48, 20)
			// FIXME 1.5 finish andolf sub preset
			.setDefaultTexture(DyeColor.BLUE, "dscombat:textures/entities/andolf_sub/blue.png")
			.addIngredient(ModItems.FUSELAGE.getId())
			.build();
	
	public static final AircraftPreset DEFAULT_ORANGE_TESLA = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "orange_tesla")
			.setItem(ModItems.ORANGE_TESLA.getId())
			.setMaxHealth(50f)
			.setMass(8f)
			.setMaxSpeed(0.7f)
			.setStealth(1.0f)
			.setIdleHeat(4f)
			.setTurnRadius(7f)
			.setMaxTurnRates(0f, 0f, 4f)
			.setTurnTorques(0f, 0f, 1f)
			.setThrottleRate(0.07f, 0.07f)
			.addPilotSeatSlot(0.5, 0.45, 0.3, 48, 20)
			// FIXME 1.6 finish orange tesla preset
			.setDefaultTexture(DyeColor.ORANGE, "dscombat:textures/entities/orange_tesla/orange.png")
			.addIngredient(ModItems.FUSELAGE.getId())
			.build();
}
