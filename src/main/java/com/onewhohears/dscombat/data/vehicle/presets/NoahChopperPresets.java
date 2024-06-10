package com.onewhohears.dscombat.data.vehicle.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.entityscreen.EntityScreenIds;
import com.onewhohears.dscombat.data.parts.SlotType;
import com.onewhohears.dscombat.data.vehicle.VehicleSoundManager.PassengerSoundPack;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;

public class NoahChopperPresets {
	
	public static final VehicleStats EMPTY_NOAH_CHOPPER = VehicleStats.Builder
			.createHelicopter(DSCombatMod.MODID, "noah_chopper_empty")
			.setAssetId("noah_chopper")
			.setSortFactor(4)
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
			.setBasicEngineSounds(ModSounds.HELI_1, ModSounds.HELI_1)
			.setRotationalInertia(8, 6, 4)
			.setCrashExplosionRadius(4)
			.set3rdPersonCamDist(6)
			.setHeliAlwaysLandingGear(true)
			.setHeliLiftFactor(10)
			.setLayerTextureNum(1)
			.setDefultPassengerSoundPack(PassengerSoundPack.ENG_NON_BINARY_GOOBER)
			.addPilotSeatSlot(0.4, -0.65, 1.5)
			.addSeatSlot("seat2", SlotType.MOUNT_MED, -0.4, -0.65, 1.5)
			.addSeatSlot("seat3", SlotType.MOUNT_MED, 0.4, -0.65, 0.3)
			.addSeatSlot("seat4", SlotType.MOUNT_MED, -0.4, -0.65, 0.3)
			.addEmptySlot("left_wing_1", SlotType.PYLON_LIGHT, 0.75, -0.5, 1.8, -90)
			.addEmptySlot("left_wing_2", SlotType.PYLON_LIGHT, 0.75, -0.5, 0.9, -90)
			.addEmptySlot("left_wing_3", SlotType.PYLON_LIGHT, 0.75, -0.5, 0, -90)
			.addEmptySlot("left_wing_4", SlotType.PYLON_LIGHT, 0.65, 0.5, 0, -90)
			.addEmptySlot("right_wing_1", SlotType.PYLON_LIGHT, -0.75, -0.5, 1.8, 90)
			.addEmptySlot("right_wing_2", SlotType.PYLON_LIGHT, -0.75, -0.5, 0.9, 90)
			.addEmptySlot("right_wing_3", SlotType.PYLON_LIGHT, -0.75, -0.5, 0, 90)
			.addEmptySlot("right_wing_4", SlotType.PYLON_LIGHT, -0.65, 0.5, 0, 90)
			.addEmptySlot("frame_1", SlotType.PYLON_HEAVY, 0, -0.67, 1.9, 180)
			.addEmptySlot("frame_2", SlotType.PYLON_MED, 0, -0.67, 1.1, 180)
			.addEmptySlot("frame_3", SlotType.PYLON_MED, 0, -0.67, 0.3, 180)
			.addEmptySlot("frame_4", SlotType.PYLON_MED, 0, -0.67, -0.5, 180)
			.addEmptySlot("internal_1", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_2", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.INTERNAL)
			.addEmptySlot("internal_5", SlotType.TECH_INTERNAL)
			.addEmptySlot("internal_6", SlotType.TECH_INTERNAL)
			.addEmptySlot("internal_7", SlotType.TECH_INTERNAL)
			.addEmptySlot("internal_8", SlotType.TECH_INTERNAL)
			.addIngredient(ModItems.FUSELAGE.getId())
			.addIngredient(ModItems.LARGE_PROPELLER.getId(), 2)
			.addIngredient(ModItems.PROPELLER.getId())
			.addIngredient(ModItems.COCKPIT.getId())
			.addIngredient(ModItems.SEAT.getId(), 3)
			.addIngredient("minecraft:gold_ingot", 8)
			.addEntityScreen(EntityScreenIds.AIR_RADAR_SCREEN,0.19,0.18,1.934,0.3,0.3,20)
			.addEntityScreen(EntityScreenIds.FUEL_SCREEN,0.19,-0.12,1.825,0.3,0.3,20)
			.addEntityScreen(EntityScreenIds.RWR_SCREEN,0.5,-0.12,1.825,0.3,0.3,20)
			.addEntityScreen(EntityScreenIds.GROUND_RADAR_SCREEN,0.5,0.18,1.934,0.3,0.3,20)
			.addHUDScreen(0.4, -0.65, 1.5)
			.setEntityMainHitboxSize(2.8f, 2.8f)
			.build();
	
	public static final VehicleStats UNARMED_NOAH_CHOPPER = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "noah_chopper_unarmed", EMPTY_NOAH_CHOPPER)
			.setCraftable()
			.setSlotItem("internal_1", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_2", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_3", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.addIngredient(ModItems.C12_ENGINE.getId(), 2)
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.build();
	
	public static final VehicleStats DEFAULT_NOAH_CHOPPER = VehicleStats.Builder
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
			.addIngredient("dscombat:aluminum_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.build();
	
}
