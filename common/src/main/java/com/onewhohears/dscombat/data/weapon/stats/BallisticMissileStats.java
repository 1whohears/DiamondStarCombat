package com.onewhohears.dscombat.data.weapon.stats;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.instance.BallisticMissileInstance;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.UtilParse;

import net.minecraft.resources.ResourceLocation;

public class BallisticMissileStats extends MissileStats {

	private final float speedFly;
	private final boolean drone;

	public BallisticMissileStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		this.speedFly = UtilParse.getFloatSafe(json, "speed_fly", 4.0f);
		this.drone = UtilParse.getBooleanSafe(json, "drone", false);
	}
	
	@Override
	public JsonPresetType getType() {
		return WeaponType.BALLISTIC_MISSILE;
	}
	
	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new BallisticMissileInstance<>(this);
	}
	
	@Override
	public String getWeaponTypeCode() {
		return UtilMCText.transString("weapon_code.dscombat.ballistic");
	}
	
	@Override
	public String getDefaultIconLocation() {
		return MODID+":textures/ui/weapon_icons/ballistic_missile.png";
	}

	@Override
	public boolean isPosGuided() {
		return true;
	}

    @Override
    public double getMobTurretRange() {
        return Math.min(320000 * DSCPhyCons.getIRLScale(), getSpeed() * getMaxAge() * 0.8);
    }
    
    /**
     * Get flight speed for ballistic missiles
     * This controls how fast the missile flies, but does NOT affect range
     * @return speed in blocks per tick
     */
    public double getSpeedFly() {
    	return speedFly;
    }

    public boolean isDrone() {
    	return drone;
    }
    
    @Override
    public double getSpeed() {
    	// For ballistic missiles, use speed_fly instead of speed
    	// This ensures speed parameter doesn't affect trajectory
    	return getSpeedFly();
    }

}
