package com.onewhohears.dscombat.data.aircraft;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.crafting.DSCIngredient;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class AircraftPresetProvider implements DataProvider {
	
	public static final List<CompoundTag> presets = new ArrayList<>();
	public static final HashMap<String, List<DSCIngredient>> ingredients = new HashMap<>();
	public static final HashMap<String, ItemStack> items = new HashMap<>();
	public static final HashMap<String, AircraftTextures> textures = new HashMap<>();
	
	protected final DataGenerator.PathProvider pathProvider;
    private final Map<ResourceLocation, AircraftPreset> aircraftMap = new HashMap<>();

    protected AircraftPresetProvider(DataGenerator output)
    {
        this.pathProvider = output.createPathProvider(DataGenerator.Target.DATA_PACK, "aircraft");
    }
	
	@Override
	public void run(CachedOutput cache) throws IOException {
		aircraftMap.clear();
		Consumer<AircraftPreset> consumer = (aircraft) -> {
			if (aircraftMap.containsKey(aircraft.getKey())) {
				throw new IllegalStateException("Duplicate aircraft " + aircraft.getKey());
			} else {
				Path path = pathProvider.json(aircraft.getKey());
				try {
					DataProvider.saveStable(cache, aircraft.getDataAsJson(), path);
				} catch (IOException e) {
					e.printStackTrace();
	            }
			}
		};
		
	}

	@Override
	public String getName() {
		return "Aircraft: "+DSCombatMod.MODID;
	}
	
	public static void setupPresets() {
		String dir = "/data/dscombat/aircraft/";
		JsonArray ja = UtilParse.getJsonFromResource(dir+"aircraft.json").get("aircraft").getAsJsonArray();
		for (int i = 0; i < ja.size(); ++i) 
			presets.add(UtilParse.getCompoundFromJsonResource(dir+ja.get(i).getAsString()));
	}
	
	@Nullable
	public static CompoundTag getPreset(String preset) {
		for (CompoundTag tag : presets) if (tag.getString("preset").equals(preset)) return tag;
		return null;
	}
	
	public static AircraftTextures getAircraftTextures(String preset) {
		AircraftTextures at = textures.get(preset);
		if (at == null) {
			at = new AircraftTextures(getPreset(preset));
			textures.put(preset, at);
		}
		return at;
	}
	
	public static List<DSCIngredient> getPlaneIngredients(String preset) {
		List<DSCIngredient> ing = ingredients.get(preset);
		if (ing == null) {
			ing = DSCIngredient.getIngredients(getPreset(preset));
			ingredients.put(preset, ing);
		}
		return ing;
	}
	
	public static ItemStack getPlaneDisplayItem(String preset) {
		ItemStack stack = items.get(preset);
		if (stack == null) {
			try {
				stack = new ItemStack(ForgeRegistries.ITEMS.getDelegate(
						new ResourceLocation(DSCombatMod.MODID, preset))
							.get().get());
			} catch(NoSuchElementException e) {
				stack = ItemStack.EMPTY;
			}
			items.put(preset, stack);
		}
		return stack;
	}
	
}
