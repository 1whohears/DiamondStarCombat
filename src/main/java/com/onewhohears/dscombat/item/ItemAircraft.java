package com.onewhohears.dscombat.item;

import java.util.Objects;

import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class ItemAircraft extends Item {
	
	private final EntityType<? extends EntityAbstractAircraft> defaultType;
	private final String defultPreset;
	
	public ItemAircraft(EntityType<? extends EntityAbstractAircraft> defaultType, String defaultPreset) {
		super(new Item.Properties().tab(ModItems.AIRCRAFT).stacksTo(1));
		this.defaultType = defaultType;
		this.defultPreset = defaultPreset;
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		if (!(level instanceof ServerLevel)) {
			return InteractionResult.SUCCESS;
		} else {
			ItemStack itemstack = context.getItemInHand();
			BlockPos blockpos = context.getClickedPos();
			Direction direction = context.getClickedFace();
			BlockState blockstate = level.getBlockState(blockpos);
			if (blockstate.is(Blocks.SPAWNER)) {
				BlockEntity blockentity = level.getBlockEntity(blockpos);
				if (blockentity instanceof SpawnerBlockEntity) {
					BaseSpawner basespawner = ((SpawnerBlockEntity)blockentity).getSpawner();
					EntityType<?> entitytype1 = this.getType(itemstack.getOrCreateTag());
					basespawner.setEntityId(entitytype1);
					blockentity.setChanged();
					level.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
					level.gameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, blockpos);
					itemstack.shrink(1);
					return InteractionResult.CONSUME;
				}
			}
			BlockPos blockpos1;
			if (blockstate.getCollisionShape(level, blockpos).isEmpty()) {
				blockpos1 = blockpos;
			} else {
				blockpos1 = blockpos.relative(direction);
			}
			CompoundTag tag = itemstack.getOrCreateTag();
			EntityType<?> entitytype = this.getType(tag);
			Player player = context.getPlayer();
			if (player != null) {
				tag.getCompound("EntityTag").putFloat("yRot", player.getYRot());
			}
			//System.out.println("MAKING ENTITY FROM = "+tag);
			Entity spawn = entitytype.spawn((ServerLevel)level, itemstack, player, blockpos1, 
					MobSpawnType.SPAWN_EGG, true, 
					!Objects.equals(blockpos, blockpos1) && direction == Direction.UP);
			if (spawn != null) {
				spawn.setPos(spawn.position().add(0, 4, 0));
				itemstack.shrink(1);
				level.gameEvent(player, GameEvent.ENTITY_PLACE, blockpos);
			}
			return InteractionResult.CONSUME;
		}
	}
	
	private EntityType<?> getType(CompoundTag nbt) {
		//System.out.println("GETTING TYPE FROM = "+nbt);
		if (nbt.contains("EntityTag", 10)) {
			//System.out.println("HAS EntityTag");
			return this.defaultType;
		}
		//System.out.println("BAD");
		CompoundTag etag = new CompoundTag();
		etag.putString("preset", defultPreset);
		nbt.put("EntityTag", etag);
		return this.defaultType;
	}

}
