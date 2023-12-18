package com.onewhohears.dscombat.item;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemCreativeWand extends Item {
	
	public final String[] translatableTooltips;
	
	public ItemCreativeWand(@Nullable String[] translatableTooltips) {
		super(new Item.Properties().tab(ModItems.DSC_ITEMS).stacksTo(1));
		this.translatableTooltips = translatableTooltips;
	}
	
	/**
	 * override this method
	 * runs on server side only when right click aircraft with this wand
	 * @param plane aircraft to modify
	 * @return if the modification was successful
	 */
	public boolean modifyAircraft(EntityVehicle plane) {
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltips, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tooltips, isAdvanced);
		if (translatableTooltips != null) for (int i = 0; i < translatableTooltips.length; ++i) {
			tooltips.add(Component.translatable(translatableTooltips[i]));
		}
	}
	
	

}
