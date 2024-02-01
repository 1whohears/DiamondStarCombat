package com.onewhohears.dscombat.data.aircraft;

import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.crafting.DSCIngredient;
import com.onewhohears.dscombat.data.JsonPreset;
import com.onewhohears.dscombat.data.PresetBuilder;
import com.onewhohears.dscombat.data.aircraft.VehicleSoundManager.PassengerSoundPack;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle.AircraftType;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AircraftPreset extends JsonPreset{

	private final AircraftType aircraft_type;
	private final CompoundTag dataNBT;
	private List<DSCIngredient> ingredients;
	private ItemStack item;
	
	public AircraftPreset(ResourceLocation key, JsonObject json) {
		super(key, json);
		this.aircraft_type = AircraftType.values()[json.get("aircraft_type").getAsInt()];
		this.dataNBT = UtilParse.getCompoundFromJson(json);
	}
	
	public void writeBuffer(FriendlyByteBuf buffer) {
		buffer.writeUtf(getKey().toString());
		buffer.writeUtf(getJsonData().toString());
	}
	
	public static AircraftPreset readBuffer(FriendlyByteBuf buffer) {
		String key_string = buffer.readUtf();
		String json_string = buffer.readUtf();
		ResourceLocation key = new ResourceLocation(key_string);
		JsonObject data = UtilParse.GSON.fromJson(json_string, JsonObject.class);
		return new AircraftPreset(key, data);
	}
	
	public CompoundTag getDataAsNBT() {
		return dataNBT;
	}
	
	public boolean isCraftable() {
		return getJsonData().get("is_craftable").getAsBoolean();
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
						new ResourceLocation(getJsonData().get("item").getAsString())).get().get());
			} catch(NoSuchElementException e) {
				item = ItemStack.EMPTY;
			}
			item.getOrCreateTag().putString("preset", getId());
		}
		return item.copy();
	}
	
	public int getDefaultPaintJob() {
		return getJsonData().get("paintjob_color").getAsInt();
	}
	
	@Override
	public String toString() {
		return getKey().toString()+" "+getJsonData().toString();
	}
	
	@Override
	public <T extends JsonPreset> T copy() {
		return (T) new AircraftPreset(getKey(), getJsonData());
	}
	
	public AircraftType getAircraftType() {
		return aircraft_type;
	}
	
	@Override
	public <T extends JsonPreset> int compare(T other) {
		AircraftPreset ap = (AircraftPreset) other;
		if (this.getAircraftType() != ap.getAircraftType()) 
			return this.getAircraftType().ordinal() - ap.getAircraftType().ordinal();
		return super.compare(other);
	}
	
	public static class Builder extends PresetBuilder<Builder> {
		
		private boolean is_craftable = false;
		
		protected Builder(String namespace, String name, JsonPresetFactory<? extends JsonPreset> sup) {
			super(namespace, name, sup);
		}
		
		protected Builder(String namespace, String name, JsonPresetFactory<? extends JsonPreset> sup, AircraftPreset copy) {
			super(namespace, name, sup, copy.getJsonData().deepCopy());
		}
		
		public static Builder create(String namespace, String name) {
			return new Builder(namespace, name, AircraftPreset::new);
		}
		
		public static Builder createFromCopy(String namespace, String name, AircraftPreset copy) {
			return new Builder(namespace, name, AircraftPreset::new, copy);
		}
		
		@Override
		public <T extends JsonPreset> T build() {
			setBoolean("landing_gear", true);
			setBoolean("is_craftable", is_craftable);
			return super.build();
		}
		
		public Builder setAircraftType(AircraftType aircraft_type) {
			return setInt("aircraft_type", aircraft_type.ordinal());
		}
		/**
		 * use to make preset appear in aircraft work bench.
		 * Builder.create/Builder.createFromCopy make presets that
		 * aren't craftable by default. 
		 */
		public Builder setCraftable() {
			is_craftable = true;
			return this;
		}
		/**
		 * used by all vehicles to add a slot that has an item by default
		 * @param name a translatable string 
		 * @param type the type of slot
		 * @param x the x position of the part relative to the vehicle at 0 rotation
		 * @param y the y position of the part relative to the vehicle at 0 rotation
		 * @param z the z position of the part relative to the vehicle at 0 rotation
		 * @param zRot used to make an external part rotate in degrees to visually connect to the surface
		 * @param item resource location of item to get part data from
		 * @param param some part items accept a setting
		 * @param filled if the item is filled by default
		 */
		public Builder addItemSlot(String name, SlotType type, double x, double y, double z, float zRot,  
				@Nullable ResourceLocation item, @Nullable String param, boolean filled) {
			JsonObject slot = getSlot(name, true);
			slot.addProperty("name", name);
			slot.addProperty("slot_type", type.getSlotTypeName());
			slot.addProperty("slot_posx", x);
			slot.addProperty("slot_posy", y);
			slot.addProperty("slot_posz", z);
			slot.addProperty("zRot", zRot);
			if (item != null) {
				JsonObject d = new JsonObject();
				d.addProperty("itemid", item.toString());
				if (param != null) d.addProperty("param", param);
				if (filled) d.addProperty("filled", filled);
				slot.add("data", d);
			}
			return this;
		}
		/**
		 * removes item data
		 * @param name slot that already exists
		 */
		public Builder setSlotEmpty(String name) {
			JsonObject slot = getSlot(name, false);
			if (slot != null) slot.remove("data");
			return this;
		}
		/**
		 * sets item data of slot
		 * @param name slot that already exists
		 * @param item resource location of item to get part data from
		 * @param param some part items accept a setting
		 * @param filled if the item is filled by default
		 */
		public Builder setSlotItem(String name, @Nullable ResourceLocation item, @Nullable String param, boolean filled) {
			if (item == null) return setSlotEmpty(name);
			JsonObject slot = getSlot(name, false);
			if (slot == null) return this;
			JsonObject d = new JsonObject();
			d.addProperty("itemid", item.toString());
			if (param != null) d.addProperty("param", param);
			if (filled) d.addProperty("filled", filled);
			slot.add("data", d);
			return this;
		}
		/**
		 * sets item data of slot
		 * @param name slot that already exists
		 * @param item resource location of item to get part data from
		 */
		public Builder setSlotItem(String name, @Nullable ResourceLocation item) {
			return setSlotItem(name, item, null, false);
		}
		/**
		 * sets item data of slot
		 * @param name slot that already exists
		 * @param item resource location of item to get part data from
		 * @param filled if the item is filled by default
		 */
		public Builder setSlotItem(String name, @Nullable ResourceLocation item, boolean filled) {
			return setSlotItem(name, item, null, filled);
		}
		
		public Builder lockSlot(String name) {
			JsonObject slot = getSlot(name, false);
			if (slot == null) return this;
			slot.addProperty("locked", true);
			return this;
		}
		
		protected JsonArray getSlots() {
			if (!getData().has("slots")) {
				getData().add("slots", new JsonArray());
			}
			return getData().get("slots").getAsJsonArray();
		}
		
		@Nullable
		protected JsonObject getSlot(String name, boolean createNew) {
			if (!getData().has("slots")) {
				getData().add("slots", new JsonArray());
			}
			JsonArray slots = getSlots();
			for (int i = 0; i < slots.size(); ++i) {
				JsonObject slot = slots.get(i).getAsJsonObject();
				if (slot.get("name").getAsString().equals(name)) {
					return slot;
				}
			}
			if (!createNew) return null;
			JsonObject slot = new JsonObject();
			slot.addProperty("name", name);
			slots.add(slot);
			return slot;
		}
		/**
		 * used by all vehicles to add a slot that has an item by default
		 * @param name a translatable string 
		 * @param type the type of slot
		 * @param x the x position of the part relative to the vehicle at 0 rotation
		 * @param y the y position of the part relative to the vehicle at 0 rotation
		 * @param z the z position of the part relative to the vehicle at 0 rotation
		 * @param zRot used to make an external part rotate in degrees to visually connect to the surface
		 * @param item resource location of item to get part data from
		 */
		public Builder addItemSlot(String name, SlotType type, double x, double y, double z, float zRot, 
				@Nullable ResourceLocation item) {
			return addItemSlot(name, type, x, y, z, zRot, item, null, false);
		}
		/**
		 * used by all vehicles to add a slot that has an item by default.
		 * meant for internal parts.
		 * @param name a translatable string 
		 * @param type the type of slot
		 * @param item resource location of item to get part data from
		 * @param param some part items accept a setting
		 * @param filled if the item is filled by default
		 */
		public Builder addItemSlot(String name, SlotType type, 
				@Nullable ResourceLocation item, @Nullable String param, boolean filled) {
			return addItemSlot(name, type, 0, 0, 0, 0, item, param, filled);
		}
		/**
		 * used by all vehicles to add a slot that has an item by default.
		 * meant for internal parts.
		 * @param name a translatable string 
		 * @param type the type of slot
		 * @param item resource location of item to get part data from
		 * @param filled if the item is filled by default
		 */
		public Builder addItemSlot(String name, SlotType type, 
				@Nullable ResourceLocation item, boolean filled) {
			return addItemSlot(name, type, 0, 0, 0, 0, item, null, filled);
		}
		/**
		 * used by all vehicles to add a slot that has an item by default.
		 * meant for internal parts.
		 * @param name a translatable string 
		 * @param type the type of slot
		 * @param item resource location of item to get part data from
		 */
		public Builder addItemSlot(String name, SlotType type, @Nullable ResourceLocation item) {
			return addItemSlot(name, type, 0, 0, 0, 0, item, null, false);
		}
		/**
		 * used by all vehicles to add a slot that is empty by default
		 * @param name a translatable string
		 * @param type the type of slot
		 * @param x the x position of the part relative to the vehicle at 0 rotation
		 * @param y the y position of the part relative to the vehicle at 0 rotation
		 * @param z the z position of the part relative to the vehicle at 0 rotation
		 * @param zRot used to make an external part rotate in degrees to visually connect to the surface
		 */
		public Builder addEmptySlot(String name, SlotType type, double x, double y, double z, float zRot) {
			return addItemSlot(name, type, x, y, z, zRot, null, null, false);
		}
		/**
		 * used by all vehicles to add a slot that is empty by default.
		 * meant for internal parts.
		 * @param name a translatable string 
		 * @param type the type of slot
		 */
		public Builder addEmptySlot(String name, SlotType type) {
			return addItemSlot(name, type, 0, 0, 0, 0, null, null, false);
		}
		/**
		 * used by all vehicles to add a slot that has a seat by default
		 * @param name a translatable string 
		 * @param x the x position of the part relative to the vehicle at 0 rotation
		 * @param y the y position of the part relative to the vehicle at 0 rotation
		 * @param z the z position of the part relative to the vehicle at 0 rotation
		 */
		public Builder addSeatSlot(String name, double x, double y, double z) {
			return addItemSlot(name, SlotType.SEAT, x, y, z, 0, ModItems.SEAT.getId(), null, false);
		}
		/**
		 * used by all vehicles to add a slot that has a seat by default
		 * @param x the x position of the part relative to the vehicle at 0 rotation
		 * @param y the y position of the part relative to the vehicle at 0 rotation
		 * @param z the z position of the part relative to the vehicle at 0 rotation
		 */
		public Builder addPilotSeatSlot(double x, double y, double z) {
			return addItemSlot(PartSlot.PILOT_SLOT_NAME, SlotType.SEAT, x, y, z, 0, ModItems.SEAT.getId(), null, false);
		}
		/**
		 * used by all vehicles to add a slot that has a seat by default
		 * @param name a translatable string 
		 * @param type the type of slot
		 * @param x the x position of the part relative to the vehicle at 0 rotation
		 * @param y the y position of the part relative to the vehicle at 0 rotation
		 * @param z the z position of the part relative to the vehicle at 0 rotation
		 */
		public Builder addSeatSlot(String name, SlotType type, double x, double y, double z) {
			return addItemSlot(name, type, x, y, z, 0, ModItems.SEAT.getId(), null, false);
		}
		/**
		 * all vehicles
		 */
		public Builder addIngredient(String itemId, int num) {
			if (!getData().has("ingredients")) {
				getData().add("ingredients", new JsonArray());
			}
			JsonObject i = new JsonObject();
			i.addProperty("item", itemId);
			i.addProperty("num", num);
			getData().get("ingredients").getAsJsonArray().add(i);
			return this;
		}
		/**
		 * all vehicles
		 */
		public Builder addIngredient(ResourceLocation item, int num) {
			return addIngredient(item.toString(), num);
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
		
		public JsonObject getStats() {
			if (!getData().has("stats")) 
				getData().add("stats", new JsonObject());
			return getData().get("stats").getAsJsonObject();
		}
		
		public JsonObject getStatsByType(String vehicleType) {
			if (!getStats().has(vehicleType)) 
				getStats().add(vehicleType, new JsonObject());
			return getStats().get(vehicleType).getAsJsonObject();
		}
		
		public Builder setStatFloat(String key, float value) {
			getStats().addProperty(key, value);
			return this;
		}
		
		public Builder setStatInt(String key, int value) {
			getStats().addProperty(key, value);
			return this;
		}
		
		public Builder setTypedStatFloat(String key, float value, String vehicleType) {
			getStatsByType(vehicleType).addProperty(key, value);
			return this;
		}
		
		public Builder setTypedStatString(String key, String value, String vehicleType) {
			getStatsByType(vehicleType).addProperty(key, value);
			return this;
		}
		
		public Builder setTypedStatBoolean(String key, boolean value, String vehicleType) {
			getStatsByType(vehicleType).addProperty(key, value);
			return this;
		}
		/**
		 * all vehicles
		 * blocks per tick
		 */
		public Builder setMaxSpeed(float max_speed) {
			return setStatFloat("max_speed", max_speed);
		}
		/**
		 * all vehicles
		 */
		public Builder setMaxHealth(float max_health) {
			setFloat("health", max_health);
			return setStatFloat("max_health", max_health);
		}
		/**
		 * all vehicles
		 */
		public Builder setMass(float mass) {
			return setStatFloat("mass", mass);
		}
		/**
		 * all vehicles
		 */
		public Builder setBaseArmor(float armor) {
			return setStatFloat("base_armor", armor);
		}
		/**
		 * all vehicles
		 */
		public Builder setCrossSecArea(float area) {
			return setStatFloat("cross_sec_area", area);
		}
		/**
		 * all vehicles
		 */
		public Builder setStealth(float stealth) {
			return setStatFloat("stealth", stealth);
		}
		/**
		 * all vehicles
		 */
		public Builder setIdleHeat(float idleheat) {
			return setStatFloat("idleheat", idleheat);
		}
		/**
		 * all vehicles that can drive
		 */
		public Builder setTurnRadius(float turn_radius) {
			return setStatFloat("turn_radius", turn_radius);
		}
		/**
		 * all vehicles
		 */
		public Builder setMaxTurnRates(float maxroll, float maxpitch, float maxyaw) {
			setStatFloat("maxroll", maxroll);
			setStatFloat("maxpitch", maxpitch);
			return setStatFloat("maxyaw", maxyaw);
		}
		/**
		 * all vehicles
		 */
		public Builder setTurnTorques(float torqueroll, float torquepitch, float torqueyaw) {
			setStatFloat("torqueroll", torqueroll);
			setStatFloat("torquepitch", torquepitch);
			return setStatFloat("torqueyaw", torqueyaw);
		}
		/**
		 * all vehicles
		 */
		public Builder setRotationalInertia(float inertiaroll, float inertiapitch, float inertiayaw) {
			setStatFloat("inertiaroll", inertiaroll);
			setStatFloat("inertiapitch", inertiapitch);
			return setStatFloat("inertiayaw", inertiayaw);
		}
		/**
		 * all vehicles
		 */
		public Builder setThrottleRate(float throttleup, float throttledown) {
			setStatFloat("throttledown", throttledown);
			return setStatFloat("throttleup", throttleup);
		}
		/**
		 * all vehicles 
		 */
		public Builder setCrashExplosionRadius(float crashExplosionRadius) {
			return setStatFloat("crashExplosionRadius", crashExplosionRadius);
		}
		/**
		 * all vehicles 
		 */
		public Builder set3rdPersonCamDist(float cameraDistance) {
			return setStatFloat("cameraDistance", cameraDistance);
		}
		
		public JsonObject getTextures() {
			if (!getData().has("textures")) 
				getData().add("textures", new JsonObject());
			return getData().get("textures").getAsJsonObject();
		}
		/**
		 * all vehicles
		 */
		public Builder setDefaultBaseTexture(int index) {
			getTextures().addProperty("baseTexture", index);
			return this;
		}
		/**
		 * all vehicles 
		 */
		public Builder setBaseTextureNum(int baseTextureVariants) {
			getTextures().addProperty("baseTextureVariants", baseTextureVariants);
			return this;
		}
		/**
		 * all vehicles 
		 */
		public Builder setLayerTextureNum(int textureLayers) {
			getTextures().addProperty("textureLayers", textureLayers);
			return this;
		}
		
		public JsonObject getSounds() {
			if (!getData().has("sounds")) 
				getData().add("sounds", new JsonObject());
			return getData().get("sounds").getAsJsonObject();
		}
		/**
		 * all vehicles 
		 */
		public Builder setEngineSounds(RegistryObject<SoundEvent> nonPassengerEngine, RegistryObject<SoundEvent> passengerEngine) {
			getSounds().addProperty("nonPassengerEngine", nonPassengerEngine.getKey().toString());
			getSounds().addProperty("passengerEngine", passengerEngine.getKey().toString());
			return this;
		}
		/**
		 * all vehicles 
		 */
		public Builder setDefultPassengerSoundPack(PassengerSoundPack passengerSoundPack) {
			getSounds().addProperty("passengerSoundPack", passengerSoundPack.id);
			return this;
		}
		/**
		 * used by planes
		 */
		public Builder setPlaneWingArea(float wing_area) {
			return setTypedStatFloat("wing_area", wing_area, "plane");
		}
		/**
		 * used by planes
		 */
		public Builder setPlaneFlapDownAOABias(float flapsAOABias) {
			return setTypedStatFloat("flapsAOABias", flapsAOABias, "plane");
		}
		/**
		 * used by planes
		 */
		public Builder setPlaneNoseCanAimDown(float canAimDown) {
			return setTypedStatFloat("canAimDown", canAimDown, "plane");
		}
		/**
		 * used by planes
		 */
		public Builder setPlaneLiftAOAGraph(LiftKGraph liftKGraph) {
			return setTypedStatString("liftKGraph", liftKGraph.id, "plane");
		}
		/**
		 * helicopters only
		 */
		public Builder setHeliHoverMovement(float accForward, float accSide) {
			setTypedStatFloat("accForward", accForward, "heli");
			return setTypedStatFloat("accSide", accSide, "heli");
		}
		/**
		 * helicopters only
		 */
		public Builder setHeliLiftFactor(float heliLiftFactor) {
			return setTypedStatFloat("heliLiftFactor", heliLiftFactor, "heli");
		}
		/**
		 * helicopters only
		 */
		public Builder setHeliAlwaysLandingGear(boolean alwaysLandingGear) {
			return setTypedStatBoolean("alwaysLandingGear", alwaysLandingGear, "heli");
		}
		/**
		 * ground vehicles only
		 */
		public Builder setCarIsTank(boolean isTank) {
			return setTypedStatBoolean("isTank", isTank, "car");
		}
		
		public Builder setBoolean(String key, boolean value) {
			getData().addProperty(key, value);
			return this;
		}
		
		public Builder setInt(String key, int value) {
			getData().addProperty(key, value);
			return this;
		}
		
		public Builder setFloat(String key, float value) {
			getData().addProperty(key, value);
			return this;
		}
		
		public Builder setString(String key, String value) {
			getData().addProperty(key, value);
			return this;
		}
		
	}
	
}
