package com.onewhohears.dscombat.data.weapon.stats;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.instance.AntiRadarMissileInstance;
import com.onewhohears.onewholibs.util.UtilMCText;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class AntiRadarMissileStats extends MissileStats {
	
	private final double scan_range;
	
	public AntiRadarMissileStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		scan_range = json.get("scan_range").getAsDouble();
	}
	
	@Override
	public JsonPresetType getType() {
		return WeaponType.ANTI_RADAR_MISSILE;
	}
	
	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new AntiRadarMissileInstance<>(this);
	}
	
	@Override
	public void addToolTips(List<Component> tips, boolean advanced) {
		super.addToolTips(tips, advanced);
		tips.add(UtilMCText.literal("TARGETS GROUNDED").setStyle(Style.EMPTY.withColor(SPECIAL_COLOR)));
		tips.add(UtilMCText.literal("Scan Range: ").append(getScanRange()+"").setStyle(Style.EMPTY.withColor(INFO_COLOR)));
	}
	
	public double getScanRange() {
		return scan_range;
	}
	
	@Override
	public String getWeaponTypeCode() {
		return UtilMCText.transString("weapon_code.dscombat.magnum");
	}
	
	@Override
	public String getDefaultIconLocation() {
		return MODID+":textures/ui/weapon_icons/anti_radar_missile.png";
	}

}
