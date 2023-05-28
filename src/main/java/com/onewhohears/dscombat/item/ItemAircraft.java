package com.onewhohears.dscombat.item;

import java.util.List;
import java.util.function.Predicate;

import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.aircraft.AircraftPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
	
	private final EntityType<? extends EntityAircraft> entityType;
	private final String defaultPreset;
	
	public ItemAircraft(EntityType<? extends EntityAircraft> entityType, AircraftPreset defaultPreset) {
		super(new Item.Properties().tab(ModItems.AIRCRAFT).stacksTo(1));
		this.entityType = entityType;
		this.defaultPreset = defaultPreset.getId();
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
				ItemStack spawn_data_stack = spawnData(itemstack, player);
				EntityAircraft e = entityType.create(level);
				Vec3 pos = hitresult.getLocation();
				if (e.isCustomBoundingBox()) e.setPos(pos.add(0, e.getBbHeight()/2d, 0));
				else e.setPos(pos);
				if (!level.noCollision(e, e.getBoundingBox())) 
					return InteractionResultHolder.fail(itemstack);
				if (!level.isClientSide) {
					int above = 0;
					if (e.isCustomBoundingBox()) above = (int)(e.getBbHeight()/2d)+1;
					Entity entity = entityType.spawn((ServerLevel)level, 
							spawn_data_stack, player, 
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
	
	private ItemStack spawnData(ItemStack itemstack, Player player) {
		ItemStack copy = itemstack.copy();
		CompoundTag tag = copy.getOrCreateTag();
		if (!tag.contains("EntityTag", 10)) {
			CompoundTag et = new CompoundTag();
			et.putString("preset", getPresetName(itemstack));
			System.out.println("item aircraft preset "+getPresetName(itemstack));
			et.putBoolean("merged_preset", false);
			tag.put("EntityTag", et);
		}
		CompoundTag et = tag.getCompound("EntityTag");
		et.putFloat("yRot", player.getYRot());
		et.putFloat("current_throttle", 0);
		et.putBoolean("landing_gear", true);
		return copy;
	}
	
	public String getPresetName(ItemStack itemstack) {
		CompoundTag tag = itemstack.getTag();
		if (tag == null || !tag.contains("preset")) return defaultPreset;
		return tag.getString("preset");
	}
	
	/*@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tips, TooltipFlag isAdvanced) {
		CompoundTag tag = stack.getTag();
		if (tag == null || !tag.contains("EntityTag")) return;
		CompoundTag et = tag.getCompound("EntityTag");
		
	}*/
	
	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		if (tag == null || !tag.contains("EntityTag")) {
			String name = getPresetName(stack);
			AircraftPreset ap = AircraftPresets.get().getPreset(name);
			if (ap == null) return new TranslatableComponent(getDescriptionId()).append(" unknown preset!");
			return ap.getDisplayName().setStyle(Style.EMPTY.withColor(0x55FFFF));
		}
		String owner = tag.getCompound("EntityTag").getString("owner");
		if (owner.isEmpty()) owner = "Someone";
		return new TextComponent(owner+"'s ").append(super.getName(stack))
				.setStyle(Style.EMPTY.withColor(0xFFAA00).withBold(true));
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		return tag != null && tag.contains("EntityTag");
	}

}
