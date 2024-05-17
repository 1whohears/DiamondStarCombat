package com.onewhohears.dscombat.item;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.instance.WeaponPartInstance;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilItem;
import com.onewhohears.dscombat.util.UtilMCText;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemWeaponPart extends ItemPart {
	
	public ItemWeaponPart(int stackSize) {
		super(stackSize);
	}
	
	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		String weapon = tag.getString("weapon");
		MutableComponent name = ((MutableComponent)super.getName(stack)).append(" ");
		if (weapon.isEmpty()) {
			name.append("EMPTY");
		} else {
			WeaponStats wd = WeaponPresets.get().get(weapon);
			if (wd != null) {
				name.append(wd.getDisplayNameComponent()).append(" ")
					.append(UtilMCText.literal(wd.getWeaponTypeCode()));
			}
			else name.append(weapon+"?");
		}
		return name;	
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tips, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		WeaponPartInstance<?> data = (WeaponPartInstance<?>) UtilParse.parsePartFromItem(stack);
		String id = data.getWeaponId();
		if (id.isEmpty()) return;
		tips.add(UtilMCText.literal("Ammo: "+(int)data.getCurrentAmmo()+"/"+data.getStats().getMaxAmmo()).setStyle(Style.EMPTY.withColor(0xAAAAAA)));
		WeaponStats wd = WeaponPresets.get().get(id);
		wd.addToolTips(tips, isAdvanced.isAdvanced());
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.WEAPONS.getId()) {
			ResourceLocation itemid = UtilItem.getItemKey(this);
			List<String> list = WeaponPresets.get().getCompatibleWeapons(itemid);
			for (int i = 0; i < list.size(); ++i) addWeaponRack(list.get(i), items);
		}
	}
	
	private void addWeaponRack(String preset, NonNullList<ItemStack> items) {
		ItemStack rack = new ItemStack(this);
		rack.setTag(getFilledPartData(preset).writeNBT());
		items.add(rack);
	}

}
