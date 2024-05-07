package com.onewhohears.dscombat.item;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilItem;
import com.onewhohears.dscombat.util.UtilMCText;

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

public class ItemAmmo extends Item implements VehicleInteractItem {
	
	private final String defaultWeaponId;
	
	public ItemAmmo(int size, String defaultWeaponId) {
		super(weaponProps(size));
		this.defaultWeaponId = defaultWeaponId;
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() != ModItems.WEAPONS.getId() && group.getId() != CreativeModeTab.TAB_SEARCH.getId()) return;
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
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tips, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		String id = getWeaponId(stack);
		WeaponStats wd = WeaponPresets.get().get(id);
		if (wd == null) return;
		wd.addToolTips(tips, isAdvanced.isAdvanced());
	}
	
	@Override
	public Component getName(ItemStack stack) {
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
			if (!stack.getOrCreateTag().contains("weapon")) return ia.defaultWeaponId;
			return stack.getOrCreateTag().getString("weapon");
		}
		return "";
	}
	
	public static Properties weaponProps(int stackSize) {
		return new Item.Properties().tab(ModItems.WEAPONS).stacksTo(stackSize);
	}

	@Override
	public InteractionResult onServerInteract(EntityVehicle vehicle, ItemStack stack, Player player, InteractionHand hand) {
		String ammoId = ItemAmmo.getWeaponId(stack);
		for (EntityTurret t : vehicle.getTurrets()) {
			WeaponInstance<?> wd = t.getWeaponData();
			if (wd == null) continue;
			if (!wd.getStatsId().equals(ammoId)) continue;
			int o = wd.addAmmo(stack.getCount());
			t.setAmmo(wd.getCurrentAmmo());
			t.updateDataAmmo();
			stack.setCount(o);
			if (stack.getCount() == 0) return InteractionResult.SUCCESS;
		}
		int o = vehicle.weaponSystem.addAmmo(ammoId, stack.getCount(), true);
		stack.setCount(o);
		return InteractionResult.SUCCESS;
	}

}
