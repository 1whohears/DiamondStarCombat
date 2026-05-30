package com.onewhohears.dscombat.data.weapon.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.instance.AllMissileInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

public class AllMissileStats extends MissileStats {

	private final boolean canPositionGuide;
	private final boolean canRadarGuide;
	private final boolean canOpticalGuide;

	private final RadarTargetType radarTargetType;
	private final boolean radarActive;

	public AllMissileStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		canPositionGuide = UtilParse.getBooleanSafe(json, "canPositionGuide", false);
		canRadarGuide = UtilParse.getBooleanSafe(json, "canRadarGuide", false);
		canOpticalGuide = UtilParse.getBooleanSafe(json, "canOpticalGuide", false);
		radarTargetType = UtilParse.getEnumSafe(json, "radarTargetType", RadarTargetType.class);
		radarActive = UtilParse.getBooleanSafe(json, "radarActive", true);
	}

	@Override
	public void addToolTips(List<Component> tips, boolean advanced) {
		super.addToolTips(tips, advanced);
		if (isCanRadarGuide()) {
			switch (getRadarTargetType()) {
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
				if (radarActive)
					tips.add(UtilMCText.translatable("info.dscombat.active_track").setStyle(Style.EMPTY.withColor(SPECIAL_COLOR)));
				else
					tips.add(UtilMCText.translatable("info.dscombat.semi_active").setStyle(Style.EMPTY.withColor(SPECIAL_COLOR)));
			}
		}
	}

	@Override
	public String getWeaponTypeCode() {
		if (isCanRadarGuide()) {
			switch (getRadarTargetType()) {
				case AIR:
					if (radarActive) return UtilMCText.transString("weapon_code.dscombat.fox3");
					else return UtilMCText.transString("weapon_code.dscombat.fox1");
				case GROUND:
					return UtilMCText.transString("weapon_code.dscombat.rifel");
				case WATER:
					return UtilMCText.transString("weapon_code.dscombat.bruiser");
			}
		}
		return UtilMCText.transString("weapon_code.dscombat.greyhound");
	}
	
	@Override
	public JsonPresetType getType() {
		return WeaponType.ALL_MISSILE;
	}
	
	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new AllMissileInstance<>(this);
	}
	
	@Override
	public String getDefaultIconLocation() {
		return MODID+":textures/ui/weapon_icons/pos_missile.png";
	}

	@Override
	public boolean isPosGuided() {
		return isCanPositionGuide();
	}

    @Override
    public double getMobTurretRange() {
        return Math.min(160000*DSCPhyCons.getIRLScale(), getSpeed() * getMaxAge() * 0.75);
    }

    public boolean isCanPositionGuide() {
        return canPositionGuide;
    }

    public boolean isCanRadarGuide() {
        return canRadarGuide;
    }

    public boolean isCanOpticalGuide() {
        return canOpticalGuide;
    }

    public RadarTargetType getRadarTargetType() {
        return radarTargetType;
    }

    public boolean isRadarActive() {
        return radarActive;
    }
}
