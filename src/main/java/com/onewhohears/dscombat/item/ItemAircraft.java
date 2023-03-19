package com.onewhohears.dscombat.item;

import java.util.List;
import java.util.function.Predicate;

import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ItemAircraft extends Item {
	
	private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS
			.and(Entity::isPickable);
	
	private final EntityType<? extends EntityAircraft> defaultType;
	private final String defultPreset;
	private final boolean addHalfHeight;
	
	public ItemAircraft(EntityType<? extends EntityAircraft> defaultType, String defaultPreset,
			boolean addHalfHeight) {
		super(new Item.Properties().tab(ModItems.AIRCRAFT).stacksTo(1));
		this.defaultType = defaultType;
		this.defultPreset = defaultPreset;
		this.addHalfHeight = addHalfHeight;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		HitResult hitresult = getPlayerPOVHitResult(level, player, 
				ClipContext.Fluid.ANY);
		if (hitresult.getType() == HitResult.Type.MISS) 
			return InteractionResultHolder.pass(itemstack);
		else {
			Vec3 vec3 = player.getViewVector(1.0F);
			List<Entity> list = level.getEntities(player, 
					player.getBoundingBox().expandTowards(vec3.scale(5.0D)).inflate(1.0D), 
					ENTITY_PREDICATE);
			if (!list.isEmpty()) {
				Vec3 vec31 = player.getEyePosition();
				for(Entity entity : list) {
					AABB aabb = entity.getBoundingBox().inflate((double)entity.getPickRadius());
					if (aabb.contains(vec31)) 
						return InteractionResultHolder.pass(itemstack);
				}
			}
			if (hitresult.getType() == HitResult.Type.BLOCK) {
				CompoundTag tag = itemstack.getOrCreateTag();
				EntityType<?> entitytype = getType(tag);
				if (player != null) {
					CompoundTag et = tag.getCompound("EntityTag");
					et.putFloat("yRot", player.getYRot());
					et.putFloat("current_throttle", 0);
				}
				Entity e = entitytype.create(level);
				if (!level.noCollision(e, e.getBoundingBox())) 
					return InteractionResultHolder.fail(itemstack);
				if (!level.isClientSide) {
					Vec3 pos = hitresult.getLocation();
					int above = 0;
					if (addHalfHeight) above = (int)(e.getBbHeight()/2d)+1;
					Entity entity = entitytype.spawn((ServerLevel)level, 
							itemstack, player, 
							new BlockPos(pos).above(above), 
							MobSpawnType.SPAWN_EGG, 
							false, false);
					if (entity != null) {
						level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
						itemstack.shrink(1);
					}
				}
				player.awardStat(Stats.ITEM_USED.get(this));
				return InteractionResultHolder.sidedSuccess(itemstack, 
						level.isClientSide());
			} else return InteractionResultHolder.pass(itemstack);
		}
	}
	
	/*@Override
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
			EntityType<?> entitytype = getType(tag);
			Player player = context.getPlayer();
			if (player != null) {
				CompoundTag et = tag.getCompound("EntityTag");
				et.putFloat("yRot", player.getYRot());
				et.putFloat("current_throttle", 0);
			}
			//System.out.println("MAKING ENTITY FROM = "+tag);
			Entity spawn = entitytype.spawn((ServerLevel)level, itemstack, 
					player, blockpos1.above(4), MobSpawnType.SPAWN_EGG, 
					true, true);
			if (spawn != null) {
				itemstack.shrink(1);
				level.gameEvent(player, GameEvent.ENTITY_PLACE, blockpos);
			}
			return InteractionResult.CONSUME;
		}
	}*/
	
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
