package com.onewhohears.dscombat.data;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.EngineData;
import com.onewhohears.dscombat.data.parts.FuelTankData;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.parts.PartsManager;
import com.onewhohears.dscombat.data.parts.RadarPartData;
import com.onewhohears.dscombat.data.parts.SeatData;
import com.onewhohears.dscombat.data.parts.WeaponPartData;
import com.onewhohears.dscombat.data.parts.WeaponRackData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.nbt.CompoundTag;

public class AircraftPresets {
	
	public static List<CompoundTag> presets = new ArrayList<CompoundTag>();
	
	public static void setupPresets() {
		presets.add(TestPreset());
		presets.add(JaviPreset());
		presets.add(AlexisPreset());
		presets.add(NoahPreset());
		// TODO check for a text file with the names of all the presets to read
	}
	
	@Nullable
	public static CompoundTag getPreset(String preset) {
		for (CompoundTag tag : presets) if (tag.getString("preset").equals(preset)) return tag;
		return null;
	}
	
	public static CompoundTag TestPreset() {
		System.out.println("CREATING TEST PRESET");
		CompoundTag tag = new CompoundTag();
		// parts
		PartsManager pm = new PartsManager();
		pm.addSlot(PartSlot.PILOT_SLOT_NAME, SlotType.SEAT, 0, -0.8, 0, 48, 20);
		pm.addSlot("dscombat.seat2", SlotType.SEAT, 1, -0.5, 1, 68, 20);
		pm.addSlot("dscombat.seat3", SlotType.SEAT, -1, -0.5, 1, 88, 20);
		pm.addSlot("dscombat.left_wing_1", SlotType.WING, -0.8, 0, 1, 48, 40);
		pm.addSlot("dscombat.left_wing_2", SlotType.WING, -1.2, 0, 1, 68, 40);
		pm.addSlot("dscombat.left_wing_3", SlotType.WING, -1.6, 0, 1, 88, 40);
		pm.addSlot("dscombat.left_wing_4", SlotType.WING, -2.0, 0, 1, 108, 40);
		pm.addSlot("dscombat.right_wing_1", SlotType.WING, 0.8, 0, 1, 48, 60);
		pm.addSlot("dscombat.right_wing_2", SlotType.WING, 1.2, 0, 1, 68, 60);
		pm.addSlot("dscombat.right_wing_3", SlotType.WING, 1.6, 0, 1, 88, 60);
		pm.addSlot("dscombat.right_wing_4", SlotType.WING, 2.0, 0, 1, 108, 60);
		pm.addSlot("dscombat.internal_1", SlotType.INTERNAL, 0, 0, 1, 48, 80);
		pm.addSlot("dscombat.internal_2", SlotType.INTERNAL, 0, 0, 1, 68, 80);
		pm.addSlot("dscombat.internal_3", SlotType.INTERNAL, 0, 0, 1, 88, 80);
		pm.addSlot("dscombat.internal_4", SlotType.INTERNAL, 0, 0, 1, 108, 80);
		pm.addPart(new SeatData(0.003f, ModItems.SEAT.getId()), 
				PartSlot.PILOT_SLOT_NAME, false);
		pm.addPart(new SeatData(0.003f, ModItems.SEAT.getId()), 
				"dscombat.seat2", false);
		pm.addPart(new SeatData(0.003f, ModItems.SEAT.getId()), 
				"dscombat.seat3", false);
		pm.addPart(new WeaponRackData(0.005f,  "agm84e", 
				WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId()), 
				"dscombat.left_wing_1", false);
		pm.addPart(new WeaponRackData(0.005f, "aim120c", 
				WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId()), 
				"dscombat.right_wing_1", false);
		pm.addPart(new WeaponRackData(0.005f, "aim9x", 
				WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId()), 
				"dscombat.left_wing_2", false);
		pm.addPart(new WeaponRackData(0.005f, "agm114k", 
				WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId()), 
				"dscombat.right_wing_2", false);
		pm.addPart(new WeaponPartData(0.002f, "bullet_1", 
				WeaponPresets.TEST_BIG_GUN, ModItems.TEST_BIG_GUN.getId()), 
				"dscombat.internal_1", false);
		pm.addPart(new FuelTankData(0.001f, 100f, 100f,
				ModItems.TEST_TANK.getId()), 
				"dscombat.internal_2", false);
		pm.addPart(new EngineData(0.01f, 0.04f, 4f, 0.005f, 
						ModItems.TEST_ENGINE.getId()), 
				"dscombat.left_wing_4", false);
		pm.addPart(new RadarPartData(0.001f, "test_air", ModItems.TEST_AIR_RADAR.getId()), 
				"dscombat.internal_3", false);
		pm.addPart(new RadarPartData(0.001f, "test_ground", ModItems.TEST_GROUND_RADAR.getId()), 
				"dscombat.internal_4", false);
		pm.write(tag);
		//System.out.println(pm);
		// other
		tag.putString("preset", "test_plane");
		tag.putFloat("max_speed", 1.5f);
		tag.putFloat("max_health", 200);
		tag.putFloat("health", 200);
		tag.putInt("flares", 1000);
		tag.putFloat("stealth", 1);
		tag.putFloat("maxroll", 8);
		tag.putFloat("maxpitch", 4);
		tag.putFloat("maxyaw", 4);
		tag.putFloat("throttleup", 0.04f);
		tag.putFloat("throttledown", 0.04f);
		tag.putFloat("idleheat", 8f);
		tag.putFloat("weight", 0.04f);
		tag.putFloat("surfacearea", 1f);
		tag.putBoolean("landing_gear", true);
		return tag;
	}
	
