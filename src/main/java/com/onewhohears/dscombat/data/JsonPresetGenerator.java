package com.onewhohears.dscombat.data;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import com.google.common.collect.Sets;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;

public abstract class JsonPresetGenerator<T extends JsonPreset> implements DataProvider {
	
	protected final DataGenerator.PathProvider pathProvider;
    private final Map<ResourceLocation, T> gen_map = new HashMap<>();

    public JsonPresetGenerator(DataGenerator output, String kind) {
        this.pathProvider = output.createPathProvider(DataGenerator.Target.DATA_PACK, kind);
    }
	
    /**
     * override this method if you want to add your own default presets
     */
    protected abstract void registerPresets();
	
	@Override
	public void run(CachedOutput cache) throws IOException {
		gen_map.clear();
		registerPresets();
		Set<ResourceLocation> set = Sets.newHashSet();
		Consumer<T> consumer = (preset) -> {
			System.out.println("ADD: "+preset.getKey().toString());
			if (!set.add(preset.getKey())) {
				throw new IllegalStateException("Duplicate Preset! " + preset.getKey());
			} else {
				Path path = pathProvider.json(preset.getKey());
				try {
					DataProvider.saveStable(cache, preset.getJsonData(), path);
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
