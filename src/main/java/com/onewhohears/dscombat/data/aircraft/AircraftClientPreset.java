package com.onewhohears.dscombat.data.aircraft;

import java.util.HashMap;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.JsonPreset;
import com.onewhohears.dscombat.data.PresetBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class AircraftClientPreset extends JsonPreset {
	
	private static final UIPos defaultPos = new UIPos(0, 0);
	
	private ResourceLocation background;
	private HashMap<String, UIPos> slotsPos;
	private AircraftTextures textures;
	
	public AircraftClientPreset(ResourceLocation key, JsonObject json) {
		super(key, json);
	}
	
	public AircraftTextures getAircraftTextures() {
		if (textures == null) {
			textures = new AircraftTextures(getJsonData());
		}
		return textures;
	}
	
	@Nullable
	public ResourceLocation getBackground() {
		if (!getJsonData().has("inventory_background")) return null;
		if (background == null) {
			background = new ResourceLocation(getJsonData().get("inventory_background").getAsString());
		}
		return background;
	}
	
	public HashMap<String, UIPos> getSlotsPos() {
		if (slotsPos == null) {
			slotsPos = new HashMap<>();
			JsonArray isp = getJsonData().get("inventory_slots_pos").getAsJsonArray();
			for (int i = 0; i < isp.size(); ++i) {
				JsonObject sp = isp.get(i).getAsJsonObject();
				slotsPos.put(sp.get("slot_name").getAsString(), new UIPos(sp));
			}
		}
		return slotsPos;
	}
	
	public UIPos getSlotPos(String slotId) {
		if (!getSlotsPos().containsKey(slotId)) return defaultPos;
		return getSlotsPos().get(slotId);
	}
	
	public static class Builder extends PresetBuilder<Builder> {
		
		@Override
		protected void setupJsonData() {
			super.setupJsonData();
			JsonObject textures = new JsonObject();
			textures.addProperty("default_texture_path", "dscombat:textures/entity/"+getPresetId()+"/");
			textures.add("overrides", new JsonObject());
			getData().add("textures", textures);
		}
		
		public Builder setBackground(String background) {
			return setString("inventory_background", background);
		}
		
		public Builder addUIPos(String name, int x, int y) {
			if (!getData().has("inventory_slots_pos")) {
				getData().add("inventory_slots_pos", new JsonArray());
			}
			JsonArray isp = getData().get("inventory_slots_pos").getAsJsonArray();
			JsonObject sp = new JsonObject();
			sp.addProperty("slot_name", name);
			sp.addProperty("slot_ui_x", x);
			sp.addProperty("slot_ui_y", y);
			isp.add(sp);
			return this;
		}
		
		public Builder makeOneTexture(String path) {
			JsonObject overrides = getData().getAsJsonObject("textures").getAsJsonObject("overrides");
			for (int i = 0; i < DyeColor.values().length; ++i) {
				overrides.addProperty(DyeColor.values()[i].getName(), path);
			}
			return this;
		}
		
		protected Builder(String namespace, String name, JsonPresetFactory<? extends JsonPreset> sup) {
			super(namespace, name, sup);
		}
		
		protected Builder(String namespace, String name, JsonPresetFactory<? extends JsonPreset> sup, AircraftClientPreset copy) {
			super(namespace, name, sup, copy.getJsonData().deepCopy());
		}
		
		public static Builder create(String namespace, String name) {
			return new Builder(namespace, name, (key, json) -> new AircraftClientPreset(key, json));
		}
		
		public static Builder createFromCopy(String namespace, String name, AircraftClientPreset copy) {
			return new Builder(namespace, name, (key, json) -> new AircraftClientPreset(key, json), copy);
		}
		
	}

	@Override
	public <T extends JsonPreset> T copy() {
		return (T) new AircraftClientPreset(getKey(), getJsonData());
	}
	
	public static class UIPos {
		private final int x, y;
		public UIPos(int x, int y) {
			this.x = x;
			this.y = y;
		}
		protected UIPos(JsonObject json) {
			x = json.get("slot_ui_x").getAsInt();
			y = json.get("slot_ui_y").getAsInt();
		}
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
	}

}
