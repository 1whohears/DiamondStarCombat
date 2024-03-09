package com.onewhohears.dscombat.item;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilItem;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemAmmo extends Item {
	
	private final String defaultWeaponId;
	
	public ItemAmmo(int size, String defaultWeaponId) {
		super(weaponProps(size));
		this.defaultWeaponId = defaultWeaponId;
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() != ModItems.WEAPONS.getId() && group.getId() != CreativeModeTab.TAB_SEARCH.getId()) return;
		String itemId = UtilItem.getItemKeyString(this);
		for (int i = 0; i < WeaponPresets.get().getPresetNum(); ++i) {
			WeaponData w = WeaponPresets.get().getAllPresets()[i];
			if (!w.getItemKey().equals(itemId)) continue;
			ItemStack test = new ItemStack(this);
			CompoundTag tag = new CompoundTag();
			tag.putString("weapon", w.getId());
			test.setTag(tag);
			items.add(test);
		}
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tips, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		if (!isAdvanced.isAdvanced()) return;
		String id = getWeaponId(stack);
		WeaponData wd = WeaponPresets.get().getPreset(id);
		if (wd == null) return;
		wd.addToolTips(tips);
	}
	
	@Override
	public Component getName(ItemStack stack) {
		String id = getWeaponId(stack);
		WeaponData wd = WeaponPresets.get().getPreset(id);
		if (wd == null) return UtilMCText.translatable(getDescriptionId()).append(" ")
				.append(UtilMCText.translatable("error.dscombat.unknown_preset"));
		return wd.getDisplayNameComponent().append(" ")
				.append(UtilMCText.literal(wd.getWeaponTypeCode())).append(" ")
				.append(UtilMCText.translatable("info.dscombat.ammo"));
	}
	
	public static String getWeaponId(ItemStack stack) {
		if (stack.getItem() instanceof ItemAmmo ia) {
			if (!stack.getOrCreateTag().contains("weapon")) return ia.defaultWeaponId;
			return stack.getOrCreateTag().getString("weapon");
		}
		return "";
	}
	
	public static Properties weaponProps(int stackSize) {
		return new Item.Properties().tab(ModItems.WEAPONS).stacksTo(stackSize);
	}

}
