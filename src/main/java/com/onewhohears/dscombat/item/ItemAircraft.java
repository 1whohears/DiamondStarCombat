package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class ItemAircraft extends Item {
	
	private final EntityType<? extends EntityAbstractAircraft> type;
	private final String preset;
	
	public ItemAircraft(EntityType<? extends EntityAbstractAircraft> type, String preset) {
		super(new Item.Properties().tab(ModItems.AIRCRAFT).stacksTo(1));
		this.type = type;
		this.preset = preset;
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		System.out.println("use item 2 side "+context.getPlayer().level.isClientSide);
		Level level = context.getLevel();
		ItemStack stack = context.getItemInHand();
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			//ItemStack stack = context.getItemInHand(); // TODO get nbt from this item check SpawnEggItem to see how
			// TODO plane spawns under the ground
			EntityAbstractAircraft plane = type.spawn((ServerLevel)level, null, null, context.getPlayer(), 
					context.getClickedPos(), MobSpawnType.SPAWN_EGG, true, true);
			if (plane != null) {
				stack.shrink(1);
	            level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, context.getClickedPos());
	            plane.setPos(plane.position().add(0, 2, 0));
	            CompoundTag tag = new CompoundTag();
				tag.putString("preset", preset);
				plane.readAdditionalSaveData(tag);
			}
			
		}
		return InteractionResult.CONSUME;
	}

}