	public static CompoundTag JaviPreset() {
		System.out.println("CREATING JAVI PRESET");
		CompoundTag tag = new CompoundTag();
		// parts
		PartsManager pm = new PartsManager();
		pm.addSlot(PartSlot.PILOT_SLOT_NAME, SlotType.SEAT, 0, -0.8, 0, 48, 20);
		pm.addSlot("dscombat.seat2", SlotType.SEAT, 1, -0.5, 1, 68, 20);
		pm.addSlot("dscombat.seat3", SlotType.SEAT, -1, -0.5, 1, 88, 20);
		pm.addSlot("dscombat.left_wing_1", SlotType.WING, -0.9, 0, 1, 48, 40);
		pm.addSlot("dscombat.left_wing_2", SlotType.WING, -1.8, 0, 1, 68, 40);
		pm.addSlot("dscombat.right_wing_1", SlotType.WING, 0.9, 0, 1, 88, 40);
		pm.addSlot("dscombat.right_wing_2", SlotType.WING, 1.8, 0, 1, 108, 40);
		pm.addSlot("dscombat.frame_rear", SlotType.FRAME, 0, 0, -1, 48, 60);
		pm.addSlot("dscombat.frame_belly", SlotType.FRAME, 0, -0.5, 0, 68, 60);
		pm.addSlot("dscombat.internal_1", SlotType.INTERNAL, 0, 0, 1, 48, 80);
		pm.addSlot("dscombat.internal_2", SlotType.INTERNAL, 0, 0, 1, 68, 80);
		pm.addSlot("dscombat.internal_3", SlotType.INTERNAL, 0, 0, 1, 88, 80);
		pm.addSlot("dscombat.internal_4", SlotType.INTERNAL, 0, 0, 1, 108, 80);
		pm.addPart(new SeatData(0.001f, ModItems.SEAT.getId()), 
				PartSlot.PILOT_SLOT_NAME, false);
		pm.addPart(new SeatData(0.001f, ModItems.SEAT.getId()), 
				"dscombat.seat2", false);
		pm.addPart(new SeatData(0.001f, ModItems.SEAT.getId()), 
				"dscombat.seat3", false);
		pm.addPart(new WeaponPartData(0.002f, "bullet_2", 
				WeaponPresets.TEST_BIG_GUN, ModItems.TEST_BIG_GUN.getId()), 
				"dscombat.internal_1", false);
		pm.addPart(new WeaponRackData(0.004f, "aim120b", 
				WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId()), 
				"dscombat.right_wing_1", false);
		pm.addPart(new WeaponRackData(0.004f, "aim7e", 
				WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId()), 
				"dscombat.right_wing_2", false);
		pm.addPart(new WeaponRackData(0.004f, "agm84e", 
				WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId()), 
				"dscombat.left_wing_2", false);
		pm.addPart(new WeaponRackData(0.004f, "agm114k", 
				WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId()), 
				"dscombat.left_wing_1", false);
		pm.addPart(new FuelTankData(0.008f, 100f, 100f,
				ModItems.TEST_TANK.getId()), 
				"dscombat.internal_2", false);
		pm.addPart(new EngineData(0.008f, 0.06f, 4f, 0.005f, 
						ModItems.TEST_ENGINE.getId()), 
				"dscombat.frame_rear", false);
		pm.addPart(new RadarPartData(0.001f, "test_air", ModItems.TEST_AIR_RADAR.getId()), 
				"dscombat.internal_3", false);
		pm.addPart(new RadarPartData(0.001f, "test_ground", ModItems.TEST_GROUND_RADAR.getId()), 
				"dscombat.internal_4", false);
		pm.write(tag);
		// other
		tag.putString("preset", "javi");
		tag.putFloat("max_speed", 1.2f);
		tag.putFloat("max_health", 100);
		tag.putFloat("health", 100);
		//tag.putInt("flares", 100);
		tag.putFloat("stealth", 1);
		tag.putFloat("maxroll", 6.0f);
		tag.putFloat("maxpitch", 2.5f);
		tag.putFloat("maxyaw", 1.5f);
		tag.putFloat("throttleup", 0.04f);
		tag.putFloat("throttledown", 0.04f);
		tag.putFloat("idleheat", 8f);
		tag.putFloat("weight", 0.03f);
		tag.putFloat("surfacearea", 1.5f);
		tag.putBoolean("landing_gear", true);
		return tag;
	}
	
