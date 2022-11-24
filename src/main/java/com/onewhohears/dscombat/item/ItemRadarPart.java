package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.parts.RadarPartData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemRadarPart extends ItemPart {
	
	public final String preset;
	
	public ItemRadarPart(float weight, String preset, SlotType[] compatibleSlots) {
		super(1, weight, compatibleSlots);
		this.preset = preset;
	}
	
	@Override
	public Component getName(ItemStack stack) {
		/*CompoundTag tag = stack.getOrCreateTag();
		MutableComponent name = UtilMCText.simpleText(getDescriptionId())
			.append(" "+tag.getInt("fuel")+"/"+tag.getInt("max"));
		return name;*/
		return super.getName(stack);
	}

	@Override
	public CompoundTag getNbt() {
		return new RadarPartData(weight, preset, getIdPart(), compatibleSlots).write();
	}

}
