package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.WeaponExternalInstance;
import com.onewhohears.minigames.util.UtilParse;

import net.minecraft.resources.ResourceLocation;

public class WeaponExternalStats extends WeaponPartStats {
	
	private final float changeLaunchPitch;
	
	public WeaponExternalStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		changeLaunchPitch = UtilParse.getFloatSafe(json, "changeLaunchPitch", 0);
	}

	@Override
	public JsonPresetType getType() {
		return PartType.EXTERNAL_WEAPON;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new WeaponExternalInstance<>(this);
	}

	public float getChangeLaunchPitch() {
		return changeLaunchPitch;
	}

}
