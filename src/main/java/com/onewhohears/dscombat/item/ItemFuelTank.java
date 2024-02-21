package com.onewhohears.dscombat.item;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.FuelTankData;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.util.UtilItem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemFuelTank extends ItemPart {
	
	public final float fuel, max;
	
	public ItemFuelTank(float weight, float fuel, float max, SlotType[] compatibleSlots) {
		super(8, weight, compatibleSlots);
		this.fuel = fuel;
		this.max = max;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tips, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		CompoundTag tag = stack.getOrCreateTag();
		tips.add(Component.literal("Fuel: "+tag.getInt("fuel")+"/"+max).setStyle(Style.EMPTY.withColor(0xAAAAAA)));
	}

	@Override
	public PartData getPartData() {
		return new FuelTankData(weight, fuel, max, UtilItem.getItemKey(this), compatibleSlots);
	}
	
	@Override
	public PartData getFilledPartData(String param) {
		return new FuelTankData(weight, max, max, UtilItem.getItemKey(this), compatibleSlots);
	}

}
