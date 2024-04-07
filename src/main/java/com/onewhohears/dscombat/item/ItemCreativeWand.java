package com.onewhohears.dscombat.item;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemCreativeWand extends Item implements VehicleInteractItem {
	
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
			tooltips.add(UtilMCText.translatable(translatableTooltips[i]));
		}
	}

	@Override
	public InteractionResult onServerInteract(EntityVehicle vehicle, ItemStack stack, Player player, InteractionHand hand) {
		if (modifyAircraft(vehicle)) {
			player.awardStat(Stats.ITEM_USED.get(this));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
	
	

}
