package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.WeaponRackData;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemWeaponPart extends ItemPart {
	
	public ItemWeaponPart() {
		super(1);
	}
	
	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return UtilMCText.simpleText(getDescriptionId())
				.append(" ")
				.append(UtilMCText.simpleText("item."+DSCombatMod.MODID+"."+tag.getString("weaponId")))
				.append(" "+tag.getInt("ammo")+"/"+tag.getInt("max"));
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			ItemStack rack1 = new ItemStack(ModItems.WEAPON_PART.get());
			rack1.setTag(new WeaponRackData(0.005f, "fox3_1", 4, 4).write());
			items.add(rack1);
			
			ItemStack rack2 = new ItemStack(ModItems.WEAPON_PART.get());
			rack2.setTag(new WeaponRackData(0.005f, "fox2_1", 12, 12).write());
			items.add(rack2);
			
			ItemStack rack3 = new ItemStack(ModItems.WEAPON_PART.get());
			rack3.setTag(new WeaponRackData(0.005f, "gbu", 8, 8).write());
			items.add(rack3);
		}
	}

}
