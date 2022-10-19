package com.onewhohears.dscombat.data;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.parts.PartsManager;
import com.onewhohears.dscombat.data.parts.SeatData;
import com.onewhohears.dscombat.data.radar.RadarData;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.weapon.BulletData;
import com.onewhohears.dscombat.data.weapon.MissileData;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.WeaponSystem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

public class AircraftPresets {
	
	public static List<CompoundTag> presets = new ArrayList<CompoundTag>();
	
	public static void setupPresets() {
		presets.add(JaviPreset());
		presets.add(TestPreset());
		// TODO check for a text file with the names of all the presets to read
	}
	
	@Nullable
	public static CompoundTag getPreset(String id) {
		for (CompoundTag tag : presets) if (tag.getString("preset").equals(id)) return tag;
		return null;
	}
	
	public static CompoundTag TestPreset() {
		System.out.println("CREATING TEST PRESET");
		CompoundTag tag = JaviPreset();
		tag.putString("preset", "test_plane");
		return tag;
	}
	
	public static CompoundTag JaviPreset() {
		System.out.println("CREATING JAVI PRESET");
		CompoundTag tag = new CompoundTag();
		// parts
		PartsManager pm = new PartsManager();
		pm.addSlot("pilot_seat", SlotType.SEAT, new Vec3(0, -0.5, 0));
		pm.addSlot("left_wing_1", SlotType.WING, new Vec3(-1, 0, 0));
		pm.addSlot("right_wing_1", SlotType.WING, new Vec3(1, 0, 0));
		pm.addPart(new SeatData("pilot_seat"), "pilot_seat", false);
		pm.write(tag);
		System.out.println(pm);
		// weapons
		WeaponSystem ws = new WeaponSystem();
		WeaponData b1 = new BulletData(WeaponPresets.getById("bullet_1"));
		WeaponData gbu = new MissileData(WeaponPresets.getById("gbu"));
		WeaponData f31 = new MissileData(WeaponPresets.getById("fox3_1"));
		WeaponData f21 = new MissileData(WeaponPresets.getById("fox2_1"));
		b1.setCurrentAmmo(b1.getMaxAmmo());
		gbu.setCurrentAmmo(gbu.getMaxAmmo());
		f31.setCurrentAmmo(f31.getMaxAmmo());
		f21.setCurrentAmmo(f21.getMaxAmmo());
		b1.setLaunchPos(new Vec3(0, 0, 1));
		gbu.setLaunchPos(new Vec3(0, 0, 1));
		f31.setLaunchPos(new Vec3(0, 0, 1));
		f21.setLaunchPos(new Vec3(0, 0, 1));
		ws.addWeapon(b1, false);
		ws.addWeapon(gbu, false);
		ws.addWeapon(f31, false);
		ws.addWeapon(f21, false);
		ws.write(tag);
		// radar
		RadarSystem rs = new RadarSystem();
		RadarData radar = new RadarData("radar_air", 1000, 70, 20);
		radar.setScanAircraft(true);
		radar.setScanPlayers(true);
		radar.setScanMobs(false);
		radar.setScanAir(true);
		radar.setScanGround(false);
		rs.addRadar(radar, false);
		RadarData radar2 = new RadarData("radar_ground", 200, -1, 15);
		radar2.setScanAircraft(true);
		radar2.setScanPlayers(true);
		radar2.setScanMobs(true);
		radar2.setScanAir(false);
		radar2.setScanGround(true);
		rs.addRadar(radar2, false);
		rs.write(tag);
		// other
		tag.putString("preset", "javi");
		tag.putFloat("max_speed", 1.5f);
		tag.putFloat("max_health", 100);
		tag.putFloat("health", 100);
		tag.putInt("flares", 100);
		tag.putFloat("stealth", 1);
		tag.putFloat("maxroll", 8);
		tag.putFloat("maxpitch", 2);
		tag.putFloat("maxyaw", 1);
		tag.putFloat("throttleup", 0.04f);
		tag.putFloat("throttledown", 0.04f);
		tag.putFloat("idleheat", 4f);
		tag.putFloat("engineheat", 4f);
		tag.putFloat("weight", 0.04f);
		tag.putFloat("maxthrust", 0.1f);
		tag.putFloat("surfacearea", 1f);
		return tag;
	}
	
}
