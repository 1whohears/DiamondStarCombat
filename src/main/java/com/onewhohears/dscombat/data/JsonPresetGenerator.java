package com.onewhohears.dscombat.data;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;

public abstract class JsonPresetGenerator<T extends JsonPreset> implements DataProvider {
	
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
	
	protected final DataGenerator generator;
	protected final String kind;
    private final Map<ResourceLocation, T> gen_map = new HashMap<>();

    public JsonPresetGenerator(DataGenerator generator, String kind) {
        this.generator = generator;
        this.kind = kind;
    }
	
    /**
     * override this method if you want to add your own default presets
     */
    protected abstract void registerPresets();
	
	@Override
	public void run(HashCache cache) throws IOException {
		gen_map.clear();
		registerPresets();
		Set<ResourceLocation> set = Sets.newHashSet();
		Consumer<T> consumer = (preset) -> {
			System.out.println("ADD: "+preset.getKey().toString());
			if (!set.add(preset.getKey())) {
				throw new IllegalStateException("Duplicate Preset! " + preset.getKey());
			} else {
				Path path = generator.getOutputFolder().resolve("data/"+preset.getNameSpace()+"/"+kind+"/"+preset.getKey().getPath()+".json");
				try {
					JsonObject object = preset.getJsonData();
	                String rawJson = GSON.toJson(object);
	                String hash = SHA1.hashUnencodedChars(rawJson).toString();
	                if(!Objects.equals(cache.getHash(path), hash) || !Files.exists(path)) {
	                    Files.createDirectories(path.getParent());
	                    try(BufferedWriter writer = Files.newBufferedWriter(path)) {
	                        writer.write(rawJson);
	                    }
	                }
	                cache.putNew(path, hash);
				} catch (IOException e) {
					e.printStackTrace();
	            }
			}
		};
		generatePresets(consumer);
	}
	
	protected void generatePresets(Consumer<T> consumer) {
		gen_map.forEach((key, preset) -> consumer.accept(preset));
	}
	
	public void addPresetToGenerate(T preset) {
		gen_map.put(preset.getKey(), preset);
	}

}
