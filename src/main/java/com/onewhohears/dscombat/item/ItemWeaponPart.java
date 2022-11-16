package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.WeaponRackData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemWeaponPart extends ItemPart {
	
	public final String[] compatible;
	
	public ItemWeaponPart(float weight, String compatibilityType) {
		super(1, weight);
		this.compatible = WeaponPresets.getCompatibility(compatibilityType);
	}
	
	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		String weapon = tag.getString("weaponId");
		MutableComponent name = UtilMCText.simpleText(getDescriptionId()).append(" ");
		if (weapon.isEmpty()) {
			name.append("EMPTY");
		} else {
			name.append(UtilMCText.simpleText("item."+DSCombatMod.MODID+"."+weapon))
				.append(" "+tag.getInt("ammo")+"/"+tag.getInt("max"));
		}
		return name;	
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			for (int i = 0; i < compatible.length; ++i) addWeaponRack(compatible[i], items);
		}
	}
	
	private void addWeaponRack(String preset, NonNullList<ItemStack> items) {
		ItemStack rack = new ItemStack(this);
		rack.setTag(new WeaponRackData(weight, preset, compatible, getIdPart()).write());
		items.add(rack);
	}
	
	/*@Override
	public ItemStack getCraftingRemainingItem(ItemStack stack) {	
		return stack;
	}*/

}
