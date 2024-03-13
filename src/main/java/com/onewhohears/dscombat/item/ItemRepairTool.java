package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModItems;

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
		if (vehicle.isEngineFire()) {
			vehicle.setEngineFire(false);
			vehicle.playRepairSound();
			stack.hurtAndBreak(40, player, 
				(p) -> p.broadcastBreakEvent(hand));
		} else if (vehicle.isFuelLeak()) {
			vehicle.setFuelLeak(false);
			vehicle.playRepairSound();
			stack.hurtAndBreak(15, player, 
				(p) -> p.broadcastBreakEvent(hand));
		} else if (!vehicle.isMaxHealth()) {
			vehicle.addHealth(repair);
			stack.hurtAndBreak(1, player, 
				(p) -> p.broadcastBreakEvent(hand));
		} else {
			vehicle.playRepairSound();
			return InteractionResult.PASS;
		}
		return InteractionResult.SUCCESS;
	}

}
