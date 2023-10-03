package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.parts.TurretData;
import com.onewhohears.dscombat.data.parts.TurretData.RotBounds;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.parts.EntityTurret.ShootType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemTurret extends ItemPart {
	
	public final String weaponId;
	public final RotBounds rotBounds;
	public final float health;
	public final String modelId;
	public final EntityDimensions size;
	public final Vec3 offset;
	public final double weaponOffset;
	public final ShootType shootType;
	
	public ItemTurret(float weight, SlotType[] compatibleSlots, String modelId, EntityDimensions size, 
			String weaponId, RotBounds rotBounds, float health, Vec3 offset, double weaponOffset) {
		this(weight, compatibleSlots, modelId, size, weaponId, rotBounds, health, offset, weaponOffset, ShootType.NORMAL);
	}
	
	public ItemTurret(float weight, SlotType[] compatibleSlots, String modelId, EntityDimensions size, 
			String weaponId, RotBounds rotBounds, float health, Vec3 offset, double weaponOffset, ShootType shootType) {
		super(1, weight, compatibleSlots);
		this.weaponId = weaponId;
		this.modelId = modelId;
		this.size = size;
		this.rotBounds = rotBounds;
		this.health = health;
		this.offset = offset;
		this.weaponOffset = weaponOffset;
		this.shootType = shootType;
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
				compatibleSlots, weaponId, rotBounds, true, health, 
				modelId, size, offset, weaponOffset, shootType);
	}
	
	@Override
	public PartData getPartData() {
		return new TurretData(weight, ForgeRegistries.ITEMS.getKey(this), 
				compatibleSlots, weaponId, rotBounds, false, health,
				modelId, size, offset, weaponOffset, shootType);
	}

}
