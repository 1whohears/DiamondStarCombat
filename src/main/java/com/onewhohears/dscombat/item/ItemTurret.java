package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.parts.TurretData;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.util.UtilItem;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
		WeaponData wd = WeaponPresets.get().getPreset(weaponId);
		if (wd != null) {
			name.append(wd.getDisplayNameComponent()).append(" ")
				.append(Component.literal(wd.getWeaponTypeCode()));
		}
		else name.append(weaponId+"?");
		int ammo = (int)data.getCurrentAmmo();
		int max = (int)data.getMaxAmmo();
		if (max != 0) name.append(" "+ammo+"/"+max);
		return name;	
	}
	
	@Override
	public PartData getFilledPartData(String param) {
		return new TurretData(weight, UtilItem.getItemKey(this), 
				compatibleSlots, turrentEntityKey, weaponId, true, health);
	}
	
	@Override
	public PartData getPartData() {
		return new TurretData(weight, UtilItem.getItemKey(this), 
				compatibleSlots, turrentEntityKey, weaponId, false, health);
	}

}
