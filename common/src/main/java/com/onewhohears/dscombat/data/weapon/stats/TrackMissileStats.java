package com.onewhohears.dscombat.data.weapon.stats;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.instance.TrackMissileInstance;
import com.onewhohears.onewholibs.util.UtilMCText;

import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class TrackMissileStats extends MissileStats {

	private final RadarTargetType targetType;
	private final boolean active;

	public TrackMissileStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		targetType = UtilParse.getEnumSafe(json, "targetType", RadarTargetType.class);
		active = UtilParse.getBooleanSafe(json, "activeTrack", true);
	}
	
	@Override
	public JsonPresetType getType() {
		return WeaponType.TRACK_MISSILE;
	}
	
	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new TrackMissileInstance<>(this);
	}
	
	public RadarTargetType getTargetType() {
		return targetType;
	}
	
	public boolean isActiveTrack() {
		return active;
	}
	
	@Override
	public void addToolTips(List<Component> tips, boolean advanced) {
		super.addToolTips(tips, advanced);
		switch(getTargetType()) {
		case AIR:
			tips.add(UtilMCText.translatable("info.dscombat.targets_flying").setStyle(Style.EMPTY.withColor(SPECIAL_COLOR)));
			break;
		case GROUND:
			tips.add(UtilMCText.translatable("info.dscombat.targets_grounded").setStyle(Style.EMPTY.withColor(SPECIAL_COLOR)));
			break;
		case WATER:
			tips.add(UtilMCText.translatable("info.dscombat.targets_in_water").setStyle(Style.EMPTY.withColor(SPECIAL_COLOR)));
			break;
		}
		if (advanced) {
			if (active) tips.add(UtilMCText.translatable("info.dscombat.active_track").setStyle(Style.EMPTY.withColor(SPECIAL_COLOR)));
			else tips.add(UtilMCText.translatable("info.dscombat.semi_active").setStyle(Style.EMPTY.withColor(SPECIAL_COLOR)));
		}
	}
	
	@Override
	public String getWeaponTypeCode() {
		switch(getTargetType()) {
		case AIR:
			if (active) return UtilMCText.transString("weapon_code.dscombat.fox3");
			else return UtilMCText.transString("weapon_code.dscombat.fox1");
		case GROUND:
			return UtilMCText.transString("weapon_code.dscombat.rifel");
		case WATER:
			return UtilMCText.transString("weapon_code.dscombat.bruiser");
		}
		return "";
	}
	
	@Override
	public String getDefaultIconLocation() {
		return MODID+":textures/ui/weapon_icons/radar_missile.png";
	}
	
	public boolean requiresRadar() {
		return true;
	}

}
