package com.onewhohears.dscombat.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.crafting.DSCIngredient;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class AircraftPresets {
	
	public static final List<CompoundTag> presets = new ArrayList<>();
	public static final HashMap<String, List<DSCIngredient>> ingredients = new HashMap<>();
	public static final HashMap<String, ItemStack> items = new HashMap<>();
	
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
			stack = new ItemStack(ForgeRegistries.ITEMS.getDelegate(
						new ResourceLocation(DSCombatMod.MODID, preset))
							.get().get());
			items.put(preset, stack);
		}
		return stack;
	}
	
}
