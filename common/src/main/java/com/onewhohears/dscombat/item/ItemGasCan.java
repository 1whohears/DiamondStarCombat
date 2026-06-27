package com.onewhohears.dscombat.item;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.init.ModCMTabs;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ItemGasCan extends Item {
	
	public ItemGasCan(int maxFuel) {
		super(new Item.Properties().stacksTo(1).durability(maxFuel).arch$tab(ModCMTabs.DSC_ITEMS));
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getDamageValue() == 0) return InteractionResultHolder.pass(stack);
		BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        // FIXME was this ForgeEventFactory#onBucketUse call needed?
		//InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onBucketUse(player, level, stack, blockhitresult);
		//if (ret != null) return ret;
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
				bucketpickup.getPickupSound().ifPresent((sound) -> {
					player.playSound(sound, 1.0F, 1.0F);
				});
				level.gameEvent(player, GameEvent.FLUID_PICKUP, blockpos);
				fillGasCan(stack, DSCGameRules.getFuelPerOilBlock(level));
				return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
			}
		}
		return InteractionResultHolder.fail(stack);
	}
	
	protected void fillGasCan(ItemStack stack, int amount) {
		stack.setDamageValue(Math.max(stack.getDamageValue()-amount, 0));
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tips, @NotNull TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		tips.add(UtilMCText.translatable("info.dscombat.gas_can_fill").setStyle(Style.EMPTY.withColor(0xAAAAAA)));
		int fuel = stack.getMaxDamage() - stack.getDamageValue();
		tips.add(UtilMCText.translatable("info.dscombat.fuel").append(": " + fuel + " / " + stack.getMaxDamage()).setStyle(Style.EMPTY.withColor(0xAAAAAA)));
	}

}
