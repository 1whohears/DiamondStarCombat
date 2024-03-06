package com.onewhohears.dscombat.data.weapon;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.JsonPreset;
import com.onewhohears.dscombat.entity.weapon.AntiRadarMissile;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class AntiRadarMissileData extends MissileData {
	
	private final double scan_range;
	
	public AntiRadarMissileData(ResourceLocation key, JsonObject json) {
		super(key, json);
		scan_range = json.get("scan_range").getAsDouble();
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
		return WeaponType.ANTIRADAR_MISSILE;
	}
	
	@Override
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = super.getInfoComponents();
		list.add(2, new ComponentColor(UtilMCText.literal("TARGETS GROUNDED"), 0xaaaa00));
		list.add(3, new ComponentColor(UtilMCText.literal("ANTI-RADAR GUIDED"), 0xaaaa00));
		return list;
	}
	
	@Override
	public <T extends JsonPreset> T copy() {
		return (T) new AntiRadarMissileData(getKey(), getJsonData());
	}
	
	@Override
	public EntityWeapon getShootEntity(WeaponShootParameters params) {
		AntiRadarMissile missile = (AntiRadarMissile) super.getShootEntity(params);
		if (missile == null) return null;
		return missile;
	}
	
	public double getScanRange() {
		return scan_range;
	}
	
	@Override
	public String getWeaponTypeCode() {
		return "AGAR";
	}
	
	@Override
	public String getDefaultIconLocation() {
		return MODID+":textures/ui/weapon_icons/anti_radar_missile.png";
	}

}
