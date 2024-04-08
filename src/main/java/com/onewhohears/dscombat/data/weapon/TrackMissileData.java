package com.onewhohears.dscombat.data.weapon;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPreset;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.entity.weapon.TrackEntityMissile;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.UtilMCText;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class TrackMissileData extends MissileData {
	
	public static enum TargetType {
		AIR,
		GROUND,
		WATER
	}
	
	private final TargetType targetType;
	private final boolean active;

	public TrackMissileData(ResourceLocation key, JsonObject json) {
		super(key, json);
		targetType = TargetType.valueOf(json.get("targetType").getAsString());
		active = UtilParse.getBooleanSafe(json, "activeTrack", true);
	}
	
	@Override
	public void readNBT(CompoundTag tag) {
		super.readNBT(tag);
	}
	
	@Override
	public CompoundTag writeNbt() {
		CompoundTag tag = super.writeNbt();
		return tag;
	}
	
	@Override
	public void readBuffer(FriendlyByteBuf buffer) {
		super.readBuffer(buffer);
	}
	
	@Override
	public void writeBuffer(FriendlyByteBuf buffer) {
		super.writeBuffer(buffer);
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.TRACK_MISSILE;
	}
	
	@Override
	public <T extends JsonPreset> T copy() {
		return (T) new TrackMissileData(getKey(), getJsonData());
	}
	
	public TargetType getTargetType() {
		return targetType;
	}
	
	public boolean isActiveTrack() {
		return active;
	}
	
	@Override
	public boolean couldRadarWeaponTargetEntity(Entity entity, Entity radar) {
		if (!super.couldRadarWeaponTargetEntity(entity, radar)) return false;
		boolean groundWater = UtilEntity.isOnGroundOrWater(entity);
		if (targetType == TargetType.AIR && groundWater) return false;
		else if (targetType == TargetType.GROUND && !groundWater) return false;
		else if (targetType == TargetType.WATER && !entity.isInWater()) return false;
		return true;
	}
	
	@Override
	public EntityWeapon getShootEntity(WeaponShootParameters params) {
		TrackEntityMissile missile = (TrackEntityMissile) super.getShootEntity(params);
		if (missile == null) return null;
		if (params.vehicle == null) return missile;
		RadarSystem radar = params.vehicle.radarSystem;
		if (!radar.hasRadar()) {
			setLaunchFail("error.dscombat.no_radar");
			return null;
		}
		Entity target = radar.getSelectedTarget();
		if (target == null) {
			setLaunchFail("error.dscombat.no_target_selected");
			return null;
		}
		boolean groundWater = UtilEntity.isOnGroundOrWater(target);
		if (targetType == TargetType.AIR && groundWater) {
			setLaunchFail("error.dscombat.air_target_only");
			return null;
		} else if (targetType == TargetType.GROUND && !groundWater) {
			setLaunchFail("error.dscombat.ground_target_only");
			return null;
		} else if (targetType == TargetType.WATER && !target.isInWater()) {
			setLaunchFail("error.dscombat.water_target_only");
			return null;
		}
		missile.target = target;
		return missile;
	}
	
	@Override
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = super.getInfoComponents();
		switch(getTargetType()) {
		case AIR:
			list.add(2, new ComponentColor(UtilMCText.literal("TARGETS FLYING"), 0xaaaa00));
			break;
		case GROUND:
			list.add(2, new ComponentColor(UtilMCText.literal("TARGETS GROUNDED"), 0xaaaa00));
			break;
		case WATER:
			list.add(2, new ComponentColor(UtilMCText.literal("TARGETS IN WATER"), 0xaaaa00));
			break;
		}
		list.add(3, new ComponentColor(UtilMCText.literal("SELF GUIDED"), 0xaaaa00));
		if (active) list.add(4, new ComponentColor(UtilMCText.literal("ACTIVE TRACK"), 0xaaaa00));
		else list.add(4, new ComponentColor(UtilMCText.literal("SEMI ACTIVE"), 0xaaaa00));
		return list;
	}
	
	@Override
	public String getWeaponTypeCode() {
		String code = "";
		switch(getTargetType()) {
		case AIR:
			code = "AA";
			break;
		case GROUND:
			code = "AG";
			break;
		case WATER:
			code = "AW";
			break;
		}
		code += "R";
		if (active) code += "AT";
		else code += "SA";
		return code;
	}
	
	@Override
	public String getDefaultIconLocation() {
		return MODID+":textures/ui/weapon_icons/radar_missile.png";
	}

}
