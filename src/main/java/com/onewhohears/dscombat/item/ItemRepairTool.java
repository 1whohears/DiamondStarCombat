package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemRepairTool extends Item implements VehicleInteractItem {
	
	public final float repair;
	
	public ItemRepairTool(int durability, float repair) {
		super(new Item.Properties().tab(ModItems.DSC_ITEMS).stacksTo(1).durability(durability));
		this.repair = repair;
	}

	@Override
	public InteractionResult onServerInteract(EntityVehicle vehicle, ItemStack stack, Player player, InteractionHand hand) {
		int damage = vehicle.onRepairTool(repair);
		stack.hurtAndBreak(damage, player, p -> p.broadcastBreakEvent(hand));
		player.awardStat(Stats.ITEM_USED.get(this));
		return InteractionResult.SUCCESS;
	}

}
