package com.onewhohears.dscombat.data.aircraft;

import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.crafting.DSCIngredient;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class AircraftPreset {
	
	private final ResourceLocation key;
	private final JsonObject data;
	private final String presetId;
	private CompoundTag dataNBT;
	private List<DSCIngredient> ingredients;
	private ItemStack item;
	private AircraftTextures textures;
	
	private AircraftPreset(String namespace, String name) {
		this.key = new ResourceLocation(namespace, name);
		this.data = new JsonObject();
		this.presetId = name;
		this.data.addProperty("preset", name);
	}
	
	public AircraftPreset(ResourceLocation key, JsonObject data) {
		this.key = key;
		this.data = data;
		this.dataNBT = UtilParse.getCompoundFromJson(data);
		this.presetId = data.get("preset").getAsString();
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
	
	public String getPresetId() {
		return presetId;
	}
	
	public String getNameSpace() {
		return getKey().getNamespace();
	}
	
	public List<DSCIngredient> getIngredients() {
		if (ingredients == null) {
			ingredients = DSCIngredient.getIngredients(dataNBT);
		}
		return ingredients;
	}
	
	public ItemStack getItem() {
		if (item == null) {
			try {
				item = new ItemStack(ForgeRegistries.ITEMS.getDelegate(
						new ResourceLocation(data.get("item").getAsString())).get().get());
			} catch(NoSuchElementException e) {
				item = ItemStack.EMPTY;
			}
			item.getOrCreateTag().putString("preset", getPresetId());
		}
		return item.copy();
	}
	
	public AircraftTextures getAircraftTextures() {
		if (textures == null) {
			textures = new AircraftTextures(dataNBT);
		}
		return textures;
	}
	
	public static class Builder {
		
		private final AircraftPreset preset;
		
		private Builder(String namespace, String name) {
			this.preset = new AircraftPreset(namespace, name);
		}
		
		public static Builder create(String namespace, String name) {
			return new Builder(namespace, name);
		}
		
		public AircraftPreset build() {
			setString("preset", preset.getPresetId());
			setBoolean("landing_gear", true);
			preset.dataNBT = UtilParse.getCompoundFromJson(preset.data);
			return preset;
		}
		
		/**
		 * used by all vehicles to add a slot that has an item by default
		 * @param name a translatable string 
		 * @param type the type of slot
		 * @param x the x position of the part relative to the vehicle at 0 rotation
		 * @param y the y position of the part relative to the vehicle at 0 rotation
		 * @param z the z position of the part relative to the vehicle at 0 rotation
		 * @param zRot used to make an external part rotate in degrees to visually connect to the surface
		 * @param uix the x position of the slot in the aircraft menu
		 * @param uiy the y position of the slot in the aircraft menu
		 * @param item resource location of item to get part data from
		 * @param param some part items accept a setting
		 * @param filled if the item is filled by default
		 */
		public Builder addItemSlot(String name, SlotType type, double x, double y, double z, float zRot, int uix, int uiy, 
				@Nullable ResourceLocation item, @Nullable String param, boolean filled) {
			if (!this.preset.data.has("slots")) {
				this.preset.data.add("slots", new JsonArray());
			}
			JsonObject slot = new JsonObject();
			slot.addProperty("name", name);
			slot.addProperty("slot_type", type.ordinal());
			slot.addProperty("slot_posx", x);
			slot.addProperty("slot_posy", y);
			slot.addProperty("slot_posz", z);
			slot.addProperty("uix", uix);
			slot.addProperty("uiy", uiy);
			slot.addProperty("zRot", zRot);
			if (item != null) {
				JsonObject d = new JsonObject();
				d.addProperty("itemid", item.toString());
				if (param != null) d.addProperty("param", param);
				if (filled) d.addProperty("filled", filled);
				slot.add("data", d);
			}
			this.preset.data.get("slots").getAsJsonArray().add(slot);
			return this;
		}
		
		/**
		 * used by all vehicles to add a slot that has an item by default.
		 * meant for internal parts.
		 * @param name a translatable string 
		 * @param type the type of slot
		 * @param uix the x position of the slot in the aircraft menu
		 * @param uiy the y position of the slot in the aircraft menu
		 * @param item resource location of item to get part data from
		 * @param param some part items accept a setting
		 * @param filled if the item is filled by default
		 */
		public Builder addItemSlot(String name, SlotType type, int uix, int uiy, 
				@Nullable ResourceLocation item, @Nullable String param, boolean filled) {
			return addItemSlot(name, type, 0, 0, 0, 0, uix, uiy, item, param, filled);
		}
		
		/**
		 * used by all vehicles to add a slot that has an item by default.
		 * meant for internal parts.
		 * @param name a translatable string 
		 * @param type the type of slot
		 * @param uix the x position of the slot in the aircraft menu
		 * @param uiy the y position of the slot in the aircraft menu
		 * @param item resource location of item to get part data from
		 * @param filled if the item is filled by default
		 */
		public Builder addItemSlot(String name, SlotType type, int uix, int uiy, 
				@Nullable ResourceLocation item, boolean filled) {
			return addItemSlot(name, type, 0, 0, 0, 0, uix, uiy, item, null, filled);
		}
		
		/**
		 * used by all vehicles to add a slot that has an item by default.
		 * meant for internal parts.
		 * @param name a translatable string 
		 * @param type the type of slot
		 * @param uix the x position of the slot in the aircraft menu
		 * @param uiy the y position of the slot in the aircraft menu
		 * @param item resource location of item to get part data from
		 */
		public Builder addItemSlot(String name, SlotType type, int uix, int uiy, @Nullable ResourceLocation item) {
			return addItemSlot(name, type, 0, 0, 0, 0, uix, uiy, item, null, false);
		}
		
		/**
		 * used by all vehicles to add a slot that is empty by default
		 * @param name a translatable string
		 * @param type the type of slot
		 * @param x the x position of the part relative to the vehicle at 0 rotation
		 * @param y the y position of the part relative to the vehicle at 0 rotation
		 * @param z the z position of the part relative to the vehicle at 0 rotation
		 * @param zRot used to make an external part rotate in degrees to visually connect to the surface
		 * @param uix the x position of the slot in the aircraft menu
		 * @param uiy the y position of the slot in the aircraft menu
		 */
		public Builder addEmptySlot(String name, SlotType type, double x, double y, double z, float zRot, int uix, int uiy) {
			return addItemSlot(name, type, x, y, z, zRot, uix, uiy, null, null, false);
		}
		
		/**
		 * used by all vehicles to add a slot that is empty by default.
		 * meant for internal parts.
		 * @param name a translatable string 
		 * @param type the type of slot
		 * @param uix the x position of the slot in the aircraft menu
		 * @param uiy the y position of the slot in the aircraft menu
		 */
		public Builder addEmptySlot(String name, SlotType type, int uix, int uiy) {
			return addItemSlot(name, type, 0, 0, 0, 0, uix, uiy, null, null, false);
		}
		
		/**
		 * used by all vehicles to add a slot that has a seat by default
		 * @param name a translatable string 
		 * @param x the x position of the part relative to the vehicle at 0 rotation
		 * @param y the y position of the part relative to the vehicle at 0 rotation
		 * @param z the z position of the part relative to the vehicle at 0 rotation
		 * @param uix the x position of the slot in the aircraft menu
		 * @param uiy the y position of the slot in the aircraft menu
		 */
		public Builder addSeatSlot(String name, double x, double y, double z, int uix, int uiy) {
			return addItemSlot(name, SlotType.SEAT, x, y, z, 0, uix, uiy, ModItems.SEAT.getId(), null, false);
		}
		
		/**
		 * used by all vehicles to add a slot that has a seat by default
		 * @param name a translatable string 
		 * @param type the type of slot
		 * @param x the x position of the part relative to the vehicle at 0 rotation
		 * @param y the y position of the part relative to the vehicle at 0 rotation
		 * @param z the z position of the part relative to the vehicle at 0 rotation
		 * @param uix the x position of the slot in the aircraft menu
		 * @param uiy the y position of the slot in the aircraft menu
		 */
		public Builder addSeatSlot(String name, SlotType type, double x, double y, double z, int uix, int uiy) {
			return addItemSlot(name, type, x, y, z, 0, uix, uiy, ModItems.SEAT.getId(), null, false);
		}
		
		/**
		 * all vehicles
		 */
		public Builder setDefaultColor(DyeColor dyecolor) {
			return setInt("dyecolor", dyecolor.getId());
		}
		
		/**
		 * all vehicles
		 */
		public Builder setColorTexture(DyeColor dyecolor, ResourceLocation texture) {
			if (!this.preset.data.has("textures")) {
				this.preset.data.add("textures", new JsonObject());
			}
			this.preset.data.get("textures").getAsJsonObject()
				.addProperty(dyecolor.getId()+"", texture.toString());
			return this;
		}
		
		/**
		 * all vehicles
		 */
		public Builder addIngredient(ResourceLocation item, int num) {
			if (!this.preset.data.has("ingredients")) {
				this.preset.data.add("ingredients", new JsonArray());
			}
			JsonObject i = new JsonObject();
			i.addProperty("item", item.toString());
			i.addProperty("num", num);
			this.preset.data.get("ingredients").getAsJsonArray().add(i);
			return this;
		}
		
		/**
		 * all vehicles
		 */
		public Builder addIngredient(String itemId, int num) {
			if (!this.preset.data.has("ingredients")) {
				this.preset.data.add("ingredients", new JsonArray());
			}
			JsonObject i = new JsonObject();
			i.addProperty("item", itemId);
			i.addProperty("num", num);
			this.preset.data.get("ingredients").getAsJsonArray().add(i);
			return this;
		}
		
		/**
		 * all vehicles
		 */
		public Builder addIngredient(ResourceLocation item) {
			return addIngredient(item, 1);
		}
		
		/**
		 * all vehicles
		 */
		public Builder setItem(ResourceLocation item) {
			return setString("item", item.toString());
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
		public Builder setPlaneWingArea(float wing_area) {
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
		
		/**
		 * all vehicles that can drive
		 */
		public Builder setTurnRadius(float turn_radius) {
			return setFloat("turn_radius", turn_radius);
		}
		
		/**
		 * all vehicles
		 */
		public Builder setMaxTurnRates(float maxroll, float maxpitch, float maxyaw) {
			setFloat("maxroll", maxroll);
			setFloat("maxpitch", maxpitch);
			return setFloat("maxyaw", maxyaw);
		}
		
		/**
		 * all vehicles
		 */
		public Builder setTurnTorques(float rolltorque, float pitchtorque, float yawtorque) {
			setFloat("rolltorque", rolltorque);
			setFloat("pitchtorque", pitchtorque);
			return setFloat("yawtorque", yawtorque);
		}
		
		/**
		 * all vehicles
		 */
		public Builder setThrottleRate(float throttleup, float throttledown) {
			setFloat("throttledown", throttledown);
			return setFloat("throttleup", throttleup);
		}
		
		/**
		 * helicopters only
		 */
		public Builder setHeliHoverMovement(float accForward, float accSide) {
			setFloat("accForward", accForward);
			return setFloat("accSide", accSide);
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
