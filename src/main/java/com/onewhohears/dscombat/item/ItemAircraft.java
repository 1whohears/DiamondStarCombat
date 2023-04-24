package com.onewhohears.dscombat.item;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.registries.ForgeRegistries;

public class ItemAircraft extends Item {
	
	private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS
			.and(Entity::isPickable);
	
	private final String entityId;
	private EntityType<? extends EntityAircraft> entityType;
	
	public ItemAircraft(String entityId) {
		super(new Item.Properties().tab(ModItems.AIRCRAFT).stacksTo(1));
		this.entityId = entityId;
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
				spawnData(tag, player);
				EntityType<? extends EntityAircraft> et = getEntityType();
				if (et == null) return InteractionResultHolder.pass(itemstack);
				EntityAircraft e = et.create(level);
				Vec3 pos = hitresult.getLocation();
				if (e.isCustomBoundingBox()) e.setPos(pos.add(0, e.getBbHeight()/2d, 0));
				else e.setPos(pos);
				if (!level.noCollision(e, e.getBoundingBox())) 
					return InteractionResultHolder.fail(itemstack);
				if (!level.isClientSide) {
					int above = 0;
					if (e.isCustomBoundingBox()) above = (int)(e.getBbHeight()/2d)+1;
					Entity entity = et.spawn((ServerLevel)level, 
							itemstack, player, 
							new BlockPos(pos).above(above), 
							MobSpawnType.SPAWN_EGG, 
							false, false);
					if (entity != null) {
						level.gameEvent(player, GameEvent.ENTITY_PLACE, new BlockPos(pos));
						itemstack.shrink(1);
					}
				}
				player.awardStat(Stats.ITEM_USED.get(this));
				return InteractionResultHolder.sidedSuccess(itemstack, 
						level.isClientSide());
			} else return InteractionResultHolder.pass(itemstack);
		}
	}
	
	private void spawnData(CompoundTag tag, Player player) {
		if (!tag.contains("EntityTag", 10)) {
			CompoundTag et = new CompoundTag();
			et.putString("preset", getPresetName());
			et.putBoolean("merged_preset", false);
			tag.put("EntityTag", et);
		}
		CompoundTag et = tag.getCompound("EntityTag");
		et.putFloat("yRot", player.getYRot());
		et.putFloat("current_throttle", 0);
		et.putBoolean("landing_gear", true);
	}
	
	public String getPresetName() {
		return ForgeRegistries.ITEMS.getKey(this).getPath();
	}
	
	@SuppressWarnings("unchecked")
	public EntityType<? extends EntityAircraft> getEntityType() {
		if (entityType != null) return entityType;
		try {
			entityType = (EntityType<? extends EntityAircraft>) ForgeRegistries.ENTITIES
				.getHolder(new ResourceLocation(entityId)).get().value();
		} catch (ClassCastException e) {
		} catch (NoSuchElementException e) {
		}
		return entityType;
	}

}
