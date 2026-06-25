package com.onewhohears.dscombat.common.container.slot;

import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.item.ItemAmmo;
import com.onewhohears.dscombat.item.ItemWeaponPart;
import com.onewhohears.dscombat.util.UtilPresetParse;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Slot that only accepts missile-type weapons for the Missile Launch Station
 */
public class MissileSlot extends Slot {
	
	public MissileSlot(Container container, int slot, int x, int y) {
		super(container, slot, x, y);
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		// Accept ItemAmmo (individual missiles) or ItemWeaponPart (weapon racks)
		if (!(stack.getItem() instanceof ItemAmmo) && !(stack.getItem() instanceof ItemWeaponPart)) {
			return false;
		}
		
		// Get weapon ID from NBT
		String weaponId = stack.getOrCreateTag().getString("weapon");
		if (weaponId.isEmpty()) {
			return false;
		}
		
		// Check if it's a missile type
		WeaponStats stats = WeaponPresets.get().get(weaponId);
		if (stats == null) {
			return false;
		}
		
		// Accept all missile types by checking the type ID string
		String typeId = stats.getWeaponType().getId();
		return typeId.equals("ir_missile") 
			|| typeId.equals("track_missile") 
			|| typeId.equals("pos_missile")
			|| typeId.equals("anti_radar_missile")
			|| typeId.equals("ballistic_missile")
			|| typeId.equals("torpedo")
			|| typeId.equals("dumb_torpedo");
	}
	
	@Override
	public int getMaxStackSize() {
		return 1; // Only 1 missile per slot
	}
}
