package com.onewhohears.dscombat.item;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.parts.WeaponRackData;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilItem;

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
	
	public final float launchPitch;
	
	public ItemWeaponPart(float weight, SlotType[] compatibleSlots, float launchPitch) {
		super(ItemAmmo.weaponProps(8), weight, compatibleSlots);
		this.launchPitch = launchPitch;
	}
	
	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		String weapon = tag.getString("weaponId");
		MutableComponent name = ((MutableComponent)super.getName(stack)).append(" ");
		if (weapon.isEmpty()) {
			name.append("EMPTY");
		} else {
			WeaponData wd = WeaponPresets.get().getPreset(weapon);
			if (wd != null) {
				name.append(wd.getDisplayNameComponent()).append(" ")
					.append(Component.literal(wd.getWeaponTypeCode()));
			}
			else name.append(weapon+"?");
		}
		return name;	
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tips, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		if (!isAdvanced.isAdvanced()) return;
		CompoundTag tag = stack.getOrCreateTag();
		String id = tag.getString("weaponId");
		if (id.isEmpty()) return;
		tips.add(Component.literal("Ammo: "+tag.getInt("ammo")+"/"+tag.getInt("max")).setStyle(Style.EMPTY.withColor(0xAAAAAA)));
		WeaponData wd = WeaponPresets.get().getPreset(id);
		wd.addToolTips(tips);
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
		rack.setTag(getFilledPartData(preset).write());
		items.add(rack);
	}

	@Override
	public PartData getPartData() {
		return getFilledPartData("");
	}
	
	@Override
	public PartData getFilledPartData(String param) {
		ResourceLocation itemid = UtilItem.getItemKey(this);
		List<String> list = WeaponPresets.get().getCompatibleWeapons(itemid);
		String[] compatible = list.toArray(new String[list.size()]);
		return new WeaponRackData(weight, param, compatible, itemid, compatibleSlots, launchPitch);
	}

}
