package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
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
		int damage = 0;
		if (vehicle.isEngineFire()) {
			vehicle.setEngineFire(false);
			damage = 40;
		} else if (vehicle.isFuelLeak()) {
			vehicle.setFuelLeak(false);
			damage = 15;
		} else if (!vehicle.isMaxHealth()) {
			vehicle.addHealth(repair);
			damage = 1;
		} else {
			vehicle.playRepairSound();
			return InteractionResult.PASS;
		}
		stack.hurtAndBreak(damage, player, p -> p.broadcastBreakEvent(hand));
		player.awardStat(Stats.ITEM_USED.get(this));
		vehicle.playRepairSound();
		return InteractionResult.SUCCESS;
	}

}
