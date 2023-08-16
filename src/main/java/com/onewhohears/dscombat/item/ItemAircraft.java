package com.onewhohears.dscombat.item;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.aircraft.AircraftPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
		if (tag.contains("display", 10)) {
			CompoundTag display = tag.getCompound("display");
			if (display.contains("Name", 8)) {
				et.putString("CustomName", display.getString("Name"));
				et.putBoolean("CustomNameVisible", true);
			}
		}
		return copy;
	}
	
	public String getPresetName(ItemStack itemstack) {
		CompoundTag tag = itemstack.getTag();
		if (tag == null || !tag.contains("preset")) return defaultPreset;
		return tag.getString("preset");
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tips, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		if (!isAdvanced.isAdvanced()) return;
		CompoundTag tag = stack.getTag();
		if (tag == null || !tag.contains("EntityTag")) return;
		CompoundTag et = tag.getCompound("EntityTag");
		// TODO 1.1 show some advanced tool tips for aircraft
	}
	
	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		if (tag == null || !tag.contains("EntityTag")) {
			String name = getPresetName(stack);
			AircraftPreset ap = AircraftPresets.get().getPreset(name);
			if (ap == null) return Component.translatable(getDescriptionId()).append(" unknown preset!");
			return ap.getDisplayNameComponent().setStyle(Style.EMPTY.withColor(0x55FFFF));
		}
		CompoundTag etag = tag.getCompound("EntityTag");
		if (etag.contains("CustomName", 8)) {
			String cn = etag.getString("CustomName");
			try { return Component.Serializer.fromJson(cn); } 
			catch (Exception e) {}
		}
		String owner = etag.getString("owner");
		if (owner.isEmpty()) owner = "Someone";
		return Component.literal(owner+"'s ").append(super.getName(stack))
				.setStyle(Style.EMPTY.withColor(0xFFAA00).withBold(true));
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		return tag != null && tag.contains("EntityTag");
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() != ModItems.AIRCRAFT.getId()) return;
		AircraftPreset[] presets = AircraftPresets.get().getAllPresets();
		for (int i = 0; i < presets.length; ++i) {
			if (presets[i].getItem().getDescriptionId().equals(this.getDescriptionId())) {
				items.add(presets[i].getItem());
			}
		}
	}

}
