package com.onewhohears.dscombat.data.parts.stats;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.EngineInstance;
import com.onewhohears.onewholibs.util.UtilMCText;

import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;

public class EngineStats extends PartStats {
	
	public static enum EngineType {
		SPIN,
		PUSH
	}
	
	private final EngineType engineType;
	private final float thrust;
	private final float heat;
	private final float fuelRate;
	
	public EngineStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		engineType = UtilParse.getEnumSafe(json, "engineType", EngineType.class);
		thrust = UtilParse.getFloatSafe(json, "thrust", 0);
		heat = UtilParse.getFloatSafe(json, "heat", 0);
		fuelRate = UtilParse.getFloatSafe(json, "fuelRate", 0);
	}

	@Override
	public JsonPresetType getType() {
		return PartType.INTERNAL_ENGINE;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new EngineInstance<>(this);
	}
	
	public EngineType getEngineType() {
		return engineType;
	}
	
	public float getThrust() {
		return thrust;
	}
	
	public float getHeat() {
		return heat;
	}
	
	public float getFuelPerTick() {
		return fuelRate;
	}
	
	@Override
	public boolean isEngine() {
		return true;
	}

	@Override
	public void addToolTips(List<Component> tips, TooltipFlag isAdvanced) {
		super.addToolTips(tips, isAdvanced);
		tips.add(UtilMCText.literal("Thrust: "+thrust).setStyle(Style.EMPTY.withColor(0xAAAAAA)));
		tips.add(UtilMCText.literal("Fuel L/M: "+String.format("%.1f", fuelRate*1200)).setStyle(Style.EMPTY.withColor(0xAAAAAA)));
	}

}
