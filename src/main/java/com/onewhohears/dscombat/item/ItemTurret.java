package com.onewhohears.dscombat.item;

import java.util.List;

import com.onewhohears.dscombat.data.parts.instance.TurretInstance;
import com.onewhohears.dscombat.data.parts.stats.PartStats;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.onewholibs.util.UtilMCText;

import com.onewhohears.dscombat.util.UtilPresetParse;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemTurret extends ItemPart {
	
	public ItemTurret(int stackSize) {
		super(stackSize);
	}
	
	@Override
	public Component getName(ItemStack stack) {
		TurretInstance<?> data = (TurretInstance<?>) UtilPresetParse.parsePartFromItem(stack);
		MutableComponent name = ((MutableComponent)super.getName(stack)).append(" ");
		WeaponStats wd = WeaponPresets.get().get(data.getWeaponId());
		if (wd != null) {
			name.append(wd.getDisplayNameComponent()).append(" ")
				.append(UtilMCText.literal(wd.getWeaponTypeCode()));
		}
		else name.append(data.getWeaponId()+"?");
		int ammo = (int)data.getCurrentAmmo();
		int max = (int)data.getMaxAmmo();
		if (max != 0) name.append(" "+ammo+"/"+max);
		return name;	
	}
	
	@Override
	protected void fillItemCategory(PartStats stats, NonNullList<ItemStack> items) {
		List<String> list = WeaponPresets.get().getCompatibleWeapons(stats.getId());
		for (int i = 0; i < list.size(); ++i) addTurret(list.get(i), items);
	}
	
	private void addTurret(String preset, NonNullList<ItemStack> items) {
		ItemStack turret = new ItemStack(this);
		turret.setTag(getDefaultPartStats().createFilledPartInstance(preset).writeNBT());
		items.add(turret);
	}
	
	@Override
	public CreativeModeTab getCreativeTab() {
		return ModItems.WEAPON_PARTS;
	}

}
