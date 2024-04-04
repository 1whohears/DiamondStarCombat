package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.ForgeEventFactory;

public class ItemGasCan extends Item {
	
	public ItemGasCan(int maxFuel) {
		super(new Item.Properties().tab(ModItems.DSC_ITEMS).stacksTo(1).durability(maxFuel));
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getDamageValue() == 0) return InteractionResultHolder.pass(stack);
		BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
		InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onBucketUse(player, level, stack, blockhitresult);
		if (ret != null) return ret;
		if (blockhitresult.getType() == HitResult.Type.MISS) return InteractionResultHolder.pass(stack);
		else if (blockhitresult.getType() != HitResult.Type.BLOCK) return InteractionResultHolder.pass(stack);
		BlockPos blockpos = blockhitresult.getBlockPos();
		Direction direction = blockhitresult.getDirection();
		BlockPos blockpos1 = blockpos.relative(direction);
		if (!(level.mayInteract(player, blockpos) 
				&& player.mayUseItemAt(blockpos1, direction, stack))) 
			return InteractionResultHolder.fail(stack);
		BlockState blockstate1 = level.getBlockState(blockpos);
		if (!blockstate1.getFluidState().is(ModTags.Fluids.OIL)) 
			return InteractionResultHolder.fail(stack);
		if (blockstate1.getBlock() instanceof BucketPickup) {
			BucketPickup bucketpickup = (BucketPickup)blockstate1.getBlock();
			ItemStack filledStack = bucketpickup.pickupBlock(level, blockpos, blockstate1);
			if (!filledStack.isEmpty()) {
				player.awardStat(Stats.ITEM_USED.get(this));
				bucketpickup.getPickupSound(blockstate1).ifPresent((sound) -> {
					player.playSound(sound, 1.0F, 1.0F);
				});
				level.gameEvent(player, GameEvent.FLUID_PICKUP, blockpos);
				fillGasCan(stack, 50);
				return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
			}
		}
		return InteractionResultHolder.fail(stack);
	}
	
	protected void fillGasCan(ItemStack stack, int amount) {
		stack.setDamageValue(Math.max(stack.getDamageValue()-amount, 0));
	}
	
	@Override
	public float getXpRepairRatio(ItemStack stack) {
		return 0.5f;
	}

}
