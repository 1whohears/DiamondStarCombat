package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.vehicle.VehiclePresets;
import com.onewhohears.dscombat.data.vehicle.client.VehicleClientPresets;
import com.onewhohears.dscombat.data.vehicle.client.VehicleClientStats;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModCMTabs;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.item.ObjModelItem;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.math.UtilGeometry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class ItemVehicle extends Item implements ObjModelItem, FillableItemCategory {

    @ExpectPlatform
    public static ItemVehicle create(String defaultPresetId) {
        throw new AssertionError();
    }

	private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS
			.and(Entity::isPickable);
	
	private final String defaultPreset;
	
	public ItemVehicle(String defaultPreset) {
		this(new Item.Properties().stacksTo(1).arch$tab(ModCMTabs.VEHICLES), defaultPreset);
	}

    protected ItemVehicle(Item.Properties props, String defaultPreset) {
        super(props);
        this.defaultPreset = defaultPreset;
        FillableItemCategory.onInit(this, props);
    }
	
	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		HitResult hitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
		if (hitresult.getType() == HitResult.Type.MISS) {
			return InteractionResultHolder.pass(itemstack);
		} else {
			Vec3 vec3 = player.getViewVector(1.0F);
			List<Entity> list = level.getEntities(player, 
					player.getBoundingBox().expandTowards(vec3.scale(5.0D)).inflate(1.0D), 
					ENTITY_PREDICATE);
			if (!list.isEmpty()) {
				Vec3 vec31 = player.getEyePosition();
				for(Entity entity : list) {
					AABB aabb = entity.getBoundingBox().inflate(entity.getPickRadius());
					if (aabb.contains(vec31)) 
						return InteractionResultHolder.pass(itemstack);
				}
			}
			if (hitresult.getType() == HitResult.Type.BLOCK) {
				String presetName = getPresetName(itemstack);
				VehicleStats vs = VehiclePresets.get().get(presetName);
				if (vs == null) vs = VehiclePresets.get().get(defaultPreset);
				EntityType<? extends EntityVehicle> entityType = vs.getEntityType();
				ItemStack spawn_data_stack = spawnData(itemstack, player, vs.getId(), player.getYRot(), null);
				EntityVehicle e = entityType.create(level);
				Vec3 pos = hitresult.getLocation();
				if (e.isCustomBoundingBox()) e.setPos(pos.add(0, e.getBbHeight()/2d, 0));
				else e.setPos(pos);
				if (!level.noCollision(e, e.getBoundingBox())) 
					return InteractionResultHolder.fail(itemstack);
				if (!level.isClientSide()) {
					int above = 0;
					if (e.isCustomBoundingBox()) above = (int)(e.getBbHeight()/2d)+1;
					Entity entity = entityType.spawn((ServerLevel)level, 
							spawn_data_stack, player, 
							UtilGeometry.toBlockPos(pos).above(above),
							MobSpawnType.SPAWN_EGG, 
							false, false);
					if (entity != null) {
						level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
						itemstack.shrink(1);
					}
				}
				player.awardStat(Stats.ITEM_USED.get(this));
				return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
			} else return InteractionResultHolder.pass(itemstack);
		}
	}
	
	public ItemStack spawnData(ItemStack itemstack, Player player, String preset, float yRot,
                               @Nullable CompoundTag additionalNbt) {
		ItemStack copy = itemstack.copy();
		CompoundTag tag = copy.getOrCreateTag();
		if (!tag.contains("EntityTag", 10)) {
			CompoundTag et = new CompoundTag();
			et.putBoolean("merged_preset", false);
			et.putUUID("owner_id", player.getUUID());
			tag.put("EntityTag", et);
		}
		CompoundTag et = tag.getCompound("EntityTag");
        et.merge(additionalNbt);
		et.putString("preset", preset);
		et.putFloat("yRot", yRot);
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
        return getPresetName(itemstack, defaultPreset);
    }

	public static String getPresetName(ItemStack itemstack, String defaultPreset) {
		CompoundTag tag = itemstack.getTag();
		if (tag == null) return defaultPreset;
		if (tag.contains("preset")) return tag.getString("preset");
		if (tag.contains("EntityTag")) {
			CompoundTag eTag = tag.getCompound("EntityTag");
			if (eTag.contains("preset")) return eTag.getString("preset");
		}
		return defaultPreset;
	}
	
	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tips,
								@NotNull TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		CompoundTag tag = stack.getTag();
		if (tag != null && tag.contains("EntityTag")) {
			CompoundTag et = tag.getCompound("EntityTag");
			if (et.contains("health")) tips.add(UtilMCText.translatable("info.dscombat.health")
					.append(": " + (int) et.getFloat("health")).setStyle(Style.EMPTY.withColor(0xAAAAAA)));
			if (et.contains("fuel")) tips.add(UtilMCText.translatable("info.dscombat.fuel")
					.append(": " + (int) et.getFloat("fuel")).setStyle(Style.EMPTY.withColor(0xAAAAAA)));
			if (et.contains("flares")) tips.add(UtilMCText.translatable("info.dscombat.flares")
					.append(": " + (int) et.getFloat("flares")).setStyle(Style.EMPTY.withColor(0xAAAAAA)));
		}
		if (isAdvanced.isAdvanced()) {
			tips.add(formatTooltip("VehicleId", getPreset(stack)));
		}
	}

	public static Component formatTooltip(String key, String value) {
		return Component.literal(String.format("%s: \"%s\"", key, value)).withStyle(ChatFormatting.DARK_GRAY);
	}
	
	@Override
	public @NotNull Component getName(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		String presetId = getPresetName(stack);
		VehicleStats vs = VehiclePresets.get().get(presetId);
		if (tag == null || !tag.contains("EntityTag")) {
			if (vs == null) return UtilMCText.translatable(getDescriptionId()).append(" unknown preset!");
			return vs.getDisplayNameComponent().setStyle(Style.EMPTY.withColor(0x55FFFF));
		}
		CompoundTag etag = tag.getCompound("EntityTag");
		if (etag.contains("CustomName", 8)) {
			String cn = etag.getString("CustomName");
			try { return Component.Serializer.fromJson(cn); } 
			catch (Exception e) {}
		}
		String owner = etag.getString("owner_name");
		if (owner.isEmpty()) owner = "Someone";
		MutableComponent component = UtilMCText.literal(owner+"'s ")
				.setStyle(Style.EMPTY.withColor(0xFFAA00).withBold(true));
		if (vs == null) return component.append(super.getName(stack));
		return component.append(vs.getBaseDisplayName());
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		return tag != null && tag.contains("EntityTag");
	}
	
	@Override
	public void fillItemCategory(@NotNull List<ItemStack> items) {
		VehicleStats[] presets = VehiclePresets.get().getAll();
        for (VehicleStats preset : presets) {
            if (preset.getItem().is(this)) {
				ItemStack stack = new ItemStack(this);
				stack.getOrCreateTag().putString("preset", preset.getId());
				items.add(stack);
            }
        }
	}

	@Override
	public ObjEntityModels.@NotNull ModelOverrides getItemModelOverrides(@NotNull String preset) {
		VehicleStats vs = VehiclePresets.get().get(preset);
		if (vs == null) vs = VehiclePresets.get().get(getDefaultPreset());
		if (vs == null) return ObjEntityModels.NO_OVERRIDES;
		String assetId = vs.getAssetId();
		VehicleClientStats vcs = VehicleClientPresets.get().get(assetId);
		if (vcs == null) return ObjEntityModels.NO_OVERRIDES;
		return vcs.getItemModelOverrides();
	}

	public String getDefaultPreset() {
		return defaultPreset;
	}

	@Override
	public @NotNull String getPreset(@NotNull ItemStack stack) {
		return getPresetName(stack);
	}

	@Override
	public @NotNull String getObjModelId(@NotNull String preset) {
		VehicleStats vs = VehiclePresets.get().get(preset);
		if (vs == null) vs = VehiclePresets.get().get(getDefaultPreset());
		if (vs == null) return "";
		String assetId = vs.getAssetId();
		VehicleClientStats vcs = VehicleClientPresets.get().get(assetId);
		if (vcs == null) return "";
		return vcs.getModelId();
	}

    public @NotNull Item asItem() {
        return this;
    }
}
