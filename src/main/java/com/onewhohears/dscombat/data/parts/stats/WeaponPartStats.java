package com.onewhohears.dscombat.data.parts.stats;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.WeaponPartInstance;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.minigames.util.UtilParse;

import net.minecraft.resources.ResourceLocation;

public class WeaponPartStats extends PartStats {
	
	private final String[] compatible;
	private final int max;
	
	public WeaponPartStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		List<String> list = WeaponPresets.get().getCompatibleWeapons(getId());
		compatible = list.toArray(new String[list.size()]);
		max = UtilParse.getIntSafe(json, "max", 0);
	}

	@Override
	public JsonPresetType getType() {
		return PartType.INTERNAL_WEAPON;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new WeaponPartInstance<>(this);
	}
	
	public boolean isWeaponCompatible(String preset) {
		if (preset == null) return false;
		for (int i = 0; i < compatible.length; ++i) 
			if (compatible[i].equals(preset)) 
				return true;
		return false;
	}
	
	public int getMaxAmmo() {
		return max;
	}

}
