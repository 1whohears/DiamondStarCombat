package com.onewhohears.dscombat.data.aircraft;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.resources.ResourceLocation;
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
			.addSeatSlot(PartSlot.PILOT_SLOT_NAME, 0, -0.3, 2, 48, 20)
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
			.setDefaultColor(DyeColor.WHITE)
			.setColorTexture(DyeColor.WHITE, new ResourceLocation("dscombat:textures/entities/alexis_plane/white.png"))
			.setColorTexture(DyeColor.RED, new ResourceLocation("dscombat:textures/entities/alexis_plane/red.png"))
			.setColorTexture(DyeColor.CYAN, new ResourceLocation("dscombat:textures/entities/alexis_plane/cyan.png"))
			.setColorTexture(DyeColor.BLACK, new ResourceLocation("dscombat:textures/entities/alexis_plane/black.png"))
			.setColorTexture(DyeColor.ORANGE, new ResourceLocation("dscombat:textures/entities/alexis_plane/orange.png"))
			.setColorTexture(DyeColor.BLUE, new ResourceLocation("dscombat:textures/entities/alexis_plane/blue.png"))
			.setColorTexture(DyeColor.PURPLE, new ResourceLocation("dscombat:textures/entities/alexis_plane/purple.png"))
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
			.addSeatSlot(PartSlot.PILOT_SLOT_NAME, 0, -0.4, 3, 48, 20)
			.addSeatSlot(PartSlot.PILOT_SLOT_NAME, SlotType.TURRET, 0, -0.4, 2, 68, 20)
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
			.setDefaultColor(DyeColor.GREEN)
			.setColorTexture(DyeColor.BLUE, new ResourceLocation("dscombat:textures/entities/javi_plane/blue.png"))
			.setColorTexture(DyeColor.RED, new ResourceLocation("dscombat:textures/entities/javi_plane/red.png"))
			.setColorTexture(DyeColor.GREEN, new ResourceLocation("dscombat:textures/entities/javi_plane/green.png"))
			.setColorTexture(DyeColor.BLACK, new ResourceLocation("dscombat:textures/entities/javi_plane/black.png"))
			.setColorTexture(DyeColor.PURPLE, new ResourceLocation("dscombat:textures/entities/javi_plane/purple.png"))
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
	
	// FIXME 1 finish default presets
	public static final AircraftPreset DEFAULT_NOAH_CHOPPER = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "noah_chopper")
			.setItem(ModItems.NOAH_CHOPPER.getId())
			
			.build();
	
	public static final AircraftPreset DEFAULT_MRBUDGER_TANK = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "mrbudger_tank")
			.setItem(ModItems.MRBUDGER_TANK.getId())
			
			.build();
	
	public static final AircraftPreset DEFAULT_SMALL_ROLLER = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "small_roller")
			.setItem(ModItems.SMALL_ROLLER.getId())
			
			.build();
	
	public static final AircraftPreset DEFAULT_NATHAN_BOAT = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "nathan_boat")
			.setItem(ModItems.NATHAN_BOAT.getId())
			
			.build();
	
	public static final AircraftPreset DEFAULT_ANDOLF_SUB = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "andolf_sub")
			.setItem(ModItems.ANDOLF_SUB.getId())
			
			.build();
	
	public static final AircraftPreset DEFAULT_ORANGE_TESLA = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "orange_tesla")
			.setItem(ModItems.ORANGE_TESLA.getId())
			
			.build();
}
