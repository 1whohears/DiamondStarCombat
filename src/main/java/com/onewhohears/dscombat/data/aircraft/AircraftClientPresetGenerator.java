package com.onewhohears.dscombat.data.aircraft;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.JsonPresetGenerator;
import com.onewhohears.dscombat.data.parts.PartSlot;

import net.minecraft.data.DataGenerator;

public class AircraftClientPresetGenerator extends JsonPresetGenerator<AircraftClientPreset> {
	
	@Override
	protected void registerPresets() {
		int alexis_middle_x = 120, alexis_wing_y = 57;
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "alexis_plane")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/alexis_plane.png")
				.makeOneTexture("dscombat:textures/entity/alexis_plane/v3_temp")
				.addUIPos("frame_1", alexis_middle_x, 2)
				.addUIPos(PartSlot.PILOT_SLOT_NAME, alexis_middle_x, 21)
				.addUIPos("internal_4", alexis_middle_x-9, 40)
				.addUIPos("internal_5", alexis_middle_x+9, 40)
				.addUIPos("internal_6", alexis_middle_x, 58)
				.addUIPos("internal_3", alexis_middle_x+9, 78)
				.addUIPos("internal_2", alexis_middle_x-9, 78)
				.addUIPos("internal_1", alexis_middle_x, 100)
				.addUIPos("left_wing_1", alexis_middle_x-27, alexis_wing_y)
				.addUIPos("left_wing_2", alexis_middle_x-45, alexis_wing_y)
				.addUIPos("right_wing_1", alexis_middle_x+27, alexis_wing_y)
				.addUIPos("right_wing_2", alexis_middle_x+45, alexis_wing_y)
				.build());
		int javi_middle_x = 124, javi_wing_y = 50;
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "javi_plane")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/javi_plane.png")
				.makeOneTexture("dscombat:textures/entity/javi_plane/buh")
				.addUIPos("frame_1", javi_middle_x+18, 0)
				.addUIPos(PartSlot.PILOT_SLOT_NAME, javi_middle_x, 9)
				.addUIPos("seat2", javi_middle_x, 27)
				.addUIPos("internal_4", javi_middle_x+9, 70)
				.addUIPos("internal_5", javi_middle_x-9, 70)
				.addUIPos("internal_6", javi_middle_x+9, 106)
				.addUIPos("internal_3", javi_middle_x-9, 106)
				.addUIPos("internal_1", javi_middle_x+12, 88)
				.addUIPos("internal_2", javi_middle_x-12, 88)
				.addUIPos("frame_2", javi_middle_x, javi_wing_y)
				.addUIPos("left_wing_1", javi_middle_x-18, javi_wing_y)
				.addUIPos("left_wing_2", javi_middle_x-36, javi_wing_y)
				.addUIPos("left_wing_3", javi_middle_x-54, javi_wing_y)
				.addUIPos("left_wing_4", javi_middle_x-72, javi_wing_y)
				.addUIPos("right_wing_1", javi_middle_x+18, javi_wing_y)
				.addUIPos("right_wing_2", javi_middle_x+36, javi_wing_y)
				.addUIPos("right_wing_3", javi_middle_x+54, javi_wing_y)
				.addUIPos("right_wing_4", javi_middle_x+72, javi_wing_y)
				.build());
		int noah_middle_x = 120, noah_seat1_y = 14;
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "noah_chopper")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/noah_chopper.png")
				.makeOneTexture("dscombat:textures/entity/noah_chopper/noah_chopper_test")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, noah_middle_x-18, noah_seat1_y)
				.addUIPos("seat2", noah_middle_x+18, noah_seat1_y)
				.addUIPos("seat3", noah_middle_x-18, noah_seat1_y+18)
				.addUIPos("seat4", noah_middle_x+18, noah_seat1_y+18)
				.addUIPos("frame_1", noah_middle_x, noah_seat1_y)
				.addUIPos("frame_2", noah_middle_x, noah_seat1_y+18)
				.addUIPos("frame_3", noah_middle_x, noah_seat1_y+18*2)
				.addUIPos("frame_4", noah_middle_x, noah_seat1_y+18*3)
				.addUIPos("left_wing_1", noah_middle_x-36, noah_seat1_y)
				.addUIPos("left_wing_2", noah_middle_x-36, noah_seat1_y+18)
				.addUIPos("left_wing_3", noah_middle_x-36, noah_seat1_y+18*2)
				.addUIPos("left_wing_4", noah_middle_x-36, noah_seat1_y+18*3)
				.addUIPos("right_wing_1", noah_middle_x+36, noah_seat1_y)
				.addUIPos("right_wing_2", noah_middle_x+36, noah_seat1_y+18)
				.addUIPos("right_wing_3", noah_middle_x+36, noah_seat1_y+18*2)
				.addUIPos("right_wing_4", noah_middle_x+36, noah_seat1_y+18*3)
				.addUIPos("internal_1", noah_middle_x-18, noah_seat1_y+18*2)
				.addUIPos("internal_2", noah_middle_x+18, noah_seat1_y+18*2)
				.addUIPos("internal_3", noah_middle_x-18, noah_seat1_y+18*3)
				.addUIPos("internal_4", noah_middle_x+18, noah_seat1_y+18*3)
				.addUIPos("internal_5", noah_middle_x-9, noah_seat1_y+18*4)
				.addUIPos("internal_6", noah_middle_x+9, noah_seat1_y+18*4)
				.addUIPos("internal_7", noah_middle_x-9, noah_seat1_y+18*5)
				.addUIPos("internal_8", noah_middle_x+9, noah_seat1_y+18*5)
				.build());
		int bud_middle_x = 120;
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "mrbudger_tank")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/mrbudger_tank.png")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, bud_middle_x, 50)
				.addUIPos("seat1", bud_middle_x+20, 12)
				.addUIPos("seat2", bud_middle_x-20, 12)
				.addUIPos("seat3", bud_middle_x+20, 90)
				.addUIPos("seat4", bud_middle_x-20, 90)
				.addUIPos("internal_1", bud_middle_x-9, 30)
				.addUIPos("internal_2", bud_middle_x+9, 30)
				.addUIPos("internal_3", bud_middle_x-9, 70)
				.addUIPos("internal_4", bud_middle_x+9, 70)
				.build());
		int roller_middle_x = 120;
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "small_roller")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/small_roller.png")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, roller_middle_x, 50)
				.addUIPos("internal_1", roller_middle_x-9, 70)
				.addUIPos("internal_2", roller_middle_x+9, 70)
				.makeOneTexture("dscombat:textures/entity/small_roller/gray")
				.build());
		int nathan_middle_x = 120;
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "nathan_boat")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/nathan_boat.png")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, nathan_middle_x-18, 42+18*3)
				.addUIPos("seat1", nathan_middle_x-18, 42)
				.addUIPos("seat2", nathan_middle_x+18, 42)
				.addUIPos("seat3", nathan_middle_x-18, 42+18)
				.addUIPos("seat4", nathan_middle_x+18, 42+18)
				.addUIPos("seat5", nathan_middle_x-18, 42+18*2)
				.addUIPos("seat6", nathan_middle_x+18, 42+18*2)
				.addUIPos("frame_1", nathan_middle_x-54, 60)
				.addUIPos("frame_2", nathan_middle_x+54, 60)
				.addUIPos("internal_1", nathan_middle_x, 42+18*3)
				.addUIPos("internal_2", nathan_middle_x, 42+18*2)
				.addUIPos("internal_3", nathan_middle_x, 42+18)
				.addUIPos("internal_4", nathan_middle_x, 42)
				.build());
		int andolf_middle_x = 117;
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "andolf_sub")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/andolf_sub.png")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, andolf_middle_x-9, 25)
				.addUIPos("seat1", andolf_middle_x+9, 25)
				.addUIPos("seat2", andolf_middle_x-9, 25+18)
				.addUIPos("seat3", andolf_middle_x+9, 25+18)
				.addUIPos("seat4", andolf_middle_x-9, 25+36)
				.addUIPos("seat5", andolf_middle_x+9, 25+36)
				.addUIPos("nose_1", andolf_middle_x, 7)
				.addUIPos("frame_1", andolf_middle_x-42, 35)
				.addUIPos("frame_2", andolf_middle_x-42, 35+18)
				.addUIPos("frame_3", andolf_middle_x-42, 35+36)
				.addUIPos("frame_4", andolf_middle_x+42, 35)
				.addUIPos("frame_5", andolf_middle_x+42, 35+18)
				.addUIPos("frame_6", andolf_middle_x+42, 35+36)
				.addUIPos("internal_1", andolf_middle_x-18, 80)
				.addUIPos("internal_2", andolf_middle_x, 80)
				.addUIPos("internal_3", andolf_middle_x+18, 80)
				.addUIPos("internal_4", andolf_middle_x-18, 98)
				.addUIPos("internal_5", andolf_middle_x, 98)
				.addUIPos("internal_6", andolf_middle_x+18, 98)
				.build());
		int orange_middle_x = 120;
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "orange_tesla")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/orange_tesla.png")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, orange_middle_x-18, 50)
				.addUIPos("seat1", orange_middle_x+18, 50)
				.addUIPos("seat2", orange_middle_x-18, 70)
				.addUIPos("seat3", orange_middle_x+18, 70)
				.addUIPos("internal_1", orange_middle_x, 25)
				.addUIPos("internal_2", orange_middle_x, 45)
				.makeOneTexture("dscombat:textures/entity/orange_tesla/orange")
				.build());
		int wood_middle_x = 118;
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "wooden_plane")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/wooden_plane.png")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, wood_middle_x, 43)
				.addUIPos("left_wing_1", wood_middle_x-30, 43)
				.addUIPos("right_wing_1", wood_middle_x+30, 43)
				.addUIPos("internal_1", wood_middle_x, 25)
				.addUIPos("internal_2", wood_middle_x, 61)
				.addUIPos("internal_3", wood_middle_x, 79)
				.makeOneTexture("dscombat:textures/entity/wooden_plane/brown")
				.build());
		int e3sentry_middle_x = 120;
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "e3sentry_plane")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/e3sentry_plane.png")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, e3sentry_middle_x-18, 5)
				.addUIPos("seat2", e3sentry_middle_x-18, 5+18)
				.addUIPos("seat3", e3sentry_middle_x-18, 5+18*2)
				.addUIPos("seat4", e3sentry_middle_x-18, 5+18*3)
				.addUIPos("seat5", e3sentry_middle_x-18, 5+18*4)
				.addUIPos("seat6", e3sentry_middle_x-18, 5+18*5)
				.addUIPos("seat1", e3sentry_middle_x+18, 5)
				.addUIPos("seat7", e3sentry_middle_x+18, 5+18)
				.addUIPos("seat8", e3sentry_middle_x+18, 5+18*2)
				.addUIPos("seat9", e3sentry_middle_x+18, 5+18*3)
				.addUIPos("seat10", e3sentry_middle_x+18, 5+18*4)
				.addUIPos("seat11", e3sentry_middle_x+18, 5+18*5)
				.addUIPos("left_wing_1", e3sentry_middle_x-40, 50)
				.addUIPos("left_wing_2", e3sentry_middle_x-60, 50)
				.addUIPos("right_wing_1", e3sentry_middle_x+40, 50)
				.addUIPos("right_wing_2", e3sentry_middle_x+60, 50)
				.addUIPos("frame_1", e3sentry_middle_x, 5+18*3)
				.addUIPos("internal_1", e3sentry_middle_x, 5)
				.addUIPos("internal_2", e3sentry_middle_x, 5+18)
				.addUIPos("internal_3", e3sentry_middle_x, 5+18*2)
				.addUIPos("internal_4", e3sentry_middle_x, 5+18*4)
				.addUIPos("internal_5", e3sentry_middle_x, 5+18*5)
				.addUIPos("internal_6", e3sentry_middle_x-36, 5+18*5)
				.addUIPos("internal_7", e3sentry_middle_x, 5+18*6)
				.addUIPos("internal_8", e3sentry_middle_x+36, 5+18*5)
				.build());
		int axcel_middle_x = 120;
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "axcel_truck")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/axcel_truck.png")
				.makeOneTexture("dscombat:textures/entity/axcel_truck/brown")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, axcel_middle_x-9, 20)
				.addUIPos("seat2", axcel_middle_x+9, 20)
				.addUIPos("frame_1", axcel_middle_x, 40)
				.addUIPos("cargo_bed_1", axcel_middle_x, 80)
				.addUIPos("internal_1", axcel_middle_x-9, 60)
				.addUIPos("internal_2", axcel_middle_x+9, 60)
				.addUIPos("internal_3", axcel_middle_x-9, 100)
				.addUIPos("internal_4", axcel_middle_x+9, 100)
				.build());
		int gronk_middle_x = 120;
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "gronk_battleship")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/gronk_battleship.png")
				.makeOneTexture("dscombat:textures/entity/boats/battleship")
				.addUIPos(PartSlot.PILOT_SLOT_NAME, gronk_middle_x-9, 40)
				.addUIPos("seat2", gronk_middle_x+9, 40)
				.addUIPos("seat3", gronk_middle_x-9, 40+18*2)
				.addUIPos("seat4", gronk_middle_x+9, 40+18*2)
				.addUIPos("seat5", gronk_middle_x-9, 40-18)
				.addUIPos("seat6", gronk_middle_x-9, 40+18*3)
				.addUIPos("seat7", gronk_middle_x, 40-18*2)
				.addUIPos("seat8", gronk_middle_x, 40+18*4)
				.addUIPos("seat9", gronk_middle_x+9, 40-18)
				.addUIPos("seat10", gronk_middle_x+9, 40+18*3)
				.addUIPos("frame_1", gronk_middle_x, 40+18)
				.addUIPos("frame_2", gronk_middle_x-45, 40)
				.addUIPos("frame_3", gronk_middle_x+45, 40)
				.addUIPos("frame_4", gronk_middle_x-45, 40+18*2)
				.addUIPos("frame_5", gronk_middle_x+45, 40+18*2)
				.addUIPos("internal_1", gronk_middle_x-27, 40+18*3)
				.addUIPos("internal_2", gronk_middle_x+27, 40+18*3)
				.addUIPos("internal_3", gronk_middle_x-27, 40)
				.addUIPos("internal_4", gronk_middle_x+27, 40)
				.addUIPos("internal_5", gronk_middle_x-27, 40+18*2)
				.addUIPos("internal_6", gronk_middle_x+27, 40+18*2)
				.addUIPos("internal_7", gronk_middle_x-18*2, 40+18)
				.addUIPos("internal_8", gronk_middle_x-18, 40+18)
				.addUIPos("internal_9", gronk_middle_x+18, 40+18)
				.addUIPos("internal_10", gronk_middle_x+18*2, 40+18)
				.build());
		int x_start = 48, y_start = 10;
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "destroyer")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/gronk_battleship.png")
				.makeOneTexture("dscombat:textures/entity/boats/battleship")
				.setAllUIPos(x_start, y_start, 8, 
					PartSlot.PILOT_SLOT_NAME, "seat2", "seat3", "seat4", 
					"seat5", "seat6", "seat7", "seat8", "seat9", "seat10", 
					"frame_1", "frame_2", "frame_3", "frame_4", "frame_5", "frame_6",
					"internal_1", "internal_2", "internal_3", "internal_4", "internal_5", 
					"internal_6", "internal_7", "internal_8", "internal_9", "internal_10")
				.build());
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "cruiser")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/gronk_battleship.png")
				.makeOneTexture("dscombat:textures/entity/boats/battleship")
				.setAllUIPos(x_start, y_start, 8, 
					PartSlot.PILOT_SLOT_NAME, "seat2", "seat3", "seat4", 
					"seat5", "seat6", "seat7", "seat8", "seat9", "seat10", 
					"frame_1", "frame_2", "frame_3", "frame_4", "frame_5", "frame_6",
					"internal_1", "internal_2", "internal_3", "internal_4", "internal_5", 
					"internal_6", "internal_7", "internal_8", "internal_9", "internal_10")
				.build());
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "corvette")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/gronk_battleship.png")
				.makeOneTexture("dscombat:textures/entity/boats/battleship")
				.setAllUIPos(x_start, y_start, 8, 
					PartSlot.PILOT_SLOT_NAME, "seat2", "seat3", "seat4", 
					"seat5", "seat6", "seat7", "seat8", "seat9", "seat10", 
					"frame_1", "frame_2", "frame_3", "frame_4", "frame_5", "frame_6",
					"internal_1", "internal_2", "internal_3", "internal_4", "internal_5", 
					"internal_6", "internal_7", "internal_8", "internal_9", "internal_10")
				.build());
		addPresetToGenerate(AircraftClientPreset.Builder.create(DSCombatMod.MODID, "aircraft_carrier")
				.setBackground("dscombat:textures/ui/vehicle_inventory_backgrounds/gronk_battleship.png")
				.makeOneTexture("dscombat:textures/entity/boats/battleship")
				.setAllUIPos(x_start, y_start, 8, 
					PartSlot.PILOT_SLOT_NAME, "seat2", "seat3", "seat4", 
					"seat5", "seat6", "seat7", "seat8", "seat9", "seat10", 
					"frame_1", "frame_2", "frame_3", "frame_4", "frame_5", "frame_6",
					"internal_1", "internal_2", "internal_3", "internal_4", "internal_5", 
					"internal_6", "internal_7", "internal_8", "internal_9", "internal_10")
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
