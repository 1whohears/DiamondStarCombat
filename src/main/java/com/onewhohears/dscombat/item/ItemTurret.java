package com.onewhohears.dscombat.item;

import java.util.List;

import com.onewhohears.dscombat.data.parts.instance.TurretInstance;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilItem;
import com.onewhohears.dscombat.util.UtilMCText;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemTurret extends ItemPart {
	
	private final String defaultWeaponId;
	
	public ItemTurret(int stackSize, String defaultWeaponId) {
		super(stackSize);
		this.defaultWeaponId = defaultWeaponId;
	}
	
	public ItemTurret(int stackSize) {
		this(stackSize, "");
	}
	
	@Override
	public Component getName(ItemStack stack) {
		TurretInstance<?> data = (TurretInstance<?>) UtilParse.parsePartFromItem(stack);
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
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			addTurret(getDefaultWeaponId(), items);
		}
	}
	
	private void addTurret(String preset, NonNullList<ItemStack> items) {
		if (preset.isEmpty()) preset = getDefaultWeaponId();
		ItemStack turret = new ItemStack(this);
		turret.setTag(getDefaultPartStats().createFilledPartInstance(preset).writeNBT());
		items.add(turret);
	}
	
	public String getDefaultWeaponId() {
		if (defaultWeaponId.isEmpty()) {
			ResourceLocation itemid = UtilItem.getItemKey(this);
			List<String> list = WeaponPresets.get().getTurretWeapons(itemid);
			if (list.size() > 0) return list.get(0);
		}
		return defaultWeaponId;
	}

}
