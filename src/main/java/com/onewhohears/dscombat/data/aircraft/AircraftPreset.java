package com.onewhohears.dscombat.data.aircraft;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class AircraftPreset {
	
	private final ResourceLocation key;
	private final JsonObject data = new JsonObject();
	private CompoundTag dataNBT;
	
	private AircraftPreset(ResourceLocation key) {
		this.key = key;
	}
	
	public CompoundTag getDataAsNBT() {
		return dataNBT;
	}
	
	public JsonObject getDataAsJson() {
		return data;
	}
	
	public ResourceLocation getKey() {
		return key;
	}
	
	public static class Builder {
		
		private final AircraftPreset preset;
		
		private Builder(ResourceLocation key) {
			this.preset = new AircraftPreset(key);
		}
		
		public Builder create(ResourceLocation key) {
			return new Builder(key);
		}
		
		public AircraftPreset build() {
			preset.dataNBT = UtilParse.getCompoundFromJson(preset.data);
			return preset;
		}
		
		public Builder setBoolean(String key, boolean value) {
			this.preset.data.addProperty(key, value);
			return this;
		}
		
		public Builder setInt(String key, int value) {
			this.preset.data.addProperty(key, value);
			return this;
		}
		
		public Builder setFloat(String key, float value) {
			this.preset.data.addProperty(key, value);
			return this;
		}
		
		public Builder setString(String key, String value) {
			this.preset.data.addProperty(key, value);
			return this;
		}
		
	}
	
}
