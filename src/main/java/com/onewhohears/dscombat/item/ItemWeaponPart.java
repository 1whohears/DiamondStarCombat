package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.parts.WeaponRackData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemWeaponPart extends ItemPart {
	
	public final String[] compatible;
	
	public ItemWeaponPart(float weight, String compatibilityType, SlotType[] compatibleSlots) {
		super(ItemAmmo.weaponProps(1), weight, compatibleSlots);
		this.compatible = WeaponPresets.getCompatibility(compatibilityType);
	}
	
	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		String weapon = tag.getString("weaponId");
		MutableComponent name = Component.translatable(getDescriptionId()).append(" ");
		if (weapon.isEmpty()) {
			name.append("EMPTY");
		} else {
			name.append(Component.translatable("item."+DSCombatMod.MODID+"."+weapon))
				.append(" "+tag.getInt("ammo")+"/"+tag.getInt("max"));
		}
		return name;	
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.WEAPONS.getId()) {
			for (int i = 0; i < compatible.length; ++i) addWeaponRack(compatible[i], items);
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
		return new WeaponRackData(weight, param, compatible, ForgeRegistries.ITEMS.getKey(this), compatibleSlots);
	}

}
