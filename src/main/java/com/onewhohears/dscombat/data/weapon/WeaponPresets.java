package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.weapon.MissileData.GuidanceType;
import com.onewhohears.dscombat.data.weapon.MissileData.TargetType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

public class WeaponPresets {
	
	public static List<CompoundTag> weapons = new ArrayList<CompoundTag>();
	
	public static void setupPresets() {
		MissileData m1 = new MissileData("fox3_1", Vec3.ZERO, 
				300, 10, 10, false, 100, 4, 0, 
				true, true, false, 
				100, 4, TargetType.AIR, GuidanceType.PITBULL, 
				2, 0.04, 3, 70);
		MissileData m2 = new MissileData("fox2_1", Vec3.ZERO, 
				200, 10, 10, false, 100, 2, 0, 
				true, true, false, 
				100, 4, TargetType.AIR, GuidanceType.IR, 
				4, 0.03, 3, 80);
		BulletData b1 = new BulletData("bullet_1", Vec3.ZERO, 
				200, 1000, 1, true, 10, 6, 0.5f);
		MissileData m3 = new MissileData("gbu", Vec3.ZERO,
				400, 8, 100, false, 100, 1.5d, 0, 
				true, true, true, 100d, 4f,
				TargetType.GROUND, GuidanceType.PITBULL,
				4.0f, 0.02d, 2.5d, -1);
		weapons.add(m1.write());
		weapons.add(m2.write());
		weapons.add(b1.write());
		weapons.add(m3.write());
	}
	
	@Nullable
	public static CompoundTag getById(String id) {
		for (CompoundTag w : weapons) if (w.getString("id").equals(id)) return w.copy();
		return null;
	}
	
	public static MissileData getDefaultTestMissile() {
		return new MissileData(weapons.get(0));
	}
	
}