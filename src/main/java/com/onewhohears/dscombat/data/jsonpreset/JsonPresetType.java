package com.onewhohears.dscombat.data.jsonpreset;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public abstract class JsonPresetType {
	
	private static int defaultSortFactorCount = 0;
	
	private final String id, displayName;
	private final JsonPresetStatsFactory<? extends JsonPresetStats> statsFactory;
	private final int sortFactor;
	
	public JsonPresetType(String id, JsonPresetStatsFactory<? extends JsonPresetStats> statsFactory, String displayName) {
		this.id = id;
		this.displayName = displayName;
		this.statsFactory = statsFactory;
		this.sortFactor = defaultSortFactorCount++;
	}
	
	public JsonPresetType(String id, JsonPresetStatsFactory<? extends JsonPresetStats> statsFactory) {
		this(id, statsFactory, "preset.type."+id);
	}
	
	public String getId() {
		return id;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public MutableComponent getDisplayNameComponent() {
		String dn = getDisplayName();
		if (dn.startsWith("preset.")) return UtilMCText.translatable(dn);
		return UtilMCText.literal(dn);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof JsonPresetType jpt) return getId().equals(jpt.getId());
		return false;
	}
	
	public boolean is(JsonPresetType type) {
		return getId().equals(type.getId());
	}
	
	public int getSortFactor() {
		return sortFactor;
	}
	
	@Override
	public String toString() {
		return "Preset Type: "+getId();
	}
	
	public <T extends JsonPresetStats> T createStats(ResourceLocation key, JsonObject data) {
		return (T) statsFactory.create(key, data);
	}

	public interface JsonPresetStatsFactory<T extends JsonPresetStats> {
		T create(ResourceLocation key, JsonObject data);
	}
	
}
