package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.parts.TurretData;
import com.onewhohears.dscombat.data.parts.TurretData.RotBounds;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemTurret extends ItemPart {
	
	public final String weaponId;
	public final String turrentEntityKey;
	public final RotBounds rotBounds;
	
	public ItemTurret(float weight, SlotType[] compatibleSlots, String turrentEntityKey, String weaponId, RotBounds rotBounds) {
		super(1, weight, compatibleSlots);
		this.weaponId = weaponId;
		this.turrentEntityKey = turrentEntityKey;
		this.rotBounds = rotBounds;
	}
	
	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		MutableComponent name = new TranslatableComponent(getDescriptionId()).append(" ")
				.append(new TranslatableComponent("item."+DSCombatMod.MODID+"."+weaponId));
		int ammo = tag.getInt("ammo");
		int max = tag.getInt("max");
		if (max != 0) name.append(" "+ammo+"/"+max);
		return name;	
	}

	@Override
	public PartData getPartData() {
		return new TurretData(weight, ForgeRegistries.ITEMS.getKey(this), compatibleSlots, turrentEntityKey, weaponId, rotBounds);
	}

}
