package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ItemGasCan extends Item implements VehicleInteractItem {
	
	public ItemGasCan(int maxFuel) {
		super(new Item.Properties().tab(ModItems.DSC_ITEMS).stacksTo(1).durability(maxFuel));
	}
	
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		ItemStack stack = ctx.getItemInHand();
		int d = stack.getDamageValue();
		if (d == 0) return InteractionResult.PASS;
		BlockPos pos = new BlockPos(ctx.getClickLocation());
		BlockState state = ctx.getLevel().getBlockState(pos);
		int r;
		if (state.is(Blocks.COAL_BLOCK)) r = 50;
		else if (state.is(Blocks.COAL_ORE)) r = 5;
		else if (state.is(Blocks.DEEPSLATE_COAL_ORE)) r = 8;
		else return InteractionResult.PASS;
		stack.setDamageValue(Math.max(d-r, 0));
		ctx.getLevel().destroyBlock(pos, false);
		return InteractionResult.CONSUME;
	}

	@Override
	public InteractionResult onServerInteract(EntityVehicle vehicle, ItemStack stack, Player player, InteractionHand hand) {
		int md = stack.getMaxDamage();
		int d = stack.getDamageValue();
		int r = (int)vehicle.addFuel(md-d);
		stack.setDamageValue(md-r);
		return InteractionResult.SUCCESS;
	}

}
