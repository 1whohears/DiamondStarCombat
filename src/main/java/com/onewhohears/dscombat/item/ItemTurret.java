package com.onewhohears.dscombat.item;

import java.util.List;
import java.util.function.Consumer;

import com.onewhohears.dscombat.data.parts.client.PartAssets;
import com.onewhohears.dscombat.data.parts.client.PartClientStats;
import com.onewhohears.dscombat.data.parts.instance.TurretInstance;
import com.onewhohears.dscombat.data.parts.stats.PartStats;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.item.ObjModelItem;
import com.onewhohears.onewholibs.util.UtilMCText;

import com.onewhohears.dscombat.util.UtilPresetParse;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class ItemTurret extends ItemPart implements ObjModelItem {
	
	public ItemTurret(int stackSize) {
		super(stackSize);
	}

	public ItemTurret(int stackSize, String defaultPresetId) {
		super(stackSize, defaultPresetId);
	}
	
	@Override
	public @NotNull Component getName(@NotNull ItemStack stack) {
		TurretInstance<?> data = (TurretInstance<?>) UtilPresetParse.parsePartFromItem(stack);
		if (data == null) return super.getName(stack);
		MutableComponent name = ((MutableComponent)super.getName(stack)).append(" ");
		String weapon = data.getWeaponId();
		if (weapon.isEmpty()) {
			name.append("EMPTY");
		} else {
			WeaponStats wd = WeaponPresets.get().get(weapon);
			if (wd != null) {
				name.append(wd.getDisplayNameComponent()).append(" ")
						.append(UtilMCText.literal(wd.getWeaponTypeCode()));
			}
			else name.append(weapon+"?");
		}
		int ammo = (int)data.getCurrentAmmo();
		int max = (int)data.getMaxAmmo();
		if (max != 0) name.append(" "+ammo+"/"+max);
		return name;	
	}
	
	@Override
	protected void fillItemCategory(PartStats stats, NonNullList<ItemStack> items) {
		List<String> list = WeaponPresets.get().getCompatibleWeapons(stats.getId());
        for (String s : list) addTurret(stats, s, items);
	}
	
	private void addTurret(PartStats stats, String preset, NonNullList<ItemStack> items) {
		ItemStack turret = new ItemStack(this);
		if (stats != null) turret.setTag(stats.createFilledPartInstance(preset).writeNBT());
		items.add(turret);
	}
	
	@Override
	public CreativeModeTab getCreativeTab() {
		return ModItems.WEAPON_PARTS;
	}

	public TurretInstance<?> getTurretInstance(ItemStack stack) {
		return (TurretInstance<?>) getPartInstance(stack);
	}

	@Override
	public @NotNull String getObjModelId(@NotNull String preset) {
		PartClientStats<?> pcs = PartAssets.get().get(preset);
		if (pcs == null) return "";
		return pcs.getModelId();
	}

	@Override
	public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
		ObjModelItem.super.initializeClient(consumer);
	}

	@Override
	public ObjEntityModels.@NotNull ModelOverrides getItemModelOverrides(@NotNull String preset) {
		PartClientStats<?> pcs = PartAssets.get().get(preset);
		if (pcs == null) return ObjEntityModels.NO_OVERRIDES;
		return pcs.getItemModelOverrides();
	}
}
