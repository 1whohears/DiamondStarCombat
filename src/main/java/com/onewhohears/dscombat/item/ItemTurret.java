package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.parts.TurretData;
import com.onewhohears.dscombat.data.parts.TurretData.RotBounds;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemTurret extends ItemPart {
	
	public final String weaponId;
	public final String turrentEntityKey;
	public final RotBounds rotBounds;
	public final float health;
	
	public ItemTurret(float weight, SlotType[] compatibleSlots, String turrentEntityKey, String weaponId, RotBounds rotBounds, float health) {
		super(1, weight, compatibleSlots);
		this.weaponId = weaponId;
		this.turrentEntityKey = turrentEntityKey;
		this.rotBounds = rotBounds;
		this.health = health;
	}
	
	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		MutableComponent name = ((MutableComponent)super.getName(stack)).append(" ");
		WeaponData wd = WeaponPresets.get().getPreset(weaponId);
		if (wd != null) {
			name.append(wd.getDisplayNameComponent()).append(" ")
				.append(Component.literal(wd.getWeaponTypeCode()));
		}
		else name.append(weaponId+"?");
		int ammo = tag.getInt("ammo");
		int max = tag.getInt("max");
		if (max != 0) name.append(" "+ammo+"/"+max);
		return name;	
	}
	
	@Override
	public PartData getFilledPartData(String param) {
		return new TurretData(weight, ForgeRegistries.ITEMS.getKey(this), 
				compatibleSlots, turrentEntityKey, weaponId, rotBounds, true, health);
	}
	
	@Override
	public PartData getPartData() {
		return new TurretData(weight, ForgeRegistries.ITEMS.getKey(this), 
				compatibleSlots, turrentEntityKey, weaponId, rotBounds, false, health);
	}

}
