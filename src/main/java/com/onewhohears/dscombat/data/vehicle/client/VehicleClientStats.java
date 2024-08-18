package com.onewhohears.dscombat.data.vehicle.client;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.client.entityscreen.VehicleScreenMapReader;
import com.onewhohears.dscombat.client.model.obj.HardCodedModelAnims;
import com.onewhohears.dscombat.client.model.obj.ObjVehicleModel;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetStats;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.onewholibs.data.jsonpreset.PresetBuilder;
import com.onewhohears.dscombat.data.vehicle.EntityScreenData;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.math.UtilGeometry;

import net.minecraft.resources.ResourceLocation;

public class VehicleClientStats extends JsonPresetStats {
	
	private ResourceLocation background;
	private HashMap<String, UIPos> slotsPos;
	private ObjVehicleModel<EntityVehicle> model;
	private List<EntityScreenData> screens;
	
	public VehicleClientStats(ResourceLocation key, JsonObject json) {
		super(key, json);
	}
	
	public ObjVehicleModel<EntityVehicle> getModel() {
		if (model != null) return model;
		if (!getJsonData().has("model_data")) {
			model = new ObjVehicleModel<>(getId());
			return model;
		}
		JsonObject model_data = getJsonData().get("model_data").getAsJsonObject();
		if (model_data.has("hard_coded_model_anims")) {
			model = HardCodedModelAnims.get(model_data.get("hard_coded_model_anims").getAsString());
			if (model != null) return model;
		}
		String model_id = getId();
		if (model_data.has("model_id")) model_id = model_data.get("model_id").getAsString();
		if (model_data.has("custom_anims")) 
			model = new ObjVehicleModel<>(model_id, model_data.get("custom_anims").getAsJsonArray());
		else model = new ObjVehicleModel<>(model_id);
		return model;
	}
	
	public List<EntityScreenData> getScreens() {
		if (screens == null) {
			ResourceLocation screenMap = new ResourceLocation(getNameSpace()+":textures/entity/vehicle/"+getId()+"/screens.png");
			screens = VehicleScreenMapReader.generateScreens(screenMap, getModel().modelId, 
					UtilGeometry.convertVector(getModel().getGlobalPivot()));
		}
		return screens;
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
	
	public UIPos getSlotPos(String slotId, int i, int x_start, int y_start) {
		if (!getSlotsPos().containsKey(slotId)) return getUIPosByIndex(i, x_start, y_start);
		return getSlotsPos().get(slotId);
	}
	
	public static UIPos getUIPosByIndex(int i, int x_start, int y_start) {
		int x = x_start + i % 9 * 18;
		int y = y_start + (int)(i / 9) * 18;
		return new UIPos(x, y);
	}
	
	public static class Builder extends PresetBuilder<Builder> {
		protected JsonObject getModelData() {
			if (!getData().has("model_data")) {
				getData().add("model_data", new JsonObject());
			}
			return getData().get("model_data").getAsJsonObject();
		}
		public Builder setCustomAnims(String model_id, JsonArray anims) {
			getModelData().add("custom_anims", anims);
			return setSimpleModelId(model_id);
		}
		public Builder setCustomAnims(JsonArray anims) {
			return setCustomAnims(getPresetId(), anims);
		}
		public Builder setHardCodedModelAnims(String hard_coded_model_anims) {
			getModelData().addProperty("hard_coded_model_anims", hard_coded_model_anims);
			return this;
		}
		public Builder setSimpleModelId(String model_id) {
			getModelData().addProperty("model_id", model_id);
			return this;
		}
		public Builder setHardCodedModelAnims() {
			return setHardCodedModelAnims(getPresetId());
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
		public Builder setAllUIPos(int x_start, int y_start, int cols, String... names) {
			int x = x_start, y = y_start;
			for (int i = 0; i < names.length; ++i) {
				if (i != 0 && i % cols == 0) {
					y += 18;
					x = x_start;
				}
				addUIPos(names[i], x, y);
				x += 18;
			}
			return this;
		}
		protected Builder(String namespace, String name, VehicleClientType type) {
			super(namespace, name, type);
		}
		protected Builder(String namespace, String name, VehicleClientType type, VehicleClientStats copy) {
			super(namespace, name, type, copy.getJsonData().deepCopy());
		}
		public static Builder create(String namespace, String name) {
			return new Builder(namespace, name, VehicleClientType.STANDARD);
		}
		public static Builder createFromCopy(String namespace, String name, VehicleClientStats copy) {
			return new Builder(namespace, name, VehicleClientType.STANDARD, copy);
		}
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

	@Override
	public JsonPresetType getType() {
		return VehicleClientType.STANDARD;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return null;
	}

}
