package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class ItemParachute extends Item {

	public ItemParachute() {
		super(new Item.Properties().tab(ModItems.DSC_ITEMS).stacksTo(1));
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (player.isOnGround()) return InteractionResultHolder.fail(itemstack);
		if (!level.isClientSide) {
			Entity entity = ModEntities.PARACHUTE.get().spawn((ServerLevel)level, itemstack, player, player.blockPosition(), 
					MobSpawnType.SPAWN_EGG, false, false);
			if (entity != null) {
				itemstack.shrink(1);
				player.startRiding(entity);
				entity.setDeltaMovement(player.getDeltaMovement());
				level.gameEvent(player, GameEvent.ENTITY_PLACE, player.position());
			}
		} else return InteractionResultHolder.pass(itemstack);
		player.awardStat(Stats.ITEM_USED.get(this));
		return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
	}

}
