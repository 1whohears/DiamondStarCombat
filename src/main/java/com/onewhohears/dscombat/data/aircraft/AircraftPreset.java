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
		
		public static Builder create(ResourceLocation key) {
			return new Builder(key);
		}
		
		public AircraftPreset build() {
			preset.dataNBT = UtilParse.getCompoundFromJson(preset.data);
			return preset;
		}
		
		/**
		 * all vehicles
		 */
		public Builder setMaxSpeed(float max_speed) {
			return setFloat("max_speed", max_speed);
		}
		
		/**
		 * all vehicles
		 */
		public Builder setMaxHealth(float max_health) {
			setFloat("health", max_health);
			return setFloat("max_health", max_health);
		}
		
		/**
		 * all vehicles
		 */
		public Builder setMass(float mass) {
			return setFloat("mass", mass);
		}
		
		/**
		 * used by planes
		 */
		public Builder setWingArea(float wing_area) {
			return setFloat("wing_area", wing_area);
		}
		
		/**
		 * all vehicles
		 */
		public Builder setStealth(float stealth) {
			return setFloat("stealth", stealth);
		}
		
		/**
		 * all vehicles
		 */
		public Builder setIdleHeat(float idleheat) {
			return setFloat("idleheat", idleheat);
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
