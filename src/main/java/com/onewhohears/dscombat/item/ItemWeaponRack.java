package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.WeaponRackData;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemWeaponRack extends ItemPart {
	
	public ItemWeaponRack() {
		super(1);
	}
	
	@Override
	public String getDescriptionId(ItemStack stack) {
		return DSCombatMod.MODID+"."+stack.getOrCreateTag().getString("weaponId");
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			ItemStack rack1 = new ItemStack(ModItems.WEAPON_RACK.get());
			rack1.setTag(new WeaponRackData(0.005f, "fox3_1", 4).write());
			//rack1.setHoverName(UtilMCText.simpleText("Test Fox3"));
			items.add(rack1);
			
			ItemStack rack2 = new ItemStack(ModItems.WEAPON_RACK.get());
			rack2.setTag(new WeaponRackData(0.005f, "fox2_1", 12).write());
			//rack2.setHoverName(UtilMCText.simpleText("Test Fox2"));
			items.add(rack2);
			
			ItemStack rack3 = new ItemStack(ModItems.WEAPON_RACK.get());
			rack3.setTag(new WeaponRackData(0.005f, "gbu", 8).write());
			//rack3.setHoverName(UtilMCText.simpleText("Test GBU"));
			items.add(rack3);
		}
	}

}
