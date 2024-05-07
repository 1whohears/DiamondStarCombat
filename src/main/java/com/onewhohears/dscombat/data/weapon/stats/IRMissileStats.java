package com.onewhohears.dscombat.data.weapon.stats;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.instance.IRMissileInstance;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class IRMissileStats extends MissileStats {
	
	private final float flareResistance;
	
	public IRMissileStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		flareResistance = json.get("flareResistance").getAsFloat();
	}
	
	@Override
	public JsonPresetType getType() {
		return WeaponType.IR_MISSILE;
	}
	
	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new IRMissileInstance<>(this);
	}
	
	/**
	 * only matters if this is an IR missile
	 * 0 means immune to flares
	 * 1 means flares effect tracking normally
	 * @return missiles flare resistance
	 */
	public float getFlareResistance() {
		return flareResistance;
	}
	
	@Override
	public void addToolTips(List<Component> tips, boolean advanced) {
		super.addToolTips(tips, advanced);
		tips.add(UtilMCText.literal("TARGETS FLYING").setStyle(Style.EMPTY.withColor(SPECIAL_COLOR)));
		if (advanced && getFlareResistance() != 0) tips.add(UtilMCText.literal("Flare Resistance: ")
				.append(getFlareResistance()+"").setStyle(Style.EMPTY.withColor(INFO_COLOR)));
	}
	
	@Override
	public String getWeaponTypeCode() {
		return "AAIR";
	}
	
	@Override
	public String getDefaultIconLocation() {
		return MODID+":textures/ui/weapon_icons/ir_missile.png";
	}
	
	public boolean isIRMissile() {
		return true;
	}

}
