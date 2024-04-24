package com.onewhohears.dscombat.item;

import java.util.List;
import java.util.function.Consumer;

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
	public final Consumer<EntityVehicle> onServerInteract;
	/**
	 * @param onServerInteract runs on server side only when right click aircraft with this wand
	 */
	public ItemCreativeWand(Consumer<EntityVehicle> onServerInteract) {
		this(onServerInteract, "");
	}
	/**
	 * @param onServerInteract runs on server side only when right click aircraft with this wand
	 * @param translatableTooltips translatable strings for info about this wand
	 */
	public ItemCreativeWand(Consumer<EntityVehicle> onServerInteract, String... translatableTooltips) {
		super(new Item.Properties().tab(ModItems.DSC_ITEMS).stacksTo(1));
		this.translatableTooltips = translatableTooltips;
		this.onServerInteract = onServerInteract;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltips, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tooltips, isAdvanced);
		for (int i = 0; i < translatableTooltips.length; ++i) 
			if (!translatableTooltips[i].isEmpty()) 
				tooltips.add(UtilMCText.translatable(translatableTooltips[i]));
	}

	@Override
	public InteractionResult onServerInteract(EntityVehicle vehicle, ItemStack stack, Player player, InteractionHand hand) {
		onServerInteract.accept(vehicle);
		player.awardStat(Stats.ITEM_USED.get(this));
		return InteractionResult.SUCCESS;
	}
	
	

}
