package com.onewhohears.dscombat.data.aircraft;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.JsonPresetGenerator;
import com.onewhohears.dscombat.data.parts.PartSlot;

import net.minecraft.data.DataGenerator;

public class AircraftClientPresetGenerator extends JsonPresetGenerator<AircraftClientPreset> {
	
	@Override
	protected void registerPresets() {
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "alexis_plane")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/alexis_plane.png")
				.makeOneTexture("dscombat:textures/entity/alexis_plane/v3_template")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, 48, 20)
				.addUIPos("left_wing_1", 48, 40)
				.addUIPos("left_wing_2", 68, 40)
				.addUIPos("right_wing_1", 48, 60)
				.addUIPos("right_wing_2", 68, 60)
				.addUIPos("frame_1", 48, 80)
				.addUIPos("internal_1", 48, 100)
				.addUIPos("internal_2", 68, 100)
				.addUIPos("internal_3", 88, 100)
				.addUIPos("internal_4", 108, 100)
				.addUIPos("internal_5", 128, 100)
				.addUIPos("internal_6", 148, 100)
				.build());
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "javi_plane")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/javi_plane.png")
				.makeOneTexture("dscombat:textures/entity/javi_plane/test")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, 48, 20)
				.addUIPos("seat2", 68, 20)
				.addUIPos("left_wing_1", 48, 40)
				.addUIPos("left_wing_2", 68, 40)
				.addUIPos("left_wing_3", 88, 40)
				.addUIPos("left_wing_4", 108, 40)
				.addUIPos("right_wing_1", 48, 60)
				.addUIPos("right_wing_2", 68, 60)
				.addUIPos("right_wing_3", 88, 60)
				.addUIPos("right_wing_4", 108, 60)
				.addUIPos("frame_1", 48, 80)
				.addUIPos("frame_2", 68, 80)
				.addUIPos("internal_1", 48, 100)
				.addUIPos("internal_2", 68, 100)
				.addUIPos("internal_3", 88, 100)
				.addUIPos("internal_4", 108, 100)
				.addUIPos("internal_5", 128, 100)
				.addUIPos("internal_6", 148, 100)
				.build());
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "noah_chopper")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/noah_chopper.png")
				.makeOneTexture("dscombat:textures/entity/noah_chopper/noah_chopper_test")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, 48, 20)
				.addUIPos("seat2", 68, 20)
				.addUIPos("seat3", 88, 20)
				.addUIPos("seat4", 108, 20)
				.addUIPos("left_wing_1", 48, 40)
				.addUIPos("left_wing_2", 68, 40)
				.addUIPos("left_wing_3", 88, 40)
				.addUIPos("left_wing_4", 108, 40)
				.addUIPos("right_wing_1", 48, 60)
				.addUIPos("right_wing_2", 68, 60)
				.addUIPos("right_wing_3", 88, 60)
				.addUIPos("right_wing_4", 108, 60)
				.addUIPos("frame_1", 48, 80)
				.addUIPos("frame_2", 68, 80)
				.addUIPos("frame_3", 88, 80)
				.addUIPos("frame_4", 108, 80)
				.addUIPos("internal_1", 48, 100)
				.addUIPos("internal_2", 68, 100)
				.addUIPos("internal_3", 88, 100)
				.addUIPos("internal_4", 108, 100)
				.addUIPos("internal_5", 128, 100)
				.addUIPos("internal_6", 148, 100)
				.addUIPos("internal_7", 168, 100)
				.addUIPos("internal_8", 188, 100)
				.build());
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "mrbudger_tank")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/mrbudger_tank.png")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, 48, 20)
				.addUIPos("seat2", 68, 20)
				.addUIPos("seat3", 88, 20)
				.addUIPos("seat4", 108, 20)
				.addUIPos("seat5", 128, 20)
				.addUIPos("internal_1", 48, 100)
				.addUIPos("internal_2", 68, 100)
				.addUIPos("internal_3", 88, 100)
				.build());
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "small_roller")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/small_roller.png")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, 48, 20)
				.addUIPos("internal_1", 48, 100)
				.addUIPos("internal_2", 68, 100)
				.makeOneTexture("dscombat:textures/entity/small_roller/gray")
				.build());
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "nathan_boat")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/nathan_boat.png")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, 48, 20)
				.addUIPos("seat2", 68, 20)
				.addUIPos("seat3", 88, 20)
				.addUIPos("seat4", 108, 20)
				.addUIPos("seat5", 128, 20)
				.addUIPos("seat6", 148, 20)
				.addUIPos("frame_1", 48, 80)
				.addUIPos("frame_2", 68, 80)
				.addUIPos("internal_1", 48, 100)
				.addUIPos("internal_2", 68, 100)
				.addUIPos("internal_3", 88, 100)
				.addUIPos("internal_4", 108, 100)
				.build());
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "andolf_sub")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/andolf_sub.png")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, 48, 20)
				.addUIPos("seat1", 68, 20)
				.addUIPos("seat2", 88, 20)
				.addUIPos("seat3", 108, 20)
				.addUIPos("seat4", 128, 20)
				.addUIPos("seat5", 148, 20)
				.addUIPos("frame_1", 48, 80)
				.addUIPos("frame_2", 68, 80)
				.addUIPos("frame_3", 88, 80)
				.addUIPos("frame_4", 108, 80)
				.addUIPos("frame_5", 128, 80)
				.addUIPos("frame_6", 148, 80)
				.addUIPos("internal_1", 48, 100)
				.addUIPos("internal_2", 68, 100)
				.addUIPos("internal_3", 88, 100)
				.addUIPos("internal_4", 108, 100)
				.addUIPos("internal_5", 128, 100)
				.addUIPos("internal_6", 148, 100)
				.build());
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "orange_tesla")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/orange_tesla.png")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, 48, 20)
				.addUIPos("seat1", 68, 20)
				.addUIPos("seat2", 88, 20)
				.addUIPos("seat3", 108, 20)
				.addUIPos("internal_1", 48, 100)
				.addUIPos("internal_2", 68, 100)
				.makeOneTexture("dscombat:textures/entity/orange_tesla/orange")
				.build());
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "wooden_plane")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/wooden_plane.png")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, 48, 20)
				.addUIPos("left_wing_1", 48, 40)
				.addUIPos("right_wing_1", 48, 60)
				.addUIPos("frame_1", 48, 80)
				.addUIPos("frame_2", 68, 80)
				.addUIPos("frame_3", 88, 80)
				.addUIPos("frame_4", 108, 80)
				.addUIPos("internal_1", 48, 100)
				.addUIPos("internal_2", 68, 100)
				.addUIPos("internal_3", 88, 100)
				.makeOneTexture("dscombat:textures/entity/wooden_plane/brown")
				.build());
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "e3sentry_plane")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/e3sentry_plane.png")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, 48, 20)
				.addUIPos("seat1", 68, 20)
				.addUIPos("seat2", 88, 20)
				.addUIPos("seat3", 108, 20)
				.addUIPos("seat4", 128, 20)
				.addUIPos("seat5", 148, 20)
				.addUIPos("seat6", 48, 40)
				.addUIPos("seat7", 68, 40)
				.addUIPos("seat8", 88, 40)
				.addUIPos("seat9", 108, 40)
				.addUIPos("seat10", 128, 40)
				.addUIPos("seat11", 148, 40)
				.addUIPos("left_wing_1", 48, 60)
				.addUIPos("left_wing_2", 68, 60)
				.addUIPos("right_wing_1", 48, 60)
				.addUIPos("right_wing_2", 68, 60)
				.addUIPos("frame_1", 48, 80)
				.addUIPos("internal_1", 48, 100)
				.addUIPos("internal_2", 68, 100)
				.addUIPos("internal_3", 88, 100)
				.addUIPos("internal_4", 108, 100)
				.addUIPos("internal_5", 128, 100)
				.addUIPos("internal_6", 148, 100)
				.addUIPos("internal_7", 168, 100)
				.addUIPos("internal_8", 188, 100)
				.build());
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "axcel_truck")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/axcel_truck.png")
				.makeOneTexture("dscombat:textures/entity/axcel_truck/brown")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, 48, 20)
				.addUIPos("seat2", 68, 20)
				.addUIPos("cargo_bed_1", 48, 80)
				.addUIPos("internal_1", 48, 100)
				.addUIPos("internal_2", 68, 100)
				.addUIPos("internal_3", 88, 100)
				.addUIPos("internal_4", 108, 100)
				.build());
	}
	
	public AircraftClientPresetGenerator(DataGenerator output) {
		super(output, "aircraft_client", DataGenerator.Target.RESOURCE_PACK);
	}

	@Override
	public String getName() {
		return "Aircraft Client: "+DSCombatMod.MODID;
	}

}