	public static CompoundTag AlexisPreset() {
		System.out.println("CREATING ALEXIS PRESET");
		CompoundTag tag = new CompoundTag();
		// parts
		PartsManager pm = new PartsManager();
		pm.addSlot(PartSlot.PILOT_SLOT_NAME, SlotType.SEAT, 0, -0.8, 0, 48, 20);
		pm.addSlot("dscombat.seat2", SlotType.SEAT, 1, -0.5, 1, 68, 20);
		pm.addSlot("dscombat.seat3", SlotType.SEAT, -1, -0.5, 1, 88, 20);
		pm.addSlot("dscombat.left_wing_1", SlotType.WING, -0.9, 0, 1, 48, 40);
		pm.addSlot("dscombat.left_wing_2", SlotType.WING, -1.8, 0, 1, 68, 40);
		pm.addSlot("dscombat.right_wing_1", SlotType.WING, 0.9, 0, 1, 88, 40);
		pm.addSlot("dscombat.right_wing_2", SlotType.WING, 1.8, 0, 1, 108, 40);
		pm.addSlot("dscombat.frame_rear", SlotType.FRAME, 0, 0, -1, 48, 60);
		pm.addSlot("dscombat.frame_belly", SlotType.FRAME, 0, -0.5, 0, 68, 60);
		pm.addSlot("dscombat.internal_1", SlotType.INTERNAL, 0, 0, 1, 48, 80);
		pm.addSlot("dscombat.internal_2", SlotType.INTERNAL, 0, 0, 1, 68, 80);
		pm.addSlot("dscombat.internal_3", SlotType.INTERNAL, 0, 0, 1, 88, 80);
		pm.addSlot("dscombat.internal_4", SlotType.INTERNAL, 0, 0, 1, 108, 80);
		pm.addPart(new SeatData(0.001f, ModItems.SEAT.getId()), 
				PartSlot.PILOT_SLOT_NAME, false);
		pm.addPart(new SeatData(0.001f, ModItems.SEAT.getId()), 
				"dscombat.seat2", false);
		pm.addPart(new SeatData(0.001f, ModItems.SEAT.getId()), 
				"dscombat.seat3", false);
		pm.addPart(new WeaponPartData(0.002f, "bullet_1", 
				WeaponPresets.TEST_BIG_GUN, ModItems.TEST_BIG_GUN.getId()), 
				"dscombat.internal_1", false);
		pm.addPart(new WeaponRackData(0.004f, "aim120c", 
				WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId()), 
				"dscombat.right_wing_1", false);
		pm.addPart(new WeaponRackData(0.004f, "agm65l", 
				WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId()), 
				"dscombat.right_wing_2", false);
		pm.addPart(new WeaponRackData(0.004f, "aim9x", 
				WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId()), 
				"dscombat.left_wing_2", false);
		pm.addPart(new WeaponRackData(0.004f, "aim120b", 
				WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId()), 
				"dscombat.left_wing_1", false);
		pm.addPart(new FuelTankData(0.008f, 100f, 100f,
				ModItems.TEST_TANK.getId()), 
				"dscombat.internal_2", false);
		pm.addPart(new EngineData(0.008f, 0.04f, 4f, 0.005f, 
						ModItems.TEST_ENGINE.getId()), 
				"dscombat.frame_rear", false);
		pm.addPart(new RadarPartData(0.001f, "test_air", ModItems.TEST_AIR_RADAR.getId()), 
				"dscombat.internal_3", false);
		pm.addPart(new RadarPartData(0.001f, "test_ground", ModItems.TEST_GROUND_RADAR.getId()), 
				"dscombat.internal_4", false);
		pm.write(tag);
		// other
		tag.putString("preset", "alexis");
		tag.putFloat("max_speed", 1.4f);
		tag.putFloat("max_health", 100);
		tag.putFloat("health", 100);
		//tag.putInt("flares", 100);
		tag.putFloat("stealth", 1);
		tag.putFloat("maxroll", 8);
		tag.putFloat("maxpitch", 2);
		tag.putFloat("maxyaw", 1);
		tag.putFloat("throttleup", 0.04f);
		tag.putFloat("throttledown", 0.04f);
		tag.putFloat("idleheat", 4f);
		tag.putFloat("weight", 0.04f);
		tag.putFloat("surfacearea", 1.2f);
		tag.putBoolean("landing_gear", true);
		return tag;
	}
	
