package com.onewhohears.dscombat.item;

import java.util.List;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.parts.TurretData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.util.UtilItem;
import com.onewhohears.dscombat.util.UtilMCText;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ItemTurret extends ItemPart {
	
	public final String weaponId;
	public final String turrentEntityKey;
	public final float health;
	
	public ItemTurret(float weight, SlotType[] compatibleSlots, String turrentEntityKey, String weaponId, float health) {
		super(1, weight, compatibleSlots);
		this.weaponId = weaponId;
		this.turrentEntityKey = turrentEntityKey;
		this.health = health;
	}
	
	@Override
	public Component getName(ItemStack stack) {
		TurretData data = (TurretData) UtilParse.parsePartFromItem(stack);
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
	public PartData getFilledPartData(String param) {
		return new TurretData(weight, UtilItem.getItemKey(this), compatibleSlots, 
				turrentEntityKey, weaponId, getCompatibleWeapons(), true, health);
	}
	
	@Override
	public PartData getPartData() {
		return new TurretData(weight, UtilItem.getItemKey(this), compatibleSlots, 
				turrentEntityKey, weaponId, getCompatibleWeapons(), false, health);
	}
	
	public String[] getCompatibleWeapons() {
		ResourceLocation itemid = UtilItem.getItemKey(this);
		List<String> list = WeaponPresets.get().getTurretWeapons(itemid);
		return list.toArray(new String[list.size()]);
	}

}
