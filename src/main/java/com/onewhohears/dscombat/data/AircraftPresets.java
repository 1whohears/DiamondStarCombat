package com.onewhohears.dscombat.data;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.onewhohears.dscombat.data.parts.EngineData;
import com.onewhohears.dscombat.data.parts.FuelTankData;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.parts.PartsManager;
import com.onewhohears.dscombat.data.parts.RadarPartData;
import com.onewhohears.dscombat.data.parts.SeatData;
import com.onewhohears.dscombat.data.parts.WeaponRackData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;

public class AircraftPresets {
	
	public static List<CompoundTag> presets = new ArrayList<CompoundTag>();
	
	public static void setupPresets() {
		String dir = "/data/dscombat/aircraft/";
		JsonArray ja = UtilParse.getJsonFromResource(dir+"aircraft.json").get("aircraft").getAsJsonArray();
		for (int i = 0; i < ja.size(); ++i) 
			presets.add(UtilParse.getCompoundFromJsonResource(dir+ja.get(i).getAsString()));
	}
	
	@Nullable
	public static CompoundTag getPreset(String preset) {
		for (CompoundTag tag : presets) if (tag.getString("preset").equals(preset)) return tag;
		return null;
	}
	
}
