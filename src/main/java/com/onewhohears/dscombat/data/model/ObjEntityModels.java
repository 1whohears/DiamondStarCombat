package com.onewhohears.dscombat.data.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
	public static final String FILE_TYPE = ".obj";
	
	private Map<String, ObjModel> unbakedModels = new HashMap<>();
	private Map<String, CompositeRenderable> models = new HashMap<>();
	
	private ObjEntityModels() {
	}
	
	public ObjModel getUnbakedModel(String name) {
		return unbakedModels.get(name);
	}
	
	public CompositeRenderable getBakedModel(String name) {
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
		unbakedModels.clear();
		manager.listResources(DIRECTORY, (key) -> {
            return key.getPath().endsWith(FILE_TYPE);
        }).forEach((key, resource) -> {
			try {
				if (unbakedModels.containsKey(key.getPath())) {
					System.out.println("ERROR: Can't have 2 models with the same path! "+key);
					return;
				}
				ObjTokenizer tokenizer = new ObjTokenizer(resource.open());
				ObjModel model = ObjModel.parse(tokenizer, 
						new ModelSettings(key, 
							false, false, true, false, 
							null));
				tokenizer.close();
				String name = new File(key.getPath()).getName().replace(FILE_TYPE, "");
				unbakedModels.putIfAbsent(name, model);
				System.out.println("ADDING = "+key);
				model.getConfigurableComponentNames().forEach((n) -> {System.out.println(n);});
				System.out.println(" ");
			} catch (Exception e) {
				System.out.println("ERROR: SKIPPING "+key);
				e.printStackTrace();
			}
		});
		bakeModels();
	}

}