	public static CompoundTag NoahPreset() {
		System.out.println("CREATING NOAH PRESET");
		CompoundTag tag = new CompoundTag();
		// parts
		PartsManager pm = new PartsManager();
		pm.addSlot(PartSlot.PILOT_SLOT_NAME, SlotType.SEAT, -0.4, -1.0, 1.5, 48, 20);
		pm.addSlot("dscombat.seat2", SlotType.SEAT, 0.4, -1.0, 1.5, 68, 20);
		pm.addSlot("dscombat.seat3", SlotType.SEAT, -0.4, -1.0, 0.4, 88, 20);
		pm.addSlot("dscombat.seat4", SlotType.SEAT, 0.4, -1.0, 0.4, 108, 20);
		pm.addSlot("dscombat.left_wing_1", SlotType.WING, -1.5, -0.5, 0, 48, 40);
		pm.addSlot("dscombat.left_wing_2", SlotType.WING, -1.5, 0.5, 0, 68, 40);
		pm.addSlot("dscombat.right_wing_1", SlotType.WING, 1.5, -0.5, 0, 88, 40);
		pm.addSlot("dscombat.right_wing_2", SlotType.WING, 1.5, 0.5, 0, 108, 40);
		pm.addSlot("dscombat.frame_rear", SlotType.FRAME, 0, 0, -1, 48, 60);
		pm.addSlot("dscombat.frame_belly", SlotType.FRAME, 0, -1.0, 0, 68, 60);
		pm.addSlot("dscombat.internal_1", SlotType.INTERNAL, 0, 0, 1, 48, 80);
		pm.addSlot("dscombat.internal_2", SlotType.INTERNAL, 0, 0, 1, 68, 80);
		pm.addSlot("dscombat.internal_3", SlotType.INTERNAL, 0, 0, 1, 88, 80);
		pm.addSlot("dscombat.internal_4", SlotType.INTERNAL, 0, 0, 1, 108, 80);
		pm.addPart(new SeatData(0.001f, ModItems.SEAT.getId()), 
				PartSlot.PILOT_SLOT_NAME, false);
		pm.addPart(new SeatData(0.001f, ModItems.SEAT.getId()), 
				"dscombat.seat2", false);
		pm.addPart(new SeatData(0.001f, ModItems.SEAT.getId()), 
				"dscombat.seat3", false);
		pm.addPart(new SeatData(0.001f, ModItems.SEAT.getId()), 
				"dscombat.seat4", false);
		pm.addPart(new WeaponRackData(0.004f, "agm84e", 
				WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId()), 
				"dscombat.right_wing_1", false);
		pm.addPart(new WeaponRackData(0.004f, "aim9x", 
				WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId()), 
				"dscombat.right_wing_2", false);
		pm.addPart(new WeaponRackData(0.004f, "agm84e", 
				WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId()), 
				"dscombat.left_wing_2", false);
		pm.addPart(new WeaponRackData(0.004f, "agm114k", 
				WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId()), 
				"dscombat.left_wing_1", false);
		pm.addPart(new FuelTankData(0.008f, 100f, 100f, 
				ModItems.TEST_TANK.getId()), 
				"dscombat.internal_2", false);
		pm.addPart(new EngineData(0.008f, 0.06f, 4f, 0.005f, 
				ModItems.TEST_ENGINE.getId()), 
				"dscombat.frame_rear", false);
		pm.addPart(new RadarPartData(0.001f, "test_ground", ModItems.TEST_GROUND_RADAR.getId()), 
				"dscombat.internal_4", false);
		pm.write(tag);
		// other
		tag.putString("preset", "noah");
		tag.putFloat("max_speed", 0.8f);
		tag.putFloat("max_health", 200);
		tag.putFloat("health", 200);
		//tag.putInt("flares", 100);
		tag.putFloat("stealth", 1);
		tag.putFloat("maxroll", 4.0f);
		tag.putFloat("maxpitch", 2.0f);
		tag.putFloat("maxyaw", 4.0f);
		tag.putFloat("throttleup", 0.02f);
		tag.putFloat("throttledown", 0.02f);
		tag.putFloat("idleheat", 8f);
		tag.putFloat("weight", 0.03f);
		tag.putFloat("surfacearea", 1.0f);
		tag.putBoolean("landing_gear", true);
		tag.putFloat("accForward", 0.05f);
		tag.putFloat("accSide", 0.01f);
		return tag;
	}
	
}
