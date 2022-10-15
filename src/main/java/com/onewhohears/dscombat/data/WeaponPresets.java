package com.onewhohears.dscombat.data;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.MissileData.GuidanceType;
import com.onewhohears.dscombat.data.MissileData.TargetType;

import net.minecraft.world.phys.Vec3;

public class WeaponPresets {
	
	public static List<WeaponData> weapons = new ArrayList<WeaponData>();
	
	public static void setupPresets() {
		weapons.add(new MissileData("fox3_1", Vec3.ZERO, 
				300, 10, 10, 100, 4, 0, 
				true, true, false, 
				100, 4, TargetType.AIR, GuidanceType.PITBULL, 
				2, 0.04, 3, 70));
		weapons.add(new MissileData("fox2_1", Vec3.ZERO, 
				200, 10, 10, 100, 2, 0, 
				true, true, false, 
				100, 4, TargetType.AIR, GuidanceType.IR, 
				4, 0.03, 3, 80));
	}
	
	@Nullable
	public static WeaponData getById(String id) {
		for (WeaponData w : weapons) if (w.getId().equals(id)) return w;
		return null;
	}
	
	public static MissileData getDefaultTestMissile() {
		return (MissileData) weapons.get(0);
	}
	
}
