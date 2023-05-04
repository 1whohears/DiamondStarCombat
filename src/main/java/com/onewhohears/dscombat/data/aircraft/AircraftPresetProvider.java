package com.onewhohears.dscombat.data.aircraft;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import com.google.common.collect.Sets;
import com.onewhohears.dscombat.DSCombatMod;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;

public class AircraftPresetProvider implements DataProvider {
	
	protected final DataGenerator.PathProvider pathProvider;
    private final Map<ResourceLocation, AircraftPreset> aircraftMap = new HashMap<>();

    public AircraftPresetProvider(DataGenerator output) {
        this.pathProvider = output.createPathProvider(DataGenerator.Target.DATA_PACK, "aircraft");
    }
    
    /**
     * override this method if you want to add your own default presets
     */
    protected void registerAircraftPresets() {
		addPreset(DefaultAircraftPresets.DEFAULT_ALEXIS_PLANE);
		addPreset(DefaultAircraftPresets.DEFAULT_JAVI_PLANE);
	}
	
	@Override
	public void run(CachedOutput cache) throws IOException {
		aircraftMap.clear();
		registerAircraftPresets();
		Set<ResourceLocation> set = Sets.newHashSet();
		Consumer<AircraftPreset> consumer = (aircraft) -> {
			System.out.println("CONSUMER FOR "+aircraft.getKey().toString());
			if (!set.add(aircraft.getKey())) {
				throw new IllegalStateException("Duplicate Aircraft! " + aircraft.getKey());
			} else {
				Path path = pathProvider.json(aircraft.getKey());
				try {
					DataProvider.saveStable(cache, aircraft.getDataAsJson(), path);
				} catch (IOException e) {
					e.printStackTrace();
	            }
			}
		};
		generateAircraftPresets(consumer);
	}
	
	protected void generateAircraftPresets(Consumer<AircraftPreset> consumer) {
		aircraftMap.forEach((key, preset) -> consumer.accept(preset));
	}
	
	public void addPreset(AircraftPreset preset) {
		aircraftMap.put(preset.getKey(), preset);
	}

	@Override
	public String getName() {
		return "Aircraft: "+DSCombatMod.MODID;
	}
	
}
