package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.BuffInstance;

import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.resources.ResourceLocation;

public class BuffStats extends PartStats {
	
	public static enum BuffType {
		DATA_LINK,
		NIGHT_VISION_HUD,
		RADIO,
		ARMOR
	}
	
	private final BuffType buffType;
	
	public BuffStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		buffType = UtilParse.getEnumSafe(json, "buffType", BuffType.class);
	}

	@Override
	public JsonPresetType getType() {
		return PartType.BUFF;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new BuffInstance<>(this);
	}
	
	public BuffType getBuffType() {
		return buffType;
	}
	
	@Override
	public boolean isRadio() {
		return getBuffType() == BuffType.RADIO;
	}
	
	@Override
	public float getAdditionalArmor() {
		if (getBuffType() == BuffType.ARMOR) return 4f;
		return 0f;
	}

}
