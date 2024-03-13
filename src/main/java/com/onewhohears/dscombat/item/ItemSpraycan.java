package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemSpraycan extends Item implements VehicleInteractItem {

	public ItemSpraycan() {
		super(new Item.Properties().tab(ModItems.DSC_ITEMS).stacksTo(1));
	}

	@Override
	public InteractionResult onServerInteract(EntityVehicle vehicle, ItemStack stack, Player player, InteractionHand hand) {
		return InteractionResult.SUCCESS;
	}

}
