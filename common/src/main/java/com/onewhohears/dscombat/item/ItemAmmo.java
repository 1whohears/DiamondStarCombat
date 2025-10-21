package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.client.WeaponAssets;
import com.onewhohears.dscombat.data.weapon.client.WeaponClientStats;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.item.ObjModelItem;
import com.onewhohears.onewholibs.util.UtilItem;
import com.onewhohears.onewholibs.util.UtilMCText;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemAmmo extends Item implements VehicleInteractItem, ObjModelItem, FillableItemCategory {

    @ExpectPlatform
    public static ItemAmmo create(int stackSize, String defaultWeaponId) {
        throw new AssertionError();
    }

	private final String defaultWeaponId;
	
	public ItemAmmo(int size, String defaultWeaponId) {
		super(weaponProps(size));
		this.defaultWeaponId = defaultWeaponId;
	}
	
	@Override
	public void fillItemCategory(@NotNull List<ItemStack> items) {
		String itemId = UtilItem.getItemKeyString(this);
		for (int i = 0; i < WeaponPresets.get().getNum(); ++i) {
			WeaponStats w = WeaponPresets.get().getAll()[i];
			if (!w.getItemKey().equals(itemId)) continue;
			ItemStack test = new ItemStack(this);
			CompoundTag tag = new CompoundTag();
			tag.putString("weapon", w.getId());
			test.setTag(tag);
			items.add(test);
		}
	}
	
	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tips,
								@NotNull TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		String id = getWeaponId(stack);
		WeaponStats wd = WeaponPresets.get().get(id);
		if (wd == null) return;
		wd.addToolTips(tips, isAdvanced.isAdvanced());
		if (isAdvanced.isAdvanced()) {
			tips.add(ItemVehicle.formatTooltip("WeaponId", id));
		}
	}
	
	@Override
	public @NotNull Component getName(@NotNull ItemStack stack) {
		String id = getWeaponId(stack);
		WeaponStats wd = WeaponPresets.get().get(id);
		if (wd == null) return UtilMCText.translatable(getDescriptionId()).append(" ")
				.append(UtilMCText.translatable("error.dscombat.unknown_preset"));
		return wd.getDisplayNameComponent().append(" ")
				.append(UtilMCText.literal(wd.getWeaponTypeCode())).append(" ")
				.append(UtilMCText.translatable("info.dscombat.ammo"));
	}
	
	public static String getWeaponId(ItemStack stack) {
		if (stack.getItem() instanceof ItemAmmo ia) {
			if (stack.getTag() != null && stack.getTag().contains("weapon"))
				return stack.getTag().getString("weapon");
			return ia.defaultWeaponId;
		}
		return "";
	}
	
	public static Properties weaponProps(int stackSize) {
		return new Item.Properties().stacksTo(stackSize);
	}

	@Override
	public InteractionResult onServerInteract(EntityVehicle vehicle, ItemStack stack, Player player, InteractionHand hand) {
		if (!vehicle.isOperational()) return InteractionResult.FAIL;
		String ammoId = ItemAmmo.getWeaponId(stack);
		for (EntityTurret t : vehicle.getTurrets()) {
			int o = t.addAmmo(stack.getCount());
			stack.setCount(o);
			if (stack.getCount() == 0) return InteractionResult.SUCCESS;
		}
		int o = vehicle.weaponSystem.addAmmo(ammoId, stack.getCount(), true);
		stack.setCount(o);
		return InteractionResult.SUCCESS;
	}
	
	@Override
	public @NotNull ItemStack getDefaultInstance() {
		ItemStack stack = super.getDefaultInstance();
		stack.getOrCreateTag().putString("weapon", defaultWeaponId);
		return stack;
	}

	@Override
	public @NotNull String getPreset(@NotNull ItemStack stack) {
        return getWeaponId(stack);
	}

	@Override
	public @NotNull String getObjModelId(@NotNull String preset) {
		WeaponStats wd = WeaponPresets.get().get(preset);
		if (wd == null) return "";
        String assetId = wd.getAssetId();
		WeaponClientStats<?> assets = WeaponAssets.get().get(assetId);
		if (assets == null) return "";
		return assets.getModelId();
	}

	@Override
	public ObjEntityModels.@NotNull ModelOverrides getItemModelOverrides(@NotNull String preset) {
		//return ObjEntityModels.get().getModelOverride("ammo_item");
		return ObjEntityModels.NO_OVERRIDES;
	}
}
