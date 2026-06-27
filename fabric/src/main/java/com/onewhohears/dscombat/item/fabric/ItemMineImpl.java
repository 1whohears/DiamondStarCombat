package com.onewhohears.dscombat.item.fabric;

import com.onewhohears.dscombat.entity.weapon.EntityMine;
import com.onewhohears.dscombat.item.ItemMine;

public class ItemMineImpl {
	public static ItemMine create(EntityMine.MineType mineType) {
		return new ItemMine(mineType);
	}
}
