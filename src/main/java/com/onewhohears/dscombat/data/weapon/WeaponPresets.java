package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.weapon.MissileData.GuidanceType;
import com.onewhohears.dscombat.data.weapon.MissileData.TargetType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

public class WeaponPresets {
	
	public static final String[] TEST_MISSILE_RACK = new String[] 
			{"fox3_1","fox2_1","gbu",
			 "aim7e","aim7mh","aim9x","aim120b","aim120c","agm65g","agm65l","agm84e","agm114k"};
	public static final String[] TEST_BIG_GUN = new String[] 
			{"bullet_1","bullet_2"};
	
	public static List<WeaponData> weapons = new ArrayList<WeaponData>();
	public static List<CompoundTag> weaponNbt = new ArrayList<CompoundTag>();
	
	public static void setupPresets() {
		add(new MissileData("fox3_1", Vec3.ZERO, 300, 4, 
				10, false, 100, 4, 0, 
				true, true, false, 
				100, 4, 
				TargetType.AIR, GuidanceType.PITBULL, 
				2, 0.04, 3, 70, 0));
		
		add(new MissileData("fox2_1", Vec3.ZERO, 200, 12, 
				10, false, 100, 2, 0, 
				true, true, false, 
				100, 4, 
				TargetType.AIR, GuidanceType.IR, 
				4, 0.03, 3, 80, 1));
		
		add(new BulletData("bullet_1", Vec3.ZERO, 
				200, 1000, 1, true, 
				15, 6, 0.5f));
		
		add(new BulletData("bullet_2", Vec3.ZERO, 
				200, 400, 4, true, 
				10, 6, 0.2f,
				true, true, true,
				10, 2f));
		
		add(new MissileData("gbu", Vec3.ZERO, 400, 8, 
				100, false, 100, 1.5d, 0, 
				true, true, true, 
				100d, 4f, 
				TargetType.GROUND, GuidanceType.PITBULL,
				4.0f, 0.02d, 2.5d, -1, 0));
		
		add(new MissileData("aim7e", Vec3.ZERO, 300, 4, 
				20, false, 100, 2, 0, 
				true, true, true, 
				100, 3f, 
				TargetType.AIR, GuidanceType.IR, 
				2, 0.02, 3, 60, 1.5f));
		
		add(new MissileData("aim7mh", Vec3.ZERO, 300, 4, 
				20, false, 100, 2.5, 0, 
				true, true, true, 
				100, 3.5f, 
				TargetType.AIR, GuidanceType.IR, 
				2.5f, 0.03, 3, 70, 1.0f));
		
		add(new MissileData("aim9x", Vec3.ZERO, 400, 4, 
				20, false, 100, 2.5, 0, 
				true, true, true, 
				100, 3f, 
				TargetType.AIR, GuidanceType.IR, 
				3.5f, 0.04, 3, 80, 0.6f));
		
		add(new MissileData("aim120b", Vec3.ZERO, 400, 2, 
				60, false, 100, 4.0, 0, 
				true, true, true, 
				100, 4, 
				TargetType.AIR, GuidanceType.PITBULL, 
				2.0f, 0.05, 3, 70, 0));
		
		add(new MissileData("aim120c", Vec3.ZERO, 400, 2, 
				60, false, 100, 5.0, 0, 
				true, true, true, 
				100, 4, 
				TargetType.AIR, GuidanceType.PITBULL, 
				1.5f, 0.05, 3, 70, 0));
		
		add(new MissileData("agm65g", Vec3.ZERO, 300, 4, 
				40, false, 100, 2.0d, 0, 
				true, true, true, 
				100d, 4f, 
				TargetType.GROUND, GuidanceType.PITBULL,
				1.0f, 0.02d, 1.5d, -1, 0));
		
		add(new MissileData("agm65l", Vec3.ZERO, 300, 4, 
				40, false, 100, 2.0d, 0, 
				true, true, true, 
				100d, 4f, 
				TargetType.POS, GuidanceType.OWNER_RADAR,
				1.0f, 0.02d, 1.5d, -1, 0));
		
		add(new MissileData("agm84e", Vec3.ZERO, 300, 3, 
				60, false, 100, 2.5d, 0, 
				true, true, true, 
				100d, 5f, 
				TargetType.GROUND, GuidanceType.PITBULL,
				2.0f, 0.03d, 2.0d, -1, 0));
		
		add(new MissileData("agm114k", Vec3.ZERO, 300, 6, 
				20, false, 100, 3.0d, 0, 
				true, true, true, 
				100d, 3f, 
				TargetType.POS, GuidanceType.PITBULL,
				3.0f, 0.05d, 2.0d, -1, 0));
	}
	
	public static void add(WeaponData data) {
		weapons.add(data);
		weaponNbt.add(data.write());
	}
	
	@Nullable
	public static CompoundTag getNbtById(String id) {
		for (CompoundTag w : weaponNbt) if (w.getString("id").equals(id)) return w.copy();
		return null;
	}
	
	@Nullable
	public static WeaponData getById(String id) {
		for (WeaponData w : weapons) if (w.getId().equals(id)) return w.copy();
		return null;
	}
	
	public static MissileData getDefaultTestMissile() {
		return new MissileData(weaponNbt.get(0));
	}
	
}
