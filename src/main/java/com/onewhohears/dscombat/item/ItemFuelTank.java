package com.onewhohears.dscombat.item;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.stats.FuelTankStats;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemFuelTank extends ItemPart {
	
	public ItemFuelTank(int stackSize) {
		super(stackSize);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tips, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		FuelTankStats stats = (FuelTankStats)getPartStats(stack);
		CompoundTag tag = stack.getOrCreateTag();
		tips.add(UtilMCText.literal("Fuel: "+tag.getInt("fuel")+"/"+stats.getMaxFuel()).setStyle(Style.EMPTY.withColor(0xAAAAAA)));
	}

}
