package com.onewhohears.dscombat.client.model.obj;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.client.model.geometry.StandaloneGeometryBakingContext;
import net.minecraftforge.client.model.obj.ObjModel;
import net.minecraftforge.client.model.obj.ObjModel.ModelSettings;
import net.minecraftforge.client.model.obj.ObjTokenizer;
import net.minecraftforge.client.model.renderable.CompositeRenderable;

public class ObjEntityModels implements ResourceManagerReloadListener {
	
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
	
	@Nullable
	public ModelOverrides getModelOverride(String name) {
		return modelOverrides.get(name);
	}
	
	public CompositeRenderable getBakedModel(String name) {
		if (!models.containsKey(name)) CompositeRenderable.builder().get();
		return models.get(name);
	}
	
	public void bakeModels() {
		System.out.println("BAKING OBJ MODELS");
		models.clear();
		unbakedModels.forEach((key, obj) -> {
			StandaloneGeometryBakingContext ctx = StandaloneGeometryBakingContext.create(obj.modelLocation);
			CompositeRenderable comp = obj.bakeRenderable(ctx);
			models.put(key, comp);
			System.out.println("BAKED "+key+" "+obj.getRootComponentNames().size()+" "+obj.getConfigurableComponentNames().size());
		});
	}
	
	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		System.out.println("RELOAD ASSET: "+DIRECTORY);
		readUnbakedModels(manager);
		readModelOverrides(manager);
		bakeModels();
	}
	
	public void readUnbakedModels(ResourceManager manager) {
		unbakedModels.clear();
		manager.listResources(DIRECTORY, (key) -> {
            return key.getPath().endsWith(MODEL_FILE_TYPE);
        }).forEach((key, resource) -> {
			try {
				String name = new File(key.getPath()).getName().replace(MODEL_FILE_TYPE, "");
				if (unbakedModels.containsKey(name)) {
					System.out.println("ERROR: Can't have 2 models with the same name! "+key);
					return;
				}
				ObjTokenizer tokenizer = new ObjTokenizer(resource.open());
				ObjModel model = ObjModel.parse(tokenizer, 
						new ModelSettings(key, 
							false, false, true, false, 
							null));
				tokenizer.close();
				unbakedModels.putIfAbsent(name, model);
				System.out.println("ADDING = "+key);
				model.getConfigurableComponentNames().forEach((n) -> {System.out.println(n);});
				System.out.println(" ");
			} catch (Exception e) {
				System.out.println("ERROR: SKIPPING "+key);
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
					System.out.println("ERROR: Can't have 2 model overrides with the same name! "+key);
					return;
				}
				JsonObject json = UtilParse.GSON.fromJson(resource.openAsReader(), JsonObject.class);
				modelOverrides.put(name, new ModelOverrides(json));
				System.out.println("ADDING OVERRIDE = "+key);
			} catch (Exception e) {
				System.out.println("ERROR: SKIPPING "+key.toString());
				e.printStackTrace();
			}
		});
	}
	
	public static class ModelOverrides {
		public float scale = 1;
		public float[] scale3d = {1f, 1f, 1f};
		public ModelOverrides(JsonObject json) {
			if (json.has("scale")) scale = json.get("scale").getAsFloat();
			if (json.has("scalex") && json.has("scaley") && json.has("scalez")) {
				scale3d[0] = json.get("scalex").getAsFloat();
				scale3d[1] = json.get("scaley").getAsFloat(); 
				scale3d[2] = json.get("scalez").getAsFloat();
			}
		}
		
	}

}
