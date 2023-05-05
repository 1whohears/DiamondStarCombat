package com.onewhohears.dscombat.data.aircraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class AircraftPresets extends SimpleJsonResourceReloadListener {
	
	private static AircraftPresets instance;
	
	public static AircraftPresets get() {
		if (instance == null) instance = new AircraftPresets();
		return instance;
	}
	
	public static void close() {
		instance = null;
	}
	
	private final Map<String, AircraftPreset> presets = new HashMap<>();
	private boolean setup = false;
	
	private AircraftPreset[] craftablePresets;
	
	public AircraftPresets() {
		super(UtilParse.GSON, "aircraft");
	}
	
	@Nullable
	public AircraftPreset getAircraftPreset(String id) {
		return presets.get(id);
	}
	
	public AircraftPreset[] getAllPresets() {
		return presets.values().toArray(new AircraftPreset[presets.size()]);
	}
	
	public AircraftPreset[] getCraftablePresets() {
		if (craftablePresets == null) {
			List<AircraftPreset> list = new ArrayList<>();
			presets.forEach((name, preset) -> {
				if (preset.isCraftable()) list.add(preset);
			});
			craftablePresets = list.toArray(new AircraftPreset[list.size()]);
		}
		return craftablePresets;
	}
	
	public int getAllPresetNum() {
		return presets.size();
	}
	
	public int getCraftablePresetNum() {
		return getCraftablePresets().length;
	}
	
	@Override
	protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
		System.out.println("APPLYING AIRCRAFT PRESETS TO COMMON CACHE");
		setup = false;
		presets.clear();
		craftablePresets = null;
		map.forEach((key, je) -> {
			System.out.println("ADDING PRESET: "+key.toString()+" "+je.toString());
			JsonObject data = UtilParse.GSON.fromJson(je, JsonObject.class);
			AircraftPreset preset = new AircraftPreset(key, data);
			if (!presets.containsKey(preset.getPresetId())) {
				presets.put(preset.getPresetId(), preset);
			} else {
				System.out.println("ERROR: Can't have 2 presets with the same name! "+key.toString());
			}
		});
		setup = true;
	}
	
	public boolean isSetup() {
		return setup;
	}
	
	public void writeBuffer(FriendlyByteBuf buffer) {
		buffer.writeInt(getAllPresetNum());
		AircraftPreset[] aps = getAllPresets();
		for (int i = 0; i < aps.length; ++i) {
			aps[i].writeBuffer(buffer);
		}
	}
	
	public static AircraftPreset[] readBuffer(FriendlyByteBuf buffer) {
		int length = buffer.readInt();
		AircraftPreset[] aps = new AircraftPreset[length];
		for (int i = 0; i < length; ++i) {
			aps[i] = AircraftPreset.readBuffer(buffer);
		}
		return aps;
	}
	
	public void manuallySetPresets(AircraftPreset[] presetList) {
		System.out.println("SET COMMON AIRCRAFT PRESET CACHE");
		setup = false;
		presets.clear();
		craftablePresets = null;
		for (int i = 0; i < presetList.length; ++i) {
			System.out.println("ADDING PRESET: "+presetList[i].toString());
			presets.put(presetList[i].getPresetId(), presetList[i]);
		}
		setup = true;
	}
	
}
