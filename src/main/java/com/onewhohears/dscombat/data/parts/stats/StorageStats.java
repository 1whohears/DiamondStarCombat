package com.onewhohears.dscombat.data.parts.stats;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.StorageInstance;
import com.onewhohears.dscombat.util.UtilMCText;
import com.onewhohears.minigames.util.UtilParse;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;

public class StorageStats extends PartStats {
	
	private final int size;
	
	public StorageStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		size = UtilParse.getIntSafe(json, "size", 0);
	}

	@Override
	public JsonPresetType getType() {
		return PartType.INTERNAL_STORAGE;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new StorageInstance<>(this);
	}
	
	public int getSize() {
		return size;
	}
	
	@Override
	public boolean isStorageBox() {
		return true;
	}

	@Override
	public void addToolTips(List<Component> tips, TooltipFlag isAdvanced) {
		super.addToolTips(tips, isAdvanced);
		tips.add(UtilMCText.literal("Total Slots: "+size).setStyle(Style.EMPTY.withColor(0xAAAAAA)));
	}

}
