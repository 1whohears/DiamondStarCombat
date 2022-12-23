package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.parts.TurretData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemTurret extends ItemPart {

	public final String turrentEntityKey;
	
	public ItemTurret(float weight, SlotType[] compatibleSlots, String turrentEntityKey) {
		super(1, weight, compatibleSlots);
		this.turrentEntityKey = turrentEntityKey;
	}
	
	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		String turret = tag.getString("turretEntity");
		MutableComponent name = Component.translatable(getDescriptionId()).append(" ");
		if (turret.isEmpty()) {
			name.append("EMPTY");
		} else {
			name.append(Component.translatable(turret))
				.append(" "+tag.getInt("ammo"));
		}
		return name;	
	}

	@Override
	public PartData getPartData() {
		return new TurretData(weight, ForgeRegistries.ITEMS.getKey(this), compatibleSlots, turrentEntityKey);
	}

}
