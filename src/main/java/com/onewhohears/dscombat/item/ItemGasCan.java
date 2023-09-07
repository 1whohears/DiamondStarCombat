package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ItemGasCan extends Item {
	
	public ItemGasCan(int maxFuel) {
		super(new Item.Properties().tab(ModItems.PARTS).stacksTo(1).durability(maxFuel));
	}
	
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		ItemStack stack = ctx.getItemInHand();
		int d = stack.getDamageValue();
		if (d == 0) return InteractionResult.PASS;
		BlockPos pos = new BlockPos(ctx.getClickLocation());
		BlockState state = ctx.getLevel().getBlockState(pos);
		if (!(state.is(Blocks.COAL_ORE) || state.is(Blocks.DEEPSLATE_COAL_ORE))) return InteractionResult.PASS;
		stack.setDamageValue(Math.max(d-10, 0));
		ctx.getLevel().destroyBlock(pos, false);
		return InteractionResult.CONSUME;
	}

}
