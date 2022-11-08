package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.WeaponPartData;
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
	
	private final int num;
	
	public ItemWeaponPart(int num) {
		super(1);
		this.num = num;
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
			if (num == 0) {
				addTestMissileRack(0.005f, "fox3_1", items);
				addTestMissileRack(0.005f, "fox2_1", items);
				addTestMissileRack(0.005f, "gbu", items);
				addTestMissileRack(0.003f, "aim7e", items);
				addTestMissileRack(0.003f, "aim7mh", items);
				addTestMissileRack(0.002f, "aim9x", items);
				addTestMissileRack(0.005f, "aim120b", items);
				addTestMissileRack(0.005f, "aim120c", items);
				addTestMissileRack(0.004f, "agm65g", items);
				addTestMissileRack(0.004f, "agm65l", items);
				addTestMissileRack(0.004f, "agm84e", items);
				addTestMissileRack(0.004f, "agm114k", items);
			} else if (num == 1) {
				addTestBigGun(0.002f, "bullet_1", items);
				addTestBigGun(0.002f, "bullet_2", items);
			}
		}
	}
	
	private void addTestMissileRack(float weight, String preset, NonNullList<ItemStack> items) {
		ItemStack rack = new ItemStack(ModItems.TEST_MISSILE_RACK.get());
		rack.setTag(new WeaponRackData(weight, preset, 
				WeaponPresets.TEST_MISSILE_RACK, 
				ModItems.TEST_MISSILE_RACK.getId())
				.write());
		items.add(rack);
	}
	
	private void addTestBigGun(float weight, String preset, NonNullList<ItemStack> items) {
		ItemStack rack = new ItemStack(ModItems.TEST_BIG_GUN.get());
		rack.setTag(new WeaponPartData(weight, preset, 
				WeaponPresets.TEST_BIG_GUN, 
				ModItems.TEST_BIG_GUN.getId())
				.write());
		items.add(rack);
	}
	
	/*@Override
	public ItemStack getCraftingRemainingItem(ItemStack stack) {	
		return stack;
	}*/

}
