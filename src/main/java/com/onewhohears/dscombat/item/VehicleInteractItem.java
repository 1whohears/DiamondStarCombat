package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface VehicleInteractItem {
	
	InteractionResult onServerInteract(EntityVehicle vehicle, ItemStack stack, Player player, InteractionHand hand);
	
}
