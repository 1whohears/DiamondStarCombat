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

/**
 * use this to generate json preset files. 
 * call {@link JsonPresetGenerator#addPresetToGenerate(JsonPreset)} inside a
 * {@link JsonPresetGenerator#registerPresets()} override.
 * use a {@link PresetBuilder} to make the presets to register.
 * 
 * see {@link com.onewhohears.dscombat.data.weapon.WeaponPresetGenerator},
 * {@link com.onewhohears.dscombat.data.radar.RadarPresetGenerator},
 * {@link com.onewhohears.dscombat.data.aircraft.AircraftPresetGenerator},
 * and {@link com.onewhohears.dscombat.data.aircraft.AircraftClientPresetGenerator} for examples.
 * see {@link JsonPresetReloadListener} for a way to read these json presets.
 * 
 * @author 1whohears
 * @param <T> the type of preset this reader builds from json files
 */
public abstract class JsonPresetGenerator<T extends JsonPreset> implements DataProvider {
	
	protected final DataGenerator.PathProvider pathProvider;
    private final Map<ResourceLocation, T> gen_map = new HashMap<>();
    
    /**
     * for data pack data generation
     * @param output
     * @param kind
     */
    public JsonPresetGenerator(DataGenerator output, String kind) {
        this(output, kind, DataGenerator.Target.DATA_PACK);
    }
    
    public JsonPresetGenerator(DataGenerator output, String kind, DataGenerator.Target target) {
        this.pathProvider = output.createPathProvider(target, kind);
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
