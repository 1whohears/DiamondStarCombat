package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.MineDispenserInstance;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.resources.ResourceLocation;

public class MineDispenserStats extends PartStats {
	
	private final int max;
	private final float damage;
	private final float radius;
	private final int armTime;
	
	public MineDispenserStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		max = UtilParse.getIntSafe(json, "max", 8);
		damage = UtilParse.getFloatSafe(json, "damage", 50.0f);
		radius = UtilParse.getFloatSafe(json, "radius", 5.0f);
		armTime = UtilParse.getIntSafe(json, "armTime", 40);
	}

	@Override
	public JsonPresetType getType() {
		return PartType.MINE_DISPENSER;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new MineDispenserInstance<>(this);
	}
	
	public int getMaxMines() {
		return max;
	}
	
	public float getDamage() {
		return damage;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public int getArmTime() {
		return armTime;
	}

}
