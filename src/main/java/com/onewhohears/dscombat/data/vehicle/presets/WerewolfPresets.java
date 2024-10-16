package com.onewhohears.dscombat.data.vehicle.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.SlotType;
import com.onewhohears.dscombat.data.vehicle.VehicleSoundManager.PassengerSoundPack;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;

public class WerewolfPresets {
	
	public static final VehicleStats EMPTY_WEREWOLF = VehicleStats.Builder
			.createHelicopter(DSCombatMod.MODID, "werewolf_empty")
			.setAssetId("werewolf")
			.setSortFactor(5)
			.setItem(ModItems.NOAH_CHOPPER.getId())
			.setMaxHealth(100f)
			.setBaseArmor(100f)
			.setArmorDamageThreshold(5f)
			.setArmorAbsorbtionPercent(0.275f)
			.setMass(6000f)
			.setMaxSpeed(0.7f)
			.setStealth(1.2f)
			.setCrossSecArea(8f)
			.setIdleHeat(8f)
			.setMaxAltitude(450)
			.setTurnRadius(0f)
			.setMaxTurnRates(4f, 2f, 4f)
			.setTurnTorques(1f, 1f, 2.5f)
			.setThrottleRate(0.01f, 0.02f)
			.setHeliHoverMovement(0.04f, 0.02f)
			.setBasicEngineSounds(ModSounds.HELI_1, ModSounds.HELI_1)
			.setRotationalInertia(8, 6, 4)
			.setCrashExplosionRadius(4)
			.set3rdPersonCamDist(6)
			.setHeliAlwaysLandingGear(false)
			.setHeliLiftFactor(10)
			.setLayerTextureNum(1)
			.setDefultPassengerSoundPack(PassengerSoundPack.ENG_NON_BINARY_GOOBER)
			.addPilotSeatSlot(0.4, -0.65, 1.5, true)
			.addEmptySlot("left_wing_1", SlotType.PYLON_MED, 0.75, -0.5, 1.8, -90)
			.addEmptySlot("left_wing_2", SlotType.PYLON_MED, 0.75, -0.5, 0.9, -90)
			.addEmptySlot("left_wing_3", SlotType.PYLON_LIGHT, 0.75, -0.5, 0, -90)
			.addEmptySlot("right_wing_1", SlotType.PYLON_MED, -0.75, -0.5, 1.8, 90)
			.addEmptySlot("right_wing_2", SlotType.PYLON_MED, -0.75, -0.5, 0.9, 90)
			.addEmptySlot("right_wing_3", SlotType.PYLON_LIGHT, -0.75, -0.5, 0, 90)
			.addEmptySlot("nose_1", SlotType.PYLON_HEAVY, 0, -0.67, 1.9, 180)
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
			.setEntityMainHitboxSize(2.8f, 2.8f)
			.build();
	
}
