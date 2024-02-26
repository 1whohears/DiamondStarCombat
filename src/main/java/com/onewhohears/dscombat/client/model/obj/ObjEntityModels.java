package com.onewhohears.dscombat.client.model.obj;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.slf4j.Logger;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.client.renderer.EntityScreenRenderer;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.client.model.geometry.StandaloneGeometryBakingContext;
import net.minecraftforge.client.model.obj.ObjModel;
import net.minecraftforge.client.model.obj.ObjModel.ModelSettings;
import net.minecraftforge.client.model.obj.ObjTokenizer;
import net.minecraftforge.client.model.renderable.CompositeRenderable;

public class ObjEntityModels implements ResourceManagerReloadListener {
	
	private static final Logger LOGGER = LogUtils.getLogger();
	private static ObjEntityModels instance;
	
	public static ObjEntityModels get() {
		if (instance == null) instance = new ObjEntityModels();
		return instance;
	}
	
	public static void close() {
		instance = null;
	}
	
	public static final String DIRECTORY = "models/entity";
	public static final String MODEL_FILE_TYPE = ".obj";
	public static final String OVERRIDE_FILE_TYPE = ".json";
	
	private Map<String, ModelOverrides> modelOverrides = new HashMap<>();
	private Map<String, ObjModel> unbakedModels = new HashMap<>();
	private Map<String, CompositeRenderable> models = new HashMap<>();
	
	private ObjEntityModels() {
	}
	
	@Nullable
	public ObjModel getUnbakedModel(String name) {
		return unbakedModels.get(name);
	}
	
	public static ModelOverrides NO_OVERRIDES = new ModelOverrides();
	
	public ModelOverrides getModelOverride(String name) {
		if (!modelOverrides.containsKey(name)) return NO_OVERRIDES;
		return modelOverrides.get(name);
	}
	
	public static final String NULL_MODEL_NAME = "simple_test";
	
	public CompositeRenderable getBakedModel(String name) {
		if (!models.containsKey(name)) return models.get(NULL_MODEL_NAME);
		return models.get(name);
	}
	
	public void bakeModels() {
		LOGGER.info("BAKING OBJ MODELS");
		models.clear();
		unbakedModels.forEach((key, obj) -> {
			StandaloneGeometryBakingContext ctx = StandaloneGeometryBakingContext.create(obj.modelLocation);
			CompositeRenderable comp = obj.bakeRenderable(ctx);
			models.put(key, comp);
			LOGGER.debug("BAKED "+key+" "+obj.getRootComponentNames().size()+" "+obj.getConfigurableComponentNames().size());
		});
	}
	
	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		LOGGER.info("RELOAD ASSET: "+DIRECTORY);
		readUnbakedModels(manager);
		readModelOverrides(manager);
		bakeModels();
		EntityScreenRenderer.clearCache();
	}
	
	public void readUnbakedModels(ResourceManager manager) {
		unbakedModels.clear();
		manager.listResources(DIRECTORY, (key) -> {
            return key.getPath().endsWith(MODEL_FILE_TYPE);
        }).forEach((key, resource) -> {
			try {
				String name = new File(key.getPath()).getName().replace(MODEL_FILE_TYPE, "");
				if (unbakedModels.containsKey(name)) {
					LOGGER.warn("ERROR: Can't have 2 models with the same name! "+key);
					return;
				}
				ObjTokenizer tokenizer = new ObjTokenizer(resource.open());
				ObjModel model = ObjModel.parse(tokenizer, 
						new ModelSettings(key, 
							false, false, true, false, 
							null));
				tokenizer.close();
				unbakedModels.putIfAbsent(name, model);
				LOGGER.debug("ADDING MODEL = "+key);
			} catch (Exception e) {
				LOGGER.warn("ERROR: SKIPPING "+key);
				e.printStackTrace();
			}
		});
	}
	
	public void readModelOverrides(ResourceManager manager) {
		modelOverrides.clear();
		manager.listResources(DIRECTORY, (key) -> {
            return key.getPath().endsWith(OVERRIDE_FILE_TYPE);
		}).forEach((key, resource) -> {
			try {
				String name = new File(key.getPath()).getName().replace(OVERRIDE_FILE_TYPE, "");
				if (modelOverrides.containsKey(name)) {
					LOGGER.warn("ERROR: Can't have 2 model overrides with the same name! "+key);
					return;
				}
				JsonObject json = UtilParse.GSON.fromJson(resource.openAsReader(), JsonObject.class);
				modelOverrides.put(name, new ModelOverrides(json));
				LOGGER.debug("ADDING OVERRIDE = "+key);
			} catch (Exception e) {
				LOGGER.warn("ERROR: SKIPPING "+key.toString());
				e.printStackTrace();
			}
		});
	}
	
	public static class ModelOverrides {
		public float scale = 1;
		public float[] scale3d = {1f, 1f, 1f};
		public double[] translate = {0, 0, 0};
		private boolean none = false;
		public ModelOverrides(JsonObject json) {
			if (json.has("scale")) scale = json.get("scale").getAsFloat();
			if (json.has("scalex")) scale3d[0] = json.get("scalex").getAsFloat();
			if (json.has("scaley")) scale3d[1] = json.get("scaley").getAsFloat(); 
			if (json.has("scalez")) scale3d[2] = json.get("scalez").getAsFloat();
			if (json.has("translatex")) translate[0] = json.get("translatex").getAsDouble();
			if (json.has("translatey")) translate[1] = json.get("translatey").getAsDouble(); 
			if (json.has("translatez")) translate[2] = json.get("translatez").getAsDouble();
		}
		private ModelOverrides() {
			none = true;
		}
		public boolean isNone() {
			return none;
		}
		public void apply(PoseStack poseStack) {
			if (isNone()) return;
			poseStack.translate(translate[0], translate[1], translate[2]);
			poseStack.scale(scale * scale3d[0], scale * scale3d[1], scale * scale3d[2]);
		}
	}

}
